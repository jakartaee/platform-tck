
drop procedure Numeric_Proc !
create procedure Numeric_Proc (out MAX_PARAM NUMERIC(30,15), out MIN_PARAM NUMERIC(30,15), out NULL_PARAM NUMERIC(30,15)) LANGUAGE SQL begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from Numeric_Tab; end !

drop procedure Decimal_Proc !
create procedure Decimal_Proc (out MAX_PARAM DECIMAL(31,15), out MIN_PARAM DECIMAL(31,15), out NULL_PARAM DECIMAL(31,15)) LANGUAGE SQL begin select MAX_VAL,MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from Decimal_Tab; end   !

drop procedure Double_Proc !
create procedure Double_Proc (out MAX_PARAM DOUBLE PRECISION, out MIN_PARAM DOUBLE PRECISION, out NULL_PARAM DOUBLE PRECISION) LANGUAGE SQL begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from Double_Tab; end !

drop procedure Float_Proc !
create procedure Float_Proc (out MAX_PARAM FLOAT, out MIN_PARAM FLOAT, out NULL_PARAM FLOAT) LANGUAGE SQL begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from Float_Tab; end !

drop procedure Real_Proc !
create procedure Real_Proc (out MAX_PARAM REAL, out MIN_PARAM REAL, out NULL_PARAM REAL) LANGUAGE SQL begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from Real_Tab; end !

drop procedure Bit_Proc !
create procedure Bit_Proc (out MAX_PARAM SMALLINT, out MIN_PARAM SMALLINT, out NULL_PARAM SMALLINT) LANGUAGE SQL begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from Bit_Tab; end !

drop procedure Smallint_Proc !
create procedure Smallint_Proc (out MAX_PARAM SMALLINT, out MIN_PARAM SMALLINT, out NULL_PARAM SMALLINT) LANGUAGE SQL begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from Smallint_Tab; end !

drop procedure Tinyint_Proc !
create procedure Tinyint_Proc (out MAX_PARAM NUMERIC, out MIN_PARAM NUMERIC, out NULL_PARAM NUMERIC) LANGUAGE SQL begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from Tinyint_Tab; end !

drop procedure Integer_Proc !
create procedure Integer_Proc (out MAX_PARAM INTEGER, out MIN_PARAM INTEGER, out NULL_PARAM INTEGER) LANGUAGE SQL begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from Integer_Tab; end !

drop procedure Bigint_Proc !
create procedure Bigint_Proc (out MAX_PARAM BIGINT, out MIN_PARAM BIGINT, out NULL_PARAM BIGINT) LANGUAGE SQL begin select MAX_VAL, MIN_VAL, NULL_VAL  into MAX_PARAM, MIN_PARAM, NULL_PARAM from Bigint_Tab; end !

drop procedure Char_Proc !
create procedure Char_Proc (out NAME_PARAM CHAR(30), out NULL_PARAM CHAR(30)) LANGUAGE SQL begin select COFFEE_NAME, NULL_VAL  into NAME_PARAM, NULL_PARAM from Char_Tab; end !

drop procedure Varchar_Proc !
create procedure Varchar_Proc (out NAME_PARAM VARCHAR(30), out NULL_PARAM VARCHAR(30)) LANGUAGE SQL begin select COFFEE_NAME, NULL_VAL  into NAME_PARAM, NULL_PARAM from Varchar_Tab; end !

drop procedure Longvarchar_Proc !
create procedure Longvarchar_Proc (out NAME_PARAM VARCHAR(448)) LANGUAGE SQL begin select COFFEE_NAME into NAME_PARAM from Longvarchar_Tab; end !

drop procedure Lvarcharnull_Proc !
create procedure Lvarcharnull_Proc (out NULL_PARAM VARCHAR(448)) LANGUAGE SQL begin select  NULL_VAL  into  NULL_PARAM from Longvarcharnull_Tab; end !

drop procedure Date_Proc !
create procedure Date_Proc (out MFG_PARAM DATE, out NULL_PARAM DATE) LANGUAGE SQL begin select MFG_DATE, NULL_VAL  into MFG_PARAM, NULL_PARAM from Date_Tab; end !

