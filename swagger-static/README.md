### A spring boot project to test swagger ui with static json files.

- Deploys the swagger-ui code as generated by the sub-project 'swagger-ui'
- Useful for testing actual swagger json files to see how the ui behaves
- `com.ak.swagger.controller.SwaggerDocController` serves the actual json files.

### Running :

``` groovy
 ./gradlew :swagger-static:bootRun
```
http://localhost:8080/index.html


Paste 'http://localhost:8080/api-docs' into the swagger explore text field


Spring boot//gradle/jetty does not stop cleanly and you get a 'Port already in use' error:
use

`kill -9 $(ps aux | grep 'gradle' | awk '{print $2}')`

`alias killgradle="kill -9 $(ps aux | grep 'gradle' | awk '{print $2}')"`