// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.stuProductInit = function() {
	  var $this = $(this);
	  var id = $(this).attr('id');
	  var width = $(document.body).width();
	  var height = $(document.body).height();
	  var studentId=$('#studentId',$this).val();
	  var queryContent = $('#queryContent',$this);
	  var searchBtn = $('#searchBtn',$this);
	  var viewList = $('#viewList',$this);
	  var selectRow = null;
	  var selectIndex = null;
	  var pageNumber = 1;
	  var pageSize = 10;
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
		columns:[[  {field:'billDetailId',hidden:true},
					{field:'billItemName',title:'项目',width:100,align:"center"},
					{field:'billDate',title:'日期',width:100,align:"center"},
					{field:'billCode',title:'单号',width:100,align:"center"},
					{field:'price',title:'价格',width:100,align:"center"},
					{field:'qty',title:'数量',width:50,align:"center"},
					{field:'returnQty',title:'已退数量',width:100,align:"center"},
					{field:'unitName',title:'单位',width:50,align:"center"},
					{field:'discountAmount',title:'优惠',width:100,align:"center"},
					{field:'payed',title:'合计',width:100,align:"center"},
					{field:'status',title:'状态',width:100,align:"center",
							formatter: function(value,row,index){
										if (value==0){
											return '<span style="color:red">未处理</span>';
										} else if (value==1){
											return '<span style="color:green">已处理</span>';
										}else if (value==2){
											return '已退';
										}else if (value==3){
											return '--';
										}
				    }},
				    {field:'warehouseName',title:'仓库',width:150,align:"center"}
		]],
		onClickRow:function(rowIndex, rowData){
			selectRow = rowData;
			selectIndex = rowIndex;
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
		var billDateBegin = $('#billDateBeginSearch',queryContent).val();
		var billDateEnd = $('#billDateEndSearch',queryContent).val();
		var content = {'bill.student.studentId':studentId,billDateBegin:billDateBegin,billDateEnd:billDateEnd,page:pageNumber,rows:pageSize};
		//取得列表信息
		var url = 'student/queryDetail.do';
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
		var url = "student/getTotalCountDetail.do";
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
  }
})(jQuery);   