// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.supplierInit = function() {
	  var $this = $(this);
	  var id = $(this).attr('id');
	  
	  var queryContent = $('#queryContent',$this);
	  var searchBtn = $('#searchBtn',$this);
	  
	  var viewList = $('#viewList',$this);
	  var selectRow = null;
	  
	  var pager = $('#pager',$this);
	  var pageNumber = 1;
	  var pageSize = 10;
	  
	  var editDialog = $('#editDialog',$this);
	  var editForm = $('#editForm',$this);
	  
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
					{text:'删除',iconCls:'icon-remove',handler:function(){onDelete();}},'-',
					{text:'启用',iconCls:'icon-info',handler:function(){onUpdateStatus(1);}},'-',
					{text:'禁用',iconCls:'icon-warn',handler:function(){onUpdateStatus(0);}}
				],
		columns:[[
			{field:'status',title:'状态',width:50,align:"center",
		    	formatter: function(value,row,index){
			    	if (value==0){
						return '<img src="style/v1/icons/warn.png"/>';
					} else if (value==1){
						return '<img src="style/v1/icons/info.png"/>';
					};
		    	}
			},
			{field:'supplierName',title:'供应商名称',width:100,align:"center"},
			{field:'linkMan',title:'联系人',width:100,align:"center"},
			{field:'tel',title:'供应商电话',width:100,align:"center"},
			{field:'address',title:'供应商地址',width:100,align:"center"}
			
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
	    title: '编辑供应商',  
	    width:400,
	    height:250,
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
		var supplierName = $('#supplierNameSearch',queryContent).val();
		var content = {supplierName:supplierName,page:pageNumber,rows:pageSize};
		//取得列表信息
		var url = 'dict/querySupplier.do';
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
	
	//添加
	var onAdd = function(){
		$(editForm).form('clear');
		$('#status',editDialog).combobox('setValue',1);
		$(editDialog).dialog('open');
	};
	//保存前的赋值操作
	var setValue = function(){
		var supplierName = $.trim($('#supplierName',editForm).val());
		if(''==supplierName){
			$.messager.alert('提示','请填写供应商名称','warning');
			return false;
		}
		return true;
	};
	//保存
	var onSave = function(){
		$(editForm).form('submit',{
			url: 'dict/saveSupplier.do',
			onSubmit: function(){
				return setValue();
			},
			success: function(data){
				var result = eval('('+data+')');
				if(result.isSuccess){
					var fn = function(){
						var supplierId = $.trim($('#supplierId',editForm).val());
						if(supplierId==''){//新增
							var  data = result.data;
							$('#supplierId',editForm).val(data.supplierId);
						}
						TRN.SupplierList = null;
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
		var url = "dict/getTotalCountSupplier.do";
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
		$(editForm).form('load',selectRow);
		$(editDialog).dialog('open');
	 };
	 
	//删除
		var onDelete = function(){
			if(selectRow==null){
				$.messager.alert("提示","请选择数据行","warning");
				return;
			}
			$.messager.confirm('确认', '确定删除该记录吗?', function(r){
				if(r){
					var url = "dict/deleteSupplier.do";
					var content = {supplierId:selectRow.supplierId};
					$.post(url,content,
						function(result){
							if(result.isSuccess){
								search(true);
								TRN.SupplierList = null;
							}else{
								$.messager.alert('提示',result.message,"error");
							}
					},'json');
				}
			});
		};
		//修改审核状态
		var onUpdateStatus = function(status){
			var msg = '';
			if(status==1){
				msg = '启用';
			}else{
				msg = '禁用';
			}
			if(selectRow==null){
				$.messager.alert("提示","请选择要"+msg+"的数据行","warning");
				return;
			}
			$.messager.confirm("提示","确定要"+msg+"记录?",function(t){ 
				if(t){
					var url = 'dict/updateStatusSupplier.do';
					var content ={supplierId:selectRow.supplierId,status:status};
					asyncCallService(url,content,function(result){
						if(result.isSuccess){
							var fn = function(){
								search(true);
								TRN.SupplierList = null;
							};
							$.messager.alert('提示',msg+'成功','info',fn);
						}else{
							$.messager.alert('提示',result.message,'error');
						}
					});
				}
			});
		};
  };
})(jQuery);   