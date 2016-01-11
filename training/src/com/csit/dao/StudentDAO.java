package com.csit.dao;

import java.util.List;

import com.csit.model.Student;
/**
 * 
 * @Description:学员Dao
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-5
 * @author jcf
 * @vesion 1.0
 */
public interface StudentDAO extends BaseDAO<Student,Integer>{

	/**
	 * 
	 * @Description: 分页查询学员
	 * @Create: 2013-3-5 上午10:23:14
	 * @author jcf
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	List<Student> query(Student model, Integer page, Integer rows);
	/**
	 * 
	 * @Description: 统计学员
	 * @Create: 2013-3-5 上午10:23:24
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	Long getTotalCount(Student model);
	/**
	 * 
	 * @Description: 查询本校区的学员
	 * @Create: 2013-4-23 上午10:25:03
	 * @author jcf
	 * @update logs
	 * @param schoolCode
	 * @return
	 */
	List<Student> queryCombobox(String schoolCode);
	/**
	 * 
	 * @Description: 教师查询自己的学员
	 * @Create: 2013-4-24 下午02:27:23
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	List<Student> queryCombobox(Student model);
}
