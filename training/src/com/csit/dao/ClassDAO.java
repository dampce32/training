package com.csit.dao;

import java.util.List;

import com.csit.model.Clazz;
/**
 * 
 * @Description: 班级DAO
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-4
 * @author yk
 * @vesion 1.0
 */
public interface ClassDAO extends BaseDAO<Clazz, Integer> {
	/**
	 * 
	 * @Description: 分页查询班级
	 * @param
	 * @Create: 2013-3-4 下午03:34:10
	 * @author yk
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 * @return
	 * @throws Exception
	 */
	List<Clazz> query(Clazz model, Integer page, Integer rows,Integer status);
	/**
	 * 
	 * @Description: 统计班级
	 * @param
	 * @Create: 2013-3-4 下午03:34:28
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	Long getTotalCount(Clazz model,Integer status);
	/**
	 * 
	 * @Description: combobox查询指定校区、上课状态、讲师下的班级
	 * @Create: 2013-3-21 下午04:06:33
	 * @author yk
	 * @update logs
	 * @param schoolId
	 * @param lessonStatus
	 * @param teacherId
	 * @param model
	 * @return
	 */
	List<Clazz> queryInSSTCombobox(Integer courseId,Integer schoolId,
			Integer lessonStatus, Integer teacherId) ;
	
}
