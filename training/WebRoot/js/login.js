$(function() {
	var userinfo = $.cookie("loginInfoTraining");
	if(userinfo!=null&&""!=userinfo){
		var array = userinfo.split("§");
		$("#userCode").val(array[0]);
		$("#userPwd").val(array[1]);
		$("#mindpwd").attr('checked','true');
		$(":radio[id=role][value="+array[2]+"]").attr('checked','true');
	}
	var loginCaptchaImageRefresh = function(){
		$("#loginCaptcha").val('');
		$("#loginCaptchaImage").attr("src", "captcha.jpg?timestamp" + (new Date()).valueOf());
	}
	// 点击刷新验证码图片
	$("#loginCaptchaImage").click( function() {
		loginCaptchaImageRefresh();
	});
	$(document).keydown(function(e){ 
		var curKey = e.which; 
		if(curKey == 13){ 
			$('#loginBtn').click();
		} 
	});
	$("#loginBtn").click(function(){
		var role = $(':radio[id=role][checked=checked]').val();
		var url = null;
		if(role=='user'){
			url = 'login/userLogin.do';			
		}else if(role=='student'){
			url = 'login/studentLogin.do';
		}
		$('#loginForm').form('submit', {
			url : url,
			onSubmit : function() {
				//记住密码
				 if($("#mindpwd").attr("checked")){
					 var role = $(':radio[id=role][checked=checked]').val();
					 var loginInfo = $("#userCode").val()+"§"+$("#userPwd").val()+"§"+role;
					 $.cookie("loginInfoTraining",loginInfo,{expires: 30,path:'/'});
				 }else{
					 $.cookie("loginInfoTraining",null,{path:'/'});
				 }
				return $(this).form('validate');
			},
			success : function(data) {
				var result = eval('(' + data + ')');
				if(result.isSuccess){
					var location = "main.do";
					if(role=='student'){
						location = 'mainStu.do';
					}
					window.location=location;
				}else{
					$.messager.alert('提示：',result.message,'error');
					loginCaptchaImageRefresh();
				}
				
			}
		});
	})
	
})
