// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.storeInit = function() {
	var $this = $(this);
	  
	var storeList = $('#storeList',$this);
	var searchBtn = $('#searchBtn',$this);  
	var pager = $('#pager',$this);
	var pageNumber = 1;
	var pageSize = 10;
	 
	$('#commodityTypeSearch',queryContent).combobox({
		 width:120,
		 data:TRN.getCommodityTypeList(),
		 valueField:'commodityTypeId',
		 textField:'commodityTypeName'
	});
	$('#warehouseSearch',queryContent).combobox({
		width:120,
		data:TRN.getWarehouseList(),
		valueField:'warehouseId',
		textField:'warehouseName'
	});
	  
	//查询库存
	$(searchBtn).click(function(){
		searchBtn(true);
	});
	//查询库存
	var searchBtn = function(flag){
		var commodityName = $.trim($('#commodityNameSearch',queryContent).val());
		var commodityType = $.trim($('#commodityTypeSearch',queryContent).combobox('getValue'));
		var warehouse = $.trim($('#warehouseSearch',queryContent).combobox('getValue'));
		var content = {"commodity.commodityName":commodityName,'commodity.commodityType.commodityTypeId':commodityType,"warehouse.warehouseId":warehouse,page:pageNumber,rows:pageSize};
		
		if(!isNumber(commodityType)){
			$.messager.alert('提示','请选择已有的商品分类','error');
			return;
		}
		if(!isNumber(warehouse)){
			$.messager.alert('提示','请选择已有的仓库','error');
			return;
		}
		
		//取得列表信息
		var url = 'warehouse/queryStore.do';
		var result = syncCallService(url,content);
		if(result.isSuccess){
			var data = result.data;
			var datagridData = eval("("+data.datagridData+")");
			$(storeList).datagrid('loadData',datagridData);
			//需要重新重新分页信息
			if(flag){
				getTotal(content);
			}
		}else{
			$.messager.alert('提示',result.message,'error');
		}
	};
		
	//商品分页条
	$(pager).pagination({   
	    onSelectPage: function(page, rows){
			pageNumber = page;
			pageSize = rows;
			search();
	    }
	});
	//商品页面统计总数
	var getTotal = function(content){
		var url = "warehouse/getTotalCountStore.do";
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
	
	//判断死否是数字
	function isNumber(s)  {   
		var strP=/^\d+$/;
		if(''==s||strP.test(s)) { 
			return true;
		}else{
			return false;
		}
	}
	
		//加载列表
	  $(storeList).datagrid({
		singleSelect:true,
		method:"POST",
		nowrap:true,
		striped: true,
		collapsible:true,
		rownumbers:true,
		fit:true,
		columns:[[
			{field:'commodityName',title:'商品',width:100,align:"center"},
			{field:'unitName',title:'单位',width:100,align:"center"},
			{field:'commodityTypeName',title:'分类',width:100,align:"center"},
			{field:'qtyTotal',title:'总量',width:100,align:"center"},
			{field:'qtyStore',title:'库存量',width:100,align:"center"},
			{field:'warehouseName',title:'仓库',width:100,align:"center"},
			{field:'qtyWarn',title:'报警量',width:100,align:"center"}
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
  };
})(jQuery);   