CREATE DEFINER=`root`@`localhost` FUNCTION `ServiceGenerateTokenOnAuth`(pusername VARCHAR(64), phash VARCHAR(128)) RETURNS blob
BEGIN
	#INSERT INTO virtualworld1.activesessions VALUES(NULL, );
    #INSERT INTO virtualworld1.activesessions VALUES(NULL, 1, "asdasdasd", NOW(), 1);
    #SELECT * FROM virtualworld1.activesessions WHERE username=pusername;
    
	DECLARE otoken BLOB DEFAULT SHA2(RANDOM_BYTES(16), 256);
	#INSERT INTO virtualworld1.activesessions VALUES(NULL, 1, otoken, NOW(), 1);
    
    #CREATE DEFINER=`root`@`localhost` FUNCTION `new_function`() RETURNS blob
	#BEGIN
		#RETURNS BLOB;
	#	DECLARE otoken BLOB DEFAULT SHA2(RANDOM_BYTES(16), 256);
	#	INSERT INTO virtualworld1.activesessions VALUES(NULL, 1, otoken, NOW(), 1);
		#RETURN otoken;
	#RETURN otoken;
	#END
RETURN otoken;
END