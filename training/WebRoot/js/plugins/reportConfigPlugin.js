// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.reportConfigInit = function() {
	  var $this = $(this);
	  var id = $(this).attr('id');
	  var width = $(document.body).width();
	  var height = $(document.body).height();
	  
	  var queryContent = $('#queryContent',$this);
	  var searchBtn = $('#searchBtn',$this);
	 
	  var editDialog = $('#editDialog',$this);
	  var editForm = $('#editForm',editDialog);
	  
	  var viewList =  $('#viewList',$this);
	  var selectRow = null;
	  var selectIndex = null;
	  
	  var pager = $('#pager',$this);
	  var pageNumber = 1;
	  var pageSize = 10;
	  
	  //列表
	  $(viewList).datagrid({
		  singleSelect:true,
		  fit:true,
		  columns:[[
			    {field:'reportKind',title:'报表类型',width:120,align:"center",formatter: function(value,row,index){
					if (value==0){
						return '模块报表';
					} else {
						return '统计报表';
					}
				}},
			    {field:'reportCode',title:'报表编号',width:120,align:"center"},
				{field:'reportName',title:'报表名称',width:200,align:"center"}
		  ]],
		  rownumbers:true,
		  pagination:false,
		  toolbar:[	
				{text:'添加',iconCls:'icon-add',handler:function(){onAdd()}},'-',
				{text:'修改',iconCls:'icon-edit',handler:function(){onUpdate()}},'-',
				{text:'删除',iconCls:'icon-remove',handler:function(){onDelete()}},'-',
				{text:'复制',iconCls:'icon-copy',handler:function(){onCopy()}}
		  ],
		  onDblClickRow:function(rowIndex, rowData){
				onUpdate();
		  },
		  onClickRow:function(rowIndex, rowData){
				selectRow = rowData;
				selectIndex = rowIndex;
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
	})
	//分页操作
	var search = function(flag){
		var reportCode = $('#reportCodeSearch',queryContent).val();
		var reportName = $('#reportNameSearch',queryContent).val();
		
		var url = "system/queryReportConfig.do";
		var content = {reportCode:reportCode,reportName:reportName,page:pageNumber,rows:pageSize};
		var result = syncCallService(url,content);
		if(result.isSuccess){
			var data = result.data;
			$(viewList).datagrid('loadData',eval("("+data.datagridData+")"));
			//需要重新重新分页信息
			if(flag){
				getTotal(content);
			}
		}else{
			$.messager.alert('提示',result.message,"error");
		}
	} 
	//统计总数
	var getTotal = function(content){
		var url = "system/getTotalCountReportConfig.do";
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
		
	//编辑框
	$(editDialog).dialog({  
	    title: '编辑报表配置',  
	    width:600,
	    height:height-10,
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
	//添加
	var onAdd = function(){
		deleteIdArray = new Array();
		$(editForm).form('clear');
		$(editDialog).dialog('open');
	}
	//保存前的赋值操作
	var setValue = function(){
		var reportKind= $.trim($('#reportKind',editForm).combobox('getValue'));
		if(reportKind==''){
			$.messager.alert('提示','请选择报表类型','warning');
			return false;
		}
		var reportCode= $.trim($('#reportCode',editForm).val());
		if(reportCode==''){
			$.messager.alert('提示','请填写报表编号','warning');
			return false;
		}
		var reportName = $.trim($('#reportName',editForm).val());
		if(reportName==''){
			$.messager.alert('提示','请填写报表名称','warning');
			return false;
		}
		var reportDetailSql = $.trim($('#reportDetailSql',editForm).val());
		if(reportDetailSql==''){
			$.messager.alert('提示','请填写报表明细网格Sql','warning');
			return false;
		}
		//验证添加的商品行
		$(reportParamConfigList).datagrid('endEdit', lastIndex);
		$(reportParamConfigList).datagrid('unselectAll');
		lastIndex = null;
		var reportParamConfigIdArray = new Array();
		var reportParamIdArray = new Array();
		var isNeedChooseArray = new Array();
		var rows = $(reportParamConfigList).datagrid('getRows');
		for ( var i = 0; i < rows.length; i++) {
			reportParamConfigIdArray.push(rows[i].reportParamConfigId);
			reportParamIdArray.push(rows[i].reportParamId);
			isNeedChooseArray.push(rows[i].isNeedChoose);
		}
		$('#reportParamConfigIds',editForm).val(reportParamConfigIdArray.join(CSIT.join));
		$('#deleteIds',editForm).val(deleteIdArray.join(CSIT.join));
		$('#reportParamIds',editForm).val(reportParamIdArray.join(CSIT.join));
		$('#isNeedChooses',editForm).val(isNeedChooseArray.join(CSIT.join));
		
		$(editDialog).mask({maskMsg:'正在保存'});
		return true;
	}
	//保存
	var onSave = function(){
		var url = 'system/saveReportConfig.do'
		$(editForm).form('submit',{
			url: url,
			onSubmit: function(){
				return setValue();
			},
			success: function(data){
				$(editDialog).mask('hide');
				var result = eval('('+data+')');
				if(result.isSuccess){
					var fn = function(){
						var reportConfigId = $.trim($('#reportConfigId',editForm).val());
						if(reportConfigId==''){//新增
							search(true);
						}else{
							var row = $(editForm).serializeObject();
							$(viewList).datagrid('updateRow',{index:selectIndex,row:row});	
						}
						$(editDialog).dialog('close');
						$(editForm).form('clear');
					}
					$.messager.alert('提示','保存成功','info',fn);
					
				}else{
					$.messager.alert('提示',result.message,'error');
				}
			}
		 });
	}
	//打开
	var onOpen = function(reportConfigId){
		var url = 'system/initReportConfig.do';
		var content ={reportConfigId:reportConfigId};
		asyncCallService(url,content,function(result){
			if(result.isSuccess){
				var data = result.data;
				var reportConfigData = eval("("+data.reportConfigData+")");
				$('#reportConfigId',editDialog).val(reportConfigData.reportConfigId);
				$('#reportCode',editDialog).val(reportConfigData.reportCode);
				$('#reportName',editDialog).val(reportConfigData.reportName);
				
				$('#reportKind',editDialog).combobox('setValue',reportConfigData.reportKind);
				$('#reportParamsSql',editDialog).val(reportConfigData.reportParamsSql);
				$('#reportDetailSql',editDialog).val(reportConfigData.reportDetailSql);
				
				var detailData = eval("("+data.detailData+")");
				$(reportParamConfigList).datagrid('loadData',detailData);
			}else{
				$.messager.alert('提示',result.message,'error');
			}
		});
	}
	//修改
	var onUpdate = function(){
		if(selectRow==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		}
		$(editForm).form('clear');
		onOpen(selectRow.reportConfigId);
		deleteIdArray = new Array();
		$(editDialog).dialog('open');
	 }
	//复制
	var onCopy = function(){
		if(selectRow==null){
			$.messager.alert("提示","请选择数据行","warning");
			return;
		}
		$(editForm).form('clear');
		var url = 'system/initReportConfig.do';
		var content ={reportConfigId:selectRow.reportConfigId};
		asyncCallService(url,content,function(result){
			if(result.isSuccess){
				var data = result.data;
				var reportConfigData = eval("("+data.reportConfigData+")");
				$('#reportCode',editDialog).val(reportConfigData.reportCode);
				$('#reportName',editDialog).val(reportConfigData.reportName);
				
				$('#reportKind',editDialog).combobox('setValue',reportConfigData.reportKind);
				$('#reportParamsSql',editDialog).val(reportConfigData.reportParamsSql);
				$('#reportDetailSql',editDialog).val(reportConfigData.reportDetailSql);
				
				var detailData = eval("("+data.detailData+")");
				$(reportParamConfigList).datagrid('loadData',detailData);
			}else{
				$.messager.alert('提示',result.message,'error');
			}
		});
		deleteIdArray = new Array();
		$(editDialog).dialog('open');
	}

	//删除
	var onDelete = function(){
		if(selectRow==null){
			 $.messager.alert('提示',"请选中要删除的纪录","warming");
			 return;	
		}
		$.messager.confirm("提示","确定要删除选中的记录?",function(t){ 
			if(t){
				var url = 'system/deleteReportConfig.do';
				var content ={reportConfigId:selectRow.reportConfigId};
				asyncCallService(url,content,function(result){
					if(result.isSuccess){
						var fn = function(){
							search(true);
						}
						$.messager.alert('提示','删除成功','info',fn);
					}else{
						$.messager.alert('提示',result.message,'error');
					}
				});
			}
		});
	}
	//-------报表参数配置--------
	var reportParamConfigList = $('#reportParamConfigList',editDialog);
	var lastIndex = null;
	var isNeedChooses = [
	 	    		    {isNeedChoose:'0',name:'否'},
	 	    		    {isNeedChoose:'1',name:'是'}
	 	];
	 	function isNeedChooseFormatter(value){
	 		for(var i=0; i<isNeedChooses.length; i++){
	 			if (isNeedChooses[i].isNeedChoose == value) return isNeedChooses[i].name;
	 		}
	 		return value;
	 	}
	var deleteIdArray = null;//修改情况下，删除掉的报表参数配置
	$(reportParamConfigList).datagrid({
		  singleSelect:true,	
		  fit:true,
		  columns:[[
		        {field:'paramName',title:'参数名称',width:180,align:"center"},
			    {field:'paramCode',title:'参数编号',width:180,align:"center"},
			    {field:'isNeedChoose',title:'是否必须',width:90,align:"center",editor:{type:'combobox',options:{valueField:'isNeedChoose',
					textField:'name',
					data:isNeedChooses}},formatter:isNeedChooseFormatter}
		  ]],
		  rownumbers:true,
		  pagination:false,
		  toolbar:[	
				{id:'addReportParamConfig'+id,text:'添加参数',iconCls:'icon-add',handler:function(){onSelectReportParamConfig()}},'-',
				{id:'deleteReportParamConfig'+id,text:'删除参数',iconCls:'icon-remove',handler:function(){onDeleteReportParamConfig()}},'-',
				{id:'moveUp'+id,text:'上移',iconCls:'icon-up',handler:function(){onMove(-1)}},'-',
				{id:'moveUp'+id,text:'下移',iconCls:'icon-down',handler:function(){onMove(1)}},'-'
		  ],
		  onBeforeLoad:function(){
				$(this).datagrid('rejectChanges');
		  },
		  onClickRow:function(rowIndex){
			if (lastIndex != rowIndex){
				$(reportParamConfigList).datagrid('endEdit', lastIndex);
				$(reportParamConfigList).datagrid('beginEdit', rowIndex);
			}
			lastIndex = rowIndex;
		  }
		 });
	//----选择报表参数------
	var selectDialog = $('#selectDialog',$this);
	var reportParamList = $('#reportParamList',$this);
	//编辑框
	$(selectDialog).dialog({  
	    title: '选择报表参数',  
	    width:600,
	    height:500,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false
	});
	$(reportParamList).datagrid({
		  fit:true,
		  cache: false, 
		  columns:[[
			    {field:'ck',checkbox:true},
			    {field:'paramName',title:'报表参数名称',width:150,align:"center"},
			    {field:'paramCode',title:'报表参数编号',width:150,align:"center"}
		  ]],
		  rownumbers:true,
		  pagination:true,
		  toolbar:[	
				{text:'选择',iconCls:'icon-ok',handler:function(){onSelectOKReportParam()}},
				{text:'退出',iconCls:'icon-cancel',handler:function(){
					onExitSelectReportParam();
				}}
		  ]
	 });
	 var onSelectReportParamConfig = function(){
		 $(selectDialog).dialog('open');
		 deleteIdArray = new Array();
	 }
	 //查询
	 $('#searchBtnSelectDialog',selectDialog).click(function(){
		 searchBtnSelect();
	 })
	 var searchBtnSelect = function(){
		var paramCode = $('#paramCodeSelectDialog',selectDialog).val();
		
		var rows = $(reportParamConfigList).datagrid('getRows');
		var reportParamArray = new Array();
		for ( var i = 0; i < rows.length; i++) {
			reportParamArray.push(rows[i].reportParamId);
		}
		
		var url = "system/selectReportParam.do";
		var content = {paramCode:paramCode,ids:reportParamArray.join(CSIT.join)};
		var result = syncCallService(url,content);
		if(result.isSuccess){
			var data = result.data;
			$(reportParamList).datagrid('loadData',eval("("+data.datagridData+")"));
		}else{
			$.messager.alert('提示',result.message,"error");
		}
	 }
	 //选择报表参数
	 var onSelectOKReportParam = function(){
		 var rows = $(reportParamList).datagrid('getSelections');
		 if(rows.length==0){
			 $.messager.alert('提示','请选择报表参数',"warning");
			 return;
		 }
		 for ( var i = 0; i < rows.length; i++) {
			var row = rows[i];
			 $(reportParamConfigList).datagrid('appendRow',{
				 reportParamConfigId:'',
				 reportParamId:row.reportParamId,
				 paramCode:row.paramCode,
				 paramName:row.paramName,
				 isNeedChoose:0,
				 array:''
			});
		}
	    $(reportParamConfigList).datagrid('endEdit', lastIndex);
	    $(reportParamConfigList).datagrid('unselectAll');
		lastIndex = null;
		onExitSelectReportParam();
	 }
	 //退出选择报表参数界面
	 var onExitSelectReportParam = function(){
		 $(selectDialog).dialog('close');
		 $(reportParamList).datagrid({url:CSIT.ClearUrl});
	 }
	 //删除报表参数
	 var onDeleteReportParamConfig = function(){
		 var row = $(reportParamConfigList).datagrid('getSelected');
		 if(row.reportParamConfigId!=''){
			 deleteIdArray.push(row.reportParamConfigId);
		 }
		 var rowIndex = $(reportParamConfigList).datagrid('getRowIndex',row);
		 $(reportParamConfigList).datagrid('deleteRow',rowIndex);
		 lastIndex = null;
	 }
	//移动
	var onMove = function(direction){
			var rows  = $(reportParamConfigList).datagrid('getRows');
			var selectRow = $(reportParamConfigList).datagrid('getSelected');
			var selectIndex = $(reportParamConfigList).datagrid('getRowIndex',selectRow);
			if(direction==-1){
				if(selectIndex==0){
					$.messager.alert('提示',"已经是第一条记录了","warming");
					return;
				}
			}else if(direction==1){//下移
				var rows  = $(reportParamConfigList).datagrid('getRows');
				if(selectIndex==rows.length-1){
					$.messager.alert('提示',"已经是最后一条记录了","warming");
					return;
				}
			}
			var updateRow = rows[selectIndex+direction];
			var reportParamConfigId = selectRow.reportParamConfigId;
			var reportParamId = selectRow.reportParamId;
			var paramCode = selectRow.paramCode;
			var paramName = selectRow.paramName;
			var isNeedChoose = selectRow.isNeedChoose;
			var array = selectRow.array;
			
			$(reportParamConfigList).datagrid('updateRow',{
				index: selectIndex,
				row: updateRow
			});
			$(reportParamConfigList).datagrid('updateRow',{
				index: selectIndex+direction,
				row: {
					reportParamConfigId:reportParamConfigId,
					reportParamId:reportParamId,
					paramCode:paramCode,
					paramName:paramName,
					isNeedChoose:isNeedChoose,
					array:array
				}
			});
			$(reportParamConfigList).datagrid('selectRow',selectIndex+direction);
			lastIndex = selectIndex+direction;
		}
  }
})(jQuery);   