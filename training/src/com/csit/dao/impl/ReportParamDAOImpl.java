package com.csit.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.csit.dao.ReportParamDAO;
import com.csit.model.ReportParam;
import com.csit.vo.GobelConstants;
/**
 * @Description:报表参数DAO实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-1-31
 * @author lys
 * @vesion 1.0
 */
@Repository
public class ReportParamDAOImpl extends BaseDAOImpl<ReportParam, String>
		implements ReportParamDAO {
	/*
	 * (non-Javadoc)   
	 * @see org.linys.dao.ReportParamDAO#query(org.linys.model.ReportParam, java.lang.Integer, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ReportParam> query(ReportParam model, Integer page, Integer rows) {
		Criteria criteria  = getCurrentSession().createCriteria(ReportParam.class);
		
		
		if(model!=null&&StringUtils.isNotEmpty(model.getParamCode())){
			criteria.add(Restrictions.like("paramCode", model.getParamCode(),MatchMode.ANYWHERE));
		}
		
		if(page==null||page<1){
			page = 1;
		}
		if(rows==null||rows<0){
			rows = GobelConstants.DEFAULTPAGESIZE;
		}
		
		Integer begin = (page-1)*rows;
		
		criteria.setFirstResult(begin);
		criteria.setMaxResults(rows);
		
		criteria.addOrder(Order.asc("paramCode"));
		
		return criteria.list();
	}
	/*
	 * (non-Javadoc)   
	 * @see org.linys.dao.ReportParamDAO#getTotalCount(org.linys.model.ReportParam)
	 */
	@Override
	public Long getTotalCount(ReportParam model) {
		Criteria criteria  = getCurrentSession().createCriteria(ReportParam.class);
		
		if(model!=null&&StringUtils.isNotEmpty(model.getParamCode())){
			criteria.add(Restrictions.like("paramCode", model.getParamCode(),MatchMode.ANYWHERE));
		}
		
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}
	/*
	 * (non-Javadoc)   
	 * @see org.linys.dao.ReportParamDAO#select(org.linys.model.ReportParam, java.lang.String[], java.lang.Integer, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ReportParam> select(ReportParam model, String[] idArray,
			Integer page, Integer rows) {
		Criteria criteria  = getCurrentSession().createCriteria(ReportParam.class);
		
		
		if(model!=null&&StringUtils.isNotEmpty(model.getParamCode())){
			criteria.add(Restrictions.like("paramCode", model.getParamCode(),MatchMode.ANYWHERE));
		}
		
		criteria.add(Restrictions.not(Restrictions.in("reportParamId", idArray)));
		
		if(page==null||page<1){
			page = 1;
		}
		if(rows==null||rows<0){
			rows = GobelConstants.DEFAULTPAGESIZE;
		}
		
		Integer begin = (page-1)*rows;
		
		criteria.setFirstResult(begin);
		criteria.setMaxResults(rows);
		
		criteria.addOrder(Order.asc("paramCode"));
		return criteria.list();
	}
	/*
	 * (non-Javadoc)   
	 * @see org.linys.dao.ReportParamDAO#getTotalCountSelect(org.linys.model.ReportParam, java.lang.String[])
	 */
	@Override
	public Long getTotalCountSelect(ReportParam model, String[] idArray) {
		Criteria criteria  = getCurrentSession().createCriteria(ReportParam.class);
		
		
		if(model!=null&&StringUtils.isNotEmpty(model.getParamCode())){
			criteria.add(Restrictions.like("paramCode", model.getParamCode(),MatchMode.ANYWHERE));
		}
		
		criteria.add(Restrictions.not(Restrictions.in("reportParamId", idArray)));
		
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}

}
