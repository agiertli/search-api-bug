This fails:
```
mvn clean -Dtest=SearchAPITest#searchApiTestParameterWithDash test 
```

This works:
```
mvn clean -Dtest=SearchAPITest#searchApiTestParameterWithoutDash test
```
