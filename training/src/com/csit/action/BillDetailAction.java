package com.csit.action;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.csit.model.BillDetail;
import com.csit.service.BillDetailService;
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
public class BillDetailAction extends BaseAction implements ModelDriven<BillDetail> {

	private static final long serialVersionUID = -4648466664583512735L;
	private static final Logger logger = Logger.getLogger(BillDetailAction.class);
	private BillDetail model = new BillDetail();
	private Date billDateBegin;
	private Date billDateEnd;
	@Resource
	private BillDetailService billDetailService;

	@Override
	public BillDetail getModel() {
		return model;
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
			result = billDetailService.query(model,billDateBegin,billDateEnd, page, rows);
		} catch (Exception e) {
			result.setMessage("查询消费单详细失败");
			logger.error("查询消费单详细失败", e);
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
			result = billDetailService.getTotalCount(model,billDateBegin,billDateEnd);
		} catch (Exception e) {
			result.setMessage("统计消费失败");
			logger.error("统计消费失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	
	public void queryProduct(){
		
		ServiceResult result = new ServiceResult(false);
		try {
			String billDetailIds=getParameter("billDetailIds");
			result = billDetailService.queryDetail(billDetailIds);
		} catch (Exception e) {
			result.setMessage("查询消费单详细失败");
			logger.error("查询消费单详细失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
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
