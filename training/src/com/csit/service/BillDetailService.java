package com.csit.service;

import java.util.Date;

import com.csit.model.BillDetail;
import com.csit.vo.ServiceResult;

/**
 * @Description:消费单详细Service
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-18
 * @author jcf
 * @vesion 1.0
 */
public interface BillDetailService extends BaseService<BillDetail, Integer> {

	/**
	 * @Description: 分页查询消费单详细
	 * @Create: 2013-3-18 下午04:37:51
	 * @author jcf
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	ServiceResult query(BillDetail model,Date beginDate,Date endDate, Integer page, Integer rows);
	/**
	 * @Description: 统计消费单详细
	 * @Create: 2013-3-18 下午04:38:04
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult getTotalCount(BillDetail model,Date beginDate,Date endDate);
	/**
	 * 
	 * @Description: 查询已购的课程（退货办理使用）
	 * @Create: 2013-3-19 下午03:06:18
	 * @author jcf
	 * @update logs
	 * @param model
	 * @return
	 */
	ServiceResult queryDetail(String billDetailIds);
}
