package com.csit.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.csit.dao.RewPunTypeDao;
import com.csit.model.RewPunType;
import com.csit.util.PageUtil;
import com.csit.vo.GobelConstants;
/**
 * @Description:奖惩分类DAO实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
@Repository
public class RewPunTypeDAOImpl extends BaseDAOImpl<RewPunType, Integer>
		implements RewPunTypeDao {
	
	@SuppressWarnings("unchecked")
	public List<RewPunType> query(RewPunType model, Integer page, Integer rows) {
		Criteria criteria  = getCurrentSession().createCriteria(RewPunType.class);
		
		if(model!=null&&StringUtils.isNotEmpty(model.getRewPunTypeName())){
			criteria.add(Restrictions.like("rewPunTypeName", model.getRewPunTypeName(),MatchMode.ANYWHERE));
		}
		
		if(rows==null||rows<0){
			rows = GobelConstants.DEFAULTPAGESIZE;
		}
		
		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);
		
		criteria.addOrder(Order.desc("rewPunTypeId"));		
		return criteria.list();
	}

	public Long getTotalCount(RewPunType model) {
		Criteria criteria  = getCurrentSession().createCriteria(RewPunType.class);
		
		if(model!=null&&StringUtils.isNotEmpty(model.getRewPunTypeName())){
			criteria.add(Restrictions.like("rewPunTypeName", model.getRewPunTypeName(),MatchMode.ANYWHERE));
		}
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}
	
}
