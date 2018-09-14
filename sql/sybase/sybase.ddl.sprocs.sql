
drop procedure Numeric_Proc !
create procedure Numeric_Proc (@MAX_PARAM NUMERIC(30,15) out, @MIN_PARAM NUMERIC(30,15) out, @NULL_PARAM NUMERIC(30,15) out) as begin select @MAX_PARAM=MAX_VAL, @MIN_PARAM=MIN_VAL, @NULL_PARAM=NULL_VAL from Numeric_Tab end !

drop procedure Decimal_Proc !
create procedure Decimal_Proc (@MAX_PARAM DECIMAL(30,15) out,@MIN_PARAM DECIMAL(30,15) out, @NULL_PARAM DECIMAL(30,15) out) as begin select @MAX_PARAM=MAX_VAL,@MIN_PARAM=MIN_VAL, @NULL_PARAM=NULL_VAL from Decimal_Tab end !

drop procedure Double_Proc !
create procedure Double_Proc (@MAX_PARAM DOUBLE PRECISION out, @MIN_PARAM DOUBLE PRECISION out, @NULL_PARAM DOUBLE PRECISION out) as begin select @MAX_PARAM=MAX_VAL, @MIN_PARAM=MIN_VAL, @NULL_PARAM=NULL_VAL from Double_Tab end !

drop procedure Float_Proc !
create procedure Float_Proc (@MAX_PARAM FLOAT out, @MIN_PARAM FLOAT out, @NULL_PARAM FLOAT out) as begin select @MAX_PARAM=MAX_VAL, @MIN_PARAM=MIN_VAL, @NULL_PARAM=NULL_VAL from Float_Tab end !

drop procedure Real_Proc !
create procedure Real_Proc (@MAX_PARAM REAL out, @MIN_PARAM REAL out, @NULL_PARAM REAL out) as begin select @MAX_PARAM=MAX_VAL, @MIN_PARAM=MIN_VAL, @NULL_PARAM=NULL_VAL from Real_Tab end !

drop procedure Bit_Proc !
create procedure Bit_Proc (@MAX_PARAM BIT out, @MIN_PARAM BIT out, @NULL_PARAM SMALLINT out) as begin select @MAX_PARAM=MAX_VAL, @MIN_PARAM=MIN_VAL, @NULL_PARAM=NULL_VAL from Bit_Tab end !

drop procedure Smallint_Proc !
create procedure Smallint_Proc (@MAX_PARAM SMALLINT out, @MIN_PARAM SMALLINT out, @NULL_PARAM SMALLINT out) as begin select @MAX_PARAM=MAX_VAL, @MIN_PARAM=MIN_VAL, @NULL_PARAM=NULL_VAL from Smallint_Tab end !

drop procedure Tinyint_Proc !
create procedure Tinyint_Proc (@MAX_PARAM TINYINT out, @MIN_PARAM TINYINT out, @NULL_PARAM TINYINT out) as begin select @MAX_PARAM=MAX_VAL, @MIN_PARAM=MIN_VAL, @NULL_PARAM=NULL_VAL from Tinyint_Tab end !

drop procedure Integer_Proc !
create procedure Integer_Proc (@MAX_PARAM INTEGER out, @MIN_PARAM INTEGER out, @NULL_PARAM INTEGER out) as begin select @MAX_PARAM=MAX_VAL, @MIN_PARAM=MIN_VAL, @NULL_PARAM=NULL_VAL from Integer_Tab end !

drop procedure Bigint_Proc !
create procedure Bigint_Proc (@MAX_PARAM NUMERIC(20,0) out, @MIN_PARAM NUMERIC(20,0) out, @NULL_PARAM NUMERIC(20,0) out) as begin select @MAX_PARAM=MAX_VAL, @MIN_PARAM=MIN_VAL, @NULL_PARAM=NULL_VAL from Bigint_Tab end !

drop procedure Char_Proc !
create procedure Char_Proc (@NAME_PARAM CHAR(30) out, @NULL_PARAM CHAR(5) out) as begin select @NAME_PARAM=COFFEE_NAME, @NULL_PARAM=NULL_VAL from Char_Tab end !

drop procedure Varchar_Proc !
create procedure Varchar_Proc (@NAME_PARAM VARCHAR(30) out, @NULL_PARAM VARCHAR(5) out) as begin select @NAME_PARAM=COFFEE_NAME, @NULL_PARAM=NULL_VAL from Varchar_Tab end !

drop procedure Longvarchar_Proc !
create procedure Longvarchar_Proc (@NAME_PARAM VARCHAR(30) out) as begin select @NAME_PARAM=COFFEE_NAME from Longvarchar_Tab end !

drop procedure Lvarcharnull_Proc !
create procedure Lvarcharnull_Proc (@NULL_PARAM VARCHAR(30) out) as begin select @NULL_PARAM=NULL_VAL from Longvarcharnull_Tab end !

drop procedure Date_Proc !
create procedure Date_Proc (@MFG_PARAM DATETIME out, @NULL_PARAM DATETIME out) as begin select @MFG_PARAM=MFG_DATE,  @NULL_PARAM=NULL_VAL from Date_Tab end !

drop procedure Time_Proc !
create procedure Time_Proc (@BRK_PARAM DATETIME out, @NULL_PARAM DATETIME out) as begin select @BRK_PARAM=BRK_TIME, @NULL_PARAM=NULL_VAL from Time_Tab end !

