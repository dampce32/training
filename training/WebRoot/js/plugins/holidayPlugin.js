// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.holidayInit = function() {
	  var $this = $(this);
	  var id = $(this).attr('id');
	  
	  var queryContent = $('#queryContent',$this);
	  var searchBtn = $('#searchBtn',$this);
	  
	  var viewList = $('#viewList',$this);
	  var selectRow = null;
	  var selectIndex = null;
	  
	  var pager = $('#pager',$this);
	  var pageNumber = 1;
	  var pageSize = 10;
	  
	  var editDialog = $('#editDialog',$this);
	  var editForm = $('#editForm',$this);
	  
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
			{field:'holidayName',title:'假期名称',width:100,align:"center"},
			{field:'startDate',title:'开始时间',width:100,align:"center"},
			{field:'endDate',title:'结束时间',width:100,align:"center"}
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
	    title: '编辑假期',  
	    width:400,
	    height:200,
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
				$(editForm).form('clear');
			}
		},'-',{
			text:'退出',
			iconCls:'icon-cancel',
			handler:function(){
				$(editDialog).dialog('close');
			}
		}]
	});    
	//添加
	var onAdd = function(){
		$(editForm).form('clear');
		$(editDialog).dialog('open');
	};
	//保存前的赋值操作
	var setValue = function(){
		var holidayName = $.trim($('#holidayName',editForm).val());
		if(''==holidayName){
			$.messager.alert('提示','请填写假期名称','warning');
			return false;
		}
		var startDate = $('#startDate',editForm).val();
		if(''==startDate){
			$.messager.alert('提示','请选择开始时间','warning');
			return false;
		}
		var endDate = $('#endDate',editForm).val();
		if(''==endDate){
			$.messager.alert('提示','请选择结束时间','warning');
			return false;
		}
		return true;
	};
	//保存
	var onSave = function(){
		$(editForm).form('submit',{
			url: 'dict/saveHoliday.do',
			onSubmit: function(){
				return setValue();
			},
			success: function(data){
				var result = eval('('+data+')');
				if(result.isSuccess){
					var fn = function(){
						var holidayId = $.trim($('#holidayId',editForm).val());
						if(holidayId==''){//新增
							var  data = result.data;
							$('#holidayId',editForm).val(data.holidayId);
						}
					};
					$.messager.alert('提示','保存成功','info',fn);
				}else{
					$.messager.alert('提示',result.message,"error");
				}
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
				var url = "dict/deleteHoliday.do";
				var content = {holidayId:selectRow.holidayId};
				$.post(url,content,
					function(result){
						if(result.isSuccess){
							search(true);
						}else{
							$.messager.alert('提示',result.message,"error");
						}
				},'json');
			}
		});
	};
	//查询
	var search = function(flag){
		var holidayName = $('#holidayNameSearch',queryContent).val();
		var content = {holidayName:holidayName,page:pageNumber,rows:pageSize};
		//取得列表信息
		var url = 'dict/queryHoliday.do';
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
	//统计总数
	var getTotal = function(content){
		var url = "dict/getTotalCountHoliday.do";
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
	//查询
	$(searchBtn).click(function(){
		search(true);
	});
  };
})(jQuery);   