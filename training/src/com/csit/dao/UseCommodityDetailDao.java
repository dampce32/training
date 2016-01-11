package com.csit.dao;

import java.util.List;

import com.csit.model.UseCommodity;
import com.csit.model.UseCommodityDetail;
/**
 * @Description:领用单DAO
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
public interface UseCommodityDetailDao extends BaseDAO<UseCommodityDetail,Integer>{
	/**
	 * 
	 * @Description: 查询需要归还的领用单详细
	 * @param
	 * @Create: 2013-3-25 下午3:55:57
	 * @author cjp
	 * @update logs
	 * @param model
	 * @param commodityName
	 * @param userName
	 * @param rows
	 * @param page
	 * @param useCommodityDetailIdArr
	 * @return
	 * @return
	 * @throws Exception
	 */
	List<UseCommodityDetail> queryNeedReturn(UseCommodity model,
			String commodityName, String userName, Integer rows, Integer page, Integer[] useCommodityDetailIdArr);
	/**
	 * 
	 * @Description: 统计需要归还的领用单详细
	 * @param
	 * @Create: 2013-3-25 下午3:55:57
	 * @author cjp
	 * @update logs
	 * @param model
	 * @param commodityName
	 * @param userName
	 * @param rows
	 * @param page
	 * @param useCommodityDetailIdArr
	 * @return
	 * @return
	 * @throws Exception
	 */
	Long getTotalCountNeedReturn(UseCommodity model, String commodityName,
			String userName,Integer[] useCommodityDetailIdArr);


}
