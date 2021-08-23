About This Resource Adapter:
This adapter is to be used as an embedded rar by tests located within the connector
directory structure at:  src/com/sun/ts/tests/connector/resourceDefs

This rar should not get deployed with the other whitebox rar files but 
instead is intended to get bundled into the .ear files within the 
resourceDefs/ejb and resourceDefs/servlet directorys.

After building this directory, there should be a whitebox-rd.rar file that
will exist in TS_HOME/dist/com/sun/ts/tests/common/connector/embedded/adapter1
This rar (whitebox-rd.rar) is going to be used for testing the connector
Resource Definitions (ConnectionFactoryDefinition and AdminObjectDefiniton).

