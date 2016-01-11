package com.csit.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.csit.dao.RoleRightDAO;
import com.csit.model.RoleRight;
import com.csit.model.RoleRightId;

@Repository
public class RoleRightDAOImpl extends BaseDAOImpl<RoleRight, RoleRightId> implements RoleRightDAO {
	/*
	 * (non-Javadoc)   
	 * @see org.linys.dao.RoleRightDAO#queryRoot(org.linys.model.RoleRight)
	 */
	@SuppressWarnings("unchecked")
	public List<RoleRight> queryRoot(RoleRight model) {
		Assert.notNull(model, "model is required");
		Criteria criteria = getCurrentSession().createCriteria(RoleRight.class);
		criteria.add(Restrictions.eq("role.roleId", model.getRole().getRoleId()));
		criteria.createAlias("right", "right");
		criteria.add(Restrictions.isNull("right.parentRight"));
		return criteria.list();
	}
	/*
	 * (non-Javadoc)   
	 * @see org.linys.dao.RoleRightDAO#queryChildren(org.linys.model.RoleRight)
	 */
	@SuppressWarnings("unchecked")
	public List<RoleRight> queryChildren(RoleRight model) {
		Assert.notNull(model, "role is required");
		Criteria criteria = getCurrentSession().createCriteria(RoleRight.class);
		criteria.add(Restrictions.eq("role.roleId", model.getRole().getRoleId()));
		criteria.createAlias("right", "right");
		criteria.add(Restrictions.eq("right.parentRight.rightId",model.getRight().getRightId()));
		criteria.addOrder(Order.asc("right"));
		return criteria.list();
	}
	/*
	 * (non-Javadoc)   
	 * @see org.linys.dao.RoleRightDAO#updateState(java.lang.Integer, java.lang.Integer, java.lang.Boolean)
	 */
	public void updateState(Integer roleId, Integer rightId, Integer state) {
		StringBuilder hql = new StringBuilder();
		hql.append("update RoleRight roleRight set state = :state where roleRight.id.roleId = :roleId and roleRight.id.rightId = :rightId");
		getCurrentSession().createQuery(hql.toString()).setInteger("roleId", roleId).setInteger("rightId", rightId).setInteger("state", state).executeUpdate();
	}
	/*
	 * (non-Javadoc)   
	 * @see org.linys.dao.RoleRightDAO#countChildrenStateSameParent(org.linys.model.RoleRight, boolean)
	 */
	public Integer countChildrenStateSameParent(RoleRight model, Integer state) {
		StringBuilder sql = new StringBuilder();
		sql.append( "select COUNT(1) ");
		sql.append( "from(select * ");
		sql.append( "	from T_Right  ");
		sql.append( "	where RightID = :rightId)a ");
		sql.append( "left join T_Right b on a.ParentRightID = b.ParentRightID ");
		sql.append( "left join (select * ");
		sql.append( "	from T_RoleRight  ");
		sql.append( "	where RoleID = :roleId)c on b.RightID = c.RightID ");
		sql.append( "where c.state = :state ");
		Object obj = getCurrentSession().createSQLQuery(sql.toString()).setInteger("roleId", model.getRole().getRoleId()).setInteger("rightId", model.getRight().getRightId())
				.setInteger("state", state).uniqueResult();
		return Integer.parseInt(obj.toString());
	}
}
