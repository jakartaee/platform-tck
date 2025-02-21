drop procedure Numeric_Proc !
create procedure Numeric_Proc (MAX_PARAM out NUMBER, MIN_PARAM out NUMBER, NULL_PARAM out NUMBER) as begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from Numeric_Tab; end; !

drop procedure Decimal_Proc !
create procedure Decimal_Proc (MAX_PARAM out NUMBER,MIN_PARAM out NUMBER, NULL_PARAM out NUMBER) as begin select MAX_VAL,MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from Decimal_Tab; end; !

drop procedure Double_Proc !
create procedure Double_Proc (MAX_PARAM out DOUBLE PRECISION, MIN_PARAM out DOUBLE PRECISION, NULL_PARAM out DOUBLE PRECISION) as begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from Double_Tab; end; !

drop procedure Float_Proc !
create procedure Float_Proc (MAX_PARAM out FLOAT, MIN_PARAM out FLOAT, NULL_PARAM out FLOAT) as begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from Float_Tab; end; !

drop procedure Real_Proc !
create procedure Real_Proc (MAX_PARAM out REAL, MIN_PARAM out REAL, NULL_PARAM out REAL) as begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from Real_Tab; end; !

drop procedure Bit_Proc !
create procedure Bit_Proc (MAX_PARAM out SMALLINT, MIN_PARAM out SMALLINT, NULL_PARAM out SMALLINT) as begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from Bit_Tab; end; !

drop procedure Smallint_Proc !
create procedure Smallint_Proc (MAX_PARAM out SMALLINT, MIN_PARAM out SMALLINT, NULL_PARAM out SMALLINT) as begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from Smallint_Tab; end; !

drop procedure Tinyint_Proc !
create procedure Tinyint_Proc (MAX_PARAM out NUMERIC, MIN_PARAM out NUMERIC, NULL_PARAM out NUMERIC) as begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from Tinyint_Tab; end; !

drop procedure Integer_Proc !
create procedure Integer_Proc (MAX_PARAM out INTEGER, MIN_PARAM out INTEGER, NULL_PARAM out INTEGER) as begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from Integer_Tab; end; !

drop procedure Bigint_Proc !
create procedure Bigint_Proc (MAX_PARAM out NUMBER, MIN_PARAM out NUMBER, NULL_PARAM out NUMBER) as begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from Bigint_Tab; end; !

drop procedure Char_Proc !
create procedure Char_Proc (NAME_PARAM out CHAR, NULL_PARAM out CHAR) as begin select COFFEE_NAME, NULL_VAL  into NAME_PARAM, NULL_PARAM from Char_Tab; end; !

drop procedure Varchar_Proc !
create procedure Varchar_Proc (NAME_PARAM out VARCHAR, NULL_PARAM out VARCHAR) as begin select COFFEE_NAME, NULL_VAL  into NAME_PARAM, NULL_PARAM from Varchar_Tab; end; !

drop procedure Longvarchar_Proc !
create procedure Longvarchar_Proc (NAME_PARAM out LONG) as begin select COFFEE_NAME  into NAME_PARAM from Longvarchar_Tab; end; !

drop procedure Lvarcharnull_Proc !
create procedure Lvarcharnull_Proc (NULL_PARAM out LONG) as begin select  NULL_VAL  into  NULL_PARAM from Longvarcharnull_Tab; end; !

drop procedure Date_Proc !
create procedure Date_Proc (MFG_PARAM out DATE, NULL_PARAM out DATE) as begin select MFG_DATE, NULL_VAL  into MFG_PARAM, NULL_PARAM from Date_Tab; end; !

drop procedure Time_Proc !
create procedure Time_Proc (BRK_PARAM out DATE, NULL_PARAM out DATE) as begin select BRK_TIME, NULL_VAL  into BRK_PARAM, NULL_PARAM from time_tab; end; !

drop procedure Timestamp_Proc !
create procedure Timestamp_Proc (IN_PARAM out DATE, NULL_PARAM out DATE) as begin select IN_TIME, NULL_VAL  into IN_PARAM, NULL_PARAM from Timestamp_Tab; end; !

drop procedure Binary_Proc !
create procedure Binary_Proc (BINARY_PARAM out RAW) as begin select BINARY_VAL into BINARY_PARAM from Binary_Tab; end; !

drop procedure Varbinary_Proc !
create procedure Varbinary_Proc (VARBINARY_PARAM out RAW) as begin select VARBINARY_VAL into VARBINARY_PARAM from Varbinary_tab; end; !

