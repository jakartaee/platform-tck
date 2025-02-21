-- Procedures

DROP FUNCTION UpdCoffee_Proc( TYPE_PARAM numeric);
CREATE OR REPLACE FUNCTION UpdCoffee_Proc (TYPE_PARAM numeric) returns void as ' begin update ctstable2 set PRICE= PRICE*20 where TYPE_ID= TYPE_PARAM; end;' language 'plpgsql';

DROP FUNCTION Coffee_Proc( TYPE_PARAM numeric);
CREATE OR REPLACE FUNCTION Coffee_Proc (TYPE_PARAM numeric) returns void as ' begin update ctstable2 set PRICE= PRICE*2 where TYPE_ID= TYPE_PARAM; delete from ctstable2 where TYPE_ID=TYPE_PARAM-1; end;' language 'plpgsql';

DROP FUNCTION Numeric_Proc(out pmax  numeric(30,15),out pmin numeric(30,15),out pnull numeric(30,15)) ;
CREATE OR REPLACE FUNCTION Numeric_Proc(out pmax  numeric(30,15),out pmin numeric(30,15),out pnull numeric(30,15)) returns record as ' begin select * into pmax, pmin, pnull from Numeric_Tab;  end;' language 'plpgsql';

DROP FUNCTION Decimal_Proc(out pmax decimal(30,15),out pmin decimal(30,15),out pnull decimal(30,15)) ;
CREATE OR REPLACE FUNCTION Decimal_Proc(out pmax decimal(30,15),out pmin decimal(30,15),out pnull decimal(30,15)) returns record as ' begin select * into pmax, pmin, pnull from Decimal_Tab;  end;' language 'plpgsql';

DROP FUNCTION Double_Proc(out pmax float8,out pmin float8,out pnull float8) ;
CREATE OR REPLACE FUNCTION Double_Proc(out pmax double precision, out pmin double precision,out pnull double precision) returns record as ' begin select * into pmax, pmin, pnull from Double_Tab; end;' language 'plpgsql';

DROP FUNCTION Float_Proc(out pmax float,out pmin float,out pnull float) ;
CREATE OR REPLACE FUNCTION Float_Proc(out pmax float,out pmin float,out pnull float) returns record as ' begin select * into pmax, pmin, pnull from Float_Tab;  end;' language 'plpgsql';

DROP FUNCTION Real_Proc(out pmax real,out pmin real,out pnull real)  ;
CREATE OR REPLACE FUNCTION Real_Proc(out pmax real,out pmin real,out pnull real) returns record as ' begin select * into pmax, pmin, pnull from Real_Tab;  end;' language 'plpgsql';

DROP FUNCTION Bit_Proc(out pmax boolean,out pmin boolean,out pnull boolean);
CREATE OR REPLACE FUNCTION Bit_Proc(out pmax boolean,out pmin boolean,out pnull boolean) returns record as ' begin select * into pmax, pmin, pnull from Bit_Tab;  end;' language 'plpgsql';

DROP FUNCTION Smallint_Proc(out pmax smallint,out pmin smallint,out pnull smallint);
CREATE OR REPLACE FUNCTION Smallint_Proc(out pmax smallint,out pmin smallint,out pnull smallint) returns record as ' begin select * into pmax, pmin, pnull from Smallint_Tab;  end;' language 'plpgsql';

DROP FUNCTION Tinyint_Proc(out pmax smallint, out pmin smallint, out pnull smallint);
CREATE OR REPLACE FUNCTION Tinyint_Proc(out pmax smallint, out pmin smallint, out pnull smallint) returns record as ' begin select * into pmax, pmin, pnull from Tinyint_Tab;  end;' language 'plpgsql';

DROP FUNCTION Integer_Proc(out pmax integer,out pmen integer,out pnull integer);
CREATE OR REPLACE FUNCTION Integer_Proc(out pmax integer,out pmin integer,out pnull integer) returns record as ' begin select * into pmax, pmin, pnull from Integer_Tab;  end;' language 'plpgsql';

DROP FUNCTION Bigint_Proc(out pmax int8,out pmin int8,out pnull int8);
CREATE OR REPLACE FUNCTION Bigint_Proc(out pmax int8,out pmin int8,out pnull int8) returns record as ' begin select * into pmax, pmin, pnull from Bigint_Tab;  end;' language 'plpgsql';

