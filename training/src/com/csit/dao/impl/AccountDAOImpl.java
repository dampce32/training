package com.csit.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.csit.dao.AccountDAO;
import com.csit.model.Account;
import com.csit.util.PageUtil;
import com.csit.vo.GobelConstants;
/**
 * @Description:帐户表Dao实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-28
 * @author jcf
 * @vesion 1.0
 */
@Repository
public class AccountDAOImpl extends BaseDAOImpl<Account, Integer> implements AccountDAO {

	@Override
	public Long getTotalCount(Account model) {
		Criteria criteria = getCurrentSession().createCriteria(Account.class);

		if (model != null && StringUtils.isNotEmpty(model.getAccountName())) {
			criteria.add(Restrictions.like("accountName", model.getAccountName(), MatchMode.ANYWHERE));
		}
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Account> query(Account model, Integer page, Integer rows) {
		Criteria criteria = getCurrentSession().createCriteria(Account.class);

		if (model != null && StringUtils.isNotEmpty(model.getAccountName())) {
			criteria.add(Restrictions.like("accountName", model.getAccountName(), MatchMode.ANYWHERE));
		}

		if (rows == null || rows < 0) {
			rows = GobelConstants.DEFAULTPAGESIZE;
		}

		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);

		criteria.addOrder(Order.asc("accountId"));
		return criteria.list();
	}

}
