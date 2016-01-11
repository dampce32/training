package com.csit.service;

import com.csit.model.Account;
import com.csit.vo.ServiceResult;
/**
 * @Description:帐户表Service
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-28
 * @author jcf
 * @vesion 1.0
 */
public interface AccountService extends BaseService<Account, Integer> {

	/**
	 * @Description: 保存帐户表
	 * @Create: 2013-3-28 下午03:54:02
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult save(Account model);
	/**
	 * @Description: 删除帐户表
	 * @Create: 2013-3-28 下午03:54:11
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult delete(Account model);
	/**
	 * @Description: 分页查询帐户表
	 * @Create: 2013-3-28 下午03:54:20
	 * @author jcf
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	ServiceResult query(Account model, Integer page, Integer rows);
	/**
	 * @Description: 统计帐户表
	 * @Create: 2013-3-28 下午03:54:29
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult getTotalCount(Account model);
	/**
	 * @Description: combobox查询
	 * @Create: 2013-3-28 下午03:54:38
	 * @author jcf
	 * @update logs
	 * @return
	 */
	String queryCombobox();
	/**
	 * @Description: 修改状态
	 * @Create: 2013-3-28 下午03:54:49
	 * @author jcf
	 * @update logs
	 * @param accountId
	 * @param state
	 * @return
	 */
	ServiceResult updateStatus(Integer accountId,Integer state);
}
