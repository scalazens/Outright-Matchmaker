package com.outright.matchmaker


import net.liftweb.json.JsonAST.JValue
import net.liftweb.json
import net.liftweb.json._
import net.liftweb.json.JsonDSL._
import Extraction._
import org.joda.time._  
import org.joda.convert._
import java.io._
import scala.io._
import OutrightImplicits._
import OutrightImplicits.enrichFile

object MatchMaker 
{



/*
Function abstraction to parse JSON File and convert to Serialized JValue (net.lift._) format
*/
def withJSONFile(filePath : String ) : String = 
{
	val fileContents = scala.io.Source.fromFile(filePath)
	try
	{
		fileContents .mkString
	}
	finally
	{
		fileContents.close
	}
}


/*
Function abstraction to parse JSON String and convert to Serialized JValue (net.lift._) format
*/
def parseJSONString(jsonString : String) : JValue = 
{
	json.JsonParser.parse(jsonString)
}


/*
Function abstraction to extract JValue abstraction from "parseJSON(...)" and map to Data Structure
*/

def serializeJSON(jsonValue : JValue) = 
{
  	
	val serializeData = jsonValue.extract[Transactions]
	serializeData.transactions
}

/*
Function abstraction to convert Serialized JValue (net.lift._) format to KV(Key->Value) pair
*/

def transformJSONtoKV(listOfTransactions : List[Transaction])  = 
{
	listOfTransactions.map { e =>
		 
		 (  Key(  e.amount , 
		   			   new LocalDate(new DateTime( (e.charged_on.getOrElse("")).trim.replaceAll("\\s+","T"))))
		 , e) 
	}.toMap
}


var confidenceCache = new collection.mutable.HashMap[ Key , collection.SortedSet[ DataValue ] ]()


def iterateMatrix(mapDataSrc : Map[Key, Transaction] , 
			      mapDataDst : Map[Key, Transaction],
			      numberOfDays : Int 
			  ) = 
{
        var confidenceCache = new collection.mutable.HashMap[ Key , collection.SortedSet[ DataValue ] ]()
  		for { 	
			  (sK,sV) <- mapDataSrc 
	      	  (dK,dV) <- mapDataDst 
			}
			yield 
			{
				 val amountExpr = if(sK.amountValue == dK.amountValue) true else false
				 val daysBetween =  Days.daysBetween(sK.dateValue,dK.dateValue).getDays()

				 if( amountExpr && (daysBetween >= 0) && (daysBetween <= numberOfDays) )
				 {
				  	val keyValue = DataValue( dK, dV , daysBetween )
				  	var dataValues = collection.SortedSet[ DataValue ]()
				  	dataValues += keyValue


				  	if(confidenceCache.contains(sK))
					  confidenceCache(sK) += keyValue
				  	else
					  confidenceCache += ( sK -> dataValues ) 
				  }
				  	
			}
			confidenceCache.toMap
}

def mergeDataContent(lookupKeys   : Map[ Key , collection.SortedSet[ DataValue ] ],
					 mergetoFile  : Map[Key, Transaction] 
					 ) 
= 
{
	val returnMap = collection.mutable.Map[Key, Transaction]()
	mergetoFile foreach { e => returnMap += e }
	
	lookupKeys foreach
	{
		case(key,value) =>
		if(returnMap.contains(key)) 
		{
			/*
			 * Fetch Key
			 */
			//var transaction : Transaction = getValuefromSome[Transaction](returnMap.get(key))
			
			var transaction : Transaction = (returnMap.get(key)).getOrElse(null)
			
			/*
		     * Get attributes from Matched Object to merge into the Destination Object
			 */
			val matchedTransaction = value.head.transaction
			val matchedPayee  = matchedTransaction.payee
			val matched_external_id = matchedTransaction.external_id
			val matched_id = (matchedTransaction.chetan_hints).get.matched_transaction_id
			val matched_company_data_source_id = matchedTransaction.company_data_source_account_id
			
			var listOfMatches = new collection.mutable.MutableList[ MatchValue ]
			
			value foreach {
				
				e => 
				listOfMatches += MatchValue( e.transaction.external_id, 
											 Some(e.key.dateValue.toString), 
											 Some(e.key.amountValue) , 
											 Some(e.confidence) )
			}
			
			/*
			 * Mutate the destination object by assignment 
			*/
			
			(transaction.chetan_hints).get.matched_external_id = matched_external_id
			(transaction.chetan_hints).get.matched_transaction_id = matched_id
			matchedPayee.confidenceScore = Some(value.head.confidence)
				 
										
			transaction.payee = matchedPayee
			
			transaction.matches = Some(listOfMatches.toList)

			/*
			 *  Mutate the destination object to add value
			 */
			returnMap(key) = transaction
		}
	
	}
	val filteredTransactions = com.outright.matchmaker.Transactions(returnMap.values.toList)
	compact(render(decompose(filteredTransactions)))
}

def withFileCurry(f : (String, String, Int) => String)
				 (srcFilePath : String , dstFilePath : String, timeSpan : Int) 
  							 =
{
	val srcFileContents = withJSONFile(srcFilePath)
	val dstFileContents = withJSONFile(dstFilePath)
	
	f(srcFileContents,dstFileContents,timeSpan)
}

val performMatchingwithFile = withFileCurry(performMatching) _


def performMatching(vendorDataSrcContents : String , vendorDataDstContents : String, timeSpan : Int)
=
{
    val serializedSrcValue = serializeJSON(parseJSONString(vendorDataSrcContents))
    val serializedDstValue = serializeJSON(parseJSONString(vendorDataDstContents))

	val ebayMap   = transformJSONtoKV(serializedSrcValue)
	val paypalMap = transformJSONtoKV(serializedDstValue)
	
	var matchedMap = iterateMatrix(paypalMap, ebayMap , timeSpan)
	mergeDataContent(matchedMap,paypalMap)
}

def createOutputFile(jsonString : String, outputFile : String)
=
{
	val fileOutput = new File(outputFile)
	fileOutput.text = jsonString
}



def main(args: Array[String])
{
	args.foreach { e => println( " Argument - " + e ) }
	
    if(args.length < 4) 
		println(" Usage : program [ebayFilePath],[paypalFilePath],[timeSpan],[outFilePath]")
	else	
	{
	    val jsonResult = performMatchingwithFile(args(0), 
												 args(1), 
												 args(2).toInt)
		createOutputFile( jsonResult , args(3))
		println(" Successfully created file - " + args(3))
	}	
}
 
}