// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.commodityInit = function() {
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
	  
	  $('#unitSearch',queryContent).combobox({
			width:120,
			data:TRN.getUnitList(),
			valueField:'unitId',
			textField:'unitName'
		});
		$('#commodityTypeSearch',queryContent).combobox({
			width:120,
			data:TRN.getCommodityTypeList(),
			valueField:'commodityTypeId',
			textField:'commodityTypeName'
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
					{text:'删除',iconCls:'icon-remove',handler:function(){onDelete();}},'-',
					{text:'启用',iconCls:'icon-info',handler:function(){onUpdateStatus(1);}},'-',
					{text:'禁用',iconCls:'icon-warn',handler:function(){onUpdateStatus(0);}}
				],
		columns:[[
		    {field:'commodityTypeId',hidden:true},
			{field:'unitId',hidden:true},
			{field:'status',title:'状态',width:50,align:"center",
		    	formatter: function(value,row,index){
			    	if (value==0){
						return '<img src="style/v1/icons/warn.png"/>';
					} else if (value==1){
						return '<img src="style/v1/icons/info.png"/>';
					};
		    	}
			},
			{field:'commodityName',title:'商品名称',width:100,align:"center"},
			{field:'commodityTypeName',title:'商品分类',width:100,align:"center"},
			{field:'unitName',title:'商品单位',width:100,align:"center"},
			{field:'purchasePrice',title:'进价',width:100,align:"center"},
			{field:'salePrice',title:'售价',width:100,align:"center"},
			{field:'size',title:'规格',width:100,align:"center"},
			{field:'qtyStore',title:'库存数量',width:100,align:"center"},
			{field:'qtyWarn',title:'预警值',width:100,align:"center"},
			{field:'note',title:'备注',width:100,align:"center"}
			
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
	    title: '编辑商品',  
	    width:450,
	    height:480,
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
		var commodityTypeId = $('#commodityTypeSearch',queryContent).combobox('getValue');
		var unitId = $('#unitSearch',queryContent).combobox('getValue');
		var commodityName = $.trim($('#commodityName',queryContent).val());
		var commodityTypeBoolean=isNumber(commodityTypeId);
		var unitBoolean=isNumber(unitId);
		
		if(commodityTypeBoolean==false){
			$.messager.alert('提示','请选择已有的商品分类','error');
			return;
		}
		if(unitBoolean==false){
			$.messager.alert('提示','请选择已有的商品单位','error');
			return;
		}
		
		var content = {commodityName:commodityName,page:pageNumber,rows:pageSize,'commodityType.commodityTypeId':commodityTypeId,'unit.unitId':unitId};
		//取得列表信息
		var url = 'dict/queryCommodity.do';
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
	
	//判断死否是数字
	function isNumber(s)  {   
		var strP=/^\d+$/;
		if(''==s||strP.test(s)) { 
			return true;
		}else{
			return false;
		}
	}
	
	var initCombobox = function(){
		$('#unit',editForm).combobox({
			width:250,
			data:TRN.getUnitList(),
			valueField:'unitId',
			textField:'unitName'
		});
		$('#commodityType',editForm).combobox({
			width:250,
			data:TRN.getCommodityTypeList(),
			valueField:'commodityTypeId',
			textField:'commodityTypeName'
		});
	};
	
	//添加
	var onAdd = function(){
		$(editForm).form('clear');
		$('#status',editDialog).combobox('setValue',1);
		initCombobox();
		$(editDialog).dialog('open');
	};
	//保存前的赋值操作
	var setValue = function(){
		var commodityName = $.trim($('#commodityName',editForm).val());
		if(''==commodityName){
			$.messager.alert('提示','请填写商品名称','warning');
			return false;
		}
		var commodityType = $('#commodityType',editForm).combobox('getValue');
		if(''==commodityType){
			$.messager.alert('提示','请填写商品分类','warning');
			return false;
		}
		var commodityTypeBoolean=isNumber(commodityType);
		if(commodityTypeBoolean==false){
			$.messager.alert('提示','请选择已有的商品分类','error');
			return false;
		}
		var unit = $('#unit',editForm).combobox('getValue');
		if(''==unit){
			$.messager.alert('提示','请填写商品单位','warning');
			return false;
		}
		var unitBoolean=isNumber(unit);
		if(unitBoolean==false){
			$.messager.alert('提示','请选择已有的商品单位','error');
			return false;
		}
		var purchasePrice=$.trim($('#purchasePrice',editForm).val());
		if(''==purchasePrice){
			$.messager.alert('提示','请填写商品进价','warning');
			return false;
		}
		var salePrice=$.trim($('#salePrice',editForm).val());
		if(''==salePrice){
			$.messager.alert('提示','请填写商品售价','warning');
			return false;
		}
		var qtyWarn=$.trim($('#qtyWarn',editForm).val());
		if(''==qtyWarn){
			$.messager.alert('提示','请填写商品预警数量','warning');
			return false;
		}
		return true;
	};
	//保存
	var onSave = function(){
		$(editForm).form('submit',{
			url: 'dict/saveCommodity.do',
			onSubmit: function(){
				return setValue();
			},
			success: function(data){
				var result = eval('('+data+')');
				if(result.isSuccess){
					var fn = function(){
						var commodityId = $.trim($('#commodityId',editForm).val());
						if(commodityId==''){//新增
							var  data = result.data;
							$('#commodityId',editForm).val(data.commodityId);
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
		var url = "dict/getTotalCountCommodity.do";
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
		$(editForm).form('load',selectRow);
		$('#unit',editForm).combobox('setText',selectRow.unitName);
		$('#commodityType',editForm).combobox('setText',selectRow.commodityTypeName);
		$('#unit',editForm).combobox('setValue',selectRow.unitId);
		$('#commodityType',editForm).combobox('setValue',selectRow.commodityTypeId);
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
					var url = "dict/deleteCommodity.do";
					var content = {commodityId:selectRow.commodityId};
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
					var url = 'dict/updateStatusCommodity.do';
					var content ={commodityId:selectRow.commodityId,status:status};
					asyncCallService(url,content,function(result){
						if(result.isSuccess){
							var fn = function(){
								search(true);
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