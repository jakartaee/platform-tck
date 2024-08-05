





<html>
<head><title>positivePDTimeStyleTest</title></head>
<body>

     <!-- dateStyle specifies formatting style that determines how
             the provided value will be parsed. dateStyle should be
             applied when type is not specified or is set to date or                  both.  -->
     <br>'type' not specified -- timeStyle should not be applied. If it is, a parse exception would occur.<br>
     Tue Nov 21 00:00:00 EST 2000<br>
     Tue Nov 21 00:00:00 EST 2000<br>
     Tue Nov 21 00:00:00 EST 2000<br>
     Tue Nov 21 00:00:00 EST 2000<br>
     Tue Nov 21 00:00:00 EST 2000<br>
     Tue Nov 21 00:00:00 EST 2000<br>
     Tue Nov 21 00:00:00 EST 2000<br>
     Tue Nov 21 00:00:00 EST 2000<br>
     Tue Nov 21 00:00:00 EST 2000<br>
     Tue Nov 21 00:00:00 EST 2000<br>
     Tue Nov 21 00:00:00 EST 2000<br>
     Tue Nov 21 00:00:00 EST 2000<br>

     <br>'type' set to 'date' -- timeStyle should not be applied. If it is, a parse exception would occur.<br>
     Tue Nov 21 00:00:00 EST 2000<br>
     Tue Nov 21 00:00:00 EST 2000<br>
     Tue Nov 21 00:00:00 EST 2000<br>
     Tue Nov 21 00:00:00 EST 2000<br>
     Tue Nov 21 00:00:00 EST 2000<br>
     Tue Nov 21 00:00:00 EST 2000<br>
     Tue Nov 21 00:00:00 EST 2000<br>
     Tue Nov 21 00:00:00 EST 2000<br>
     Tue Nov 21 00:00:00 EST 2000<br>
     Tue Nov 21 00:00:00 EST 2000<br>
     Tue Nov 21 00:00:00 EST 2000<br>
     Tue Nov 21 00:00:00 EST 2000<br>

     <br>'type' set to 'time' -- timeStyle should be applied.<br>
     Thu Jan 01 03:45:03 EST 1970<br>
     Thu Jan 01 03:45:03 EST 1970<br>
     Thu Jan 01 03:45:03 EST 1970<br>
     Thu Jan 01 03:45:03 EST 1970<br>
     Thu Jan 01 03:45:00 EST 1970<br>
     Thu Jan 01 03:45:00 EST 1970<br>
     Thu Jan 01 03:45:02 EST 1970<br>
     Thu Jan 01 03:45:03 EST 1970<br>
     Thu Jan 01 03:45:03 EST 1970<br>
     Thu Jan 01 03:45:03 EST 1970<br>
     Thu Jan 01 03:45:03 EST 1970<br>
     Thu Jan 01 03:45:03 EST 1970<br>

     <br>'type' set to 'both' -- timeStyle should be applied.<br>
     Tue Nov 21 03:45:02 EST 2000<br>
     Tue Nov 21 03:45:02 EST 2000<br>
     Tue Nov 21 03:45:02 EST 2000<br>
     Tue Nov 21 03:45:02 EST 2000<br>
     Tue Nov 21 03:45:00 EST 2000<br>
     Tue Nov 21 03:45:00 EST 2000<br>
     Tue Nov 21 03:45:02 EST 2000<br>
     Tue Nov 21 03:45:02 EST 2000<br>
     Tue Nov 21 03:45:02 EST 2000<br>
     Tue Nov 21 03:45:02 EST 2000<br>
     Tue Nov 21 03:45:02 EST 2000<br>
     Tue Nov 21 03:45:02 EST 2000<br>

</body>
</html>


