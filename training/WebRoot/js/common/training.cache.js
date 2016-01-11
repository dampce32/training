var TRN={};
//课程类型
TRN.CourseTypeList = null;
TRN.getCourseTypeList = function(){
	if(TRN.CourseTypeList==null){
		var url = 'dict/queryComboboxCourseType.do';
		TRN.CourseTypeList = syncCallService(url);
	}
	return TRN.CourseTypeList;
};
//商品单位
TRN.UnitList = null;
TRN.getUnitList = function(){
	if(TRN.UnitList==null){
		var url = 'dict/queryComboboxUnit.do';
		TRN.UnitList = syncCallService(url);
	}
	return TRN.UnitList;
};
//商品分类
TRN.CommodityTypeList = null;
TRN.getCommodityTypeList = function(){
	if(TRN.CommodityTypeList==null){
		var url = 'dict/queryComboboxCommodityType.do';
		TRN.CommodityTypeList = syncCallService(url);
	}
	return TRN.CommodityTypeList;
};
//奖惩分类
TRN.RewPunTypeList = null;
TRN.getRewPunTypeList = function(){
	if(TRN.RewPunTypeList==null){
		var url = 'dict/queryComboboxRewPunType.do';
		TRN.RewPunTypeList = syncCallService(url);
	}
	return TRN.RewPunTypeList;
};
//供应商
TRN.SupplierList = null;
TRN.getSupplierList = function(){
	if(TRN.SupplierList==null){
		var url = 'dict/queryComboboxSupplier.do';
		TRN.SupplierList = syncCallService(url);
	}
	return TRN.SupplierList;
};
//媒体
TRN.MediaList = null;
TRN.getMediaList = function(){
	if(TRN.MediaList==null){
		var url = 'dict/queryComboboxMedia.do';
		TRN.MediaList = syncCallService(url);
	}
	return TRN.MediaList;
};
//咨询课程
TRN.PotCourseList = null;
TRN.getPotCourseList = function(){
	if(TRN.PotCourseList==null){
		var url = 'potCourse/queryComboboxPotCourse.do';
		TRN.PotCourseList = syncCallService(url);
	}
	return TRN.PotCourseList;
};
//潜在生源状态
TRN.PotentialStuStatusList = null;
TRN.getPotentialStuStatusList = function(){
	if(TRN.PotentialStuStatusList==null){
		var url = 'dict/queryComboboxPotentialStuStatus.do';
		TRN.PotentialStuStatusList = syncCallService(url);
	}
	return TRN.PotentialStuStatusList;
};

//所有校区
TRN.AllSchoolList = null;
TRN.getAllSchoolList = function(){
	if(TRN.AllSchoolList==null){
		var url = 'system/queryAllComboboxSchool.do';
		TRN.AllSchoolList = syncCallService(url);
	}
	return TRN.AllSchoolList;
};

//用户所在校区
TRN.SelfSchoolList = null;
TRN.getSelfSchoolList = function(){
	if(TRN.SelfSchoolList==null){
		var url = 'system/querySelfComboboxSchool.do';
		TRN.SelfSchoolList = syncCallService(url);
	}
	return TRN.SelfSchoolList;
};
TRN.getSelfSchoolListIframe = function(){
	if(TRN.SelfSchoolList==null){
		var url = '../system/querySelfComboboxSchool.do';
		TRN.SelfSchoolList = syncCallService(url);
	}
	return TRN.SelfSchoolList;
};
//仓库
TRN.WarehouseList = null;
TRN.getWarehouseList = function(){
	if(TRN.WarehouseActionList==null){
		var url = 'dict/queryComboboxWarehouse.do';
		TRN.WarehouseActionList = syncCallService(url);
	}
	return TRN.WarehouseActionList;
};

