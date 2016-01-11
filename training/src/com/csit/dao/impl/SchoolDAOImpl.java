package com.csit.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.csit.dao.SchoolDAO;
import com.csit.model.School;
import com.csit.util.PageUtil;
/**
 * @Description:校区DAO实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-25
 * @Author lys
 */
@Repository
public class SchoolDAOImpl extends BaseDAOImpl<School, Integer> implements
		SchoolDAO {
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.SchoolDAO#selectRoot()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public School selectRoot() {
		Criteria criteria = getCurrentSession().createCriteria(School.class);
		criteria.add(Restrictions.isNull("parentSchool"));
		criteria.addOrder(Order.asc("array"));
		List<School> list = criteria.list();
		if(list!=null&&list.size()==1){
			return list.get(0);
		}
		return null;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.SchoolDAO#getChildren(com.csit.model.School)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<School> getChildren(School school) {
		Criteria criteria = getCurrentSession().createCriteria(School.class);
		criteria.createAlias("parentSchool", "model").add(Restrictions.eq("model.schoolId", school.getSchoolId()));
		criteria.addOrder(Order.asc("array"));
		return criteria.list();
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.SchoolDAO#query(java.lang.Integer, java.lang.Integer, com.csit.model.School)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<School> query(Integer page, Integer rows, School model) {
		Criteria criteria = getCurrentSession().createCriteria(School.class);
		if(model.getSchoolId()==null){
			criteria.add(Restrictions.isNull("parentSchool"));
		}else{
			criteria.createAlias("parentSchool", "model").add(Restrictions.eq("model.schoolId",model.getSchoolId()));
		}
		if(StringUtils.isNotEmpty(model.getSchoolName())){
			criteria.add(Restrictions.like("schoolName", model.getSchoolName(),MatchMode.ANYWHERE));
		} 
		
		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);
		criteria.addOrder(Order.asc("array"));
		return criteria.list();
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.SchoolDAO#count(com.csit.model.School)
	 */
	@Override
	public Long count(School model) {
		Criteria criteria = getCurrentSession().createCriteria(School.class);
		if(model.getSchoolId()==null){
			criteria.add(Restrictions.isNull("parentSchool"));
		}else{
			criteria.createAlias("parentSchool", "model").add(Restrictions.eq("model.schoolId",model.getSchoolId()));
		}
		if(StringUtils.isNotEmpty(model.getSchoolName())){
			criteria.add(Restrictions.like("schoolName", model.getSchoolName(),MatchMode.ANYWHERE));
		} 
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.SchoolDAO#getNewCode(java.lang.String)
	 */
	@Override
	public String getNewCode(String schoolCode) {
		StringBuilder sb = new StringBuilder();
		sb.append("select max(school.schoolCode) ");
		sb.append("from School school ");
		sb.append("where school.schoolCode like :schoolCode and length(school.schoolCode) = :schoolCodeLength ");
		
		Query query = getCurrentSession().createQuery(sb.toString());
		query.setString("schoolCode", schoolCode+"%");
		query.setInteger("schoolCodeLength", schoolCode.length()+2);
		Object result = query.uniqueResult();
		String maxCode = result==null?null:result.toString();
		int index = 0;
		if(maxCode!=null){
			String code = maxCode.substring(schoolCode.length(), maxCode.length());
			if(StringUtils.isNotEmpty(code)){
				index = Integer.parseInt(code);	
			}
		}
		maxCode = schoolCode+String.format("%02d", index+1);
		return maxCode;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.SchoolDAO#getMaxArray(java.lang.Integer)
	 */
	@Override
	public Integer getMaxArray(Integer schoolId) {
		Criteria criteria = getCurrentSession().createCriteria(School.class);
		criteria.createAlias("parentSchool", "model").add(Restrictions.eq("model.schoolId", schoolId));
		criteria.setProjection(Projections.max("array"));
		Object obj = criteria.uniqueResult();
		return obj==null?0:Integer.parseInt(obj.toString());
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.SchoolDAO#countChildren(java.lang.Integer)
	 */
	@Override
	public Long countChildren(Integer parentID) {
		StringBuilder hql = new StringBuilder("select count(*) from School model where model.parentSchool.schoolId =:parentID ");
		return (Long) getCurrentSession().createQuery(hql.toString()).setInteger("parentID", parentID).uniqueResult();
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.SchoolDAO#updateIsLeaf(java.lang.Integer, boolean)
	 */
	@Override
	public void updateIsLeaf(Integer schoolId, boolean isLeaf) {
		StringBuilder hql = new StringBuilder("update School set isLeaf = :isLeaf where schoolId = :schoolId");
		getCurrentSession().createQuery(hql.toString()).setBoolean("isLeaf", isLeaf).setInteger("schoolId", schoolId).executeUpdate();
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.SchoolDAO#querySelfCombobox(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<School> querySelfCombobox(String schoolCode) {
		Criteria criteria = getCurrentSession().createCriteria(School.class);
		if(StringUtils.isNotEmpty(schoolCode)){
			criteria.add(Restrictions.like("schoolCode", schoolCode,MatchMode.START));
		}
		criteria.add(Restrictions.eq("status", 1));
		criteria.addOrder(Order.asc("array"));
		return criteria.list();
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.SchoolDAO#queryAllCombobox()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<School> queryAllCombobox() {
		Criteria criteria = getCurrentSession().createCriteria(School.class);
		criteria.add(Restrictions.eq("status", 1));
		criteria.addOrder(Order.asc("array"));
		return criteria.list();
	}

}
