// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.stuChangeInfoInit = function() {
	  var $this = $(this);
	  var id = $(this).attr('id');
	  var height = $(document.body).height();  
	  
	  var queryContent = $('#queryContent',$this);
	  var searchBtn = $('#searchBtn',$this);
	  
	  var viewList = $('#viewList',$this);
	  var selectRow = null;
	  
	  var pager = $('#pager',$this);
	  var pageNumber = 1;
	  var pageSize = 10;
	  var statusArray = [ 
	                     {"value" : 3,"text" : "转班"}, 
	                     {"value" : 4,"text" : "休学"}, 
	                     {"value" : 5,"text" : "退学"},
	                     {"value" : 6,"text" : "弃学"},
	                     {"value" : 7,"text" : "所有"}
	         		                   ];
	  $('#typeSearch',queryContent).combobox({
		  width:150,
		  data:statusArray
	  });
	  //判断死否是数字
	function isNumber(s)  {   
		var strP=/^\d+$/;
		if(''==s||strP.test(s)) { 
			return true;
		}else{
			return false;
		}
	}
	  
	//加载列表
	  $(viewList).datagrid({
		singleSelect:true,
		method:"POST",
		nowrap:true,
		striped: true,
		collapsible:true,
		rownumbers:true,
		fit:true,
		columns:[[
			{field:'studentId',title:'学员编号',width:100,align:"center"},
			{field:'studentName',title:'学员姓名',width:100,align:"center"},
			{field:'changeType',title:'类型',width:100,align:"center",
				formatter: function(value,row,index){
			    	if (value=="3"){
						return '换班';
					} else if (value=="4"){
						return '休学';
					}else if (value=="5"){
						return '退学';
					}else if (value=="6"){
						return '弃学';
					};
		    	}},
			{field:'className',title:'班级名称',width:100,align:"center"},
			{field:'date',title:'办理时间',width:100,align:"center"},
			{field:'newStuClassName',title:'新班号',width:100,align:"center"},
			{field:'note',title:'详情',width:100,align:"center"},
			{field:'userName',title:'经办人',width:100,align:"center"}
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
	$(searchBtn).click(function(){
		search(true);
	});
	//查询
	var search = function(flag){
		var type = $('#typeSearch',queryContent).combobox('getValue');
		var typeBoolean=isNumber(type);
		if(typeBoolean==false){
			$.messager.alert('提示','请选择已有的类型','error');
			return;
		}
		var studentId=$('#studentId',$this).val();
		var content = {'student.studentId':studentId,'changeType':type,page:pageNumber,rows:pageSize};
		//取得列表信息
		var url = 'student/queryChange.do';
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
		var url = "student/getTotalCountChange.do";
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