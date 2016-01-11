package com.csit.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.csit.model.Attend;
/**
 * 
 * @Description: 学员出勤表DAO
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-4-3
 * @author yk
 * @vesion 1.0
 */
public interface AttendDAO extends BaseDAO<Attend, Integer> {
	/**
	 * 
	 * @Description: 分页查询学院出勤表
	 * @Create: 2013-4-3 上午11:10:25
	 * @author yk
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	List<Attend> query(Attend model, Integer page, Integer rows);
	/**
	 * 
	 * @Description: 统计学员出勤表
	 * @Create: 2013-4-3 上午11:10:38
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 */
	Long getTotalCount(Attend model);
	/**
	 * 
	 * @Description: 查询今日签到
	 * @Create: 2013-4-18 下午04:37:40
	 * @author yk
	 * @update logs
	 * @param studentId
	 * @param schoolId
	 * @param lessonDegreeDate
	 * @param page
	 * @param rows
	 * @return
	 */
	List<Map<String, Object>> todayAttendQuery(Integer studentId,Integer schoolId,Date lessonDegreeDate,Integer page,Integer rows);
	/**
	 * 
	 * @Description: 统计今日签到 
	 * @Create: 2013-4-18 下午04:39:15
	 * @author yk
	 * @update logs
	 * @return
	 */
	Integer todayAttendTotal(Integer studentId,Integer schoolId,Date lessonDegreeDate);
}
