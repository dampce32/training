$(function(){
	var url = '../system/getReportAllReportConfig.do';
	var content ={};
	asyncCallService(url,content,function(result){
		if(result.isSuccess){
			 $(".hadAddPrint").remove();
			var data = result.data;
			var reportConfigData = eval("("+data.reportConfigData+")");
			currReportData = reportConfigData;
			var appendStr = "";
			
			for ( var i = 0; i <reportConfigData.length; i++) {
				if(i%6==0){
					appendStr += "<tr class = \"hadAdd\" style=\"margin-top:6px\">";
				}
				appendStr +="<td style=\"padding:6px\">"+
					 			"<a href=\"#\" class=\"easyui-linkbutton\" reportCode =\""+reportConfigData[i]["reportCode"]+"\" id=\""+reportConfigData[i]["reportConfigId"]+"\"  style=\"cursor: pointer;\" value=\""+reportConfigData[i]["reportName"]+"\">"+reportConfigData[i]["reportName"]+"</a>"+
					 		"</td>";
				if(i+1%6==0){
					appendStr +="</tr>";
				}
			}
			 if ("" != appendStr) {
				   $(appendStr).insertAfter("#reportTable");
			   }
			 $('.easyui-linkbutton').linkbutton({  
				    iconCls: 'icon-search'  
			}); 
			  
			 $('.easyui-linkbutton').bind('click', function(){  
				 var id =  $(this).attr('id');
				 var text =  $(this).attr('value');
				 var reportCode =  $(this).attr('reportCode');
				 //取得该报表对应的参数列表
				 var url = 'designReport.jsp?reportCode='+reportCode+"&reportConfigId="+id;
				 window.open(url);
			 });
		}else{
			$.messager.alert('提示',result.message,'error');
		}
	});
	  
	//编辑框
	$(printDialog).dialog({  
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    toolbar:[{
			text:'打印预览',
			iconCls:'icon-print',
			handler:function(){
				onPrint();
			}
		},'-',{
			text:'退出',
			iconCls:'icon-cancel',
			handler:function(){
				$(printDialog).dialog('close');
			}
		}]
	});    
	//生成选择条件
	var createChoose = function(kind){
		if('@supplierId'==kind){
			var appendStr = "";
			appendStr += "<tr class = \"hadAddPrint\" style=\"padding-top:6px\">"+
				"<td style=\"padding:10px 16px\">供应商:</td>"+
				"<td>"+
					"<input type=\"text\" id=\"supplier\"  style=\"width: 250px\" size=\"40\" />"+
				"</td>"+
				"</tr>";
		    if ("" != appendStr) {
				$(appendStr).insertAfter("#printTable");
			}
		     
		   //供应商
			$('#supplier').combogrid({  
			    panelWidth:450,  
			    idField:'supplierId',  
			    textField:'supplierName',  
			    url:'dict/queryCombogridSupplier.do',  
			    columns:[[  
			        {field:'supplierCode',title:'供应商编号',width:120},
			        {field:'supplierName',title:'供应商名称',width:120}
			    ]]  
			});
		}else if('@txtBeginDate'==kind){
			var appendStr = "";
			appendStr += "<tr class = \"hadAddPrint\" style=\"padding-top:6px\">"+
					"<td style=\"padding:10px 16px\">日期:</td>"+
					"<td>"+
						"<input type=\"text\" id=\"txtBeginDate\"  style=\"width: 110px\" class=\"Wdate\" size=\"40\" onFocus=\"WdatePicker({dateFmt:'yyyy-MM-dd'});\" />"+
						"&nbsp;&nbsp;至&nbsp;&nbsp;<input type=\"text\" id=\"txtEndDate\"  style=\"width: 110px\" class=\"Wdate\" size=\"40\" onFocus=\"WdatePicker({dateFmt:'yyyy-MM-dd'});\" />"+
					"</td>"+
				"</tr>";
		    if ("" != appendStr) {
				$(appendStr).insertAfter("#printTable");
			}
		}else if('@warehouseId'==kind){
			var appendStr = "";
			appendStr += "<tr class = \"hadAddPrint\" style=\"padding-top:6px\">"+
					"<td style=\"padding:10px 16px\">存放仓库:</td>"+
					"<td>"+
						"<input type=\"text\" id=\"warehouse\"  style=\"width: 250px\" size=\"40\" />"+
					"</td>"+
				"</tr>";
		    if ("" != appendStr) {
				$(appendStr).insertAfter("#printTable");
			}
		    
		    //仓库
			$('#warehouse').combobox({
				valueField:'warehouseId',
				textField:'warehouseName',
				data:PSS.getWarehouseList()
			})
		}
	}
	//打印前检查值
	var onPrintValue = function(){
		if(currReportParamsData!=null){
			for ( var i = 0; i <currReportParamsData.length; i++) {
				if(currReportParamsData[i]["isNeedChoose"]==1){
					if(currReportParamsData[i]["paramCode"]=='@txtBeginDate'){
						var txtBeginDate = $('#txtBeginDate').val();
						if(''==txtBeginDate){
							$.messager.alert('提示','请选择开始日期','warning');
							return false;
						}
						var txtEndDate = $('#txtEndDate').val();
						if(''==txtEndDate){
							$.messager.alert('提示','请选择结束日期','warning');
							return false;
						}
						var diff = DateDiff(txtEndDate,txtBeginDate);
						if(diff<0){
							$.messager.alert('提示','结束日期必须大于等于开始日期','warning');
							return false;
						}
					}else if('@supplierId'==currReportParamsData[i]["paramCode"]){
						var supplierId = $('#supplier').combogrid('getValue');
						if(supplierId==''){
							$.messager.alert('提示','请选择供应商','warning');
							return false;
						}
					}else if('@warehouseId'==currReportParamsData[i]["paramCode"]){
						var warehouseId = $('#warehouse').combobox('getValue');
						if(warehouseId==''){
							$.messager.alert('提示','请选择仓库','warning');
							return false;
						}
					}
				}
			 }
		}
		return true;
	}
	//打印预览
	var onPrint = function(){
		if(onPrintValue()){
			var url = "printReport.jsp?report="+currReportCode+"&data=ReportServlet";
			if(currReportParamsData!=null){
				for ( var i = 0; i <currReportParamsData.length; i++) {
					if(currReportParamsData[i]["paramCode"]=='@txtBeginDate'){
						var txtBeginDate = $('#txtBeginDate').val();
						var txtEndDate = $('#txtEndDate').val();
						if(i==0){
							url += "?txtBeginDate="+txtBeginDate+LYS.join+"txtEndDate="+txtEndDate;
						}else{
							url += LYS.join+"txtBeginDate="+txtBeginDate+LYS.join+"txtEndDate="+txtEndDate;
						}
					}else if(currReportParamsData[i]["paramCode"]=='@supplierId'){
						var supplierId = $('#supplier').combogrid('getValue');
						if(i==0){
							url += "?supplierId="+supplierId;
						}else{
							url += LYS.join+"supplierId="+supplierId;
						}
					}else if(currReportParamsData[i]["paramCode"]=='@warehouseId'){
						var warehouseId = $('#warehouse').combobox('getValue');
						if(i==0){
							url += "?warehouseId="+warehouseId;
						}else{
							url +=  LYS.join+"warehouseId="+warehouseId;
						}
					}
				}
				window.open(url);
			}
		}
	}
})	
