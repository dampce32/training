package com.csit.service;

import com.csit.model.ReminderItem;
import com.csit.vo.ServiceResult;
/**
 * @Description:系统提醒项Service
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-4
 * @Author lys
 */
public interface ReminderItemService extends BaseService<ReminderItem,Integer>{
	/**
	 * @Description: 保存系统提醒项
	 * @Created Time: 2013-3-4 下午4:10:40
	 * @Author lys
	 * @param model
	 * @return
	 */
	ServiceResult save(ReminderItem model);
	/**
	 * @Description: 删除系统提醒项
	 * @Created Time: 2013-3-4 下午4:10:57
	 * @Author lys
	 * @param model
	 * @return
	 */
	ServiceResult delete(ReminderItem model);
	/**
	 * @Description: 分页查询系统提醒项 
	 * @Created Time: 2013-3-4 下午4:11:08
	 * @Author lys
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	ServiceResult query(ReminderItem model, Integer page, Integer rows);
	/**
	 * @Description: 统计系统提醒项
	 * @Created Time: 2013-3-4 下午4:11:20
	 * @Author lys
	 * @param model
	 * @return
	 */
	ServiceResult getTotalCount(ReminderItem model);
	/**
	 * @Description: 系统提醒时分页选择查询系统提醒项
	 * @Created Time: 2013-3-4 下午10:08:59
	 * @Author lys
	 * @param ids
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	String selectQuery(String ids, ReminderItem model, Integer page,
			Integer rows);

}
