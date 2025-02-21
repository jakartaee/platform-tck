drop procedure Numeric_Proc (NUMERIC, NUMERIC,NUMERIC) ;
create procedure Numeric_Proc (out MAX_PARAM NUMERIC, out MIN_PARAM NUMERIC, out NULL_PARAM NUMERIC) LANGUAGE  JAVA SPECIFIC Numeric_Proc DETERMINISTIC READS SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Numeric_Proc" PARAMETER STYLE SQL ;

drop procedure Decimal_Proc (DECIMAL, DECIMAL, DECIMAL)  ;
create procedure Decimal_Proc (out MAX_PARAM DECIMAL, out MIN_PARAM DECIMAL, out NULL_PARAM DECIMAL) LANGUAGE  JAVA SPECIFIC Decimal_Proc DETERMINISTIC READS SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Decimal_Proc" PARAMETER STYLE SQL ;

drop procedure Double_Proc (DOUBLE PRECISION, DOUBLE PRECISION, DOUBLE PRECISION)  ;
create procedure Double_Proc (out MAX_PARAM DOUBLE PRECISION, out MIN_PARAM DOUBLE PRECISION, out NULL_PARAM  DOUBLE PRECISION) LANGUAGE JAVA SPECIFIC Double_Proc DETERMINISTIC READS SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Double_Proc"  PARAMETER STYLE SQL ;

drop procedure Float_Proc (FLOAT, FLOAT, FLOAT)  ;
create procedure Float_Proc (out MAX_PARAM FLOAT, out MIN_PARAM FLOAT, out NULL_PARAM FLOAT) LANGUAGE JAVA  SPECIFIC Float_Proc DETERMINISTIC READS SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Float_Proc" PARAMETER STYLE SQL ;

drop procedure Real_Proc (REAL, REAL, REAL)  ;
create procedure Real_Proc (out MAX_PARAM REAL, out MIN_PARAM REAL, out NULL_PARAM REAL) LANGUAGE JAVA SPECIFIC  Real_Proc DETERMINISTIC READS SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Real_Proc" PARAMETER STYLE SQL ;

drop procedure Bit_Proc (SMALLINT, SMALLINT, SMALLINT)  ;
create procedure Bit_Proc (out MAX_PARAM SMALLINT, out MIN_PARAM SMALLINT, out NULL_PARAM SMALLINT) LANGUAGE JAVA  SPECIFIC Bit_Proc DETERMINISTIC READS SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Bit_Proc" PARAMETER STYLE SQL ;

drop procedure Smallint_Proc (SMALLINT, SMALLINT, SMALLINT)   ;
create procedure Smallint_Proc (out MAX_PARAM SMALLINT, out MIN_PARAM SMALLINT, out NULL_PARAM SMALLINT)  LANGUAGE JAVA SPECIFIC Smallint_Proc DETERMINISTIC READS SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Smallint_Proc" PARAMETER STYLE  SQL ;
  ;
drop procedure Tinyint_Proc (SMALLINT, SMALLINT, SMALLINT)   ;
create procedure Tinyint_Proc (out MAX_PARAM SMALLINT, out MIN_PARAM SMALLINT, out NULL_PARAM SMALLINT)  LANGUAGE JAVA SPECIFIC Tinyint_Proc DETERMINISTIC READS SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Tinyint_Proc" PARAMETER STYLE  SQL ;

drop procedure Integer_Proc (INTEGER, INTEGER, INTEGER)  ;
create procedure Integer_Proc (out MAX_PARAM INTEGER, out MIN_PARAM INTEGER, out NULL_PARAM INTEGER) LANGUAGE  JAVA SPECIFIC Integer_Proc DETERMINISTIC READS SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Integer_Proc" PARAMETER STYLE SQL ;

drop procedure Bigint_Proc (BIGINT, BIGINT, BIGINT)  ;
create procedure Bigint_Proc (out MAX_PARAM BIGINT, out MIN_PARAM BIGINT, out NULL_PARAM BIGINT) LANGUAGE  JAVA SPECIFIC Bigint_Proc DETERMINISTIC READS SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Bigint_Proc" PARAMETER STYLE SQL ;

drop procedure Char_Proc (CHAR, CHAR)  ;
create procedure Char_Proc (out NAME_PARAM CHAR, out NULL_PARAM CHAR) LANGUAGE JAVA SPECIFIC Char_Proc  DETERMINISTIC READS SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Char_Proc" PARAMETER STYLE SQL ;