drop procedure Timestamp_Proc !
create procedure Timestamp_Proc (@IN_PARAM DATETIME out, @NULL_PARAM DATETIME out) as begin select @IN_PARAM=IN_TIME, @NULL_PARAM=NULL_VAL  from Timestamp_Tab end !

drop procedure Binary_Proc !
create procedure Binary_Proc (@BINARY_PARAM BINARY(255) out) as begin select @BINARY_PARAM=BINARY_VAL from Binary_Tab end !

drop procedure Varbinary_Proc !
create procedure Varbinary_Proc (@VARBINARY_PARAM VARBINARY(255) out) as begin select @VARBINARY_PARAM=VARBINARY_VAL from Varbinary_Tab end !

drop procedure Longvarbinary_Proc !
create procedure Longvarbinary_Proc (@LONGVARBINARY_PARAM VARBINARY(255) out) as begin select @LONGVARBINARY_PARAM=LONGVARBINARY_VAL from Longvarbinary_Tab end !

drop procedure Integer_In_Proc !
create procedure Integer_In_Proc (@IN_PARAM INTEGER) as begin update Integer_Tab set MAX_VAL=@IN_PARAM end !

drop procedure Integer_InOut_Proc !
create procedure Integer_InOut_Proc (@INOUT_PARAM INTEGER out) as begin select @INOUT_PARAM=MAX_VAL from Integer_Tab where MIN_VAL=@INOUT_PARAM end !

drop procedure UpdCoffee_Proc !
create procedure UpdCoffee_Proc (@TYPE_PARAM NUMERIC) as begin update ctstable2 set PRICE=PRICE*20 where TYPE_ID=@TYPE_PARAM end !

drop procedure SelCoffee_Proc !
create procedure SelCoffee_Proc (@KEYID_PARAM NUMERIC out) as begin select @KEYID_PARAM=KEY_ID from ctstable2 where TYPE_ID=1 end !

drop procedure IOCoffee_Proc !
create procedure IOCoffee_Proc (@PRICE_PARAM FLOAT out) as begin select @PRICE_PARAM=PRICE*2 from ctstable2 where PRICE=@PRICE_PARAM end !

drop procedure Coffee_Proc !
create procedure Coffee_Proc(@TYPE_PARAM Numeric) as begin update ctstable2 set PRICE=PRICE*2 where TYPE_ID=@TYPE_PARAM delete from ctstable2 where TYPE_ID=@TYPE_PARAM-1 end !

drop procedure Numeric_Io_Max  !
create procedure Numeric_Io_Max (@MAX_PARAM NUMERIC(30,15) out) as begin update Numeric_Tab set MAX_VAL=@MAX_PARAM  select @MAX_PARAM=MAX_VAL from Numeric_Tab end !
drop procedure Numeric_Io_Min  !
create procedure Numeric_Io_Min (@MIN_PARAM NUMERIC(30,15) out) as begin update Numeric_Tab set MIN_VAL=@MIN_PARAM select @MIN_PARAM=MIN_VAL from Numeric_Tab end !
drop procedure Numeric_Io_Null  !
create procedure Numeric_Io_Null (@NULL_PARAM NUMERIC(30,15) out) as begin update Numeric_Tab set NULL_VAL=@NULL_PARAM select @NULL_PARAM=NULL_VAL from Numeric_Tab end !

drop procedure Decimal_Io_Max  !
create procedure Decimal_Io_Max (@MAX_PARAM DECIMAL(30,15) out) as begin update Decimal_Tab set MAX_VAL=@MAX_PARAM select @MAX_PARAM=MAX_VAL from Decimal_Tab end !
drop procedure Decimal_Io_Min  !
create procedure Decimal_Io_Min (@MIN_PARAM DECIMAL(30,15) out) as begin update Decimal_Tab set MIN_VAL=@MIN_PARAM select @MIN_PARAM=MIN_VAL from Decimal_Tab end !
drop procedure Decimal_Io_Null  !
create procedure Decimal_Io_Null (@NULL_PARAM DECIMAL(30,15) out) as begin update Decimal_Tab set NULL_VAL=@NULL_PARAM select @NULL_PARAM=NULL_VAL from Decimal_Tab end !

drop procedure Double_Io_Max  !
create procedure Double_Io_Max (@MAX_PARAM DOUBLE PRECISION out) as begin update Double_Tab set MAX_VAL=@MAX_PARAM select @MAX_PARAM=MAX_VAL from Double_Tab end !
drop procedure Double_Io_Min  !
create procedure Double_Io_Min (@MIN_PARAM DOUBLE PRECISION out) as begin update Double_Tab set MIN_VAL=@MIN_PARAM select @MIN_PARAM=MIN_VAL from Double_Tab end !
drop procedure Double_Io_Null  !
create procedure Double_Io_Null (@NULL_PARAM DOUBLE PRECISION out) as begin update Double_Tab set NULL_VAL=@NULL_PARAM select @NULL_PARAM=NULL_VAL from Double_Tab end !

drop procedure Float_Io_Max  !
create procedure Float_Io_Max (@MAX_PARAM FLOAT out) as begin update Float_Tab set MAX_VAL=@MAX_PARAM select @MAX_PARAM=MAX_VAL from Float_Tab end !
drop procedure Float_Io_Min  !
create procedure Float_Io_Min (@MIN_PARAM FLOAT out) as begin update Float_Tab set MIN_VAL=@MIN_PARAM select @MIN_PARAM=MIN_VAL from Float_Tab end !
drop procedure Float_Io_Null !
create procedure Float_Io_Null (@NULL_PARAM FLOAT out) as begin update Float_Tab set NULL_VAL=@NULL_PARAM select @NULL_PARAM=NULL_VAL from Float_Tab end !

