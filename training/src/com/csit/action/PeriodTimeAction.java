package com.csit.action;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.csit.model.PeriodTime;
import com.csit.service.PeriodTimeService;
import com.csit.vo.ServiceResult;
import com.opensymphony.xwork2.ModelDriven;
/**
 * 
 * @Description: 时间段Action
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-1
 * @author yk
 * @vesion 1.0
 */
@Controller
@Scope("prototype")
public class PeriodTimeAction extends BaseAction implements
		ModelDriven<PeriodTime> {
	
	private static final long serialVersionUID = 4015884017822611486L;
	private static final Logger logger = Logger.getLogger(PeriodTimeAction.class);
	private PeriodTime model = new PeriodTime();
	@Resource
	private PeriodTimeService periodTimeService;
	public PeriodTime getModel() {
		return model;
	}
	/**
	 * 
	 * @Description: 保存时间段
	 * @param
	 * @Create: 2013-3-1 下午05:03:13
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void save(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = periodTimeService.save(model);
		} catch (Exception e) {
			result.setMessage("保存时间段失败");
			logger.error("保存时间段失败", e);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 删除时间段
	 * @param
	 * @Create: 2013-3-1 下午05:03:04
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void delete(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = periodTimeService.delete(model);
		} catch (Exception e) {
			if(e instanceof org.springframework.dao.DataIntegrityViolationException){
				result.setMessage("该记录已被使用，不能删除");
			}else{
				result.setMessage("删除时间段失败");
				logger.error("删除时间段失败", e);
			}
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 分页查询时间段
	 * @param
	 * @Create: 2013-3-1 下午05:02:52
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void query(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = periodTimeService.query(model,page,rows);
		} catch (Exception e) {
			result.setMessage("查询时间段失败");
			logger.error("查询时间段失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: 统计时间段
	 * @param
	 * @Create: 2013-3-1 下午05:02:39
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void getTotalCount(){
		ServiceResult result = new ServiceResult(false);
		try {
			result = periodTimeService.getTotalCount(model);
		} catch (Exception e) {
			result.setMessage("统计时间段失败");
			logger.error("统计时间段失败", e);
			result.setIsSuccess(false);
		}
		String jsonString = result.toJSON();
		ajaxJson(jsonString);
	}
	/**
	 * 
	 * @Description: combobox查询
	 * @param
	 * @Create: 2013-3-7 上午11:10:23
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void queryCombobox(){
		try {
			String jsonString = periodTimeService.queryCombobox();
			ajaxJson(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @Description: combox查询指定分组下的时间段
	 * @param
	 * @Create: 2013-3-7 上午09:17:14
	 * @author yk
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	public void queryInGroupTypeCombobox(){
		try {
			String jsonString = periodTimeService.queryInGroupTypeCombobox(model.getGroupType());
			ajaxJson(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
