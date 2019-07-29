CREATE DEFINER=`root`@`localhost` PROCEDURE `Invalidate_activesessions`()
BEGIN
	#UPDATE virtualworld1.activesessions SET active=0 WHERE active=1 AND (NOW() - dateOfCreation) > 10;
    UPDATE virtualworld1.activesessions SET active=0 WHERE active=1 AND dateOfCreation < NOW() - INTERVAL 10 SECOND;
END