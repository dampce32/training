package com.csit.dao;

import java.util.List;

import com.csit.model.ReportParamConfig;
/**
 * @Description:报表参数配置DAO
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-1-31
 * @author lys
 * @vesion 1.0
 */
public interface ReportParamConfigDAO extends BaseDAO<ReportParamConfig,String>{
	/**
	 * @Description: 根据报表配置查找报表配置下的报表参数配置
	 * @Create: 2013-1-31 下午10:30:59
	 * @author lys
	 * @update logs
	 * @param reportConfigId
	 * @return
	 */
	List<ReportParamConfig> queryByReportConfigId(String reportConfigId);

}
