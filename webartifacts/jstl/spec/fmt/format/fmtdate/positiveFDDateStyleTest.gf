






<html>
<head><title>positiveFDDateStyleTest</title></head>
<body>

     <!-- dateStyle specifies what date style the formated value                  will be returned in.  This will only be applied
             when type is not specified, or is set to date or both.                   if dateStyle is not specified, the default of 'default' 
             will be applied if applicable. -->
     <br>'type' not specified -- dateStyle should be applied<br>
     Dec 26, 1997<br>
     Dec 26, 1997<br>
     Dec 26, 1997<br>
     Dec 26, 1997<br>
     12/26/97<br>
     12/26/97<br>
     Dec 26, 1997<br>
     Dec 26, 1997<br>
     December 26, 1997<br>
     December 26, 1997<br>
     Friday, December 26, 1997<br>
     Friday, December 26, 1997<br>

     <br>'type' set to 'date' -- dateStyle should be applied<br>
     Dec 26, 1997<br>
     Dec 26, 1997<br>
     Dec 26, 1997<br>
     Dec 26, 1997<br>
     12/26/97<br>
     12/26/97<br>
     Dec 26, 1997<br>
     Dec 26, 1997<br>
     December 26, 1997<br>
     December 26, 1997<br>
     Friday, December 26, 1997<br>
     Friday, December 26, 1997<br>

     <br>'type' set to 'time' -- dateStyle should not be applied<br>          10:11:34 PM<br>
     10:11:34 PM<br>
     10:11:34 PM<br>
     10:11:34 PM<br>
     10:11:34 PM<br>
     10:11:34 PM<br>
     10:11:34 PM<br>
     10:11:34 PM<br>
     10:11:34 PM<br>
     10:11:34 PM<br>
     10:11:34 PM<br>
     10:11:34 PM<br>

     <br>'type' set to 'both' -- dateStyle should be applied<br>
     Dec 26, 1997 10:11:34 PM<br>
     Dec 26, 1997 10:11:34 PM<br>
     Dec 26, 1997 10:11:34 PM<br>
     Dec 26, 1997 10:11:34 PM<br>
     12/26/97 10:11:34 PM<br>
     12/26/97 10:11:34 PM<br>
     Dec 26, 1997 10:11:34 PM<br>
     Dec 26, 1997 10:11:34 PM<br>
     December 26, 1997 10:11:34 PM<br>
     December 26, 1997 10:11:34 PM<br>
     Friday, December 26, 1997 10:11:34 PM<br>
     Friday, December 26, 1997 10:11:34 PM<br>

</body>
</html>