DROP FUNCTION Char_Proc(out pcn character(30),out pnull character(30));
CREATE OR REPLACE FUNCTION Char_Proc(out pcn character(30),out pnull character(30)) returns record as ' begin select * into pcn, pnull from Char_Tab;  end;' language 'plpgsql';

DROP FUNCTION Varchar_Proc(out character varying(30),out character varying(30));
CREATE OR REPLACE FUNCTION Varchar_Proc(out pcn character varying(30),out pnull character varying(30)) returns record as ' begin select * into pcn, pnull from Varchar_Tab;  end;' language 'plpgsql';

DROP FUNCTION Longvarchar_Proc(out pcn text);
CREATE OR REPLACE FUNCTION Longvarchar_Proc(out pcn text) returns text as ' begin select * into pcn from Longvarchar_Tab;  end;' language 'plpgsql';

DROP FUNCTION Longvarcharnull_Proc(out pcn text);
CREATE OR REPLACE FUNCTION Longvarcharnull_Proc(out pcn text) returns text as ' begin select * into pcn from Longvarcharnull_Tab;  end;' language 'plpgsql';

DROP FUNCTION Date_Proc(out pd date,out pnull date);
CREATE OR REPLACE FUNCTION Date_Proc(out pd date,out pnull date) returns record as ' begin select * into pd, pnull from Date_Tab; end;' language 'plpgsql';

DROP FUNCTION Time_Proc(out pt time,out pnull time);
CREATE OR REPLACE FUNCTION Time_Proc(out pt time,out pnull time) returns record as ' begin select * into pt, pnull from Time_Tab;  end;' language 'plpgsql';

DROP FUNCTION Timestamp_Proc(out pt  timestamp,out pnull timestamp) ;
CREATE OR REPLACE FUNCTION Timestamp_Proc(out pt  timestamp,out pnull timestamp) returns record as ' begin select * into pt, pnull from Timestamp_Tab;  end;' language 'plpgsql';

DROP FUNCTION Binary_Proc(out pb bytea);
CREATE OR REPLACE FUNCTION Binary_Proc(out pb bytea) returns bytea as ' begin select * into pb from Binary_Tab;  end;' language 'plpgsql';

DROP FUNCTION Varbinary_Proc(out pb bytea);
CREATE OR REPLACE FUNCTION Varbinary_Proc(out pb bytea) returns bytea as ' begin select * into pb from Varbinary_Tab;  end;' language 'plpgsql';

DROP FUNCTION Longvarbinary_Proc(out pb bytea);
CREATE OR REPLACE FUNCTION Longvarbinary_Proc(out pb bytea) returns bytea as ' begin select * into pb from Longvarbinary_Tab;  end;' language 'plpgsql';



-- not sure what these are for
DROP FUNCTION concat(character varying, character varying);
CREATE OR REPLACE FUNCTION concat(in character varying,in character varying, out character varying) returns character varying as ' select ($1 || $2); ' language 'sql';

CREATE OR REPLACE FUNCTION LOCATE(VARCHAR,VARCHAR,integer) RETURNS INTEGER AS ' DECLARE string ALIAS For $2; string_to_search ALIAS For $1; beg_index alias for $3; pos integer NOT NULL DEFAULT 0; temp_str VARCHAR;  beg INTEGER; length INTEGER; ss_length INTEGER; BEGIN  IF beg_index > 0 THEN temp_str := substring(string FROM beg_index); pos := position(string_to_search IN temp_str); IF pos = 0 THEN RETURN 0; ELSE RETURN pos + beg_index - 1; END IF; ELSE ss_length := char_length(string_to_search); length := char_length(string); beg := length + beg_index - ss_length + 2; WHILE beg > 0 LOOP temp_str := substring(string FROM beg For ss_length); pos := position(string_to_search IN temp_str); IF pos > 0 THEN RETURN beg; END IF; beg := beg - 1; END LOOP; RETURN 0; END IF; END;' LANGUAGE 'plpgsql';

--DROP FUNCTION locate(varchar,varchar) ;
CREATE OR REPLACE FUNCTION locate(out varchar,out varchar) returns record as ' declare pos integer; begin pos:=locate($1,$2,1);  end;' language 'plpgsql';

---------
-- In/Out
---------

