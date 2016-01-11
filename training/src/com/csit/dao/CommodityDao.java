package com.csit.dao;

import java.util.List;

import com.csit.model.Commodity;
/**
 * @Description:商品DAO
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
public interface CommodityDao extends BaseDAO<Commodity,Integer>{

	
	/**
	 * 
	 * @Description: 分页查询商品
	 * @Create: 2013-2-28 下午1:40:05
	 * @author cjp
	 * @update logs
	 * @param model
	 * @param page
	 * @param rows
	 * @param commodityIds 
	 * @throws Exception
	 */
	List<Commodity> query(Commodity model, Integer page, Integer rows, Integer[] commodityIdArr);
	/**
	 * 
	 * @Description: 统计商品
	 * @Create: 2013-2-28 下午1:43:43
	 * @author cjp
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	Long getTotalCount(Commodity model, Integer[] commodityIdArr);
	/**
	 * 
	 * @Description: 商品库存预警
	 * @Create: 2013-2-28 下午1:43:43
	 * @author cjp
	 * @param Commodity model 
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	List<Commodity> queryQtyWarm( Commodity model, Integer page, Integer rows);
	/**
	 * 
	 * @Description: 统计库存预警
	 * @param
	 * @Create: 2013-3-27 上午11:18:11
	 * @author cjp
	 * @update logs
	 * @return
	 * @return
	 * @throws Exception
	 */
	Long getTotalCountQtyWarm(Commodity model);

}
