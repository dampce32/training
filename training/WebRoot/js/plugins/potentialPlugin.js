// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.potentialInit = function() {
	  var $this = $(this);
	  var id = $(this).attr('id');
	  var width = $(document.body).width();
	  var height = $(document.body).height();
	  
	  var queryContent = $('#queryContent',$this);
	   var queryReplyContent = $('#queryReplyContent',$this);
	  var searchBtn = $('#searchBtn',$this);
	  var searchReplyBtn = $('#searchReplyBtn',$this);
	  var mulSearchBtn = $('#mulSearchBtn',$this);
	  var changeSearch = false;
	  var replyList = $('#replyList',replyDialog);
	  var viewList = $('#viewList',$this);
	  var selectRow = null;
	  var selectIndex = null;
	  var selectRowReply = null;
	  var selectIndexReply = null;
	  var pager = $('#pager',$this);
	  var pagerReply = $('#pagerReply',$this);
	  var pageNumber = 1;
	  var pageSize = 10;
	  
	  var editDialog = $('#editDialog',$this);
	  var editStuDialog = $('#editStuDialog',$this);
	  var editReplyDialog = $('#editReplyDialog',$this);
	  var editForm = $('#editForm',$this);
	  var editStuForm = $('#editStuForm',$this);
	  var replyDialog = $('#replyDialog',$this);
	  var editReplyForm = $('#editReplyForm',$this);
	  $('#schoolSearch',queryContent).combobox({
		  width:150,
		  data:TRN.getSelfSchoolList(),
		  valueField:'schoolId',
		  textField:'schoolName'
	  });
	  $('#courseSearch',queryContent).combobox({
		  width:150,
		  data:TRN.getPotCourseList(),
		  valueField:'potCourseId',
		  textField:'courseName'
	  });
	  $('#potentialStuStatusSearch',queryReplyContent).combobox({
		  width:150,
		  data:TRN.getPotentialStuStatusList(),
		  valueField:'potentialStuStatusId',
		  textField:'potentialStuStatusName'
	  });
	//加载列表
	  $(viewList).datagrid({
		method:"POST",
		singleSelect:true,
		nowrap:true,
		striped: true,
		collapsible:true,
		rownumbers:true,
		fit:true,
		toolbar:[	
					{text:'添加',iconCls:'icon-add',handler:function(){onAdd()}},'-',
					{text:'修改',iconCls:'icon-edit',handler:function(){onUpdate()}},'-',
					{text:'删除',iconCls:'icon-remove',handler:function(){onDelete()}},'-',
					{text:'回访',iconCls:'icon-undo',handler:function(){onReply()}},'-',
					{text:'转正',iconCls:'icon-redo',handler:function(){onStu()}}
				],
		columns:[[
			{field:'potentialName',title:'姓名',width:100,align:"center"},
			{field:'potentialStuStatusName',title:'客户状态',width:100,align:"center"},
			{field:'isStu',title:'是否转正',width:100,align:"center",
						formatter: function(value,row,index){
							if (value==0){
								return '未转正';
							} else if (value==1){
								return '已转正';
							}
			}},
			{field:'schoolName',title:'校区',width:100,align:"center"},
			{field:'courseName',title:'咨询课程',width:100,align:"center"},
			{field:'potentialDate',title:'咨询日期',width:100,align:"center"},
			{field:'reCount',title:'回访次数',width:100,align:"center"},
			{field:'nextReplyDate',title:'下次回访日期',width:100,align:"center"},
			{field:'userName',title:'咨询顾问',width:100,align:"center"}
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
	    title: '编辑咨询表',  
	    width:780,
	    height:450,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    toolbar:[
	            {id:'save'+id,text:'保存',iconCls:'icon-save',handler:function(){onSave();}},'-',
				{id:'add'+id,text:'新增',iconCls:'icon-add',handler:function(){onAdd();}},'-',
				{id:'delete'+id,text:'删除',iconCls:'icon-remove',handler:function(){onDelete();}},'-',
				{id:'pre'+id,text:'上一笔',iconCls:'icon-left',handler:function(){onOpenIndex(-1);}},'-',
				{id:'next'+id,text:'下一笔',iconCls:'icon-right',handler:function(){onOpenIndex(1);}},'-',
				{text:'退出',iconCls:'icon-cancel',handler:function(){
						$(editDialog).dialog('close');
					}
				}
	   ]
	});
	//新增时，按钮的状态
	var addBtnStatus = function(){
		$('#delete'+id).linkbutton('disable');
		$('#pre'+id).linkbutton('disable');
		$('#next'+id).linkbutton('disable');
	}
	//更新时,按钮的状态
	var updateBtnStatus = function(){
		$('#delete'+id).linkbutton('enable');
	}
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
	}
	//初始化选择条件转正使用
	var initComboboxStu = function(){
		//所在校区
		$('#school',editStuDialog).combobox({
			valueField:'schoolId',
			textField:'schoolName',
			width:250,
			data:TRN.getSelfSchoolList()
		});
		//获知方式
		$('#media',editStuDialog).combobox({
		  width:250,
		  data:TRN.getMediaList(),
		  valueField:'mediaId',
		  textField:'mediaName'
	  });
		//学习顾问
		$('#user',editStuDialog).combobox({
		  width:250,
		  data:TRN.getEmployeeList(),
		  valueField:'userId',
		  textField:'userName'
	  });
	}
	//添加
	var onAdd = function(){
		$(viewList).datagrid('unselectAll');
		selectIndex = null;
		selectRow = null;
		$(editForm).form('clear');
		addBtnStatus();
		initCombobox();
		$('#sex',editDialog).combobox('setValue',1);
		$(editDialog).dialog('open');
	}
	//保存前的赋值操作
	var setValue = function(){
		//姓名
		var potentialName = $.trim($('#potentialName',editForm).val());
		if(''==potentialName){
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
		//咨询日期
		var potentialDate = $.trim($('#potentialDate',editForm).val());
		if(''==potentialDate){
			$.messager.alert('提示','请填写咨询日期','warning');
			return false;
		}
		//咨询课程
		var potCourseId = $('#potCourse',editDialog).combobox('getValue') ;
		if(''==potCourseId){
			$.messager.alert('提示','请选择咨询课程','warning');
			return false;
		}
		if(isNaN(parseInt(potCourseId))){
			$.messager.alert('提示','请选择提供选择的咨询课程','warning');
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
		//状态
		var potentialStuStatusId = $('#potentialStuStatus',editDialog).combobox('getValue') ;
		if(''==potentialStuStatusId){
			$.messager.alert('提示','请选择状态','warning');
			return false;
		}
		if(isNaN(parseInt(potentialStuStatusId))){
			$.messager.alert('提示','请选择提供选择的状态','warning');
			return false;
		}
		//咨询顾问
		var userId = $('#user',editDialog).combobox('getValue') ;
		if(''==userId){
			$.messager.alert('提示','请选择咨询顾问','warning');
			return false;
		}
		if(isNaN(parseInt(userId))){
			$.messager.alert('提示','请选择提供选择的咨询顾问','warning');
			return false;
		}
		return true;
	}
	//保存
	var onSave = function(){
		$(editForm).form('submit',{
			url: 'customerService/savePotential.do',
			onSubmit: function(){
				return setValue();
			},
			success: function(data){
				var result = eval('('+data+')');
				if(result.isSuccess){
					var fn = function(){
						var potentialId = $.trim($('#potentialId',editForm).val());
						if(potentialId==''){//新增
							var  data = result.data;
							$('#potentialId',editForm).val(data.potentialId);
							$('#delete'+id).linkbutton('enable');
						}
					}
					$.messager.alert('提示','保存成功','info',fn);
				}else{
					$.messager.alert('提示',result.message,"error");
				}
			}
		});
	}
	//修改
	var onUpdate = function(){
		if(selectRow==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		}
		onOpen();
		$(editDialog).dialog('open');
	 }
	//打开
	var onOpen = function(){
		$(editForm).form('clear');
		var preDisable = false;
		var nextDisable = false;
		if(selectIndex==null||selectIndex==0){
			preDisable = true;
		}
		var rows = $(viewList).datagrid('getRows');
		if(selectIndex==null||selectIndex==rows.length-1){
			nextDisable = true;
		}
		if(preDisable){
			$('#pre'+id).linkbutton('disable');
		}else{
			$('#pre'+id).linkbutton('enable');
		}
		if(nextDisable){
			$('#next'+id).linkbutton('disable');
		}else{
			$('#next'+id).linkbutton('enable');
		}
		$(editForm).form('load',selectRow);
		initCombobox();
		$('#school',editDialog).combobox('setValue',selectRow.schoolId);
		$('#potCourse',editDialog).combobox('setValue',selectRow.potCourseId);
		$('#media',editDialog).combobox('setValue',selectRow.mediaId);
		$('#user',editDialog).combobox('setValue',selectRow.userId);
		$('#potentialStuStatus',editDialog).combobox('setValue',selectRow.potentialStuStatusId);
		updateBtnStatus();
	}
	//上下一笔
	var onOpenIndex = function(index){
		var rows = $(viewList).datagrid('getRows');
		selectIndex = selectIndex + index;
		selectRow = rows[selectIndex];
		onOpen(selectRow.userId);
		//界面选中
		$(viewList).datagrid('unselectAll');
		$(viewList).datagrid('selectRow',selectIndex);
	}
	//删除
	var onDelete = function(){
		if(selectRow==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		}
		$.messager.confirm("提示","确定要删除记录?",function(t){ 
			if(t){
				var url = 'customerService/deletePotential.do';
				var content ={potentialId:selectRow.potentialId};
				asyncCallService(url,content,function(result){
					if(result.isSuccess){
						var fn = function(){
							selectRow = null;
							selectIndex = null;
							$(editDialog).dialog('close');
							search(true);
						}
						$.messager.alert('提示','删除成功','info',fn);
					}else{
						$.messager.alert('提示',result.message,'error');
					}
				});
			}
		});
	}
	//回访
	var onReply = function() {
		if (selectRow == null) {
			$.messager.alert("提示", "请选择数据行", "warning");
			return;
		}
		$(replyList).datagrid('loadData', { total: 0, rows: [] });
		$(replyDialog).dialog('open');
	}
	$(replyList).datagrid( {
			method:"POST",
			singleSelect:true,
			nowrap:true,
			striped: true,
			collapsible:true,
			rownumbers:true,
			fit:true,
			toolbar:[	
						{text:'添加',iconCls:'icon-add',handler:function(){onAddReply()}},'-',
						{text:'修改',iconCls:'icon-edit',handler:function(){onUpdateReply()}},'-',
						{text:'删除',iconCls:'icon-remove',handler:function(){onDeleteReply()}},'-',
						{text:'退出',iconCls:'icon-cancel',handler:function(){$(replyDialog).dialog('close')}}
					],
			columns : [ [ {field:'ck',checkbox:true},
						  {field : 'potentialStuStatusName',title : '结果',width : 100,align : "center"}, 
						  {field : 'userName',title : '回访人',width : 100,align : "center"}, 
						  {field : 'replyDate',title : '回访日期',width : 100,align : 'center'}, 
						  {field : 'content',title : '回访内容',width : 300,align : 'center'}, 
						  {field : 'nextReplyDate',title : '下次回访日期',width : 100,align : "center"}
					 ] ],
			onClickRow:function(rowIndex, rowData){
			selectRowReply = rowData;
			selectIndexReply = rowIndex;
			},
			onDblClickRow:function(rowIndex,rowData){
				onUpdateReply();
			},
			onLoadSuccess:function(){
				selectRowReply = null;
		 		selectIndexReply = null;
				pageNumber = 1;
			}
		});
	//分页条
	$(pagerReply).pagination({   
	    onSelectPage: function(page, rows){
			pageNumber = page;
			pageSize = rows;
			searchReply();
	    }
	});
	//回访记录
	$(replyDialog).dialog({  
	    title: '回访记录',  
	    width:900,
	    height:500,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false
	});
	//编辑框
	$(editReplyDialog).dialog({  
	    title: '编辑回访表',  
	    width:450,
	    height:300,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    toolbar:[
	            {id:'save'+id,text:'保存',iconCls:'icon-save',handler:function(){onSaveReply();}},'-',
				{text:'退出',iconCls:'icon-cancel',handler:function(){
						$(editReplyDialog).dialog('close');
					}
				}
	   ]
	});
	//添加回访信息
	var onAddReply = function(){
		$(editReplyForm).form('clear');
		//状态
		$('#potentialStuStatus',editReplyDialog).combobox({
		  width:250,
		  data:TRN.getPotentialStuStatusList(),
		  valueField:'potentialStuStatusId',
		  textField:'potentialStuStatusName'
	  });
		$(editReplyDialog).dialog('open');
	}
	//保存前的赋值操作
	var setValueReply = function(){
		//回访日期
		var replyDate = $.trim($('#replyDate',editReplyForm).val());
		if(''==replyDate){
			$.messager.alert('提示','请填写日期','warning');
			return false;
		}
		//内容
		var content = $.trim($('#content',editReplyForm).val());
		if(''==content){
			$.messager.alert('提示','请填写内容','warning');
			return false;
		}
		//结果
		var potentialStuStatusId = $('#potentialStuStatus',editReplyForm).combobox('getValue') ;
		if(''==potentialStuStatusId){
			$.messager.alert('提示','请选择状态','warning');
			return false;
		}
		if(isNaN(parseInt(potentialStuStatusId))){
			$.messager.alert('提示','请选择提供选择的状态','warning');
			return false;
		}
		$('#potential',editReplyForm).val(selectRow.potentialId);
		return true;
	}
	var onSaveReply = function(){
		$(editReplyForm).form('submit',{
			url: 'customerService/saveReply.do',
			onSubmit: function(){
				return setValueReply();
			},
			success:function(data){
				var result = eval('('+data+')');
				if(result.isSuccess){
					var fn = function(){
						searchReply(true);
						$(editReplyDialog).dialog('close');
					}
					$.messager.alert('提示','保存成功','info',fn);
				}else{
					$.messager.alert('提示',result.message,"warning");
				}
			}
		});
	}
	//修改回访信息
	var onUpdateReply = function(){
		if(selectRowReply==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		}
		$(editReplyForm).form('clear');
		$('#potentialStuStatus',editReplyDialog).combobox({
		  width:250,
		  data:TRN.getPotentialStuStatusList(),
		  valueField:'potentialStuStatusId',
		  textField:'potentialStuStatusName'
	 	});
		$('#potentialStuStatus',editReplyDialog).combobox('setValue',selectRowReply.potentialStuStatusId);
		$(editReplyForm).form('load',selectRowReply);
		$(editReplyDialog).dialog('open');
	}
	//删除回访信息
	var onDeleteReply = function(){
		if(selectRowReply==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		}
		$.messager.confirm("提示","确定要删除记录?",function(t){ 
			if(t){
				var url = 'customerService/deleteReply.do';
				var content ={replyId:selectRowReply.replyId};
				asyncCallService(url,content,function(result){
					if(result.isSuccess){
						var fn = function(){
							selectRowReply = null;
							selectIndexReply = null;
							searchReply(true);
						}
						$.messager.alert('提示','删除成功','info',fn);
					}else{
						$.messager.alert('提示',result.message,'error');
					}
				});
			}
		});
	}
	//查询
	var search = function(flag){
		var potentialName = $('#nameSearch',queryContent).val();
		var schoolId = $('#schoolSearch',queryContent).combobox('getValue');
		if(schoolId==''){
			schoolId = -1 ;
		}
		var courseId = $('#courseSearch',queryContent).combobox('getValue');
		var content = {'school.schoolId':schoolId,'potCourse.potCourseId':courseId,potentialName:potentialName,page:pageNumber,rows:pageSize};
		//取得列表信息
		var url = 'customerService/queryPotential.do';
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
	}
	//统计总数
	var getTotal = function(content){
		var url = "customerService/getTotalCountPotential.do";
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
		})
	}	
	//查询
	$(searchBtn).click(function(){
		search(true);
	})
	//查询回访记录
	var searchReply = function(flag){
		var potentialStuStatusId = $('#potentialStuStatusSearch',queryReplyContent).combobox('getValue');
		var replyDate = $('#replyDateSearch',queryReplyContent).val();
		var nextReplyDate = $('#nextReplyDateSearch',queryReplyContent).val();
		var content = {'potentialStuStatus.potentialStuStatusId':potentialStuStatusId,replyDate:replyDate,nextReplyDate:nextReplyDate,'potential.potentialId':selectRow.potentialId,page:pageNumber,rows:pageSize};
		//取得列表信息
		var url = 'customerService/queryReply.do';
		var result = syncCallService(url,content);
		if(result.isSuccess){
			var  data = result.data;
			var datagridData = eval("("+data.datagridData+")");
			$(replyList).datagrid('loadData',datagridData);
			//需要重新重新分页信息
			if(flag){
				getTotalReply(content);
			}
		}else{
			$.messager.alert('提示',result.message,'error');
		}
	}
	//统计总数回访记录
	var getTotalReply = function(content){
		var url = "customerService/getTotalCountReply.do";
		asyncCallService(url,content,
		function(result){
			if(result.isSuccess){
				var data = result.data;
				$(pagerReply).pagination({  
					pageNumber:1,
					total:data.total
				});
			}else{
				$.messager.alert('提示',result.message,"error");
			}
		})
	}	
	//查询
	$(searchReplyBtn).click(function(){
		searchReply(true);
	})
	//转正编辑框
	$(editStuDialog).dialog({  
	    title: '编辑转正学员',  
	    width:780,
	    height:480,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    toolbar:[
	            {id:'save'+id,text:'保存',iconCls:'icon-save',handler:function(){onSaveStu();}},'-',
				{text:'退出',iconCls:'icon-cancel',handler:function(){
						$(editStuDialog).dialog('close');
					}
				}
	   ]
	});
	//转正
	var onStu = function() {
		if (selectRow == null) {
			$.messager.alert("提示", "请选择数据行", "warning");
			return;
		}
		if(selectRow.isStu==1){
			$.messager.alert("提示", "该潜在学员已转正，此操作无效", "warning");
			return;
		}
		initStu();
		$(editStuDialog).dialog('open');
	}
	//打开
	var initStu = function(){
		$(editStuForm).form('clear');
		initComboboxStu();
		$('#studentName',editStuDialog).val(selectRow.potentialName);
		$('#appellation',editStuDialog).val(selectRow.appellation);
		$('#tel',editStuDialog).val(selectRow.tel);
		$('#mobileTel',editStuDialog).val(selectRow.mobileTel);
		$('#tel1',editStuDialog).val(selectRow.tel1);
		$('#qq',editStuDialog).val(selectRow.qq);
		$('#email',editStuDialog).val(selectRow.email);
		$('#school',editStuDialog).combobox('setValue',selectRow.schoolId);
		$('#media',editStuDialog).combobox('setValue',selectRow.mediaId);
		$('#user',editStuDialog).combobox('setValue',selectRow.userId);
		$('#sex',editStuDialog).combobox('setValue',selectRow.sex);
		$('#studentType',editStuDialog).combobox('setValue',0);
		$('#potentialId',editStuForm).val(selectRow.potentialId);
	}
	//保存前的赋值操作
	var setValueStu = function(){
		//姓名
		var studentName = $.trim($('#studentName',editStuForm).val());
		if(''==studentName){
			$.messager.alert('提示','请填写姓名','warning');
			return false;
		}
		//性别
		var sex = $('#sex',editStuForm).combobox('getValue') ;
		if(''==sex){
			$.messager.alert('提示','请选择性别','warning');
			return false;
		}
		if(isNaN(parseInt(sex))){
			$.messager.alert('提示','请选择提供选择的性别','warning');
			return false;
		}
		//校区
		var schoolId = $('#school',editStuForm).combobox('getValue') ;
		if(''==schoolId){
			$.messager.alert('提示','请选择校区','warning');
			return false;
		}
		if(isNaN(parseInt(schoolId))){
			$.messager.alert('提示','请选择提供选择的校区','warning');
			return false;
		}
		//报名日期
		var enrollDate = $.trim($('#enrollDate',editStuForm).val());
		if(''==enrollDate){
			$.messager.alert('提示','请填写报名日期','warning');
			return false;
		}
		//电话号码
		var tel = $.trim($('#tel',editStuForm).val());
		if(''==tel){
			$.messager.alert('提示','请填写电话号码','warning');
			return false;
		}
		//手机号码
		var mobileTel = $.trim($('#mobileTel',editStuForm).val());
		if(''==mobileTel){
			$.messager.alert('提示','请填写手机号码','warning');
			return false;
		}
		//获知方式
		var mediaId = $('#media',editStuForm).combobox('getValue') ;
		if(''==mediaId){
			$.messager.alert('提示','请选择获知方式','warning');
			return false;
		}
		if(isNaN(parseInt(mediaId))){
			$.messager.alert('提示','请选择提供选择的获知方式','warning');
			return false;
		}
		//学习顾问
		var userId = $('#user',editStuForm).combobox('getValue') ;
		if(''==userId){
			$.messager.alert('提示','请选择学习顾问','warning');
			return false;
		}
		if(isNaN(parseInt(userId))){
			$.messager.alert('提示','请选择提供选择的学习顾问','warning');
			return false;
		}
		return true;
	}
	//保存
	var onSaveStu = function(){
		$(editStuForm).form('submit',{
			url: 'customerService/saveStudent.do',
			onSubmit: function(){
				return setValueStu();
			},
			success: function(data){
				var result = eval('('+data+')');
				if(result.isSuccess){
					var fn = function(){
						var studentId = $.trim($('#studentId',editStuForm).val());
						if(studentId==''){//新增
							var  data = result.data;
							$('#studentId',editStuForm).val(data.studentId);
						}
					}
					$.messager.alert('提示','保存成功','info',fn);
				}else{
					$.messager.alert('提示',result.message,"error");
				}
			}
		});
	}
	//添加
	var onAddStu = function(){
		$(viewList).datagrid('unselectAll');
		selectIndex = null;
		selectRow = null;
		$(editStuForm).form('clear');
		initComboboxStu();
		$('#sex',editStuDialog).combobox('setValue',1);
		$('#studentType',editStuDialog).combobox('setValue',0);
		$(editStuDialog).dialog('open');
	}
  }
})(jQuery);   