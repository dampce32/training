$(function(){
	//当前选中的节点
    var currentNode = null;
	tabCloseEven();
 	$('#navi a').click(function(){
		var href =$(this).attr('name');
		var title = $(this).text();
		if(href==null){
			return false;
		}
		
		addTab(title,href);
		return false;
	});
 	
 	$('#rightTree').tree({
		url: 'system/getUrlRightAllUser.do',
		onSelect:function (node) {
			$(this).tree('toggle',node.target);
            if (currentNode != null && node.id == currentNode.id) return;
            if (node.attributes == null) return;
            var url = node.attributes.rightUrl;
            var title = node.text;
            
            CSIT.currTabRightId = node.id;
            addTab(title, url);
        }
	});
 	//退出系统
 	 $('#exitSystem').click(function(){
     	$.messager.confirm('提示','确定要退出系统吗?',function(r){
     		if(r){
     			$.post('system/logoutUser.do',function(result){
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
 		  var userPwd = $('#userPwd').val();
 		  if('' == userPwd){
 			  $.messager.alert('提示','请输入原密码','warning');
 			  return false;
 		  }
 		  var newUserPwd = $('#newUserPwd').val();
 		  if('' == newUserPwd){
 			  $.messager.alert('提示','请输入新密码','warning');
 			  return false;
 		  }
 		  var newUserPwd2 = $('#newUserPwd2').val();
 		  if(newUserPwd!=newUserPwd2){
 			  $.messager.alert('提示','两次输入的新密码不一样','warning');
 			  return false;
 		  }
 		  return true;
 	  }
 	  
 	  var onSaveModifyPwd = function(){
 		  $('#modifyPwdForm').form('submit',{
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
 	  }
 	  //----系统提醒--------
 	  $('#searchReminder').click(function(){
 		 onSearchReminder();
 	  })
 	  //查询系统提醒
 	  var onSearchReminder = function(){
 		 var url = "system/getCurrentUserReminder.do";
	 	 asyncCallService(url,
	 		function(result){
	 			if(result.isSuccess){
	 				var data = result.data;
	 				$('.addReminder').remove();
	 				$(data.appendHtml).insertAfter("#reminderTable");
	 				
	 				$('#reminder a').click(function(){
	 					var href =$(this).attr('navigateUrl');
	 					var title =$(this).attr('navigateTabName');
	 					var rightId =$(this).attr('rightId');
	 					CSIT.currTabRightId = rightId;
	 					if(href==null){
	 						return false;
	 					}
	 					addTab(title,href);
	 				});
	 			}else{
	 				$.messager.alert('提示',result.message,"error");
	 			}
	 	 	}
	 	 )
 	  }
 	 onSearchReminder();
})

function addTab(title,href){
	if($('#tabs').tabs('exists',title)){//选择并更新tab
		$('#tabs').tabs('select',title);
	}else{
		var index = href.indexOf('.');
		var postfix = href.substring(index+1);
		if(href.indexOf('.do')!=-1){
			var panelInfo = {
	                title:title,
	                closable:true,
	                href:href, border:false, plain:true,
	                extractor:function (d) {
	                    if (!d) {
	                        return d;
	                    }
	                    var currTabRightId = CSIT.currTabRightId;
	                    d = d.replace(/\$\{rightId\}/g, currTabRightId);
	                    if (window['CSIT']) {
	                        var id = CSIT.genId();
	                        return d.replace(/\$\{id\}/g, id);
	                    }
	                    return d;
	                },
	                onLoad:function (panel) {
	                    var tab = $('.plugins', this);
	                    if ($(tab).size() == 0) {
	                        return;
	                    }
	                   (window['CSIT'] && CSIT.initContent && CSIT.initContent(this));
	                }
	            };
			 $('#tabs').tabs('add', panelInfo);
		}else{
        	var content = createFrame(href);
    		$('#tabs').tabs('add',{
    			title:title,
    			content:content,
    			closable:true
    		});
        }
			
           
	}
	tabClose();
}

function createFrame(url) {
	var s = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';
	return s;
}

function tabClose() {
	/*双击关闭TAB选项卡*/
	$(".tabs-inner").dblclick(function(){
		var subtitle = $(this).children(".tabs-closable").text();
		$('#tabs').tabs('close',subtitle);
	})
	/*为选项卡绑定右键*/
	$(".tabs-inner").bind('contextmenu',function(e){
		$('#mm').menu('show', {
			left: e.pageX,
			top: e.pageY
		});

		var subtitle =$(this).children(".tabs-closable").text();

		$('#mm').data("currtab",subtitle);
		$('#tabs').tabs('select',subtitle);
		return false;
	});
}	


//绑定右键菜单事件
function tabCloseEven() {
	//刷新
	$('#mm-tabupdate').click(function(){
		var currTab = $('#tabs').tabs('getSelected');
		var url = $(currTab.panel('options').content).attr('src');
		if(url != undefined && currTab.panel('options').title != '首页') {
			$('#tabs').tabs('update',{
				tab:currTab,
				options:{
					content:createFrame(url)
				}
			})
		}
	})
	//关闭当前
	$('#mm-tabclose').click(function(){
		var currtab_title = $('#mm').data("currtab");
		$('#tabs').tabs('close',currtab_title);
	})
	//全部关闭
	$('#mm-tabcloseall').click(function(){
		$('.tabs-inner span').each(function(i,n){
			var t = $(n).text();
			if(t != '首页') {
				$('#tabs').tabs('close',t);
			}
		});
	});
	//关闭除当前之外的TAB
	$('#mm-tabcloseother').click(function(){
		var prevall = $('.tabs-selected').prevAll();
		var nextall = $('.tabs-selected').nextAll();		
		if(prevall.length>0){
			prevall.each(function(i,n){
				var t=$('a:eq(0) span',$(n)).text();
				if(t != '首页') {
					$('#tabs').tabs('close',t);
				}
			});
		}
		if(nextall.length>0) {
			nextall.each(function(i,n){
				var t=$('a:eq(0) span',$(n)).text();
				if(t != '首页') {
					$('#tabs').tabs('close',t);
				}
			});
		}
		return false;
	});
	//关闭当前右侧的TAB
	$('#mm-tabcloseright').click(function(){
		var nextall = $('.tabs-selected').nextAll();
		if(nextall.length==0){
			//msgShow('系统提示','后边没有啦~~','error');
			alert('后边没有啦~~');
			return false;
		}
		nextall.each(function(i,n){
			var t=$('a:eq(0) span',$(n)).text();
			$('#tabs').tabs('close',t);
		});
		return false;
	});
	//关闭当前左侧的TAB
	$('#mm-tabcloseleft').click(function(){
		var prevall = $('.tabs-selected').prevAll();
		if(prevall.length==1){
			alert('到头了，前边没有啦~~');
			return false;
		}
		prevall.each(function(i,n){
			var t=$('a:eq(0) span',$(n)).text();
			$('#tabs').tabs('close',t);
		});
		return false;
	});

	//退出
	$("#mm-exit").click(function(){
		$('#mm').menu('hide');
	})
}