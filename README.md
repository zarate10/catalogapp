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
<img src="https://i.imgur.com/UMZ22l4.gif" alt="Splash Screen" width="220" /> | Splash | 


