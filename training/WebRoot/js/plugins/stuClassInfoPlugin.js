// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.stuClassInfoInit = function() {
	var $this = $(this);
	var id = $(this).attr('id');
	
	var height = $(document.body).height();
	
	var stuClassInfoList = $('#stuClassInfoList',$this);
		
	var stuClassInfoRow = null;
	var stuClassInfoIndex = null;
		
	var browseDialog = $('#browseDialog',$this);
	var browseForm = $('#browseForm',browseDialog);
	
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
	$(stuClassInfoList).datagrid({
		singleSelect:true,
		method:"POST",
		nowrap:true,
		striped: true,
		collapsible:true,
		rownumbers:true,
		fit:true,
		toolbar:[	
			{text:'查询',iconCls:'icon-search',handler:function(){stuClassInfoSearch();}},'-',
			{text:'浏览',iconCls:'icon-view',handler:function(){stuClassInfoBrowse();}}
		],
		columns:[[
			{field:'stuClassId',title:'选班号',width:50,align:"center"},
			{field:'classId',title:'班号',width:50,align:"center"},
			{field:'className',title:'班级名称',width:150,align:"center"},
			{field:'lessonSchoolName',title:'上课地点',width:100,align:"center"},
			{field:'selectDate',title:'选班日期',width:100,align:"center"},
			{field:'scStatus',title:'选班状态',width:100,align:"center",
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
			{field:'courseProgress',title:'课时进度',width:150,align:"center",
				formatter: function(value,row,index){
					return getBar(row.courseProgress,row.lessons);
				}},
			{field:'selectSchoolName',title:'报名点',width:100,align:"center"}
		]],
		onClickRow:function(rowIndex, rowData){
			stuClassInfoRow = rowData;
			stuClassInfoIndex = rowIndex;
		},
		onLoadSuccess:function(){
			stuClassInfoRow = null;
			stuClassInfoIndex = null;
		}
	});
	
	//查询
	var stuClassInfoSearch = function(){
		var studentId = $('#studentId',$this).val();
		var url = 'student/queryStuClass.do';
		var content = {'student.studentId':studentId};
		var result = syncCallService(url,content);
		if(result.isSuccess){
			var  data = result.data;
			var datagridData = eval("("+data.datagridData+")");
			$(stuClassInfoList).datagrid('loadData',datagridData);
		}else{
			$.messager.alert('提示',result.message,'error');
		}
	};
	
	//浏览对话框
	$(browseDialog).dialog({  
	    title: '浏览',  
	    width:535,
	    height:height-10,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    toolbar:[{
			text:'退出',
			iconCls:'icon-cancel',
			handler:function(){
				$(browseDialog).dialog('close');
			}
		}]
	});
	
	$(stuClassPanel).panel({
	    width:500,  
	    height:300,  
	    title: '基本信息'
	});
	
	$(lessonPanel).panel({
	    width:500,  
	    height:300,  
	    title: '上课信息'
	});
	
	var stuClassInfoBrowse = function(){
		if(stuClassInfoRow==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		}
		
		$(browseForm).form('clear');
		$(browseForm).form('load',stuClassInfoRow);
		
		var scStatus = ['正常','插班','转班','休学','退学','弃学'];
		$('#scStatus',stuClassPanel).val(scStatus[stuClassInfoRow.scStatus-1]);
		$('#showProgress',lessonPanel).html(getBar(stuClassInfoRow.courseProgress,stuClassInfoRow.lessons));
		
		$(browseDialog).dialog('open');
	};
  };
})(jQuery);  