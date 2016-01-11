// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.rechargeInit = function() {
	  var $this = $(this);
	  var id = $(this).attr('id');
	  var width = $(document.body).width();
	  var height = $(document.body).height();
	  var pageNumber = 1;
	  var pageSize = 10;
	  var queryPaymentContent = $('#queryPaymentContent',$this);
	  var searchBtnPayment = $('#searchBtnPayment',$this);
	  var paymentList = $('#paymentList',$this);
	  
	  $('#schoolSearch',queryPaymentContent).combobox({
		  width:150,
		  data:TRN.getSelfSchoolList(),
		  valueField:'schoolId',
		  textField:'schoolName'
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
		columns : [ [ 	{field : 'studentId',title : '学号',width : 100,align : "center"},
						{field : 'studentName',title : '姓名',width : 100,align : "center"},
						{field : 'paymentType',title : '类型',width : 100,align : "center",
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
		if(schoolId==''){
			schoolId = -1 ;
		}
		var transactionDateBegin = $('#transactionDateBeginSearch',queryPaymentContent).val();
		var transactionDateEnd = $('#transactionDateEndSearch',queryPaymentContent).val();
		var content = {paymentType:paymentType,'school.schoolId':schoolId,transactionDateBegin:transactionDateBegin,transactionDateEnd:transactionDateEnd,page:pageNumber,rows:pageSize};
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
	//统计总数回访记录
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
	})
  }
})(jQuery);   