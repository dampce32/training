// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.rejectInit = function() {
	  var $this = $(this);
	  var id = $(this).attr('id');
	  var height = $(document.body).height();
	  
	  var rejectViewList = $('#rejectViewList',$this);
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
	  
	  var oldReceiveDetailIdArray=new Array();
	  var detailIndex=null;
	  var total=null;
	  
	  //查询出库单
		$(searchBtn).click(function(){
			searchBtn(true);
		});
		//查询出库单
		var searchBtn = function(flag){
			var beginDate = $.trim($('#beginDate',queryContent).val());
			var endDate = $.trim($('#endDate',queryContent).val());
			var recRejCode = $.trim($('#recRejCodeSearch',queryContent).val());
			var content = {recRejCode:recRejCode,beginDate:beginDate,endDate:endDate,page:pageNumber,rows:pageSize,recRejType:-1};
			//取得列表信息
			var url = 'warehouse/queryRecRej.do';
			var result = syncCallService(url,content);
			if(result.isSuccess){
				var data = result.data;
				var datagridData = eval("("+data.datagridData+")");
				$(rejectViewList).datagrid('loadData',datagridData);
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
			var url = "warehouse/getTotalCountRecRej.do";
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
	  $(rejectViewList).datagrid({
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
		    {field:'recRejCode',title:'单号',width:100,align:"center"},
			{field:'recRejDate',title:'进货日期',width:100,align:"center"},
			{field:'supplierName',title:'供应商',width:100,align:"center"},
			{field:'qtyTotal',title:'总数量',width:100,align:"center"},
			{field:'amountTotal',title:'总金额',width:100,align:"center"},
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
		  
		//添加出库单
		var onAdd = function(){
			$(detailViewList).datagrid('loadData',{rows:[],total:0});
			$(editForm).form('clear');
			$('#preReject'+id,editDialog).linkbutton('disable');
			$('#nextReject'+id,editDialog).linkbutton('disable');
			initRecRejCode();
			initCombobox();
			$(editDialog).dialog('open');
		};
		
		//初始化出库单编号
		function initRecRejCode()  {   
			var url='warehouse/initCodeRecRej.do';
			asyncCallService(url,null,function(result){
				if(result.isSuccess){
					$('#recRejCode',editForm).val(result.data.recRejCode);
				}else{
					$.messager.alert('提示',result.message,"error");
				}
			});
		}
		
		//初始化Combobox
		var initCombobox = function(){
			$('#supplier',editForm).combobox({
				 width:250,
				 data:TRN.getSupplierList(),
				 valueField:'supplierId',
				 textField:'supplierName'
			});
			$('#user',editForm).combobox({
				width:250,
				data:TRN.getEmployeeList(),
				valueField:'userId',
				textField:'userName'
			});
		};
		
		//修改出库单
		var onUpdate = function(){
			if(selectRow==null){
				$.messager.alert("提示","请选择一条数据航","warning");
				return;
			}
			var index=$(rejectViewList).datagrid("getRowIndex",selectRow);
			if(index==0){
				$('#preReject'+id,editDialog).linkbutton('disable');
			}else{
				$('#preReject'+id,editDialog).linkbutton('enable');
			}
			if(index==total-1){
				$('#nextReject'+id,editDialog).linkbutton('disable');
			}else{
				$('#nextReject'+id,editDialog).linkbutton('enable');
			}
			initCombobox();
			onOpen(selectRow);
			$(editDialog).dialog('open');
		};
		//打开
		var onOpen = function(row){
			$(editForm).form('load',row);
			$('#supplier',editForm).combobox('setText',row.supplierName);
			$('#user',editForm).combobox('setText',row.userName);
			$('#supplier',editForm).combobox('setValue',row.supplierId);
			$('#user',editForm).combobox('setValue',row.userId);
			
			//取得列表信息
			var url = 'warehouse/queryDetailRecRej.do';
			var content={recRejId:row.recRejId};
			var result = syncCallService(url,content);
			if(result.isSuccess){
				var data = result.data;
				var datagridData = eval("("+data.datagridData+")");
				$(detailViewList).datagrid('loadData',datagridData);
			}else{
				$.messager.alert('提示',result.message,'error');
			}
		};
		//删除出库单
		var onRemove = function(){
			var rows = $(rejectViewList).datagrid('getChecked');
			if(rows.length==0){
				$.messager.alert("提示","请选择一条以上数据行","warning");
				return;
			}
			var recrRejIds=new Array();
			for(var i=0;i<rows.length;i++){
				recrRejIds.push(rows[i].recRejId);
			}
			$.messager.confirm('提示','确定要删除吗?',function(r){
				if(r){
					var url = 'warehouse/deleteRecRej.do';
					var content={recrRejIds:recrRejIds.join(CSIT.join)};
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
		
		//编辑出库单
		$(editDialog).dialog({  
			title: '编辑退货单',  
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
				id:'preReject'+id,
				text:'上一条',
				iconCls:'icon-left',
				handler:function(){
					onOpenIndex(-1);
				}
			},'-',{
				id:'nextReject'+id,
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
			var receiveDetailIds = new Array();
			var qtys = new Array();
			var prices = new Array();
			var totalPrices = new Array();

			//出库日期
			var recRejDate=$('#recRejDate',editForm).val();
			if(recRejDate==""||recRejDate==null){
				$.messager.alert('提示','请填写出库日期','error');
				return false;
			}
			//供应商
			var supplier=$('#supplier',editForm).combobox('getValue');
			if(supplier==""||supplier==null){
				$.messager.alert('提示','请选择供应商','error');
				return false;
			}
			var supplierIsNumber=isNumber(supplier);
			if(supplierIsNumber==false){
				$.messager.alert('提示','选择已有的供应商','error');
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
				receiveDetailIds.push(row.recRejDetailId);
				warehouseDetailIds.push(row.warehouseId);
				prices.push(row.price);
				totalPrices.push(row.totalPrice);
			}
			var delReceiveDetailIdArray=new Array();
			//统计原记录中被删除的记录
			for ( var i = 0; i < oldReceiveDetailIdArray.length; i++) {
				var haveDel = true;
				for(var j=0;j<rows.length;j++){
					if(oldReceiveDetailIdArray[i]==rows[j].recRejDetailId){
						haveDel = false;
						break;
					}
				}
				if(haveDel){
					delReceiveDetailIdArray.push(oldReceiveDetailIdArray[i]);
				}
			}
			$('#delReceiveDetailIds',editForm).val(delReceiveDetailIdArray.join(CSIT.join));
			$('#recRejType',editForm).val(-1);
			$('#receiveDetailIds',editForm).val(receiveDetailIds.join(CSIT.join));
			$('#commodityIds',editForm).val(commodityIds.join(CSIT.join));
			$('#warehouseDetailIds',editForm).val(warehouseDetailIds.join(CSIT.join));
			$('#qtys',editForm).val(qtys.join(CSIT.join));
			$('#purchasePrices',editForm).val(prices.join(CSIT.join));
			$('#totalPrices',editForm).val(totalPrices.join(CSIT.join));
			
			return true;
		};
		//保存
		function onSaveReceive(){
			$(editForm).form('submit',{
				url: 'warehouse/saveRecRej.do',
				onSubmit: function(){
					return setValue();
				},
				success: function(data){
					var result = eval('('+data+')');
					if(result.isSuccess){
						var fn = function(){
							var recRejId = $.trim($('#recRejId',editForm).val());
							if(recRejId==''){//新增
								var  data = result.data;
								$('#recRejId',editForm).val(data.recRejId);
							}
						//	TRN.CourseTypeList = null;
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
			$(detailViewList).datagrid('loadData',CSIT.ClearData);
			$(editForm).form('clear');
			$('#preReject'+id,editDialog).linkbutton('disable');
			$('#nextReject'+id,editDialog).linkbutton('disable');
			initRecRejCode();
			initCombobox();
		};
		//上下一条
		function onOpenIndex(index){
			if(selectIndex+index==0){
				$('#preReject'+id,editDialog).linkbutton('disable');
			}else{
				$('#preReject'+id,editDialog).linkbutton('enable');
			}
			if(selectIndex+index==total-1){
				$('#nextReject'+id,editDialog).linkbutton('disable');
			}else{
				$('#nextReject'+id,editDialog).linkbutton('enable');
			}
			$(rejectViewList).datagrid('unselectRow',selectIndex);
			$(rejectViewList).datagrid('selectRow',selectIndex+index);
			
			onOpen(selectRow);
		};
		
		var warehouses=TRN.getWarehouseList();
		function warehouseFormatter(value){
			for(var i=0; i<warehouses.length; i++){
				if (warehouses[i].warehouseId == value) return warehouses[i].warehouseName;
			};
			return value;
		};
		//出库单详细
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
			    {field:'recRejDetailId',hidden:true},
				{field:'commodityName',title:'商品名称',width:100,align:"center"},
				{field:'commodityTypeName',title:'商品分类',width:100,align:"center"},
				{field:'unitName',title:'商品单位',width:100,align:"center"},
				{field:'price',title:'进价',width:100,align:"center",
					editor:{
						type:'numberbox',
						options:{
							precision:2
						}	
					}
				},
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
				{field:'totalPrice',title:'合计',width:100,align:"center",
					editor:{
						type:'numberbox',
						options:{
							disabled:true,
							precision:2,
							onChange:function(newValue,oldValue){
								var totalAmount = 0 ;
								var rows =  $(detailViewList).datagrid('getRows');
								var row = $(detailViewList).datagrid('getSelected');
								var rowIndex = $(detailViewList).datagrid('getRowIndex',row); 
								for ( var i = 0; i < rows.length; i++) {
									if(i!=rowIndex){
										row = rows[i];
										if(row.totalPrice==''||row.totalPrice==null){
											totalAmount +=0;
										}else{
											totalAmount += parseFloat(row.totalPrice);
										}
									};
								}
								totalAmount+=parseFloat(newValue);
					    		$('#amountTotal',editForm).val(totalAmount);
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
					setEditing(rowIndex);
				}
				detailIndex = rowIndex;
			},
			onLoadSuccess:function(data){
				var dataRows = data.rows;
				oldReceiveDetailIdArray = new Array();
				for ( var i = 0; i < dataRows.length; i++) {
					oldReceiveDetailIdArray.push(dataRows[i].recRejDetailId);
				}
			}
		});
		
		//联动改变总金额的值
		function setEditing(rowIndex){  
			var editors = $(detailViewList).datagrid('getEditors', rowIndex);  
		    
		    var priceEditor = editors[0];  
		    var countEditor = editors[1];  
		    var totalPriceEditor = editors[2];  
		    priceEditor.target.bind('change', function(){  
		        calculate(rowIndex);  
		    });  
		    countEditor.target.bind('change', function(){  
		        calculate(rowIndex);  
		    });  
		    function calculate(rowIndex){  
		    	if(priceEditor.target.val()==''){
		    		$(priceEditor.target).numberbox('setValue',0.00);
		    	}
		    	if(countEditor.target.val()==''){
		    		$(countEditor.target).numberbox('setValue',0);
		    	}
		        var cost = priceEditor.target.val() * countEditor.target.val();  
		        $(totalPriceEditor.target).numberbox('setValue',cost);
		    }  
		}  
		
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
		
		//编辑出库单详细
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
					$(commodityList).datagrid('loadData',{rows:[],total:0});
					$(commodityDialog).dialog('close');
				}
			}]
		});
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
		
		//出库单详细的商品
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
		    {field:'price',hidden:true},
		    {field:'commodityId',hidden:true},
			{field:'commodityName',title:'商品名称',width:100,align:"center"},
			{field:'commodityTypeName',title:'商品分类',width:100,align:"center"},
			{field:'unitName',title:'商品单位',width:100,align:"center"},
			{field:'purchasePrice',title:'进价',width:100,align:"center"},
			{field:'salePrice',title:'售价',width:100,align:"center"},
			{field:'size',title:'规格',width:100,align:"center"},
			{field:'qtyStore',title:'库存数量',width:100,align:"center"},
			{field:'note',title:'备注',width:100,align:"center"}
		]]
	  });
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
	
	//判断死否是数字
	function isNumber(s)  {   
		var strP=/^\d+$/;
		if(''==s||strP.test(s)) { 
			return true;
		}else{
			return false;
		}
	}
		
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
  };
})(jQuery);   