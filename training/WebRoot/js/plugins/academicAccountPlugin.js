// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.academicAccountInit = function() {
	  var $this = $(this);
	  var id = $(this).attr('id');
	  
	  var pageNumber = 1;
	  var pageSize = 10;
	  var queryPaymentContent = $('#queryPaymentContent',$this);
	  var searchBtnPayment = $('#searchBtnPayment',$this);
	  var paymentList = $('#paymentList',$this);
	  var selectRowPayment = null;
	  var selectIndexPayment = null;
	  var editDialog = $('#editDialog',$this);
	  var editForm = $('#editForm',$this);
	$(paymentList).datagrid( {
		method:"POST",
		singleSelect:true,
		nowrap:true,
		striped: true,
		collapsible:true,
		rownumbers:true,
		pagination:true,
		fit:true,
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
	//查询回访记录
	var searchPayment = function(flag){
		var studentId=$('#studentId',$this).val();
		var paymentType = $('#paymentTypeSearch',queryPaymentContent).combobox('getValue');
		var transactionDateBegin = $('#transactionDateBeginSearch',queryPaymentContent).val();
		var transactionDateEnd = $('#transactionDateEndSearch',queryPaymentContent).val();
		var content = {paymentType:paymentType,transactionDateBegin:transactionDateBegin,transactionDateEnd:transactionDateEnd,'student.studentId':studentId,page:pageNumber,rows:pageSize};
		//取得列表信息
		var url = 'academic/queryPayment.do';
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
	};
	//统计总数回访记录
	var getTotalPayment = function(content){
		var url = "academic/getTotalCountPayment.do";
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
		});
	};	
	//查询
	$(searchBtnPayment).click(function(){
		searchPayment(true);
	});
	//添加支付宝
	var onAdd = function(){
		$(editForm).form('clear');
		$(editDialog).dialog('open');
	};
	$(editDialog).dialog({
		title:'支付宝充值',
	    width:400,
	    height:150,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    toolbar:[
	    		{id:'save'+id,text:'保存',iconCls:'icon-save',handler:function(){onSavePayment();}},'-',
				{text:'退出',iconCls:'icon-cancel',handler:function(){
						$(editDialog).dialog('close');
					}
				}
	   ]
	});
  };
})(jQuery);   