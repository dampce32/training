package com.csit.dao.impl;

import java.sql.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.csit.dao.ReturnCommodityDao;
import com.csit.model.ReturnCommodity;
import com.csit.util.PageUtil;
/**
 * @Description领用单DAO实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
@Repository
public class ReturnCommodityDAOImpl extends BaseDAOImpl<ReturnCommodity, Integer>
		implements ReturnCommodityDao {

	@SuppressWarnings("unchecked")
	public List<ReturnCommodity> query(ReturnCommodity model, String beginDate,
			String endDate, Integer page, Integer rows) {
		Criteria criteria = getCurrentSession().createCriteria(ReturnCommodity.class);
		
		if(model!=null&&model.getUser()!=null&&model.getUser().getUserId()!=null){
			criteria.add(Restrictions.eq("user",model.getUser()));
		}
		if(StringUtils.isNotEmpty(beginDate)){
			criteria.add(Restrictions.ge("returnDate",Date.valueOf(beginDate)));
		}
		if(StringUtils.isNotEmpty(endDate)){
			criteria.add(Restrictions.le("returnDate",Date.valueOf(endDate)));
		}
		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);
		
		criteria.addOrder(Order.desc("returnCommodityId"));		
		return criteria.list();		
	}

	public Long getTotalCount(ReturnCommodity model, String beginDate,
			String endDate) {
		Criteria criteria  = getCurrentSession().createCriteria(ReturnCommodity.class);
		if(model!=null&&model.getUser()!=null&&model.getUser().getUserId()!=null){
			criteria.add(Restrictions.eq("user",model.getUser()));
		}
		if(StringUtils.isNotEmpty(beginDate)){
			criteria.add(Restrictions.ge("returnDate",Date.valueOf(beginDate)));
		}
		if(StringUtils.isNotEmpty(endDate)){
			criteria.add(Restrictions.le("returnDate",Date.valueOf(endDate)));
		}
		
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}
	
	
}
