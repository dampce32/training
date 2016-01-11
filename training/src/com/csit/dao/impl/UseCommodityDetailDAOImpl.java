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

import com.csit.dao.UseCommodityDetailDao;
import com.csit.model.UseCommodity;
import com.csit.model.UseCommodityDetail;
import com.csit.util.PageUtil;
import com.csit.vo.GobelConstants;
/**
 * @Description:领用单DAO实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
@Repository
public class UseCommodityDetailDAOImpl extends BaseDAOImpl<UseCommodityDetail, Integer>
		implements UseCommodityDetailDao {

	@SuppressWarnings("unchecked")
	public List<UseCommodityDetail> queryNeedReturn(UseCommodity model,
			String commodityName, String userName, Integer rows, Integer page, Integer[] useCommodityDetailIdArr) {
		Criteria criteria = getCurrentSession().createCriteria(UseCommodityDetail.class);
		
		criteria.createAlias("useCommoidty", "useCommoidty",CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("commodity", "commodity",CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("warehouse", "warehouse",CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("useCommoidty.user", "user",CriteriaSpecification.LEFT_JOIN);
		criteria.add(Restrictions.ne("isNeedReturn",0));
		criteria.add(Restrictions.ne("returnStatus",0));
		if(StringUtils.isNotEmpty(commodityName)){
			criteria.add(Restrictions.like("commodity.commodityName",commodityName,MatchMode.ANYWHERE));
		}
		if(StringUtils.isNotEmpty(userName)){
			criteria.add(Restrictions.like("user.userName",userName,MatchMode.ANYWHERE));
		}
		
		if(rows==null||rows<0){
			rows = GobelConstants.DEFAULTPAGESIZE;
		}
		
		if(useCommodityDetailIdArr!=null && useCommodityDetailIdArr.length!=0){
			criteria.add(Restrictions.not(Restrictions.in("useCommodityDetailId", useCommodityDetailIdArr)));
		}
		
		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);
		
		criteria.addOrder(Order.desc("useCommodityDetailId"));		
		return criteria.list();
	}

	public Long getTotalCountNeedReturn(UseCommodity model,
			String commodityName, String userName,
			Integer[] useCommodityDetailIdArr) {
		Criteria criteria  = getCurrentSession().createCriteria(UseCommodityDetail.class);

		criteria.createAlias("useCommoidty", "useCommoidty",CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("commodity", "commodity",CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("warehouse", "warehouse",CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("useCommoidty.user", "user",CriteriaSpecification.LEFT_JOIN);
		criteria.add(Restrictions.ne("isNeedReturn",0));
		criteria.add(Restrictions.ne("returnStatus",0));
		if(StringUtils.isNotEmpty(commodityName)){
			criteria.add(Restrictions.like("commodity.commodityName",commodityName,MatchMode.ANYWHERE));
		}
		if(StringUtils.isNotEmpty(userName)){
			criteria.add(Restrictions.like("user.userName",userName,MatchMode.ANYWHERE));
		}
		
		if(useCommodityDetailIdArr!=null && useCommodityDetailIdArr.length!=0){
			criteria.add(Restrictions.not(Restrictions.in("useCommodityDetailId", useCommodityDetailIdArr)));
		}
		
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}
	
	
}
