// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.returnCommodityInit = function() {
	  var $this = $(this);
	  var id = $(this).attr('id');
	  var height = $(document.body).height();
	  
	  var returnCommodityViewList = $('#returnCommodityViewList',$this);
	  var editDialog = $('#editDialog',$this);
	  var editForm = $('#editForm',editDialog);
	  var searchBtn = $('#searchBtn',$this);
	  var selectRow = null;
	  var selectIndex=null;
	  
	  var useCommodityDetailList = $('#useCommodityDetailList',$this);
	  var useCommodityDetailDialog = $('#useCommodityDetailDialog',$this);
	  var useCommodityDetailSearchBtn = $('#useCommodityDetailSearchBtn',useCommodityDetailDialog);
	  
	  var useCommodityDetailPager = $('#useCommodityDetailPager',$this);
	  var useCommodityDetailPageNumber = 1;
	  var useCommodityDetailPageSize = 10;
	  
	  var pager = $('#pager',$this);
	  var pageNumber = 1;
	  var pageSize = 10;
	  
	  var oldReturnCommodityDetailIdArray=new Array();
	  var detailViewList = $('#detailViewList',editDialog);
	  var detailIndex=null;
	  var total=null;
	  
	  $('#returnerSearch',queryContent).combobox({
			width:100,
			data:TRN.getIsTeacherEmployeeList(),
			valueField:'userId',
			textField:'userName'
	  });
	  
	  //查询归还单
		$(searchBtn).click(function(){
			searchBtn(true);
		});
		//查询归还单
		var searchBtn = function(flag){
			var beginDate = $.trim($('#beginDate',queryContent).val());
			var endDate = $.trim($('#endDate',queryContent).val());
			var userId = $.trim($('#returnerSearch',queryContent).combobox('getValue'));
			if(!isNumber(userId)){
				$.messager.alert('提示','请选择已有的归还人','error');
				return false;
			}
			var content = {'user.userId':userId,beginDate:beginDate,endDate:endDate,page:pageNumber,rows:pageSize};
			//取得列表信息
			var url = 'warehouse/queryReturnCommodity.do';
			var result = syncCallService(url,content);
			if(result.isSuccess){
				var data = result.data;
				var datagridData = eval("("+data.datagridData+")");
				$(returnCommodityViewList).datagrid('loadData',datagridData);
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
			var url = "warehouse/getTotalCountReturnCommodity.do";
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
		  $(returnCommodityViewList).datagrid({
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
				{field:'returnDate',title:'归还日期',width:100,align:"center"},
				{field:'qtyTotal',title:'数量',width:100,align:"center"},
				{field:'userName',title:'归还人',width:100,align:"center"},
				{field:'handleName',title:'经办人',width:100,align:"center"},
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
		  
		//添加归还单
		var onAdd = function(){
			$(detailViewList).datagrid('loadData',{rows:[],total:0});
			$(editForm).form('clear');
			$('#pre'+id,editDialog).linkbutton('disable');
			$('#next'+id,editDialog).linkbutton('disable');
			initCombobox();
			$(editDialog).dialog('open');
		};
		
		//初始化Combobox
		var initCombobox = function(){
			$('#handler',editForm).combobox({
				width:250,
				data:TRN.getEmployeeList(),
				valueField:'userId',
				textField:'userName'
			});
			$('#user',editForm).combobox({
				width:250,
				data:TRN.getIsTeacherEmployeeList(),
				valueField:'userId',
				textField:'userName'
			});
		};
		
		//修改归还单
		var onUpdate = function(){
			if(selectRow==null){
				$.messager.alert("提示","请选择一条数据航","warning");
				return;
			}
			var index=$(returnCommodityViewList).datagrid("getRowIndex",selectRow);
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
			$('#handler',editForm).combobox('setText',row.handleName);
			$('#handler',editForm).combobox('setValue',row.handleId);
			$('#user',editForm).combobox('setValue',row.userId);
			
			//取得列表信息
			var url = 'warehouse/queryDetailReturnCommodity.do';
			var content={returnCommodityId:row.returnCommodityId};
			var result = syncCallService(url,content);
			if(result.isSuccess){
				var data = result.data;
				var datagridData = eval("("+data.datagridData+")");
				$(detailViewList).datagrid('loadData',datagridData);
			}else{
				$.messager.alert('提示',result.message,'error');
			}
		};
		//删除归还单
		var onRemove = function(){
			var rows = $(returnCommodityViewList).datagrid('getChecked');
			if(rows.length==0){
				$.messager.alert("提示","请选择一条以上数据行","warning");
				return;
			}
			var returnCommodityIds=new Array();
			for(var i=0;i<rows.length;i++){
				returnCommodityIds.push(rows[i].returnCommodityId);
			}
			$.messager.confirm('提示','确定要删除吗?',function(r){
				if(r){
					var url = 'warehouse/deleteReturnCommodity.do';
					var content={returnCommodityIds:returnCommodityIds.join(CSIT.join)};
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
		
		//编辑归还单
		$(editDialog).dialog({  
			title: '编辑归还单',  
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
			var useCommodityDetailIds = new Array();
			var warehouseDetailIds = new Array();
			var returnCommodityDetailIds = new Array();
			var qtys = new Array();
			
			//归还日期
			var returnDate=$('#returnDate',editForm).val();
			if(returnDate==""||returnDate==null){
				$.messager.alert('提示','请填写归还日期','error');
				return false;
			}
			
			//归还人
			var user=$('#user',editForm).combobox('getValue');
			if(user==""||user==null){
				$.messager.alert('提示','请选择归还人','error');
				return false;
			}
			var userIsNumber=isNumber(user);
			if(userIsNumber==false){
				$.messager.alert('提示','选择已有的归还人','error');
				return false;
			}
			
			//经办人
			var handler=$('#handler',editForm).combobox('getValue');
			if(handler==""||handler==null){
				$.messager.alert('提示','请选择经办人','error');
				return false;
			}
			var handlerIsNumber=isNumber(handler);
			if(handlerIsNumber==false){
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
				useCommodityDetailIds.push(row.useCommodityDetailId);
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
				warehouseDetailIds.push(row.warehouseId);
				returnCommodityDetailIds.push(row.returnCommodityDetailId);
			}
			var delReturnCommodityDetailIds=new Array();
			//统计原记录中被删除的记录
			for ( var i = 0; i < oldReturnCommodityDetailIdArray.length; i++) {
				var haveDel = true;
				for(var j=0;j<rows.length;j++){
					if(oldReturnCommodityDetailIdArray[i]==rows[j].returnCommodityDetailId){
						haveDel = false;
						break;
					}
				}
				if(haveDel){
					delReturnCommodityDetailIds.push(oldReturnCommodityDetailIdArray[i]);
				}
			}
			
			$('#delReturnCommodityDetailIds',editForm).val(delReturnCommodityDetailIds.join(CSIT.join));
			$('#returnCommodityDetailIds',editForm).val(returnCommodityDetailIds.join(CSIT.join));
			$('#useCommodityDetailIds',editForm).val(useCommodityDetailIds.join(CSIT.join));
			$('#warehouseDetailIds',editForm).val(warehouseDetailIds.join(CSIT.join));
			$('#qtys',editForm).val(qtys.join(CSIT.join));
			return true;
		};
		//保存
		function onSaveReceive(){
			$(editForm).form('submit',{
				url: 'warehouse/saveReturnCommodity.do',
				onSubmit: function(){
					return setValue();
				},
				success: function(data){
					var result = eval('('+data+')');
					if(result.isSuccess){
						var fn = function(){
							var returnCommodityId = $.trim($('returnCommodityId',editForm).val());
							if(useCommodityId==''){//新增
								var  data = result.data;
								$('#returnCommodityId',editForm).val(data.returnCommodityId);
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
			$(detailViewList).datagrid('loadData',CSIT.ClearData);
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
			$(returnCommodityViewList).datagrid('unselectRow',selectIndex);
			$(returnCommodityViewList).datagrid('selectRow',selectIndex+index);
			
			onOpen(selectRow);
		};
		
		var warehouses=TRN.getWarehouseList();
		function warehouseFormatter(value){
			for(var i=0; i<warehouses.length; i++){
				if (warehouses[i].warehouseId == value) return warehouses[i].warehouseName;
			};
			return value;
		};
		 var isNeedReturnArr = [ 
            {"value" : 1,"text" : "是"}, 
            {"value" : 0,"text" : "否"} 
			                   ];
		function returnFormatter(value){
			for(var i=0; i<isNeedReturnArr.length; i++){
				if (isNeedReturnArr[i].value == value) return isNeedReturnArr[i].text;
			};
			return value;
		};
		
		var statusArray = [ 
            {"value" : 1,"text" : "未还",select:"select"}, 
            {"value" : 0,"text" : "已还"} 
		                   ];
		function returnStatusFormatter(value){
			for(var i=0; i<statusArray.length; i++){
				if (statusArray[i].value == value) return statusArray[i].text;
			};
			return value;
		};
		//归还单详细
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
				{field:'commodityName',title:'商品名称',width:100,align:"center"},
				{field:'commodityTypeName',title:'商品分类',width:100,align:"center"},
				{field:'unitName',title:'商品单位',width:100,align:"center"},
				{field:'qty',title:'归还数量',width:100,align:"center",
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
					}
				}
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
				oldReturnCommodityDetailIdArray = new Array();
				for ( var i = 0; i < dataRows.length; i++) {
					oldReturnCommodityDetailIdArray.push(dataRows[i].returnCommodityDetailId);
				}
			}
		});
		
		//添加商品
		var onAddCommodity = function(){
		  $(useCommodityDetailList).datagrid('loadData',CSIT.ClearData);
		  $(useCommodityDetailDialog).dialog('open');
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
		//编辑归还单详细
		$(useCommodityDetailDialog).dialog({  
			title: '选择领用记录',  
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
					$(useCommodityDetailList).datagrid('loadData',{rows:[],total:0});
					$(useCommodityDetailDialog).dialog('close');
				}
			}]
		});
		//添加商品
		function onCommoditySave()  {   
			var rows = $(useCommodityDetailList).datagrid('getSelections');
			if(rows.length==0){
				$.messager.alert("提示",'请至少选择一条数据行',"warning");
				return;
			}
			var qtyTotal=0;
			for(var i=0;i<rows.length;i++){
				var row=rows[i];
				row.qty=row.unReturnQty;
				qtyTotal+=parseFloat(row.qty);
				$(detailViewList).datagrid('appendRow',row);
				
			}
			$('#qtyTotal',editForm).val(qtyTotal);
			$(useCommodityDetailList).datagrid('loadData',CSIT.ClearData);
			$(useCommodityDetailDialog).dialog('close');
		}
		
		//归还单详细的商品
	  $(useCommodityDetailList).datagrid({
		fit:true,
		method:"POST",
		nowrap:true,
		striped: true,
		collapsible:true,
		rownumbers:true,
		fit:true,
		columns:[[
		    {field:'ck',width:50,checkbox:true},
		    {field:'useCommodityDetailId',hidden:true},
			{field:'commodityName',title:'商品名称',width:100,align:"center"},
			{field:'commodityTypeName',title:'商品分类',width:100,align:"center"},
			{field:'unitName',title:'商品单位',width:100,align:"center"},
			{field:'userName',title:'领用人',width:100,align:"center"},
			{field:'returnDate',title:'领用日期',width:100,align:"center"},
			{field:'warehouseName',title:'领用仓库',width:100,align:"center"},
			{field:'qty',title:'领用数量',width:100,align:"center"},
			{field:'returnQty',title:'归还数量',width:100,align:"center"},
			{field:'unReturnQty',title:'未还数量',width:100,align:"center"},
			{field:'note',title:'备注',width:100,align:"center"}
		]]
	  });
	//查询商品
	$(useCommodityDetailSearchBtn).click(function(){
		useCommodityDetailSearch(true);
	});
		//查询领用单详细
	var useCommodityDetailSearch = function(flag){
		var commodityName = $.trim($('#commodityName',useCommodityDetailQueryContent).val());
		var userName= $.trim($('#userSearch',useCommodityDetailQueryContent).val());
		
		var rows =  $(detailViewList).datagrid('getRows');
		var useCommodityDetailIds=new Array();
		for ( var i = 0; i < rows.length; i++) {
			var row=rows[i];
			useCommodityDetailIds.push(row.useCommodityDetailId);
		}
		
		var content = {commodityName:commodityName,userName:userName,useCommodityDetailIds:useCommodityDetailIds.join(CSIT.join),page:useCommodityDetailPageNumber,rows:useCommodityDetailPageSize};
		//取得列表信息
		var url = 'warehouse/queryNeedReturnUseCommodity.do';
		var result = syncCallService(url,content);
		if(result.isSuccess){
			var data = result.data;
			var datagridData = eval("("+data.datagridData+")");
			$(useCommodityDetailList).datagrid('loadData',datagridData);
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
		
		var url = "warehouse/getTotalCountNeedReturnUseCommodity.do";
		asyncCallService(url,content,
		function(result){
			if(result.isSuccess){
				var data = result.data;
				$(useCommodityDetailPager).pagination({  
					useCommodityDetailPageNumber:1,
					total:data.total
				});
			}else{
				$.messager.alert('提示',result.message,"error");
			}
		});
	};	
  };
})(jQuery);   