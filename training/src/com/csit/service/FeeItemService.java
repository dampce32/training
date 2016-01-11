package com.csit.service;

import com.csit.model.FeeItem;
import com.csit.vo.ServiceResult;
/**
 * 
 * @Description: 消费项Service
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-28
 * @author yk
 * @vesion 1.0
 */
public interface FeeItemService extends BaseService<FeeItem, Integer> {
	/**
	 * 
	 * @Description: 保存消费项
	 * @param
	 * @Create: 2013-2-28 上午10:58:43
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult save(FeeItem model);
	/**
	 * 
	 * @Description: 删除消费项
	 * @param
	 * @Create: 2013-2-28 上午10:58:55
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult delete(FeeItem model);
	/**
	 * 
	 * @Description: 分页查询消费项
	 * @param
	 * @Create: 2013-2-28 上午10:59:07
	 * @author yk
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult query(FeeItem model, Integer page, Integer rows, String feeItemIds);
	/**
	 * 
	 * @Description: 统计消费项
	 * @param
	 * @Create: 2013-2-28 上午10:59:18
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult getTotalCount(FeeItem model, String feeItemIds);
	/**
	 * 
	 * @Description: combobox消费项
	 * @param
	 * @Create: 2013-2-28 上午10:59:35
	 * @author yk
	 * @update logs
	 * @return
	 * @return
	 * @throws Exception
	 */
	String queryCombobox();
	/**
	 * 
	 * @Description: 修改消费项状态
	 * @param
	 * @Create: 2013-3-1 下午04:32:35
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult updateStatus(FeeItem model);
}
