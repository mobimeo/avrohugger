package avrohugger
package test
package scavro

import avrohugger.format.Scavro
import org.specs2._

class ScavroStringToStringsSpec extends Specification {
  
  def is = s2"""
    Scavro Generator stringToStrings method should
      correctly generate with messy schema $eA
      correctly generate from a protocol with messages $e1
      correctly generate a simple case class definition in a package $e2
      correctly generate a simple case class definition in the default package $e3
      correctly generate a nested case class definition from a schema $e4
      correctly generate a nested case class from IDL $e5
      correctly generate a recursive case class from IDL $e6
      correctly generate enums from schema $e7
      correctly generate enums from protocol $e8
      correctly generate enums from IDL $e9
      correctly generate nested enums $e10
      correctly generate bytes from schema $e11
      correctly generate bytes from protocol $e12
      correctly generate bytes from IDL $e13
      correctly generate records depending on others defined in a different- and same-namespaced AVDL and AVSC $e14
      correctly generate an empty case class definition $e15
      correctly generate default values $e16
      
      
      
      
      correctly generate a protocol with no ADT when asked $e21
  """
  
  // tests specific to Scavro
  def eA = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/clash.avsc")
    val gen = new Generator(Scavro)
    val List(source0, source1, source2) = gen.stringToStrings(inputString)
    source0 === util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/model/ClashInner.scala")
    source1 === util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/model/ClashOuter.scala")
    source2 === util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/model/ClashRecord.scala")
  }
  
  // tests common to fileToX and stringToX
  def e1 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/mail.avpr")
    val gen = new Generator(Scavro)
    val List(source) = gen.stringToStrings(inputString)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/proto/model/Message.scala")
    source === expected
  }
  
  def e2 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/user.avsc")
    val gen = new Generator(Scavro)
    val List(source) = gen.stringToStrings(inputString)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/model/User.scala")
    source === expected
  }
  
  def e3 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/AvroTypeProviderTestNoNamespace.avsc")
    val gen = new Generator(Scavro)
    gen.stringToStrings(inputString) must throwA(new java.lang.RuntimeException("Scavro requires a namespace because Java classes cannot be imported from the default package"))
  }
  
  def e4 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/nested.avsc")
    val gen = new Generator(Scavro)
    val List(source2, source1, source0) = gen.stringToStrings(inputString)

    val expected0 = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/model/Level0.scala")
    val expected1 = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/model/Level1.scala")
    val expected2 = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/model/Level2.scala")
    
    source0 === expected0
    source1 === expected1
    source2 === expected2
  }
  
  def e5 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/nested.avdl")
    val myAvroScalaCustomTypes = Scavro.defaultTypes.copy(protocol = types.ScalaADT)
    val gen = new Generator(format = Scavro, avroScalaCustomTypes = Some(myAvroScalaCustomTypes))
    val List(source) = gen.stringToStrings(inputString)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/idl/model/NestedProtocol.scala")
    source === expected      
  }
  
  def e6 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/recursive.avdl")
    val gen = new Generator(Scavro)
    val List(source) = gen.stringToStrings(inputString)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/idl/model/Recursive.scala")
    source === expected
  }
  
  def e7 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/enums.avsc")
    val gen = new Generator(Scavro)
    val List(source) = gen.stringToStrings(inputString)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/model/Suit.scala")
    source === expected
  }
  
  def e8 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/enums.avpr")
    val myAvroScalaCustomTypes = Scavro.defaultTypes.copy(protocol = types.ScalaADT)
    val gen = new Generator(format = Scavro, avroScalaCustomTypes = Some(myAvroScalaCustomTypes))
    val List(source) = gen.stringToStrings(inputString)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/proto/model/EnumProtocol.scala")
    source === expected
  }
  
  def e9 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/enums.avdl")
    val myAvroScalaCustomTypes = Scavro.defaultTypes.copy(protocol = types.ScalaADT)
    val gen = new Generator(format = Scavro, avroScalaCustomTypes = Some(myAvroScalaCustomTypes))
    val List(source) = gen.stringToStrings(inputString)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/idl/model/EnumProtocol.scala")
    source === expected
  }
  
  def e10 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/enums_nested.avsc")
    val gen = new Generator(Scavro)
    val List(sourceEnum, sourceRecord) = gen.stringToStrings(inputString)

    val expectedEnum = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/model/Direction.scala")
    val expectedRecord = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/model/Compass.scala")

    sourceEnum === expectedEnum
    sourceRecord === expectedRecord
  }
  
  def e11 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/bytes.avsc")
    val gen = new Generator(Scavro)
    val List(source) = gen.stringToStrings(inputString)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/model/BinarySc.scala")
    source === expected
  }
  
  def e12 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/bytes.avpr")
    val gen = new Generator(Scavro)
    val List(source) = gen.stringToStrings(inputString)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/proto/model/BinaryPr.scala")
    source === expected
  }
  
  def e13 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/bytes.avdl")
    val gen = new Generator(Scavro)
    val List(source) = gen.stringToStrings(inputString)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/idl/model/BinaryIdl.scala")
    source === expected
  }
  
  def e14 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/import.avdl")
    val gen = new Generator(Scavro)
    gen.stringToStrings(inputString) must throwA(new java.lang.RuntimeException("Imports not supported in String IDLs, only avdl files."))
  }
  
  def e15 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/AvroTypeProviderTestEmptyRecord.avdl")
    val myAvroScalaCustomTypes = Scavro.defaultTypes.copy(protocol = types.ScalaADT)
    val gen = new Generator(format = Scavro, avroScalaCustomTypes = Some(myAvroScalaCustomTypes))
    val List(source) = gen.stringToStrings(inputString)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/scavro/test/model/Calculator.scala")
    source === expected
  }
  
  def e16 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/defaults.avdl")
    val myAvroScalaCustomTypes = Scavro.defaultTypes.copy(protocol = types.ScalaADT)
    val gen = new Generator(format = Scavro, avroScalaCustomTypes = Some(myAvroScalaCustomTypes))
    val List(source) = gen.stringToStrings(inputString)
    val expected = util.Util.readFile("avrohugger-core/src/test/expected/scavro/example/idl/model/Defaults.scala")
    source === expected
  }
  
  
  
  
  def e21 = {
    val inputString = util.Util.readFile("avrohugger-core/src/test/avro/AvroTypeProviderTestProtocol.avdl")
    val gen = new Generator(format = Scavro)
  
    val List(source) = gen.stringToStrings(inputString)
  
    source === util.Util.readFile("avrohugger-core/src/test/expected/scavro/test/model/Joystick.scala")
  }

}
