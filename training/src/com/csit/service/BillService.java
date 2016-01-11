package com.csit.service;

import java.util.Date;

import com.csit.model.Bill;
import com.csit.vo.ServiceResult;

/**
 * @Description:账单Service
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-7
 * @author jcf
 * @vesion 1.0
 */
public interface BillService extends BaseService<Bill, Integer> {

	/**
	 * @Description: 保存消费
	 * @Create: 2013-3-11 下午03:16:19
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult save(Bill model,String billDetail,String cost);
	/**
	 * @Description: 删除消费
	 * @Create: 2013-3-11 下午03:16:59
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult delete(Bill model);
	/**
	 * @Description: 分页查询消费
	 * @Create: 2013-3-11 下午03:17:19
	 * @author jcf
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	ServiceResult query(Bill model,Date beginDate,Date endDate, Integer page, Integer rows);
	/**
	 * @Description: 统计消费
	 * @Create: 2013-3-11 下午03:18:05
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult getTotalCount(Bill model,Date beginDate,Date endDate);
	/**
	 * @Description: 初始化消费单号
	 * @Create: 2013-3-13 上午09:07:31
	 * @author jcf
	 * @update logs
	 * @return
	 */
	ServiceResult initBill(String userId);
	/**
	 * 
	 * @Description: 查看详细
	 * @Create: 2013-3-14 下午04:19:20
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult queryDetail(Bill model);
}