//本校区员工
TRN.EmployeeList = null;
TRN.getEmployeeList = function(){
	if(TRN.EmployeeList==null){
		var url = 'employee/queryComboboxEmployee.do';
		TRN.EmployeeList = syncCallService(url);
	}
	return TRN.EmployeeList;
};
//本校区教工
TRN.IsTeacherEmployeeList = null;
TRN.getIsTeacherEmployeeList = function(){
	if(TRN.IsTeacherEmployeeList==null){
		var url = 'employee/queryIsTeacherComboboxEmployee.do';
		TRN.IsTeacherEmployeeList = syncCallService(url);
	}
	return TRN.IsTeacherEmployeeList;
};
//指定类型下的课程
TRN.IsTypeCourseList = null;
TRN.getIsTypeCourseList = function(courseTypeId){
	var url = 'dict/queryIsTypeComboboxCourse.do?courseType.courseTypeId='+courseTypeId;
	TRN.IsTypeCourseList = syncCallService(url);
	return TRN.IsTypeCourseList;
};
//指定校区下的教室
TRN.InSchoolClassroomList = null;
TRN.getInSchoolClassroomList = function(schoolId){
	var url = 'dict/queryInSchoolComboboxClassroom.do?school.schoolId='+schoolId;
	TRN.InSchoolClassroomList = syncCallService(url);
	return TRN.InSchoolClassroomList;
};
//时间段
TRN.PeriodTimeList = null;
TRN.getPeriodTimeList = function(){
	if(TRN.PeriodTimeList==null){
		var url = 'dict/queryComboboxPeriodTime.do';
		TRN.PeriodTimeList = syncCallService(url);
	}
	return TRN.PeriodTimeList;
};
//学员
TRN.StudentList = null;
TRN.getStudentList = function(){
	if(TRN.StudentList==null){
		var url = 'customerService/queryComboboxStudent.do';
		TRN.StudentList = syncCallService(url);
	}
	return TRN.StudentList;
};
//教师查询学员
TRN.StudentByTeaList = null;
TRN.getStudentByTeaList = function(){
	if(TRN.StudentByTeaList==null){
		var url = 'teacher/queryComboboxByTeaStudent.do';
		TRN.StudentByTeaList = syncCallService(url);
	}
	return TRN.StudentByTeaList;
};
//指定分组下的时间段
TRN.InGroupTypeTimeList = null;
TRN.getInGroupTypeTimeList = function(groupType){
	var url = 'dict/queryInGroupTypeComboboxPeriodTime.do?groupType='+groupType;
	TRN.InGroupTypeTimeList = syncCallService(url);
	return TRN.InGroupTypeTimeList;
};
//指定课程、校区、上课状态、讲师下的班级
TRN.InSSTClassList = null;
TRN.getInSSTClassList = function(courseId,schoolId,lessonStatus,teacherId){
	if(lessonStatus==null){
		lessonStatus='';
	}
	var url = 'academic/queryInSSTComboboxClass.do';
	var content = {'course.courseId':courseId,'school.schoolId':schoolId,lessonStatus:lessonStatus,'teacher.userId':teacherId};
	TRN.InSSTClassList = syncCallService(url,content);
	return TRN.InSSTClassList;
};
//已选班级
TRN.selectedClassList = null;
TRN.getSelectedClassList = function(courseId,studentId,scStatus){
	var url = 'stuClass/querySelectedClassComboboxStuClass.do';
	var content = {'clazz.course.courseId':courseId,'student.studentId':studentId,scStatus:scStatus};
	TRN.selectedClassList = syncCallService(url,content);
	return TRN.selectedClassList;
};
//已选班级
TRN.classList = null;
TRN.getClassList = function(schoolId){
	var url = 'stuClass/queryComboboxClass.do?school.schoolId='+schoolId;
	TRN.selectedClassList = syncCallService(url);
	return TRN.selectedClassList;
};
//账户表
TRN.AccountList = null;
TRN.getAccountList = function(){
	if(TRN.AccountList==null){
		var url = 'dict/queryComboboxAccount.do';
		TRN.AccountList = syncCallService(url);
	}
	return TRN.AccountList;
};
