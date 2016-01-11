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

import com.csit.dao.PaymentDAO;
import com.csit.model.Payment;
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
public class PaymentDAOImpl extends BaseDAOImpl<Payment, Integer> implements PaymentDAO {

	@Override
	public Long getTotalCount(Payment model,Date beginDate,Date endDate) {
		Criteria criteria = getCurrentSession().createCriteria(Payment.class);

		criteria.createAlias("student", "student", CriteriaSpecification.LEFT_JOIN);
		if (model != null && model.getStudent()!=null&&model.getStudent().getStudentId()!=null) {
			criteria.add(Restrictions.eq("student.studentId", model.getStudent().getStudentId()));
		}
		if(model!=null&&model.getSchool()!=null){
			if(model.getSchool().getSchoolId()==-1){//用户默认没有选择校区
				criteria.createAlias("school", "school", CriteriaSpecification.LEFT_JOIN);
				criteria.add(Restrictions.like("school.schoolCode",model.getSchool().getSchoolCode(),MatchMode.START));
			}else{
				criteria.add(Restrictions.eq("school",model.getSchool()));
			}
		}
		if (model != null && model.getPaymentType()!=null) {
			criteria.add(Restrictions.eq("paymentType", model.getPaymentType()));
		}
		if (beginDate!=null) {
			criteria.add(Restrictions.ge("transactionDate",beginDate));
		}
		if (endDate!=null) {
			criteria.add(Restrictions.le("transactionDate",endDate));
		}
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Payment> query(Payment model,Date beginDate,Date endDate, Integer page, Integer rows) {
		Criteria criteria = getCurrentSession().createCriteria(Payment.class);

		criteria.createAlias("student", "student", CriteriaSpecification.LEFT_JOIN);
		if (model != null && model.getStudent()!=null&&model.getStudent().getStudentId()!=null) {
			criteria.add(Restrictions.eq("student.studentId", model.getStudent().getStudentId()));
		}
		if(model!=null&&model.getSchool()!=null){
			if(model.getSchool().getSchoolId()==-1){//用户默认没有选择校区
				criteria.createAlias("school", "school", CriteriaSpecification.LEFT_JOIN);
				criteria.add(Restrictions.like("school.schoolCode",model.getSchool().getSchoolCode(),MatchMode.START));
			}else{
				criteria.add(Restrictions.eq("school",model.getSchool()));
			}
		}
		if (model != null && model.getPaymentType()!=null) {
			criteria.add(Restrictions.eq("paymentType", model.getPaymentType()));
		}
		if (beginDate!=null) {
			criteria.add(Restrictions.ge("transactionDate",beginDate));
		}
		if (endDate!=null) {
			criteria.add(Restrictions.le("transactionDate",endDate));
		}
		if (rows == null || rows < 0) {
			rows = GobelConstants.DEFAULTPAGESIZE;
		}

		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);

		criteria.addOrder(Order.desc("paymentId"));
		return criteria.list();
	}

	@Override
	public Double sum(Payment model, Date beginDate, Date endDate,Integer type) {
		Criteria criteria = getCurrentSession().createCriteria(Payment.class);
		Double count=0d;
		if(model!=null&&model.getSchool()!=null){
			if(model.getSchool().getSchoolId()==-1){//用户默认没有选择校区
				criteria.createAlias("school", "school", CriteriaSpecification.LEFT_JOIN);
				criteria.add(Restrictions.like("school.schoolCode",model.getSchool().getSchoolCode(),MatchMode.START));
			}else{
				criteria.add(Restrictions.eq("school",model.getSchool()));
			}
		}
		if (beginDate!=null) {
			criteria.add(Restrictions.ge("transactionDate",beginDate));
		}
		if (endDate!=null) {
			criteria.add(Restrictions.le("transactionDate",endDate));
		}
		if(type==1){
			Object[] values={1,2};
			criteria.add(Restrictions.in("paymentType", values));
		}
		if(type==2){
			Object[] values={5,6};
			criteria.add(Restrictions.in("paymentType", values));
		}
		count=(Double) criteria.setProjection(Projections.sum("payMoney")).uniqueResult();
		return count;
	}

}
