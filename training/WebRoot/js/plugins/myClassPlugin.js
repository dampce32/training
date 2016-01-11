// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.myClassInit = function() {
	  var $this = $(this);
	  var id = $(this).attr('id');
	  
	  var height = $(document.body).height();
	  
	  var queryContent = $('#queryContent',$this);
	  var searchBtn = $('#searchBtn',$this);
	  
	  var viewList = $('#viewList',$this);
	  var selectRow = null;
	  var selectIndex = null;
	  
	  var editDialog = $('#editDialog',$this);
	  var editForm = $('#editForm',$this);
	  
	  var pager = $('#pager',$this);
	  var pageNumber = 1;
	  var pageSize = 10;
	  
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
					{text:'查看详细',iconCls:'icon-view',handler:function(){onUpdate();}}
				],
		columns:[[
		    {field:'classId',title:'班号',width:50,align:"center"},
			{field:'className',title:'班级名称',width:100,align:"center"},
			{field:'startDate',title:'开课时间',width:100,align:"center"},
			{field:'timeRule',title:'上课规律',width:100,align:"center"},
			{field:'schoolName',title:'校区',width:100,align:"center"},
			{field:'stuCount',title:'招生比例',width:150,align:"center",
				formatter: function(value,row,index){
					 var bar = '<div style="width:80px;height:12px;background:#fff;border:1px solid #ccc;text-align:left;float:left;">' +
						'<div style="width:' + row.stuCount*100/row.planCount + '%;height:100%;background:blue"></div>'+
						'</div>';
					  var number = '<span style="width:50px;margin-left:5px;text-align:left;">'+
						'<span style="color: red;">'+row.stuCount+'</span>/'+row.planCount+
						'</span>';
					  return bar+number;
				}},
			{field:'courseProgress',title:'课程进度',width:150,align:"center",
				formatter: function(value,row,index){
					 var bar = '<div style="width:80px;height:12px;background:#fff;border:1px solid #ccc;text-align:left;float:left;">' +
						'<div style="width:' +row.courseProgress*100/row.lessons + '%;height:100%;background:blue"></div>'+
						'</div>';
					  var number = '<span style="width:50px;margin-left:5px;text-align:left;">'+
						'<span style="color: red;">'+row.courseProgress+'</span>/'+row.lessons+
						'</span>';
					  return bar+number;
				}},
			{field:'arrangeLessons',title:'排课进度',width:150,align:"center",
				formatter: function(value,row,index){
					 var bar = '<div style="width:80px;height:12px;background:#fff;border:1px solid #ccc;text-align:left;float:left;">' +
						'<div style="width:' + row.arrangeLessons*100/row.lessons + '%;height:100%;background:blue"></div>'+
						'</div>';
					  var number = '<span style="width:50px;margin-left:5px;text-align:left;">'+
						'<span style="color: red;">'+row.arrangeLessons+'</span>/'+row.lessons+
						'</span>';
					  return bar+number;
				}}
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
	//查询
	var search = function(flag){
		var className = $('#classNameSearch',queryContent).val();
		var content = {className:className,page:pageNumber,rows:pageSize};
		//取得列表信息
		var url = 'teacher/queryByTeacherClass.do';
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
		var url = "teacher/getTotalCountByTeacherClass.do";
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
	
	//修改
	var onUpdate = function(){
		if(selectRow==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		}
		$(editForm).form('clear');
		$('#courseType',editForm).val(selectRow.courseTypeName);
		$('#course',editForm).val(selectRow.courseName);
		$('#classType',editForm).combobox('setValue',selectRow.classType);
		$('#teacher',editForm).val(selectRow.teacherName);
		$('#school',editForm).val(selectRow.schoolName);
		$('#classroom',editForm).val(selectRow.classroomName);
		$('#creater',editForm).val(selectRow.createrName);
		$(editForm).form('load',selectRow);
		$(editDialog).dialog('open');
	 };
	//编辑框
	$(editDialog).dialog({  
	    title: '编辑班级',  
	    width:440,
	    height:height-10,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    toolbar:[{
			text:'退出',
			iconCls:'icon-cancel',
			handler:function(){
				$(editDialog).dialog('close');
			}
		}]
	});
  };
})(jQuery);   