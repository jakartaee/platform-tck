DROP PROCEDURE IF EXISTS Bigint_In_Max!
CREATE PROCEDURE Bigint_In_Max(MAX_PARAM BIGINT)
begin update Bigint_Tab set MAX_VAL=MAX_PARAM; end
!

DROP PROCEDURE IF EXISTS Bigint_In_Min!
CREATE PROCEDURE Bigint_In_Min(MIN_PARAM BIGINT)
begin update Bigint_Tab set MIN_VAL=MIN_PARAM; end
!

DROP PROCEDURE IF EXISTS Bigint_In_Null!
CREATE PROCEDURE Bigint_In_Null(NULL_PARAM BIGINT)
begin update Bigint_Tab set NULL_VAL=NULL_PARAM; end
!

DROP PROCEDURE IF EXISTS Bigint_Io_Max!
CREATE PROCEDURE Bigint_Io_Max(inout MAX_PARAM BIGINT)
begin update Bigint_Tab set MAX_VAL=MAX_PARAM; select MAX_VAL into MAX_PARAM from Bigint_Tab; end
!

DROP PROCEDURE IF EXISTS Bigint_Io_Min!
CREATE PROCEDURE Bigint_Io_Min(inout MIN_PARAM BIGINT)
begin update Bigint_Tab set MIN_VAL=MIN_PARAM; select MIN_VAL into MIN_PARAM from Bigint_Tab; end
!

DROP PROCEDURE IF EXISTS Bigint_Io_Null!
CREATE PROCEDURE Bigint_Io_Null(inout NULL_PARAM BIGINT)
begin update Bigint_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL into NULL_PARAM from Bigint_Tab; end
!

DROP PROCEDURE IF EXISTS Bigint_Proc!
CREATE PROCEDURE Bigint_Proc(out MAX_PARAM BIGINT, out MIN_PARAM BIGINT, out NULL_PARAM BIGINT)
begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from Bigint_Tab; end
!

DROP PROCEDURE IF EXISTS Binary_Proc!
CREATE PROCEDURE Binary_Proc(out BINARY_PARAM BLOB)
begin select BINARY_VAL into BINARY_PARAM from Binary_Tab; end
!

DROP PROCEDURE IF EXISTS Binary_Proc_In!
CREATE PROCEDURE Binary_Proc_In(BINARY_PARAM BLOB)
begin update Binary_Tab set BINARY_VAL=BINARY_PARAM; end
!

DROP PROCEDURE IF EXISTS Binary_Proc_Io!
CREATE PROCEDURE Binary_Proc_Io(inout BINARY_PARAM BLOB)
begin update Binary_Tab set BINARY_VAL=BINARY_PARAM; select BINARY_VAL into BINARY_PARAM from Binary_Tab; end
!

DROP PROCEDURE IF EXISTS Bit_In_Max!
CREATE PROCEDURE Bit_In_Max(MAX_PARAM TINYINT)
begin update Bit_Tab set MAX_VAL=MAX_PARAM; end
!

DROP PROCEDURE IF EXISTS Bit_In_Min!
CREATE PROCEDURE Bit_In_Min(MIN_PARAM TINYINT)
begin update Bit_Tab set MIN_VAL=MIN_PARAM; end
!

DROP PROCEDURE IF EXISTS Bit_In_Null!
CREATE PROCEDURE Bit_In_Null(NULL_PARAM TINYINT)
begin update Bit_Tab set NULL_VAL=NULL_PARAM; end
!

DROP PROCEDURE IF EXISTS Bit_Io_Max!
CREATE PROCEDURE Bit_Io_Max(inout MAX_PARAM TINYINT)
begin update Bit_Tab set MAX_VAL=MAX_PARAM; select MAX_VAL into MAX_PARAM from Bit_Tab; end
!

DROP PROCEDURE IF EXISTS Bit_Io_Min!
CREATE PROCEDURE Bit_Io_Min(inout MIN_PARAM TINYINT)
begin update Bit_Tab set MIN_VAL=MIN_PARAM; select MIN_VAL into MIN_PARAM from Bit_Tab; end
!

DROP PROCEDURE IF EXISTS Bit_Io_Null!
CREATE PROCEDURE Bit_Io_Null(inout NULL_PARAM TINYINT)
begin update Bit_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL  into NULL_PARAM from Bit_Tab; end
!

