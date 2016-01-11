package com.csit.dao;

import java.util.List;

import com.csit.model.CourseType;
/**
 * @Description:课程类型DAO
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author lys
 */
public interface CourseTypeDAO extends BaseDAO<CourseType,Integer>{
	/**
	 * @Description: 分页查询课程类型
	 * @Created Time: 2013-2-26 下午9:20:54
	 * @Author lys
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	List<CourseType> query(CourseType model, Integer page, Integer rows);
	/**
	 * @Description: 统计课程类型
	 * @Created Time: 2013-2-26 下午9:22:20
	 * @Author lys
	 * @param model
	 * @return
	 */
	Long getTotalCount(CourseType model);

}
