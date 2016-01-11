package com.csit.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.csit.dao.AttendDAO;
import com.csit.dao.BillDetailDAO;
import com.csit.dao.ClassDAO;
import com.csit.dao.LessonDegreeDAO;
import com.csit.dao.StuClassDAO;
import com.csit.dao.StudentDAO;
import com.csit.model.Attend;
import com.csit.model.BillDetail;
import com.csit.model.Clazz;
import com.csit.model.LessonDegree;
import com.csit.model.StuClass;
import com.csit.model.Student;
import com.csit.service.StuClassService;
import com.csit.util.JSONUtil;
import com.csit.vo.ServiceResult;
/**
 * 
 * @Description: 选班Service实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-14
 * @author yk
 * @vesion 1.0
 */
@Service
public class StuClassServiceImpl extends BaseServiceImpl<StuClass, Integer>
		implements StuClassService {
	@Resource
	private StuClassDAO stuClassDAO;
	@Resource
	private ClassDAO classDAO;
	@Resource
	private BillDetailDAO billDetailDAO;
	@Resource
	private StudentDAO studentDAO;
	@Resource
	private LessonDegreeDAO lessonDegreeDAO;
	@Resource
	private AttendDAO attendDAO;
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.StuClassService#save(com.csit.model.StuClass)
	 */
	@Override
	public ServiceResult oneToManySave(StuClass model,Integer updateParam,Integer billDetailId) {
		ServiceResult result = new ServiceResult(false);
		if(model==null){
			result.setMessage("请填写一对多选班信息");
			return result;
		}
		BillDetail billDetail = null;
		Student student = null;
		if(model.getStuClassId()==null){//新增
			StuClass oldStuClass = stuClassDAO.getStuClass(
					model.getStudent().getStudentId(),
					model.getClazz().getClassId(),
					0,
					null
					);
			if(oldStuClass!=null){
				result.setMessage("该班级已存在");
				return result;
			}
			model.setAdvanceCount(0);
			model.setLateCount(0);
			model.setFactCount(0);
			model.setFillCount(0);
			model.setLeaveCount(0);
			model.setTruancyCount(0);
			model.setScStatus(1);
			model.setInsertTime(new Date());
			stuClassDAO.save(model);
			
			billDetail = billDetailDAO.load(billDetailId);
			//此选班添加到消费明细表
			billDetail.setStuClass(model);
			billDetail.setStatus(1);
			
			//更新学员数
			Clazz clazz = classDAO.load(model.getClazz().getClassId());
			clazz.setStuCount(clazz.getStuCount()+1);
			
			//学员最新选班和选班数
			Long scCount = stuClassDAO.getTotalCount(model);
			student = studentDAO.load(model.getStudent().getStudentId());
			student.setNewStuClass(model.getClazz().getClassName()+
					"（<span style=\"color:red\">"+scCount+"</span>）");
			
		}else{
			StuClass oldModel = stuClassDAO.get(model.getStuClassId());
			if(oldModel==null){
				result.setMessage("该选班信息已不存在");
				return result;
			}
			//编辑选班信息
			if(updateParam==0){
				oldModel.setLessonSchool(model.getLessonSchool());
				oldModel.setClazz(model.getClazz());
				oldModel.setLessons(model.getLessons());
				oldModel.setScStatus(model.getScStatus());
				oldModel.setSelectDate(model.getSelectDate());
				oldModel.setSelectSchool(model.getSelectSchool());
				
				Long scCount = stuClassDAO.getTotalCount(null);
				student = studentDAO.load(model.getStudent().getStudentId());
				student.setNewStuClass(model.getClazz().getClassName()+","+scCount);
			}
			
			//选择已选班级
			if(updateParam==1){
				oldModel.setLessons(oldModel.getLessons()+model.getLessons());
				
				billDetail = billDetailDAO.load(billDetailId);
				billDetail.setStuClass(model);
				billDetail.setStatus(1);
			}
			oldModel.setContinueReg(model.getContinueReg());
			oldModel.setUser(model.getUser());
		}
		
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.StuClassService#oneToOneSave(com.csit.model.StuClass)
	 */
	@Override
	public ServiceResult oneToOneSave(StuClass model,Integer billDetailId) {
		ServiceResult result = new ServiceResult(false);
		if(model==null){
			result.setMessage("请填写一对一开班信息");
			return result;
		}
		if(model.getClazz()==null||model.getClazz().getClassName()==null){
			result.setMessage("请填写班级名称");
			return result;
		}
		if(model.getClazz()==null||model.getClazz().getCourse()==null||model.getClazz().getCourse().getCourseName()==null){
			result.setMessage("请填写课程名称");
			return result;
		}
		if(model.getClazz()==null||model.getClazz().getStartDate()==null){
			result.setMessage("请选择开课日期");
			return result;
		}
		if(model.getClazz()==null||model.getClazz().getTeacher()==null||model.getClazz().getTeacher().getUserId()==null){
			result.setMessage("请选择主讲老师");
			return result;
		}
		if(model.getClazz()==null||model.getClazz().getTimeRule()==null){
			result.setMessage("请填写上课规律");
			return result;
		}
		if(model.getLessonSchool()==null||model.getLessonSchool().getSchoolId()==null){
			result.setMessage("请选择上课地点");
			return result;
		}
		if(model.getClazz()==null||model.getClazz().getClassroom()==null||model.getClazz().getClassroom().getClassroomId()==null){
			result.setMessage("请选择教室");
			return result;
		}
		if(model.getClazz()==null||model.getClazz().getLessonMinute()==null){
			result.setMessage("请填写每课时分钟数");
			return result;
		}
		if(model.getSelectDate()==null){
			result.setMessage("请选择选班日期");
			return result;
		}
		if(model.getContinueReg()==null){
			result.setMessage("请选择类型");
			return result;
		}
		if(model.getSelectSchool()==null||model.getSelectSchool().getSchoolId()==null){
			result.setMessage("请选择报名点");
			return result;
		}
		if(model.getUser()==null||model.getUser().getUserId()==null){
			result.setMessage("请选择经办人");
			return result;
		}
		
		Clazz clazz = model.getClazz();
		clazz.setSchool(model.getLessonSchool());
		clazz.setCourseProgress(0);
		clazz.setPlanCount(1);
		clazz.setStuCount(1);
		clazz.setClassType(1);
		clazz.setArrangeLessons(0);
		clazz.setCreateDate(model.getSelectDate());
		clazz.setCreater(model.getUser());
		clazz.setLessons(model.getLessons());
		classDAO.save(clazz);
		
		model.setAdvanceCount(0);
		model.setLateCount(0);
		model.setFactCount(0);
		model.setFillCount(0);
		model.setLeaveCount(0);
		model.setTruancyCount(0);
		model.setLessons(model.getLessons());
		model.setCourseProgress(0);
		model.setScStatus(1);
		model.setInsertTime(new Date());
		stuClassDAO.save(model);
		
		BillDetail billDetail = billDetailDAO.load(billDetailId);
		billDetail.setStuClass(model);
		billDetail.setStatus(1);
		
		//学员最新选班和选班数
		Long scCount = stuClassDAO.getTotalCount(model);
		Student student = studentDAO.load(model.getStudent().getStudentId());
		student.setNewStuClass(model.getClazz().getClassName()+
				"（<span style=\"color:red\">"+scCount+"</span>）");
		
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.StuClassService#delete(com.csit.model.StuClass)
	 */
	@Override
	public ServiceResult delete(StuClass model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null||model.getStuClassId()==null){
			result.setMessage("请选择要删除的选班信息");
			return result;
		}
		StuClass oldModel = stuClassDAO.load(model.getStuClassId());
		if(oldModel==null){
			result.setMessage("该选班信息已不存在");
			return result;
		}else{
			
			List<BillDetail> billDetailList = billDetailDAO.query("stuClass",model);
			for(BillDetail billDetail:billDetailList){
				billDetail.setStuClass(null);
				billDetail.setStatus(0);
			}
			
			Integer studentId = oldModel.getStudent().getStudentId();
			
			Integer classType = oldModel.getClazz().getClassType();
			Integer classId = oldModel.getClazz().getClassId();
			
			stuClassDAO.delete(oldModel);
			
			//删除一对一班级
			if(classType==1){
				classDAO.delete(classId);
			}
			
			//更新学员数
			Clazz clazz = classDAO.load(classId);
			clazz.setStuCount(clazz.getStuCount()-1);
			
			//更新学员最新选班
			Date maxSelectDate = stuClassDAO.getMax(studentId);
			StuClass stuClass = stuClassDAO.getStuClass(studentId,null,1,maxSelectDate);
			Student student = studentDAO.load(studentId);
			Long scCount = stuClassDAO.getTotalCount(null);
			if(stuClass!=null&&stuClass.getClazz()!=null&&stuClass.getClazz().getClassName()!=null){
				student.setNewStuClass(stuClass.getClazz().getClassName()+
						"（<span style=\"color:red\">"+scCount+"</span>）");
			}else{
				student.setNewStuClass(null);
			}
		}
		
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.StuClassService#query(com.csit.model.StuClass, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public ServiceResult query(StuClass model, Integer page, Integer rows) {
		ServiceResult result = new ServiceResult(false);
		
		List<StuClass> list = stuClassDAO.query(model,page,rows);
		
		String[] properties = {"stuClassId","student.studentId","student.studentName",
				"scStatus","continueReg","selectDate","courseProgress","lessons",
				"clazz.classId"};
		
		String data = JSONUtil.toJson(list,properties);
		result.addData("datagridData", data);
		
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.StuClassService#getTotalCount(com.csit.model.StuClass)
	 */
	@Override
	public ServiceResult getTotalCount(StuClass model) {
		ServiceResult result = new ServiceResult(false);
		Long data = stuClassDAO.getTotalCount(model);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.StuClassService#query(com.csit.model.StuClass)
	 */
	@Override
	public ServiceResult query(StuClass model) {
		ServiceResult result = new ServiceResult(false);
		
		List<StuClass> list = stuClassDAO.query(model,null);
		String[] properties = {"stuClassId","clazz.classId","clazz.className",
				"lessonSchool.schoolName:lessonSchoolName","selectDate","scStatus",
				"continueReg","selectSchool.schoolName:selectSchoolName","lessons",
				"courseProgress","lessonSchool.schoolId:lessonSchoolId","user.userId",
				"selectSchool.schoolId:selectSchoolId","clazz.course.courseId","note",
				"student.studentId","student.studentName","user.userName","fillCount",
				"factCount","lateCount","advanceCount","truancyCount","leaveCount"};
		
		String data = JSONUtil.toJson(list,properties);
		result.addData("datagridData", data);
		
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.StuClassService#querySelectedClassCombobox(java.lang.Integer)
	 */
	@Override
	public String querySelectedClassCombobox(Integer courseId,Integer studentId,Integer scStatus) {
		List<StuClass> list = stuClassDAO.querySelectedClassCombobox(courseId,studentId,scStatus);
		String[] properties = {"clazz.classId","clazz.className","stuClassId","lessons","courseProgress"};
		String jsonString = JSONUtil.toJsonWithoutRows(list,properties);
		return jsonString;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.StuClassService#selectedValidate()
	 */
	@Override
	public ServiceResult selectedValidate(StuClass model) {
		ServiceResult result = new ServiceResult(false);

		List<StuClass> list = stuClassDAO.querySelectedClassCombobox(null,model.getStudent().getStudentId(),0);
		
		boolean existence = false;
		if(list.size()!=0){
			existence = true;
		}
		result.addData("existence", existence);
		result.setIsSuccess(true);
		return result;
	}
	
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.StuClassService#getStudentTree()
	 */
	@Override
	public ServiceResult getStudentTree(StuClass model,Date lessonDegreeDate,Integer lessonDegreeId) {
		ServiceResult result = new ServiceResult(false);
		
		List<StuClass> stuClassList = stuClassDAO.query(model,lessonDegreeDate);
		
		LessonDegree lessonDegree = lessonDegreeDAO.load(lessonDegreeId);
		List<Attend> attendList = attendDAO.query("lessonDegree", lessonDegree);
		
		StringBuilder treeStr = new StringBuilder();
		
		treeStr.append("[");
		for(int i=0;i<stuClassList.size();i++){
			StuClass stuClass = stuClassList.get(i);
			
			if(treeStr.length()>1){
				treeStr.append(",");
			}
			
			//跳过已考勤的学生
			boolean flag = false;
			for(Attend attend:attendList){
				if(stuClass.getStuClassId()==attend.getStuClass().getStuClassId()){
					flag = true;
					break;
				}
			}
			if(flag){
				continue;
			}
			
			treeStr.append("{");
			treeStr.append(
					"\"id\":"+stuClass.getStudent().getStudentId()+","+
					"\"text\":\""+stuClass.getStudent().getStudentName()+"\"}"
			);
		}
		treeStr.append("]");
		
		result.addData("studentTree", treeStr.toString());
		
		result.setIsSuccess(true);
		return result;
	}
}