drop procedure Real_Io_Max  !
create procedure Real_Io_Max (@MAX_PARAM REAL out) as begin update Real_Tab set MAX_VAL=@MAX_PARAM select @MAX_PARAM=MAX_VAL from Real_Tab end !
drop procedure Real_Io_Min  !
create procedure Real_Io_Min (@MIN_PARAM REAL out) as begin update Real_Tab set MIN_VAL=@MIN_PARAM select @MIN_PARAM=MIN_VAL from Real_Tab end !
drop procedure Real_Io_Null  !
create procedure Real_Io_Null (@NULL_PARAM REAL out) as begin update Real_Tab set NULL_VAL=@NULL_PARAM select @NULL_PARAM=NULL_VAL from Real_Tab end !

drop procedure Bit_Io_Max  !
create procedure Bit_Io_Max (@MAX_PARAM BIT out) as begin update Bit_Tab set MAX_VAL=@MAX_PARAM select @MAX_PARAM=MAX_VAL from Bit_Tab end !
drop procedure Bit_Io_Min  !
create procedure Bit_Io_Min (@MIN_PARAM BIT out) as begin update Bit_Tab set MIN_VAL=@MIN_PARAM select @MIN_PARAM=MIN_VAL from Bit_Tab end !
drop procedure Bit_Io_Null  !
create procedure Bit_Io_Null (@NULL_PARAM SMALLINT out) as begin update Bit_Tab set NULL_VAL=@NULL_PARAM select @NULL_PARAM=NULL_VAL from Bit_Tab end !

drop procedure Smallint_Io_Max  !
create procedure Smallint_Io_Max (@MAX_PARAM SMALLINT out) as begin update Smallint_Tab set MAX_VAL=@MAX_PARAM select @MAX_PARAM=MAX_VAL from Smallint_Tab end !
drop procedure Smallint_Io_Min  !
create procedure Smallint_Io_Min (@MIN_PARAM SMALLINT out) as begin update Smallint_Tab set MIN_VAL=@MIN_PARAM select @MIN_PARAM=MIN_VAL from Smallint_Tab end !
drop procedure Smallint_Io_Null  !
create procedure Smallint_Io_Null (@NULL_PARAM SMALLINT out) as begin update Smallint_Tab set NULL_VAL=@NULL_PARAM select @NULL_PARAM=NULL_VAL from Smallint_Tab end !

drop procedure Tinyint_Io_Max  !
create procedure Tinyint_Io_Max (@MAX_PARAM TINYINT out) as begin update Tinyint_Tab set MAX_VAL=@MAX_PARAM select @MAX_PARAM=MAX_VAL from Tinyint_Tab end !
drop procedure Tinyint_Io_Min  !
create procedure Tinyint_Io_Min (@MIN_PARAM TINYINT out) as begin update Tinyint_Tab set MIN_VAL=@MIN_PARAM select @MIN_PARAM=MIN_VAL from Tinyint_Tab end !
drop procedure Tinyint_Io_Null  !
create procedure Tinyint_Io_Null (@NULL_PARAM TINYINT out) as begin update Tinyint_Tab set NULL_VAL=@NULL_PARAM select @NULL_PARAM=NULL_VAL from Tinyint_Tab end !

drop procedure Integer_Io_Max  !
create procedure Integer_Io_Max (@MAX_PARAM INTEGER out) as begin update Integer_Tab set MAX_VAL=@MAX_PARAM select @MAX_PARAM=MAX_VAL from Integer_Tab end !
drop procedure Integer_Io_Min  !
create procedure Integer_Io_Min (@MIN_PARAM INTEGER out) as begin update Integer_Tab set MIN_VAL=@MIN_PARAM select @MIN_PARAM=MIN_VAL from Integer_Tab end !
drop procedure Integer_Io_Null  !
create procedure Integer_Io_Null (@NULL_PARAM INTEGER out) as begin update Integer_Tab set NULL_VAL=@NULL_PARAM select @NULL_PARAM=NULL_VAL from Integer_Tab end !

drop procedure Bigint_Io_Max  !
create procedure Bigint_Io_Max (@MAX_PARAM NUMERIC(20,0) out) as begin update Bigint_Tab set MAX_VAL=@MAX_PARAM select @MAX_PARAM=MAX_VAL from Bigint_Tab end !
drop procedure Bigint_Io_Min  !
create procedure Bigint_Io_Min (@MIN_PARAM NUMERIC(20,0) out) as begin update Bigint_Tab set MIN_VAL=@MIN_PARAM select @MIN_PARAM=MIN_VAL from Bigint_Tab end !
drop procedure Bigint_Io_Null  !
create procedure Bigint_Io_Null (@NULL_PARAM NUMERIC(20,0) out) as begin update Bigint_Tab set NULL_VAL=@NULL_PARAM select @NULL_PARAM=NULL_VAL from Bigint_Tab end !

drop procedure Char_Io_Name !
create procedure Char_Io_Name(@NAME_PARAM CHAR(30) out) as begin update Char_Tab set COFFEE_NAME=@NAME_PARAM select @NAME_PARAM=COFFEE_NAME from Char_Tab end !
drop procedure Char_Io_Null !
create procedure Char_Io_Null(@NULL_PARAM CHAR(30) out) as begin update Char_Tab set NULL_VAL=@NULL_PARAM select @NULL_PARAM=NULL_VAL from Char_Tab end !

