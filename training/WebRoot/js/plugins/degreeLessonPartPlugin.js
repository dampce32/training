// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.degreeLessonPartInit = function() {
	var $this = $(this);
	var id = $(this).attr('id');
	
	var height = $(document.body).height();
	
	var classTableForm = $('#classTableForm',$this);
	
	var singleDegreeDialog = $('#singleDegreeDialog',$this);
	var singleDegreeForm = $('#singleDegreeForm',$this);
	
	var degreeList = $('#degreeList',$this);
	var selectDegreeRow = null;
	var selectDegreeIndex = null;
	
	var degreePager = $('#degreePager',$this);
	var degreePagerNumber = 1;
	var degreePagerSize = 10;
	
	var getBar = function(left,right){
	    var bar = '<div style="width:80px;height:12px;background:#fff;border:1px solid #ccc;text-align:left;float:left;">' +
		  '<div style="width:' + left*100/right + '%;height:100%;background:blue"></div>'+
		  '</div>';
	    var number = '<span style="width:50px;margin-left:5px;text-align:left;">'+
		  '<span style="color: red;">'+left+'</span>/'+right+
	  	  '</span>';
	    return bar+number;
    };
	
	$(singleDegreeDialog).dialog({  
	    title: '编辑上课记录',  
	    width:400,
	    height:470,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    toolbar:[{
			text:'保存',
			iconCls:'icon-save',
			handler:function(){
				saveSingleDegree();
			}
		},'-',{
			id:'add'+id,
			text:'新增',
			iconCls:'icon-add',
			handler:function(){
				onDegreeAdd();
			}
		},'-',{
			text:'退出',
			iconCls:'icon-cancel',
			handler:function(){
				$(singleDegreeDialog).dialog('close');
			}
		}]
	});  
	
	var singleDegreeCombobox = function(){
		$('#doTime',singleDegreeForm).combobox({
			  width:250,
			  data:TRN.getPeriodTimeList(),
			  valueField:'periodTimeId',
			  textField:'time',
			  onSelect:function(record){
				  var timeStrs=record.time.split("-");
				  $('#startTime',singleDegreeForm).val(timeStrs[0]);
				  $('#endTime',singleDegreeForm).val(timeStrs[1]);
			  }
		  });
		$('#teacher',singleDegreeForm).combobox({
			  width:250,
			  data:TRN.getIsTeacherEmployeeList(),
			  valueField:'userId',
			  textField:'userName'
		  });
		var sRschoolId = $('#sRschoolId',$this).val();
		$('#classroom',singleDegreeForm).combobox({
			  width:250,
			  data:TRN.getInSchoolClassroomList(sRschoolId),
			  valueField:'classroomId',
			  textField:'classroomName'
		  });
		$('#user',singleDegreeForm).combobox({
			  width:250,
			  data:TRN.getEmployeeList(),
			  valueField:'userId',
			  textField:'userName'
		  });
	};
	//手动排课
	var onDegreeAdd = function(){
		$(singleDegreeForm).form('clear');
		singleDegreeCombobox();
		$('input[name="lessonType"]:eq(0)').attr('checked','checked');
		var sRclassId = $('#sRclassId',$this).val();
		$('#classId',singleDegreeForm).val(sRclassId);
		var sRteacherId = $('#sRteacherId',$this).val();
		$('#teacher',singleDegreeForm).combobox('setValue',sRteacherId);
		var sRclassroomId = $('#sRclassroomId',$this).val();
		$('#classroom',singleDegreeForm).combobox('setValue',sRclassroomId);
		$(singleDegreeDialog).dialog('open');
	};
	
	var validateSingleDegree = function(){
		var lessonDegreeDate = $('#lessonDegreeDate',singleDegreeForm).val();
		if(''==lessonDegreeDate){
			$.messager.alert('提示','请选择上课日期','warning');
			return false;
		}
		var startTime = $('#startTime',singleDegreeForm).val();
		if(''==startTime){
			$.messager.alert('提示','请选择上课时间','warning');
			return false;
		}
		var endTime = $('#endTime',singleDegreeForm).val();
		if(''==endTime){
			$.messager.alert('提示','请选择下课时间','warning');
			return false;
		}
		var lessons = $('#lessons',singleDegreeForm).val();
		if(''==lessons){
			$.messager.alert('提示','请填写本次课时','warning');
			return false;
		}
		var teacherId = $('#teacher',singleDegreeForm).combobox('getValue');
		if(''==teacherId){
			$.messager.alert('提示','请选择讲师','warning');
			return false;
		}
		var classroomId = $('#classroom',singleDegreeForm).combobox('getValue');
		if(''==classroomId){
			$.messager.alert('提示','请选择教室','warning');
			return false;
		}
		var userId = $('#user',singleDegreeForm).combobox('getValue');
		if(''==userId){
			$.messager.alert('提示','请选择经办人','warning');
			return false;
		}
	};
	//添加排课
	var saveSingleDegree = function(){
		$(singleDegreeForm).form('submit',{
			url: 'academic/saveLessonDegree.do',
			onSubmit: function(){
				return validateSingleDegree();
			},
			success: function(data){
				var result = eval('('+data+')');
				if(result.isSuccess){
					var fn = function(){
						var lessonDegreeId = $.trim($('#lessonDegreeId',singleDegreeForm).val());
						if(lessonDegreeId==''){//新增
							var  data = result.data;
							$('#lessonDegreeId',singleDegreeForm).val(data.lessonDegreeId);
						}
						degreeSearch(true);
					};
					var arrangeLessons = result.data.arrangeLessons;
					var sRlessons = $('#sRlessons',$this).val();
					$('#arrangeProgress',autoDegreeDialog).html('<span style="color:red">'
							+arrangeLessons+'</span>/'+sRlessons);
					$('#sRarrangeLessons',$this).val(arrangeLessons);
					$.messager.alert('提示','保存成功','info',fn);
					$(singleDegreeDialog).dialog('close');
				}else{
					$.messager.alert('提示',result.message,"error");
				}
			}
		});
	};
	//排课列表
	$(degreeList).datagrid({
		singleSelect:true,
		method:"POST",
		nowrap:true,
		striped: true,
		collapsible:true,
		rownumbers:true,
		fit:true,
		toolbar:[	
		         	{text:'查询',iconCls:'icon-search',handler:function(){degreeSearch(true);}},'-',
		         	{text:'自动排课',iconCls:'icon-add',handler:function(){autoDegree();}},
		         	{text:'手动排课',iconCls:'icon-add',handler:function(){onDegreeAdd();}},
		         	{text:'清除排课',iconCls:'icon-remove',handler:function(){clearParam();}},'-',
					{text:'编辑',iconCls:'icon-edit',handler:function(){onDegreeUpdate();}},
					{text:'删除',iconCls:'icon-remove',handler:function(){onDegreeDelete();}},'-',
					{text:'点名',iconCls:'icon-hided',handler:function(){pointName();}}
				],
		columns:[[
		    {field:'lessonStatus',title:'状态',width:50,align:"center",
				formatter: function(value,row,index){
					if (value==0){
						return '<span style="color:red">未上课</span>';
					} else if (value==1){
						return '<span style="color:green">已上课</span>';
					}
			  }},
			{field:'subject',title:'课题',width:120,align:"center"},
			{field:'lessonDegreeDateAndWeek',title:'上课日期',width:125,align:"center",
				formatter:function(value,row,index){
					var date = row.lessonDegreeDate;
					var week = ['日','一','二','三','四','五','六'];
					return date+' <span style="color: red;">['+week[getWeekOfDate(date)]+']</span>';
			  }},
			{field:'time',title:'上课时段',width:100,align:"center"},
			{field:'lessonType',title:'类型',width:50,align:"center",
				formatter: function(value,row,index){
					if (value==0){
						return '补课';
					} else if (value==1){
						return '正常';
					}
			  }},
			{field:'lessons',title:'课时',width:50,align:"center"},
			{field:'factCount',title:'出勤',width:50,align:"center"},
			{field:'truancyCount',title:'旷课',width:50,align:"center"},
			{field:'lateCount',title:'迟到',width:50,align:"center"},
			{field:'advanceCount',title:'早退',width:50,align:"center"},
			{field:'leaveCount',title:'请假',width:50,align:"center"},
			{field:'teacherName',title:'讲师',width:100,align:"center"},
			{field:'classroomName',title:'教室',width:100,align:"center"}
		]],
		onClickRow:function(rowIndex, rowData){
			selectDegreeRow = rowData;
			selectDegreeIndex = rowIndex;
		},
		onDblClickRow:function(rowIndex,rowData){
			onDegreeUpdate();
		},
		onLoadSuccess:function(){
			selectDegreeRow = null;
	 		selectDegreeIndex = null;
	 		degreePageNumber = 1;
		}
	});
	//分页条
	$(degreePager).pagination({   
	    onSelectPage: function(page, rows){
	    	degreePagerNumber = page;
	    	degreePagerSize = rows;
			degreeSearch();
	    }
	});
	
	//查询
	var degreeSearch = function(flag){
		var sRclassId = $('#sRclassId',$this).val();
		var content = {'clazz.classId':sRclassId,page:degreePagerNumber,rows:degreePagerSize};
		//取得列表信息
		var url = 'academic/queryLessonDegree.do';
		var result = syncCallService(url,content);
		if(result.isSuccess){
			var  data = result.data;
			var datagridData = eval("("+data.datagridData+")");
			$(degreeList).datagrid('loadData',datagridData);
			//需要重新重新分页信息
			if(flag){
				getDegreeTotal(content);
			}
		}else{
			$.messager.alert('提示',result.message,'error');
		}
	};
	//统计总数
	var getDegreeTotal = function(content){
		var url = "academic/getTotalCountLessonDegree.do";
		asyncCallService(url,content,
		function(result){
			if(result.isSuccess){
				var data = result.data;
				$(degreePager).pagination({  
					pageNumber:1,
					total:data.total
				});
			}else{
				$.messager.alert('提示',result.message,"error");
			}
		});
	};
	//修改
	var onDegreeUpdate = function(){
		if(selectDegreeRow==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		}
		$(singleDegreeForm).form('clear');
		singleDegreeCombobox();
		$(singleDegreeForm).form('load',selectDegreeRow);
		$('#teacher',singleDegreeForm).combobox('setValue',selectDegreeRow.teacherId);
		$('#classroom',singleDegreeForm).combobox('setValue',selectDegreeRow.classroomId);
		$('#user',singleDegreeForm).combobox('setValue',selectDegreeRow.userId);
		$('#classId',singleDegreeForm).val(selectDegreeRow.classId);
		$(singleDegreeDialog).dialog('open');
	};
	//删除
	var onDegreeDelete = function(){
		if(selectDegreeRow==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		}
		$.messager.confirm('确认', '确定删除该记录吗?', function(r){
			if(r){
				var url = "academic/deleteLessonDegree.do";
				var content = {lessonDegreeId:selectDegreeRow.lessonDegreeId,lessons:selectDegreeRow.lessons,
						'clazz.classId':selectDegreeRow.classId};
				$.post(url,content,
					function(result){
						if(result.isSuccess){
							degreeSearch(true);
							var arrangeLessons = result.data.arrangeLessons;
							var sRlessons = $('#sRlessons',$this).val();
							$('#arrangeProgress',autoDegreeDialog).html('<span style="color:red">'
									+arrangeLessons+'</span>/'+sRlessons);
							$('#sRarrangeLessons',$this).val(arrangeLessons);
						}else{
							$.messager.alert('提示',result.message,"error");
						}
				},'json');
			}
		});
	};
	
	//-----------------------------------自动排课-----------------------------------
	var autoDegreeDialog = $('#autoDegreeDialog',$this);
	var autoDegreeForm = $('#autoDegreeForm',$this);
	
	var paramList = $('#paramList',autoDegreeDialog);
	var selectParamIndex = null;
	
	//初始化combobox
	var autoDegreeCombobox = function(){
		$('#groupType',autoDegreeForm).combobox({
			  width:150,
			  onSelect:function(record){
				  TRN.InGroupTypeTimeList = null;
				  var groupType=$('#groupType',autoDegreeForm).combobox('getValue');
				  $('#time',autoDegreeForm).combobox({
					  disabled:false,
					  data:TRN.getInGroupTypeTimeList(groupType)
				  });
			  }
		  });
		$('#time',autoDegreeForm).combobox({
			  width:150,
			  disabled:true,
			  valueField:'periodTimeId',
			  textField:'time'
		  });
		$('#teacher',autoDegreeForm).combobox({
			  width:150,
			  data:TRN.getIsTeacherEmployeeList(),
			  valueField:'userId',
			  textField:'userName'
		  });
		var sRschoolId = $('#sRschoolId',$this).val();
		$('#classroom',autoDegreeForm).combobox({
			  width:150,
			  data:TRN.getInSchoolClassroomList(sRschoolId),
			  valueField:'classroomId',
			  textField:'classroomName'
		  });
	};
	
	//自动排课
	var autoDegree = function(){
		$(autoDegreeForm).form('clear');
		$(paramList).datagrid('loadData',{rows:[]});
		var sRarrangeLessons = $('#sRarrangeLessons',$this).val();
		var sRlessons = $('#sRlessons',$this).val();
		$('#arrangeProgress',autoDegreeDialog).html('<span style="color:red">'
				+sRarrangeLessons+'</span>/'+sRlessons);
		var waitDegree = sRlessons - sRarrangeLessons;
		if(waitDegree<=0){
			$.messager.alert('提示','没有可排课时','warning');
			return;
		}
		var sRclassId = $('#sRclassId',$this).val();
		$('#classId',autoDegreeDialog).val(sRclassId);
		$('#waitDegree',autoDegreeDialog).val(waitDegree);
		$('#firstDegreeDate',autoDegreeDialog).val(nowDate32());
		autoDegreeCombobox();
		var sRteacherId = $('#sRteacherId',$this).val();
		$('#teacher',autoDegreeDialog).combobox('setValue',sRteacherId);
		var sRclassroomId = $('#sRclassroomId',$this).val();
		$('#classroom',autoDegreeDialog).combobox('setValue',sRclassroomId);
		$(autoDegreeDialog).dialog('open');
	};
	
	$(autoDegreeDialog).dialog({
		title: '编辑排课参数',  
	    width:850,
	    height:500,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    toolbar:[{
			text:'排课',
			iconCls:'icon-save',
			handler:function(){
				saveAutoDegree();
			}
		},'-',{
			text:'退出',
			iconCls:'icon-cancel',
			handler:function(){
				$(autoDegreeDialog).dialog('close');
			}
		}]
	});
	
	$(paramList).datagrid({
		singleSelect:true,
		method:"POST",
		nowrap:true,
		striped: true,
		collapsible:true,
		rownumbers:true,
		fit:true,
		toolbar:[	
		         	{text:'添加',iconCls:'icon-add',handler:function(){addParam();}},'-',
					{text:'编辑',iconCls:'icon-edit',handler:function(){ }},'-',
					{text:'删除',iconCls:'icon-remove',handler:function(){deleteParam();}}
				],
		columns:[[
			{field:'weekValue',hidden:true},
			{field:'teacherId',hidden:true},
			{field:'classroomId',hidden:true},
		    {field:'week',title:'周期',width:50,align:'center'},
		    {field:'time',title:'时间段',width:150,align:"center"},
		    {field:'minute',title:'分钟',width:100,align:"center",
				formatter: function(value,row,index){
					var time = row.time.split('-');
					return parseInt(minuteDiff(time[0],time[1]));
			  }},
		    {field:'lessons',title:'课时',width:50,align:"center"},
		    {field:'teacher',title:'讲师',width:100,align:"center"},
		    {field:'classroom',title:'教室',width:150,align:"center"}
		]],
		onClickRow:function(rowIndex, rowData){
			selectParamIndex = rowIndex;
		}
	});
	
	var addParamValidate = function(){
		var week = $('#week',autoDegreeDialog).combobox('getValue');
		if(''==week){
			$.messager.alert('提示','请选择周期','warning');
			return false;
		}
		var time = $('#time',autoDegreeDialog).combobox('getValue');
		if(''==time){
			$.messager.alert('提示','请选择时间段','warning');
			return false;
		}
		var teacher = $('#teacher',autoDegreeDialog).combobox('getValue');
		if(''==teacher){
			$.messager.alert('提示','请选择讲师','warning');
			return false;
		}
		var classroom = $('#classroom',autoDegreeDialog).combobox('getValue');
		if(''==classroom){
			$.messager.alert('提示','请选择教室','warning');
			return false;
		}
		return true;
	};
	//添加自动排课参数
	var addParam = function(){
		if(!addParamValidate()){
			return;
		}
		var weekValue = $('#week',autoDegreeDialog).combobox('getValue');
		var teacherId = $('#teacher',autoDegreeDialog).combobox('getValue');
		var classroomId = $('#classroom',autoDegreeDialog).combobox('getValue');
		var week = $('#week',autoDegreeDialog).combobox('getText');
		var time = $('#time',autoDegreeDialog).combobox('getText');
		var timeStr = time.split('-');
		var sRlessonMinute = $('#sRlessonMinute',$this).val();
		var lessons = parseInt(minuteDiff(timeStr[0],timeStr[1])/sRlessonMinute);
		if(lessons==0){
			$.messager.alert('提示','时间段不足一个课时','warning');
			return;
		}
		var teacher = $('#teacher',autoDegreeDialog).combobox('getText');
		var classroom = $('#classroom',autoDegreeDialog).combobox('getText');
		var content = {weekValue:weekValue,teacherId:teacherId,classroomId:classroomId,
				week:week,time:time,lessons:lessons,teacher:teacher,classroom:classroom};
		$(paramList).datagrid('appendRow',content);
	};
	//删除自动排课参数
	var deleteParam =function(){
		$(paramList).datagrid('deleteRow',selectParamIndex);
	};
	//保存自动排课
	var saveAutoDegree = function(){
		var rows = $(paramList).datagrid('getRows');
		if(rows.length==0){
			$.messager.alert('提示','请添加自动排课信息','warning');
			return;
		}
		var allInfo = new Array();
		var rowInfo = null;
		for(var i=0;i<rows.length;i++){
			rowInfo = new Array();
			rowInfo.push(rows[i].weekValue);
			rowInfo.push(rows[i].time);
			rowInfo.push(rows[i].lessons);
			rowInfo.push(rows[i].teacherId);
			rowInfo.push(rows[i].classroomId);
			allInfo.push(rowInfo.join(','));
		}
		var allInfoStr = allInfo.join(';');
		$('#params',autoDegreeDialog).val(allInfoStr);
		
		$(autoDegreeForm).form('submit',{
			url: 'academic/autoDegreeSaveLessonDegree.do',
			onSubmit: function(){
				return true;
			},
			success: function(data){
				var result = eval('('+data+')');
				if(result.isSuccess){
					var fn = function(){
						degreeSearch(true);
					};
					$.messager.alert('提示','保存成功','info',fn);
					var arrangeLessons = result.data.arrangeLessons;
					var sRlessons = $('#sRlessons',$this).val();
					$('#arrangeProgress',autoDegreeDialog).html('<span style="color:red">'
							+arrangeLessons+'</span>/'+sRlessons);
					$('#sRarrangeLessons',$this).val(arrangeLessons);
					$(autoDegreeDialog).dialog('close');
				}else{
					$.messager.alert('提示',result.message,"error");
				}
			}
		});
	};
	
	//清除排课
	var clearParam = function(){
		var rows = $(degreeList).datagrid('getRows');
		if(rows.length==0){
			$.messager.alert('提示','排课信息列表为空','warning');
			return;
		}
		$.messager.confirm('确认', '确定清除排课吗?', function(r){
			if(r){
				var url = "academic/clearLessonDegree.do";
				var idList = new Array();
				var lessonList = new Array();
				for(var i=0;i<rows.length;i++){
					idList.push(rows[i].lessonDegreeId);
					lessonList.push(rows[i].lessons);
				}
				var idStr = idList.join(',');
				var lessonStr = lessonList.join(',');
				var sRclassId = $('#sRclassId',$this).val();
				var content = {idStr:idStr,lessonStr:lessonStr,
						'clazz.classId':sRclassId};
				$.post(url,content,
					function(result){
						if(result.isSuccess){
							degreeSearch(true);
							var arrangeLessons = result.data.arrangeLessons;
							var sRlessons = $('#sRlessons',$this).val();
							$('#arrangeProgress',autoDegreeDialog).html('<span style="color:red">'
									+arrangeLessons+'</span>/'+sRlessons);
							$('#sRarrangeLessons',$this).val(arrangeLessons);
						}else{
							$.messager.alert('提示',result.message,"error");
						}
				},'json');
			}
		});
	};
	//====================================点名========================================
	var pointNameDialog = $('#pointNameDialog',$this);
	var pointNameForm = $('#pointNameForm',pointNameDialog);
	
	var pointNameList = $('#pointNameList',pointNameDialog);
	var pointNameRow = null;
	var pointNameIndex = null;
	
	var pointNamePager = $('#pointNamePager',pointNameDialog);
	var pointNameNumber = 1;
	var pointNameSize = 10;
	
	var studentTree = $('#studentTree',pointNameDialog);
	
	var savePointDialog = $('#savePointDialog',pointNameDialog);
	var savePointForm = $('#savePointForm',pointNameDialog);
	
	var isAdd = false;
	
	var lessonInfoForm = $('#lessonInfoForm',pointNameDialog);
	
	$(pointNameDialog).dialog({
		title: '点名',  
	    width:1000,
	    height:height-10,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false
	});
	
	//打开点名面板
	var pointName = function(){
		if(selectDegreeRow==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		}
		$(pointNameForm).form('clear');
		$(lessonInfoForm).form('clear');
		$(lessonInfoForm).form('load',selectDegreeRow);
		$('#teacherId',lessonInfoForm).val(selectDegreeRow.teacherId);
		$('#classroomId',lessonInfoForm).val(selectDegreeRow.classroomId);
		$('#userId',lessonInfoForm).val(selectDegreeRow.userId);
		$('#classId',lessonInfoForm).val(selectDegreeRow.classId);
		getStudentTree();
		pointNameSearch(true);
		$(pointNameDialog).dialog('open');
	};
	
	//更新课程信息
	var saveWorkUpdate = function(){
		$(lessonInfoForm).form('submit',{
			url: 'academic/saveLessonDegree.do',
			onSubmit: function(){
				return true;
			},
			success: function(data){
				var result = eval('('+data+')');
				if(result.isSuccess){
					$.messager.alert('提示','更新成功','info');
				}else{
					$.messager.alert('提示',result.message,"error");
				}
			}
		});
	};
	
	
	$('#workUpdate',pointNameDialog).click(function(){
		saveWorkUpdate();
	});
	
	$(studentTree).tree({
		checkbox:true
	});
	
	//获取学员列表
	var getStudentTree = function(){
		var url = 'stuClass/getStudentTreeStuClass.do';
		var content = {'clazz.classId':selectDegreeRow.classId,
				lessonDegreeDate:selectDegreeRow.lessonDegreeDate,
				lessonDegreeId:selectDegreeRow.lessonDegreeId};
		var result = syncCallService(url,content);
		if(result.isSuccess){
			var studentTreeData = eval("("+result.data.studentTree+")");
			$(studentTree).tree({
				data:studentTreeData
			});
		}else{
			$.messager.alert('提示',result.message,"error");
		}
	};
	
	$(pointNameList).datagrid({
		singleSelect:true,
		method:"POST",
		nowrap:true,
		striped: true,
		collapsible:true,
		rownumbers:true,
		fit:true,
		toolbar:[
		         {text:'登记',iconCls:'icon-add',handler:function(){pointNameAdd();}},
		         {text:'编辑',iconCls:'icon-edit',handler:function(){pointNameUpdate();}},
		         {text:'删除',iconCls:'icon-remove',handler:function(){pointNameDelete();}},'-',
		         {text:'退出',iconCls:'icon-cancel',
		        	 handler:function(){
		        		 $(pointNameDialog).dialog('close');
		        	 }
		         }
		         ],
		columns:[[
			{field:'studentId',title:'学号',width:100,align:"center"},
			{field:'studentName',title:'姓名',width:100,align:"center"},
			{field:'scStatus',title:'选班状态',width:100,align:"center",
				formatter:function(value,row,index){
					if(value==1){
						return "正常";
					}else if(value==2){
						return "插班";
					}else if(value==3){
						return "转班";
					}else if(value==4){
						return "退学";
					}else if(value==5){
						return "休学";
					}else if(value==6){
						return "弃学";
					}
				}
			},
			{field:'lessons',title:'课时',width:50,align:"center"},
			{field:'status',title:'出勤状态',width:100,align:"center",
				formatter:function(value,row,index){
					if(value==0){
						return "正常";
					}else if(value==1){
						return "迟到";
					}else if(value==2){
						return "早退";
					}else if(value==3){
						return "旷课";
					}else if(value==4){
						return "请假";
					}
				}
			},
//			{field:'comeTime',title:'签到',width:100,align:"center"},
//			{field:'goTime',title:'签退',width:100,align:"center"},
			{field:'note',title:'课堂表现',width:100,align:"center"},
			{field:'showProgress',title:'个人课时进度',width:150,align:"center",
				formatter:function(value,row,index){
					return getBar(row.courseProgress,row.maxLessons);
				}
			}
		]],
		onClickRow:function(rowIndex, rowData){
			pointNameRow = rowData;
			pointNameIndex = rowIndex;
		},
		onLoadSuccess:function(){
			pointNameRow = null;
			pointNameIndex = null;
			pointNameNumber = 1;
		}
	});
	
	$(savePointDialog).dialog({
		title:'编辑出勤信息',  
	    width:500,
	    height:200,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    toolbar:[{
			text:'保存',
			iconCls:'icon-save',
			handler:function(){
				pointNameSave();
			}
		},'-',{
			text:'退出',
			iconCls:'icon-cancel',
			handler:function(){
				$(savePointDialog).dialog('close');
			}
		}]
	});
	
	//点名登记
	var pointNameAdd = function(){
		var nodes = $(studentTree).tree('getChecked');
		if(nodes.length==0){
			$.messager.alert('提示','请在学员名单勾选要登记的学员',"warning");
			return;
		}
		$(savePointForm).form('clear');
		isAdd = true;
		$('#lessonDegreeId',savePointForm).val(selectDegreeRow.lessonDegreeId);
		$('#classId',savePointForm).val(selectDegreeRow.classId);
		$('#lessons',savePointForm).val(selectDegreeRow.lessons);
		$('#lessonType',savePointForm).val(selectDegreeRow.lessonType);
		$('#lessonStatus',savePointForm).val(selectDegreeRow.lessonStatus);
		$(savePointDialog).dialog({title:'批量考勤'});
		$(savePointDialog).dialog('open');
	};
	
	var pointNameUpdate = function(){
		if(pointNameRow==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		}
		$(savePointForm).form('clear');
		isAdd = false;
		$(savePointForm).form('load',pointNameRow);
		$(savePointDialog).dialog('open');
	};
	
	//保存学员出勤表
	var pointNameSave = function(){
		var url = 'academic/saveAttend.do';
		
		var targetList = new Array();
		if(isAdd){
			var nodes = $(studentTree).tree('getChecked');
			var idList = new Array();
			for(var i=0;i<nodes.length;i++){
				idList.push(nodes[i].id);
				targetList.push(nodes[i].target);
			}
			var idStr = idList.join(',');
			url = 'academic/batchSaveAttend.do?idStr='+idStr;
		}
		
		$(savePointForm).form('submit',{
			url:url,
			onSubmit: function(){
				var status = $('input:radio[name="status"]:checked',savePointForm).val();
				if(status==null){
					$.messager.alert('提示','请选择出勤状态',"warning");
					return false;
				}
				return true;
			},
			success: function(data){
				var result = eval('('+data+')');
				if(result.isSuccess){
					var fn = function(){
						pointNameSearch(true);
					};
					$.messager.alert('提示','保存成功','info',fn);
					
					for(var i=0;i<targetList.length;i++){
						$(studentTree).tree('remove',targetList[i]);
					}
					$(savePointDialog).dialog('close');
				}else{
					$.messager.alert('提示',result.message,"error");
				}
			}
		});
	};
	
	var pointNameSearch = function(flag){
		var content = {'lessonDegree.lessonDegreeId':selectDegreeRow.lessonDegreeId,
				page:pointNameNumber,rows:pointNameSize};
		//取得列表信息
		var url = 'academic/queryAttend.do';
		var result = syncCallService(url,content);
		if(result.isSuccess){
			var  data = result.data;
			var datagridData = eval("("+data.datagridData+")");
			$(pointNameList).datagrid('loadData',datagridData);
			//需要重新重新分页信息
			if(flag){
				pointNameGetTotal(content);
			}
		}else{
			$.messager.alert('提示',result.message,'error');
		}
	};
	
	var pointNameGetTotal = function(content){
		var url = "academic/getTotalCountAttend.do";
		asyncCallService(url,content,
		function(result){
			if(result.isSuccess){
				var data = result.data;
				$(pointNamePager).pagination({  
					pageNumber:1,
					total:data.total
				});
			}else{
				$.messager.alert('提示',result.message,"error");
			}
		});
	};

	var pointNameDelete = function(){
		if(pointNameRow==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		}
		$.messager.confirm('确认', '确定删除该记录吗?', function(r){
			if(r){
				var url = "academic/deleteAttend.do";
				var content = {attendId:pointNameRow.attendId};
				$.post(url,content,
					function(result){
						if(result.isSuccess){
							pointNameSearch(true);
							getStudentTree();
						}else{
							$.messager.alert('提示',result.message,"error");
						}
				},'json');
			}
		});
	};
  };
})(jQuery);   