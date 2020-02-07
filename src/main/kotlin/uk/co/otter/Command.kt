package uk.co.otter

public enum class Parameter {
    Period,
    Start,
    End,
    Config
}

public enum class AndSrc {
    Exposure1,
    Exposure2,
    Exposure3,
    Exposure4,
    None
}

public enum class CountSrc {
    Exposure1,
    Exposure2,
    Exposure3,
    Exposure4,
    Internal
}

public sealed class Value {
    data class Int(val i : kotlin.Int) : Value()
    data class Config(val inp_src : CountSrc, val clk_div : Short, val and_src : AndSrc, val continuous : Boolean)
}

public sealed class Command {
    data class Write(val counter_id : Byte, val parameter : Parameter, val value : Value) : Command()
    data class Read(val counter_id : Byte, val parameter : Parameter) : Command()
    object Version : Command()
}

public sealed class Response {
    data class Value(val value : uk.co.otter.Value) : Response()
    data class VersionNumber(val major : Byte, val minor : Byte, val patch : Byte) : Response()
}