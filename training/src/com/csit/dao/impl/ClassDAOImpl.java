package com.csit.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.csit.dao.ClassDAO;
import com.csit.model.Clazz;
import com.csit.util.PageUtil;
import com.csit.vo.GobelConstants;
/**
 * 
 * @Description:
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-4
 * @author yk
 * @vesion 1.0
 */
@Repository
public class ClassDAOImpl extends BaseDAOImpl<Clazz, Integer> implements
		ClassDAO {
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.ClassDAO#query(com.csit.model.Clazz, java.lang.Integer, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Clazz> query(Clazz model, Integer page, Integer rows,Integer status) {
		Criteria criteria  = getCurrentSession().createCriteria(Clazz.class);
		
		if(model!=null&&StringUtils.isNotEmpty(model.getClassName())){
			criteria.add(Restrictions.like("className", model.getClassName(),MatchMode.ANYWHERE));
		}
		if(model!=null&&model.getSchool()!=null&&model.getSchool().getSchoolId()!=null){
			criteria.add(Restrictions.like("school.schoolId", model.getSchool().getSchoolId()));
		}
		if(model!=null && model.getTeacher()!=null && model.getTeacher().getUserId()!=null){
			criteria.add(Restrictions.eq("teacher",model.getTeacher()));
		}
		
		Date today = new Date();
		if(status==1){
			criteria.add(Restrictions.or(Restrictions.isNull("endDate"), Restrictions.gt("startDate",today)));
		}else if(status==2){
			criteria.add(Restrictions.isNotNull("endDate"));
			criteria.add(Restrictions.le("startDate",today));
			criteria.add(Restrictions.ge("endDate",today));
		}else if(status==3){
			criteria.add(Restrictions.isNotNull("endDate"));
			criteria.add(Restrictions.lt("endDate",today));
		}else if(status==4){
			criteria.add(Restrictions.or(Restrictions.isNull("endDate"),Restrictions.ge("endDate",today)));
		}
		
		if(rows==null||rows<0){
			rows = GobelConstants.DEFAULTPAGESIZE;
		}
		
		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);
		
		criteria.addOrder(Order.desc("classId"));		
		return criteria.list();
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.ClassDAO#getTotalCount(com.csit.model.Clazz)
	 */
	@Override
	public Long getTotalCount(Clazz model,Integer status) {
		Criteria criteria  = getCurrentSession().createCriteria(Clazz.class);
		
		if(model!=null&&StringUtils.isNotEmpty(model.getClassName())){
			criteria.add(Restrictions.like("className", model.getClassName(),MatchMode.ANYWHERE));
		}
		if(model!=null&&model.getSchool()!=null&&model.getSchool().getSchoolId()!=null){
			criteria.add(Restrictions.like("school.schoolId", model.getSchool().getSchoolId()));
		}
		if(model!=null && model.getTeacher()!=null && model.getTeacher().getUserId()!=null){
			criteria.add(Restrictions.eq("teacher",model.getTeacher()));
		}
		
		Date today = new Date();
		if(status==1){
			criteria.add(Restrictions.or(Restrictions.isNull("endDate"), Restrictions.gt("startDate",today)));
		}else if(status==2){
			criteria.add(Restrictions.isNotNull("endDate"));
			criteria.add(Restrictions.le("startDate",today));
			criteria.add(Restrictions.ge("endDate",today));
		}else if(status==3){
			criteria.add(Restrictions.isNotNull("endDate"));
			criteria.add(Restrictions.lt("endDate",today));
		}else if(status==4){
			criteria.add(Restrictions.or(Restrictions.isNull("endDate"),Restrictions.ge("endDate",today)));
		}
		
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.ClassDAO#queryInSSTCombobox(java.lang.Integer, java.lang.Integer, java.lang.Integer, com.csit.model.Clazz)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Clazz> queryInSSTCombobox(Integer courseId,Integer schoolId,
			Integer lessonStatus, Integer teacherId) {
		Criteria criteria  = getCurrentSession().createCriteria(Clazz.class);
		
		criteria.add(Restrictions.eq("course.courseId",courseId));
		
		if(schoolId!=null){
			criteria.add(Restrictions.eq("school.schoolId",schoolId));
		}
		
		if(lessonStatus!=null){
			Date today = new Date();
			if(lessonStatus==0){
				criteria.add(Restrictions.gt("startDate",today));
			}else if(lessonStatus==1){
				criteria.add(Restrictions.le("startDate",today));
			}
		}
			
		if(teacherId!=null){
			criteria.add(Restrictions.eq("teacher.userId",teacherId));
		}
		
		criteria.addOrder(Order.asc("className"));
		return criteria.list();
	}

}