drop procedure Longvarbinary_Proc !
create procedure Longvarbinary_Proc (LONGVARBINARY_PARAM out LONG RAW) as begin select LONGVARBINARY_VAL into LONGVARBINARY_PARAM from LongVarbinary_tab; end; !

drop procedure Integer_In_Proc !
create procedure Integer_In_Proc (IN_PARAM INTEGER) as begin update Integer_Tab set MAX_VAL=IN_PARAM; end; !

drop procedure Integer_InOut_Proc !
create procedure Integer_InOut_Proc (INOUT_PARAM out INTEGER) as begin select MAX_VAL into INOUT_PARAM from Integer_Tab where MIN_VAL=INOUT_PARAM; end; !

drop procedure UpdCoffee_Proc !
create procedure UpdCoffee_Proc (TYPE_PARAM in VARCHAR) as begin update ctstable2 set PRICE=PRICE*20 where TYPE_ID=TYPE_PARAM; end; !

drop procedure SelCoffee_Proc !
create procedure SelCoffee_Proc (KEYID_PARAM out NUMERIC) as begin select KEY_ID into KEYID_PARAM from ctstable2 where TYPE_ID=1; end; !

drop procedure IOCoffee_Proc !
create procedure IOCoffee_Proc (PRICE_PARAM out FLOAT) as begin select PRICE*2 into PRICE_PARAM from ctstable2 where PRICE=PRICE_PARAM; end; !

drop procedure Coffee_Proc !
create procedure Coffee_Proc(TYPE_PARAM in Numeric) as begin update ctstable2 set PRICE=PRICE*2 where TYPE_ID=TYPE_PARAM; delete from ctstable2 where TYPE_ID=TYPE_PARAM-1;end; !

drop procedure Numeric_Io_Max  !
create procedure Numeric_Io_Max (MAX_PARAM in out NUMBER) as begin update Numeric_Tab set MAX_VAL=MAX_PARAM; select MAX_VAL into MAX_PARAM from Numeric_Tab; end; !

drop procedure Numeric_Io_Min  !
create procedure Numeric_Io_Min (MIN_PARAM in out NUMBER) as begin update Numeric_Tab set MIN_VAL=MIN_PARAM; select MIN_VAL into MIN_PARAM from Numeric_Tab;end; !

drop procedure Numeric_Io_Null  !
create procedure Numeric_Io_Null (NULL_PARAM in out NUMBER) as begin update Numeric_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL  into NULL_PARAM from Numeric_Tab;end; !

drop procedure Decimal_Io_Max  !
create procedure Decimal_Io_Max (MAX_PARAM in out NUMBER) as begin update Decimal_Tab set MAX_VAL=MAX_PARAM; select MAX_VAL into MAX_PARAM from Decimal_Tab; end; !

drop procedure Decimal_Io_Min  !
create procedure Decimal_Io_Min (MIN_PARAM in out NUMBER) as begin update Decimal_Tab set MIN_VAL=MIN_PARAM; select MIN_VAL  into MIN_PARAM from Decimal_Tab; end; !

drop procedure Decimal_Io_Null  !
create procedure Decimal_Io_Null (NULL_PARAM in out NUMBER) as begin update Decimal_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL  into NULL_PARAM from Decimal_Tab; end; !

drop procedure Double_Io_Max  !
create procedure Double_Io_Max (MAX_PARAM in out DOUBLE PRECISION) as begin update Double_Tab set MAX_VAL=MAX_PARAM; select MAX_VAL into MAX_PARAM from Double_Tab; end; !

drop procedure Double_Io_Min  !
create procedure Double_Io_Min (MIN_PARAM in out DOUBLE PRECISION) as begin update Double_Tab set MIN_VAL=MIN_PARAM; select MIN_VAL into MIN_PARAM from Double_Tab; end; !

drop procedure Double_Io_Null  !
create procedure Double_Io_Null (NULL_PARAM in out DOUBLE PRECISION) as begin update Double_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL  into NULL_PARAM from Double_Tab; end; !

drop procedure Float_Io_Max  !
create procedure Float_Io_Max (MAX_PARAM in out FLOAT) as begin update Float_Tab set MAX_VAL=MAX_PARAM; select MAX_VAL into MAX_PARAM from Float_Tab; end; !

