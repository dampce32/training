// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.myProductInit = function() {
	  var $this = $(this);
	  var id = $(this).attr('id');
	  var width = $(document.body).width();
	  var height = $(document.body).height();
	  var searchBtn = $('#searchBtn',$this);
	  var queryContent = $('#queryContent',$this);
	  var viewList = $('#viewList',$this);
	  var ReturnCourse = $('#ReturnCourse',$this);
	  var selectRow = null;
	  var selectIndex = null;
	  var selectRowReturnCourse = null;
	  var selectIndexReturnCourse = null;
	  var pageNumber = 1;
	  var pageSize = 10;
	  var editReturnCourseForm = $('#editReturnCourseForm',$this);
	  var editReturnCourseDialog = $('#editReturnCourseDialog',$this);
	  
	  var selectClassTypeDialog = $('#selectClassTypeDialog',$this);
		
	  var selectedDialog = $('#selectedDialog',$this);
	  var selectedForm = $('#selectedForm',$this);
	
	  var selectedClassInfoPanel = $('#selectedClassInfoPanel',selectedDialog);
	  var selectedClassInfoForm= $('#selectedClassInfoForm',selectedDialog);
	
	  var oneToOneDialog = $('#oneToOneDialog',$this);
	  var oneToOneForm = $('#oneToOneForm',$this);
	
	  var oneToManyDialog = $('#oneToManyDialog',$this);
	  var oneToManyForm = $('#oneToManyForm',$this);
	
	  var classInfoPanel = $('#classInfoPanel',oneToManyDialog);
	  var classInfoForm = $('#classInfoForm',oneToManyDialog);
	
	  var classInfo = null;
		
	  //保留小数点后2位
	  var toDecimal =function(x) {  
            var f = parseFloat(x);  
            if (isNaN(f)) {  
                return false;  
            }  
            var f = Math.round(x*100)/100;  
            var s = f.toString();  
            var rs = s.indexOf('.');  
            if (rs < 0) {  
                rs = s.length;  
                s += '.';  
            }  
            while (s.length <= rs + 2) {  
                s += '0';  
            }  
            return s;  
        };
	//加载列表
	  $(viewList).datagrid({
		method:"POST",
		singleSelect:false,
		nowrap:true,
		striped: true,
		collapsible:true,
		rownumbers:true,
		pagination:true,
		fit:true,
		toolbar:[	
		         	{text:'选班',iconCls:'icon-edit',handler:function(){selectClassType();}},
					{text:'退货办理',iconCls:'icon-add',handler:function(){onReturnCourse();}}
				],
		columns:[[  {field:'ck',title:'选择',checkbox:true},
					{field:'billDetailId',hidden:true},
					{field:'billItemName',title:'项目',width:100,align:"center"},
					{field:'billDate',title:'日期',width:100,align:"center"},
					{field:'billCode',title:'单号',width:100,align:"center"},
					{field:'price',title:'价格',width:100,align:"center"},
					{field:'qty',title:'数量',width:50,align:"center"},
					{field:'returnQty',title:'已退数量',width:100,align:"center"},
					{field:'unitName',title:'单位',width:50,align:"center"},
					{field:'discountAmount',title:'优惠',width:100,align:"center"},
					{field:'payed',title:'合计',width:100,align:"center"},
					{field:'status',title:'状态',width:100,align:"center",
							formatter: function(value,row,index){
										if (value==0){
											return '<span style="color:red">未处理</span>';
										} else if (value==1){
											return '<span style="color:green">已处理</span>';
										}else if (value==2){
											return '已退';
										}else if (value==3){
											return '--';
										}
				    }},
				    {field:'warehouseName',title:'仓库',width:150,align:"center"}
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
	$(viewList).datagrid('getPager').pagination({   
	    onSelectPage: function(page, rows){
			pageNumber = page;
			pageSize = rows;
			search();
	    }
	});
	//查询
	var search = function(flag){
		var studentId=$('#studentId',$this).val();
		var billDateBegin = $('#billDateBeginSearch',queryContent).val();
		var billDateEnd = $('#billDateEndSearch',queryContent).val();
		var content = {'bill.student.studentId':studentId,billDateBegin:billDateBegin,billDateEnd:billDateEnd,page:pageNumber,rows:pageSize};
		//取得列表信息
		var url = 'customerService/queryDetail.do';
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
		var url = "customerService/getTotalCountDetail.do";
		asyncCallService(url,content,
		function(result){
			if(result.isSuccess){
				var data = result.data;
				$(viewList).datagrid('getPager').pagination({  
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
	$(editReturnCourseDialog).dialog({
		title:'退货办理',
	    width:1000,
	    height:height-40,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    toolbar:[
	    		{id:'saveReturnCourse',text:'确定退货',iconCls:'icon-save',handler:function(){onSaveReturnCourse();}},'-',
				{text:'退出',iconCls:'icon-cancel',handler:function(){
						$(editReturnCourseDialog).dialog('close');
					}
				}
	   ]
	});
	$(ReturnCourse).datagrid( {
		method:"POST",
		singleSelect:true,
		nowrap:true,
		striped: true,
		collapsible:true,
		rownumbers:true,
		fit:true,
		columns : [ [ {field : 'billItemId',hidden:true},
					  {field : 'billItemName',title : '收费项目',width : 100,align : 'center'}, 
					  {field : 'price',title : '单价',width : 100,align : 'center'}, 
					  {field : 'actualQty',title : '数量',width : 80,align : 'center',
							editor:{
								type:'numberbox'
							}},
					  {field : 'unitName',title : '单位',width : 80,align : 'center'},
					  {field:'totalPrice',title:'合计',width:100,align:"center",
							editor:{
								type:'numberbox',
								options:{
									disabled:true,
									precision:2,
									onChange:function(newValue,oldValue){
										var totalAmount = 0 ;
										var rows =  $(ReturnCourse).datagrid('getRows');
										var row = $(ReturnCourse).datagrid('getSelected');
										var rowIndex = $(ReturnCourse).datagrid('getRowIndex',row); 
										for ( var i = 0; i < rows.length; i++) {
											if(i!=rowIndex){
												row = rows[i];
												if(row.totalPrice==''||row.totalPrice==null){
													totalAmount +=0;
												}else{
													totalAmount += parseFloat(row.totalPrice);
												}
											};
										}
										totalAmount+=parseFloat(newValue);
							    		$('#pay',editReturnCourseForm).val(totalAmount);
									}
								}
							}},
					  {field : 'discount',title : '折扣',width : 50,align : "center",
								editor:{
									type:'numberbox',
									options:{
										max:10,
										min:0,
										precision:2
									}	
								}},
					  {field : 'discountAmount',title : '扣除',width : 100,align : 'center',
								editor:{
									type:'numberbox',
									options:{
										disabled:true,
										precision:2,
										onChange:function(newValue,oldValue){
											var totalDiscountAmount = 0 ;
											var rows =  $(ReturnCourse).datagrid('getRows');
											var row = $(ReturnCourse).datagrid('getSelected');
											var rowIndex = $(ReturnCourse).datagrid('getRowIndex',row); 
											for ( var i = 0; i < rows.length; i++) {
												if(i!=rowIndex){
													row = rows[i];
													if(row.discountAmount==''||row.discountAmount==null){
														totalDiscountAmount +=0;
													}else{
														totalDiscountAmount += parseFloat(row.discountAmount);
													}
												};
											}
											totalDiscountAmount+=parseFloat(newValue);
								    		$('#favourable',editReturnCourseForm).val(toDecimal(totalDiscountAmount));
										}
									}	
								}},
					  {field : 'payed',title : '金额',width : 100,align : 'center',
								editor:{
									type:'numberbox',
									options:{
										disabled:true,
										precision:2,
										onChange:function(newValue,oldValue){
											var totalPayed = 0 ;
											var rows =  $(ReturnCourse).datagrid('getRows');
											var row = $(ReturnCourse).datagrid('getSelected');
											var rowIndex = $(ReturnCourse).datagrid('getRowIndex',row); 
											for ( var i = 0; i < rows.length; i++) {
												if(i!=rowIndex){
													row = rows[i];
													if(row.payed==''||row.payed==null){
														totalPayed +=0;
													}else{
														totalPayed += parseFloat(row.payed);
													}
												};
											}
											totalPayed+=parseFloat(newValue);
								    		$('#payed',editReturnCourseForm).val(toDecimal(totalPayed));
								    	}
									}	
								}},
					  {field:'warehouseName',title:'仓库',width:150,align:"center"}
				 ] ],
		onClickRow:function(rowIndex, rowData){
				selectRowReturnCourse = rowData;
				if (selectIndexReturnCourse != rowIndex){
					$(this).datagrid('endEdit', selectIndexReturnCourse);
					$(this).datagrid('beginEdit', rowIndex);
					isEditing(rowIndex,rowData);
				}
				selectIndexReturnCourse = rowIndex;
		},
		onLoadSuccess:function(){
			selectRowReturnCourse = null;
	 		selectIndexReturnCourse = null;
			pageNumber = 1;
		}
	});
	function isEditing(rowIndex,rowData){  
		
	    var editors = $(ReturnCourse).datagrid('getEditors', rowIndex);
	    var qtyEditor = editors[0];
	    var totalPriceEditor = editors[1];
	    var discountEditor = editors[2]; 
	    var discountAmountEditor = editors[3]; 
	    var payedEditor = editors[4];
	    discountEditor.target.bind('change', function(){  
	        calculate(rowIndex);  
	    });
	    qtyEditor.target.bind('change', function(){  
	        calculate(rowIndex);  
	    });
	    function calculate(rowIndex){
	    	if(qtyEditor.target.val()==''){
	    		$(discountEditor.target).numberbox('setValue',1);
	    	}
	    	var cost = toDecimal(selectRowReturnCourse.price * qtyEditor.target.val());  
	        $(totalPriceEditor.target).numberbox('setValue',cost);
	    	if(discountEditor.target.val()==''){
	    		$(discountEditor.target).numberbox('setValue',10.00);
	    	}
	        var discountAmount = toDecimal(cost*(10-discountEditor.target.val())*0.1);  
	        $(discountAmountEditor.target).numberbox('setValue',discountAmount);
	        var payed=toDecimal(cost-discountAmount);
	        $(payedEditor.target).numberbox('setValue',payed);
	    }  
	} 
	var initReturnCourse = function(ids){
		var url = "customerService/initBill.do";
		var result = syncCallService(url,null);
		if(result.isSuccess){
			var data = result.data;
			$('#billCode',editReturnCourseForm).val(data.billCode);
		}else{
			$.messager.alert('提示',result.message,'error');
		}
		var url2 = 'customerService/queryProductDetail.do';
		var content={'billDetailIds':ids};
		var result = syncCallService(url2,content);
		if(result.isSuccess){
			var data = result.data;
			var datagridData = eval("("+data.datagridData+")");
			$(ReturnCourse).datagrid('loadData',datagridData);
		}else{
			$.messager.alert('提示',result.message,'error');
		}
		
		//办理校区
		$('#school',editReturnCourseForm).combobox({
			valueField:'schoolId',
			textField:'schoolName',
			width:250,
			data:TRN.getSelfSchoolList()
		});
		//经办人
		$('#user',editReturnCourseForm).combobox({
		  width:250,
		  data:TRN.getEmployeeList(),
		  valueField:'userId',
		  textField:'userName'
	  });
	};
	//退货办理
	var onReturnCourse = function(){
		$(editReturnCourseForm).form('clear');
		var rows =  $(viewList).datagrid('getSelections');
		if(rows.length==0){
			$.messager.alert("提示",'请选择数据行',"warning");
			return;
		}
		var billDetailIds = new Array();
		var pay=0;
		var favourable=0;
		var payed=0;
		for ( var i = 0; i < rows.length; i++) {
			var row=rows[i];
			if(row.status==1){
				$.messager.alert("提示","您所选中第"+(i+1)+"行数据已处理，不能退货","warning");
				return;
			}
			if(row.status==2){
				$.messager.alert("提示","您所选中第"+(i+1)+"行数据已退货，不能退货","warning");
				return;
			}
			pay+=parseFloat(row.totalPrice);
			favourable+=parseFloat(row.discountAmount);
			payed+=parseFloat(row.payed);
			billDetailIds.push(row.billDetailId);
		}
		var ids=billDetailIds.join(CSIT.join);
		$('#pay',editReturnCourseForm).val(pay);
		$('#favourable',editReturnCourseForm).val(favourable);
		$('#payed',editReturnCourseForm).val(payed);
		initReturnCourse(ids);
		$(editReturnCourseDialog).dialog('open');
	};
	var setValueReturnCourse = function(){
		var detail=new Array();
		//单号
		var billCode = $.trim($('#billCode',editReturnCourseForm).val());
		if(''==billCode){
			$.messager.alert('提示','对不起，单号为空','warning');
			return false;
		}
		//消费日期
		var billDate = $.trim($('#billDate',editReturnCourseForm).val());
		if(''==billDate){
			$.messager.alert('提示','请退货日期','warning');
			return false;
		}
		//办理校区
		var schoolId = $('#school',editReturnCourseForm).combobox('getValue') ;
		if(''==schoolId){
			$.messager.alert('提示','请选择办理校区','warning');
			return false;
		}
		if(isNaN(parseInt(schoolId))){
			$.messager.alert('提示','请选择提供选择的办理校区','warning');
			return false;
		}
		//经办人
		var userId = $('#user',editReturnCourseForm).combobox('getValue') ;
		if(''==userId){
			$.messager.alert('提示','请选择经办人','warning');
			return false;
		}
		if(isNaN(parseInt(userId))){
			$.messager.alert('提示','请选择提供选择的经办人','warning');
			return false;
		}
		$(ReturnCourse).datagrid('endEdit', selectIndexReturnCourse);
		$(ReturnCourse).datagrid('unselectAll');
		selectIndexReturnCourse=null;
		var rows =  $(ReturnCourse).datagrid('getRows');
		for ( var i = 0; i < rows.length; i++) {
			var row=rows[i];
			detail.push(row.billDetailId+","+row.actualQty+","+row.discount+","+row.discountAmount);
		}
		$('#billDetail',editReturnCourseForm).val(detail.join(CSIT.join));
		var studentId=$('#studentId',$this).val();
		$('#studentId',editReturnCourseForm).val(studentId);
		$('#billType',editReturnCourseForm).val(0);
		return true;
	};
	//保存退货办理
	var onSaveReturnCourse = function(){
		$(editReturnCourseForm).form('submit',{
			url: 'customerService/saveBill.do',
			onSubmit: function(){
				return setValueReturnCourse();
			},
			success: function(data){
				var result = eval('('+data+')');
				if(result.isSuccess){
					var fn = function(){
						search(true);
						$(editReturnCourseDialog).dialog('close');
					};
					$.messager.alert('提示','保存成功','info',fn);
				}else{
					$.messager.alert('提示',result.message,"error");
				}
			}
		});
	};
	
	//=====================================选班=======================================
	var getBar = function(left,right){
		  var bar = '<div style="width:80px;height:12px;background:#fff;border:1px solid #ccc;text-align:left;float:left;">' +
			'<div style="width:' + left*100/right + '%;height:100%;background:blue"></div>'+
			'</div>';
		  var number = '<span style="width:50px;margin-left:5px;text-align:left;">'+
			'<span style="color: red;">'+left+'</span>/'+right+
			'</span>';
		  return bar+number;
	  };
	
	//选择班级类型窗口
	$(selectClassTypeDialog).dialog({  
	    title: '班级类型',  
	    width:400,
	    height:250,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    toolbar:[{
			text:'选择',
			iconCls:'icon-ok',
			handler:function(){
				var value = $('input:radio[name="classType"]:checked',selectClassTypeDialog).val();
				if(value==0){
					selected();
				}else if(value==1){
					oneToOne();
				}else if(value==2){
					oneToMany();
				}
				$(selectClassTypeDialog).dialog('close');
			}
		},'-',{
			text:'退出',
			iconCls:'icon-cancel',
			handler:function(){
				$(selectClassTypeDialog).dialog('close');
			}
		}]
	});
	//班级类型
	var selectClassType = function(){
		if(selectRow==null){
			$.messager.alert('提示',"请选择数据行","warning");
			return;
		}
		if(selectRow.status!=0){
			$.messager.alert('提示',"请选择状态为\"未处理\"的数据行","warning");
			return;
		}
		if(selectRow.unitName=='课时'){
			//验证学生是否已选班
			var url = 'stuClass/selectedValidateStuClass.do';
			var content = {'student.studentId':selectRow.studentId};
			var result = syncCallService(url,content);
			if(result.isSuccess){
				var  data = result.data;
				if(data.existence==true){
					$('#selected',selectClassTypeDialog).attr('disabled',false);
				}else{
					$('#selected',selectClassTypeDialog).attr('disabled',true);
				}
			}else{
				$.messager.alert('提示',result.message,'error');
			}
			
			$('input:radio[name="classType"]',selectClassTypeDialog).attr("checked",false);
			$(selectClassTypeDialog).dialog('open');
		}else if(selectRow.unitName=='学期'){
			oneToMany();
		}
		
	};
	
	$(selectedDialog).dialog({  
	    title: '选择已选班级',  
	    width:600,
	    height:480,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    toolbar:[{
			text:'保存',
			iconCls:'icon-save',
			handler:function(){
				selectedSave();
				$(oneToOneDialog).dialog('close');
			}
		},'-',{
			text:'退出',
			iconCls:'icon-cancel',
			handler:function(){
				$(selectedDialog).dialog('close');
			}
		}]
	});
	
	var selectedCombobox = function(){
		$('#scStatus',selectedDialog).combobox({
			onSelect:function(record){
			    $('#class',selectedDialog).combobox({
				    data:TRN.getSelectedClassList(
				    		selectRow.courseId,
				    		selectRow.studentId,
				    		parseInt($('#scStatus',selectedDialog).combobox('getValue'))
				    		)
			    });
			}
		});
		$('#class',selectedDialog).combobox({
			data:TRN.getSelectedClassList(
					selectRow.courseId,
					selectRow.studentId,
					parseInt($('#scStatus',selectedDialog).combobox('getValue'))
					),
			width:200,
			valueField:'classId',
			textField:'className',
			onSelect:function(record){
				selectedClassInfoInit(record.classId);
			}
		});
		$('#user',selectedDialog).combobox({
			  width:200,
			  data:TRN.getEmployeeList(),
			  valueField:'userId',
			  textField:'userName'
		});
	};
	
	var selected = function(){
		$(selectedForm).form('clear');
		$('#scStatus',selectedDialog).combobox('setValue',1);
		selectedCombobox();
		$('#lessons',selectedForm).val(selectRow.qty);
		$('#showLessons',selectedDialog).val(selectRow.qty);
		$('#continueReg',selectedDialog).combobox('setValue',2);
		$(selectedDialog).dialog('open');
	};
	
	var selectedValidate = function(){
		var clazz = $('#class',selectedDialog).combobox('getValue');
		if(''==clazz){
			$.messager.alert('提示',"请选择班级","warning");
			return false;
		}
		var continueReg = $('#continueReg',selectedDialog).combobox('getValue');
		if(''==continueReg){
			$.messager.alert('提示',"请选择类型","warning");
			return false;
		}
		var user = $('#user',selectedDialog).combobox('getValue');
		if(''==user){
			$.messager.alert('提示',"请选择经办人","warning");
			return false;
		}
		return true;
	};
	
	var selectedSave = function(){
		$(selectedForm).form('submit',{
			//updateParam用于区分“选择已选班级”(1)和“编辑选班”(0)
			url: 'stuClass/oneToManySaveStuClass.do?updateParam='+1+'&billDetailId='+selectRow.billDetailId,
			onSubmit: function(){
				return selectedValidate();
			},
			success: function(data){
				var result = eval('('+data+')');
				if(result.isSuccess){
					var fn = function(){
						$(selectedDialog).dialog('close');
						search(true);
					};
					$.messager.alert('提示','保存成功','info',fn);
				}else{
					$.messager.alert('提示',result.message,"error");
				}
			}
		});
	};
	
	$(selectedClassInfoPanel).panel({
	    width:350,  
	    height:260,  
	    title: '班级信息'
	});  
	
	var selectedClassInfoInit = function(classId){
		$(selectedClassInfoForm).form('clear');
		var url = 'academic/queryOneClass.do';
	    var content = {studentId:selectRow.studentId,classId:classId};
	    var result = syncCallService(url,content);
	    if(result.isSuccess){
	    	var data = result.data.classInfo;
	    	classInfo = eval('('+data+')');
	    	$(selectedClassInfoForm).form('load',classInfo);
	    	var statusList = ['正常','插班','转班','休学','退学','弃学'];
	    	$('#oldScStatus',selectedClassInfoPanel).val(statusList[result.data.scStatus-1]);
	    	$('#stuClassId',selectedForm).val(result.data.stuClassId);
	    	$('#showStuCount',selectedClassInfoPanel).html(getBar(classInfo.stuCount,classInfo.planCount));
	    	$('#showCourseProgress',selectedClassInfoPanel).html(getBar(classInfo.courseProgress,classInfo.lessons));
	    }else{
		    $.messager.alert('提示',result.message,"error");
	    }
	};
	
	$(oneToOneDialog).dialog({  
	    title: '开设一对一个人班',  
	    width:400,
	    height:520,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    toolbar:[{
			text:'保存',
			iconCls:'icon-save',
			handler:function(){
				oneToOneSave();
			}
		},'-',{
			text:'退出',
			iconCls:'icon-cancel',
			handler:function(){
				$(oneToOneDialog).dialog('close');
			}
		}]
	});
	
	var oneToOneCombobox = function(){
		$('#teacher',oneToOneDialog).combobox({
			  width:200,
			  data:TRN.getIsTeacherEmployeeList(),
			  valueField:'userId',
			  textField:'userName'
		  });
		$('#lessonSchool',oneToOneDialog).combobox({
			  width:200,
			  data:TRN.getSelfSchoolList(),
			  valueField:'schoolId',
			  textField:'schoolName',
			  onSelect:function(record){
				  $('#classroom',oneToOneDialog).combobox({
					  disabled:false,
					  data:TRN.getInSchoolClassroomList(record.schoolId)
				  });
			  }
		  });
		$('#classroom',oneToOneDialog).combobox({
			disabled:true,
			width:200,
			valueField:'classroomId',
			textField:'classroomName'
		});
		$('#selectSchool',oneToOneDialog).combobox({
			  width:200,
			  data:TRN.getSelfSchoolList(),
			  valueField:'schoolId',
			  textField:'schoolName'
		  });
		$('#user',oneToOneDialog).combobox({
			  width:200,
			  data:TRN.getEmployeeList(),
			  valueField:'userId',
			  textField:'userName'
		});
	};
	
	var oneToOne = function(){
		$(oneToOneForm).form('clear');
		oneToOneCombobox();
		$('#className',oneToOneForm).val(selectRow.courseName+
				'个人班（'+selectRow.studentName+'）');
		$('#courseName',oneToOneForm).val(selectRow.courseName);
		$('#showCourseName',oneToOneDialog).val(selectRow.courseName);
		$('#lessons',oneToOneForm).val(selectRow.qty);
		$('#showLessons',oneToOneDialog).val(selectRow.qty);
		$('#studentId',oneToOneForm).val(selectRow.studentId);
		$('#courseId',oneToOneForm).val(selectRow.courseId);
		$('#continueReg',oneToOneForm).combobox('setValue',1);
		$(oneToOneDialog).dialog('open');
	};
	
	var oneToOneValidate = function(){
		var className = $('#className',oneToOneForm).val();
		if(''==className){
			$.messager.alert('提示',"请填写班级名称","warning");
			return false;
		}
		var startDate = $('#startDate',oneToOneForm).val();
		if(''==startDate){
			$.messager.alert('提示',"请选择开课日期","warning");
			return false;
		}
		var teacher = $('#teacher',oneToOneForm).combobox('getValue');
		if(''==teacher){
			$.messager.alert('提示',"请选择主讲老师","warning");
			return false;
		}
		var timeRule = $('#timeRule',oneToOneForm).val();
		if(''==timeRule){
			$.messager.alert('提示',"请填写上课规律","warning");
			return false;
		}
		var lessonSchool = $('#lessonSchool',oneToOneForm).combobox('getValue');
		if(''==lessonSchool){
			$.messager.alert('提示',"请选择上课地点","warning");
			return false;
		}
		var classroom = $('#classroom',oneToOneForm).combobox('getValue');
		if(''==classroom){
			$.messager.alert('提示',"请选择教室","warning");
			return false;
		}
		var lessonMinute = $('#lessonMinute',oneToOneForm).val();
		if(''==lessonMinute){
			$.messager.alert('提示',"请填写每课时分钟数","warning");
			return false;
		}
		var selectDate = $('#selectDate',oneToOneForm).val();
		if(''==selectDate){
			$.messager.alert('提示',"请选择选班日期","warning");
			return false;
		}
		var continueReg = $('#continueReg',oneToOneForm).combobox('getValue');
		if(''==continueReg){
			$.messager.alert('提示',"请选择类型","warning");
			return false;
		}
		var selectSchool = $('#selectSchool',oneToOneForm).combobox('getValue');
		if(''==selectSchool){
			$.messager.alert('提示',"请选择报名点","warning");
			return false;
		}
		var user = $('#user',oneToOneForm).combobox('getValue');
		if(''==user){
			$.messager.alert('提示',"请选择经办人","warning");
			return false;
		}
		return true;
	};
	
	var oneToOneSave = function(){
		$(oneToOneForm).form('submit',{
			url: 'stuClass/oneToOneSaveStuClass.do?billDetailId='+selectRow.billDetailId,
			onSubmit: function(){
				return oneToOneValidate();
			},
			success: function(data){
				var result = eval('('+data+')');
				if(result.isSuccess){
					var fn = function(){
						$(oneToOneDialog).dialog('close');
						search(true);
					};
					$.messager.alert('提示','保存成功','info',fn);
				}else{
					$.messager.alert('提示',result.message,"error");
				}
			}
		});
	};
	
	$(oneToManyDialog).dialog({  
	    title: '学员选班',  
	    width:780,
	    height:500,
	    closed: true,  
	    cache: false,  
	    modal: true,
	    closable:false,
	    toolbar:[{
			text:'保存',
			iconCls:'icon-save',
			handler:function(){
				oneToManySave();
			}
		},'-',{
			text:'退出',
			iconCls:'icon-cancel',
			handler:function(){
				$(oneToManyDialog).dialog('close');
			}
		}]
	});
	
	
	var oneToManyCombobox = function(courseId){
		$('#lessonSchool',oneToManyDialog).combobox({
			  width:262,
			  data:TRN.getSelfSchoolList(),
			  valueField:'schoolId',
			  textField:'schoolName',
			  onSelect:function(record){
				  $('#class',oneToManyDialog).combobox({
					  disabled:false,
					  data:TRN.getInSSTClassList(
							  courseId,
							  record.schoolId,
							  $('input:radio[name="lessonStatus"]:checked').val(),
							  $('#teacher',oneToManyDialog).combobox('getValue')
							  )
				  });
			  }
		  });
		$('input:radio[name="lessonStatus"]').click(function(){
			var lessonStatus = $('input:radio[name="lessonStatus"]:checked').val();
			$('#scStatus',oneToManyDialog).combobox('setValue',parseInt(lessonStatus)+1);
			$('#class',oneToManyDialog).combobox({
				  disabled:false,
				  data:TRN.getInSSTClassList(
						  courseId,
						  $('#lessonSchool',oneToManyDialog).combobox('getValue'),
						  lessonStatus,
						  $('#teacher',oneToManyDialog).combobox('getValue')
						  )
			  });
		});
		$('#teacher',oneToManyDialog).combobox({
			  width:200,
			  data:TRN.getIsTeacherEmployeeList(),
			  valueField:'userId',
			  textField:'userName',
			  onSelect:function(record){
				  $('#class',oneToManyDialog).combobox({
					  disabled:false,
					  data:TRN.getInSSTClassList(
							  courseId,
							  $('#lessonSchool',oneToManyDialog).combobox('getValue'),
							  $('input:radio[name="lessonStatus"]:checked').val(),
							  record.userId
							  )
				  });
			  }
		  });
		$('#class',oneToManyDialog).combobox({
			  disabled:true,
			  width:262,
			  valueField:'classId',
			  textField:'className',
			  onSelect:function(record){
				  classInfoInit(record.classId);
				  setLessons();
				  $('#className',oneToManyForm).val(record.className);
			  }
		  });
		$('#selectSchool',oneToManyDialog).combobox({
			  width:262,
			  data:TRN.getSelfSchoolList(),
			  valueField:'schoolId',
			  textField:'schoolName'
		  });
		$('#user',oneToManyDialog).combobox({
			  width:262,
			  data:TRN.getEmployeeList(),
			  valueField:'userId',
			  textField:'userName'
		});
	};
	
	var setLessons = function(){
		var courseProgress = $('#courseProgress',oneToManyForm);
		var lessons = $('#lessons',oneToManyForm);
		if(courseProgress.val()=='')
			courseProgress.val(0);
		if(lessons.val()==''){
			if(selectRow.unitName=='课时'){
				lessons.val(selectRow.qty);
			}else if(selectRow.unitName=='学期'){
				lessons.val(classInfo.lessons);
			}
		}
		$('#showProgress',oneToManyDialog).html('<span style="color:red">'+
				courseProgress.val()+'</span>/'+lessons.val());
	};
	
	var oneToMany = function(){
		$(oneToManyForm).form('clear');
		$('#class',oneToManyDialog).combobox({
			data:CSIT.ClearCombobxData
		});
		oneToManyCombobox(selectRow.courseId);
		$('#scStatus',oneToManyDialog).combobox('setValue',1);
		$('#scStatus',oneToManyDialog).combobox('disable');
		$('#continueReg',oneToManyForm).combobox('setValue',1);
		$('#studentId',oneToManyForm).val(selectRow.studentId);
		$('#showProgress',oneToManyDialog).html('<span style="color:red">'+
				0+'</span>/'+0);
		$(oneToManyDialog).dialog('open');
	};
	
	var oneToManyValidate = function(){
		var lessonSchool = $('#lessonSchool',oneToManyDialog).combobox('getValue');
		if(''==lessonSchool){
			$.messager.alert('提示','请选择上课校区','warning');
			return false;
		}
		var clazz = $('#class',oneToManyDialog).combobox('getValue');
		if(''==clazz){
			$.messager.alert('提示','请选择班级','warning');
			return false;
		}
		var selectDate = $('#selectDate',oneToManyDialog).val();
		if(''==selectDate){
			$.messager.alert('提示','请选择选班日期','warning');
			return false;
		}
		var selectSchool = $('#selectSchool',oneToManyDialog).combobox('getValue');
		if(''==selectSchool){
			$.messager.alert('提示','请选择报名点','warning');
			return false;
		}
		var user = $('#user',oneToManyDialog).combobox('getValue');
		if(''==user){
			$.messager.alert('提示','请选择经办人','warning');
			return false;
		}
		return true;
	};
	
	var oneToManySave = function(){
		$(oneToManyForm).form('submit',{
			//updateParam用于区分“选择已选班级”(1)和“编辑选班”(0)
			url: 'stuClass/oneToManySaveStuClass.do?updateParam='+0+'&billDetailId='+selectRow.billDetailId,
			onSubmit: function(){
				return oneToManyValidate();
			},
			success: function(data){
				var result = eval('('+data+')');
				if(result.isSuccess){
					var fn = function(){
						$(oneToManyDialog).dialog('close');
						search(true);
					};
					$.messager.alert('提示','保存成功','info',fn);
				}else{
					$.messager.alert('提示',result.message,"error");
				}
			}
		});
	};
	
	$(classInfoPanel).panel({
	    width:350,  
	    height:230,  
	    title: '班级信息'
	});  
	
	var  classInfoInit = function(classId){
		$(classInfoForm).form('clear');
		var url = 'academic/queryOneClass.do';
	    var content = {studentId:'',classId:classId};
	    var result = syncCallService(url,content);
	    if(result.isSuccess){
	    	var data = result.data.classInfo;
	    	classInfo = eval('('+data+')');
	    	$(classInfoForm).form('load',classInfo);
	    	$('#showStuCount',classInfoPanel).html(getBar(classInfo.stuCount,classInfo.planCount));
	    	$('#showCourseProgress',classInfoPanel).html(getBar(classInfo.courseProgress,classInfo.lessons));
	    }else{
		    $.messager.alert('提示',result.message,"error");
	    }
	};
  };
})(jQuery);   