DROP FUNCTION Numeric_Io_Max(numeric(30,15)) ;
CREATE OR REPLACE FUNCTION Numeric_Io_Max(inout p numeric(30,15)) returns numeric as ' begin update Numeric_Tab set max_val = $1; select max_val into p from Numeric_Tab;  end; ' language 'plpgsql';

DROP FUNCTION Numeric_Io_Min(numeric(30,15)) ;
CREATE OR REPLACE FUNCTION Numeric_Io_Min(inout p numeric(30,15)) returns numeric as ' begin update Numeric_Tab set min_val = $1; select min_val into p from Numeric_Tab;   end; ' language 'plpgsql';

DROP FUNCTION Numeric_Io_Null(numeric(30,15)) ;
CREATE OR REPLACE FUNCTION Numeric_Io_Null(inout p numeric(30,15)) returns numeric as ' begin update Numeric_Tab set null_val = $1; select null_val into p from Numeric_Tab;  end; ' language 'plpgsql';

DROP FUNCTION Decimal_Io_Max(decimal(30,15));
CREATE OR REPLACE FUNCTION Decimal_Io_Max(inout p decimal(30,15)) returns decimal as ' begin update Decimal_Tab set max_val = $1; select max_val into p from Decimal_Tab;  end; ' language 'plpgsql';

DROP FUNCTION Decimal_Io_Min(decimal(30,15)) ;
CREATE OR REPLACE FUNCTION Decimal_Io_Min(inout p decimal(30,15)) returns decimal as ' begin update Decimal_Tab set min_val = $1; select min_val into p from Decimal_Tab;  end; ' language 'plpgsql';

DROP FUNCTION Decimal_Io_Null(decimal(30,15)) ;
CREATE OR REPLACE FUNCTION Decimal_Io_Null(inout p decimal(30,15)) returns decimal as ' begin update Decimal_Tab set null_val = $1; select null_val into p from Decimal_Tab;  end; ' language 'plpgsql';

DROP FUNCTION Double_Io_Max(float8);
CREATE OR REPLACE FUNCTION Double_Io_Max(inout p float8) returns float8 as ' begin update Double_Tab set max_val = $1; select max_val into p from Double_Tab;  end; ' language 'plpgsql';

DROP FUNCTION Double_Io_Min(float8);
CREATE OR REPLACE FUNCTION Double_Io_Min(inout p float8) returns float8  as ' begin update Double_Tab set min_val = $1; select min_val into p from Double_Tab;  end; ' language 'plpgsql';

DROP FUNCTION Double_Io_Null(float8) ;
CREATE OR REPLACE FUNCTION Double_Io_Null(inout p float8) returns float8 as ' begin update Double_Tab set null_val = $1; select null_val into p from Double_Tab;  end; ' language 'plpgsql';

DROP FUNCTION Float_Io_Max(float) ;
CREATE OR REPLACE FUNCTION Float_Io_Max(inout p float) returns float as ' begin update Float_Tab set max_val = $1; select max_val into p from Float_Tab;  end;' language 'plpgsql';

DROP FUNCTION Float_Io_Min(float) ;
CREATE OR REPLACE FUNCTION Float_Io_Min(inout p float) returns float as ' begin update Float_Tab set min_val = $1; select min_val into p from Float_Tab;  end;' language 'plpgsql';

DROP FUNCTION Float_Io_Null(float) ;
CREATE OR REPLACE FUNCTION Float_Io_Null(inout p float) returns float as ' begin update Float_Tab set null_val = $1; select null_val into p from Float_Tab;  end;' language 'plpgsql';

--DROP FUNCTION Real_Io_Max(real) ;
--CREATE OR REPLACE FUNCTION Real_Io_Max(inout p real) returns real as ' begin update Real_Tab set max_val = $1; select max_val into p from Real_Tab;  end;' language 'plpgsql';

CREATE OR REPLACE FUNCTION Real_Io_Max(inout p double precision) returns double precision as 'begin update Real_Tab set max_val = $1; select max_val into p from Real_Tab;  end;'language 'plpgsql';

DROP FUNCTION Real_Io_Min(real) ;
CREATE OR REPLACE FUNCTION Real_Io_Min(inout p real) returns real as ' begin update Real_Tab set min_val = $1; select min_val into p from Real_Tab;  end;' language 'plpgsql';

--DROP FUNCTION Real_Io_Null(real);
--CREATE OR REPLACE FUNCTION Real_Io_Null(inout p real) returns real as ' begin update Real_Tab set null_val = $1; select null_val into p from Real_Tab;  end;' language 'plpgsql';

