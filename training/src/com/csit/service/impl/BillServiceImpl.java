package com.csit.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.csit.dao.BillDAO;
import com.csit.dao.BillDetailDAO;
import com.csit.dao.CommodityDao;
import com.csit.dao.PaymentDAO;
import com.csit.dao.StoreDao;
import com.csit.dao.StudentDAO;
import com.csit.model.Bill;
import com.csit.model.BillDetail;
import com.csit.model.Commodity;
import com.csit.model.Course;
import com.csit.model.FeeItem;
import com.csit.model.Payment;
import com.csit.model.Store;
import com.csit.model.Student;
import com.csit.model.Warehouse;
import com.csit.service.BillService;
import com.csit.util.JSONUtil;
import com.csit.util.StringUtil;
import com.csit.vo.ServiceResult;
/**
 * @Description:消费Service实现类
 * @Copyright: 福州骏华信息有限公司 (c)2013
 * @Created Date : 2013-3-11
 * @author jcf
 * @vesion 1.0
 */
@Service
public class BillServiceImpl extends BaseServiceImpl<Bill, Integer> implements BillService {

	@Resource
	private BillDAO billDAO;
	@Resource
	private StudentDAO studentDAO;
	@Resource
	private BillDetailDAO billDetailDAO;
	@Resource
	private StoreDao storeDao;
	@Resource
	private PaymentDAO paymentDAO;
	@Resource
	private CommodityDao commodityDao;
	
	/* (non-Javadoc)   
	 * @see com.csit.service.PaymentService#delete(com.csit.model.Payment)   
	 */
	public ServiceResult delete(Bill model) {
		ServiceResult result = new ServiceResult(false);
		if (model == null || model.getBillId() == null) {
			result.setMessage("请选择要删除的消费");
			return result;
		}
		Bill oldModel = billDAO.load(model.getBillId());
		if (oldModel == null) {
			result.setMessage("该消费已不存在");
			return result;
		} else {
			billDAO.delete(oldModel);
		}
		result.setIsSuccess(true);
		return result;
	}

	public ServiceResult getTotalCount(Bill model,Date beginDate,Date endDate) {
		ServiceResult result = new ServiceResult(false);
		Long data = billDAO.getTotalCount(model,beginDate,endDate);
		result.addData("total", data);
		result.setIsSuccess(true);
		return result;
	}
	