drop procedure Varchar_Proc (VARCHAR, VARCHAR)  ;
create procedure Varchar_Proc (out NAME_PARAM VARCHAR, out NULL_PARAM VARCHAR) LANGUAGE JAVA SPECIFIC  Varchar_Proc DETERMINISTIC READS SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Varchar_Proc" PARAMETER STYLE SQL ;

drop procedure Longvarchar_Proc (CLOB(2000)) ;
create procedure Longvarchar_Proc (out NAME_PARAM CLOB(2000)) LANGUAGE JAVA SPECIFIC Longvarchar_Proc  DETERMINISTIC READS SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Longvarchar_Proc" PARAMETER STYLE SQL  ;

drop procedure Lvarcharnull_Proc (CLOB(2000))  ;
create procedure Lvarcharnull_Proc (out NULL_PARAM CLOB(2000)) LANGUAGE JAVA SPECIFIC  Longvarcharnull_Proc DETERMINISTIC READS SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Longvarcharnull_Proc" PARAMETER STYLE SQL ;

drop procedure Date_Proc (DATE, DATE)  ;
create procedure Date_Proc (out MFG_PARAM DATE, out NULL_PARAM DATE) LANGUAGE JAVA SPECIFIC Date_Proc  DETERMINISTIC READS SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Date_Proc" PARAMETER STYLE SQL  ;

drop procedure Time_Proc (DATE, DATE)  ;
create procedure Time_Proc (out BRK_PARAM TIME, out NULL_PARAM TIME) LANGUAGE JAVA SPECIFIC Time_Proc  DETERMINISTIC READS SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Time_Proc" PARAMETER STYLE SQL  ;

drop procedure Timestamp_Proc (TIMESTAMP, TIMESTAMP)  ;
create procedure Timestamp_Proc (out IN_PARAM TIMESTAMP, out NULL_PARAM TIMESTAMP) LANGUAGE JAVA SPECIFIC  Timestamp_Proc DETERMINISTIC READS SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Timestamp_Proc" PARAMETER STYLE SQL   ;

drop procedure Binary_Proc (LONGVARBINARY(24000)) ;
create procedure Binary_Proc (out BINARY_PARAM LONGVARBINARY(24000)) LANGUAGE JAVA SPECIFIC Binary_Proc DETERMINISTIC  READS SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Binary_Proc" PARAMETER STYLE SQL ;

drop procedure Varbinary_Proc (LONGVARBINARY(48000))  ;
create procedure Varbinary_Proc (out VARBINARY_PARAM LONGVARBINARY(48000)) LANGUAGE JAVA SPECIFIC Varbinary_Proc  DETERMINISTIC READS SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Varbinary_Proc" PARAMETER STYLE SQL ;
  ;
drop procedure Longvarbinary_Proc (LONGVARBINARY(20000))  ;
create procedure Longvarbinary_Proc (out LONGVARBINARY_PARAM LONGVARBINARY(20000)) LANGUAGE JAVA SPECIFIC  Longvarbinary_Proc DETERMINISTIC READS SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Longvarbinary_Proc" PARAMETER STYLE SQL  ;

drop procedure Integer_In_Proc (INTEGER)  ;
create procedure Integer_In_Proc (in IN_PARAM INTEGER) LANGUAGE JAVA SPECIFIC Integer_In_Proc  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Integer_In_Proc" PARAMETER STYLE SQL ;

drop procedure Integer_InOut_Proc (INTEGER)  ;
create procedure Integer_InOut_Proc (out INOUT_PARAM INTEGER) LANGUAGE JAVA SPECIFIC Integer_InOut_Proc  DETERMINISTIC READS SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Integer_InOut_Proc" PARAMETER STYLE SQL ;

drop procedure UpdCoffee_Proc (VARCHAR)  ;
create procedure UpdCoffee_Proc (in TYPE_PARAM VARCHAR) LANGUAGE JAVA SPECIFIC UpdCoffee_Proc DETERMINISTIC   MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::UpdCoffee_Proc" PARAMETER STYLE SQL  ;

drop procedure SelCoffee_Proc (NUMERIC)  ;
create procedure SelCoffee_Proc (out KEYID_PARAM NUMERIC) LANGUAGE JAVA SPECIFIC SelCoffee_Proc  DETERMINISTIC READS SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::SelCoffee_Proc" PARAMETER STYLE SQL ;

