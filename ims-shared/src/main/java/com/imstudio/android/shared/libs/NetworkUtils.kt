package com.imstudio.android.shared.libs

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.lifecycle.LiveData
import com.imstudio.android.shared.common.extensions.applySingleIoScheduler
import com.imstudio.android.shared.common.extensions.observableFromCallable
import com.imstudio.android.shared.common.extensions.singleFromCallable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import timber.log.Timber
import java.net.*

object NetworkUtils : LiveData<Boolean>() {

    private var connectivityManager: ConnectivityManager? = null
    private lateinit var networkRequest: NetworkRequest

    // Default UDP socket timeout, 3 seconds is much long enough these day.
    private const val UDP_SO_TIMEOUT = 3000

    // Default Http timeout after connected, we set it for 1.5 Seconds
    private const val HTTP_TIMEOUT = 1500

    @JvmStatic
    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    fun initializeWithDefaults(application: Application) {
        connectivityManager =
            (application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
        networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()
    }

    @JvmStatic
    fun isNetworkAvailable(): Boolean {
        throwIfNotInitializedYet()
        val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    @JvmStatic // Ping a Server
    fun assumptionThatNetworkAccessNow(): Boolean {
        val runtime = Runtime.getRuntime()
        try {
            val ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8")
            val exitValue = ipProcess.waitFor()
            return exitValue == 0
        } catch (e: Exception) {
            Timber.d(e)
        }
        return false
    }

    @JvmStatic // Connect to a Socket on the Internet
    fun assumptionThatNetworkAccess(): Single<Boolean> {
        return singleFromCallable {
            try { // Connect to Google DNS to check for connection
                val timeoutMs = 2000
                val socket = Socket()
                val socketAddress = InetSocketAddress("8.8.8.8", 53)
                socket.connect(socketAddress, timeoutMs)
                socket.close()
                true
            } catch (e: Exception) {
                false
            }
        }.compose(applySingleIoScheduler())
    }

    override fun onActive() {
        super.onActive()
        throwIfNotInitializedYet()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager!!.registerDefaultNetworkCallback(networkCallback)
        } else {
            connectivityManager!!.registerNetworkCallback(networkRequest, networkCallback)
        }
    }

    override fun onInactive() {
        super.onInactive()
        throwIfNotInitializedYet()
        connectivityManager!!.unregisterNetworkCallback(networkCallback)
    }

    private fun throwIfNotInitializedYet() {
        if (connectivityManager == null)
            throw RuntimeException("NotInitializedYet")
    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            postValue(true) // call when network is connected, so set the value true
        }

        override fun onUnavailable() {
            postValue(false) // call when network is unavailable, so set the value false
        }

        override fun onLost(network: Network) {
            postValue(false) // call when network is lost, so set the value false
        }
    }

    /*
    * This is the worker function, it describe element and process to connect
    * the UDP socket in low-level
    */
    @JvmStatic
    private fun checkUdpWorker(hostServer: String, portNumber: Int): Boolean {
        val socket = DatagramSocket()
        try {
            val requestBytes = ByteArray(128)
            val receiveBytes = ByteArray(1024)

            val requestPacket = DatagramPacket(requestBytes, requestBytes.size)
            val receivePacket = DatagramPacket(receiveBytes, receiveBytes.size)
            val socketAddress = InetSocketAddress(hostServer, portNumber)

            //Set socket timeout for UDP protocol is mandate
            //Because UDP is connection less oriented protocol
            socket.soTimeout = UDP_SO_TIMEOUT
            socket.connect(socketAddress)
            socket.send(requestPacket)
            socket.receive(receivePacket)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        } finally {
            //Close the socket if it was not closed yet
            if (socket.isConnected and !socket.isClosed)
                socket.close()
        }
    }


    /*
    * Wrapper function of above "checkUdpWorker" function
    * This function crate Result to be Observable from Callable for simplicity
    */
    fun checkUdp(hostServer: String, portNumber: Int): Observable<Boolean> {
        //Create instance of Callable conventionally using
        //instance of Callable, Kotlin fashion means instance of class that implement Callable interface
        return observableFromCallable { checkUdpWorker(hostServer, portNumber) }
    }


    /*
    * Worker function that do Http request in low-level
    */
    private fun checkHttpWorker(hostServer: String): Boolean {
        var urlc: HttpURLConnection? = null
        try {
            urlc = URL(hostServer).openConnection() as HttpURLConnection
            urlc.setRequestProperty(
                "User-Agent",
                "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.92 Safari/537.36"
            )
            urlc.setRequestProperty("Connection", "close")
            urlc.connectTimeout = HTTP_TIMEOUT
            urlc.connect()
            val responseCode = urlc.responseCode
            return if (responseCode == 200) {
                true
            } else {
                //Avoid Leaked, even we've set Response Timeout
                urlc.disconnect()
                false
            }

        } catch (ex: Throwable) {
            return false
        } finally {
            urlc?.disconnect()
        }

    }


    /*
    * Wrapper function of above 'checkHttpWorkerKt' function
    * For simplicity, it will create Observable from Callable of Above function
    */
    fun checkHttp(hostServer: String): Observable<Boolean> {
        //in Kotlin we can create class instance implementing Callable interface easily
        return Observable.fromCallable { checkHttpWorker(hostServer) }
    }


}
