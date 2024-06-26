package ar.edu.uade.tpo.data

import android.content.Context
import android.util.Log
import ar.edu.uade.tpo.data.dbLocal.AppDatabase
import ar.edu.uade.tpo.data.dbLocal.FavoriteLocal
import ar.edu.uade.tpo.model.Favorite
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FavoriteDataSource {
    private val db = FirebaseFirestore.getInstance()
    private val COLLECTION = "favorites"

    private suspend fun checkIfProductIsFavorite(userID: String, productID: String): QuerySnapshot? {
        return try {
            db.collection(COLLECTION)
                .whereEqualTo("userID", userID)
                .whereEqualTo("productID", productID)
                .get()
                .await()
        } catch (e: Exception) {
            Log.w("TPO", "(FavoriteDataSource) Error verificando producto favorito", e)
            null
        }
    }

    suspend fun saveOrRemoveProductID(favorite: Favorite) {
        val querySnapshot = checkIfProductIsFavorite(favorite.userID, favorite.productID)
        if (querySnapshot != null && !querySnapshot.isEmpty) {
            // Eliminar el producto de favoritos
            try {
                for (document in querySnapshot.documents) {
                    document.reference.delete().await()
                    Log.d("TPO", "(FavoriteDataSource) Documento eliminado con ID: ${document.id}")
                }
            } catch (e: Exception) {
                Log.w("TPO", "(FavoriteDataSource) Error eliminando documento", e)
            }
        } else {
            // Agregar el producto a favoritos
            try {
                db.collection(COLLECTION)
                    .add(favorite)
                    .await()
                Log.d("TPO", "(FavoriteDataSource) Documento agregado")
            } catch (e: Exception) {
                Log.w("TPO", "(FavoriteDataSource) Error agregando documento", e)
            }
        }
    }

    suspend fun getUserFavsRemote(userID: String): List<String> {
        return try {
            val result = db.collection(COLLECTION)
                .whereEqualTo("userID", userID)
                .get()
                .await()
            result.documents.map { it.getString("productID") ?: "" }
        } catch (e: Exception) {
            Log.w("TPO", "(FavoriteDataSource) Error obteniendo favoritos", e)
            emptyList()
        }
    }

    suspend fun syncFavorites(userID: String, context: Context) {
        /* sincronizamos los favoritos remotos con los favs locales */
        withContext(Dispatchers.IO) {
            val db = AppDatabase.getInstance(context)
            val favoritesDao = db.favoritesDao()

            val remoteFavorites = getUserFavsRemote(userID)

            remoteFavorites.forEach { productID ->
                favoritesDao.insert(FavoriteLocal(productID, userID))
            }
        }
    }


    suspend fun getLocalFavs(context: Context): List<String> {
        val db = AppDatabase.getInstance(context)
        val favoritesDao = db.favoritesDao()

        val localFavorites = favoritesDao.getAll()

        return localFavorites
    }

    suspend fun addOrRemoveFavLocal(context: Context, productID: String, userID: String) {
        val db = AppDatabase.getInstance(context)
        val favoritesDao = db.favoritesDao()

        val localFavorites = favoritesDao.getAll()

        if (productID in localFavorites) {
            favoritesDao.delete((FavoriteLocal(productID, userID)))
        } else {
            favoritesDao.insert((FavoriteLocal(productID, userID)))
        }
    }
}