drop procedure Float_Io_Min  !
create procedure Float_Io_Min (MIN_PARAM in out FLOAT) as begin update Float_Tab set MIN_VAL=MIN_PARAM; select MIN_VAL into MIN_PARAM from Float_Tab; end; !

drop procedure Float_Io_Null !
create procedure Float_Io_Null (NULL_PARAM in out FLOAT) as begin update Float_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL  into NULL_PARAM from Float_Tab; end; !

drop procedure Real_Io_Max  !
create procedure Real_Io_Max (MAX_PARAM in out REAL) as begin update Real_Tab set MAX_VAL=MAX_PARAM; select MAX_VAL into MAX_PARAM from Real_Tab; end; !

drop procedure Real_Io_Min  !
create procedure Real_Io_Min (MIN_PARAM in out REAL) as begin update Real_Tab set MIN_VAL=MIN_PARAM; select MIN_VAL into MIN_PARAM from Real_Tab; end; !

drop procedure Real_Io_Null  !

create procedure Real_Io_Null (NULL_PARAM in out REAL) as begin update Real_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL  into NULL_PARAM from Real_Tab; end; !

drop procedure Bit_Io_Max  !
create procedure Bit_Io_Max (MAX_PARAM in out SMALLINT) as begin update Bit_Tab set MAX_VAL=MAX_PARAM; select MAX_VAL into MAX_PARAM from Bit_Tab; end; !

drop procedure Bit_Io_Min  !
create procedure Bit_Io_Min (MIN_PARAM in out SMALLINT) as begin update Bit_Tab set MIN_VAL=MIN_PARAM; select MIN_VAL into MIN_PARAM from Bit_Tab; end; !

drop procedure Bit_Io_Null  !
create procedure Bit_Io_Null (NULL_PARAM in out SMALLINT) as begin update Bit_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL  into NULL_PARAM from Bit_Tab; end; !

drop procedure Smallint_Io_Max  !
create procedure Smallint_Io_Max (MAX_PARAM in out SMALLINT) as begin update Smallint_Tab set MAX_VAL=MAX_PARAM; select MAX_VAL into MAX_PARAM from Smallint_Tab; end; !

drop procedure Smallint_Io_Min  !
create procedure Smallint_Io_Min (MIN_PARAM in out SMALLINT) as begin update Smallint_Tab set MIN_VAL=MIN_PARAM; select MIN_VAL into MIN_PARAM from Smallint_Tab; end; !

drop procedure Smallint_Io_Null  !
create procedure Smallint_Io_Null (NULL_PARAM in out SMALLINT) as begin update Smallint_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL  into NULL_PARAM from Smallint_Tab; end; !

drop procedure Tinyint_Io_Max  !
create procedure Tinyint_Io_Max (MAX_PARAM in out NUMERIC)as begin update Tinyint_Tab set MAX_VAL=MAX_PARAM; select MAX_VAL into MAX_PARAM from Tinyint_Tab; end; !

drop procedure Tinyint_Io_Min  !
create procedure Tinyint_Io_Min (MIN_PARAM in out NUMERIC) as begin update Tinyint_Tab set MIN_VAL=MIN_PARAM; select MIN_VAL into MIN_PARAM from Tinyint_Tab; end; !

drop procedure Tinyint_Io_Null  !
create procedure Tinyint_Io_Null (NULL_PARAM in out NUMERIC) as begin update Tinyint_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL  into NULL_PARAM from Tinyint_Tab; end; !

drop procedure Integer_Io_Max  !
create procedure Integer_Io_Max (MAX_PARAM in out INTEGER) as begin update Integer_Tab set MAX_VAL=MAX_PARAM; select MAX_VAL into MAX_PARAM from Integer_Tab; end; !

drop procedure Integer_Io_Min  !
create procedure Integer_Io_Min (MIN_PARAM in out INTEGER) as begin update Integer_Tab set MIN_VAL=MIN_PARAM; select MIN_VAL into MIN_PARAM from Integer_Tab; end; !

drop procedure Integer_Io_Null  !
create procedure Integer_Io_Null (NULL_PARAM in out INTEGER) as begin update Integer_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL into NULL_PARAM from Integer_Tab; end; !

drop procedure Bigint_Io_Max  !
create procedure Bigint_Io_Max (MAX_PARAM in out NUMERIC) as begin update Bigint_Tab set MAX_VAL=MAX_PARAM; select MAX_VAL into MAX_PARAM from Bigint_Tab; end; !