DROP PROCEDURE IF EXISTS Bit_Proc!
CREATE PROCEDURE Bit_Proc(out MAX_PARAM TINYINT, out MIN_PARAM TINYINT, out NULL_PARAM TINYINT)
begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from Bit_Tab; end
!

DROP PROCEDURE IF EXISTS Char_In_Name!
CREATE PROCEDURE Char_In_Name(NAME_PARAM CHAR(255))
begin update Char_Tab set COFFEE_NAME=NAME_PARAM; end
!

DROP PROCEDURE IF EXISTS Char_In_Null!
CREATE PROCEDURE Char_In_Null(NULL_PARAM CHAR(255))
begin update Char_Tab set NULL_VAL=NULL_PARAM; end
!

DROP PROCEDURE IF EXISTS Char_Io_Name!
CREATE PROCEDURE Char_Io_Name(inout NAME_PARAM CHAR(255))
begin update Char_Tab set COFFEE_NAME=NAME_PARAM; select COFFEE_NAME into NAME_PARAM from Char_Tab; end
!

DROP PROCEDURE IF EXISTS Char_Io_Null!
CREATE PROCEDURE Char_Io_Null(inout NULL_PARAM VARCHAR(255))
begin update Char_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL  into NULL_PARAM from Char_Tab; end
!

DROP PROCEDURE IF EXISTS Char_Proc!
CREATE PROCEDURE Char_Proc(out NAME_PARAM CHAR(255) ,out NULL_PARAM CHAR(255))
begin select COFFEE_NAME, NULL_VAL  into NAME_PARAM, NULL_PARAM from Char_Tab; end
!

DROP PROCEDURE IF EXISTS Coffee_Proc!
CREATE PROCEDURE Coffee_Proc(TYPE_PARAM TINYINT)
begin update ctstable2 set PRICE=PRICE*2 where TYPE_ID=TYPE_PARAM; delete from ctstable2 where TYPE_ID=TYPE_PARAM-1;end
!

DROP PROCEDURE IF EXISTS Date_In_Mfg!
CREATE PROCEDURE Date_In_Mfg(MFG_PARAM DATE)
begin update Date_Tab set MFG_DATE=MFG_PARAM; end
!

DROP PROCEDURE IF EXISTS Date_In_Null!
CREATE PROCEDURE Date_In_Null(NULL_PARAM DATE)
begin update Date_Tab set NULL_VAL=NULL_PARAM; end
!

DROP PROCEDURE IF EXISTS Date_Io_Mfg!
CREATE PROCEDURE Date_Io_Mfg(inout MFG_PARAM DATE)
begin update Date_Tab set MFG_DATE=MFG_PARAM; select MFG_DATE into MFG_PARAM from Date_Tab; end
!

DROP PROCEDURE IF EXISTS Date_Io_Null!
CREATE PROCEDURE Date_Io_Null(inout NULL_PARAM DATE)
begin update Date_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL  into NULL_PARAM from Date_Tab; end
!

DROP PROCEDURE IF EXISTS Date_Proc!
CREATE PROCEDURE Date_Proc(out MFG_PARAM DATE, out NULL_PARAM DATE)
begin select MFG_DATE, NULL_VAL  into MFG_PARAM, NULL_PARAM from Date_Tab; end
!

DROP PROCEDURE IF EXISTS Decimal_In_Max!
CREATE PROCEDURE Decimal_In_Max(MAX_PARAM DECIMAL(30,15))
begin update Decimal_Tab set MAX_VAL=MAX_PARAM;  end
!

DROP PROCEDURE IF EXISTS Decimal_In_Min!
CREATE PROCEDURE Decimal_In_Min(MIN_PARAM DECIMAL(30,15))
begin update Decimal_Tab set MIN_VAL=MIN_PARAM;  end
!

DROP PROCEDURE IF EXISTS Decimal_In_Null!
CREATE PROCEDURE Decimal_In_Null(NULL_PARAM DECIMAL(30,15))
begin update Decimal_Tab set NULL_VAL=NULL_PARAM;  end
!

