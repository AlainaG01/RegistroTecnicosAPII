package edu.ucne.registrotecnicos.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.ucne.registrotecnicos.data.remote.UsuarioingApi
import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import javax.inject.Singleton
import okhttp3.MediaType.Companion.toMediaType
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

@InstallIn(SingletonComponent::class)
@Module
object ApiModule {
    const val BASE_URL = "http://www.AlainaApi.somee.com"

    /*
    // Moshi
    @Provides
    @Singleton
    fun providesMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(DateAdapter())
            .build()
    */

    // Json parser para kotlinx.serialization
    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = false // Cambiado a false para omitir valores por defecto
        prettyPrint = true
    }

    @Provides
    @Singleton
    fun providesUsuariosApi(json: Json): UsuarioingApi {
        val contentType = "application/json".toMediaType()

        // (opcional) Logging interceptor para ver el JSON que se envía
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType)) // Usando kotlinx.serialization
            .build()
            .create(UsuarioingApi::class.java)
    }
}