DROP PROCEDURE IF EXISTS `P_BillDetail`;
DELIMITER ;;
# ���ѵ�
CREATE  PROCEDURE `P_BillDetail`(IN `billId` int)
BEGIN
   SELECT 
	a.productType,
	case 
            when a.productType = '��Ʒ' then b.`commodityName`
            when a.productType = '�շ���' then d.`feeItemName`
	    when a.productType = '�γ�' then c.`courseName`
        end productName,
	e.`warehouseName`,
	a.price,
	a.qty,
	a.price*a.qty-a.discountAmount amount,
	a.discount,
	a.discountAmount,
	a.unitName 
   FROM(SELECT * FROM t_billdetail a WHERE a.`billId`=billId) a
   LEFT JOIN t_commodity b ON b.`commodityId`=a.`commodityId` 
   LEFT JOIN t_course c ON c.`courseId`=a.`courseId` 
   LEFT JOIN t_feeitem d ON d.`feeItemId`=a.`feeItemId`
   LEFT JOIN t_warehouse e ON e.`warehouseId`=a.`warehouseId`;

END
;;
DELIMITER ;