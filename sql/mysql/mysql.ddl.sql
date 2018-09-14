
drop table ctstable2 !
drop table ctstable1 !

CREATE TABLE ctstable1 (  `TYPE_ID` int(11) NOT NULL default '0',  `TYPE_DESC` varchar(32) default NULL,  PRIMARY KEY  (`TYPE_ID`)) ENGINE=InnoDB DEFAULT CHARSET=latin1 !

CREATE TABLE ctstable2 (  `KEY_ID` int(11) NOT NULL default '0',  `COF_NAME` varchar(32) default NULL,  `PRICE` float default NULL,  `TYPE_ID` int(11) default NULL,  PRIMARY KEY  (`KEY_ID`),  KEY `TYPE_ID` (`TYPE_ID`),  CONSTRAINT `0_52` FOREIGN KEY (`TYPE_ID`) REFERENCES `ctstable1` (`TYPE_ID`)) ENGINE=InnoDB DEFAULT CHARSET=latin1 !

drop table concurrencetable !
CREATE TABLE concurrencetable (  `TYPE_ID` int(11) NOT NULL default '0',  `TYPE_DESC` varchar(32) default NULL,  PRIMARY KEY  (`TYPE_ID`)) ENGINE=InnoDB DEFAULT CHARSET=latin1 !

