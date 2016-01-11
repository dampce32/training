// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.classInit = function() {
	  var $this = $(this);
	  var id = $(this).attr('id');
	  
	  var height = $(document.body).height();
	  
	  var queryContent = $('#queryContent',$this);
	  var searchBtn = $('#searchBtn',$this);
	  
	  var viewList = $('#viewList',$this);
	  var selectRow = null;
	  var selectIndex = null;
	  
	  var pager = $('#pager',$this);
	  var pageNumber = 1;
	  var pageSize = 10;
	  
	  var editDialog = $('#editDialog',$this);
	  var editForm = $('#editForm',$this);
	  
	  var printDialog = $('#printDialog',$this);
	  
	  $('#schoolSearch',queryContent).combobox({
		  width:120,
		  data:TRN.getSelfSchoolList(),
		  valueField:'schoolId',
		  textField:'schoolName'
	  });

	  $('#statusSearch',queryContent).combobox('select',4);
	  
	  var getBar = function(left,right){
		  var bar = '<div style="width:80px;height:12px;background:#fff;border:1px solid #ccc;text-align:left;float:left;">' +
			'<div style="width:' + left*100/right + '%;height:100%;background:blue"></div>'+
			'</div>';
		  var number = '<span style="width:50px;margin-left:5px;text-align:left;">'+
			'<span style="color: red;">'+left+'</span>/'+right+
			'</span>';
		  return bar+number;
	  };
	  
	//加载列表
	  $(viewList).datagrid({
		singleSelect:true,
		method:"POST",
		nowrap:true,
		striped: true,
		collapsible:true,
		rownumbers:true,
		fit:true,
		toolbar:[	
					{text:'添加',iconCls:'icon-add',handler:function(){onAdd();}},
					{text:'修改',iconCls:'icon-edit',handler:function(){onUpdate();}},
					{text:'删除',iconCls:'icon-remove',handler:function(){onDelete();}},'-',
					{text:'打印',iconCls:'icon-print',handler:function(){onPrint();}},
					{text:'控制面板',iconCls:'icon-view',handler:function(){controlPanel();}},'-',
					{text:'课程表',iconCls:'icon-scan',handler:function(){onClassTable();}},
					{text:'课程总表（周）',iconCls:'icon-page',handler:function(){weekTable();}}
//					{text:'课程总表（月）',handler:function(){monthTable();}}
				],
		columns:[[
		    {field:'classId',title:'班号',width:50,align:"center"},
			{field:'className',title:'班级名称',width:170,align:"center"},
			{field:'startDate',title:'开课时间',width:100,align:"center"},
			{field:'timeRule',title:'上课规律',width:100,align:"center"},
			{field:'schoolName',title:'校区',width:100,align:"center"},
			{field:'stuCount',title:'招生比例',width:150,align:"center",
				formatter: function(value,row,index){
					return getBar(row.stuCount,row.planCount);
				}},
			{field:'courseProgress',title:'课程进度',width:150,align:"center",
				formatter: function(value,row,index){
					return getBar(row.courseProgress,row.lessons);
				}},
			{field:'arrangeLessons',title:'排课进度',width:150,align:"center",
				formatter: function(value,row,index){
					return getBar(row.arrangeLessons,row.lessons);
				}}
		]],
		onClickRow:function(rowIndex, rowData){
			selectRow = rowData;
			selectIndex = rowIndex;
		},
		onDblClickRow:function(rowIndex,rowData){
			onUpdate();
		},
		onLoadSuccess:function(){
			selectRow = null;
	 		selectIndex = null;
			pageNumber = 1;
		}
	  });
	//分页条
	$(pager).pagination({   
	    onSelectPage: function(page, rows){
			pageNumber = page;
			pageSize = rows;
			search();
	    }
	});
	//编辑框
	$(editDialog).dialog({  
	    title: '编辑班级',  
	    width:440,
	    height:height-10,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    toolbar:[{
			text:'保存',
			iconCls:'icon-save',
			handler:function(){
				onSave();
			}
		},'-',{
			id:'add'+id,
			text:'新增',
			iconCls:'icon-add',
			handler:function(){
				onAdd();
			}
		},'-',{
			text:'退出',
			iconCls:'icon-cancel',
			handler:function(){
				$(editDialog).dialog('close');
			}
		}]
	});    
	var initCombobox = function(){
		$('#courseType',editForm).combobox({
			  width:250,
			  data:TRN.getCourseTypeList(),
			  valueField:'courseTypeId',
			  textField:'courseTypeName',
			  onSelect:function(record){
				  $('#course',editForm).combobox({
					  disabled:false,
					  data:TRN.getIsTypeCourseList(record.courseTypeId)
				  });
			  }
		  });
		$('#course',editForm).combobox({
			  width:250,
			  disabled:true,
			  valueField:'courseId',
			  textField:'courseName'
		  });
		$('#teacher',editForm).combobox({
			  width:250,
			  data:TRN.getIsTeacherEmployeeList(),
			  valueField:'userId',
			  textField:'userName'
		  });
		$('#school',editForm).combobox({
			  width:250,
			  data:TRN.getSelfSchoolList(),
			  valueField:'schoolId',
			  textField:'schoolName',
			  onSelect:function(record){
				  $('#classroom',editForm).combobox({
					  disabled:false,
					  data:TRN.getInSchoolClassroomList(record.schoolId)
				  });
			  }
		  });
		$('#classroom',editForm).combobox({
			  width:250,
			  disabled:true,
			  valueField:'classroomId',
			  textField:'classroomName'
		  });
		$('#creater',editForm).combobox({
			  width:250,
			  data:TRN.getEmployeeList(),
			  valueField:'userId',
			  textField:'userName'
		  });
	};
	//添加
	var onAdd = function(){
		$(editForm).form('clear');
		initCombobox();
		$('#classType',editDialog).combobox('setValue',0);
		$('#classType',editDialog).combobox('disable');
		$(editDialog).dialog('open');
	};
	//保存前的赋值操作
	var setValue = function(){
		var className = $.trim($('#className',editForm).val());
		if(''==className){
			$.messager.alert('提示','请填写班级名称','warning');
			return false;
		}
		var courseId = $('#course',editForm).combobox('getValue');
		if(''==courseId){
			$.messager.alert('提示','请选择课程','warning');
			return false;
		}
		var classType = $('#classType',editForm).combobox('getValue');
		if(''==classType){
			$.messager.alert('提示','请选择授课方式','warning');
			return false;
		}
		var teacherId = $('#teacher',editForm).combobox('getValue');
		if(''==teacherId){
			$.messager.alert('提示','请选择主讲老师','warning');
			return false;
		}
		var startDate = $('#startDate',editForm).val();
		if(''==startDate){
			$.messager.alert('提示','请选择开课时间','warning');
			return false;
		}
		var school = $('#school',editForm).combobox('getValue');
		if(''==school){
			$.messager.alert('提示','请选择校区','warning');
			return false;
		}
		var planCount = $('#planCount',editForm).val();
		if(''==planCount){
			$.messager.alert('提示','请填写计划招生数','warning');
			return false;
		}
		var classroomId = $('#classroom',editForm).combobox('getValue');
		if(''==classroomId){
			$.messager.alert('提示','请选择教室','warning');
			return false;
		}
		var lessons = $('#lessons',editForm).val();
		if(''==lessons){
			$.messager.alert('提示','请填写总课时','warning');
			return false;
		}
		var lessonMinute = $('#lessonMinute',editForm).val();
		if(''==lessonMinute){
			$.messager.alert('提示','请填写每课时的时长','warning');
			return false;
		}
		var createrId = $('#creater',editForm).combobox('getValue');
		if(''==createrId){
			$.messager.alert('提示','请选择建立者','warning');
			return false;
		}
		return true;
	};
	//保存
	var onSave = function(){
		$(editForm).form('submit',{
			url: 'academic/saveClass.do',
			onSubmit: function(){
				return setValue();
			},
			success: function(data){
				var result = eval('('+data+')');
				if(result.isSuccess){
					var fn = function(){
						var classId = $.trim($('#classId',editForm).val());
						if(classId==''){//新增
							var  data = result.data;
							$('#classId',editForm).val(data.classId);
						}
						TRN.ClassList = null;
					};
					$.messager.alert('提示','保存成功','info',fn);
				}else{
					$.messager.alert('提示',result.message,"error");
				}
			}
		});
	};
	//修改
	var onUpdate = function(){
		if(selectRow==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		}
		$(editForm).form('clear');
		initCombobox();
		$('#courseType',editForm).combobox('select',selectRow.courseTypeId);
		$('#courseType',editForm).combobox('setValue',selectRow.courseTypeId);
		$('#course',editForm).combobox('setValue',selectRow.courseId);
		$('#classType',editForm).combobox('setValue',selectRow.classType);
		$('#teacher',editForm).combobox('setValue',selectRow.teacherId);
		$('#school',editForm).combobox('select',selectRow.schoolId);
		$('#school',editForm).combobox('setValue',selectRow.schoolId);
		$('#classroom',editForm).combobox('setValue',selectRow.classroomId);
		$('#creater',editForm).combobox('setValue',selectRow.createrId);
		$(editForm).form('load',selectRow);
		$(editDialog).dialog('open');
	 };
	//删除
	var onDelete = function(){
		if(selectRow==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		}
		$.messager.confirm('确认', '确定删除该记录吗?', function(r){
			if(r){
				var url = "academic/deleteClass.do";
				var content = {classId:selectRow.classId};
				$.post(url,content,
					function(result){
						if(result.isSuccess){
							search(true);
							TRN.ClassList = null;
						}else{
							$.messager.alert('提示',result.message,"error");
						}
				},'json');
			}
		});
	};
	//查询
	var search = function(flag){
		var className = $('#classNameSearch',queryContent).val();
		var schoolId = $('#schoolSearch',queryContent).combobox('getValue');
		var status = $('#statusSearch',queryContent).combobox('getValue');
		var content = {className:className,'school.schoolId':schoolId,status:status,
				page:pageNumber,rows:pageSize};
		//取得列表信息
		var url = 'academic/queryClass.do';
		var result = syncCallService(url,content);
		if(result.isSuccess){
			var  data = result.data;
			var datagridData = eval("("+data.datagridData+")");
			$(viewList).datagrid('loadData',datagridData);
			//需要重新重新分页信息
			if(flag){
				getTotal(content);
			}
		}else{
			$.messager.alert('提示',result.message,'error');
		}
	};
	//统计总数
	var getTotal = function(content){
		var url = "academic/getTotalCountClass.do";
		asyncCallService(url,content,
		function(result){
			if(result.isSuccess){
				var data = result.data;
				$(pager).pagination({  
					pageNumber:1,
					total:data.total
				});
			}else{
				$.messager.alert('提示',result.message,"error");
			}
		});
	};
	//查询
	$(searchBtn).click(function(){
		search(true);
	});
	
	$(printDialog).dialog({
		title: '编辑班级',  
	    width:400,
	    height:200,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    toolbar:[{
			text:'确定',
			iconCls:'icon-ok',
			handler:function(){
				var printParam = $('#printSearch',printDialog).combobox('getValue');
				print(printParam);
			}
		},'-',{
			text:'退出',
			iconCls:'icon-cancel',
			handler:function(){
				$(printDialog).dialog('close');
			}
		}]
	});
	
	var onPrint = function(){
		if(selectRow==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		}
		$('#printSearch',printDialog).combobox('setValue','');
		$(printDialog).dialog('open');
	};
	
	var print = function(printParam){
		if(printParam==''){
			$.messager.alert("提示","请选择打印类型","warning");
			return;
		}
		if(printParam==0){
			window.open("printReport.jsp?report=classStudentInfo&data=ReportServlet?classId="+selectRow.classId);
		}
	};
	
	//=====================================排课=======================================
	var classTableDialog = $('#classTableDialog',$this);
	
	$(classTableDialog).dialog({  
	    title: '排课上课',  
	    width:1000,
	    height:height-10,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    toolbar:[{
			text:'退出',
			iconCls:'icon-cancel',
			handler:function(){
				$(classTableDialog).dialog('close');
			}
		}]
	}); 

	//打开课程表
	var onClassTable = function(){
		if(selectRow==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		}
		$('#degreeList',classTableDialog).datagrid('loadData',{ total: 0, rows: [] });
    	$(classTableDialog).dialog({
    		href:'academic/degreeLessonPart.do',
            extractor:function (d) {
                if (!d) {
                    return d;
                }
                if (window['CSIT']) {
                    var id = CSIT.genId();
                    return d.replace(/\$\{id\}/,id);
                }
                return d;
            },
            onLoad:function (panel) {
                (window['CSIT'] && CSIT.initContent && CSIT.initContent(this));
                $('#classTableForm',classTableDialog).form('clear');
        		$('#classTableForm',classTableDialog).form('load',selectRow);
                $('#sRschoolId',classTableDialog).val(selectRow.schoolId);
               	$('#sRclassId',classTableDialog).val(selectRow.classId);
               	$('#sRteacherId',classTableDialog).val(selectRow.teacherId);
               	$('#sRclassroomId',classTableDialog).val(selectRow.classroomId);
               	$('#sRlessons',classTableDialog).val(selectRow.lessons);
               	$('#sRarrangeLessons',classTableDialog).val(selectRow.arrangeLessons);
               	$('#sRlessonMinute',classTableDialog).val(selectRow.lessonMinute);
            }
    	});
		$(classTableDialog).dialog('open');
	};
	
	//================================课程总表（周）===================================
	var weekTableDialog = $('#weekTableDialog',$this);
	
	var weekTableList = $('#weekTableList',weekTableDialog);
	
	//星期，0表示本周，负数表示以前的，正数表示以后的
	var toWeekOfToday = 0;
	//分组参数，0 - 按教室分组，1 - 按老师分组
	var queryParam = 0;
	
	$('#schoolSearch',weekTableDialog).combobox({
		  width:150,
		  data:TRN.getSelfSchoolList(),
		  valueField:'schoolId',
		  textField:'schoolName'
	  });
	
	//窗口
	$(weekTableDialog).dialog({
		title: '课程总表（周）',  
	    width:1000,
	    height:500,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    toolbar:[{
			text:'退出',
			iconCls:'icon-cancel',
			handler:function(){
				$(weekTableDialog).dialog('close');
			}
		}]
	});
	
	
	var getTitle = function(index,toWeekOfToday){
		var week = ['日','一','二','三','四','五','六'];
		var today = new Date();
		var todayWeek = getTodayWeek(today);
		var toNumber = index-todayWeek+toWeekOfToday*7;
		toNumber = parseInt(""+toNumber);
		var date = addDays(today,toNumber);
		var nowDate = addDays(new Date(),0);
		if(date==nowDate){
			return '<span style="color:blue">周'+week[index]+'（'+date+'）</span>';
		}
		return '周'+week[index]+'（'+date+'）';
	};
	
	//列表
	$(weekTableList).datagrid({
		singleSelect:true,
		method:"POST",
		nowrap:true,
		striped: true,
		collapsible:true,
		fit:true,
		toolbar:[	
					{text:'本周',iconCls:'icon-down',
						handler:function(){
							weekTableSearch(0);
						}
					},'-',
					{text:'上周',iconCls:'icon-left',
						handler:function(){
							toWeekOfToday--;
							weekTableSearch(toWeekOfToday);
						}
					},
					{text:'下周',iconCls:'icon-right',
						handler:function(){
							toWeekOfToday++;
							weekTableSearch(toWeekOfToday);
						}
					}
				],
		columns:[[
		    {field:'classroomName',title:'教室',width:100,align:"center"},
		    {field:'Sunday',title:getTitle(0,toWeekOfToday),width:150,align:"center"},
		    {field:'Monday',title:getTitle(1,toWeekOfToday),width:150,align:"center"},
		    {field:'Tuesday',title:getTitle(2,toWeekOfToday),width:150,align:"center"},
		    {field:'Wednesday',title:getTitle(3,toWeekOfToday),width:150,align:"center"},
		    {field:'Thursday',title:getTitle(4,toWeekOfToday),width:150,align:"center"},
		    {field:'Friday',title:getTitle(5,toWeekOfToday),width:150,align:"center"},
		    {field:'Saturday',title:getTitle(6,toWeekOfToday),width:150,align:"center"}
		]],
		onClickRow:function(rowIndex, rowData){
			selectParamIndex = rowIndex;
		}
	});
	
	//查询
	var weekTableSearch = function(toWeekOfToday){
		var dateSearch = $('#dateSearch',weekTableDialog).val();
		if(dateSearch!=''){
			toWeekOfToday = getWeekBetweenDate(stringToDate(dateSearch),new Date());
		}
		var todayWeek = getTodayWeek(new Date());
		var toNumber = 0-todayWeek+toWeekOfToday*7;
		toNumber = parseInt(""+toNumber);
		var schoolId = $('#schoolSearch',weekTableDialog).combobox('getValue');
		var content = {schoolId:schoolId,toSunday:toNumber,toWeekOfToday:toWeekOfToday,queryParam:queryParam};
		//取得列表信息
		var url = "academic/weekTableQueryLessonDegree.do";
		var result = syncCallService(url,content);
		if(result.isSuccess){
			var  data = result.data;
			
			if(queryParam==0){
				$(weekTableList).datagrid({
					columns:[[
					    {field:'classroomName',title:'教室',width:100,align:"center"},
					    {field:'Sunday',title:getTitle(0,toWeekOfToday),width:150,align:"center"},
					    {field:'Monday',title:getTitle(1,toWeekOfToday),width:150,align:"center"},
					    {field:'Tuesday',title:getTitle(2,toWeekOfToday),width:150,align:"center"},
					    {field:'Wednesday',title:getTitle(3,toWeekOfToday),width:150,align:"center"},
					    {field:'Thursday',title:getTitle(4,toWeekOfToday),width:150,align:"center"},
					    {field:'Friday',title:getTitle(5,toWeekOfToday),width:150,align:"center"},
					    {field:'Saturday',title:getTitle(6,toWeekOfToday),width:150,align:"center"}
					]]
				});
			}else if(queryParam==1){
				$(weekTableList).datagrid({
					columns:[[
					    {field:'teacherName',title:'老师',width:100,align:"center"},
					    {field:'Sunday',title:getTitle(0,toWeekOfToday),width:150,align:"center"},
					    {field:'Monday',title:getTitle(1,toWeekOfToday),width:150,align:"center"},
					    {field:'Tuesday',title:getTitle(2,toWeekOfToday),width:150,align:"center"},
					    {field:'Wednesday',title:getTitle(3,toWeekOfToday),width:150,align:"center"},
					    {field:'Thursday',title:getTitle(4,toWeekOfToday),width:150,align:"center"},
					    {field:'Friday',title:getTitle(5,toWeekOfToday),width:150,align:"center"},
					    {field:'Saturday',title:getTitle(6,toWeekOfToday),width:150,align:"center"}
					]]
				});
			}
			
			var datagridData = eval("("+data.datagridData+")");
			$(weekTableList).datagrid('loadData',datagridData);
		}else{
			$.messager.alert('提示',result.message,'error');
		}
	};
	
	var weekTable = function(){
		//初始化
		$('#dateSearch',weekTableDialog).val('');
		toWeekOfToday = 0;
		queryParam = 0;
		$(groupSearchBtn).linkbutton({text:'按老师分组'});
		
		$(weekTableDialog).dialog('open');
	};
	
	//查询按钮
	$('#searchBtn',weekTableDialog).click(function(){
		weekTableSearch(toWeekOfToday);
	});
	
	var groupSearchBtn = $('#groupSearchBtn',weekTableDialog);
	$(groupSearchBtn).click(function(){
		if($(groupSearchBtn).text()=='按老师分组'){
			$(groupSearchBtn).linkbutton({text:'按教室分组'});
			queryParam = 1;
			weekTableSearch(toWeekOfToday);
		}else if($(groupSearchBtn).text()=='按教室分组'){
			$(groupSearchBtn).linkbutton({text:'按老师分组'});
			queryParam = 0;
			weekTableSearch(toWeekOfToday);
		}
	});
	//================================课程总表（月）===================================
	var monthTableDialog = $('#monthTableDialog',$this);
	
	var monthTableList = $('#monthTableList',monthTableDialog);
	
	//窗口
	$(monthTableDialog).dialog({
		title: '课程总表（月）',  
	    width:1000,
	    height:500,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    toolbar:[{
			text:'退出',
			iconCls:'icon-cancel',
			handler:function(){
				$(monthTableDialog).dialog('close');
			}
		}]
	});
	//列表
	$(monthTableList).datagrid({
	});
	
	var monthTable = function(){
		$(monthTableDialog).dialog('open');
	};
	
	//=================================控制面板=======================================
	var controlPanelDialog = $('#controlPanelDialog',$this);
	
	var controlPanelTabs = $('#controlPanelTabs',controlPanelDialog);
	
	//控制面板窗口
	$(controlPanelDialog).dialog({
	    width:1000,
	    height:height-10,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    toolbar:[{
			text:'退出',
			iconCls:'icon-cancel',
			handler:function(){
				$(controlPanelDialog).dialog('close');
			}
		}]
	});
	
	var controlPanel = function(){
		if(selectRow==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		}
		$(controlPanelTabs).tabs('select',0);
		$(controlPanelDialog).dialog({title:'班级控制面板（'+selectRow.className+'）'});
		$(controlPanelDialog).dialog('open');
	};
	//-------------------------------控制面板切换-----------------------------------
	$(controlPanelTabs).tabs({
	    onSelect:function(title){
	    	var tab = $(controlPanelTabs).tabs('getSelected');
	    	
	    	var classInfo = null;
			var url = 'academic/queryOneClass.do';
		    var content = {studentId:'',classId:selectRow.classId};
		    var result = syncCallService(url,content);
		    if(result.isSuccess){
		    	var data = result.data.classInfo;
		    	classInfo = eval('('+data+')');
		    }else{
			    $.messager.alert('提示',result.message,"error");
		    }
	    	if(title=='基本信息'){
	    		$('#classInfoForm',tab).form('clear');
	    		var panelInfo = {
	                    href:'academic/classBaseInfo.do', border:false, plain:true,
	                    extractor:function (d) {
	                        if (!d) {
	                            return d;
	                        }
	                        if (window['CSIT']) {
	                            var id = CSIT.genId();
	                            return d.replace(/\$\{id\}/, id);
	                        }
	                        return d;
	                    },
	                    onLoad:function (panel) {
	                       (window['CSIT'] && CSIT.initContent && CSIT.initContent(this));
	                       $('#classId',tab).val(selectRow.classId);
	                       $('#className',tab).val(classInfo.className);
	                       $('#courseName',tab).val(classInfo.courseName);
	                       $('#teacherName',tab).val(classInfo.userName);
	                       $('#lessons',tab).val(classInfo.lessons);
	                       $('#startDate',tab).val(classInfo.startDate);
	                       $('#endDate',tab).val(classInfo.endDate);
	                       $('#timeRule',tab).val(classInfo.timeRule);
	                       $('#classroomName',tab).val(classInfo.classroomName);
	                       $('#showCourseProgress',tab).html(getBar(classInfo.courseProgress,classInfo.lessons));
	                       $('#showStuCount',tab).html(getBar(classInfo.stuCount,classInfo.planCount));
	                       $('#showAttendance',tab).html();
	                       $('#note',tab).val(classInfo.note);
	                       $('#schoolName',tab).val(classInfo.schoolName);
	                    }
	            };
	        	$(controlPanelTabs).tabs('update', {
	        		tab: tab,
	        		options: panelInfo
	        	});
	        	 $('#classId',tab).val(selectRow.classId);
                 $('#className',tab).val(classInfo.className);
                 $('#courseName',tab).val(classInfo.courseName);
                 $('#teacherName',tab).val(classInfo.userName);
                 $('#lessons',tab).val(classInfo.lessons);
                 $('#startDate',tab).val(classInfo.startDate);
                 $('#endDate',tab).val(classInfo.endDate);
                 $('#timeRule',tab).val(classInfo.timeRule);
                 $('#classroomName',tab).val(classInfo.classroomName);
                 $('#showCourseProgress',tab).html(getBar(classInfo.courseProgress,classInfo.lessons));
                 $('#showStuCount',tab).html(getBar(classInfo.stuCount,classInfo.planCount));
                 $('#showAttendance',tab).html();
                 $('#note',tab).val(classInfo.note);
                 $('#schoolName',tab).val(classInfo.schoolName);
	    	}
	    	if(title=='班级学员'){
	    		$('#viewList',tab).datagrid('loadData',{ total: 0, rows: [] });
	    		var panelInfo = {
	                    href:'academic/classStudentInfo.do', border:false, plain:true,
	                    extractor:function (d) {
	                        if (!d) {
	                            return d;
	                        }
	                        if (window['CSIT']) {
	                            var id = CSIT.genId();
	                            return d.replace(/\$\{id\}/, id);
	                        }
	                        return d;
	                    },
	                    onLoad:function (panel) {
	                       (window['CSIT'] && CSIT.initContent && CSIT.initContent(this));
	                       $('#classSearch',tab).val(selectRow.classId);
	                    }
	            };
	        	$(controlPanelTabs).tabs('update', {
	        		tab: tab,
	        		options: panelInfo
	        	});
	        	$('#classSearch',tab).val(selectRow.classId);
	    	}
	    	if(title=='排课上课'){
	        	$('#degreeList',tab).datagrid('loadData',{ total: 0, rows: [] });
	        	var panelInfo = {
	                    href:'academic/degreeLessonPart.do', border:false, plain:true,
	                    extractor:function (d) {
	                        if (!d) {
	                            return d;
	                        }
	                        if (window['CSIT']) {
	                            var id = CSIT.genId();
	                            return d.replace(/\$\{id\}/, id);
	                        }
	                        return d;
	                    },
	                    onLoad:function (panel) {
	                        (window['CSIT'] && CSIT.initContent && CSIT.initContent(this));
	                        $('#classTableForm',tab).form('load',selectRow);
		                    $('#sRschoolId',tab).val(selectRow.schoolId);
		                   	$('#sRclassId',tab).val(selectRow.classId);
		                   	$('#sRteacherId',tab).val(selectRow.teacherId);
		                   	$('#sRclassroomId',tab).val(selectRow.classroomId);
		                   	$('#sRlessons',tab).val(selectRow.lessons);
		                   	$('#sRarrangeLessons',tab).val(selectRow.arrangeLessons);
		                   	$('#sRlessonMinute',tab).val(selectRow.lessonMinute);
	                    }
	            };
	        	$(controlPanelTabs).tabs('update', {
	        		tab: tab,
	        		options: panelInfo
	        	});
	        	$('#classTableForm',tab).form('clear');
                $('#classTableForm',tab).form('load',selectRow);
	        	$('#sRschoolId',tab).val(selectRow.schoolId);
               	$('#sRclassId',tab).val(selectRow.classId);
               	$('#sRteacherId',tab).val(selectRow.teacherId);
               	$('#sRclassroomId',tab).val(selectRow.classroomId);
               	$('#sRlessons',tab).val(selectRow.lessons);
               	$('#sRarrangeLessons',tab).val(selectRow.arrangeLessons);
               	$('#sRlessonMinute',tab).val(selectRow.lessonMinute);
	        }
	    } 
	});
  };
})(jQuery);   