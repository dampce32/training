// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.modifyPwdInit = function() {
	  var $this = $(this);
	  var id = $(this).attr('id');
	  var editForm = $('#editForm',$this);
	  $(editForm).form('clear');
	  //保存前检验表单值
	  var setValue = function(){
		  var userPwd = $('#userPwd',$this).val();
		  if('' == userPwd){
			  $.messager.alert('提示','请输入原密码','warning');
			  return false;
		  }
		  var newUserPwd = $('#newUserPwd',$this).val();
		  if('' == newUserPwd){
			  $.messager.alert('提示','请输入新密码','warning');
			  return false;
		  }
		  var newUserPwd2 = $('#newUserPwd2',$this).val();
		  if(newUserPwd!=newUserPwd2){
			  $.messager.alert('提示','两次输入的新密码不一样','warning');
			  return false;
		  }
		  return true;
	  }
	  
	  $('#saveBtn',$this).click(function(){
		  $(editForm).form('submit',{
			url: 'system/modifyPwdUser.do',
			onSubmit: function(){
				return setValue();
			},
			success: function(data){
				var result = eval('('+data+')');
				if(result.isSuccess){
					$.messager.alert('提示','密码修改成功','info');
				}else{
					$.messager.alert('提示',result.message,"warning");
				}
			}
		  });
	  })
	  
  }
})(jQuery);   