drop table ctstable2 ;
drop table ctstable1 ;
create table ctstable1 (TYPE_ID int, TYPE_DESC varchar(32), primary key(TYPE_ID)) ;
create table ctstable2 (KEY_ID int, COF_NAME varchar(32), PRICE float, TYPE_ID int, primary key(KEY_ID), foreign key(TYPE_ID) references ctstable1) ;

drop table Numeric_Tab ;
create table Numeric_Tab (MAX_VAL NUMERIC(30,15), MIN_VAL NUMERIC(30,15), NULL_VAL NUMERIC(30,15) NULL) ;

drop table Decimal_Tab ;
 create table Decimal_Tab (MAX_VAL DECIMAL(30,15),MIN_VAL DECIMAL(30,15), NULL_VAL DECIMAL(30,15) NULL) ;

drop table Double_Tab ;
create table Double_Tab (MAX_VAL DOUBLE PRECISION, MIN_VAL DOUBLE PRECISION, NULL_VAL DOUBLE PRECISION NULL) ;

drop table Float_Tab ;
create table Float_Tab (MAX_VAL FLOAT, MIN_VAL FLOAT, NULL_VAL FLOAT NULL) ;

drop table Real_Tab ;
create table Real_Tab (MAX_VAL REAL, MIN_VAL REAL,NULL_VAL REAL NULL) ;

drop table Bit_Tab ;
create table Bit_Tab (MAX_VAL SMALLINT, MIN_VAL SMALLINT, NULL_VAL SMALLINT NULL) ;

drop table Smallint_Tab ;
create table Smallint_Tab (MAX_VAL SMALLINT, MIN_VAL SMALLINT, NULL_VAL SMALLINT NULL) ;

drop table Tinyint_Tab ;
create table Tinyint_Tab (MAX_VAL SMALLINT, MIN_VAL SMALLINT, NULL_VAL SMALLINT NULL) ;

drop table Integer_Tab ;
create table Integer_Tab (MAX_VAL INTEGER, MIN_VAL INTEGER, NULL_VAL INTEGER NULL) ;

drop table Bigint_Tab ;
create table Bigint_Tab (MAX_VAL BIGINT, MIN_VAL BIGINT, NULL_VAL BIGINT NULL) ;

drop table Char_Tab ;
create table Char_Tab (COFFEE_NAME CHAR(30), NULL_VAL CHAR(30) NULL) ;

drop table Varchar_Tab ;
create table Varchar_Tab (COFFEE_NAME VARCHAR(30), NULL_VAL VARCHAR(30) NULL) ;

drop table Longvarchar_Tab ;
create table Longvarchar_Tab (COFFEE_NAME VARCHAR(255)) ;

drop table Longvarcharnull_Tab ;
create table Longvarcharnull_Tab (NULL_VAL VARCHAR(255) NULL) ;

drop table Date_Tab ;
create table Date_Tab (MFG_DATE DATE, NULL_VAL DATE NULL) ;

drop table Time_Tab ;
create table Time_Tab (BRK_TIME TIME, NULL_VAL TIME NULL) ;

drop table Timestamp_Tab ;
create table Timestamp_Tab (IN_TIME TIMESTAMP, NULL_VAL TIMESTAMP NULL) ;

drop table Binary_Tab ;
create table Binary_Tab (BINARY_VAL LONGVARBINARY(240000) NULL) ;

drop table Varbinary_Tab ;
create table Varbinary_Tab (VARBINARY_VAL LONGVARBINARY(48000) NULL) ;

drop table Longvarbinary_Tab ;
create table Longvarbinary_Tab (LONGVARBINARY_VAL LONGVARBINARY(20000) NULL) ;

DROP TABLE ctstable3 ;
CREATE TABLE ctstable3(STRING1 VARCHAR(20), STRING2 VARCHAR(20), STRING3 VARCHAR(20), NUMCOL NUMERIC, FLOATCOL FLOAT, DATECOL DATE, TIMECOL TIME, TSCOL1 TIMESTAMP, TSCOL2 TIMESTAMP) ;

DROP TABLE ctstable4 ;
CREATE TABLE ctstable4(STRING4 VARCHAR(20), NUMCOL NUMERIC) ;

drop table TxBean_Tab1 ;
create table TxBean_Tab1 (KEY_ID int, TABONE_NAME varchar(32), PRICE float) ;

drop table TxBean_Tab2 ;
create table TxBean_Tab2 (KEY_ID int, TABTWO_NAME varchar(32), PRICE float) ;

