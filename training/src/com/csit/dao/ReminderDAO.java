package com.csit.dao;

import java.util.List;
import java.util.Map;

import com.csit.model.Reminder;
/**
 * @Description:系统提醒DAO
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-4
 * @Author lys
 */
public interface ReminderDAO extends BaseDAO<Reminder,Integer>{
	/**
	 * @Description: 分页查询系统提醒
	 * @Created Time: 2013-3-5 上午9:22:31
	 * @Author lys
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	List<Reminder> query(Reminder model, Integer page, Integer rows);
	/**
	 * @Description: 统计系统提醒
	 * @Created Time: 2013-3-5 上午9:23:34
	 * @Author lys
	 * @param model
	 * @return
	 */
	Long getTotalCount(Reminder model);
	/**
	 * @Description: 取得最大排序
	 * @Created Time: 2013-3-5 上午9:32:57
	 * @Author lys
	 * @return
	 */
	Integer getMaxArray();
	/**
	 * @Description: 取得当前用户的系统提醒
	 * @Created Time: 2013-3-5 下午4:42:35
	 * @Author lys
	 * @param i
	 * @return
	 */
	List<Map<String, Object>> getCurrentUserReminder(Integer userId);

}
