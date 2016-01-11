package com.csit.dao;

import java.util.List;

import com.csit.model.ReportParam;
/**
 * @Description:报表参数DAO
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-1-31
 * @author lys
 * @vesion 1.0
 */
public interface ReportParamDAO extends BaseDAO<ReportParam,String>{
	/**
	 * @Description: 分页查询报表参数
	 * @Create: 2013-1-31 下午2:54:50
	 * @author lys
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	List<ReportParam> query(ReportParam model, Integer page, Integer rows);
	/**
	 * @Description: 统计报表参数
	 * @Create: 2013-1-31 下午2:55:13
	 * @author lys
	 * @update logs
	 * @param model
	 * @return
	 */
	Long getTotalCount(ReportParam model);
	/**
	 * @Description: 报表配置选择报表参数 分页查询
	 * @Create: 2013-1-31 下午4:05:08
	 * @author lys
	 * @update logs
	 * @param model
	 * @param idArray
	 * @param page
	 * @param rows
	 * @return
	 */
	List<ReportParam> select(ReportParam model, String[] idArray, Integer page,
			Integer rows);
	/**
	 * @Description: 报表配置选择报表参数 统计
	 * @Create: 2013-1-31 下午4:05:59
	 * @author lys
	 * @update logs
	 * @param model
	 * @param idArray
	 * @return
	 */
	Long getTotalCountSelect(ReportParam model, String[] idArray);

}
