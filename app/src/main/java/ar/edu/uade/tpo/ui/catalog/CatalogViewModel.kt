package ar.edu.uade.tpo.ui.catalog

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.uade.tpo.data.FavoriteRepository
import ar.edu.uade.tpo.data.MeliRepository
import ar.edu.uade.tpo.model.Favorite
import ar.edu.uade.tpo.model.Product
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.newSingleThreadContext

class CatalogViewModel: ViewModel() {
    /*
        Nos conectamos con el repositorio, el repositorio se comunica con
        el DataSource y el DataSource se comunica con lo que debe comunicarse.
     */
    private lateinit var context: Context
    private val productsRepo = MeliRepository()
    private val favsRepo = FavoriteRepository()

    /*
        Creamos un MutableLiveData para almacenar los productos.
        Este tipo de datos hace a la lista observable.
     */
    var productsOfSearch: MutableLiveData<ArrayList<Product>> = MutableLiveData<ArrayList<Product>>()
    var productsInFavs: MutableLiveData<List<String>> = MutableLiveData()
    var user: MutableLiveData<String> = MutableLiveData()

    var orderByMaxPrice: Boolean = true;
    /*
        Se crea una rutina para que la aplicación no se suspenda mientras se hacen
        peticiones a la red.
     */
    val coroutineCtx = newSingleThreadContext("scopeProducts")
    val scope = CoroutineScope(coroutineCtx)

    val coroutineCtxFav = newSingleThreadContext("scopeFavsCatalog")
    val scopeFavs = CoroutineScope(coroutineCtxFav)


    fun searchProductsByName(name: String) {
        if (orderByMaxPrice) {
            search(name, "price_desc")
        } else {
            search(name, "price_asc")
        }
    }

    fun addRemoveFav(productId: String) {
        scopeFavs.launch {
            try {
                favsRepo.saveOrRemoveProductID(context, Favorite(user.value.toString(), productId))
                Log.i("LOG-TP0", "(CatalogViewModel) Producto $productId eliminado correctamente de favs.")
            } catch (e: Exception) {
                Log.e("LOG-TP0", "(CatalogViewModel) Ocurrió un error al eliminar un fav: $e")
            }
        }
    }

    private fun search(name: String, sort: String? = null) {
        /*
            Se hace uso del scope de la corrutina para que no afecte al uso de la aplicación
            además de que dentro se hace un try-catch por si llegase a fallar algo.
         */
        scope.launch {
            try {
                /* le pedimos los resultados al repo */
                val result = productsRepo.getProductsByName(name, sort)
                /* guardamos eso con postValue en nuestra LiveData */
                productsOfSearch.postValue(result.results)

                Log.i("LOG-TP0", "(CatalogViewModel) " + result.results.toString())
            } catch (e: Exception) {
                /* array vacío por si ocurre un error */
                productsOfSearch.postValue(ArrayList())
                Log.e("LOG-TP0", "(CatalogViewModel) Ocurrió un error " + e)
            }
        }
    }

    fun syncFavorites(context: Context, userID: String) {
        viewModelScope.launch {
            try {
                favsRepo.syncFavorites(userID, context)
            } catch (e: Exception) {
                Log.e("TPO-L0G", "(CatalogViewModel) Error syncFavorites: $e")
            }
        }
    }

    fun getFavsCache() {
        viewModelScope.launch {
            try {
                val result = favsRepo.getLocalFavs(context)
                productsInFavs.postValue(result)
                Log.i("TPO-L0G", "(CatalogViewModel) getFavsCache: $result")
            } catch (e: Exception) {
                Log.i("TPO-L0G", "(CatalogViewModel) Error getFavsCache: $e")
                productsInFavs.postValue(listOf())
            }
        }
    }

    fun init(context: Context, user: String) {
        this.user.postValue(user)
        this.context = context

        viewModelScope.launch {
            try {
                syncFavorites(context, user)
                getFavsCache()
                search("auto", "price_desc")
            } catch (e: Exception) {
                Log.e("TPO-L0G", "(CatalogViewModel) Error init: $e")
            }
        }
    }
}