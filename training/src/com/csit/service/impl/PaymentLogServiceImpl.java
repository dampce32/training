package com.csit.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.csit.dao.PaymentLogDAO;
import com.csit.model.PaymentLog;
import com.csit.service.PaymentLogService;
import com.csit.util.JSONUtil;
import com.csit.vo.ServiceResult;

/**
 * @Description:支付宝日志Service实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-4-7
 * @author jcf
 * @vesion 1.0
 */
@Service
public class PaymentLogServiceImpl extends BaseServiceImpl<PaymentLog, Integer> implements PaymentLogService {

	@Resource
	private PaymentLogDAO paymentLogDAO;

	public ServiceResult getTotalCount(PaymentLog model) {
		ServiceResult result = new ServiceResult(false);
		Long data = paymentLogDAO.getTotalCount(model);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.service.PaymentLogService#query(com.csit.model.PaymentLog, java.lang.Integer, java.lang.Integer)
	 */
	public ServiceResult query(PaymentLog model, Integer page, Integer rows) {
		ServiceResult result = new ServiceResult(false);

		List<PaymentLog> list = paymentLogDAO.query(model, page, rows);

		String[] properties = { "paymentLogId","status" };

		String data = JSONUtil.toJson(list, properties);
		result.addData("datagridData", data);

		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult save(PaymentLog model) {
		ServiceResult result = new ServiceResult(false);
		if (model == null) {
			result.setMessage("请填写支付宝日志信息");
			return result;
		}
		if (model.getPaymentLogId() == null) {// 新增
			paymentLogDAO.save(model);
		}
		result.setIsSuccess(true);
		result.addData("paymentLogId", model.getPaymentLogId());
		return result;
	}

	@Override
	public ServiceResult updateStatus(PaymentLog model) {
		ServiceResult result = new ServiceResult(false);
		if (model.getPaymentLogId() == null) {
			result.setMessage("请选择要修改的支付宝日志信息");
			return result;
		}
		if (model.getStatus() == null) {
			result.setMessage("请选择要修改的状态");
			return result;
		}
		PaymentLog oldModel=paymentLogDAO.load(model.getPaymentLogId());
		if(oldModel==null){
			result.setMessage("对不起，你要修改的支付宝日志信息已不存在");
			return result;
		}
		if(oldModel.getStatus().intValue()==model.getStatus().intValue()){
			result.setMessage("没有可修改的支付宝日志信息");
			return result;
		}
		else {
			oldModel.setStatus(model.getStatus());
			//paymentLogDAO.update(oldModel);
		}
		result.setIsSuccess(true);
		return result;
	}

}
