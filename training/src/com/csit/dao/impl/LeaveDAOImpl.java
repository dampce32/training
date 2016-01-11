package com.csit.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.csit.dao.LeaveDao;
import com.csit.model.Leave;
import com.csit.util.PageUtil;
/**
 * @Description:请假分类DAO实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
@Repository
public class LeaveDAOImpl extends BaseDAOImpl<Leave, Integer>
		implements LeaveDao {
	
	@SuppressWarnings("unchecked")
	public List<Leave> query(Leave model,String schoolCode, Integer page, Integer rows) {
		Criteria criteria  = getCurrentSession().createCriteria(Leave.class);
		criteria.createAlias("student", "student", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("student.school", "school", CriteriaSpecification.LEFT_JOIN);
		if("".equals(schoolCode)){
			criteria.add(Restrictions.like("school.schoolCode",schoolCode,MatchMode.START));
		}
		else {
			criteria.add(Restrictions.eq("school.schoolCode",schoolCode));
		}
		if(model!=null && model.getStudent()!=null && model.getStudent().getStudentId()!=null){
			criteria.add(Restrictions.eq("student",model.getStudent()));
		}
		
		if(model!=null && model.getUser()!=null && model.getUser().getUserId()!=null){
			criteria.add(Restrictions.eq("user",model.getUser()));
		}
		
		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);
		
		criteria.addOrder(Order.desc("leaveId"));		
		return criteria.list();
	}

	public Long getTotalCount(Leave model,String schoolCode) {
		Criteria criteria  = getCurrentSession().createCriteria(Leave.class);
		criteria.createAlias("student", "student", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("student.school", "school", CriteriaSpecification.LEFT_JOIN);
		if("".equals(schoolCode)){
			criteria.add(Restrictions.like("school.schoolCode",schoolCode,MatchMode.START));
		}
		else {
			criteria.add(Restrictions.eq("school.schoolCode",schoolCode));
		}
		if(model!=null && model.getStudent()!=null && model.getStudent().getStudentId()!=null){
			criteria.add(Restrictions.eq("student",model.getStudent()));
		}
		if(model!=null && model.getUser()!=null && model.getUser().getUserId()!=null){
			criteria.add(Restrictions.eq("user",model.getUser()));
		}
		
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}

	@Override
	public Long getTotalCount(Leave model) {
		Criteria criteria  = getCurrentSession().createCriteria(Leave.class);
		if(model!=null && model.getStudent()!=null && model.getStudent().getStudentId()!=null){
			criteria.add(Restrictions.eq("student",model.getStudent()));
		}
		if(model!=null && model.getUser()!=null && model.getUser().getUserId()!=null){
			criteria.add(Restrictions.eq("user",model.getUser()));
		}
		
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Leave> query(Leave model, Integer page, Integer rows) {
		Criteria criteria  = getCurrentSession().createCriteria(Leave.class);
		if(model!=null && model.getStudent()!=null && model.getStudent().getStudentId()!=null){
			criteria.add(Restrictions.eq("student",model.getStudent()));
		}
		
		if(model!=null && model.getUser()!=null && model.getUser().getUserId()!=null){
			criteria.add(Restrictions.eq("user",model.getUser()));
		}
		
		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);
		
		criteria.addOrder(Order.desc("leaveId"));		
		return criteria.list();
	}
	
}
