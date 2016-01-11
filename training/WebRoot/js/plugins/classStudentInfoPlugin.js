// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.classStudentInfoInit = function() {
	  var $this = $(this);
	  var id = $(this).attr('id');
	  
	  var viewList = $('#viewList',$this);
	  var selectRow = null;
	  var selectIndex = null;
	  
	  var pager = $('#pager',$this);
	  var pageNumber = 1;
	  var pageSize = 10;
	  
	  var getBar = function(left,right){
		  var bar = '<div style="width:80px;height:12px;background:#fff;border:1px solid #ccc;text-align:left;float:left;">' +
			  '<div style="width:' + left*100/right + '%;height:100%;background:blue"></div>'+
			  '</div>';
	   	  var number = '<span style="width:50px;margin-left:5px;text-align:left;">'+
			  '<span style="color: red;">'+left+'</span>/'+right+
			  '</span>';
		  return bar+number;
	  };
	  
	//列表
	$(viewList).datagrid({
		singleSelect:true,
		method:"POST",
		nowrap:true,
		striped: true,
		collapsible:true,
		rownumbers:true,
		fit:true,
		toolbar:[	
//			{text:'控制面板',iconCls:'icon-remove',handler:function(){}}
			{text:'打印',iconCls:'icon-print',handler:function(){onPrint();}}
		],
		columns:[[
			{field:'studentId',title:'学号',width:50,align:"center"},
			{field:'studentName',title:'姓名',width:50,align:"center"},
			{field:'scStatus',title:'选班状态',width:150,align:"center",
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
			{field:'continueReg',title:'类型',width:100,align:"center",
				formatter:function(value,row,index){
					if(value==0){
						return '试听';
					}else if(value==1){
						return '新增';
					}else if(value==2){
						return '续报';
					}
				}
			},
			{field:'selectDate',title:'选班日期',width:100,align:"center"},
			{field:'showCourseProgress',title:'课时进度',width:100,align:"center",
				formatter:function(value,row,index){
					getBar(row.courseProgress,row.lessons);
				}
			},
			{field:' ',title:'回访',width:100,align:"center"}
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
	var search = function(flag){
//		var classroomName = $('#classroomNameSearch',queryContent).val();
//		var seating = $('#seatingSearch',queryContent).val();
		var classId = $('#classSearch',$this).val();
		var content = {'clazz.classId':classId,page:pageNumber,rows:pageSize};
		//取得列表信息
		var url = 'stuClass/queryStuClass.do';
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
		var url = "stuClass/getTotalCountStuClass.do";
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
	
	//打印 
	var onPrint = function(){
		window.open("printReport.jsp?report=classStudentInfo&data=ReportServlet?classId="+selectRow.classId);
	};
  };
})(jQuery);  
