package com.csit.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.csit.dao.ClassDAO;
import com.csit.dao.StuClassDAO;
import com.csit.model.Clazz;
import com.csit.model.StuClass;
import com.csit.service.ClassService;
import com.csit.util.JSONUtil;
import com.csit.vo.ServiceResult;
/**
 * 
 * @Description: 班级Service实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-4
 * @author yk
 * @vesion 1.0
 */
@Service
public class ClassServiceImpl extends BaseServiceImpl<Clazz, Integer> implements
		ClassService {
	@Resource
	private ClassDAO classDAO;
	@Resource
	private StuClassDAO stuClassDAO;
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.ClassService#save(com.csit.model.Clazz)
	 */
	@Override
	public ServiceResult save(Clazz model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null){
			result.setMessage("请填写班级信息");
			return result;
		}
		if(StringUtils.isEmpty(model.getClassName())){
			result.setMessage("请填写班级名称");
			return result;
		}
		if(model.getCourse()==null||model.getCourse().getCourseType()==null
				||model.getCourse().getCourseType().getCourseTypeId()==null){
			result.setMessage("请选择课程类型");
			return result;
		}
		if(model.getTeacher()==null||model.getTeacher().getUserId()==null){
			result.setMessage("请选择主讲老师");
			return result;
		}
		if(model.getStartDate()==null){
			result.setMessage("请选择开课时间");
			return result;
		}
		if(model.getSchool()==null||model.getSchool().getSchoolId()==null){
			result.setMessage("请选择校区");
			return result;
		}
		if(model.getPlanCount()==null){
			result.setMessage("请填写计划招生数");
			return result;
		}
		
		if(model.getClassroom()==null||model.getClassroom().getClassroomId()==null){
			result.setMessage("请选择教室");
			return result;
		}
		if(model.getLessons()==null){
			result.setMessage("请填写总课时");
			return result;
		}
		if(model.getLessonMinute()==null){
			result.setMessage("请填写每课时的时长");
			return result;
		}
		if(model.getCreater()==null||model.getCreater().getUserId()==null){
			result.setMessage("请选择建立者");
			return result;
		}
		if(model.getClassId()==null){//新增
			model.setCreateDate(new Date());
			model.setClassType(0);
			model.setStuCount(0);
			model.setCourseProgress(0);
			model.setArrangeLessons(0);
			classDAO.save(model);
		}else{
			Clazz oldModel = classDAO.load(model.getClassId());
			if(oldModel==null){
				result.setMessage("该班级已不存在");
				return result;
			}
			oldModel.setClassName(model.getClassName());
			oldModel.setCourse(model.getCourse());
			oldModel.setClassType(model.getClassType());
			oldModel.setTeacher(model.getTeacher());
			oldModel.setStartDate(model.getStartDate());
			oldModel.setEndDate(model.getEndDate());
			oldModel.setTimeRule(model.getTimeRule());
			oldModel.setSchool(model.getSchool());
			oldModel.setPlanCount(model.getPlanCount());
			oldModel.setLessons(model.getLessons());
			oldModel.setLessonMinute(model.getLessonMinute());
			oldModel.setLessonCommission(model.getLessonCommission());
			oldModel.setNote(model.getNote());
			oldModel.setCreater(model.getCreater());
		}
		result.setIsSuccess(true);
		result.addData("classId", model.getClassId());
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.ClassService#delete(com.csit.model.Clazz)
	 */
	@Override
	public ServiceResult delete(Clazz model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null||model.getClassId()==null){
			result.setMessage("请选择要删除的班级");
			return result;
		}
		Clazz oldModel = classDAO.load(model.getClassId());
		if(oldModel==null){
			result.setMessage("该班级已不存在");
			return result;
		}else{
			classDAO.delete(oldModel);
		}
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.ClassService#query(com.csit.model.Clazz, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public ServiceResult query(Clazz model, Integer page, Integer rows,Integer status) {
		ServiceResult result = new ServiceResult(false);
		
		List<Clazz> list = classDAO.query(model,page,rows,status);
		
		String[] properties = {"classId","className","startDate","timeRule","school.schoolId","school.schoolName",
				"planCount","stuCount","lessons","courseProgress","lessonMinute","course.courseType.courseTypeId",
				"course.courseType.courseTypeName","course.courseId","course.courseName","teacher.userId:teacherId","teacher.userName:teacherName",
				"school.schoolId","classroom.classroomId","note","creater.userId:createrId","creater.userName:createrName",
				"classType","endDate","lessonCommission","classroom.classroomName","arrangeLessons"};
		
		String data = JSONUtil.toJson(list,properties);
		result.addData("datagridData", data);
		
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.ClassService#getTotalCount(com.csit.model.Clazz)
	 */
	@Override
	public ServiceResult getTotalCount(Clazz model,Integer status) {
		ServiceResult result = new ServiceResult(false);
		Long data = classDAO.getTotalCount(model,status);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.ClassService#queryCombobox()
	 */
	@Override
	public String queryCombobox(Clazz model) {
		List<Clazz> list = classDAO.query("school",model.getSchool());
		String[] properties = {"classId","className"};
		String jsonString = JSONUtil.toJsonWithoutRows(list,properties);
		return jsonString;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.ClassService#queryInSSTCombobox(java.lang.String, com.csit.model.Clazz)
	 */
	@Override
	public String queryInSSTCombobox(Integer courseId,Integer schoolId,Integer lessonStatus, Integer teacherId) {
		List<Clazz> list = classDAO.queryInSSTCombobox(courseId,schoolId,lessonStatus,teacherId);
		String[] properties = {"classId","className"};
		String jsonString = JSONUtil.toJsonWithoutRows(list,properties);
		return jsonString;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.ClassService#queryOne(java.lang.Integer)
	 */
	@Override
	public ServiceResult queryOne(Integer studentId,Integer classId) {
		ServiceResult result = new ServiceResult(false);
		Clazz clazz = classDAO.load(classId);
		String[] properties = {"className","startDate","timeRule","teacher.userName",
				"classroom.classroomName","stuCount","planCount","lessons","endDate",
				"courseProgress","course.unitPrice","school.schoolId","note",
				"course.courseName","school.schoolName"};
		String classInfo = JSONUtil.toJson(clazz, properties);
		result.addData("classInfo", classInfo);
		
		if(studentId!=null){
			StuClass stuClass = stuClassDAO.getStuClass(studentId,classId,0,null);
			result.addData("scStatus",stuClass.getScStatus());
			result.addData("stuClassId",stuClass.getStuClassId());
		}
		
		result.setIsSuccess(true);
		return result;
	}
}
