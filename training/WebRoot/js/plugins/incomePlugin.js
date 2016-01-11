// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.incomeInit = function() {
	  var $this = $(this);
	  var id = $(this).attr('id');
	  var width = $(document.body).width();
	  var height = $(document.body).height();
	  var queryContent = $('#queryContent',$this);
	  var searchBtn = $('#searchBtn',$this);
	  var viewList = $('#viewList',$this);
	  var selectRow = null;
	  var selectIndex = null;
	  var pageNumber = 1;
	  var pageSize = 10;
	  var editDialog = $('#editDialog',$this);
	  var editForm = $('#editForm',$this);
	  $('#schoolSearch',queryContent).combobox({
		  width:150,
		  data:TRN.getSelfSchoolList(),
		  valueField:'schoolId',
		  textField:'schoolName'
	  });
	//加载列表
	  $(viewList).datagrid({
		singleSelect:true,
		method:"POST",
		nowrap:true,
		striped: true,
		collapsible:true,
		rownumbers:true,
		pagination:true,
		fit:true,
		toolbar:[	
					{text:'添加',iconCls:'icon-add',handler:function(){onAdd();}},'-',
					{text:'修改',iconCls:'icon-edit',handler:function(){onUpdate();}},'-',
					{text:'删除',iconCls:'icon-remove',handler:function(){onDelete();}}
				],
		columns:[[
			{field:'type',title:'类型',width:50,sortable:true,align:"center",
						formatter: function(value,row,index){
							if (value==0){
								return '支出';
							} else if (value==1){
								return '收入';
							}
			}},
			{field:'accountDate',title:'日期',width:100,align:"center"},
			{field:'incomeClassName',title:'账目',width:100,align:"center"},
			{field:'price',title:'金额',width:100,align:"center"},
			{field:'accountName',title:'账户',width:100,align:"center"},
			{field:'schoolName',title:'校区',width:100,align:"center"},
			{field:'note',title:'备注',width:300,align:"center"}
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
	$(viewList).datagrid('getPager').pagination({   
	    onSelectPage: function(page, rows){
			pageNumber = page;
			pageSize = rows;
			search();
	    }
	});
	//编辑框
	$(editDialog).dialog({  
	    title: '编辑新账目',  
	    width:770,
	    height:330,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    toolbar:[{
			text:'保存',
			iconCls:'icon-save',
			handler:function(){
				onSave();
			}
		},'-',{
			text:'退出',
			iconCls:'icon-cancel',
			handler:function(){
				$(editDialog).dialog('close');
			}
		}]
	});
	var init = function(ids){
		//校区
		$('#school',editForm).combobox({
			valueField:'schoolId',
			textField:'schoolName',
			width:250,
			data:TRN.getSelfSchoolList()
		});
		//经办人
		$('#user',editForm).combobox({
		  width:250,
		  data:TRN.getEmployeeList(),
		  valueField:'userId',
		  textField:'userName'
	  });
		//资金账户
		$('#account',editForm).combobox({
			valueField:'accountId',
			textField:'accountName',
			width:250,
			data:TRN.getAccountList()
		});
		$('#type',editForm).combobox({
			
			onSelect:function (record){
				var type=$('#type',editForm).combobox('getValue');
				$('#incomeClass',editForm).combobox({
					width:250,
					valueField: 'incomeClassId',
					textField: 'incomeClassName',
		    		url:'dict/queryComboboxIncomeClass.do?type='+type
		    	});
			}
		});
	}
	//添加
	var onAdd = function(){
		$(editForm).form('clear');
		init();
		$(editDialog).dialog('open');
	}
	//保存前的赋值操作
	var setValue = function(){
		var accountDate = $('#accountDate',editForm).val();
		if(''==accountDate){
			$.messager.alert('提示','请填写入账日期','warning');
			return false;
		}
		//办理校区
		var schoolId = $('#school',editForm).combobox('getValue') ;
		if(''==schoolId){
			$.messager.alert('提示','请选择办理校区','warning');
			return false;
		}
		if(isNaN(parseInt(schoolId))){
			$.messager.alert('提示','请选择提供选择的办理校区','warning');
			return false;
		}
		//经办人
		var userId = $('#user',editForm).combobox('getValue') ;
		if(''==userId){
			$.messager.alert('提示','请选择经办人','warning');
			return false;
		}
		if(isNaN(parseInt(userId))){
			$.messager.alert('提示','请选择提供选择的经办人','warning');
			return false;
		}
		var type = $('#type',editForm).combobox('getValue');
		if(''==type){
			$.messager.alert('提示','请选择类型','warning');
			return false;
		}
		if(isNaN(parseInt(type))){
			$.messager.alert('提示','请选择提供选择的类型','warning');
			return false;
		}
		var incomeClass = $('#incomeClass',editForm).combobox('getValue');
		if(''==incomeClass){
			$.messager.alert('提示','请选择账目分类','warning');
			return false;
		}
		var price = $('#price',editForm).val();
		if(''==price){
			$.messager.alert('提示','请填写金额','warning');
			return false;
		}
		var account = $('#account',editForm).combobox('getValue');
		if(''==account){
			$.messager.alert('提示','请选择资金账户','warning');
			return false;
		}
		if(isNaN(parseInt(account))){
			$.messager.alert('提示','请选择提供选择的资金账户','warning');
			return false;
		}
		return true;
	}
	//保存
	var onSave = function(){
		$(editForm).form('submit',{
			url: 'finance/saveIncome.do',
			onSubmit: function(){
				return setValue();
			},
			success: function(data){
				var result = eval('('+data+')');
				if(result.isSuccess){
					var fn = function(){
						search(true);
						$(editDialog).dialog('close');
					}
					$.messager.alert('提示','保存成功','info',fn);
				}else{
					$.messager.alert('提示',result.message,"error");
				}
			}
		});
	}
	var onOpen = function (){
		//校区
		$('#school',editForm).combobox({
			valueField:'schoolId',
			textField:'schoolName',
			width:250,
			data:TRN.getSelfSchoolList()
		});
		//经办人
		$('#user',editForm).combobox({
		  width:250,
		  data:TRN.getEmployeeList(),
		  valueField:'userId',
		  textField:'userName'
	  });
		//资金账户
		$('#account',editForm).combobox({
			valueField:'accountId',
			textField:'accountName',
			width:250,
			data:TRN.getAccountList()
		});
		var type=selectRow.type;
		$('#incomeClass',editForm).combobox({
			valueField:'incomeClassId',
			textField:'incomeClassName',
			width:250,
			url:'dict/queryComboboxIncomeClass.do?type='+type
		});
		$('#type',editForm).combobox({
			onSelect:function (record){
				var type=$('#type',editForm).combobox('getValue');
				$('#incomeClass',editForm).combobox({
					width:250,
					valueField: 'incomeClassId',
					textField: 'incomeClassName',
		    		url:'dict/queryComboboxIncomeClass.do?type='+type
		    	});
			}
		});
	}
	//修改
	var onUpdate = function(){
		if(selectRow==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		}
		$(editForm).form('clear');
		$(editForm).form('load',selectRow);
		onOpen();
		$('#school',editForm).combobox('setValue',selectRow.schoolId);
		$('#user',editForm).combobox('setValue',selectRow.userId);
		$('#account',editForm).combobox('setValue',selectRow.accountId);
		$('#incomeClass',editForm).combobox('setValue',selectRow.incomeClassId);
		$(editDialog).dialog('open');
	 }
	//删除
	var onDelete = function(){
		if(selectRow==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		}
		$.messager.confirm('确认', '确定删除该记录吗?', function(r){
			if(r){
				var url = "finance/deleteIncome.do";
				var content = {incomeId:selectRow.incomeId};
				$.post(url,content,
					function(result){
						if(result.isSuccess){
							search(true);
						}else{
							$.messager.alert('提示',result.message,"error");
						}
				},'json');
			}
		})
	}
	//查询
	var search = function(flag){
		var schoolId = $('#schoolSearch',queryContent).combobox('getValue');
		if(schoolId==''){
			schoolId = -1 ;
		}
		var accountDateBegin = $('#accountDateBeginSearch',queryContent).val();
		var accountDateEnd = $('#accountDateEndSearch',queryContent).val();
		var content = {'school.schoolId':schoolId,accountDateBegin:accountDateBegin,accountDateEnd:accountDateEnd,page:pageNumber,rows:pageSize};
		//取得列表信息
		var url = 'finance/queryIncome.do';
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
		var url = "finance/getTotalCountIncome.do";
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