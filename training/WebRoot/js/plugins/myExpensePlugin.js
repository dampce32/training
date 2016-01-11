// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.myExpenseInit = function() {
	  var $this = $(this);
	  var id = $(this).attr('id');
	  var width = $(document.body).width();
	  var height = $(document.body).height();
	  var pageNumber = 1;
	  var pageSize = 10;
	  var queryBillContent = $('#queryBillContent',$this);
	  var queryContentCourse = $('#queryContentCourse',$this); 
	  var queryContentFee = $('#queryContentFee',$this); 
	  var queryContentCommodity = $('#queryContentCommodity',$this);
	  var searchBillBtn = $('#searchBillBtn',$this);
	  var searchFeeBtn = $('#searchFeeBtn',$this);
	  var searchCourseBtn = $('#searchCourseBtn',$this);
	  var searchCommodityBtn = $('#searchCommodityBtn',$this);
	  var detailList = $('#detailList',$this);
	  var billList = $('#billList',$this);
	  var feeList = $('#feeList',$this);
	  var courseList = $('#courseList',$this);
	  var commodityList = $('#commodityList',$this);
	  var selectRowBill = null;
	  var selectIndexBill = null;
	  var selectRowDetail = null;
	  var selectIndexDetail = null;
	  var editBillDialog = $('#editBillDialog',$this);
	  var feeItemDialog = $('#feeItemDialog',$this);
	  var courseDialog = $('#courseDialog',$this);
	  var commodityDialog = $('#commodityDialog',$this);
	  var editBillForm = $('#editBillForm',$this);
	  var availableMoney = null;
	  var editReturnCourseDialog = $('#editReturnCourseDialog',$this);
	  var editReturnCourseForm = $('#editReturnCourseForm',editReturnCourseDialog);
	  var ReturnCourse = $('#ReturnCourse',$this);
	  var isEditor = true;
	  //保留小数点后2位
	  var toDecimal =function(x) {  
            var f = parseFloat(x);  
            if (isNaN(f)) {  
                return false;  
            }  
            var f = Math.round(x*100)/100;  
            var s = f.toString();  
            var rs = s.indexOf('.');  
            if (rs < 0) {  
                rs = s.length;  
                s += '.';  
            }  
            while (s.length <= rs + 2) {  
                s += '0';  
            }  
            return s;  
        }
		$(billList).datagrid( {
			method:"POST",
			singleSelect:true,
			nowrap:true,
			striped: true,
			collapsible:true,
			rownumbers:true,
			pagination:true,
			fit:true,
			toolbar:[	
						{text:'添加',iconCls:'icon-add',handler:function(){onAddBill()}},'-',
						{text:'查看详细',iconCls:'icon-search',handler:function(){onLookDetail();}},'-',
						{text:'打印',iconCls:'icon-print',handler:function(){onPrint();}}
					],
			columns : [ [ {field : 'billId',hidden:true},
						  {field : 'billCode',title : '单号',width : 150,align : "center"},
						  {field : 'billDate',title : '日期',width : 100,align : "center"},
						  {field : 'billType',title : '类型',width : 100,align : "center",
									formatter: function(value,row,index){
												if (value==0){
													return '退货';
												} else if (value==1){
													return '购买';
												}
						  }},
						  {field : 'pay',title : '应交金额',width : 100,align : 'center'}, 
						  {field : 'favourable',title : '优惠',width : 100,align : "center"},
						  {field : 'payed',title : '实收金额',width : 100,align : "center"},
						  {field : 'schoolName',title : '办理学校',width : 100,align : "center"}, 
						  {field : 'userName',title : '经办人',width : 100,align : 'center'}
					 ] ],
			onClickRow:function(rowIndex, rowData){
			selectRowBill = rowData;
			selectIndexBill = rowIndex;
			},
			onDblClickRow:function(rowIndex,rowData){
				onLookDetail();
			},
			onLoadSuccess:function(){
				selectRowBill = null;
		 		selectIndexBill = null;
				pageNumber = 1;
			}
		});
	//分页条
	$(billList).datagrid('getPager').pagination({   
	    onSelectPage: function(page, rows){
			pageNumber = page;
			pageSize = rows;
			searchBill();
	    }
	});
	var initBill = function(){
		var studentId=$('#studentId',$this).val();
	    var studentName=$('#studentName',$this).val();
		var url = "customerService/initBill.do";
		var result = syncCallService(url,null);
		if(result.isSuccess){
			var data = result.data;
			$('#billCode',editBillForm).val(data.billCode);
		}else{
			$.messager.alert('提示',result.message,'error');
		}
		var url2 = "customerService/initStudent.do";
		var content={type:0,studentId:studentId};
		var result = syncCallService(url2,content);
		if(result.isSuccess){
			var data = result.data;
			var student=eval("("+data.student+")");
			availableMoney =student.availableMoney;
		}else{
			$.messager.alert('提示',result.message,'error');
		}
		$('#studentName',editBillForm).val(studentName);
		$('#studentId',editBillForm).val(studentId);
		//办理校区
		$('#school',editBillForm).combobox({
			disabled:false,
			valueField:'schoolId',
			textField:'schoolName',
			width:250,
			data:TRN.getSelfSchoolList()
		});
		//经办人
		$('#user',editBillForm).combobox({
		  disabled:false,
		  width:250,
		  data:TRN.getEmployeeList(),
		  valueField:'userId',
		  textField:'userName'
	  });
		$('#payWay',editBillForm).combobox('setValue',1);
		$('#isCosttr',editBillForm).hide();
		$('#saveBill'+id).linkbutton('enable');
		$('#addFee'+id).linkbutton('enable');
		$('#addCourse'+id).linkbutton('enable');
		$('#addCommodity'+id).linkbutton('enable');
		$('#del'+id).linkbutton('enable');
		isEditor=true;
		$('#billDate',editBillForm).attr('disabled',false);
		$('#note',editBillForm).attr('disabled',false);
	}
	$(ReturnCourse).datagrid({
						method:"POST",
						singleSelect:true,
						nowrap:true,
						striped: true,
						collapsible:true,
						rownumbers:true,
						fit:true,
						columns : [ [ {field : 'billItemId',hidden:true},
								  {field : 'billItemName',title : '收费项目',width : 100,align : 'center'}, 
								  {field : 'price',title : '单价',width : 100,align : 'center'}, 
								  {field : 'qty',title : '数量',width : 80,align : 'center'},
								  {field : 'unitName',title : '单位',width : 80,align : 'center'},
								  {field:'totalPrice',title:'合计',width:100,align:"center"},
								  {field : 'discount',title : '折扣',width : 50,align : "center"},
								  {field : 'discountAmount',title : '扣除',width : 100,align : 'center'},
								  {field : 'payed',title : '金额',width : 100,align : 'center'},
								  {field:'warehouseName',title:'仓库',width:150,align:"center"}
							 ] ]
				});
	$(editReturnCourseDialog).dialog({
		title:'退货办理',
	    width:width-350,
	    height:height-40,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    toolbar:[
	    		{id:'saveReturnCourse',text:'确定退货',iconCls:'icon-save',handler:function(){onSaveReturnCourse();}},'-',
				{text:'退出',iconCls:'icon-cancel',handler:function(){
						$(editReturnCourseDialog).dialog('close');
					}
				}
	   ]
	});
	var onLookDetail = function(){
		if(selectRowBill==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		};
		var studentId=$('#studentId',$this).val();
	    var studentName=$('#studentName',$this).val();
		if(selectRowBill.billType==1){
			//办理校区
			$('#school',editBillForm).combobox({
				valueField:'schoolId',
				textField:'schoolName',
				disabled:true,
				width:250,
				data:TRN.getSelfSchoolList()
			});
			//经办人
			$('#user',editBillForm).combobox({
			  disabled:true,
			  width:250,
			  data:TRN.getEmployeeList(),
			  valueField:'userId',
			  textField:'userName'
		  	});
			$(editBillForm).form('load',selectRowBill);
			$('#billDate',editBillForm).attr('disabled',true);
			$('#note',editBillForm).attr('disabled',true);
			$('#studentName',editBillForm).val(studentName);
			$('#studentId',editBillForm).val(studentId);
			$('#school',editBillForm).combobox('setValue',selectRowBill.schoolId);
			$('#user',editBillForm).combobox('setValue',selectRowBill.userId);
			$('#isCosttr',editBillForm).hide();
			isEditor=false;
			
			$(detailList).datagrid('loadData', { total: 0, rows: [] });
			//取得列表信息
			var url = 'customerService/queryDetailBill.do';
			var content={billId:selectRowBill.billId};
			var result = syncCallService(url,content);
			if(result.isSuccess){
				var data = result.data;
				var datagridData = eval("("+data.datagridData+")");
				$(detailList).datagrid('loadData',datagridData);
			}else{
				$.messager.alert('提示',result.message,'error');
			}
			$('#saveBill'+id).linkbutton('disable');
			$('#addFee'+id).linkbutton('disable');
			$('#addCourse'+id).linkbutton('disable');
			$('#addCommodity'+id).linkbutton('disable');
			$('#del'+id).linkbutton('disable');
			$(editBillDialog).dialog('open');
		}
		if(selectRowBill.billType==0){
			//办理校区
			$('#school',editReturnCourseForm).combobox({
				valueField:'schoolId',
				textField:'schoolName',
				disabled:true,
				width:250,
				data:TRN.getSelfSchoolList()
			});
			//经办人
			$('#user',editReturnCourseForm).combobox({
			  disabled:true,
			  width:250,
			  data:TRN.getEmployeeList(),
			  valueField:'userId',
			  textField:'userName'
		  	});
			$(editReturnCourseForm).form('load',selectRowBill);
			$('#billDate',editReturnCourseForm).attr('disabled',true);
			$('#note',editReturnCourseForm).attr('disabled',true);
			$('#school',editReturnCourseForm).combobox('setValue',selectRowBill.schoolId);
			$('#user',editReturnCourseForm).combobox('setValue',selectRowBill.userId);
			$(ReturnCourse).datagrid('loadData', { total: 0, rows: [] });
			//取得列表信息
			var url = 'customerService/queryDetailBill.do';
			var content={billId:selectRowBill.billId};
			var result = syncCallService(url,content);
			if(result.isSuccess){
				var data = result.data;
				var datagridData = eval("("+data.datagridData+")");
				$(ReturnCourse).datagrid('loadData',datagridData);
			}else{
				$.messager.alert('提示',result.message,'error');
			}
			$('#saveReturnCourse').linkbutton('disable');
			$(editReturnCourseDialog).dialog('open');
		}
	}
	
	//添加消费
	var onAddBill = function(){
		$(editBillForm).form('clear');
		$(detailList).datagrid('loadData',{ total: 0, rows: [] });
		$(billList).datagrid('unselectAll');
		selectIndexBill = null;
		selectRowBill = null;
		initBill();
		$(editBillDialog).dialog('open');
	}
	$(editBillDialog).dialog({
		title:'添加消费',
	    width:1000,
	    height:height-40,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    toolbar:[
	    		{id:'saveBill'+id,text:'保存',iconCls:'icon-save',handler:function(){onSaveBill();}},'-',
				{text:'退出',iconCls:'icon-cancel',handler:function(){
						$(editBillDialog).dialog('close');
					}
				}
	   ]
	});
	var setValueBill = function(){
		var detailArray = new Array();
		//单号
		var billCode = $.trim($('#billCode',editBillForm).val());
		if(''==billCode){
			$.messager.alert('提示','对不起，单号为空','warning');
			return false;
		}
		//消费日期
		var billDate = $.trim($('#billDate',editBillForm).val());
		if(''==billDate){
			$.messager.alert('提示','请消费日期','warning');
			return false;
		}
		//办理校区
		var schoolId = $('#school',editBillForm).combobox('getValue') ;
		if(''==schoolId){
			$.messager.alert('提示','请选择办理校区','warning');
			return false;
		}
		if(isNaN(parseInt(schoolId))){
			$.messager.alert('提示','请选择提供选择的办理校区','warning');
			return false;
		}
		//经办人
		var userId = $('#user',editBillForm).combobox('getValue') ;
		if(''==userId){
			$.messager.alert('提示','请选择经办人','warning');
			return false;
		}
		if(isNaN(parseInt(userId))){
			$.messager.alert('提示','请选择提供选择的经办人','warning');
			return false;
		}
		$(detailList).datagrid('endEdit', selectIndexDetail);
		$(detailList).datagrid('unselectAll');
		selectIndexDetail=null;
		var rows =  $(detailList).datagrid('getRows');
		if(rows.length==0){
			$.messager.alert('提示','请添加收费项','error');
			return false;
		}
		for ( var i = 0; i < rows.length; i++) {
			var row=rows[i];
			//收费项数量
			if(row.qty==""||row.qty==null){
				$.messager.alert('提示','第'+(i+1)+'行数据没有填写数量','error');
				return false;
			}
			//商品存放仓库
			if(row.productType=="商品"&&(row.warehouseId==null||row.warehouseId=="")){
				$.messager.alert('提示','第'+(i+1)+'行数据没有选择仓库','error');
				return false;
				var warehouseIsNumber=isNumber(row.warehouseId);
				if(warehouseIsNumber==false){
					$.messager.alert('提示','第'+(i+1)+'行数据没有选择已有的仓库','error');
					return false;
				}
			}
			detailArray.push(row.productType+","+row.billItemId+","+row.price+","+row.qty+","+row.discount+","+row.discountAmount+","+row.unitName+","+row.warehouseId);
 			if (!!$("#isCost").attr("checked")) {
                var cost = $('#money',editBillForm).val()+","+$('#payWay',editBillForm).combobox('getValue');
            }
		}
		var detail = detailArray.join(CSIT.join);
		$('#billDetail',editBillForm).val(detail);
		$('#cost',editBillForm).val(cost);
		$('#billType',editBillForm).val(1);
		return true;
	}
	//保存消费
	var onSaveBill = function(){
		
		$(editBillForm).form('submit',{
			url: 'customerService/saveBill.do',
			onSubmit: function(){
				return setValueBill();
			},
			success: function(data){
				var result = eval('('+data+')');
				if(result.isSuccess){
					var fn = function(){
						searchBill(true);
						$(editBillDialog).dialog('close');
					};
					$.messager.alert('提示','保存成功','info',fn);
				}else{
					$.messager.alert('提示',result.message,"error");
				}
			}
		});
	};
	var warehouses=TRN.getWarehouseList();
	function warehouseFormatter(value){
		for(var i=0; i<warehouses.length; i++){
			if (warehouses[i].warehouseId == value) return warehouses[i].warehouseName;
		};
		return value;
	};
	$(detailList).datagrid( {
		method:"POST",
		singleSelect:true,
		nowrap:true,
		striped: true,
		collapsible:true,
		rownumbers:true,
		fit:true,
		toolbar:[	
					{id:'addFee'+id,text:'添加收费项',iconCls:'icon-add',handler:function(){onAddFeeItem()}},'-',
					{id:'addCourse'+id,text:'添加课程',iconCls:'icon-add',handler:function(){onAddCourse()}},'-',
					{id:'addCommodity'+id,text:'添加商品',iconCls:'icon-add',handler:function(){onAddCommodity()}},'-',
					{id:'del'+id,text:'删除',iconCls:'icon-remove',handler:function(){onDeleteDetail()}}
				],
		columns : [ [ {field : 'billItemId',hidden:true},
					  {field : 'productType',title : '收费类型',width : 100,align : "center"}, 
					  {field : 'billItemName',title : '收费项名称',width : 100,align : 'center'}, 
					  {field : 'price',title : '单价',width : 80,align : 'center',
								editor:{
									type:'numberbox',
									options:{
										precision:2
									}	
								}}, 
					  {field : 'qty',title : '数量',width : 80,align : 'center',
							editor:{
								type:'numberbox'
							}},
					  {field:'totalPrice',title:'合计',width:80,align:"center",
							editor:{
								type:'numberbox',
								options:{
									disabled:true,
									precision:2,
									onChange:function(newValue,oldValue){
										var totalAmount = 0 ;
										var rows =  $(detailList).datagrid('getRows');
										var row = $(detailList).datagrid('getSelected');
										var rowIndex = $(detailList).datagrid('getRowIndex',row); 
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
							    		$('#pay',editBillForm).val(toDecimal(totalAmount));
									}
								}
							}
						},
					  {field : 'discount',title : '折扣',width : 50,align : "center",
								editor:{
									type:'numberbox',
									options:{
										max:10,
										min:0,
										precision:2
									}	
								}},
					  {field : 'discountAmount',title : '优惠金额',width : 80,align : 'center',
								editor:{
									type:'numberbox',
									options:{
										disabled:true,
										precision:2,
										onChange:function(newValue,oldValue){
											var totalDiscountAmount = 0 ;
											var rows =  $(detailList).datagrid('getRows');
											var row = $(detailList).datagrid('getSelected');
											var rowIndex = $(detailList).datagrid('getRowIndex',row); 
											for ( var i = 0; i < rows.length; i++) {
												if(i!=rowIndex){
													row = rows[i];
													if(row.discountAmount==''||row.discountAmount==null){
														totalDiscountAmount +=0;
													}else{
														totalDiscountAmount += parseFloat(row.discountAmount);
													}
												};
											}
											totalDiscountAmount+=parseFloat(newValue);
								    		$('#favourable',editBillForm).val(toDecimal(totalDiscountAmount));
										}
									}	
								}},
					  {field : 'payed',title : '实收金额',width : 80,align : 'center',
								editor:{
									type:'numberbox',
									options:{
										disabled:true,
										precision:2,
										onChange:function(newValue,oldValue){
											var totalPayed = 0 ;
											var rows =  $(detailList).datagrid('getRows');
											var row = $(detailList).datagrid('getSelected');
											var rowIndex = $(detailList).datagrid('getRowIndex',row); 
											for ( var i = 0; i < rows.length; i++) {
												if(i!=rowIndex){
													row = rows[i];
													if(row.payed==''||row.payed==null){
														totalPayed +=0;
													}else{
														totalPayed += parseFloat(row.payed);
													}
												};
											}
											totalPayed+=parseFloat(newValue);
								    		$('#payed',editBillForm).val(toDecimal(totalPayed));
								    		if(parseFloat(availableMoney)<parseFloat($('#payed',editBillForm).val())){
								    			$('#money',editBillForm).val(toDecimal(parseFloat($('#payed',editBillForm).val())-parseFloat(availableMoney)));
								    			$('#isCosttr',editBillForm).show();
								    		}
								    		if(parseFloat(availableMoney)>=parseFloat($('#payed',editBillForm).val())){
								    			$('#isCost',editBillForm).attr("checked",false);
								    			$('#isCosttr',editBillForm).hide();
								    		}
								    	}
									}	
								}},
					  {field : 'unitName',title : '单位',width : 80,align : 'center'},
					  {field:'warehouseId',title:'仓库',width:173,align:"center",formatter:warehouseFormatter,
							editor:{
								type:'combobox',
								options:{
									valueField:'warehouseId',
									textField:'warehouseName',
									data:warehouses
								}
							}}
				 ] ],
		onClickRow:function(rowIndex, rowData){
				if(isEditor){
					if (selectIndexDetail != rowIndex){
							$(this).datagrid('endEdit', selectIndexDetail);
							$(this).datagrid('beginEdit', rowIndex);
							setEditing(rowIndex,rowData);
						}
				}
				selectIndexDetail = rowIndex;
		},
		onLoadSuccess:function(){
			selectRowDetail = null;
	 		selectIndexDetail = null;
			pageNumber = 1;
		}
	});
	//联动改变总金额的值
	function setEditing(rowIndex,rowData){  
	    var editors = $(detailList).datagrid('getEditors', rowIndex);  
	    var priceEditor = editors[0];  
	    var countEditor = editors[1];  
	    var totalPriceEditor = editors[2];
	    var discountEditor = editors[3]; 
	    var discountAmountEditor = editors[4]; 
	    var payedEditor = editors[5];
	    var warehouseEditor = editors[6];
	    
	    priceEditor.target.bind('change', function(){  
	        calculate(rowIndex);  
	    });  
	    countEditor.target.bind('change', function(){  
	        calculate(rowIndex);  
	    }); 
	    discountEditor.target.bind('change', function(){  
	        calculate(rowIndex);  
	    });
	    if(rowData.productType=='收费项'||rowData.productType=='课程'){
			$(warehouseEditor.target).combobox('disable');
		}
		if(rowData.productType=='商品'){
			$(warehouseEditor.target).combobox('enable');
		}
	    function calculate(rowIndex){  
	    	if(countEditor.target.val()==''){
	    		$(countEditor.target).numberbox('setValue',0);
	    	}
	    	if(discountEditor.target.val()==''){
	    		$(discountEditor.target).numberbox('setValue',10.00);
	    	}
	        var cost = priceEditor.target.val() * countEditor.target.val();  
	        $(totalPriceEditor.target).numberbox('setValue',cost);
	        var discountAmount = totalPriceEditor.target.val()*(10-discountEditor.target.val())*0.1;  
	        $(discountAmountEditor.target).numberbox('setValue',discountAmount);
	        var payed=cost-discountAmount;
	        $(payedEditor.target).numberbox('setValue',payed);
	    }  
	} 
	var onDeleteDetail = function (){
		if(selectIndexDetail==null){
			$.messager.alert("提示",'请选择数据行',"warning");
			return;
		}
		$.messager.confirm('提示','确定要删除吗?',function(r){
			if(r){
				$(detailList).datagrid('deleteRow',selectIndexDetail);
				selectIndexDetail = null;
				$(detailList).datagrid('unselectAll');
				
				//统计删除后的总金额,优惠金额,实收金额
				 var totalAmount = 0 ;
				 var totalDiscountAmount = 0 ;
				 var totalPayed = 0 ;
				 var rows =  $(detailList).datagrid('getRows');
				 for ( var i = 0; i < rows.length; i++) {
					var row = rows[i];
					if(row.totalPrice==''||row.totalPrice==null){
						totalAmount +=0;
					}else{
						totalAmount += parseFloat(row.totalPrice);
					}
					if(row.discountAmount==''||row.discountAmount==null){
						totalDiscountAmount +=0;
					}else{
						totalDiscountAmount += parseFloat(row.discountAmount);
					}
					if(row.payed==''||row.payed==null){
						totalPayed +=0;
					}else{
						totalPayed += parseFloat(row.payed);
					}
				}
				$('#pay',editBillForm).val(totalAmount);
				$('#favourable',editBillForm).val(totalDiscountAmount);
				$('#payed',editBillForm).val(totalPayed);
				if(totalPayed>parseFloat(availableMoney)){
					$('#money',editBillForm).val(totalPayed-parseFloat(availableMoney));
				}
				else {
					$('#isCost',editBillForm).attr("checked",false);
					$('#isCosttr',editBillForm).hide();
				}
			}
		});
	}
	//======================================添加消费项===================================================
	//消费项列表
	  $(feeList).datagrid({
		fit:true,
		method:"POST",
		nowrap:true,
		striped: true,
		collapsible:true,
		rownumbers:true,
		fit:true,
		columns:[[
		    {field:'ck',width:50,checkbox:true},
		    {field:'feeItemId',hidden:true},
			{field:'feeItemName',title:'消费项名称',width:100,align:"center"},
			{field:'fee',title:'金额',width:100,align:"center"}
		]]
	  });
	  //分页条
	$(pagerFee).pagination({   
	    onSelectPage: function(page, rows){
			pageNumber = page;
			pageSize = rows;
			searchFee();
	    }
	});
	//查询消费项
	var searchFee = function(flag){
		var rows =  $(detailList).datagrid('getRows');
		var feeItemIds = new Array();
		for ( var i = 0; i < rows.length; i++) {
			var row=rows[i];
			if(row.productType=="收费项"){
				feeItemIds.push(row.billItemId);
			}
		}
		var feeItemName = $('#feeItemNameSearch',queryContentFee).val();
		var content = {status:1,feeItemIds:feeItemIds.join(CSIT.join),feeItemName:feeItemName,page:pageNumber,rows:pageSize};
		//取得列表信息
		var url = 'dict/queryFeeItem.do';
		var result = syncCallService(url,content);
		if(result.isSuccess){
			var  data = result.data;
			var datagridData = eval("("+data.datagridData+")");
			$(feeList).datagrid('loadData',datagridData);
			//需要重新重新分页信息
			if(flag){
				getTotalFee(content);
			}
		}else{
			$.messager.alert('提示',result.message,'error');
		}
	}
	//统计总数
	var getTotalFee = function(content){
		var url = "dict/getTotalCountFeeItem.do";
		asyncCallService(url,content,
		function(result){
			if(result.isSuccess){
				var data = result.data;
				$(pagerFee).pagination({  
					pageNumber:1,
					total:data.total
				});
			}else{
				$.messager.alert('提示',result.message,"error");
			}
		})
	}	
	//查询
	$(searchFeeBtn).click(function(){
		searchFee(true);
	})
	//添加消费项
	function onFeeSave()  {   
		var rows = $(feeList).datagrid('getSelections');
		if(rows.length==0){
			$.messager.alert("提示",'请至少选择一条数据行',"warning");
			return;
		}
		//判断是否已选过该消费项
		var oldRows =  $(detailList).datagrid('getRows');
		for(var i=0;i<rows.length;i++){
			 var row=rows[i];
			for(var ii=0;ii<oldRows.length;ii++){
				var oldRow=oldRows[ii];
				if(oldRow.productType=='收费项'&&row.feeItemId==oldRow.billItemId){
					var oldRowIndex = $(detailList).datagrid('getRowIndex',oldRow); 
					$(detailList).datagrid('deleteRow',oldRowIndex);
				}
			}
			selectIndexDetail==null;
			$(detailList).datagrid('insertRow',{
				row: {
					productType: '收费项',
					billItemId: row.feeItemId,
					billItemName: row.feeItemName,
					price: row.fee,
					unitName:'笔'
				}
			});
		}
		
		$(feeList).datagrid('loadData',{ total: 0, rows: [] });
		$(feeItemDialog).dialog('close');
	}
	$(feeItemDialog).dialog({
		title:'添加消费项',
	    width:800,
	    height:450,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    toolbar:[
	    		{id:'save'+id,text:'选择',iconCls:'icon-ok',handler:function(){onFeeSave();}},'-',
				{text:'退出',iconCls:'icon-cancel',handler:function(){
						$(feeItemDialog).dialog('close');
					}
				}
	   ]
	});
	//添加消费项
	var onAddFeeItem = function(){
		$(feeList).datagrid('loadData',{ total: 0, rows: [] });
		$(feeItemDialog).dialog('open');
	};
	//查询消费单
	var searchBill = function(flag){
		var studentId=$('#studentId',$this).val();
		var billType = $('#billTypeSearch',queryBillContent).combobox('getValue');
		var billDateBegin = $('#billDateBeginSearch',queryBillContent).val();
		var billDateEnd = $('#billDateEndSearch',queryBillContent).val();
		var content = {'student.studentId':studentId,billDateBegin:billDateBegin,billDateEnd:billDateEnd,billType:billType,page:pageNumber,rows:pageSize};
		//取得列表信息
		var url = 'customerService/queryBill.do';
		var result = syncCallService(url,content);
		if(result.isSuccess){
			var  data = result.data;
			var datagridData = eval("("+data.datagridData+")");
			$(billList).datagrid('loadData',datagridData);
			//需要重新重新分页信息
			if(flag){
				getTotalBill(content);
			}
		}else{
			$.messager.alert('提示',result.message,'error');
		}
	}
	//统计总数
	var getTotalBill = function(content){
		var url = "customerService/getTotalCountBill.do";
		asyncCallService(url,content,
		function(result){
			if(result.isSuccess){
				var data = result.data;
				$(billList).datagrid('getPager').pagination({  
					pageNumber:1,
					total:data.total
				});
			}else{
				$.messager.alert('提示',result.message,"error");
			}
		})
	}	
	//查询消费单
	$(searchBillBtn).click(function(){
		searchBill(true);
	})
