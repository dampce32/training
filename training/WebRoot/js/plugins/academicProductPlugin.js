// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.academicProductInit = function() {
	  var $this = $(this);
	  var id = $(this).attr('id');
	  var width = $(document.body).width();
	  var height = $(document.body).height();
	  
	  var queryContent = $('#queryContent',$this);
	  var searchBtn = $('#searchBtn',$this);
	  var viewList = $('#viewList',$this);
	  var selectRow = null;
	  var selectIndex = null;
	  var pageNumber = 1;
	  var pageSize = 10;
	  
	  var selectClassTypeDialog = $('#selectClassTypeDialog',$this);
		
	  var selectedDialog = $('#selectedDialog',$this);
	  var selectedForm = $('#selectedForm',$this);
	
	  var selectedClassInfoPanel = $('#selectedClassInfoPanel',selectedDialog);
	  var selectedClassInfoForm= $('#selectedClassInfoForm',selectedDialog);
	
	  var oneToOneDialog = $('#oneToOneDialog',$this);
	  var oneToOneForm = $('#oneToOneForm',$this);
	
	  var oneToManyDialog = $('#oneToManyDialog',$this);
	  var oneToManyForm = $('#oneToManyForm',$this);
	
	  var classInfoPanel = $('#classInfoPanel',oneToManyDialog);
	  var classInfoForm = $('#classInfoForm',oneToManyDialog);
	
	  var classInfo = null;
	  
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
		method:"POST",
		singleSelect:true,
		nowrap:true,
		striped: true,
		collapsible:true,
		rownumbers:true,
		pagination:true,
		fit:true,
		toolbar:[
		         {text:'选班',iconCls:'icon-edit',handler:function(){selectClassType();}}
		         ],
		columns:[[  
	            {field:'billDetailId',hidden:true},
				{field:'billItemName',title:'项目',width:100,align:"center"},
				{field:'billDate',title:'日期',width:100,align:"center"},
				{field:'billCode',title:'单号',width:100,align:"center"},
				{field:'price',title:'价格',width:100,align:"center"},
				{field:'qty',title:'数量',width:50,align:"center"},
				{field:'returnQty',title:'已退数量',width:100,align:"center"},
				{field:'unitName',title:'单位',width:50,align:"center"},
				{field:'discountAmount',title:'优惠',width:100,align:"center"},
				{field:'payed',title:'合计',width:100,align:"center"},
				{field:'status',title:'状态',width:100,align:"center",
						formatter: function(value,row,index){
									if (value==0){
										return '<span style="color:red">未处理</span>';
									} else if (value==1){
										return '<span style="color:green">已处理</span>';
									}else if (value==2){
										return '已退';
									}else if (value==3){
										return '--';
									}
			    }}
		]],
		onClickRow:function(rowIndex, rowData){
			selectRow = rowData;
			selectIndex = rowIndex;
		},
		onLoadSuccess:function(){
			selectRow = null;
	 		selectIndex = null;
			pageNumber = 1;
		}
	  });
	//分页条
	$(viewList).datagrid('getPager').pagination({   
	    onSelectPage: function(page, rows){
			pageNumber = page;
			pageSize = rows;
			search();
	    }
	});
	//查询
	var search = function(flag){
		var studentId=$('#studentId',$this).val();
		var billDateBegin = $('#billDateBeginSearch',queryContent).val();
		var billDateEnd = $('#billDateEndSearch',queryContent).val();
		var content = {productType:'课程','bill.student.studentId':studentId,billDateBegin:billDateBegin,billDateEnd:billDateEnd,page:pageNumber,rows:pageSize};
		//取得列表信息
		var url = 'academic/queryBillDetail.do';
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
		var url = "academic/getTotalCountBillDetail.do";
		asyncCallService(url,content,
		function(result){
			if(result.isSuccess){
				var data = result.data;
				$(viewList).datagrid('getPager').pagination({  
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
	
	//=====================================选班=======================================
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
				}else{
					return;
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
		if(selectRow==null){
			$.messager.alert('提示',"请选择数据行","warning");
			return;
		}
		if(selectRow.status!=0){
			$.messager.alert('提示',"请选择状态为\"未处理\"的数据行","warning");
			return;
		}
		if(selectRow.unitName=='课时'){
			//验证学生是否已选班
			var url = 'stuClass/selectedValidateStuClass.do';
			var content = {'student.studentId':selectRow.studentId};
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
			
			$('input:radio[name="classType"]',selectClassTypeDialog).attr("checked",false);
			$(selectClassTypeDialog).dialog('open');
		}else if(selectRow.unitName=='学期'){
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
				    		selectRow.courseId,
				    		selectRow.studentId,
				    		parseInt($('#scStatus',selectedDialog).combobox('getValue'))
				    		)
			    });
			}
		});
		$('#class',selectedDialog).combobox({
			data:TRN.getSelectedClassList(
					selectRow.courseId,
					selectRow.studentId,
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
		$('#lessons',selectedForm).val(selectRow.qty);
		$('#showLessons',selectedDialog).val(selectRow.qty);
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
			url: 'stuClass/oneToManySaveStuClass.do?updateParam='+1+'&billDetailId='+selectRow.billDetailId,
			onSubmit: function(){
				return selectedValidate();
			},
			success: function(data){
				var result = eval('('+data+')');
				if(result.isSuccess){
					var fn = function(){
						$(selectedDialog).dialog('close');
						search(true);
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
	    var content = {studentId:selectRow.studentId,classId:classId};
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
		$('#className',oneToOneForm).val(selectRow.courseName+
				'个人班（'+selectRow.studentName+'）');
		$('#courseName',oneToOneForm).val(selectRow.courseName);
		$('#showCourseName',oneToOneDialog).val(selectRow.courseName);
		$('#lessons',oneToOneForm).val(selectRow.qty);
		$('#showLessons',oneToOneDialog).val(selectRow.qty);
		$('#studentId',oneToOneForm).val(selectRow.studentId);
		$('#courseId',oneToOneForm).val(selectRow.courseId);
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
			url: 'stuClass/oneToOneSaveStuClass.do?billDetailId='+selectRow.billDetailId,
			onSubmit: function(){
				return oneToOneValidate();
			},
			success: function(data){
				var result = eval('('+data+')');
				if(result.isSuccess){
					var fn = function(){
						$(oneToOneDialog).dialog('close');
						search(true);
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
			if(selectRow.unitName=='课时'){
				lessons.val(selectRow.qty);
			}else if(selectRow.unitName=='学期'){
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
		oneToManyCombobox(selectRow.courseId);
		$('#scStatus',oneToManyDialog).combobox('setValue',1);
		$('#scStatus',oneToManyDialog).combobox('disable');
		$('#continueReg',oneToManyForm).combobox('setValue',1);
		$('#studentId',oneToManyForm).val(selectRow.studentId);
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
			url: 'stuClass/oneToManySaveStuClass.do?updateParam='+0+'&billDetailId='+selectRow.billDetailId,
			onSubmit: function(){
				return oneToManyValidate();
			},
			success: function(data){
				var result = eval('('+data+')');
				if(result.isSuccess){
					var fn = function(){
						$(oneToManyDialog).dialog('close');
						search(true);
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
  };
})(jQuery);   