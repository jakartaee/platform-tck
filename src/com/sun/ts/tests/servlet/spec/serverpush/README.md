# HTTP/2 Tests

This package tests HTTP/2 functionality.
It uses an HTTP/2 client found in `lib/http.jar`.
The JAR is the Java 9 `jdk.incubator.http` module modified to run on Java 8.
The code used for these tests originally had a dependency on `sun.misc.InnocuousThread` although these tests did not actually use that class.
`sun.misc.InnocuousThread` is not available on Java 11 and that prevented the execution of these HTTP/2 tests when running on Java 11.

Several options where considered to address this issue.
The solution selected was to use Apache Commons BCEL to remove the unused utility methods that referred to `sun.misc.InnocuousThread`.

The steps taken to modify `lib/http.jar` were:
 - Unpack the contents of `lib/http.jar` to an empty directory
 - Renamed `jdk/incubator/http/internal/common/Utils.class` to `jdk/incubator/http/internal/common/Utils.class.original`
 - Run the code below to remove the references to  `sun.misc.InnocuousThread`
 - Re-pack the JAR file so it contained both the renamed original class and the modified class.

The code used to modify `lib/http.jar` was:

```
package org.apache.markt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.bcel.classfile.ClassFormatException;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ClassGen;

public class HttpJarFix {

    public static void main(String[] args) throws ClassFormatException, IOException {

        // Open the class file
        File f1 = new File("/home/mark/Documents/http.jar/jdk/incubator/http/internal/common/Utils.class.original");
        InputStream is = new FileInputStream(f1);

        // Parse the file
        ClassParser parser = new ClassParser(is, "Utils.class");
        JavaClass javaClass = parser.parse();

        // Find the method(s) to remove
        ClassGen modClass = new ClassGen(javaClass);
        Method toRemove;
        do {
            toRemove = null;
            Method[] methods = modClass.getMethods();
            for (Method m : methods) {
                if (m.getName().contains("innocuousThreadPool")) {
                    toRemove = m;
                    break;
                }
            }
            modClass.removeMethod(toRemove);
        } while (toRemove != null);

        modClass.update();

        // Write the file back out
        File f2= new File("/home/mark/Documents/http.jar/jdk/incubator/http/internal/common/Utils.class");
        modClass.getJavaClass().dump(f2);
    }
}
```

Once the TCK no longer needs to execute on Java 8, this package can be updated to use the `java.net.http` module in Java 11 onwards and `lib/http.jar` may be removed.