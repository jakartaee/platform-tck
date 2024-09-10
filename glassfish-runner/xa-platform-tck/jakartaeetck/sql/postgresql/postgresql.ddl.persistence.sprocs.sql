drop table Integer_Tab ;
create table Integer_Tab (MAX_VAL INTEGER, MIN_VAL INTEGER, NULL_VAL INTEGER NULL) ;

DROP FUNCTION Integer_Proc(out pmax integer,out pmen integer,out pnull integer);
CREATE OR REPLACE FUNCTION Integer_Proc(out pmax integer,out pmin integer,out pnull integer) returns record as ' begin select * into pmax, pmin, pnull from Integer_Tab;  end;' language 'plpgsql';

DROP FUNCTION GetEmpOneFirstNameFromOut(out OUT_PARAM text) ;
CREATE OR REPLACE FUNCTION GetEmpOneFirstNameFromOut (out OUT_PARAM text) returns text as ' begin SELECT FIRSTNAME into OUT_PARAM FROM EMPLOYEE WHERE ID=1; END;' language 'plpgsql';

DROP FUNCTION GetEmpFirstNameFromOut(in IN_PARAM int, out OUT_PARAM text) ;
CREATE OR REPLACE FUNCTION GetEmpFirstNameFromOut (in IN_PARAM int, out OUT_PARAM text) returns text as ' begin SELECT FIRSTNAME into OUT_PARAM FROM EMPLOYEE WHERE ID=IN_PARAM; END ;' language 'plpgsql';

DROP FUNCTION GetEmpLastNameFromInOut(inout INOUT_PARAM text) ;
CREATE OR REPLACE FUNCTION GetEmpLastNameFromInOut (inout INOUT_PARAM text) returns text as ' begin SELECT LASTNAME into INOUT_PARAM FROM EMPLOYEE WHERE ID=CAST(INOUT_PARAM AS int); END ;' language 'plpgsql';

DROP FUNCTION GetEmpASCFromRS() ;
CREATE OR REPLACE FUNCTION GetEmpASCFromRS() RETURNS refcursor AS ' DECLARE ref refcursor; BEGIN OPEN ref FOR SELECT ID, FIRSTNAME, LASTNAME, HIREDATE, SALARY from EMPLOYEE ORDER BY ID ASC; RETURN ref; END; ' LANGUAGE plpgsql;

DROP FUNCTION GetEmpIdFNameLNameFromRS(in IN_PARAM int) ;
CREATE OR REPLACE FUNCTION GetEmpIdFNameLNameFromRS(in IN_PARAM int) RETURNS refcursor AS ' DECLARE ref refcursor; BEGIN OPEN ref FOR  SELECT ID, FIRSTNAME, LASTNAME from EMPLOYEE WHERE ID=IN_PARAM; RETURN ref; END; ' LANGUAGE plpgsql;

DROP FUNCTION GetEmpIdUsingHireDateFromOut(in IN_PARAM DATE, out OUT_PARAM int) ;
CREATE OR REPLACE FUNCTION GetEmpIdUsingHireDateFromOut (in IN_PARAM DATE, out OUT_PARAM int) returns int as ' begin SELECT ID into OUT_PARAM FROM EMPLOYEE WHERE HIREDATE=IN_PARAM; END ;' language 'plpgsql';

DROP FUNCTION UpdateEmpSalaryColumn() ;
CREATE OR REPLACE FUNCTION UpdateEmpSalaryColumn() returns void as ' begin UPDATE EMPLOYEE set SALARY=0.00; END ;' language 'plpgsql';

DROP FUNCTION DeleteAllEmp() ;
CREATE OR REPLACE FUNCTION DeleteAllEmp() returns void as ' begin DELETE FROM EMPLOYEE; END ;' language 'plpgsql';