drop procedure IOCoffee_Proc (FLOAT)  ;
create procedure IOCoffee_Proc (out PRICE_PARAM FLOAT) LANGUAGE JAVA SPECIFIC IOCoffee_Proc DETERMINISTIC  READS SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::IOCoffee_Proc" PARAMETER STYLE SQL  ;

drop procedure Coffee_Proc (NUMERIC)  ;
create procedure Coffee_Proc(in TYPE_PARAM NUMERIC) LANGUAGE JAVA SPECIFIC Coffee_Proc DETERMINISTIC MODIFIES  SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Coffee_Proc" PARAMETER STYLE SQL ;

drop procedure Numeric_Io_Max (NUMERIC)  ;
create procedure Numeric_Io_Max (inout MAX_PARAM NUMERIC) LANGUAGE JAVA SPECIFIC Numeric_Io_Max  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Numeric_Io_Max" PARAMETER STYLE SQL ;

drop procedure Numeric_Io_Min (NUMERIC)  ;
create procedure Numeric_Io_Min (inout MIN_PARAM NUMERIC) LANGUAGE JAVA SPECIFIC Numeric_Io_Min  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Numeric_Io_Min" PARAMETER STYLE SQL ;

drop procedure Numeric_Io_Null (NUMERIC)  ;
create procedure Numeric_Io_Null (inout NULL_PARAM NUMERIC) LANGUAGE JAVA SPECIFIC Numeric_Io_Null  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Numeric_Io_Null" PARAMETER STYLE SQL ;

drop procedure Decimal_Io_Max (DECIMAL)  ;
create procedure Decimal_Io_Max (inout MAX_PARAM DECIMAL) LANGUAGE JAVA SPECIFIC Decimal_Io_Max  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Decimal_Io_Max" PARAMETER STYLE SQL  ;

drop procedure Decimal_Io_Min (DECIMAL) ;
create procedure Decimal_Io_Min (inout MIN_PARAM DECIMAL) LANGUAGE JAVA SPECIFIC Decimal_Io_Min  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Decimal_Io_Min" PARAMETER STYLE SQL  ;

drop procedure Decimal_Io_Null (DECIMAL) ;
create procedure Decimal_Io_Null (inout NULL_PARAM DECIMAL) LANGUAGE JAVA SPECIFIC Decimal_Io_Null  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Decimal_Io_Null" PARAMETER STYLE SQL  ;

drop procedure Double_Io_Max (DOUBLE PRECISION)  ;
create procedure Double_Io_Max (inout MAX_PARAM DOUBLE PRECISION) LANGUAGE JAVA SPECIFIC Double_Io_Max  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Double_Io_Max" PARAMETER STYLE SQL  ;

drop procedure Double_Io_Min (DOUBLE PRECISION) ;
create procedure Double_Io_Min (inout MIN_PARAM DOUBLE PRECISION) LANGUAGE JAVA SPECIFIC Double_Io_Min  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Double_Io_Min" PARAMETER STYLE SQL  ;

drop procedure Double_Io_Null (DOUBLE PRECISION) ;
create procedure Double_Io_Null (inout NULL_PARAM DOUBLE PRECISION) LANGUAGE JAVA SPECIFIC  Double_Io_Null DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Double_Io_Null" PARAMETER STYLE SQL  ;

drop procedure Float_Io_Max (FLOAT)  ;
create procedure Float_Io_Max (inout MAX_PARAM FLOAT) LANGUAGE JAVA SPECIFIC Float_Io_Max DETERMINISTIC  MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Float_Io_Max" PARAMETER STYLE SQL ;

drop procedure Float_Io_Min (FLOAT) ;
create procedure Float_Io_Min (inout MIN_PARAM FLOAT) LANGUAGE JAVA SPECIFIC Float_Io_Min DETERMINISTIC  MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Float_Io_Min" PARAMETER STYLE SQL ;

drop procedure Float_Io_Null (FLOAT) ;
create procedure Float_Io_Null (inout NULL_PARAM FLOAT) LANGUAGE JAVA SPECIFIC Float_Io_Null  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Float_Io_Null" PARAMETER STYLE SQL  ;

drop procedure Real_Io_Max (REAL)  ;
create procedure Real_Io_Max (inout MAX_PARAM REAL) LANGUAGE JAVA SPECIFIC Real_Io_Max DETERMINISTIC  MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Real_Io_Max" PARAMETER STYLE SQL  ;

