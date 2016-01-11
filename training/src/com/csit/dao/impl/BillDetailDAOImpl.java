package com.csit.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.csit.dao.BillDetailDAO;
import com.csit.model.BillDetail;
import com.csit.util.PageUtil;
import com.csit.vo.GobelConstants;
/**
 * @Description:账单Dao实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-7
 * @author jcf
 * @vesion 1.0
 */
@Repository
public class BillDetailDAOImpl extends BaseDAOImpl<BillDetail, Integer> implements BillDetailDAO {

	@Override
	public Long getTotalCount(BillDetail model,Date beginDate,Date endDate) {
		Criteria criteria = getCurrentSession().createCriteria(BillDetail.class);

		criteria.createAlias("bill", "bill", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("bill.student", "student", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("course", "course", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("bill.school", "school", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("commodity", "commodity", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("warehouse", "warehouse", CriteriaSpecification.LEFT_JOIN);
		
		if(model!=null&&model.getBill()!=null&&model.getBill().getStudent()!=null&&model.getBill().getStudent().getStudentId()!=null){
			criteria.add(Restrictions.eq("student.studentId", model.getBill().getStudent().getStudentId()));
		}
		
		criteria.add(Restrictions.isNotNull("status"));
		
		if(model != null &&model.getProductType()!=null){
			criteria.add(Restrictions.eq("productType", model.getProductType()));
		}
		
		criteria.add(Restrictions.ne("productType", "收费项"));
		
		if(model.getStatus()!=null){
			criteria.add(Restrictions.eq("status", model.getStatus()));
		}
		
		if (beginDate!=null) {
			criteria.add(Restrictions.ge("bill.billDate",beginDate));
		}
		if (endDate!=null) {
			criteria.add(Restrictions.le("bill.billDate",endDate));
		}
		
		if(model!=null&&model.getCourse()!=null&&model.getCourse().getCourseId()!=null){
			criteria.add(Restrictions.le("course.courseId",model.getCourse().getCourseId()));
		}
		
		if(model!=null&&model.getBill()!=null&&model.getBill().getSchool()!=null&&model.getBill().getSchool().getSchoolId()!=null){
			criteria.add(Restrictions.eq("school.schoolId",model.getBill().getSchool().getSchoolId()));
		}
		
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BillDetail> query(BillDetail model,Date beginDate,Date endDate, Integer page, Integer rows) {
		Criteria criteria = getCurrentSession().createCriteria(BillDetail.class);

		criteria.createAlias("bill", "bill", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("bill.student", "student", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("bill.school", "school", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("course", "course", CriteriaSpecification.LEFT_JOIN);
		
		if(model!=null&&model.getBill()!=null&&model.getBill().getStudent()!=null&&model.getBill().getStudent().getStudentId()!=null){
			criteria.add(Restrictions.eq("student.studentId", model.getBill().getStudent().getStudentId()));
		}
		
		criteria.add(Restrictions.isNotNull("status"));
		
		if(model != null &&model.getProductType()!=null){
			criteria.add(Restrictions.eq("productType", model.getProductType()));
		}
		
		criteria.add(Restrictions.ne("productType", "收费项"));
		
		if(model.getStatus()!=null){
			criteria.add(Restrictions.eq("status", model.getStatus()));
		}
		
		if (beginDate!=null) {
			criteria.add(Restrictions.ge("bill.billDate",beginDate));
		}
		if (endDate!=null) {
			criteria.add(Restrictions.le("bill.billDate",endDate));
		}
		
		if(model!=null&&model.getCourse()!=null&&model.getCourse().getCourseId()!=null){
			criteria.add(Restrictions.eq("course.courseId",model.getCourse().getCourseId()));
		}
		
		if(model!=null&&model.getBill()!=null&&model.getBill().getSchool()!=null&&
				model.getBill().getSchool().getSchoolId()!=null){
			criteria.add(Restrictions.eq("school.schoolId",model.getBill().getSchool().getSchoolId()));
		}
		
		if (rows == null || rows < 0) {
			rows = GobelConstants.DEFAULTPAGESIZE;
		}

		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);

		criteria.addOrder(Order.desc("billDetailId"));
		return criteria.list();
	}

	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.StudentDAO#billInfoQuery(com.csit.model.Student, java.lang.Integer, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BillDetail> aboutStuClassQuery(BillDetail model, Integer page, Integer rows) {
		
		Criteria criteria = getCurrentSession().createCriteria(BillDetail.class);
		
		if(model.getStatus()!=null){
			criteria.add(Restrictions.eq("status", model.getStatus()));
		}
		
		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);

		criteria.addOrder(Order.desc("bill.billDate"));
		return criteria.list();
	}
}
