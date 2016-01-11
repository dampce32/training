package com.csit.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.csit.dao.ReportConfigDAO;
import com.csit.model.ReportConfig;
import com.csit.vo.GobelConstants;
/**
 * @Description:报表配置DAO实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-1-23
 * @author lys
 * @vesion 1.0
 */
@Repository
public class ReportConfigDAOImpl extends BaseDAOImpl<ReportConfig, String>
		implements ReportConfigDAO {
	/*
	 * (non-Javadoc)   
	 * @see org.linys.dao.ReportConfigDAO#query(org.linys.model.ReportConfig, java.lang.Integer, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ReportConfig> query(ReportConfig model, Integer page,
			Integer rows) {
		Criteria criteria  = getCurrentSession().createCriteria(ReportConfig.class);
		
		if(model!=null&&StringUtils.isNotEmpty(model.getReportCode())){
			criteria.add(Restrictions.like("reportCode", model.getReportCode(),MatchMode.ANYWHERE));
		}
		
		if(model!=null&&StringUtils.isNotEmpty(model.getReportName())){
			criteria.add(Restrictions.like("reportName", model.getReportCode(),MatchMode.ANYWHERE));
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
		
		criteria.addOrder(Order.asc("reportCode"));
		return criteria.list();
	}
	/*
	 * (non-Javadoc)   
	 * @see org.linys.dao.ReportConfigDAO#getTotalCount(org.linys.model.ReportConfig)
	 */
	@Override
	public Long getTotalCount(ReportConfig model) {
		Criteria criteria  = getCurrentSession().createCriteria(ReportConfig.class);
		
		if(model!=null&&StringUtils.isNotEmpty(model.getReportCode())){
			criteria.add(Restrictions.like("reportCode", model.getReportCode(),MatchMode.ANYWHERE));
		}
		
		if(model!=null&&StringUtils.isNotEmpty(model.getReportName())){
			criteria.add(Restrictions.like("reportName", model.getReportCode(),MatchMode.ANYWHERE));
		}
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}
	/*
	 * (non-Javadoc)   
	 * @see org.linys.dao.ReportConfigDAO#getReport1()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ReportConfig> getReport1() {
		Criteria criteria  = getCurrentSession().createCriteria(ReportConfig.class);
		criteria.add(Restrictions.eq("reportKind", 1));
		criteria.addOrder(Order.asc("reportCode"));
		return criteria.list();
	}

}
