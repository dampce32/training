package com.csit.action;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.csit.model.Payment;
import com.csit.model.User;
import com.csit.service.PaymentService;
import com.csit.vo.ServiceResult;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @Description:账单Action
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-7
 * @author jcf
 * @vesion 1.0
 */
@Controller
@Scope("prototype")
public class PaymentAction extends BaseAction implements ModelDriven<Payment> {

	private static final long serialVersionUID = -4648466664583512735L;
	private static final Logger logger = Logger.getLogger(PaymentAction.class);
	private Payment model = new Payment();
	@Resource
	private PaymentService paymentService;
	private Integer confirmCreditExpiration;
	private Date transactionDateBegin;
	private Date transactionDateEnd;
	private Integer type;

	@Override
	public Payment getModel() {
		return model;
	}
	/**
	 * @Description: 保存账单
	 * @Create: 2013-3-7 下午05:14:49
	 * @author jcf
	 * @update logs
	 */
	public void save() {
		ServiceResult result = new ServiceResult(false);
		try {
			result = paymentService.save(model,confirmCreditExpiration);
		} catch (Exception e) {
			result.setMessage("保存账单失败");
			logger.error("保存账单失败", e);
		}
		try {
			String jsonString = result.toJSON();
			ajaxJson(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Description:删除账单 
	 * @Create: 2013-3-7 下午05:15:00
	 * @author jcf
	 * @update logs
	 */
	public void delete() {
		ServiceResult result = new ServiceResult(false);
		try {
			result = paymentService.delete(model);
		} catch (Exception e) {
			if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
				result.setMessage("该记录已被使用，不能删除");
			} else {
				result.setMessage("删除账单失败");
				logger.error("删除账单失败", e);
			}
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

	/**
	 * @Description: 分页查询账单
	 * @Create: 2013-3-7 下午05:15:12
	 * @author jcf
	 * @update logs
	 */
	public void query() {
		ServiceResult result = new ServiceResult(false);
		try {
			String schoolCode = getSession(User.LOGIN_SCHOOLCODE).toString();
			if(model!=null&&model.getSchool()!=null){
				model.getSchool().setSchoolCode(schoolCode);
			}
			result = paymentService.query(model,transactionDateBegin,transactionDateEnd, page, rows);
		} catch (Exception e) {
			result.setMessage("查询账单失败");
			logger.error("查询账单失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

	/**
	 * @Description: 统计账单
	 * @Create: 2013-3-7 下午05:15:25
	 * @author jcf
	 * @update logs
	 */
	public void getTotalCount() {
		ServiceResult result = new ServiceResult(false);
		try {
			String schoolCode = getSession(User.LOGIN_SCHOOLCODE).toString();
			if(model!=null&&model.getSchool()!=null){
				model.getSchool().setSchoolCode(schoolCode);
			}
			result = paymentService.getTotalCount(model,transactionDateBegin,transactionDateEnd);
		} catch (Exception e) {
			result.setMessage("统计账单失败");
			logger.error("统计账单失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	
	public void sum(){
		ServiceResult result = new ServiceResult(false);
		try {
			String schoolCode = getSession(User.LOGIN_SCHOOLCODE).toString();
			if(model!=null&&model.getSchool()!=null){
				model.getSchool().setSchoolCode(schoolCode);
			}
			result = paymentService.sum(model, transactionDateBegin, transactionDateEnd, type);
		} catch (Exception e) {
			result.setMessage("统计账单失败");
			logger.error("统计账单失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	
	public Integer getConfirmCreditExpiration() {
		return confirmCreditExpiration;
	}
	public void setConfirmCreditExpiration(Integer confirmCreditExpiration) {
		this.confirmCreditExpiration = confirmCreditExpiration;
	}
	public Date getTransactionDateBegin() {
		return transactionDateBegin;
	}
	public void setTransactionDateBegin(Date transactionDateBegin) {
		this.transactionDateBegin = transactionDateBegin;
	}
	public Date getTransactionDateEnd() {
		return transactionDateEnd;
	}
	public void setTransactionDateEnd(Date transactionDateEnd) {
		this.transactionDateEnd = transactionDateEnd;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}

}
