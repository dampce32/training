package com.csit.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.csit.dao.ReminderDAO;
import com.csit.model.Reminder;
import com.csit.vo.GobelConstants;
/**
 * @Description:系统提醒DAO实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-4
 * @Author lys
 */
@Repository
public class ReminderDAOImpl extends BaseDAOImpl<Reminder, Integer> implements
		ReminderDAO {
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.ReminderDAO#query(com.csit.model.Reminder, java.lang.Integer, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Reminder> query(Reminder model, Integer page, Integer rows) {
		Criteria criteria  = getCurrentSession().createCriteria(Reminder.class);
		
		if(model!=null&&StringUtils.isNotEmpty(model.getTitle())){
			criteria.add(Restrictions.like("title", model.getTitle(),MatchMode.ANYWHERE));
		}
		
		if(page==null||page<1){
			page = 1;
		}
		if(rows==null||rows<0){
			rows = GobelConstants.DEFAULTPAGESIZE;
		}
		
		Integer begin = (page-1)*rows;
		
		criteria.setFirstResult(begin);
		criteria.setMaxResults(rows);
		
		criteria.addOrder(Order.asc("array"));
		return criteria.list();
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.ReminderDAO#getTotalCount(com.csit.model.Reminder)
	 */
	@Override
	public Long getTotalCount(Reminder model) {
		Criteria criteria  = getCurrentSession().createCriteria(Reminder.class);
		
		if(model!=null&&StringUtils.isNotEmpty(model.getTitle())){
			criteria.add(Restrictions.like("title", model.getTitle(),MatchMode.ANYWHERE));
		}
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.ReminderDAO#getMaxArray()
	 */
	@Override
	public Integer getMaxArray() {
		Criteria criteria = getCurrentSession().createCriteria(Reminder.class);
		criteria.setProjection(Projections.max("array"));
		Object obj = criteria.uniqueResult();
		return obj==null?0:Integer.parseInt(obj.toString());
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.ReminderDAO#getCurrentUserReminder(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getCurrentUserReminder(Integer userId) {
		StringBuilder sql = new StringBuilder();
		
		sql.append( "select a.reminderId,e.title,b.reminderItemId,c.rightId,b.message,b.countSql,c.rightName,c.rightUrl ");
		sql.append( "from t_reminderdetail  a ");
		sql.append( "left join t_reminderitem b on a.reminderItemId = b.reminderItemId ");
		sql.append( "left join t_right c on b.rightId = c.rightId ");
		sql.append( "left join (select  distinct b.rightId ");
		sql.append( "		from(select * ");
		sql.append( "			from t_userrole a ");
		sql.append( "			where a.userId = :userId)a ");
		sql.append( "left join t_roleright b on a.roleId = b.roleId ");
		sql.append( "where b.state = 1)d on c.rightId = d.rightId ");
		sql.append( "left join t_reminder e on a.reminderId = e.reminderId ");
		sql.append( "where c.rightId = d.rightId and e.`status` = 1 ");
		sql.append( "order by e.array asc,b.reminderItemId ");
		return getCurrentSession().createSQLQuery(sql.toString()).setInteger("userId", userId).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	
	}

}