//============================添加课程=================================================================
	  $('#courseTypeSearch',queryContentCourse).combobox({
			  width:150,
			  data:TRN.getCourseTypeList(),
			  valueField:'courseTypeId',
			  textField:'courseTypeName'
		  });
	  //课程列表
	  $(courseList).datagrid({
		fit:true,
		method:"POST",
		nowrap:true,
		striped: true,
		collapsible:true,
		rownumbers:true,
		fit:true,
		columns:[[
		    {field:'ck',width:50,checkbox:true},
		    {field:'courseId',hidden:true},
			{field:'courseName',title:'课程名称',width:100,align:"center"},
			{field:'unitPrice',title:'价格',width:100,align:"center"},
			{field:'courseUnit',title:'单位',width:100,align:"center"},
			{field:'courseTypeName',title:'分类',width:100,align:"center"}
		]]
	  });
	  //分页条
	$(pagerCourse).pagination({   
	    onSelectPage: function(page, rows){
			pageNumber = page;
			pageSize = rows;
			searchCourse();
	    }
	});
	//查询课程
	var searchCourse = function(flag){
		var rows =  $(detailList).datagrid('getRows');
		var courseIds = new Array();
		for ( var i = 0; i < rows.length; i++) {
			var row=rows[i];
			if(row.productType=="课程"){
				courseIds.push(row.billItemId);
			}
		}
		var courseName = $('#courseNameSearch',queryContentCourse).val();
		var courseTypeId = $('#courseTypeSearch',queryContentCourse).combobox('getValue');
		var content = {status:1,courseIds:courseIds.join(CSIT.join),courseName:courseName,'courseType.courseTypeId':courseTypeId,page:pageNumber,rows:pageSize};
		//取得列表信息
		var url = 'dict/queryCourse.do';
		var result = syncCallService(url,content);
		if(result.isSuccess){
			var  data = result.data;
			var datagridData = eval("("+data.datagridData+")");
			$(courseList).datagrid('loadData',datagridData);
			//需要重新重新分页信息
			if(flag){
				getTotalCourse(content);
			}
		}else{
			$.messager.alert('提示',result.message,'error');
		}
	}
	//统计课程总数
	var getTotalCourse = function(content){
		var url = "dict/getTotalCountCourse.do";
		asyncCallService(url,content,
		function(result){
			if(result.isSuccess){
				var data = result.data;
				$(pagerCourse).pagination({  
					pageNumber:1,
					total:data.total
				});
			}else{
				$.messager.alert('提示',result.message,"error");
			}
		})
	}	
	//查询课程
	$(searchCourseBtn).click(function(){
		searchCourse(true);
	})
	//添加课程
	function onCourseSave()  {   
		var rows = $(courseList).datagrid('getSelections');
		if(rows.length==0){
			$.messager.alert("提示",'请至少选择一条数据行',"warning");
			return;
		}
		//判断是否已选过该课程
		var oldRows =  $(detailList).datagrid('getRows');
		for(var i=0;i<rows.length;i++){
			 var row=rows[i];
			for(var ii=0;ii<oldRows.length;ii++){
				var oldRow=oldRows[ii];
				if(oldRow.productType=='课程'&&row.courseId==oldRow.billItemId){
					var oldRowIndex = $(detailList).datagrid('getRowIndex',oldRow); 
					$(detailList).datagrid('deleteRow',oldRowIndex);
				}
			}
			selectIndexDetail==null;
			$(detailList).datagrid('insertRow',{
				row: {
					productType: '课程',
					billItemId: row.courseId,
					billItemName: row.courseName,
					price: row.unitPrice,
					unitName:row.courseUnit
				}
			});
		}
		
		$(courseList).datagrid('loadData',{ total: 0, rows: [] });
		$(courseDialog).dialog('close');
	}
	$(courseDialog).dialog({
		title:'添加课程',
	    width:800,
	    height:450,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    toolbar:[
	    		{id:'save'+id,text:'选择',iconCls:'icon-ok',handler:function(){onCourseSave();}},'-',
				{text:'退出',iconCls:'icon-cancel',handler:function(){
						$(courseDialog).dialog('close');
					}
				}
	   ]
	});
	//添加消费项
	var onAddCourse = function(){
		$(courseList).datagrid('loadData',{ total: 0, rows: [] });
		$(courseDialog).dialog('open');
	}