drop procedure Time_Proc !
create procedure Time_Proc (out BRK_PARAM TIME, out NULL_PARAM TIME) LANGUAGE SQL begin select BRK_TIME, NULL_VAL  into BRK_PARAM, NULL_PARAM from time_tab; end !

drop procedure Timestamp_Proc !
create procedure Timestamp_Proc (out IN_PARAM TIMESTAMP, out NULL_PARAM TIMESTAMP) LANGUAGE SQL begin select IN_TIME, NULL_VAL  into IN_PARAM, NULL_PARAM from Timestamp_Tab; end !

drop procedure Binary_Proc !
create procedure Binary_Proc (out BINARY_PARAM VARCHAR(448) FOR BIT DATA) LANGUAGE SQL begin select BINARY_VAL into BINARY_PARAM from Binary_Tab; end !

drop procedure Varbinary_Proc !
create procedure Varbinary_Proc (out VARBINARY_PARAM VARCHAR(448) FOR BIT DATA) LANGUAGE SQL begin select VARBINARY_VAL into VARBINARY_PARAM from Varbinary_tab; end !

drop procedure Longvarbinary_Proc !
create procedure Longvarbinary_Proc (out LONGVARBINARY_PARAM VARCHAR(448) FOR BIT DATA) LANGUAGE SQL begin select LONGVARBINARY_VAL into LONGVARBINARY_PARAM from LongVarbinary_tab; end !

drop procedure Integer_In_Proc !
create procedure Integer_In_Proc (IN_PARAM INTEGER) LANGUAGE SQL begin update Integer_Tab set MAX_VAL=IN_PARAM; end !

drop procedure Integer_InOut_Proc !
create procedure Integer_InOut_Proc (out INOUT_PARAM INTEGER) LANGUAGE SQL begin select MAX_VAL into INOUT_PARAM from Integer_Tab where MIN_VAL=INOUT_PARAM; end !

drop procedure UpdCoffee_Proc !
create procedure UpdCoffee_Proc (in TYPE_PARAM INTEGER) LANGUAGE SQL begin update ctstable2 set PRICE=PRICE*20 where TYPE_ID=TYPE_PARAM; end !

drop procedure SelCoffee_Proc !
create procedure SelCoffee_Proc (out KEYID_PARAM NUMERIC) LANGUAGE SQL begin select KEY_ID into KEYID_PARAM from ctstable2 where TYPE_ID=1; end !

drop procedure IOCoffee_Proc !
create procedure IOCoffee_Proc (out PRICE_PARAM FLOAT) LANGUAGE SQL begin select PRICE*2 into PRICE_PARAM from ctstable2 where PRICE=PRICE_PARAM; end !

drop procedure Coffee_Proc !
create procedure Coffee_Proc(in TYPE_PARAM Numeric) LANGUAGE SQL begin update ctstable2 set PRICE=PRICE*2 where TYPE_ID=TYPE_PARAM; delete from ctstable2 where TYPE_ID=TYPE_PARAM-1;end !

drop procedure Numeric_Io_Max  !
create procedure Numeric_Io_Max (inout MAX_PARAM NUMERIC(30,15)) LANGUAGE SQL begin update Numeric_Tab set MAX_VAL=MAX_PARAM; select MAX_VAL into MAX_PARAM from Numeric_Tab; end !

drop procedure Numeric_Io_Min  !
create procedure Numeric_Io_Min (inout MIN_PARAM NUMERIC(30,15)) LANGUAGE SQL begin update Numeric_Tab set MIN_VAL=MIN_PARAM; select MIN_VAL into MIN_PARAM from Numeric_Tab;end !

drop procedure Numeric_Io_Null  !
create procedure Numeric_Io_Null (inout NULL_PARAM NUMERIC(30,15)) LANGUAGE SQL begin update Numeric_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL  into NULL_PARAM from Numeric_Tab;end !

drop procedure Decimal_Io_Max  !
create procedure Decimal_Io_Max (inout MAX_PARAM DECIMAL(31,15)) LANGUAGE SQL begin update Decimal_Tab set MAX_VAL=MAX_PARAM; select MAX_VAL into MAX_PARAM from Decimal_Tab; end !

drop procedure Decimal_Io_Min  !
create procedure Decimal_Io_Min (inout MIN_PARAM DECIMAL(31,15)) LANGUAGE SQL begin update Decimal_Tab set MIN_VAL=MIN_PARAM; select MIN_VAL  into MIN_PARAM from Decimal_Tab; end !

