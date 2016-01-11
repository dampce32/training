// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.scrappedInit = function() {
	  var $this = $(this);
	  var id = $(this).attr('id');
	  var height = $(document.body).height();
	  
	  var scrappedViewList = $('#scrappedViewList',$this);
	  var editDialog = $('#editDialog',$this);
	  var editForm = $('#editForm',editDialog);
	  var searchBtn = $('#searchBtn',$this);
	  var selectRow = null;
	  var selectIndex=null;
	  
	  var pager = $('#pager',$this);
	  var pageNumber = 1;
	  var pageSize = 10;
	  
	  var commodityList = $('#commodityList',$this);
	  var commodityDialog = $('#commodityDialog',$this);
	  var commoditySearchBtn = $('#commoditySearchBtn',commodityDialog);
	  
	  var commodityPager = $('#commodityPager',$this);
	  var commodityPageNumber = 1;
	  var commodityPageSize = 10;
	  
	  var detailViewList = $('#detailViewList',editDialog);
	  
	  var oldScrappedDetailIdArray=new Array();
	  var detailIndex=null;
	  var total=null;
	  
	//查询入库单
	$(searchBtn).click(function(){
		searchBtn(true);
	});
	//查询入库单
	var searchBtn = function(flag){
		var beginDate = $.trim($('#beginDate',queryContent).val());
		var endDate = $.trim($('#endDate',queryContent).val());
		var content = {beginDate:beginDate,endDate:endDate,page:pageNumber,rows:pageSize};
		//取得列表信息
		var url = 'warehouse/queryScrapped.do';
		var result = syncCallService(url,content);
		if(result.isSuccess){
			var data = result.data;
			var datagridData = eval("("+data.datagridData+")");
			$(scrappedViewList).datagrid('loadData',datagridData);
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
		var url = "warehouse/getTotalCountScrapped.do";
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
	
	//加载列表
	  $(scrappedViewList).datagrid({
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
			{text:'添加',iconCls:'icon-add',handler:function(){onAdd();}},'-',
			{text:'修改',iconCls:'icon-edit',handler:function(){onUpdate();}},'-',
			{text:'删除',iconCls:'icon-remove',handler:function(){onRemove();}}
				],
		columns:[[
		    {field:'ck',width:50,checkbox:true},
		    {field:'scrappedType',title:'类型',width:100,align:"center",
		    	formatter : function(value) {
					if (value == 0) {
						return '报废';
					} else{
						return '溢出';
					}
				}
		    },
			{field:'scrappedDate',title:'日期',width:100,align:"center"},
			{field:'qtyTotal',title:'总数量',width:100,align:"center"},
			{field:'userName',title:'经办人',width:100,align:"center"},
			{field:'note',title:'备注',width:100,align:"center"}
			
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
	  
	//添加入库单
	var onAdd = function(){
		$(detailViewList).datagrid('loadData',{rows:[],total:0});
		$(editForm).form('clear');
		$('#pre'+id,editDialog).linkbutton('disable');
		$('#next'+id,editDialog).linkbutton('disable');
		initCombobox();
		$(editDialog).dialog('open');
	};
	//修改入库单
	var onUpdate = function(){
		if(selectRow==null){
			$.messager.alert("提示","请选择一条数据航","warning");
			return;
		}
		var index=$(scrappedViewList).datagrid("getRowIndex",selectRow);
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
		initCombobox();
		onOpen(selectRow);
		$(editDialog).dialog('open');
	};
	//打开
	var onOpen = function(row){
		$(editForm).form('load',row);
		$('#user',editForm).combobox('setText',row.userName);
		$('#user',editForm).combobox('setValue',row.userId);
		$('input:radio[name="scrappedType"]').attr('disabled','disabled');
		//取得列表信息
		var url = 'warehouse/queryDetailScrapped.do';
		var content={scrappedId:row.scrappedId};
		var result = syncCallService(url,content);
		if(result.isSuccess){
			var data = result.data;
			var datagridData = eval("("+data.datagridData+")");
			$(detailViewList).datagrid('loadData',datagridData);
		}else{
			$.messager.alert('提示',result.message,'error');
		}
	};
	//删除入库单
	var onRemove = function(){
		var rows = $(scrappedViewList).datagrid('getChecked');
		if(rows.length==0){
			$.messager.alert("提示","请选择一条以上数据行","warning");
			return;
		}
		var scrappedIds=new Array();
		for(var i=0;i<rows.length;i++){
			scrappedIds.push(rows[i].scrappedId);
		}
		$.messager.confirm('提示','确定要删除吗?',function(r){
			if(r){
				var url = 'warehouse/deleteScrapped.do';
				var content={scrappedIds:scrappedIds.join(CSIT.join)};
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

	//编辑入库单
	$(editDialog).dialog({  
		title: '编辑损溢单',  
		width:900,
		height:height-10,
		closed: true,  
		cache: false,  
		modal: true,
		closable:false,
		toolbar:[{
			text:'保存',
			iconCls:'icon-save',
			handler:function(){
				onSaveReceive();
			}
		},'-',{
			id:'add'+id,
			text:'新增',
			iconCls:'icon-add',
			handler:function(){
				onAddReceive();
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
				$(detailViewList).datagrid('loadData',{rows:[],total:0});
				detailIndex = null;
				selectIndex=null;
				$(editForm).form('clear');
				$(editDialog).dialog('close');
			}
		}]
	}); 
	
	//添加前赋值操作
	function setValue(){
		var commodityIds = new Array();
		var warehouseDetailIds = new Array();
		var scrappedDetailIds = new Array();
		var qtys = new Array();

		//入库日期
		var ScrappedDate=$('#scrappedDate',editForm).val();
		if(ScrappedDate==""||ScrappedDate==null){
			$.messager.alert('提示','请填写日期','error');
			return false;
		}
		//经办人
		var user=$('#user',editForm).combobox('getValue');
		var userIsNumber=isNumber(user);
		if(userIsNumber==false){
			$.messager.alert('提示','选择已有的经办人','error');
			return false;
		}
		
		$(detailViewList).datagrid('endEdit', detailIndex);
		$(detailViewList).datagrid('unselectAll');
		detailIndex=null;
		var rows =  $(detailViewList).datagrid('getRows');
		if(rows.length==0){
			$.messager.alert('提示','请添加商品','error');
			return false;
		}
		for ( var i = 0; i < rows.length; i++) {
			var row=rows[i];
			commodityIds.push(row.commodityId);
			//商品数量
			if(row.qty==""||row.qty==null){
				$.messager.alert('提示','第'+(i+1)+'行数据没有填写数量','error');
				return false;
			}
			qtys.push(row.qty);
			//商品存放仓库
			if(row.warehouseId==""||row.warehouseId==null){
				$.messager.alert('提示','第'+(i+1)+'行数据没有选择仓库','error');
				return false;
			}
			var warehouseIsNumber=isNumber(row.warehouseId);
			if(warehouseIsNumber==false){
				$.messager.alert('提示','第'+(i+1)+'行数据没有选择已有的仓库','error');
				return false;
			}
			scrappedDetailIds.push(row.scrappedDetailId);
			warehouseDetailIds.push(row.warehouseId);
		}
		var delScrappedDetailIdArray=new Array();
		//统计原记录中被删除的记录
		for ( var i = 0; i < oldScrappedDetailIdArray.length; i++) {
			var haveDel = true;
			for(var j=0;j<rows.length;j++){
				if(oldScrappedDetailIdArray[i]==rows[j].scrappedDetailId){
					haveDel = false;
					break;
				}
			}
			if(haveDel){
				delScrappedDetailIdArray.push(oldScrappedDetailIdArray[i]);
			}
		}
		$('#delScrappedIds',editForm).val(delScrappedDetailIdArray.join(CSIT.join));
		$('#scrappedDetailIds',editForm).val(scrappedDetailIds.join(CSIT.join));
		$('#commodityIds',editForm).val(commodityIds.join(CSIT.join));
		$('#warehouseDetailIds',editForm).val(warehouseDetailIds.join(CSIT.join));
		$('#qtys',editForm).val(qtys.join(CSIT.join));
		return true;
	};
	//保存
	function onSaveReceive(){
		
		$(editForm).form('submit',{
			url: 'warehouse/saveScrapped.do',
			onSubmit: function(){
				return setValue();
			},
			success: function(data){
				var result = eval('('+data+')');
				if(result.isSuccess){
					var fn = function(){
						var scrappedId = $.trim($('#scrappedId',editForm).val());
						if(scrappedId==''){//新增
							var  data = result.data;
							$('#wcrappedId',editForm).val(data.wcrappedId);
						}
					};
					$.messager.alert('提示','保存成功','info',fn);
				}else{
					$.messager.alert('提示',result.message,"error");
				}
			}
		});
	};
	//新增
	function onAddReceive(){
		$(detailViewList).datagrid('loadData',{rows:[],total:0});
		$(editForm).form('clear');
		$('#pre'+id,editDialog).linkbutton('disable');
		$('#next'+id,editDialog).linkbutton('disable');
		initCombobox();
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
		$(scrappedViewList).datagrid('unselectRow',selectIndex);
		$(scrappedViewList).datagrid('selectRow',selectIndex+index);
		
		onOpen(selectRow);
	};
	
	//编辑入库单详细
	$(commodityDialog).dialog({  
		title: '选择商品',  
		width:900,
		height:height-10,
		closed: true,  
		cache: false,  
		modal: true,
		closable:false,
		toolbar:[{
			text:'选择',
			iconCls:'icon-ok',
			handler:function(){
				onCommoditySave();
			}
		},'-',{
			text:'退出',
			iconCls:'icon-cancel',
			handler:function(){
				detailIndex==null;
				$(commodityList).datagrid('loadData',{rows:[],total:0});
				$(commodityDialog).dialog('close');
			}
		}]
	}); 
	var warehouses=TRN.getWarehouseList();
	function warehouseFormatter(value){
		for(var i=0; i<warehouses.length; i++){
			if (warehouses[i].warehouseId == value) return warehouses[i].warehouseName;
		};
		return value;
	};
	//入库单详细
	$(detailViewList).datagrid({
		fit:true,
		singleSelect:true,
		method:"POST",
		nowrap:true,
		striped: true,
		collapsible:true,
		rownumbers:true,
		fit:true,
		toolbar:[	
			{text:'添加商品',iconCls:'icon-add',handler:function(){onAddCommodity();}},'-',
			{text:'删除商品',iconCls:'icon-remove',handler:function(){onDelCommodity();}}
				],
		columns:[[
		    {field:'commodityId',hidden:true},
		    {field:'ScrappedDetailId',hidden:true},
			{field:'commodityName',title:'商品名称',width:100,align:"center"},
			{field:'commodityTypeName',title:'商品分类',width:100,align:"center"},
			{field:'unitName',title:'商品单位',width:100,align:"center"},
			{field:'qty',title:'数量',width:100,align:"center",
				editor:{
					type:'numberbox',
					options:{
						onChange:function(newValue,oldValue){
							var qtyTotal = 0 ;
							var rows =  $(detailViewList).datagrid('getRows');
							var row = $(detailViewList).datagrid('getSelected');
							var rowIndex = $(detailViewList).datagrid('getRowIndex',row); 
							for ( var i = 0; i < rows.length; i++) {
								if(i!=rowIndex){
									row = rows[i];
									if(row.qty==''||row.qty==null){
										qtyTotal += 0;
									}else{
										qtyTotal += parseFloat(row.qty);
									}
								}
							}
							qtyTotal+=parseFloat(newValue);
				    		$('#qtyTotal',editForm).val(qtyTotal);
						}
					}
				}
			},
			{field:'warehouseId',title:'仓库',width:173,align:"center",formatter:warehouseFormatter,
				editor:{
					type:'combobox',
					options:{
						valueField:'warehouseId',
						textField:'warehouseName',
						data:warehouses
					}
				}}
		]],
		onClickRow:function(rowIndex,rowData){
			if (detailIndex != rowIndex){
				$(this).datagrid('endEdit', detailIndex);
				$(this).datagrid('beginEdit', rowIndex);
			}
			detailIndex = rowIndex;
		},
		onLoadSuccess:function(data){
			var dataRows = data.rows;
			oldScrappedDetailIdArray = new Array();
			for ( var i = 0; i < dataRows.length; i++) {
				oldScrappedDetailIdArray.push(dataRows[i].scrappedDetailId);
			}
		}
	});
	
	
	//入库单详细的商品
	  $(commodityList).datagrid({
		fit:true,
		method:"POST",
		nowrap:true,
		striped: true,
		collapsible:true,
		rownumbers:true,
		fit:true,
		columns:[[
		    {field:'ck',width:50,checkbox:true},
		    {field:'commodityId',hidden:true},
			{field:'commodityName',title:'商品名称',width:100,align:"center"},
			{field:'commodityTypeName',title:'商品分类',width:100,align:"center"},
			{field:'unitName',title:'商品单位',width:100,align:"center"},
			{field:'qtyStore',title:'库存数量',width:100,align:"center"},
			{field:'note',title:'备注',width:100,align:"center"}
		]]
	  });
	  
	//初始化Combobox
	var initCombobox = function(){
		$('#user',editForm).combobox({
			width:250,
			data:TRN.getEmployeeList(),
			valueField:'userId',
			textField:'userName'
		});
	};
		
	//添加商品
	var onAddCommodity = function(){
		$('#unitSearch',commodityQueryContent).combobox({
			width:150,
			data:TRN.getUnitList(),
			valueField:'unitId',
			textField:'unitName'
	  });
	  $('#commodityTypeSearch',commodityQueryContent).combobox({
			width:150,
			data:TRN.getCommodityTypeList(),
			valueField:'commodityTypeId',
			textField:'commodityTypeName'
	  });
		$(commodityList).datagrid('loadData',{rows:[],total:0});
		$(commodityDialog).dialog('open');
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
	//商品分页条
	$(commodityPager).pagination({   
	    onSelectPage: function(page, rows){
	    	commodityPageNumber = page;
	    	commodityPageSize = rows;
			search();
	    }
	});
	
	//商品页面统计总数
	var getCommodityTotal = function(content){
		var url = "dict/getTotalCountCommodity.do";
		asyncCallService(url,content,
		function(result){
			if(result.isSuccess){
				var data = result.data;
				$(commodityPager).pagination({  
					commodityPageNumber:1,
					total:data.total
				});
			}else{
				$.messager.alert('提示',result.message,"error");
			}
		});
	};	
	
	//查询商品
	$(commoditySearchBtn).click(function(){
		commoditySearch(true);
	});
	//查询商品
	var commoditySearch = function(flag){
		var commodityTypeId = $('#commodityTypeSearch',commodityQueryContent).combobox('getValue');
		var unitId = $('#unitSearch',commodityQueryContent).combobox('getValue');
		var commodityName = $.trim($('#commodityName',commodityQueryContent).val());
		var commodityTypeBoolean=isNumber(commodityTypeId);
		var unitBoolean=isNumber(unitId);
		var commodityIds = new Array();
		
		if(commodityTypeBoolean==false){
			$.messager.alert('提示','请选择已有的商品分类','error');
			return;
		}
		if(unitBoolean==false){
			$.messager.alert('提示','请选择已有的商品单位','error');
			return;
		}
		
		var rows =  $(detailViewList).datagrid('getRows');
		for ( var i = 0; i < rows.length; i++) {
			var row=rows[i];
			commodityIds.push(row.commodityId);
		}
		
		var content = {commodityName:commodityName,commodityIds:commodityIds.join(CSIT.join),status:1,page:commodityPageNumber,rows:commodityPageSize,'commodityType.commodityTypeId':commodityTypeId,'unit.unitId':unitId};
		//取得列表信息
		var url = 'dict/queryCommodity.do';
		var result = syncCallService(url,content);
		if(result.isSuccess){
			var data = result.data;
			var datagridData = eval("("+data.datagridData+")");
			$(commodityList).datagrid('loadData',datagridData);
			//需要重新重新分页信息
			if(flag){
				getCommodityTotal(content);
			}
		}else{
			$.messager.alert('提示',result.message,'error');
		}
	};
	//添加商品
	function onCommoditySave()  {   
		var rows = $(commodityList).datagrid('getSelections');
		if(rows.length==0){
			$.messager.alert("提示",'请至少选择一条数据行',"warning");
			return;
		}
		for(var i=0;i<rows.length;i++){
			var row=rows[i];
			row.price=row.purchasePrice;
			$(detailViewList).datagrid('appendRow',row);
		}
		
		$(commodityList).datagrid('loadData',{rows:[],total:0});
		$(commodityDialog).dialog('close');
	}
	//删除商品
	function onDelCommodity()  {   
		if(detailIndex==null){
			$.messager.alert("提示",'请选择数据行',"warning");
			return;
		}
		$.messager.confirm('提示','确定要删除吗?',function(r){
			if(r){
				$(detailViewList).datagrid('deleteRow',detailIndex);
				detailIndex = null;
				$(detailViewList).datagrid('unselectAll');
				
				//统计删除后的总金额
				 var totalAmount = 0 ;
				 var rows =  $(detailViewList).datagrid('getRows');
				 for ( var i = 0; i < rows.length; i++) {
					var row = rows[i];
					if(row.totalPrice==""||row.totalPrice==null){
						row.totalPrice=0;
					}
					totalAmount += parseFloat(row.totalPrice);
				}
				$('#amountTotal',editForm).val(totalAmount);
				
				//统计删除后的总数量
				var qtyTotal = 0 ;
				var rows =  $(detailViewList).datagrid('getRows');
				for ( var i = 0; i < rows.length; i++) {
					var row = rows[i];
					if(row.qty==''||row.qty==null){
						row.qty=0;
					}
					qtyTotal += parseFloat(row.qty);
				}
				$('#qtyTotal',editForm).val(qtyTotal);
			}
		});
	}
  };
})(jQuery);   