package ar.edu.uade.tpo.data.dbLocal

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDAO {
    @Query("SELECT productID FROM favorites")
    suspend fun getAll(): List<String>

    @Query("SELECT * FROM favorites WHERE productID = :productID LIMIT 1")
    suspend fun getByPK(productID: String): FavoriteLocal

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavoriteLocal)

    @Delete
    suspend fun delete(favorite: FavoriteLocal)
}