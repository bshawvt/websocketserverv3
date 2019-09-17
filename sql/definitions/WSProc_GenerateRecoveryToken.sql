CREATE DEFINER=`root`@`localhost` PROCEDURE `WSProc_GenerateRecoveryToken`(IN email VARCHAR(128))
BEGIN
	SET @token = WSService_MakeHash(TO_BASE64(RANDOM_BYTES(24)), email);
	INSERT INTO useraccountrecovery (token, owner_id, recoveryExpirationDate)
	SELECT @token, (SELECT t1.user_id FROM useraccounts AS t1 WHERE t1.email = email), TIMESTAMPADD(HOUR, 2, NOW());
    
    SELECT @token AS recoveryToken;
END