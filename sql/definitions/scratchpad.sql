/*
SELECT * FROM useraccounts AS b1
		JOIN sessions AS b2
		JOIN characters AS b3 ON b3.character_owner = b1.user_id
		WHERE b1.user_id = b2.session_owner AND b2.session_token = "07ca25ef3d0c15857b64b383a7c486af0b7bd8fb721dcdf7212a1a5b4edd2abc" 
			AND b2.session_expirationDate > NOW() AND b2.session_active = FALSE;# AND b3.character_owner = b1.user_id;
*/


/*SELECT * FROM useraccounts AS b1
		JOIN sessions AS b2 ON b2.session_owner = b1.user_id 
			AND b2.session_token = "452ea2d524240a0b0c1bd849dd5fcf562cddded17fefd19cb0006ba94de83b95"
            AND b2.session_expirationDate > NOW()
            AND b2.session_active = TRUE
		LEFT JOIN characters AS b3 ON b3.character_owner = b1.user_id*/
        

/*
INSERT INTO characters (character_name, character_description, character_owner)
	SELECT "agudnaem", "this is test character", 1;
INSERT INTO characters (character_name, character_description, character_owner)
	SELECT "testuser0 1", "this is test character", 2;
INSERT INTO characters (character_name, character_description, character_owner)
	SELECT "testuser0 2", "this is test character", 2;*/

#SELECT * FROM characters;

		#WHERE b1.user_id = b2.session_owner AND b2.session_token = "452ea2d524240a0b0c1bd849dd5fcf562cddded17fefd19cb0006ba94de83b95" 
		#	AND b2.session_expirationDate > NOW() AND b2.session_active = FALSE;# AND b3.character_owner = b1.user_id;


#FROM useraccounts JOIN sessions AS t1 WHERE SELECT *;
#INSERT INTO characters (character_name, character_description, character_owner)
#SELECT "testuser1 1", "this is test character", 0;

SELECT * FROM useraccounts;
#SELECT * FROM characters;
#SELECT * FROM sessions;

#UPDATE useraccounts SET locked=0 WHERE username='testuser0';
#CALL WSProc_GenerateSessionToken("testuser0", "2", "localhost");
#CALL WSProc_InsertUserAccount("testuser3", "password", "n/a0");

#INSERT INTO useraccounts(username, combinedHash, salt, email) 
#SELECT LOWER(myuser), newCombinedHash, randbytes, LOWER(myemail); # FROM DUAL 

# create a new recovery token row but requires a valid user_id from useraccounts
#INSERT INTO useraccountrecovery (token, owner_id)
#SELECT "generated_token1", (SELECT t1.user_id FROM useraccounts AS t1 WHERE t1.email = "a@b.com");
#UPDATE useraccountrecovery AS t1 SET active = 0 WHERE token = "" AND recoveryExpirationDate  > NOW();

#(IN mytoken BLOB, IN myemail VARCHAR(128), IN myhash BLOB)
#CALL WSProc_ConsumeRecoveryToken("1c7a296883975b6cf64d7da01c117f3b99e85b602941a3b64fd947fac2cfeb66", "nituvious@gmail.com", "test2");

# 
#CALL WSProc_GenerateSessionToken("testuser0", "2", "localhost");
#CALL WSProc_ConsumeSessionToken("ab3a72e761c83ef182e5e2208835e59c09af853b29634fb203ee6c60babf0772", "localhost");

#SELECT * FROM useraccounts JOIN sessions WHERE user_id = session_owner AND session_active = TRUE;

#SELECT * FROM sessions;

#SELECT * FROM sessions AS t1 JOIN useraccounts AS t2 WHERE t1.session_owner = t2.user_id AND t1.session_expirationDate > NOW();
#SELECT * FROM useraccounts AS t1 JOIN sessions AS t2 WHERE t2.session_token = mytoken AND t2.session_active = TRUE AND t2.session_expirationDate > NOW();
#SELECT * FROM sessions AS t1 LEFT JOIN useraccounts AS t2 WHERE t1.session_expirationDate > NOW();
#SELECT * FROM sessions AS t1 JOIN useraccounts AS t2 WHERE t1.session_expirationDate > NOW();
#SELECT * FROM useraccounts;
# now set new password based if token and time is valid
#UPDATE 

    /*IF (SELECT @myid:=user_id FROM useraccounts AS t1 WHERE t1.sessionToken=mytoken AND t1.sessionExpirationDate > NOW()) THEN
		#SELECT @myid AS result;
        UPDATE useraccounts AS u1
        SET u1.sessionExpirationDate=NULL, u1.sessionToken=NULL, u1.sessionIp=myip
        WHERE u1.sessionToken=mytoken;
        
        SELECT * FROM useraccounts AS t2 WHERE t2.user_id=@myid;
	ELSE

		SELECT * FROM useraccounts LIMIT 0; # return an empty set for schema purposes 
	END IF;*/

# consume 
#UPDATE useraccounts AS u1
#SET u1.combinedHash = WSService_MakeHash("new", "salt")
#WHERE u1.owner_id = (SELECT * FROM useraccountrecovery AS t1 WHERE t1.email = "a@b.com" AND t2.);

#SELECT * FROM useraccounts;#recovery;# AS t1 WHERE t1.token = "generated_token";
#CALL WSProc_GenerateRecoveryToken("nituvious@gmail.com");

#SET token = "poop", owner_id = (SELECT t1.user_id FROM useraccounts AS t1 WHERE email = "a@bc.comc");

#SELECT * FROM useraccountrecovery;


#CALL WSProc_GenerateActiveSessionOnAuth("testuser0", "password");
# step one: auth user
# step two: generate session token
# step three: result set with session token

#SELECT * FROM wss_debug.useraccounts AS t1 WHERE (t1.username=LOWER("testuser0") AND t1.combinedHash=WSService_MakeHash("password1", t1.salt));

#CALL WSProc_GenerateSessionOnAuth("testuser0", "password", "127.0.0.1");
#SELECT*FROM wss_debug.useraccounts;
#CALL WSProc_ConsumeToken('00b01fed09cf2e266ff39f9d126483a03874fc2dfd3c002dec2fc8bec0c003ca', "127.0.0.1");
#CALL WSProc_SelectUserAccountOnAuth("testuser0", "password");
#SELECT * FROM wss_debug.useraccounts;

#CALL WSProc_GenerateTokenOnAuth("testuser0", "password", "");
#CALL WSProc_ConsumeToken('cb5bfcf7591f13a37fd6b38446887e3bbf65028abc041c2876b54fcc7722e238', "127.0.0.1");
#SELECT * FROM wss_debug.useraccounts WHERE id = 213;


#SET @t = NOW() + INTERVAL 25 SECOND;
#SELECT timestampadd(SECOND, 50, now());#TIMESTAMPDIFF(SECOND, NOW(), @t);

#SELECT * FROM wss_debug.useraccounts;
#UPDATE wss_debug.useraccounts as u SET u.lastLoginDate=NOW() WHERE username=LOWER(myuser) AND combinedHash=WSService_MakeHash(myhash, salt);
#SELECT * FROM wss_debug.useraccounts WHERE username=LOWER(myuser) AND combinedHash=WSService_MakeHash(myhash, salt);

#SELECT * FROM wss_debug.useraccounts WHERE id=4;
#SELECT * FROM wss_debug.useraccounts AS t1 JOIN wss_debug.activesessions AS t2 ON t2.useraccountid=t1.id;