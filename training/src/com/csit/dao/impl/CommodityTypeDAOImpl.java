package com.csit.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.csit.dao.CommodityTypeDao;
import com.csit.model.CommodityType;
import com.csit.util.PageUtil;
import com.csit.vo.GobelConstants;
/**
 * @Description:商品分类DAO实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
@Repository
public class CommodityTypeDAOImpl extends BaseDAOImpl<CommodityType, Integer>
		implements CommodityTypeDao {
	
	@SuppressWarnings("unchecked")
	public List<CommodityType> query(CommodityType model, Integer page, Integer rows) {
		Criteria criteria  = getCurrentSession().createCriteria(CommodityType.class);
		
		if(model!=null&&StringUtils.isNotEmpty(model.getCommodityTypeName())){
			criteria.add(Restrictions.like("commodityTypeName", model.getCommodityTypeName(),MatchMode.ANYWHERE));
		}
		
		if(rows==null||rows<0){
			rows = GobelConstants.DEFAULTPAGESIZE;
		}
		
		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);
		
		criteria.addOrder(Order.desc("commodityTypeId"));		
		return criteria.list();
	}

	public Long getTotalCount(CommodityType model) {
		Criteria criteria  = getCurrentSession().createCriteria(CommodityType.class);
		
		if(model!=null&&StringUtils.isNotEmpty(model.getCommodityTypeName())){
			criteria.add(Restrictions.like("commodityTypeName", model.getCommodityTypeName(),MatchMode.ANYWHERE));
		}
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}
	
}
