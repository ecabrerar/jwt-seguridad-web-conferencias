# jwt-seguridad-web-conferencias
Recursos para la presentación Seguridad para aplicaciones Web Java con JSON Web Tokens (JWT)

Ver presentación en [SlideShare](https://www.slideshare.net/eudris/seguridad-para-aplicaciones-web-java-con-json-web-tokens-jwt-2020)

# Demo Springboot con JWT usando Okta

clonar el repositorio :
```bash

git clone https://github.com/ecabrerar/jwt-seguridad-web-conferencias.git
cd spring-boot-token-auth-demo
```


Para instalar las dependencias y correr el proyecto localmente:
```bash
./gradlew bootRun
```
Ahora necesitamos crear una aplicación en Okta. En este caso la plataforma okta es quien proveerá los JWT.

1. Crear una cuenta en el portal [Okta Developer](https://developer.okta.com/signup)
Luego navegar a **Applications > Add Application**. 
Hacer click en la opción **Web**, luego click en **Next**.
Agregar un nombre a la aplicación que pueda recordar y seleccionar la opción **"Client Credentials"**. Luego hacer click en Done.

Debes copiar el los valores de clientIdy y clientSecret para colocarlos en src/main/resources/application.properties.

```bash
okta.oauth2.issuer=https://{yourOktaDomain}/oauth2/default
okta.oauth2.clientId={yourClientId}
okta.oauth2.clientSecret={yourClientSecret}	
```
__Nota:__

Actualmente el demo tiene estos valores configurados y solo tienes que correrlo.

2. Instalar curl o [httpie](https://httpie.org)

Hacer petición para obtener un JWT

Generalmente lo que se hace es hacer una petición a la plataforma usando el **clientId** y el **clientSecret** codificado en base 64 

Ejemplo:
```bash
Authorization: Basic Base64Encode(<clientId>:<clientSecret>)
```
Tomar en cuenta los **(:)**, estos sirven para unir el **clientId** y el **clientSecret**.

Tomando los valores que ya tenemos en el demo, vamos a la página [https://www.base64encode.org](https://www.base64encode.org) y obtenemos el cifrado de <clientId>:<clientSecret>
Resultando lo siguiente:
    
```bash
    MG9hYXBpcWg2SFBReEo0Njk0eDY6cjh6M2N2WjA1LVhTaXk0ZWZ1WFlZVGFtQlNmOWV0WmY2U0NVOHMwNg==
```
Del archivo __src/main/resources/application.properties__ tomar el valor de la variable __okta.oauth2.issuer__ y agregar lo siguiente __/v1/token__

Ejemplo :
```bash
http -f POST https://dev-847671.okta.com/oauth2/default/v1/token \
'Authorization: Basic MG9hYXBpcWg2SFBReEo0Njk0eDY6cjh6M2N2WjA1LVhTaXk0ZWZ1WFlZVGFtQlNmOWV0WmY2U0NVOHMwNg==' \
grant_type=client_credentials
```
Si creamos nuestra propia cuenta en __Okta__, en lugar de usar los datos del demo, hay que crear el scope. Para esto ir a nuestra cuenta, buscar la opción __API__
y __Authorization Servers__, luego hacer click en la lista de servidores, en el que dice __default__. Luego __Scopes__ y click en __Add Scope__. Colocar un nombre que recordemos y darle click en el botón de __create__.

Volvemos a hacer una petición y debe devolver un JWT.
```bash
http -f POST https://dev-847671.okta.com/oauth2/default/v1/token \
'Authorization: Basic MG9hYXBpcWg2SFBReEo0Njk0eDY6cjh6M2N2WjA1LVhTaXk0ZWZ1WFlZVGFtQlNmOWV0WmY2U0NVOHMwNg==' \
grant_type=client_credentials \
scope=AuthDemo
```

Ejemplo:
```json
{
    "access_token": "eyJraWQiOiJiUWJMTU9MZ2hTd18zR1NLWkRNR3RaTWI0ejlLci04VWlZREZnS0ItNEgwIiwiYWxnIjoiUlMyNTYifQ.eyJ2ZXIiOjEsImp0aSI6IkFULmV0aWFMREt0Qk1aWjFoaWdwQXZ1YjdjSVAwZ0RHenBWUERXaUpZN0JlQnciLCJpc3MiOiJodHRwczovL2Rldi04NDc2NzEub2t0YS5jb20vb2F1dGgyL2RlZmF1bHQiLCJhdWQiOiJhcGk6Ly9kZWZhdWx0IiwiaWF0IjoxNTg4MjcwNTE3LCJleHAiOjE1ODgyNzQxMTcsImNpZCI6IjBvYWFwaXFoNkhQUXhKNDY5NHg2Iiwic2NwIjpbIkF1dGhEZW1vIl0sInN1YiI6IjBvYWFwaXFoNkhQUXhKNDY5NHg2In0.ldNS2DMDlqoz00DVCdcR82CU9Nv92a32o1SzpLXHAGuPnNEhroReaOlAysmSquwunw5dMw70oAZOc4SGB-2XzzJh04ZB2CIOZt2WxbmRarBbfSRX01cQ4GSdhyIndObIvtFKF5QxtHXDiH_KgGHikkHqcBw7dz8L4JWmAELIzUWLJt5aNyQ8rlElPFfWwOTE0w_Csl_529bRnbVoavR-pEM289TBUGS7tnPoKGdpUE79OTW-G0Q8I9B67HtyaPe0lcqeXmPTfEDrNEeL7IfShOOkpvsRHSP3yva2trquFuxBq3PqlrSYp_PZ7ahnhf0xTS8v8_VpTa4J-nQJHTN0bA",
    "expires_in": 3600,
    "scope": "AuthDemo",
    "token_type": "Bearer"
}
```

El JWT corresponde al valor del atributo __"access_token"__

Tomar el valor del __access_token__ e ir a la página [https://jwt.io](https://jwt.io)
Observar los valores de cada una de las partes que componente el JWT.

Con el token que conseguimos podemos hacer peticiones al servicio web.
```bash
http GET http://localhost:8080/jconfdominicana/speakers \
'Authorization: Bearer eyJraWQiOiJiUWJMTU9MZ2hTd18zR1NLWkRNR3RaTWI0ejlLci04VWlZREZnS0ItNEgwIiwiYWxnIjoiUlMyNTYifQ.eyJ2ZXIiOjEsImp0aSI6IkFULmV0aWFMREt0Qk1aWjFoaWdwQXZ1YjdjSVAwZ0RHenBWUERXaUpZN0JlQnciLCJpc3MiOiJodHRwczovL2Rldi04NDc2NzEub2t0YS5jb20vb2F1dGgyL2RlZmF1bHQiLCJhdWQiOiJhcGk6Ly9kZWZhdWx0IiwiaWF0IjoxNTg4MjcwNTE3LCJleHAiOjE1ODgyNzQxMTcsImNpZCI6IjBvYWFwaXFoNkhQUXhKNDY5NHg2Iiwic2NwIjpbIkF1dGhEZW1vIl0sInN1YiI6IjBvYWFwaXFoNkhQUXhKNDY5NHg2In0.ldNS2DMDlqoz00DVCdcR82CU9Nv92a32o1SzpLXHAGuPnNEhroReaOlAysmSquwunw5dMw70oAZOc4SGB-2XzzJh04ZB2CIOZt2WxbmRarBbfSRX01cQ4GSdhyIndObIvtFKF5QxtHXDiH_KgGHikkHqcBw7dz8L4JWmAELIzUWLJt5aNyQ8rlElPFfWwOTE0w_Csl_529bRnbVoavR-pEM289TBUGS7tnPoKGdpUE79OTW-G0Q8I9B67HtyaPe0lcqeXmPTfEDrNEeL7IfShOOkpvsRHSP3yva2trquFuxBq3PqlrSYp_PZ7ahnhf0xTS8v8_VpTa4J-nQJHTN0bA'


http GET http://localhost:8080/jconfdominicana/speakers \
'Authorization: Bearer eyJraWQiOiJiUWJMTU9MZ2hTd18zR1NLWkRNR3RaTWI0ejlLci04VWlZREZnS0ItNEgwIiwiYWxnIjoiUlMyNTYifQ.eyJ2ZXIiOjEsImp0aSI6IkFULmV0aWFMREt0Qk1aWjFoaWdwQXZ1YjdjSVAwZ0RHenBWUERXaUpZN0JlQnciLCJpc3MiOiJodHRwczovL2Rldi04NDc2NzEub2t0YS5jb20vb2F1dGgyL2RlZmF1bHQiLCJhdWQiOiJhcGk6Ly9kZWZhdWx0IiwiaWF0IjoxNTg4MjcwNTE3LCJleHAiOjE1ODgyNzQxMTcsImNpZCI6IjBvYWFwaXFoNkhQUXhKNDY5NHg2Iiwic2NwIjpbIkF1dGhEZW1vIl0sInN1YiI6IjBvYWFwaXFoNkhQUXhKNDY5NHg2In0.ldNS2DMDlqoz00DVCdcR82CU9Nv92a32o1SzpLXHAGuPnNEhroReaOlAysmSquwunw5dMw70oAZOc4SGB-2XzzJh04ZB2CIOZt2WxbmRarBbfSRX01cQ4GSdhyIndObIvtFKF5QxtHXDiH_KgGHikkHqcBw7dz8L4JWmAELIzUWLJt5aNyQ8rlElPFfWwOTE0w_Csl_529bRnbVoavR-pEM289TBUGS7tnPoKGdpUE79OTW-G0Q8I9B67HtyaPe0lcqeXmPTfEDrNEeL7IfShOOkpvsRHSP3yva2trquFuxBq3PqlrSYp_PZ7ahnhf0xTS8v8_VpTa4J-nQJHTN0bA'


http http://localhost:8080/jconfdominicana/sessions/101 \
'Authorization: Bearer eyJraWQiOiJiUWJMTU9MZ2hTd18zR1NLWkRNR3RaTWI0ejlLci04VWlZREZnS0ItNEgwIiwiYWxnIjoiUlMyNTYifQ.eyJ2ZXIiOjEsImp0aSI6IkFULmV0aWFMREt0Qk1aWjFoaWdwQXZ1YjdjSVAwZ0RHenBWUERXaUpZN0JlQnciLCJpc3MiOiJodHRwczovL2Rldi04NDc2NzEub2t0YS5jb20vb2F1dGgyL2RlZmF1bHQiLCJhdWQiOiJhcGk6Ly9kZWZhdWx0IiwiaWF0IjoxNTg4MjcwNTE3LCJleHAiOjE1ODgyNzQxMTcsImNpZCI6IjBvYWFwaXFoNkhQUXhKNDY5NHg2Iiwic2NwIjpbIkF1dGhEZW1vIl0sInN1YiI6IjBvYWFwaXFoNkhQUXhKNDY5NHg2In0.ldNS2DMDlqoz00DVCdcR82CU9Nv92a32o1SzpLXHAGuPnNEhroReaOlAysmSquwunw5dMw70oAZOc4SGB-2XzzJh04ZB2CIOZt2WxbmRarBbfSRX01cQ4GSdhyIndObIvtFKF5QxtHXDiH_KgGHikkHqcBw7dz8L4JWmAELIzUWLJt5aNyQ8rlElPFfWwOTE0w_Csl_529bRnbVoavR-pEM289TBUGS7tnPoKGdpUE79OTW-G0Q8I9B67HtyaPe0lcqeXmPTfEDrNEeL7IfShOOkpvsRHSP3yva2trquFuxBq3PqlrSYp_PZ7ahnhf0xTS8v8_VpTa4J-nQJHTN0bA'
```
__Nota:__
Los tokens tienen un tiempo de expiración, para hacer las pruebas con los ejemplos anteriores deben generar sus propios token.
**A continuación una forma más sencilla que la anterior.**
```bash
 RESULT=`http -f POST https://dev-847671.okta.com/oauth2/default/v1/token 'Authorization: Basic MG9hYXBpcWg2SFBReEo0Njk0eDY6cjh6M2N2WjA1LVhTaXk0ZWZ1WFlZVGFtQlNmOWV0WmY2U0NVOHMwNg==' grant_type=client_credentials scope=AuthDemo`
   
   TOKEN=`echo $RESULT | sed 's/.*access_token":"//g' | sed 's/".*//g'`
   
   http GET  http://localhost:8080/jconfdominicana/speakers "Authorization: Bearer $TOKEN"
   
 ```  
## Demo con keycloak

  
###   Descargar Keycloak 
   1. Docker
   ```bash
   docker pull quay.io/keycloak/keycloak
   ```
   
   2. [https://www.keycloak.org/downloads.html](https://www.keycloak.org/downloads.html)
   
   Arrancar keycloak en docker
  ```bash
     docker run -p 8080:8080 -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=admin quay.io/keycloak/keycloak
  ```

   Entrar a la consola de keycloak
   [http://localhost:8080/auth/admin](http://localhost:8080/auth/admin)
   
   Digitar la clave especificada anteriormente

  Ver guía [Keycloak con Docker](https://www.keycloak.org/getting-started/getting-started-docker) para más detalles.

Por defecto keycloak tiene el realms **master**, para correr nuestro demo vamos a crear un realm y le pondremos **AuthDemo**, protocolo __openid-connect__, Access Type __public__.
  Luego iremos a la opción de __roles__ y crear un nuevo rol llamado __ROL_API_DEMO_WS__.
  
  Ahora procedemos a crear un usuario con los siguientes valores:
  **username : demo
  First Name : Demo
  Last Name : JUG**
  
  Le damos a salvar y ahora vamos a pestaña __Rol Mapping__ y le agregamos el __ROL_API_DEMO_WS__ a este usuario.
  En la pestaña **credentials** le asignaremos una contraseña **"demojug2020"** y quitar la opción de passowrd temporal. **Temporary password off**.
  
  Para verificar que todo está bien podemos entrar a [](http://localhost:8080/auth/realms/AuthDemo/account)
  
  Colocamos usuario y password y debe permitirnos loguearnos.
  
  Con el usuario y cliente creado podemos pedir tokens ejecutando el siguiente comando   

```bash   
  curl --data "grant_type=password&client_id=authdemo&username=demo&password=demojug2020" http://localhost:8080/auth/realms/AuthDemo/protocol/openid-connect/token
```

 Hasta ahora tenemos un usuario y un cliente que nos permiten generar token, necesitamos crear clientes para las aplicaciones que queremos autenticar.
 
 Vamos a crear otro cliente que le llamaremos **demoauth-ws** y el protocolo **openid-connect** y el Access Type **bearer-only**.
 
 Cuando queremos utilizar keycloak para autenticar un servicio rest el protocolo debe ser **bearer-only**, con esto nos aseguramos que no sea redireccionado a una pantalla de login cuando hacemos una petición.
 
 Luego de crear el cliente vamos a la pestaña de Installation y seleccionamos la opción keycloak OIDC json y vamos a obtener el siguiente json:

```json
{
  "realm": "AuthDemo",
  "bearer-only": true,
  "auth-server-url": "http://localhost:8080/auth/",
  "ssl-required": "external",
  "resource": "demoauth-ws",
  "confidential-port": 0
}
```

En nuestra aplicación tenemos que tener esta información en el application.properties en el caso de spring-boot.

Ahora vamos a revisar el demo de keycloak con spring-boot.
```bash
cd spring-boot-keycloak-token-auth-demo
```

En el archivo __src/main/resources/application.properties__ tenemos la siguiente configuración:
```plantext
server.port=8081

keycloak.security-constraints[0].authRoles[0]=ROL_API_DEMO_WS

keycloak.security-constraints[1].securityCollections[0].patterns[0]=/jconfdominicana/*

keycloak.auth-server-url=http://localhost:8080/auth/
keycloak.realm=AuthDemo
keycloak.resource=demoauth-ws
keycloak.credentials.secret=9c7059b3-b04d-4fb8-8b8d-6203be46c1da

keycloak.ssl-required=external
keycloak.bearer-only=true
keycloak.use-resource-role-mappings=true
keycloak.confidential-port=0
```

La información que estamos viendo es la información del cliente y la información del endpoint que estamos asegurando.
Hay una información que no la tenemos en el json anterior y es **keycloak.credentials.secret**, esa información se obtiene en la pestaña de **Credentials** del cliente y el atributo **Secret**.

Vamos a correr el demo.
```bash
mvn clean spring-boot:run
```
y hacemos peticiones al servicio rest

```bash
RESULT=`curl --data "grant_type=password&client_id=authdemo&username=demo&password=demojug2020" http://localhost:8080/auth/realms/AuthDemo/protocol/openid-connect/token`

TOKEN=`echo $RESULT | sed 's/.*access_token":"//g' | sed 's/".*//g'`

curl http://localhost:8081/jconfdominicana/sessions -H "Authorization: bearer $TOKEN"
```

### Referencias:

* [https://www.baeldung.com/spring-boot-keycloak](https://www.baeldung.com/spring-boot-keycloak)
* [https://www.keycloak.org/getting-started/getting-started-docker](https://www.keycloak.org/getting-started/getting-started-docker)
* [https://quay.io/repository/keycloak/keycloak](https://quay.io/repository/keycloak/keycloak)
* [Keycloak: Core concepts of open source identity and access management](https://developers.redhat.com/blog/2019/12/11/keycloak-core-concepts-of-open-source-identity-and-access-management/?sc_cid=701f2000000RtqCAAS)

### Autor: 
Eudris Cabrera
* [Github](https://github.com/ecabrerar)
* [Twitter](https://twitter.com/eudriscabrera)
* [Linkedin](https://www.linkedin.com/in/eudriscabrera)
