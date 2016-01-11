// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.stuLessonInfoInit = function() {
	  var $this = $(this);
	  var id = $(this).attr('id');
	  var width = $(document.body).width();
	  var height = $(document.body).height();
	  var studentId=$('#studentId',$this).val();
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
		toolbar:[	
			{text:'查看',iconCls:'icon-search',handler:function(){search(true);}}
			
		],
		columns:[[  
					{field:'状态',title:'状态',width:100,align:"center",formatter: function(value,row,index){
							if (value==0){
								return '未开课';
							} else if (value==1){
								return '<span style="color:green">已处理</span>';
							}
					  }},
					{field:'课题',title:'课题',width:100,align:"center"},
					{field:'上课日期',title:'上课日期',width:100,align:"center"},
					{title:'上课时间段',width:100,align:"center",formatter: function(value,row,index){
						return  row.上课时间+"-"+row.下课时间;
					  }},
					{field:'班号',title:'班号',width:100,align:"center"},
					{field:'班级名称',title:'班级名称',width:100,align:"center"},
					{field:'教室',title:'教室',width:100,align:"center"},
					{field:'讲师',title:'讲师',width:100,align:"center"},
					{field:'课时',title:'课时',width:100,align:"center"},
					{field:'家庭作业',title:'家庭作业',width:100,align:"center"},
					{field:'课堂表现',title:'课堂表现',width:100,align:"center"}
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
		var content = {page:pageNumber,rows:pageSize};
		//取得列表信息
		var url = 'student/queryByStuLessionDegree.do';
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
		var url = "student/getTotalCountByStuLessionDegree.do";
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