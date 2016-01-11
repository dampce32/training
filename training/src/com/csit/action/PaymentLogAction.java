package com.csit.action;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.csit.model.PaymentLog;
import com.csit.service.PaymentLogService;
import com.csit.vo.ServiceResult;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @Description:支付宝日志Action
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-4-7
 * @author jcf
 * @vesion 1.0
 */
@Controller
@Scope("prototype")
public class PaymentLogAction extends BaseAction implements ModelDriven<PaymentLog> {

	private static final long serialVersionUID = -4648466664583512735L;
	private static final Logger logger = Logger.getLogger(PaymentLogAction.class);
	private PaymentLog model = new PaymentLog();
	@Resource
	private PaymentLogService paymentLogService;

	@Override
	public PaymentLog getModel() {
		return model;
	}

	/**
	 * @Description: 保存支付宝日志
	 * @Create: 2013-4-7 上午11:23:01
	 * @author jcf
	 * @update logs
	 */
	public void save() {
		ServiceResult result = new ServiceResult(false);
		try {
			result = paymentLogService.save(model);
		} catch (Exception e) {
			result.setMessage("保存支付宝日志失败");
			logger.error("保存支付宝日志失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

	/**
	 * @Description: 分页查询支付宝日志
	 * @Create: 2013-4-7 上午11:23:40
	 * @author jcf
	 * @update logs
	 */
	public void query() {
		ServiceResult result = new ServiceResult(false);
		try {
			result = paymentLogService.query(model, page, rows);
		} catch (Exception e) {
			result.setMessage("查询支付宝日志失败");
			logger.error("查询支付宝日志失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

	/**
	 * @Description: 统计支付宝日志
	 * @Create: 2013-4-7 上午11:23:55
	 * @author jcf
	 * @update logs
	 */
	public void getTotalCount() {
		ServiceResult result = new ServiceResult(false);
		try {
			result = paymentLogService.getTotalCount(model);
		} catch (Exception e) {
			result.setMessage("统计支付宝日志失败");
			logger.error("统计支付宝日志失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

	/**
	 * 
	 * @Description: 修改状态
	 * @Create: 2013-4-7 上午11:24:50
	 * @author jcf
	 * @update logs
	 */
	public void updateStatus(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = paymentLogService.updateStatus(model);
		} catch (Exception e) {
			result.setMessage("修改支付宝日志状态失败");
			logger.error("修改支付宝日志状态失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

}
