// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.myAccountInit = function() {
	  var $this = $(this);
	  var id = $(this).attr('id');
	  var width = $(document.body).width();
	  var height = $(document.body).height();
	  var pageNumber = 1;
	  var pageSize = 10;
	  var queryPaymentContent = $('#queryPaymentContent',$this);
	  var searchBtnPayment = $('#searchBtnPayment',$this);
	  var paymentList = $('#paymentList',$this);
	  var selectRowPayment = null;
	  var selectIndexPayment = null;
	  var editPaymentForm = $('#editPaymentForm',$this);
	  var editPaymentDialog = $('#editPaymentDialog',$this);
	  
	$(paymentList).datagrid( {
		method:"POST",
		singleSelect:true,
		nowrap:true,
		striped: true,
		collapsible:true,
		rownumbers:true,
		pagination:true,
		fit:true,
		toolbar:[	
					{text:'添加',iconCls:'icon-add',handler:function(){onAddPayment()}}
				],
		columns : [ [ {field : 'paymentType',title : '类型',width : 100,align : "center",
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
					  {field : 'creditExpiration',title : '借款到期',width : 100,align : "center"},
					  {field : 'schoolName',title : '学习中心',width : 100,align : "center"},
					  {field : 'userName',title : '收费文员',width : 100,align : "center"}
				 ] ],
		onClickRow:function(rowIndex, rowData){
		selectRowPayment = rowData;
		selectIndexPayment = rowIndex;
		},
		onLoadSuccess:function(){
			selectRowPayment = null;
	 		selectIndexPayment = null;
			pageNumber = 1;
		}
	});
	//分页条
	$(paymentList).datagrid('getPager').pagination({   
	    onSelectPage: function(page, rows){
			pageNumber = page;
			pageSize = rows;
			searchPayment();
	    }
	});
	//账单使用
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
		selectIndexPayment = null;
		selectRowPayment = null;
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
		var studentId=$('#studentId',$this).val();
		$('#student',editPaymentForm).val(studentId);
		return true;
	}
	
	//保存账单
	var onSavePayment = function(){
		$.messager.confirm("提示","确定要此操作吗?",function(t){
			if(t){
				var creditExpiration = $.trim($('#creditExpiration',editPaymentForm).val());
				var oldCreditExpiration=$('#creditExpiration',$this).val();
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
					url: 'customerService/savePayment.do',
					onSubmit: function(){
						return setValuePayment();
					},
					success: function(data){
						var result = eval('('+data+')');
						if(result.isSuccess){
							var fn = function(){
								$('#arrearageMoney',$this).val(result.data.arrearageMoney);
								$('#availableMoney',$this).val(result.data.availableMoney);
								$('#consumedMoney',$this).val(result.data.consumedMoney);
								$('#creditExpiration',$this).val(result.data.creditExpiration);
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
	//查询回访记录
	var searchPayment = function(flag){
		var studentId=$('#studentId',$this).val();
		var paymentType = $('#paymentTypeSearch',queryPaymentContent).combobox('getValue');
		var transactionDateBegin = $('#transactionDateBeginSearch',queryPaymentContent).val();
		var transactionDateEnd = $('#transactionDateEndSearch',queryPaymentContent).val();
		var content = {paymentType:paymentType,transactionDateBegin:transactionDateBegin,transactionDateEnd:transactionDateEnd,'student.studentId':studentId,page:pageNumber,rows:pageSize};
		//取得列表信息
		var url = 'customerService/queryPayment.do';
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
	//统计总数回访记录
	var getTotalPayment = function(content){
		var url = "customerService/getTotalCountPayment.do";
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
	})
  }
})(jQuery);   