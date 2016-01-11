package com.csit.dao.impl;

import java.sql.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.csit.dao.StoreTuneOutDao;
import com.csit.model.StoreTuneOut;
import com.csit.util.PageUtil;
/**
 * @Description:调拨单DAO实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
@Repository
public class StoreTuneOutDAOImpl extends BaseDAOImpl<StoreTuneOut, Integer>
		implements StoreTuneOutDao {
	
	@SuppressWarnings("unchecked")
	public List<StoreTuneOut> query(StoreTuneOut model, String beginDate, String endDate, Integer page, Integer rows) {
		Criteria criteria = getCurrentSession().createCriteria(StoreTuneOut.class);
		criteria.createAlias("commodity", "commodity",CriteriaSpecification.LEFT_JOIN);
		if(model!=null&&model.getUser()!=null&&model.getUser().getUserId()!=null){
			criteria.add(Restrictions.eq("user",model.getUser()));
		}
		if(model!=null&&model.getCommodity()!=null&&StringUtils.isNotEmpty(model.getCommodity().getCommodityName())){
			criteria.add(Restrictions.like("commodity.commodityName",model.getCommodity().getCommodityName(),MatchMode.ANYWHERE));
		}
		
		if(StringUtils.isNotEmpty(beginDate)){
			criteria.add(Restrictions.ge("useDate",Date.valueOf(beginDate)));
		}
		if(StringUtils.isNotEmpty(endDate)){
			criteria.add(Restrictions.le("useDate",Date.valueOf(endDate)));
		}
		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);
		
		criteria.addOrder(Order.desc("storeTuneOutId"));		
		return criteria.list();		
	}

	public Long getTotalCount(StoreTuneOut model, String beginDate, String endDate) {
		Criteria criteria  = getCurrentSession().createCriteria(StoreTuneOut.class);
		criteria.createAlias("commodity", "commodity",CriteriaSpecification.LEFT_JOIN);
		if(model!=null&&model.getUser()!=null&&model.getUser().getUserId()!=null){
			criteria.add(Restrictions.eq("user",model.getUser()));
		}
		if(model!=null&&model.getCommodity()!=null&&StringUtils.isNotEmpty(model.getCommodity().getCommodityName())){
			criteria.add(Restrictions.like("commodity.commodityName",model.getCommodity().getCommodityName(),MatchMode.ANYWHERE));
		}
		
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}

}
