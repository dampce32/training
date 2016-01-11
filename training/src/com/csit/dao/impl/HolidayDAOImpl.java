package com.csit.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.csit.dao.HolidayDAO;
import com.csit.model.Holiday;
import com.csit.util.PageUtil;
import com.csit.vo.GobelConstants;
/**
 * 
 * @Description: 假期表DAO实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-28
 * @author yk
 * @vesion 1.0
 */
@Repository
public class HolidayDAOImpl extends BaseDAOImpl<Holiday, Integer> implements
		HolidayDAO {
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.HolidayDAO#query(com.csit.model.Holiday, java.lang.Integer, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Holiday> query(Holiday model, Integer page, Integer rows) {
		Criteria criteria  = getCurrentSession().createCriteria(Holiday.class);
		
		if(model!=null&&StringUtils.isNotEmpty(model.getHolidayName())){
			criteria.add(Restrictions.like("holidayName", model.getHolidayName(),MatchMode.ANYWHERE));
		}
		
		if(rows==null||rows<0){
			rows = GobelConstants.DEFAULTPAGESIZE;
		}
		
		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);
		
		criteria.addOrder(Order.asc("startDate"));		
		return criteria.list();
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.HolidayDAO#getTotalCount(com.csit.model.Holiday)
	 */
	@Override
	public Long getTotalCount(Holiday model) {
		Criteria criteria  = getCurrentSession().createCriteria(Holiday.class);
		
		if(model!=null&&StringUtils.isNotEmpty(model.getHolidayName())){
			criteria.add(Restrictions.like("holidayName", model.getHolidayName(),MatchMode.ANYWHERE));
		}
		
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}

}
