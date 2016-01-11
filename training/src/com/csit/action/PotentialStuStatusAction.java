package com.csit.action;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.csit.model.PotentialStuStatus;
import com.csit.service.PotentialStuStatusService;
import com.csit.vo.ServiceResult;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * @Description:咨询课程Action
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-28
 * @author jcf
 * @vesion 1.0
 */
@Controller
@Scope("prototype")
public class PotentialStuStatusAction extends BaseAction implements ModelDriven<PotentialStuStatus> {

	private static final long serialVersionUID = -4648466664583512735L;
	private static final Logger logger = Logger.getLogger(PotentialStuStatusAction.class);
	private PotentialStuStatus model = new PotentialStuStatus();
	@Resource
	private PotentialStuStatusService potentialStuStatusService;

	@Override
	public PotentialStuStatus getModel() {
		return model;
	}

	/**
	 * 
	 * @Description: 保存咨询课程
	 * @Create: 2013-2-28 上午10:03:01
	 * @author jcf
	 * @update logs
	 */
	public void save() {
		ServiceResult result = new ServiceResult(false);
		try {
			result = potentialStuStatusService.save(model);
		} catch (Exception e) {
			result.setMessage("保存咨询课程失败");
			logger.error("保存咨询课程失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

	/**
	 * 
	 * @Description: 删除咨询课程
	 * @Create: 2013-2-28 上午10:04:40
	 * @author jcf
	 * @update logs
	 */
	public void delete() {
		ServiceResult result = new ServiceResult(false);
		try {
			result = potentialStuStatusService.delete(model);
		} catch (Exception e) {
			if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
				result.setMessage("该记录已被使用，不能删除");
			} else {
				result.setMessage("删除咨询课程失败");
				logger.error("删除咨询课程失败", e);
			}
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

	/**
	 * 
	 * @Description: 分页查询咨询课程
	 * @Create: 2013-2-28 上午10:04:51
	 * @author jcf
	 * @update logs
	 */
	public void query() {
		ServiceResult result = new ServiceResult(false);
		try {
			result = potentialStuStatusService.query(model, page, rows);
		} catch (Exception e) {
			result.setMessage("查询咨询课程失败");
			logger.error("查询咨询课程失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

	/**
	 * 
	 * @Description: 统计咨询课程
	 * @Create: 2013-2-28 上午10:05:07
	 * @author jcf
	 * @update logs
	 */
	public void getTotalCount() {
		ServiceResult result = new ServiceResult(false);
		try {
			result = potentialStuStatusService.getTotalCount(model);
		} catch (Exception e) {
			result.setMessage("统计咨询课程失败");
			logger.error("统计咨询课程失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

	/**
	 * 
	 * @Description: combobox查询
	 * @Create: 2013-2-28 上午10:05:18
	 * @author jcf
	 * @update logs
	 */
	public void queryCombobox() {
		String jsonString = potentialStuStatusService.queryCombobox();
		ajaxJson(jsonString);
	}

	/**
	 * 
	 * @Description: 修改状态
	 * @Create: 2013-2-28 下午04:05:58
	 * @author jcf
	 * @update logs
	 */
	public void updateStatus(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = potentialStuStatusService.updateStatus(model.getPotentialStuStatusId(), model.getStatus());
		} catch (Exception e) {
			result.setMessage("修改媒体状态失败");
			logger.error("修改媒体状态失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

}
