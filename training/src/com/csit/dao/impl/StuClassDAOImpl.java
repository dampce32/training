package com.csit.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.csit.dao.StuClassDAO;
import com.csit.model.StuClass;
import com.csit.util.PageUtil;
import com.csit.vo.GobelConstants;
/**
 * 
 * @Description: 选班表DAO实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-14
 * @author yk
 * @vesion 1.0
 */
@Repository
public class StuClassDAOImpl extends BaseDAOImpl<StuClass, Integer> implements
		StuClassDAO {
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.StuClassDAO#query(com.csit.model.StuClass, java.lang.Integer, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<StuClass> query(StuClass model, Integer page, Integer rows) {
		Criteria criteria  = getCurrentSession().createCriteria(StuClass.class);
		
		if(model!=null&&model.getClazz()!=null&&model.getClazz().getClassId()!=null){
			criteria.add(Restrictions.eq("clazz.classId",model.getClazz().getClassId()));
		}
		
		if(rows==null||rows<0){
			rows = GobelConstants.DEFAULTPAGESIZE;
		}
		
		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);
		
		criteria.addOrder(Order.asc("selectDate"));		
		return criteria.list();
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.StuClassDAO#getTotalCount(com.csit.model.StuClass)
	 */
	@Override
	public Long getTotalCount(StuClass model) {
		Criteria criteria  = getCurrentSession().createCriteria(StuClass.class);
		
		if(model!=null&&model.getClazz()!=null&&model.getClazz().getClassId()!=null){
			criteria.add(Restrictions.eq("clazz.classId",model.getClazz().getClassId()));
		}
		
		if(model!=null&&model.getStudent()!=null&&model.getStudent().getStudentId()!=null){
			criteria.add(Restrictions.eq("student.studentId", model.getStudent().getStudentId()));
		}
		
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.StuClassDAO#query(com.csit.model.StuClass)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<StuClass> query(StuClass model,Date lessonDegreeDate) {
		Criteria criteria  = getCurrentSession().createCriteria(StuClass.class);
		
		if(model!=null&&model.getClazz()!=null&&model.getClazz().getClassId()!=null){
			criteria.add(Restrictions.eq("clazz.classId",model.getClazz().getClassId()));
		}
		
		if(lessonDegreeDate!=null){
			criteria.add(Restrictions.le("selectDate",lessonDegreeDate));
			criteria.add(Restrictions.ltProperty("courseProgress","lessons"));
		}
		
		if(model!=null&&model.getStudent()!=null&&model.getStudent().getStudentId()!=null){
			criteria.add(Restrictions.eq("student.studentId",model.getStudent().getStudentId()));
		}
		
		criteria.addOrder(Order.desc("selectDate"));		
		return criteria.list();
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.StuClassDAO#querySelectedClassCombobox(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<StuClass> querySelectedClassCombobox(Integer courseId,Integer studentId,Integer scStatus) {
		Criteria criteria = getCurrentSession().createCriteria(StuClass.class);
		
		criteria.createAlias("clazz", "clazz", CriteriaSpecification.LEFT_JOIN);
		
		if(studentId!=null){
			criteria.add(Restrictions.eq("student.studentId",studentId));
		}
		
		if(courseId!=null){
			criteria.createAlias("clazz.course", "course", CriteriaSpecification.LEFT_JOIN);
			criteria.add(Restrictions.eq("course.courseId",courseId));
		}
		
		criteria.add(Restrictions.eq("student.studentId",studentId));
		
		if(scStatus==1){
			criteria.add(Restrictions.eq("scStatus",scStatus));
		}else if(scStatus==3){
			criteria.add(Restrictions.lt("scStatus", 3));
		}
		
		criteria.addOrder(Order.asc("selectDate"));
		return criteria.list();
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.StuClassDAO#getScStatus(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public StuClass getStuClass(Integer studentId,Integer classId,Integer param,Date maxSelectDate) {
		
		Criteria criteria = getCurrentSession().createCriteria(StuClass.class);
		
		criteria.add(Restrictions.eq("student.studentId",studentId));
		
		if(classId!=null){
			criteria.add(Restrictions.eq("clazz.classId",classId));
		}
		
		//查询最新选班
		if(param==1){
			criteria.add(Restrictions.eq("selectDate",maxSelectDate));
		}
		
		return (StuClass) criteria.uniqueResult();
	}
	
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.StuClassDAO#getMax(java.lang.Integer)
	 */
	@Override
	public Date getMax(Integer studentId) {
		
		Criteria criteria = getCurrentSession().createCriteria(StuClass.class);
		
		criteria.add(Restrictions.eq("student.studentId",studentId));
		
		Date maxDate = (Date) criteria.setProjection(Projections.max("selectDate")).uniqueResult();
		
		return maxDate;
	}
}
