package com.csit.action;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.csit.model.Bill;
import com.csit.model.User;
import com.csit.service.BillService;
import com.csit.vo.ServiceResult;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @Description:消费Action
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-11
 * @author jcf
 * @vesion 1.0
 */
@Controller
@Scope("prototype")
public class BillAction extends BaseAction implements ModelDriven<Bill> {

	private static final long serialVersionUID = -4648466664583512735L;
	private static final Logger logger = Logger.getLogger(BillAction.class);
	private Bill model = new Bill();
	private String billDetail;
	private String cost;
	private Date billDateBegin;
	private Date billDateEnd;
	@Resource
	private BillService billService;

	@Override
	public Bill getModel() {
		return model;
	}
	/**
	 * @Description: 保存消费
	 * @Create: 2013-3-11 下午03:26:37
	 * @author jcf
	 * @update logs
	 */
	public void save() {
		ServiceResult result = new ServiceResult(false);
		try {
			result = billService.save(model,billDetail,cost);
		} catch (Exception e) {
			result.setMessage("保存消费失败"+e.getMessage());
			logger.error("保存消费失败", e);
		}
		try {
			String jsonString = result.toJSON();
			ajaxJson(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Description: 删除消费
	 * @Create: 2013-3-11 下午03:26:54
	 * @author jcf
	 * @update logs
	 */
	public void delete() {
		ServiceResult result = new ServiceResult(false);
		try {
			result = billService.delete(model);
		} catch (Exception e) {
			if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
				result.setMessage("该记录已被使用，不能删除");
			} else {
				result.setMessage("删除消费失败");
				logger.error("删除消费失败", e);
			}
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

	/**
	 * @Description: 分页查询消费
	 * @Create: 2013-3-7 下午05:15:12
	 * @author jcf
	 * @update logs
	 */
	public void query() {
		ServiceResult result = new ServiceResult(false);
		try {
			
			if(model!=null&&model.getSchool()!=null){
				String schoolCode = getSession(User.LOGIN_SCHOOLCODE).toString();
				model.getSchool().setSchoolCode(schoolCode);
			}
			result = billService.query(model,billDateBegin,billDateEnd, page, rows);
		} catch (Exception e) {
			result.setMessage("查询消费失败");
			logger.error("查询消费失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

	/**
	 * @Description: 统计消费
	 * @Create: 2013-3-11 下午03:27:35
	 * @author jcf
	 * @update logs
	 */
	public void getTotalCount() {
		ServiceResult result = new ServiceResult(false);
		try {
			
			if(model!=null&&model.getSchool()!=null){
				String schoolCode = getSession(User.LOGIN_SCHOOLCODE).toString();
				model.getSchool().setSchoolCode(schoolCode);
			}
			result = billService.getTotalCount(model,billDateBegin,billDateEnd);
		} catch (Exception e) {
			result.setMessage("统计消费失败");
			logger.error("统计消费失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	public void init(){
		ServiceResult result = new ServiceResult(false);
		String userId=getSession().get(User.LOGIN_USERID).toString();
		try {
			result = billService.initBill(userId);
		} catch (Exception e) {
			result.setMessage("初始化消费单编号失败");
			logger.error("初始化消费单编号失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	public void queryDetail(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = billService.queryDetail(model);
		} catch (Exception e) {
			result.setMessage("查询消费单详细失败");
			logger.error("查询消费单详细失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	public String getBillDetail() {
		return billDetail;
	}
	public void setBillDetail(String billDetail) {
		this.billDetail = billDetail;
	}
	public String getCost() {
		return cost;
	}
	public void setCost(String cost) {
		this.cost = cost;
	}
	public Date getBillDateBegin() {
		return billDateBegin;
	}
	public void setBillDateBegin(Date billDateBegin) {
		this.billDateBegin = billDateBegin;
	}
	public Date getBillDateEnd() {
		return billDateEnd;
	}
	public void setBillDateEnd(Date billDateEnd) {
		this.billDateEnd = billDateEnd;
	}

}
