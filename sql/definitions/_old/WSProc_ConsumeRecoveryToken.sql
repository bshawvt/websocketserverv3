CREATE DEFINER=`root`@`localhost` PROCEDURE `WSProc_ConsumeRecoveryToken`(IN mytoken BLOB, IN myemail VARCHAR(128), IN myhash BLOB)
    DETERMINISTIC
BEGIN
	
    
    DECLARE randbytes BLOB DEFAULT TO_BASE64(RANDOM_BYTES(16)); # generate random bytes
    DECLARE newCombinedHash BLOB DEFAULT WSService_MakeHash(myhash, randbytes);
    
	UPDATE useraccounts AS t1
		INNER JOIN useraccountrecovery AS t2
		ON t1.user_id = t2.owner_id
    SET t1.combinedHash = newCombinedHash, t1.salt = randbytes, t2.active = FALSE
		WHERE t2.active = TRUE AND t1.email = myemail AND t2.token = mytoken AND t2.recoveryExpirationDate > NOW();
    
    
END