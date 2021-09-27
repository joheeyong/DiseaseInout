package yong.aop.aop.diseaseindex.Services

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import yong.aop.aop.diseaseindex.BuildConfig
import yong.aop.aop.diseaseindex.Jsondis.DisInfo
import yong.aop.aop.diseaseindex.Jsonair.AirInfo


object Repository {

    suspend fun getDisease(dissCd : Int, znCd: Int): DisInfo? =
        API_SERVICES
            .getDisease(dissCd,znCd)
            .body()
            ?.response
            ?.body
            ?.disInfos
            ?.firstOrNull()
    private val API_SERVICES: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://apis.data.go.kr/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(buildHttpClient())
            .build()
            .create()
    }

    suspend fun getAir(stationName: String): AirInfo? =
        API_SERVICE
            .getAir(stationName)
            .body()
            ?.response
            ?.body
            ?.airInfos
            ?.firstOrNull()
    private val API_SERVICE: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://apis.data.go.kr/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(buildHttpClient())
            .build()
            .create()
    }

    private fun buildHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if(BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            )
            .build()
}
