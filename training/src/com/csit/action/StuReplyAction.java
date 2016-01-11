package com.csit.action;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.csit.model.StuReply;
import com.csit.model.User;
import com.csit.service.StuReplyService;
import com.csit.vo.ServiceResult;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * @Description:学员关怀
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-6
 * @author jcf
 * @vesion 1.0
 */
@Controller
@Scope("prototype")
public class StuReplyAction extends BaseAction implements ModelDriven<StuReply> {

	private static final long serialVersionUID = -4648466664583512735L;
	private static final Logger logger = Logger.getLogger(StuReplyAction.class);
	private StuReply model = new StuReply();
	@Resource
	private StuReplyService stuReplyService;

	@Override
	public StuReply getModel() {
		return model;
	}

	/**
	 * 
	 * @Description: 保存学员关怀信息
	 * @Create: 2013-3-4 下午01:38:51
	 * @author jcf
	 * @update logs
	 */
	public void save() {
		ServiceResult result = new ServiceResult(false);
		try {
			Integer userId=(Integer) getSession().get(User.LOGIN_USERID);
			User user=new User();
			user.setUserId(userId);
			model.setUser(user);
			result = stuReplyService.save(model);
		} catch (Exception e) {
			result.setMessage("保存学员关怀信息失败");
			logger.error("保存学员关怀信息失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

	/**
	 * 
	 * @Description: 删除学员关怀信息
	 * @Create: 2013-3-4 下午01:39:01
	 * @author jcf
	 * @update logs
	 */
	public void delete() {
		ServiceResult result = new ServiceResult(false);
		try {
			result = stuReplyService.delete(model);
		} catch (Exception e) {
			if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
				result.setMessage("该记录已被使用，不能删除");
			} else {
				result.setMessage("删除学员关怀信息失败");
				logger.error("删除学员关怀信息失败", e);
			}
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

	/**
	 * 
	 * @Description: 分页查询学员关怀信息
	 * @Create: 2013-3-4 下午01:39:12
	 * @author jcf
	 * @update logs
	 */
	public void query() {
		ServiceResult result = new ServiceResult(false);
		try {
			result = stuReplyService.query(model, page, rows);
		} catch (Exception e) {
			result.setMessage("查询学员关怀信息失败");
			logger.error("查询学员关怀信息失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

	/**
	 * 
	 * @Description: 统计学员关怀信息
	 * @Create: 2013-3-4 下午01:39:20
	 * @author jcf
	 * @update logs
	 */
	public void getTotalCount() {
		ServiceResult result = new ServiceResult(false);
		try {
			result = stuReplyService.getTotalCount(model);
		} catch (Exception e) {
			result.setMessage("统计学员关怀信息失败");
			logger.error("统计学员关怀信息失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

}
