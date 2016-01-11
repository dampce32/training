package com.csit.service;

import com.csit.model.PaymentLog;
import com.csit.vo.ServiceResult;
/**
 * @Description:支付宝日志Service
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-4-7
 * @author jcf
 * @vesion 1.0
 */
public interface PaymentLogService extends BaseService<PaymentLog, Integer> {

	/**
	 * @Description: 保存支付宝日志
	 * @Create: 2013-4-7 上午11:13:45
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult save(PaymentLog model);
	/**
	 * @Description: 分页查询支付宝日志
	 * @Create: 2013-4-7 上午11:14:14
	 * @author jcf
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	ServiceResult query(PaymentLog model, Integer page, Integer rows);
	/**
	 * @Description: 统计支付宝日志
	 * @Create: 2013-4-7 上午11:14:41
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult getTotalCount(PaymentLog model);
	/**
	 * @Description: 修改状态
	 * @Create: 2013-4-7 上午11:15:02
	 * @author jcf
	 * @update logs
	 * @param PaymentLogId
	 * @param state
	 * @return
	 */
	ServiceResult updateStatus(PaymentLog model);
}
