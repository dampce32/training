// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.stuLeaveInfoInit = function() {
	  var $this = $(this);
	  var id = $(this).attr('id');
	  var height = $(document.body).height();  
	  
	  var viewList = $('#viewList',$this);
	  var selectRow = null;
	  
	  var pager = $('#pager',$this);
	  var pageNumber = 1;
	  var pageSize = 10;
	  
	  
	//加载列表
	  $(viewList).datagrid({
		singleSelect:true,
		method:"POST",
		nowrap:true,
		striped: true,
		collapsible:true,
		rownumbers:true,
		fit:true,
		toolbar:[	
					{text:'查看',iconCls:'icon-search',handler:function(){search(true);}}
				],
		columns:[[
			{field:'studentName',title:'学员',width:100,align:"center"},
			{field:'date',title:'办理时间',width:100,align:"center"},
			{field:'beginDate',title:'开始时间',width:100,align:"center"},
			{field:'endDate',title:'结束时间',width:100,align:"center"},
			{field:'note',title:'理由',width:100,align:"center"},
			{field:'userName',title:'录入员',width:100,align:"center"}
			
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
	$(pager).pagination({   
	    onSelectPage: function(page, rows){
			pageNumber = page;
			pageSize = rows;
			search();
	    }
	});
	//查询
	var search = function(flag){
		var content = {page:pageNumber,rows:pageSize};
		//取得列表信息
		var url = 'student/queryByStuLeave.do';
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
	};
	
	
	//统计总数
	var getTotal = function(content){
		var url = "student/getTotalCountByStuLeave.do";
		asyncCallService(url,content,
		function(result){
			if(result.isSuccess){
				var data = result.data;
				$(pager).pagination({  
					pageNumber:1,
					total:data.total
				});
			}else{
				$.messager.alert('提示',result.message,"error");
			}
		});
	};	
  };
})(jQuery);   