DROP PROCEDURE IF EXISTS Decimal_Io_Max!
CREATE PROCEDURE Decimal_Io_Max(inout MAX_PARAM DECIMAL(30,15))
begin update Decimal_Tab set MAX_VAL=MAX_PARAM; select MAX_VAL into MAX_PARAM from Decimal_Tab; end
!

DROP PROCEDURE IF EXISTS Decimal_Io_Min!
CREATE PROCEDURE Decimal_Io_Min(inout MIN_PARAM DECIMAL(30,15))
begin update Decimal_Tab set MIN_VAL=MIN_PARAM; select MIN_VAL  into MIN_PARAM from Decimal_Tab; end
!

DROP PROCEDURE IF EXISTS Decimal_Io_Null!
CREATE PROCEDURE Decimal_Io_Null(inout NULL_PARAM DECIMAL(30,15))
begin update Decimal_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL  into NULL_PARAM from Decimal_Tab; end
!

DROP PROCEDURE IF EXISTS Decimal_Proc!
CREATE PROCEDURE Decimal_Proc(out MAX_PARAM DECIMAL(30,15), out MIN_PARAM DECIMAL(30,15), out NULL_PARAM DECIMAL(30,15))
begin select MAX_VAL,MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from Decimal_Tab; end
!

DROP PROCEDURE IF EXISTS Double_In_Max!
CREATE PROCEDURE Double_In_Max(MAX_PARAM DOUBLE)
begin update Double_Tab set MAX_VAL=MAX_PARAM; end
!

DROP PROCEDURE IF EXISTS Double_In_Min!
CREATE PROCEDURE Double_In_Min(MIN_PARAM DOUBLE)
begin update Double_Tab set MIN_VAL=MIN_PARAM; end
!

DROP PROCEDURE IF EXISTS Double_In_Null!
CREATE PROCEDURE Double_In_Null(NULL_PARAM DOUBLE)
begin update Double_Tab set NULL_VAL=NULL_PARAM; end
!

DROP PROCEDURE IF EXISTS Double_Io_Max!
CREATE PROCEDURE Double_Io_Max(inout MAX_PARAM DOUBLE)
begin update Double_Tab set MAX_VAL=MAX_PARAM; select MAX_VAL into MAX_PARAM from Double_Tab; end
!

DROP PROCEDURE IF EXISTS Double_Io_Min!
CREATE PROCEDURE Double_Io_Min(inout MIN_PARAM DOUBLE)
begin update Double_Tab set MIN_VAL=MIN_PARAM; select MIN_VAL into MIN_PARAM from Double_Tab; end
!

DROP PROCEDURE IF EXISTS Double_Io_Null!
CREATE PROCEDURE Double_Io_Null(inout NULL_PARAM DOUBLE)
begin update Double_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL  into NULL_PARAM from Double_Tab; end
!

DROP PROCEDURE IF EXISTS Double_Proc!
CREATE PROCEDURE Double_Proc(out MAX_PARAM DOUBLE, out MIN_PARAM DOUBLE, out NULL_PARAM DOUBLE)
begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from Double_Tab; end
!

DROP PROCEDURE IF EXISTS Float_In_Max!
CREATE PROCEDURE Float_In_Max(MAX_PARAM DOUBLE)
begin update Float_Tab set MAX_VAL=MAX_PARAM; end
!

DROP PROCEDURE IF EXISTS Float_In_Min!
CREATE PROCEDURE Float_In_Min(MIN_PARAM DOUBLE)
begin update Float_Tab set MIN_VAL=MIN_PARAM; end
!

DROP PROCEDURE IF EXISTS Float_In_Null!
CREATE PROCEDURE Float_In_Null(NULL_PARAM DOUBLE)
begin update Float_Tab set NULL_VAL=NULL_PARAM; end
!

DROP PROCEDURE IF EXISTS Float_Io_Max!
CREATE PROCEDURE Float_Io_Max(inout MAX_PARAM DOUBLE)
begin update Float_Tab set MAX_VAL=MAX_PARAM; select MAX_VAL into MAX_PARAM from Float_Tab; end
!

DROP PROCEDURE IF EXISTS Float_Io_Min!
CREATE PROCEDURE Float_Io_Min(inout MIN_PARAM DOUBLE)
begin update Float_Tab set MIN_VAL=MIN_PARAM; select MIN_VAL into MIN_PARAM from Float_Tab; end
!

