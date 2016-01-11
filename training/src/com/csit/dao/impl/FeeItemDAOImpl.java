package com.csit.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.csit.dao.FeeItemDAO;
import com.csit.model.FeeItem;
import com.csit.util.PageUtil;
import com.csit.vo.GobelConstants;
/**
 * 
 * @Description: 消费项DAO实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-28
 * @author yk
 * @vesion 1.0
 */
@Repository
public class FeeItemDAOImpl extends BaseDAOImpl<FeeItem, Integer> implements
		FeeItemDAO {
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.FeeItemDAO#query(com.csit.model.FeeItem, java.lang.Integer, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<FeeItem> query(FeeItem model, Integer page, Integer rows, Integer[] feeItemIdArr) {
		Criteria criteria  = getCurrentSession().createCriteria(FeeItem.class);
		
		if(model!=null&&StringUtils.isNotEmpty(model.getFeeItemName())){
			criteria.add(Restrictions.like("feeItemName", model.getFeeItemName(),MatchMode.ANYWHERE));
		}
		if(model!=null&&model.getStatus()!=null){
			criteria.add(Restrictions.eq("status", model.getStatus()));
		}
		
		if(model!=null&&model.getFee()!=null){
			criteria.add(Restrictions.eq("fee", model.getFee()));
		}
		if(feeItemIdArr!=null && feeItemIdArr.length!=0){
			criteria.add(Restrictions.not(Restrictions.in("feeItemId", feeItemIdArr)));
		}
		if(rows==null||rows<0){
			rows = GobelConstants.DEFAULTPAGESIZE;
		}
		
		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);
		
		criteria.addOrder(Order.asc("feeItemId"));		
		return criteria.list();
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.FeeItemDAO#getTotalCount(com.csit.model.FeeItem)
	 */
	@Override
	public Long getTotalCount(FeeItem model, Integer[] feeItemIdArr) {
		Criteria criteria  = getCurrentSession().createCriteria(FeeItem.class);
		
		if(model!=null&&StringUtils.isNotEmpty(model.getFeeItemName())){
			criteria.add(Restrictions.like("feeItemName", model.getFeeItemName(),MatchMode.ANYWHERE));
		}
		if(model!=null&&model.getStatus()!=null){
			criteria.add(Restrictions.eq("status", model.getStatus()));
		}
		if(model!=null&&model.getFee()!=null){
			criteria.add(Restrictions.eq("fee", model.getFee()));
		}
		if(feeItemIdArr!=null && feeItemIdArr.length!=0){
			criteria.add(Restrictions.not(Restrictions.in("feeItemId", feeItemIdArr)));
		}
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}

}
