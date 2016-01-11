package com.csit.dao;

import java.util.List;

import com.csit.model.PeriodTime;
/**
 * 
 * @Description: 时间段DAO
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-28
 * @author yk
 * @vesion 1.0
 */
public interface PeriodTimeDAO extends BaseDAO<PeriodTime, Integer> {
	/**
	 * 
	 * @Description: 分页查询时间段
	 * @param
	 * @Create: 2013-2-28 下午11:57:41
	 * @author yk
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 * @return
	 * @throws Exception
	 */
	List<PeriodTime> query(PeriodTime model, Integer page, Integer rows);
	/**
	 * 
	 * @Description: 统计时间段
	 * @param
	 * @Create: 2013-2-28 下午11:57:58
	 * @author yk
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	Long getTotalCount(PeriodTime model);
	/**
	 * 
	 * @Description: combox查询指定分组下的时间段
	 * @param
	 * @Create: 2013-3-7 上午09:21:22
	 * @author yk
	 * @update logs
	 * @param groupType
	 * @return
	 * @return
	 * @throws Exception
	 */
	List<PeriodTime> queryInGroupTypeCombobox(Integer groupType);
}
