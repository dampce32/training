package com.csit.action;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.csit.model.RewPun;
import com.csit.service.RewPunService;
import com.csit.vo.ServiceResult;
import com.opensymphony.xwork2.ModelDriven;
/**
 * @Description:奖惩Action
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author lys
 */
@Controller
@Scope("prototype")
public class RewPunAction extends BaseAction implements ModelDriven<RewPun> {
	
	
	private static final long serialVersionUID = 7781909820549872681L;
	private static final Logger logger = Logger.getLogger(RewPunAction.class);
	private RewPun model = new RewPun();
	
	@Resource
	private RewPunService rewPunService;
	
	public RewPun getModel() {
		return model;
	}
	
	/**
	 * @Description: 保存课程类型
	 * @Create: 2012-12-23 下午5:42:17
	 * @author cjp
	 * @update logs
	 */
	public void save(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = rewPunService.save(model);
		} catch (Exception e) {
			result.setMessage("保存奖惩失败");
			logger.error("保存奖惩失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	
	/**
	 * 
	 * @Description: 分页查询奖惩
	 * @Create: 2013-2-28 上午11:26:10
	 * @author cjp
	 * @update logs
	 * @throws Exception
	 */
	public void query(){
		ServiceResult result = new ServiceResult(false);
		String beginDate=getParameter("beginDate");
		String endDate=getParameter("endDate");
		try {
			result = rewPunService.query(model,beginDate,endDate,page,rows);
		} catch (Exception e) {
			result.setMessage("查询奖惩失败");
			logger.error("查询奖惩失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	
	/**
	 * @Description: 统计奖惩
	 * @Create: 2012-12-18 下午10:59:26
	 * @author cjp
	 * @update logs
	 */
	public void getTotalCount(){
		ServiceResult result = new ServiceResult(false);
		String beginDate=getParameter("beginDate");
		String endDate=getParameter("endDate");
		try {
			result = rewPunService.getTotalCount(model,beginDate,endDate);
		} catch (Exception e) {
			result.setMessage("统计奖惩失败");
			logger.error("统计奖惩失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * @Description: 删除奖惩
	 * @Create: 2012-12-18 下午10:59:26
	 * @author cjp
	 * @update logs
	 */
	public void delete(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = rewPunService.delete(model);
		} catch (Exception e) {
			if(e instanceof org.springframework.dao.DataIntegrityViolationException){
				result.setMessage("该记录已被使用，不能删除");
			}else{
				result.setMessage("删除奖惩失败");
				logger.error("删除奖惩失败", e);
			}
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
}
