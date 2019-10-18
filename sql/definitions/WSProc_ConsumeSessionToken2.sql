CREATE DEFINER=`root`@`localhost` PROCEDURE `WSProc_ConsumeSessionToken`(IN mytoken BLOB, IN myip BLOB)
BEGIN

    UPDATE sessions AS a1
		JOIN useraccounts AS a2
	SET a1.session_active = FALSE, a2.lastLoginDate = NOW(), a1.session_consumedBy = myip
	WHERE a2.user_id = a1.session_owner AND a1.session_token = mytoken AND a1.session_expirationDate > NOW() AND a1.session_active = TRUE;
    
    IF (ROW_COUNT() > 0) THEN
		SELECT * FROM useraccounts AS b1
		JOIN sessions AS b2 ON b2.session_owner = b1.user_id 
			AND b2.session_token = mytoken
            AND b2.session_expirationDate > NOW()
            AND b2.session_active = FALSE
		LEFT JOIN characters AS b3 ON b3.character_owner = b1.user_id;
        /*
        SELECT * FROM useraccounts AS b1
			JOIN sessions AS b2
				#JOIN characters AS b3
		WHERE b1.user_id = b2.session_owner AND b2.session_token = mytoken AND b2.session_expirationDate > NOW() AND b2.session_active = FALSE;# AND b3.character_owner = b1.user_id;
        
        */
	END IF;
    
END