DROP PROCEDURE IF EXISTS Float_Io_Null!
CREATE PROCEDURE Float_Io_Null(inout NULL_PARAM DOUBLE)
begin update Float_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL  into NULL_PARAM from Float_Tab; end
!

DROP PROCEDURE IF EXISTS Float_Proc!
CREATE PROCEDURE Float_Proc(out MAX_PARAM DOUBLE, out MIN_PARAM DOUBLE, out NULL_PARAM DOUBLE)
begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from Float_Tab; end
!

DROP PROCEDURE IF EXISTS Integer_InOut_Proc!
CREATE PROCEDURE Integer_InOut_Proc(out INOUT_PARAM INTEGER)
begin select MAX_VAL into INOUT_PARAM from Integer_Tab where MIN_VAL=INOUT_PARAM; end
!

DROP PROCEDURE IF EXISTS Integer_In_Max!
CREATE PROCEDURE Integer_In_Max(MAX_PARAM INTEGER)
begin update Integer_Tab set MAX_VAL=MAX_PARAM; end
!

DROP PROCEDURE IF EXISTS Integer_In_Min!
CREATE PROCEDURE Integer_In_Min(MIN_PARAM INTEGER)
begin update Integer_Tab set MIN_VAL=MIN_PARAM; end
!

DROP PROCEDURE IF EXISTS Integer_In_Null!
CREATE PROCEDURE Integer_In_Null(NULL_PARAM INTEGER)
begin update Integer_Tab set NULL_VAL=NULL_PARAM; end
!

DROP PROCEDURE IF EXISTS Integer_In_Proc!
CREATE PROCEDURE Integer_In_Proc(IN_PARAM INTEGER)
begin update Integer_Tab set MAX_VAL=IN_PARAM; end
!

DROP PROCEDURE IF EXISTS Integer_Io_Max!
CREATE PROCEDURE Integer_Io_Max(inout MAX_PARAM INTEGER)
begin update Integer_Tab set MAX_VAL=MAX_PARAM; select MAX_VAL into MAX_PARAM from Integer_Tab; end
!

DROP PROCEDURE IF EXISTS Integer_Io_Min!
CREATE PROCEDURE Integer_Io_Min(inout MIN_PARAM INTEGER)
begin update Integer_Tab set MIN_VAL=MIN_PARAM; select MIN_VAL into MIN_PARAM from Integer_Tab; end
!

DROP PROCEDURE IF EXISTS Integer_Io_Null!
CREATE PROCEDURE Integer_Io_Null(inout NULL_PARAM INTEGER)
begin update Integer_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL into NULL_PARAM from Integer_Tab; end
!

DROP PROCEDURE IF EXISTS Integer_Proc!
CREATE PROCEDURE Integer_Proc(out MAX_PARAM INTEGER, out MIN_PARAM INTEGER, out NULL_PARAM INTEGER)
begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from Integer_Tab; end
!

DROP PROCEDURE IF EXISTS IOCoffee_Proc!
CREATE PROCEDURE IOCoffee_Proc(out PRICE_PARAM FLOAT)
begin select PRICE*2 into PRICE_PARAM from ctstable2 where PRICE=PRICE_PARAM; end
!

DROP PROCEDURE IF EXISTS Longvarbinary_In!
CREATE PROCEDURE Longvarbinary_In(LONGVARBINARY_PARAM LONGBLOB)
begin update Longvarbinary_Tab set LONGVARBINARY_VAL=LONGVARBINARY_PARAM; end
!

DROP PROCEDURE IF EXISTS Longvarbinary_Io!
CREATE PROCEDURE Longvarbinary_Io(inout LONGVARBINARY_PARAM LONGBLOB)
begin update Longvarbinary_Tab set LONGVARBINARY_VAL=LONGVARBINARY_PARAM; select LONGVARBINARY_VAL into LONGVARBINARY_PARAM from Longvarbinary_Tab; end
!

DROP PROCEDURE IF EXISTS Longvarbinary_Proc!
CREATE PROCEDURE Longvarbinary_Proc(out LONGVARBINARY_PARAM LONGBLOB)
begin select LONGVARBINARY_VAL into LONGVARBINARY_PARAM from Longvarbinary_Tab; end
!

