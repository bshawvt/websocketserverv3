CREATE DEFINER=`root`@`localhost` PROCEDURE `WSProc_ConsumeToken`(IN mytoken BLOB, IN myip BLOB)
BEGIN
	# @param mytoken: the unique session token generated from WSProc_GenerateSessionOnAuth
	# @param myip: an ip address, typically $_SERVER['REMOTE_ADDR'], the one which initiated the query
	# @return: useraccount set
    IF (SELECT @myid:=id FROM wss_debug.useraccounts AS t1 WHERE t1.sessionToken=mytoken AND t1.sessionIP=myip AND t1.sessionExpirationDate > NOW()) THEN
		#SELECT @myid AS result;
        UPDATE wss_debug.useraccounts AS u1
        SET sessionExpirationDate=NULL
        WHERE sessionToken=mytoken AND sessionIP=myip;
        
        SELECT * FROM wss_debug.useraccounts AS t2 WHERE t2.id=@myid;
	ELSE
		SELECT * FROM wss_debug.useraccounts LIMIT 0; # return an empty set for schema purposes 
	END IF;

    
	
END