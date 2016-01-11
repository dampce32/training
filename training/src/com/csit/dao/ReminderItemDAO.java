package com.csit.dao;

import java.util.List;

import com.csit.model.ReminderItem;

/**
 * @Description:系统提醒项DAO
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-4
 * @Author lys
 */
public interface ReminderItemDAO extends BaseDAO<ReminderItem,Integer>{
	/**
	 * @Description: 分页查询系统提醒项
	 * @Created Time: 2013-3-4 下午4:21:22
	 * @Author lys
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	List<ReminderItem> query(ReminderItem model, Integer page, Integer rows);
	/**
	 * @Description: 统计系统提醒项
	 * @Created Time: 2013-3-4 下午4:22:01
	 * @Author lys
	 * @param model
	 * @return
	 */
	Long getTotalCount(ReminderItem model);
	/**
	 * @Description: 系统提醒时分页选择查询系统提醒项
	 * @Created Time: 2013-3-4 下午10:17:19
	 * @Author lys
	 * @param idArray
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	List<ReminderItem> selectQuery(Integer[] idArray, ReminderItem model,
			Integer page, Integer rows);
	/**
	 * @param idArray 
	 * @Description: 系统提醒时统计选择查询系统提醒项
	 * @Created Time: 2013-3-4 下午10:17:56
	 * @Author lys
	 * @param model
	 * @return
	 */
	Long getTotalCountSelectQuery(Integer[] idArray, ReminderItem model);

}