CREATE OR REPLACE FUNCTION Real_Io_Null(inout p double precision) returns double precision as 'begin update Real_Tab set null_val = $1; select null_val into p from Real_Tab;  end;'language 'plpgsql';

DROP FUNCTION Bit_Io_Max(boolean) ;
CREATE OR REPLACE FUNCTION Bit_Io_Max(inout p boolean) returns boolean as ' begin update Bit_Tab set max_val = $1; select max_val into p from Bit_Tab;  end;' language 'plpgsql';

DROP FUNCTION Bit_Io_Min(boolean);
CREATE OR REPLACE FUNCTION Bit_Io_Min(inout p boolean) returns boolean as ' begin update Bit_Tab set min_val = $1; select min_val into p from Bit_Tab;  end;' language 'plpgsql';

DROP FUNCTION Bit_Io_Null(boolean) ;
CREATE OR REPLACE FUNCTION Bit_Io_Null(inout p boolean) returns boolean as ' begin update Bit_Tab set null_val = $1; select null_val into p from Bit_Tab;  end;' language 'plpgsql';

--DROP FUNCTION Smallint_Io_Max(integer) ;
--CREATE OR REPLACE FUNCTION Smallint_Io_Max(inout p integer) returns integer as ' begin update Smallint_Tab set max_val = $1; select max_val into p from Smallint_Tab;  end;' language 'plpgsql';

CREATE OR REPLACE FUNCTION Smallint_Io_Max(inout p smallint) returns smallint as 'begin update Smallint_Tab set max_val = $1; select max_val into p from Smallint_Tab;  end;' language 'plpgsql';

-- DROP FUNCTION Smallint_Io_Min(integer) ;
--CREATE OR REPLACE FUNCTION Smallint_Io_Min(inout p integer) returns integer as ' begin update Smallint_Tab set min_val = $1; select min_val into p from Smallint_Tab;  end;' language 'plpgsql';

CREATE OR REPLACE FUNCTION Smallint_Io_Min(inout p smallint) returns smallint as 'begin update Smallint_Tab set min_val = $1; select min_val into p from Smallint_Tab;  end;' language 'plpgsql';

-- DROP FUNCTION Smallint_Io_Null(integer);
--CREATE OR REPLACE FUNCTION Smallint_Io_Null(inout p integer) returns integer as ' begin update Smallint_Tab set null_val = $1; select null_val into p from Smallint_Tab;  end;' language 'plpgsql';

CREATE OR REPLACE FUNCTION Smallint_Io_Null(inout p smallint) returns smallint as 'begin update Smallint_Tab set null_val = $1; select null_val into p from Smallint_Tab; end;'language 'plpgsql';

-- DROP FUNCTION Tinyint_Io_Max(integer) ;
--CREATE OR REPLACE FUNCTION Tinyint_Io_Max(inout p integer) returns integer as ' begin update Tinyint_Tab set max_val = $1; select max_val into p from Tinyint_Tab;  end;' language 'plpgsql';

CREATE OR REPLACE FUNCTION Tinyint_Io_Max(inout p tinyint) returns tinyint as 'begin update Tinyint_Tab set max_val = $1; select max_val into p from Tinyint_Tab;  end;'language 'plpgsql';

DROP FUNCTION Tinyint_Io_Min(integer);
CREATE OR REPLACE FUNCTION Tinyint_Io_Min(inout p integer) returns integer as ' begin update Tinyint_Tab set min_val = $1; select min_val into p from Tinyint_Tab;  end;' language 'plpgsql';

-- DROP FUNCTION Tinyint_Io_Null(integer) ;
--CREATE OR REPLACE FUNCTION Tinyint_Io_Null(inout p integer) returns integer as ' begin update Tinyint_Tab set null_val = $1; select null_val into p from Tinyint_Tab;  end;' language 'plpgsql'; 

CREATE OR REPLACE FUNCTION Tinyint_Io_Null(inout p tinyint) returns tinyint as 'begin update Tinyint_Tab set null_val = $1; select null_val into p from Tinyint_Tab;  end;'language 'plpgsql';

CREATE OR REPLACE FUNCTION Tinyint_Io_Min(inout p tinyint) returns tinyint as 'begin update Tinyint_Tab set min_val = $1; select min_val into p from Tinyint_Tab;  end;'language 'plpgsql';

