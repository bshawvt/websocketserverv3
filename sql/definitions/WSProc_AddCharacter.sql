CREATE DEFINER=`root`@`localhost` PROCEDURE `WSProc_AddCharacter`(IN userId BIGINT)
BEGIN
	INSERT INTO characters (character_owner)
		SELECT userId;
	SELECT 1;
END