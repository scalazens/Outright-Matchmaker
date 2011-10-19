package com.outright.matchmaker

import scala.io.Source
import MatchMaker._


class MatchmakerServiceSpec extends AbstractSpec {
  "MatchmakerService" should {

    
    val criteria1_input_1 = 
	  """
		{
		"transactions":
	    [	
		{
	        "external_id":"Mon Aug 15 21:09:45 UTC 2011_266011488023_360382975804_SALE",
	        "charged_on":"2011-08-15T21:09:45Z",
	        "payee":
	        {
	            "name":"steven pargeon",
	            "street":"12 East 3rd. Street",
	            "city":"Pottstown",
	            "state":"PA",
	            "zip_code":"19464",
	            "country":"US",
	            "email":null,
	            "phone_number":null
	        },
	        "account":"eBay Meta Data Store",
	        "description":"Robert Rauschenberg-Darryl Pottorf-1999-Butler-Poster",
	        "amount":15.24,
	        "dests":
	        [
	            {
	                "amount":10.0,
	                "category":
	                {
	                    "name":"eBay Sales",
	                    "type":"INCOME"
	                },
	                "description":"Robert Rauschenberg-Darryl Pottorf-1999-Butler-Poster",
	                "payee":
	                {
	                    "name":"steven pargeon",
	                    "street":"12 East 3rd. Street",
	                    "city":"Pottstown",
	                    "state":"PA",
	                    "zip_code":"19464",
	                    "country":"US",
	                    "email":null,
	                    "phone_number":null
	                },
	                "item_id":"360382975804"
	            },
	            {
	                "amount":6.0,
	                "category":
	                {
	                    "name":"Shipping Income",
	                    "type":"INCOME"
	                },
	                "description":"Shipping (Robert Rauschenberg-Darryl Pottorf-1999-Butler-Poster)",
	                "payee":
	                {
	                    "name":"steven pargeon",
	                    "street":"12 East 3rd. Street",
	                    "city":"Pottstown",
	                    "state":"PA",
	                    "zip_code":"19464",
	                    "country":"US",
	                    "email":null,
	                    "phone_number":null
	                },
	                "item_id":"360382975804"
	            },
	            {
	                "amount":-0.76,
	                "category":
	                {
	                    "name":"PayPal Fees",
	                    "type":"EXPENSE",
	                    "tax_form":"ScheduleC",
	                    "tax_form_line":"line4"
	                },
	                "description":"PayPal Fee (Robert Rauschenberg-Darryl Pottorf-1999-Butler-Poster)",
	                "payee":
	                {
	                    "name":"PayPal"
	                },
	                "item_id":"360382975804"
	            }
	        ],
	        "company_data_source_account_id":179953,
	        "chetan_hints":
	        {
	            "transaction_id":"32A38258XV948450B"
	        }
	    }
	    ]
		}	

	  """


	  val criteria1_input_2 =
	  """
		{
		"transactions":
	    [
		{
	        "external_id":"2011-08-15-Payment-32A38258XV948450B",
	        "charged_on":"2011-08-15 23:42:56",
	        "payee":
	        {
	            "name":"Deposit"
	        },
	        "account":"PayPal",
	        "description":"",
	        "amount":15.24,
	        "dests":
	        [
	            {
	                "category":
	                {
	                    "type":"income"
	                },
	                "amount":15.24,
	                "description":"xxx"
	            }
	        ],
	        "company_data_source_account_id":179952,
	        "chetan_hints":
	        {
	            "transaction_id":"32A38258XV948450B"
	        }
	    }
	    ]
		}

	  """

	   val criteria1_result =
	   """
		{
		"transactions":
	    [
	        {
	            "external_id":"2011-08-15-Payment-32A38258XV948450B",
	            "charged_on":"2011-08-15 23:42:56",
	            "payee":
	            {
	                "name":"steven pargeon",
	                "street":"12 East 3rd. Street",
	                "city":"Pottstown",
	                "state":"PA",
	                "zip_code":"19464",
	                "country":"US",
	                "confidenceScore":0
	            },
	            "account":"PayPal",
	            "description":"",
	            "amount":15.24,
	            "company_data_source_account_id":179952,
	            "chetan_hints":
	            {
	                "transaction_id":"32A38258XV948450B",
	                "matched_external_id":"Mon Aug 15 21:09:45 UTC 2011_266011488023_360382975804_SALE"
	            },
	            "matches":
	            [
	                {
	                    "external_id":"Mon Aug 15 21:09:45 UTC 2011_266011488023_360382975804_SALE",
	                    "charged_on":"2011-08-15",
	                    "amount":15.24,
	                    "confidenceScore":0
	                }
	            ]
	        }
		]
		}

	  	"""
	
	
	 	val criteria2_input_1 = 
		  """
			{
			"transactions":
		    [
				{
			        "external_id":"Sun Aug 14 14:06:38 UTC 2011_EBAY_ORDER_93496123018",
			        "charged_on":"2011-08-14T14:06:38Z",
			        "payee":
			        {
			            "name":"Keith Huebner",
			            "street":"815 W Conant St",
			            "city":"Portage",
			            "state":"WI",
			            "zip_code":"53901-1911",
			            "country":"US",
			            "phone_number":null,
			            "email":null
			        },
			        "account":"eBay Meta Data Store",
			        "description":"Sale (Items 360384918104,
			         360385128086)",
			        "amount":5.02,
			        "dests":
			        [
			            {
			                "description":"Items 360384918104,
			                 360385128086",
			                "payee":
			                {
			                    "name":"Keith Huebner",
			                    "street":"815 W Conant St",
			                    "city":"Portage",
			                    "state":"WI",
			                    "zip_code":"53901-1911",
			                    "country":"US",
			                    "phone_number":null,
			                    "email":null
			                },
			                "category":
			                {
			                    "name":"eBay Sales",
			                    "type":"INCOME"
			                },
			                "amount":1.98
			            },
			            {
			                "description":"Shipping (Items 360384918104,
			                 360385128086)",
			                "payee":
			                {
			                    "name":"Keith Huebner",
			                    "street":"815 W Conant St",
			                    "city":"Portage",
			                    "state":"WI",
			                    "zip_code":"53901-1911",
			                    "country":"US",
			                    "phone_number":null,
			                    "email":null
			                },
			                "category":
			                {
			                    "name":"Shipping Income",
			                    "type":"INCOME"
			                },
			                "amount":3.5
			            },
			            {
			                "amount":-0.46,
			                "category":
			                {
			                    "name":"PayPal Fees",
			                    "type":"EXPENSE",
			                    "tax_form":"ScheduleC",
			                    "tax_form_line":"line4"
			                },
			                "description":"PayPal Fee (Items 360384918104,
			                 360385128086)",
			                "payee":
			                {
			                    "name":"PayPal"
			                }
			            }
			        ],
			        "company_data_source_account_id":179953,
			        "chetan_hints":
			        {
			            "transaction_id":null
			        }
			    }

			    {
			        "external_id":"Sun Aug 14 14:06:38 UTC 2011_EBAY_ORDER_93496123018_DUPLICATE",
			        "charged_on":"2011-08-19T14:06:38Z",
			        "payee":
			        {
			            "name":"Keith Huebner",
			            "street":"815 W Conant St",
			            "city":"Portage",
			            "state":"WI",
			            "zip_code":"53901-1911",
			            "country":"US",
			            "phone_number":null,
			            "email":null
			        },
			        "account":"eBay Meta Data Store",
			        "description":"Sale (Items 360384918104,
			         360385128086)",
			        "amount":5.02,
			        "dests":
			        [
			            {
			                "description":"Items 360384918104,
			                 360385128086",
			                "payee":
			                {
			                    "name":"Keith Huebner",
			                    "street":"815 W Conant St",
			                    "city":"Portage",
			                    "state":"WI",
			                    "zip_code":"53901-1911",
			                    "country":"US",
			                    "phone_number":null,
			                    "email":null
			                },
			                "category":
			                {
			                    "name":"eBay Sales",
			                    "type":"INCOME"
			                },
			                "amount":1.98
			            },
			            {
			                "description":"Shipping (Items 360384918104,
			                 360385128086)",
			                "payee":
			                {
			                    "name":"Keith Huebner",
			                    "street":"815 W Conant St",
			                    "city":"Portage",
			                    "state":"WI",
			                    "zip_code":"53901-1911",
			                    "country":"US",
			                    "phone_number":null,
			                    "email":null
			                },
			                "category":
			                {
			                    "name":"Shipping Income",
			                    "type":"INCOME"
			                },
			                "amount":3.5
			            },
			            {
			                "amount":-0.46,
			                "category":
			                {
			                    "name":"PayPal Fees",
			                    "type":"EXPENSE",
			                    "tax_form":"ScheduleC",
			                    "tax_form_line":"line4"
			                },
			                "description":"PayPal Fee (Items 360384918104,
			                 360385128086)",
			                "payee":
			                {
			                    "name":"PayPal"
			                }
			            }
			        ],
			        "company_data_source_account_id":179953,
			        "chetan_hints":
			        {
			            "transaction_id":null
			        }
			    }
	  		]
			}
		  """

		val criteria2_input_2 = 
		  """
			{
			"transactions":
		    [
				 {
			            "external_id":"2011-08-14-Payment-6GV28178V24485001",
			            "charged_on":"2011-08-14 14:56:54",
			            "payee":
			            {
			                "name":"Deposit"
			            },
			            "account":"PayPal",
			            "description":"",
			            "amount":5.02,
			            "dests":
			            [
			                {
			                    "category":
			                    {
			                        "type":"income"
			                    },
			                    "amount":5.02,
			                    "description":"xxx"
			                }
			            ],
			            "company_data_source_account_id":179952,
			            "chetan_hints":
			            {
			                "transaction_id":"6GV28178V24485001"
			            }
			        }
	  		]
			}
		  """

		val criteria2_result = 
		  """
			{
			"transactions":
		    [
				{
		            "external_id":"2011-08-14-Payment-6GV28178V24485001",
		            "charged_on":"2011-08-14 14:56:54",
		            "payee":
		            {
		                "name":"Keith Huebner",
		                "street":"815 W Conant St",
		                "city":"Portage",
		                "state":"WI",
		                "zip_code":"53901-1911",
		                "country":"US",
		                "confidenceScore":0
		            },
		            "account":"PayPal",
		            "description":"",
		            "amount":5.02,
		            "company_data_source_account_id":179952,
		            "chetan_hints":
		            {
		                "transaction_id":"6GV28178V24485001",
		                "matched_external_id":"Sun Aug 14 14:06:38 UTC 2011_EBAY_ORDER_93496123018"
		            },
		            "matches":
		            [
		                {
		                    "external_id":"Sun Aug 14 14:06:38 UTC 2011_EBAY_ORDER_93496123018",
		                    "charged_on":"2011-08-14",
		                    "amount":5.02,
		                    "confidenceScore":0
		                },
		                {
		                    "external_id":"Sun Aug 14 14:06:38 UTC 2011_EBAY_ORDER_93496123018_DUPLICATE",
		                    "charged_on":"2011-08-19",
		                    "amount":5.02,
		                    "confidenceScore":5
		                }
		            ]
		        }
	  		]
			}
		  """
		
    val matchingresult_1 = matchmaker.getmatchingelements(criteria1_input_1,criteria1_input_2,0)()
    
    "Match FileasString[1] Transaction[1](amount,date) with FileasString[2] Transaction[2](amount,date)" in {
	  serializeJSON(parseJSONString(matchingresult_1)) mustEqual serializeJSON(parseJSONString(criteria1_result))
    }

	val matchingresult_2 = matchmaker.getmatchingelements(criteria2_input_1,criteria2_input_2,30)()

    "Match FileasString[2] Transaction[2](amount,date <= 30) with FileasString[2] Transaction[2](amount,date)" in 
    {
	  serializeJSON(parseJSONString(matchingresult_2)) mustEqual serializeJSON(parseJSONString(criteria2_result))
    }
  
    val fromfile = Source.fromURL(getClass.getResource("/ebay_txns_cleaned_693395.json")).mkString
    val tofile = Source.fromURL(getClass.getResource("/paypal_txns_cleaned_693395.json")).mkString
    val expectedresult = Source.fromURL(getClass.getResource("/expected_result.json")).mkString
	val matchingresult_3 = matchmaker.getmatchingelements(fromfile,tofile,30)()

	"Match A Transactional Source List (steam/file) to another" in
	{
		serializeJSON(parseJSONString(matchingresult_3)) mustEqual serializeJSON(parseJSONString(expectedresult))
	}
	
  }
}
