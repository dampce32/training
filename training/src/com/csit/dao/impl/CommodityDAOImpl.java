package com.csit.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.csit.dao.CommodityDao;
import com.csit.model.Commodity;
import com.csit.util.PageUtil;
import com.csit.vo.GobelConstants;

/**
 * @Description:商品DAO实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
@Repository
public class CommodityDAOImpl extends BaseDAOImpl<Commodity, Integer>
		implements CommodityDao {
	
	@SuppressWarnings("unchecked")
	public List<Commodity> query(Commodity model, Integer page, Integer rows, Integer[] commodityIdArr) {
		Criteria criteria = getCurrentSession().createCriteria(Commodity.class);
		
		if(model!=null&&StringUtils.isNotEmpty(model.getCommodityName())){
			criteria.add(Restrictions.like("commodityName", model.getCommodityName(),MatchMode.ANYWHERE));
		}
		
		if(model!=null&&model.getUnit()!=null&&model.getUnit().getUnitId()!=null){
			criteria.add(Restrictions.eq("unit", model.getUnit()));
		}
		if(model!=null&&model.getCommodityType()!=null&&model.getCommodityType().getCommodityTypeId()!=null){
			criteria.add(Restrictions.eq("commodityType", model.getCommodityType()));
		}
		if(model!=null&&model.getStatus()!=null){
			criteria.add(Restrictions.eq("status", model.getStatus()));
		}
		if(model!=null&&model.getStatus()!=null){
			criteria.add(Restrictions.eq("status", model.getStatus()));
		}
		
		if(commodityIdArr!=null && commodityIdArr.length!=0){
			criteria.add(Restrictions.not(Restrictions.in("commodityId", commodityIdArr)));
		}
		
		if(rows==null||rows<0){
			rows = GobelConstants.DEFAULTPAGESIZE;
		}
		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);
		
		criteria.addOrder(Order.desc("commodityId"));		
		return criteria.list();
	}

	public Long getTotalCount(Commodity model,Integer[] commodityIdArr) {
		Criteria criteria  = getCurrentSession().createCriteria(Commodity.class);
		
		if(model!=null&&StringUtils.isNotEmpty(model.getCommodityName())){
			criteria.add(Restrictions.like("commodityName", model.getCommodityName(),MatchMode.ANYWHERE));
		}
		
		if(model!=null&&model.getUnit()!=null&&model.getUnit().getUnitId()!=null){
			criteria.add(Restrictions.eq("unit", model.getUnit()));
		}
		if(model!=null&&model.getCommodityType()!=null&&model.getCommodityType().getCommodityTypeId()!=null){
			criteria.add(Restrictions.eq("commodityType", model.getCommodityType()));
		}
		if(model!=null&&model.getStatus()!=null){
			criteria.add(Restrictions.eq("status", model.getStatus()));
		}
		if(model!=null&&model.getStatus()!=null){
			criteria.add(Restrictions.eq("status", model.getStatus()));
		}
		if(commodityIdArr!=null && commodityIdArr.length!=0){
			criteria.add(Restrictions.not(Restrictions.in("commodityId", commodityIdArr)));
		}
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<Commodity> queryQtyWarm(Commodity model,Integer page, Integer rows) {
		Criteria criteria = getCurrentSession().createCriteria(Commodity.class);
		
		criteria.add(Expression.ltProperty("qtyStore","qtyWarn"));
		
		if(model!=null&&StringUtils.isNotEmpty(model.getCommodityName())){
			criteria.add(Restrictions.like("commodityName", model.getCommodityName(),MatchMode.ANYWHERE));
		}
		
		if(model!=null&&model.getUnit()!=null&&model.getUnit().getUnitId()!=null){
			criteria.add(Restrictions.eq("unit", model.getUnit()));
		}
		if(model!=null&&model.getCommodityType()!=null&&model.getCommodityType().getCommodityTypeId()!=null){
			criteria.add(Restrictions.eq("commodityType", model.getCommodityType()));
		}
		
		if(rows==null||rows<0){
			rows = GobelConstants.DEFAULTPAGESIZE;
		}
		
		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);
		
		criteria.addOrder(Order.desc("commodityId"));		
		return criteria.list();
	}

	@SuppressWarnings("deprecation")
	public Long getTotalCountQtyWarm(Commodity model) {
		Criteria criteria = getCurrentSession().createCriteria(Commodity.class);
		criteria.add(Expression.ltProperty("qtyStore","qtyWarn"));
		if(model!=null&&StringUtils.isNotEmpty(model.getCommodityName())){
			criteria.add(Restrictions.like("commodityName", model.getCommodityName(),MatchMode.ANYWHERE));
		}
		
		if(model!=null&&model.getUnit()!=null&&model.getUnit().getUnitId()!=null){
			criteria.add(Restrictions.eq("unit", model.getUnit()));
		}
		if(model!=null&&model.getCommodityType()!=null&&model.getCommodityType().getCommodityTypeId()!=null){
			criteria.add(Restrictions.eq("commodityType", model.getCommodityType()));
		}
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}
}
