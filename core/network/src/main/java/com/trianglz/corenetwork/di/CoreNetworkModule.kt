package com.trianglz.corenetwork.di

import com.trianglz.corenetwork.connectivity.INetworkConnectivity
import com.trianglz.corenetwork.interceptor.NetworkConnectivityInterceptor
import com.trianglz.corenetwork.NetworkHelper
import com.trianglz.corenetwork.connectivity.NetworkConnectivity
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {
    @Binds
    abstract fun bindNetworkConnectivityChecker(
        networkConnectivity: NetworkConnectivity
    ): INetworkConnectivity

    @Binds
    abstract fun bindNetworkHelper(
        networkHelper: NetworkHelper
    ): NetworkHelper


    @Binds
    abstract fun bindNetworkInterceptor(
        networkInterceptor: NetworkConnectivityInterceptor
    ): NetworkConnectivityInterceptor
}
