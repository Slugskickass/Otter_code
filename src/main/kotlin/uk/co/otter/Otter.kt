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

                    val commands = listOf(
                        Command.Write(0, Parameter.Period, Value.Int(2)),
                        Command.Write(0, Parameter.Start, Value.Int(0)),
                        Command.Write(0, Parameter.End, Value.Int(0)),

                        Command.Write(1, Parameter.Period, Value.Int(2)),
                        Command.Write(1, Parameter.Start, Value.Int(1)),
                        Command.Write(1, Parameter.End, Value.Int(1)),

                        Command.Write(2, Parameter.Period, Value.Int(2)),
                        Command.Write(2, Parameter.Start, Value.Int(2)),
                        Command.Write(2, Parameter.End, Value.Int(2))
                    ) + (3..11).map { Command.Write(it.toByte(), Parameter.Config, Value.Config(CountSrc.Exposure1, 1, AndSrc.Disable, false)) }

                    commands.map { ott.send(it) }

                    ott.send(Command.Start)
                    Thread.sleep(5000)
                    ott.send(Command.Stop)
                }
            } catch (e : OtterException) {
                e.printStackTrace()
            }

        }
    }
}