package com.csit.dao;

import java.util.List;

import com.csit.model.Classroom;

/**
 * 
 * @Description: 教室DAO
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-28
 * @author yk
 * @vesion 1.0
 */
public interface ClassroomDAO extends BaseDAO<Classroom, Integer> {
	/**
	 * 
	 * @Description: 分页查询教室
	 * @param
	 * @Create: 2013-2-28 下午11:55:37
	 * @author yk
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 * @return
	 * @throws Exception
	 */
	List<Classroom> query(Classroom model, Integer page, Integer rows);
	/**
	 * 
	 * @Description: 统计教室
	 * @param
	 * @Create: 2013-2-28 下午11:55:59
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	Long getTotalCount(Classroom model);
	/**
	 * 
	 * @Description: combobox查询指定校区的教室
	 * @param
	 * @Create: 2013-3-5 下午01:54:24
	 * @author yk
	 * @update logs
	 * @param schoolCode
	 * @return
	 * @return
	 * @throws Exception
	 */
	List<Classroom> queryInSchoolCombobox(Integer schoolId);
}
