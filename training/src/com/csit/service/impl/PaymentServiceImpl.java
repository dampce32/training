package com.csit.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.csit.dao.PaymentDAO;
import com.csit.dao.StudentDAO;
import com.csit.model.Payment;
import com.csit.model.Student;
import com.csit.service.PaymentService;
import com.csit.util.DateUtil;
import com.csit.util.JSONUtil;
import com.csit.vo.ServiceResult;
/**
 * @Description:账单Service实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-7
 * @author jcf
 * @vesion 1.0
 */
@Service
public class PaymentServiceImpl extends BaseServiceImpl<Payment, Integer> implements PaymentService {

	@Resource
	private PaymentDAO paymentDAO;
	@Resource
	private StudentDAO studentDAO;
	
	/* (non-Javadoc)   
	 * @see com.csit.service.PaymentService#delete(com.csit.model.Payment)   
	 */
	public ServiceResult delete(Payment model) {
		ServiceResult result = new ServiceResult(false);
		if (model == null || model.getPaymentId() == null) {
			result.setMessage("请选择要删除的账单");
			return result;
		}
		Payment oldModel = paymentDAO.load(model.getPaymentId());
		if (oldModel == null) {
			result.setMessage("该账单已不存在");
			return result;
		} else {
			paymentDAO.delete(oldModel);
		}
		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult getTotalCount(Payment model,Date beginDate,Date endDate) {
		ServiceResult result = new ServiceResult(false);
		Long data = paymentDAO.getTotalCount(model,beginDate,endDate);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}
	
	/* (non-Javadoc)   
	 * @see com.csit.service.PaymentService#query(com.csit.model.Payment, java.lang.Integer, java.lang.Integer)   
	 */
	public ServiceResult query(Payment model,Date beginDate,Date endDate, Integer page, Integer rows) {
		ServiceResult result = new ServiceResult(false);

		List<Payment> list = paymentDAO.query(model,beginDate,endDate, page, rows);

		String[] properties = { "paymentId", "school.schoolName","user.userName","school.schoolId","user.userId","paymentType", "payway","student.studentId","student.studentName","payMoney", "transactionDate","note","creditExpiration"};

		String data = JSONUtil.toJson(list, properties);
		result.addData("datagridData", data);

		result.setIsSuccess(true);
		return result;
	}

	
	public ServiceResult save(Payment model,Integer confirmCreditExpiration) {
		ServiceResult result = new ServiceResult(false);
		if (model == null) {
			result.setMessage("请填写账单信息");
			return result;
		}
		if(model.getStudent()==null&&model.getStudent().getStudentId()==null){
			result.setMessage("您还没有选择学员");
			return result;
		}
		Student oldStu=studentDAO.load(model.getStudent().getStudentId());
		if(model.getPaymentType()==null){
			result.setMessage("请选择交费类型");
			return result;
		}
		if(model.getPayway()==null){
			result.setMessage("请选择收费方式");
			return result;
		}
		if(model.getPayMoney()==null){
			result.setMessage("请填写金额");
			return result;
		}
		if(model.getPayMoney()<=0){
			result.setMessage("填写的金额不能小于0");
			return result;
		}
		if(model.getTransactionDate()==null){
			result.setMessage("填写日期");
			return result;
		}
		if(model.getSchool()==null&&model.getSchool().getSchoolId()==null){
			result.setMessage("请选择交费点");
			return result;
		}
		if(model.getUser()==null&&model.getUser().getUserId()==null){
			result.setMessage("请选择收费文员");
			return result;
		}
		if(model.getPaymentType()==1){
			oldStu.setAvailableMoney(oldStu.getAvailableMoney()+model.getPayMoney());
		}
		if(model.getPaymentType()==3){
			model.setPayway(null);
			if(model.getCreditExpiration()==null){
				result.setMessage("请填写借款到期");
				return result;
			}
			
			oldStu.setArrearageMoney(oldStu.getArrearageMoney()+model.getPayMoney());
			oldStu.setAvailableMoney(oldStu.getAvailableMoney()+model.getPayMoney());
			if(confirmCreditExpiration==1){
				oldStu.setCreditExpiration(model.getCreditExpiration());
			}
		}
		else if(model.getPaymentType()==4){
			model.setPayway(null);
			if(oldStu.getArrearageMoney()==0){
				result.setMessage("暂无还款金额");
				return result;
			}
			if(model.getPayMoney()>oldStu.getArrearageMoney()){
				result.setMessage("只需要输入"+oldStu.getArrearageMoney()+"的还款金额");
				return result;
			}
			if(oldStu.getAvailableMoney()-model.getPayMoney()<0){
				result.setMessage("余额不足还款");
				return result;
			}
			oldStu.setAvailableMoney(oldStu.getAvailableMoney()-model.getPayMoney());
			oldStu.setArrearageMoney(oldStu.getArrearageMoney()-model.getPayMoney());
			if(oldStu.getArrearageMoney()==0){
				oldStu.setCreditExpiration(null);
			}
		}
		if (model.getPaymentId() == null) {// 新增
			model.setInsertTime(new Date());
			paymentDAO.save(model);
		} else {
			Payment oldModel = paymentDAO.load(model.getPaymentId());
			if (oldModel == null) {
				result.setMessage("该账单已不存在");
				return result;
			}
			paymentDAO.update(model);
		}
		result.setIsSuccess(true);
		result.addData("arrearageMoney",oldStu.getArrearageMoney());
		result.addData("availableMoney",oldStu.getAvailableMoney());
		result.addData("consumedMoney",oldStu.getConsumedMoney());
		if(oldStu.getCreditExpiration()!=null){
			result.addData("creditExpiration",DateUtil.dateToString(oldStu.getCreditExpiration()) );
		}
		result.addData("paymentId", model.getPaymentId());
		return result;
	}

	@Override
	public ServiceResult sum(Payment model, Date beginDate, Date endDate, Integer type) {
		ServiceResult result = new ServiceResult(false);
		Double count=paymentDAO.sum(model, beginDate, endDate, type);
		if(count==null){
			result.addData("count", 0);
		}else {
			if(type==2){
				result.addData("count", -count);
			}
			else {
				result.addData("count", count);
			}
		}
		
		result.setIsSuccess(true);
		return result;
	}

}
