// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.todayLessonsInit = function() {
	  var $this = $(this);
	  var id = $(this).attr('id');
	  
	  var height = $(document.body).height();
	  
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
	  
	  var pointNameDialog = $('#pointNameDialog',$this);
	  var pointNameForm = $('#pointNameForm',pointNameDialog);
		
	  var pointNameList = $('#pointNameList',pointNameDialog);
	  var pointNameRow = null;
	  var pointNameIndex = null;
	
	  var pointNamePager = $('#pointNamePager',pointNameDialog);
	  var pointNameNumber = 1;
	  var pointNameSize = 10;
	
	  var studentTree = $('#studentTree',pointNameDialog);
	
	  var savePointDialog = $('#savePointDialog',pointNameDialog);
	  var savePointForm = $('#savePointForm',pointNameDialog);
	
	  var isAdd = false;
	
	  var lessonInfoForm = $('#lessonInfoForm',pointNameDialog);
	  
	  var getBar = function(left,right){
		  var bar = '<div style="width:80px;height:12px;background:#fff;border:1px solid #ccc;text-align:left;float:left;">' +
			'<div style="width:' + left*100/right + '%;height:100%;background:blue"></div>'+
			'</div>';
		  var number = '<span style="width:50px;margin-left:5px;text-align:left;">'+
			'<span style="color: red;">'+left+'</span>/'+right+
			'</span>';
		  return bar+number;
	  };
	  
	  $('#schoolSearch',queryContent).combobox({
		  data:TRN.getSelfSchoolList(),
		  width:120,
		  valueField:'schoolId',
		  textField:'schoolName'
	  });
	  
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
					{text:'批量出勤',iconCls:'icon-add',handler:function(){batchFact();}},
					{text:'个别处理',iconCls:'icon-edit',handler:function(){pointName();}}//,'-',
