package com.csit.service;

import com.csit.model.Course;
import com.csit.vo.ServiceResult;
/**
 * 
 * @Description: 课程Service
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-28
 * @author yk
 * @vesion 1.0
 */
public interface CourseService extends BaseService<Course, Integer> {
	/**
	 * 
	 * @Description: 保存课程
	 * @param
	 * @Create: 2013-2-28 上午09:55:34
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult save(Course model);
	/**
	 * 
	 * @Description: 删除课程
	 * @param
	 * @Create: 2013-2-28 上午09:55:47
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult delete(Course model);
	/**
	 * 
	 * @Description: 分页查询课程
	 * @param
	 * @Create: 2013-2-28 上午09:55:57
	 * @author yk
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult query(Course model, Integer page, Integer rows, String courseIds);
	/**
	 * 
	 * @Description: 统计课程
	 * @param
	 * @Create: 2013-2-28 上午09:56:08
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult getTotalCount(Course model, String courseIds);
	/**
	 * 
	 * @Description: combobox查询
	 * @param
	 * @Create: 2013-2-28 上午09:56:18
	 * @author yk
	 * @update logs
	 * @return
	 * @return
	 * @throws Exception
	 */
	String queryCombobox();
	/**
	 * 
	 * @Description: 修改课程状态
	 * @param
	 * @Create: 2013-3-1 下午04:32:52
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult updateStatus(Course model);
	/**
	 * 
	 * @Description: combobox查询指定类型的课程
	 * @param
	 * @Create: 2013-3-5 上午09:30:13
	 * @author yk
	 * @update logs
	 * @param courseTypeId
	 * @return
	 * @return
	 * @throws Exception
	 */
	String queryIsTypeCombobox(Integer courseTypeId);
}