//================================添加商品==========================================================
		$('#unitSearch',queryContentCommodity).combobox({
				width:150,
				data:TRN.getUnitList(),
				valueField:'unitId',
				textField:'unitName'
		});
		$('#commodityTypeSearch',queryContentCommodity).combobox({
			  width:150,
			  data:TRN.getCommodityTypeList(),
			  valueField:'commodityTypeId',
			  textField:'commodityTypeName'
		});
	  //商品列表
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
		    {field:'commodityTypeId',hidden:true},
			{field:'unitId',hidden:true},
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
	  //分页条
	$(pagerCommodity).pagination({   
	    onSelectPage: function(page, rows){
			pageNumber = page;
			pageSize = rows;
			searchCourse();
	    }
	});
	//查询商品
	var searchCommodity = function(flag){
		var rows =  $(detailList).datagrid('getRows');
		var commodityIds = new Array();
		for ( var i = 0; i < rows.length; i++) {
			var row=rows[i];
			if(row.productType=="商品"){
				commodityIds.push(row.billItemId);
			}
		}
		var commodityTypeId = $('#commodityTypeSearch',queryContentCommodity).combobox('getValue');
		var unitId = $('#unitSearch',queryContentCommodity).combobox('getValue');
		var commodityName = $.trim($('#commodityNameSearch',queryContentCommodity).val());
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
		
		var content = {status:1,commodityIds:commodityIds.join(CSIT.join),commodityName:commodityName,page:pageNumber,rows:pageSize,'commodityType.commodityTypeId':commodityTypeId,'unit.unitId':unitId};
		//取得列表信息
		var url = 'dict/queryCommodity.do';
		var result = syncCallService(url,content);
		if(result.isSuccess){
			var  data = result.data;
			var datagridData = eval("("+data.datagridData+")");
			$(commodityList).datagrid('loadData',datagridData);
			//需要重新重新分页信息
			if(flag){
				getTotalCommodity(content);
			}
		}else{
			$.messager.alert('提示',result.message,'error');
		}
	}
	//判断死否是数字
	function isNumber(s)  {   
		var strP=/^\d+$/;
		if(''==s||strP.test(s)) { 
			return true;
		}else{
			return false;
		}
	}
	//统计商品总数
	var getTotalCommodity = function(content){
		var url = "dict/getTotalCountCommodity.do";
		asyncCallService(url,content,
		function(result){
			if(result.isSuccess){
				var data = result.data;
				$(pagerCommodity).pagination({  
					pageNumber:1,
					total:data.total
				});
			}else{
				$.messager.alert('提示',result.message,"error");
			}
		});
	}	
	//查询商品
	$(searchCommodityBtn).click(function(){
		searchCommodity(true);
	})
	//添加商品
	function onCommoditySave()  {   
		var rows = $(commodityList).datagrid('getSelections');
		if(rows.length==0){
			$.messager.alert("提示",'请至少选择一条数据行',"warning");
			return;
		}
		//判断是否已选过该商品
		var oldRows =  $(detailList).datagrid('getRows');
		for(var i=0;i<rows.length;i++){
			 var row=rows[i];
			for(var ii=0;ii<oldRows.length;ii++){
				var oldRow=oldRows[ii];
				if(oldRow.productType=='商品'&&row.commodityId==oldRow.billItemId){
					var oldRowIndex = $(detailList).datagrid('getRowIndex',oldRow); 
					$(detailList).datagrid('deleteRow',oldRowIndex);
				}
			}
			selectIndexDetail==null;
			$(detailList).datagrid('insertRow',{
				row: {
					productType: '商品',
					billItemId: row.commodityId,
					billItemName: row.commodityName,
					price: row.salePrice,
					unitName:row.unitName
				}
			});
		}
		
		$(commodityList).datagrid('loadData',{ total: 0, rows: [] });
		$(commodityDialog).dialog('close');
	}
	$(commodityDialog).dialog({
		title:'添加商品',
	    width:900,
	    height:450,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    toolbar:[
	    		{id:'save'+id,text:'选择',iconCls:'icon-ok',handler:function(){onCommoditySave();}},'-',
				{text:'退出',iconCls:'icon-cancel',handler:function(){
						$(commodityDialog).dialog('close');
					}
				}
	   ]
	});
	//添加消费项
	var onAddCommodity = function(){
		$(commodityList).datagrid('loadData',{ total: 0, rows: [] });
		$(commodityDialog).dialog('open');
	};
	//打印 
	var onPrint = function(){
		if(selectRowBill ==null){
			$.messager.alert('提示','请选中要打印的记录行','warning');
    		return;
		}
		print(selectRowBill.billId);
	}
	//打印
	var print = function(billId){
		window.open("printReport.jsp?report=bill&data=ReportServlet?billId="+billId);
	}
  }
})(jQuery);   