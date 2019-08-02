#SELECT * FROM virtualworld1.useraccountrecovery WHERE creationDate BETWEEN DATE_SUB(NOW(), INTERVAL 2 HOUR) AND NOW();

#SELECT * FROM virtualworld1.activesessions WHERE dateOfCreation BETWEEN DATE_SUB(NOW(), INTERVAL 49 MINUTE) AND NOW();
#SELECT * FROM virtualworld1.AcTiVeSeSsIoNs;
INSERT INTO virtualworld1.activesessions VALUES(NULL, 1, "asdasdasd", NOW(), 1);

UPDATE virtualworld1.activesessions SET active=0 WHERE active=1 AND dateOfCreation < NOW() - INTERVAL 10 SECOND;

SELECT * FROM virtualworld1.AcTiVeSeSsIoNs;