drop procedure Decimal_Io_Null  !
create procedure Decimal_Io_Null (inout NULL_PARAM DECIMAL(31,15)) LANGUAGE SQL begin update Decimal_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL  into NULL_PARAM from Decimal_Tab; end !

drop procedure Double_Io_Max  !
create procedure Double_Io_Max (inout MAX_PARAM DOUBLE PRECISION) LANGUAGE SQL begin update Double_Tab set MAX_VAL=MAX_PARAM; select MAX_VAL into MAX_PARAM from Double_Tab; end !

drop procedure Double_Io_Min  !
create procedure Double_Io_Min (inout MIN_PARAM DOUBLE PRECISION) LANGUAGE SQL begin update Double_Tab set MIN_VAL=MIN_PARAM; select MIN_VAL into MIN_PARAM from Double_Tab; end !

drop procedure Double_Io_Null  !
create procedure Double_Io_Null (inout NULL_PARAM DOUBLE PRECISION) LANGUAGE SQL begin update Double_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL  into NULL_PARAM from Double_Tab; end !

drop procedure Float_Io_Max  !
create procedure Float_Io_Max (inout MAX_PARAM FLOAT) LANGUAGE SQL begin update Float_Tab set MAX_VAL=MAX_PARAM; select MAX_VAL into MAX_PARAM from Float_Tab; end !

drop procedure Float_Io_Min  !
create procedure Float_Io_Min (inout MIN_PARAM FLOAT) LANGUAGE SQL begin update Float_Tab set MIN_VAL=MIN_PARAM; select MIN_VAL into MIN_PARAM from Float_Tab; end !

drop procedure Float_Io_Null !
create procedure Float_Io_Null (inout NULL_PARAM FLOAT) LANGUAGE SQL begin update Float_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL  into NULL_PARAM from Float_Tab; end !

drop procedure Real_Io_Max  !
create procedure Real_Io_Max (inout MAX_PARAM REAL) LANGUAGE SQL begin update Real_Tab set MAX_VAL=MAX_PARAM; select MAX_VAL into MAX_PARAM from Real_Tab; end !

drop procedure Real_Io_Min  !
create procedure Real_Io_Min (inout MIN_PARAM REAL) LANGUAGE SQL begin update Real_Tab set MIN_VAL=MIN_PARAM; select MIN_VAL into MIN_PARAM from Real_Tab; end !

drop procedure Real_Io_Null  !
create procedure Real_Io_Null (inout NULL_PARAM REAL) LANGUAGE SQL begin update Real_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL  into NULL_PARAM from Real_Tab; end !

drop procedure Bit_Io_Max  !
create procedure Bit_Io_Max (inout MAX_PARAM SMALLINT) LANGUAGE SQL begin update Bit_Tab set MAX_VAL=MAX_PARAM; select MAX_VAL into MAX_PARAM from Bit_Tab; end !

drop procedure Bit_Io_Min  !
create procedure Bit_Io_Min (inout MIN_PARAM SMALLINT) LANGUAGE SQL begin update Bit_Tab set MIN_VAL=MIN_PARAM; select MIN_VAL into MIN_PARAM from Bit_Tab; end !

drop procedure Bit_Io_Null  !
create procedure Bit_Io_Null (inout NULL_PARAM SMALLINT) LANGUAGE SQL begin update Bit_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL  into NULL_PARAM from Bit_Tab; end !

drop procedure Smallint_Io_Max  !
create procedure Smallint_Io_Max (inout MAX_PARAM SMALLINT) LANGUAGE SQL begin update Smallint_Tab set MAX_VAL=MAX_PARAM; select MAX_VAL into MAX_PARAM from Smallint_Tab; end !

drop procedure Smallint_Io_Min  !
create procedure Smallint_Io_Min (inout MIN_PARAM SMALLINT) LANGUAGE SQL begin update Smallint_Tab set MIN_VAL=MIN_PARAM; select MIN_VAL into MIN_PARAM from Smallint_Tab; end !

drop procedure Smallint_Io_Null  !
create procedure Smallint_Io_Null (inout NULL_PARAM SMALLINT) LANGUAGE SQL begin update Smallint_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL  into NULL_PARAM from Smallint_Tab; end !