drop table Numeric_Tab !
CREATE TABLE Numeric_Tab (  `MAX_VAL` decimal(30,15) default NULL,  `MIN_VAL` decimal(30,15) default NULL,  `NULL_VAL` decimal(30,15) default NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1 !


drop table Decimal_Tab !
create table Decimal_Tab (MAX_VAL DECIMAL(30,15),MIN_VAL DECIMAL(30,15), NULL_VAL DECIMAL(30,15) NULL)  ENGINE=InnoDB DEFAULT CHARSET=latin1 !

drop table Double_Tab !
CREATE TABLE Double_Tab (  `MAX_VAL` double default NULL,  `MIN_VAL` double default NULL,  `NULL_VAL` double default NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1 !

drop table Float_Tab !
CREATE TABLE Float_Tab (  `MAX_VAL` double default NULL,  `MIN_VAL` double default NULL,  `NULL_VAL` double default NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1 !


drop table Real_Tab !
CREATE TABLE Real_Tab (  `MAX_VAL` double default NULL,  `MIN_VAL` double default NULL,  `NULL_VAL` double default NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1 !


drop table Bit_Tab !
CREATE TABLE Bit_Tab (  `MAX_VAL` BIT default NULL,  `MIN_VAL` BIT default NULL,  `NULL_VAL` BIT default NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1 !


drop table Smallint_Tab !
CREATE TABLE Smallint_Tab (  `MAX_VAL` smallint(6) default NULL,  `MIN_VAL` smallint(6) default NULL,  `NULL_VAL` smallint(6) default NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1 !


drop table Tinyint_Tab !
CREATE TABLE Tinyint_Tab (  `MAX_VAL` tinyint(4) default NULL,  `MIN_VAL` tinyint(4) default NULL,  `NULL_VAL` tinyint(4) default NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1 !


drop table Integer_Tab !
CREATE TABLE Integer_Tab (  `MAX_VAL` int(11) default NULL,  `MIN_VAL` int(11) default NULL,  `NULL_VAL` int(11) default NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1 !


drop table Bigint_Tab !
CREATE TABLE Bigint_Tab (  `MAX_VAL` bigint(19) default NULL,  `MIN_VAL` bigint(19) default NULL,  `NULL_VAL` bigint(19) default NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1 !


drop table Char_Tab !
create table Char_Tab (COFFEE_NAME CHAR(30), NULL_VAL CHAR(30) NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1 !

drop table Varchar_Tab !
create table Varchar_Tab (COFFEE_NAME VARCHAR(30), NULL_VAL VARCHAR(30) NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1 !

drop table Longvarchar_Tab !
CREATE TABLE Longvarchar_Tab (  `COFFEE_NAME` mediumtext) ENGINE=InnoDB DEFAULT CHARSET=latin1 !


drop table Longvarcharnull_Tab !
CREATE TABLE Longvarcharnull_Tab (  `NULL_VAL` mediumtext) ENGINE=InnoDB DEFAULT CHARSET=latin1 !


drop table Date_Tab !
CREATE TABLE Date_Tab (  `MFG_DATE` date default NULL,  `NULL_VAL` date default NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1 !


drop table Time_Tab !
CREATE TABLE Time_Tab (  `BRK_TIME` time default NULL,  `NULL_VAL` time default NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1 !


drop table Timestamp_Tab !
CREATE TABLE Timestamp_Tab (  `IN_TIME` datetime default NULL,  `NULL_VAL` datetime default NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1 !


drop table Binary_Tab !
CREATE TABLE Binary_Tab (  `BINARY_VAL` blob) ENGINE=InnoDB DEFAULT CHARSET=latin1 !


drop table Varbinary_Tab !
CREATE TABLE Varbinary_Tab (  `VARBINARY_VAL` blob) ENGINE=InnoDB DEFAULT CHARSET=latin1 !


drop table Longvarbinary_Tab !
CREATE TABLE Longvarbinary_Tab (  `LONGVARBINARY_VAL` longblob) ENGINE=InnoDB DEFAULT CHARSET=latin1 !


drop table ctstable3 !
CREATE TABLE ctstable3 (  `STRING1` varchar(20) default NULL,  `STRING2` varchar(20) default NULL,  `STRING3` varchar(20) default NULL,  `NUMCOL` decimal(10,0) default NULL,  `FLOATCOL` float default NULL,  `DATECOL` date default NULL,  `TIMECOL` time default NULL,  `TSCOL1` datetime default NULL,  `TSCOL2` datetime default NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1 !


drop table ctstable4 !
CREATE TABLE ctstable4 (  `STRING4` varchar(20) default NULL,  `NUMCOL` decimal(10,0) default NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1 !



drop table TxBean_Tab1 !
CREATE TABLE TxBean_Tab1 (  `KEY_ID` int(11) default NULL,  `TABONE_NAME` varchar(32) default NULL,  `PRICE` float default NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1 !


drop table TxBean_Tab2 !
CREATE TABLE TxBean_Tab2 (  `KEY_ID` int(11) default NULL,  `TABTWO_NAME` varchar(32) default NULL,  `PRICE` float default NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1 !

drop table TxEBean_Tab !
CREATE TABLE TxEBean_Tab (  `KEY_ID` int(11) NOT NULL default '0',  `BRAND_NAME` varchar(32) default NULL,  `PRICE` float default NULL,  PRIMARY KEY  (`KEY_ID`)) ENGINE=InnoDB DEFAULT CHARSET=latin1 !


drop table Integration_Tab !
CREATE TABLE Integration_Tab (  `ACCOUNT` int(11) NOT NULL default '0',  `BALANCE` double default NULL,  PRIMARY KEY  (`ACCOUNT`)) ENGINE=InnoDB DEFAULT CHARSET=latin1 !


drop table Integration_Sec_Tab !
CREATE TABLE Integration_Sec_Tab (  `LOG_NO` int(11) default NULL,  `LINE_NO` int(11) default NULL, `MESSAGE` varchar(255) default NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1 !


drop table BB_Tab !
CREATE TABLE BB_Tab (  `KEY_ID` int(11) NOT NULL default '0',  `BRAND_NAME` varchar(32) default NULL,  `PRICE` float default NULL,  PRIMARY KEY  (`KEY_ID`)) ENGINE=InnoDB DEFAULT CHARSET=latin1 !


drop table JTA_Tab1 !
CREATE TABLE JTA_Tab1 (  `KEY_ID` int(11) default NULL,  `COF_NAME` varchar(32) default NULL,  `PRICE` float default NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1 !


drop table JTA_Tab2 !
CREATE TABLE JTA_Tab2 (  `KEY_ID` int(11) default NULL,  `CHOC_NAME` varchar(32) default NULL,  `PRICE` float default NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1 !


drop table Deploy_Tab1 !
CREATE TABLE Deploy_Tab1 (  `KEY_ID` int(11) NOT NULL default '0',  `BRAND_NAME` varchar(32) default NULL,  `PRICE` float default NULL,  PRIMARY KEY  (`KEY_ID`)) ENGINE=InnoDB DEFAULT CHARSET=latin1 !

drop table Deploy_Tab2 !
CREATE TABLE Deploy_Tab2 (  `KEY_ID` varchar(100) NOT NULL default '',  `BRAND_NAME` varchar(32) default NULL,  `PRICE` float default NULL,  PRIMARY KEY  (`KEY_ID`)) ENGINE=InnoDB DEFAULT CHARSET=latin1 !


drop table Deploy_Tab3 !
CREATE TABLE Deploy_Tab3 (  `KEY_ID` bigint(19) NOT NULL default '0',  `BRAND_NAME` varchar(32) default NULL,  `PRICE` float default NULL,  PRIMARY KEY  (`KEY_ID`)) ENGINE=InnoDB DEFAULT CHARSET=latin1 !


drop table Deploy_Tab4 !
CREATE TABLE Deploy_Tab4 (  `KEY_ID` float NOT NULL default '0',  `BRAND_NAME` varchar(32) default NULL,  `PRICE` float default NULL,  PRIMARY KEY  (`KEY_ID`)) ENGINE=InnoDB DEFAULT CHARSET=latin1 !


drop table Deploy_Tab5 !
CREATE TABLE Deploy_Tab5 (  `KEY_ID1` int(11) NOT NULL default '0',  `KEY_ID2` varchar(100) NOT NULL default '',  `KEY_ID3` float NOT NULL default '0',  `BRAND_NAME` varchar(32) default NULL,  `PRICE` float default NULL,  PRIMARY KEY  (`KEY_ID1`,`KEY_ID2`,`KEY_ID3`)) ENGINE=InnoDB DEFAULT CHARSET=latin1  !


drop table Xa_Tab1 !
CREATE TABLE Xa_Tab1 (  `col1` int(11) NOT NULL default '0',  `col2` varchar(32) default NULL,  `col3` varchar(32) default NULL,  PRIMARY KEY  (`col1`)) ENGINE=InnoDB DEFAULT CHARSET=latin1 !


drop table Xa_Tab2 !
CREATE TABLE Xa_Tab2 (  `col1` int(11) NOT NULL default '0',  `col2` varchar(32) default NULL,  `col3` varchar(32) default NULL,  PRIMARY KEY  (`col1`)) ENGINE=InnoDB DEFAULT CHARSET=latin1 !


drop table SEC_Tab1 !
CREATE TABLE SEC_Tab1 (  `KEY_ID` int(11) NOT NULL default '0',  `PRICE` float default NULL,  `BRAND` varchar(32) default NULL,  PRIMARY KEY  (`KEY_ID`)) ENGINE=InnoDB DEFAULT CHARSET=latin1 !


drop table Connector_Tab !
CREATE TABLE Connector_Tab (  `KEY_ID` int(11) default NULL,  `PRODUCT_NAME` varchar(32) default NULL,  `PRICE` float default NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1 !


drop table Coffee_Table !
create table Coffee_Table (KEY_ID INTEGER NOT NULL, BRAND_NAME varchar(32) NOT NULL, PRICE float NOT NULL, primary key(KEY_ID)) !

drop table Coffee_StringPK_Table !
create table Coffee_StringPK_Table (KEY_ID VARCHAR(100) NOT NULL, BRAND_NAME varchar(32) NOT NULL, PRICE float NOT NULL, primary key(KEY_ID)) !

drop table Coffee_LongPK_Table !
create table Coffee_LongPK_Table (KEY_ID NUMERIC(38) NOT NULL, BRAND_NAME varchar(32) NOT NULL, PRICE float NOT NULL, primary key(KEY_ID)) !

drop table Coffee_FloatPK_Table !
create table Coffee_FloatPK_Table (KEY_ID FLOAT(3,1) NOT NULL, BRAND_NAME varchar(32) NOT NULL, PRICE float NOT NULL, primary key(KEY_ID)) !

drop table Coffee_CompoundPK_Table !
create table Coffee_CompoundPK_Table (KEY_ID1 int NOT NULL, KEY_ID2 varchar(100) NOT NULL, KEY_ID3 float(3,1) NOT NULL, BRAND_NAME varchar(32) NOT NULL, PRICE float NOT NULL, primary key(KEY_ID1, KEY_ID2, KEY_ID3)) !

DROP TABLE COFFEEEJBLITE !
CREATE TABLE COFFEEEJBLITE (ID INTEGER PRIMARY KEY NOT NULL, BRANDNAME VARCHAR(25), PRICE REAL ) ENGINE=innoDB DEFAULT CHARSET=latin1!

DROP TABLE EJB_AUTOCLOSE_TAB !
CREATE TABLE EJB_AUTOCLOSE_TAB (NAME VARCHAR(25) NOT NULL, MESSAGE VARCHAR(25) NOT NULL)!

DROP TABLE caller !
DROP TABLE caller_groups !

CREATE TABLE caller(name VARCHAR(64) PRIMARY KEY, password VARCHAR(1024)) !
CREATE TABLE caller_groups(caller_name VARCHAR(64), group_name VARCHAR(64)) !

INSERT INTO caller VALUES('tom', 'secret1') !
INSERT INTO caller VALUES('emma', 'secret2') !
INSERT INTO caller VALUES('bob', 'secret3') !

INSERT INTO caller_groups VALUES('tom', 'Administrator') !
INSERT INTO caller_groups VALUES('tom', 'Manager') !

INSERT INTO caller_groups VALUES('emma', 'Administrator') !
INSERT INTO caller_groups VALUES('emma', 'Employee') !

INSERT INTO caller_groups VALUES('bob', 'Administrator') !

 INSERT INTO caller VALUES('tom_hash512_saltsize16', 'PBKDF2WithHmacSHA512:1024:DbjXqT9p8VhJ7OtU6DrqDw==:p/qihG8IZKkz03JzKd6XXA==') !
 INSERT INTO caller VALUES('tom_hash256_saltsize32', 'PBKDF2WithHmacSHA256:2048:suVayUIJMQMc6wCgckvAIgKRlo1UkxyFXhXbTxX6C7s=:cvdHkBXVUCN2WL3LRAYodeCdNZxEM4RLlNCCYP68Kmg=') !
 INSERT INTO caller VALUES('tom_hash512_saltsize32', 'PBKDF2WithHmacSHA512:2048:dPTjUfiklfyg2bas/KOQKqEfdtoXK8YvbBscIxA8tNg=:ixBg0wr3ySBI86y8HP7+Yw==') !

 INSERT INTO caller_groups VALUES('tom_hash512_saltsize16', 'Administrator') !
 INSERT INTO caller_groups VALUES('tom_hash512_saltsize16', 'Manager') !

 INSERT INTO caller_groups VALUES('tom_hash256_saltsize32', 'Administrator') !
 INSERT INTO caller_groups VALUES('tom_hash256_saltsize32', 'Manager') !

 INSERT INTO caller_groups VALUES('tom_hash512_saltsize32', 'Administrator') !
 INSERT INTO caller_groups VALUES('tom_hash512_saltsize32', 'Manager') !

