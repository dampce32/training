package com.csit.dao.impl;

import java.sql.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.csit.dao.UseCommodityDao;
import com.csit.model.UseCommodity;
import com.csit.util.PageUtil;
/**
 * @Description领用单DAO实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
@Repository
public class UseCommodityDAOImpl extends BaseDAOImpl<UseCommodity, Integer>
		implements UseCommodityDao {
	
	@SuppressWarnings("unchecked")
	public List<UseCommodity> query(UseCommodity model, String beginDate, String endDate, Integer page, Integer rows) {
		Criteria criteria = getCurrentSession().createCriteria(UseCommodity.class);
		
		if(model!=null&&model.getUser()!=null&&model.getUser().getUserId()!=null){
			criteria.add(Restrictions.eq("user",model.getUser()));
		}
		if(StringUtils.isNotEmpty(beginDate)){
			criteria.add(Restrictions.ge("useDate",Date.valueOf(beginDate)));
		}
		if(StringUtils.isNotEmpty(endDate)){
			criteria.add(Restrictions.le("useDate",Date.valueOf(endDate)));
		}
		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);
		
		criteria.addOrder(Order.desc("useCommodityId"));		
		return criteria.list();		
	}

	public Long getTotalCount(UseCommodity model, String beginDate, String endDate) {
		Criteria criteria  = getCurrentSession().createCriteria(UseCommodity.class);
		if(model!=null&&model.getUser()!=null&&model.getUser().getUserId()!=null){
			criteria.add(Restrictions.eq("user",model.getUser()));
		}
		if(StringUtils.isNotEmpty(beginDate)){
			criteria.add(Restrictions.ge("useDate",Date.valueOf(beginDate)));
		}
		if(StringUtils.isNotEmpty(endDate)){
			criteria.add(Restrictions.le("useDate",Date.valueOf(endDate)));
		}
		
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}

	public Long initCode(String date) {
			
		Criteria criteria  = getCurrentSession().createCriteria(UseCommodity.class);
			
		criteria.add(Restrictions.like("UseCommodityCode", date,MatchMode.START));

		Long UseCommodityCode= 0l;
			
		if(criteria.list().size()==0){
			UseCommodityCode=Long.parseLong(date+"001");
		}else{
			UseCommodityCode =Long.parseLong((String) criteria.setProjection(Projections.max("UseCommodityCode")).uniqueResult());
			UseCommodityCode++;
		}
		return UseCommodityCode;
	}	
}
