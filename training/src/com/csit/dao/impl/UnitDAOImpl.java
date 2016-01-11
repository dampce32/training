package com.csit.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.csit.dao.UnitDao;
import com.csit.model.Unit;
import com.csit.util.PageUtil;
import com.csit.vo.GobelConstants;
/**
 * @Description:商品单位DAO实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
@Repository
public class UnitDAOImpl extends BaseDAOImpl<Unit, Integer>
		implements UnitDao {
	
	@SuppressWarnings("unchecked")
	public List<Unit> query(Unit model, Integer page, Integer rows) {
		Criteria criteria  = getCurrentSession().createCriteria(Unit.class);
		
		if(model!=null&&StringUtils.isNotEmpty(model.getUnitName())){
			criteria.add(Restrictions.like("unitName", model.getUnitName(),MatchMode.ANYWHERE));
		}
		
		if(rows==null||rows<0){
			rows = GobelConstants.DEFAULTPAGESIZE;
		}
		
		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);
		
		criteria.addOrder(Order.desc("unitId"));		
		return criteria.list();
	}

	public Long getTotalCount(Unit model) {
		Criteria criteria  = getCurrentSession().createCriteria(Unit.class);
		
		if(model!=null&&StringUtils.isNotEmpty(model.getUnitName())){
			criteria.add(Restrictions.like("unitName", model.getUnitName(),MatchMode.ANYWHERE));
		}
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}
	
}
