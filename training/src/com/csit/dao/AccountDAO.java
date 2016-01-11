package com.csit.dao;

import java.util.List;

import com.csit.model.Account;

/**
 * @Description:帐户表Dao
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-28
 * @author jcf
 * @vesion 1.0
 */
public interface AccountDAO extends BaseDAO<Account,Integer>{
	/**
	 * @Description: 分页查询帐户表
	 * @Create: 2013-3-28 下午03:48:41
	 * @author jcf
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	List<Account> query(Account model, Integer page, Integer rows);
	/**
	 * @Description: 统计帐户表
	 * @Create: 2013-3-28 下午03:48:52
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	Long getTotalCount(Account model);
}
