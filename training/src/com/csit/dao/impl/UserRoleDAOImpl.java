package com.csit.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.csit.dao.UserRoleDAO;
import com.csit.model.UserRole;
import com.csit.model.UserRoleId;
@Repository
public class UserRoleDAOImpl extends BaseDAOImpl<UserRole, UserRoleId> implements UserRoleDAO {
	/*
	 * (non-Javadoc)   
	 * @see org.linys.dao.UserRoleDAO#queryRole(org.linys.model.UserRole)
	 */
	@SuppressWarnings("unchecked")
	public List<UserRole> queryRole(UserRole model) {
		Criteria criteria = getCurrentSession().createCriteria(UserRole.class);
		if(model!=null&&model.getUser()!=null&&model.getUser().getUserId()!=null){
			criteria.add(Restrictions.eq("user.userId",model.getUser().getUserId()));
		}
		
		return criteria.list();
	}


}
