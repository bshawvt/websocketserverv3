create database if not exists wss_debug;
use wss_debug;

SET FOREIGN_KEY_CHECKS = 0;

#drop table if exists useraccounts;
drop table if exists characters;
#drop table if exists useraccountrecovery;

#drop table if exists serverstatus;
#drop table if exists characters;
#drop table if exists items;
#drop table if exists actions;

#drop table if exists sessions;

SET FOREIGN_KEY_CHECKS = 1;







/*

drop table if exists forums;
drop table if exists threads;
drop table if exists contents;
SET FOREIGN_KEY_CHECKS = 1;
# 
CREATE TABLE IF NOT EXISTS forum (
	id BIGINT(20) NOT NULL AUTO_INCREMENT,
    name VARCHAR(64),
    description VARCHAR(128),
    
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS thread (
	id BIGINT(20) NOT NULL AUTO_INCREMENT,
    userId BIGINT(20) NOT NULL, # useraccount id
    PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS content (
	id BIGINT(20) NOT NULL AUTO_INCREMENT,
    threadId BIGINT(20) NOT NULL,
    content BLOB NULL,
    
    PRIMARY KEY (id)
);

*/

CREATE TABLE IF NOT EXISTS characters (
	character_id BIGINT(20) NOT NULL AUTO_INCREMENT,
    character_owner BIGINT(20) NOT NULL,
    
    x DOUBLE DEFAULT 0.0,
    y DOUBLE DEFAULT 0.0,
    z DOUBLE DEFAULT 0.0,
    
    character_name VARCHAR(24) NULL,
    character_description VARCHAR(128) NULL,
    
    level INT DEFAULT 0,
    levelPoints INT DEFAULT 0,
    
    safeExperience BIGINT(20) DEFAULT 0,
    unsafeExperience BIGINT(20) DEFAULT 0,
	
    health_max INT DEFAULT 10,
    health_current INT DEFAULT 10,
    
    stamina_max INT DEFAULT 10,
    stamina_current INT DEFAULT 10,
    
    hunger_current INT DEFAULT 10,
    
    characterCreationDate DATETIME DEFAULT NOW(),
    
    FOREIGN KEY (character_owner) REFERENCES useraccounts(user_id),
    
    PRIMARY KEY (character_id)
);

# 
CREATE TABLE IF NOT EXISTS useraccounts (
	user_id BIGINT(20) NOT NULL AUTO_INCREMENT, # primary key
	username VARCHAR(18) NOT NULL,
	combinedHash BLOB NOT NULL, # result of user input and salt
	salt BLOB NOT NULL, # a random series of bytes
    email VARCHAR(128) DEFAULT NULL, # 
    permission INT DEFAULT 0,
    locked BOOL DEFAULT FALSE, 
	dateOfCreation DATETIME DEFAULT NOW(),
    lastLoginDate DATETIME NULL, # set on token generation and consumption
	
    PRIMARY KEY (user_id),
    UNIQUE KEY (username),
    UNIQUE KEY (email)
);

CREATE TABLE IF NOT EXISTS useraccountrecovery (
	recovery_id BIGINT(20) NOT NULL AUTO_INCREMENT,
    owner_id BIGINT(20) NOT NULL,
	token BLOB NOT NULL, 
    active BOOL DEFAULT TRUE,
    
    recoveryExpirationDate DATETIME NULL,
    
    FOREIGN KEY (owner_id) REFERENCES useraccounts(user_id),
    
    PRIMARY KEY (recovery_id)
);

/*CREATE TABLE IF NOT EXISTS characters(
	character_id BIGINT(20) NOT NULL AUTO_INCREMENT,
    owner_id BIGINT(20) NOT NULL,
    name VARCHAR(16) NULL,
    characterCreationDate DATETIME DEFAULT NOW(),
    
    FOREIGN KEY (owner_id) REFERENCES useraccounts(user_id),
    PRIMARY KEY(character_id)
);*/

CREATE TABLE IF NOT EXISTS sessions (
	session_id BIGINT(20) NOT NULL AUTO_INCREMENT,
	session_owner BIGINT(20) NOT NULL,
    
    session_token VARCHAR(128) DEFAULT NULL,
    session_active BOOL DEFAULT TRUE,
    session_expirationDate DATETIME NOT NULL,
    
    session_createdBy BLOB DEFAULT NULL, 
    session_consumedBy BLOB DEFAULT NULL,
    
    FOREIGN KEY (session_owner) REFERENCES useraccounts(user_id),
    
    PRIMARY KEY(session_id),
    UNIQUE KEY (session_token)
);