DROP FUNCTION Integer_Io_Max(int) ;
CREATE OR REPLACE FUNCTION Integer_Io_Max(inout p int) returns int as ' begin update Integer_Tab set max_val = $1; select max_val into p from Integer_Tab;  end;' language 'plpgsql';

DROP FUNCTION Integer_Io_Min(int) ;
CREATE OR REPLACE FUNCTION Integer_Io_Min(inout p int) returns int as ' begin update Integer_Tab set min_val = $1; select min_val into p from Integer_Tab;  end;' language 'plpgsql';

DROP FUNCTION Integer_Io_Null(int);
CREATE OR REPLACE FUNCTION Integer_Io_Null(inout p int) returns int as ' begin update Integer_Tab set null_val = $1; select null_val into p from Integer_Tab;  end;' language 'plpgsql';

DROP FUNCTION Bigint_Io_Max(int8) ;
CREATE OR REPLACE FUNCTION Bigint_Io_Max(inout p int8) returns int8 as ' begin update Bigint_Tab set max_val = $1; select max_val into p from Bigint_Tab;  end;' language 'plpgsql';

DROP FUNCTION Bigint_Io_Min(int8);
CREATE OR REPLACE FUNCTION Bigint_Io_Min(inout p int8) returns int8 as ' begin update Bigint_Tab set min_val = $1; select min_val into p from Bigint_Tab;  end;' language 'plpgsql';

DROP FUNCTION Bigint_Io_Null(int8) ;
CREATE OR REPLACE FUNCTION Bigint_Io_Null(inout p int8) returns int8 as ' begin update Bigint_Tab set null_val = $1; select null_val into p from Bigint_Tab;  end;' language 'plpgsql';

DROP FUNCTION Char_Io_Name(char(30));
CREATE OR REPLACE FUNCTION Char_Io_Name(inout p char(30)) returns char as ' begin update Char_Tab set coffee_name = $1; select coffee_name into p from Char_Tab;  end;' language 'plpgsql';

DROP FUNCTION Char_Io_Null(char(30)) ;
CREATE OR REPLACE FUNCTION Char_Io_Null(inout p char(30)) returns char as ' begin update Char_Tab set null_val = $1; select null_val into p from Char_Tab;  end;' language 'plpgsql';

DROP FUNCTION Varchar_Io_Name(character varying(30));
CREATE OR REPLACE FUNCTION Varchar_Io_Name(inout p character varying(30)) returns character varying as ' begin update Varchar_Tab set coffee_name = $1; select coffee_name into p from Varchar_Tab;  end;' language 'plpgsql';

DROP FUNCTION Varchar_Io_Null(character varying(30)) ;
CREATE OR REPLACE FUNCTION Varchar_Io_Null(inout p character varying(30)) returns character varying as ' begin update Varchar_Tab set null_val = $1; select null_val into p from VarChar_Tab;  end;' language 'plpgsql';

DROP FUNCTION Longvarchar_Io_Name(text) ;
CREATE OR REPLACE FUNCTION Longvarchar_Io_Name(inout p text) returns text as ' begin update Longvarchar_Tab set coffee_name = $1; select coffee_name into p from Longvarchar_Tab;  end;' language 'plpgsql';

DROP FUNCTION Longvarchar_Io_Null(text) ;
CREATE OR REPLACE FUNCTION Longvarchar_Io_Null(inout p text) returns text as ' begin update Longvarcharnull_Tab set null_val = $1; select null_val into p from Longvarcharnull_Tab;  end;' language 'plpgsql';

DROP FUNCTION Date_Io_Mfg(date);
CREATE OR REPLACE FUNCTION Date_Io_Mfg(inout p date) returns date as ' begin update Date_Tab set mfg_date = $1; select mfg_date into p from Date_Tab;  end;' language 'plpgsql';

DROP FUNCTION Date_Io_Null(date);
CREATE OR REPLACE FUNCTION Date_Io_Null(inout p date) returns date as ' begin update Date_Tab set null_val = $1; select null_val into p from Date_Tab;  end;' language 'plpgsql';
 
DROP FUNCTION Time_Io_Brk(time) ;
CREATE OR REPLACE FUNCTION Time_Io_Brk(inout p time) returns time as ' begin update Time_Tab set brk_time = $1; select brk_time into p from Time_Tab;  end;' language 'plpgsql';

