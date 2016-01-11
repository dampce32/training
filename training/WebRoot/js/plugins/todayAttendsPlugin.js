// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.todayAttendsInit = function() {
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
	  
	  var getBar = function(left,right){
		  var bar = '<div style="width:80px;height:12px;background:#fff;border:1px solid #ccc;text-align:left;float:left;">' +
			'<div style="width:' + left*100/right + '%;height:100%;background:blue"></div>'+
			'</div>';
		  var number = '<span style="width:50px;margin-left:5px;text-align:left;">'+
			'<span style="color: red;">'+left+'</span>/'+right+
			'</span>';
		  return bar+number;
	  };
	  
	//加载列表
	  $(viewList).datagrid({
		checkbox:true,
		singleSelect:true,
		method:"POST",
		nowrap:true,
		striped: true,
		collapsible:true,
		rownumbers:true,
		fit:true,
		toolbar:[	
					{text:'编辑考勤',iconCls:'',handler:function(){onUpdate();}},
					{text:'删除考勤',iconCls:'',handler:function(){onDelete();}},'-',
					{text:'学员控制面板',iconCls:'icon-view',handler:function(){}},
					{text:'班级控制面板',iconCls:'icon-view',handler:function(){}}
				],
		columns:[[
			{field:'studentId',title:'学号',width:100,align:"center"},
			{field:'studentName',title:'姓名',width:100,align:"center"},
			{field:'classId',title:'班号',width:100,align:"center"},
			{field:'className',title:'班级名称',width:170,align:"center"},
			{field:'scStatus',title:'状态',width:100,align:"center",
				formatter:function(value,row,index){
					if(value==1){
						return '正常';
					}else if(value==2){
						return '插班';
					}else if(value==3){
						return '转班';
					}else if(value==4){
						return '休学';
					}else if(value==5){
						return '退学';
					}else if(value==6){
						return '弃学';
					}
				}
			},
			{field:'lessonDegreeDate',title:'上课日期',width:100,align:"center"},
			{field:'time',title:'上课时段',width:100,align:"center"},
			{field:'status',title:'出勤状态',width:100,align:"center"},
			{field:'lessons',title:'课时',width:50,align:"center"},
			{field:'note',title:'备注',width:100,align:"center"},
			{field:'showCourseProgress',title:'个人课时进度',width:150,align:"center",
				formatter:function(value,row,index){
					return getBar(row.courseProgress,row.scLessons);
				}
			}
		]],
		onClickRow:function(rowIndex, rowData){
			selectRow = rowData;
			selectIndex = rowIndex;
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
	$(searchBtn).click(function(){
		search(true);
	});
	
	//查询
	var search = function(flag){
		//取得列表信息
		var url = 'academic/todayQueryAttend.do';
		var content = {studentId:'',schoolId:'',lessonDegreeDate:'',
				page:pageNumber,rows:pageSize};
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
		var url = "academic/todayTotalAttend.do";
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
	
	//编辑框
	$(editDialog).dialog({  
	    title: '编辑考勤',  
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
			text:'退出',
			iconCls:'icon-cancel',
			handler:function(){
				$(editDialog).dialog('close');
			}
		}]
	});
	
//	var getLessons = function(){
//		var lessonSelect = new Array();
//		var scLessons = parseInt(selectRow.scLessons);
//		for(var i=0;i<=scLessons;i++){
//			if(i=0){
////				lessonSelect.push({"value":i,"text":scLessons-i,selected:true});
//				
//			}else{
////				lessonSelect.push({"value":i,"text":scLessons-i});
//			}
//		}
//		alert(lessonSelect);
//		return lessonSelect;
//	};
	
//	//初始化状态框
//	$('#lessons',editForm).combobox({
//		editable:false,
//		data : getLessons()
//	});
	
	//编辑
	var onUpdate = function(){
		if(selectRow==null){
			$.messager.alert('提示','请选择数据行',"warning");
			return;
		}
		$(editForm).form('clear');
		$(editForm).form('load',selectRow);
		
		$('#lessonDegreeId',editForm).val(selectRow.lessonDegreeId);
		$('#classId',editForm).val(selectRow.classId);
		$('#lessonStatus',editForm).val(selectRow.lessonStatus);
		$('#scLessons',editForm).val(selectRow.scLessons);
		$(editDialog).dialog('open');
	};
	
	var setValue = function(){
		return true;
	};
	
	//保存
	var onSave = function(){
		alert(selectRow.attendId);
		
		var url = 'academic/batchSaveAttend.do?idStr='+selectRow.studentId;
		
		$(editForm).form('submit',{
			url: url,
			onSubmit: function(){
				return setValue();
			},
			success: function(data){
//				var result = eval('('+data+')');
				if(result.isSuccess){
					$.messager.alert('提示','保存成功','info');
				}else{
					$.messager.alert('提示',result.message,"error");
				}
			}
		});
	};
	
	//删除
	var onDelete = function(){
		
	};
  };
})(jQuery);   