DROP PROCEDURE IF EXISTS `P_RecRejDetail`;
DELIMITER ;;

CREATE  PROCEDURE `P_RecRejDetail`(IN `recRejId` int)
BEGIN
   select 
		b.commodityName,
		c.commodityTypeName,
		d.unitName,
		a.qty,
		a.price,
		a.price*a.qty amount,
		e.warehouseName
  from(select *
		from t_recrejdetail a
		where a.recRejId = recRejId) a
  left join t_commodity b on a.commodityId = b.commodityId
  left join t_commoditytype c on b.commoditytypeId = c.commodityTypeId
  left join t_unit d on b.unitId = d.unitId
  left join t_warehouse e on a.warehouseId = e.warehouseId;

END
;;
DELIMITER ;