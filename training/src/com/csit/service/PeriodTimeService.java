package com.csit.service;

import com.csit.model.PeriodTime;
import com.csit.vo.ServiceResult;
/**
 * 
 * @Description: 时间段Sercive
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-1
 * @author yk
 * @vesion 1.0
 */
public interface PeriodTimeService extends BaseService<PeriodTime, Integer> {
	/**
	 * 
	 * @Description: 保存时间段
	 * @param
	 * @Create: 2013-3-1 上午09:09:38
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult save(PeriodTime model);
	/**
	 * 
	 * @Description: 删除时间段
	 * @param
	 * @Create: 2013-3-1 上午09:09:45
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult delete(PeriodTime model);
	/**
	 * 
	 * @Description: 分页查询时间段
	 * @param
	 * @Create: 2013-3-1 上午09:09:56
	 * @author yk
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult query(PeriodTime model, Integer page, Integer rows);
	/**
	 * 
	 * @Description: 统计时间段
	 * @param
	 * @Create: 2013-3-1 上午09:10:06
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult getTotalCount(PeriodTime model);
	/**
	 * 
	 * @Description: combobox查询
	 * @param
	 * @Create: 2013-3-7 上午11:04:19
	 * @author yk
	 * @update logs
	 * @return
	 * @return
	 * @throws Exception
	 */
	String queryCombobox();
	/**
	 * 
	 * @Description: combox查询指定分组下的时间段
	 * @param
	 * @Create: 2013-3-7 上午09:16:23
	 * @author yk
	 * @update logs
	 * @return
	 * @return
	 * @throws Exception
	 */
	String queryInGroupTypeCombobox(Integer groupType);

}
