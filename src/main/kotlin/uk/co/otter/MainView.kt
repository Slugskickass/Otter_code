package uk.co.otter

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.layout.BorderPane
import javafx.stage.FileChooser
//import sun.jvm.hotspot.oops.IntField
import tornadofx.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*

//class Channel_data(val Channel: Int,
//                   val ChannelPeriod: Int,
//                   val Start_time: Int,
//                   val Length_on: Int,
//                   val Input_chan: String,
//                   val Divider: Int){


val Input_Drivers = FXCollections.observableArrayList("Camera_1","Camera_2","Camera_3","Camera_4","Internal")

val Source_Drivers = FXCollections.observableArrayList("Camera_1","Camera_2","Camera_3","Camera_4","ON")

var otter : Otter? = null
val port_list = listOf(*Otter.getPorts()).asObservable()
val port = SimpleStringProperty()

@Serializable
data class SerializableChannel(val ID: Int, val name: String, val ChannelPeriod: Int, val Start_time: Int, val Length_on: Int, val Input_chan: String, val Source_add: String, val Divider: Int, val Enable: Boolean)

class Channel(ID: Int, name: String, ChannelPeriod: Int, Start_time: Int, Length_on: Int, Input_chan: String, Source_add: String, Divider: Int, Enable: Boolean) {
    val IDProperty = SimpleIntegerProperty(this, "ID", ID)
//    var name by nameProperty

    val nameProperty = SimpleStringProperty(this, "name", name)
//    var name by nameProperty

    val PeriodProperty = SimpleIntegerProperty(this, "ChannelPeriod", ChannelPeriod)
//    var ChannelPeriod by PeriodProperty

    val StartProperty = SimpleIntegerProperty(this, "Start", Start_time)
//    var Start_time by StartProperty

    val LengthProperty = SimpleIntegerProperty(this, "Length", Length_on)
//    var Length_on by LengthProperty

    val InputProperty = SimpleStringProperty(this, "Input", Input_chan)
//    var Input_chan by InputProperty

    val SourceProperty = SimpleStringProperty(this, "Source Add", Source_add)


    val DividerProperty = SimpleIntegerProperty(this, "Divider", Divider)
//    var Divider by DividerProperty

    val EnableProperty =SimpleBooleanProperty(this, "Enable", Enable)

    fun serialisable() =
        SerializableChannel(IDProperty.value,
            nameProperty.value,
            PeriodProperty.value,
            StartProperty.value,
            LengthProperty.value,
            InputProperty.value,
            SourceProperty.value,
            DividerProperty.value,
            EnableProperty.value)

    fun update(sc : SerializableChannel)
    {
        IDProperty.set(sc.ID)
        nameProperty.set(sc.name)
        PeriodProperty.set(sc.ChannelPeriod)
        StartProperty.set(sc.Start_time)
        LengthProperty.set(sc.Length_on)
        InputProperty.set(sc.Input_chan)
        SourceProperty.set(sc.Source_add)
        DividerProperty.set(sc.Divider)
        EnableProperty.set(sc.Enable)
    }
}

class ChannelEditor : View("Channel Editor") {
    override val root = BorderPane()
    val Channels = listOf(Channel(1, "Channel 1", 8, 1, 1, Input_Drivers[0], Source_Drivers[0],1,true),
        Channel(2, "Channel 2", 8, 2, 1, Input_Drivers[0], Source_Drivers[0],1,true),
        Channel(3, "Channel 3", 6, 3, 1, Input_Drivers[0], Source_Drivers[0],1,true),
        Channel(4, "Channel 4", 8, 4, 1, Input_Drivers[0], Source_Drivers[0],1,true),
        Channel(5, "Channel 5", 6, 5, 1, Input_Drivers[0], Source_Drivers[0],1,true),
        Channel(6, "Channel 6", 8, 6, 1, Input_Drivers[0], Source_Drivers[0],1,true),
        Channel(7, "Channel 7", 6, 7, 1, Input_Drivers[0], Source_Drivers[0],1,true),
        Channel(8, "Channel 8", 8, 8, 1, Input_Drivers[0], Source_Drivers[0],1,true),
        Channel(9, "Channel 9", 8, 8, 1, Input_Drivers[0], Source_Drivers[0],1,true),
        Channel(10, "Channel 10", 8, 8, 1, Input_Drivers[0], Source_Drivers[0],1,true),
        Channel(11, "Channel 11", 8, 8, 1, Input_Drivers[0], Source_Drivers[0],1,true),
        Channel(12, "Channel 12", 6, 0, 1, Input_Drivers[0], Source_Drivers[0],1,true)).asObservable()


    val model = ChannelnModel(Channels[0])