drop table TxEBean_Tab ;
create table TxEBean_Tab (KEY_ID INTEGER NOT NULL, BRAND_NAME VARCHAR(32), PRICE FLOAT, primary key(KEY_ID)) ;


drop table Integration_Tab ;
create table Integration_Tab (ACCOUNT INTEGER NOT NULL, BALANCE FLOAT, primary key(ACCOUNT)) ;

drop table Integration_Sec_Tab ;
create table Integration_Sec_Tab (LOG_NO INTEGER, LINE_NO INTEGER, MESSAGE varchar(255)) ;

drop table BB_Tab ;
create table BB_Tab (KEY_ID INTEGER NOT NULL, BRAND_NAME varchar(32), PRICE float, primary key(KEY_ID)) ;

drop table JTA_Tab1 ;
create table JTA_Tab1 (KEY_ID int, COF_NAME varchar(32), PRICE float) ;

drop table JTA_Tab2 ;
create table JTA_Tab2 (KEY_ID int, CHOC_NAME varchar(32), PRICE float) ;

drop table Deploy_Tab1 ;
create table Deploy_Tab1 (KEY_ID INTEGER NOT NULL, BRAND_NAME varchar(32), PRICE float, primary key(KEY_ID)) ;

drop table Deploy_Tab2 ;
create table Deploy_Tab2 (KEY_ID VARCHAR(100) NOT NULL, BRAND_NAME varchar(32), PRICE float, primary key(KEY_ID)) ;

drop table Deploy_Tab3 ;
create table Deploy_Tab3 (KEY_ID INTEGER NOT NULL, BRAND_NAME varchar(32), PRICE float, primary key(KEY_ID)) ;

drop table Deploy_Tab4 ;
create table Deploy_Tab4 (KEY_ID FLOAT NOT NULL, BRAND_NAME varchar(32), PRICE float, primary key(KEY_ID)) ;

drop table Deploy_Tab5 ;
create table Deploy_Tab5 (KEY_ID1 int NOT NULL, KEY_ID2 varchar(100) NOT NULL, KEY_ID3 float NOT NULL, BRAND_NAME varchar(32), PRICE float, primary key(KEY_ID1, KEY_ID2, KEY_ID3)) ;

drop table Xa_Tab1 ;
create table Xa_Tab1 (col1 varchar(32), col2 varchar(32), col3 varchar(32)) ;

drop table Xa_Tab2 ;
create table Xa_Tab2 (col1 varchar(32), col2 varchar(32), col3 varchar(32)) ;

drop table SEC_Tab1 ;
create table SEC_Tab1 (KEY_ID INTEGER NOT NULL, PRICE float, BRAND varchar(32), primary key(KEY_ID)) ;

drop table Connector_Tab ;
create table Connector_Tab (KEY_ID int, PRODUCT_NAME varchar(32), PRICE float) ;

drop table Coffee_Table ;
create table Coffee_Table (KEY_ID INTEGER NOT NULL, BRAND_NAME varchar(32) NOT NULL, PRICE float NOT NULL, primary key(KEY_ID)) ;

drop table Coffee_StringPK_Table ;
create table Coffee_StringPK_Table (KEY_ID VARCHAR(100) NOT NULL, BRAND_NAME varchar(32) NOT NULL, PRICE float NOT NULL, primary key(KEY_ID)) ;

drop table Coffee_LongPK_Table ;
create table Coffee_LongPK_Table (KEY_ID NUMBER(38) NOT NULL, BRAND_NAME varchar(32) NOT NULL, PRICE float NOT NULL, primary key(KEY_ID)) ;

drop table Coffee_FloatPK_Table ;
create table Coffee_FloatPK_Table (KEY_ID FLOAT NOT NULL, BRAND_NAME varchar(32) NOT NULL, PRICE float NOT NULL, primary key(KEY_ID)) ;

drop table Coffee_CompoundPK_Table ;
create table Coffee_CompoundPK_Table (KEY_ID1 int NOT NULL, KEY_ID2 varchar(100) NOT NULL, KEY_ID3 float NOT NULL, BRAND_NAME varchar(32) NOT NULL, PRICE float NOT NULL, primary key(KEY_ID1, KEY_ID2, KEY_ID3)) ;

DROP TABLE COFFEEEJBLITE cascade;
CREATE TABLE COFFEEEJBLITE (ID INTEGER PRIMARY KEY NOT NULL, BRANDNAME VARCHAR(25), PRICE REAL);

DROP TABLE EJB_AUTOCLOSE_TAB ;
CREATE TABLE EJB_AUTOCLOSE_TAB (NAME VARCHAR(25) NOT NULL, MESSAGE VARCHAR(25) NOT NULL);