DROP PROCEDURE IF EXISTS Longvarbinary_Proc_In!
CREATE PROCEDURE Longvarbinary_Proc_In(LONGVARBINARY_PARAM LONGBLOB)
begin update Longvarbinary_Tab set LONGVARBINARY_VAL=LONGVARBINARY_PARAM; end
!

DROP PROCEDURE IF EXISTS Longvarcharnull_Proc!
CREATE PROCEDURE Longvarcharnull_Proc(out NULL_PARAM BIGINT)
begin select  NULL_VAL  into  NULL_PARAM from Longvarcharnull_Tab; end
!

DROP PROCEDURE IF EXISTS Longvarchar_In_Name!
CREATE PROCEDURE Longvarchar_In_Name(NAME_PARAM MEDIUMTEXT)
begin update Longvarchar_Tab set COFFEE_NAME=NAME_PARAM; end
!

DROP PROCEDURE IF EXISTS Longvarchar_In_Null!
CREATE PROCEDURE Longvarchar_In_Null(NULL_PARAM MEDIUMTEXT)
begin update Longvarcharnull_Tab set NULL_VAL=NULL_PARAM; end
!

DROP PROCEDURE IF EXISTS Longvarchar_Io_Name!
CREATE PROCEDURE Longvarchar_Io_Name(inout NAME_PARAM BIGINT)
begin update Longvarchar_Tab set COFFEE_NAME=NAME_PARAM; select COFFEE_NAME into NAME_PARAM from Longvarchar_Tab; end
!

DROP PROCEDURE IF EXISTS Longvarchar_Io_Null!
CREATE PROCEDURE Longvarchar_Io_Null(inout NULL_PARAM MEDIUMTEXT)
begin update Longvarcharnull_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL  into NULL_PARAM from Longvarcharnull_Tab; end
!

DROP PROCEDURE IF EXISTS Longvarchar_Proc!
CREATE PROCEDURE Longvarchar_Proc(out NAME_PARAM MEDIUMTEXT)
begin select COFFEE_NAME  into NAME_PARAM from Longvarchar_Tab; end
!

DROP PROCEDURE IF EXISTS Numeric_In_Max!
CREATE PROCEDURE Numeric_In_Max(MAX_PARAM DECIMAL(30,15))
begin update Numeric_Tab set MAX_VAL=MAX_PARAM;  end
!

DROP PROCEDURE IF EXISTS Numeric_In_Min!
CREATE PROCEDURE Numeric_In_Min(MIN_PARAM INT)
begin update Numeric_Tab set MIN_VAL=MIN_PARAM; end
!

DROP PROCEDURE IF EXISTS Numeric_In_Null!
CREATE PROCEDURE Numeric_In_Null(NULL_PARAM DECIMAL(30,15))
begin update Numeric_Tab set NULL_VAL=NULL_PARAM; end
!

DROP PROCEDURE IF EXISTS Numeric_Io_Max!
CREATE PROCEDURE Numeric_Io_Max(inout MAX_PARAM DECIMAL(30,15))
begin update Numeric_Tab set MAX_VAL=MAX_PARAM; select MAX_VAL into MAX_PARAM from Numeric_Tab; end
!

DROP PROCEDURE IF EXISTS Numeric_Io_Min!
CREATE PROCEDURE Numeric_Io_Min(inout MIN_PARAM INT)
begin update Numeric_Tab set MIN_VAL=MIN_PARAM; select MIN_VAL into MIN_PARAM from Numeric_Tab;end
!

DROP PROCEDURE IF EXISTS Numeric_Io_Null!
CREATE PROCEDURE Numeric_Io_Null(inout NULL_PARAM DECIMAL(30,15))
begin update Numeric_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL  into NULL_PARAM from Numeric_Tab;end
!

DROP PROCEDURE IF EXISTS Numeric_Proc!
CREATE PROCEDURE Numeric_Proc(out MAX_PARAM DECIMAL(30,15), out MIN_PARAM decimal(30,15), OUT NULL_PARAM decimal(30,15))
begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from Numeric_Tab; end
!

DROP PROCEDURE IF EXISTS Numeric_Proc2!
CREATE PROCEDURE Numeric_Proc2(out MAX_PARAM DECIMAL(30,15), out MIN_PARAM DECIMAL(30,15), out NULL_PARAM DECIMAL(30,15))
BEGIN  select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from Numeric_Tab; end
!

