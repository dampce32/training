package com.csit.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.csit.dao.ChangeDao;
import com.csit.dao.ClassDAO;
import com.csit.dao.PaymentDAO;
import com.csit.dao.StuClassDAO;
import com.csit.dao.StudentDAO;
import com.csit.dao.UserDAO;
import com.csit.model.Change;
import com.csit.model.Clazz;
import com.csit.model.Payment;
import com.csit.model.StuClass;
import com.csit.model.Student;
import com.csit.model.User;
import com.csit.service.ChangeService;
import com.csit.util.JSONUtil;
import com.csit.vo.ServiceResult;

@Service
public class ChangeServiceImpl implements ChangeService {
	@Resource
	private ChangeDao changeDao;
	@Resource
	private UserDAO userDao;
	@Resource
	private StuClassDAO stuClassDao;
	@Resource
	private PaymentDAO paymentDao;
	@Resource
	private StudentDAO studentDao;
	@Resource
	private ClassDAO classDao;
	
	
	public ServiceResult save(Change model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null){
			result.setMessage("请填写异动信息");
			return result;
		}
		if(model.getStudent()==null){
			result.setMessage("请选择异动学员");
			return result;
		}
		Student student=studentDao.get(model.getStudent().getStudentId());
		if( student==null){
			result.setMessage("请选择已有的学员");
			return result;
		}
		
		if(model.getChangeId()==null){
			if(model.getStuClass()==null){
				result.setMessage("请选择学员所在班级");
				return result;
			}
			StuClass stuClass=stuClassDao.get(model.getStuClass().getStuClassId());
			if( stuClass==null){
				result.setMessage("请选择已有的学院班级");
				return result;
			}
		}
		
