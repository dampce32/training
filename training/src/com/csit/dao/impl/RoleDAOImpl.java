package com.csit.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.csit.dao.RoleDAO;
import com.csit.model.Role;

@Repository
public class RoleDAOImpl extends BaseDAOImpl<Role, Integer> implements RoleDAO {
	/*
	 * (non-Javadoc)   
	 * @see org.linys.dao.RoleDAO#query(org.linys.model.Role)
	 */
	@SuppressWarnings("unchecked")
	public List<Role> query(Role model) {
		Criteria criteria = getCurrentSession().createCriteria(Role.class);
		if(model!=null&&StringUtils.isNotEmpty(model.getRoleName())){
			criteria.add(Restrictions.like("roleName", model.getRoleName(), MatchMode.ANYWHERE));
		}
		criteria.addOrder(Order.asc("roleName"));
		return criteria.list();
	}
	/*
	 * (non-Javadoc)   
	 * @see org.linys.dao.RoleDAO#queryAll()
	 */
	@SuppressWarnings("unchecked")
	public List<Role> queryAll() {
		Criteria criteria = getCurrentSession().createCriteria(Role.class);
		criteria.addOrder(Order.asc("roleName"));
		return criteria.list();
	}
}