drop procedure Tinyint_Io_Max  !
create procedure Tinyint_Io_Max (inout MAX_PARAM NUMERIC)LANGUAGE SQL begin update Tinyint_Tab set MAX_VAL=MAX_PARAM; select MAX_VAL into MAX_PARAM from Tinyint_Tab; end !

drop procedure Tinyint_Io_Min  !
create procedure Tinyint_Io_Min (inout MIN_PARAM NUMERIC) LANGUAGE SQL begin update Tinyint_Tab set MIN_VAL=MIN_PARAM; select MIN_VAL into MIN_PARAM from Tinyint_Tab; end !

drop procedure Tinyint_Io_Null  !
create procedure Tinyint_Io_Null (inout NULL_PARAM NUMERIC) LANGUAGE SQL begin update Tinyint_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL  into NULL_PARAM from Tinyint_Tab; end !

drop procedure Integer_Io_Max  !
create procedure Integer_Io_Max (inout MAX_PARAM INTEGER) LANGUAGE SQL begin update Integer_Tab set MAX_VAL=MAX_PARAM; select MAX_VAL into MAX_PARAM from Integer_Tab; end !

drop procedure Integer_Io_Min  !
create procedure Integer_Io_Min (inout MIN_PARAM INTEGER) LANGUAGE SQL begin update Integer_Tab set MIN_VAL=MIN_PARAM; select MIN_VAL into MIN_PARAM from Integer_Tab; end !

drop procedure Integer_Io_Null  !
create procedure Integer_Io_Null (inout NULL_PARAM INTEGER) LANGUAGE SQL begin update Integer_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL into NULL_PARAM from Integer_Tab; end !

drop procedure Bigint_Io_Max  !
create procedure Bigint_Io_Max (inout MAX_PARAM BIGINT) LANGUAGE SQL begin update Bigint_Tab set MAX_VAL=MAX_PARAM; select MAX_VAL into MAX_PARAM from Bigint_Tab; end !

drop procedure Bigint_Io_Min  !
create procedure Bigint_Io_Min (inout MIN_PARAM BIGINT) LANGUAGE SQL begin update Bigint_Tab set MIN_VAL=MIN_PARAM; select MIN_VAL into MIN_PARAM from Bigint_Tab; end !

drop procedure Bigint_Io_Null  !
create procedure Bigint_Io_Null (inout NULL_PARAM BIGINT) LANGUAGE SQL begin update Bigint_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL into NULL_PARAM from Bigint_Tab; end !

drop procedure Char_Io_Name !
create procedure Char_Io_Name(inout NAME_PARAM CHAR(30)) LANGUAGE SQL begin update Char_Tab set COFFEE_NAME=NAME_PARAM; select COFFEE_NAME into NAME_PARAM from Char_Tab; end !

drop procedure Char_Io_Null !
create procedure Char_Io_Null(inout NULL_PARAM VARCHAR(30)) LANGUAGE SQL begin update Char_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL  into NULL_PARAM from Char_Tab; end !

drop procedure Varchar_Io_Name !
create procedure Varchar_Io_Name(inout NAME_PARAM VARCHAR(30)) LANGUAGE SQL begin update Varchar_Tab set COFFEE_NAME=NAME_PARAM; select COFFEE_NAME into NAME_PARAM from Varchar_Tab; end !

drop procedure Varchar_Io_Null !
create procedure Varchar_Io_Null(inout NULL_PARAM VARCHAR(30)) LANGUAGE SQL begin update Varchar_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL  into NULL_PARAM from Varchar_Tab; end !

drop procedure Lvarchar_Io_Name !
create procedure Lvarchar_Io_Name(inout NAME_PARAM VARCHAR(448)) LANGUAGE SQL begin update Longvarchar_Tab set COFFEE_NAME=NAME_PARAM; select COFFEE_NAME into NAME_PARAM from Longvarchar_Tab; end !

drop procedure Lvarchar_Io_Null !
create procedure Lvarchar_Io_Null(inout NULL_PARAM VARCHAR(448)) LANGUAGE SQL begin update Longvarcharnull_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL  into NULL_PARAM from Longvarcharnull_Tab; end !

