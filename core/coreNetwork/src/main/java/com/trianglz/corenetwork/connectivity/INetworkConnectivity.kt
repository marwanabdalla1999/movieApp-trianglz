package com.trianglz.corenetwork.connectivity

/**
 * Interface for checking network connectivity status
 * Following dependency inversion principle to abstract network checking logic
 */
interface INetworkConnectivity {

    /**
     * Checks if the device is currently connected to the internet
     * @return true if connected, false otherwise
     */
    fun isNetworkAvailable(): Boolean

    /**
     * Checks if the device has WiFi connectivity
     * @return true if WiFi is connected, false otherwise
     */
    fun isWifiConnected(): Boolean

    /**
     * Checks if the device has mobile data connectivity
     * @return true if mobile data is connected, false otherwise
     */
    fun isMobileDataConnected(): Boolean
} 