drop procedure Varchar_Io_Name !
create procedure Varchar_Io_Name(@NAME_PARAM VARCHAR(30) out) as begin update Varchar_Tab set COFFEE_NAME=@NAME_PARAM select @NAME_PARAM=COFFEE_NAME from Varchar_Tab end !
drop procedure Varchar_Io_Null !
create procedure Varchar_Io_Null(@NULL_PARAM VARCHAR(30) out) as begin update Varchar_Tab set NULL_VAL=@NULL_PARAM select @NULL_PARAM=NULL_VAL from Varchar_Tab end !

drop procedure Lvarchar_Io_Name !
create procedure Lvarchar_Io_Name(@NAME_PARAM VARCHAR(30) out) as begin update Longvarchar_Tab set COFFEE_NAME=@NAME_PARAM select @NAME_PARAM=COFFEE_NAME from Longvarchar_Tab end !
drop procedure Lvarchar_Io_Null !
create procedure Lvarchar_Io_Null(@NULL_PARAM VARCHAR(30) out  )as begin update Longvarcharnull_Tab set NULL_VAL=@NULL_PARAM select @NULL_PARAM=NULL_VAL from Longvarcharnull_Tab end !

drop procedure Date_Io_Mfg !
create procedure Date_Io_Mfg(@MFG_PARAM DATETIME out) as begin update Date_Tab set MFG_DATE=@MFG_PARAM select @MFG_PARAM=MFG_DATE from Date_Tab end !
drop procedure Date_Io_Null !
create procedure Date_Io_Null(@NULL_PARAM DATETIME out) as begin update Date_Tab set NULL_VAL=@NULL_PARAM select @NULL_PARAM=NULL_VAL from Date_Tab end !

drop procedure Time_Io_Brk !
create procedure Time_Io_Brk(@BRK_PARAM DATETIME out) as begin update Time_Tab set BRK_TIME=@BRK_PARAM select @BRK_PARAM=BRK_TIME from Time_Tab end !
drop procedure Time_Io_Null !
create procedure Time_Io_Null(@NULL_PARAM DATETIME out) as begin update Time_Tab set NULL_VAL=@NULL_PARAM select @NULL_PARAM=NULL_VAL from Time_Tab end !

drop procedure Timestamp_Io_Intime !
create procedure Timestamp_Io_Intime(@INTIME_PARAM DATETIME out) as begin update Timestamp_Tab set IN_TIME=@INTIME_PARAM select @INTIME_PARAM=IN_TIME from Timestamp_Tab end !
drop procedure Timestamp_Io_Null !
create procedure Timestamp_Io_Null(@NULL_PARAM DATETIME out) as begin update Timestamp_Tab set NULL_VAL=@NULL_PARAM select @NULL_PARAM=NULL_VAL from Timestamp_Tab end !

drop procedure Binary_Proc_Io !
create procedure Binary_Proc_Io(@BINARY_PARAM BINARY(24) out) as begin update Binary_Tab set BINARY_VAL=@BINARY_PARAM select @BINARY_PARAM=BINARY_VAL from Binary_Tab end !

drop procedure Varbinary_Proc_Io !
create procedure Varbinary_Proc_Io(@VARBINARY_PARAM VARBINARY(255) out) as begin update Varbinary_Tab set VARBINARY_VAL=@VARBINARY_PARAM select @VARBINARY_PARAM=VARBINARY_VAL from Varbinary_Tab end !

drop procedure Longvarbinary_Io !
create procedure Longvarbinary_Io(@LONGVARBINARY_PARAM VARBINARY(255) out) as begin update Longvarbinary_Tab set LONGVARBINARY_VAL=@LONGVARBINARY_PARAM select @LONGVARBINARY_PARAM=LONGVARBINARY_VAL from Longvarbinary_Tab end !


drop procedure Numeric_In_Max  !
create procedure Numeric_In_Max (@MAX_PARAM NUMERIC(30,15)) as begin update Numeric_Tab set MAX_VAL=@MAX_PARAM  end !
drop procedure Numeric_In_Min  !
create procedure Numeric_In_Min (@MIN_PARAM NUMERIC(30,15)) as begin update Numeric_Tab set MIN_VAL=@MIN_PARAM end !
drop procedure Numeric_In_Null  !
create procedure Numeric_In_Null (@NULL_PARAM NUMERIC(30,15)) as begin update Numeric_Tab set NULL_VAL=@NULL_PARAM end !

drop procedure Decimal_In_Max  !
create procedure Decimal_In_Max (@MAX_PARAM DECIMAL(30,15)) as begin update Decimal_Tab set MAX_VAL=@MAX_PARAM  end !
drop procedure Decimal_In_Min  !
create procedure Decimal_In_Min (@MIN_PARAM DECIMAL(30,15)) as begin update Decimal_Tab set MIN_VAL=@MIN_PARAM  end !
drop procedure Decimal_In_Null  !
create procedure Decimal_In_Null (@NULL_PARAM DECIMAL(30,15)) as begin update Decimal_Tab set NULL_VAL=@NULL_PARAM  end !

