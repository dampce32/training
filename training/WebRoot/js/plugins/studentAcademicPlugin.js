// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.studentAcademicInit = function() {
	  var $this = $(this);
	  var id = $(this).attr('id');
	  
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
	  
	  $('#schoolSearch',queryContent).combobox({
		  width:150,
		  data:TRN.getAllSchoolList(),
		  valueField:'schoolId',
		  textField:'schoolName'
	  });
	  
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
					{text:'修改',iconCls:'icon-edit',handler:function(){onUpdate();}},'-',
					{text:'控制面板',iconCls:'icon-view',handler:function(){controlPanel();}},'-',
					{text:'选班处理',iconCls:'icon-flag',handler:function(){beforeSelectClass();}}
				],
		columns:[[
			{field:'studentId',title:'学号',width:100,align:"center"},
			{field:'studentName',title:'学员姓名',width:100,align:"center"},
			{field:'enrollDate',title:'报名日期',width:100,align:"center"},
			{field:'schoolName',title:'校区',width:100,align:"center"},
			{field:'availableMoney',title:'可用余额',width:100,align:"center"},
			{field:'billCount',title:'消费单',width:100,align:"center"},
			{field:'newStuClass',title:'最新选班',width:200,align:"center"},
			{field:' ',title:'学员关怀',width:100,align:"center"}
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
	//学员编辑框
	$(editDialog).dialog({  
	    title: '编辑学员',  
	    width:780,
	    height:500,
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
			text:'退出',
			iconCls:'icon-cancel',
			handler:function(){
				$(editDialog).dialog('close');
			}
		}]
	});
	
	//初始化选择条件
	var initCombobox = function(){
		//所在校区
		$('#school',editDialog).combobox({
			valueField:'schoolId',
			textField:'schoolName',
			width:250,
			data:TRN.getSelfSchoolList()
		});
		//咨询课程
		$('#potCourse',editDialog).combobox({
		  width:250,
		  data:TRN.getPotCourseList(),
		  valueField:'potCourseId',
		  textField:'courseName'
	  });
		//获知方式
		$('#media',editDialog).combobox({
		  width:250,
		  data:TRN.getMediaList(),
		  valueField:'mediaId',
		  textField:'mediaName'
	  });
		//状态
		$('#potentialStuStatus',editDialog).combobox({
		  width:250,
		  data:TRN.getPotentialStuStatusList(),
		  valueField:'potentialStuStatusId',
		  textField:'potentialStuStatusName'
	  });
		//咨询顾问
		$('#user',editDialog).combobox({
		  width:250,
		  data:TRN.getEmployeeList(),
		  valueField:'userId',
		  textField:'userName'
	  });
	};
	
	//保存前的赋值操作
	var setValue = function(){
		//姓名
		var studentName = $.trim($('#studentName',editForm).val());
		if(''==studentName){
			$.messager.alert('提示','请填写姓名','warning');
			return false;
		}
		//性别
		var sex = $('#sex',editDialog).combobox('getValue') ;
		if(''==sex){
			$.messager.alert('提示','请选择性别','warning');
			return false;
		}
		if(isNaN(parseInt(sex))){
			$.messager.alert('提示','请选择提供选择的性别','warning');
			return false;
		}
		//类型
		var studentType = $('#studentType',editDialog).combobox('getValue') ;
		if(''==studentType){
			$.messager.alert('提示','请选择类型','warning');
			return false;
		}
		if(isNaN(parseInt(studentType))){
			$.messager.alert('提示','请选择提供选择的类型','warning');
			return false;
		}
		//报名日期
		var enrollDate = $.trim($('#enrollDate',editForm).val());
		if(''==enrollDate){
			$.messager.alert('提示','请填写报名日期','warning');
			return false;
		}
		//校区
		var schoolId = $('#school',editDialog).combobox('getValue') ;
		if(''==schoolId){
			$.messager.alert('提示','请选择校区','warning');
			return false;
		}
		if(isNaN(parseInt(schoolId))){
			$.messager.alert('提示','请选择提供选择的校区','warning');
			return false;
		}
		//电话号码
		var tel = $.trim($('#tel',editForm).val());
		if(''==tel){
			$.messager.alert('提示','请填写电话号码','warning');
			return false;
		}
		//手机号码
		var mobileTel = $.trim($('#mobileTel',editForm).val());
		if(''==mobileTel){
			$.messager.alert('提示','请填写手机号码','warning');
			return false;
		}
		//获知方式
		var mediaId = $('#media',editDialog).combobox('getValue') ;
		if(''==mediaId){
			$.messager.alert('提示','请选择获知方式','warning');
			return false;
		}
		if(isNaN(parseInt(mediaId))){
			$.messager.alert('提示','请选择提供选择的获知方式','warning');
			return false;
		}
		//学习顾问
		var userId = $('#user',editDialog).combobox('getValue') ;
		if(''==userId){
			$.messager.alert('提示','请选择学习顾问','warning');
			return false;
		}
		if(isNaN(parseInt(userId))){
			$.messager.alert('提示','请选择提供选择的学习顾问','warning');
			return false;
		}
		return true;
	};

	//保存
	var onSave = function(){
		$(editForm).form('submit',{
			url: 'academic/saveStudent.do',
			onSubmit: function(){
				return setValue();
			},
			success: function(data){
				var result = eval('('+data+')');
				if(result.isSuccess){
					var fn = function(){
						TRN.StudentList = null;
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
		$(editForm).form('load',selectRow);
		initCombobox();
		$('#school',editDialog).combobox('setValue',selectRow.schoolId);
		$('#media',editDialog).combobox('setValue',selectRow.mediaId);
		$('#user',editDialog).combobox('setValue',selectRow.userId);
		$('#sex',editDialog).combobox('setValue',selectRow.sex);
		$('#studentType',editDialog).combobox('setValue',selectRow.studentType);
		$(editDialog).dialog('open');
	 };

	//查询
	var search = function(flag){
		var studentName = $('#nameSearch',queryContent).val();
		var schoolId = $('#schoolSearch',queryContent).combobox('getValue');
		//定义type,让action不报错,跳过action的判断
		var content = {type:1,page:pageNumber,rows:pageSize,
				studentName:studentName,'school.schoolId':schoolId};
		//取得列表信息
		var url = 'academic/queryStudent.do';
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
		var url = "academic/getTotalCountStudent.do";
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

	//=================================选班=====================================
	var beforeSelectClassDialog = $('#beforeSelectClassDialog',$this);

	var beforeSelectClassList = $('#beforeSelectClassList',beforeSelectClassDialog);
	
	var beforeSelectClassRow = null;
	var beforeSelectClassIndex = null;
	
	var beforeSelectClassPager = $('#beforeSelectClassPager',beforeSelectClassDialog);
	var beforeSelectClassPageNumber = 1;
	var beforeSelectClassPageSize = 10;
	
	var selectClassTypeDialog = $('#selectClassTypeDialog',beforeSelectClassDialog);
	
	var selectedDialog = $('#selectedDialog',beforeSelectClassDialog);
	var selectedForm = $('#selectedForm',beforeSelectClassDialog);
	
	var selectedClassInfoPanel = $('#selectedClassInfoPanel',selectedDialog);
	var	selectedClassInfoForm= $('#selectedClassInfoForm',selectedDialog);
	
	var oneToOneDialog = $('#oneToOneDialog',beforeSelectClassDialog);
	var oneToOneForm = $('#oneToOneForm',beforeSelectClassDialog);
	
	var oneToManyDialog = $('#oneToManyDialog',beforeSelectClassDialog);
	var oneToManyForm = $('#oneToManyForm',beforeSelectClassDialog);
	
	var classInfoPanel = $('#classInfoPanel',oneToManyDialog);
	var	classInfoForm = $('#classInfoForm',oneToManyDialog);
	
	var classInfo = null;
	
	var getBar = function(left,right){
		  var bar = '<div style="width:80px;height:13px;background:#fff;border:1px solid #ccc;text-align:left;float:left;">' +
			'<div style="width:' + left*100/right + '%;height:100%;background:blue"></div>'+
			'</div>';
		  var number = '<span style="width:50px;margin-left:5px;text-align:left;">'+
			'<span style="color: red;">'+left+'</span>/'+right+
			'</span>';
		  return bar+number;
	  };
	
	//选班处理窗口
	$(beforeSelectClassDialog).dialog({  
	    title: '选班处理',  
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
				$(beforeSelectClassDialog).dialog('close');
			}
		}]
	});
	
	//选班处理
	var beforeSelectClass = function(){
		$('#schoolSearch',beforeSelectClassQuery).combobox({
			  width:150,
			  data:TRN.getAllSchoolList(),
			  valueField:'schoolId',
			  textField:'schoolName'
		  });
		$('#courseSearch',beforeSelectClassQuery).combobox({
			  width:150,
			  data:TRN.getIsTypeCourseList(''),
			  valueField:'courseId',
			  textField:'courseName'
		  });
		$(beforeSelectClassDialog).dialog('open');
	};
	
	//未处理选班的学员
	$(beforeSelectClassList).datagrid({
		singleSelect:true,
		method:"POST",
		nowrap:true,
		striped: true,
		collapsible:true,
		rownumbers:true,
		fit:true,
		toolbar:[	
			{text:'选班',iconCls:'icon-ok',handler:function(){selectClassType();}}
		],
		columns:[[
			{field:'studentId',title:'学号',width:100,align:"center"},
			{field:'studentName',title:'学员姓名',width:100,align:"center"},
			{field:'courseName',title:'课程',width:100,align:"center"},
			{field:'billDate',title:'日期',width:100,align:"center"},
			{field:'billCode',title:'单号',width:120,align:"center"},
			{field:'price',title:'单价',width:100,align:"center"},
			{field:'actualQty',title:'数量',width:100,align:"center"},
			{field:'unitName',title:'单位',width:100,align:"center"},
			{field:'discountAmount',title:'优惠',width:100,align:"center"},
			{field:'total',title:'合计',width:100,align:"center",
				formatter:function(value,row,index){
					return row.price-row.discountAmount;
				}
			},
			{field:'status',title:'状态',width:100,align:"center",
				formatter:function(value,row,index){
					if (value==0){
						return '<span style="color:red">未处理</span>';
					} else if (value==1){
						return '<span style="color:green">已处理</span>';
					}else if (value==2){
						return '已退';
					}
				}
			}
		]],
		onClickRow:function(rowIndex, rowData){
			beforeSelectClassRow = rowData;
			beforeSelectClassIndex = rowIndex;
		},
		onLoadSuccess:function(){
			beforeSelectClassRow = null;
	 		beforeSelectClassIndex = null;
	 		beforeSelectClassPageNumber = 1;
		}
	});
	$('#selectClassSearchBtn',beforeSelectClassQuery).click(function(){
		beforeSelectClassSearch(true);
	});
	
	//查询课程消费明细
	var beforeSelectClassSearch = function(flag){
		var schoolId = $('#schoolSearch',beforeSelectClassQuery).combobox('getValue');
		var courseId = $('#courseSearch',beforeSelectClassQuery).combobox('getValue');
		var studentId = $('#studentSearch',beforeSelectClassQuery).val();
		var content = {'bill.school.schoolId':schoolId,'course.courseId':courseId,
				'bill.student.studentId':studentId,productType:'课程',status:0,
				page:beforeSelectClassPageNumber,rows:beforeSelectClassPageSize};
		//取得列表信息
		var url = 'academic/queryBillDetail.do';
		var result = syncCallService(url,content);
		if(result.isSuccess){
			var  data = result.data;
			var datagridData = eval("("+data.datagridData+")");
			$(beforeSelectClassList).datagrid('loadData',datagridData);
			//需要重新重新分页信息
			if(flag){
				beforeSelectClassGetTotal(content);
			}
		}else{
			$.messager.alert('提示',result.message,'error');
		}
	};
	//统计总数
	var beforeSelectClassGetTotal = function(content){
		var url = "academic/getTotalCountBillDetail.do";
		asyncCallService(url,content,
		function(result){
			if(result.isSuccess){
				var data = result.data;
				$(beforeSelectClassPager).pagination({  
					pageNumber:1,
					total:data.total
				});
			}else{
				$.messager.alert('提示',result.message,"error");
			}
		});
	};
	//选择班级类型窗口
	$(selectClassTypeDialog).dialog({  
	    title: '班级类型',  
	    width:400,
	    height:250,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    toolbar:[{
			text:'选择',
			iconCls:'icon-ok',
			handler:function(){
				var value = $('input:radio[name="classType"]:checked',selectClassTypeDialog).val();
				if(value==0){
					selected();
				}else if(value==1){
					oneToOne();
				}else if(value==2){
					oneToMany();
				}
				$(selectClassTypeDialog).dialog('close');
			}
		},'-',{
			text:'退出',
			iconCls:'icon-cancel',
			handler:function(){
				$(selectClassTypeDialog).dialog('close');
			}
		}]
	});
	//班级类型
	var selectClassType = function(){
		if(beforeSelectClassRow==null){
			$.messager.alert('提示',"请选择数据行","warning");
			return;
		}
		if(beforeSelectClassRow.unitName=='课时'){
			//验证学生是否已选班
			var url = 'stuClass/selectedValidateStuClass.do';
			var content = {'student.studentId':beforeSelectClassRow.studentId};
			var result = syncCallService(url,content);
			if(result.isSuccess){
				var  data = result.data;
				if(data.existence==true){
					$('#selected',selectClassTypeDialog).attr('disabled',false);
				}else{
					$('#selected',selectClassTypeDialog).attr('disabled',true);
				}
			}else{
				$.messager.alert('提示',result.message,'error');
			}
			
			$('input:radio[name="classType"]').attr("checked",false);
			$(selectClassTypeDialog).dialog('open');
		}else if(beforeSelectClassRow.unitName=='学期'){
			oneToMany();
		}
		
	};
	
	$(selectedDialog).dialog({  
	    title: '选择已选班级',  
	    width:600,
	    height:480,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    toolbar:[{
			text:'保存',
			iconCls:'icon-save',
			handler:function(){
				selectedSave();
				$(oneToOneDialog).dialog('close');
				beforeSelectClassSearch(true);
			}
		},'-',{
			text:'退出',
			iconCls:'icon-cancel',
			handler:function(){
				$(selectedDialog).dialog('close');
			}
		}]
	});
	
	var selectedCombobox = function(){
		$('#scStatus',selectedDialog).combobox({
			onSelect:function(record){
			    $('#class',selectedDialog).combobox({
				    data:TRN.getSelectedClassList(
				    		beforeSelectClassRow.courseId,
				    		beforeSelectClassRow.studentId,
				    		parseInt($('#scStatus',selectedDialog).combobox('getValue'))
				    		)
			    });
			}
		});
		$('#class',selectedDialog).combobox({
			data:TRN.getSelectedClassList(
					beforeSelectClassRow.courseId,
					beforeSelectClassRow.studentId,
					parseInt($('#scStatus',selectedDialog).combobox('getValue'))
					),
			width:200,
			valueField:'classId',
			textField:'className',
			onSelect:function(record){
				selectedClassInfoInit(record.classId);
			}
		});
		$('#user',selectedDialog).combobox({
			  width:200,
			  data:TRN.getEmployeeList(),
			  valueField:'userId',
			  textField:'userName'
		});
	};
	
	var selected = function(){
		$(selectedForm).form('clear');
		$('#scStatus',selectedDialog).combobox('setValue',1);
		selectedCombobox();
		$('#lessons',selectedForm).val(beforeSelectClassRow.qty);
		$('#showLessons',selectedDialog).val(beforeSelectClassRow.qty);
		$('#continueReg',selectedDialog).combobox('setValue',2);
		$(selectedDialog).dialog('open');
	};
	
	var selectedValidate = function(){
		var clazz = $('#class',selectedDialog).combobox('getValue');
		if(''==clazz){
			$.messager.alert('提示',"请选择班级","warning");
			return false;
		}
		var continueReg = $('#continueReg',selectedDialog).combobox('getValue');
		if(''==continueReg){
			$.messager.alert('提示',"请选择类型","warning");
			return false;
		}
		var user = $('#user',selectedDialog).combobox('getValue');
		if(''==user){
			$.messager.alert('提示',"请选择经办人","warning");
			return false;
		}
		return true;
	};
	
	var selectedSave = function(){
		$(selectedForm).form('submit',{
			//updateParam用于区分“选择已选班级”(1)和“编辑选班”(0)
			url: 'stuClass/oneToManySaveStuClass.do?updateParam='+1+'&billDetailId='+beforeSelectClassRow.billDetailId,
			onSubmit: function(){
				return selectedValidate();
			},
			success: function(data){
				var result = eval('('+data+')');
				if(result.isSuccess){
					var fn = function(){
						$(selectedDialog).dialog('close');
						beforeSelectClassSearch(true);
					};
					$.messager.alert('提示','保存成功','info',fn);
				}else{
					$.messager.alert('提示',result.message,"error");
				}
			}
		});
	};
	
	$(selectedClassInfoPanel).panel({
	    width:350,  
	    height:260,  
	    title: '班级信息'
	});  
	
	var selectedClassInfoInit = function(classId){
		$(selectedClassInfoForm).form('clear');
		var url = 'academic/queryOneClass.do';
	    var content = {studentId:beforeSelectClassRow.studentId,classId:classId};
	    var result = syncCallService(url,content);
	    if(result.isSuccess){
	    	var data = result.data.classInfo;
	    	classInfo = eval('('+data+')');
	    	$(selectedClassInfoForm).form('load',classInfo);
	    	var statusList = ['正常','插班','转班','休学','退学','弃学'];
	    	$('#oldScStatus',selectedClassInfoPanel).val(statusList[result.data.scStatus-1]);
	    	$('#stuClassId',selectedForm).val(result.data.stuClassId);
	    	$('#showStuCount',selectedClassInfoPanel).html(getBar(classInfo.stuCount,classInfo.planCount));
	    	$('#showCourseProgress',selectedClassInfoPanel).html(getBar(classInfo.courseProgress,classInfo.lessons));
	    }else{
		    $.messager.alert('提示',result.message,"error");
	    }
	};
	
	$(oneToOneDialog).dialog({  
	    title: '开设一对一个人班',  
	    width:400,
	    height:520,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    toolbar:[{
			text:'保存',
			iconCls:'icon-save',
			handler:function(){
				oneToOneSave();
			}
		},'-',{
			text:'退出',
			iconCls:'icon-cancel',
			handler:function(){
				$(oneToOneDialog).dialog('close');
			}
		}]
	});
	
	var oneToOneCombobox = function(){
		$('#teacher',oneToOneDialog).combobox({
			  width:200,
			  data:TRN.getIsTeacherEmployeeList(),
			  valueField:'userId',
			  textField:'userName'
		  });
		$('#lessonSchool',oneToOneDialog).combobox({
			  width:200,
			  data:TRN.getSelfSchoolList(),
			  valueField:'schoolId',
			  textField:'schoolName',
			  onSelect:function(record){
				  $('#classroom',oneToOneDialog).combobox({
					  disabled:false,
					  data:TRN.getInSchoolClassroomList(record.schoolId)
				  });
			  }
		  });
		$('#classroom',oneToOneDialog).combobox({
			disabled:true,
			width:200,
			valueField:'classroomId',
			textField:'classroomName'
		});
		$('#selectSchool',oneToOneDialog).combobox({
			  width:200,
			  data:TRN.getSelfSchoolList(),
			  valueField:'schoolId',
			  textField:'schoolName'
		  });
		$('#user',oneToOneDialog).combobox({
			  width:200,
			  data:TRN.getEmployeeList(),
			  valueField:'userId',
			  textField:'userName'
		});
	};
	
	var oneToOne = function(){
		$(oneToOneForm).form('clear');
		oneToOneCombobox();
		$('#className',oneToOneForm).val(beforeSelectClassRow.courseName+
				'个人班（'+beforeSelectClassRow.studentName+'）');
		$('#courseName',oneToOneForm).val(beforeSelectClassRow.courseName);
		$('#showCourseName',oneToOneDialog).val(beforeSelectClassRow.courseName);
		$('#lessons',oneToOneForm).val(beforeSelectClassRow.qty);
		$('#showLessons',oneToOneDialog).val(beforeSelectClassRow.qty);
		$('#studentId',oneToOneForm).val(beforeSelectClassRow.studentId);
		$('#courseId',oneToOneForm).val(beforeSelectClassRow.courseId);
		$('#continueReg',oneToOneForm).combobox('setValue',1);
		$(oneToOneDialog).dialog('open');
	};
	
	var oneToOneValidate = function(){
		var className = $('#className',oneToOneForm).val();
		if(''==className){
			$.messager.alert('提示',"请填写班级名称","warning");
			return false;
		}
		var startDate = $('#startDate',oneToOneForm).val();
		if(''==startDate){
			$.messager.alert('提示',"请选择开课日期","warning");
			return false;
		}
		var teacher = $('#teacher',oneToOneForm).combobox('getValue');
		if(''==teacher){
			$.messager.alert('提示',"请选择主讲老师","warning");
			return false;
		}
		var timeRule = $('#timeRule',oneToOneForm).val();
		if(''==timeRule){
			$.messager.alert('提示',"请填写上课规律","warning");
			return false;
		}
		var lessonSchool = $('#lessonSchool',oneToOneForm).combobox('getValue');
		if(''==lessonSchool){
			$.messager.alert('提示',"请选择上课地点","warning");
			return false;
		}
		var classroom = $('#classroom',oneToOneForm).combobox('getValue');
		if(''==classroom){
			$.messager.alert('提示',"请选择教室","warning");
			return false;
		}
		var lessonMinute = $('#lessonMinute',oneToOneForm).val();
		if(''==lessonMinute){
			$.messager.alert('提示',"请填写每课时分钟数","warning");
			return false;
		}
		var selectDate = $('#selectDate',oneToOneForm).val();
		if(''==selectDate){
			$.messager.alert('提示',"请选择选班日期","warning");
			return false;
		}
		var continueReg = $('#continueReg',oneToOneForm).combobox('getValue');
		if(''==continueReg){
			$.messager.alert('提示',"请选择类型","warning");
			return false;
		}
		var selectSchool = $('#selectSchool',oneToOneForm).combobox('getValue');
		if(''==selectSchool){
			$.messager.alert('提示',"请选择报名点","warning");
			return false;
		}
		var user = $('#user',oneToOneForm).combobox('getValue');
		if(''==user){
			$.messager.alert('提示',"请选择经办人","warning");
			return false;
		}
		return true;
	};
	
	var oneToOneSave = function(){
		$(oneToOneForm).form('submit',{
			url: 'stuClass/oneToOneSaveStuClass.do?billDetailId='+beforeSelectClassRow.billDetailId,
			onSubmit: function(){
				return oneToOneValidate();
			},
			success: function(data){
				var result = eval('('+data+')');
				if(result.isSuccess){
					var fn = function(){
						$(oneToOneDialog).dialog('close');
						beforeSelectClassSearch(true);
					};
					$.messager.alert('提示','保存成功','info',fn);
				}else{
					$.messager.alert('提示',result.message,"error");
				}
			}
		});
	};
	
	$(oneToManyDialog).dialog({  
	    title: '学员选班',  
	    width:780,
	    height:500,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    toolbar:[{
			text:'保存',
			iconCls:'icon-save',
			handler:function(){
				oneToManySave();
			}
		},'-',{
			text:'退出',
			iconCls:'icon-cancel',
			handler:function(){
				$(oneToManyDialog).dialog('close');
			}
		}]
	});
	
	
	var oneToManyCombobox = function(courseId){
		$('#lessonSchool',oneToManyDialog).combobox({
			  width:262,
			  data:TRN.getSelfSchoolList(),
			  valueField:'schoolId',
			  textField:'schoolName',
			  onSelect:function(record){
				  $('#class',oneToManyDialog).combobox({
					  disabled:false,
					  data:TRN.getInSSTClassList(
							  courseId,
							  record.schoolId,
							  $('input:radio[name="lessonStatus"]:checked').val(),
							  $('#teacher',oneToManyDialog).combobox('getValue')
							  )
				  });
			  }
		  });
		$('input:radio[name="lessonStatus"]').click(function(){
			var lessonStatus = $('input:radio[name="lessonStatus"]:checked').val();
			$('#scStatus',oneToManyDialog).combobox('setValue',parseInt(lessonStatus)+1);
			$('#class',oneToManyDialog).combobox({
				  disabled:false,
				  data:TRN.getInSSTClassList(
						  courseId,
						  $('#lessonSchool',oneToManyDialog).combobox('getValue'),
						  lessonStatus,
						  $('#teacher',oneToManyDialog).combobox('getValue')
						  )
			  });
		});
		$('#teacher',oneToManyDialog).combobox({
			  width:200,
			  data:TRN.getIsTeacherEmployeeList(),
			  valueField:'userId',
			  textField:'userName',
			  onSelect:function(record){
				  $('#class',oneToManyDialog).combobox({
					  disabled:false,
					  data:TRN.getInSSTClassList(
							  courseId,
							  $('#lessonSchool',oneToManyDialog).combobox('getValue'),
							  $('input:radio[name="lessonStatus"]:checked').val(),
							  record.userId
							  )
				  });
			  }
		  });
		$('#class',oneToManyDialog).combobox({
			  disabled:true,
			  width:262,
			  valueField:'classId',
			  textField:'className',
			  onSelect:function(record){
				  classInfoInit(record.classId);
				  setLessons();
				  $('#className',oneToManyForm).val(record.className);
			  }
		  });
		$('#selectSchool',oneToManyDialog).combobox({
			  width:262,
			  data:TRN.getSelfSchoolList(),
			  valueField:'schoolId',
			  textField:'schoolName'
		  });
		$('#user',oneToManyDialog).combobox({
			  width:262,
			  data:TRN.getEmployeeList(),
			  valueField:'userId',
			  textField:'userName'
		});
	};
	
	var setLessons = function(){
		var courseProgress = $('#courseProgress',oneToManyForm);
		var lessons = $('#lessons',oneToManyForm);
		if(courseProgress.val()=='')
			courseProgress.val(0);
		if(lessons.val()==''){
			if(beforeSelectClassRow.unitName=='课时'){
				lessons.val(beforeSelectClassRow.qty);
			}else if(beforeSelectClassRow.unitName=='学期'){
				lessons.val(classInfo.lessons);
			}
		}
		$('#showProgress',oneToManyDialog).html('<span style="color:red">'+
				courseProgress.val()+'</span>/'+lessons.val());
	};
	
	var oneToMany = function(){
		$(oneToManyForm).form('clear');
		$('#class',oneToManyDialog).combobox({
			data:CSIT.ClearCombobxData
		});
		oneToManyCombobox(beforeSelectClassRow.courseId);
		$('#scStatus',oneToManyDialog).combobox('setValue',1);
		$('#scStatus',oneToManyDialog).combobox('disable');
		$('#continueReg',oneToManyForm).combobox('setValue',1);
		$('#studentId',oneToManyForm).val(beforeSelectClassRow.studentId);
		$('#showProgress',oneToManyDialog).html('<span style="color:red">'+
				0+'</span>/'+0);
		$(oneToManyDialog).dialog('open');
	};
	
	var oneToManyValidate = function(){
		var lessonSchool = $('#lessonSchool',oneToManyDialog).combobox('getValue');
		if(''==lessonSchool){
			$.messager.alert('提示','请选择上课校区','warning');
			return false;
		}
		var clazz = $('#class',oneToManyDialog).combobox('getValue');
		if(''==clazz){
			$.messager.alert('提示','请选择班级','warning');
			return false;
		}
		var selectDate = $('#selectDate',oneToManyDialog).val();
		if(''==selectDate){
			$.messager.alert('提示','请选择选班日期','warning');
			return false;
		}
		var selectSchool = $('#selectSchool',oneToManyDialog).combobox('getValue');
		if(''==selectSchool){
			$.messager.alert('提示','请选择报名点','warning');
			return false;
		}
		var user = $('#user',oneToManyDialog).combobox('getValue');
		if(''==user){
			$.messager.alert('提示','请选择经办人','warning');
			return false;
		}
		return true;
	};
	
	var oneToManySave = function(){
		$(oneToManyForm).form('submit',{
			//updateParam用于区分“选择已选班级”(1)和“编辑选班”(0)
			url: 'stuClass/oneToManySaveStuClass.do?updateParam='+0+'&billDetailId='+beforeSelectClassRow.billDetailId,
			onSubmit: function(){
				return oneToManyValidate();
			},
			success: function(data){
				var result = eval('('+data+')');
				if(result.isSuccess){
					var fn = function(){
						$(oneToManyDialog).dialog('close');
						beforeSelectClassSearch(true);
					};
					$.messager.alert('提示','保存成功','info',fn);
				}else{
					$.messager.alert('提示',result.message,"error");
				}
			}
		});
	};
	
	$(classInfoPanel).panel({
	    width:350,  
	    height:230,  
	    title: '班级信息'
	});  
	
	var  classInfoInit = function(classId){
		$(classInfoForm).form('clear');
		var url = 'academic/queryOneClass.do';
	    var content = {studentId:'',classId:classId};
	    var result = syncCallService(url,content);
	    if(result.isSuccess){
	    	var data = result.data.classInfo;
	    	classInfo = eval('('+data+')');
	    	$(classInfoForm).form('load',classInfo);
	    	$('#showStuCount',classInfoPanel).html(getBar(classInfo.stuCount,classInfo.planCount));
	    	$('#showCourseProgress',classInfoPanel).html(getBar(classInfo.courseProgress,classInfo.lessons));
	    }else{
		    $.messager.alert('提示',result.message,"error");
	    }
	};
	
	//===============================控制面板===================================
	var controlPanelDialog = $('#controlPanelDialog',$this);
	
	var controlPanelTabs = $('#controlPanelTabs',controlPanelDialog);
	
	//控制面板窗口
	$(controlPanelDialog).dialog({
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
				$(controlPanelDialog).dialog('close');
			}
		}]
	});
	
	var controlPanel = function(){
		if(selectRow==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		}
		$(controlPanelTabs).tabs("select",0);
		$(controlPanelDialog).dialog({title:'学员控制面板（'+selectRow.studentName+'）'});
		$(controlPanelDialog).dialog('open');
	};
	
	//-------------------------------控制面板切换-----------------------------------
	$(controlPanelTabs).tabs({
		
	    onSelect:function(title){
	    	var tab = $(controlPanelTabs).tabs('getSelected');
	    	var studentId=selectRow.studentId;
	    	var studentName=selectRow.studentName;
	    	
	    	//获取学员基本信息
	    	var url = "customerService/initStudent.do";
			var content={studentId:studentId,type:0};
			var result = syncCallService(url,content);
			if(result.isSuccess){
				var data = result.data;
				var student=eval("("+data.student+")");
				var availableMoney =student.availableMoney;
				var consumedMoney=student.consumedMoney;
				var arrearageMoney=student.arrearageMoney;
   	    	    var creditExpiration=student.creditExpiration;
   	    	    var mediaName=student.mediaName;
   	    	    var schoolName=student.schoolName;
   	    	    var userName=student.userName;
   	    	    var studentName=student.studentName;
   	    	    var appellation=student.appellation;
   	    	    var sex=student.sex;
   	    	    var studentType=student.studentType;
   	    	    var birthday=student.birthday;
   	    	    var enrollDate=student.enrollDate;
   	    	    var tel=student.tel;
   	    	    var mobileTel=student.mobileTel;
   	    	    var tel1=student.tel1;
   	    	    var qq=student.qq;
   	    	    var email=student.email;
   	    	    var address=student.address;
   	    	    var postCode=student.postCode;
   	    	    var idcard=student.idcard;
   	    	    var diploma=student.diploma;
   	    	    var nextReplyDate=student.nextReplyDate;
   	    	    var note=student.note;
			}else{
				$.messager.alert('提示',result.message,'error');
			}
			
			if(title=='基本信息'){
				var panelInfo = {
	                    href:'customerService/myInformation.do', border:false, plain:true,
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
	                       $('#studentId',tab).val(studentId);
	                       $('#mediaName',tab).val(mediaName);
	                       $('#schoolName',tab).val(schoolName);
	                       $('#userName',tab).val(userName);
	                       $('#appellation',tab).val(appellation);
	                       $('#studentName',tab).val(studentName);
	                       $('#birthday',tab).val(birthday);
	                       $('#enrollDate',tab).val(enrollDate);
	                       $('#mobileTel',tab).val(mobileTel);
	                       $('#tel',tab).val(tel);
	                       $('#tel1',tab).val(tel1);
	                       $('#qq',tab).val(qq);
	                       $('#email',tab).val(email);
	                       $('#address',tab).val(address);
	                       $('#postCode',tab).val(postCode);
	                       $('#idcard',tab).val(idcard);
	                       $('#nextReplyDate',tab).val(nextReplyDate);
	                       $('#diploma',tab).val(diploma);
	                       $('#note',tab).val(note);
	                       if(sex==0){
								$('#sex',tab).val('女');
							}
							else {
								$('#sex',tab).val('男');
							}
							if(studentType==0){
								$('#studentType',tab).val('学生');
							}
							else {
								$('#studentType',tab).val('上班族');
							}
	                    }
	                };
	        	
	        	$(controlPanelTabs).tabs('update', {
	        		tab: tab,
	        		options: panelInfo
	        	});
	        	$('#studentId',tab).val(studentId);
               $('#mediaName',tab).val(mediaName);
               $('#schoolName',tab).val(schoolName);
               $('#userName',tab).val(userName);
               $('#appellation',tab).val(appellation);
               $('#studentName',tab).val(studentName);
               $('#birthday',tab).val(birthday);
               $('#enrollDate',tab).val(enrollDate);
               $('#mobileTel',tab).val(mobileTel);
               $('#tel',tab).val(tel);
               $('#tel1',tab).val(tel1);
               $('#qq',tab).val(qq);
               $('#email',tab).val(email);
               $('#address',tab).val(address);
               $('#postCode',tab).val(postCode);
               $('#idcard',tab).val(idcard);
               $('#nextReplyDate',tab).val(nextReplyDate);
               $('#diploma',tab).val(diploma);
               $('#note',tab).val(note);
               if(sex==0){
					$('#sex',tab).val('女');
				}
				else {
					$('#sex',tab).val('男');
				}
				if(studentType==0){
					$('#studentType',tab).val('学生');
				}
				else {
					$('#studentType',tab).val('上班族');
				}
			}
	    	if(title=='账户信息'){
	        	$('#paymentList',tab).datagrid('loadData',{ total: 0, rows: [] });
	        	var panelInfo = {
	                    href:'academic/academicAccount.do', border:false, plain:true,
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
	                       $('#studentId',tab).val(studentId);
	                       $('#availableMoney',tab).val(availableMoney);
	                       $('#consumedMoney',tab).val(consumedMoney);
	                       $('#arrearageMoney',tab).val(arrearageMoney);
	                       $('#creditExpiration',tab).val(creditExpiration);
	                    }
	                };
	        	
	        	$(controlPanelTabs).tabs('update', {
	        		tab: tab,
	        		options: panelInfo
	        	});
	        	$('#studentId',tab).val(studentId);
	        	$('#availableMoney',tab).val(availableMoney);
                $('#consumedMoney',tab).val(consumedMoney);
                $('#arrearageMoney',tab).val(arrearageMoney);
                $('#creditExpiration',tab).val(creditExpiration);
	        }
	        if(title=='已购产品'){
	        	$('#viewList',tab).datagrid('loadData',{ total: 0, rows: [] });
	        	var panelInfo = {
	                    href:'academic/academicProduct.do', border:false, plain:true,
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
	                       $('#studentId',tab).val(studentId);
	                    }
	                };
	        	
	        	$(controlPanelTabs).tabs('update', {
	        		tab: tab,
	        		options: panelInfo
	        	});
	        	$('#studentId',tab).val(studentId);
	        }
	        if(title=='消费单'){
	        	$('#billList',tab).datagrid('loadData',{ total: 0, rows: [] });
	        	var panelInfo = {
	                    href:'academic/academicExpense.do', border:false, plain:true,
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
	                       $('#studentId',tab).val(studentId);
	                       $('#studentName',tab).val(studentName);
	                    }
	                };
	        	
	        	$(controlPanelTabs).tabs('update', {
	        		tab: tab,
	        		options: panelInfo
	        	});
	        	$('#studentId',tab).val(studentId);
	        	$('#studentName',tab).val(studentName);
	        }
	        if(title=='选班信息'){
	        	$('#stuClassInfoList',tab).datagrid('loadData',{ total: 0, rows: [] });
	        	var panelInfo = {
	                    href:'academic/academicStuClassInfo.do', border:false, plain:true,
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
	                       $('#selectRowStudentId',tab).val(studentId);
	                    }
	            };
	        	$(controlPanelTabs).tabs('update', {
	        		tab: tab,
	        		options: panelInfo
	        	});
	        	$('#selectRowStudentId',tab).val(studentId);
	        }
	    } 
	});
  };
})(jQuery);   