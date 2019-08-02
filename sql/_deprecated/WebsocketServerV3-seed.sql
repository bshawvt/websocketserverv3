create database if not exists wss_debug;
use wss_debug;

drop table if exists useraccounts;
drop table if exists useraccountrecovery;
drop table if exists activesessions;
#drop table if exists serverstatus;
#drop table if exists characters;
#drop table if exists items;
#drop table if exists actions;

CREATE TABLE IF NOT EXISTS useraccounts (
	id BIGINT(20) NOT NULL AUTO_INCREMENT, # primary key
	username VARCHAR(64) NOT NULL, # username
	combinedHash BLOB NOT NULL, # hash
	salt BLOB NOT NULL, # a random salt
    email VARCHAR(128) NOT NULL, # email 
    permission INT NOT NULL, # permissions given to this
    locked BOOL NOT NULL,
	dateOfCreation DATETIME NOT NULL, # account creation date
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
    active BOOL NOT NULL,
    creationDate DATETIME NOT NULL,
    
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS activesessions (
	id BIGINT(20) NOT NULL AUTO_INCREMENT,
    useraccountid BIGINT(20) NOT NULL, # useraccounts.id
    token BLOB NOT NULL, # generated on login
    dateOfCreation DATETIME NOT NULL,
    
    active BOOL NOT NULL,
    
    PRIMARY KEY (id),
    FOREIGN KEY (useraccountid) REFERENCES useraccounts(id)
);


# todo
CREATE TABLE IF NOT EXISTS characters (

	id BIGINT(20) NOT NULL AUTO_INCREMENT,
    dateOfCreation DATETIME NOT NULL,
    owner BIGINT(20) NOT NULL, # useraccounts id that owns this character
	
    # character name
    name VARCHAR(64) NOT NULL,
    description VARCHAR(150) NULL, # a short description for this character
        
    # world position
    #mapId INT NULL, # todo? should be one open world i think
    x DOUBLE NULL,
    y DOUBLE NULL,
    z DOUBLE NULL,
    
    # character progression
    wisdomLevel INT NULL, # 
    wisdomPoints INT NULL, #
    unsafeExperiencePoints BIGINT(20) NULL, # player hasnt used these points, therefore they can be stolen or lost
    safeExperiencePoints BIGINT(20) NULL, # 
    lifetimeExperiencePoints BIGINT(20) NULL, # total = total < unsafe + safe ? unsafe+safe : total;
    
    # character stats
    maxHealth INT NULL, 
    maxStamina INT NULL,    
      
    # 
	PRIMARY KEY (id),
    FOREIGN KEY (owner) REFERENCES useraccounts(id)
    
);

CREATE TABLE IF NOT EXISTS items (
	id BIGINT(20) NOT NULL AUTO_INCREMENT, # primary key
    ownerId BIGINT(20) NULL, # character id
    actionId BIGINT(20) NULL, # action id if applicable
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS actions (
	id BIGINT(20) NOT NULL AUTO_INCREMENT, # primary key
    ownerId BIGINT(20) NULL, # character id or item id if imbued
    imbuedId BIGINT(20) NULL, # item id, null if it belongs to a character
    
    # 
    name VARCHAR(32) NULL, # 
    description VARCHAR(64) NULL, #
    
    # procedural content preparation
    seed1 INT NULL, # 
    seed2 INT NULL, # 
    
    # persistent formula data
    min INT NULL, # 
    max INT NULL, # 
    effectiveness DOUBLE NULL, #
    
    effectId1 INT NULL,
    effectId2 INT NULL,
    effectId3 INT NULL,
    effectId4 INT NULL,
    
    PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS serverstatus (
	id BIGINT(20) NOT NULL AUTO_INCREMENT,
    pingDate DATETIME NOT NULL,
    
    PRIMARY KEY (id)
);