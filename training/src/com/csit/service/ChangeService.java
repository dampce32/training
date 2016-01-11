package com.csit.service;

import com.csit.model.Change;
import com.csit.vo.ServiceResult;
/**
 * @Description:异动Service
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
public interface ChangeService extends BaseService<Change,Integer>{
	/**
	 * @Description: 保存异动
	 * @Created Time: 2013-2-26 下午5:28:28
	 * @Author cjp
	 * @param model
	 * @return
	 */
	ServiceResult save(Change model);

	/**
	 * @Description: 分页查询异动
	 * @Create: 2013-2-28 上午11:28:25
	 * @author cjp
	 * @update logs
	 * @param model
	 * @param rows 
	 * @param page 
	 * @return
	 * @throws Exception
	 */
	ServiceResult query(Change model, Integer page, Integer rows);

	/**
	 * 
	 * @Description: 统计异动
	 * @Create: 2013-2-28 下午1:41:54
	 * @author cjp
	 * @update logs
	 * @param model
	 * @param endDate 
	 * @param beginDate 
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult getTotalCount(Change model);

	/**
	 * 
	 * @Description: 删除异动
	 * @param
	 * @Create: 2013-2-28 下午2:04:11
	 * @author cjp
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult delete(Change model);
}