drop procedure Bigint_Io_Min  !
create procedure Bigint_Io_Min (MIN_PARAM in out NUMERIC) as begin update Bigint_Tab set MIN_VAL=MIN_PARAM; select MIN_VAL into MIN_PARAM from Bigint_Tab; end; !

drop procedure Bigint_Io_Null  !
create procedure Bigint_Io_Null (NULL_PARAM in out NUMERIC) as begin update Bigint_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL into NULL_PARAM from Bigint_Tab; end; !

drop procedure Char_Io_Name !
create procedure Char_Io_Name(NAME_PARAM in out CHAR) as begin update Char_Tab set COFFEE_NAME=NAME_PARAM; select COFFEE_NAME into NAME_PARAM from Char_Tab; end; !

drop procedure Char_Io_Null !
create procedure Char_Io_Null(NULL_PARAM in out VARCHAR) as begin update Char_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL  into NULL_PARAM from Char_Tab; end; !

drop procedure Varchar_Io_Name !
create procedure Varchar_Io_Name(NAME_PARAM in out VARCHAR) as begin update Varchar_Tab set COFFEE_NAME=NAME_PARAM; select COFFEE_NAME into NAME_PARAM from Varchar_Tab; end; !

drop procedure Varchar_Io_Null !
create procedure Varchar_Io_Null(NULL_PARAM in out VARCHAR) as begin update Varchar_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL  into NULL_PARAM from Varchar_Tab; end; !

drop procedure Lvarchar_Io_Name !
create procedure Lvarchar_Io_Name(NAME_PARAM in out LONG) as begin update Longvarchar_Tab set COFFEE_NAME=NAME_PARAM; select COFFEE_NAME into NAME_PARAM from Longvarchar_Tab; end; !

drop procedure Lvarchar_Io_Null !
create procedure Lvarchar_Io_Null(NULL_PARAM in out LONG) as begin update Longvarcharnull_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL  into NULL_PARAM from Longvarcharnull_Tab; end; !

drop procedure Date_Io_Mfg !
create procedure Date_Io_Mfg(MFG_PARAM in out DATE) as begin update Date_Tab set MFG_DATE=MFG_PARAM; select MFG_DATE into MFG_PARAM from Date_Tab; end; !

drop procedure Date_Io_Null !
create procedure Date_Io_Null(NULL_PARAM in out DATE) as begin update Date_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL  into NULL_PARAM from Date_Tab; end; !

drop procedure Time_Io_Brk !
create procedure Time_Io_Brk(BRK_PARAM in out DATE) as begin update Time_Tab set BRK_TIME=BRK_PARAM; select BRK_TIME into BRK_PARAM from Time_Tab; end; !

drop procedure Time_Io_Null !
create procedure Time_Io_Null(NULL_PARAM in out DATE) as begin update Time_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL  into NULL_PARAM from Time_Tab; end; !

drop procedure Timestamp_Io_Intime !
create procedure Timestamp_Io_Intime(INTIME_PARAM in out DATE) as begin update Timestamp_Tab set IN_TIME=INTIME_PARAM; select IN_TIME into INTIME_PARAM from Timestamp_Tab; end; !

drop procedure Timestamp_Io_Null !
create procedure Timestamp_Io_Null(NULL_PARAM in out DATE) as begin update Timestamp_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL  into NULL_PARAM from Timestamp_Tab; end; !

drop procedure Binary_Proc_Io !
create procedure Binary_Proc_Io(BINARY_PARAM in out RAW) as begin update Binary_Tab set BINARY_VAL=BINARY_PARAM; select BINARY_VAL into BINARY_PARAM from Binary_Tab; end; !

drop procedure Varbinary_Proc_Io !
create procedure Varbinary_Proc_Io(VARBINARY_PARAM in out RAW) as begin update Varbinary_Tab set VARBINARY_VAL=VARBINARY_PARAM; select VARBINARY_VAL into VARBINARY_PARAM from Varbinary_Tab; end; !

drop procedure Longvarbinary_Io !
create procedure Longvarbinary_Io(LONGVARBINARY_PARAM in out LONG RAW) as begin update Longvarbinary_Tab set LONGVARBINARY_VAL=LONGVARBINARY_PARAM; select LONGVARBINARY_VAL into LONGVARBINARY_PARAM from Longvarbinary_Tab; end; !

drop procedure Numeric_In_Max  !
create procedure Numeric_In_Max (MAX_PARAM in NUMBER) as begin update Numeric_Tab set MAX_VAL=MAX_PARAM;  end; !

