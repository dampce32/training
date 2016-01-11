// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.potCourseInit = function() {
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
					{text:'删除',iconCls:'icon-remove',handler:function(){onDelete();}},'-',
					{text:'启用',iconCls:'icon-info',handler:function(){onMulUpdateStatus(1)}},'-',
					{text:'禁用',iconCls:'icon-warn',handler:function(){onMulUpdateStatus(0)}}
				],
		columns:[[
			{field:'status',title:'状态',width:50,sortable:true,align:"center",
						formatter: function(value,row,index){
							if (value==0){
								return '<img src="style/v1/icons/warn.png"/>';
							} else if (value==1){
								return '<img src="style/v1/icons/info.png"/>';
							}
			}},
			{field:'courseName',title:'咨询课程名称',width:150,align:"center"}
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
	    title: '编辑咨询课程',  
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
	//添加
	var onAdd = function(){
		$(editForm).form('clear');
		$('#status',editForm).combobox('setValue',1);
		$(editDialog).dialog('open');
	}
	//保存前的赋值操作
	var setValue = function(){
		var courseName = $.trim($('#courseName',editForm).val());
		if(''==courseName){
			$.messager.alert('提示','请填写咨询课程名称','warning');
			return false;
		}
		return true;
	}
	//保存
	var onSave = function(){
		$(editForm).form('submit',{
			url: 'potCourse/savePotCourse.do',
			onSubmit: function(){
				return setValue();
			},
			success: function(data){
				var result = eval('('+data+')');
				if(result.isSuccess){
					var fn = function(){
						var potCourseId = $.trim($('#potCourseId',editForm).val());
						if(potCourseId==''){//新增
							var  data = result.data;
							$('#potCourseId',editForm).val(data.potCourseId);
						}
						TRN.PotCourseList = null;
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
				var url = "potCourse/deletePotCourse.do";
				var content = {potCourseId:selectRow.potCourseId};
				$.post(url,content,
					function(result){
						if(result.isSuccess){
							search(true);
							TRN.PotCourseList = null;
						}else{
							$.messager.alert('提示',result.message,"error");
						}
				},'json');
			}
		})
	}
	
	var onMulUpdateStatus = function(status){
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
				var url = 'potCourse/updateStatusPotCourse.do';
				var content ={potCourseId:selectRow.potCourseId,status:status};
				asyncCallService(url,content,function(result){
					if(result.isSuccess){
						var fn = function(){
							search(true);
							TRN.PotCourseList = null;
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
		var courseName = $('#courseNameSearch',queryContent).val();
		var content = {courseName:courseName,page:pageNumber,rows:pageSize};
		//取得列表信息
		var url = 'potCourse/queryPotCourse.do';
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
		var url = "potCourse/getTotalCountPotCourse.do";
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
  }
})(jQuery);   