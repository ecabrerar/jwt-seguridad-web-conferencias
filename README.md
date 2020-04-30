# jwt-seguridad-web-conferencias
Recursos para la presentación Seguridad para aplicaciones Web Java con JSON Web Tokens (JWT)

# Demo Springboot con JWT usando Okta

clonar el repositorio :
git clone https://github.com/ecabrerar/jwt-seguridad-web-conferencias.git

cd spring-boot-token-auth-demo

Para instalar las dependencias y correr el proyecto localmente:
./gradlew bootRun

Ahora necesitamos crear una aplicación en Okta. En este caso la plataforma okta es quien proveerá los JWT.

1. Crear una cuenta en el portal [Okta Developer](https://developer.okta.com/signup)
Luego navegar a Applications > Add Application. 
Hacer click en la opción Web, luego click en Next.
Agregar un nombre a la aplicación que pueda recordar y seleccionar la opción "Client Credentials". Luego hacer click en Done.

Debes copiar el los valores de clientIdy y clientSecret para colocarlos en src/main/resources/application.properties.

okta.oauth2.issuer=https://{yourOktaDomain}/oauth2/default
okta.oauth2.clientId={yourClientId}
okta.oauth2.clientSecret={yourClientSecret}	

Nota:

Actualmente el demo tiene estos valores configurados y solo tienes que correrlo.

2. Instalar curl o [httpie](https://httpie.org)

Hacer petición para obtener un JWT

Generalmente lo que se hace es hacer una petición a la plataforma usando el clientId y el clientSecret codificado en base 64 

Ejemplo:

Authorization: Basic Base64Encode(<clientId>:<clientSecret>)

Tomar en cuenta los (:), estos sirven para unir el clientId y el clientSecret.

Tomando los valores que ya tenemos en el demo, vamos a la página https://www.base64encode.org/ y obtenemos el cifrado de <clientId>:<clientSecret>
Resultando lo siguiente:
MG9hYXBpcWg2SFBReEo0Njk0eDY6cjh6M2N2WjA1LVhTaXk0ZWZ1WFlZVGFtQlNmOWV0WmY2U0NVOHMwNg==

Del archivo src/main/resources/application.properties tomar el valor de la variable okta.oauth2.issuer y agregar lo siguiente /v1/token

Ejemplo :

http -f POST https://dev-847671.okta.com/oauth2/default/v1/token \
'Authorization: Basic MG9hYXBpcWg2SFBReEo0Njk0eDY6cjh6M2N2WjA1LVhTaXk0ZWZ1WFlZVGFtQlNmOWV0WmY2U0NVOHMwNg==' \
grant_type=client_credentials

Si creamos nuestra propia cuenta en Okta, en lugar de usar los datos del demo, hay que crear el scope. Para esto ir a nuestra cuenta, buscar la opción API
y Authorization Servers, luego hacer click en la lista de servidores, en el que dice default. Luego Scopes y click en Add Scope. Colocar un nombre que recordemos y darle click en el botón de crear.

Volvemos a hacer una petición y debe devolver un JWT.

http -f POST https://dev-847671.okta.com/oauth2/default/v1/token \
'Authorization: Basic MG9hYXBpcWg2SFBReEo0Njk0eDY6cjh6M2N2WjA1LVhTaXk0ZWZ1WFlZVGFtQlNmOWV0WmY2U0NVOHMwNg==' \
grant_type=client_credentials \
scope=AuthDemo


Ejemplo:

{
    "access_token": "eyJraWQiOiJiUWJMTU9MZ2hTd18zR1NLWkRNR3RaTWI0ejlLci04VWlZREZnS0ItNEgwIiwiYWxnIjoiUlMyNTYifQ.eyJ2ZXIiOjEsImp0aSI6IkFULmV0aWFMREt0Qk1aWjFoaWdwQXZ1YjdjSVAwZ0RHenBWUERXaUpZN0JlQnciLCJpc3MiOiJodHRwczovL2Rldi04NDc2NzEub2t0YS5jb20vb2F1dGgyL2RlZmF1bHQiLCJhdWQiOiJhcGk6Ly9kZWZhdWx0IiwiaWF0IjoxNTg4MjcwNTE3LCJleHAiOjE1ODgyNzQxMTcsImNpZCI6IjBvYWFwaXFoNkhQUXhKNDY5NHg2Iiwic2NwIjpbIkF1dGhEZW1vIl0sInN1YiI6IjBvYWFwaXFoNkhQUXhKNDY5NHg2In0.ldNS2DMDlqoz00DVCdcR82CU9Nv92a32o1SzpLXHAGuPnNEhroReaOlAysmSquwunw5dMw70oAZOc4SGB-2XzzJh04ZB2CIOZt2WxbmRarBbfSRX01cQ4GSdhyIndObIvtFKF5QxtHXDiH_KgGHikkHqcBw7dz8L4JWmAELIzUWLJt5aNyQ8rlElPFfWwOTE0w_Csl_529bRnbVoavR-pEM289TBUGS7tnPoKGdpUE79OTW-G0Q8I9B67HtyaPe0lcqeXmPTfEDrNEeL7IfShOOkpvsRHSP3yva2trquFuxBq3PqlrSYp_PZ7ahnhf0xTS8v8_VpTa4J-nQJHTN0bA",
    "expires_in": 3600,
    "scope": "AuthDemo",
    "token_type": "Bearer"
}


El JWT corresponde al valor del atributo "access_token"

Tomar el valor del access_token e ir a la página ()[https://jwt.io]
Observar los valores de cada una de las partes que componente el JWT.

Con el token que conseguimos podemos hacer peticiones al servicio web.

http GET http://localhost:8080/jconfdominicana/speakers \
'Authorization: Bearer eyJraWQiOiJiUWJMTU9MZ2hTd18zR1NLWkRNR3RaTWI0ejlLci04VWlZREZnS0ItNEgwIiwiYWxnIjoiUlMyNTYifQ.eyJ2ZXIiOjEsImp0aSI6IkFULmV0aWFMREt0Qk1aWjFoaWdwQXZ1YjdjSVAwZ0RHenBWUERXaUpZN0JlQnciLCJpc3MiOiJodHRwczovL2Rldi04NDc2NzEub2t0YS5jb20vb2F1dGgyL2RlZmF1bHQiLCJhdWQiOiJhcGk6Ly9kZWZhdWx0IiwiaWF0IjoxNTg4MjcwNTE3LCJleHAiOjE1ODgyNzQxMTcsImNpZCI6IjBvYWFwaXFoNkhQUXhKNDY5NHg2Iiwic2NwIjpbIkF1dGhEZW1vIl0sInN1YiI6IjBvYWFwaXFoNkhQUXhKNDY5NHg2In0.ldNS2DMDlqoz00DVCdcR82CU9Nv92a32o1SzpLXHAGuPnNEhroReaOlAysmSquwunw5dMw70oAZOc4SGB-2XzzJh04ZB2CIOZt2WxbmRarBbfSRX01cQ4GSdhyIndObIvtFKF5QxtHXDiH_KgGHikkHqcBw7dz8L4JWmAELIzUWLJt5aNyQ8rlElPFfWwOTE0w_Csl_529bRnbVoavR-pEM289TBUGS7tnPoKGdpUE79OTW-G0Q8I9B67HtyaPe0lcqeXmPTfEDrNEeL7IfShOOkpvsRHSP3yva2trquFuxBq3PqlrSYp_PZ7ahnhf0xTS8v8_VpTa4J-nQJHTN0bA'


http GET http://localhost:8080/jconfdominicana/speakers \
'Authorization: Bearer eyJraWQiOiJiUWJMTU9MZ2hTd18zR1NLWkRNR3RaTWI0ejlLci04VWlZREZnS0ItNEgwIiwiYWxnIjoiUlMyNTYifQ.eyJ2ZXIiOjEsImp0aSI6IkFULmV0aWFMREt0Qk1aWjFoaWdwQXZ1YjdjSVAwZ0RHenBWUERXaUpZN0JlQnciLCJpc3MiOiJodHRwczovL2Rldi04NDc2NzEub2t0YS5jb20vb2F1dGgyL2RlZmF1bHQiLCJhdWQiOiJhcGk6Ly9kZWZhdWx0IiwiaWF0IjoxNTg4MjcwNTE3LCJleHAiOjE1ODgyNzQxMTcsImNpZCI6IjBvYWFwaXFoNkhQUXhKNDY5NHg2Iiwic2NwIjpbIkF1dGhEZW1vIl0sInN1YiI6IjBvYWFwaXFoNkhQUXhKNDY5NHg2In0.ldNS2DMDlqoz00DVCdcR82CU9Nv92a32o1SzpLXHAGuPnNEhroReaOlAysmSquwunw5dMw70oAZOc4SGB-2XzzJh04ZB2CIOZt2WxbmRarBbfSRX01cQ4GSdhyIndObIvtFKF5QxtHXDiH_KgGHikkHqcBw7dz8L4JWmAELIzUWLJt5aNyQ8rlElPFfWwOTE0w_Csl_529bRnbVoavR-pEM289TBUGS7tnPoKGdpUE79OTW-G0Q8I9B67HtyaPe0lcqeXmPTfEDrNEeL7IfShOOkpvsRHSP3yva2trquFuxBq3PqlrSYp_PZ7ahnhf0xTS8v8_VpTa4J-nQJHTN0bA'


http http://localhost:8080/jconfdominicana/sessions/101 \
'Authorization: Bearer eyJraWQiOiJiUWJMTU9MZ2hTd18zR1NLWkRNR3RaTWI0ejlLci04VWlZREZnS0ItNEgwIiwiYWxnIjoiUlMyNTYifQ.eyJ2ZXIiOjEsImp0aSI6IkFULmV0aWFMREt0Qk1aWjFoaWdwQXZ1YjdjSVAwZ0RHenBWUERXaUpZN0JlQnciLCJpc3MiOiJodHRwczovL2Rldi04NDc2NzEub2t0YS5jb20vb2F1dGgyL2RlZmF1bHQiLCJhdWQiOiJhcGk6Ly9kZWZhdWx0IiwiaWF0IjoxNTg4MjcwNTE3LCJleHAiOjE1ODgyNzQxMTcsImNpZCI6IjBvYWFwaXFoNkhQUXhKNDY5NHg2Iiwic2NwIjpbIkF1dGhEZW1vIl0sInN1YiI6IjBvYWFwaXFoNkhQUXhKNDY5NHg2In0.ldNS2DMDlqoz00DVCdcR82CU9Nv92a32o1SzpLXHAGuPnNEhroReaOlAysmSquwunw5dMw70oAZOc4SGB-2XzzJh04ZB2CIOZt2WxbmRarBbfSRX01cQ4GSdhyIndObIvtFKF5QxtHXDiH_KgGHikkHqcBw7dz8L4JWmAELIzUWLJt5aNyQ8rlElPFfWwOTE0w_Csl_529bRnbVoavR-pEM289TBUGS7tnPoKGdpUE79OTW-G0Q8I9B67HtyaPe0lcqeXmPTfEDrNEeL7IfShOOkpvsRHSP3yva2trquFuxBq3PqlrSYp_PZ7ahnhf0xTS8v8_VpTa4J-nQJHTN0bA'

Nota:
Los tokens tienen un tiempo de expiración, para hacer las pruebas con los ejemplos anteriores deben generar sus propios token.
A continuación una forma más sencilla que la anterior.

 RESULT=`http -f POST https://dev-847671.okta.com/oauth2/default/v1/token 'Authorization: Basic MG9hYXBpcWg2SFBReEo0Njk0eDY6cjh6M2N2WjA1LVhTaXk0ZWZ1WFlZVGFtQlNmOWV0WmY2U0NVOHMwNg==' grant_type=client_credentials scope=AuthDemo`
   
   TOKEN=`echo $RESULT | sed 's/.*access_token":"//g' | sed 's/".*//g'`
   
   http GET  http://localhost:8080/jconfdominicana/speakers "Authorization: Bearer $TOKEN"
   
   



