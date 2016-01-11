package com.csit.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.csit.dao.ClassDAO;
import com.csit.dao.ClassroomDAO;
import com.csit.dao.HolidayDAO;
import com.csit.dao.LessonDegreeDAO;
import com.csit.dao.UserDAO;
import com.csit.model.Classroom;
import com.csit.model.Clazz;
import com.csit.model.Holiday;
import com.csit.model.LessonDegree;
import com.csit.model.User;
import com.csit.service.LessonDegreeService;
import com.csit.util.JSONUtil;
import com.csit.vo.ServiceResult;
/**
 * 
 * @Description: 排课表Service实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-7
 * @author yk
 * @vesion 1.0
 */
@Service
public class LessonDegreeServiceImpl extends
		BaseServiceImpl<LessonDegree, Integer> implements LessonDegreeService {
	@Resource
	private LessonDegreeDAO lessonDegreeDAO;
	@Resource
	private ClassDAO classDAO;
	@Resource
	private UserDAO userDAO;
	@Resource
	private ClassroomDAO classroomDAO;
	@Resource
	private HolidayDAO holidayDAO;
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.LessonDegreeService#save(com.csit.model.LessonDegree)
	 */
	@Override
	public ServiceResult save(LessonDegree model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null){
			result.setMessage("请填写排课信息");
			return result;
		}
		if(model.getLessonDegreeDate()==null){
			result.setMessage("请选择上课日期");
			return result;
		}
		if(StringUtils.isEmpty(model.getStartTime())){
			result.setMessage("请选择上课时间");
			return result;
		}
		if(StringUtils.isEmpty(model.getEndTime())){
			result.setMessage("请选择下课时间");
			return result;
		}
		if(model.getLessons()==null){
			result.setMessage("请填写本次课时");
			return result;
		}
		if(model.getTeacher()==null||model.getTeacher().getUserId()==null){
			result.setMessage("请选择讲师");
			return result;
		}
		if(model.getClassroom()==null||model.getClassroom().getClassroomId()==null){
			result.setMessage("请选择教室");
			return result;
		}
		if(model.getUser()==null||model.getUser().getUserId()==null){
			result.setMessage("请选择经办人");
			return result;
		}
		Clazz clazz = classDAO.load(model.getClazz().getClassId());
		if(model.getLessonDegreeId()==null){//新增
			clazz.setArrangeLessons(clazz.getArrangeLessons()+model.getLessons());
			if(model.getLessonType()==null){
				model.setLessonType(1);
			}
			model.setLessonStatus(0);
			model.setFactCount(0);
			model.setLateCount(0);
			model.setAdvanceCount(0);
			model.setTruancyCount(0);
			model.setLeaveCount(0);
			lessonDegreeDAO.save(model);
			result.addData("arrangeLessons",clazz.getArrangeLessons());
		}else{
			LessonDegree oldModel = lessonDegreeDAO.load(model.getLessonDegreeId());
			if(oldModel==null){
				result.setMessage("该排课课次已不存在");
				return result;
			}
			oldModel.setSubject(model.getSubject());
			oldModel.setLessonDegreeDate(model.getLessonDegreeDate());
			oldModel.setStartTime(model.getStartTime());
			oldModel.setEndTime(model.getEndTime());
			oldModel.setLessons(model.getLessons());
			oldModel.setLessonType(model.getLessonType());
			oldModel.setTeacher(model.getTeacher());
			oldModel.setClassroom(model.getClassroom());
			oldModel.setNote(model.getNote());
			oldModel.setUser(model.getUser());
		}
		clazz.setEndDate(lessonDegreeDAO.getMaxLessonDegreeDate(model.getClazz().getClassId()));
		
		result.setIsSuccess(true);
		result.addData("lessonDegreeId", model.getLessonDegreeId());
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.LessonDegreeService#delete(com.csit.model.LessonDegree)
	 */
	@Override
	public ServiceResult delete(LessonDegree model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null||model.getLessonDegreeId()==null){
			result.setMessage("请选择要删除的排课课次");
			return result;
		}
		LessonDegree oldModel = lessonDegreeDAO.load(model.getLessonDegreeId());
		if(oldModel==null){
			result.setMessage("该排课课次已不存在");
			return result;
		}else{
			Clazz clazz = classDAO.load(model.getClazz().getClassId());
			lessonDegreeDAO.delete(oldModel);
			int newArrangeLessons = clazz.getArrangeLessons()-model.getLessons();
			if(newArrangeLessons>=0){
				clazz.setArrangeLessons(newArrangeLessons);
			}else{
				clazz.setArrangeLessons(0);
			}
			clazz.setEndDate(lessonDegreeDAO.getMaxLessonDegreeDate(model.getClazz().getClassId()));
			result.addData("arrangeLessons",clazz.getArrangeLessons());
		}
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.LessonDegreeService#query(com.csit.model.LessonDegree, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public ServiceResult query(LessonDegree model, Integer page, Integer rows) {
		ServiceResult result = new ServiceResult(false);
		
		List<LessonDegree> list = lessonDegreeDAO.query(model,page,rows);
		
		for(LessonDegree lessonDegree:list){
			lessonDegree.setTime(lessonDegree.getStartTime()+'-'+lessonDegree.getEndTime());
		}
		
		String[] properties = {"lessonDegreeId","startTime","endTime","subject","lessons",
				"teacher.userId:teacherId","user.userId","classroom.classroomId","lessonDegreeDate",
				"note","lessonType","teacher.userName:teacherName","user.userName","time",
				"factCount","truancyCount","lateCount","advanceCount","leaveCount",
				"lessonStatus","classroom.classroomName","clazz.classId","clazz.className"};
		
		String data = JSONUtil.toJson(list,properties);
		result.addData("datagridData", data);
		
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.LessonDegreeService#getTotalCount(com.csit.model.LessonDegree)
	 */
	@Override
	public ServiceResult getTotalCount(LessonDegree model) {
		ServiceResult result = new ServiceResult(false);
		Long data = lessonDegreeDAO.getTotalCount(model);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.LessonDegreeService#autoDegreeSave(com.csit.model.LessonDegree)
	 */
	@Override
	public ServiceResult autoDegreeSave(String params,String firstDegreeDate,String waitDegree,String userId,LessonDegree model) {
		ServiceResult result = new ServiceResult(false);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		//排课起始日转化为Date型
		Date dtStart = null;
		try {
			dtStart = sdf.parse(firstDegreeDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//获取排课起始日的星期
		Calendar cal = Calendar.getInstance();
        cal.setTime(dtStart);
        int weekStart = cal.get(Calendar.DAY_OF_WEEK) - 1;
        
        Clazz clazz = classDAO.load(model.getClazz().getClassId());
        
        //剩余可排课时数
        int newWaitDegree = Integer.parseInt(waitDegree);;
        //完成自动排课循环的次数
        int count = 0;
		do{
			//所有的排课参数
			String[] allInfo = params.split(";");
			for(int i=0;i<allInfo.length;i++){
				//index of rowInfo: 0 - 周期, 1 - 时间段, 2 - 课时, 3 - 讲师Id, 4 - 教室Id
				String[] rowInfo = allInfo[i].split(",");
				LessonDegree newModel = new LessonDegree();
				
				//用户选择的星期
				int week = Integer.parseInt(rowInfo[0]);
				if(weekStart<week){
					cal.add( Calendar.DAY_OF_MONTH, (week-weekStart)+(count*7) );
				}else{
					cal.add( Calendar.DAY_OF_MONTH, (week-weekStart+7)+(count*7) );
				}
				
				//当前日期是假期时跳出本次循环
				List<Holiday> holidayList = holidayDAO.queryAll();
				boolean flag = false;
				for(Holiday holiday:holidayList){
					//上课日期小于假期开始时间
					boolean isBefore = cal.getTime().before(holiday.getStartDate());
					//上课日期大于假期结束时间
					boolean isAfter = cal.getTime().after(holiday.getEndDate());
					//假期
					boolean isHoliday = !(isBefore||isAfter);
					if(isHoliday){
						flag = true;
						break;
					}
				}
				if(flag){
					cal.setTime(dtStart);
					continue;
				}
				
				//时间段
				String[] time = rowInfo[1].split("-");
				
				//课时
				int lessons = Integer.parseInt(rowInfo[2]);
				newWaitDegree -= lessons;
				
				//讲师
				User teacher = userDAO.load(Integer.parseInt(rowInfo[3]));
				
				//教室
				Classroom classroom = classroomDAO.load(Integer.parseInt(rowInfo[4]));
				
				//经办人
				User user = userDAO.load(Integer.parseInt(userId));
				
				newModel.setLessonDegreeDate(cal.getTime());
				newModel.setStartTime(time[0]);
				newModel.setEndTime(time[1]);
				newModel.setLessons(lessons);
				newModel.setTeacher(teacher);
				newModel.setClassroom(classroom);
				newModel.setUser(user);
				newModel.setLessonType(1);
				newModel.setLessonStatus(0);
				clazz.setArrangeLessons(clazz.getArrangeLessons()+lessons);
				newModel.setClazz(clazz);
				newModel.setFactCount(0);
				newModel.setLateCount(0);
				newModel.setAdvanceCount(0);
				newModel.setTruancyCount(0);
				newModel.setLeaveCount(0);
				lessonDegreeDAO.save(newModel);
				
				//初始化排课起始日
				cal.setTime(dtStart);
				
				//可排课时数为零时停止排课
				if(newWaitDegree<=0){
					break;
				}
			}
			count++;
		}while(newWaitDegree>0);
		clazz.setEndDate(lessonDegreeDAO.getMaxLessonDegreeDate(model.getClazz().getClassId()));
		result.addData("arrangeLessons",clazz.getArrangeLessons());
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.LessonDegreeService#clear()
	 */
	@Override
	public ServiceResult clear(String idStr,String lessonStr,LessonDegree model) {
		ServiceResult result = new ServiceResult(false);
		
		String[] idList = idStr.split(",");
		String[] lessonList = lessonStr.split(",");
		Clazz clazz = classDAO.load(model.getClazz().getClassId());
		for(int i=0;i<idList.length;i++){
			LessonDegree oldModel = lessonDegreeDAO.get(Integer.parseInt(idList[i]));
			if(oldModel==null){
				result.setMessage("该排课课次已不存在");
				return result;
			}else{
				lessonDegreeDAO.delete(oldModel);
				int newArrangeLessons = clazz.getArrangeLessons()-Integer.parseInt(lessonList[i]);
				if(newArrangeLessons>=0){
					clazz.setArrangeLessons(newArrangeLessons);
				}else{
					clazz.setArrangeLessons(0);
				}
			}
		}
		clazz.setEndDate(null);
		result.addData("arrangeLessons",clazz.getArrangeLessons());
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.LessonDegreeService#weekTableQuery(com.csit.model.LessonDegree)
	 */
	@Override
	public ServiceResult weekTableQuery(String toSunday,String toWeekOfToday,LessonDegree model,Integer schoolId,Integer queryParam) {
		ServiceResult result = new ServiceResult(false);
		
		Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH,Integer.parseInt(toSunday));
        //本周日
        Date weekStart = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH,6);
        //本周六
        Date weekEnd = cal.getTime();
        
		List<LessonDegree> lessonDegreeList = lessonDegreeDAO.weekTableQuery(weekStart,weekEnd,model,schoolId,queryParam);
		
		Date today = new Date();
		//今天的星期
		Calendar c1 = Calendar.getInstance();
		c1.setTime(today);
        int todayWeek = c1.get(Calendar.DAY_OF_WEEK) - 1;
        //一周各天的日期
        Calendar c2 = Calendar.getInstance();
        
		//一个教室（老师）的行数
		int count = 0;
		//各个教室（老师）的行数
		List<Integer> countList = new ArrayList<Integer>();
		
		//queryParam: 0 - 按教室分组，1 - 按老师分组
		if(queryParam==0){
			if(lessonDegreeList.size()!=0&&lessonDegreeList.size()>1){
				count = 1;
				for(int i=1;i<lessonDegreeList.size();i++){
					if(lessonDegreeList.get(i).getClassroom()!=lessonDegreeList.get(i-1).getClassroom()){
						//保存前一个教室的行数
						countList.add(count);
						//重置行数
						count = 1;
					}else{
						count++;
					}
					//保存最后一个教室的行数
					if(i==lessonDegreeList.size()-1){
						countList.add(count);
					}
				}
			}else if(lessonDegreeList.size()==1){
				countList.add(1);
			}
			
			//循环行次
			int rows = 0;
			//返回前台的json数据
			StringBuilder weekTableData = new StringBuilder();
			weekTableData.append("{\"rows\":[");
			//各行list数据
			LessonDegree lessonDegree = null;
			//各个教室
			for(int i=0;i<countList.size();i++){
				StringBuilder Sunday = new StringBuilder();
				StringBuilder Monday = new StringBuilder();
				StringBuilder Tuesday = new StringBuilder();
				StringBuilder Wednesday = new StringBuilder();
				StringBuilder Thursday = new StringBuilder();
				StringBuilder Friday = new StringBuilder();
				StringBuilder Saturday = new StringBuilder();
				
				//各行数据
				lessonDegree = lessonDegreeList.get(rows);
				if(i==0){
					weekTableData.append("{");
				}else{
					weekTableData.append(",{");
				}
				
				weekTableData.append("\"classroomName\":\""+lessonDegree.getClassroom().getClassroomName()+"\",");
				
				//一个教室，不同行数
				for(int j=0;j<countList.get(i);j++){
					
					lessonDegree = lessonDegreeList.get(rows);
					
					Date lessonDegreeDate = lessonDegree.getLessonDegreeDate();
					String time = lessonDegree.getStartTime()+"-"+lessonDegree.getEndTime();
					String className = lessonDegree.getClazz().getClassName();
					
					for(int k=0;k<7;k++){
				        c2.setTime(today);
						c2.add(Calendar.DAY_OF_MONTH,(int)(k-todayWeek+Integer.parseInt(toWeekOfToday)*7));
						
						//日期转为字符串比较是否相等
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
					    String s1 = sdf.format(lessonDegreeDate); 
					    String s2 = sdf.format(c2.getTime()); 
						
			    		if(s1.equals(s2)){
			    			switch(k){
			    				case 0:
			    					Sunday.append(time+","+className+"<br/>");
			    					break;
			    				case 1:
			    					Monday.append(time+","+className+"<br/>");
			    					break;
			    				case 2:
			    					Tuesday.append(time+","+className+"<br/>");
			    					break;
			    				case 3:
			    					Wednesday.append(time+","+className+"<br/>");
			    					break;
			    				case 4:
			    					Thursday.append(time+","+className+"<br/>");
			    					break;
			    				case 5:
			    					Friday.append(time+","+className+"<br/>");
			    					break;
			    				case 6:
			    					Saturday.append(time+","+className+"<br/>");
			    					break;
			    				default:
			    					break;
			    			}
			    		}
					}
					rows++;
				}
				weekTableData.append("\"Sunday\":\""+Sunday+"\",");
				weekTableData.append("\"Monday\":\""+Monday+"\",");
				weekTableData.append("\"Tuesday\":\""+Tuesday+"\",");
				weekTableData.append("\"Wednesday\":\""+Wednesday+"\",");
				weekTableData.append("\"Thursday\":\""+Thursday+"\",");
				weekTableData.append("\"Friday\":\""+Friday+"\",");
				weekTableData.append("\"Saturday\":\""+Saturday+"\"}");
			}
			weekTableData.append("]}");
			
			result.addData("datagridData", weekTableData.toString());
		}else if(queryParam==1){
			if(lessonDegreeList.size()!=0&&lessonDegreeList.size()>1){
				count = 1;
				for(int i=1;i<lessonDegreeList.size();i++){
					if(lessonDegreeList.get(i).getTeacher()!=lessonDegreeList.get(i-1).getTeacher()){
						//保存前一个老师的行数
						countList.add(count);
						//重置行数
						count = 1;
					}else{
						count++;
					}
					//保存最后一个老师的行数
					if(i==lessonDegreeList.size()-1){
						countList.add(count);
					}
				}
			}else if(lessonDegreeList.size()==1){
				countList.add(1);
			}
			
			//循环行次
			int rows = 0;
			//返回前台的json数据
			StringBuilder weekTableData = new StringBuilder();
			weekTableData.append("{\"rows\":[");
			//各行list数据
			LessonDegree lessonDegree = null;
			//各个老师
			for(int i=0;i<countList.size();i++){
				StringBuilder Sunday = new StringBuilder();
				StringBuilder Monday = new StringBuilder();
				StringBuilder Tuesday = new StringBuilder();
				StringBuilder Wednesday = new StringBuilder();
				StringBuilder Thursday = new StringBuilder();
				StringBuilder Friday = new StringBuilder();
				StringBuilder Saturday = new StringBuilder();
				
				//各行数据
				lessonDegree = lessonDegreeList.get(rows);
				if(i==0){
					weekTableData.append("{");
				}else{
					weekTableData.append(",{");
				}
				
				weekTableData.append("\"teacherName\":\""+lessonDegree.getTeacher().getUserName()+"\",");
				
				//一个老师，不同行数
				for(int j=0;j<countList.get(i);j++){
					
					lessonDegree = lessonDegreeList.get(rows);
					
					Date lessonDegreeDate = lessonDegree.getLessonDegreeDate();
					String time = lessonDegree.getStartTime()+"-"+lessonDegree.getEndTime();
					String className = lessonDegree.getClazz().getClassName();
					
					for(int k=0;k<7;k++){
				        c2.setTime(today);
						c2.add(Calendar.DAY_OF_MONTH,(int)(k-todayWeek+Integer.parseInt(toWeekOfToday)*7));
						
						//日期转为字符串比较是否相等
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
					    String s1 = sdf.format(lessonDegreeDate); 
					    String s2 = sdf.format(c2.getTime()); 
						
			    		if(s1.equals(s2)){
			    			switch(k){
			    				case 0:
			    					Sunday.append(time+","+className+"<br/>");
			    					break;
			    				case 1:
			    					Monday.append(time+","+className+"<br/>");
			    					break;
			    				case 2:
			    					Tuesday.append(time+","+className+"<br/>");
			    					break;
			    				case 3:
			    					Wednesday.append(time+","+className+"<br/>");
			    					break;
			    				case 4:
			    					Thursday.append(time+","+className+"<br/>");
			    					break;
			    				case 5:
			    					Friday.append(time+","+className+"<br/>");
			    					break;
			    				case 6:
			    					Saturday.append(time+","+className+"<br/>");
			    					break;
			    				default:
			    					break;
			    			}
			    		}
					}
					rows++;
				}
				weekTableData.append("\"Sunday\":\""+Sunday+"\",");
				weekTableData.append("\"Monday\":\""+Monday+"\",");
				weekTableData.append("\"Tuesday\":\""+Tuesday+"\",");
				weekTableData.append("\"Wednesday\":\""+Wednesday+"\",");
				weekTableData.append("\"Thursday\":\""+Thursday+"\",");
				weekTableData.append("\"Friday\":\""+Friday+"\",");
				weekTableData.append("\"Saturday\":\""+Saturday+"\"}");
			}
			weekTableData.append("]}");
			
			result.addData("datagridData", weekTableData.toString());
		}
		
		result.setIsSuccess(true);
		return result;
	}
	@Override
	public ServiceResult findByStu(Integer stuId, Integer page, Integer rows) {

		ServiceResult result = new ServiceResult(false);
		List<Map<String,Object>> lessonList=lessonDegreeDAO.getLessonDegreeByStu(stuId, page, rows);

		String[] properties = { "状态", "课题","上课日期","上课时间","下课时间","班号", "班级名称","教室","讲师","课时", "家庭作业","课堂表现"};

		String data = JSONUtil.toJson(lessonList, properties);
		result.addData("datagridData",data);
		result.setIsSuccess(true);
		return result;
	}
	@Override
	public ServiceResult getTotalCountByStu(Integer stuId) {
		ServiceResult result = new ServiceResult(false);
		Integer data = lessonDegreeDAO.getTotalCountByStu(stuId);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}
	
}
