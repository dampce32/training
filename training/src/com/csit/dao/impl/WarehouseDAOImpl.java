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

import com.csit.dao.WarehouseDao;
import com.csit.model.Warehouse;
import com.csit.util.PageUtil;
import com.csit.vo.GobelConstants;
/**
 * @Description:仓库DAO实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
@Repository
public class WarehouseDAOImpl extends BaseDAOImpl<Warehouse, Integer>
		implements WarehouseDao {
	
	@SuppressWarnings("unchecked")
	public List<Warehouse> query(Warehouse model, Integer page, Integer rows) {
		Criteria criteria = getCurrentSession().createCriteria(Warehouse.class);
		
		if(model!=null&&StringUtils.isNotEmpty(model.getWarehouseName())){
			criteria.add(Restrictions.like("warehouseName", model.getWarehouseName(),MatchMode.ANYWHERE));
		}
		if(model!=null&&model.getSchool()!=null){
			if(model.getSchool().getSchoolId()==-1){//用户默认没有选择校区
				criteria.createAlias("school", "school", CriteriaSpecification.LEFT_JOIN);
				criteria.add(Restrictions.like("school.schoolCode",model.getSchool().getSchoolCode(),MatchMode.START));
			}else{
				criteria.add(Restrictions.eq("school",model.getSchool()));
			}
		}
		
		if(rows==null||rows<0){
			rows = GobelConstants.DEFAULTPAGESIZE;
		}
		
		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);
		
		criteria.addOrder(Order.desc("warehouseId"));		
		return criteria.list();
	}

	public Long getTotalCount(Warehouse model) {
		Criteria criteria  = getCurrentSession().createCriteria(Warehouse.class);
		
		if(model!=null&&StringUtils.isNotEmpty(model.getWarehouseName())){
			criteria.add(Restrictions.like("warehouseName", model.getWarehouseName(),MatchMode.ANYWHERE));
		}
		if(model!=null&&model.getSchool()!=null){
			if(model.getSchool().getSchoolId()==-1){//用户默认没有选择校区
				criteria.createAlias("school", "school", CriteriaSpecification.LEFT_JOIN);
				criteria.add(Restrictions.like("school.schoolCode",model.getSchool().getSchoolCode(),MatchMode.START));
			}else{
				criteria.add(Restrictions.eq("school",model.getSchool()));
			}
		}
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Warehouse> querySelfCombobox(String schoolCode) {
		Criteria criteria = getCurrentSession().createCriteria(Warehouse.class);
		criteria.createAlias("school", "school", CriteriaSpecification.LEFT_JOIN);
		
		criteria.add(Restrictions.like("school.schoolCode", schoolCode, MatchMode.START));
		criteria.add(Restrictions.eq("status", 1));
		criteria.addOrder(Order.asc("warehouseId"));
		return criteria.list();
	}
	
}
