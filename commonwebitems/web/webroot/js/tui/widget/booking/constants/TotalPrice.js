define("tui/widget/booking/constants/TotalPrice", [ "dojo",
                                                  "dojo/_base/declare",
                                                  "dojo/query",
                                                  "dojo/dom-class",
                                                  'dojo/dom',
                                                  "dojo/dom-attr"
    ], function (dojo,declare, query, domClass,dom, domAttr) {

	var totalPriceVal = declare("tui.widget.booking.constants.TotalPrice", [], {
		
	

		updatetotalprice :function(totalPrice, poundClass, priceClass, decimalClass){
				var regex = /^[0-9]+$/;
				var price = totalPrice.split('.'),
				    total = price[0],
				    decimal = price[1],
				    lengthofprice = total.length,
				    pound = total.slice(0,1),
				    currency = totalPrice.slice(1,lengthofprice),
				    totalPriceStr = totalPrice;
				if(pound === "+"){
					 pound = total.slice(0,2);
					 currency = totalPrice.slice(2,lengthofprice);
				}
				if(decimal == undefined){
					decimal = "00";
		      	}
				
				if(regex.test(parseInt(currency))){
					if(poundClass != null){
						totalPriceStr = '<span class="'+poundClass+'">' + pound + '</span>' +
							'<span class="'+priceClass+'">' + currency + '</span>' + '.' +
							'<span class="'+decimalClass+'">' + decimal + '</span>' ;
								
					}else{
						totalPriceStr = pound + currency + '.'+ decimal ;
						
					}
				}
				return totalPriceStr;
			}
		
	});
	 return new totalPriceVal();
});