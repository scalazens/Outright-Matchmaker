package com.outright.matchmaker


import org.joda.time._  
import org.joda.convert._
import scala.collection.mutable



case class Payee(var name : String, 
				 var street : Option[String], 
				 var city : Option[String], 
				 var state : Option[String], 
				 var zip_code : Option[String], 
				 var country : Option[String], 
				 var email : Option[String] , 
				 var phone_number : Option[String],
				 var confidenceScore : Option[Int])
				
case class Category(var name : String, 
				    var `type` : String, 
					var tax_form : Option[String], 
					var tax_form_line : Option[String])				

case class Split(var amount : String, 
				 var category : Category, 
				 var description : String, 
				 var payee : Payee, 
				 var item_id : Option[String])
													
case class Hints(var transaction_id : Option[String],
				 var matched_transaction_id : Option[String], 
				 var matched_external_id : Option[String])

case class Matches(matchValues : Option[List[MatchValue]])
case class MatchValue(var external_id : Option[String], 
					  var charged_on : Option[String],
					  var amount : Option[Double],
					  var confidenceScore : Option[Int]
					  )
									
case class Transaction(var external_id : Option[String], 
					   var charged_on : Option[String], 
					   var payee : Payee, account : String,
					   var description : String, 
					   var amount : Double, 
					   var dests : Option[List[Split]],
					   var company_data_source_account_id : Int, 
					   var chetan_hints : Option[Hints],
					   var matches : Option[List[MatchValue]]
					  )

case class Transactions(var transactions : List[Transaction])

case class Key(var amountValue : Double, var dateValue : LocalDate)
case class DataValue(var key : Key, var transaction : Transaction, var confidence : Int ) extends Ordered[DataValue]  
{ def compare(o:DataValue) = confidence -o.confidence }


