package com.csit.service;

import java.io.File;

import com.csit.model.ReportConfig;
import com.csit.vo.ServiceResult;
/**
 * @Description:报表配置Service
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-1-23
 * @author lys
 * @vesion 1.0
 */
public interface ReportConfigService extends BaseService<ReportConfig,String>{
	/**
	 * @Description: 保存报表配置
	 * @Create: 2013-1-23 下午2:16:57
	 * @author lys
	 * @update logs
	 * @param model
	 * @param file 
	 * @param isNeedChooses 
	 * @param reportParamIds 
	 * @param deleteIds 
	 * @param reportParamConfigIds 
	 * @param isNeedChooses2 
	 * @return
	 */
	ServiceResult save(ReportConfig model, File file, String reportTemplatePath, String reportParamConfigIds, String deleteIds, String reportParamIds, String isNeedChooses);
	/**
	 * @Description: 删除报表配置
	 * @Create: 2013-1-23 下午2:17:29
	 * @author lys
	 * @update logs
	 * @param model
	 * @param reportTemplatePath 
	 * @return
	 */
	ServiceResult delete(ReportConfig model, String reportTemplatePath);
	/**
	 * @Description: 分页查询报表配置
	 * @Create: 2013-1-23 下午2:17:48
	 * @author lys
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	ServiceResult query(ReportConfig model, Integer page, Integer rows);
	/**
	 * @Description: 统计报表配置
	 * @Create: 2013-1-23 下午2:18:00
	 * @author lys
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult getTotalCount(ReportConfig model);
	/**
	 * @Description: 根据报表编号取得报表配置
	 * @Create: 2013-1-23 下午3:05:44
	 * @author lys
	 * @update logs
	 * @param reportCode
	 * @return
	 */
	ReportConfig  getByReportCode(String reportCode);
	/**
	 * @Description: 打开初始化
	 * @Create: 2013-1-31 下午10:19:32
	 * @author lys
	 * @update logs
	 * @param reportConfigId
	 * @return
	 */
	ServiceResult init(String reportConfigId);
	/**
	 * @Description: 取得所有的统计报表
	 * @Create: 2013-1-31 下午10:59:48
	 * @author lys
	 * @update logs
	 * @param reportConfigId
	 * @return
	 */
	ServiceResult getReport1();
	/**
	 * @Description: 取得报表的报表参数
	 * @Create: 2013-2-1 上午9:06:52
	 * @author lys
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult getReportParams(ReportConfig model);
	/**
	 * @Description: 取得所有的报表
	 * @Create: 2013-2-1 下午4:34:27
	 * @author lys
	 * @update logs
	 * @return
	 */
	ServiceResult getReportAll();

}
