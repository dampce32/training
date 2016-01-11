package com.csit.action;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.csit.model.ReportParam;
import com.csit.service.ReportParamService;
import com.csit.vo.ServiceResult;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @Description:报表参数Action
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-1-31
 * @author lys
 * @vesion 1.0
 */
@Controller
@Scope("prototype")
public class ReportParamAction extends BaseAction implements ModelDriven<ReportParam>{
	
	private static final long serialVersionUID = 1210292351588308170L;
	private static final Logger logger = Logger.getLogger(ReportParamAction.class);
	@Resource
	private ReportParamService reportParamService;
	ReportParam model = new ReportParam();
	@Override
	public ReportParam getModel() {
		return model;
	}
	/**
	 * @Description: 保存报表参数
	 * @Create: 2012-12-23 下午5:42:17
	 * @author lys
	 * @update logs
	 */
	public void save(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = reportParamService.save(model);
		} catch (Exception e) {
			result.setMessage("保存报表参数失败");
			logger.error("保存报表参数失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	
	/**
	 * @Description: 删除报表参数
	 * @Create: 2012-12-18 下午10:56:44
	 * @author lys
	 * @update logs
	 */
	public void delete(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = reportParamService.delete(model);
		} catch (Exception e) {
			result.setMessage("删除报表参数失败");
			logger.error("删除报表参数失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * @Description:分页查询报表参数 
	 * @Create: 2012-12-18 下午10:58:02
	 * @author lys
	 * @update logs
	 */
	public void query(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = reportParamService.query(model,page,rows);
		} catch (Exception e) {
			result.setMessage("查询报表参数失败");
			logger.error("查询报表参数失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * @Description: 统计报表参数
	 * @Create: 2012-12-18 下午10:59:26
	 * @author lys
	 * @update logs
	 */
	public void getTotalCount(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = reportParamService.getTotalCount(model);
		} catch (Exception e) {
			result.setMessage("统计报表参数失败");
			logger.error("统计报表参数失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * @Description: 报表配置选择查询报表参数
	 * @Create: 2013-1-31 下午4:01:19
	 * @author lys
	 * @update logs
	 */
	public void select(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = reportParamService.select(model,ids,page,rows);
		} catch (Exception e) {
			result.setMessage("报表配置选择报表参数失败");
			logger.error("报表配置选择报表参数失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
}
