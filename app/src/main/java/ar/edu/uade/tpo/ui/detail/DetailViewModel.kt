package ar.edu.uade.tpo.ui.detail

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.uade.tpo.data.FavoriteRepository
import ar.edu.uade.tpo.data.MeliRepository
import ar.edu.uade.tpo.model.Favorite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext

class DetailViewModel: ViewModel() {

    private lateinit var context: Context
    val meliRepo = MeliRepository()
    val favsRepo = FavoriteRepository()

    private lateinit var userID: String
    private lateinit var productID: String

    var title: MutableLiveData<String> = MutableLiveData()
    var condition: MutableLiveData<String> = MutableLiveData()
    var description: MutableLiveData<String> = MutableLiveData()
    var price: MutableLiveData<String> = MutableLiveData()
    var imageUrl: MutableLiveData<String> = MutableLiveData()
    var textBtnFav: MutableLiveData<String> = MutableLiveData()
    var productAddFav: Boolean = false

    val coroutineCtx = newSingleThreadContext("detailScope")
    val scope = CoroutineScope(coroutineCtx)

    val coroutineCtxFav = newSingleThreadContext("scopeFavsCatalog2")
    val scopeFavs = CoroutineScope(coroutineCtxFav)

    val coroutineCtxFav2 = newSingleThreadContext("scopeGetFavs")
    val scopeGetFavs = CoroutineScope(coroutineCtxFav2)

    fun setInfo() {
        scope.launch {
            try {
                val result = meliRepo.getDescriptionByID(productID).plain_text
                val result2 = meliRepo.getProductDetail(productID)

                title.postValue(result2.title)
                condition.postValue(result2.condition)
                price.postValue(result2.price.toInt().toString())
                description.postValue(result)
                imageUrl.postValue(result2.thumbnail)
            } catch (e: Exception) {
                Log.e("LOG-TP0", "(DetailViewModel) Ocurrió un error: ", e)
            }
        }
    }

    fun addRemoveFav() {

        changeTextBtnFav()
        scopeFavs.launch {
            try {
                favsRepo.saveOrRemoveProductID(context, Favorite(userID, productID))
                changeTextBtnFavConsistent()
                Log.i("LOG-TP0", "(DetailViewModel) Producto agregado/eliminado de favs.")
            } catch (e: Exception) {
                Log.e("LOG-TP0", "(DetailViewModel) Ocurrió un error al eliminar un fav: $e")
            }
        }
    }

    fun changeTextBtnFav() {
        productAddFav = !productAddFav
        /* esto para dar respuesta rápida a la UI */
        if (productAddFav) {
            textBtnFav.postValue("Eliminar de favoritos")
        } else {
            textBtnFav.postValue("Agregar a favoritos")
        }
    }

    fun changeTextBtnFavConsistent() {
        /* esto para que la UI sea consistente con la db */
        scopeGetFavs.launch {
            try {
                val userFavorites = favsRepo.getUserFavsRemote(userID)

                if (productID in userFavorites) {
                    textBtnFav.postValue("Eliminar de favoritos")
                    productAddFav = true
                } else {
                    textBtnFav.postValue("Agregar a favoritos")
                    productAddFav = false
                }
                Log.i("LOG-TP0", "(DetailViewModel) ARRAY: $userFavorites")
            } catch (e: Exception) {
                Log.e("LOG-TP0", "(DetailViewModel) Error: $e")
            }
        }
    }

    fun init(context: Context, productID: String, user: String) {
        this.userID = user
        this.productID = productID
        this.context = context
    }

    init {
        changeTextBtnFav()
    }
}