drop procedure Real_Io_Min (REAL) ;
create procedure Real_Io_Min (inout MIN_PARAM REAL) LANGUAGE JAVA SPECIFIC Real_Io_Min DETERMINISTIC  MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Real_Io_Min" PARAMETER STYLE SQL ;

drop procedure Real_Io_Null (REAL) ;
create procedure Real_Io_Null (inout NULL_PARAM REAL) LANGUAGE JAVA SPECIFIC Real_Io_Null DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Real_Io_Null" PARAMETER STYLE SQL ;

drop procedure Bit_Io_Max (SMALLINT) ;
create procedure Bit_Io_Max (inout MAX_PARAM SMALLINT) LANGUAGE JAVA SPECIFIC Bit_Io_Max DETERMINISTIC  MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Bit_Io_Max" PARAMETER STYLE SQL  ;

drop procedure Bit_Io_Min (SMALLINT) ;
create procedure Bit_Io_Min (inout MIN_PARAM SMALLINT) LANGUAGE JAVA SPECIFIC Bit_Io_Min DETERMINISTIC  MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Bit_Io_Min" PARAMETER STYLE SQL ;

drop procedure Bit_Io_Null (SMALLINT) ;
create procedure Bit_Io_Null (inout NULL_PARAM SMALLINT) LANGUAGE JAVA SPECIFIC Bit_Io_Null DETERMINISTIC  MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Bit_Io_Null" PARAMETER STYLE SQL  ;

drop procedure Smallint_Io_Max (SMALLINT) ;
create procedure Smallint_Io_Max (inout MAX_PARAM SMALLINT) LANGUAGE JAVA SPECIFIC Smallint_Io_Max  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Smallint_Io_Max" PARAMETER STYLE SQL ;

drop procedure Smallint_Io_Min (SMALLINT) ;
create procedure Smallint_Io_Min (inout MIN_PARAM SMALLINT) LANGUAGE JAVA SPECIFIC Smallint_Io_Min  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Smallint_Io_Min" PARAMETER STYLE SQL ;

drop procedure Smallint_Io_Null (SMALLINT) ;
create procedure Smallint_Io_Null (inout NULL_PARAM SMALLINT) LANGUAGE JAVA SPECIFIC  Smallint_Io_Null DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Smallint_Io_Null" PARAMETER STYLE SQL ;

drop procedure Tinyint_Io_Max (SMALLINT) ;
create procedure Tinyint_Io_Max (inout MAX_PARAM SMALLINT) LANGUAGE JAVA SPECIFIC Tinyint_Io_Max  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Tinyint_Io_Max" PARAMETER STYLE SQL ;

drop procedure Tinyint_Io_Min (SMALLINT) ;
create procedure Tinyint_Io_Min (inout MIN_PARAM SMALLINT) LANGUAGE JAVA SPECIFIC Tinyint_Io_Min  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Tinyint_Io_Min" PARAMETER STYLE SQL ;

drop procedure Tinyint_Io_Null (SMALLINT) ;
create procedure Tinyint_Io_Null (inout NULL_PARAM SMALLINT) LANGUAGE JAVA SPECIFIC Tinyint_Io_Null  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Tinyint_Io_Null" PARAMETER STYLE SQL ;

drop procedure Integer_Io_Max (INTEGER)  ;
create procedure Integer_Io_Max (inout MAX_PARAM INTEGER) LANGUAGE JAVA SPECIFIC Integer_Io_Max  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Integer_Io_Max" PARAMETER STYLE SQL ;

drop procedure Integer_Io_Min (INTEGER) ;
create procedure Integer_Io_Min (inout MIN_PARAM INTEGER) LANGUAGE JAVA SPECIFIC Integer_Io_Min  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Integer_Io_Min" PARAMETER STYLE SQL ;

drop procedure Integer_Io_Null (INTEGER) ;
create procedure Integer_Io_Null (inout NULL_PARAM INTEGER) LANGUAGE JAVA SPECIFIC Integer_Io_Null  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Integer_Io_Null" PARAMETER STYLE SQL ;

drop procedure Bigint_Io_Max (BIGINT)  ;
create procedure Bigint_Io_Max (inout MAX_PARAM BIGINT) LANGUAGE JAVA SPECIFIC Bigint_Io_Max  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Bigint_Io_Max" PARAMETER STYLE SQL ;

drop procedure Bigint_Io_Min (BIGINT) ;
create procedure Bigint_Io_Min (inout MIN_PARAM BIGINT) LANGUAGE JAVA SPECIFIC Bigint_Io_Min  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Bigint_Io_Min" PARAMETER STYLE SQL ;

