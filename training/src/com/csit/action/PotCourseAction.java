package com.csit.action;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.csit.model.PotCourse;
import com.csit.service.PotCourseService;
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
public class PotCourseAction extends BaseAction implements ModelDriven<PotCourse> {

	private static final long serialVersionUID = -4648466664583512735L;
	private static final Logger logger = Logger.getLogger(PotCourseAction.class);
	private PotCourse model = new PotCourse();
	@Resource
	private PotCourseService potCourseService;

	@Override
	public PotCourse getModel() {
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
			result = potCourseService.save(model);
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
			result = potCourseService.delete(model);
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
			result = potCourseService.query(model, page, rows);
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
			result = potCourseService.getTotalCount(model);
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
		String jsonString = potCourseService.queryCombobox();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 修改状态
	 * @Create: 2013-2-28 下午03:59:15
	 * @author jcf
	 * @update logs
	 */
	public void updateStatus(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = potCourseService.updateStatus(model.getPotCourseId(), model.getStatus());
		} catch (Exception e) {
			result.setMessage("修改咨询课程状态失败");
			logger.error("修改咨询课程状态失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

}
