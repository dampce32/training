// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.arrearsInit = function() {
	  var $this = $(this);
	  var id = $(this).attr('id');
	  var width = $(document.body).width();
	  var height = $(document.body).height();
	  var queryContent = $('#queryContent',$this);
	  var searchBtn = $('#searchBtn',$this);
	  var changeSearch = false;
	  var viewList = $('#viewList',$this);
	  var selectRow = null;
	  var selectIndex = null;
	  var pageNumber = 1;
	  var pageSize = 10;
	  var billDialog = $('#billDialog',$this);
	  var billList = $('#billList',$this);
	  var selectRowBill = null;
	  var selectIndexBill = null;
	  var queryBillContent = $('#queryBillContent',$this);
	  var searchBillBtn = $('#searchBillBtn',$this);
	  var selectRowDetail = null;
	  var selectIndexDetail = null;
	  var editBillDialog = $('#editBillDialog',$this);
	  var editBillForm = $('#editBillForm',$this);
	  var editReturnCourseDialog = $('#editReturnCourseDialog',$this);
	  var editReturnCourseForm = $('#editReturnCourseForm',editReturnCourseDialog);
	  var ReturnCourse = $('#ReturnCourse',$this);
	  var detailList = $('#detailList',$this);
	  var queryPaymentContent = $('#queryPaymentContent',$this);
	  var searchBtnPayment = $('#searchBtnPayment',$this);
	  var paymentDialog = $('#paymentDialog',$this);
	  var paymentList = $('#paymentList',$this);
	  var editPaymentForm = $('#editPaymentForm',$this);
	  var editPaymentDialog = $('#editPaymentDialog',$this);
	  $('#schoolSearch',queryContent).combobox({
		  width:150,
		  data:TRN.getSelfSchoolList(),
		  valueField:'schoolId',
		  textField:'schoolName'
	  });
