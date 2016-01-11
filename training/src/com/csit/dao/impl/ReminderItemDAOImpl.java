package com.csit.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.csit.dao.ReminderItemDAO;
import com.csit.model.ReminderItem;
import com.csit.util.PageUtil;
import com.csit.vo.GobelConstants;
/**
 * @Description:系统提醒项DAO实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-4
 * @Author lys
 */
@Repository
public class ReminderItemDAOImpl extends BaseDAOImpl<ReminderItem, Integer>
		implements ReminderItemDAO {
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.ReminderItemDAO#query(com.csit.model.ReminderItem, java.lang.Integer, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ReminderItem> query(ReminderItem model, Integer page,
			Integer rows) {
		Criteria criteria  = getCurrentSession().createCriteria(ReminderItem.class);
		
		if(model!=null&&StringUtils.isNotEmpty(model.getTitle())){
			criteria.add(Restrictions.like("title", model.getTitle(),MatchMode.ANYWHERE));
		}
		
		
		if(rows==null||rows<0){
			rows = GobelConstants.DEFAULTPAGESIZE;
		}
		
		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);
		
		criteria.addOrder(Order.asc("reminderItemId"));		
		return criteria.list();
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.ReminderItemDAO#getTotalCount(com.csit.model.ReminderItem)
	 */
	@Override
	public Long getTotalCount(ReminderItem model) {
		Criteria criteria  = getCurrentSession().createCriteria(ReminderItem.class);
		
		if(model!=null&&StringUtils.isNotEmpty(model.getTitle())){
			criteria.add(Restrictions.like("title", model.getTitle(),MatchMode.ANYWHERE));
		}
		
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.ReminderItemDAO#selectQuery(java.lang.String, com.csit.model.ReminderItem, java.lang.Integer, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ReminderItem> selectQuery(Integer[] idArray, ReminderItem model,
			Integer page, Integer rows) {
		Criteria criteria  = getCurrentSession().createCriteria(ReminderItem.class);
		
		if(model!=null&&StringUtils.isNotEmpty(model.getTitle())){
			criteria.add(Restrictions.like("title", model.getTitle(),MatchMode.ANYWHERE));
		}
		if(idArray!=null){
			criteria.add(Restrictions.not(Restrictions.in("reminderItemId", idArray)));
		}
		if(rows==null||rows<0){
			rows = GobelConstants.DEFAULTPAGESIZE;
		}
		
		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);
		
		criteria.addOrder(Order.asc("reminderItemId"));		
		return criteria.list();
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.ReminderItemDAO#getTotalCountSelectQuery(com.csit.model.ReminderItem)
	 */
	@Override
	public Long getTotalCountSelectQuery(Integer[] idArray,ReminderItem model) {
		Criteria criteria  = getCurrentSession().createCriteria(ReminderItem.class);
		
		if(model!=null&&StringUtils.isNotEmpty(model.getTitle())){
			criteria.add(Restrictions.like("title", model.getTitle(),MatchMode.ANYWHERE));
		}
		if(idArray!=null){
			criteria.add(Restrictions.not(Restrictions.in("reminderItemId", idArray)));
		}
		
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}

}
