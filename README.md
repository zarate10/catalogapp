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
### Uso 

Para que la función arranque: 
1. Sincronizar las dependencias de Gradle una vez se monte el proyecto en Android Studio.
2. Obtener un TOKEN para el consumo de la API de MercadoLibre. El mismo deberá reemplazar "TOKEN_PRIVADO" en MeliDataSource:
   
```kotlin
private val API_BASE_URL = "https://api.mercadolibre.com/"
private val TOKEN = "TOKEN_PRIVADO"
```

### Screenshots
<img src="https://i.imgur.com/qA68xAY.png" alt="Splash Screen" width="220" /><img src="https://i.imgur.com/6gyXXHX.png" alt="Login Screen" width="220" /><img src="https://i.imgur.com/y934mRl.jpeg" alt="Catalog Screen" width="220" /><img src="https://i.imgur.com/eifXGkQ.jpeg" alt="Favs Screen" width="220" /><img src="https://i.imgur.com/wkzDpmQ.jpeg" alt="No Favs Screen" width="220" /><img src="https://i.imgur.com/azbXGwF.jpeg" alt="Detail Screen" width="220" />

### Preview app
Preview | Especificación y descripción |
--- | --- |
<img src="https://i.imgur.com/UMZ22l4.gif" alt="Splash Screen" width="350" /> | **Splash** | 
<img src="https://i.imgur.com/BWrLPcV.gif" alt="Login Screen" width="350" /> | **Registro y Login de Usuario** <br> Se hace uso de Google Firebase para el registro y autenticación de un usuario. Esto con el fin de relacionar un producto a un usuario y así obtener sus favoritos (en este caso, a través de su correo electrónico). | 
<img src="https://i.imgur.com/mkEARZX.gif" alt="Catalog Screen" width="350" /> | **Pantalla de listado** <br> A través de Retrofit, la aplicación realiza peticiones HTTP a la API que provee MercadoLibre para obtener un listado de productos, como así tmabién los detalles respectivos de cada producto. | 
<img src="https://i.imgur.com/Uhpgvsn.gif" alt="Search Screen" width="350" /> | **Búsqueda** <br> En la misma pantalla del listado se encuentra un input que permite modificar dicho listado a través de la búsqueda por nombre, además de también, permitir ordenar la búsqueda por mayor o menor precio como se ve en el preview. |
<img src="https://i.imgur.com/b3jkaGz.gif" alt="Detail and Favs Screen" width="350" /> | **Descripción y favs** <br> Cada producto tiene un ID. Se hace uso del susodicho ID para obtener los detalles (descripción) a través de una segunda consulta a la API de MercadoLibre.<br> También los productos pueden agregarse a favoritos, esto se logra a través de Firestore, dentro se guarda una colección de favoritos, compuesta por campos Favorite, donde dentro tiene: userID (email del usuario previamente obtenido en la autenticación) y productID (id obtenido del catálogo de productos) |
<img src="https://i.imgur.com/3iJlviA.gif" alt="Room Screen" width="350" /> | **Uso de room** <br> Se utilizó Room para guardar de forma local (algo así como un caché) los elementos que fueron agregados a favoritos. Room se sincroniza con Firestore al momento de iniciar la pantalla principal de la aplicación e intercepta a cada enlace que hace el ViewHolder del RecyclerView principal para ver si dicho elemento se encuentra almacenado localmente y de ser así, se le asigna un icono u otro (star_fill para cuando se encuentra que la ID está almacenada localmente y star para cuando no es así), lo que permite explicitar en un primer pantallazo si un elemento se encuentra o no en la lista de favoritos.  |

Para sincronizar la información se hace uso de hilos de ejecución paralelos que eventualmente darán consistencia. También para lograr eso se hizo uso del Activity Lifecycle y sus respectivos callbacks. 

<hr>

<p align="right">**Lautaro Zarate** <br>
Desarrollo de Aplicaciones, <br>
Facultad de Ingeniería y Ciencias Exactas,<br>
UADE.</p>



