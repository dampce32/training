// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.stuListInit = function() {
	  var $this = $(this);
	  var id = $(this).attr('id');
	  var width = $(document.body).width();
	  var height = $(document.body).height();
	  var queryContent = $('#queryContent',$this);
	  var queryReplyContent = $('#queryReplyContent',$this);
	  var searchBtn = $('#searchBtn',$this);
	  var searchReplyBtn = $('#searchReplyBtn',$this);
	  var changeSearch = false;
	  var isEditor = true;
	  var getF = true;
	  var replyList = $('#replyList',replyDialog);
	  var viewList = $('#viewList',$this);
	  var selectRow = null;
	  var selectIndex = null;
	  var selectRowReply = null;
	  var selectIndexReply = null;
	  var pager = $('#pager',$this);
	  var pagerReply = $('#pagerReply',$this);
	  var pageNumber = 1;
	  var pageSize = 10;
	  var editDialog = $('#editDialog',$this);
	  var editReplyDialog = $('#editReplyDialog',$this);
	  var controlPanelDialog = $('#controlPanelDialog',$this);
	  var editForm = $('#editForm',$this);
	  var editReplyDialog = $('#editReplyDialog',$this);
	  var editReplyForm = $('#editReplyForm',$this);
	  $('#schoolSearch',queryContent).combobox({
		  width:150,
		  data:TRN.getSelfSchoolList(),
		  valueField:'schoolId',
		  textField:'schoolName'
	  });
	  //保留小数点后2位
	  var toDecimal =function(x) {  
            var f = parseFloat(x);  
            if (isNaN(f)) {  
                return false;  
            }  
            var f = Math.round(x*100)/100;  
            var s = f.toString();  
            var rs = s.indexOf('.');  
            if (rs < 0) {  
                rs = s.length;  
                s += '.';  
            }  
            while (s.length <= rs + 2) {  
                s += '0';  
            }  
            return s;  
        }
