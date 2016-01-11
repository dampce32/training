package com.csit.dao;

import java.util.List;

import com.csit.model.Commodity;
import com.csit.model.RecRej;
import com.csit.model.Store;
import com.csit.model.StoreTuneOut;
import com.csit.model.Warehouse;
/**
 * @Description:入库出库单DAO
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
public interface StoreDao extends BaseDAO<Store,Integer>{

	/**
	 * 
	 * @Description: 根据商品Id和仓库Id获得Model
	 * @param
	 * @Create: 2013-3-6 上午9:18:15
	 * @author cjp
	 * @update logs
	 * @param commodity
	 * @param warehouse
	 * @return
	 * @return
	 * @throws Exception
	 */
	Store query(Commodity commodity, Warehouse warehouse);
	
	/**
	 * 
	 * @Description: 分页查询库存
	 * @param
	 * @Create: 2013-3-6 上午9:18:15
	 * @author cjp
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	List<RecRej> query(Store model, Integer page, Integer rows);
	/**
	 * 
	 * @Description: 统计库存
	 * @param
	 * @Create: 2013-3-6 上午9:18:15
	 * @author cjp
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	Long getTotalCount(Store model);

	/**
	 * 
	 * @Description: 分页查询库存不为空库存
	 * @param
	 * @Create: 2013-3-6 上午9:18:15
	 * @author cjp
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	List<Store> queryNotEmpty(Store model, Integer page, Integer rows);
	/**
	 * 
	 * @Description: 统计库存不为空库存
	 * @param
	 * @Create: 2013-3-6 上午9:18:15
	 * @author cjp
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	Long getTotalCountNotEmpty(Store model);

}
