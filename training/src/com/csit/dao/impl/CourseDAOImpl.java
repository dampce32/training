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

import com.csit.dao.CourseDAO;
import com.csit.model.Course;
import com.csit.util.PageUtil;
import com.csit.vo.GobelConstants;

/**
 * 
 * @Description: 课程DAO实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-28
 * @author yk
 * @vesion 1.0
 */
@Repository
public class CourseDAOImpl extends BaseDAOImpl<Course, Integer> implements
		CourseDAO {
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.CourseDAO#query(com.csit.model.Course, java.lang.Integer, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Course> query(Course model, Integer page, Integer rows, Integer[] courseIdArr) {
		Criteria criteria  = getCurrentSession().createCriteria(Course.class);
		
		if(model!=null&&StringUtils.isNotEmpty(model.getCourseName())){
			criteria.add(Restrictions.like("courseName", model.getCourseName(),MatchMode.ANYWHERE));
		}
		if(model!=null&&model.getStatus()!=null){
			criteria.add(Restrictions.eq("status", model.getStatus()));
		}
		if(model!=null&&model.getCourseType()!=null&&model.getCourseType().getCourseTypeId()!=null){
			criteria.add(Restrictions.eq("courseType.courseTypeId", model.getCourseType().getCourseTypeId()));
		}
		if(courseIdArr!=null && courseIdArr.length!=0){
			criteria.add(Restrictions.not(Restrictions.in("courseId", courseIdArr)));
		}
		if(rows==null||rows<0){
			rows = GobelConstants.DEFAULTPAGESIZE;
		}
		
		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);
		
		criteria.addOrder(Order.asc("courseId"));		
		return criteria.list();
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.CourseDAO#getTotalCount(com.csit.model.Course)
	 */
	@Override
	public Long getTotalCount(Course model, Integer[] courseIdArr) {
		Criteria criteria  = getCurrentSession().createCriteria(Course.class);
		
		if(model!=null&&StringUtils.isNotEmpty(model.getCourseName())){
			criteria.add(Restrictions.like("courseName", model.getCourseName(),MatchMode.ANYWHERE));
		}
		if(model!=null&&model.getStatus()!=null){
			criteria.add(Restrictions.eq("status", model.getStatus()));
		}
		if(model!=null&&model.getCourseType()!=null&&model.getCourseType().getCourseTypeId()!=null){
			criteria.add(Restrictions.eq("courseType.courseTypeId", model.getCourseType().getCourseTypeId()));
		}
		if(courseIdArr!=null && courseIdArr.length!=0){
			criteria.add(Restrictions.not(Restrictions.in("courseId", courseIdArr)));
		}
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.CourseDAO#queryIsTypeCombobox(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Course> queryIsTypeCombobox(Integer courseTypeId) {
		Criteria criteria = getCurrentSession().createCriteria(Course.class);
		criteria.createAlias("courseType", "courseType", CriteriaSpecification.LEFT_JOIN);
		
		if(courseTypeId!=null){
			criteria.add(Restrictions.eq("courseType.courseTypeId", courseTypeId));
		}
		
		criteria.addOrder(Order.asc("courseId"));
		return criteria.list();
	}
	
}
