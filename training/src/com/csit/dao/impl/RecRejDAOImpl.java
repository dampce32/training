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

import com.csit.dao.RecRejDao;
import com.csit.model.RecRej;
import com.csit.util.PageUtil;
/**
 * @Description:入库出库单DAO实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
@Repository
public class RecRejDAOImpl extends BaseDAOImpl<RecRej, Integer>
		implements RecRejDao {
	
	@SuppressWarnings("unchecked")
	public List<RecRej> query(RecRej model, String beginDate, String endDate, Integer page, Integer rows) {
		Criteria criteria = getCurrentSession().createCriteria(RecRej.class);
		
		if(model!=null&&StringUtils.isNotEmpty(model.getRecRejCode())){
			criteria.add(Restrictions.like("recRejCode",model.getRecRejCode(),MatchMode.ANYWHERE));
		}
		if(StringUtils.isNotEmpty(beginDate)){
			criteria.add(Restrictions.ge("recRejDate",Date.valueOf(beginDate)));
		}
		if(StringUtils.isNotEmpty(endDate)){
			criteria.add(Restrictions.le("recRejDate",Date.valueOf(endDate)));
		}
		if(model!=null){
			criteria.add(Restrictions.eq("recRejType",model.getRecRejType()));
		}
		
		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);
		
		criteria.addOrder(Order.desc("recRejCode"));		
		return criteria.list();		
	}

	public Long getTotalCount(RecRej model, String beginDate, String endDate) {
		Criteria criteria  = getCurrentSession().createCriteria(RecRej.class);
		
		if(model!=null&&StringUtils.isNotEmpty(model.getRecRejCode())){
			criteria.add(Restrictions.like("recRejCode",model.getRecRejCode(),MatchMode.ANYWHERE));
		}
		if(StringUtils.isNotEmpty(beginDate)){
			criteria.add(Restrictions.ge("recRejDate",Date.valueOf(beginDate)));
		}
		if(StringUtils.isNotEmpty(endDate)){
			criteria.add(Restrictions.le("recRejDate",Date.valueOf(endDate)));
		}
		if(model!=null){
			criteria.add(Restrictions.eq("recRejType",model.getRecRejType()));
		}
		
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}

	public Long initCode(String date) {
			
		Criteria criteria  = getCurrentSession().createCriteria(RecRej.class);
			
		criteria.add(Restrictions.like("recRejCode", date,MatchMode.START));

		Long recRejCode= 0l;
			
		if(criteria.list().size()==0){
			recRejCode=Long.parseLong(date+"001");
		}else{
			recRejCode =Long.parseLong((String) criteria.setProjection(Projections.max("recRejCode")).uniqueResult());
			recRejCode++;
		}
		return recRejCode;
	}	
}
