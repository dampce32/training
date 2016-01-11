// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.changeInit = function() {
	  var $this = $(this);
	  var id = $(this).attr('id');
	  var height = $(document.body).height();  
	  
	  var queryContent = $('#queryContent',$this);
	  var searchBtn = $('#searchBtn',$this);
	  
	  var viewList = $('#viewList',$this);
	  var selectRow = null;
	  
	  var pager = $('#pager',$this);
	  var pageNumber = 1;
	  var pageSize = 10;
	  
	  var editDialog = $('#editDialog',$this);
	  var editForm = $('#editForm',$this);
	  
	  var classInfoPanel = $('#classInfoPanel',editDialog);
	  var classInfoForm = $('#classInfoForm',editDialog);
	  
	  $('#studentSearch',queryContent).combobox({
			width:150,
			data:TRN.getStudentList(),
			valueField:'studentId',
			textField:'studentName'
	  });
	  $('#schoolSearch',queryContent).combobox({
		  width:150,
		  data:TRN.getSelfSchoolList(),
		  valueField:'schoolId',
		  textField:'schoolName'
	  });
	  
	  var statusArray = [ 
	                     {"value" : 3,"text" : "转班"}, 
	                     {"value" : 4,"text" : "休学"}, 
	                     {"value" : 5,"text" : "退学"},
	                     {"value" : 6,"text" : "弃学"},
	                     {"value" : 7,"text" : "所有"}
	         		                   ];
	  $('#typeSearch',queryContent).combobox({
		  width:150,
		  data:statusArray
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
					{text:'添加',iconCls:'icon-add',handler:function(){onAdd();}},'-',
					{text:'修改',iconCls:'icon-edit',handler:function(){onUpdate();}},'-',
					{text:'删除',iconCls:'icon-remove',handler:function(){onDelete();}}
				],
		columns:[[
			{field:'studentId',title:'学员编号',width:100,align:"center"},
			{field:'studentName',title:'学员姓名',width:100,align:"center"},
			{field:'changeType',title:'类型',width:100,align:"center",
				formatter: function(value,row,index){
			    	if (value=="3"){
						return '换班';
					} else if (value=="4"){
						return '休学';
					}else if (value=="5"){
						return '退学';
					}else if (value=="6"){
						return '弃学';
					};
		    	}},
			{field:'className',title:'班级名称',width:100,align:"center"},
			{field:'date',title:'办理时间',width:100,align:"center"},
			{field:'newStuClassName',title:'新班号',width:100,align:"center"},
			{field:'note',title:'详情',width:100,align:"center"},
			{field:'userName',title:'经办人',width:100,align:"center"}
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
	//编辑框
	$(editDialog).dialog({  
	    title: '编辑异动单',  
	    width:780,
	    height:400,
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
	
	//分页条
	$(pager).pagination({   
	    onSelectPage: function(page, rows){
			pageNumber = page;
			pageSize = rows;
			search();
	    }
	});
	
	//查询
	$(searchBtn).click(function(){
		search(true);
	});
	//查询
	var search = function(flag){
		var studentId = $('#studentSearch',queryContent).combobox('getValue');
		var studentBoolean=isNumber(studentId);
		if(studentBoolean==false){
			$.messager.alert('提示','请选择已有的学员','error');
			return;
		}
		var schoolId = $('#schoolSearch',queryContent).combobox('getValue');
		var schoolBoolean=isNumber(schoolId);
		if(schoolBoolean==false){
			$.messager.alert('提示','请选择已有的校区','error');
			return;
		}
		var type = $('#typeSearch',queryContent).combobox('getValue');
		var typeBoolean=isNumber(type);
		if(typeBoolean==false){
			$.messager.alert('提示','请选择已有的类型','error');
			return;
		}
		
		var content = {'student.studentId':studentId,'student.school.schoolId':schoolId,'changeType':type,page:pageNumber,rows:pageSize};
		//取得列表信息
		var url = 'academic/queryChange.do';
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
	
	//判断是否是数字
	function isNumber(s)  {   
		var strP=/^\d+$/;
		if(''==s||strP.test(s)) { 
			return true;
		}else{
			return false;
		}
	}
	
	var initCombobox = function(){
		$('#student',editForm).combobox({
			width:250,
			data:TRN.getStudentList(),
			valueField:'studentId',
			textField:'studentName',
			onSelect:function(record){
				 $('#stuClass',editForm).combobox({
					  disabled:false,
					  valueField:'stuClassId',
					  textField:'className',
					  url:'stuClass/querySelectedClassComboboxStuClass.do?student.studentId='+record.studentId
				  });
			}
		});
		$('#stuClass',editForm).combobox({
			width:250,
			valueField:'stuClassId',
			textField:'stuClassName',
			onSelect:function(record){
				  classInfoInit(record);
				  $('#showProgress',editForm).html(setLessons(record.lessons,record.courseProgress));
				  
			}
		});
		$('#user',editForm).combobox({
			width:250,
			data:TRN.getEmployeeList(),
			valueField:'userId',
			textField:'userName'
		});
	};
	//初始化课程框
	var  classInfoInit = function(record){
		$(classInfoForm).form('clear');
		var url = 'academic/queryOneClass.do';
	    var content = {studentId:'',classId:record.classId};
	    var result = syncCallService(url,content);
	    if(result.isSuccess){
	    	var classInfo = eval('('+result.data.classInfo+')');
	    	$(classInfoForm).form('load',classInfo);
	    	$('#intoAccount',editForm).val(classInfo.unitPrice*(record.lessons-record.courseProgress));
	    	$('#schoolId',editForm).val(classInfo.schoolId);
	    	$('#showStuCount',classInfoForm).html(setLessons(classInfo.planCount,classInfo.stuCount));
	    	$('#showProgress',classInfoForm).html(setLessons(classInfo.lessons,classInfo.courseProgress));
	    }else{
		    $.messager.alert('提示',result.message,"error");
	    }
	};
	//设置课程的进度条
	var setLessons = function(lessons,courseProgress){
		 var bar = '<div style="width:80px;height:12px;background:#fff;border:1px solid #ccc;text-align:left;float:left;">' +
			'<div style="width:' +courseProgress*100/lessons + '%;height:100%;background:blue"></div>'+
			'</div>';
		var number = '<span style="width:50px;margin-left:5px;text-align:left">'+
		'<span style="color: red;">'+courseProgress+'</span>/'+lessons+
		'</span>';
		alert(lessons+','+courseProgress);
		$('#changelessons',editForm).val(lessons-courseProgress);
		return bar+number;
	};
	//点击异动类型
	$('#changeType',editForm).click(function(){
		$('#newStuClass',editForm).combobox({
			width:250,
			valueField:'classId',
			textField:'className',
			url:'academic/queryComboboxClass.do?school.schoolId='+$('#schoolId',editForm).val()
		});
		if($('#changeType:checked',editForm).val()=='3'){
			$('#newStuClass',editForm).combobox('setValue','');
			$('#newStuClass',editForm).combobox('setText','');
			$('#newClass',editForm).show();
		}else{
			$('#newStuClass',editForm).combobox('setText','');
			$('#newClass',editForm).hide();
		}
		if($('#changeType:checked',editForm).val()=='4'){
			$('#expireDate',editForm).val('');
			$('#expireDateTR',editForm).show();
		}else{
			$('#expireDate',editForm).val('');
			$('#expireDateTR',editForm).hide();
		}
		
	});
	//添加
	var onAdd = function(){
		$(editForm).form('clear');
		$('#newClass',editForm).hide();
		$('#expireDateTR',editForm).hide();
		initCombobox();
		$('#showProgress',editForm).html('<span style="color:red">'+
				0+'</span>/'+0);
		$(editDialog).dialog('open');
	};
	//保存前的赋值操作
	var setValue = function(){
		var changeId=$('#changeId',editForm).val();
		var studentId = $('#student',editForm).combobox('getValue');
		var studentBoolean=isNumber(studentId);
		if(studentId==''||studentBoolean==false){
			$.messager.alert('提示','请选择已有的学员','error');
			return false;
		}
		var stuClassId = $('#stuClass',editForm).combobox('getValue');
		var studentBoolean=isNumber(stuClassId);
		if(changeId==''&&(stuClassId==''||studentBoolean==false)){
			$.messager.alert('提示','请选择已有的班级','error');
			return false;
		}
		var changeType=$.trim($('#changeType:checked',editForm).val());
		if(''==changeType){
			$.messager.alert('提示','请选择异动类型','warning');
			return false;
		}
		var intoAccount=$.trim($('#intoAccount',editForm).val());
		if(''==intoAccount){
			$.messager.alert('提示','请填写返还金额','warning');
			return false;
		}
		var date=$.trim($('#date',editForm).val());
		if(''==date){
			$.messager.alert('提示','请填写办理日期','warning');
			return false;
		}
		
		if(changeType==3){
			var newStuClassId = $('#newStuClass',editForm).combobox('getValue');
			var newStuClassBoolean=isNumber(newStuClassId);
			if(changeId==''&&(newStuClassId==''||newStuClassBoolean==false)){
				$.messager.alert('提示','请选择已有的新转入班级','error');
				return false;
			}
		}else if(changeType==4){
			var expireDate=$.trim($('#expireDate',editForm).val());
			if(''==expireDate){
				$.messager.alert('提示','请填写休学到期日期','warning');
				return false;
			}
		}
		
		var userId = $('#user',editForm).combobox('getValue');
		var userBoolean=isNumber(userId);
		if(userId==''||userBoolean==false){
			$.messager.alert('提示','请选择已有的经办人','warning');
			return false;
		}
		return true;
	};
	//保存
	var onSave = function(){
		$(editForm).form('submit',{
			url: 'academic/saveChange.do',
			onSubmit: function(){
				return setValue();
			},
			success: function(data){
				var result = eval('('+data+')');
				if(result.isSuccess){
					var fn = function(){
						var changeId = $.trim($('#changeId',editForm).val());
						if(changeId==''){//新增
							var  data = result.data;
							$('#changeId',editForm).val(data.leaveId);
						}
					};
					$.messager.alert('提示','保存成功','info',fn);
				}else{
					$.messager.alert('提示',result.message,"error");
				}
			}
		});
	};
	
	//统计总数
	var getTotal = function(content){
		var url = "academic/getTotalCountChange.do";
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
	
	//修改
	var onUpdate = function(){
		if(selectRow==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		}
		initCombobox();
		classInfoInit(selectRow.classId);
		setLessons();
		$('#newStuClass',editForm).combobox({
			width:250,
			valueField:'classId',
			textField:'className',
			url:'academic/queryComboboxClass.do?school.schoolId='+$('#schoolId',editForm).val()
		});
		$(editForm).form('load',selectRow);
		$('#changelessons',editForm).val(selectRow.lessons);
		$('#student',editForm).combobox({disabled:true});
		$('#stuClass',editForm).combobox({disabled:true});
		$('#user',editForm).combobox('setText',selectRow.userName);
		$('#student',editForm).combobox('setText',selectRow.studentName);
		$('#stuClass',editForm).combobox('setText',selectRow.className);
		$('#user',editForm).combobox('setValue',selectRow.userId);
		$('#student',editForm).combobox('setValue',selectRow.studentId);
		if($('#changeType:checked',editForm).val()=='3'){
			$('#newStuClass',editForm).combobox('setValue',selectRow.newStuClassId);
			$('#newStuClass',editForm).combobox('setText',selectRow.newStuClassName);
			$('#newClass',editForm).show();
		}else{
			$('#newClass',editForm).hide();
		}
		if($('#changeType:checked',editForm).val()=='4'){
			$('#expireDateTR',editForm).show();
		}else{
			$('#expireDateTR',editForm).hide();
		}
		$(editDialog).dialog('open');
	};
	
	$(classInfoPanel).panel({
	    width:350,  
	    height:230,  
	    title: '班级信息'
	});  
	
	//删除
	var onDelete = function(){
		if(selectRow==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		}
		$.messager.confirm('确认', '确定删除该记录吗?', function(r){
			if(r){
				var url = "academic/deleteChange.do";
				var content = {changeId:selectRow.changeId};
				$.post(url,content,
					function(result){
						if(result.isSuccess){
							search(true);
						}else{
							$.messager.alert('提示',result.message,"error");
						}
				},'json');
			}
		});
	};
  };
})(jQuery);   