		if(model.getChangeType()==null){
			result.setMessage("请选择异动类型");
			return result;
		}
		if(model.getIntoAccount()==null){
			result.setMessage("请填写退还金额");
			return result;
		}
		if(model.getDate()==null){
			result.setMessage("请选择办理日期");
			return result;
		}
		if(model.getChangeType().equals(3)&&model.getChangeId()==null){
			if(model.getNewStuClass()==null){
				result.setMessage("请选择学员新班级");
				return result;
			}
			Clazz newClass=classDao.get(model.getNewStuClass().getClazz().getClassId());
			if(newClass==null){
				result.setMessage("请选择学员新班级");
				return result;
			}
		}
		if(model.getChangeType().equals(4)){
			if(model.getExpireDate()==null){
				result.setMessage("请选择休学到期日期");
				return result;
			}
		}
		if(model.getUser()==null){
			result.setMessage("请选择经办人");
			return result;
		}
		User user=userDao.get(model.getUser().getUserId());
		if( user==null){
			result.setMessage("请选择已有的经办人");
			return result;
		}
		if(model.getChangeId()==null){//新增

			student.setAvailableMoney(student.getAvailableMoney()+ model.getIntoAccount());
			
			Payment intoPayment=new Payment();
			intoPayment.setSchool(student.getSchool());
			intoPayment.setUser(user);
			intoPayment.setStudent(student);
			intoPayment.setPaymentType(2);
			intoPayment.setPayMoney(model.getIntoAccount());
			intoPayment.setTransactionDate(model.getDate());
			paymentDao.save(intoPayment);
			
			model.setPayment(intoPayment);
			
			StuClass stuClass=stuClassDao.get(model.getStuClass().getStuClassId());
			
			stuClass.setScStatus(model.getChangeType());
			Clazz clazz=stuClass.getClazz();
			
			clazz.setStuCount(clazz.getStuCount()-1);
			
			if(model.getChangeType().equals(3)){
				Clazz newClass=classDao.get(model.getNewStuClass().getClazz().getClassId());
				newClass.setStuCount(newClass.getStuCount()+1);
				
				StuClass newStuClass=new StuClass();
				stuClass.setStudent(student);
				newStuClass.setClazz(newClass);
				newStuClass.setLessonSchool(student.getSchool());
				newStuClass.setUser(user);
				newStuClass.setStudent(student);
				newStuClass.setSelectSchool(student.getSchool());
				newStuClass.setLessons(newClass.getLessons()-newClass.getCourseProgress());
				newStuClass.setSelectDate(model.getDate());
				newStuClass.setCourseProgress(0);
				newStuClass.setScStatus(2);
				newStuClass.setContinueReg(1);
				
				stuClassDao.save(newStuClass);
				
				model.setNewStuClass(newStuClass);
				
				Payment outPayment=new Payment();
				outPayment.setSchool(student.getSchool());
				outPayment.setUser(user);
				outPayment.setStudent(student);
				outPayment.setPaymentType(1);
				outPayment.setPayMoney((newClass.getLessons()-newClass.getCourseProgress())*newClass.getCourse().getUnitPrice());
				outPayment.setTransactionDate(model.getDate());
				paymentDao.save(outPayment);
				
				student.setAvailableMoney(student.getAvailableMoney()+ ((newClass.getLessons()-newClass.getCourseProgress())*newClass.getCourse().getUnitPrice()));
				model.setPayment(outPayment);
			}
			
			changeDao.save(model);
		}else{
			Change oldModel = changeDao.load(model.getChangeId());
			if(oldModel==null){
				result.setMessage("该异动已不存在");
				return result;
			}
			Payment payment=oldModel.getPayment();
			if(oldModel.getChangeType().equals(3) && !model.getChangeType().equals(3)){
				StuClass newStuClass=oldModel.getNewStuClass();
				Clazz newClazz=newStuClass.getClazz();
				
				newClazz.setStuCount(newClazz.getStuCount()-1);
				oldModel.setNewStuClass(null);
				stuClassDao.delete(newStuClass);
				
				Clazz clazz=oldModel.getStuClass().getClazz();
				clazz.setStuCount(clazz.getStuCount()+1);
				
				payment.setPaymentType(2);
			}else if(oldModel.getChangeType().equals(3) && model.getChangeType().equals(3)){
				StuClass newStuClass=oldModel.getNewStuClass();
				
				Clazz oldClass=newStuClass.getClazz();
				oldClass.setStuCount(oldClass.getStuCount()-1);
				
				Clazz newClass=classDao.get(model.getNewStuClass().getClazz().getClassId());
				newClass.setStuCount(newClass.getStuCount()+1);
				
				newStuClass.setClazz(newClass);
				oldModel.setNewStuClass(newStuClass);
			}
			
			if(!oldModel.getChangeType().equals(3) && model.getChangeType().equals(3)){
				Clazz newClass=classDao.get(model.getNewStuClass().getClazz().getClassId());
				newClass.setStuCount(newClass.getStuCount()+1);
				
				StuClass newStuClass=new StuClass();
				newStuClass.setClazz(newClass);
				newStuClass.setLessonSchool(student.getSchool());
				newStuClass.setUser(user);
				newStuClass.setStudent(student);
				newStuClass.setSelectSchool(student.getSchool());
				newStuClass.setLessons(newClass.getLessons()-newClass.getCourseProgress());
				newStuClass.setSelectDate(model.getDate());
				newStuClass.setCourseProgress(0);
				newStuClass.setScStatus(2);
				newStuClass.setContinueReg(1);
				
				stuClassDao.save(newStuClass);
				
				payment.setPaymentType(1);
				oldModel.setNewStuClass(newStuClass);
			}
			
			payment.setPayMoney(oldModel.getIntoAccount());
			
			oldModel.setUser(model.getUser());
			oldModel.setPayment(payment);
			oldModel.setChangeType(model.getChangeType());
			oldModel.setDate(model.getDate());
			oldModel.setIntoAccount(model.getIntoAccount());
			if(model.getExpireDate()!=null){
				oldModel.setExpireDate(model.getExpireDate());
			}
			oldModel.setNote(model.getNote());
			
		}
		result.setIsSuccess(true);
		result.addData("changeId", model.getChangeId());
		return result;
	}
	public ServiceResult query(Change model, Integer page, Integer rows) {
		
		ServiceResult result = new ServiceResult(false);
		
		List<Change> list = changeDao.query(model,page,rows);
		
		String[] properties = {"changeId","user.userId","user.userName","student.studentName",
				"student.studentId","date","expireDate","note","changeType","newStuClassName","newStuClassId","className","classId","intoAccount","lessons"};
		
		String data = JSONUtil.toJson(list,properties);
		result.addData("datagridData", data);
		
		result.setIsSuccess(true);
		return result;
	}
	public ServiceResult getTotalCount(Change model) {
		ServiceResult result = new ServiceResult(false);
		Long data = changeDao.getTotalCount(model);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult delete(Change model) {
		ServiceResult result = new ServiceResult(false);
		if(model==null||model.getChangeId()==null){
			result.setMessage("请选择要删除的异动单");
			return result;
		}
		Change oldModel = changeDao.load(model.getChangeId());
		if(oldModel==null){
			result.setMessage("该异动已不存在");
			return result;
		}else{
			changeDao.delete(oldModel);
			
			Payment payment=oldModel.getPayment();
			paymentDao.delete(payment);
			
			StuClass stuClass=oldModel.getStuClass();
			stuClass.setScStatus(1);
			Clazz clazz=stuClass.getClazz();
			clazz.setStuCount(clazz.getStuCount()+1);
			
			Student student=oldModel.getStudent();
			student.setAvailableMoney(student.getAvailableMoney()-oldModel.getIntoAccount());
			
			if(oldModel.getNewStuClass()!=null){
				StuClass newStuClass=oldModel.getNewStuClass();
				Clazz newClazz=newStuClass.getClazz();
				newClazz.setStuCount(newClazz.getStuCount()-1);
				stuClassDao.delete(newStuClass);
			}
		}
		result.setIsSuccess(true);
		return result;
	}
}
