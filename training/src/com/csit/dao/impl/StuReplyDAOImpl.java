package com.csit.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.csit.dao.StuReplyDAO;
import com.csit.model.StuReply;
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
public class StuReplyDAOImpl extends BaseDAOImpl<StuReply, Integer> implements StuReplyDAO {

	@Override
	public Long getTotalCount(StuReply model) {
		Criteria criteria = getCurrentSession().createCriteria(StuReply.class);

		criteria.createAlias("user", "user", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("student", "student", CriteriaSpecification.LEFT_JOIN);
		if (model != null && model.getUser() != null && model.getUser().getUserId() != null) {
			criteria.add(Restrictions.eq("user.userId", model.getUser().getUserId()));
		}
		if (model != null && model.getStudent() != null && model.getStudent().getStudentId() != null) {
			criteria.add(Restrictions.eq("student.studentId", model.getStudent().getStudentId()));
		}
		if (model != null && model.getReplyDate() != null) {
			criteria.add(Restrictions.eq("replyDate", model.getReplyDate()));
		}
		if (model != null && model.getNextReplyDate() != null) {
			criteria.add(Restrictions.eq("nextReplyDate", model.getNextReplyDate()));
		}
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StuReply> query(StuReply model, Integer page, Integer rows) {
		Criteria criteria = getCurrentSession().createCriteria(StuReply.class);

		criteria.createAlias("user", "user", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("student", "student", CriteriaSpecification.LEFT_JOIN);
		if (model != null && model.getUser() != null && model.getUser().getUserId() != null) {
			criteria.add(Restrictions.eq("user.userId", model.getUser().getUserId()));
		}
		if (model != null && model.getStudent() != null && model.getStudent().getStudentId() != null) {
			criteria.add(Restrictions.eq("student.studentId", model.getStudent().getStudentId()));
		}
		if (model != null && model.getReplyDate() != null) {
			criteria.add(Restrictions.eq("replyDate", model.getReplyDate()));
		}
		if (model != null && model.getNextReplyDate() != null) {
			criteria.add(Restrictions.eq("nextReplyDate", model.getNextReplyDate()));
		}

		if (rows == null || rows < 0) {
			rows = GobelConstants.DEFAULTPAGESIZE;
		}

		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);

		criteria.addOrder(Order.asc("replyId"));
		return criteria.list();
	}

}
