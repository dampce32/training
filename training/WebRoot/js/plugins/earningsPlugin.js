// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.earningsInit = function() {
	  var $this = $(this);
	  var id = $(this).attr('id');
	  var width = $(document.body).width();
	  var height = $(document.body).height();
	  var queryContent = $('#queryContent',$this);
	  var searchBtn = $('#searchBtn',$this);
	  var viewList = $('#viewList',$this);
	  
	  $('#schoolSearch',queryContent).combobox({
		  width:150,
		  data:TRN.getSelfSchoolList(),
		  valueField:'schoolId',
		  textField:'schoolName'
	  });
	$(viewList).datagrid({
		fit:true,
		method:"POST",
		nowrap:true,
		striped: true,
		collapsible:true,
		rownumbers:true,
		pagination:false,
		singleSelect:true,
		 rowStyler: function(index,row){  
             if (row.count < 0){  
            	 return 'color:red;'
             }  
         } ,
		columns:[[
			{field:'count',title:'总额',width:200,align:"center"
			}
		]]
	  });
	//查询
	var search = function(flag){
		var type = $('#typeSearch',queryContent).combobox('getValue');
		var schoolId = $('#schoolSearch',queryContent).combobox('getValue');
		if(schoolId==''){
			schoolId = -1 ;
		}
		var transactionDateBegin = $('#transactionDateBeginSearch',queryContent).val();
		var transactionDateEnd = $('#transactionDateEndSearch',queryContent).val();
		var content = {type:type,'school.schoolId':schoolId,transactionDateBegin:transactionDateBegin,transactionDateEnd:transactionDateEnd};
		var url = 'finance/sumPayment.do';
		var result = syncCallService(url,content);
		if(result.isSuccess){
			var  data = result.data;
			$(viewList).datagrid('loadData',[{count:data.count}]);
		}else{
			$.messager.alert('提示',result.message,'error');
		}
	}
	//查询
	$(searchBtn).click(function(){
		search(true);
	})
  }
})(jQuery);   