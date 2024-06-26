package ar.edu.uade.tpo.data.dbLocal

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteLocal(
    @PrimaryKey val productID: String,
    val userID: String
)
