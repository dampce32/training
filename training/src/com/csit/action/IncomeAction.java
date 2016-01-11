package com.csit.action;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.csit.model.Income;
import com.csit.model.User;
import com.csit.service.IncomeService;
import com.csit.vo.ServiceResult;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @Description:记帐表Action
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-28
 * @author jcf
 * @vesion 1.0
 */
@Controller
@Scope("prototype")
public class IncomeAction extends BaseAction implements ModelDriven<Income> {

	private static final long serialVersionUID = 6327883096852459546L;
	private static final Logger logger = Logger.getLogger(IncomeAction.class);
	private Income model = new Income();
	@Resource
	private IncomeService incomeService;
	private Date accountDateBegin;
	private Date accountDateEnd;

	@Override
	public Income getModel() {
		return model;
	}
	/**
	 * @Description: 保存记帐表
	 * @Create: 2013-3-29 上午09:18:21
	 * @author jcf
	 * @update logs
	 */
	public void save() {
		ServiceResult result = new ServiceResult(false);
		try {
			result = incomeService.save(model);
		} catch (Exception e) {
			result.setMessage("保存记帐表失败");
			logger.error("保存记帐表失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

	/**
	 * @Description: 删除记帐表
	 * @Create: 2013-3-29 上午09:18:29
	 * @author jcf
	 * @update logs
	 */
	public void delete() {
		ServiceResult result = new ServiceResult(false);
		try {
			result = incomeService.delete(model);
		} catch (Exception e) {
			if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
				result.setMessage("该记录已被使用，不能删除");
			} else {
				result.setMessage("删除记帐表失败");
				logger.error("删除记帐表失败", e);
			}
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

	/**
	 * @Description: 分页查询记帐表
	 * @Create: 2013-3-29 上午09:18:38
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
			result = incomeService.query(model,accountDateBegin,accountDateEnd, page, rows);
		} catch (Exception e) {
			result.setMessage("查询记帐表失败");
			logger.error("查询记帐表失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

	/**
	 * @Description: 统计记帐表
	 * @Create: 2013-3-29 上午09:18:47
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
			result = incomeService.getTotalCount(model,accountDateBegin,accountDateEnd);
		} catch (Exception e) {
			result.setMessage("统计记帐表失败");
			logger.error("统计记帐表失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	public Date getAccountDateBegin() {
		return accountDateBegin;
	}
	public void setAccountDateBegin(Date accountDateBegin) {
		this.accountDateBegin = accountDateBegin;
	}
	public Date getAccountDateEnd() {
		return accountDateEnd;
	}
	public void setAccountDateEnd(Date accountDateEnd) {
		this.accountDateEnd = accountDateEnd;
	}

}
