// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.roleInit = function() {
	  var $this = $(this);
	  var id = $(this).attr('id');
	  var selectRow = null;
	  var selectIndex = null;
	
	  var pageNumber = 1;
	  var pageSize = 20;
	  var isUpdate = false;
	  var viewList = $('#viewList_'+id,$this);
	  var editDialog = $('#editDialog_'+id,$this);
	  var editForm = $('#editForm_'+id,$this);
	  var queryContent = $('#queryContent_'+id,$this);
	  
	//加载列表
	  $(viewList).datagrid({
		url:"system/queryRole.do",
		singleSelect:true,
		method:"POST",
		nowrap:true,
		striped: true,
		collapsible:true,
		rownumbers:true,
		fit:true,
		toolbar:[	
					{text:'添加',iconCls:'icon-add',handler:function(){onAdd()}},'-',
					{text:'修改',iconCls:'icon-edit',handler:function(){onUpdate()}},'-',
					{text:'删除',iconCls:'icon-remove',handler:function(){onDelete()}},'-',
					{text:'角色权限',iconCls:'icon-role',handler:function(){onRoleRight()}}
				],
		columns:[[
			{field:'roleName',title:'角色名',width:150,align:"center"}
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
	    title: '编辑系统角色信息',  
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
		isUpdate = false;
		$(editForm).form('clear');
		$(editDialog).dialog('open');
	}
	//保存前的赋值操作
	var setValue = function(){
		var roleName = $.trim($('#roleName',editForm).val());
		if(''==roleName){
			$.messager.alert('提示','请填写角色名','warning');
			return false;
		}
		return true;
	}
	//保存
	var onSave = function(){
		var url = null;
		if(!isUpdate){
			url = 'system/addRole.do'
		}else{
			url = 'system/updateRole.do'
		}
		$(editForm).form('submit',{
			url: url,
			onSubmit: function(){
				return setValue();
			},
			success: function(data){
				var result = eval('('+data+')');
				if(result.isSuccess){
					var fn = function(){
						//新增
						if(!isUpdate){
							$(viewList).datagrid('reload');
						}else{
							var row = $(editForm).serializeObject();
							$(viewList).datagrid('updateRow',{index:selectIndex,row:row});
						}
						$(editDialog).dialog('close');
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
		if(selectRow.roleName=='超级管理员'){
			$.messager.alert('提示','超级管理员不能修改',"error");
			return false;
		}
		isUpdate = true;
		$(editForm).form('load',selectRow);
		$(editDialog).dialog('open');
	 }
	//删除
	var onDelete = function(){
		if(selectRow==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		}
		if(selectRow.roleName=='超级管理员'){
			$.messager.alert('提示','超级管理员不能删除',"warning");
			return false;
		}
		$.messager.confirm('确认', '确定删除该记录吗?', function(r){
			if(r){
				var url = "system/deleteRole.do";
				var content = {roleId:selectRow.roleId};
				$.post(url,content,
					function(result){
						if(result.isSuccess){
							search(true)
						}else{
							$.messager.alert('提示',result.message,"error");
						}
				},'json');
			}
		})
	}
	//查询
	var search = function(flag){
		var roleNameSearch = $('#roleNameSearch',queryContent).val();
		var content = {roleName:roleNameSearch};
		
		$(viewList).datagrid({
			queryParams:content
		});
	}
	//查询
	$('#search',$this).click(function(){
		search(true);
	})
	//-------------------角色权限-----------------------
	var roleRightTree = $('#roleRightTree_'+id,$this);
	var roleRightDialog = $('#roleRightDialog_'+id,$this);
	var loading = false;
	//编辑框
	$(roleRightDialog).dialog({  
	    title: '角色权限',  
	    width:400,
	    height:500,
	    cache: false, 
	    closed: true,  
	    modal: true,
	    closable:false,
	    toolbar:[{
			text:'退出',
			iconCls:'icon-cancel',
			handler:function(){
				$(roleRightDialog).dialog('close');
			}
		}]
	}); 
	//角色权限
	var onRoleRight = function(){
		$(roleRightTree).tree({
			url: 'system/queryRootRoleRight.do?role.roleId='+selectRow.roleId,
			checkbox:true,
			onBeforeExpand:function(node,param){
				var roleRightId = node.id;
				var rightId = roleRightId.split('_')[1];
				$(roleRightTree).tree('options').url = 'system/queryChildrenRoleRight.do?role.roleId='+selectRow.roleId+'&right.rightId='+rightId;
	        },
	        onBeforeLoad:function(node, param){ 
				loading=true; 
			}, 
			onLoadSuccess:function(node, data){ 
			    loading=false; 
			},
			onClick:function(node){ 
				$(roleRightTree).tree('expand',node.target);
			},
            onCheck:function(node,checked){
            	if(!loading){
            		var roleRightId = node.id;
    				var rightId = roleRightId.split('_')[1];
    				var state=0;
    				if(checked){
    					state=1;
    				}
    				var url = 'system/updateStateRoleRight.do?role.roleId='+selectRow.roleId+'&right.rightId='+rightId+'&state='+state;
            		$.post(url,
					  function(result){
            			if(!result.isSuccess){
							$.messager.alert('提示',result.message,"error");
						}
            		},'json');
            	}
            }
	  });
		$(roleRightDialog).dialog('open');
	}
  }
})(jQuery);   