    init {
        with(root) {
            center {
                tableview(Channels) {
                    column("ID", Channel::IDProperty)
                    column("Channel Name", Channel::nameProperty)
                    column("Period", Channel::PeriodProperty)
                    column("Start", Channel::StartProperty)
                    column("Length", Channel::LengthProperty)
                    column("Input", Channel::InputProperty)
                    column("Source AND",Channel::SourceProperty)
                    column("Divider", Channel::DividerProperty)
                    column("Enable", Channel::EnableProperty).useCheckbox()
                    // Update inside the view model on selection change
                    model.rebindOnChange(this) { selectedChannel ->
                        item = selectedChannel ?: Channels[0]
                    }
                }
            }

            top {
                form {
                        fieldset("Interaction") {
                            hbox(3) {
                            button("Set three LEDs") {
                                action {
                                    println("Set LEDs")
                                    Channels[0].nameProperty.set("Blue")
                                    Channels[1].nameProperty.set("Green")
                                    Channels[2].nameProperty.set("Red")

                                    Channels[0].PeriodProperty.set(3)
                                    Channels[1].PeriodProperty.set(3)
                                    Channels[2].PeriodProperty.set(3)

                                    Channels.take(3).map { it.LengthProperty.set(1) }
                                    Channels.take(12).map { it.EnableProperty.set(false) }
                                    Channels.take(3).map { it.EnableProperty.set(true) }
                                }
                            }
                            button("Save to File") {
                                //enableWhen(model.dirty)
                                action {
                                    savetofile()
                                }
                            }
                                button("Load from File") {
                                    //enableWhen(model.dirty)
                                    action {
                                        loadFromFile()
                                    }
                                }
                                button("Write to Otter") {
                                    //    enableWhen(model.dirty)
                                    action {
                                        println("out")
                                        writetodevice()
                                    }
                                }
                                button("Start"){
                                    action{
                                        println("test")
                                    }
                                }
                                button("Stop"){
                                    action{
                                        println("test")
                                    }
                                }
                                button("Pause"){
                                    action{
                                        println("test")
                                    }
                                }
                                field("Ports") {
                                    val cmbxPort = combobox(port, port_list)
                                    port.onChange { newPort ->
                                        otter?.close()
                                        try {
                                            if (newPort != null) otter = Otter(newPort)
                                        } catch (e : OtterException) {
                                            alert(Alert.AlertType.ERROR, "Failed to connect to serial port!",
                                                e.message, ButtonType.OK)

                                        }
                                    }
                                    text = "Port List"
                                }
                        }
                    }
                }
            }


            right {
                form {
                    fieldset("Edit Channel") {
                        field("Name") {
                            textfield(model.name)
                        }
                        field("ChannelPeriod") {
                            textfield(model.ChannelPeriod)
                        }
                        field("Start Time") {
                            textfield(model.Start_time)
                        }
                        field("On time") {
                            textfield(model.Length_on)
                        }
                        field("Test") {
                            combobox(model.Input_chan, Input_Drivers)
                            text = "Input Channel"
                        }
                        field("Source") {
                            combobox(model.Source_add, Source_Drivers)
                            text = "Source Channel"
                        }
                        field("Divider") {
                            textfield(model.Divider)
                        }
//                        checkbox("Enable",booleanProperty){
//                            action{model.Enable}
//                        }

                        ///////

                        //                  combobox<Channel> {
                        //                    itemsProperty().bind(model.Input_chan)
                        //                  itemsProperty().onChange {
                        //                    model.item = Channels[0]
                        //                  selectionModel.selectFirst()
                        //            }
                        //          model.rebindOnChange(model.selectedItemProperty()) { mark ->
                        //            item = mark
                        //      }
                        //    selectionModel.selectFirst()
                        //  cellFormat { text = it.Input_chan }
                        //}

                        //////


                        button("Update") {
                            enableWhen(model.dirty)
                            action {
                                update()
                            }
                        }


                        //    button("Reset").action {
                        //        model.rollback()
                        //    }
                    }
                }
            }
        }
    }


    private fun update() {
        // Flush changes from the text fields into the model
        model.commit()

        // The edited person is contained in the model
        val channel = model.name

        // A real application would persist the person here
        //       println("Saving ${channel.value} / ${model.toString()}")

    }

    private fun savetofile() {
        FileChooser().showSaveDialog(null)
            ?.printWriter()?.use { f ->
                f.print(Json(JsonConfiguration.Stable)
                    .stringify(SerializableChannel.serializer().list, Channels.map { it.serialisable() }))
            }
    }

    private fun loadFromFile() {
        FileChooser().showOpenDialog(null)?.reader()?.use { f ->
            Json(JsonConfiguration.Stable)
                .parse(SerializableChannel.serializer().list, f.readText())
                .zip(Channels)
                .forEach { (fromFile, channel) -> channel.update(fromFile) }
        }
    }



    private fun writetodevice() {

        try {
            Otter("/dev/tty.usbserial-210328AB77681").use { /*otter ->
                val id = model.ID.value.toByte()
                val commands = listOf(
                    Command.Write(id, Parameter.Period, Value.Int(model.ChannelPeriod.value.toInt())),
                    Command.Write(id, Parameter.Start, Value.Int(model.Start_time.value.toInt())),
                    Command.Write(id, Parameter.End, Value.Int(model.Length_on.value.toInt())))


                    commands.map { cmd -> otter.send(cmd) }*/

            }
        } catch (e : OtterException) {
            e.printStackTrace()
        }
        println(model.name.value)
        println(model.ChannelPeriod.value)
        println(model.Length_on.value)
        println(model.Start_time.value)
    }
}

// channel is defined here Channel is the class
class ChannelnModel(channel: Channel) : ItemViewModel<Channel>(channel) {
    val name = bind(Channel::nameProperty)
    val ChannelPeriod = bind(Channel::PeriodProperty)
    val Start_time = bind(Channel::StartProperty)
    val Length_on = bind(Channel::LengthProperty)
    val Input_chan = bind(Channel::InputProperty)
    val Source_add = bind(Channel::SourceProperty)
    val Divider = bind(Channel::DividerProperty)
    val Enable = bind(Channel::EnableProperty)
    val ID = bind(Channel::IDProperty)
}

class MyApp: App(ChannelEditor::class)

fun main(args : Array<String>) {
 //   Otter.getPorts().forEach(::println)
 //   Otter("/dev/tty.usbserial-210328AB77681").use { otter ->
 //       val response = otter.send(Command.Version)
 //       if (response is Response.VersionNumber) {
 //           println("Otter Version: ${response.major}.${response.minor}.${response.patch}")
 //       } else {
 //           println("Unexpected response to version query!")
 //       }
 //   }
    launch<MyApp>(args)

}