//					{text:'班级控制面板',iconCls:'icon-view',handler:function(){}}
				],
		columns:[[
			{field:'classId',title:'班号',width:100,align:"center"},
			{field:'className',title:'班级名称',width:100,align:"center"},
			{field:'lessonStatus',title:'状态',width:100,align:"center",
				formatter:function(value,row,index){
					if(value==0){
						return '<span style="color:red">未上课</span>';
					}else if(value==1){
						return '<span style="color:green">已上课</span>';
					}
				}
			},
			{field:'subject',title:'课题',width:100,align:"center"},
			{field:'lessonDegreeDate',title:'上课日期',width:100,align:"center"},
			{field:'time',title:'上课时段',width:100,align:"center"},
			{field:'lessons',title:'课时',width:50,align:"center"},
			{field:'factCount',title:'出勤',width:100,align:"center"},
			{field:'teacherName',title:'讲师',width:100,align:"center"},
			{field:'classroomName',title:'教室',width:100,align:"center"}
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
	$(searchBtn).click(function(){
		search(true);
	});
	
	//查询
	var search = function(flag){
		var schoolId = $('#schoolSearch',queryContent).combobox('getValue');
		var date = $('#dateSearch',queryContent).val();
		if(date==''){
			date = addDays(new Date(),0);
		}
		var content = {'clazz.school.schoolId':schoolId,lessonDegreeDate:date,
				page:pageNumber,rows:pageSize};
		//取得列表信息
		var url = 'academic/queryLessonDegree.do';
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
		var url = "academic/getTotalCountLessonDegree.do";
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
	
	//批量出勤
	var batchFact = function(){
		if(selectRow==null){
			$.messager.alert('提示','请选择数据行','warning');
			return;
		}
		$.messager.confirm('确认', '确定执行批量出勤？<br/>此操作无法撤销', function(r){
			if(r){
				var url = 'academic/batchFactAttend.do';
				var content = {
						'stuClass.clazz.classId':selectRow.classId,
						'lessonDegree.lessonDegreeId':selectRow.lessonDegreeId,
						'lessonDegree.clazz.classId':selectRow.classId,
						'lessonDegree.lessons':selectRow.lessons,
						'lessonDegree.lessonType':selectRow.lessonType,
						'lessonDegree.lessonDegreeDate':selectRow.lessonDegreeDate,
						'lessonDegree.lessonStatus':selectRow.lessonStatus
						};
				var result = syncCallService(url,content);
				if(result.isSuccess){
					search(true);
				}else{
					$.messager.alert('提示',result.message,"error");
				}
			}
		});
	};
	
	//---------------------------------------个别处理------------------------------------
	$(pointNameDialog).dialog({
		title: '点名',  
	    width:1000,
	    height:height-10,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false
	});
	
	//打开点名面板
	var pointName = function(){
		if(selectRow==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		}
		$(pointNameForm).form('clear');
		$(lessonInfoForm).form('clear');
		$(lessonInfoForm).form('load',selectRow);
		$('#teacherId',lessonInfoForm).val(selectRow.teacherId);
		$('#classroomId',lessonInfoForm).val(selectRow.classroomId);
		$('#userId',lessonInfoForm).val(selectRow.userId);
		$('#classId',lessonInfoForm).val(selectRow.classId);
		getStudentTree();
		pointNameSearch(true);
		$(pointNameDialog).dialog('open');
	};
	
	//更新课程信息
	var saveWorkUpdate = function(){
		$(lessonInfoForm).form('submit',{
			url: 'academic/saveLessonDegree.do',
			onSubmit: function(){
				return true;
			},
			success: function(data){
				var result = eval('('+data+')');
				if(result.isSuccess){
					$.messager.alert('提示','更新成功','info');
				}else{
					$.messager.alert('提示',result.message,"error");
				}
			}
		});
	};
	
	
	$('#workUpdate',pointNameDialog).click(function(){
		saveWorkUpdate();
	});
	
	$(studentTree).tree({
		checkbox:true
	});
	
	//获取学员列表
	var getStudentTree = function(){
		var url = 'stuClass/getStudentTreeStuClass.do';
		var content = {'clazz.classId':selectRow.classId,
				lessonDegreeDate:selectRow.lessonDegreeDate,
				lessonDegreeId:selectRow.lessonDegreeId};
		var result = syncCallService(url,content);
		if(result.isSuccess){
			var studentTreeData = eval("("+result.data.studentTree+")");
			$(studentTree).tree({
				data:studentTreeData
			});
		}else{
			$.messager.alert('提示',result.message,"error");
		}
	};
	
	$(pointNameList).datagrid({
		singleSelect:true,
		method:"POST",
		nowrap:true,
		striped: true,
		collapsible:true,
		rownumbers:true,
		fit:true,
		toolbar:[
		         {text:'登记',iconCls:'icon-add',handler:function(){pointNameAdd();}},
		         {text:'编辑',iconCls:'icon-edit',handler:function(){pointNameUpdate();}},
		         {text:'删除',iconCls:'icon-remove',handler:function(){pointNameDelete();}},'-',
		         {text:'退出',iconCls:'icon-cancel',
		        	 handler:function(){
		        		 $(pointNameDialog).dialog('close');
		        	 }
		         }
		         ],
		columns:[[
			{field:'studentId',title:'学号',width:100,align:"center"},
			{field:'studentName',title:'姓名',width:100,align:"center"},
			{field:'scStatus',title:'选班状态',width:100,align:"center",
				formatter:function(value,row,index){
					if(value==1){
						return "正常";
					}else if(value==2){
						return "插班";
					}else if(value==3){
						return "转班";
					}else if(value==4){
						return "退学";
					}else if(value==5){
						return "休学";
					}else if(value==6){
						return "弃学";
					}
				}
			},
			{field:'lessons',title:'课时',width:50,align:"center"},
			{field:'status',title:'出勤状态',width:100,align:"center",
				formatter:function(value,row,index){
					if(value==0){
						return "正常";
					}else if(value==1){
						return "迟到";
					}else if(value==2){
						return "早退";
					}else if(value==3){
						return "旷课";
					}else if(value==4){
						return "请假";
					}
				}
			},
//			{field:'comeTime',title:'签到',width:100,align:"center"},
//			{field:'goTime',title:'签退',width:100,align:"center"},
			{field:'note',title:'课堂表现',width:100,align:"center"},
			{field:'showProgress',title:'个人课时进度',width:150,align:"center",
				formatter:function(value,row,index){
					return getBar(row.courseProgress,row.maxLessons);
				}
			}
		]],
		onClickRow:function(rowIndex, rowData){
			pointNameRow = rowData;
			pointNameIndex = rowIndex;
		},
		onLoadSuccess:function(){
			pointNameRow = null;
			pointNameIndex = null;
			pointNameNumber = 1;
		}
	});
	
	$(savePointDialog).dialog({
		title:'编辑出勤信息',  
	    width:500,
	    height:200,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    toolbar:[{
			text:'保存',
			iconCls:'icon-save',
			handler:function(){
				pointNameSave();
			}
		},'-',{
			text:'退出',
			iconCls:'icon-cancel',
			handler:function(){
				$(savePointDialog).dialog('close');
			}
		}]
	});
	
	//点名登记
	var pointNameAdd = function(){
		var nodes = $(studentTree).tree('getChecked');
		if(nodes.length==0){
			$.messager.alert('提示','请在学员名单勾选要登记的学员',"warning");
			return;
		}
		$(savePointForm).form('clear');
		isAdd = true;
		$('#lessonDegreeId',savePointForm).val(selectRow.lessonDegreeId);
		$('#classId',savePointForm).val(selectRow.classId);
		$('#lessons',savePointForm).val(selectRow.lessons);
		$('#lessonType',savePointForm).val(selectRow.lessonType);
		$('#lessonStatus',savePointForm).val(selectRow.lessonStatus);
		$(savePointDialog).dialog({title:'批量考勤'});
		$(savePointDialog).dialog('open');
	};
	
	var pointNameUpdate = function(){
		if(pointNameRow==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		}
		$(savePointForm).form('clear');
		isAdd = false;
		$(savePointForm).form('load',pointNameRow);
		$(savePointDialog).dialog('open');
	};
	
	//保存学员出勤表
	var pointNameSave = function(){
		var url = 'academic/saveAttend.do';
		
		var targetList = new Array();
		if(isAdd){
			var nodes = $(studentTree).tree('getChecked');
			var idList = new Array();
			for(var i=0;i<nodes.length;i++){
				idList.push(nodes[i].id);
				targetList.push(nodes[i].target);
			}
			var idStr = idList.join(',');
			url = 'academic/batchSaveAttend.do?idStr='+idStr;
		}
		
		$(savePointForm).form('submit',{
			url:url,
			onSubmit: function(){
				var status = $('input:radio[name="status"]:checked',savePointForm).val();
				if(status==null){
					$.messager.alert('提示','请选择出勤状态',"warning");
					return false;
				}
				return true;
			},
			success: function(data){
				var result = eval('('+data+')');
				if(result.isSuccess){
					var fn = function(){
						pointNameSearch(true);
					};
					$.messager.alert('提示','保存成功','info',fn);
					
					for(var i=0;i<targetList.length;i++){
						$(studentTree).tree('remove',targetList[i]);
					}
					$(savePointDialog).dialog('close');
				}else{
					$.messager.alert('提示',result.message,"error");
				}
			}
		});
	};
	
	var pointNameSearch = function(flag){
		var content = {'lessonDegree.lessonDegreeId':selectRow.lessonDegreeId,
				page:pointNameNumber,rows:pointNameSize};
		//取得列表信息
		var url = 'academic/queryAttend.do';
		var result = syncCallService(url,content);
		if(result.isSuccess){
			var  data = result.data;
			var datagridData = eval("("+data.datagridData+")");
			$(pointNameList).datagrid('loadData',datagridData);
			//需要重新重新分页信息
			if(flag){
				pointNameGetTotal(content);
			}
		}else{
			$.messager.alert('提示',result.message,'error');
		}
	};
	
	var pointNameGetTotal = function(content){
		var url = "academic/getTotalCountAttend.do";
		asyncCallService(url,content,
		function(result){
			if(result.isSuccess){
				var data = result.data;
				$(pointNamePager).pagination({  
					pageNumber:1,
					total:data.total
				});
			}else{
				$.messager.alert('提示',result.message,"error");
			}
		});
	};

	var pointNameDelete = function(){
		if(pointNameRow==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		}
		$.messager.confirm('确认', '确定删除该记录吗?', function(r){
			if(r){
				var url = "academic/deleteAttend.do";
				var content = {attendId:pointNameRow.attendId};
				$.post(url,content,
					function(result){
						if(result.isSuccess){
							pointNameSearch(true);
							getStudentTree();
						}else{
							$.messager.alert('提示',result.message,"error");
						}
				},'json');
			}
		});
	};
  };
})(jQuery);   