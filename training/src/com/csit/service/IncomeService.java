package com.csit.service;

import java.util.Date;

import com.csit.model.Income;
import com.csit.vo.ServiceResult;
/**
 * @Description:记帐表Service
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-29
 * @author jcf
 * @vesion 1.0
 */
public interface IncomeService extends BaseService<Income, Integer> {

	/**
	 * @Description: 保存记帐表
	 * @Create: 2013-3-29 上午09:11:16
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult save(Income model);
	/**
	 * @Description: 删除记帐表
	 * @Create: 2013-3-29 上午09:11:25
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult delete(Income model);
	/**
	 * @Description: 分页查询记帐表
	 * @Create: 2013-3-29 上午09:11:34
	 * @author jcf
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	ServiceResult query(Income model,Date accountDateBegin,Date accountDateEnd, Integer page, Integer rows);
	/**
	 * @Description: 统计记帐表
	 * @Create: 2013-3-29 上午09:11:44
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult getTotalCount(Income model,Date accountDateBegin,Date accountDateEnd);
}
