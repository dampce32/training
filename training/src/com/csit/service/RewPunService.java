package com.csit.service;

import com.csit.model.RewPun;
import com.csit.vo.ServiceResult;
/**
 * @Description:奖惩Service
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
public interface RewPunService extends BaseService<RewPun,Integer>{
	/**
	 * @Description: 保存奖惩
	 * @Created Time: 2013-2-26 下午5:28:28
	 * @Author cjp
	 * @param model
	 * @return
	 */
	ServiceResult save(RewPun model);

	/**
	 * @Description: 分页查询奖惩
	 * @Create: 2013-2-28 上午11:28:25
	 * @author cjp
	 * @update logs
	 * @param model
	 * @param endDate 
	 * @param beginDate 
	 * @param rows 
	 * @param page 
	 * @return
	 * @throws Exception
	 */
	ServiceResult query(RewPun model, String beginDate, String endDate, Integer page, Integer rows);

	/**
	 * 
	 * @Description: 统计奖惩
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
	ServiceResult getTotalCount(RewPun model, String beginDate, String endDate);

	/**
	 * 
	 * @Description: 删除奖惩
	 * @param
	 * @Create: 2013-2-28 下午2:04:11
	 * @author cjp
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult delete(RewPun model);
}
