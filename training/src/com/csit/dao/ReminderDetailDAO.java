package com.csit.dao;

import java.util.List;

import com.csit.model.Reminder;
import com.csit.model.ReminderDetail;
/**
 * @Description:系统提醒明细DAO
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-4
 * @Author lys
 */
public interface ReminderDetailDAO extends BaseDAO<ReminderDetail,Integer>{
	/**
	 * @Description: 
	 * @Created Time: 2013-3-5 上午9:47:14
	 * @Author lys
	 * @param reminderId
	 * @return
	 */
	List<ReminderDetail> queryByReminder(Reminder reminder);
	/**
	 * @Description: 统计数量
	 * @Created Time: 2013-3-6 上午9:00:29
	 * @Author lys
	 * @param countSql
	 * @return
	 */
	Long count(String countSql);

}
