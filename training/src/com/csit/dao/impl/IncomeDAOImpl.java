package com.csit.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.csit.dao.IncomeDAO;
import com.csit.model.Income;
import com.csit.util.PageUtil;
import com.csit.vo.GobelConstants;
/**
 * @Description:记帐表Dao实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-29
 * @author jcf
 * @vesion 1.0
 */
@Repository
public class IncomeDAOImpl extends BaseDAOImpl<Income, Integer> implements IncomeDAO {

	@Override
	public Long getTotalCount(Income model,Date accountDateBegin,Date accountDateEnd) {
		Criteria criteria = getCurrentSession().createCriteria(Income.class);

		if(model!=null&&model.getSchool()!=null){
			if(model.getSchool().getSchoolId()==-1){//用户默认没有选择校区
				criteria.createAlias("school", "school", CriteriaSpecification.LEFT_JOIN);
				criteria.add(Restrictions.like("school.schoolCode",model.getSchool().getSchoolCode(),MatchMode.START));
			}else{
				criteria.add(Restrictions.eq("school",model.getSchool()));
			}
		}
		if (accountDateBegin!=null) {
			criteria.add(Restrictions.ge("accountDate",accountDateBegin));
		}
		if (accountDateEnd!=null) {
			criteria.add(Restrictions.le("accountDate",accountDateEnd));
		}
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Income> query(Income model,Date accountDateBegin,Date accountDateEnd, Integer page, Integer rows) {
		Criteria criteria = getCurrentSession().createCriteria(Income.class);

		criteria.createAlias("user", "user", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("account", "account", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("incomeClass", "incomeClass", CriteriaSpecification.LEFT_JOIN);
		if(model!=null&&model.getSchool()!=null){
			if(model.getSchool().getSchoolId()==-1){//用户默认没有选择校区
				criteria.createAlias("school", "school", CriteriaSpecification.LEFT_JOIN);
				criteria.add(Restrictions.like("school.schoolCode",model.getSchool().getSchoolCode(),MatchMode.START));
			}else{
				criteria.add(Restrictions.eq("school",model.getSchool()));
			}
		}
		if (accountDateBegin!=null) {
			criteria.add(Restrictions.ge("accountDate",accountDateBegin));
		}
		if (accountDateEnd!=null) {
			criteria.add(Restrictions.le("accountDate",accountDateEnd));
		}

		if (rows == null || rows < 0) {
			rows = GobelConstants.DEFAULTPAGESIZE;
		}

		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);

		criteria.addOrder(Order.asc("incomeId"));
		return criteria.list();
	}

}
