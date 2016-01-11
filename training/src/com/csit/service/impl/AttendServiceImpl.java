package com.csit.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.csit.dao.AttendDAO;
import com.csit.dao.ClassDAO;
import com.csit.dao.LessonDegreeDAO;
import com.csit.dao.StuClassDAO;
import com.csit.model.Attend;
import com.csit.model.Clazz;
import com.csit.model.LessonDegree;
import com.csit.model.StuClass;
import com.csit.service.AttendService;
import com.csit.util.JSONUtil;
import com.csit.vo.ServiceResult;
/**
 * 
 * @Description: 学员出勤表Service实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-4-3
 * @author yk
 * @vesion 1.0
 */
@Service
public class AttendServiceImpl extends BaseServiceImpl<Attend, Integer>
		implements AttendService {
	@Resource
	private AttendDAO attendDAO;
	@Resource
	private StuClassDAO stuClassDAO;
	@Resource
	private ClassDAO classDAO;
	@Resource
	private LessonDegreeDAO lessonDegreeDAO;
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.AttendService#save(com.csit.model.Attend)
	 */
	@Override
	public ServiceResult save(Attend model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null){
			result.setMessage("请填写学员出勤信息");
			return result;
		}
		Attend oldModel = attendDAO.get(model.getAttendId());
		if(oldModel==null){
			result.setMessage("该学员出勤信息已不存在");
			return result;
		}
		
		StuClass stuClass = oldModel.getStuClass();
		LessonDegree lessonDegree = oldModel.getLessonDegree();
		//更新学员出勤情况
		switch(oldModel.getStatus()){
			case 0:{
				stuClass.setFactCount(stuClass.getFactCount()-1);
				lessonDegree.setFactCount(lessonDegree.getFactCount()-1);
				break;
			}
			case 1:{
				stuClass.setLateCount(stuClass.getLateCount()-1);
				lessonDegree.setLateCount(lessonDegree.getLateCount()-1);
				break;
			}
			case 2:{
				stuClass.setAdvanceCount(stuClass.getAdvanceCount()-1);
				lessonDegree.setAdvanceCount(lessonDegree.getAdvanceCount()-1);
				break;
			}
			case 3:{
				stuClass.setTruancyCount(stuClass.getTruancyCount()-1);
				lessonDegree.setTruancyCount(lessonDegree.getTruancyCount()-1);
				break;
			}
			case 4:{
				stuClass.setLeaveCount(stuClass.getLeaveCount()-1);
				lessonDegree.setLeaveCount(lessonDegree.getLeaveCount()-1);
				break;
			}
			default:{
				break;
			}
		}
		switch(model.getStatus()){
			case 0:{
				stuClass.setFactCount(stuClass.getFactCount()+1);
				lessonDegree.setFactCount(lessonDegree.getFactCount()+1);
				break;
			}
			case 1:{
				stuClass.setLateCount(stuClass.getLateCount()+1);
				lessonDegree.setLateCount(lessonDegree.getLateCount()+1);
				break;
			}
			case 2:{
				stuClass.setAdvanceCount(stuClass.getAdvanceCount()+1);
				lessonDegree.setAdvanceCount(lessonDegree.getAdvanceCount()+1);
				break;
			}
			case 3:{
				stuClass.setTruancyCount(stuClass.getTruancyCount()+1);
				lessonDegree.setTruancyCount(lessonDegree.getTruancyCount()+1);
				break;
			}
			case 4:{
				stuClass.setLeaveCount(stuClass.getLeaveCount()+1);
				lessonDegree.setLeaveCount(lessonDegree.getLeaveCount()+1);
				break;
			}
			default:{
				break;
			}
		}
		
		oldModel.setStatus(model.getStatus());
		oldModel.setNote(model.getNote());
		
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.AttendService#delete(com.csit.model.Attend)
	 */
	@Override
	public ServiceResult delete(Attend model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null||model.getAttendId()==null){
			result.setMessage("请选择要删除的学员出勤信息");
			return result;
		}
		Attend oldModel = attendDAO.load(model.getAttendId());
		if(oldModel==null){
			result.setMessage("该学员出勤信息已不存在");
			return result;
		}else{
			StuClass stuClass = oldModel.getStuClass();
			LessonDegree lessonDegree = oldModel.getLessonDegree();
			Clazz clazz = stuClass.getClazz();
			
			attendDAO.delete(oldModel);
			//更新学员课程进度
			stuClass.setCourseProgress(stuClass.getCourseProgress()-lessonDegree.getLessons());
			
			List<Attend> attendList = attendDAO.query("lessonDegree",lessonDegree);
			if(attendList.size()==0){
				//更新上课状态
				lessonDegree.setLessonStatus(0);
				//更新班级课程进度
				clazz.setCourseProgress(clazz.getCourseProgress()-lessonDegree.getLessons());
			}
			//更新学员出勤情况
			switch(oldModel.getStatus()){
				case 0:{
					stuClass.setFactCount(stuClass.getFactCount()-1);
					lessonDegree.setFactCount(lessonDegree.getFactCount()-1);
					break;
				}
				case 1:{
					stuClass.setLateCount(stuClass.getLateCount()-1);
					lessonDegree.setLateCount(lessonDegree.getLateCount()-1);
					break;
				}
				case 2:{
					stuClass.setAdvanceCount(stuClass.getAdvanceCount()-1);
					lessonDegree.setAdvanceCount(lessonDegree.getAdvanceCount()-1);
					break;
				}
				case 3:{
					stuClass.setTruancyCount(stuClass.getTruancyCount()-1);
					lessonDegree.setTruancyCount(lessonDegree.getTruancyCount()-1);
					break;
				}
				case 4:{
					stuClass.setLeaveCount(stuClass.getLeaveCount()-1);
					lessonDegree.setLeaveCount(lessonDegree.getLeaveCount()-1);
					break;
				}
				default:{
					break;
				}
			}
			//更新学员补课次数
			if(oldModel.getLessonDegree().getLessonType()==0){
				stuClass.setFillCount(stuClass.getFillCount()-1);
			}
		}
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.AttendService#query(com.csit.model.Attend, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public ServiceResult query(Attend model, Integer page, Integer rows) {
		ServiceResult result = new ServiceResult(false);
		
		List<Attend> list = attendDAO.query(model,page,rows);
		
		String[] properties = {"stuClass.student.studentId","stuClass.student.studentName",
				"stuClass.scStatus","lessonDegree.lessons","status","comeTime","goTime",
				"note","stuClass.courseProgress","stuClass.lessons:maxLessons","attendId"};
		
		String data = JSONUtil.toJson(list,properties);
		result.addData("datagridData", data);
		
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.AttendService#getTotalCount(com.csit.model.Attend)
	 */
	@Override
	public ServiceResult getTotalCount(Attend model) {
		ServiceResult result = new ServiceResult(false);
		Long data = attendDAO.getTotalCount(model);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.AttendService#batchSave(java.lang.String)
	 */
	@Override
	public ServiceResult batchSave(Attend model,String idStr) {
		ServiceResult result = new ServiceResult(false);
		
		Integer classId = model.getLessonDegree().getClazz().getClassId();
		
		String[] idList = idStr.split(",");
		
		//统计各项考勤人数
		int factCount = 0;
		int lateCount = 0;
		int advanceCount = 0;
		int truancyCount = 0;
		int leaveCount = 0;
		for(int i=0;i<idList.length;i++){
			Attend attend = new Attend();
			attend.setLessonDegree(model.getLessonDegree());
			
			StuClass stuClass = stuClassDAO.getStuClass(Integer.parseInt(idList[i]),classId,0,null);
			//更新学员课程进度
			stuClass.setCourseProgress(stuClass.getCourseProgress()+model.getLessonDegree().getLessons());
			//更新学员出勤情况
			switch(model.getStatus()){
				case 0:{
					stuClass.setFactCount(stuClass.getFactCount()+1);
					factCount++;
					break;
				}
				case 1:{
					stuClass.setLateCount(stuClass.getLateCount()+1);
					lateCount++;
					break;
				}
				case 2:{
					stuClass.setAdvanceCount(stuClass.getAdvanceCount()+1);
					advanceCount++;
					break;
				}
				case 3:{
					stuClass.setTruancyCount(stuClass.getTruancyCount()+1);
					truancyCount++;
					break;
				}
				case 4:{
					stuClass.setLeaveCount(stuClass.getLeaveCount()+1);
					leaveCount++;
					break;
				}
				default:{
					break;
				}
			}
			//更新学员补课次数
			if(model.getLessonDegree().getLessonType()==0){
				stuClass.setFillCount(stuClass.getFillCount()+1);
			}
			attend.setStuClass(stuClass);
			
			attend.setNote(model.getNote());
			attend.setStatus(model.getStatus());
			attendDAO.save(attend);
			
			//更新课程状态
			LessonDegree lessonDegree = lessonDegreeDAO.load(model.getLessonDegree().getLessonDegreeId());
			lessonDegree.setLessonStatus(1);
			//更新课程各项考勤人数
			lessonDegree.setFactCount(lessonDegree.getFactCount()+factCount);
			lessonDegree.setLateCount(lessonDegree.getLateCount()+lateCount);
			lessonDegree.setAdvanceCount(lessonDegree.getAdvanceCount()+advanceCount);
			lessonDegree.setTruancyCount(lessonDegree.getTruancyCount()+truancyCount);
			lessonDegree.setLeaveCount(lessonDegree.getLeaveCount()+leaveCount);
		}
		
		//更新班级课程进度
		if(model.getLessonDegree().getLessonStatus()==0){
			Clazz clazz = classDAO.load(classId);
			clazz.setCourseProgress(clazz.getCourseProgress()+model.getLessonDegree().getLessons());
		}
		
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.AttendService#todayAttendQuery(java.lang.Integer, java.lang.Integer, java.util.Date, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public ServiceResult todayAttendQuery(Integer studentId, Integer schoolId,
			Date lessonDegreeDate, Integer page, Integer rows) {
		ServiceResult result = new ServiceResult(false);
		
		List<Map<String, Object>> list = attendDAO.todayAttendQuery(studentId,schoolId,lessonDegreeDate,page,rows);
		
		for(Map<String, Object> map:list){
			map.put("time", map.get("startTime")+"-"+map.get("endTime"));
		}
		
		String[] properties = {"studentId","studentName","classId","className",
				"scStatus","lessonDegreeDate","time","courseProgress","scLessons",
				"status","lessons","note","lessonDegreeId","attendId","lessonStatus"};
		
		String data = JSONUtil.toJson(list,properties);
		result.addData("datagridData", data);
		
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.AttendService#getTotalCount(java.lang.Integer, java.lang.Integer, java.util.Date)
	 */
	@Override
	public ServiceResult todayAttendTotal(Integer studentId, Integer schoolId,
			Date lessonDegreeDate) {
		ServiceResult result = new ServiceResult(false);
		Integer data = attendDAO.todayAttendTotal(studentId,schoolId,lessonDegreeDate);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}
	
	public ServiceResult batchFact(Attend model) {
		
		ServiceResult result = new ServiceResult(false);
		
		Integer classId = model.getLessonDegree().getClazz().getClassId();
		
		//筛选出要操作的学生id
		List<Integer> idList = new ArrayList<Integer>(); 
		List<StuClass> stuClassList = stuClassDAO.query(model.getStuClass(),model.getLessonDegree().getLessonDegreeDate());
		//保存之前查询本次课的点名表，用于判断学员是否已点名
		LessonDegree ld = lessonDegreeDAO.load(model.getLessonDegree().getLessonDegreeId());
		List<Attend> attendList = attendDAO.query("lessonDegree", ld);
		for(int i=0;i<stuClassList.size();i++){
			StuClass stuClass = stuClassList.get(i);
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
			idList.add(stuClass.getStudent().getStudentId());
		}
		
		//统计各项考勤人数
		int factCount = 0;
		
		for(int i=0;i<idList.size();i++){
			Attend attend = new Attend();
			attend.setLessonDegree(model.getLessonDegree());
			
			StuClass stuClass = stuClassDAO.getStuClass(idList.get(i),classId,0,null);
			//更新学员课程进度
			stuClass.setCourseProgress(stuClass.getCourseProgress()+model.getLessonDegree().getLessons());
			//更新学员出勤情况
			stuClass.setFactCount(stuClass.getFactCount()+1);
			factCount++;
			//更新学员补课次数
			if(model.getLessonDegree().getLessonType()==0){
				stuClass.setFillCount(stuClass.getFillCount()+1);
			}
			attend.setStuClass(stuClass);
			
			attend.setNote(model.getNote());
			attend.setStatus(0);
			attendDAO.save(attend);
			
			//更新课程状态
			LessonDegree lessonDegree = lessonDegreeDAO.load(model.getLessonDegree().getLessonDegreeId());
			lessonDegree.setLessonStatus(1);
			//更新课程各项考勤人数
			lessonDegree.setFactCount(lessonDegree.getFactCount()+factCount);
		}
		
		
		//更新班级课程进度
		if(model.getLessonDegree().getLessonStatus()==0){
			Clazz clazz = classDAO.load(classId);
			clazz.setCourseProgress(clazz.getCourseProgress()+model.getLessonDegree().getLessons());
		}
		
		result.setIsSuccess(true);
		return result;
	}
	
}
