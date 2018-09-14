





<html>
<head><title>positiveFDTimeZonePrecedenceTest</title></head>
<body>

    <!--  The time zone to be used when formatting operates
              with the following order of presendence:
              - If timeZone specified, use that value.
              - If wrapped by a timeZone action, use that 
                value.
              - Use the value of the scoped attribute
                javax.servlet.jsp.jstl.fmt.timeZone. -->
    <br>TimeZone attribute specified with a value of PST:<br>
      Wrapped by &lt;fmt:timeZone&gt; action with MST.  Time should be 7:11:34 PM:<br>
      
        Dec 26, 1997 7:11:34 PM<br>
      

      Not wrapped.  Page has a time zone of EST.  Time should be 7:11:34 PM<br>
      Dec 26, 1997 7:11:34 PM<br>

    <br>No TimeZone attribute specified:<br>
      Wrapped by &lt;fmt:timeZone&gt; action with MST.  Time should be 8:11:34 PM:<br>
      
        Dec 26, 1997 8:11:34 PM<br>
      

      Not wrapped.  Page has a time zone of EST.  Time should be 10:11:34 PM<br>
      Dec 26, 1997 10:11:34 PM<br>
    <br>

</body>
</html>


