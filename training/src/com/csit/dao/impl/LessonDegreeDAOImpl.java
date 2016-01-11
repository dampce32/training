package com.csit.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.csit.dao.LessonDegreeDAO;
import com.csit.model.LessonDegree;
import com.csit.util.PageUtil;
import com.csit.vo.GobelConstants;
/**
 * 
 * @Description: 排课表DAO实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-7
 * @author yk
 * @vesion 1.0
 */
@Repository
public class LessonDegreeDAOImpl extends BaseDAOImpl<LessonDegree, Integer>
		implements LessonDegreeDAO {
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.LessonDegreeDAO#query(com.csit.model.LessonDegree, java.lang.Integer, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LessonDegree> query(LessonDegree model, Integer page, Integer rows) {
		
		Criteria criteria  = getCurrentSession().createCriteria(LessonDegree.class);
				
		criteria.createAlias("clazz", "clazz", CriteriaSpecification.LEFT_JOIN);
		
		if(model!=null&&model.getClazz()!=null&&model.getClazz().getClassId()!=null){
			criteria.add(Restrictions.eq("clazz.classId", model.getClazz().getClassId()));
		}
		
		if(model!=null&&model.getClazz()!=null&&model.getClazz().getSchool()!=null&&model.getClazz().getSchool().getSchoolId()!=null){
			criteria.add(Restrictions.eq("clazz.school.schoolId", model.getClazz().getSchool().getSchoolId()));
		}
		
		if(model!=null&&model.getLessonDegreeDate()!=null){
			criteria.add(Restrictions.eq("lessonDegreeDate", model.getLessonDegreeDate()));
		}
		
		if(rows==null||rows<0){
			rows = GobelConstants.DEFAULTPAGESIZE;
		}
		
		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);
		
		criteria.addOrder(Order.asc("lessonDegreeDate"));		
		return criteria.list();
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.LessonDegreeDAO#getTotalCount(com.csit.model.LessonDegree)
	 */
	@Override
	public Long getTotalCount(LessonDegree model) {
		
		Criteria criteria  = getCurrentSession().createCriteria(LessonDegree.class);
		
		criteria.createAlias("clazz", "clazz", CriteriaSpecification.LEFT_JOIN);
		
		if(model!=null&&model.getClazz()!=null&&model.getClazz().getClassId()!=null){
			criteria.add(Restrictions.eq("clazz.classId", model.getClazz().getClassId()));
		}
		
		if(model!=null&&model.getClazz()!=null&&model.getClazz().getSchool()!=null&&model.getClazz().getSchool().getSchoolId()!=null){
			criteria.add(Restrictions.eq("clazz.school.schoolId", model.getClazz().getSchool().getSchoolId()));
		}
		
		if(model!=null&&model.getLessonDegreeDate()!=null){
			criteria.add(Restrictions.eq("lessonDegreeDate", model.getLessonDegreeDate()));
		}
		
		criteria.setProjection(Projections.rowCount());
		return new Long(criteria.uniqueResult().toString());
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.LessonDegreeDAO#weekTable(com.csit.model.LessonDegree)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LessonDegree> weekTableQuery(Date weekStart,Date weekEnd,LessonDegree model,Integer schoolId,Integer queryParam) {
		Criteria criteria  = getCurrentSession().createCriteria(LessonDegree.class);
		
		criteria.createAlias("classroom", "classroom", CriteriaSpecification.LEFT_JOIN);
		criteria.createAlias("teacher", "teacher", CriteriaSpecification.LEFT_JOIN);
		
		if(model!=null && model.getTeacher()!=null && model.getTeacher().getUserId()!=null){
			criteria.add(Restrictions.eq("teacher",model.getTeacher()));
		}
		if(schoolId!=null){
			criteria.add(Restrictions.eq("classroom.school.schoolId",schoolId));
		}
		
		criteria.add(Restrictions.ge("lessonDegreeDate",weekStart));
		criteria.add(Restrictions.le("lessonDegreeDate",weekEnd));
		
		if(queryParam==0){
			criteria.addOrder(Order.asc("classroom.classroomName"));
		}else if(queryParam==1){
			criteria.addOrder(Order.asc("teacher.userName"));
		}
				
		return criteria.list();
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.LessonDegreeDAO#getMaxLessonDegreeDate(java.lang.Integer)
	 */
	@Override
	public Date getMaxLessonDegreeDate(Integer classId) {
		Criteria criteria  = getCurrentSession().createCriteria(LessonDegree.class);
		
		criteria.add(Restrictions.eq("clazz.classId", classId));
		
		Date maxDate = (Date) criteria.setProjection(Projections.max("lessonDegreeDate")).uniqueResult();
		
		return maxDate;
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.LessonDegreeDAO#getLessonDegreeByStu(java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getLessonDegreeByStu(Integer stuId, Integer page, Integer rows) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT ld.lessonStatus '状态',ld.subject '课题',ld.lessonDegreeDate '上课日期',ld.startTime '上课时间',ld.endTime '下课时间',c.classId '班号',c.className '班级名称',cr.classroomName '教室',teacher.userName '讲师',ld.lessons '课时',ld.note '家庭作业',a.note '课堂表现' FROM t_lessondegree ld");
		sql.append(" LEFT JOIN t_class c ON ld.classId=c.classId LEFT JOIN t_classroom cr ON cr.classroomId=ld.classroomId LEFT JOIN t_user teacher ON teacher.userId=ld.teacherId LEFT JOIN t_stuclass sc ON sc.classId=ld.classId LEFT JOIN t_attend a ON a.lessonDegreeId=ld.lessonDegreeId");
		sql.append(" where ld.lessonStatus=1 and sc.studentId=:studentId");
		sql.append(" limit :begin,:end");
		
		return getCurrentSession().createSQLQuery(sql.toString()).setInteger("studentId", stuId).setInteger("begin", (page-1)*rows).setInteger("end", page*rows).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
	}
	
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.LessonDegreeDAO#getTotalCountByStu(java.lang.Integer)
	 */
	@Override
	public Integer getTotalCountByStu(Integer stuId) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT ld.lessonDegreeId FROM t_lessondegree ld");
		sql.append(" LEFT JOIN t_stuclass sc ON sc.classId=ld.classId");
		sql.append(" where ld.lessonStatus=1 and sc.studentId=:studentId");
		
		return getCurrentSession().createSQLQuery(sql.toString()).setInteger("studentId", stuId).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list().size();
	}

}
