package ru.nightgoat.weather.core.utils

const val BASE_URL: String = "https://api.openweathermap.org/"
const val FONTS_PATH = "fonts/weathericons.ttf"

//KEYS
const val METRIC = "metric"
const val IMPERIAL = "imperial"
const val CITY_ID_KEY = "cityId"
const val SETTINGS_KEY = "settings"
const val TEMP_KEY = "temp"
const val TIME_KEY = "time"
const val ICON_KEY = "icon"
const val FIRST_TIME_OPEN_KEY = "first"
const val NOT_FOUND_KEY = "nf"
const val API_KEY = "api_key"
const val PRESSURE_KEY = "pressure"
const val DEGREE_KEY = "degree"
const val NAME_KEY = "name"
const val CITY_NAME_KEY = "cityName"

//WEATHER CODES
val THUNDERSTORM_WITH_LIGHT_RAIN_INTERVAL = 200..201
const val THUNDERSTORM_WITH_RAIN = 202
val THUNDERSTORM_INTERVAL = 202..232
val DRIZZLE_INTERVAL = 300..321
val RAIN_INTERVAL = 500..511
val SHOWER_RAIN_INTERVAL = 520..531
val SNOW_INTERVAL = 600..622
const val FOG = 701
const val MIST = 721
const val HAZE = 741
const val SMOKE = 711
const val SAND_1 = 731
const val SAND_2 = 751
const val DUST = 761
const val VOLCANIC_ASH = 762
const val SQUALL = 771
const val TORNADO = 781
const val CLEAR_SKY = 800
const val CLOUDY_SKY = 801
val CLOUDS_INTERVAL = 802..804

//DATE
const val DATE_PATTERN_EEEE_D_MMM = "EEEE, d MMM"
const val DATE_PATTERN_EEEE = "EEEE"

//OTHER
const val pressureFromHPaToMmHgDiv = 1.33322387415
const val PERCENT = "%"
const val DEFAULT_CITY_ID = 551487