drop procedure Double_In_Max  !
create procedure Double_In_Max (@MAX_PARAM DOUBLE PRECISION) as begin update Double_Tab set MAX_VAL=@MAX_PARAM end !
drop procedure Double_In_Min  !
create procedure Double_In_Min (@MIN_PARAM DOUBLE PRECISION) as begin update Double_Tab set MIN_VAL=@MIN_PARAM end !
drop procedure Double_In_Null  !
create procedure Double_In_Null (@NULL_PARAM DOUBLE PRECISION) as begin update Double_Tab set NULL_VAL=@NULL_PARAM end !

drop procedure Float_In_Max  !
create procedure Float_In_Max (@MAX_PARAM FLOAT) as begin update Float_Tab set MAX_VAL=@MAX_PARAM end !
drop procedure Float_In_Min  !
create procedure Float_In_Min (@MIN_PARAM FLOAT) as begin update Float_Tab set MIN_VAL=@MIN_PARAM end !
drop procedure Float_In_Null !
create procedure Float_In_Null (@NULL_PARAM FLOAT) as begin update Float_Tab set NULL_VAL=@NULL_PARAM end !

drop procedure Real_In_Max  !
create procedure Real_In_Max (@MAX_PARAM REAL) as begin update Real_Tab set MAX_VAL=@MAX_PARAM end !
drop procedure Real_In_Min  !
create procedure Real_In_Min (@MIN_PARAM REAL) as begin update Real_Tab set MIN_VAL=@MIN_PARAM end !
drop procedure Real_In_Null  !
create procedure Real_In_Null (@NULL_PARAM REAL) as begin update Real_Tab set NULL_VAL=@NULL_PARAM end !

drop procedure Bit_In_Max  !
create procedure Bit_In_Max (@MAX_PARAM BIT) as begin update Bit_Tab set MAX_VAL=@MAX_PARAM end !
drop procedure Bit_In_Min  !
create procedure Bit_In_Min (@MIN_PARAM BIT) as begin update Bit_Tab set MIN_VAL=@MIN_PARAM end !
drop procedure Bit_In_Null  !
create procedure Bit_In_Null (@NULL_PARAM SMALLINT) as begin update Bit_Tab set NULL_VAL=@NULL_PARAM end !

drop procedure Smallint_In_Max  !
create procedure Smallint_In_Max (@MAX_PARAM SMALLINT) as begin update Smallint_Tab set MAX_VAL=@MAX_PARAM end !
drop procedure Smallint_In_Min  !
create procedure Smallint_In_Min (@MIN_PARAM SMALLINT) as begin update Smallint_Tab set MIN_VAL=@MIN_PARAM end !
drop procedure Smallint_In_Null  !
create procedure Smallint_In_Null (@NULL_PARAM SMALLINT) as begin update Smallint_Tab set NULL_VAL=@NULL_PARAM end !

drop procedure Tinyint_In_Max  !
create procedure Tinyint_In_Max (@MAX_PARAM TINYINT) as begin update Tinyint_Tab set MAX_VAL=@MAX_PARAM end !
drop procedure Tinyint_In_Min  !
create procedure Tinyint_In_Min (@MIN_PARAM TINYINT) as begin update Tinyint_Tab set MIN_VAL=@MIN_PARAM end !
drop procedure Tinyint_In_Null  !
create procedure Tinyint_In_Null (@NULL_PARAM TINYINT) as begin update Tinyint_Tab set NULL_VAL=@NULL_PARAM end !

drop procedure Integer_In_Max  !
create procedure Integer_In_Max (@MAX_PARAM INTEGER) as begin update Integer_Tab set MAX_VAL=@MAX_PARAM end !
drop procedure Integer_In_Min  !
create procedure Integer_In_Min (@MIN_PARAM INTEGER) as begin update Integer_Tab set MIN_VAL=@MIN_PARAM end !

drop procedure Integer_In_Null  !
create procedure Integer_In_Null (@NULL_PARAM INTEGER) as begin update Integer_Tab set NULL_VAL=@NULL_PARAM end !

drop procedure Bigint_In_Max  !
create procedure Bigint_In_Max (@MAX_PARAM NUMERIC(20,0)) as begin update Bigint_Tab set MAX_VAL=@MAX_PARAM end !
drop procedure Bigint_In_Min  !
create procedure Bigint_In_Min (@MIN_PARAM NUMERIC(20,0)) as begin update Bigint_Tab set MIN_VAL=@MIN_PARAM end !
drop procedure Bigint_In_Null  !
create procedure Bigint_In_Null (@NULL_PARAM NUMERIC(20,0)) as begin update Bigint_Tab set NULL_VAL=@NULL_PARAM end !

drop procedure Char_In_Name !
create procedure Char_In_Name(@NAME_PARAM CHAR(30)) as begin update Char_Tab set COFFEE_NAME=@NAME_PARAM end !
drop procedure Char_In_Null !
create procedure Char_In_Null(@NULL_PARAM CHAR(30)) as begin update Char_Tab set NULL_VAL=@NULL_PARAM end !

drop procedure Varchar_In_Name !
create procedure Varchar_In_Name(@NAME_PARAM VARCHAR(30)) as begin update Varchar_Tab set COFFEE_NAME=@NAME_PARAM  end !

drop procedure Varchar_In_Null !
create procedure Varchar_In_Null(@NULL_PARAM VARCHAR(30)) as begin update Varchar_Tab set NULL_VAL=@NULL_PARAM  end !

