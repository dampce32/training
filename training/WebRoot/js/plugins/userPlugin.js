// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.userInit = function() {
	  var $this = $(this);
	  var id =   $(this).attr('id');
	  
	  var viewList = $('#viewList_'+id,$this);
	  var editDialog = $('#editDialog_'+id,$this);
	  var editForm = $('#editForm_'+id,editDialog);
	  var queryContent = $('#queryContent_'+id,$this);
	  
	  var selectRow = null;
	  var selectIndex = null;
	
	  $('#schoolSearch',queryContent).combobox({
		  width:120,
		  data:TRN.getSelfSchoolList(),
		  valueField:'schoolId',
		  textField:'schoolName'
	  });
	//加载列表
	$(viewList).datagrid({
			fit:true,
			singleSelect:true,
			method:"POST",
			nowrap:true,
			striped: true,
			collapsible:true,
			rownumbers:true,
			pagination:true,
			remoteSort:false,
			toolbar:[	
						{text:'添加',iconCls:'icon-add',handler:function(){onAdd()}},'-',
						{text:'修改',iconCls:'icon-edit',handler:function(){onUpdate()}},'-',
						{text:'删除',iconCls:'icon-remove',handler:function(){onDelete()}},'-',
						{text:'用户角色',iconCls:'icon-role',handler:function(){onUserRole()}},'-',
						{text:'用户权限',iconCls:'icon-role-right',handler:function(){onUserRight()}}
					],
			columns:[[
				{field:'userName',title:'用户名称',width:100,align:"center"},
				{field:'userCode',title:'用户登录名',width:100,align:"center"},
				{field:'schoolName',title:'校区',width:100,align:"center"}
			]],
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
	    title: '编辑用户信息',  
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
	  //初始化选择条件
	  var initChoose = function(){
		//所在校区
		$('#school',editDialog).combobox({
			valueField:'schoolId',
			textField:'schoolName',
			width:250,
			data:TRN.getSelfSchoolList()
		})	
	  }
	//添加信息
	var onAdd = function(){
		$(editForm).form('clear');
		initChoose();
		$(editDialog).dialog('open');
	}
	//修改
	var onUpdate = function(){
		if(selectRow==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		}
		$(editForm).form('load',selectRow);
		initChoose();
		$('#school',editDialog).combobox('setValue',selectRow.schoolId);
		$(editDialog).dialog('open');
	 }
	//删除
	var onDelete = function(){
		if(selectRow==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		}
		$.messager.confirm('确认', '确定删除该记录吗?', function(r){
			if(r){
				var url = "system/deleteUser.do";
				var content = {userId:selectRow.userId};
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
	//提交前验证
	var setValue = function(){
		var userName = $.trim($('#userName',editForm).val());
		if(''==userName){
			$.messager.alert('提示','请填写用户名称','warning');
			return false;
		}
		var userCode = $.trim($('#userCode',editForm).val());
		if(''==userCode){
			$.messager.alert('提示','请填写用户登录名','warning');
			return false;
		}
		var schoolId = $('#school',editDialog).combobox('getValue') ;
		if(''==schoolId){
			$.messager.alert('提示','请选择校区','warning');
			return false;
		}
		if(!parseInt(schoolId)){
			$.messager.alert('提示','请选择已存在的校区','warning');
			return false;
		}
		$('#schoolId',editDialog).val(schoolId);
		return true;
	}
	//保存
	var onSave = function(){
		$(editForm).form('submit',{
			url: 'system/saveUser.do',
			onSubmit: function(){
				return setValue();
			},
			success: function(data){
				var result = eval('(' + data + ')'); 
				if(result.isSuccess){
					$.messager.alert('提示','保存成功','info');
					$(editDialog).dialog('close');
				}else{
					$.messager.alert('提示',result.message,'error');
				}
				
			}
		});
	}
	//查询
	var search = function(flag){
		var userName = $('#userNameSearch',queryContent).val();
		var userCode = $('#userCodeSearch',queryContent).val();
		var schoolId =  $.trim($('#schoolSearch',queryContent).combobox('getValue'));
		if(schoolId==''){
			schoolId = -1 ;
		}
		var content = {userCode:userCode,userName:userName,'school.schoolId':schoolId};
		
		$(viewList).datagrid({
			url:"system/queryUser.do",
			queryParams:content
		});
	}
	//查询
	$('#search',$this).click(function(){
		search(true);
	})
	//---用户角色---
	var userRoleList = $('#userRoleList_'+id,$this);
	var userRoleDialog = $('#userRoleDialog_'+id,$this);
	var oldIdArray = new Array();
	var idArray = new Array();
	var deleteIdList = new Array();
	var addIdList = new Array();
	//编辑框
	$(userRoleDialog).dialog({  
	    title: '编辑用户角色信息',  
	    width:400,
	    height:500,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    toolbar:[{
			text:'保存',
			iconCls:'icon-save',
			handler:function(){
				onSaveUserRole();
			}
		},'-',{
			text:'退出',
			iconCls:'icon-cancel',
			handler:function(){
				$(userRoleDialog).dialog('close');
			}
		}]
	});
	//用户角色
	var onUserRole = function(){
		if(selectRow==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		}
		 //加载列表
		$(userRoleList).datagrid({
			url:"system/queryRoleUserRole.do?user.userId="+selectRow.userId,
			fit:true,
			method:"POST",
			nowrap:true,
			striped: true,
			collapsible:true,
			rownumbers:true,
			columns:[[
			    {field:'ck',checkbox:true},
				{field:'roleName',title:'角色名',width:100,align:"center"}
			]],
			onLoadSuccess:function(data){
				var rows = $(userRoleList).datagrid('getRows');
				oldIdArray = new Array();
				for(var i=0;i<rows.length;i++){
					if(rows[i].checked){
						oldIdArray.push(rows[i].roleId);
						$(userRoleList).datagrid('selectRow',i);
					}
				}
			}
		  });
		$(userRoleDialog).dialog('open');
	}
	//保存用户角色前验证
	var setValueUserRole = function(){
		deleteIdList = new Array();
		addIdList = new Array();
		idArray = new Array();
		var rows = $(userRoleList).datagrid('getSelections');
		for(var i =0;i<rows.length;i++){
			idArray.push(rows[i].roleId);
		}
		
		for (var i =0;i<oldIdArray.length;i++) {
			var oldId = oldIdArray[i];
			var isDel = true;
			for (var j =0;j<idArray.length;j++) {
				var id = idArray[j];
				if(oldId==id){
					isDel =false;
					break;
				}
			}
			if(isDel){
				deleteIdList.push(oldId);
			}
		}
		for (var i =0;i<idArray.length;i++) {
			var id = idArray[i];
			var isAdd = true;
			for (var j =0;j<oldIdArray.length;j++) {
				var oldId = idArray[j];
				if(oldId==id){
					isAdd =false;
					break;
				}
			}
			if(isAdd){
				addIdList.push(id);
			}
		}
		if(addIdList.length==0&&deleteIdList.length==0){
			$.messager.alert('提示','角色没有修改');
			return false;
		}
		return true;
	}
	//保存
	var onSaveUserRole = function(){
		if(setValueUserRole()){
			var userId = selectRow.userId;
			var content = {'user.userId':userId,ids: idArray.join(CSIT.join),oldIds:oldIdArray.join(CSIT.join)};
			var url = "system/updateRoleUserRole.do";
			$.post(url,content,
				function(result){
					if(result.isSuccess){
						oldIdArray = idArray;
						$.messager.alert('提示','保存成功');
					}else{
						$.messager.alert('提示',result.message,"warning");
					}
			},'json')
		}
	}
	
	//--------用户权限----------
	var userRightDialog = $('#userRightDialog_'+id,$this);
	var userRightTree = $('#userRightTree_'+id,$this);
	//编辑框
	$(userRightDialog).dialog({  
	    title: '用户权限',  
	    width:400,
	    height:500,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    toolbar:[{
			text:'取消',
			iconCls:'icon-cancel',
			handler:function(){
				$(userRightDialog).dialog('close');
			}
		}]
	});
	//用户权限
	var onUserRight = function(){
		if(selectRow==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		}
		$(userRightTree).tree({
			url: 'system/queryRootRightUser.do?userId='+selectRow.userId,
			checkbox:true,
			onBeforeExpand:function(node,param){
				$(userRightTree).tree('options').url = 'system/queryChildrenRightUser.do?userId='+selectRow.userId+'&rightId='+node.id;  
	        }, 
			onLoadSuccess:function(node, data){ 
				 $(this).find('span.tree-checkbox').unbind().click(function(){
			            return false;
		         });
			}
		});
		$(userRightDialog).dialog('open');
	}
}	
})(jQuery);   