package com.csit.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.csit.dao.ClassroomDAO;
import com.csit.model.Classroom;
import com.csit.util.PageUtil;
import com.csit.vo.GobelConstants;
/**
 * 
 * @Description: 教室DAO实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-28
 * @author yk
 * @vesion 1.0
 */
@Repository
public class ClassroomDAOImpl extends BaseDAOImpl<Classroom, Integer> implements
		ClassroomDAO {
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.ClassroomDAO#query(com.csit.model.Classroom, java.lang.Integer, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Classroom> query(Classroom model, Integer page, Integer rows) {
		Criteria criteria  = getCurrentSession().createCriteria(Classroom.class);
		
		if(model!=null&&StringUtils.isNotEmpty(model.getClassroomName())){
			criteria.add(Restrictions.like("classroomName", model.getClassroomName(),MatchMode.ANYWHERE));
		}
		
		if(model!=null&&model.getSeating()!=null){
			criteria.add(Restrictions.eq("seating", model.getSeating()));
		}
		if(model!=null&&model.getSchool()!=null){
			if(model.getSchool().getSchoolId()==-1){//用户默认没有选择校区
				criteria.createAlias("school", "school", CriteriaSpecification.LEFT_JOIN);
				criteria.add(Restrictions.like("school.schoolCode",model.getSchool().getSchoolCode(),MatchMode.START));
			}else{
				criteria.add(Restrictions.eq("school",model.getSchool()));
			}
		}
		
		if(rows==null||rows<0){
			rows = GobelConstants.DEFAULTPAGESIZE;
		}
		
		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);
		
		criteria.addOrder(Order.asc("classroomId"));		
		return criteria.list();
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.ClassroomDAO#getTotalCount(com.csit.model.Classroom)
	 */
	@Override
	public Long getTotalCount(Classroom model) {
		Criteria criteria  = getCurrentSession().createCriteria(Classroom.class);
		
		if(model!=null&&StringUtils.isNotEmpty(model.getClassroomName())){
			criteria.add(Restrictions.like("classroomName", model.getClassroomName(),MatchMode.ANYWHERE));
		}
		
		if(model!=null&&model.getSeating()!=null){
			criteria.add(Restrictions.eq("seating", model.getSeating()));
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
	 * @see com.csit.dao.ClassroomDAO#queryInSchoolCombobox(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Classroom> queryInSchoolCombobox(Integer schoolId) {
		Criteria criteria = getCurrentSession().createCriteria(Classroom.class);
		criteria.createAlias("school", "school", CriteriaSpecification.LEFT_JOIN);
		
		criteria.add(Restrictions.eq("school.schoolId", schoolId));
		criteria.addOrder(Order.asc("classroomId"));
		return criteria.list();
	}

}
