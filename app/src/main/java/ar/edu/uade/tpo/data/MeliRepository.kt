package ar.edu.uade.tpo.data

import ar.edu.uade.tpo.model.ResponseDescription
import ar.edu.uade.tpo.model.ResponseProductDetail
import ar.edu.uade.tpo.model.ResponseProducts
import ar.edu.uade.tpo.model.Token


class MeliRepository {
    suspend fun getProductsByName(name: String, sort: String?): ResponseProducts {
        return MeliDataSource.getProductsByName(name, sort)
    }

    suspend fun getDescriptionByID(productID: String): ResponseDescription{
        return MeliDataSource.getDescriptionByID(productID)
    }

    suspend fun getProductDetail(productID: String): ResponseProductDetail {
        return MeliDataSource.getProductDetail(productID)
    }

    suspend fun refreshToken(): Token {
        return MeliDataSource.refreshToken()
    }


    fun updateToken(newToken: String) {
        return MeliDataSource.updateToken(newToken)
    }
}