DROP PROCEDURE IF EXISTS Real_In_Max!
CREATE PROCEDURE Real_In_Max(MAX_PARAM REAL)
begin update Real_Tab set MAX_VAL=MAX_PARAM; end
!

DROP PROCEDURE IF EXISTS Real_In_Min!
CREATE PROCEDURE Real_In_Min(MIN_PARAM REAL)
begin update Real_Tab set MIN_VAL=MIN_PARAM; end
!

DROP PROCEDURE IF EXISTS Real_In_Null!
CREATE PROCEDURE Real_In_Null(NULL_PARAM REAL)
begin update Real_Tab set NULL_VAL=NULL_PARAM; end
!

DROP PROCEDURE IF EXISTS Real_Io_Max!
CREATE PROCEDURE Real_Io_Max(inout MAX_PARAM REAL)
begin update Real_Tab set MAX_VAL=MAX_PARAM; select MAX_VAL into MAX_PARAM from Real_Tab; end
!

DROP PROCEDURE IF EXISTS Real_Io_Min!
CREATE PROCEDURE Real_Io_Min(inout MIN_PARAM REAL)
begin update Real_Tab set MIN_VAL=MIN_PARAM; select MIN_VAL into MIN_PARAM from Real_Tab; end
!

DROP PROCEDURE IF EXISTS Real_Io_Null!
CREATE PROCEDURE Real_Io_Null(inout NULL_PARAM REAL)
begin update Real_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL  into NULL_PARAM from Real_Tab; end
!

DROP PROCEDURE IF EXISTS Real_Proc!
CREATE PROCEDURE Real_Proc(out MAX_PARAM REAL, out MIN_PARAM REAL, out NULL_PARAM REAL)
begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from Real_Tab; end
!

DROP PROCEDURE IF EXISTS SelCoffee_Proc!
CREATE PROCEDURE SelCoffee_Proc(out KEYID_PARAM NUMERIC)
begin select KEY_ID into KEYID_PARAM from ctstable2 where TYPE_ID=1; end
!

DROP PROCEDURE IF EXISTS Smallint_In_Max!
CREATE PROCEDURE Smallint_In_Max(MAX_PARAM SMALLINT)
begin update Smallint_Tab set MAX_VAL=MAX_PARAM; end
!

DROP PROCEDURE IF EXISTS Smallint_In_Min!
CREATE PROCEDURE Smallint_In_Min(MIN_PARAM SMALLINT)
begin update Smallint_Tab set MIN_VAL=MIN_PARAM; end
!

DROP PROCEDURE IF EXISTS Smallint_In_Null!
CREATE PROCEDURE Smallint_In_Null(NULL_PARAM SMALLINT)
begin update Smallint_Tab set NULL_VAL=NULL_PARAM; end
!

DROP PROCEDURE IF EXISTS Smallint_Io_Max!
CREATE PROCEDURE Smallint_Io_Max(inout MAX_PARAM SMALLINT)
begin update Smallint_Tab set MAX_VAL=MAX_PARAM; select MAX_VAL into MAX_PARAM from Smallint_Tab; end
!

DROP PROCEDURE IF EXISTS Smallint_Io_Min!
CREATE PROCEDURE Smallint_Io_Min(inout MIN_PARAM SMALLINT)
begin update Smallint_Tab set MIN_VAL=MIN_PARAM; select MIN_VAL into MIN_PARAM from Smallint_Tab; end
!

DROP PROCEDURE IF EXISTS Smallint_Io_Null!
CREATE PROCEDURE Smallint_Io_Null(inout NULL_PARAM SMALLINT)
begin update Smallint_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL  into NULL_PARAM from Smallint_Tab; end
!

DROP PROCEDURE IF EXISTS Smallint_Proc!
CREATE PROCEDURE Smallint_Proc(out MAX_PARAM SMALLINT, out MIN_PARAM SMALLINT, out NULL_PARAM SMALLINT)
begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from Smallint_Tab; end
!

DROP PROCEDURE IF EXISTS Timestamp_In_Intime!
CREATE PROCEDURE Timestamp_In_Intime(INTIME_PARAM TIMESTAMP)
begin update Timestamp_Tab set IN_TIME=INTIME_PARAM; end
!