drop procedure Bigint_Io_Null (BIGINT) ;
create procedure Bigint_Io_Null (inout NULL_PARAM BIGINT) LANGUAGE JAVA SPECIFIC Bigint_Io_Null  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Bigint_Io_Mull" PARAMETER STYLE SQL ;

drop procedure Char_Io_Name (CHAR)  ;
create procedure Char_Io_Name(inout NAME_PARAM CHAR) LANGUAGE JAVA SPECIFIC Char_Io_Name DETERMINISTIC  MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Char_Io_Name" PARAMETER STYLE SQL ;

drop procedure Char_Io_Null(CHAR) ;
create procedure Char_Io_Null(inout NULL_PARAM CHAR) LANGUAGE JAVA SPECIFIC Char_Io_Null DETERMINISTIC  MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Char_Io_Null" PARAMETER STYLE SQL ;

drop procedure Varchar_Io_Name(VARCHAR)  ;
create procedure Varchar_Io_Name(inout NAME_PARAM VARCHAR) LANGUAGE JAVA SPECIFIC Varchar_Io_Name  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Varchar_Io_Name" PARAMETER STYLE SQL ;

drop procedure Varchar_Io_Null(VARCHAR) ;
create procedure Varchar_Io_Null(inout NULL_PARAM VARCHAR) LANGUAGE JAVA SPECIFIC Varchar_Io_Null  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Varchar_Io_Null" PARAMETER STYLE SQL ;

drop procedure Lvarchar_Io_Name (CLOB(2000)) ;
create procedure Lvarchar_Io_Name(inout NAME_PARAM CLOB(2000)) LANGUAGE JAVA SPECIFIC  Longvarchar_Io_Name DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Longvarchar_Io_Name" PARAMETER STYLE SQL ;

drop procedure Lvarchar_Io_Null (CLOB(2000)) ;
create procedure Lvarchar_Io_Null(inout NULL_PARAM CLOB(2000)) LANGUAGE JAVA SPECIFIC  Longvarchar_Io_Null DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Longvarchar_Io_Null" PARAMETER STYLE SQL ;

drop procedure Date_Io_Mfg (DATE)  ;
create procedure Date_Io_Mfg(inout MFG_PARAM DATE) LANGUAGE JAVA SPECIFIC Date_Io_Mfg DETERMINISTIC  MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Date_Io_Mfg" PARAMETER STYLE SQL ;

drop procedure Date_Io_Null (DATE) ;
create procedure Date_Io_Null(inout NULL_PARAM DATE) LANGUAGE JAVA SPECIFIC Date_Io_Null DETERMINISTIC  MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Date_Io_Null" PARAMETER STYLE SQL ;

drop procedure Time_Io_Brk (DATE)  ;
create procedure Time_Io_Brk(inout BRK_PARAM DATE) LANGUAGE JAVA SPECIFIC Time_Io_Brk DETERMINISTIC  MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Time_Io_Brk" PARAMETER STYLE SQL ;

drop procedure Time_Io_Null (TIME) ;
create procedure Time_Io_Null(inout NULL_PARAM TIME) LANGUAGE JAVA SPECIFIC Time_Io_Null DETERMINISTIC  MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Time_Io_Null" PARAMETER STYLE SQL ;

drop procedure Timestamp_Io_Intime (Timestamp) ;
create procedure Timestamp_Io_Intime(inout INTIME_PARAM TIMESTAMP) LANGUAGE JAVA SPECIFIC  Timestamp_Io_Intime DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Timestamp_Io_Intime" PARAMETER STYLE SQL ;

drop procedure Timestamp_Io_Null(TimeStamp) ;
create procedure Timestamp_Io_Null(inout NULL_PARAM TIMESTAMP) LANGUAGE JAVA SPECIFIC  Timestamp_Io_Null DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Timestamp_Io_Null" PARAMETER STYLE SQL ;

drop procedure Binary_Proc_Io (LONGVARBINARY(24000))  ;
create procedure Binary_Proc_Io(inout BINARY_PARAM LONGVARBINARY(24000)) LANGUAGE JAVA SPECIFIC Binary_Proc_Io  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Binary_Proc_Io" PARAMETER STYLE SQL ;

drop procedure Varbinary_Proc_Io (LONGVARBINARY(48000))  ;
create procedure Varbinary_Proc_Io(inout VARBINARY_PARAM LONGVARBINARY(48000)) LANGUAGE JAVA SPECIFIC  Varbinary_Proc_Io DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Varbinary_Proc_Io" PARAMETER STYLE SQL ;

