package com.csit.service;

import java.util.Date;

import com.csit.model.Attend;
import com.csit.vo.ServiceResult;
/**
 * 
 * @Description: 学员出勤表Service
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-4-3
 * @author yk
 * @vesion 1.0
 */
public interface AttendService extends BaseService<Attend, Integer> {
	/**
	 * 
	 * @Description: 保存学员出勤表
	 * @Create: 2013-4-11 下午04:15:30
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult save(Attend model);
	/**
	 * 
	 * @Description: 删除学员出勤表
	 * @Create: 2013-4-3 上午11:34:35
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult delete(Attend model);
	/**
	 *  
	 * @Description: 分页查询学员出勤表 
	 * @Create: 2013-4-3 上午11:34:46
	 * @author yk
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	ServiceResult query(Attend model, Integer page, Integer rows);
	/**
	 * 
	 * @Description: 统计学员出勤表
	 * @Create: 2013-4-3 上午11:35:00
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult getTotalCount(Attend model);
	/**
	 * 
	 * @Description: 批量保存学员出勤表
	 * @Create: 2013-4-7 下午10:01:35
	 * @author yk
	 * @update logs
	 * @param idStr
	 * @return
	 */
	ServiceResult batchSave(Attend model,String idStr);
	/**
	 * 
	 * @Description: 查询今日签到
	 * @Create: 2013-4-18 下午04:47:04
	 * @author yk
	 * @update logs
	 * @param studentId
	 * @param schoolId
	 * @param lessonDegreeDate
	 * @param page
	 * @param rows
	 * @return
	 */
	ServiceResult todayAttendQuery(Integer studentId,Integer schoolId,Date lessonDegreeDate,Integer page,Integer rows);
	/**
	 * 
	 * @Description: 统计今日签到
	 * @Create: 2013-4-18 下午04:47:17
	 * @author yk
	 * @update logs
	 * @param studentId
	 * @param schoolId
	 * @param lessonDegreeDate
	 * @return
	 */
	ServiceResult todayAttendTotal(Integer studentId,Integer schoolId,Date lessonDegreeDate);
	/**
	 * 
	 * @Description: 批量添加出勤学员
	 * @Create: 2013-4-25 下午04:13:29
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult batchFact(Attend model);
}
