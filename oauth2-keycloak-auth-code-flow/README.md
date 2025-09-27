# Getting Started
Project uses [Keycloak](https://www.keycloak.org/) as Identity provider and uses OAuth2 JWT token to protect the API endpoint.

## Protected API endpoint
1. http://<IP>:<port> -> protected by Admin role
[Code](./src/main/java/com/nr/authcodeflow/controller/AdminController.java)
```java
@PreAuthorize("hasRole('ROLE_Admin')")
```
2. http://<IP>:<port> -> protected by User role
[Code](./src/main/java/com/nr/authcodeflow/controller/UserController.java)
```java
@PreAuthorize("hasRole('ROLE_Admin')")
```

## Security 
Added Security Filter change

```java
@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> requests.anyRequest().authenticated())
                .oauth2ResourceServer((oauth2ResourceServer) ->
                        oauth2ResourceServer.jwt()
                                .jwtAuthenticationConverter(jwtAuthConverter)

                );
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return http.build();
    }
```

### Custom role extraction from JWT

Below is trimmed JSON focusing only on the section contains the Roles
```json
{
  "resource_access": {
    "Client_name_configured_in_keycloak": {
      "roles": [
        "User"
      ]
    },
    "account": {
      "roles": [
        ""
      ]
    }
  }
}
```
In above JWT, Roles are not in key:value format. hence needs to write the custom logic to  extract the roles.

In SecurityConfiguration, added below code

```java
.oauth2ResourceServer((oauth2ResourceServer) ->
                        oauth2ResourceServer.jwt()
                                .jwtAuthenticationConverter(jwtAuthConverter)

                );
```

Added [JwtAuthConverter](./src/main/java/com/nr/authcodeflow/config/JwtAuthConverter.java)

## Curl commands

```bash
curl --location 'http://localhost:8080/realms/naveen/protocol/openid-connect/token?grant_type=password&username=admin_user&password=user&client_id=oauth2-client' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'grant_type=password' \
--data-urlencode 'username=admin_user' \
--data-urlencode 'password=password' \
--data-urlencode 'client_id=Client_name_configured_in_keycloak'

```


### Reference Documentation

1. [KeyCloak](https://www.keycloak.org/)
2. [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
3. [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.5.6/maven-plugin)
4. [Create an OCI image](https://docs.spring.io/spring-boot/3.5.6/maven-plugin/build-image.html)
5. [OAuth2 Resource Server](https://docs.spring.io/spring-boot/3.5.6/reference/web/spring-security.html#web.security.oauth2.server)
6. [Spring Web](https://docs.spring.io/spring-boot/3.5.6/reference/web/servlet.html)