	/* (non-Javadoc)   
	 * @see com.csit.service.PaymentService#query(com.csit.model.Payment, java.lang.Integer, java.lang.Integer)   
	 */
	public ServiceResult query(Bill model,Date beginDate,Date endDate, Integer page, Integer rows) {
		ServiceResult result = new ServiceResult(false);

		List<Bill> list = billDAO.query(model,beginDate,endDate, page, rows);

		String[] properties = { "billId", "billCode","school.schoolName","school.schoolId","user.userName","user.userId","billType", "billDate","pay","favourable", "payed","note","student.studentName","student.studentId"};

		String data = JSONUtil.toJson(list, properties);
		result.addData("datagridData", data);

		result.setIsSuccess(true);
		return result;
	}

	
	public ServiceResult save(Bill model,String billDetail,String cost) {
		ServiceResult result = new ServiceResult(false);
		if (model == null) {
			result.setMessage("请填写消费信息");
			return result;
		}
		List<Bill> billList=billDAO.query("billCode", model.getBillCode());
		if(model.getStudent()==null&&model.getStudent().getStudentId()==null){
			result.setMessage("您还没有选择学员");
			return result;
		}
		if(billList.size()!=0){
			result.setMessage("单号已存在，请重新生成");
			return result;
		}
		Student oldStu=studentDAO.load(model.getStudent().getStudentId());
		if(oldStu==null){
			result.setMessage("找不到该学员");
			return result;
		}
		if (model.getBillId() == null) {// 新增
			model.setInsertDate(new Date());
			billDAO.save(model);
			if(model.getBillType()==1){
				String[] billDetailStrs=StringUtil.split(billDetail);
				if(cost!=null){
					String[] costs=cost.split(",");
					Payment payment=new Payment();
					payment.setInsertTime(new Date());
					payment.setNote("即充即扣");
					payment.setSchool(model.getSchool());
					payment.setStudent(model.getStudent());
					payment.setUser(model.getUser());
					payment.setPaymentType(1);
					payment.setPayway(Integer.parseInt(costs[1]));
					payment.setPayMoney(Double.parseDouble(costs[0]));
					payment.setTransactionDate(model.getBillDate());
					paymentDAO.save(payment);
					oldStu.setAvailableMoney(oldStu.getAvailableMoney()+Double.parseDouble(costs[0])-model.getPayed());
					oldStu.setConsumedMoney(Math.round((oldStu.getConsumedMoney()+model.getPayed())*100)/100.0);
				}
				else {
					if(oldStu.getAvailableMoney()<model.getPayed()){
						throw new RuntimeException("学员账户余额不足");
					}
					oldStu.setAvailableMoney(Math.round((oldStu.getAvailableMoney()-model.getPayed())*100)/100.0);
					oldStu.setConsumedMoney(Math.round((oldStu.getConsumedMoney()+model.getPayed())*100)/100.0);
				}
				for(int i=0;i<billDetailStrs.length;i++){
					String[] detail=billDetailStrs[i].split(",");
					BillDetail billDetailModel=new BillDetail();
					billDetailModel.setProductType(detail[0]);
					if("商品".equals(detail[0])){
						Commodity commodity=commodityDao.load(Integer.parseInt(detail[1]));
						commodity.setQtyStore(commodity.getQtyStore()-Integer.parseInt(detail[3]));
						billDetailModel.setCommodity(commodity);
						Warehouse warehouse=new Warehouse();
						warehouse.setWarehouseId(Integer.parseInt(detail[7]));
						Store store=storeDao.query(commodity, warehouse);
						if(store==null||store.getQtyStore()==null||(store.getQtyStore()-Integer.parseInt(detail[3])<0)){
							throw new RuntimeException("第"+(i+1)+"行商品库存不足");
						}
						store.setQtyStore(store.getQtyStore()-Integer.parseInt(detail[3]));
						billDetailModel.setWarehouse(warehouse);
						billDetailModel.setStatus(3);
					}
					if("收费项".equals(detail[0])){
						FeeItem feeItem=new FeeItem();
						feeItem.setFeeItemId(Integer.parseInt(detail[1]));
						billDetailModel.setFeeItem(feeItem);
					}
					if("课程".equals(detail[0])){
						Course course=new Course();
						course.setCourseId(Integer.parseInt(detail[1]));
						billDetailModel.setCourse(course);
						billDetailModel.setStatus(0);
					}
					billDetailModel.setPrice(Double.parseDouble(detail[2]));
					billDetailModel.setQty(Integer.parseInt(detail[3]));
					billDetailModel.setDiscount(Double.parseDouble(detail[4]));
					billDetailModel.setDiscountAmount(Double.parseDouble(detail[5]));
					billDetailModel.setUnitName(detail[6]);
					billDetailModel.setBill(model);
					billDetailModel.setReturnQty(0);
					billDetailDAO.save(billDetailModel);
				}
				Payment payment2=new Payment();
				payment2.setInsertTime(new Date());
				payment2.setSchool(model.getSchool());
				payment2.setStudent(model.getStudent());
				payment2.setUser(model.getUser());
				payment2.setPaymentType(5);
				payment2.setNote("单号："+model.getBillCode());
				payment2.setPayMoney(-model.getPayed());
				payment2.setTransactionDate(model.getBillDate());
				paymentDAO.save(payment2);
				oldStu.setBillCount(oldStu.getBillCount()+1);
			}
			if(model.getBillType()==0){
				String[] billDetailStrs=StringUtil.split(billDetail);
				for(int i=0;i<billDetailStrs.length;i++){
					String[] detail=billDetailStrs[i].split(",");
					BillDetail oldBillDetail=billDetailDAO.load(Integer.parseInt(detail[0]));
					if(oldBillDetail.getStatus()==2){
						throw new RuntimeException("第"+(i+1)+"行物品已退货");
					}
					if(oldBillDetail.getStatus()==1){
						throw new RuntimeException("第"+(i+1)+"行物品已处理");
					}
					if(oldBillDetail.getActualQty().intValue()<Integer.parseInt(detail[1])){
						throw new RuntimeException("第"+(i+1)+"行物品退货数量大于购买时数量");
					}
					BillDetail billDetailModel=new BillDetail();
					billDetailModel.setProductType(oldBillDetail.getProductType());
					if("商品".equals(oldBillDetail.getProductType())){
						billDetailModel.setCommodity(oldBillDetail.getCommodity());
						Commodity commodity=commodityDao.load(oldBillDetail.getCommodity().getCommodityId());
						commodity.setQtyStore(commodity.getQtyStore()+Integer.parseInt(detail[1]));
						billDetailModel.setWarehouse(oldBillDetail.getWarehouse());
						Store store=storeDao.query(oldBillDetail.getCommodity(), oldBillDetail.getWarehouse());
						store.setQtyStore(store.getQtyStore()+Integer.parseInt(detail[1]));
					}
					if("课程".equals(oldBillDetail.getProductType())){
						billDetailModel.setCourse(oldBillDetail.getCourse());
					}
					billDetailModel.setQty(Integer.parseInt(detail[1]));
					if((oldBillDetail.getReturnQty()+Integer.parseInt(detail[1]))==oldBillDetail.getQty().intValue()){
						oldBillDetail.setStatus(2);
					}
					oldBillDetail.setReturnQty(oldBillDetail.getReturnQty()+Integer.parseInt(detail[1]));
					billDetailModel.setPrice(oldBillDetail.getPrice());
					billDetailModel.setDiscount(Double.parseDouble(detail[2]));
					billDetailModel.setDiscountAmount(Double.parseDouble(detail[3]));
					billDetailModel.setUnitName(oldBillDetail.getUnitName());
					billDetailModel.setReturnQty(0);
					billDetailModel.setBill(model);
					billDetailDAO.save(billDetailModel);
				}
				oldStu.setAvailableMoney(oldStu.getAvailableMoney()+model.getPayed());
				oldStu.setConsumedMoney(oldStu.getConsumedMoney()-model.getPayed());
				Payment payment=new Payment();
				payment.setInsertTime(new Date());
				payment.setSchool(model.getSchool());
				payment.setStudent(model.getStudent());
				payment.setUser(model.getUser());
				payment.setPaymentType(6);
				payment.setPayMoney(model.getPayed());
				payment.setTransactionDate(model.getBillDate());
				payment.setNote("单号："+model.getBillCode());
				paymentDAO.save(payment);
			}
			
		} else {
			
		}
		result.setIsSuccess(true);
		result.addData("billId", model.getBillId());
		result.addData("availableMoney", oldStu.getAvailableMoney());
		result.addData("consumedMoney", oldStu.getConsumedMoney());
		return result;
	}

	@Override
	public ServiceResult initBill(String userId) {
		ServiceResult result = new ServiceResult(false);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		String date = sdf.format(new Date())+userId;
		Long data = billDAO.initBillCode(date);
		result.addData("billCode", data);
		result.setIsSuccess(true);
		return result;
	}

	@Override
	public ServiceResult queryDetail(Bill model) {
		ServiceResult result = new ServiceResult(false);
		List<BillDetail> list = billDetailDAO.query("bill",model);
		String[] properties = { "billDetailId","productType","price", "qty","discount", "discountAmount", "warehouse.warehouseId", "warehouse.warehouseName", "unitName", "billItemName","totalPrice","payed"
				};
		String data = JSONUtil.toJson(list, properties);
		result.addData("datagridData", data);
		result.setIsSuccess(true);
		return result;
	}

}
