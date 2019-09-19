# grant user access to our stored functions and procedures
use wss_debug;

#GRANT EXECUTE ON FUNCTION wss_debug.WSService_MakeHash TO 'web'@'%';
#REVOKE EXECUTE ON FUNCTION wss_debug.WSService_MakeHash FROM 'web'@'%';

GRANT EXECUTE ON PROCEDURE WSProc_SelectUserAccountOnAuth TO 'wss_website'@'%';
#REVOKE EXECUTE ON PROCEDURE WSProc_SelectUserAccountOnAuth FROM 'web'@'%';

GRANT EXECUTE ON PROCEDURE WSProc_GenerateTokenOnAuth TO 'wss_website'@'%';
#REVOKE EXECUTE ON PROCEDURE WSProc_GenerateTokenOnAuth FROM 'web'@'%';

GRANT EXECUTE ON PROCEDURE WSProc_InsertUserAccount TO 'wss_website'@'%';
#REVOKE EXECUTE ON PROCEDURE WSProc_InsertUserAccount FROM 'web'@'%';

GRANT EXECUTE ON PROCEDURE WSProc_GenerateRecoveryToken TO 'wss_website'@'%';
#REVOKE EXECUTE ON PROCEDURE WSProc_InsertUserAccount FROM 'web'@'%';

GRANT EXECUTE ON PROCEDURE WSProc_ConsumeRecoveryToken TO 'wss_website'@'%';
#REVOKE EXECUTE ON PROCEDURE WSProc_InsertUserAccount FROM 'web'@'%';

GRANT EXECUTE ON PROCEDURE WSProc_ConsumeToken TO 'server'@'%';
#REVOKE EXECUTE ON PROCEDURE WSProc_ConsumeToken FROM 'server'@'%';