drop procedure Longvarbinary_Io (LONGVARBINARY(20000))  ;
create procedure Longvarbinary_Io(inout LONGVARBINARY_PARAM LONGVARBINARY(20000)) LANGUAGE JAVA SPECIFIC  Longvarbinary_Io DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Longvarbinary_Io" PARAMETER STYLE SQL ;

drop procedure Numeric_In_Max (NUMERIC)  ;
create procedure Numeric_In_Max (in MAX_PARAM NUMERIC) LANGUAGE JAVA SPECIFIC Numeric_In_Max  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Numeric_In_Max" PARAMETER STYLE SQL ;

drop procedure Numeric_In_Min (NUMERIC) ;
create procedure Numeric_In_Min (in MIN_PARAM NUMERIC) LANGUAGE JAVA SPECIFIC Numeric_In_Min  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Numeric_In_Min" PARAMETER STYLE SQL ;

drop procedure Numeric_In_Null (NUMERIC) ;
create procedure Numeric_In_Null (in NULL_PARAM NUMERIC) LANGUAGE JAVA SPECIFIC Numeric_In_Null  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Numeric_In_Null" PARAMETER STYLE SQL ;

drop procedure Decimal_In_Max (DECIMAL)  ;
create procedure Decimal_In_Max (in MAX_PARAM DECIMAL) LANGUAGE JAVA SPECIFIC Decimal_In_Max  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Decimal_In_Max" PARAMETER STYLE SQL ;

drop procedure Decimal_In_Min (DECIMAL) ;
create procedure Decimal_In_Min (in MIN_PARAM DECIMAL) LANGUAGE JAVA SPECIFIC Decimal_In_Min  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Decimal_In_Min" PARAMETER STYLE SQL ;

drop procedure Decimal_In_Null (DECIMAL) ;
create procedure Decimal_In_Null (in NULL_PARAM DECIMAL) LANGUAGE JAVA SPECIFIC Decimal_In_NULL  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Decimal_In_Null" PARAMETER STYLE SQL ;

drop procedure Double_In_Max (DOUBLE PRECISION)  ;
create procedure Double_In_Max (in MAX_PARAM DOUBLE PRECISION) LANGUAGE JAVA SPECIFIC Double_In_Max  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Double_In_Max" PARAMETER STYLE SQL ;

drop procedure Double_In_Min (DOUBLE PRECISION) ;
create procedure Double_In_Min (in MIN_PARAM DOUBLE PRECISION) LANGUAGE JAVA SPECIFIC Double_In_Min  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Double_In_Min" PARAMETER STYLE SQL ;

drop procedure Double_In_Null (DOUBLE PRECISION) ;
create procedure Double_In_Null (in NULL_PARAM DOUBLE PRECISION) LANGUAGE JAVA SPECIFIC Double_In_Null  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Double_In_Null" PARAMETER STYLE SQL ;

drop procedure Float_In_Max (FLOAT)  ;
create procedure Float_In_Max (in MAX_PARAM FLOAT) LANGUAGE JAVA SPECIFIC Float_In_Max DETERMINISTIC  MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Float_In_Max" PARAMETER STYLE SQL ;

drop procedure Float_In_Min (FLOAT) ;
create procedure Float_In_Min (in MIN_PARAM FLOAT) LANGUAGE JAVA SPECIFIC Float_In_Min DETERMINISTIC  MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Float_In_Min" PARAMETER STYLE SQL ;

drop procedure Float_In_Null(FLOAT) ;
create procedure Float_In_Null (in NULL_PARAM FLOAT) LANGUAGE JAVA SPECIFIC Float_In_Null DETERMINISTIC  MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Float_In_Null" PARAMETER STYLE SQL ;

drop procedure Real_In_Max (REAL)  ;
create procedure Real_In_Max (in MAX_PARAM REAL) LANGUAGE JAVA SPECIFIC Real_In_Max DETERMINISTIC  MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Real_In_Max" PARAMETER STYLE SQL ;

drop procedure Real_In_Min (REAL) ;
create procedure Real_In_Min (in MIN_PARAM REAL) LANGUAGE JAVA SPECIFIC Real_In_Min DETERMINISTIC  MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Real_In_Min" PARAMETER STYLE SQL ;

