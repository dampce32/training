package com.csit.dao;

import java.util.List;

import com.csit.model.Holiday;
/**
 * 
 * @Description: 假期表DAO
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-28
 * @author yk
 * @vesion 1.0
 */
public interface HolidayDAO extends BaseDAO<Holiday, Integer> {
	/**
	 * 
	 * @Description: 分页查询假期表
	 * @param
	 * @Create: 2013-2-28 下午07:21:19
	 * @author yk
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 * @return
	 * @throws Exception
	 */
	List<Holiday> query(Holiday model, Integer page, Integer rows);
	/**
	 * 
	 * @Description: 统计假期表
	 * @param
	 * @Create: 2013-2-28 下午07:23:02
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	Long getTotalCount(Holiday model);
}
