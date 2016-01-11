// 创建一个闭包  
(function($) {  
  // 插件的定义  
  $.fn.myLessonInit = function() {
	  var $this = $(this);
	  var id = $(this).attr('id');
	  
	  var height = $(document.body).height();
		
	  var weekTableList = $('#weekTableList',$this);
		
	  //星期，0表示本周，负数表示以前的，正数表示以后的
	  var toWeekOfToday = 0;
		
	  $('#schoolSearch',$this).combobox({
		 width:150,
		 data:TRN.getSelfSchoolList(),
		 valueField:'schoolId',
		 textField:'schoolName'
	  });
		
		var getTitle = function(index,toWeekOfToday){
			var week = ['日','一','二','三','四','五','六'];
			var today = new Date();
			var todayWeek = getTodayWeek(today);
			var toNumber = index-todayWeek+toWeekOfToday*7;
			toNumber = parseInt(""+toNumber);
			var date = addDays(today,toNumber);
			var nowDate = addDays(new Date(),0);
			if(date==nowDate){
				return '<span style="color:blue">周'+week[index]+'（'+date+'）</span>';
			}
			return '周'+week[index]+'（'+date+'）';
		};
		
		//列表
		$(weekTableList).datagrid({
			singleSelect:true,
			method:"POST",
			nowrap:true,
			striped: true,
			collapsible:true,
			fit:true,
			toolbar:[	
						{text:'上周',iconCls:'icon-left',
							handler:function(){
								toWeekOfToday--;
								weekTableSearch(toWeekOfToday);
							}
						},'-',
						{text:'下周',iconCls:'icon-right',
							handler:function(){
								toWeekOfToday++;
								weekTableSearch(toWeekOfToday);
							}
						}
					],
			columns:[[
			    {field:'classroomName',title:'教室',width:100,align:"center"},
			    {field:'Sunday',title:getTitle(0,toWeekOfToday),width:150,align:"center"},
			    {field:'Monday',title:getTitle(1,toWeekOfToday),width:150,align:"center"},
			    {field:'Tuesday',title:getTitle(2,toWeekOfToday),width:150,align:"center"},
			    {field:'Wednesday',title:getTitle(3,toWeekOfToday),width:150,align:"center"},
			    {field:'Thursday',title:getTitle(4,toWeekOfToday),width:150,align:"center"},
			    {field:'Friday',title:getTitle(5,toWeekOfToday),width:150,align:"center"},
			    {field:'Saturday',title:getTitle(6,toWeekOfToday),width:150,align:"center"}
			]],
			onClickRow:function(rowIndex, rowData){
				selectParamIndex = rowIndex;
			}
		});
		
		//查询
		var weekTableSearch = function(toWeekOfToday){
			var todayWeek = getTodayWeek(new Date());
			var toNumber = 0-todayWeek+toWeekOfToday*7;
			toNumber = parseInt(""+toNumber);
			var content = {toSunday:toNumber,toWeekOfToday:toWeekOfToday};
			//取得列表信息
			var url = "teacher/queryByTeacherLessonDegree.do";
			var result = syncCallService(url,content);
			if(result.isSuccess){
				var  data = result.data;
				$(weekTableList).datagrid({
					columns:[[
					    {field:'classroomName',title:'教室',width:100,align:"center"},
					    {field:'Sunday',title:getTitle(0,toWeekOfToday),width:150,align:"center"},
					    {field:'Monday',title:getTitle(1,toWeekOfToday),width:150,align:"center"},
					    {field:'Tuesday',title:getTitle(2,toWeekOfToday),width:150,align:"center"},
					    {field:'Wednesday',title:getTitle(3,toWeekOfToday),width:150,align:"center"},
					    {field:'Thursday',title:getTitle(4,toWeekOfToday),width:150,align:"center"},
					    {field:'Friday',title:getTitle(5,toWeekOfToday),width:150,align:"center"},
					    {field:'Saturday',title:getTitle(6,toWeekOfToday),width:150,align:"center"}
					]]
				});
				var datagridData = eval("("+data.datagridData+")");
				$(weekTableList).datagrid('loadData',datagridData);
			}else{
				$.messager.alert('提示',result.message,'error');
			}
		};
		
		//查询按钮
		$('#searchBtn',$this).click(function(){
			var dateSearch = $('#dateSearch',$this).val();
			if(dateSearch!=''){
				toWeekOfToday = getWeekBetweenDate(stringToDate(dateSearch),new Date());
			}
			weekTableSearch(toWeekOfToday);
		});
  };
})(jQuery);   