drop procedure Numeric_In_Min  !
create procedure Numeric_In_Min (MIN_PARAM in NUMBER) as begin update Numeric_Tab set MIN_VAL=MIN_PARAM; end; !

drop procedure Numeric_In_Null  !
create procedure Numeric_In_Null (NULL_PARAM in NUMBER) as begin update Numeric_Tab set NULL_VAL=NULL_PARAM; end; !

drop procedure Decimal_In_Max  !
create procedure Decimal_In_Max (MAX_PARAM in NUMBER) as begin update Decimal_Tab set MAX_VAL=MAX_PARAM;  end; !

drop procedure Decimal_In_Min  !
create procedure Decimal_In_Min (MIN_PARAM in NUMBER) as begin update Decimal_Tab set MIN_VAL=MIN_PARAM;  end; !

drop procedure Decimal_In_Null  !
create procedure Decimal_In_Null (NULL_PARAM in NUMBER) as begin update Decimal_Tab set NULL_VAL=NULL_PARAM;  end; !

drop procedure Double_In_Max  !
create procedure Double_In_Max (MAX_PARAM in DOUBLE PRECISION) as begin update Double_Tab set MAX_VAL=MAX_PARAM; end; !

drop procedure Double_In_Min  !
create procedure Double_In_Min (MIN_PARAM in DOUBLE PRECISION) as begin update Double_Tab set MIN_VAL=MIN_PARAM; end; !

drop procedure Double_In_Null  !
create procedure Double_In_Null (NULL_PARAM in DOUBLE PRECISION) as begin update Double_Tab set NULL_VAL=NULL_PARAM; end; !

drop procedure Float_In_Max  !
create procedure Float_In_Max (MAX_PARAM in FLOAT) as begin update Float_Tab set MAX_VAL=MAX_PARAM; end; !

drop procedure Float_In_Min  !
create procedure Float_In_Min (MIN_PARAM in FLOAT) as begin update Float_Tab set MIN_VAL=MIN_PARAM; end; !

drop procedure Float_In_Null !
create procedure Float_In_Null (NULL_PARAM in FLOAT) as begin update Float_Tab set NULL_VAL=NULL_PARAM; end; !

drop procedure Real_In_Max  !
create procedure Real_In_Max (MAX_PARAM in REAL) as begin update Real_Tab set MAX_VAL=MAX_PARAM; end; !

drop procedure Real_In_Min  !
create procedure Real_In_Min (MIN_PARAM in REAL) as begin update Real_Tab set MIN_VAL=MIN_PARAM; end; !

drop procedure Real_In_Null  !
create procedure Real_In_Null (NULL_PARAM in REAL) as begin update Real_Tab set NULL_VAL=NULL_PARAM; end; !

drop procedure Bit_In_Max  !
create procedure Bit_In_Max (MAX_PARAM in SMALLINT) as begin update Bit_Tab set MAX_VAL=MAX_PARAM; end; !

drop procedure Bit_In_Min  !
create procedure Bit_In_Min (MIN_PARAM in SMALLINT) as begin update Bit_Tab set MIN_VAL=MIN_PARAM; end; !

drop procedure Bit_In_Null  !
create procedure Bit_In_Null (NULL_PARAM in SMALLINT) as begin update Bit_Tab set NULL_VAL=NULL_PARAM; end; !

drop procedure Smallint_In_Max  !
create procedure Smallint_In_Max (MAX_PARAM in SMALLINT) as begin update Smallint_Tab set MAX_VAL=MAX_PARAM; end; !

drop procedure Smallint_In_Min  !
create procedure Smallint_In_Min (MIN_PARAM in SMALLINT) as begin update Smallint_Tab set MIN_VAL=MIN_PARAM; end; !

drop procedure Smallint_In_Null  !
create procedure Smallint_In_Null (NULL_PARAM in SMALLINT) as begin update Smallint_Tab set NULL_VAL=NULL_PARAM; end; !

drop procedure Tinyint_In_Max  !
create procedure Tinyint_In_Max (MAX_PARAM in NUMERIC) as begin update Tinyint_Tab set MAX_VAL=MAX_PARAM; end; !

drop procedure Tinyint_In_Min  !
create procedure Tinyint_In_Min (MIN_PARAM in NUMERIC) as begin update Tinyint_Tab set MIN_VAL=MIN_PARAM; end; !

