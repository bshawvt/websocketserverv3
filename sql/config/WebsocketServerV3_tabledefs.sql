create database if not exists wss_debug;
use wss_debug;

drop table if exists useraccountrecovery;
drop table if exists useraccounts;

#drop table if exists serverstatus;
#drop table if exists characters;
#drop table if exists items;
#drop table if exists actions;

drop table if exists forum;
drop table if exists thread;
drop table if exists content;

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

# 
CREATE TABLE IF NOT EXISTS useraccounts (
	id BIGINT(20) NOT NULL AUTO_INCREMENT, # primary key
	username VARCHAR(18) NOT NULL,
	combinedHash BLOB NOT NULL, # result of user input and salt
	salt BLOB NOT NULL, # a random series of bytes
    email VARCHAR(128) NOT NULL,
    permission INT DEFAULT 0,
    locked BOOL DEFAULT FALSE, 
	dateOfCreation DATETIME DEFAULT NOW(),
    lastLoginDate DATETIME NULL, # set on token generation and consumption
    sessionIP BLOB NULL, # set on token generation and consumption
    sessionExpirationDate DATETIME NULL, # set on token generation and set to null on consumption
    sessionToken BLOB NULL, # set on token generation and set to null on consumption
	PRIMARY KEY (id),
    UNIQUE KEY (username),
    UNIQUE KEY (email)
);

CREATE TABLE IF NOT EXISTS useraccountrecovery (
	id BIGINT(20) NOT NULL AUTO_INCREMENT,
	token BLOB NOT NULL, 
    email VARCHAR(128),
    active BOOL DEFAULT TRUE,
    dateOfCreation DATETIME DEFAULT NOW(),
    
    PRIMARY KEY (id)
);
