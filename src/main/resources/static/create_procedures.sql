DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_create_commerce`(
	e_codent varchar(4),
	e_centalta varchar(4),
	e_cuenta varchar(12),
	e_codcom varchar(15),
	e_tiporeg varchar(2),
	e_numorden bigint,
	e_nomcomred varchar(27),
	e_codcadena varchar(3),
	e_descadena varchar(30),
	e_codholding varchar(4),
	e_desholding varchar(30),
	e_identcli varchar(8),
	e_tipdoc varchar(3),
	e_numdoc varchar(20),
	e_producto varchar(2),
	e_subprodu varchar(4),
	e_desprod varchar(30),
	e_conprod varchar(3),
	e_desconred varchar(10),
	e_tipbon varchar(6),
	e_tipoprov varchar(1),
	e_codact bigint,
	e_desact varchar(30),
	e_nivaplcg varchar(1),
	e_iban varchar(4),
	e_ctacargo varchar(30),
	e_clamon bigint,
	e_desclamon varchar(30),
	e_indtipcta varchar(2),
	e_indajena varchar(1),
	e_codcam varchar(6),
	e_fecalta date,
	e_fecbaja date,
	e_motbaja varchar(2),
	e_desmot varchar(30),
	e_fecreact date,
	e_codrie bigint,
	e_fecultrie date,
	e_indcont varchar(1),
	e_codproceso bigint,
	e_codgrupo bigint,
	e_codregimen bigint,
	e_desregimen varchar(30),
	e_processdate date
)
BEGIN
	INSERT INTO commerce (pc_codent, pc_centalta, pc_cuenta, pc_codcom, pc_tiporeg, pc_numorden, pc_nomcomred, pc_codcadena, pc_descadena, pc_codholding, pc_desholding, pc_identcli, pc_tipdoc, pc_numdoc, pc_producto, pc_subprodu, pc_desprod, pc_conprod, pc_desconred, pc_tipbon, pc_tipoprov, pc_codact, pc_desact, pc_nivaplcg, pc_iban, pc_ctacargo, pc_clamon, pc_desclamon, pc_indtipcta, pc_indajena, pc_codcam, pc_fecalta, pc_fecbaja, pc_motbaja, pc_desmot, pc_fecreact, pc_codrie, pc_fecultrie, pc_indcont, pc_codproceso, pc_codgrupo, pc_codregimen, pc_desregimen, pc_processdate)
    VALUES (e_codent, e_centalta, e_cuenta, e_codcom, e_tiporeg, e_numorden, e_nomcomred, e_codcadena, e_descadena, e_codholding, e_desholding, e_identcli, e_tipdoc, e_numdoc, e_producto, e_subprodu, e_desprod, e_conprod, e_desconred, e_tipbon, e_tipoprov, e_codact, e_desact, e_nivaplcg, e_iban, e_ctacargo, e_clamon, e_desclamon, e_indtipcta, e_indajena, e_codcam, e_fecalta, e_fecbaja, e_motbaja, e_desmot, e_fecreact, e_codrie, e_fecultrie, e_indcont, e_codproceso, e_codgrupo, e_codregimen, e_desregimen, e_processdate);
END ;;
DELIMITER ;


DELIMITER ;;

CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_extract_commerce`(IN e_processdate DATE)
BEGIN
    DECLARE modified_rows INT DEFAULT 0;

    START TRANSACTION;

    -- Insertar filas en commerce_quarantine desde commerce
    INSERT INTO commerce_quarantine (pc_codent, pc_centalta, pc_cuenta, pc_codcom, pc_tiporeg, pc_numorden, pc_nomcomred,
        pc_codcadena, pc_descadena, pc_codholding, pc_desholding, pc_identcli, pc_tipdoc, pc_numdoc, pc_producto, pc_subprodu,
        pc_desprod, pc_conprod, pc_desconred, pc_tipbon, pc_tipoprov, pc_codact, pc_desact, pc_nivaplcg, pc_iban, pc_ctacargo,
        pc_clamon, pc_desclamon, pc_indtipcta, pc_indajena, pc_codcam, pc_fecalta, pc_fecbaja, pc_motbaja, pc_desmot, pc_fecreact,
        pc_codrie, pc_fecultrie, pc_indcont, pc_codproceso, pc_codgrupo, pc_codregimen, pc_desregimen, pc_processdate)
    SELECT pc_codent, pc_centalta, pc_cuenta, pc_codcom, pc_tiporeg, pc_numorden, pc_nomcomred, pc_codcadena, pc_descadena,
        pc_codholding, pc_desholding, pc_identcli, pc_tipdoc, pc_numdoc, pc_producto, pc_subprodu, pc_desprod, pc_conprod, pc_desconred,
        pc_tipbon, pc_tipoprov, pc_codact, pc_desact, pc_nivaplcg, pc_iban, pc_ctacargo, pc_clamon, pc_desclamon, pc_indtipcta, pc_indajena,
        pc_codcam, pc_fecalta, pc_fecbaja, pc_motbaja, pc_desmot, pc_fecreact, pc_codrie, pc_fecultrie, pc_indcont, pc_codproceso, pc_codgrupo,
        pc_codregimen, pc_desregimen, pc_processdate
    FROM commerce
    WHERE CHARACTER_LENGTH(TRIM(pc_nomcomred)) = 0 
        OR CHARACTER_LENGTH(TRIM(pc_numdoc)) = 0 
        OR pc_numdoc NOT REGEXP '^[0-9]+$';

    -- Capturar el número de filas insertadas
    SET modified_rows = ROW_COUNT();

    -- Eliminar coincidencias de commerce_quarantine
    DELETE FROM commerce
    WHERE CHARACTER_LENGTH(TRIM(pc_nomcomred)) = 0 
        OR CHARACTER_LENGTH(TRIM(pc_numdoc)) = 0 
        OR pc_numdoc NOT REGEXP '^[0-9]+$';

    -- Confirmar o revertir la transacción según el número de filas afectadas
    IF ROW_COUNT() = modified_rows THEN 
        COMMIT;
    ELSE
        ROLLBACK;
        SET modified_rows = -1;
    END IF;

    -- Retornar el número de filas afectadas
    SELECT modified_rows AS total_columns;
END ;;

DELIMITER ;