//=====================================我的学员信息===========================================
	//加载列表
	  $(viewList).datagrid({
		method:"POST",
		singleSelect:true,
		nowrap:true,
		striped: true,
		collapsible:true,
		rownumbers:true,
		fit:true,
		toolbar:[	
					{text:'添加',iconCls:'icon-add',handler:function(){onAdd()}},'-',
					{text:'修改',iconCls:'icon-edit',handler:function(){onUpdate()}},'-',
					{text:'关怀',iconCls:'icon-undo',handler:function(){onReply()}},'-',
					{text:'控制面板',iconCls:'icon-view',handler:function(){onControlPanel()}}
				],
		columns:[[  {field:'studentId',title:'学号',width:100,align:"center"},
					{field:'studentName',title:'姓名',width:100,align:"center"},
					{field:'enrollDate',title:'报名日期',width:100,align:"center"},
					{field:'schoolName',title:'校区',width:100,align:"center"},
					{field:'availableMoney',title:'可用金额',width:100,align:"center"},
					{field:'billCount',title:'消费单数量',width:100,align:"center"},
					{field:'arrearageMoney',title:'欠费金额',width:100,align:"center"},
					{field:'userName',title:'学习顾问',width:100,align:"center"},
					{field:'creditExpiration',title:'借款到期时间',width:100,align:"center"}
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
	//编辑框
	$(editDialog).dialog({  
	    title: '编辑正式学员',  
	    width:780,
	    height:500,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    toolbar:[
	            {id:'save'+id,text:'保存',iconCls:'icon-save',handler:function(){onSave();}},'-',
				{id:'add'+id,text:'新增',iconCls:'icon-add',handler:function(){onAdd();}},'-',
				{id:'pre'+id,text:'上一笔',iconCls:'icon-left',handler:function(){onOpenIndex(-1);}},'-',
				{id:'next'+id,text:'下一笔',iconCls:'icon-right',handler:function(){onOpenIndex(1);}},'-',
				{text:'退出',iconCls:'icon-cancel',handler:function(){
						$(editDialog).dialog('close');
					}
				}
	   ]
	});
	//新增时，按钮的状态
	var addBtnStatus = function(){
		$('#pre'+id).linkbutton('disable');
		$('#next'+id).linkbutton('disable');
	}
	//初始化选择条件
	var initCombobox = function(){
		//所在校区
		$('#school',editDialog).combobox({
			valueField:'schoolId',
			textField:'schoolName',
			width:250,
			data:TRN.getSelfSchoolList()
		});
		//咨询课程
		$('#potCourse',editDialog).combobox({
		  width:250,
		  data:TRN.getPotCourseList(),
		  valueField:'potCourseId',
		  textField:'courseName'
	  });
		//获知方式
		$('#media',editDialog).combobox({
		  width:250,
		  data:TRN.getMediaList(),
		  valueField:'mediaId',
		  textField:'mediaName'
	  });
		//状态
		$('#potentialStuStatus',editDialog).combobox({
		  width:250,
		  data:TRN.getPotentialStuStatusList(),
		  valueField:'potentialStuStatusId',
		  textField:'potentialStuStatusName'
	  });
		//咨询顾问
		$('#user',editDialog).combobox({
		  width:250,
		  data:TRN.getEmployeeList(),
		  valueField:'userId',
		  textField:'userName'
	  });
	}
	//添加
	var onAdd = function(){
		$(viewList).datagrid('unselectAll');
		selectIndex = null;
		selectRow = null;
		$(editForm).form('clear');
		addBtnStatus();
		initCombobox();
		$('#sex',editDialog).combobox('setValue',1);
		$('#studentType',editDialog).combobox('setValue',0);
		$(editDialog).dialog('open');
	}
	//保存前的赋值操作
	var setValue = function(){
		//姓名
		var studentName = $.trim($('#studentName',editForm).val());
		if(''==studentName){
			$.messager.alert('提示','请填写姓名','warning');
			return false;
		}
		//性别
		var sex = $('#sex',editDialog).combobox('getValue') ;
		if(''==sex){
			$.messager.alert('提示','请选择性别','warning');
			return false;
		}
		if(isNaN(parseInt(sex))){
			$.messager.alert('提示','请选择提供选择的性别','warning');
			return false;
		}
		//类型
		var studentType = $('#studentType',editDialog).combobox('getValue') ;
		if(''==studentType){
			$.messager.alert('提示','请选择类型','warning');
			return false;
		}
		if(isNaN(parseInt(studentType))){
			$.messager.alert('提示','请选择提供选择的类型','warning');
			return false;
		}
		//报名日期
		var enrollDate = $.trim($('#enrollDate',editForm).val());
		if(''==enrollDate){
			$.messager.alert('提示','请填写报名日期','warning');
			return false;
		}
		//校区
		var schoolId = $('#school',editDialog).combobox('getValue') ;
		if(''==schoolId){
			$.messager.alert('提示','请选择校区','warning');
			return false;
		}
		if(isNaN(parseInt(schoolId))){
			$.messager.alert('提示','请选择提供选择的校区','warning');
			return false;
		}
		//电话号码
		var tel = $.trim($('#tel',editForm).val());
		if(''==tel){
			$.messager.alert('提示','请填写电话号码','warning');
			return false;
		}
		//手机号码
		var mobileTel = $.trim($('#mobileTel',editForm).val());
		if(''==mobileTel){
			$.messager.alert('提示','请填写手机号码','warning');
			return false;
		}
		//获知方式
		var mediaId = $('#media',editDialog).combobox('getValue') ;
		if(''==mediaId){
			$.messager.alert('提示','请选择获知方式','warning');
			return false;
		}
		if(isNaN(parseInt(mediaId))){
			$.messager.alert('提示','请选择提供选择的获知方式','warning');
			return false;
		}
		//学习顾问
		var userId = $('#user',editDialog).combobox('getValue') ;
		if(''==userId){
			$.messager.alert('提示','请选择学习顾问','warning');
			return false;
		}
		if(isNaN(parseInt(userId))){
			$.messager.alert('提示','请选择提供选择的学习顾问','warning');
			return false;
		}
		return true;
	}
	//保存
	var onSave = function(){
		$(editForm).form('submit',{
			url: 'customerService/saveStudent.do',
			onSubmit: function(){
				return setValue();
			},
			success: function(data){
				var result = eval('('+data+')');
				if(result.isSuccess){
					var fn = function(){
						var studentId = $.trim($('#studentId',editForm).val());
						if(studentId==''){//新增
							var  data = result.data;
							$('#studentId',editForm).val(data.studentId);
							$('#delete'+id).linkbutton('enable');
						}
					}
					$.messager.alert('提示','保存成功','info',fn);
				}else{
					$.messager.alert('提示',result.message,"error");
				}
			}
		});
	}
	//修改
	var onUpdate = function(){
		if(selectRow==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		}
		onOpen();
		$(editDialog).dialog('open');
	 }
	//打开
	var onOpen = function(){
		$(editForm).form('clear');
		var preDisable = false;
		var nextDisable = false;
		if(selectIndex==null||selectIndex==0){
			preDisable = true;
		}
		var rows = $(viewList).datagrid('getRows');
		if(selectIndex==null||selectIndex==rows.length-1){
			nextDisable = true;
		}
		if(preDisable){
			$('#pre'+id).linkbutton('disable');
		}else{
			$('#pre'+id).linkbutton('enable');
		}
		if(nextDisable){
			$('#next'+id).linkbutton('disable');
		}else{
			$('#next'+id).linkbutton('enable');
		}
		$(editForm).form('load',selectRow);
		initCombobox();
		$('#school',editDialog).combobox('setValue',selectRow.schoolId);
		$('#media',editDialog).combobox('setValue',selectRow.mediaId);
		$('#user',editDialog).combobox('setValue',selectRow.userId);
		$('#sex',editDialog).combobox('setValue',selectRow.sex);
		$('#studentType',editDialog).combobox('setValue',selectRow.studentType);
	}
	//上下一笔
	var onOpenIndex = function(index){
		var rows = $(viewList).datagrid('getRows');
		selectIndex = selectIndex + index;
		selectRow = rows[selectIndex];
		onOpen(selectRow.userId);
		//界面选中
		$(viewList).datagrid('unselectAll');
		$(viewList).datagrid('selectRow',selectIndex);
	}
	//查询
	var search = function(flag){
		var studentName = $('#nameSearch',queryContent).val();
		var schoolId = $('#schoolSearch',queryContent).combobox('getValue');
		if(schoolId==''){
			schoolId = -1 ;
		}
		var content = {'school.schoolId':schoolId,studentName:studentName,type:1,page:pageNumber,rows:pageSize};
		//取得列表信息
		var url = 'customerService/queryStudent.do';
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
		var url = "customerService/getTotalCountStudent.do";
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
		})
	}	
	//查询
	$(searchBtn).click(function(){
		search(true);
	})
//===========================================关怀====================================
	//关怀
	var onReply = function() {
		if (selectRow == null) {
			$.messager.alert("提示", "请选择数据行", "warning");
			return;
		}
		$(replyList).datagrid('loadData', { total: 0, rows: [] });
		$(replyDialog).dialog('open');
	}
	$(replyList).datagrid( {
			method:"POST",
			singleSelect:true,
			nowrap:true,
			striped: true,
			collapsible:true,
			rownumbers:true,
			fit:true,
			toolbar:[	
						{text:'添加',iconCls:'icon-add',handler:function(){onAddReply()}},'-',
						{text:'修改',iconCls:'icon-edit',handler:function(){onUpdateReply()}},'-',
						{text:'删除',iconCls:'icon-remove',handler:function(){onDeleteReply()}},'-',
						{text:'退出',iconCls:'icon-cancel',handler:function(){$(replyDialog).dialog('close')}}
					],
			columns : [ [ {field:'ck',checkbox:true},
						  {field : 'userName',title : '关怀人',width : 100,align : "center"}, 
						  {field : 'replyDate',title : '关怀日期',width : 100,align : 'center'}, 
						  {field : 'content',title : '关怀内容',width : 300,align : 'center'}, 
						  {field : 'nextReplyDate',title : '下次关怀日期',width : 100,align : "center"}
					 ] ],
			onClickRow:function(rowIndex, rowData){
			selectRowReply = rowData;
			selectIndexReply = rowIndex;
			},
			onDblClickRow:function(rowIndex,rowData){
				onUpdateReply();
			},
			onLoadSuccess:function(){
				selectRowReply = null;
		 		selectIndexReply = null;
				pageNumber = 1;
			}
		});
	//分页条
	$(pagerReply).pagination({   
	    onSelectPage: function(page, rows){
			pageNumber = page;
			pageSize = rows;
			searchReply();
	    }
	});
	//关怀记录
	$(replyDialog).dialog({  
	    title: '关怀记录',  
	    width:900,
	    height:500,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false
	});
	//编辑框
	$(editReplyDialog).dialog({  
	    title: '编辑关怀表',  
	    width:450,
	    height:300,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    toolbar:[
	            {id:'save'+id,text:'保存',iconCls:'icon-save',handler:function(){onSaveReply();}},'-',
				{text:'退出',iconCls:'icon-cancel',handler:function(){
						$(editReplyDialog).dialog('close');
					}
				}
	   ]
	});
	//添加关怀信息
	var onAddReply = function(){
		$(editReplyForm).form('clear');
		$(editReplyDialog).dialog('open');
	}
	//保存前的赋值操作
	var setValueReply = function(){
		//关怀日期
		var replyDate = $('#replyDate',editReplyForm).val();
		if(''==replyDate){
			$.messager.alert('提示','请填写日期','warning');
			return false;
		}
		//内容
		var content = $('#content',editReplyForm).val();
		if(''==content){
			$.messager.alert('提示','请填写内容','warning');
			return false;
		}
		$('#studentId',editReplyForm).val(selectRow.studentId);
		return true;
	}
	var onSaveReply = function(){
		$(editReplyForm).form('submit',{
			url: 'customerService/saveReplyStu.do',
			onSubmit: function(){
				return setValueReply();
			},
			success:function(data){
				var result = eval('('+data+')');
				if(result.isSuccess){
					var fn = function(){
						searchReply(true);
						$(editReplyDialog).dialog('close');
					}
					$.messager.alert('提示','保存成功','info',fn);
				}else{
					$.messager.alert('提示',result.message,"warning");
				}
			}
		});
	}
	//修改关怀信息
	var onUpdateReply = function(){
		if(selectRowReply==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		}
		$(editReplyForm).form('clear');
		$(editReplyForm).form('load',selectRowReply);
		$(editReplyDialog).dialog('open');
	}
	//删除关怀信息
	var onDeleteReply = function(){
		if(selectRowReply==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		}
		$.messager.confirm("提示","确定要删除记录?",function(t){ 
			if(t){
				var url = 'customerService/deleteReplyStu.do';
				var content ={replyId:selectRowReply.replyId};
				asyncCallService(url,content,function(result){
					if(result.isSuccess){
						var fn = function(){
							selectRowReply = null;
							selectIndexReply = null;
							searchReply(true);
						}
						$.messager.alert('提示','删除成功','info',fn);
					}else{
						$.messager.alert('提示',result.message,'error');
					}
				});
			}
		});
	}
	
	//查询关怀记录
	var searchReply = function(flag){
		var replyDate = $('#replyDateSearch',queryReplyContent).val();
		var nextReplyDate = $('#nextReplyDateSearch',queryReplyContent).val();
		var content = {replyDate:replyDate,nextReplyDate:nextReplyDate,'student.studentId':selectRow.studentId,page:pageNumber,rows:pageSize};
		//取得列表信息
		var url = 'customerService/queryReplyStu.do';
		var result = syncCallService(url,content);
		if(result.isSuccess){
			var  data = result.data;
			var datagridData = eval("("+data.datagridData+")");
			$(replyList).datagrid('loadData',datagridData);
			//需要重新重新分页信息
			if(flag){
				getTotalReply(content);
			}
		}else{
			$.messager.alert('提示',result.message,'error');
		}
	}
	//统计总数关怀记录
	var getTotalReply = function(content){
		var url = "customerService/getTotalCountReplyStu.do";
		asyncCallService(url,content,
		function(result){
			if(result.isSuccess){
				var data = result.data;
				$(pagerReply).pagination({  
					pageNumber:1,
					total:data.total
				});
			}else{
				$.messager.alert('提示',result.message,"error");
			}
		})
	}	
	//查询
	$(searchReplyBtn).click(function(){
		searchReply(true);
	})
	//=============================打开控制面板使用============================================
	var onControlPanel = function(){
		if(selectRow==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		}
		$('#tabs',controlPanelDialog).tabs("select",0);
		$(controlPanelDialog).dialog({  
	    title: '学员'+selectRow.studentId+'控制面板'});
		$(controlPanelDialog).dialog('open');
		
	}
	$(controlPanelDialog).dialog({  
	    width:width-300,
	    height:height-20,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    toolbar:[
				{text:'退出',iconCls:'icon-cancel',handler:function(){
						$(controlPanelDialog).dialog('close');
					}
				}
	   ]
	});
	//------------控制面板切换-------------------
	$('#tabs',controlPanelDialog).tabs({
		
	    onSelect:function(title){  
	    	var tab = $('#tabs',controlPanelDialog).tabs('getSelected');
	    	var studentId=selectRow.studentId;
	    	var studentName=selectRow.studentName;
	    	var url = "customerService/initStudent.do";
			var content={studentId:studentId,type:0};
			var result = syncCallService(url,content);
			if(result.isSuccess){
				var data = result.data;
				var student=eval("("+data.student+")");
				var availableMoney =student.availableMoney;
				var consumedMoney=student.consumedMoney;
				var arrearageMoney=student.arrearageMoney;
   	    	    var creditExpiration=student.creditExpiration;
   	    	    var mediaName=student.mediaName;
   	    	    var schoolName=student.schoolName;
   	    	    var userName=student.userName;
   	    	    var studentName=student.studentName;
   	    	    var appellation=student.appellation;
   	    	    var sex=student.sex;
   	    	    var studentType=student.studentType;
   	    	    var birthday=student.birthday;
   	    	    var enrollDate=student.enrollDate;
   	    	    var tel=student.tel;
   	    	    var mobileTel=student.mobileTel;
   	    	    var tel1=student.tel1;
   	    	    var qq=student.qq;
   	    	    var email=student.email;
   	    	    var address=student.address;
   	    	    var postCode=student.postCode;
   	    	    var idcard=student.idcard;
   	    	    var diploma=student.diploma;
   	    	    var nextReplyDate=student.nextReplyDate;
   	    	    var note=student.note;
			}else{
				$.messager.alert('提示',result.message,'error');
			}
			if(title=='基本信息'){
				var panelInfo = {
	                    closable:true,
	                    href:'customerService/myInformation.do', border:false, plain:true,
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
	                       $('#studentId',tab).val(studentId);
	                       $('#mediaName',tab).val(mediaName);
	                       $('#schoolName',tab).val(schoolName);
	                       $('#userName',tab).val(userName);
	                       $('#appellation',tab).val(appellation);
	                       $('#studentName',tab).val(studentName);
	                       $('#birthday',tab).val(birthday);
	                       $('#enrollDate',tab).val(enrollDate);
	                       $('#mobileTel',tab).val(mobileTel);
	                       $('#tel',tab).val(tel);
	                       $('#tel1',tab).val(tel1);
	                       $('#qq',tab).val(qq);
	                       $('#email',tab).val(email);
	                       $('#address',tab).val(address);
	                       $('#postCode',tab).val(postCode);
	                       $('#idcard',tab).val(idcard);
	                       $('#nextReplyDate',tab).val(nextReplyDate);
	                       $('#diploma',tab).val(diploma);
	                       $('#note',tab).val(note);
	                       if(sex==0){
								$('#sex',tab).val('女');
							}
							else {
								$('#sex',tab).val('男');
							}
							if(studentType==0){
								$('#studentType',tab).val('学生');
							}
							else {
								$('#studentType',tab).val('上班族');
							}
	                    }
	                };
	        	
	        	$('#tabs',controlPanelDialog).tabs('update', {
	        		tab: tab,
	        		options: panelInfo
	        	});
	        	$('#studentId',tab).val(studentId);
               $('#mediaName',tab).val(mediaName);
               $('#schoolName',tab).val(schoolName);
               $('#userName',tab).val(userName);
               $('#appellation',tab).val(appellation);
               $('#studentName',tab).val(studentName);
               $('#birthday',tab).val(birthday);
               $('#enrollDate',tab).val(enrollDate);
               $('#mobileTel',tab).val(mobileTel);
               $('#tel',tab).val(tel);
               $('#tel1',tab).val(tel1);
               $('#qq',tab).val(qq);
               $('#email',tab).val(email);
               $('#address',tab).val(address);
               $('#postCode',tab).val(postCode);
               $('#idcard',tab).val(idcard);
               $('#nextReplyDate',tab).val(nextReplyDate);
               $('#diploma',tab).val(diploma);
               $('#note',tab).val(note);
               if(sex==0){
					$('#sex',tab).val('女');
				}
				else {
					$('#sex',tab).val('男');
				}
				if(studentType==0){
					$('#studentType',tab).val('学生');
				}
				else {
					$('#studentType',tab).val('上班族');
				}
			}
	    	if(title=='账户信息'){
	        	$('#paymentList',tab).datagrid('loadData',{ total: 0, rows: [] });
	        	var panelInfo = {
	                    closable:true,
	                    href:'customerService/myAccount.do', border:false, plain:true,
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
	                       $('#studentId',tab).val(studentId);
	                       $('#availableMoney',tab).val(availableMoney);
	                       $('#consumedMoney',tab).val(consumedMoney);
	                       $('#arrearageMoney',tab).val(arrearageMoney);
	                       $('#creditExpiration',tab).val(creditExpiration);
	                    }
	                };
	        	
	        	$('#tabs',controlPanelDialog).tabs('update', {
	        		tab: tab,
	        		options: panelInfo
	        	});
	        	$('#studentId',tab).val(studentId);
	        	$('#availableMoney',tab).val(availableMoney);
                $('#consumedMoney',tab).val(consumedMoney);
                $('#arrearageMoney',tab).val(arrearageMoney);
                $('#creditExpiration',tab).val(creditExpiration);
	        }
	        if(title=='已购产品'){
	        	$('#viewList',tab).datagrid('loadData',{ total: 0, rows: [] });
	        	var panelInfo = {
	                    closable:true,
	                    href:'customerService/myProduct.do', border:false, plain:true,
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
	                       $('#studentId',tab).val(studentId);
	                    }
	                };
	        	
	        	$('#tabs',controlPanelDialog).tabs('update', {
	        		tab: tab,
	        		options: panelInfo
	        	});
	        	$('#studentId',tab).val(studentId);
	        }
	        if(title=='消费单'){
	        	$('#billList',tab).datagrid('loadData',{ total: 0, rows: [] });
	        	var panelInfo = {
	                    closable:true,
	                    href:'customerService/myExpense.do', border:false, plain:true,
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
	                       $('#studentId',tab).val(studentId);
	                       $('#studentName',tab).val(studentName);
	                    }
	                };
	        	
	        	$('#tabs',controlPanelDialog).tabs('update', {
	        		tab: tab,
	        		options: panelInfo
	        	});
	        	$('#studentId',tab).val(studentId);
	        	$('#studentName',tab).val(studentName);
	        }
	        if(title=='选班信息'){
	        	$('#stuClassInfoList',tab).datagrid('loadData',{ total: 0, rows: [] });
	        	var panelInfo = {
	                    closable:true,
	                    href:'customerService/myStuClassInfo.do', border:false, plain:true,
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
	                       $('#studentId',tab).val(studentId);
	                    }
	                };
	        	
	        	$('#tabs',controlPanelDialog).tabs('update', {
	        		tab: tab,
	        		options: panelInfo
	        	});
	        	$('#studentId',tab).val(studentId);
	        }
	    }  
	});
  }
})(jQuery);   