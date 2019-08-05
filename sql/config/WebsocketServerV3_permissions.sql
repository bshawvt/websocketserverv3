# grant user access to our stored functions and procedures
GRANT EXECUTE ON FUNCTION wss_debug.WSService_MakeHash TO 'web'@'%';
REVOKE EXECUTE ON FUNCTION wss_debug.WSService_MakeHash FROM 'web'@'%';

GRANT EXECUTE ON PROCEDURE wss_debug.WSProc_SelectUserAccountOnAuth TO 'web'@'%';
REVOKE EXECUTE ON PROCEDURE wss_debug.WSProc_SelectUserAccountOnAuth FROM 'web'@'%';