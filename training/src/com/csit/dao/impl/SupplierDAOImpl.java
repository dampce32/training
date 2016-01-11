package com.csit.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.csit.dao.SupplierDao;
import com.csit.model.Supplier;
import com.csit.util.PageUtil;
import com.csit.vo.GobelConstants;
/**
 * @Description:供应商DAO实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
@Repository
public class SupplierDAOImpl extends BaseDAOImpl<Supplier, Integer>
		implements SupplierDao {
	
	@SuppressWarnings("unchecked")
	public List<Supplier> query(Supplier model, Integer page, Integer rows) {
		Criteria criteria  = getCurrentSession().createCriteria(Supplier.class);
		
		if(model!=null&&StringUtils.isNotEmpty(model.getSupplierName())){
			criteria.add(Restrictions.like("supplierName", model.getSupplierName(),MatchMode.ANYWHERE));
		}
		
		if(rows==null||rows<0){
			rows = GobelConstants.DEFAULTPAGESIZE;
		}
		
		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);
		
		criteria.addOrder(Order.asc("supplierId"));		
		return criteria.list();
	}

	public Long getTotalCount(Supplier model) {
		Criteria criteria  = getCurrentSession().createCriteria(Supplier.class);
		
		if(model!=null&&StringUtils.isNotEmpty(model.getSupplierName())){
			criteria.add(Restrictions.like("supplierName", model.getSupplierName(),MatchMode.ANYWHERE));
		}
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}
	
}
