// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.storeTuneOutInit = function() {
	var $this = $(this);
	var id = $(this).attr('id');
    var height = $(document.body).height();  

    var storeTuneOutList = $('#storeTuneOutList',$this);
    var searchBtn = $('#searchBtn',$this);
    var pager = $('#pager',$this);
	var pageNumber = 1;
	var pageSize = 10;
    var selectRow = null;
    var selectIndex=null;

    var storeDialog = $('#storeDialog',$this);
    var storeList = $('#storeList',storeDialog);
    var storeSearchBtn = $('#storeSearchBtn',storeDialog);
    var storePager = $('#storePager',storeDialog);
	var storePageNumber = 1;
	var storePageSize = 10;
	var storeIndex=null;
	var storeRow=null;

	var editDialog = $('#editDialog',$this);
    var editForm = $('#editForm',editDialog);
    
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
  
  //查询调拨单
	$(searchBtn).click(function(){
		searchBtn(true);
	});
	//查询调拨单
	var searchBtn = function(flag){
		var beginDate = $.trim($('#beginDate',queryContent).val());
		var endDate = $.trim($('#endDate',queryContent).val());
		var commodityName = $.trim($('#commodityNameSearch',queryContent).val());
		var content = {'commodity.commodityName':commodityName,beginDate:beginDate,endDate:endDate,page:pageNumber,rows:pageSize};
		//取得列表信息
		var url = 'warehouse/queryStoreTuneOut.do';
		var result = syncCallService(url,content);
		if(result.isSuccess){
			var data = result.data;
			var datagridData = eval("("+data.datagridData+")");
			$(storeTuneOutList).datagrid('loadData',datagridData);
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
		var url = "warehouse/getTotalCountStoreTuneOut.do";
		asyncCallService(url,content,
		function(result){
			if(result.isSuccess){
				var data = result.data;
				$(pager).pagination({  
					pageNumber:1,
					total:data.total
				});
				total=data.total;
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
	  $(storeTuneOutList).datagrid({
		method:"POST",
		nowrap:true,
		striped: true,
		collapsible:true,
		rownumbers:true,
		selectOnCheck:false,
		checkOnSelect:false,
		singleSelect:true,
		fit:true,
		toolbar:[	
			{text:'添加',iconCls:'icon-add',handler:function(){onAdd();}},
			{text:'修改',iconCls:'icon-edit',handler:function(){onUpdate();}},
			{text:'删除',iconCls:'icon-remove',handler:function(){onRemove();}}
				],
		columns:[[
		    {field:'ck',width:50,checkbox:true},
			{field:'commodityName',title:'商品名称',width:100,align:"center"},
			{field:'tuneOutWarehouseName',title:'调出仓库',width:100,align:"center"},
			{field:'tuneInWarehouseName',title:'调入仓库',width:100,align:"center"},
			{field:'tuneOutDate',title:'调拨日期',width:100,align:"center"},
			{field:'qty',title:'数量',width:100,align:"center"},
			{field:'note',title:'备注',width:100,align:"center"},
			{field:'userName',title:'经办人',width:100,align:"center"}
		]],
		onSelect:function(rowIndex, rowData){
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
	//添加调拨单
	var onAdd = function(){
		initCombobox();
		storeSearch(true);
		$(storeDialog).dialog('open');
	};
	
	//初始化Combobox
	var initCombobox = function(){
		$('#commodityTypeSearch',storeDialog).combobox({
			 width:150,
			 data:TRN.getCommodityTypeList(),
			 valueField:'commodityTypeId',
			 textField:'commodityTypeName'
		});
		$('#warehouseSearch',storeDialog).combobox({
			width:150,
			data:TRN.getWarehouseList(),
			valueField:'warehouseId',
			textField:'warehouseName'
		});
	};
	
	//修改调拨单
	var onUpdate = function(){
		if(selectRow==null){
			$.messager.alert("提示","请选择一条数据航","warning");
			return;
		}
		var index=$(storeTuneOutList).datagrid("getRowIndex",selectRow);
		if(index==0){
			$('#pre'+id,editDialog).linkbutton('disable');
		}else{
			$('#pre'+id,editDialog).linkbutton('enable');
		}
		if(index==total-1){
			$('#next'+id,editDialog).linkbutton('disable');
		}else{
			$('#next'+id,editDialog).linkbutton('enable');
		}
		initTuneOutCombobox();
		onOpen(selectRow);
		$(editDialog).dialog('open');
	};
	//打开
	var onOpen = function(row){
		$(editForm).form('load',row);
		$('#storeTuneOutId',editDialog).val(row.storeTuneOutId);
		$('#commodity',editDialog).val(row.commodityId);
		$('#user',editForm).combobox('setText',row.userName);
		$('#tuneOutWarehouse',editDialog).combobox('setText',row.tuneOutWarehouseName);
		$('#tuneInWarehouse',editDialog).combobox('setText',row.tuneInWarehouseName);
		$('#tuneOutWarehouse',editDialog).combobox('setValue',row.tuneOutWarehouseId);
		$('#tuneInWarehouse',editDialog).combobox('setValue',row.tuneInWarehouseId);
		$('#user',editForm).combobox('setValue',row.userId);
		
	};
	//删除调拨单
	var onRemove = function(){
		var rows = $(storeTuneOutList).datagrid('getChecked');
		if(rows.length==0){
			$.messager.alert("提示","请选择一条以上数据行","warning");
			return;
		}
		var storeTuneOutIds=new Array();
		for(var i=0;i<rows.length;i++){
			storeTuneOutIds.push(rows[i].storeTuneOutId);
		}
		$.messager.confirm('提示','确定要删除吗?',function(r){
			if(r){
				var url = 'warehouse/deleteStoreTuneOut.do';
				var content={storeTuneOutIds:storeTuneOutIds.join(CSIT.join)};
				var result = syncCallService(url,content);
				if(result.isSuccess){
					var fn = function(){
						searchBtn(true);
					};
					$.messager.alert('提示','删除成功','info',fn);
				}else{
					$.messager.alert('提示',result.message,'error');
				}
			}
		});
	};
	
	//库存列表
	$(storeDialog).dialog({  
		title: '调拨',  
		width:900,
		height:height-10,
		closed: true,  
		cache: false,  
		modal: true,
		closable:false,
		toolbar:[{
			text:'调拨商品',
			iconCls:'icon-ok',
			handler:function(){
				onTuneOut();
			}
		},'-',{
			text:'退出',
			iconCls:'icon-cancel',
			handler:function(){
				$(storeList).datagrid('loadData',{rows:[],total:0});
				storeIndex = null;
				selectIndex=null;
				$(storeDialog).dialog('close');
			}
		}]
	}); 
	//调拨商品
	function onTuneOut(){
		if(storeRow==null){
			$.messager.alert('提示','请选择数据行','error');
			return;
		}
		if(storeRow.qtyStore==0){
			$.messager.alert('提示','仓库'+storeRow.warehouseName+'库存不足，无法进行调拨','error');
			return;
		}
		initTuneOutCombobox();
		
		$('#commodity',editDialog).val(storeRow.commodityId);
		$('#qty',editDialog).val(storeRow.qtyStore);
		$('#pre'+id,editDialog).linkbutton('disable');
		$('#next'+id,editDialog).linkbutton('disable');
		$('#tuneOutWarehouse',editDialog).combobox('setValue',storeRow.warehouseId);
		$('#tuneOutWarehouse',editDialog).combobox('setText',storeRow.warehouseName);
		
		$(editDialog).dialog('open');
	};
	//保存
	function initTuneOutCombobox(){
		$('#tuneOutWarehouse',editDialog).combobox({
			width:250,
			data:TRN.getWarehouseList(),
			valueField:'warehouseId',
			textField:'warehouseName',
			disabled:true
		});
		$('#tuneInWarehouse',editDialog).combobox({
			width:250,
			data:TRN.getWarehouseList(),
			valueField:'warehouseId',
			textField:'warehouseName'
		});
		$('#user',editDialog).combobox({
			width:250,
			data:TRN.getEmployeeList(),
			valueField:'userId',
			textField:'userName'
		});
	};
	
	//查询库存单
	$(storeSearchBtn).click(function(){
		storeSearch(true);
	});
	
	//查询库存
	var storeSearch = function(flag){
		var commodityName = $.trim($('#commodityNameSearch',storeDialog).val());
		var commodityType = $('#commodityTypeSearch',storeDialog).combobox('getValue');
		var warehouse =$('#warehouseSearch',storeDialog).combobox('getValue');
		var content = {"commodity.commodityName":commodityName,'commodity.commodityType.commodityTypeId':commodityType,"warehouse.warehouseId":warehouse,page:storePageNumber,rows:storePageSize};
		
		if(!isNumber(commodityType)){
			$.messager.alert('提示','请选择已有的商品分类','error');
			return;
		}
		if(!isNumber(warehouse)){
			$.messager.alert('提示','请选择已有的仓库','error');
			return;
		}
		
		//取得列表信息
		var url = 'warehouse/queryNotEmptyStore.do';
		var result = syncCallService(url,content);
		if(result.isSuccess){
			var data = result.data;
			var datagridData = eval("("+data.datagridData+")");
			$(storeList).datagrid('loadData',datagridData);
			//需要重新重新分页信息
			if(flag){
				getStoreTotal(content);
			}
		}else{
			$.messager.alert('提示',result.message,'error');
		}
	};
		
	//商品分页条
	$(storePager).pagination({   
	    onSelectPage: function(page, rows){
			storePageNumber = page;
			storePageSize = rows;
			storeSearchBtn();
	    }
	});
	//商品页面统计总数
	var getStoreTotal = function(content){
		var url = "warehouse/getTotalCountNotEmptyStore.do";
		asyncCallService(url,content,
		function(result){
			if(result.isSuccess){
				var data = result.data;
				$(storePager).pagination({  
					pageNumber:1,
					total:data.total
				});
			}else{
				$.messager.alert('提示',result.message,"error");
			}
		});
	};
	
	//仓库列表
	$(storeList).datagrid({
		fit:true,
		singleSelect:true,
		method:"POST",
		nowrap:true,
		striped: true,
		collapsible:true,
		rownumbers:true,
		fit:true,
		columns:[[
		    {field:'storeId',hidden:true},
			{field:'commodityName',title:'商品名称',width:100,align:"center"},
			{field:'warehouseName',title:'仓库名称',width:100,align:"center"},
			{field:'qtyStore',title:'库存数量',width:100,align:"center"}
		]],
		onClickRow:function(rowIndex,rowData){
			storeRow = rowData;
			storeIndex = rowIndex;
		},
		onLoadSuccess:function(data){
			storeRow = null;
			storeIndex = null;
			storePageNumber = 1;
		}
	});
	
	//编辑调拨单
	$(editDialog).dialog({  
		title: '编辑调拨单',  
		width:400,
		height:350,
		closed: true,  
		cache: false,  
		modal: true,
		closable:false,
		toolbar:[{
			text:'执行调拨',
			iconCls:'icon-save',
			handler:function(){
				onTuneOutSave();
			}
		},'-',{
			id:'pre'+id,
			text:'上一条',
			iconCls:'icon-left',
			handler:function(){
				onOpenIndex(-1);
			}
		},'-',{
			id:'next'+id,
			text:'下一条',
			iconCls:'icon-right',
			handler:function(){
				onOpenIndex(1);
			}
		},'-',{
			text:'退出',
			iconCls:'icon-cancel',
			handler:function(){
				$(editForm).form('clear');
				$(editDialog).dialog('close');
			}
		}]
	});
	//添加调拨单
	function onTuneOutSave()  {   
		$(editForm).form('submit',{
			url: 'warehouse/saveStoreTuneOut.do',
			onSubmit: function(){
				return setValue();
			},
			success: function(data){
				var result = eval('('+data+')');
				if(result.isSuccess){
					var fn = function(){
						var storeTuneOutId = $.trim($('#storeTuneOutId',editForm).val());
						if(storeTuneOutId==''){//新增
							var  data = result.data;
							$('#storeTuneOutId',editForm).val(data.storeTuneOutId);
						}
						$(editForm).form('clear');
						$(editDialog).dialog('close');
						storeSearch(true);
					};
					$.messager.alert('提示','保存成功','info',fn);
				}else{
					$.messager.alert('提示',result.message,"error");
				}
			}
		});
	}
	//保存前的赋值操作
	var setValue = function(){
		//调入仓库
		var tuneInWarehouse = $('#tuneInWarehouse',editDialog).combobox('getValue');
		if(tuneInWarehouse==''){
			$.messager.alert('提示','请选择调入仓库','warning');
			return false;
		}
		if(!isNumber(tuneInWarehouse)){
			$.messager.alert('提示','请选择已有的调入仓库','warning');
			return false;
		}
		if(tuneInWarehouse==$('#tuneOutWarehouse',editDialog).combobox('getValue')){
			$.messager.alert('提示','调入仓库不能和调出仓库一样','warning');
			return false;
		}
		//调拨日期
		var tuneOutDate = $('#tuneOutDate',editDialog).val();
		if(tuneOutDate==''){
			$.messager.alert('提示','请填写调拨日期','warning');
			return false;
		}
		//调拨数量
		var qty = $('#qty',editDialog).val();
		if(qty==''){
			$.messager.alert('提示','请填写调拨数量','warning');
			return false;
		}
		//经办人
		var user = $('#user',editDialog).combobox('getValue');
		if(user==''){
			$.messager.alert('提示','请选择经办人','warning');
			return false;
		}
		return true;
	};
	//上下一条
	function onOpenIndex(index){
		if(selectIndex+index==0){
			$('#pre'+id,editDialog).linkbutton('disable');
		}else{
			$('#pre'+id,editDialog).linkbutton('enable');
		}
		if(selectIndex+index==total-1){
			$('#next'+id,editDialog).linkbutton('disable');
		}else{
			$('#next'+id,editDialog).linkbutton('enable');
		}
		$(storeTuneOutList).datagrid('unselectRow',selectIndex);
		$(storeTuneOutList).datagrid('selectRow',selectIndex+index);
		
		onOpen(selectRow);
	};
  };
})(jQuery);   