//=====================================欠费学员信息===========================================
	//加载列表
	  $(viewList).datagrid({
		method:"POST",
		singleSelect:true,
		nowrap:true,
		striped: true,
		collapsible:true,
		rownumbers:true,
		pagination:true,
		fit:true,
		toolbar:[	
					{text:'账单',iconCls:'icon-search',handler:function(){onBill()}},'-',
					{text:'充值记录',iconCls:'icon-search',handler:function(){onPayment()}}
				],
		columns:[[  {field:'studentId',title:'学号',width:100,align:"center"},
					{field:'studentName',title:'姓名',width:100,align:"center"},
					{field:'schoolName',title:'校区',width:100,align:"center"},
					{field:'availableMoney',title:'可用金额',width:100,align:"center"},
					{field:'billCount',title:'消费单数量',width:100,align:"center"},
					{field:'arrearageMoney',title:'欠费金额',width:100,align:"center"},
					{field:'creditExpiration',title:'借款到期时间',width:100,align:"center"}
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
	//分页条
	$(viewList).datagrid('getPager').pagination({   
	    onSelectPage: function(page, rows){
			pageNumber = page;
			pageSize = rows;
			search();
	    }
	});
	//查询
	var search = function(flag){
		var studentName = $('#nameSearch',queryContent).val();
		var schoolId = $('#schoolSearch',queryContent).combobox('getValue');
		if(schoolId==''){
			schoolId = -1 ;
		}
		var content = {'school.schoolId':schoolId,studentName:studentName,arrearageMoney:0,type:1,page:pageNumber,rows:pageSize};
		//取得列表信息
		var url = 'finance/queryStudent.do';
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
	}
	//统计总数
	var getTotal = function(content){
		var url = "finance/getTotalCountStudent.do";
		asyncCallService(url,content,
		function(result){
			if(result.isSuccess){
				var data = result.data;
				$(viewList).datagrid('getPager').pagination({  
					pageNumber:1,
					total:data.total
				});
			}else{
				$.messager.alert('提示',result.message,"error");
			}
		})
	}	
	//查询
	$(searchBtn).click(function(){
		search(true);
	})
//==========================查看账单==================================================================
	var onBill = function(){
		if(selectRow==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		};
		$(billList).datagrid('loadData',{ total: 0, rows: [] });
		$(billDialog).dialog('open');
	};
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
						{text:'查看详细',iconCls:'icon-search',handler:function(){onLookDetail();}},'-',
						{text:'退出',iconCls:'icon-cancel',handler:function(){$(billDialog).dialog('close')}}
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
	$(billDialog).dialog({  
	    title: '账单',  
	    width:width-200,
	    height:height-20,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false
	});
	//查询消费单
	var searchBill = function(flag){
		var billType = $('#billTypeSearch',queryBillContent).combobox('getValue');
		var billDateBegin = $('#billDateBeginSearch',queryBillContent).val();
		var billDateEnd = $('#billDateEndSearch',queryBillContent).val();
		var content = {'student.studentId':selectRow.studentId,billDateBegin:billDateBegin,billDateEnd:billDateEnd,billType:billType,page:pageNumber,rows:pageSize};
		//取得列表信息
		var url = 'finance/queryBill.do';
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
		var url = "finance/getTotalCountBill.do";
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
	});
	//消费单明细
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
		$(editBillForm).form('clear');
		if(selectRowBill.billType==1){
			$(editBillForm).form('load',selectRowBill);
			$('#studentName',editBillForm).val(selectRowBill.studentName);
			$('#studentId',editBillForm).val(selectRowBill.studentId);
			$('#school',editBillForm).val(selectRowBill.schoolName);
			$('#user',editBillForm).val(selectRowBill.userName);
			$(detailList).datagrid('loadData', { total: 0, rows: [] });
			//取得列表信息
			var url = 'finance/queryDetailBill.do';
			var content={billId:selectRowBill.billId};
			var result = syncCallService(url,content);
			if(result.isSuccess){
				var data = result.data;
				var datagridData = eval("("+data.datagridData+")");
				$(detailList).datagrid('loadData',datagridData);
			}else{
				$.messager.alert('提示',result.message,'error');
			}
			$(editBillDialog).dialog('open');
		}
		if(selectRowBill.billType==0){
			$(editReturnCourseForm).form('load',selectRowBill);
			$('#school',editReturnCourseForm).val(selectRowBill.schoolName);
			$('#user',editReturnCourseForm).val(selectRowBill.userName);
			$(ReturnCourse).datagrid('loadData', { total: 0, rows: [] });
			//取得列表信息
			var url = 'finance/queryDetailBill.do';
			var content={billId:selectRowBill.billId};
			var result = syncCallService(url,content);
			if(result.isSuccess){
				var data = result.data;
				var datagridData = eval("("+data.datagridData+")");
				$(ReturnCourse).datagrid('loadData',datagridData);
			}else{
				$.messager.alert('提示',result.message,'error');
			}
			$(editReturnCourseDialog).dialog('open');
		}
	};
	$(editBillDialog).dialog({
		title:'添加消费',
	    width:width-350,
	    height:height-40,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    toolbar:[
				{text:'退出',iconCls:'icon-cancel',handler:function(){
						$(editBillDialog).dialog('close');
					}
				}
	   ]
	});
	$(detailList).datagrid( {
		method:"POST",
		singleSelect:true,
		nowrap:true,
		striped: true,
		collapsible:true,
		rownumbers:true,
		fit:true,
		columns : [ [ {field : 'billItemId',hidden:true},
					  {field : 'productType',title : '收费类型',width : 100,align : "center"}, 
					  {field : 'billItemName',title : '收费项名称',width : 100,align : 'center'}, 
					  {field : 'price',title : '单价',width : 100,align : 'center'},
					  {field:'totalPrice',title:'合计',width:100,align:"center"},
					  {field : 'discount',title : '折扣',width : 50,align : "center"},
					  {field : 'discountAmount',title : '优惠金额',width : 100,align : 'center'},
					  {field : 'payed',title : '实收金额',width : 100,align : 'center'},
					  {field : 'unitName',title : '单位',width : 80,align : 'center'},
					  {field:'warehouseName',title:'仓库',width:150,align:"center"}
				 ] ]
	});
//================================查看充值记录=================================================
	$('#schoolSearch',queryPaymentContent).combobox({
		  width:150,
		  data:TRN.getAllSchoolList(),
		  valueField:'schoolId',
		  textField:'schoolName'
	  });
	var onPayment = function(){
		if(selectRow==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		};
		$(paymentList).datagrid('loadData',{ total: 0, rows: [] });
		$(paymentDialog).dialog('open');
	};
	$(paymentDialog).dialog({  
	    title: '充值记录',  
	    width:width-200,
	    height:height-20,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false
	});
	$(paymentList).datagrid( {
		method:"POST",
		singleSelect:true,
		nowrap:true,
		striped: true,
		collapsible:true,
		rownumbers:true,
		pagination:true,
		fit:true,
		toolbar:[		{text:'添加',iconCls:'icon-add',handler:function(){onAddPayment()}},'-',
						{text:'退出',iconCls:'icon-cancel',handler:function(){$(paymentDialog).dialog('close')}}
					],
		columns : [ [ 	{field : 'paymentType',title : '类型',width : 100,align : "center",
								formatter: function(value,row,index){
											if (value==1){
												return '交费';
											} else if (value==2){
												return '退费';
											}else if (value==3){
												return '借款';
											}else if (value==4){
												return '扣除借款';
											}else if (value==5){
												return '业务扣费';
											}else if (value==6){
												return '业务退费';
											}
					  }}, 
					  {field : 'payMoney',title : '金额',width : 100,align : 'center'}, 
					  {field : 'payway',title : '收款方式',width : 100,align : 'center',
								formatter: function(value,row,index){
											if (value==1){
												return '现金';
											} else if (value==2){
												return '刷卡';
											}else if (value==3){
												return '转账';
											}else if (value==4){
												return '支票';
											}else if (value==5){
												return '网银';
											}else if (value==6){
												return '支付宝';
											}else if (value==7){
												return '其它';
											}
					  }}, 
					  {field : 'transactionDate',title : '日期',width : 100,align : "center"},
					  {field : 'note',title : '备注',width : 200,align : "center"},
					  {field : 'schoolName',title : '学习中心',width : 100,align : "center"},
					  {field : 'userName',title : '收费文员',width : 100,align : "center"}
				 ] ]
	});
	//分页条
	$(paymentList).datagrid('getPager').pagination({   
	    onSelectPage: function(page, rows){
			pageNumber = page;
			pageSize = rows;
			searchPayment();
	    }
	});
	//查询充值记录
	var searchPayment = function(flag){
		var paymentType = $('#paymentTypeSearch',queryPaymentContent).combobox('getValue');
		var schoolId = $('#schoolSearch',queryPaymentContent).combobox('getValue');
		var transactionDateBegin = $('#transactionDateBeginSearch',queryPaymentContent).val();
		var transactionDateEnd = $('#transactionDateEndSearch',queryPaymentContent).val();
		var content = {'student.studentId':selectRow.studentId,paymentType:paymentType,'school.schoolId':schoolId,transactionDateBegin:transactionDateBegin,transactionDateEnd:transactionDateEnd,page:pageNumber,rows:pageSize};
		//取得列表信息
		var url = 'finance/queryPayment.do';
		var result = syncCallService(url,content);
		if(result.isSuccess){
			var  data = result.data;
			var datagridData = eval("("+data.datagridData+")");
			$(paymentList).datagrid('loadData',datagridData);
			//需要重新重新分页信息
			if(flag){
				getTotalPayment(content);
			}
		}else{
			$.messager.alert('提示',result.message,'error');
		}
	}
	//统计总数账单记录
	var getTotalPayment = function(content){
		var url = "finance/getTotalCountPayment.do";
		asyncCallService(url,content,
		function(result){
			if(result.isSuccess){
				var data = result.data;
				$(paymentList).datagrid('getPager').pagination({  
					pageNumber:1,
					total:data.total
				});
			}else{
				$.messager.alert('提示',result.message,"error");
			}
		})
	}	
	//查询
	$(searchBtnPayment).click(function(){
		searchPayment(true);
	});
	var initComboboxPayment = function(){
		//交费点
		$('#school',editPaymentDialog).combobox({
			valueField:'schoolId',
			textField:'schoolName',
			width:250,
			data:TRN.getSelfSchoolList()
		});
		//收费文员
		$('#user',editPaymentDialog).combobox({
		  width:250,
		  data:TRN.getEmployeeList(),
		  valueField:'userId',
		  textField:'userName'
	  });
	}
	//添加账单
	var onAddPayment = function(){
		$(paymentList).datagrid('unselectAll');
		$(editPaymentForm).form('clear');
		initComboboxPayment();
		$('#paymentType',editPaymentDialog).combobox('setValue',1);
		$('#payway',editPaymentDialog).combobox('setValue',1);
		$('#paywaytr',editPaymentDialog).show();
		$('#creditExpirationtr',editPaymentDialog).hide();
		$(editPaymentDialog).dialog('open');
	}
	$(editPaymentDialog).dialog({
		title:'添加账单',
	    width:500,
	    height:300,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    toolbar:[
	    		{id:'save'+id,text:'保存',iconCls:'icon-save',handler:function(){onSavePayment();}},'-',
				{text:'退出',iconCls:'icon-cancel',handler:function(){
						$(editPaymentDialog).dialog('close');
					}
				}
	   ]
	});

	$('#paymentType',editPaymentForm).combobox({
		editable:false,
		onSelect:function(record){ 
			if($('#paymentType',editPaymentForm).combobox('getValue')==1){
				$('#creditExpirationtr',editPaymentDialog).hide();
				$('#paywaytr',editPaymentDialog).show();
			}
			else if($('#paymentType',editPaymentForm).combobox('getValue')==3){
				$('#creditExpirationtr',editPaymentDialog).show();
				$('#paywaytr',editPaymentDialog).hide();
			}else {
				$('#creditExpirationtr',editPaymentDialog).hide();
				$('#paywaytr',editPaymentDialog).hide();
			}
        }
	});
	var setValuePayment = function(){
		//交费类型
		var paymentType = $('#paymentType',editPaymentForm).combobox('getValue') ;
		if(''==paymentType){
			$.messager.alert('提示','请选择交费类型','warning');
			return false;
		}
		if(isNaN(parseInt(paymentType))){
			$.messager.alert('提示','请选择提供选择的交费类型','warning');
			return false;
		}
		//金额
		var payMoney = $.trim($('#payMoney',editPaymentForm).val());
		if(''==payMoney){
			$.messager.alert('提示','请填写金额','warning');
			return false;
		}
		if(payMoney<=0){
			$.messager.alert('提示','填写金额不能小于0','warning');
			return false;
		}
		//日期
		var transactionDate = $.trim($('#transactionDate',editPaymentForm).val());
		if(''==transactionDate){
			$.messager.alert('提示','请填写日期','warning');
			return false;
		}
		//交费点
		var schoolId = $('#school',editPaymentForm).combobox('getValue') ;
		if(''==schoolId){
			$.messager.alert('提示','请选择交费点','warning');
			return false;
		}
		if(isNaN(parseInt(schoolId))){
			$.messager.alert('提示','请选择提供选择的交费点','warning');
			return false;
		}
		
		//收费文员
		var userId = $('#user',editPaymentForm).combobox('getValue') ;
		if(''==userId){
			$.messager.alert('提示','请选择收费文员','warning');
			return false;
		}
		if(isNaN(parseInt(userId))){
			$.messager.alert('提示','请选择提供选择的收费文员','warning');
			return false;
		}
		$('#student',editPaymentForm).val(selectRow.studentId);
		return true;
	}
	
	//保存账单
	var onSavePayment = function(){
		$.messager.confirm("提示","确定要此操作吗?",function(t){
			if(t){
				var creditExpiration = $('#creditExpiration',editPaymentForm).val();
				var oldCreditExpiration=selectRow.creditExpiration;
				if($('#paymentType',editPaymentForm).combobox('getValue')==3){
					if(oldCreditExpiration!=null&&oldCreditExpiration!=''){
							$.messager.confirm("提示","该学员之前还款日期为"+oldCreditExpiration+"确定将还款日期改为"+creditExpiration+"吗?",function(t){
							if(t){
								$('#confirmCreditExpiration',editPaymentForm).val(1);
								savePayment();
							}else{
								$('#confirmCreditExpiration',editPaymentForm).val(0);
								savePayment();
							}
						})
					}else{
						$('#confirmCreditExpiration',editPaymentForm).val(1);
						savePayment();
					}
				}else{
					savePayment();
				}
				
			}
		})
	}
	var savePayment = function(){
		$(editPaymentForm).form('submit',{
					url: 'finance/savePayment.do',
					onSubmit: function(){
						return setValuePayment();
					},
					success: function(data){
						var result = eval('('+data+')');
						if(result.isSuccess){
							var fn = function(){
								selectRow.creditExpiration=result.data.creditExpiration;
								searchPayment(true);
								$(editPaymentDialog).dialog('close');
							}
							$.messager.alert('提示','保存成功','info',fn);
						}else{
							$.messager.alert('提示',result.message,"error");
						}
					}
				});
	}
  }
})(jQuery);   