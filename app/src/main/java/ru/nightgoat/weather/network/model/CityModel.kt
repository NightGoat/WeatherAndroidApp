package ru.nightgoat.weather.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.github.nightgoat.kexcore.orZero
import ru.nightgoat.weather.core.utils.toIntOrZero
import ru.nightgoat.weather.data.entity.CityEntity

data class CityModel(

    @SerializedName("weather")
    @Expose
    val weather: List<Weather>?,

    @SerializedName("main")
    @Expose
    val main: Main?,

    @SerializedName("wind")
    @Expose
    val wind: Wind?,

    @SerializedName("dt")
    @Expose
    val dt: Long?,

    @SerializedName("sys")
    @Expose
    val sys: Sys?,

    @SerializedName("id")
    @Expose
    val id: Int?,

    @SerializedName("name")
    @Expose
    val name: String?,

    @SerializedName("list")
    @Expose
    val list: List<TimeGap>?,

    @SerializedName("city")
    @Expose
    val city: City?,

    var position: Int
) {
    fun convertToCityEntity(): CityEntity {
        return CityEntity(
            cityId = id.orZero(),
            position = position,
            date = dt.orZero() * DATE_DIV,
            name = name.orEmpty(),
            country = sys?.country.orEmpty(),
            temp = main?.temp.toIntOrZero(),
            feelsTemp = main?.feelsLike.toIntOrZero(),
            maxTemp = main?.tempMax.toIntOrZero(),
            minTemp = main?.tempMin.toIntOrZero(),
            humidity = main?.humidity.orZero(),
            pressure = main?.pressure.orZero(),
            wind = wind?.speed.toIntOrZero(),
            description = weather?.firstOrNull()?.description.orEmpty(),
            iconId = weather?.firstOrNull()?.id.orZero(),
            sunrise = sys?.sunrise.orZero() * DATE_DIV,
            sunset = sys?.sunset.orZero() * DATE_DIV
        )
    }

    companion object {
        private const val DATE_DIV = 1000
    }
}





