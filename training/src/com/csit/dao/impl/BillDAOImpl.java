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

import com.csit.dao.BillDAO;
import com.csit.model.Bill;
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
public class BillDAOImpl extends BaseDAOImpl<Bill, Integer> implements BillDAO {

	@Override
	public Long getTotalCount(Bill model,Date beginDate,Date endDate) {
		Criteria criteria = getCurrentSession().createCriteria(Bill.class);

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
		if(model != null && model.getBillType()!=null){
			criteria.add(Restrictions.eq("billType", model.getBillType()));
		}
		if (beginDate!=null) {
			criteria.add(Restrictions.ge("billDate",beginDate));
		}
		if (endDate!=null) {
			criteria.add(Restrictions.le("billDate",endDate));
		}
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Bill> query(Bill model,Date beginDate,Date endDate, Integer page, Integer rows) {
		Criteria criteria = getCurrentSession().createCriteria(Bill.class);

		criteria.createAlias("student", "student", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("user", "user", CriteriaSpecification.LEFT_JOIN);
		if (model != null && model.getStudent()!=null&&model.getStudent().getStudentId()!=null) {
			criteria.add(Restrictions.eq("student.studentId", model.getStudent().getStudentId()));
		}
		if(model != null && model.getBillType()!=null){
			criteria.add(Restrictions.eq("billType", model.getBillType()));
		}
		if(model!=null&&model.getSchool()!=null){
			if(model.getSchool().getSchoolId()==-1){//用户默认没有选择校区
				criteria.createAlias("school", "school", CriteriaSpecification.LEFT_JOIN);
				criteria.add(Restrictions.like("school.schoolCode",model.getSchool().getSchoolCode(),MatchMode.START));
			}else{
				criteria.add(Restrictions.eq("school",model.getSchool()));
			}
		}
		if (beginDate!=null) {
			criteria.add(Restrictions.ge("billDate",beginDate));
		}
		if (endDate!=null) {
			criteria.add(Restrictions.le("billDate",endDate));
		}
		if (rows == null || rows < 0) {
			rows = GobelConstants.DEFAULTPAGESIZE;
		}

		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);

		criteria.addOrder(Order.desc("billCode"));
		return criteria.list();
	}

	@Override
	public Long initBillCode(String date) {
		Criteria criteria  = getCurrentSession().createCriteria(Bill.class);
		
		criteria.add(Restrictions.like("billCode", date,MatchMode.START));

		Long billCode= 0l;
			
		if(criteria.list().size()==0){
			billCode=Long.parseLong(date+"001");
		}else{
			billCode =Long.parseLong((String) criteria.setProjection(Projections.max("billCode")).uniqueResult());
			billCode++;
		}
		return billCode;
	}

}
