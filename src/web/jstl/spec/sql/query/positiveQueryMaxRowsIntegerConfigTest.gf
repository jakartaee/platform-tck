







<html>
<head><title>positiveQueryMaxRowsIntegerConfigTest</title></head>
<body>







   <h1>Validating sql:query action and javax.servlet.jsp.jstl.sql.maxRows config
   variable passed as an Integer </h1>
   <p>

   <!-- Validate that a sql:query action will return
            the number of rows specified by the javax.servlet.jsp.jstl.sql.maxRows
            config variable  when specified as an Integer  -->










         The <strong>javax.servlet.jsp.jstl.sql.maxRows</strong> config
         variable was set to <strong>5
         </strong> and a Result object that contained this row count was
         returned as expected.
         <p>











         The maxRows attribute <strong>did override</strong> the config value for <strong>
         javax.servlet.jsp.jstl.sql.maxRows</strong> and a Result object that
         contained  <strong>"2"</strong>
         rows was returned as expected.
         <p>











         The config parameter <strong>javax.servlet.jsp.sql.maxRows</strong>
         was set to <strong>-1</strong>.
         A Result object that contained <strong>
         10</strong> rows was returned as
         expected.
         <p>







</body>
</html>

