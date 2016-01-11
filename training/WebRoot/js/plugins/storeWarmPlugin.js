// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.storeWarmInit = function() {
	var $this = $(this);
	
	var queryContent = $('#queryContent',$this);
	var qtyWarmList = $('#qtyWarmList',$this);
	var searchBtn = $('#searchBtn',$this);  
	var pager = $('#pager',$this);
	var pageNumber = 1;
	var pageSize = 10;
	 
	 $('#unitSearch',queryContent).combobox({
			width:150,
			data:TRN.getUnitList(),
			valueField:'unitId',
			textField:'unitName'
		});
	$('#commodityTypeSearch',queryContent).combobox({
		  width:150,
		  data:TRN.getCommodityTypeList(),
		  valueField:'commodityTypeId',
		  textField:'commodityTypeName'
	});
	
	//判断死否是数字
	function isNumber(s)  {   
		var strP=/^\d+$/;
		if(''==s||strP.test(s)) { 
			return true;
		}else{
			return false;
		}
	}
	
	//查询
	$(searchBtn).click(function(){
		search(true);
	});
	
	//查询库存
	var search = function(flag){
		var commodityTypeId = $('#commodityTypeSearch',queryContent).combobox('getValue');
		var unitId = $('#unitSearch',queryContent).combobox('getValue');
		var commodityName = $.trim($('#commodityNameSearch',queryContent).val());
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
		var url = 'dict/queryQtyWarmCommodity.do';
		var result = syncCallService(url,content);
		if(result.isSuccess){
			var data = result.data;
			var datagridData = eval("("+data.datagridData+")");
			$(qtyWarmList).datagrid('loadData',datagridData);
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
			init(true);
	    }
	});
	//商品页面统计总数
	var getTotal = function(content){
		var url = "dict/getTotalCountQtyWarmCommodity.do";
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
		//加载列表
	  $(qtyWarmList).datagrid({
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
			{field:'qtyStore',title:'库存总量',width:100,align:"center"},
			{field:'qtyWarn',title:'库存报警量',width:100,align:"center"}
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