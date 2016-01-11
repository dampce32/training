DROP PROCEDURE IF EXISTS `P_RecRejParams`;
DELIMITER ;;

CREATE  PROCEDURE `P_RecRejParams`(IN `recRejId` int)
BEGIN
	SELECT 
		a.recRejCode,
		a.recRejDate,
		a.qtyTotal,
		a.amountTotal,
		b.supplierName,
		c.userName,
		a.note,
		a.recRejType
	from t_recrej a
	left join t_supplier b on a.supplierId = b.supplierId
	left join t_user c on a.userId = c.userId
	where a.recRejId = recRejId;	

END
;;
DELIMITER ;