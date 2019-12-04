package avrohugger
package stores

import java.util.concurrent.ConcurrentHashMap
import scala.jdk.CollectionConverters._

import scala.reflect.runtime.universe._

class TypecheckDependencyStore {

  val knownClasses: scala.collection.concurrent.Map[String, Tree] = new ConcurrentHashMap[String, Tree]().asScala

  def accept(tree: Tree): Unit = {
    val _ = knownClasses += (tree.toString -> tree)
  }

}