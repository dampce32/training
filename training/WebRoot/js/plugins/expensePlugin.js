// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.expenseInit = function() {
	  var $this = $(this);
	  var id = $(this).attr('id');
	  var width = $(document.body).width();
	  var height = $(document.body).height();
	  var pageNumber = 1;
	  var pageSize = 10;
	  var queryBillContent = $('#queryBillContent',$this);
	  var searchBillBtn = $('#searchBillBtn',$this);
	  var detailList = $('#detailList',$this);
	  var billList = $('#billList',$this);
	  var selectRowBill = null;
	  var selectIndexBill = null;
	  var selectRowDetail = null;
	  var selectIndexDetail = null;
	  var editBillDialog = $('#editBillDialog',$this);
	  var editBillForm = $('#editBillForm',$this);
	  var editReturnCourseDialog = $('#editReturnCourseDialog',$this);
	  var editReturnCourseForm = $('#editReturnCourseForm',editReturnCourseDialog);
	  var ReturnCourse = $('#ReturnCourse',$this);
	  
	  $('#schoolSearch',queryBillContent).combobox({
		  width:150,
		  data:TRN.getSelfSchoolList(),
		  valueField:'schoolId',
		  textField:'schoolName'
	  });
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
						{text:'查看详细',iconCls:'icon-search',handler:function(){onLookDetail();}}
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
		title:'退货单详细',
	    width:940,
	    height:600,
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
	}
	
	$(editBillDialog).dialog({
		title:'消费单详细',
	    width:940,
	    height:600,
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
	//查询消费单
	var searchBill = function(flag){
		var billType = $('#billTypeSearch',queryBillContent).combobox('getValue');
		var schoolId = $('#schoolSearch',queryBillContent).combobox('getValue');
		if(schoolId==''){
			schoolId = -1 ;
		}
		var billDateBegin = $('#billDateBeginSearch',queryBillContent).val();
		var billDateEnd = $('#billDateEndSearch',queryBillContent).val();
		var content = {'school.schoolId':schoolId,billDateBegin:billDateBegin,billDateEnd:billDateEnd,billType:billType,page:pageNumber,rows:pageSize};
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
	})
  }
})(jQuery);   