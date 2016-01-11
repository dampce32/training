// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.warehouseInit = function() {
	  var $this = $(this);
	  var id = $(this).attr('id');
	  
	  var queryContent = $('#queryContent',$this);
	  var searchBtn = $('#searchBtn',$this);
	  
	  var viewList = $('#viewList',$this);
	  var selectRow = null;
	  
	  var pager = $('#pager',$this);
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
		fit:true,
		toolbar:[	
					{text:'添加',iconCls:'icon-add',handler:function(){onAdd();}},'-',
					{text:'修改',iconCls:'icon-edit',handler:function(){onUpdate();}},'-',
					{text:'删除',iconCls:'icon-remove',handler:function(){onDelete();}},'-',
					{text:'启用',iconCls:'icon-info',handler:function(){onUpdateStatus(1);}},'-',
					{text:'禁用',iconCls:'icon-warn',handler:function(){onUpdateStatus(0);}}
				],
		columns:[[
		    {field:'schoolId',hidden:true},
			{field:'status',title:'状态',width:50,align:"center",
		    	formatter: function(value,row,index){
			    	if (value==0){
						return '<img src="style/v1/icons/warn.png"/>';
					} else if (value==1){
						return '<img src="style/v1/icons/info.png"/>';
					};
		    	}
			},
			{field:'warehouseName',title:'仓库名称',width:100,align:"center"},
			{field:'tel',title:'仓库电话',width:100,align:"center"},
			{field:'address',title:'仓库地址',width:100,align:"center"},
			{field:'schoolName',title:'所属校区',width:100,align:"center"}
			
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
	    title: '编辑仓库',  
	    width:400,
	    height:300,
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
		var warehouseName = $('#warehouseNameSearch',queryContent).val();
		var schoolId =  $.trim($('#schoolSearch',queryContent).combobox('getValue'));
		if(schoolId==''){
			schoolId = -1 ;
		}
		var content = {warehouseName:warehouseName,page:pageNumber,rows:pageSize,'school.schoolId':schoolId};
		//取得列表信息
		var url = 'dict/queryWarehouse.do';
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
		$('#school',editForm).combobox({
			  width:250,
			  data:TRN.getSelfSchoolList(),
			  valueField:'schoolId',
			  textField:'schoolName'
		});
	};
	
	//添加
	var onAdd = function(){
		$(editForm).form('clear');
		$('#status',editDialog).combobox('setValue',1);
		initCombobox();
		$(editDialog).dialog('open');
	};
	//保存前的赋值操作
	var setValue = function(){
		var warehouseName = $.trim($('#warehouseName',editForm).val());
		if(''==warehouseName){
			$.messager.alert('提示','请填写仓库名称','warning');
			return false;
		}
		var schoolId = $('#school',editForm).combobox('getValue');
		if(''==schoolId){
			$.messager.alert('提示','请选择所属校区','warning');
			return false;
		}
		var bool=isNumber(schoolId);
		if(bool==false){
			$.messager.alert('提示','请选择已有的校区','error');
			return;
		}
		return true;
	};
	//保存
	var onSave = function(){
		
		var schoolId = $('#school',editForm).combobox('getValue');
		var bool=isNumber(schoolId);
		if(bool){
			$(editForm).form('submit',{
				url: 'dict/saveWarehouse.do',
				onSubmit: function(){
					return setValue();
				},
				success: function(data){
					var result = eval('('+data+')');
					if(result.isSuccess){
						var fn = function(){
							var warehouseId = $.trim($('#warehouseId',editForm).val());
							if(warehouseId==''){//新增
								var  data = result.data;
								$('#warehouseId',editForm).val(data.warehouseId);
							}
							TRN.CourseTypeList = null;
						};
						$.messager.alert('提示','保存成功','info',fn);
					}else{
						$.messager.alert('提示',result.message,"error");
					}
				}
			});
		}else{
			$.messager.alert('提示','请选择已有的校区','error');
		}
	};
	
	//统计总数
	var getTotal = function(content){
		var url = "dict/getTotalCountWarehouse.do";
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
		$(editForm).form('load',selectRow);
		initCombobox();
		$('#school',editForm).combobox('setValue',selectRow.schoolId);
		$('#school',editForm).combobox('setText',selectRow.schoolName);
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
					var url = "dict/deleteWarehouse.do";
					var content = {warehouseId:selectRow.warehouseId};
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
		//修改审核状态
		var onUpdateStatus = function(status){
			var msg = '';
			if(status==1){
				msg = '启用';
			}else{
				msg = '禁用';
			}
			if(selectRow==null){
				$.messager.alert("提示","请选择要"+msg+"的数据行","warning");
				return;
			}
			$.messager.confirm("提示","确定要"+msg+"记录?",function(t){ 
				if(t){
					var url = 'dict/updateStatusWarehouse.do';
					var content ={warehouseId:selectRow.warehouseId,status:status};
					asyncCallService(url,content,function(result){
						if(result.isSuccess){
							var fn = function(){
								search(true);
							};
							$.messager.alert('提示',msg+'成功','info',fn);
						}else{
							$.messager.alert('提示',result.message,'error');
						}
					});
				}
			});
		};
  };
})(jQuery);   