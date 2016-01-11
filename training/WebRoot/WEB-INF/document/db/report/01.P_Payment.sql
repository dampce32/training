DROP PROCEDURE IF EXISTS `P_Payment`;
DELIMITER ;;

CREATE  PROCEDURE `P_Payment`(IN `schoolId_in` INT)
BEGIN
	declare var_schoolCode VARCHAR(100);
        select schoolCode into var_schoolCode
        from t_school
        where schoolId = schoolId_in;
	SELECT 
		p.`paymentId`,
		p.`payMoney`,
		p.`transactionDate`,
		p.`note`,
		p.`creditExpiration`,
		sc.`schoolName`,
		st.`studentName`,
		u.`userName`,
	case 
            when p.`paymentType` = 1 then '交费'
            when p.`paymentType` = 2 then '退费'
	    when p.`paymentType` = 3 then '借款'
	    when p.`paymentType` = 4 then '扣除借款'
        end paymentType,
	case 
            when p.`payway` = 1 then '现金'
            when p.`payway` = 2 then '刷卡'
	    when p.`payway` = 3 then '转账'
	    when p.`payway` = 4 then '支票'
	    when p.`payway` = 5 then '网银'
	    when p.`payway` = 6 then '支付宝'
	    when p.`payway` = 7 then '其他'
        end payway 
 	FROM t_payment p LEFT JOIN t_school sc ON sc.`schoolId`=p.`schoolId`
  	LEFT JOIN t_user u ON u.`userId`=p.`userId`
   	LEFT JOIN t_student st ON st.`studentId`=p.`studentId`
   	WHERE p.`paymentType` IN(1,2,3,4) and sc.schoolCode like concat(var_schoolCode,'%');	

END
;;
DELIMITER ;