CREATE DEFINER=`root`@`localhost` PROCEDURE `WSProc_GenerateSessionToken`(IN myuser VARCHAR(18), IN myhash BLOB, IN myip BLOB)
BEGIN

    DECLARE randbytes BLOB DEFAULT TO_BASE64(CONCAT(RANDOM_BYTES(32), NOW()));
    DECLARE token VARCHAR(128) DEFAULT WSService_MakeHash(myuser, randbytes);

    INSERT INTO sessions ( session_token, session_expirationDate, session_owner, session_createdBy ) 
	SELECT token, TIMESTAMPADD(SECOND, 10, NOW()), t1.user_id, myip
		FROM useraccounts AS t1
	WHERE ( t1.username = LOWER(myuser) AND t1.combinedHash = WSService_MakeHash(myhash, t1.salt) );
    
	IF (ROW_COUNT() > 0) THEN
		SELECT token AS sessionToken;
	END IF;
	
END