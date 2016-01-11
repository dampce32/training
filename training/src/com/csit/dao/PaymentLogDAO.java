package com.csit.dao;

import java.util.List;

import com.csit.model.PaymentLog;
/**
 * @Description:支付宝日志Dao
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-4-7
 * @author jcf
 * @vesion 1.0
 */
public interface PaymentLogDAO extends BaseDAO<PaymentLog,Integer>{

	/**
	 * @Description: 分页查询支付宝日志
	 * @Create: 2013-4-7 上午11:01:42
	 * @author jcf
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	List<PaymentLog> query(PaymentLog model, Integer page, Integer rows);
	/**
	 * @Description: 统计支付宝日志
	 * @Create: 2013-4-7 上午11:01:32
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	Long getTotalCount(PaymentLog model);
}
