// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.rightInit = function() {
	  var $this = $(this);
	  var id = $(this).attr('id');
	  var rightId = $(this).attr('rightId');
	  
	  var selectRow = null;
	  var selectIndex = null;
	  var pageNumber = 1;
	  var pageSize = 20;
	  var editDialog = $('#editDialog_'+id,$this);
	  var editForm = $('#editForm_'+id,editDialog);
	  var rightTree =  $('#rightTree_'+id,$this);
	  var rightList =  $('#rightList_'+id,$this);
	  
	  //权限树  
	  $(rightTree).tree({
			url: 'system/selectRootRight.do',
			onBeforeExpand:function(node,param){
				$(rightTree).tree('options').url = 'system/selectTreeNodeRight.do?rightId='+node.id;  
	        },
			onClick:function(node){ 
				$(rightTree).tree('expand',node.target);
				var rightName = $('#rightNameSearch').val();
				var content = {rightId:node.id,rightName:rightName,page:pageNumber,rows:pageSize};
				$(rightList).datagrid({
					queryParams:content
				});
			}
	  });
	  //列表
	  $(rightList).datagrid({
			fit:true,
			url:'system/getTreeNodeChildrenRight.do',
			idField:'rightId',
			columns:[[{field:'ck',checkbox:true},
			    {field:'rightName',title:'权限名称',width:120,sortable:true,align:"center"},
				{field:'rightUrl',title:'权限Url',width:300,sortable:true,align:"center"},
				{field:'kind',title:'类型',width:300,sortable:true,align:"center",formatter: function(value,row,index){
					if (value==1){
						return 'Url权限';
					} else if (value==2){
						return '界面按钮权限';
					}else if (value==3){
						return '数据显示权限';
					}
			  }}
			]],
			rownumbers:true,
			pagination:true,
			selectOnCheck:false,
			checkOnSelect:false,
			singleSelect:true,
			toolbar:[	
						{id:'add_'+id,text:'添加',iconCls:'icon-add',handler:function(){onAdd()}},'-',
						{id:'update_'+id,text:'修改',iconCls:'icon-edit',handler:function(){onUpdate()}},'-',
						{id:'delete_'+id,text:'删除',iconCls:'icon-remove',handler:function(){onDelete()}},'-',
						{id:'moveUp_'+id,text:'上移',iconCls:'icon-up',handler:function(){onMove(-1)}},'-',
						{id:'moveDown_'+id,text:'下移',iconCls:'icon-down',handler:function(){onMove(1)}},'-'
					],
			onDblClickRow:function(rowIndex, rowData){
				onUpdate();
			},
			onClickRow:function(rowIndex, rowData){
				selectRow = rowData;
				selectIndex = rowIndex;
			}
	 });
	//编辑框
	$(editDialog).dialog({  
	    title: '编辑系统权限信息',  
	    width:400,
	    height:200,
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
	//添加
	var onAdd = function(){
		$(editForm).form('clear');
		$('#kind',editForm).combobox('setValue',1);
		$(editDialog).dialog('open');
	}
	//保存前的赋值操作
	var setValue = function(){
		var rightName = $.trim($('#rightName',editForm).val());
		if(rightName==''){
			$.messager.alert('提示','请填写权限名称','warning');
			return false;
		}
		var kind = $.trim($('#kind',editForm).combobox('getValue'));
		if(kind==''){
			$.messager.alert('提示','请选择权限类型','warning');
			return false;
		}
		var selectedNote = $(rightTree).tree('getSelected');
		if(selectedNote==null){
			var root = $(rightTree).tree('getRoot');
			if(root==null){
				$('#parentID',editForm).val(null);
			}else{
				$('#parentID',editForm).val(root.id);
			}
		}else{
			$('#parentID',editForm).val(selectedNote.id);
		}
		return true;
	}
	//保存
	var onSave = function(){
		 $(editForm).form('submit',{
			url: 'system/saveRight.do',
			onSubmit: function(){
				return setValue();
			},
			success: function(data){
				var result = eval('('+data+')');
				if(result.isSuccess){
					var fn = function(){
						var rightId = $('#rightId',editForm).val();
						//新增
						if(rightId==''){
							var node = $(rightTree).tree('getSelected');
							if(node==null){
								node = $(rightTree).tree('getRoot');
							}
							var rightName = $('#rightName',editForm).val();
							$(rightTree).tree('append',{
								parent: (node?node.target:null),
								data:[{
									id:result.data.rightId,
									text:rightName
								}]
							});
							search();
						}else{
							var row = $(editForm).serializeObject();
							$(rightList).datagrid('updateRow',{index:selectIndex,row:row});
							
							var rightId=$("#rightId",editForm).val();
							var rightName = $('#rightName',editForm).val();
							var updateNote=$(rightTree).tree('find',rightId);
							updateNote.text=rightName;
							$(rightTree).tree('update', updateNote);
						}
					}
					$.messager.alert('提示','保存成功','info',fn);
				}else{
					$.messager.alert('提示',result.message,'error');
				}
				$(editDialog).dialog('close');
			}
		 });
	}
	//修改
	var onUpdate = function(){
		var options = $('#update_'+id,$this).linkbutton('options');
		if(!options.disabled){
			if(selectRow==null){
				$.messager.alert("提示","请选择数据行","warning");
				return;
			}
			$(editForm).form('load',selectRow);
			$(editDialog).dialog('open');
		}
	 }
	//删除
	var onDelete = function(){
		var rows = $(rightList).datagrid('getChecked');
		if(rows.length==0){
			 $.messager.alert('提示',"请选中要删除的纪录","warming");
			 return;	
		}
		$.messager.confirm("提示！","确定要删除选中的记录?",function(t){ 
			if(!t) return;
			if(rows.length==0){
				 $.messager.alert('提示',"请选中要删除的纪录","warming");
				 return;	
			}
			var idArray = new Array();
			for(var i=0;i<rows.length;i++){
				idArray.push(rows[i].rightId);
			}
			var ids = idArray.join(CSIT.join);
			var url = "system/mulDeleteRight.do";
			var content = {ids:ids};
			$.post(url,content,
				function(result){
					if(result.isSuccess){
						var rows = $(rightList).datagrid('getSelections');
						for(var i=0;i<rows.length>0;i++){
							$(rightTree).tree('remove',$(rightTree).tree('find',rows[i].rightId).target);
						}
						search();
						$(rightList).datagrid('unselectAll');
					}else{
						$.messager.alert('提示',result.message,"error");
					}
				}, "json");
		});
	}
	//查询
	$('#search',$this).click(function(){
		search();
	})
	//分页操作
	var search = function(){
		var queryContent = $('.queryContent',$this);
		var rightNameSearch = $('#rightNameSearch',queryContent).val();
		var rightId = '';
		var selectedNote = $(rightTree).tree('getSelected');
		if(selectedNote==null){
			var root = $(rightTree).tree('getRoot');
			if(root!=null){
				rightId = root.id;
			}
		}else{
			rightId = selectedNote.id;
		}
		var content = {rightId:rightId,rightName:rightNameSearch,page:pageNumber,rows:pageSize};
		
		$(rightList).datagrid({
			queryParams:content
		});
		selectRow = null;
	}
	//移动
	var onMove = function(direction){
		var rows  = $(rightList).datagrid('getRows');
		if(direction==-1){
			if(selectIndex==0){
				$.messager.alert('提示',"已经是第一条记录了","warming");
				return;
			}
		}else if(direction==1){//下移
			var rows  = $(rightList).datagrid('getRows');
			if(selectIndex==rows.length-1){
				$.messager.alert('提示',"已经是最后一条记录了","warming");
				return;
			}
		}
		var updateRow = rows[selectIndex+direction];
		var rightName = selectRow.rightName;
		var rightUrl = selectRow.rightUrl;
		var rightId = selectRow.rightId;
		//后台更新排序
		var url = "system/updateArrayRight.do";
		var content = {rightId:rightId,updateRightId:updateRow.rightId};
		var result = syncCallService(url,content);
		if(result.isSuccess){
			//更新树节点
			var node = $(rightTree).tree('find',rows[selectIndex].rightId);
			var updateNode = $(rightTree).tree('find',rows[selectIndex+direction].rightId);
			$(rightTree).tree('update', {
				target: node.target,
				text: updateNode.text,
				id: updateNode.id
			});
			
			$(rightTree).tree('update', {
				target: updateNode.target,
				text: node.text,
				id: node.id
			});
			
			$(rightList).datagrid('updateRow',{
				index: selectIndex,
				row: updateRow
			});
			$(rightList).datagrid('updateRow',{
				index: selectIndex+direction,
				row: {
					rightId:rightId,
					rightName:rightName,
					rightUrl:rightUrl
				}
			});
			$(rightList).datagrid('unselectAll');
			$(rightList).datagrid('selectRow',selectIndex+direction);
			selectIndex = selectIndex+direction;
			selectRow = $(rightList).datagrid('getSelected');
		}else{
			$.messager.alert('提示',result.message,"error");
		}
	}
	//----------检查权限--------------
	var checkArray = new Array();
	var addBtn = $('#add_'+id,$this);
	var updateBtn = $('#update_'+id,$this);
	var deleteBtn = $('#delete_'+id,$this);
	var moveUpBtn = $('#moveUp_'+id,$this);
	var moveDownBtn = $('#moveDown_'+id,$this);
	
	checkArray.push(addBtn);
	checkArray.push(updateBtn);
	checkArray.push(deleteBtn);
	checkArray.push(moveUpBtn);
	checkArray.push(moveDownBtn);
	
	checkRight(checkArray,rightId);
  }
})(jQuery);   