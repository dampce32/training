package com.csit.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.csit.dao.BaseDAO;
/**
 * 基本DAO
 * 包含了对实体T的基本操作：
 * 增 save 删 delete 改 update 分页查询 get和count
 * @author LYS
 *
 * @param <T>
 */
public abstract class BaseDAOImpl<T,PK extends Serializable> implements BaseDAO<T,PK> {
	
	@Resource
	protected HibernateTemplate hibernateTemplate;
	
	@Resource
	protected SessionFactory sessionFactory;
	
	private Class<T> entityClass;
	
	@SuppressWarnings({"unchecked", "rawtypes" })
	public BaseDAOImpl() {
		this.entityClass = null;
		Class c = getClass();
		Type type = c.getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			Type[] parameterizedType = ((ParameterizedType) type).getActualTypeArguments();
			this.entityClass = (Class<T>) parameterizedType[0];
		}
	}
	
	/**
	 * 保存相应实体
	 * @param model
	 */
	public void save(T model) {
		hibernateTemplate.save(model);
	}
	/**
	 * 删除相应实体
	 * @param model
	 */
	public void delete(T model) {
		hibernateTemplate.delete(model);
	}
	/**
	 * 更新相应实体
	 * @param model
	 */
	public void update(T model) {
		hibernateTemplate.update(model);
	}
	/**
	 * 刷新session
	 */
	public void flush(){
		hibernateTemplate.flush();
	}
	/**
	 * 清空session
	 */
	public void clear(){
		hibernateTemplate.clear();
	}
	/**
	 * 移除相应实体
	 * @param model
	 */
	public void evict(Object object) {
		if(object!=null){
			hibernateTemplate.evict(object);
		}
	}
	/**
	 * 更新相应实体
	 * @param model
	 */
	public void merger(T model){
		hibernateTemplate.merge(model);
	}
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public T load(PK id) {
		return hibernateTemplate.load(entityClass, id);
	}
	
	public T get(PK id) {
		return hibernateTemplate.get(entityClass, id);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public T load(String propertyName, Object value) {
		String hql = "from " + entityClass.getName() + " as model where model." + propertyName + " = ?";
		List list = hibernateTemplate.find(hql, value);
		if(list.size()==1){
			return (T) list.get(0);
		}
		return null;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public T load(String[] paramNames, Object[] values) {
		String hql = "from " + entityClass.getName() + " as model where 1 = 1";
		for (String paramName : paramNames) {
			hql+= " and model." + paramName + " = ?";
		}
		List list = hibernateTemplate.find(hql, values);
		if(list.size()==1){
			return (T) list.get(0);
		}
		return null;
	}
	/**
	 * @Description: 取得当前的Session
	 * @Create: 2012-10-14 下午10:34:18
	 * @author lys
	 * @update logs
	 * @return
	 * @throws Exception
	 */
	protected Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
	/*
	 * (non-Javadoc)   
	 * @see org.linys.dao.BaseDAO#delete(java.io.Serializable)
	 */
	public void delete(PK pk) {
		T t = load(pk);
		delete(t);
	}
	/*
	 * (non-Javadoc)   
	 * @see org.linys.dao.BaseDAO#queryAll()
	 */
	@SuppressWarnings("unchecked")
	public List<T> queryAll() {
		String hql = "from " + entityClass.getName() + " as model";
		List<T> list = hibernateTemplate.find(hql);
		return list;
	}
	/*
	 * (non-Javadoc)   
	 * @see org.linys.dao.BaseDAO#query(java.lang.String, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public List<T> query(String propertyName, Object value) {
		String hql = "from " + entityClass.getName() + " as model where model." + propertyName + " = ?";
		List<T> list = hibernateTemplate.find(hql, value);
		return list;
	}
	@SuppressWarnings("unchecked")
	public List<T> query(String[] propertyNames, Object[] values) {
		String hql = "from " + entityClass.getName() + " as model where 1 = 1";
		for (String paramName : propertyNames) {
			hql+= " and model." + paramName + " = ?";
		}
		List<T> list = hibernateTemplate.find(hql, values);
		return list;
	}
}
