// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.reminderInit = function() {
	  var $this = $(this);
	  var id = $(this).attr('id');
	  var width = $(document.body).width();
	  var height = $(document.body).height();
	  
	  var queryContent = $('#queryContent',$this);
	  var searchBtn = $('#searchBtn',$this);
	 
	  var editDialog = $('#editDialog',$this);
	  var editForm = $('#editForm',editDialog);
	  
	  var viewList =  $('#viewList',$this);
	  var selectRow = null;
	  var selectIndex = null;
	  
	  var pager = $('#pager',$this);
	  var pageNumber = 1;
	  var pageSize = 10;
	  
	  var delReminderDetailIdArray = null;
	  
	  //列表
	  $(viewList).datagrid({
		  fit:true,
		  nowrap:true,
		  striped: true,
		  collapsible:true,
		  rownumbers:true,
		  columns:[[
		        {field:'ck',title:'选择',checkbox:true},
		        {field:'status',title:'状态',width:60,align:"center",
					formatter: function(value,row,index){
						if (value==0){
							return '<img src="style/v1/icons/warn.png"/>';
						}else if (value==1) {
							return '<img src="style/v1/icons/info.png"/>';
						}
					}},
				{field:'title',title:'标题',width:150,align:"center"}
				
		  ]],
		  rownumbers:true,
		  pagination:false,
		  toolbar:[	
				{text:'添加',iconCls:'icon-add',handler:function(){onAdd();}},'-',
				{text:'修改',iconCls:'icon-edit',handler:function(){onUpdate();}},'-',
				{text:'删除',iconCls:'icon-remove',handler:function(){onMulDelete();}},'-',
				{text:'启用',iconCls:'icon-info',handler:function(){onMulUpdateStatus(1);}},'-',
				{text:'禁用',iconCls:'icon-warn',handler:function(){onMulUpdateStatus(0);}},'-',
				{id:'moveUp'+id,text:'上移',iconCls:'icon-up',handler:function(){onMove(-1)}},'-',
				{id:'moveUp'+id,text:'下移',iconCls:'icon-down',handler:function(){onMove(1)}},'-'
		  ],
		  onDblClickRow:function(rowIndex, rowData){
				onUpdate();
		  },
		  onClickRow:function(rowIndex, rowData){
				selectRow = rowData;
				selectIndex = rowIndex;
				$(viewList).datagrid('unselectAll');
				$(viewList).datagrid('selectRow',selectIndex);
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
	
	//查询
	$(searchBtn).click(function(){
		search(true);
	})
	//分页操作
	var search = function(flag){
		var reminderCode = $('#reminderCodeSearch',queryContent).val();
		
		var url = "system/queryReminder.do";
		var content = {reminderCode:reminderCode,page:pageNumber,rows:pageSize};
		var result = syncCallService(url,content);
		if(result.isSuccess){
			var data = result.data;
			$(viewList).datagrid('loadData',eval("("+data.datagridData+")"));
			//需要重新重新分页信息
			if(flag){
				getTotal(content);
			}
		}else{
			$.messager.alert('提示',result.message,"error");
		}
	} 
	//统计总数
	var getTotal = function(content){
		var url = "system/getTotalCountReminder.do";
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
	//编辑框
	$(editDialog).dialog({  
	    title: '编辑系统提醒',  
	    width:600,
	    height:500,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    onClose:function(){
	    	$(editForm).form('clear');
	    	var rows  = $(reminderDetail).datagrid('getRows');
			if(rows.length!=0){
				$(reminderDetail).datagrid('loadData',CSIT.ClearData);
			}
	    },
	    toolbar:[	
	 			{id:'save'+id,text:'保存',iconCls:'icon-save',handler:function(){onSave();}},'-',
	 			{text:'添加',iconCls:'icon-add',handler:function(){onAdd();}},'-',
	 			{text:'退出',iconCls:'icon-cancel',handler:function(){
	 					$(editDialog).dialog('close');
	 				}
	 			}
	 	  ],
	 	 onOpen:function(){
	 		delReminderDetailIdArray = new Array();
	 	 }
	}); 
	//添加
	var onAdd = function(){
		$(editForm).form('clear');
		var rows  = $(reminderDetail).datagrid('getRows');
		if(rows.length!=0){
			$(reminderDetail).datagrid('loadData',CSIT.ClearData);
		}
		delReminderDetailIdArray = new Array();
		$('#status',editDialog).combobox('setValue',1);
		$(editDialog).dialog('open');
	}
	
	//保存前的赋值操作
	var setValue = function(){
		var title = $.trim($('#title',editForm).val());
		if(title == ''){
			$.messager.alert('提示','请填写标题','warning');
			return false;
		}
		var rows = $(reminderDetail).datagrid('getRows');
		if(rows.length==0){
			$.messager.alert('提示','请添加系统提醒明细','warning');
			return false;
		}
		var reminderDetailIdArray = new Array();
		var reminderItemIdArray = new Array();
		for ( var i = 0; i < rows.length; i++) {
			reminderDetailIdArray.push(rows[i].reminderDetailId);
			reminderItemIdArray.push(rows[i].reminderItemId);
		}
		$("#reminderDetailIds",editForm).val(reminderDetailIdArray.join(CSIT.join));
		$("#reminderItemIds",editForm).val(reminderItemIdArray.join(CSIT.join));
		$("#delReminderDetailIds",editForm).val(delReminderDetailIdArray.join(CSIT.join));
		return true;
	}
	//保存
	var onSave = function(){
		var url = 'system/saveReminder.do'
		$(editForm).form('submit',{
			url: url,
			onSubmit: function(){
				return setValue();
			},
			success: function(data){
				var result = eval('('+data+')');
				if(result.isSuccess){
					var fn = function(){
						var data = result.data;
						onOpen(data.reminderId);
					}
					$.messager.alert('提示','保存成功','info',fn);
				}else{
					$.messager.alert('提示',result.message,'error');
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
		onOpen(selectRow.reminderId);
		$(editDialog).dialog('open');
	 }
	//打开
	var onOpen = function(reminderId){
		var url = 'system/initReminder.do';
		var content ={reminderId:reminderId};
		asyncCallService(url,content,function(result){
			if(result.isSuccess){
				var data = result.data;
				var reminderData = eval("("+data.reminderData+")");
				$('#reminderId',editDialog).val(reminderData.reminderId);
				$('#title',editDialog).val(reminderData.title);
				$('#status',editDialog).combobox('setValue',reminderData.status);
				
				var detailData = eval("("+data.detailData+")");
				$(reminderDetail).datagrid('loadData',detailData);
			}else{
				$.messager.alert('提示',result.message,'error');
			}
		});
	}
	//删除多个
	var onMulDelete = function(){
		var rows =  $(viewList).datagrid('getSelections');
		if(rows.length==0){
			$.messager.alert("提示","请选择要删除的数据行","warning");
			return;
		}
		var idArray = new Array();
		for ( var i = 0; i < rows.length; i++) {
			idArray.push(rows[i].reminderId);
		}
		$.messager.confirm("提示","确定要删除选中的记录?",function(t){ 
			if(t){
				var url = 'system/mulDeleteReminder.do';
				var content ={ids:idArray.join(CSIT.join)};
				asyncCallService(url,content,function(result){
					if(result.isSuccess){
						var fn = function(){
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
	//修改多个审核状态
	var onMulUpdateStatus = function(status){
		var rows =  $(viewList).datagrid('getSelections');
		var msg = '';
		if(status==1){
			msg = '启用';
		}else{
			msg = '禁用';
		}
		if(rows.length==0){
			$.messager.alert("提示","请选择要"+msg+"的数据行","warning");
			return;
		}
		var idArray = new Array();
		for ( var i = 0; i < rows.length; i++) {
			idArray.push(rows[i].reminderId);
		}
		$.messager.confirm("提示","确定要"+msg+"记录?",function(t){ 
			if(t){
				var url = 'system/mulUpdateStatusReminder.do';
				var content ={ids:idArray.join(CSIT.join),status:status};
				asyncCallService(url,content,function(result){
					if(result.isSuccess){
						var fn = function(){
							search(true);
						}
						$.messager.alert('提示',msg+'成功','info',fn);
					}else{
						$.messager.alert('提示',result.message,'error');
					}
				});
			}
		});
	}
	//-----系统提醒明细----------
	var reminderDetail = $('#reminderDetail',editDialog);
	$(reminderDetail).datagrid({
	  title:'系统提醒明细',
	  singleSelect:true,	
	  fit:true,
	  columns:[[
		    {field:'title',title:'标题',width:200,align:"center"},
		    {field:'message',title:'提示信息',width:200,align:"center"},
		    {field:'rightName',title:'重定向Url',width:100,align:"center"}
	  ]],
	  rownumbers:true,
	  pagination:false,
	  toolbar:[	
			{id:'addReminderItem'+id,text:'添加系统提醒项',iconCls:'icon-add',handler:function(){onSelectReminderItem()}},'-',
			{id:'deleteReminderItem'+id,text:'删除系统提醒项',iconCls:'icon-remove',handler:function(){onDeleteReminderItem()}}
	  ],
	  onBeforeLoad:function(){
			$(this).datagrid('rejectChanges');
	  }
	 });
	//删除系统提醒明细项
	 var onDeleteReminderItem = function(){
		 var row = $(reminderDetail).datagrid('getSelected');
		 var rowIndex = $(reminderDetail).datagrid('getRowIndex',row);
		 if(row.reminderDetailId!=''){
			 delReminderDetailIdArray.push(row.reminderDetailId);
		 }
		 $(reminderDetail).datagrid('deleteRow',rowIndex);
	 }
	 //---------选择系统提醒项--------------------
	var selectDialog = $('#selectDialog',$this);
	var reminderItemList = $('#reminderItemList',selectDialog);
	var searchBtnSelectDialog = $('#searchBtnSelectDialog',selectDialog);
	//编辑框
	$(selectDialog).dialog({  
	    title: '选择系统提醒项',  
	    width:600,
	    height:400,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    onClose:function(){
	    	$(selectDialog).form('clear');
	    	$(reminderItemList).datagrid('loadData',CSIT.ClearData);
	    }
	});
	$(reminderItemList).datagrid({
		  fit:true,
		  cache: false, 
		  columns:[[
			    {field:'ck',checkbox:true},
			    {field:'title',title:'标题',width:200,align:"center"},
				{field:'message',title:'提示信息',width:200,align:"center"},
				{field:'rightName',title:'重定向Url',width:100,align:"center"}
		  ]],
		  rownumbers:true,
		  pagination:true,
		  toolbar:[	
				{text:'选择',iconCls:'icon-ok',handler:function(){onSelectOKReminderItem();}},
				{text:'退出',iconCls:'icon-cancel',handler:function(){
					 $(selectDialog).dialog('close');
				}}
		  ]
	 });
	//选择系统提醒项
	var onSelectReminderItem = function(){
		$(selectDialog).dialog('open');
	}
	//查询系统提醒项
	$(searchBtnSelectDialog).click(function(){
		//排除系统提醒明细中已添加的
		var rows = $(reminderDetail).datagrid('getRows');
		var idArray = new Array();
		for ( var i = 0; i < rows.length; i++) {
			idArray.push(rows[i].reminderItemId);
		}
		var ids = idArray.join(CSIT.join);
		var title = $('#title',selectDialog).val();
		$(reminderItemList).datagrid({
			url:'system/selectQueryReminderItem.do',
			queryParams: {
				ids: ids,
				title:title
			}
		});
	});
	//选择系统提醒项
	var onSelectOKReminderItem = function(){
		var rows = $(reminderItemList).datagrid('getSelections');
		 if(rows.length==0){
			 $.messager.alert('提示','请选择系统提醒项',"warning");
			 return;
		 }
		 for ( var i = 0; i < rows.length; i++) {
			var row = rows[i];
			 $(reminderDetail).datagrid('appendRow',{
				 reminderDetailId:'',
				 reminderItemId:row.reminderItemId,
				 title:row.title,
				 message:row.message,
				 rightName:row.rightName
			});
		}
		$(selectDialog).dialog('close');
	}
	//上下移
	var onMove = function(direction){
		var rows  = $(viewList).datagrid('getRows');
		if(direction==-1){
			if(selectIndex==0){
				$.messager.alert('提示',"已经是第一条记录了","warming");
				return;
			}
		}else if(direction==1){//下移
			if(selectIndex==rows.length-1){
				$.messager.alert('提示',"已经是最后一条记录了","warming");
				return;
			}
		}
		var updateRow = rows[selectIndex+direction];
		var reminderId = selectRow.reminderId;
		var title = selectRow.title;
		var status = selectRow.status;
		//后台更新排序
		var url = "system/updateArrayReminder.do";
		var content = {reminderId:reminderId,updateReminderId:updateRow.reminderId};
		var result = syncCallService(url,content);
		if(result.isSuccess){
			$(viewList).datagrid('updateRow',{
				index: selectIndex,
				row: updateRow
			});
			$(viewList).datagrid('updateRow',{
				index: selectIndex+direction,
				row: {
					reminderId:reminderId,
					title:title,
					status:status
				}
			});
			$(viewList).datagrid('unselectAll');
			$(viewList).datagrid('selectRow',selectIndex+direction);
			selectIndex = selectIndex+direction;
			selectRow = $(viewList).datagrid('getSelected');
		}else{
			$.messager.alert('提示',result.message,"error");
		}
	}
  }
})(jQuery);   