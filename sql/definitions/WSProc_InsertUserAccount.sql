CREATE DEFINER=`root`@`localhost` PROCEDURE `WSProc_InsertUserAccount`(IN myuser VARCHAR(18), IN myhash BLOB, IN myemail VARCHAR(128))
BEGIN
	# inserts a new user if @myuser and @myemail are not already in use
    # @param myuser: becomes useraccounts.username
    # @param myhash: should be a raw sha256 hash
    # @param myemail: becomes useraccounts.email

	DECLARE randbytes BLOB DEFAULT TO_BASE64(RANDOM_BYTES(16)); # generate random bytes
    DECLARE newCombinedHash BLOB DEFAULT WSService_MakeHash(myhash, randbytes);
    
    INSERT INTO useraccounts(username, combinedHash, salt, email) 
		SELECT LOWER(myuser), newCombinedHash, randbytes, LOWER(myemail); # FROM DUAL 
        #WHERE NOT EXISTS (
		#	SELECT * FROM wss_debug.useraccounts 
        #    WHERE username=LOWER(myuser) OR email=LOWER(myemail)
		#);

END