







<html>
<head><title>positiveTxIsolationAttributeRepeatable_Read</title></head>
<body>







   <!-- Validate sql:transaction action which specifies repeatable_read for the
          isolation attribute  -->

   <h1>Validate sql:transaction action which specifies repeatable_read for the
          isolation attribute </h1>
   <p>







    <strong>Dump LifeCycle for sql:transaction action setting isolation attribute</strong>
    <p>

       Connection.getTransactionIsolation()
       <br>

       Connection.setAutoCommit(false)
       <br>

       Connection.setTransactionIsolation(TRANSACTION_REPEATABLE_READ)
       <br>

       Connection.commit()
       <br>

       Connection.setTransactionIsolation()
       <br>

       Connection.setAutoCommit(true)
       <br>

       Connection.close()
       <br>





</body>
</html>

