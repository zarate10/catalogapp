package ar.edu.uade.tpo.ui.favs

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.edu.uade.tpo.data.FavoriteRepository
import ar.edu.uade.tpo.data.MeliRepository
import ar.edu.uade.tpo.model.Favorite
import ar.edu.uade.tpo.model.ResponseProductDetail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext

class FavsViewModel: ViewModel() {

    private lateinit var context: Context

    private var meliRepo: MeliRepository = MeliRepository()
    private var favsRepository: FavoriteRepository = FavoriteRepository()
    private var user: String = ""

    var favsList: MutableLiveData<List<String>> = MutableLiveData()
    val productDetail = MutableLiveData<ResponseProductDetail>()

    val coroutineCtx = newSingleThreadContext("scopeGetFavs")
    val scope = CoroutineScope(coroutineCtx)

    val coroutineProductDetailCtx = newSingleThreadContext("scopeProductDetail")
    val scopeProductDetail = CoroutineScope(coroutineProductDetailCtx)

    private fun getFavsUser() {
        scope.launch {
            try {
                val listaProductsID = favsRepository.getUserFavsRemote(user)
                favsList.postValue(listaProductsID)
                Log.i("LOG-TP0", "(FavsViewModel) Lista de favoritos obtenida correctamente: " + listaProductsID.toString())
            } catch (e: Exception) {
                Log.i("LOG-TP0", "(FavsViewModel) Ocurrió un error: " + e)
            }
        }
    }

    suspend fun fetchProductDetail(productId: String): ResponseProductDetail? {
        return withContext(Dispatchers.IO) {
            try {
                meliRepo.getProductDetail(productId)
            } catch (e: Exception) {
                Log.i("LOG-TP0", "(FavsViewModel) Ocurrió un error: " + e)
                null
            }
        }
    }

    fun removeFav(productId: String) {
        scopeProductDetail.launch {
            try {
                favsRepository.saveOrRemoveProductID(context, Favorite(user, productId))
                getFavsUser()
                Log.i("LOG-TP0", "(FavsViewModel) Producto $productId eliminado correctamente de favs.")

            } catch (e: Exception) {
                Log.e("LOG-TP0", "(FavsViewModel) Ocurrió un error al eliminar un fav: $e")
            }
        }
    }

    fun init(context: Context, user: String) {
        this.user = user
        this.context = context

        getFavsUser()
    }

}