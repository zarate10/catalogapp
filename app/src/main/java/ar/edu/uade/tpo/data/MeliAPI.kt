package ar.edu.uade.tpo.data

import ar.edu.uade.tpo.model.ResponseDescription
import ar.edu.uade.tpo.model.ResponseProductDetail
import ar.edu.uade.tpo.model.ResponseProducts
import ar.edu.uade.tpo.model.Token
import ar.edu.uade.tpo.model.TokenRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MeliAPI {

    @GET("search")
    suspend fun getProductsByName(
        @Query("q") name: String,
        @Query("sort") sort: String? = null
    ): ResponseProducts

    @GET("items/{PRODUCT_ID}/description")
    suspend fun getProductDescription(
        @Path("PRODUCT_ID") productID: String
    ): ResponseDescription

    @GET("items/{PRODUCT_ID}")
    suspend fun getProductDetail(
        @Path("PRODUCT_ID") productID: String
    ): ResponseProductDetail

    @POST("oauth/token")
    suspend fun refreshToken(@Body requestBody: TokenRequest): Token
}