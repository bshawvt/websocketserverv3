use wss_debug;

#CALL WSProc_InsertUserAccount("server", "", "");
#CALL WSProc_InsertUserAccount("testuser1", "password", "n/a1");
#CALL WSProc_InsertUserAccount("testuser2", "password", "n/a2");

#SELECT * FROM useraccounts;
/*
INSERT INTO characters(owner, name)
		SELECT (SELECT t1.id FROM useraccounts AS t1 WHERE t1.username="testuser2"), "test00";
     
INSERT INTO characters(owner, name)
		SELECT (SELECT t1.id FROM useraccounts AS t1 WHERE t1.username="testuser0"), "test00";
        
INSERT INTO characters(owner, name)
		SELECT (SELECT t1.id FROM useraccounts AS t1 WHERE t1.username="testuser1"), "test01";
        
INSERT INTO characters(owner, name)
		SELECT (SELECT t1.id FROM useraccounts AS t1 WHERE t1.username="testuser0"), "test00";
        
INSERT INTO characters(owner, name)
		SELECT (SELECT t1.id FROM useraccounts AS t1 WHERE t1.username="testuser2"), "test02";
        
INSERT INTO characters(owner, name)
		SELECT (SELECT t1.id FROM useraccounts AS t1 WHERE t1.username="testuser2"), "test02";
        
INSERT INTO characters(owner, name)
		SELECT (SELECT t1.id FROM useraccounts AS t1 WHERE t1.username="testuser1"), "test01";
*/
#SELECT * FROM useraccounts;
#SELECT * FROM characters AS t1 JOIN useraccounts AS t2 WHERE t2.id = t1.owner;
#SELECT * FROM characters;
#SELECT * FROM characters AS t1 INNER JOIN useraccounts AS t2 WHERE t2.id = t1.owner;

# create a user for npc's and set it's user id to 0 and lock it
INSERT INTO useraccounts(username, combinedHash, salt) 
		SELECT "world", "", "";
UPDATE useraccounts as u 
	SET u.user_id=0, u.locked=true
	WHERE username="world";
        
