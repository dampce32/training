// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.employeeInit = function() {
	  var $this = $(this);
	  var id = $(this).attr('id');
	  var width = $(document.body).width();
	  var height = $(document.body).height();
	  
	  var queryContent = $('#queryContent',$this);
	  var searchBtn = $('#searchBtn',queryContent);
	  var changeSearch = false;
	  
	  var viewList = $('#viewList',$this);
	  var selectRow = null;
	  var selectIndex = null;
	  var editDialog = $('#editDialog',$this);
	  var editForm = $('#editForm',$this);
	  $('#schoolSearch',queryContent).combobox({
		  width:120,
		  data:TRN.getSelfSchoolList(),
		  valueField:'schoolId',
		  textField:'schoolName'
	  });
	//加载列表
	  $(viewList).datagrid({
		method:"POST",
		nowrap:true,
		collapsible:true,
		rownumbers:true,
		pagination:true,
		fit:true,
		selectOnCheck:false,
		checkOnSelect:false,
		singleSelect:true,
		toolbar:[	
					{text:'添加',iconCls:'icon-add',handler:function(){onAdd();}},'-',
					{text:'修改',iconCls:'icon-edit',handler:function(){onUpdate();}},'-',
					{text:'删除',iconCls:'icon-remove',handler:function(){onMulDelete();}},'-',
					{text:'启用',iconCls:'icon-info',handler:function(){onMulUpdateStatus(1);}},'-',
					{text:'禁用',iconCls:'icon-warn',handler:function(){onMulUpdateStatus(0);}},'-',
					{text:'员工角色',iconCls:'icon-role',handler:function(){onUserRole();}}
				],
		columns:[[
		    {field:'ck',title:'选择',checkbox:true},
			{field:'status',title:'状态',width:50,sortable:true,align:"center",
				formatter: function(value,row,index){
					if (value==0){
						return '<img src="style/v1/icons/warn.png"/>';
					} else if (value==1){
						return '<img src="style/v1/icons/info.png"/>';
					}
			  }},
			{field:'userId',title:'工号',width:100,align:"center"},
			{field:'userName',title:'姓名',width:100,align:"center"},
			{field:'userCode',title:'登陆名称',width:100,align:"center"},
			{field:'basePay',title:'基本工资',width:100,align:"center"},
			{field:'schoolName',title:'校区',width:100,align:"center"},
			{field:'hourFee',title:'课时费',width:100,align:"center"},
			{field:'isTeacher',title:'兼职',width:100,align:"center",
				formatter: function(value,row,index){
					if (value==0){
						return '否';
					} else if (value==1){
						return '是';
					}
			  }}
		]],
		onLoadSuccess:function(){
			selectRow = null;
	 		selectIndex = null;
		},
		onDblClickRow:function(rowIndex,rowData){
			onUpdate();
		},onClickRow:function(rowIndex, rowData){
			selectRow = rowData;
			selectIndex = rowIndex;
		}
	  });
	//分页条
	$(viewList).datagrid('getPager').pagination({   
	    onSelectPage: function(page, rows){
	    	search();
	    }
	});
	//编辑框
	$(editDialog).dialog({  
	    title: '编辑员工',  
	    width:800,
	    height:510,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    toolbar:[
	            {id:'save'+id,text:'保存',iconCls:'icon-save',handler:function(){onSave();}},'-',
				{id:'add'+id,text:'新增',iconCls:'icon-add',handler:function(){onAdd();}},'-',
				{id:'delete'+id,text:'删除',iconCls:'icon-remove',handler:function(){onDelete();}},'-',
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
		$('#delete'+id).linkbutton('disable');
		$('#pre'+id).linkbutton('disable');
		$('#next'+id).linkbutton('disable');
	}
	//更新时,按钮的状态
	var updateBtnStatus = function(){
		$('#delete'+id).linkbutton('enable');
	}
	//初始化选择条件
	var initCombobox = function(){
		//所在校区
		$('#school',editDialog).combobox({
			valueField:'schoolId',
			textField:'schoolName',
			width:250,
			data:TRN.getSelfSchoolList()
		})	
	}
	//添加
	var onAdd = function(){
		$(viewList).datagrid('unselectAll');
		selectIndex = null;
		selectRow = null;
		$(editForm).form('clear');
		addBtnStatus();
		initCombobox();
		$('#status',editDialog).combobox('setValue',1);
		$('#sex',editDialog).combobox('setValue',1);
		$('#isPartTime',editDialog).combobox('setValue',0);
		$('#isTeacher',editDialog).combobox('setValue',0);
		$(editDialog).dialog('open');
	}
	//保存前的赋值操作
	var setValue = function(){
		//姓名
		var userName = $.trim($('#userName',editForm).val());
		if(''==userName){
			$.messager.alert('提示','请填写姓名','warning');
			return false;
		}
		//登陆名称
		var userCode = $.trim($('#userCode',editForm).val());
		if(''==userCode){
			$.messager.alert('提示','请填写登陆名称','warning');
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
		//出生日期
		var birthday = $.trim($('#birthday',editForm).val());
		if(''==birthday){
			$.messager.alert('提示','请选择出生日期','warning');
			return false;
		}
		//是否兼职
		var isPartTime = $('#isPartTime',editDialog).combobox('getValue') ;
		if(''==isPartTime){
			$.messager.alert('提示','请选择是否兼职','warning');
			return false;
		}
		if(isNaN(parseInt(isPartTime))){
			$.messager.alert('提示','请选择提供选择的是否兼职','warning');
			return false;
		}
		//是否授课
		var isTeacher = $('#isTeacher',editDialog).combobox('getValue') ;
		if(''==isTeacher){
			$.messager.alert('提示','请选择是否授课','warning');
			return false;
		}
		if(isNaN(parseInt(isTeacher))){
			$.messager.alert('提示','请选择提供选择的是否授课','warning');
			return false;
		}
		//任聘日期
		var comeDate = $.trim($('#comeDate',editForm).val());
		if(''==comeDate){
			$.messager.alert('提示','请选择任聘日期','warning');
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
		//状态
		var status = $('#status',editDialog).combobox('getValue') ;
		if(''==status){
			$.messager.alert('提示','请选择状态','warning');
			return false;
		}
		if(isNaN(parseInt(status))){
			$.messager.alert('提示','请选择提供选择的状态','warning');
			return false;
		}
		return true;
	}
	//保存
	var onSave = function(){
		$(editForm).form('submit',{
			url: 'employee/saveEmployee.do',
			onSubmit: function(){
				return setValue();
			},
			success: function(data){
				var result = eval('('+data+')');
				if(result.isSuccess){
					var fn = function(){
						var userId = $.trim($('#userId',editForm).val());
						if(userId==''){//新增
							var  data = result.data;
							$('#userId',editForm).val(data.userId);
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
		$(editForm).form('load',selectRow);
		onOpen(selectRow.userId);
		$(editDialog).dialog('open');
	 }
	//打开
	var onOpen = function(userId){
		var url = 'employee/initEmployee.do';
		var content ={userId:userId};
		asyncCallService(url,content,function(result){
			if(result.isSuccess){
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
				
				var data = result.data;
				var userData = eval("("+data.userData+")");
				initCombobox();
				$('#userId',editDialog).val(userData.userId);
				$('#userCode',editDialog).val(userData.userCode);
				$('#userName',editDialog).val(userData.userName);
				$('#sex',editDialog).combobox('setValue',userData.sex);
				$('#birthday',editDialog).val(userData.birthday);
				$('#isPartTime',editDialog).combobox('setValue',userData.isPartTime);
				$('#isTeacher',editDialog).combobox('setValue',userData.isTeacher);
				$('#comeDate',editDialog).val(userData.comeDate);
				$('#outDate',editDialog).val(userData.outDate);
				
				$('#course',editDialog).val(userData.course);
				$('#finishSchool',editDialog).val(userData.finishSchool);
				$('#diploma',editDialog).val(userData.diploma);
				$('#resume',editDialog).val(userData.resume);
				$('#tel',editDialog).val(userData.tel);
				$('#email',editDialog).val(userData.email);
				$('#address',editDialog).val(userData.address);
				$('#postCode',editDialog).val(userData.postCode);
				
				$('#headship',editDialog).val(userData.headship);
				$('#idcard',editDialog).val(userData.idcard);
				$('#basePay',editDialog).numberbox('setValue',userData.basePay);
				$('#hourFee',editDialog).numberbox('setValue',userData.hourFee);
				
				$('#status',editDialog).combobox('setValue',userData.status);
				$('#school',editDialog).combobox('setValue',userData.schoolId);
				
				updateBtnStatus();
			}else{
				$.messager.alert('提示',result.message,'error');
			}
		});
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
	//删除
	var onDelete = function(){
		var userId = $('#userId',editDialog).val();
		$.messager.confirm("提示","确定要删除记录?",function(t){ 
			if(t){
				var url = 'employee/deleteEmployee.do';
				var content ={userId:userId};
				asyncCallService(url,content,function(result){
					if(result.isSuccess){
						var fn = function(){
							selectRow = null;
							selectIndex = null;
							$(editDialog).dialog('close');
							search(true);
						}
						$.messager.alert('提示','删除成功','info',fn);
					}else{
						$.messager.alert('提示',result.message,'error');
					}
				});
			}
		});
	}
	//删除多个
	var onMulDelete = function(){
		var rows =  $(viewList).datagrid('getChecked');
		if(rows.length==0){
			$.messager.alert("提示","请选择要删除的数据行","warning");
			return;
		}
		var idArray = new Array();
		for ( var i = 0; i < rows.length; i++) {
			idArray.push(rows[i].userId);
		}
		$.messager.confirm("提示","确定要删除选中的记录?",function(t){ 
			if(t){
				var url = 'employee/mulDeleleEmployee.do';
				var content ={ids:idArray.join(CSIT.join)};
				asyncCallService(url,content,function(result){
					if(result.isSuccess){
						var fn = function(){
							search(true);
						}
						$.messager.alert('提示','删除成功','info',fn);
					}else{
						$.messager.alert('提示',result.message,'error');
					}
				});
			}
		});
	}
	//修改多个审核状态
	var onMulUpdateStatus = function(status){
		var rows =  $(viewList).datagrid('getChecked');
		var msg = '';
		if(status==1){
			msg = '启用';
		}else{
			msg = '禁用';
		}
		if(rows.length==0){
			$.messager.alert("提示","请选择要"+msg+"的数据行","warning");
			return;
		}
		var idArray = new Array();
		for ( var i = 0; i < rows.length; i++) {
			idArray.push(rows[i].userId);
		}
		$.messager.confirm("提示","确定要"+msg+"记录?",function(t){ 
			if(t){
				var url = 'employee/mulUpdateStatusEmployee.do';
				var content ={ids:idArray.join(CSIT.join),status:status};
				asyncCallService(url,content,function(result){
					if(result.isSuccess){
						var fn = function(){
							search(true);
						}
						$.messager.alert('提示',msg+'成功','info',fn);
					}else{
						$.messager.alert('提示',result.message,'error');
					}
				});
			}
		});
	}
	//查询
	var search = function(flag){
		var userId = $('#userIdSearch',queryContent).val();
		var userName = $('#userNameSearch',queryContent).val();
		var schoolId =  $.trim($('#schoolSearch',queryContent).combobox('getValue'));
		if(schoolId==''){
			schoolId = -1 ;
		}
		var options = $(viewList).datagrid('options');
		var content = {userId:userId,userName:userName,'school.schoolId':schoolId,page:options.pageNumber,rows:options.pageSize};
		//取得列表信息
		var url = 'employee/queryEmployee.do';
		var result = syncCallService(url,content);
		if(result.isSuccess){
			var  data = result.data;
			var datagridData = eval("("+data.datagridData+")");
			$(viewList).datagrid('loadData',datagridData);
			//需要重新重新分页信息
			if(flag){
				var url = "employee/getTotalCountEmployee.do";
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
		}else{
			$.messager.alert('提示',result.message,'error');
		}
	}
	//查询
	$(searchBtn).click(function(){
		search(true);
	})
	//---用户角色---
	var userRoleList = $('#userRoleList',$this);
	var userRoleDialog = $('#userRoleDialog',$this);
	var oldIdArray = new Array();
	var idArray = new Array();
	var deleteIdList = new Array();
	var addIdList = new Array();
	//编辑框
	$(userRoleDialog).dialog({  
	    title: '编辑员工角色信息',  
	    width:400,
	    height:500,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    toolbar:[{
			text:'保存',
			iconCls:'icon-save',
			handler:function(){
				onSaveUserRole();
			}
		},'-',{
			text:'退出',
			iconCls:'icon-cancel',
			handler:function(){
				$(userRoleDialog).dialog('close');
			}
		}]
	});
	//用户角色
	var onUserRole = function(){
		if(selectRow==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		}
		 //加载列表
		$(userRoleList).datagrid({
			url:"system/queryRoleUserRole.do?user.userId="+selectRow.userId,
			fit:true,
			method:"POST",
			nowrap:true,
			striped: true,
			collapsible:true,
			rownumbers:true,
			columns:[[
			    {field:'ck',checkbox:true},
				{field:'roleName',title:'角色名',width:100,align:"center"}
			]],
			onLoadSuccess:function(data){
				var rows = $(userRoleList).datagrid('getRows');
				oldIdArray = new Array();
				for(var i=0;i<rows.length;i++){
					if(rows[i].checked){
						oldIdArray.push(rows[i].roleId);
						$(userRoleList).datagrid('selectRow',i);
					}
				}
			}
		  });
		$(userRoleDialog).dialog('open');
	}
	//保存用户角色前验证
	var setValueUserRole = function(){
		deleteIdList = new Array();
		addIdList = new Array();
		idArray = new Array();
		var rows = $(userRoleList).datagrid('getSelections');
		for(var i =0;i<rows.length;i++){
			idArray.push(rows[i].roleId);
		}
		
		for (var i =0;i<oldIdArray.length;i++) {
			var oldId = oldIdArray[i];
			var isDel = true;
			for (var j =0;j<idArray.length;j++) {
				var id = idArray[j];
				if(oldId==id){
					isDel =false;
					break;
				}
			}
			if(isDel){
				deleteIdList.push(oldId);
			}
		}
		for (var i =0;i<idArray.length;i++) {
			var id = idArray[i];
			var isAdd = true;
			for (var j =0;j<oldIdArray.length;j++) {
				var oldId = idArray[j];
				if(oldId==id){
					isAdd =false;
					break;
				}
			}
			if(isAdd){
				addIdList.push(id);
			}
		}
		if(addIdList.length==0&&deleteIdList.length==0){
			$.messager.alert('提示','角色没有修改');
			return false;
		}
		return true;
	}
	//保存
	var onSaveUserRole = function(){
		if(setValueUserRole()){
			var userId = selectRow.userId;
			var content = {'user.userId':userId,ids: idArray.join(CSIT.join),oldIds:oldIdArray.join(CSIT.join)};
			var url = "system/updateRoleUserRole.do";
			$.post(url,content,
				function(result){
					if(result.isSuccess){
						oldIdArray = idArray;
						$.messager.alert('提示','保存成功');
					}else{
						$.messager.alert('提示',result.message,"warning");
					}
			},'json')
		}
	}
  }
})(jQuery);   