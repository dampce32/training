// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.selfInit = function() {
	  var $this = $(this);
	  var id = $(this).attr('id');
	  var editForm = $('#editForm',$this);
	  
	  var url = 'system/getSelfInforUser.do';
	  
	  asyncCallService(url,
		function(result){
			if(result.isSuccess){
				var data = result.data;
				  $('#userId',$this).val(data.userId);
				  $('#userCode',$this).val(data.userCode);
				  $('#userName',$this).val(data.userName);
			}else{
				$.messager.alert('提示',result.message,"error");
			}
	  }) 
	  
	 $('#saveBtn',$this).click(function(){
		 $(editForm).form('submit',{
			url: 'system/updateSelfInfoUser.do',
			success: function(data){
				var result = eval('('+data+')');
				if(result.isSuccess){
					 $.messager.alert('提示','已成功修改个人信息')
				}else{
					$.messager.alert('提示',result.message,'error');
				}
			}
		 });
	 })
  }
})(jQuery);   