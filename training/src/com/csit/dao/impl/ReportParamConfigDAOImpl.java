package com.csit.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.csit.dao.ReportParamConfigDAO;
import com.csit.model.ReportParamConfig;
/**
 * @Description:报表参数配置DAO实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-1-31
 * @author lys
 * @vesion 1.0
 */
@Repository
public class ReportParamConfigDAOImpl extends
		BaseDAOImpl<ReportParamConfig, String> implements ReportParamConfigDAO {
	/*
	 * (non-Javadoc)   
	 * @see org.linys.dao.ReportParamConfigDAO#queryByReportConfigId(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ReportParamConfig> queryByReportConfigId(String reportConfigId) {
		Criteria criteria  = getCurrentSession().createCriteria(ReportParamConfig.class);
		criteria.add(Restrictions.eq("reportConfig.reportConfigId", reportConfigId));
		criteria.addOrder(Order.asc("array"));
		return criteria.list();
	}

}
