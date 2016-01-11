package com.csit.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.csit.dao.IncomeClassDAO;
import com.csit.model.IncomeClass;
import com.csit.util.PageUtil;
import com.csit.vo.GobelConstants;
/**
 * @Description:帐目分类表Dao实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-28
 * @author jcf
 * @vesion 1.0
 */
@Repository
public class IncomeClassDAOImpl extends BaseDAOImpl<IncomeClass, Integer> implements IncomeClassDAO {

	@Override
	public Long getTotalCount(IncomeClass model) {
		Criteria criteria = getCurrentSession().createCriteria(IncomeClass.class);

		if (model != null && StringUtils.isNotEmpty(model.getIncomeClassName())) {
			criteria.add(Restrictions.like("incomeClassName", model.getIncomeClassName(), MatchMode.ANYWHERE));
		}
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IncomeClass> query(IncomeClass model, Integer page, Integer rows) {
		Criteria criteria = getCurrentSession().createCriteria(IncomeClass.class);

		if (model != null && StringUtils.isNotEmpty(model.getIncomeClassName())) {
			criteria.add(Restrictions.like("incomeClassName", model.getIncomeClassName(), MatchMode.ANYWHERE));
		}

		if (rows == null || rows < 0) {
			rows = GobelConstants.DEFAULTPAGESIZE;
		}

		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);

		criteria.addOrder(Order.asc("incomeClassId"));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IncomeClass> queryByType(IncomeClass model) {
		Criteria criteria = getCurrentSession().createCriteria(IncomeClass.class);

		if (model != null && model.getStatus()!=null) {
			criteria.add(Restrictions.eq("status", model.getStatus()));
		}
		if (model != null && model.getType()!=null) {
			criteria.add(Restrictions.eq("type", model.getType()));
		}

		return criteria.list();
	}

}
