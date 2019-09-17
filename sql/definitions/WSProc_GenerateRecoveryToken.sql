CREATE PROCEDURE `WSProc_GenerateRecoveryToken` (IN email VARCHAR(128))
BEGIN
	INSERT INTO useraccountrecovery (token, owner_id)
	SELECT WSService_MakeHash(TO_BASE64(RANDOM_BYTES(24)), email), (SELECT t1.user_id FROM useraccounts AS t1 WHERE t1.email = email);
END
