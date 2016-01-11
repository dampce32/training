// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.leaveInit = function() {
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
	  $('#schoolSearch',queryContent).combobox({
		  width:150,
		  data:TRN.getSelfSchoolList(),
		  valueField:'schoolCode',
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
					{text:'删除',iconCls:'icon-remove',handler:function(){onDelete();}}
				],
		columns:[[
			{field:'studentName',title:'学员',width:100,align:"center"},
			{field:'date',title:'办理时间',width:100,align:"center"},
			{field:'beginDate',title:'开始时间',width:100,align:"center"},
			{field:'endDate',title:'结束时间',width:100,align:"center"},
			{field:'note',title:'理由',width:100,align:"center"},
			{field:'userName',title:'录入员',width:100,align:"center"}
			
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
	    title: '编辑请假单',  
	    width:400,
	    height:340,
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
		var studentId = $('#studentSearch',queryContent).val();
		var schoolCode = $('#schoolSearch',queryContent).combobox('getValue');
		var content = {schoolCode:schoolCode,'student.studentId':studentId,page:pageNumber,rows:pageSize};
		//取得列表信息
		var url = 'academic/queryLeave.do';
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
		$('#student',editForm).combobox({
			width:250,
			data:TRN.getStudentList(),
			valueField:'studentId',
			textField:'studentName'
		});
		$('#user',editForm).combobox({
			width:250,
			data:TRN.getEmployeeList(),
			valueField:'userId',
			textField:'userName'
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
		var studentId = $('#student',editForm).combobox('getValue');
		var studentBoolean=isNumber(studentId);
		if(studentId==''||studentBoolean==false){
			$.messager.alert('提示','请选择已有的学员','error');
			return false;
		}
		var date=$.trim($('#date',editForm).val());
		if(''==date){
			$.messager.alert('提示','请填写办理日期','warning');
			return false;
		}
		var beginDate=$.trim($('#beginDate',editForm).val());
		if(''==beginDate){
			$.messager.alert('提示','请填写开始日期','warning');
			return false;
		}
		var endDate=$.trim($('#endDate',editForm).val());
		if(''==endDate){
			$.messager.alert('提示','请填写结束日期','warning');
			return false;
		}
		var userId = $('#user',editForm).combobox('getValue');
		var userBoolean=isNumber(userId);
		if(userId==''||userBoolean==false){
			$.messager.alert('提示','请选择已有的经办人','warning');
			return false;
		}
		return true;
	};
	//保存
	var onSave = function(){
		$(editForm).form('submit',{
			url: 'academic/saveLeave.do',
			onSubmit: function(){
				return setValue();
			},
			success: function(data){
				var result = eval('('+data+')');
				if(result.isSuccess){
					var fn = function(){
						var leaveId = $.trim($('#leaveId',editForm).val());
						if(leaveId==''){//新增
							var  data = result.data;
							$('#leaveId',editForm).val(data.leaveId);
						}
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
		var url = "academic/getTotalCountLeave.do";
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
		$('#student',editForm).combobox('setText',selectRow.studentName);
		$('#user',editForm).combobox('setValue',selectRow.userId);
		$('#student',editForm).combobox('setValue',selectRow.studentId);
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
					var url = "academic/deleteLeave.do";
					var content = {leaveId:selectRow.leaveId};
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