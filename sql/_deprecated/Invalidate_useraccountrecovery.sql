CREATE DEFINER=`root`@`localhost` PROCEDURE `Invalidate_useraccountrecovery`()
BEGIN
	UPDATE virtualworld1.useraccountrecovery SET active=0 WHERE active=1 AND creationDate < NOW() - INTERVAL 10 SECOND;
END