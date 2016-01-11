package com.csit.service;

import com.csit.model.Reminder;
import com.csit.vo.ServiceResult;
/**
 * @Description:系统提醒Service
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-4
 * @Author lys
 */
public interface ReminderService extends BaseService<Reminder,Integer>{
	/**
	 * @Description: 分页查询系统提醒 
	 * @Created Time: 2013-3-5 上午9:15:12
	 * @Author lys
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	ServiceResult query(Reminder model, Integer page, Integer rows);
	/**
	 * @Description: 统计系统提醒
	 * @Created Time: 2013-3-5 上午9:15:34
	 * @Author lys
	 * @param model
	 * @return
	 */
	ServiceResult getTotalCount(Reminder model);
	/**
	 * @Description: 保存系统提醒单
	 * @Created Time: 2013-3-5 上午9:16:56
	 * @Author lys
	 * @param model
	 * @param delReminderDetailIds
	 * @param reminderDetailIds
	 * @param reminderItemIdArray
	 * @return
	 */
	ServiceResult save(Reminder model, String delReminderDetailIds,
			String reminderDetailIds, String reminderItemIds);
	/**
	 * @Description: 打开初始化
	 * @Created Time: 2013-3-5 上午9:17:22
	 * @Author lys
	 * @param model
	 * @return
	 */
	ServiceResult init(Reminder model);
	/**
	 * @Description: 批量删除系统提醒单
	 * @Created Time: 2013-3-5 上午9:18:07
	 * @Author lys
	 * @param ids
	 * @return
	 */
	ServiceResult mulDelete(String ids);
	/**
	 * @Description: 更新系统提醒单状态
	 * @Created Time: 2013-3-5 上午9:18:59
	 * @Author lys
	 * @param ids
	 * @param model 
	 * @return
	 */
	ServiceResult mulUpdateStatus(String ids, Reminder model);
	/**
	 * @Description: 首页显示当前用户的系统提醒
	 * @Created Time: 2013-3-5 下午4:26:33
	 * @Author lys
	 * @param userId
	 * @param schoolCode 
	 * @param schoolId 
	 * @return
	 */
	ServiceResult getCurrentUser(String userId, String schoolId, String schoolCode);
	/**
	 * @Description: 更新系统提醒顺序
	 * @Created Time: 2013-3-6 下午4:28:33
	 * @Author lys
	 * @param reminderId
	 * @param updateReminderId
	 * @return
	 */
	ServiceResult updateArray(Integer reminderId, Integer updateReminderId);

}