drop procedure Tinyint_In_Null  !
create procedure Tinyint_In_Null (NULL_PARAM in NUMERIC) as begin update Tinyint_Tab set NULL_VAL=NULL_PARAM; end; !

drop procedure Integer_In_Max  !
create procedure Integer_In_Max (MAX_PARAM in INTEGER) as begin update Integer_Tab set MAX_VAL=MAX_PARAM; end; !

drop procedure Integer_In_Min  !
create procedure Integer_In_Min (MIN_PARAM in INTEGER) as begin update Integer_Tab set MIN_VAL=MIN_PARAM; end; !

drop procedure Integer_In_Null  !
create procedure Integer_In_Null (NULL_PARAM in INTEGER) as begin update Integer_Tab set NULL_VAL=NULL_PARAM; end; !

drop procedure Bigint_In_Max  !
create procedure Bigint_In_Max (MAX_PARAM in NUMERIC) as begin update Bigint_Tab set MAX_VAL=MAX_PARAM; end; !

drop procedure Bigint_In_Min  !
create procedure Bigint_In_Min (MIN_PARAM in NUMERIC) as begin update Bigint_Tab set MIN_VAL=MIN_PARAM; end; !

drop procedure Bigint_In_Null  !
create procedure Bigint_In_Null (NULL_PARAM in NUMERIC) as begin update Bigint_Tab set NULL_VAL=NULL_PARAM; end; !

drop procedure Char_In_Name !
create procedure Char_In_Name(NAME_PARAM in CHAR) as begin update Char_Tab set COFFEE_NAME=NAME_PARAM; end; !

drop procedure Char_In_Null !
create procedure Char_In_Null(NULL_PARAM in CHAR) as begin update Char_Tab set NULL_VAL=NULL_PARAM; end; !

drop procedure Varchar_In_Name !
create procedure Varchar_In_Name(NAME_PARAM in VARCHAR) as begin update Varchar_Tab set COFFEE_NAME=NAME_PARAM;  end; !

drop procedure Varchar_In_Null !
create procedure Varchar_In_Null(NULL_PARAM in VARCHAR) as begin update Varchar_Tab set NULL_VAL=NULL_PARAM;  end; !

drop procedure Lvarchar_In_Name !
create procedure Lvarchar_In_Name(NAME_PARAM in LONG) as begin update Longvarchar_Tab set COFFEE_NAME=NAME_PARAM; end; !

drop procedure Lvarchar_In_Null !
create procedure Lvarchar_In_Null(NULL_PARAM in LONG) as begin update Longvarcharnull_Tab set NULL_VAL=NULL_PARAM; end; !

drop procedure Date_In_Mfg !
create procedure Date_In_Mfg(MFG_PARAM in DATE) as begin update Date_Tab set MFG_DATE=MFG_PARAM; end; !

drop procedure Date_In_Null !
create procedure Date_In_Null(NULL_PARAM in DATE) as begin update Date_Tab set NULL_VAL=NULL_PARAM; end; !

drop procedure Time_In_Brk !
create procedure Time_In_Brk(BRK_PARAM in DATE) as begin update Time_Tab set BRK_TIME=BRK_PARAM; end; !

drop procedure Time_In_Null !
create procedure Time_In_Null(NULL_PARAM in DATE) as begin update Time_Tab set NULL_VAL=NULL_PARAM; end; !

drop procedure Timestamp_In_Intime !
create procedure Timestamp_In_Intime(INTIME_PARAM in DATE) as begin update Timestamp_Tab set IN_TIME=INTIME_PARAM; end; !

drop procedure Timestamp_In_Null !
create procedure Timestamp_In_Null(NULL_PARAM in DATE) as begin update Timestamp_Tab set NULL_VAL=NULL_PARAM; end; !

drop procedure Binary_Proc_In !
create procedure Binary_Proc_In(BINARY_PARAM in RAW) as begin update Binary_Tab set BINARY_VAL=BINARY_PARAM; end; !

drop procedure Varbinary_Proc_In !
create procedure Varbinary_Proc_In(VARBINARY_PARAM in RAW) as begin update Varbinary_Tab set VARBINARY_VAL=VARBINARY_PARAM; end; !

drop procedure Longvarbinary_In !
create procedure Longvarbinary_In(LONGVARBINARY_PARAM in LONG RAW) as begin update Longvarbinary_Tab set LONGVARBINARY_VAL=LONGVARBINARY_PARAM; end; !
