package com.csit.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.csit.model.LessonDegree;
/**
 * 
 * @Description: 排课表DAO
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-7
 * @author yk
 * @vesion 1.0
 */
public interface LessonDegreeDAO extends BaseDAO<LessonDegree, Integer> {
	/**
	 * 
	 * @Description: 分页查询排课
	 * @param
	 * @Create: 2013-3-7 下午02:08:30
	 * @author yk
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 * @return
	 * @throws Exception
	 */
	List<LessonDegree> query(LessonDegree model, Integer page, Integer rows);
	/**
	 * 
	 * @Description: 统计排课
	 * @param
	 * @Create: 2013-3-7 下午02:21:06
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	Long getTotalCount(LessonDegree model);
	/**
	 * 
	 * @Description: 查询课程总表（周）
	 * @param
	 * @Create: 2013-3-18 下午03:52:15
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	List<LessonDegree> weekTableQuery(Date weekStart,Date weekEnd,LessonDegree model,Integer schoolId,Integer queryParam);
	/**
	 * 
	 * @Description: 查询指定班级的最大排课日期
	 * @Create: 2013-3-25 下午03:51:15
	 * @author yk
	 * @update logs
	 * @param lessonDegreeId
	 * @return
	 */
	Date getMaxLessonDegreeDate(Integer classId);
	/**
	 * 
	 * @Description: 查询某个学生的上课信息
	 * @Create: 2013-4-11 下午05:02:06
	 * @author jcf
	 * @update logs
	 * @param stuId
	 * @param page
	 * @param rows
	 * @return
	 */
	List<Map<String, Object>> getLessonDegreeByStu(Integer stuId, Integer page, Integer rows);
	/**
	 * 
	 * @Description: 统计某个学生的上课信息
	 * @Create: 2013-4-12 上午11:02:35
	 * @author jcf
	 * @update logs
	 * @param stuId
	 * @return
	 */
	Integer getTotalCountByStu(Integer stuId);
}
