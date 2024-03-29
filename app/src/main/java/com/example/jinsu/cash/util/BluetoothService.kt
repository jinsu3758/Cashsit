package com.example.jinsu.cash.util

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import com.example.jinsu.cash.activity.MainActivity
import com.example.jinsu.cash.common.Constant
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

class BluetoothService private constructor()  {
    private val adapter: BluetoothAdapter?
    private var handler: Handler? = null
    private var callback: BluetoothCallback? = null
    private object Holder { val INSTANCE = BluetoothService() }

    private val STATE_NONE = 0// we're doing nothing
    private val STATE_LISTEN = 1// now listening for incoming connections
    private val STATE_CONNECTING = 2// now initiating an outgoing connection
    private val STATE_CONNECTED = 3// now connected to a remote device
    public val REQUEST_ENABLE_BT = 2
    public val REQUEST_CONNECT_DEVICE = 3
    public val MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    //     Bluetooth 상태 get
    // Bluetooth 상태 set

//    @get:Synchronized
    var state: Int = 0
        private set

    private var mConnectThread: ConnectThread? = null
    private var mConnectedThread: ConnectedThread? = null

    val deviceState: Boolean
        get() = if (adapter == null) {
            false
        } else {
            true
        }

    init {
        Log.d("blue_","init")
        adapter = BluetoothAdapter.getDefaultAdapter()

    }


    fun setHandler(handler: Handler) {
        this.handler = handler
    }

    fun setHandler(handler: MainActivity.MyHandler) {
        this.handler = handler
    }

    fun enableBluetooth() {
        //블루투스 상태가 on인 경우
        if (adapter!!.isEnabled) {
            scanDevice()
        } else {
            callback!!.startIntent(REQUEST_ENABLE_BT)
        }//블루투스 상태가 off인 경우
    }

    //블루투스 디바이스 찾기
    fun scanDevice() {
        callback!!.startIntent(REQUEST_CONNECT_DEVICE)
    }

    fun setCallback(callback: BluetoothCallback) {
        this.callback = callback
    }

    fun getDeviceInfo(data: Intent?) {
        if (data != null) {
            val address = data.extras!!.getString("device_address")
            val device = adapter!!.getRemoteDevice(address)
            connect(device)
        }
    }

    @Synchronized
    fun start() {
        //     Cancel any thread attempting to make a connection
        if (mConnectThread == null) {
        } else {
            mConnectThread!!.cancel()
            mConnectThread = null
        }
        //     Cancel any thread currently running a connection

        if (mConnectedThread == null) {
        } else {
            mConnectedThread!!.cancel()
            mConnectedThread = null
        }
    }

    //    ConnectThread 초기화 device의 모든 연결 제거
    @Synchronized
    fun connect(device: BluetoothDevice) {
        //        Cancel any thread attempting to make a connection
        if (state == STATE_CONNECTING) {
            if (mConnectThread == null) {
            } else {
                mConnectThread!!.cancel()
                mConnectThread = null
            }
        }

        //        Cancel any thread currently running a connection
        if (mConnectedThread == null) {
        } else {
            mConnectedThread!!.cancel()
            mConnectedThread = null
        }
        //        Start the thread to connect with the given device

        mConnectThread = ConnectThread(device)
        mConnectThread!!.start()
        state = STATE_CONNECTING
    }

    //    ConnectedThread 초기화
    @Synchronized
    fun connected(socket: BluetoothSocket, device: BluetoothDevice) {
        //        Cancel the thread that completed the connection
        if (mConnectThread == null) {
        } else {
            mConnectThread!!.cancel()
            mConnectThread = null
        }

        //        Cancel any thread currently running a connection
        if (mConnectedThread == null) {
        } else {
            mConnectedThread!!.cancel()
            mConnectedThread = null
        }

        //        Start the thread to manage the connection and perform transmissions
        mConnectedThread = ConnectedThread(socket)
        mConnectedThread!!.start()
        state = STATE_CONNECTED
    }

    //    모든 thread stop
    @Synchronized
    fun stop() {
        if (mConnectThread != null) {
            mConnectThread!!.cancel()
            mConnectThread = null
        }

        if (mConnectedThread != null) {
            mConnectedThread!!.cancel()
            mConnectedThread = null
        }
        state = STATE_NONE
    }

    //    값을 쓰는 부분(보내는 부분)
    fun write(out: ByteArray) {
        //        Create temporary object
        var r: ConnectedThread?
        //        Synchronize a copy of the ConnectedThread
        synchronized(this) {
            if (state != STATE_CONNECTED)
                return

            r = mConnectedThread
            r!!.write(out)
        }

        //        Perform the write unsynchronized

    }


    //    연결 실패했을때
    private fun connectionFailed() {
        state = STATE_LISTEN
    }

    //    연결을 잃었을 때
    private fun connectionLost() {
        state = STATE_LISTEN
    }


