package com.csit.action;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.csit.model.Potential;
import com.csit.model.User;
import com.csit.service.PotentialService;
import com.csit.vo.ServiceResult;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * @Description:咨询信息Action
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-1
 * @author jcf
 * @vesion 1.0
 */
@Controller
@Scope("prototype")
public class PotentialAction extends BaseAction implements ModelDriven<Potential> {

	private static final long serialVersionUID = -4648466664583512735L;
	private static final Logger logger = Logger.getLogger(PotentialAction.class);
	private Potential model = new Potential();
	@Resource
	private PotentialService potentialService;

	@Override
	public Potential getModel() {
		return model;
	}

	/**
	 * 
	 * @Description: 保存咨询信息
	 * @Create: 2013-3-1 下午02:31:42
	 * @author jcf
	 * @update logs
	 */
	public void save() {
		ServiceResult result = new ServiceResult(false);
		try {
			result = potentialService.save(model);
		} catch (Exception e) {
			result.setMessage("保存咨询信息失败");
			logger.error("保存咨询信息失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

	/**
	 * 
	 * @Description: 删除咨询信息
	 * @Create: 2013-3-1 下午02:31:53
	 * @author jcf
	 * @update logs
	 */
	public void delete() {
		ServiceResult result = new ServiceResult(false);
		try {
			result = potentialService.delete(model);
		} catch (Exception e) {
			if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
				result.setMessage("该记录已被使用，不能删除");
			} else {
				result.setMessage("删除咨询信息失败");
				logger.error("删除咨询信息失败", e);
			}
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

	/**
	 * 
	 * @Description: 分页查询咨询信息
	 * @Create: 2013-3-1 下午02:32:04
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
			result = potentialService.query(model, page, rows);
		} catch (Exception e) {
			result.setMessage("查询咨询信息失败");
			logger.error("查询咨询信息失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

	/**
	 * 
	 * @Description: 统计咨询信息
	 * @Create: 2013-3-1 下午02:32:13
	 * @author jcf
	 * @update logs
	 */
	public void getTotalCount() {
		ServiceResult result = new ServiceResult(false);
		try {
			result = potentialService.getTotalCount(model);
		} catch (Exception e) {
			result.setMessage("统计咨询信息失败");
			logger.error("统计咨询信息失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	

}