drop procedure Date_Io_Mfg !
create procedure Date_Io_Mfg(inout MFG_PARAM DATE) LANGUAGE SQL begin update Date_Tab set MFG_DATE=MFG_PARAM; select MFG_DATE into MFG_PARAM from Date_Tab; end !

drop procedure Date_Io_Null !
create procedure Date_Io_Null(inout NULL_PARAM DATE) LANGUAGE SQL begin update Date_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL  into NULL_PARAM from Date_Tab; end !

drop procedure Time_Io_Brk !
create procedure Time_Io_Brk(inout BRK_PARAM TIME) LANGUAGE SQL begin update Time_Tab set BRK_TIME=BRK_PARAM; select BRK_TIME into BRK_PARAM from Time_Tab; end !

drop procedure Time_Io_Null !
create procedure Time_Io_Null(inout NULL_PARAM TIME) LANGUAGE SQL begin update Time_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL  into NULL_PARAM from Time_Tab; end !

drop procedure Timestamp_Io_Intime !
create procedure Timestamp_Io_Intime(inout INTIME_PARAM TIMESTAMP) LANGUAGE SQL begin update Timestamp_Tab set IN_TIME=INTIME_PARAM; select IN_TIME into INTIME_PARAM from Timestamp_Tab; end !

drop procedure Timestamp_Io_Null !
create procedure Timestamp_Io_Null(inout NULL_PARAM TIMESTAMP) LANGUAGE SQL begin update Timestamp_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL  into NULL_PARAM from Timestamp_Tab; end !

drop procedure Binary_Proc_Io !
create procedure Binary_Proc_Io(inout BINARY_PARAM VARCHAR(448) FOR BIT DATA) LANGUAGE SQL begin update Binary_Tab set BINARY_VAL=BINARY_PARAM; select BINARY_VAL into BINARY_PARAM from Binary_Tab; end !

drop procedure Varbinary_Proc_Io !
create procedure Varbinary_Proc_Io(inout VARBINARY_PARAM VARCHAR(448) FOR BIT DATA) LANGUAGE SQL begin update Varbinary_Tab set VARBINARY_VAL=VARBINARY_PARAM; select VARBINARY_VAL into VARBINARY_PARAM from Varbinary_Tab; end !

drop procedure Longvarbinary_Io !
create procedure Longvarbinary_Io(inout LONGVARBINARY_PARAM VARCHAR(448) FOR BIT DATA) LANGUAGE SQL begin update Longvarbinary_Tab set LONGVARBINARY_VAL=LONGVARBINARY_PARAM; select LONGVARBINARY_VAL into LONGVARBINARY_PARAM from Longvarbinary_Tab; end !

drop procedure Numeric_In_Max  !
create procedure Numeric_In_Max (in MAX_PARAM NUMERIC(30,15)) LANGUAGE SQL begin update Numeric_Tab set MAX_VAL=MAX_PARAM;  end !

drop procedure Numeric_In_Min  !
create procedure Numeric_In_Min (in MIN_PARAM NUMERIC(30,15)) LANGUAGE SQL begin update Numeric_Tab set MIN_VAL=MIN_PARAM; end !

drop procedure Numeric_In_Null  !
create procedure Numeric_In_Null (in NULL_PARAM NUMERIC(30,15)) LANGUAGE SQL begin update Numeric_Tab set NULL_VAL=NULL_PARAM; end !

drop procedure Decimal_In_Max  !
create procedure Decimal_In_Max (in MAX_PARAM DECIMAL(31,15)) LANGUAGE SQL begin update Decimal_Tab set MAX_VAL=MAX_PARAM;  end !

drop procedure Decimal_In_Min  !
create procedure Decimal_In_Min (in MIN_PARAM DECIMAL(31,15)) LANGUAGE SQL begin update Decimal_Tab set MIN_VAL=MIN_PARAM;  end !

drop procedure Decimal_In_Null  !
create procedure Decimal_In_Null (in NULL_PARAM DECIMAL(31,15)) LANGUAGE SQL begin update Decimal_Tab set NULL_VAL=NULL_PARAM;  end !

drop procedure Double_In_Max  !
create procedure Double_In_Max (in MAX_PARAM DOUBLE PRECISION) LANGUAGE SQL begin update Double_Tab set MAX_VAL=MAX_PARAM; end !