drop procedure Real_In_Null (REAL) ;
create procedure Real_In_Null (in NULL_PARAM REAL) LANGUAGE JAVA SPECIFIC Real_In_Null DETERMINISTIC  MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Real_In_Null" PARAMETER STYLE SQL ;

drop procedure Bit_In_Max (SMALLINT)  ;
create procedure Bit_In_Max (in MAX_PARAM SMALLINT) LANGUAGE JAVA SPECIFIC Bit_In_Max DETERMINISTIC  MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Bit_In_Max" PARAMETER STYLE SQL ;

drop procedure Bit_In_Min (SMALLINT) ;
create procedure Bit_In_Min (in MIN_PARAM SMALLINT) LANGUAGE JAVA SPECIFIC Bit_In_Min DETERMINISTIC  MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Bit_In_Min" PARAMETER STYLE SQL ;

drop procedure Bit_In_Null (SMALLINT) ;
create procedure Bit_In_Null (in NULL_PARAM SMALLINT) LANGUAGE JAVA SPECIFIC Bit_In_Null DETERMINISTIC  MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Bit_In_Null" PARAMETER STYLE SQL ;

drop procedure Smallint_In_Max (SMALLINT)  ;
create procedure Smallint_In_Max (in MAX_PARAM SMALLINT) LANGUAGE JAVA SPECIFIC Smallint_In_Max  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Smallint_In_Max" PARAMETER STYLE SQL ;

drop procedure Smallint_In_Min (SMALLINT) ;
create procedure Smallint_In_Min (in MIN_PARAM SMALLINT) LANGUAGE JAVA SPECIFIC Smallint_In_Min  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Smallint_In_Min" PARAMETER STYLE SQL ;

drop procedure Smallint_In_Null (SMALLINT) ;
create procedure Smallint_In_Null (in NULL_PARAM SMALLINT) LANGUAGE JAVA SPECIFIC Smallint_In_Null  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Smallint_In_Null" PARAMETER STYLE SQL ;

drop procedure Tinyint_In_Max (SMALLINT) ;
create procedure Tinyint_In_Max (in MAX_PARAM SMALLINT) LANGUAGE JAVA SPECIFIC Tinyint_In_Max  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Tinyint_In_Max" PARAMETER STYLE SQL ;

drop procedure Tinyint_In_Min (SMALLINT) ;
create procedure Tinyint_In_Min (in MIN_PARAM SMALLINT) LANGUAGE JAVA SPECIFIC Tinyint_In_Min  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Tinyint_In_Min" PARAMETER STYLE SQL ;

drop procedure Tinyint_In_Null (SMALLINT) ;
create procedure Tinyint_In_Null (in MIN_PARAM SMALLINT) LANGUAGE JAVA SPECIFIC Tinyint_In_Null  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Tinyint_In_Null" PARAMETER STYLE SQL ;

drop procedure Integer_In_Max (INTEGER) ;
create procedure Integer_In_Max (in MAX_PARAM INTEGER) LANGUAGE JAVA SPECIFIC Integer_In_Max  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Integer_In_Max" PARAMETER STYLE SQL ;

drop procedure Integer_In_Min (INTEGER) ;
create procedure Integer_In_Min (in MIN_PARAM INTEGER) LANGUAGE JAVA SPECIFIC Integer_In_Min  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Integer_In_Min" PARAMETER STYLE SQL ;

drop procedure Integer_In_Null (INTEGER) ;
create procedure Integer_In_Null (in NULL_PARAM INTEGER) LANGUAGE JAVA SPECIFIC Integer_In_Null  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Integer_In_Null" PARAMETER STYLE SQL ;

drop procedure Bigint_In_Max (BIGINT)  ;
create procedure Bigint_In_Max (in MAX_PARAM BIGINT) LANGUAGE JAVA SPECIFIC Bigint_In_Max  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Bigint_In_Max" PARAMETER STYLE SQL ;

drop procedure Bigint_In_Min (BIGINT) ;
create procedure Bigint_In_Min (in MIN_PARAM BIGINT) LANGUAGE JAVA SPECIFIC Bigint_In_Min  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Bigint_In_Min" PARAMETER STYLE SQL ;

drop procedure Bigint_In_Null (BIGINT) ;
create procedure Bigint_In_Null (in NULL_PARAM BIGINT) LANGUAGE JAVA SPECIFIC Bigint_In_Null  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Bigint_In_Null" PARAMETER STYLE SQL ;

