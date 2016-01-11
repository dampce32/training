package com.csit.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.csit.dao.PeriodTimeDAO;
import com.csit.model.PeriodTime;
import com.csit.util.PageUtil;
import com.csit.vo.GobelConstants;
/**
 * 
 * @Description: 时间段DAO实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-1
 * @author yk
 * @vesion 1.0
 */
@Repository
public class PeriodTimeDAOImpl extends BaseDAOImpl<PeriodTime, Integer> implements PeriodTimeDAO {
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.PeriodTimeDAO#query(com.csit.model.PeriodTime, java.lang.Integer, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PeriodTime> query(PeriodTime model, Integer page, Integer rows) {
		Criteria criteria  = getCurrentSession().createCriteria(PeriodTime.class);
		
		if(model!=null&&model.getGroupType()!=null){
			criteria.add(Restrictions.eq("groupType", model.getGroupType()));
		}
		
		if(rows==null||rows<0){
			rows = GobelConstants.DEFAULTPAGESIZE;
		}
		
		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);
		
		criteria.addOrder(Order.asc("startTime"));		
		return criteria.list();
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.PeriodTimeDAO#getTotalCount(com.csit.model.PeriodTime)
	 */
	@Override
	public Long getTotalCount(PeriodTime model) {
		Criteria criteria  = getCurrentSession().createCriteria(PeriodTime.class);
		
		if(model!=null&&model.getGroupType()!=null){
			criteria.add(Restrictions.eq("groupType", model.getGroupType()));
		}
		
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.PeriodTimeDAO#queryInGroupTypeCombobox(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PeriodTime> queryInGroupTypeCombobox(Integer groupType) {
		Criteria criteria = getCurrentSession().createCriteria(PeriodTime.class);
		
		criteria.add(Restrictions.eq("groupType", groupType));
		criteria.addOrder(Order.asc("startTime"));
		return criteria.list();
	}

}
