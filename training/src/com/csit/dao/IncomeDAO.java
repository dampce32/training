package com.csit.dao;

import java.util.Date;
import java.util.List;

import com.csit.model.Income;

/**
 * @Description:记帐表Dao
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-29
 * @author jcf
 * @vesion 1.0
 */
public interface IncomeDAO extends BaseDAO<Income,Integer>{
	/**
	 * @Description: 分页查询记帐表
	 * @Create: 2013-3-29 上午09:05:23
	 * @author jcf
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	List<Income> query(Income model,Date accountDateBegin,Date accountDateEnd, Integer page, Integer rows);
	/**
	 * @Description: 统计记帐表
	 * @Create: 2013-3-29 上午09:05:32
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	Long getTotalCount(Income model,Date accountDateBegin,Date accountDateEnd);
}
