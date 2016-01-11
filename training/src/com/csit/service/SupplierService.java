package com.csit.service;

import com.csit.model.Supplier;
import com.csit.vo.ServiceResult;
/**
 * @Description:供应商Service
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
public interface SupplierService extends BaseService<Supplier,Integer>{
	/**
	 * @Description: 保存供应商
	 * @Created Time: 2013-2-26 下午5:28:28
	 * @Author cjp
	 * @param model
	 * @return
	 */
	ServiceResult save(Supplier model);

	/**
	 * @Description: 分页查询供应商
	 * @Create: 2013-2-28 上午11:28:25
	 * @author cjp
	 * @update logs
	 * @param model
	 * @param rows 
	 * @param page 
	 * @return
	 * @throws Exception
	 */
	ServiceResult query(Supplier model, Integer page, Integer rows);

	/**
	 * 
	 * @Description: 统计供应商
	 * @Create: 2013-2-28 下午1:41:54
	 * @author cjp
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult getTotalCount(Supplier model);

	/**
	 * 
	 * @Description: 删除供应商
	 * @param
	 * @Create: 2013-2-28 下午2:04:11
	 * @author cjp
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult delete(Supplier model);

	/**
	 * 
	 * @Description: combobox查询
	 * @param
	 * @Create: 2013-2-28 下午2:21:05
	 * @author cjp
	 * @update logs
	 * @return
	 * @return
	 * @throws Exception
	 */
	String queryCombobox();

	ServiceResult updateStatus(Supplier model);
}
