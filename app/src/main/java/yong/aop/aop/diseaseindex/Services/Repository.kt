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

    suspend fun ggetLatestAirQualityData(dissCd : Int, znCd: Int): DisInfo? =
        AIR_KOREA_API_SERVICE
            .ggetRealtimeAirQualties(dissCd,znCd)
            .body()
            ?.response
            ?.body
            ?.disInfos
            ?.firstOrNull()

    private val AIR_KOREA_API_SERVICE: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://apis.data.go.kr/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(buildHttpClient())
            .build()
            .create()
    }

    suspend fun getLatestAirQualityData(stationName: String): AirInfo? =
        API_SERVICE
            .getRealtimeAirQualties(stationName)
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
