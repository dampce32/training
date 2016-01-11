DROP PROCEDURE IF EXISTS `P_BillParams`;
DELIMITER ;;
# 消费单
CREATE  PROCEDURE `P_BillParams`(IN `billId` INT)
BEGIN
	SELECT    
              b.`billCode`,
              b.`billDate`,
	      b.pay,
	      b.payed,
              b.`note`,
              s.`studentName`,
              s.`studentId`,
              sc.`schoolName`,
              u.`userName`,
	case 
            when b.`billType` = 1 then '消费单'
            when b.`billType` = 0 then '退货单'
        end billType
        FROM t_bill b 
        LEFT JOIN t_student s ON b.`studentId`=s.`studentId`
        LEFT JOIN t_school sc ON b.`schoolId`=sc.`schoolId` 
        LEFT JOIN t_user u ON u.`userId`=b.`userId`
        WHERE b.`billId`=billId;	

END
;;
DELIMITER ;