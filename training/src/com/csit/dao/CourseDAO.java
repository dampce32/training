package com.csit.dao;

import java.util.List;

import com.csit.model.Course;

/**
 * 
 * @Description: 课程DAO
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-28
 * @author yk
 * @vesion 1.0
 */
public interface CourseDAO extends BaseDAO<Course,Integer> {
	/**
	 * 
	 * @Description: 分页查询课程
	 * @param
	 * @Create: 2013-2-28 上午09:25:47
	 * @author yk
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 * @return
	 * @throws Exception
	 */
	List<Course> query(Course model, Integer page, Integer rows, Integer[] courseIdArr);
	/**
	 * 
	 * @Description: 统计课程
	 * @param
	 * @Create: 2013-2-28 上午09:26:05
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	Long getTotalCount(Course model, Integer[] courseIdArr);
	/**
	 * 
	 * @Description: combobox查询指定类型的课程
	 * @param
	 * @Create: 2013-3-5 上午09:34:14
	 * @author yk
	 * @update logs
	 * @param courseTypeId
	 * @return
	 * @return
	 * @throws Exception
	 */
	List<Course> queryIsTypeCombobox(Integer courseTypeId);
}
