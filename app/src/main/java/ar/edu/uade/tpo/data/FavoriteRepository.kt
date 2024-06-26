package ar.edu.uade.tpo.data

import android.content.Context
import ar.edu.uade.tpo.model.Favorite

class FavoriteRepository {

    suspend fun saveOrRemoveProductID(context: Context, favorite: Favorite) {
        FavoriteDataSource().saveOrRemoveProductID(favorite)
        addOrRemoveFavLocal(context, favorite.productID, favorite.userID)
    }

    suspend fun getUserFavsRemote(userID: String): List<String> {
        return FavoriteDataSource().getUserFavsRemote(userID)
    }

    suspend fun syncFavorites(userID: String, context: Context) {
        FavoriteDataSource().syncFavorites(userID, context)
    }

    suspend fun getLocalFavs(context: Context): List<String> {
        return FavoriteDataSource().getLocalFavs(context)
    }

    suspend fun addOrRemoveFavLocal(context: Context, productID: String, userID: String) {
        FavoriteDataSource().addOrRemoveFavLocal(context, productID, userID)
    }
}