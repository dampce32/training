package com.csit.service;

import com.csit.model.ReportParam;
import com.csit.vo.ServiceResult;
/**
 * @Description:报表参数Service
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-1-31
 * @author lys
 * @vesion 1.0
 */
public interface ReportParamService extends BaseService<ReportParam,String>{
	/**
	 * @Description: 保存报表参数
	 * @Create: 2013-1-31 下午2:46:20
	 * @author lys
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult save(ReportParam model);
	/**
	 * @Description: 删除报表参数
	 * @Create: 2013-1-31 下午2:46:37
	 * @author lys
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult delete(ReportParam model);
	/**
	 * @Description: 分页查询报表参数 
	 * @Create: 2013-1-31 下午2:46:50
	 * @author lys
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	ServiceResult query(ReportParam model, Integer page, Integer rows);
	/**
	 * @Description: 统计报表参数
	 * @Create: 2013-1-31 下午2:47:07
	 * @author lys
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult getTotalCount(ReportParam model);
	/**
	 * @Description: 报表配置选择报表参数
	 * @Create: 2013-1-31 下午4:02:10
	 * @author lys
	 * @update logs
	 * @param model
	 * @param ids
	 * @param rows 
	 * @param page 
	 * @return
	 */
	ServiceResult select(ReportParam model, String ids, Integer page, Integer rows);

}
