







<html>
<head><title>positiveTxCommitLifeCycleTest</title></head>
<body>


   <!-- Validate sql:transaction action Lifecycle for a committed transaction
    -->

   <h1>Validate sql:transaction action LifeCycle for a committed transaction </h1>
   <p>







    <strong>Dump LifeCycle for sql:transaction action for a committed transaction </strong>
    <p>

       Connection.getTransactionIsolation()
       <br>

       Connection.setAutoCommit(false)
       <br>

       Connection.commit()
       <br>

       Connection.setAutoCommit(true)
       <br>

       Connection.close()
       <br>





</body>
</html>

