package com.csit.action;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.csit.model.Media;
import com.csit.service.MediaService;
import com.csit.vo.ServiceResult;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * @Description:媒体Action
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-28
 * @author jcf
 * @vesion 1.0
 */
@Controller
@Scope("prototype")
public class MediaAction extends BaseAction implements ModelDriven<Media> {

	private static final long serialVersionUID = -4648466664583512735L;
	private static final Logger logger = Logger.getLogger(MediaAction.class);
	private Media model = new Media();
	@Resource
	private MediaService mediaService;

	@Override
	public Media getModel() {
		return model;
	}

	/**
	 * 
	 * @Description: 保存媒体
	 * @Create: 2013-2-28 上午10:03:01
	 * @author jcf
	 * @update logs
	 */
	public void save() {
		ServiceResult result = new ServiceResult(false);
		try {
			result = mediaService.save(model);
		} catch (Exception e) {
			result.setMessage("保存媒体失败");
			logger.error("保存媒体失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

	/**
	 * 
	 * @Description: 删除媒体
	 * @Create: 2013-2-28 上午10:04:40
	 * @author jcf
	 * @update logs
	 */
	public void delete() {
		ServiceResult result = new ServiceResult(false);
		try {
			result = mediaService.delete(model);
		} catch (Exception e) {
			if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
				result.setMessage("该记录已被使用，不能删除");
			} else {
				result.setMessage("删除媒体失败");
				logger.error("删除媒体失败", e);
			}
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

	/**
	 * 
	 * @Description: 分页查询媒体
	 * @Create: 2013-2-28 上午10:04:51
	 * @author jcf
	 * @update logs
	 */
	public void query() {
		ServiceResult result = new ServiceResult(false);
		try {
			result = mediaService.query(model, page, rows);
		} catch (Exception e) {
			result.setMessage("查询媒体失败");
			logger.error("查询媒体失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

	/**
	 * 
	 * @Description: 统计媒体
	 * @Create: 2013-2-28 上午10:05:07
	 * @author jcf
	 * @update logs
	 */
	public void getTotalCount() {
		ServiceResult result = new ServiceResult(false);
		try {
			result = mediaService.getTotalCount(model);
		} catch (Exception e) {
			result.setMessage("统计媒体失败");
			logger.error("统计媒体失败", e);
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
		String jsonString = mediaService.queryCombobox();
		ajaxJson(jsonString);
	}
	
	public void updateStatus(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = mediaService.updateStatus(model.getMediaId(), model.getStatus());
		} catch (Exception e) {
			result.setMessage("修改媒体状态失败");
			logger.error("修改媒体状态失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}

}