drop procedure Lvarchar_In_Name !
create procedure Lvarchar_In_Name(@NAME_PARAM VARCHAR(30)) as begin update Longvarchar_Tab set COFFEE_NAME=@NAME_PARAM end !

drop procedure Lvarchar_In_Null !
create procedure Lvarchar_In_Null(@NULL_PARAM VARCHAR(30)) as begin update Longvarcharnull_Tab set NULL_VAL=@NULL_PARAM end !

drop procedure Date_In_Mfg !
create procedure Date_In_Mfg(@MFG_PARAM DATETIME) as begin update Date_Tab set MFG_DATE=@MFG_PARAM end !

drop procedure Date_In_Null !
create procedure Date_In_Null(@NULL_PARAM DATETIME) as begin update Date_Tab set NULL_VAL=@NULL_PARAM end !

drop procedure Time_In_Brk !
create procedure Time_In_Brk(@BRK_PARAM DATETIME) as begin update Time_Tab set BRK_TIME=@BRK_PARAM end !

drop procedure Time_In_Null !
create procedure Time_In_Null(@NULL_PARAM DATETIME) as begin update Time_Tab set NULL_VAL=@NULL_PARAM end !

drop procedure Timestamp_In_Intime !
create procedure Timestamp_In_Intime(@INTIME_PARAM DATETIME) as begin update Timestamp_Tab set IN_TIME=@INTIME_PARAM end !

drop procedure Timestamp_In_Null !
create procedure Timestamp_In_Null(@NULL_PARAM DATETIME) as begin update Timestamp_Tab set NULL_VAL=@NULL_PARAM end !

drop procedure Binary_Proc_In !
create procedure Binary_Proc_In(@BINARY_PARAM BINARY(24)) as begin update Binary_Tab set BINARY_VAL=@BINARY_PARAM end !

drop procedure Varbinary_Proc_In !
create procedure Varbinary_Proc_In(@VARBINARY_PARAM VARBINARY(255)) as begin update Varbinary_Tab set VARBINARY_VAL=@VARBINARY_PARAM end !

drop procedure Longvarbinary_In !
create procedure Longvarbinary_In(@LONGVARBINARY_PARAM VARBINARY(255)) as begin update Longvarbinary_Tab set LONGVARBINARY_VAL=@LONGVARBINARY_PARAM end !




grant all on ctstable2 to public !

grant all on ctstable1 to public !

grant all on Numeric_Tab to public !

grant all on Decimal_Tab to public !

grant all on Double_Tab to public !

grant all on Float_Tab to public !

grant all on Real_Tab to public !

grant all on Bit_Tab to public !

grant all on Smallint_Tab to public !

grant all on Tinyint_Tab to public !

grant all on Integer_Tab to public !

grant all on Bigint_Tab to public !

grant all on Char_Tab to public !

grant all on Varchar_Tab to public !

grant all on Longvarchar_Tab to public !

grant all on Longvarcharnull_Tab to public !

grant all on Date_Tab to public !

grant all on Time_Tab to public !

grant all on Timestamp_Tab to public !

grant all on Binary_Tab to public !

grant all on Varbinary_Tab to public !

grant all on Longvarbinary_Tab to public !

grant all on ctstable3 to public !
grant all on ctstable4 to public !

grant all on TxBean_Tab1 to public !
grant all on TxBean_Tab2 to public !

grant all on TxEBean_Tab to public !

grant all on BB_Tab to public !

grant all on Integration_Tab to public !
grant all on Integration_Sec_Tab to public !

grant all on JTA_Tab1 to public !
grant all on JTA_Tab2 to public !

grant all on Deploy_Tab1 to public !

grant all on Deploy_Tab2 to public !

grant all on Deploy_Tab3 to public !

grant all on Deploy_Tab4 to public !

grant all on Deploy_Tab5 to public !

grant all on Xa_Tab1 to public !
grant all on Xa_Tab2 to public !

grant all on SEC_Tab1 to public !

grant all on Connector_Tab to public !

grant execute on Numeric_Proc to public !

grant execute on Decimal_Proc to public !

grant execute on Double_Proc to public !

grant execute on Float_Proc to public !

grant execute on Real_Proc to public !

grant execute on Bit_Proc to public !

grant execute on Smallint_Proc to public !

grant execute on Tinyint_Proc to public !

grant execute on Integer_Proc to public !

grant execute on Bigint_Proc to public !

grant execute on Char_Proc to public !

grant execute on Varchar_Proc to public !

grant execute on Longvarchar_Proc to public !

grant execute on Lvarcharnull_Proc to public !

grant execute on Date_Proc to public !

grant execute on Time_Proc to public !

grant execute on Timestamp_Proc to public !

grant execute on Binary_Proc to public !

grant execute on Varbinary_Proc to public !

grant execute on Longvarbinary_Proc to public !

grant execute on Integer_In_Proc to public !

grant execute on Integer_InOut_Proc to public !

grant execute on UpdCoffee_Proc to public !

grant execute on SelCoffee_Proc to public !

grant execute on IOCoffee_Proc to public !

grant execute on Coffee_Proc to public !

grant execute on Numeric_Io_Max to public !

grant execute on Numeric_Io_Min to public !

grant execute on Numeric_Io_Null to public !

grant execute on Decimal_Io_Max to public !

grant execute on Decimal_Io_Min to public !

grant execute on Decimal_Io_Null to public !

grant execute on Double_Io_Max to public !

grant execute on Double_Io_Min to public !

grant execute on Double_Io_Null to public !

grant execute on Float_Io_Max to public !