-- DROP FUNCTION Time_Io_Null(time) ;
CREATE OR REPLACE FUNCTION Time_Io_Null(inout p time) returns time as ' begin update Time_Tab set null_val = $1; select null_val into p from Time_Tab;  end;' language 'plpgsql';

DROP FUNCTION Timestamp_Io_Intime(timestamp);
CREATE OR REPLACE FUNCTION Timestamp_Io_Intime(inout p timestamp) returns timestamp as ' begin update Timestamp_Tab set in_time = $1; select in_time into p from Timestamp_Tab;  end;' language 'plpgsql';

DROP FUNCTION Time_Io_Null(time) ;
CREATE OR REPLACE FUNCTION Time_Io_Null(inout p time) returns time as ' begin update Time_Tab set null_val = $1; select null_val into p from Time_Tab;  end;' language 'plpgsql';

DROP FUNCTION Timestamp_Io_Intime(timestamp);
CREATE OR REPLACE FUNCTION Timestamp_Io_Intime(inout p timestamp) returns timestamp as ' begin update Timestamp_Tab set in_time = $1; select in_time into p from Timestamp_Tab;  end;' language 'plpgsql';

--DROP FUNCTION Timestamp_Io_Null(timestamp);
--CREATE OR REPLACE FUNCTION Timestamp_Io_Null(inout p timestamp) returns timestamp as ' begin update Timestamp_Tab set null_val = $1; select null_val into p from Timestamp_Tab;  end;' language 'plpgsql';

CREATE OR REPLACE FUNCTION Timestamp_Io_Null(inout p timestamp with time zone) returns timestamp with time zone as 'begin update Timestamp_Tab set null_val = $1; select null_val into p from Timestamp_Tab;end;'language 'plpgsql';

DROP FUNCTION Binary_Proc_Io(bytea);
CREATE OR REPLACE FUNCTION Binary_Proc_Io(inout p bytea) returns bytea as ' begin update Binary_Tab set binary_val = $1; select binary_val into p from Binary_Tab;  end;' language 'plpgsql';

DROP FUNCTION Varbinary_Proc_Io(bytea) ;
CREATE OR REPLACE FUNCTION Varbinary_Proc_Io(inout p bytea) returns bytea as ' begin update Varbinary_Tab set varbinary_val = $1; select varbinary_val into p from Varbinary_Tab;  end;' language 'plpgsql';

DROP FUNCTION Longvarbinary_Io(bytea) ;
CREATE OR REPLACE FUNCTION Longvarbinary_Io(inout p bytea) returns bytea as ' begin update Longvarbinary_Tab set longvarbinary_val = $1; select longvarbinary_val into p from Longvarbinary_Tab;  end;' language 'plpgsql';

---------
-- Out
---------

--CREATE OR REPLACE FUNCTION Numeric_In_Max (MAX_PARAM  INTEGER) returns void as ' begin update Numeric_Tab set MAX_VAL=MAX_PARAM;  end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Numeric_In_Max (MAX_PARAM NUMERIC) returns void as 'begin update Numeric_Tab set MAX_VAL=MAX_PARAM;end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Numeric_In_Min (MIN_PARAM  INTEGER) returns void as ' begin update Numeric_Tab set MIN_VAL=MIN_PARAM; end;'language 'plpgsql';

--CREATE OR REPLACE FUNCTION Numeric_In_Null(NULL_PARAM  INTEGER) returns void as ' begin update Numeric_Tab set NULL_VAL=NULL_PARAM; end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Numeric_In_Null(NULL_PARAM NUMERIC) returns void as ' begin update Numeric_Tab set NULL_VAL=NULL_PARAM; end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Decimal_In_Max(MAX_PARAM  DECIMAL) returns void as  ' begin update Decimal_Tab set MAX_VAL=MAX_PARAM; end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Decimal_In_Min (MIN_PARAM  DECIMAL) returns void as ' begin update Decimal_Tab set MIN_VAL=MIN_PARAM; end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Decimal_In_Null (NULL_PARAM  DECIMAL) returns void as ' begin update Decimal_Tab set NULL_VAL=NULL_PARAM; end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Double_In_Max (MAX_PARAM  DOUBLE PRECISION) returns void as ' begin update Double_Tab set MAX_VAL=MAX_PARAM; end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Double_In_Min (MIN_PARAM  DOUBLE PRECISION) returns void as ' begin update Double_Tab set MIN_VAL=MIN_PARAM; end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Double_In_Null(NULL_PARAM  DOUBLE PRECISION) returns void as ' begin update Double_Tab set NULL_VAL=NULL_PARAM; end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Float_In_Max (MAX_PARAM  FLOAT) returns void as ' begin update Float_Tab set MAX_VAL=MAX_PARAM; end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Float_In_Min (MIN_PARAM  FLOAT) returns void as ' begin update Float_Tab set MIN_VAL=MIN_PARAM; end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Float_In_Null (NULL_PARAM  FLOAT) returns void as ' begin update Float_Tab set NULL_VAL=NULL_PARAM; end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Real_In_Max (MAX_PARAM  REAL) returns void as ' begin update Real_Tab set MAX_VAL=MAX_PARAM; end;' language 'plpgsql' ;

