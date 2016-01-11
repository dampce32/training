package com.csit.dao;

import java.io.Serializable;
import java.util.List;

/**
 * 基本DAO
 * 包含了对实体T的基本操作：
 * 增 save 删 delete 改 update 分页查询 get和count
 * @author LYS
 *
 * @param <T>
 */
public interface BaseDAO<T,PK extends Serializable> {

	/**
	 * @Description: 保存实体
	 * @Created: 2012-10-8 下午1:49:13
	 * @Author lys
	 * @param model
	 */
	public void save(T model);
	/**
	 * @Description:  删除相应实体
	 * @Created: 2012-10-8 下午1:48:36
	 * @Author lys
	 * @param model 要删除的实体
	 */
	public void delete(T model);
	/**
	 * @Description: 根据主键删除实体
	 * @Create: 2012-10-27 下午2:21:26
	 * @author lys
	 * @update logs
	 * @param pk
	 * @throws Exception
	 */
	public void delete(PK pk);
	/**
	 * 更新相应实体
	 * @param model
	 */
	public void update(T model);
	
	/**
	 * 
	 * @Description:  刷新session.
	 * @Create: 2012-3-28 下午06:49:59
	 * @author longweier
	 * @update logs
	 * @throws Exception
	 */
	public void flush();
	
	/**
	 * 
	 * @Description: 清除Session.
	 * @Create: 2012-3-28 下午06:50:13
	 * @author longweier
	 * @update logs
	 * @throws Exception
	 */
	public void clear();
	
	/**
	 * 
	 * @Description: 清除某一对象.
	 * @Create: 2012-3-28 下午06:50:30
	 * @author longweier
	 * @update logs
	 * @param object 清除对象.
	 * @throws Exception
	 */
	public void evict(Object object);
	/**
	 * 更新相应实体
	 * @param model
	 */
	public void merger(T model);
	/**
	 * @Description: 根据id取得model
	 * @Create: 2012-9-17 下午11:27:16
	 * @author lys
	 * @param id
	 * @return
	 */
	public T load(PK id);
	/**
	 * @Description: 根据id取得model
	 * @Create: 2013-2-17 下午2:13:21
	 * @author lys
	 * @update logs
	 * @param id
	 * @return
	 */
	public T get(PK id);
	/**
	 * @Description: 根据某种属性取得model
	 * @Create: 2012-9-17 下午11:33:29
	 * @author lys
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public T load(String propertyName, Object value);
	/**
	 * @Description: 根据多个属性取得Model
	 * @Create: 2012-10-14 下午10:18:00
	 * @author lys
	 * @update logs
	 * @param propertyNames
	 * @param values
	 * @return
	 * @throws Exception
	 */
	public T load(String[] propertyNames, Object[] values);
	/**
	 * @Description: 查询所有的model
	 * @Create: 2012-12-23 下午10:54:57
	 * @author lys
	 * @update logs
	 * @return
	 */
	public List<T> queryAll();
	/**
	 * @Description: 查询属性propertyName值为value的model
	 * @Create: 2012-12-23 下午10:58:44
	 * @author lys
	 * @update logs
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public List<T> query(String propertyName, Object value);
	/**
	 * 
	 * @Description: 根据多个属性查询
	 * @Create: 2013-4-22 上午10:58:42
	 * @author jcf
	 * @update logs
	 * @param propertyNames
	 * @param values
	 * @return
	 */
	public List<T> query(String[] propertyNames, Object[] values);
	
}
