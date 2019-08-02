CREATE DEFINER=`root`@`localhost` PROCEDURE `WSProc_SelectUserAccountOnAuth`(IN myuser CHAR(18), IN myhash BLOB )
BEGIN
# updates useraccount.lastLoginDate on successful auth and returns the useraccount
# @param myuser: typically matches useraccounts.username
# @param myhash: should be a raw sha256 hash
# @return: row or nothing at all if user details are incorrect
UPDATE wss_debug.useraccounts as u SET u.lastLoginDate=NOW() WHERE username=LOWER(myuser) AND combinedHash=WSService_MakeHash(myhash, salt);
SELECT * FROM wss_debug.useraccounts WHERE username=LOWER(myuser) AND combinedHash=WSService_MakeHash(myhash, salt);

END