





<html>
<head><title>positiveOutEscXmlTest</title></head>
<body>


     <!-- If escapeXml is true, the following characters will
             be converted to their corresponding entity codes:
             < -> &lt;
             > -> &gt;
             & -? &amp;
             ' -> &#039;
             " -> &#034
             If false, no escaping will occur.
             If escapeXml is not specified, escaping will occur. -->
     escapeXml == true: 
          &lt; &gt; &amp; &#039; &#034;
        
     escapeXml == false: 
          < > & ' "
        <br>
     escapeXml == not specified: 
          &lt; &gt; &amp; &#039; &#034;
        <br>

</body>
</html>


