// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.academicStuClassInfoInit = function() {
	var $this = $(this);
	var id = $(this).attr('id');
	
	var height = $(document.body).height();
	
	var stuClassInfoList = $('#stuClassInfoList',$this);
		
	var stuClassInfoRow = null;
	var stuClassInfoIndex = null;
		
	var oneToManyDialog = $('#oneToManyDialog',$this);
	var oneToManyForm = $('#oneToManyForm',oneToManyDialog);
	var classInfoPanel = $('#classInfoPanel',oneToManyDialog);
	var classInfoForm = $('#classInfoForm',oneToManyDialog);
	
	var browseDialog = $('#browseDialog',$this);
	var browseForm = $('#browseForm',browseDialog);
	
	var getBar = function(left,right){
		var bar = '<div style="width:80px;height:12px;background:#fff;border:1px solid #ccc;text-align:left;float:left;">' +
			'<div style="width:' + left*100/right + '%;height:100%;background:blue"></div>'+
			'</div>';
		var number = '<span style="width:50px;margin-left:5px;text-align:left;">'+
			'<span style="color: red;">'+left+'</span>/'+right+
			'</span>';
		return bar+number;
	};
	
	//列表
	$(stuClassInfoList).datagrid({
		singleSelect:true,
		method:"POST",
		nowrap:true,
		striped: true,
		collapsible:true,
		rownumbers:true,
		fit:true,
		toolbar:[	
			{text:'查询',iconCls:'icon-search',handler:function(){stuClassInfoSearch();}},'-',
			{text:'浏览',iconCls:'icon-view',handler:function(){stuClassInfoBrowse();}},
			{text:'编辑',iconCls:'icon-edit',handler:function(){stuClassInfoUpadate();}},
			{text:'删除',iconCls:'icon-remove',handler:function(){stuClassInfoDelete();}}
		],
		columns:[[
			{field:'stuClassId',title:'选班号',width:50,align:"center"},
			{field:'classId',title:'班号',width:50,align:"center"},
			{field:'className',title:'班级名称',width:150,align:"center"},
			{field:'lessonSchoolName',title:'上课地点',width:100,align:"center"},
			{field:'selectDate',title:'选班日期',width:100,align:"center"},
			{field:'scStatus',title:'选班状态',width:100,align:"center",
				formatter:function(value,row,index){
					if(value==1){
						return '正常';
					}else if(value==2){
						return '插班';
					}else if(value==3){
						return '转班';
					}else if(value==4){
						return '休学';
					}else if(value==5){
						return '退学';
					}else if(value==6){
						return '弃学';
					}
				}
			},
			{field:'continueReg',title:'类型',width:100,align:"center",
				formatter:function(value,row,index){
					if(value==0){
						return '试听';
					}else if(value==1){
						return '新增';
					}else if(value==2){
						return '续报';
					}
				}
			},
			{field:'courseProgress',title:'课时进度',width:150,align:"center",
				formatter: function(value,row,index){
					return getBar(row.courseProgress,row.lessons);
				}},
			{field:'selectSchoolName',title:'报名点',width:100,align:"center"}
		]],
		onClickRow:function(rowIndex, rowData){
			stuClassInfoRow = rowData;
			stuClassInfoIndex = rowIndex;
		},
		onLoadSuccess:function(){
			stuClassInfoRow = null;
			stuClassInfoIndex = null;
		}
	});
	
	//查询
	var stuClassInfoSearch = function(){
		var studentId = $('#selectRowStudentId',$this).val();
		var url = 'stuClass/queryStuClass.do';
		var content = {'student.studentId':studentId};
		var result = syncCallService(url,content);
		if(result.isSuccess){
			var  data = result.data;
			var datagridData = eval("("+data.datagridData+")");
			$(stuClassInfoList).datagrid('loadData',datagridData);
		}else{
			$.messager.alert('提示',result.message,'error');
		}
	};
	
	//初始化combobox
	var oneToManyCombobox = function(courseId){
		$('#lessonSchool',oneToManyDialog).combobox({
			  width:200,
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
			  width:200,
			  valueField:'classId',
			  textField:'className',
			  onSelect:function(record){
				  classInfoInit(record.classId);
				  $(classInfoPanel).panel('open');
				  setLessons();
				  $('#className',oneToManyForm).val(record.className);
			  }
		  });
		$('#selectSchool',oneToManyDialog).combobox({
			  width:200,
			  data:TRN.getSelfSchoolList(),
			  valueField:'schoolId',
			  textField:'schoolName'
		  });
		$('#user',oneToManyDialog).combobox({
			  width:200,
			  data:TRN.getEmployeeList(),
			  valueField:'userId',
			  textField:'userName'
		});
	};
	
	//定义编辑框
	$(oneToManyDialog).dialog({  
	    title: '编辑学员选班',  
	    width:750,
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
	
	//编辑
	var stuClassInfoUpadate = function(){
		if(stuClassInfoRow==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		}
		$(oneToManyForm).form('clear');
		$(oneToManyForm).form('load',stuClassInfoRow);
		oneToManyCombobox(stuClassInfoRow.courseId);
		$('#class',oneToManyDialog).combobox({
			data:CSIT.ClearCombobxData
		});
		$('#lessonSchool',oneToManyDialog).combobox('setValue',stuClassInfoRow.lessonSchoolId);
		$('#lessonSchool',oneToManyDialog).combobox('select',stuClassInfoRow.lessonSchoolId);
		$('#class',oneToManyDialog).combobox('setValue',stuClassInfoRow.classId);
		$('#class',oneToManyDialog).combobox('select',stuClassInfoRow.classId);
		$('#selectSchool',oneToManyDialog).combobox('setValue',stuClassInfoRow.selectSchoolId);
		$('#user',oneToManyDialog).combobox('setValue',stuClassInfoRow.userId);
		$('#showProgress',oneToManyDialog).html('<span style="color:red">'+
				stuClassInfoRow.courseProgress+'</span>/'+stuClassInfoRow.lessons);
		$(oneToManyDialog).dialog('open');
	};
	
	//删除
	var stuClassInfoDelete = function(){
		if(stuClassInfoRow==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		}
		$.messager.confirm('确认', '确定删除该记录吗?',function(r){
			if(r){
				var url = "stuClass/deleteStuClass.do";
				var content = {stuClassId:stuClassInfoRow.stuClassId};
				$.post(url,content,
					function(result){
						if(result.isSuccess){
							stuClassInfoSearch();
						}else{
							$.messager.alert('提示',result.message,"error");
						}
				},'json');
			}
		});
	};
	
	$(classInfoPanel).panel({
	    closed: true,
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
	
	//浏览对话框
	$(browseDialog).dialog({  
	    title: '浏览',  
	    width:535,
	    height:height-10,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    toolbar:[{
			text:'退出',
			iconCls:'icon-cancel',
			handler:function(){
				$(browseDialog).dialog('close');
			}
		}]
	});
	
	$(stuClassPanel).panel({
	    width:500,  
	    height:300,  
	    title: '基本信息'
	});
	
	$(lessonPanel).panel({
	    width:500,  
	    height:300,  
	    title: '上课信息'
	});
	
	var stuClassInfoBrowse = function(){
		if(stuClassInfoRow==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		}
		
		$(browseForm).form('clear');
		$(browseForm).form('load',stuClassInfoRow);
		
		var scStatus = ['正常','插班','转班','休学','退学','弃学'];
		$('#scStatus',stuClassPanel).val(scStatus[stuClassInfoRow.scStatus-1]);
		$('#showProgress',lessonPanel).html(getBar(stuClassInfoRow.courseProgress,stuClassInfoRow.lessons));
		
		$(browseDialog).dialog('open');
	};
  };
})(jQuery);  