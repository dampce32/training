package com.csit.dao.impl;

import java.sql.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.csit.dao.ScrappedDao;
import com.csit.model.Scrapped;
import com.csit.util.PageUtil;
/**
 * @Description:入库出库单DAO实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
@Repository
public class ScrappedDAOImpl extends BaseDAOImpl<Scrapped, Integer>
		implements ScrappedDao {
	
	@SuppressWarnings("unchecked")
	public List<Scrapped> query(Scrapped model, String beginDate, String endDate, Integer page, Integer rows) {
		Criteria criteria = getCurrentSession().createCriteria(Scrapped.class);
		
		if(StringUtils.isNotEmpty(beginDate)){
			criteria.add(Restrictions.ge("scrappedDate",Date.valueOf(beginDate)));
		}
		if(StringUtils.isNotEmpty(endDate)){
			criteria.add(Restrictions.le("scrappedDate",Date.valueOf(endDate)));
		}
		
		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);
		
		criteria.addOrder(Order.desc("scrappedId"));		
		return criteria.list();		
	}

	public Long getTotalCount(Scrapped model, String beginDate, String endDate) {
		Criteria criteria  = getCurrentSession().createCriteria(Scrapped.class);
		
		if(StringUtils.isNotEmpty(beginDate)){
			criteria.add(Restrictions.ge("scrappedDate",Date.valueOf(beginDate)));
		}
		if(StringUtils.isNotEmpty(endDate)){
			criteria.add(Restrictions.le("scrappedDate",Date.valueOf(endDate)));
		}
		
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}

}
