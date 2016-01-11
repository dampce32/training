DROP PROCEDURE IF EXISTS `P_DailySummary`;
DELIMITER ;;
# 统计
CREATE  PROCEDURE `P_DailySummary`(IN `schoolId_in` INT,IN `beginDate` DATE,IN `endDate` DATE)
BEGIN
	DECLARE var_schoolCode VARCHAR(100);
        SELECT schoolCode INTO var_schoolCode
        FROM t_school
        WHERE schoolId = schoolId_in;
	SELECT 
	      p.`transactionDate` '日期',
	      SUM(p.`payMoney`) '金额' 
	      FROM t_payment p LEFT JOIN t_school sc ON sc.schoolId=p.`schoolId` 
	      WHERE p.`paymentType`=1 AND p.`transactionDate` BETWEEN beginDate AND endDate AND sc.schoolCode LIKE CONCAT(var_schoolCode,'%')
	      GROUP BY p.`transactionDate`;	
END
;;
DELIMITER ;