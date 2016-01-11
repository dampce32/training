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

import com.csit.dao.StudentDAO;
import com.csit.model.Student;
import com.csit.util.PageUtil;
import com.csit.vo.GobelConstants;
/**
 * 
 * @Description:学员Dao实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-5
 * @author jcf
 * @vesion 1.0
 */
@Repository
public class StudentDAOImpl extends BaseDAOImpl<Student, Integer> implements StudentDAO {

	@Override
	public Long getTotalCount(Student model) {
		Criteria criteria = getCurrentSession().createCriteria(Student.class);

		if(model!=null&&model.getUser()!=null&&model.getUser().getUserId()!=null){
			criteria.add(Restrictions.eq("user.userId", model.getUser().getUserId()));
		}
		if(model!=null&&model.getSchool()!=null){
			if(model.getSchool().getSchoolId()==-1){//用户默认没有选择校区
				criteria.createAlias("school", "school", CriteriaSpecification.LEFT_JOIN);
				criteria.add(Restrictions.like("school.schoolCode",model.getSchool().getSchoolCode(),MatchMode.START));
			}else{
				criteria.add(Restrictions.eq("school",model.getSchool()));
			}
		}
		if(model!=null&&model.getArrearageMoney()!=null){
			criteria.add(Restrictions.gt("arrearageMoney", model.getArrearageMoney()));
		}
		if (model != null && StringUtils.isNotEmpty(model.getStudentName())) {
			criteria.add(Restrictions.like("studentName", model.getStudentName(), MatchMode.ANYWHERE));
		}
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Student> query(Student model, Integer page, Integer rows) {
		Criteria criteria = getCurrentSession().createCriteria(Student.class);

		criteria.createAlias("media", "media", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("user", "user", CriteriaSpecification.LEFT_JOIN);
		if(model!=null&&model.getUser()!=null&&model.getUser().getUserId()!=null){
			criteria.add(Restrictions.eq("user.userId", model.getUser().getUserId()));
		}
		if(model!=null&&model.getSchool()!=null){
			if(model.getSchool().getSchoolId()==-1){//用户默认没有选择校区
				criteria.createAlias("school", "school", CriteriaSpecification.LEFT_JOIN);
				criteria.add(Restrictions.like("school.schoolCode",model.getSchool().getSchoolCode(),MatchMode.START));
			}else{
				criteria.add(Restrictions.eq("school",model.getSchool()));
			}
		}
		if(model!=null&&model.getArrearageMoney()!=null){
			criteria.add(Restrictions.gt("arrearageMoney", model.getArrearageMoney()));
		}
		if (model != null && StringUtils.isNotEmpty(model.getStudentName())) {
			criteria.add(Restrictions.like("studentName", model.getStudentName(), MatchMode.ANYWHERE));
		}

		if (rows == null || rows < 0) {
			rows = GobelConstants.DEFAULTPAGESIZE;
		}

		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);

		criteria.addOrder(Order.asc("studentId"));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Student> queryCombobox(String schoolCode) {
		Criteria criteria = getCurrentSession().createCriteria(Student.class);
		criteria.createAlias("school", "school", CriteriaSpecification.LEFT_JOIN);
		
		criteria.add(Restrictions.like("school.schoolCode", schoolCode, MatchMode.START));
		criteria.addOrder(Order.asc("studentId"));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Student> queryCombobox(Student model) {
		Criteria criteria = getCurrentSession().createCriteria(Student.class);
		if(model!=null&&model.getUser()!=null&&model.getUser().getUserId()!=null){
			criteria.add(Restrictions.eq("user", model.getUser()));
		}
		criteria.addOrder(Order.asc("studentId"));
		return criteria.list();
	}
}
