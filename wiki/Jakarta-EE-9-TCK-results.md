# Count/link of the recent test failures

## Date : 2020-08-30


### Full Platform TCK results 

CI Job: [eftl-jakartaeetck-run-900/25/junit-reports-with-handlebars](https://ci.eclipse.org/jakartaee-tck/job/eftl-jakartaeetck-run-900/25/junit-reports-with-handlebars/testSuitesOverview.html)

Glassfish Bundle used: 
https://download.eclipse.org/ee4j/glassfish/weekly/glassfish-6.0.0-SNAPSHOT-2020-08-30.zip


| test group      | failure count | issue link(s) |
| ----------- | ----------- | ----------- |
| ejb32      | 1       |      | 
| webservices12 | 62   |   |
| webservices13 | 1   |  | 


### Web Profile TCK results 

CI Job: [eftl-jakartaeetck-run-web-900/25/junit-reports-with-handlebars](https://ci.eclipse.org/jakartaee-tck/job/eftl-jakartaeetck-run-web-900/25/junit-reports-with-handlebars/testSuitesOverview.html) & [eftl-jakartaeetck-run-web-900/26/junit-reports-with-handlebars](https://ci.eclipse.org/jakartaee-tck/job/eftl-jakartaeetck-run-web-900/26/junit-reports-with-handlebars/testSuitesOverview.html)

Glassfish Web Bundle used: 
https://download.eclipse.org/ee4j/glassfish/weekly/web-6.0.0-SNAPSHOT-2020-08-30.zip

All tests in platform TCK now passes in web profile mode.

| test group      | failure count | issue link(s) |
| ----------- | ----------- | ------------ |
| CDI porting kit | 47 |  https://github.com/eclipse-ee4j/glassfish/issues/23184 |


### Platform TCK Standalone build/run 

All tests in standalone TCKs passes.


## Past Results

Note that on a weekly basis, we will update this page and previous results will just appear below (with the latest results always at top).

## Date : 2020-08-23


### Full Platform TCK results 

CI Job: [eftl-jakartaeetck-run-900/23/junit-reports-with-handlebars](https://ci.eclipse.org/jakartaee-tck/job/eftl-jakartaeetck-run-900/23/junit-reports-with-handlebars/testSuitesOverview.html)

Glassfish Bundle used: 
https://download.eclipse.org/ee4j/glassfish/weekly/glassfish-6.0.0-SNAPSHOT-2020-08-23.zip


| test group      | failure count | issue link(s) |
| ----------- | ----------- | ----------- |
| ejb32      | 1       |      | 
| securityapi   | 5  | https://github.com/eclipse-ee4j/soteria/issues/280 |
| webservices12 | 62   |   |
| webservices13 | 1   |  | 


### Web Profile TCK results 
CI Job: [eftl-jakartaeetck-run-web-900/22/junit-reports-with-handlebars](https://ci.eclipse.org/jakartaee-tck/job/eftl-jakartaeetck-run-web-900/22/junit-reports-with-handlebars/testSuitesOverview.html)

Glassfish Web Bundle used: 
https://download.eclipse.org/ee4j/glassfish/weekly/web-6.0.0-SNAPSHOT-2020-08-23.zip

| test group      | failure count | issue link(s) |
| ----------- | ----------- | ------------ |
| ejb30_lite_packaging      | 31       | |
| ejb30_lite_tx      | 163       | |
| ejb32      | 8       | | 
| jaspic      | 50       | |
| jdbc      | 2386       | |
| jpa      | 1878       | |
| jstl      | 85       | |
| samples      | 1       | |
| CDI porting kit | 47 |  https://github.com/eclipse-ee4j/glassfish/issues/23184 |


### Platform TCK Standalone build/run 

CI Job: [jakartaee-tck/job/eftl-standalonetck-certification](https://ci.eclipse.org/jakartaee-tck/job/eftl-standalonetck-certification/?)
| standalone TCK      | failure count |issue link(s) |
| ----------- | ----------- | -------------- |
| security api      | 5       | https://github.com/eclipse-ee4j/soteria/issues/280 | 