--CREATE OR REPLACE FUNCTION Real_In_Min (MIN_PARAM  REAL) returns void as ' begin update Real_Tab set MIN_VAL=MIN_PARAM; end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Real_In_Min (MIN_PARAM double precision) returns void as 'begin update Real_Tab set MIN_VAL=MIN_PARAM;end;' language 'plpgsql' ;

--CREATE OR REPLACE FUNCTION Real_In_Null (NULL_PARAM  REAL) returns void as  ' begin update Real_Tab set NULL_VAL=NULL_PARAM; end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Real_In_Null (NULL_PARAM double precision) returns void as 'begin update Real_Tab set NULL_VAL=NULL_PARAM;end;' language 'plpgsql' ;

--CREATE OR REPLACE FUNCTION Bit_In_Max (MAX_PARAM  SMALLINT) returns void as ' begin update Bit_Tab set MAX_VAL=MAX_PARAM; end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Bit_In_Max (MAX_PARAM boolean) returns void as 'begin update Bit_Tab set MAX_VAL=MAX_PARAM;end;' language 'plpgsql' ;

--CREATE OR REPLACE FUNCTION Bit_In_Min (MIN_PARAM  SMALLINT) returns void as ' begin update Bit_Tab set MIN_VAL=MIN_PARAM; end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Bit_In_Min (MIN_PARAM boolean) returns void as 'begin update Bit_Tab set MIN_VAL=MIN_PARAM; end;' language 'plpgsql' ;

--CREATE OR REPLACE FUNCTION Bit_In_Null (NULL_PARAM  SMALLINT) returns void as ' begin update Bit_Tab set NULL_VAL=NULL_PARAM; end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Bit_In_Null (NULL_PARAM boolean) returns void as 'begin update Bit_Tab set NULL_VAL=NULL_PARAM;end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Bit_In_MinEG () returns void as ' DECLARE thoma SMALLINT ; begin thoma = 10; select Bit_In_Min(thoma); end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Smallint_In_Max (MAX_PARAM  SMALLINT) returns void as ' begin update Smallint_Tab set MAX_VAL=MAX_PARAM;  end;' language 'plpgsql' ;

--CREATE OR REPLACE FUNCTION Smallint_In_Min (MIN_PARAM  SMALLINT) returns void as ' begin update Smallint_Tab set MIN_VAL=MIN_PARAM; end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Smallint_In_Min (MIN_PARAM INT) returns void as 'begin update Smallint_Tab set MIN_VAL=MIN_PARAM;end;' language 'plpgsql' ;