drop procedure Double_In_Min  !
create procedure Double_In_Min (in MIN_PARAM DOUBLE PRECISION) LANGUAGE SQL begin update Double_Tab set MIN_VAL=MIN_PARAM; end !

drop procedure Double_In_Null  !
create procedure Double_In_Null (in NULL_PARAM DOUBLE PRECISION) LANGUAGE SQL begin update Double_Tab set NULL_VAL=NULL_PARAM; end !

drop procedure Float_In_Max  !
create procedure Float_In_Max (in MAX_PARAM FLOAT) LANGUAGE SQL begin update Float_Tab set MAX_VAL=MAX_PARAM; end !

drop procedure Float_In_Min  !
create procedure Float_In_Min (in MIN_PARAM FLOAT) LANGUAGE SQL begin update Float_Tab set MIN_VAL=MIN_PARAM; end !

drop procedure Float_In_Null !
create procedure Float_In_Null (in NULL_PARAM FLOAT) LANGUAGE SQL begin update Float_Tab set NULL_VAL=NULL_PARAM; end !

drop procedure Real_In_Max  !
create procedure Real_In_Max (in MAX_PARAM REAL) LANGUAGE SQL begin update Real_Tab set MAX_VAL=MAX_PARAM; end !

drop procedure Real_In_Min  !
create procedure Real_In_Min (in MIN_PARAM REAL) LANGUAGE SQL begin update Real_Tab set MIN_VAL=MIN_PARAM; end !

drop procedure Real_In_Null  !
create procedure Real_In_Null (in NULL_PARAM REAL) LANGUAGE SQL begin update Real_Tab set NULL_VAL=NULL_PARAM; end !

drop procedure Bit_In_Max  !
create procedure Bit_In_Max (in MAX_PARAM SMALLINT) LANGUAGE SQL begin update Bit_Tab set MAX_VAL=MAX_PARAM; end !

drop procedure Bit_In_Min  !
create procedure Bit_In_Min (in MIN_PARAM SMALLINT) LANGUAGE SQL begin update Bit_Tab set MIN_VAL=MIN_PARAM; end !

drop procedure Bit_In_Null  !
create procedure Bit_In_Null (in NULL_PARAM SMALLINT) LANGUAGE SQL begin update Bit_Tab set NULL_VAL=NULL_PARAM; end !

drop procedure Smallint_In_Max  !
create procedure Smallint_In_Max (in MAX_PARAM SMALLINT) LANGUAGE SQL begin update Smallint_Tab set MAX_VAL=MAX_PARAM; end !

drop procedure Smallint_In_Min  !
create procedure Smallint_In_Min (in MIN_PARAM SMALLINT) LANGUAGE SQL begin update Smallint_Tab set MIN_VAL=MIN_PARAM; end !

drop procedure Smallint_In_Null  !
create procedure Smallint_In_Null (in NULL_PARAM SMALLINT) LANGUAGE SQL begin update Smallint_Tab set NULL_VAL=NULL_PARAM; end !

drop procedure Tinyint_In_Max  !
create procedure Tinyint_In_Max (in MAX_PARAM NUMERIC) LANGUAGE SQL begin update Tinyint_Tab set MAX_VAL=MAX_PARAM; end !

drop procedure Tinyint_In_Min  !
create procedure Tinyint_In_Min (in MIN_PARAM NUMERIC) LANGUAGE SQL begin update Tinyint_Tab set MIN_VAL=MIN_PARAM; end !

drop procedure Tinyint_In_Null  !
create procedure Tinyint_In_Null (in NULL_PARAM NUMERIC) LANGUAGE SQL begin update Tinyint_Tab set NULL_VAL=NULL_PARAM; end !

drop procedure Integer_In_Max  !
create procedure Integer_In_Max (in MAX_PARAM INTEGER) LANGUAGE SQL begin update Integer_Tab set MAX_VAL=MAX_PARAM; end !

drop procedure Integer_In_Min  !
create procedure Integer_In_Min (in MIN_PARAM INTEGER) LANGUAGE SQL begin update Integer_Tab set MIN_VAL=MIN_PARAM; end !

drop procedure Integer_In_Null  !
create procedure Integer_In_Null (in NULL_PARAM INTEGER) LANGUAGE SQL begin update Integer_Tab set NULL_VAL=NULL_PARAM; end !


