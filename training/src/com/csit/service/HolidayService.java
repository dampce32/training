package com.csit.service;

import com.csit.model.Holiday;
import com.csit.vo.ServiceResult;
/**
 * 
 * @Description: 假期表Service
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-28
 * @author yk
 * @vesion 1.0
 */
public interface HolidayService extends BaseService<Holiday, Integer> {
	/**
	 * 
	 * @Description: 保存假期表
	 * @param
	 * @Create: 2013-2-28 下午10:44:44
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult save(Holiday model);
	/**
	 * 
	 * @Description: 删除假期表
	 * @param
	 * @Create: 2013-2-28 下午10:45:24
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult delete(Holiday model);
	/**
	 * 
	 * @Description: 分页查询假期表
	 * @param
	 * @Create: 2013-2-28 下午10:45:36
	 * @author yk
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult query(Holiday model, Integer page, Integer rows);
	/**
	 * 
	 * @Description: 统计假期表
	 * @param
	 * @Create: 2013-2-28 下午10:46:19
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult getTotalCount(Holiday model);
	/**
	 * 
	 * @Description: combobox查询
	 * @param
	 * @Create: 2013-2-28 下午10:46:32
	 * @author yk
	 * @update logs
	 * @return
	 * @return
	 * @throws Exception
	 */
	String queryCombobox();

}