    private inner class ConnectThread(private val mmDevice: BluetoothDevice) : Thread() {
        private val mmSocket: BluetoothSocket?

        init {
            var tmp: BluetoothSocket? = null
            // 디바이스 정보를 얻어서 BluetoothSocket 생성
            try {
                tmp = mmDevice.createRfcommSocketToServiceRecord(MY_UUID)
            } catch (e: IOException) {

            }

            mmSocket = tmp
        }

        override fun run() {
            name = "ConnectThread"
            // 연결을 시도하기 전에는 항상 기기 검색을 중지한다.
            // 기기 검색이 계속되면 연결속도가 느려지기 때문이다.
            adapter!!.cancelDiscovery()
            // BluetoothSocket 연결 시도
            try {
                // BluetoothSocket 연결 시도에 대한 return 값은 succes 또는 exception이다.
                mmSocket!!.connect()
            } catch (e: IOException) {
                connectionFailed()
                // 연결 실패시 불러오는 메소드
                Log.d("BluetoohService", "Connect Fail")
                // socket을 닫는다.
                try {
                    mmSocket!!.close()
                } catch (e2: IOException) {
                }

                // 연결중? 혹은 연결 대기상태인 메소드를 호출한다.
                this@BluetoothService.start()
                return
            }

            // ConnectThread 클래스를 reset한다.
            synchronized(this@BluetoothService) {
                mConnectThread = null
            }
            // ConnectThread를 시작한다.
            connected(mmSocket, mmDevice)
        }

        fun cancel() {
            try {
                mmSocket!!.close()
            } catch (e: IOException) {
            }

        }
    }

    private inner class ConnectedThread(private val mmSocket: BluetoothSocket) : Thread() {
        private val mmInStream: InputStream?
        private val mmOutStream: OutputStream?
        var pointcnt = 0
        init {
            var tmpIn: InputStream? = null
            var tmpOut: OutputStream? = null
            // BluetoothSocket의 inputstream 과 outputstream을 얻는다.
            try {
                tmpIn = mmSocket.inputStream
                tmpOut = mmSocket.outputStream
            } catch (e: IOException) {
            }

            mmInStream = tmpIn
            mmOutStream = tmpOut
        }

        override fun run() {
            val buffer = ByteArray(1024)
            var bytes: Int
            // Keep listening to the InputStream while connected
            while (true) {
                try {
                    Log.d("BlUETOOTH", "값 읽음")
                    // InputStream으로부터 값을 받는 읽는 부분(값을 받는다)
                    bytes = mmInStream!!.read(buffer)
                    Log.d("BlUETOOTH", "값 : " + String(buffer).trimMargin())
                    if(String(buffer)[0] == 'd'){
                        Log.d("Message","메세지 보냄")
                        var m : Message = Message()
                        var bd : Bundle =  Bundle()
                        m.what = 9
                        m.data = bd
                        Log.d("Message",m.toString())
                        if(handler!=null)
                            handler!!.sendMessage(m)
                    }
                    else if(String(buffer)[0] == 'a'){
                        pointcnt++
                        if(pointcnt == 3) {
                            Constant.money += 3
                            pointcnt=0
                        }
                        Constant.total_time+=3
                        Constant.Right_time+=3
                        Constant.Posture = 1
                        Log.d("Message","메세지 보냄")
                        var m : Message = Message()
                        var bd : Bundle =  Bundle()
                        m.what = Constant.Posture
                        m.data = bd
                        Log.d("Message",m.toString())
                        if(handler!=null)
                            handler!!.sendMessage(m)

                    }
                    else if(String(buffer)[0] == 'f'){
                        Constant.total_time+=3
                        Constant.bad_time+=3
                        Constant.Posture = 3
                        Log.d("Message","메세지 보냄")
                        var m : Message = Message()
                        var bd : Bundle =  Bundle()
                        m.what = Constant.Posture  //앞으로 숙인 자세
                        m.data = bd
                        Log.d("Message",m.toString())
                        if(handler!=null)
                            handler!!.sendMessage(m)
                    }
                    else if(String(buffer)[0] == 'b'){
                        Constant.total_time+=3
                        Constant.bad_time+=3
                        Constant.Posture = 2
                        Log.d("Message","메세지 보냄")
                        var m : Message = Message()
                        var bd : Bundle =  Bundle()
                        m.what = Constant.Posture
                        m.data = bd
                        Log.d("Message",m.toString())
                        if(handler!=null)
                            handler!!.sendMessage(m)
                    }
                    else if(String(buffer)[0] == 'r'){
                        Constant.total_time+=3
                        Constant.bad_time+=3
                        Constant.Posture = 5
                        Log.d("Message","메세지 보냄")
                        var m : Message = Message()
                        var bd : Bundle =  Bundle()
                        m.what = Constant.Posture
                        m.data = bd
                        Log.d("Message",m.toString())
                        if(handler!=null)
                            handler!!.sendMessage(m)
                    }
                    else if(String(buffer)[0] == 'l'){
                        Constant.total_time+=3
                        Constant.bad_time+=3
                        Constant.Posture = 4
                        Log.d("Message","메세지 보냄")
                        var m : Message = Message()
                        var bd : Bundle =  Bundle()
                        m.what = Constant.Posture
                        m.data = bd
                        Log.d("Message",m.toString())
                        if(handler!=null)
                            handler!!.sendMessage(m)
                    }
                    Log.d("자세", ""+Constant.Posture)
                    Log.d("바른자세", Constant.Right_time.toString())
                } catch (e: IOException) {
                    break
                }

            }
        }

        /** * Write to the connected OutStream. * @param buffer The bytes to write  */
        fun write(buffer: ByteArray) {
            try {
                // 값을 쓰는 부분(값을 보낸다)
                mmOutStream!!.write(buffer)
            } catch (e: IOException) {
            }

        }

        fun cancel() {
            try {
                mmSocket.close()
            } catch (e: IOException) {
            }

        }
    }
    companion object {
        val get: BluetoothService by lazy { Holder.INSTANCE }

    }


}
