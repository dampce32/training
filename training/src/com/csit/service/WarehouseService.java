package com.csit.service;

import com.csit.model.Warehouse;
import com.csit.vo.ServiceResult;
/**
 * @Description:仓库Service
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
public interface WarehouseService extends BaseService<Warehouse,Integer>{
	/**
	 * @Description: 保存仓库
	 * @Created Time: 2013-2-26 下午5:28:28
	 * @Author cjp
	 * @param model
	 * @return
	 */
	ServiceResult save(Warehouse model);

	/**
	 * @Description: 分页查询仓库
	 * @Create: 2013-2-28 上午11:28:25
	 * @author cjp
	 * @update logs
	 * @param model
	 * @param rows 
	 * @param page 
	 * @return
	 * @throws Exception
	 */
	ServiceResult query(Warehouse model, Integer page, Integer rows);

	/**
	 * 
	 * @Description: 统计仓库
	 * @Create: 2013-2-28 下午1:41:54
	 * @author cjp
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult getTotalCount(Warehouse model);

	/**
	 * 
	 * @Description: 删除仓库
	 * @param
	 * @Create: 2013-2-28 下午2:04:11
	 * @author cjp
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult delete(Warehouse model);

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

	/**
	 * 
	 * @Description: 修改状态
	 * @param
	 * @Create: 2013-3-1 下午5:14:18
	 * @author cjp
	 * @update logs
	 * @param model
	 * @return
	 * @return
	 * @throws Exception
	 */
	ServiceResult updateStatus(Warehouse model);
	/**
	 * 
	 * @Description: combobox查询用户所在校区仓库
	 * @Create: 2013-4-22 下午01:12:36
	 * @author jcf
	 * @update logs
	 * @param schoolCode
	 * @return
	 */
	String queryCombobox(String schoolCode);
}
