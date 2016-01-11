// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.rewPunInit = function() {
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
	  
	  var editDialog = $('#editDialog',$this);
	  var editForm = $('#editForm',$this);
	  
	  $('#userSearch',queryContent).combobox({
			width:120,
			data:TRN.getEmployeeList(),
			valueField:'userId',
			textField:'userName'
	  });
	  
	//加载列表
	  $(viewList).datagrid({
		singleSelect:true,
		method:"POST",
		nowrap:true,
		striped: true,
		collapsible:true,
		rownumbers:true,
		fit:true,
		toolbar:[	
					{text:'添加',iconCls:'icon-add',handler:function(){onAdd();}},'-',
					{text:'修改',iconCls:'icon-edit',handler:function(){onUpdate();}},'-',
					{text:'删除',iconCls:'icon-remove',handler:function(){onDelete();}}
				],
		columns:[[
			{field:'userName',title:'奖惩员工',width:100,align:"center"},
			{field:'rewPunTypeName',title:'奖惩类别',width:100,align:"center"},
			{field:'rewPunPrice',title:'金额',width:100,align:"center"},
			{field:'rewPunDate',title:'时间',width:100,align:"center"},
			{field:'note',title:'理由',width:100,align:"center"},
			{field:'handlerName',title:'录入员',width:100,align:"center"}
			
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
	//编辑框
	$(editDialog).dialog({  
	    title: '编辑奖惩',  
	    width:400,
	    height:350,
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
			id:'add'+id,
			text:'新增',
			iconCls:'icon-add',
			handler:function(){
				onAdd();
			}
		},'-',{
			text:'退出',
			iconCls:'icon-cancel',
			handler:function(){
				$(editDialog).dialog('close');
			}
		}]
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
		var userId = $('#userSearch',queryContent).combobox('getValue');
		var beginDate = $.trim($('#beginDate',queryContent).val());
		var endDate = $.trim($('#endDate',queryContent).val());
		var userBoolean=isNumber(userId);
		if(userBoolean==false){
			$.messager.alert('提示','请选择已有的员工','error');
			return;
		}
		
		var content = {'user.userId':userId,beginDate:beginDate,endDate:endDate,page:pageNumber,rows:pageSize};
		//取得列表信息
		var url = 'employee/queryRewPun.do';
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
	
	//判断死否是数字
	function isNumber(s)  {   
		var strP=/^\d+$/;
		if(''==s||strP.test(s)) { 
			return true;
		}else{
			return false;
		}
	}
	
	var initCombobox = function(){
		$('#user',editForm).combobox({
			width:250,
			data:TRN.getEmployeeList(),
			valueField:'userId',
			textField:'userName'
		});
		$('#handler',editForm).combobox({
			width:250,
			data:TRN.getEmployeeList(),
			valueField:'userId',
			textField:'userName'
		});
		$('#rewPunType',editForm).combobox({
			width:250,
			data:TRN.getRewPunTypeList(),
			valueField:'rewPunTypeId',
			textField:'rewPunTypeName'
		});
	};
	
	//添加
	var onAdd = function(){
		$(editForm).form('clear');
		initCombobox();
		$(editDialog).dialog('open');
	};
	//保存前的赋值操作
	var setValue = function(){
		var userId = $('#user',editForm).combobox('getValue');
		var userBoolean=isNumber(userId);
		if(userId==''||userBoolean==false){
			$.messager.alert('提示','请选择已有的奖惩人','error');
			return false;
		}
		var rewPunTypeId = $('#rewPunType',editForm).combobox('getValue');
		var rewPunTypeBoolean=isNumber(rewPunTypeId);
		if(rewPunTypeId==''||rewPunTypeBoolean==false){
			$.messager.alert('提示','请选择已有的奖惩类别','warning');
			return false;
		}
		var rewPunPrice=$.trim($('#rewPunPrice',editForm).val());
		if(''==rewPunPrice){
			$.messager.alert('提示','请填写奖惩金额','warning');
			return false;
		}
		var rewPunDate=$.trim($('#rewPunDate',editForm).val());
		if(''==rewPunDate){
			$.messager.alert('提示','请填写奖惩日期','warning');
			return false;
		}
		var handlerId = $('#handler',editForm).combobox('getValue');
		var handlerIdBoolean=isNumber(handlerId);
		if(handlerId==''||handlerIdBoolean==false){
			$.messager.alert('提示','请选择已有的经办人','warning');
			return false;
		}
		return true;
	};
	//保存
	var onSave = function(){
		$(editForm).form('submit',{
			url: 'employee/saveRewPun.do',
			onSubmit: function(){
				return setValue();
			},
			success: function(data){
				var result = eval('('+data+')');
				if(result.isSuccess){
					var fn = function(){
						var rewPunId = $.trim($('#rewPunId',editForm).val());
						if(rewPunId==''){//新增
							var  data = result.data;
							$('#rewPunId',editForm).val(data.rewPunId);
						}
						TRN.CourseTypeList = null;
					};
					$.messager.alert('提示','保存成功','info',fn);
				}else{
					$.messager.alert('提示',result.message,"error");
				}
			}
		});
	};
	
	//统计总数
	var getTotal = function(content){
		var url = "employee/getTotalCountRewPun.do";
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
	
	//修改
	var onUpdate = function(){
		if(selectRow==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		}
		initCombobox();
		$(editForm).form('load',selectRow);
		$('#user',editForm).combobox('setText',selectRow.userName);
		$('#handle',editForm).combobox('setText',selectRow.handlerName);
		$('#rewPunType',editForm).combobox('setText',selectRow.rewPunTypeName);
		$('#user',editForm).combobox('setValue',selectRow.userId);
		$('#handler',editForm).combobox('setValue',selectRow.handlerId);
		$('#rewPunType',editForm).combobox('setValue',selectRow.rewPunTypeId);
		$(editDialog).dialog('open');
	};
	 
	//删除
		var onDelete = function(){
			if(selectRow==null){
				$.messager.alert("提示","请选择数据行","warning");
				return;
			}
			$.messager.confirm('确认', '确定删除该记录吗?', function(r){
				if(r){
					var url = "employee/deleteRewPun.do";
					var content = {rewPunId:selectRow.rewPunId};
					$.post(url,content,
						function(result){
							if(result.isSuccess){
								search(true);
								TRN.CourseTypeList = null;
							}else{
								$.messager.alert('提示',result.message,"error");
							}
					},'json');
				}
			});
		};
  };
})(jQuery);   