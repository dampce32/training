package com.csit.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.csit.dao.AttendDAO;
import com.csit.model.Attend;
import com.csit.util.PageUtil;
import com.csit.vo.GobelConstants;
/**
 * 
 * @Description: 学员考勤表DAO实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-4-3
 * @author yk
 * @vesion 1.0
 */
@Repository
public class AttendDAOImpl extends BaseDAOImpl<Attend, Integer> implements
		AttendDAO {
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.impl.AttendDAO#query(com.csit.model.Attend, java.lang.Integer, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Attend> query(Attend model, Integer page, Integer rows) {
		Criteria criteria  = getCurrentSession().createCriteria(Attend.class);
		
		if(model.getLessonDegree()!=null&&model.getLessonDegree().getLessonDegreeId()!=null){
			criteria.add(Restrictions.eq("lessonDegree.lessonDegreeId",model.getLessonDegree().getLessonDegreeId()));
		}
		
		if(rows==null||rows<0){
			rows = GobelConstants.DEFAULTPAGESIZE;
		}
		
		criteria.setFirstResult(PageUtil.getPageBegin(page, rows));
		criteria.setMaxResults(rows);
		
		criteria.addOrder(Order.asc("attendId"));		
		return criteria.list();
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.impl.AttendDAO#getTotalCount(com.csit.model.Attend)
	 */
	@Override
	public Long getTotalCount(Attend model) {
		Criteria criteria  = getCurrentSession().createCriteria(Attend.class);
		
		if(model.getLessonDegree()!=null&&model.getLessonDegree().getLessonDegreeId()!=null){
			criteria.add(Restrictions.eq("lessonDegree.lessonDegreeId",model.getLessonDegree().getLessonDegreeId()));
		}
		
		criteria.setProjection(Projections.rowCount());
		
		return new Long(criteria.uniqueResult().toString());
	}

	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.AttendDAO#todayAttendQuery(java.lang.Integer, java.lang.Integer, java.util.Date, java.lang.Integer, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> todayAttendQuery(Integer studentId,Integer schoolId,Date lessonDegreeDate,Integer page,Integer rows){
		StringBuilder sql = new StringBuilder();
		sql.append(
				"SELECT sc.studentId studentId,s.studentName studentName,c.classId classId,c.className className," +
					"sc.scStatus scStatus,sc.courseProgress courseProgress,sc.lessons scLessons," +
					"l.lessonDegreeDate lessonDegreeDate,l.startTime startTime,l.endTime endTime," +
					"a.status status,a.lessons lessons,a.note note," + 
					"l.lessonDegreeId lessonDegreeId,a.attendId attendId,l.lessonStatus lessonStatus " +
				"FROM (t_stuclass sc,t_lessondegree l) " +
				"LEFT JOIN t_student s on sc.studentId = s.studentId " +
				"LEFT JOIN t_class c on sc.classId = c.classId " +
				"LEFT JOIN t_attend a on l.lessonDegreeId = a.lessonDegreeId " +
				"WHERE sc.classId = l.classId "
				);
		
		if(studentId!=null){
			sql.append("and s.studentId = :studentId ");
		}
		
		if(schoolId!=null){
			sql.append("and c.schoolId = :schoolId ");
		}
		
		sql.append("and l.lessonDegreeDate = :lessonDegreeDate ");
		
		
		sql.append("limit :page,:rows");
		
		Query query = getCurrentSession().createSQLQuery(sql.toString());
		
		if(studentId!=null){
			query.setInteger("studentId", studentId);
		}
		
		if(schoolId!=null){
			query.setInteger("schoolId", schoolId);
		}
		
		if(lessonDegreeDate!=null){
			query.setDate("lessonDegreeDate", lessonDegreeDate);
		}else{
			query.setDate("lessonDegreeDate", new Date());
		}
		
		query.setInteger("page", (page-1)*rows);
		query.setInteger("rows", page*rows);
		
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		
		return query.list();
	}
	/*
	 * (non-Javadoc)   
	 * @see com.csit.dao.AttendDAO#getTotalCount(java.lang.Integer, java.lang.Integer, java.util.Date)
	 */
	@Override
	public Integer todayAttendTotal(Integer studentId, Integer schoolId,Date lessonDegreeDate) {
		StringBuilder sql = new StringBuilder();
		sql.append(
				"SELECT sc.studentId " + 
				"FROM (t_stuclass sc,t_lessondegree l) " +
				"LEFT JOIN t_student s on sc.studentId = s.studentId " +
				"LEFT JOIN t_class c on sc.classId = c.classId " +
				"LEFT JOIN t_attend a on l.lessonDegreeId = a.lessonDegreeId " +
				"WHERE sc.classId = l.classId "
				);
		
		if(studentId!=null){
			sql.append("and s.studentId = :studentId ");
		}
		
		if(schoolId!=null){
			sql.append("and c.schoolId = :schoolId ");
		}
		
		sql.append("and l.lessonDegreeDate = :lessonDegreeDate ");
		
		
		Query query = getCurrentSession().createSQLQuery(sql.toString());
		
		if(studentId!=null){
			query.setInteger("studentId", studentId);
		}
		
		if(schoolId!=null){
			query.setInteger("schoolId", schoolId);
		}
		
		if(lessonDegreeDate!=null){
			query.setDate("lessonDegreeDate", lessonDegreeDate);
		}else{
			query.setDate("lessonDegreeDate", new Date());
		}
		
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		
		return query.list().size();
	}
	
}
