mvn -Dtcktestgroup=jpa -Dts.home=/home/smarlow/tck/tck10/jakartaeetck org.openrewrite.maven:rewrite-maven-plugin:runNoFork  2>&1 | tee /tmp/tck.log
