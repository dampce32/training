$(function(){
	$('#tabs').tabs("select",0);
	
	//------------控制面板切换-------------------
	$('#tabs').tabs({
	    onSelect:function(title){  
	    	var tab = $('#tabs').tabs('getSelected');
	    	var url = "student/initStudent.do";
	    	var content={type:1};
			var result = syncCallService(url,content);
			if(result.isSuccess){
				var data = result.data;
				var student=eval("("+data.student+")");
			}else{
				$.messager.alert('提示',result.message,'error');
			}
			if(title=='基本信息'){
				$('#editForm',tab).form('clear');
				var panelInfo = {
	                    href:'student/stuInformation.do', border:false, plain:true,
	                    extractor:function (d) {
	                        if (!d) {
	                            return d;
	                        }
	                        if (window['CSIT']) {
	                            var id = CSIT.genId();
	                            return d.replace(/\$\{id\}/, id);
	                        }
	                        return d;
	                    },
	                    onLoad:function (panel) {
	                       (window['CSIT'] && CSIT.initContent && CSIT.initContent(this));
	                       
	                       $('#editForm',tab).form('load',student);
	                       if(student.sex==0){
								$('#sex',tab).val('女');
							}
							else {
								$('#sex',tab).val('男');
							}
							if(student.studentType==0){
								$('#studentType',tab).val('学生');
							}
							else {
								$('#studentType',tab).val('上班族');
							}
	                    }
	                };
	        	
	        	$('#tabs').tabs('update', {
	        		tab: tab,
	        		options: panelInfo
	        	});
	        	$('#editForm',tab).form('load',student);
               if(student.sex==0){
					$('#sex',tab).val('女');
				}
				else {
					$('#sex',tab).val('男');
				}
				if(student.studentType==0){
					$('#studentType',tab).val('学生');
				}
				else {
					$('#studentType',tab).val('上班族');
				}
			}
	    	if(title=='账户信息'){
	        	var panelInfo = {
	                    closable:true,
	                    href:'student/stuAccount.do', border:false, plain:true,
	                    extractor:function (d) {
	                        if (!d) {
	                            return d;
	                        }
	                        if (window['CSIT']) {
	                            var id = CSIT.genId();
	                            return d.replace(/\$\{id\}/, id);
	                        }
	                        return d;
	                    },
	                    onLoad:function (panel) {
	                       (window['CSIT'] && CSIT.initContent && CSIT.initContent(this));
	                       $('#studentId',tab).val(student.studentId);
	                       $('#availableMoney',tab).val(student.availableMoney);
	                       $('#consumedMoney',tab).val(student.consumedMoney);
	                       $('#arrearageMoney',tab).val(student.arrearageMoney);
	                       $('#creditExpiration',tab).val(student.creditExpiration);
	                    }
	                };
	        	
	        	$('#tabs').tabs('update', {
	        		tab: tab,
	        		options: panelInfo
	        	});
	        	$('#studentId',tab).val(student.studentId);
	        	$('#availableMoney',tab).val(student.availableMoney);
                $('#consumedMoney',tab).val(student.consumedMoney);
                $('#arrearageMoney',tab).val(student.arrearageMoney);
                $('#creditExpiration',tab).val(student.creditExpiration);
	        }
	        if(title=='已购产品'){
	        	var panelInfo = {
	                    closable:true,
	                    href:'student/stuProduct.do', border:false, plain:true,
	                    extractor:function (d) {
	                        if (!d) {
	                            return d;
	                        }
	                        if (window['CSIT']) {
	                            var id = CSIT.genId();
	                            return d.replace(/\$\{id\}/, id);
	                        }
	                        return d;
	                    },
	                    onLoad:function (panel) {
	                       (window['CSIT'] && CSIT.initContent && CSIT.initContent(this));
	                       $('#studentId',tab).val(student.studentId);
	                    }
	                };
	        	
	        	$('#tabs').tabs('update', {
	        		tab: tab,
	        		options: panelInfo
	        	});
	        	$('#studentId',tab).val(student.studentId);
	        }
	        if(title=='消费单'){
	        	var panelInfo = {
	                    closable:true,
	                    href:'student/stuExpense.do', border:false, plain:true,
	                    extractor:function (d) {
	                        if (!d) {
	                            return d;
	                        }
	                        if (window['CSIT']) {
	                            var id = CSIT.genId();
	                            return d.replace(/\$\{id\}/, id);
	                        }
	                        return d;
	                    },
	                    onLoad:function (panel) {
	                       (window['CSIT'] && CSIT.initContent && CSIT.initContent(this));
	                       $('#studentId',tab).val(student.studentId);
	                    }
	                };
	        	$('#tabs').tabs('update', {
	        		tab: tab,
	        		options: panelInfo
	        	});
	        	$('#studentId',tab).val(student.studentId);
	        }
	        if(title=='选班信息'){
	        	var panelInfo = {
	                    closable:true,
	                    href:'student/stuClassInfo.do', border:false, plain:true,
	                    extractor:function (d) {
	                        if (!d) {
	                            return d;
	                        }
	                        if (window['CSIT']) {
	                            var id = CSIT.genId();
	                            return d.replace(/\$\{id\}/, id);
	                        }
	                        return d;
	                    },
	                    onLoad:function (panel) {
	                       (window['CSIT'] && CSIT.initContent && CSIT.initContent(this));
	                       $('#studentId',tab).val(student.studentId);
	                    }
	                };
	        	
	        	$('#tabs').tabs('update', {
	        		tab: tab,
	        		options: panelInfo
	        	});
	        	$('#studentId',tab).val(student.studentId);
	        }
	        if(title=='上课信息'){
	        	var panelInfo = {
	                    closable:true,
	                    href:'student/stuLessonInfo.do', border:false, plain:true,
	                    extractor:function (d) {
	                        if (!d) {
	                            return d;
	                        }
	                        if (window['CSIT']) {
	                            var id = CSIT.genId();
	                            return d.replace(/\$\{id\}/, id);
	                        }
	                        return d;
	                    },
	                    onLoad:function (panel) {
	                       (window['CSIT'] && CSIT.initContent && CSIT.initContent(this));
	                       $('#studentId',tab).val(student.studentId);
	                    }
	                };
	        	$('#tabs').tabs('update', {
	        		tab: tab,
	        		options: panelInfo
	        	});
	        	$('#studentId',tab).val(student.studentId);
	        }
	        if(title=='异动信息'){
	        	var panelInfo = {
	                    closable:true,
	                    href:'student/stuChangeInfo.do', border:false, plain:true,
	                    extractor:function (d) {
	                        if (!d) {
	                            return d;
	                        }
	                        if (window['CSIT']) {
	                            var id = CSIT.genId();
	                            return d.replace(/\$\{id\}/, id);
	                        }
	                        return d;
	                    },
	                    onLoad:function (panel) {
	                       (window['CSIT'] && CSIT.initContent && CSIT.initContent(this));
	                       $('#studentId',tab).val(student.studentId);
	                    }
	                };
	        	$('#tabs').tabs('update', {
	        		tab: tab,
	        		options: panelInfo
	        	});
	        	$('#studentId',tab).val(student.studentId);
	        }
	        if(title=='请假信息'){
	        	var panelInfo = {
	                    closable:true,
	                    href:'student/stuLeaveInfo.do', border:false, plain:true,
	                    extractor:function (d) {
	                        if (!d) {
	                            return d;
	                        }
	                        if (window['CSIT']) {
	                            var id = CSIT.genId();
	                            return d.replace(/\$\{id\}/, id);
	                        }
	                        return d;
	                    },
	                    onLoad:function (panel) {
	                       (window['CSIT'] && CSIT.initContent && CSIT.initContent(this));
	                       $('#studentId',tab).val(student.studentId);
	                    }
	                };
	        	$('#tabs').tabs('update', {
	        		tab: tab,
	        		options: panelInfo
	        	});
	        	$('#studentId',tab).val(student.studentId);
	        }
	    }  
	});
 	//退出系统
 	 $('#exitSystem').click(function(){
     	$.messager.confirm('提示','确定要退出系统吗?',function(r){
     		if(r){
     			$.post('login/logout.do',function(result){
     				if(result.isSuccess){
     					window.location.href='login.html';
     				}
     			},'json');
     		}
     	})
     })
   //---修改密码---
     $('#modifyPwdDialog').dialog({
     	title: '修改密码',  
 	    width:500,
 	    height:300,
 	    closed: true,  
 	    cache: false,  
 	    modal: true,
 	    closable:false,
 	    toolbar:[{
 			text:'保存',
 			iconCls:'icon-save',
 			handler:function(){
 				onSaveModifyPwd();
 			}
 		},'-',{
 			text:'退出',
 			iconCls:'icon-cancel',
 			handler:function(){
 				$('#modifyPwdDialog').dialog('close');
 			}
 		}]
     })
      $('#modifyPwdBtn').click(function(){
     	$('#modifyPwdForm').form('clear');
      	$('#modifyPwdDialog').dialog('open');
      })
      
      //保存前检验表单值
 	  var setValue = function(){
 		  var password = $('#password').val();
 		  if('' == password){
 			  $.messager.alert('提示','请输入原密码','warning');
 			  return false;
 		  }
 		  var newStuPwd = $('#newStuPwd').val();
 		  if('' == newStuPwd){
 			  $.messager.alert('提示','请输入新密码','warning');
 			  return false;
 		  }
 		  var newStuPwd2 = $('#newStuPwd2').val();
 		  if(newStuPwd!=newStuPwd2){
 			  $.messager.alert('提示','两次输入的新密码不一样','warning');
 			  return false;
 		  }
 		  return true;
 	  }
 	  
 	  var onSaveModifyPwd = function(){
 		  $('#modifyPwdForm').form('submit',{
 			url: 'student/modifyPwd.do',
 			onSubmit: function(){
 				return setValue();
 			},
 			success: function(data){
 				var result = eval('('+data+')');
 				if(result.isSuccess){
 					$.messager.alert('提示','密码修改成功','info');
 					$('#modifyPwdDialog').dialog('close');
 				}else{
 					$.messager.alert('提示',result.message,"warning");
 				}
 			}
 		  });
 	  }
})
