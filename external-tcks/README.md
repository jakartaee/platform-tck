# JakartaEE TCK tests from extenral sources

The TCK tests for some specs are housed in other repositories and are provided as binary Maven artifacts. Those maven artifacts can then be consumed
by other maven projects and run with various implementations.

The external TCK tests are aggregated here and each implementation has its own profile for verification.

## How to run

To run the JSON-B TCK tests in standalone mode with Yasson for example, you can do:

```
cd jsonb
mvn verify -Pstandalone
```

To run the Bean Validation tests with Liberty, you can do:

```
cd beanvalidation
mvn verify -Pliberty
```

## Supported profiles

- liberty

