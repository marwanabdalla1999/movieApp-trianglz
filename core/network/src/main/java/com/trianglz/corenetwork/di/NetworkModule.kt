package com.trianglz.corenetwork.di

import android.content.Context
import com.trianglz.corenetwork.NetworkConstants
import com.trianglz.corenetwork.connectivity.INetworkConnectivity
import com.trianglz.corenetwork.interceptor.NetworkConnectivityInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.trianglz.corenetwork.BuildConfig
import com.trianglz.corenetwork.connectivity.NetworkConnectivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Provides
    @Singleton
    fun provideNetworkConnectivityChecker(
        @ApplicationContext context: Context
    ): INetworkConnectivity = NetworkConnectivity(context)

    @Provides
    @Singleton
    fun provideNetworkConnectivityInterceptor(
        checker: INetworkConnectivity
    ): NetworkConnectivityInterceptor = NetworkConnectivityInterceptor(checker)

    @Provides
    @Singleton
    fun provideOkHttpClient(
        networkInterceptor: NetworkConnectivityInterceptor
    ): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        }

        return OkHttpClient.Builder().addInterceptor(networkInterceptor).addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader(NetworkConstants.Headers.AUTHORIZATION, BuildConfig.USER_TOKEN)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json").build()
                chain.proceed(request)
            }.addInterceptor(logging).build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient, moshi: Moshi
    ): Retrofit = Retrofit.Builder().baseUrl(NetworkConstants.BASE_URL).client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi)).build()
}
