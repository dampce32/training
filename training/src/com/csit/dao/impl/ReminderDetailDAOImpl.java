package com.csit.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.csit.dao.ReminderDetailDAO;
import com.csit.model.Reminder;
import com.csit.model.ReminderDetail;
/**
 * @Description:系统提醒明细DAO实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-4
 * @Author lys
 */
@Repository
public class ReminderDetailDAOImpl extends BaseDAOImpl<ReminderDetail, Integer>
		implements ReminderDetailDAO {
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.ReminderDetailDAO#queryByReminder(com.csit.model.Reminder)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ReminderDetail> queryByReminder(Reminder reminder) {
		Criteria criteria  = getCurrentSession().createCriteria(ReminderDetail.class);
		criteria.add(Restrictions.eq("reminder", reminder));
		return criteria.list();
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.ReminderDetailDAO#count(java.lang.String)
	 */
	@Override
	public Long count(String countSql) {
		Object object = getCurrentSession().createSQLQuery(countSql).uniqueResult();
		return object==null?null:Long.parseLong(object.toString());
	}

}
