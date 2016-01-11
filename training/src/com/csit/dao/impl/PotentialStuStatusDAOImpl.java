package com.csit.dao.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.csit.dao.PotentialStuStatusDAO;
import com.csit.model.PotentialStuStatus;
import com.csit.util.PageUtil;
import com.csit.vo.GobelConstants;
/**
 * 
 * @Description:媒体Dao实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-28
 * @author jcf
 * @vesion 1.0
 */
@Repository
public class PotentialStuStatusDAOImpl extends BaseDAOImpl<PotentialStuStatus, Integer> implements PotentialStuStatusDAO {

	@Override
	public Long getTotalCount(PotentialStuStatus model) {
		Criteria criteria = getCurrentSession().createCriteria(PotentialStuStatus.class);

		if (model != null && StringUtils.isNotEmpty(model.getPotentialStuStatusName())) {
			criteria.add(Restrictions.like("potentialStuStatusName", model.getPotentialStuStatusName(), MatchMode.ANYWHERE));
		}
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PotentialStuStatus> query(PotentialStuStatus model, Integer page, Integer rows) {
		Criteria criteria = getCurrentSession().createCriteria(PotentialStuStatus.class);

		if (model != null && StringUtils.isNotEmpty(model.getPotentialStuStatusName())) {
			criteria.add(Restrictions.like("potentialStuStatusName", model.getPotentialStuStatusName(), MatchMode.ANYWHERE));
		}

		if (rows == null || rows < 0) {
			rows = GobelConstants.DEFAULTPAGESIZE;
		}

		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);

		criteria.addOrder(Order.asc("potentialStuStatusId"));
		return criteria.list();
	}

}
