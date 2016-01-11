package com.csit.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.csit.dao.ChangeDao;
import com.csit.model.Change;
import com.csit.util.PageUtil;
/**
 * @Description:异动分类DAO实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-26
 * @Author cjp
 */
@Repository
public class ChangeDAOImpl extends BaseDAOImpl<Change, Integer>
		implements ChangeDao {
	
	@SuppressWarnings("unchecked")
	public List<Change> query(Change model, Integer page, Integer rows) {
		Criteria criteria  = getCurrentSession().createCriteria(Change.class);
		criteria.createAlias("student", "student", CriteriaSpecification.LEFT_JOIN);
		if(model!=null && model.getStudent()!=null && model.getStudent().getStudentId()!=null){
			criteria.add(Restrictions.eq("student",model.getStudent()));
		}
		if(model!=null && model.getStudent()!=null &&model.getStudent().getSchool()!=null&& model.getStudent().getSchool().getSchoolId()!=null){
			criteria.add(Restrictions.eq("student.school",model.getStudent().getSchool()));
		}
		if(model!=null && model.getChangeType()!=null){
			criteria.add(Restrictions.eq("changeType",model.getChangeType()));
		}
		
		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);
		
		criteria.addOrder(Order.desc("changeId"));		
		return criteria.list();
	}

	public Long getTotalCount(Change model) {
		Criteria criteria  = getCurrentSession().createCriteria(Change.class);
		criteria.createAlias("student", "student", CriteriaSpecification.LEFT_JOIN);
		if(model!=null && model.getStudent()!=null && model.getStudent().getStudentId()!=null){
			criteria.add(Restrictions.eq("student",model.getStudent()));
		}
		if(model!=null && model.getStudent()!=null &&model.getStudent().getSchool()!=null&& model.getStudent().getSchool().getSchoolId()!=null){
			criteria.add(Restrictions.eq("student.school",model.getStudent().getSchool()));
		}
		if(model!=null && model.getChangeType()!=null){
			criteria.add(Restrictions.eq("changeType",model.getChangeType()));
		}
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}
	
}
