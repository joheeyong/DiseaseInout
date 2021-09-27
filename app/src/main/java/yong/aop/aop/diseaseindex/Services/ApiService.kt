package yong.aop.aop.diseaseindex.Services

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import yong.aop.aop.diseaseindex.BuildConfig
import yong.aop.aop.diseaseindex.Jsondis.DiseaseResponse
import yong.aop.aop.diseaseindex.Jsonair.AirQualityResponse

interface ApiService {
    @GET("B550928/dissForecastInfoSvc/getDissForecastInfo"+
            "?serviceKey=0tL1OHr4bk07quY%2FkjF0ukr4dLEk34D3A3%2BxVdJDsBUPuWcUiM2M50lTmuamFba%2FST74ZJlNn%2BG43Cygv2FXHg%3D%3D" +
            "&numOfRows=1" +
            "&pageNo=1"+
            "&type=json")
    suspend fun getDisease(
        @Query("dissCd") dissCd: Int,
        @Query("znCd") znCd: Int,
    ): Response<DiseaseResponse>

    @GET("B552584/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty" +
            "?serviceKey=${BuildConfig.DATA_GO_KEY}" +
            "&returnType=json" +
            "&dataTerm=DAILY" +
            "&ver=1.3")
    suspend fun getAir(
        @Query("stationName") stationName: String
    ): Response<AirQualityResponse>
}
