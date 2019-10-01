CREATE DEFINER=`root`@`localhost` PROCEDURE `WSProc_GenerateTokenOnAuth`(IN myuser VARCHAR(18), IN myhash BLOB, IN myip BLOB)
BEGIN
	# if user password is the same then generate a new random token and update user set to include a new lastLoginDate, session token, sessionIP and sessionDateOfCreation
	# @param myuser: a username
    # @param myhash: a raw sha256 hash
    # @param myip: an ip address, typically $_SERVER['REMOTE_ADDR'], the one which initiated the query
    # @return: the newly generated session token
    IF (SELECT COUNT(*) FROM wss_debug.useraccounts AS t1 WHERE (t1.username=LOWER(myuser) AND t1.combinedHash=WSService_MakeHash(myhash, t1.salt))) THEN
		SET @mytoken = WSService_MakeHash(myuser, TO_BASE64(RANDOM_BYTES(32))); # this token should be unique because usernames are unique
        
        UPDATE wss_debug.useraccounts AS u1 
        SET u1.sessionIP=myip, u1.lastLoginDate=NOW(), u1.sessionExpirationDate=TIMESTAMPADD(SECOND, 10, NOW()), sessionToken=@mytoken 
        WHERE u1.username=LOWER(myuser);
        
        SELECT @mytoken as sessionToken;
	#ELSE
		#SELECT NULL;
	END IF;

END