--CREATE OR REPLACE FUNCTION Smallint_In_Null (NULL_PARAM  SMALLINT) returns void as ' begin update Smallint_Tab set NULL_VAL=NULL_PARAM;  end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Smallint_In_Null (NULL_PARAM INT) returns void as 'begin update Smallint_Tab set NULL_VAL=NULL_PARAM;  end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Tinyint_In_Max (MAX_PARAM  NUMERIC) returns void as  ' begin update Tinyint_Tab set MAX_VAL=MAX_PARAM; end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Tinyint_In_Min (MIN_PARAM  NUMERIC) returns void as ' begin update Tinyint_Tab set MIN_VAL=MIN_PARAM; end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Tinyint_In_Null (NULL_PARAM  NUMERIC) returns void as ' begin update Tinyint_Tab set NULL_VAL=NULL_PARAM;  end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Integer_In_Max (MAX_PARAM  INTEGER) returns void as ' begin update Integer_Tab set MAX_VAL=MAX_PARAM; end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Integer_In_Min (MIN_PARAM INTEGER) returns void as ' begin update Integer_Tab set MIN_VAL=MIN_PARAM; end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Integer_In_Null (NULL_PARAM  INTEGER) returns void as ' begin update Integer_Tab set NULL_VAL=NULL_PARAM; end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Bigint_In_Max (MAX_PARAM  NUMERIC) returns void as ' begin update Bigint_Tab set MAX_VAL=MAX_PARAM;  end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Bigint_In_Min (MIN_PARAM  NUMERIC) returns void as ' begin update Bigint_Tab set MIN_VAL=MIN_PARAM; end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Bigint_In_Null (NULL_PARAM  NUMERIC) returns void as ' begin update Bigint_Tab set NULL_VAL=NULL_PARAM; end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Char_In_Name(NAME_PARAM  CHAR) returns void as  ' begin update Char_Tab set COFFEE_NAME=NAME_PARAM; end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Char_In_Null(NULL_PARAM  CHAR) returns void as ' begin update Char_Tab set NULL_VAL=NULL_PARAM; end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Varchar_In_Name(NAME_PARAM  VARCHAR) returns void as ' begin update Varchar_Tab set COFFEE_NAME=NAME_PARAM; end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Varchar_In_Null(NULL_PARAM  VARCHAR) returns void as ' begin update Varchar_Tab set NULL_VAL=NULL_PARAM; end;' language 'plpgsql' ;

--CREATE OR REPLACE FUNCTION Lvarchar_In_Name(NAME_PARAM  BIGINT) returns void as ' begin update Longvarchar_Tab set COFFEE_NAME=NAME_PARAM; end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Lvarchar_In_Name(NAME_PARAM character varying) returns void as 'begin update Longvarchar_Tab set COFFEE_NAME=NAME_PARAM;end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Lvarchar_Io_Null(inout NULL_PARAM character varying) returns character varying as 'begin update Longvarcharnull_Tab set NULL_VAL=NULL_PARAM; select NULL_VAL  into NULL_PARAM from Longvarcharnull_Tab; end;' language 'plpgsql';

drop function Lvarcharnull_Proc(out NULL_PARAM TEXT) ;
create or replace function Lvarcharnull_Proc(out NULL_PARAM TEXT) as ' begin select NULL_VAL into NULL_PARAM from Longvarcharnull_Tab; end ;' language 'plpgsql';

--CREATE OR REPLACE FUNCTION Lvarchar_In_Null(NULL_PARAM  BIGINT) returns void as ' begin update Longvarcharnull_Tab set NULL_VAL=NULL_PARAM; end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Lvarchar_In_Null(NULL_PARAM  character varying) returns void as 'begin update Longvarcharnull_Tab set NULL_VAL=NULL_PARAM;end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Date_In_Mfg(MFG_PARAM  DATE) returns void as ' begin update Date_Tab set MFG_DATE=MFG_PARAM; end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Date_In_Null(NULL_PARAM  DATE) returns void as ' begin update Date_Tab set NULL_VAL=NULL_PARAM; end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Time_In_Brk(BRK_PARAM  DATE) returns void as ' begin update Time_Tab set BRK_TIME=BRK_PARAM; end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Time_In_Null(NULL_PARAM  time without time zone) returns void as ' begin update Time_Tab set NULL_VAL=NULL_PARAM; end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Timestamp_In_Intime(INTIME_PARAM  timestamp with time zone) returns void as ' begin update Timestamp_Tab set IN_TIME=INTIME_PARAM; end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Timestamp_In_Null(NULL_PARAM  timestamp with time zone) returns void as ' begin update Timestamp_Tab set NULL_VAL=NULL_PARAM; end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Binary_Proc_In(BINARY_PARAM  BYTEA) returns void as ' begin update Binary_Tab set BINARY_VAL=BINARY_PARAM; end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Varbinary_Proc_In(VARBINARY_PARAM  BYTEA) returns void as ' begin update Varbinary_Tab set VARBINARY_VAL=VARBINARY_PARAM ; end;' language 'plpgsql' ;

CREATE OR REPLACE FUNCTION Longvarbinary_In(LONGVARBINARY_PARAM  BYTEA) returns void as ' begin update Longvarbinary_Tab set LONGVARBINARY_VAL=LONGVARBINARY_PARAM ; end;' language 'plpgsql' ;

