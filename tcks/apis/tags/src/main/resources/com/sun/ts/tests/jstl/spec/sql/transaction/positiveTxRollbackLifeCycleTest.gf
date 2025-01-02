







<html>
<head><title>positiveTxRollbackLifeCycleTest</title></head>
<body>


   <!-- Validate sql:transaction action Lifecycle for a rolledback transaction
    -->

   <h1>Validate sql:transaction action LifeCycle for a rolledback transaction </h1>
   <p>





    <strong>Dump LifeCycle for sql:transaction action for commited transaction </strong>
    <p>

       Connection.getTransactionIsolation()
       <br>

       Connection.setAutoCommit(false)
       <br>

       Connection.rollback()
       <br>

       Connection.setAutoCommit(true)
       <br>

       Connection.close()
       <br>





</body>
</html>

