package com.csit.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/***
 * 
 * @Description:媒体
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-2-27
 * @author cjp
 * @vesion 1.0
 */
@Entity
@Table(name = "T_Media")
public class Media extends BaseModel {

	private static final long serialVersionUID = 2006328638738250106L;

	/**
	 * 媒体Id
	 */
	private Integer mediaId;
	/**
	 * 媒体名称
	 */
	private String mediaName;
	/**
	 * 状态
	 * 0--禁用
	 * 1--启用
	 */
	private Integer status;
	/**
	 * 学生
	 */
	private Set<Student> students = new HashSet<Student>(0);
	/**
	 * 咨询信息单
	 */
	private Set<Potential> potentials = new HashSet<Potential>(0);

	public Media() {
	}

	public Media(String mediaName, Integer status) {
		this.mediaName = mediaName;
		this.status = status;
	}

	public Media(String mediaName, Integer status, Set<Student> students,
			Set<Potential> potentials) {
		this.mediaName = mediaName;
		this.status = status;
		this.students = students;
		this.potentials = potentials;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "mediaId", unique = true, nullable = false)
	public Integer getMediaId() {
		return this.mediaId;
	}

	public void setMediaId(Integer mediaId) {
		this.mediaId = mediaId;
	}

	@Column(name = "mediaName", nullable = false, length = 50)
	public String getMediaName() {
		return this.mediaName;
	}

	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}

	@Column(name = "status", nullable = false)
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "media")
	public Set<Student> getStudents() {
		return this.students;
	}

	public void setStudents(Set<Student> students) {
		this.students = students;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "media")
	public Set<Potential> getPotentials() {
		return this.potentials;
	}

	public void setPotentials(Set<Potential> potentials) {
		this.potentials = potentials;
	}
}