CREATE DEFINER=`root`@`localhost` FUNCTION `WSService_MakeHash`(mysalt BLOB, myhash BLOB) RETURNS blob
BEGIN 
# @param mysalt: should be base64 encoded bytes, typically resulting from TO_BASE64(useraccounts.salt)
# @param myhash: should be a raw sha256 hash
# @return:  a new hash
	DECLARE newhash BLOB DEFAULT NULL;
    SET newhash = SHA2(CONCAT(myhash, mysalt), 256);

RETURN newhash;
END