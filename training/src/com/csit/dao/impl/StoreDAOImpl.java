package com.csit.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.csit.dao.StoreDao;
import com.csit.model.Commodity;
import com.csit.model.RecRej;
import com.csit.model.Store;
import com.csit.model.Warehouse;
import com.csit.util.PageUtil;
import com.csit.vo.GobelConstants;
/**
 * @Description:入库出库单DAO实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
@Repository
public class StoreDAOImpl extends BaseDAOImpl<Store, Integer>
		implements StoreDao {

	public Store query(Commodity commodity, Warehouse warehouse) {
		Assert.notNull(commodity, "commodity is required");
		Assert.notNull(warehouse, "warehouse is required");
		Criteria criteria  = getCurrentSession().createCriteria(Store.class);

		criteria.add(Restrictions.eq("commodity", commodity));
		criteria.add(Restrictions.eq("warehouse", warehouse));

		return (Store) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<RecRej> query(Store model, Integer page, Integer rows) {
		
		Criteria criteria = getCurrentSession().createCriteria(Store.class);
		
		criteria.createAlias("commodity", "commodity",CriteriaSpecification.LEFT_JOIN);
		if(model!=null&&model.getCommodity()!=null&&StringUtils.isNotEmpty(model.getCommodity().getCommodityName())){
			criteria.add(Restrictions.like("commodity.commodityName", model.getCommodity().getCommodityName(),MatchMode.ANYWHERE));
		}
		
		if(model!=null&&model.getWarehouse()!=null&&model.getWarehouse().getWarehouseId()!=null){
			criteria.add(Restrictions.eq("warehouse", model.getWarehouse()));
		}
		if(model!=null&&model.getCommodity()!=null&&model.getCommodity().getCommodityType()!=null&&model.getCommodity().getCommodityType().getCommodityTypeId()!=null){
			criteria.add(Restrictions.eq("commodity.commodityType", model.getCommodity().getCommodityType()));
		}
		
		if(rows==null||rows<0){
			rows = GobelConstants.DEFAULTPAGESIZE;
		}
		
		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);
		
		criteria.addOrder(Order.desc("storeId"));		
		return criteria.list();
	}

	public Long getTotalCount(Store model) {
		Criteria criteria  = getCurrentSession().createCriteria(Store.class);
		
		criteria.createAlias("commodity", "commodity",CriteriaSpecification.LEFT_JOIN);
		if(model!=null&&model.getCommodity()!=null&&StringUtils.isNotEmpty(model.getCommodity().getCommodityName())){
			criteria.add(Restrictions.like("commodity.commodityName", model.getCommodity().getCommodityName(),MatchMode.ANYWHERE));
		}
		
		if(model!=null&&model.getWarehouse()!=null&&model.getWarehouse().getWarehouseId()!=null){
			criteria.add(Restrictions.eq("warehouse", model.getWarehouse()));
		}
		if(model!=null&&model.getCommodity()!=null&&model.getCommodity().getCommodityType()!=null&&model.getCommodity().getCommodityType().getCommodityTypeId()!=null){
			criteria.add(Restrictions.eq("commodity.commodityType", model.getCommodity().getCommodityType()));
		}
		
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}

	@SuppressWarnings("unchecked")
	public List<Store> queryNotEmpty(Store model, Integer page, Integer rows) {
		Criteria criteria  = getCurrentSession().createCriteria(Store.class);
		
		criteria.createAlias("commodity", "commodity",CriteriaSpecification.LEFT_JOIN);
		criteria.add(Restrictions.ne("qtyStore",0));
		if(model!=null&&model.getCommodity()!=null&&StringUtils.isNotEmpty(model.getCommodity().getCommodityName())){
			criteria.add(Restrictions.like("commodity.commodityName", model.getCommodity().getCommodityName(),MatchMode.ANYWHERE));
		}
		
		if(model!=null&&model.getWarehouse()!=null&&model.getWarehouse().getWarehouseId()!=null){
			criteria.add(Restrictions.eq("warehouse", model.getWarehouse()));
		}
		if(model!=null&&model.getCommodity()!=null&&model.getCommodity().getCommodityType()!=null&&model.getCommodity().getCommodityType().getCommodityTypeId()!=null){
			criteria.add(Restrictions.eq("commodity.commodityType", model.getCommodity().getCommodityType()));
		}
		
		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);
		
		criteria.addOrder(Order.asc("storeId"));		
		return criteria.list();
	}

	public Long getTotalCountNotEmpty(Store model) {
		
		Criteria criteria  = getCurrentSession().createCriteria(Store.class);
		
		criteria.createAlias("commodity", "commodity",CriteriaSpecification.LEFT_JOIN);
		criteria.add(Restrictions.ne("qtyStore",0));
		if(model!=null&&model.getCommodity()!=null&&StringUtils.isNotEmpty(model.getCommodity().getCommodityName())){
			criteria.add(Restrictions.like("commodity.commodityName", model.getCommodity().getCommodityName(),MatchMode.ANYWHERE));
		}
		
		if(model!=null&&model.getWarehouse()!=null&&model.getWarehouse().getWarehouseId()!=null){
			criteria.add(Restrictions.eq("warehouse", model.getWarehouse()));
		}
		if(model!=null&&model.getCommodity()!=null&&model.getCommodity().getCommodityType()!=null&&model.getCommodity().getCommodityType().getCommodityTypeId()!=null){
			criteria.add(Restrictions.eq("commodity.commodityType", model.getCommodity().getCommodityType()));
		}
		
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}
}
