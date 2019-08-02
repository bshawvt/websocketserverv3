create database if not exists wss_debug;
use wss_debug;


drop table if exists activesessions;
drop table if exists useraccountrecovery;
drop table if exists useraccounts;
#drop table if exists serverstatus;
#drop table if exists characters;
#drop table if exists items;
#drop table if exists actions;

CREATE TABLE IF NOT EXISTS useraccounts (
	id BIGINT(20) NOT NULL AUTO_INCREMENT, # primary key
	username VARCHAR(18) NOT NULL, # username
	combinedHash BLOB NOT NULL, # hash
	salt BLOB NOT NULL, # a random salt
    email VARCHAR(128) NOT NULL, # email 
    permission INT DEFAULT 0, # permissions given to this
    locked BOOL DEFAULT FALSE,
	dateOfCreation DATETIME DEFAULT NOW(), # account creation date
    lastLoginDate DATETIME NULL, # date of last login
    #lastLoginAttempt DATETIME NULL, # date of any login attempt, successful or not
    #failedLogins BIGINT(20) NULL, # reset on successful login
    #lastLoginAddress VARCHAR(64) NULL, # last known ip
	PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS useraccountrecovery (
	id BIGINT(20) NOT NULL AUTO_INCREMENT,
	token BLOB NOT NULL, 
    email VARCHAR(128),
    active BOOL DEFAULT TRUE,
    dateOfCreation DATETIME DEFAULT NOW(),
    
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS activesessions (
	id BIGINT(20) NOT NULL AUTO_INCREMENT,
    useraccountid BIGINT(20) NOT NULL, # useraccounts.id
    token BLOB NOT NULL, # generated on login
    dateOfCreation DATETIME DEFAULT NOW(),
    
    active BOOL DEFAULT TRUE,
    
    PRIMARY KEY (id),
    FOREIGN KEY (useraccountid) REFERENCES useraccounts(id)
);

