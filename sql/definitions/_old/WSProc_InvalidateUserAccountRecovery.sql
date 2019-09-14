CREATE DEFINER=`root`@`localhost` PROCEDURE `WSProc_InvalidateUserAccountRecovery`()
    DETERMINISTIC
BEGIN
	UPDATE virtualworld1.useraccountrecovery SET active=0 WHERE active=1 AND creationDate < NOW() - INTERVAL 2 HOUR;
END