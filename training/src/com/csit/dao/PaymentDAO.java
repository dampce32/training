package com.csit.dao;

import java.util.Date;
import java.util.List;

import com.csit.model.Payment;
/**
 * @Description:账单Dao
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-7
 * @author jcf
 * @vesion 1.0
 */
public interface PaymentDAO extends BaseDAO<Payment,Integer>{

	/**
	 * @Description: 分页查询账单
	 * @Create: 2013-3-7 下午01:34:22
	 * @author jcf
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	List<Payment> query(Payment model,Date beginDate,Date endDate, Integer page, Integer rows);
	/**
	 * @Description: 统计账单
	 * @Create: 2013-3-7 下午01:34:38
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	Long getTotalCount(Payment model,Date beginDate,Date endDate);
	/**
	 * 
	 * @Description: 统计费用
	 * @Create: 2013-3-28 上午10:47:11
	 * @author jcf
	 * @update logs
	 * @param model
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	Double sum(Payment model,Date beginDate,Date endDate,Integer type);
}