drop procedure Char_In_Name(CHAR)  ;
create procedure Char_In_Name(in NAME_PARAM CHAR) LANGUAGE JAVA SPECIFIC Char_In_Name DETERMINISTIC  MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Char_In_Name" PARAMETER STYLE SQL ;

drop procedure Char_In_Null(CHAR) ;
create procedure Char_In_Null(in NULL_PARAM CHAR) LANGUAGE JAVA SPECIFIC Char_In_Null DETERMINISTIC  MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Char_In_Null" PARAMETER STYLE SQL ;

drop procedure Varchar_In_Name(VARCHAR) ;
create procedure Varchar_In_Name(in NAME_PARAM VARCHAR) LANGUAGE JAVA SPECIFIC Varchar_In_Name  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Varchar_In_Name" PARAMETER STYLE SQL ;

drop procedure Varchar_In_Null(VARCHAR) ;
create procedure Varchar_In_Null(in NULL_PARAM VARCHAR) LANGUAGE JAVA SPECIFIC Varchar_In_Null  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Varchar_In_Null" PARAMETER STYLE SQL ;


drop procedure Lvarchar_In_Name (CLOB(2000)) ;
create procedure Lvarchar_In_Name(in NAME_PARAM CLOB(2000)) LANGUAGE JAVA SPECIFIC  Longvarchar_In_Name DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Longvarchar_In_Name" PARAMETER STYLE SQL ;

drop procedure Lvarchar_In_Null(CLOB(2000)) ;
create procedure Lvarchar_In_Null(in NULL_PARAM CLOB(2000)) LANGUAGE JAVA SPECIFIC  Longvarchar_In_Null DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Longvarchar_In_Null" PARAMETER STYLE SQL ;

drop procedure Date_In_Mfg (DATE) ;
create procedure Date_In_Mfg(in MFG_PARAM DATE) LANGUAGE JAVA SPECIFIC Date_In_Mfg DETERMINISTIC MODIFIES  SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Date_In_Mfg" PARAMETER STYLE SQL ;

drop procedure Date_In_Null(DATE) ;
create procedure Date_In_Null(in NULL_PARAM DATE) LANGUAGE JAVA SPECIFIC Date_In_Null DETERMINISTIC  MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Date_In_Null" PARAMETER STYLE SQL ;

drop procedure Time_In_Brk (DATE) ;
create procedure Time_In_Brk(in BRK_PARAM DATE) LANGUAGE JAVA SPECIFIC Time_In_Brk DETERMINISTIC MODIFIES  SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Time_In_Brk" PARAMETER STYLE SQL ;

drop procedure Time_In_Null(TIME) ;
create procedure Time_In_Null(in NULL_PARAM TIME) LANGUAGE JAVA SPECIFIC Time_In_Null DETERMINISTIC  MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Time_In_Null" PARAMETER STYLE SQL ;

drop procedure Timestamp_In_Intime(TIMESTAMP) ;
create procedure Timestamp_In_Intime(in INTIME_PARAM TIMESTAMP) LANGUAGE JAVA SPECIFIC  Timestamp_In_Intime DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Timestamp_In_Intime" PARAMETER STYLE SQL ;

drop procedure Timestamp_In_Null(TIMESTAMP) ;
create procedure Timestamp_In_Null(in Null_PARAM TIMESTAMP) LANGUAGE JAVA SPECIFIC  Timestamp_In_Null DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Timestamp_In_Null" PARAMETER STYLE SQL ;

drop procedure Binary_Proc_In (LONGVARBINARY(24000)) ;
create procedure Binary_Proc_In(in BINARY_PARAM LONGVARBINARY(24000)) LANGUAGE JAVA SPECIFIC Binary_Proc_In  DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Binary_Proc_In" PARAMETER STYLE SQL ;

drop procedure Varbinary_Proc_In (LONGVARBINARY(48000)) ;
create procedure Varbinary_Proc_In(in VARBINARY_PARAM LONGVARBINARY(48000)) LANGUAGE JAVA SPECIFIC  Varbinary_Proc_In DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Varbinary_Proc_In" PARAMETER STYLE SQL ;

drop procedure Longvarbinary_In(LONGVARBINARY(20000))  ;
create procedure Longvarbinary_In(in LONGVARBINARY_PARAM LONGVARBINARY(20000)) LANGUAGE JAVA SPECIFIC  Longvarbinary_In DETERMINISTIC MODIFIES SQL DATA EXTERNAL NAME "com.sun.ts.lib.tests.jdbc.PointbaseProcedures::Longvarbinary_In" PARAMETER STYLE SQL ;
