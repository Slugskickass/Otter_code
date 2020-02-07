package uk.co.otter

import java.lang.Exception

class OtterException(message : String) : Exception(message)

class Otter (port : String) : AutoCloseable {

    private var nativeRef : Long = 0

    init { connect(port) }

    external override fun close()
    protected fun finalize() { close() }
    private external fun connect(port : String)
    external fun send(command : Command) : Response?

    companion object {
        init {
            try {
                System.load(System.getProperty("user.dir") + "/" + System.mapLibraryName("otter_jni"))
            } catch (e : UnsatisfiedLinkError) {
                println(e)
            }
        }

        @JvmStatic
        external fun getPorts() : Array<String>

        @JvmStatic
        fun main(args : Array<String>) {
            try {
                getPorts().forEach(::println)

                Otter("/dev/ttyUSB1").use { ott ->
                    val response = ott.send(Command.Version)
                    if (response is Response.VersionNumber) {
                        println("Otter Version: ${response.major}.${response.minor}.${response.patch}")
                    } else {
                        println("Unexpected response to version query!")
                    }

                    ott.send(Command.Write(0, Parameter.Period, Value.Int(10)))

                    when (val response = ott.send(Command.Read(0, Parameter.Period))) {
                        is Response.Value -> println("Read ok! Response ${(response.value as Value.Int).i}")
                        else -> println("Read failed!")
                    }
                }
            } catch (e : OtterException) {
                e.printStackTrace()
            }

        }
    }
}