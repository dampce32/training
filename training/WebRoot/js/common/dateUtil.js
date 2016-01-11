//两个字符串型(yyyy-MM-dd)日期的差值
function DateDiff(d1,d2){ 
    var day = 24 * 60 * 60 *1000; 
	try{     
	        var dateArr = d1.split("-"); 
	   var checkDate = new Date(); 
	        checkDate.setFullYear(dateArr[0], dateArr[1]-1, dateArr[2]); 
	   var checkTime = checkDate.getTime(); 
	   
	   var dateArr2 = d2.split("-"); 
	   var checkDate2 = new Date(); 
	        checkDate2.setFullYear(dateArr2[0], dateArr2[1]-1, dateArr2[2]); 
	   var checkTime2 = checkDate2.getTime(); 
	     
	   var cha = (checkTime - checkTime2)/day;   
	        return cha; 
	}catch(e){ 
	   return false; 
	} 
}
//日期加上天数后的新日期. 
function addDays(date,days){ 
	var nd = new Date(date); 
	   nd = nd.valueOf(); 
	   nd = nd + days * 24 * 60 * 60 * 1000; 
	   nd = new Date(nd); 
	   //alert(nd.getFullYear() + "年" + (nd.getMonth() + 1) + "月" + nd.getDate() + "日"); 
	var y = nd.getFullYear(); 
	var m = nd.getMonth()+1; 
	var d = nd.getDate(); 
	if(m <= 9) m = "0"+m; 
	if(d <= 9) d = "0"+d; 
	var cdate = y+"-"+m+"-"+d; 
	return cdate; 
}

//两个字符串型(HH:mm)日期的差值(小时)
function hourDiff(d1,d2){
	var hour = 60*60*1000;
	
	var str1 = d1.split(":");
	var str2 = d2.split(":");
	
	var HHPart = str2[0]-str1[0];
	var mmPart = str2[1]-str1[1];
	
	var difference = 0;
	
	if(mmPart<0){
		difference = HHPart - 1;
	}else{
		difference = HHPart;
	}
	
	return difference;
}

//两个字符串型(HH:mm)日期的差值(分钟)
function minuteDiff(d1,d2){
	var minute = 60*1000;
	
	var str1 = d1.split(":");
	var str2 = d2.split(":");
	
	var HHPart = str2[0]-str1[0];
	var mmPart = str2[1]-str1[1];
	
	var difference = HHPart*60 + mmPart;
	
	return difference;
}
//当前日期(yyyy-MM-dd HH:mm)
function nowDate32(){
	var dt = new Date();
	
	var year = dt.getFullYear();
	var month = dt.getMonth()+1;
	month = (month<10)?('0'+month):month;
	var day = dt.getDate();
	day = (day<10)?('0'+day):day;
	var hour = dt.getHours();
	var minute = dt.getMinutes();
	
	var nowDate = year+'-'+month+'-'+day+' '+hour+':'+minute;

	return nowDate;
}
//指定日期(yyyy-MM-dd)的星期(数字0-6)
function getWeekOfDate(d){
	var dList = d.split('-');
	
	var dt = new Date(dList[0],dList[1]-1,dList[2]);
	
	var week = dt.getDay();
	
	return week;
}
//指定日期的星期
function getTodayWeek(date){
	var dt = new Date(date);
	
	var week = dt.getDay();
	
	return week;
}
//两个日期相差的星期
function getWeekBetweenDate(date1,date2){
	
	//跟本周日差多少天
	var toSunday1 = 0-getTodayWeek(date1);
	var toSunday2 = 0-getTodayWeek(date2);
	
	//两个日期的周日
	var d1 = addDays(date1,toSunday1);
	var d2 = addDays(date2,toSunday2);
	
	//两个周日相差的天数
	var diffDay = DateDiff(d1,d2);
	//相差的星期
	var diffWeek = parseInt(''+diffDay/7);
	
	return diffWeek;
}
//字符串型日期(yyyy-MM-dd)化成Date型
function stringToDate(d){
	var dArr = d.split("-"); 
	
	var date = new Date();
	
	date.setFullYear(dArr[0],dArr[1]-1,dArr[2]);
	
	return date.getTime();
}