DROP PROCEDURE IF EXISTS `P_BillParams`;
DELIMITER ;;
# ���ѵ�
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
            when b.`billType` = 1 then '���ѵ�'
            when b.`billType` = 0 then '�˻���'
        end billType
        FROM t_bill b 
        LEFT JOIN t_student s ON b.`studentId`=s.`studentId`
        LEFT JOIN t_school sc ON b.`schoolId`=sc.`schoolId` 
        LEFT JOIN t_user u ON u.`userId`=b.`userId`
        WHERE b.`billId`=billId;	

END
;;
DELIMITER ;