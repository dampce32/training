// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.schoolInit = function() {
	  var $this = $(this);
	  var id = $(this).attr('id');
	  var queryContent = $('#queryContent',$this);
	  
	  var selectRow = null;
	  var selectIndex = null;
	  var pageNumber = 1;
	  var pageSize = 20;
	  var editDialog = $('#editDialog',$this);
	  var editForm = $('#editForm',editDialog);
	  var schoolTree =  $('#schoolTree',$this);
	  var schoolList =  $('#schoolList',$this);
	  
	  //校区树  
	  $(schoolTree).tree({
			url: 'system/selectRootSchool.do',
			onBeforeExpand:function(node,param){
				$(schoolTree).tree('options').url = 'system/selectTreeNodeSchool.do?schoolId='+node.id;  
	        },
			onClick:function(node){ 
				$(schoolTree).tree('expand',node.target);
				var schoolName = $('#schoolNameSearch').val();
				var content = {schoolId:node.id,schoolName:schoolName,page:pageNumber,rows:pageSize};
				$(schoolList).datagrid({
					queryParams:content
				});
			}
	  });
	  //列表
	  $(schoolList).datagrid({
			fit:true,
			url:'system/getTreeNodeChildrenSchool.do',
			idField:'schoolId',
			columns:[[{field:'ck',checkbox:true},
			          {field:'status',title:'状态',width:120,sortable:true,align:"center",
						formatter: function(value,row,index){
							if (value==0){
								return '<img src="style/v1/icons/warn.png"/>';
							} else if (value==1){
								return '<img src="style/v1/icons/info.png"/>';
							}
					  }},
			          {field:'schoolCode',title:'校区编号',width:120,sortable:true,align:"center"},
		              {field:'schoolName',title:'校区名称',width:120,sortable:true,align:"center"},
		              {field:'tel',title:'电话',width:120,sortable:true,align:"center"},
		              {field:'address',title:'地址',width:120,sortable:true,align:"center"}
			]],
			rownumbers:true,
			pagination:true,
			toolbar:[	
						{text:'添加',iconCls:'icon-add',handler:function(){onAdd();}},'-',
						{text:'修改',iconCls:'icon-edit',handler:function(){onUpdate();}},'-',
						{text:'删除',iconCls:'icon-remove',handler:function(){onDelete();}},'-',
						{id:'moveUp'+id,text:'上移',iconCls:'icon-up',handler:function(){onMove(-1);}},'-',
						{id:'moveUp'+id,text:'下移',iconCls:'icon-down',handler:function(){onMove(1);}},'-'
					],
			onDblClickRow:function(rowIndex, rowData){
				onUpdate();
			},
			onClickRow:function(rowIndex, rowData){
				selectRow = rowData;
				selectIndex = rowIndex;
				$(schoolList).datagrid('unselectAll');
				$(schoolList).datagrid('selectRow',selectIndex);
			}
	 });
	//编辑框
	$(editDialog).dialog({  
	    title: '编辑校区',  
	    width:400,
	    height:300,
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
		$('#status',editForm).combobox('setValue',1);
		$(editDialog).dialog('open');
	}
	//保存前的赋值操作
	var setValue = function(){
		var schoolName = $('#schoolName',editForm).val();
		if(schoolName==''){
			$.messager.alert('提示','请填写校区名称','warning');
			return false;
		}
		var selectedNote = $(schoolTree).tree('getSelected');
		if(selectedNote==null){
			var root = $(schoolTree).tree('getRoot');
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
			url: 'system/saveSchool.do',
			onSubmit: function(){
				return setValue();
			},
			success: function(data){
				var result = eval('('+data+')');
				if(result.isSuccess){
					var fn = function(){
						var schoolId = $('#schoolId',editForm).val();
						//新增
						if(schoolId==''){
							var node = $(schoolTree).tree('getSelected');
							if(node==null){
								node = $(schoolTree).tree('getRoot');
							}
							var schoolName = $('#schoolName',editForm).val();
							$(schoolTree).tree('append',{
								parent: (node?node.target:null),
								data:[{
									id:result.data.schoolId,
									text:schoolName
								}]
							});
							search();
						}else{
							var row = $(editForm).serializeObject();
							$(schoolList).datagrid('updateRow',{index:selectIndex,row:row});
							
							var schoolId=$("#schoolId",editForm).val();
							var schoolName = $('#schoolName',editForm).val();
							var updateNote=$(schoolTree).tree('find',schoolId);
							updateNote.text=schoolName;
							$(schoolTree).tree('update', updateNote);
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
		if(selectRow==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		}
		$(editForm).form('load',selectRow);
		$('#status',editForm).combobox('setValue',selectRow.status);
		$(editDialog).dialog('open');
	 }
	//删除
	var onDelete = function(){
		var rows = $(schoolList).datagrid('getSelections');
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
				idArray.push(rows[i].schoolId);
			}
			var ids = idArray.join(CSIT.join);
			var url = "system/mulDeleteSchool.do";
			var content = {ids:ids};
			$.post(url,content,
				function(result){
					if(result.isSuccess){
						var rows = $(schoolList).datagrid('getSelections');
						for(var i=0;i<rows.length>0;i++){
							$(schoolTree).tree('remove',$(schoolTree).tree('find',rows[i].schoolId).target);
						}
						search();
						$(schoolList).datagrid('unselectAll');
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
		var schoolNameSearch = $('#schoolNameSearch',queryContent).val();
		var schoolId = '';
		var selectedNote = $(schoolTree).tree('getSelected');
		if(selectedNote==null){
			var root = $(schoolTree).tree('getRoot');
			if(root!=null){
				schoolId = root.id;
			}
		}else{
			schoolId = selectedNote.id;
		}
		var content = {schoolId:schoolId,schoolName:schoolNameSearch,page:pageNumber,rows:pageSize};
		
		$(schoolList).datagrid({
			queryParams:content
		});
		selectRow = null;
	}
	//移动
	var onMove = function(direction){
		var rows  = $(schoolList).datagrid('getRows');
		if(direction==-1){
			if(selectIndex==0){
				$.messager.alert('提示',"已经是第一条记录了","warming");
				return;
			}
		}else if(direction==1){//下移
			var rows  = $(schoolList).datagrid('getRows');
			if(selectIndex==rows.length-1){
				$.messager.alert('提示',"已经是最后一条记录了","warming");
				return;
			}
		}
		var updateRow = rows[selectIndex+direction];
		var schoolId = selectRow.schoolId;
		var schoolCode = selectRow.schoolCode;
		var schoolName = selectRow.schoolName;
		var tel = selectRow.tel;
		var address = selectRow.address;
		var status = selectRow.status;
		//后台更新排序
		var url = "system/updateArraySchool.do";
		var content = {schoolId:schoolId,updateSchoolId:updateRow.schoolId};
		var result = syncCallService(url,content);
		if(result.isSuccess){
			//更新树节点
			var node = $(schoolTree).tree('find',rows[selectIndex].schoolId);
			var updateNode = $(schoolTree).tree('find',rows[selectIndex+direction].schoolId);
			$(schoolTree).tree('update', {
				target: node.target,
				text: updateNode.text,
				id: updateNode.id
			});
			
			$(schoolTree).tree('update', {
				target: updateNode.target,
				text: node.text,
				id: node.id
			});
			
			$(schoolList).datagrid('updateRow',{
				index: selectIndex,
				row: updateRow
			});
			$(schoolList).datagrid('updateRow',{
				index: selectIndex+direction,
				row: {
					schoolId:schoolId,
					schoolCode:schoolCode,
					schoolName:schoolName,
					tel:tel,
					address:address,
					status:status
				}
			});
			$(schoolList).datagrid('unselectAll');
			$(schoolList).datagrid('selectRow',selectIndex+direction);
			selectIndex = selectIndex+direction;
			selectRow = $(schoolList).datagrid('getSelected');
		}else{
			$.messager.alert('提示',result.message,"error");
		}
	}
	
  }
})(jQuery);   