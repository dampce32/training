package com.csit.service;

import java.util.Date;

import com.csit.model.Payment;
import com.csit.vo.ServiceResult;

/**
 * @Description:账单Service
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-7
 * @author jcf
 * @vesion 1.0
 */
public interface PaymentService extends BaseService<Payment, Integer> {

	/**
	 * @Description: 
	 * @Create: 2013-3-8 下午04:17:11
	 * @author jcf
	 * @update logs
	 * @param model
	 * @param confirmCreditExpiration 判断是否修改还款日期
	 * @return
	 */
	ServiceResult save(Payment model,Integer confirmCreditExpiration);
	/**
	 * @Description: 删除账单
	 * @Create: 2013-3-7 下午02:25:04
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult delete(Payment model);
	/**
	 * 
	 * @Description: 分页查询账单
	 * @Create: 2013-2-28 上午09:41:28
	 * @author jcf
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	ServiceResult query(Payment model,Date beginDate,Date endDate, Integer page, Integer rows);
	/**
	 * @Description: 统计账单
	 * @Create: 2013-3-7 下午02:25:20
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult getTotalCount(Payment model,Date beginDate,Date endDate);
	
	ServiceResult sum(Payment model,Date beginDate,Date endDate,Integer type);
}
