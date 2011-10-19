package com.outright.matchmaker


import java.io._
import scala.io._
import net.liftweb.json._
import JsonAST._ 
import JsonDSL._ 
import JsonParser._ 
import Extraction._

class RichFile( file: File ) {

  def text = Source.fromFile( file ).mkString

  def text_=( s: String ) {
    val out = new PrintWriter( file )
    try{ out.println( s ) }
    finally{ out.close }
  }
}


object OutrightImplicits {
  implicit val formats = DefaultFormats
   implicit def enrichFile( file: File ) = new RichFile( file )
}