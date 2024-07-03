<p align="center"><img src="https://i.imgur.com/I4C3x0Q.png" width="150"></p> 

#### CatalogApp
Se pensó en una aplicación estilo catálogo de comercio electrónico. Se puede visualizar el diseño de la aplicación [acá](https://www.figma.com/design/BG3UMGzLi1efG52COftCH9/TPO-DESARROLLO-APPS-I?t=8bFwuAWWMTXF6Do0-0). La idea principal es permitir al usuario crear una cuenta (o ingresar, si es que ya posee una), para ver la pantalla de inicio (Catálogo), donde podrá hacer uso de diversos filtros para una búsqueda personalizada.

Una vez encuentre el producto que está buscando, podrá agregarlo a favoritos y persistir esos datos dentro de su cuenta. 

También se le permitirá visualizar el detalle del producto. 

La aplicación está desarrollada con Kotlin en Android Studio.

**Patrón arquitectónico utilizado**: MVVM<br>
**Patrones de diseño**: Singleton <br>
[Código](https://github.com/zarate10/catalogapp/tree/main/app/src/main/java/ar/edu/uade/tpo) <br>

<hr>

## Uso 

Pasos para arrancar la app: 
1. Crear una aplicación en el `devcenter` de MercadoLibre. Dicha app proveerá un `client_id` (App ID) y un `client_secret`.
2. Editar el scope de la aplicación de MercadoLibre para que admita lectura y acceso offline.
3. Seguir la guía de [Autenticación y Autorización](https://developers.mercadolibre.com.ar/es_ar/autenticacion-y-autorizacion/) de MercadoLibre para obtener un access_token (duración 6 hs) y un refresh_token (duración 6 meses).
4. Rellenar los campos pertinentes con la información generada de forma anterior en MeliDataSource:
```kotlin
// Data -> MeliDataSource
        suspend fun refreshToken(): Token {
            val api = Retrofit.Builder()
                .baseUrl("https://api.mercadolibre.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(MeliAPI::class.java)

                val tokenRequest = TokenRequest(
                    grant_type = "refresh_token",
                    client_id = "ACÁ AGREGAR CLIENT ID",
                    client_secret = "ACÁ AGREGAR TOKEN SECRETO",
                    refresh_token = "ACÁ AGREGAR REFRESH TOKEN"
                )

            return api.refreshToken(tokenRequest)
        }
```
5. Generar el certificado SHA-1 de la app.
6. Sincronizar el certificado SHA-1 con Firebase para habilitar servicios como la autenticación y el almacenamiento en la nube.
7. Abrir aplicación en Android Studio y sincronizar Gradle. 

## Preview app
Preview | Especificación requerida y descripción |
--- | --- |
<img src="https://i.imgur.com/UMZ22l4.gif" alt="Splash Screen" width="350" /> | **Splash**<br>El splash ahora sirve para hacer una pausa hasta obtener el access_token de la API de MercadoLibre, en el momento de la obtención se esperará 1,5 s para pasar a la siguiente pantalla (HomeActivity) y se actualizará el TOKEN dentro de MeliDataSource, lo que permitirá acceder desde cualquier dispositivo a los detalles de los productos. | 
<img src="https://i.imgur.com/BWrLPcV.gif" alt="Login Screen" width="350" /> | **Registro y Login de Usuario** <br> Se hace uso de Google Firebase para el registro y autenticación de un usuario. Se relaciona un producto a un usuario y así se obtienen sus favoritos (en este caso, a través de su correo electrónico). | 
<img src="https://i.imgur.com/Izhw49U.gif" alt="Catalog Screen" width="350" /> | **Pantalla de listado** <br> A través de Retrofit, la aplicación realiza peticiones HTTP a la API que provee MercadoLibre para obtener un listado de productos, como así tmabién los detalles respectivos de cada producto. <br><br>**Endpoint/s**: <ul><br><li>GET `/sites/MLA/search?q=$STRING`</li></ul> | 
<img src="https://i.imgur.com/A756HYr.gif" alt="Search Screen" width="350" /> | **Búsqueda** <br> En la misma pantalla del listado se encuentra un input que permite modificar dicho listado a través de la búsqueda por nombre, además de también, permitir ordenar la búsqueda por mayor o menor precio según corresponda, tal como se visualiza en el preview.  <br><br>**Endpoint/s**: <ul><br><li>GET `/sites/MLA/search?q=$STRING`</li><li>GET `/sites/MLA/search?q=$STRING&sort=[price_asc OR price_desc]`</li></ul> |
<img src="https://i.imgur.com/AzhyPAU.gif" alt="Detail and Favs Screen" width="350" /> | **Descripción y favs** <br> Cada producto tiene un ID. Se hace uso del susodicho ID para obtener los detalles (descripción) a través de una segunda consulta a la API de MercadoLibre a la hora de iniciar la DetailActivity.<br> También los productos pueden agregarse a favoritos, esto se logra a través de Firestore; dentro se guarda una colección de favoritos: compuesta por campos Favorite. Favorite como modelo está constituido por un userID (email del usuario previamente obtenido en la autenticación) y productID (id obtenido del catálogo de productos) <br><br>**Endpoint/s**: <ul><br><li>GET `/items/{productID}/description`</li><li>Métodos accesorios de Firestore.</li></ul> |
<img src="https://i.imgur.com/bhTyXVM.gif" alt="Room Screen" width="350" /> | **Uso de room** <br> Se utilizó Room para guardar de forma local (algo así como un caché) los elementos que fueron agregados a favoritos. Room se sincroniza con Firestore al momento de iniciar la pantalla principal de la aplicación e intercepta a cada enlace que hace el ViewHolder del RecyclerView principal para ver si dicho elemento se encuentra almacenado localmente y de ser así, se le asigna un icono u otro (star_fill para cuando se encuentra que la ID está almacenada localmente y star para cuando no es así), lo que permite explicitar en un primer pantallazo si un elemento se encuentra o no en la lista de favoritos.  |

Para sincronizar la información se utilizan hilos de ejecución paralelos que eventualmente garantizan la consistencia de los datos. Además, se ha implementado el ciclo de vida de actividades (Activity Lifecycle) junto con sus respectivos callbacks para mantener la consistencia entre pantallas.

En la última actualización se agregó como loader a la librería Shimmer, se quitó el icono default de android y además se hizo que cuando el input de búsqueda se mande, se baje de manera automática el keyboard.
## API
API https://api.mercadolibre.com/sites/MLA <br>
$SORT = ‘price_asc’ | ‘price_desc’ | ‘relevance’

<hr>

**Lautaro Zarate** <br>
Desarrollo de Aplicaciones, <br>
Facultad de Ingeniería y Ciencias Exactas,<br>
UADE.



