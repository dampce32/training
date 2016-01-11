package com.csit.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.csit.dao.CourseTypeDAO;
import com.csit.model.CourseType;
import com.csit.util.PageUtil;
import com.csit.vo.GobelConstants;
/**
 * @Description:课程类型DAO实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author lys
 */
@Repository
public class CourseTypeDAOImpl extends BaseDAOImpl<CourseType, Integer>
		implements CourseTypeDAO {
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.CourseTypeDAO#query(com.csit.model.CourseType, java.lang.Integer, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CourseType> query(CourseType model, Integer page, Integer rows) {
		Criteria criteria  = getCurrentSession().createCriteria(CourseType.class);
		
		if(model!=null&&StringUtils.isNotEmpty(model.getCourseTypeName())){
			criteria.add(Restrictions.like("courseTypeName", model.getCourseTypeName(),MatchMode.ANYWHERE));
		}
		
		if(rows==null||rows<0){
			rows = GobelConstants.DEFAULTPAGESIZE;
		}
		
		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);
		
		criteria.addOrder(Order.asc("courseTypeId"));		
		return criteria.list();
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.CourseTypeDAO#getTotalCount(com.csit.model.CourseType)
	 */
	@Override
	public Long getTotalCount(CourseType model) {
		Criteria criteria  = getCurrentSession().createCriteria(CourseType.class);
		
		if(model!=null&&StringUtils.isNotEmpty(model.getCourseTypeName())){
			criteria.add(Restrictions.like("courseTypeName", model.getCourseTypeName(),MatchMode.ANYWHERE));
		}
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}

}