drop procedure Bigint_In_Max  !
create procedure Bigint_In_Max (in MAX_PARAM BIGINT) LANGUAGE SQL begin update Bigint_Tab set MAX_VAL=MAX_PARAM; end !

drop procedure Bigint_In_Min  !
create procedure Bigint_In_Min (in MIN_PARAM BIGINT) LANGUAGE SQL begin update Bigint_Tab set MIN_VAL=MIN_PARAM; end !

drop procedure Bigint_In_Null  !
create procedure Bigint_In_Null (in NULL_PARAM BIGINT) LANGUAGE SQL begin update Bigint_Tab set NULL_VAL=NULL_PARAM; end !

drop procedure Char_In_Name !
create procedure Char_In_Name(in NAME_PARAM CHAR(30)) LANGUAGE SQL begin update Char_Tab set COFFEE_NAME=NAME_PARAM; end !

drop procedure Char_In_Null !
create procedure Char_In_Null(in NULL_PARAM CHAR(30)) LANGUAGE SQL begin update Char_Tab set NULL_VAL=NULL_PARAM; end !

drop procedure Varchar_In_Name !
create procedure Varchar_In_Name(in NAME_PARAM VARCHAR(30)) LANGUAGE SQL begin update Varchar_Tab set COFFEE_NAME=NAME_PARAM;  end !

drop procedure Varchar_In_Null !
create procedure Varchar_In_Null(in NULL_PARAM VARCHAR(30)) LANGUAGE SQL begin update Varchar_Tab set NULL_VAL=NULL_PARAM;  end !

drop procedure Lvarchar_In_Name !
create procedure Lvarchar_In_Name(in NAME_PARAM VARCHAR(448)) LANGUAGE SQL begin update Longvarchar_Tab set COFFEE_NAME=NAME_PARAM; end !

drop procedure Lvarchar_In_Null !
create procedure Lvarchar_In_Null(in NULL_PARAM VARCHAR(448)) LANGUAGE SQL begin update Longvarcharnull_Tab set NULL_VAL=NULL_PARAM; end !

drop procedure Date_In_Mfg !
create procedure Date_In_Mfg(in MFG_PARAM DATE) LANGUAGE SQL begin update Date_Tab set MFG_DATE=MFG_PARAM; end !

drop procedure Date_In_Null !
create procedure Date_In_Null(in NULL_PARAM DATE) LANGUAGE SQL begin update Date_Tab set NULL_VAL=NULL_PARAM; end !

drop procedure Time_In_Brk !
create procedure Time_In_Brk(in BRK_PARAM TIME) LANGUAGE SQL begin update Time_Tab set BRK_TIME=BRK_PARAM; end !

drop procedure Time_In_Null !
create procedure Time_In_Null(in NULL_PARAM TIME) LANGUAGE SQL begin update Time_Tab set NULL_VAL=NULL_PARAM; end !

drop procedure Timestamp_In_Intime !
create procedure Timestamp_In_Intime(in INTIME_PARAM TIMESTAMP) LANGUAGE SQL begin update Timestamp_Tab set IN_TIME=INTIME_PARAM; end !

drop procedure Timestamp_In_Null !
create procedure Timestamp_In_Null(in NULL_PARAM TIMESTAMP) LANGUAGE SQL begin update Timestamp_Tab set NULL_VAL=NULL_PARAM; end !

drop procedure Binary_Proc_In !
create procedure Binary_Proc_In(in BINARY_PARAM VARCHAR(448) FOR BIT DATA) LANGUAGE SQL begin update Binary_Tab set BINARY_VAL=BINARY_PARAM; end !

drop procedure Varbinary_Proc_In !
create procedure Varbinary_Proc_In(in VARBINARY_PARAM VARCHAR(448) FOR BIT DATA) LANGUAGE SQL begin update Varbinary_Tab set VARBINARY_VAL=VARBINARY_PARAM; end !

drop procedure Longvarbinary_In !
create procedure Longvarbinary_In(in LONGVARBINARY_PARAM VARCHAR(448) FOR BIT DATA) LANGUAGE SQL begin update Longvarbinary_Tab set LONGVARBINARY_VAL=LONGVARBINARY_PARAM; end !
