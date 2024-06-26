package ar.edu.uade.tpo.data

import ar.edu.uade.tpo.model.ResponseDescription
import ar.edu.uade.tpo.model.ResponseProductDetail
import ar.edu.uade.tpo.model.ResponseProducts
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MeliDataSource {
    companion object {

        private val API_BASE_URL = "https://api.mercadolibre.com/"
        private val TOKEN = "TOKEN_PRIVADO"

        suspend fun getProductsByName(name: String, sort: String?): ResponseProducts {
            val api = Retrofit.Builder()
                .baseUrl(API_BASE_URL + "/sites/MLA/")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(MeliAPI::class.java)

            return api.getProductsByName(name, sort)
        }

        suspend fun getDescriptionByID(productID: String): ResponseDescription {
            val api = Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(
                    OkHttpClient.Builder().apply {
                        addInterceptor { chain ->
                            val original = chain.request()
                            val requestBuilder = original.newBuilder()
                                .header("Authorization", "Bearer " + TOKEN)
                            val request = requestBuilder.build()
                            chain.proceed(request)
                        }
                    }.build()
                ).build().create(MeliAPI::class.java)

            return api.getProductDescription(productID)
        }

        suspend fun getProductDetail(productID: String): ResponseProductDetail {
            val api = Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(
                    OkHttpClient.Builder().apply {
                        addInterceptor { chain ->
                            val original = chain.request()
                            val requestBuilder = original.newBuilder()
                                .header("Authorization", "Bearer " + TOKEN)
                            val request = requestBuilder.build()
                            chain.proceed(request)
                        }
                    }.build()
                ).build().create(MeliAPI::class.java)

            return api.getProductDetail(productID)
        }
    }
}
