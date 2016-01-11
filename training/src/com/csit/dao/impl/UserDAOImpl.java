package com.csit.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.csit.dao.UserDAO;
import com.csit.model.User;
import com.csit.vo.GobelConstants;

@Repository("userDAO")
public class UserDAOImpl  extends BaseDAOImpl<User,Integer> implements UserDAO{

	public User login(User user) {
		if(user!=null){
			String[] paramNams = {"userCode","userPwd"};
			Object[] values ={user.getUserCode(),user.getUserPwd()};
			return load(paramNams, values);
		}
		return null;
	}
	/*
	 * (non-Javadoc)   
	 * @see org.linys.dao.UserDAO#query(int, int, org.linys.model.User)
	 */
	@SuppressWarnings("unchecked")
	public List<User> query(int page, int rows, User model) {
		Criteria criteria = getCurrentSession().createCriteria(User.class);
		if(model!=null&&StringUtils.isNotEmpty(model.getUserCode())){
			criteria.add(Restrictions.like("userCode",model.getUserCode(),MatchMode.ANYWHERE));
		}
		if(model!=null&&StringUtils.isNotEmpty(model.getUserName())){
			criteria.add(Restrictions.like("userName",model.getUserName(),MatchMode.ANYWHERE));
		}
		if(model!=null&&model.getSchool()!=null){
			if(model.getSchool().getSchoolId()==-1){//用户默认没有选择校区
				criteria.createAlias("school", "school", CriteriaSpecification.LEFT_JOIN);
				criteria.add(Restrictions.like("school.schoolCode",model.getSchool().getSchoolCode(),MatchMode.START));
			}else{
				criteria.add(Restrictions.eq("school",model.getSchool()));
			}
		}
		
		if(page<1){
			page = 1;
		}
		if(rows<1){
			rows = GobelConstants.DEFAULTPAGESIZE;
		}
		
		Integer begin = (page-1)*rows;
		criteria.setFirstResult(begin);
		criteria.setMaxResults(rows);
		
		criteria.addOrder(Order.asc("userCode"));
		return criteria.list();
	}
	/*
	 * (non-Javadoc)   
	 * @see org.linys.dao.UserDAO#count(org.linys.model.User)
	 */
	public Long count(User model) {
		Criteria criteria = getCurrentSession().createCriteria(User.class);
		if(model!=null&&StringUtils.isNotEmpty(model.getUserCode())){
			criteria.add(Restrictions.like("userCode",model.getUserCode(),MatchMode.ANYWHERE));
		}
		if(model!=null&&StringUtils.isNotEmpty(model.getUserName())){
			criteria.add(Restrictions.like("userName",model.getUserName(),MatchMode.ANYWHERE));
		}
		if(model!=null&&model.getSchool()!=null){
			if(model.getSchool().getSchoolId()==-1){//用户默认没有选择校区
				criteria.createAlias("school", "school", CriteriaSpecification.LEFT_JOIN);
				criteria.add(Restrictions.like("school.schoolCode",model.getSchool().getSchoolCode(),MatchMode.START));
			}else{
				criteria.add(Restrictions.eq("school",model.getSchool()));
			}
		}
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}
	/*
	 * (non-Javadoc)   
	 * @see org.linys.dao.UserDAO#getRooRight(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getRootRight(Integer userId) {
		StringBuilder sql = new StringBuilder();
		sql.append( "select c.array,b.rightId,max(b.State) state,c.isLeaf,c.rightName,c.rightUrl ");
		sql.append( "from(select RoleId from T_UserRole ");
		sql.append( "where userId = :userId) a ");
		sql.append( "left join T_RoleRight b on a.RoleId = b.RoleID ");
		sql.append( "left join T_Right c on b.RightId = c.RightId ");
		sql.append( "where c.ParentRightId is null ");
		sql.append( "group by  c.array,b.RightID,c.IsLeaf,c.RightName,c.RightURL");
		return getCurrentSession().createSQLQuery(sql.toString()).setInteger("userId", userId).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	/*
	 * (non-Javadoc)   
	 * @see org.linys.dao.UserDAO#getChildrenRight(java.lang.Integer, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getChildrenRight(Integer userId,Integer rightId) {
		StringBuilder sql = new StringBuilder();
		sql.append( "select c.array,b.rightId,MAX(b.State) state,c.isLeaf,c.rightName,c.rightUrl ");
		sql.append( "from(select RoleId from T_UserRole ");
		sql.append( "where userId = :userId) a ");
		sql.append( "left join T_RoleRight b on a.RoleId = b.RoleID ");
		sql.append( "left join T_Right c on b.RightId = c.RightId ");
		sql.append( "where c.ParentRightId = :rightId ");
		sql.append( "group by  c.array,b.RightID,c.IsLeaf,c.RightName,c.RightURL ");
		sql.append( "having max(b.State) = 1 ");
		return getCurrentSession().createSQLQuery(sql.toString()).setInteger("userId", userId).setInteger("rightId", rightId).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	/*
	 * (non-Javadoc)   
	 * @see org.linys.dao.UserDAO#queryUserRight(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryUserRight(Integer userId) {
		StringBuilder sql = new StringBuilder();
		sql.append( "select b.RightID,MAX(b.State) State ");
		sql.append( "from(select roleId from T_UserRole ");
		sql.append( "where userId = :userId) a ");
		sql.append( "left join T_RoleRight b on a.roleId = b.RoleID ");
		sql.append( "group by b.RightID");
		return getCurrentSession().createSQLQuery(sql.toString()).setInteger("userId", userId).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.UserDAO#queryEmployee(com.csit.model.User, java.lang.Integer, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<User> queryEmployee(User model, Integer page, Integer rows) {
		Criteria criteria = getCurrentSession().createCriteria(User.class);
		if(model!=null&&model.getUserId()!=null){
			criteria.add(Restrictions.eq("userId",model.getUserId()));
		}
		if(model!=null&&StringUtils.isNotEmpty(model.getUserName())){
			criteria.add(Restrictions.like("userName",model.getUserName(),MatchMode.ANYWHERE));
		}
		if(model!=null&&model.getSchool()!=null){
			if(model.getSchool().getSchoolId()==-1){//用户默认没有选择校区
				criteria.createAlias("school", "school", CriteriaSpecification.LEFT_JOIN);
				criteria.add(Restrictions.like("school.schoolCode",model.getSchool().getSchoolCode(),MatchMode.START));
			}else{
				criteria.add(Restrictions.eq("school",model.getSchool()));
			}
		}
		if(page<1){
			page = 1;
		}
		if(rows<1){
			rows = GobelConstants.DEFAULTPAGESIZE;
		}
		Integer begin = (page-1)*rows;
		criteria.setFirstResult(begin);
		criteria.setMaxResults(rows);
		
		criteria.addOrder(Order.asc("userId"));
		return criteria.list();
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.UserDAO#getTotalCountEmployee(com.csit.model.User)
	 */
	@Override
	public Long getTotalCountEmployee(User model) {
		Criteria criteria = getCurrentSession().createCriteria(User.class);
		if(model!=null&&model.getUserId()!=null){
			criteria.add(Restrictions.eq("userId",model.getUserId()));
		}
		if(model!=null&&StringUtils.isNotEmpty(model.getUserName())){
			criteria.add(Restrictions.like("userName",model.getUserName(),MatchMode.ANYWHERE));
		}
		if(model!=null&&model.getSchool()!=null){
			if(model.getSchool().getSchoolId()==-1){//用户默认没有选择校区
				criteria.createAlias("school", "school", CriteriaSpecification.LEFT_JOIN);
				criteria.add(Restrictions.like("school.schoolCode",model.getSchool().getSchoolCode(),MatchMode.START));
			}else{
				criteria.add(Restrictions.eq("school",model.getSchool()));
			}
		}
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}
	/*
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<User> queryCombobox(String schoolCode) {
		Criteria criteria = getCurrentSession().createCriteria(User.class);
		criteria.createAlias("school", "school", CriteriaSpecification.LEFT_JOIN);
		
		criteria.add(Restrictions.like("school.schoolCode", schoolCode, MatchMode.START));
		criteria.add(Restrictions.eq("status", 1));
		criteria.addOrder(Order.asc("userId"));
		return criteria.list();
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.UserDAO#queryIsTeacherCombobox(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<User> queryIsTeacherCombobox(Integer isTeacher, String schoolCode) {
		Criteria criteria = getCurrentSession().createCriteria(User.class);
		criteria.createAlias("school", "school", CriteriaSpecification.LEFT_JOIN);
		
		criteria.add(Restrictions.like("school.schoolCode", schoolCode, MatchMode.START));
		criteria.add(Restrictions.eq("isTeacher", 1));
		criteria.add(Restrictions.eq("status", 1));
		criteria.addOrder(Order.asc("userId"));
		return criteria.list();
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.UserDAO#getChildrenRight(java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getChildrenRight(Integer userId,
			Integer rightId, Integer kind) {
		StringBuilder sql = new StringBuilder();
		sql.append( "select c.array,b.rightId,max(b.State) state,c.isLeaf,c.rightName,c.rightUrl ");
		sql.append( "from(select RoleId from T_UserRole ");
		sql.append( "where userId = :userId) a ");
		sql.append( "left join T_RoleRight b on a.RoleId = b.RoleID ");
		sql.append( "left join T_Right c on b.RightId = c.RightId ");
		sql.append( "where c.ParentRightId = :rightId ");
		if(kind!=null){
			sql.append( "and c.kind = :kind ");
		}
		sql.append( "group by  c.array,b.RightID,c.IsLeaf,c.RightName,c.RightURL ");
		sql.append( "having max(b.State) = 1 ");
		
		Query query = getCurrentSession().createSQLQuery(sql.toString()).setInteger("userId", userId).setInteger("rightId", rightId);
		if(kind!=null){
			query = query.setInteger("kind", kind);
		}
		return query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	
	}
}
