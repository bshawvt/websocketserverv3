CREATE DEFINER=`root`@`localhost` PROCEDURE `WSProc_GenerateTokenOnAuth`(IN myuser VARCHAR(18), IN myhash BLOB, IN myip BLOB)
BEGIN
	# if user password is the same then generate a new random token and update user set to include a new lastLoginDate, session token, sessionIP and sessionDateOfCreation
	# @param myuser: a username
    # @param myhash: a raw sha256 hash
    # @param myip: an ip address, typically $_SERVER['REMOTE_ADDR'], the one which initiated the query
    # @return: the newly generated session token
    /*IF (SELECT COUNT(*) FROM useraccounts AS t1 WHERE (t1.username=LOWER(myuser) AND t1.combinedHash=WSService_MakeHash(myhash, t1.salt))) THEN
		SET @mytoken = WSService_MakeHash(myuser, TO_BASE64(RANDOM_BYTES(32))); # this token should be unique because usernames are unique
        
        UPDATE useraccounts AS u1 
        SET u1.sessionIp=myip, u1.lastLoginDate=NOW(), u1.sessionExpirationDate=TIMESTAMPADD(SECOND, 10, NOW()), sessionToken=@mytoken 
        WHERE u1.username=LOWER(myuser);
        
        SELECT @mytoken as sessionToken;
	#ELSE
		#SELECT NULL;
	END IF;*/
    DECLARE randbytes BLOB DEFAULT TO_BASE64(RANDOM_BYTES(32));
    DECLARE token BLOB DEFAULT WSService_MakeHash(myhash, randbytes);
    #DECLARE tmpGenToken BLOB DEFAULT NULL; # will only be set if the update WHERE criteria are met
    
    UPDATE useraccounts AS t1
    SET t1.sessionToken = token, t1.sessionExpirationDate=TIMESTAMPADD(SECOND, 10, NOW()), t1.lastLoginDate = NOW(), t1.sessionIp = myip
    WHERE t1.username = LOWER(myuser) AND t1.combinedHash = WSService_MakeHash(myhash, t1.salt);
    
	IF (ROW_COUNT() > 0) THEN
		SELECT token AS sessionToken;
	ELSE
		SELECT NULL AS sessionToken;
	END IF;
	
    
    
END