grant execute on Float_Io_Min to public !

grant execute on Float_Io_Null to public !

grant execute on Real_Io_Max to public !

grant execute on Real_Io_Min to public !

grant execute on Real_Io_Null to public !

grant execute on Bit_Io_Max to public !

grant execute on Bit_Io_Min to public !

grant execute on Bit_Io_Null to public !

grant execute on Smallint_Io_Max to public !

grant execute on Smallint_Io_Min to public !

grant execute on Smallint_Io_Null to public !

grant execute on Tinyint_Io_Max to public !

grant execute on Tinyint_Io_Min to public !

grant execute on Tinyint_Io_Null to public !

grant execute on Integer_Io_Max to public !

grant execute on Integer_Io_Min to public !

grant execute on Integer_Io_Null to public !

grant execute on Bigint_Io_Max to public !

grant execute on Bigint_Io_Min to public !

grant execute on Bigint_Io_Null to public !

grant execute on Char_Io_Name to public !

grant execute on Char_Io_Null to public !

grant execute on Varchar_Io_Name to public !

grant execute on Varchar_Io_Null to public !

grant execute on Lvarchar_Io_Name to public !

grant execute on Lvarchar_Io_Null to public !

grant execute on Date_Io_Mfg to public !

grant execute on Date_Io_Null to public !

grant execute on Time_Io_Brk to public !

grant execute on Time_Io_Null to public !

grant execute on Timestamp_Io_Intime to public !

grant execute on Timestamp_Io_Null to public !

grant execute on Binary_Proc_Io to public !

grant execute on Varbinary_Proc_Io to public !

grant execute on Longvarbinary_Io to public !

grant execute on Numeric_In_Max to public !

grant execute on Numeric_In_Min to public !

grant execute on Numeric_In_Null to public !

grant execute on Decimal_In_Max to public !

grant execute on Decimal_In_Min to public !

grant execute on Decimal_In_Null to public !

grant execute on Double_In_Max to public !

grant execute on Double_In_Min to public !

grant execute on Double_In_Null to public !

grant execute on Float_In_Max to public !

grant execute on Float_In_Min to public !

grant execute on Float_In_Null to public !

grant execute on Real_In_Max to public !

grant execute on Real_In_Min to public !

grant execute on Real_In_Null to public !

grant execute on Bit_In_Max to public !

grant execute on Bit_In_Min to public !

grant execute on Bit_In_Null to public !

grant execute on Smallint_In_Max to public !

grant execute on Smallint_In_Min to public !

grant execute on Smallint_In_Null to public !

grant execute on Tinyint_In_Max to public !

grant execute on Tinyint_In_Min to public !

grant execute on Tinyint_In_Null to public !

grant execute on Integer_In_Max to public !

grant execute on Integer_In_Min to public !

grant execute on Integer_In_Null to public !

grant execute on Bigint_In_Max to public !

grant execute on Bigint_In_Min to public !

grant execute on Bigint_In_Null to public !

grant execute on Char_In_Name to public !

grant execute on Char_In_Null to public !

grant execute on Varchar_In_Name to public !

grant execute on Varchar_In_Null to public !

grant execute on Lvarchar_In_Name to public !

grant execute on Lvarchar_In_Null to public !

grant execute on Date_In_Mfg to public !

grant execute on Date_In_Null to public !

grant execute on Time_In_Brk to public !

grant execute on Time_In_Null to public !

grant execute on Timestamp_In_Intime to public !

grant execute on Timestamp_In_Null to public !

grant execute on Binary_Proc_In to public !

grant execute on Varbinary_Proc_In to public !

grant execute on Longvarbinary_In to public !
exec sp_procxmode  Numeric_Proc, anymode !

exec sp_procxmode  Decimal_Proc, anymode !

exec sp_procxmode  Double_Proc, anymode !

exec sp_procxmode  Float_Proc, anymode !

exec sp_procxmode  Real_Proc, anymode !

exec sp_procxmode  Bit_Proc, anymode !

exec sp_procxmode  Smallint_Proc, anymode !

exec sp_procxmode  Tinyint_Proc, anymode !

exec sp_procxmode  Integer_Proc, anymode !

exec sp_procxmode  Bigint_Proc, anymode !

exec sp_procxmode  Char_Proc, anymode !

exec sp_procxmode  Varchar_Proc, anymode !

exec sp_procxmode  Longvarchar_Proc, anymode !

exec sp_procxmode  Date_Proc, anymode !

exec sp_procxmode  Time_Proc, anymode !

exec sp_procxmode  Timestamp_Proc, anymode !

exec sp_procxmode  Binary_Proc, anymode !

exec sp_procxmode  Varbinary_Proc, anymode !

exec sp_procxmode  Longvarbinary_Proc, anymode !

exec sp_procxmode  Integer_In_Proc, anymode !

exec sp_procxmode  Integer_InOut_Proc, anymode !

exec sp_procxmode  UpdCoffee_Proc, anymode !

exec sp_procxmode  SelCoffee_Proc, anymode !

exec sp_procxmode  IOCoffee_Proc, anymode !

exec sp_procxmode  Coffee_Proc, anymode !

exec sp_procxmode  Numeric_Io_Max , anymode !
exec sp_procxmode  Numeric_Io_Min , anymode !
exec sp_procxmode  Numeric_Io_Null , anymode !

