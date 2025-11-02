package com.example.flerkthsound.network

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private const val BASE_URL = "http://10.0.2.2:3000/api/"

    fun createApiService(context: Context): FlerkthSoundApiService {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Видим все запросы в логах
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FlerkthSoundApiService::class.java)
    }
}


//package com.example.flerkthsound.network
//
//import android.content.Context
//import com.google.gson.*
//import okhttp3.OkHttpClient
//import okhttp3.logging.HttpLoggingInterceptor
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import java.lang.reflect.Type
//import java.util.concurrent.TimeUnit
//
//// Адаптер для преобразования чисел в boolean
//class BooleanTypeAdapter : JsonDeserializer<Boolean> {
//    override fun deserialize(
//        json: JsonElement,
//        typeOfT: Type,
//        context: JsonDeserializationContext
//    ): Boolean {
//        return when {
//            json.isJsonPrimitive -> {
//                val primitive = json.asJsonPrimitive
//                when {
//                    primitive.isBoolean -> primitive.asBoolean
//                    primitive.isNumber -> primitive.asInt != 0
//                    primitive.isString -> primitive.asString.toBoolean()
//                    else -> false
//                }
//            }
//            else -> false
//        }
//    }
//}
//
//object RetrofitInstance {
//    private const val BASE_URL = "http://10.0.2.2:3000/api"
//
//    // Создаем кастомный Gson с адаптером для Boolean
//    private val gson = GsonBuilder()
//        .registerTypeAdapter(Boolean::class.java, BooleanTypeAdapter())
//        .registerTypeAdapter(Boolean::class.javaPrimitiveType, BooleanTypeAdapter())
//        .create()
//
//    fun createApiService(context: Context): FlerkthSoundApiService {
//        val loggingInterceptor = HttpLoggingInterceptor().apply {
//            level = HttpLoggingInterceptor.Level.BODY
//        }
//
//        val client = OkHttpClient.Builder()
//            .addInterceptor(AuthInterceptor(context))
//            .addInterceptor(loggingInterceptor)
//            .connectTimeout(30, TimeUnit.SECONDS)
//            .readTimeout(30, TimeUnit.SECONDS)
//            .writeTimeout(30, TimeUnit.SECONDS)
//            .build()
//
//        return Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .client(client)
//            .addConverterFactory(GsonConverterFactory.create(gson)) // Используем кастомный Gson
//            .build()
//            .create(FlerkthSoundApiService::class.java)
//    }
//}