DROP PROCEDURE IF EXISTS Timestamp_In_Null!
CREATE PROCEDURE Timestamp_In_Null(NULL_PARAM TIMESTAMP)
begin update Timestamp_Tab set NULL_VAL=NULL_PARAM; end
!

DROP PROCEDURE IF EXISTS Timestamp_Io_Intime!
CREATE PROCEDURE Timestamp_Io_Intime(inout INTIME_PARAM TIMESTAMP)
begin update Timestamp_Tab set IN_TIME=INTIME_PARAM; select IN_TIME into INTIME_PARAM from Timestamp_Tab; end
!

DROP PROCEDURE IF EXISTS Timestamp_Io_Null!
CREATE PROCEDURE Timestamp_Io_Null(inout NULL_PARAM TIMESTAMP)
begin update Timestamp_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL  into NULL_PARAM from Timestamp_Tab; end
!

DROP PROCEDURE IF EXISTS Timestamp_Proc!
CREATE PROCEDURE Timestamp_Proc(out IN_PARAM TIMESTAMP, out NULL_PARAM TIMESTAMP)
begin select IN_TIME, NULL_VAL  into IN_PARAM, NULL_PARAM from Timestamp_Tab; end
!

DROP PROCEDURE IF EXISTS Time_In_Brk!
CREATE PROCEDURE Time_In_Brk(BRK_PARAM TIME)
begin update Time_Tab set BRK_TIME=BRK_PARAM; end
!

DROP PROCEDURE IF EXISTS Time_In_Null!
CREATE PROCEDURE Time_In_Null(NULL_PARAM TIME)
begin update Time_Tab set NULL_VAL=NULL_PARAM; end
!

DROP PROCEDURE IF EXISTS Time_Io_Brk!
CREATE PROCEDURE Time_Io_Brk(inout BRK_PARAM TIME)
begin update Time_Tab set BRK_TIME=BRK_PARAM; select BRK_TIME into BRK_PARAM from Time_Tab; end
!

DROP PROCEDURE IF EXISTS Time_Io_Null!
CREATE PROCEDURE Time_Io_Null(inout NULL_PARAM TIME)
begin update Time_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL  into NULL_PARAM from Time_Tab; end
!

DROP PROCEDURE IF EXISTS Time_Proc!
CREATE PROCEDURE Time_Proc(out BRK_PARAM TIME, out NULL_PARAM TIME)
begin select BRK_TIME, NULL_VAL  into BRK_PARAM, NULL_PARAM from Time_Tab; end
!

DROP PROCEDURE IF EXISTS Tinyint_In_Max!
CREATE PROCEDURE Tinyint_In_Max(MAX_PARAM NUMERIC(30,15))
begin update Tinyint_Tab set MAX_VAL=MAX_PARAM; end
!

DROP PROCEDURE IF EXISTS Tinyint_In_Min!
CREATE PROCEDURE Tinyint_In_Min(MIN_PARAM NUMERIC(30,15))
begin update Tinyint_Tab set MIN_VAL=MIN_PARAM; end
!

DROP PROCEDURE IF EXISTS Tinyint_In_Null!
CREATE PROCEDURE Tinyint_In_Null(NULL_PARAM NUMERIC(30,15))
begin update Tinyint_Tab set NULL_VAL=NULL_PARAM; end
!

DROP PROCEDURE IF EXISTS Tinyint_Io_Max!
CREATE PROCEDURE Tinyint_Io_Max(inout MAX_PARAM TINYINT)
begin update Tinyint_Tab set MAX_VAL=MAX_PARAM; select MAX_VAL into MAX_PARAM from Tinyint_Tab; end
!

DROP PROCEDURE IF EXISTS Tinyint_Io_Min!
CREATE PROCEDURE Tinyint_Io_Min(inout MIN_PARAM TINYINT)
begin update Tinyint_Tab set MIN_VAL=MIN_PARAM; select MIN_VAL into MIN_PARAM from Tinyint_Tab; end
!

DROP PROCEDURE IF EXISTS Tinyint_Io_Null!
CREATE PROCEDURE Tinyint_Io_Null(inout NULL_PARAM TINYINT)
begin update Tinyint_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL  into NULL_PARAM from Tinyint_Tab; end
!

