






<html>
<head><title>positivePDTimeZonePrecedenceTest</title></head>
<body>

    <!-- The time zone to be used when formatting operates
              with the following order of presendence:
              - If timeZone specified, use that value.
              - If wrapped by a timeZone action, use that 
                value.
              - Use the value of the scoped attribute
                javax.servlet.jsp.jstl.fmt.timeZone. -->
    <br>TimeZone attribute specified with a value of PST:<br>
      Wrapped by &lt;fmt:timeZone&gt; action with MST.  Time should be offset by 3 hours:<br>
      
        
      
      Nov 21, 2000 6:45 AM<br>

      Not wrapped.  Page has a time zone of EST, timeZone attribute specified.  Time should be offset by 3 hours:<br>
      <br>
      Nov 21, 2000 6:45 AM<br>

    <br>No TimeZone attribute specified:<br>
      Wrapped by &lt;fmt:timeZone&gt; action with MST.  Time should be offset by 2 hours:<br>
      
        <br>
      
      Nov 21, 2000 5:45 AM<br>
      
      Not wrapped.  Page has a time zone of EST.  Time should not be offset:<br>
      <br>
      Nov 21, 2000 3:45 AM<br>
    <br>

</body>
</html>