exec sp_procxmode  Decimal_Io_Max , anymode !
exec sp_procxmode  Decimal_Io_Min , anymode !
exec sp_procxmode  Decimal_Io_Null , anymode !

exec sp_procxmode  Double_Io_Max , anymode !
exec sp_procxmode  Double_Io_Min , anymode !
exec sp_procxmode  Double_Io_Null , anymode !

exec sp_procxmode  Float_Io_Max , anymode !
exec sp_procxmode  Float_Io_Min , anymode !
exec sp_procxmode  Float_Io_Null, anymode !

exec sp_procxmode  Real_Io_Max , anymode !
exec sp_procxmode  Real_Io_Min , anymode !
exec sp_procxmode  Real_Io_Null , anymode !

exec sp_procxmode  Bit_Io_Max , anymode !
exec sp_procxmode  Bit_Io_Min , anymode !
exec sp_procxmode  Bit_Io_Null , anymode !

exec sp_procxmode  Smallint_Io_Max , anymode !
exec sp_procxmode  Smallint_Io_Min , anymode !
exec sp_procxmode  Smallint_Io_Null , anymode !

exec sp_procxmode  Tinyint_Io_Max , anymode !
exec sp_procxmode  Tinyint_Io_Min , anymode !
exec sp_procxmode  Tinyint_Io_Null , anymode !

exec sp_procxmode  Integer_Io_Max , anymode !
exec sp_procxmode  Integer_Io_Min , anymode !
exec sp_procxmode  Integer_Io_Null , anymode !

exec sp_procxmode  Bigint_Io_Max , anymode !
exec sp_procxmode  Bigint_Io_Min , anymode !
exec sp_procxmode  Bigint_Io_Null , anymode !

exec sp_procxmode  Char_Io_Name, anymode !
exec sp_procxmode  Char_Io_Null, anymode !

exec sp_procxmode  Varchar_Io_Name, anymode !
exec sp_procxmode  Varchar_Io_Null, anymode !

exec sp_procxmode  Lvarchar_Io_Name, anymode !
exec sp_procxmode  Lvarchar_Io_Null, anymode !

exec sp_procxmode  Date_Io_Mfg, anymode !
exec sp_procxmode  Date_Io_Null, anymode !

exec sp_procxmode  Time_Io_Brk, anymode !
exec sp_procxmode  Time_Io_Null, anymode !

exec sp_procxmode  Timestamp_Io_Intime, anymode !
exec sp_procxmode  Timestamp_Io_Null, anymode !

exec sp_procxmode  Binary_Proc_Io, anymode !

exec sp_procxmode  Varbinary_Proc_Io, anymode !

exec sp_procxmode  Longvarbinary_Io, anymode !

exec sp_procxmode  Numeric_In_Max , anymode !
exec sp_procxmode  Numeric_In_Min , anymode !
exec sp_procxmode  Numeric_In_Null , anymode !

exec sp_procxmode  Decimal_In_Max , anymode !
exec sp_procxmode  Decimal_In_Min , anymode !
exec sp_procxmode  Decimal_In_Null , anymode !

exec sp_procxmode  Double_In_Max , anymode !
exec sp_procxmode  Double_In_Min , anymode !
exec sp_procxmode  Double_In_Null , anymode !

exec sp_procxmode  Float_In_Max , anymode !
exec sp_procxmode  Float_In_Min , anymode !
exec sp_procxmode  Float_In_Null, anymode !

exec sp_procxmode  Real_In_Max , anymode !
exec sp_procxmode  Real_In_Min , anymode !
exec sp_procxmode  Real_In_Null , anymode !

exec sp_procxmode  Bit_In_Max , anymode !
exec sp_procxmode  Bit_In_Min , anymode !
exec sp_procxmode  Bit_In_Null , anymode !

exec sp_procxmode  Smallint_In_Max , anymode !
exec sp_procxmode  Smallint_In_Min , anymode !
exec sp_procxmode  Smallint_In_Null , anymode !

exec sp_procxmode  Tinyint_In_Max , anymode !
exec sp_procxmode  Tinyint_In_Min , anymode !
exec sp_procxmode  Tinyint_In_Null , anymode !

exec sp_procxmode  Integer_In_Max , anymode !
exec sp_procxmode  Integer_In_Min , anymode !
exec sp_procxmode  Integer_In_Null , anymode !

exec sp_procxmode  Bigint_In_Max , anymode !
exec sp_procxmode  Bigint_In_Min , anymode !
exec sp_procxmode  Bigint_In_Null , anymode !

exec sp_procxmode  Char_In_Name, anymode !
exec sp_procxmode  Char_In_Null, anymode !

exec sp_procxmode  Varchar_In_Name, anymode !
exec sp_procxmode  Varchar_In_Null, anymode !

exec sp_procxmode  Lvarchar_In_Name, anymode !
exec sp_procxmode  Lvarchar_In_Null, anymode !

exec sp_procxmode  Date_In_Mfg, anymode !
exec sp_procxmode  Date_In_Null, anymode !

exec sp_procxmode  Time_In_Brk, anymode !
exec sp_procxmode  Time_In_Null, anymode !

exec sp_procxmode  Timestamp_In_Intime, anymode !
exec sp_procxmode  Timestamp_In_Null, anymode !

exec sp_procxmode  Binary_Proc_In, anymode !

exec sp_procxmode  Varbinary_Proc_In, anymode !

exec sp_procxmode  Longvarbinary_In, anymode !

exec sp_procxmode  Lvarcharnull_Proc, anymode !