DROP PROCEDURE IF EXISTS Tinyint_Proc!
CREATE PROCEDURE Tinyint_Proc(out MAX_PARAM NUMERIC(30,15), out MIN_PARAM NUMERIC(30,15), out NULL_PARAM NUMERIC(30,15))
begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from Tinyint_Tab; end
!

DROP PROCEDURE IF EXISTS UpdCoffee_Proc!
CREATE PROCEDURE UpdCoffee_Proc(in TYPE_PARAM NUMERIC)
begin update ctstable2 set PRICE=PRICE*20 where TYPE_ID=TYPE_PARAM; end
!

DROP PROCEDURE IF EXISTS Varbinary_Proc!
CREATE PROCEDURE Varbinary_Proc(out VARBINARY_PARAM BLOB)
begin select VARBINARY_VAL into VARBINARY_PARAM from Varbinary_Tab;end
!

DROP PROCEDURE IF EXISTS Varbinary_Proc_In!
CREATE PROCEDURE Varbinary_Proc_In(VARBINARY_PARAM BLOB)
begin update Varbinary_Tab set VARBINARY_VAL=VARBINARY_PARAM; end
!

DROP PROCEDURE IF EXISTS Varbinary_Proc_Io!
CREATE PROCEDURE Varbinary_Proc_Io(inout VARBINARY_PARAM BLOB)
begin update Varbinary_Tab set VARBINARY_VAL=VARBINARY_PARAM; select VARBINARY_VAL into VARBINARY_PARAM from Varbinary_Tab; end
!

DROP PROCEDURE IF EXISTS Varchar_In_Name!
CREATE PROCEDURE Varchar_In_Name(NAME_PARAM VARCHAR(255))
begin update Varchar_Tab set COFFEE_NAME=NAME_PARAM;  end
!

DROP PROCEDURE IF EXISTS Varchar_In_Null!
CREATE PROCEDURE Varchar_In_Null(NULL_PARAM VARCHAR(255))
begin update Varchar_Tab set NULL_VAL=NULL_PARAM;  end
!

DROP PROCEDURE IF EXISTS Varchar_Io_Name!
CREATE PROCEDURE Varchar_Io_Name(inout NAME_PARAM VARCHAR(255))
begin update Varchar_Tab set COFFEE_NAME=NAME_PARAM; select COFFEE_NAME into NAME_PARAM from Varchar_Tab; end
!

DROP PROCEDURE IF EXISTS Varchar_Io_Null!
CREATE PROCEDURE Varchar_Io_Null(inout NULL_PARAM VARCHAR(255))
begin update Varchar_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL  into NULL_PARAM from Varchar_Tab; end
!

DROP PROCEDURE IF EXISTS Varchar_Proc!
CREATE PROCEDURE Varchar_Proc(out NAME_PARAM VARCHAR(255), out NULL_PARAM VARCHAR(255))
begin select COFFEE_NAME, NULL_VAL into NAME_PARAM, NULL_PARAM from Varchar_Tab; end !


drop procedure IF EXISTS Lvarcharnull_Proc!
create procedure Lvarcharnull_Proc(out NULL_PARAM LONG)
begin select NULL_VAL into NULL_PARAM from Longvarcharnull_Tab;
end !


drop procedure IF EXISTS Lvarchar_Io_Name!
create procedure Lvarchar_Io_Name(inout NAME_PARAM LONG)
begin update Longvarchar_Tab set COFFEE_NAME=NAME_PARAM; select COFFEE_NAME into NAME_PARAM from Longvarchar_Tab;
end ! 

drop procedure IF EXISTS Lvarchar_Io_Null!
create procedure Lvarchar_Io_Null(inout NULL_PARAM LONG)
begin update Longvarcharnull_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL into NULL_PARAM from Longvarcharnull_Tab;
end ! 

DROP PROCEDURE IF EXISTS Lvarchar_In_Name!
CREATE PROCEDURE Lvarchar_In_Name(in NAME_PARAM LONG)
begin update Longvarchar_Tab set COFFEE_NAME=NAME_PARAM; end
!

DROP PROCEDURE IF EXISTS Lvarchar_In_Null!
CREATE PROCEDURE Lvarchar_In_Null(in NULL_PARAM LONG)
begin update Longvarcharnull_Tab set NULL_VAL=NULL_PARAM; end
!


