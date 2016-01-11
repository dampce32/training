// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.stuExpenseInit = function() {
	  var $this = $(this);
	  var id = $(this).attr('id');
	  var width = $(document.body).width();
	  var height = $(document.body).height();
	  var studentId=$('#studentId',$this).val();
	  var pageNumber = 1;
	  var pageSize = 10;
	  var queryContent = $('#queryContent',$this);
	  var searchBtn = $('#searchBtn',$this);
	  var detailList = $('#detailList',$this);
	  var billList = $('#billList',$this);
	  var selectRow = null;
	  var selectIndex = null;
	  var selectRowDetail = null;
	  var selectIndexDetail = null;
	  var editBillDialog = $('#editBillDialog',$this);
	  var editBillForm = $('#editBillForm',$this);
	  var editReturnCourseDialog = $('#editReturnCourseDialog',$this);
	  var editReturnCourseForm = $('#editReturnCourseForm',editReturnCourseDialog);
	  var ReturnCourse = $('#ReturnCourse',$this);
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
			selectRow = rowData;
			selectIndex = rowIndex;
			},
			onDblClickRow:function(rowIndex,rowData){
				onLookDetail();
			},
			onLoadSuccess:function(){
				selectRow = null;
		 		selectIndex = null;
				pageNumber = 1;
			}
		});
	//分页条
	$(billList).datagrid('getPager').pagination({   
	    onSelectPage: function(page, rows){
			pageNumber = page;
			pageSize = rows;
			search();
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
		if(selectRow==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		};
		$(editBillForm).form('clear');
		if(selectRow.billType==1){
			$(editBillForm).form('load',selectRow);
			$('#studentName',editBillForm).val(selectRow.studentName);
			$('#studentId',editBillForm).val(selectRow.studentId);
			$('#school',editBillForm).val(selectRow.schoolName);
			$('#user',editBillForm).val(selectRow.userName);
			$(detailList).datagrid('loadData', { total: 0, rows: [] });
			//取得列表信息
			var url = 'student/queryDetailBill.do';
			var content={billId:selectRow.billId};
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
		if(selectRow.billType==0){
			$(editReturnCourseForm).form('load',selectRow);
			$('#school',editReturnCourseForm).val(selectRow.schoolName);
			$('#user',editReturnCourseForm).val(selectRow.userName);
			$(ReturnCourse).datagrid('loadData', { total: 0, rows: [] });
			//取得列表信息
			var url = 'student/queryDetailBill.do';
			var content={billId:selectRow.billId};
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
					  {field : 'qty',title : '数量',width : 80,align : 'center'},
					  {field:'totalPrice',title:'合计',width:100,align:"center"},
					  {field : 'discount',title : '折扣',width : 50,align : "center"},
					  {field : 'discountAmount',title : '优惠金额',width : 100,align : 'center'},
					  {field : 'payed',title : '实收金额',width : 100,align : 'center'},
					  {field : 'unitName',title : '单位',width : 80,align : 'center'},
					  {field:'warehouseName',title:'仓库',width:150,align:"center"}
				 ] ],
		onClickRow:function(rowIndex, rowData){
				selectIndexDetail = rowIndex;
		},
		onLoadSuccess:function(){
			selectRowDetail = null;
	 		selectIndexDetail = null;
			pageNumber = 1;
		}
	});
	//查询消费单
	var search = function(flag){
		var billType = $('#billTypeSearch',queryContent).combobox('getValue');
		var billDateBegin = $('#billDateBeginSearch',queryContent).val();
		var billDateEnd = $('#billDateEndSearch',queryContent).val();
		var content = {'student.studentId':studentId,billDateBegin:billDateBegin,billDateEnd:billDateEnd,billType:billType,page:pageNumber,rows:pageSize};
		//取得列表信息
		var url = 'student/queryBill.do';
		var result = syncCallService(url,content);
		if(result.isSuccess){
			var  data = result.data;
			var datagridData = eval("("+data.datagridData+")");
			$(billList).datagrid('loadData',datagridData);
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
		var url = "student/getTotalCountBill.do";
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
	$(searchBtn).click(function(){
		search(true);
	})
  }
})(jQuery);   