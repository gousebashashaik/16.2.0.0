var sortFlag = true;
var flightNumberPlaceHolder = 'Type Flight Number. i.e TOM 619';
var departOrArrFromPlaceHolder = 'Type Airport or Code i.e Gatwick or LGW';

function sortData(timeType, gridObj){
	   var upArr = "&uarr;",
	       downArr = "&darr;";
		var gridData = gridObj.getElementsByTagName('tbody').item(0);
        var gridRowData = gridData.getElementsByTagName('tr');
        	for(var i = 0, len = gridRowData.length; i < len - 1; i++){
                for(var j = 0; j < gridRowData.length - (i + 1); j++){
                	if(sortFlag){
                      if(parseFloat(gridRowData.item(j).getElementsByTagName('td')[timeType].getElementsByTagName('input')[0].value) < parseFloat(gridRowData.item(j+1).getElementsByTagName('td')[timeType].getElementsByTagName('input')[0].value)){
                      	 gridData.insertBefore(gridRowData.item(j+1),gridRowData.item(j));
                        }
                	}
                	else{
                		if(parseFloat(gridRowData.item(j).getElementsByTagName('td')[timeType].getElementsByTagName('input')[0].value) > parseFloat(gridRowData.item(j+1).getElementsByTagName('td')[timeType].getElementsByTagName('input')[0].value)){
                         	 gridData.insertBefore(gridRowData.item(j+1),gridRowData.item(j));
                           }
                	}
                }
        }
        sortFlag = !sortFlag;
    }
if(document.getElementById("flightnumber")){
	document.getElementById("flightnumber").value = "";
}
if(document.getElementById("departFrom")){
	document.getElementById("departFrom").value = "";
}
if(document.getElementById("arriveTo")){
	document.getElementById("arriveTo").value = "";
}

var onkeypress = function () {
dojo.addOnLoad(function(){
	
		dojo.require("dojo/dom-attr");
	    dojo.require("dojo/NodeList-manipulate");
	    dojo.require("dojo/NodeList-traverse");
	 	 
	    if(dojo.isIE){
			var flight_icons = dojo.query(".FlightSearch>div img");
			dojo.forEach(flight_icons, function(elm, idx){
				dojo.style(elm, {
					"position":"relative", 
					"top":"9px"
				});
			});
			
			var elmPlc = dojo.query(".FlightSearch>div input");
			dojo.forEach(elmPlc, function(elm, idx){
				if(idx === 0){
					return false;
				}else{
					elm.placeholder = "";
					elm.value = "";
					dojo.removeAttr(elm, "class");
				}
				
			});
			
			dojo.style(dojo.byId("list-of-deps"), {
				"top":"34px"
			});
			dojo.style(dojo.byId("list-of-arrs"), {
				"top":"34px"
			});
		}
	    
	    
//yymmdd- format date//
var dateFormat = function () {
	var token = /d{1,4}|m{1,4}|yy(?:yy)?|([HhMsTt])\1?|[LloSZ]|"[^"]*"|'[^']*'/g,
        timezone = /\b(?:[PMCEA][SDP]T|(?:Pacific|Mountain|Central|Eastern|Atlantic) (?:Standard|Daylight|Prevailing) Time|(?:GMT|UTC)(?:[-+]\d{4})?)\b/g,
        timezoneClip = /[^-+\dA-Z]/g,
        pad = function (val, len) {
            val = String(val);
            len = len || 2;
            while (val.length < len)
                val = "0" + val;
            return val;
        };

    // Regexes and supporting functions are cached through closure
    return function (date, mask, utc) {
        var dF = dateFormat;

        // You can't provide utc if you skip other args (use the "UTC:" mask
        // prefix)
        if (arguments.length == 1 && Object.prototype.toString.call(date) == "[object String]" && !/\d/.test(date)) {
            mask = date;
            date = undefined;
        }

        // Passing date through Date applies Date.parse, if necessary
        date = date ? new Date(date) : new Date;
        if (isNaN(date))
            throw SyntaxError("invalid date");

        mask = String(dF.masks[mask] || mask || dF.masks["default"]);

        // Allow setting the utc argument via the mask
        if (mask.slice(0, 4) == "UTC:") {
            mask = mask.slice(4);
            utc = true;
        }

        var _ = utc ? "getUTC" : "get",
            d = date[_ + "Date"](),
            D = date[_ + "Day"](),
            m = date[_ + "Month"](),
            y = date[_ + "FullYear"](),
            H = date[_ + "Hours"](),
            M = date[_ + "Minutes"](),
            s = date[_ + "Seconds"](),
            L = date[_ + "Milliseconds"](),
            o = utc ? 0 : date.getTimezoneOffset(),
            flags = {
                d: d,
                dd: pad(d),
                ddd: dF.i18n.dayNames[D],
                dddd: dF.i18n.dayNames[D + 7],
                m: m + 1,
                mm: pad(m + 1),
                mmm: dF.i18n.monthNames[m],
                mmmm: dF.i18n.monthNames[m + 12],
                yy: String(y).slice(2),
                yyyy: y,
                h: H % 12 || 12,
                hh: pad(H % 12 || 12),
                H: H,
                HH: pad(H),
                M: M,
                MM: pad(M),
                s: s,
                ss: pad(s),
                l: pad(L, 3),
                L: pad(L > 99 ? Math.round(L / 10) : L),
                t: H < 12 ? "a" : "p",
                tt: H < 12 ? "am" : "pm",
                T: H < 12 ? "A" : "P",
                TT: H < 12 ? "AM" : "PM",
                Z: utc ? "UTC" : (String(date).match(timezone) || [""])
                    .pop().replace(timezoneClip, ""),
                o: (o > 0 ? "-" : "+") + pad(Math.floor(Math.abs(o) / 60) * 100 + Math.abs(o) % 60, 4),
                S: ["th", "st", "nd", "rd"][d % 10 > 3 ? 0 : (d % 100 - d % 10 != 10) * d % 10]
            };

        return mask.replace(token, function ($0) {
            return $0 in flags ? flags[$0] : $0.slice(1, $0.length - 1);
        });
    };
}();

// Some common format strings
dateFormat.masks = {
    "default": "ddd mmm dd yyyy HH:MM:ss",
    shortDate: "m/d/yy",
    mediumDate: "mmm d, yyyy",
    longDate: "mmmm d, yyyy",
    fullDate: "dddd, mmmm d, yyyy",
    shortTime: "h:MM TT",
    mediumTime: "h:MM:ss TT",
    longTime: "h:MM:ss TT Z",
    isoDate: "yyyy-mm-dd",
    isoTime: "HH:MM:ss",
    isoDateTime: "yyyy-mm-dd'T'HH:MM:ss",
    isoUtcDateTime: "UTC:yyyy-mm-dd'T'HH:MM:ss'Z'"
};

// Internationalization strings
dateFormat.i18n = {
    dayNames: ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sunday",
        "Monday", "Tuesday", "Wednesday", "Thursday", "Friday",
        "Saturday"
    ],
    monthNames: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug",
        "Sep", "Oct", "Nov", "Dec", "January", "February", "March",
        "April", "May", "June", "July", "August", "September",
        "October", "November", "December"
    ]
};

    // For convenience...

var today = dateFormat(new Date(), "yymmdd").toUpperCase();
var tomday = dateFormat(new Date().getTime() + 86400000, "yymmdd").toUpperCase();

Date.prototype.format = function (mask, utc) {
    return dateFormat(this, mask, utc);
};

function addColons(str) {
    var result = '';
    while (str.length > 0) {
        result += str.substring(0, 2) + ':';
        str = str.substring(2);
    }
    return result;
}

var dt = new Date();
var tom = dt.setTime(dt.getTime() + 86400000);
var isFlightFound = true;


dojo.ready(function () {
    var x = dojo.byId("searchRe");
    var flightVal = dojo.byId("flightnumber");
    var tomtext = dojo.byId("tomtext");
    var flag = false;
    dojo.query("#displayer").empty();
    dojo.query("#displayer").append((dateFormat((new Date()), "dddd dS mmmm yyyy")).toUpperCase());
    dojo.query("#displayer1").empty();
    dojo.query("#displayer1").append((dateFormat((new Date(tom)), "dddd dS mmmm yyyy")).toUpperCase());
    
    
    var eventHandle  = {
    		browserMode: function(){
    			var evt = null;
    			if(dojo.isIE){
    				evt = window.event
    			}else if(dojo.isFF){
    				evt = window.Event
    			}else if(dojo.isChrome){
    				evt = event;
    			}
    		}
    }
    //UTIL Function
    var utils = {
    	LOADING_PLACEHOLDER:dojo.byId("loading-flight-results"), 
    	LOADING_ICON:"<img src='images/loading-3-anim-transparent.gif' alt='Loading..'/>", 
    	requestDa:{
    		url: "",
    		fieldId:"",
    		callJson: function(){
    			
    			dojo.xhrGet({
    		        url: this.url,
    		        fieldId:this.fieldId,
    		        handleAs: "json",
    	            load: function (jsonData) {
    	            	utils.handleJsonObj(this.url, this.fieldId, jsonData);
    	            },
    	            error: function () { 
    	            	
    	            }
    			});
    		}
    		
    	},
    	handleJsonObj:function(url, fieldId, obj){
    		this.call(url, fieldId, obj);
    	},
    	call: function (url, fieldId, jsonData) { 
		    flvalue = flightVal.value;
		    var flightNumberSpaced;
		    if (flvalue.indexOf("TOM") == -1) {
		        flvalue = "TOM" + flvalue;
		    }
		    
		    this.afterResults(url, fieldId, jsonData);
		 },
		 enableShowAllLink:function(url){
			if(url == "allDepartures"){
				dojo.style(dojo.byId("allDeps"),{"color":"black"});//#8bbce3
				dojo.style(dojo.byId("allArvs"),{"color":"#8bbce3"});//#8bbce3
				dojo.style(dojo.query(".allFlights .flightImg")[0], {
				  background: "url(images/FlightStatus-Ico.png) no-repeat -10px -135px",
				  width: "11px",
				  height: "11px"
				});
				dojo.style(dojo.query(".allFlights .flightImg1")[0], {
					background: "url(images/FlightStatus-Ico.png) no-repeat -10px -156px",
					width: "11px",
					height: "11px"
				});
			}
			else if(url == "allArrivals"){
				
				dojo.style(dojo.byId("allArvs"),{"color":"black"});//#8bbce3
				dojo.style(dojo.byId("allDeps"),{"color":"#8bbce3"});//#8bbce3
				dojo.style(dojo.query(".allFlights .flightImg1")[0], {
					background: "url(images/FlightStatus-Ico.png) no-repeat -10px -177px",
					width: "11px",
					height: "11px"
				});
				dojo.style(dojo.query(".allFlights .flightImg")[0], {
				  background: "url(images/FlightStatus-Ico.png) no-repeat -10px -114px",
				  width: "11px",
				  height: "11px"
				});
			}
			else{
				dojo.style(dojo.byId("allDeps"),{"color":"#8bbce3"});
				dojo.style(dojo.byId("allArvs"),{"color":"#8bbce3"});
				dojo.style(dojo.query(".allFlights .flightImg")[0], {
				  background: "url(images/FlightStatus-Ico.png) no-repeat -10px -114px",
				  width: "11px",
				  height: "11px"
				});
				dojo.style(dojo.query(".allFlights .flightImg1")[0], {
					background: "url(images/FlightStatus-Ico.png) no-repeat -10px -156px",
					width: "11px",
					height: "11px"
				});
			}
		 },
		 
		 afterResults: function(url, fieldId, jsonData){ 
			 
		 	this.enableShowAllLink(url.split("/")[0]);
		 	//url = url2;
		 	dojo.byId("loading-flight-results").innerHTML ="";
		 	var departFrom = dojo.byId("departFrom");
		 	var deprtsId = dojo.byId("list-of-deps");
		 	 var arriveTo = dojo.byId("arriveTo");
		 	var arvlsId = dojo.byId("list-of-arrs");
		 	//var nonUKHeader = '<li class="disable-list-select">- - - - - - - - - - - - - - -</li>';
        	if(fieldId == "departFrom" ){
		 		if(arriveTo.value.length == 0){
		 			var deptAutodetails = jsonData.departureAutoSuggestData;
			 		var deptsAutoDiv = "<ul>";
			 		var UKList = "";
			 		var NonUKList = "";
			 		for(var i = 0; i < deptAutodetails.length; i++){
			 			if(deptAutodetails[i].depAirPortCountryCode == "GBR")
			 				UKList+= '<li>'+deptAutodetails[i].depAirPortInfo+'</li>';
			 			else{
			 				NonUKList+= '<li>'+deptAutodetails[i].depAirPortInfo+'</li>';
			 			}
			 		}
			 		
			 		deptsAutoDiv+=UKList+NonUKList;
			 		deptsAutoDiv+="</ul>";
			 		dojo.byId("list-of-deps").innerHTML =  deptsAutoDiv;
			 		
			 		dojo.forEach(dojo.query("#list-of-deps li"), function (elm, i) {
			 	        dojo.connect(elm, "onclick", function (evt) {
			 	        	departFrom.value = this.innerText || this.textContent;
			 	            dojo.style(deprtsId, {"display": "none"});
			 	           //this.innerText = "";
			 	            saerchTypes(departFrom.id);
			 	        });
			 	    });
			 		_searchDeptCodes("", eventHandle.browserMode(), 0);
		 		}
		 		else{ 
		 			//alert(url)
		 			
		 			var atoSuggestdepts = jsonData.departureAutoSuggestData;
		 			var deptsAutoDiv = "<ul>";
		 			var UKList = "";
			 		var NonUKList = "";
			 		for(var i = 0; i < atoSuggestdepts.length; i++){
			 			if(atoSuggestdepts[i].depAirPortCountryCode == "GBR")
			 				UKList+= '<li>'+atoSuggestdepts[i].depAirPortInfo+'</li>';
			 			else{
			 				NonUKList+= '<li>'+atoSuggestdepts[i].depAirPortInfo+'</li>';
			 			}
			 			//deptsAutoDiv+= "<li>"+atoSuggestdepts[i].depAirPortInfo;+"</li>";
			 		}
			 		deptsAutoDiv+=UKList+NonUKList;
			 		deptsAutoDiv+="</ul>";
			 		dojo.byId("list-of-deps").innerHTML =  deptsAutoDiv;
			 		
			 		dojo.forEach(dojo.query("#list-of-deps li"), function (elm, i) {
			 	        dojo.connect(elm, "onclick", function (evt) {
			 	        	departFrom.value = this.innerText || this.textContent;
			 	            dojo.style(deprtsId, {"display": "none"});
			 	            //this.innerText = "";
			 	            saerchTypes(departFrom.id);
			 	        });
			 	    });
		 		
			 		_searchDeptCodes("", eventHandle.browserMode(), 0);
		 		}
		 	}
		 	else if(fieldId == "arriveTo"){ 
		 		if(departFrom.value.length == 0){
			 		var arriveTo = dojo.byId("arriveTo");
				 	var arvlsId = dojo.byId("list-of-arrs");
			 		var arrAutodetails = jsonData.arrivalAutoSuggestData;
			 		var arrsAutoDiv = "<ul>";
			 		var UKList = "";
			 		var NonUKList = "";
			 		for(var i = 0; i < arrAutodetails.length; i++){
			 			if(arrAutodetails[i].arrAirPortCountryCode == "GBR")
			 				UKList+= '<li>'+arrAutodetails[i].arrAirPortInfo+'</li>';
			 			else{
			 				NonUKList+= '<li>'+arrAutodetails[i].arrAirPortInfo+'</li>';
			 			}
			 			//arrsAutoDiv+= "<li>"+arrAutodetails[i].arrAirPortInfo+"</li>";
			 		}
			 		arrsAutoDiv+=UKList+NonUKList;
			 		arrsAutoDiv+= "</ul>";
			 		dojo.byId("list-of-arrs").innerHTML = arrsAutoDiv;
			 		
			 		dojo.forEach(dojo.query("#list-of-arrs li"), function (elm, i) {
			 	        dojo.connect(elm, "onclick", function (evt) {
			 	        	arriveTo.value = this.innerText || this.textContent;
			 	            dojo.style(arvlsId, {"display": "none"});
			 	            //this.innerText = "";
			 	             saerchTypes(arriveTo.id);
			 	        });
			 		});
			 		_searchArrCodes("", window.Event || event, 0);
		 		}
		 		else{
		 			
		 			var atoSuggestArvls = jsonData.arrivalAutoSuggestData;
		 			var arrsAutoDiv = "<ul>";
		 			var UKList = "";
			 		var NonUKList = "";
			 		for(var i = 0; i < atoSuggestArvls.length; i++){
			 			if(atoSuggestArvls[i].arrAirPortCountryCode == "GBR")
			 				UKList+= '<li>'+atoSuggestArvls[i].arrAirPortInfo+'</li>';
			 			else{
			 				NonUKList+= '<li>'+atoSuggestArvls[i].arrAirPortInfo+'</li>';
			 			}
			 			//arrsAutoDiv+= "<li>"+atoSuggestArvls[i].arrAirPortInfo;+"</li>";
			 		}
			 		arrsAutoDiv+=UKList+NonUKList;
			 		arrsAutoDiv+="</ul>";
			 		dojo.byId("list-of-arrs").innerHTML =  arrsAutoDiv;
			 		
			 		dojo.forEach(dojo.query("#list-of-arrs li"), function (elm, i) {
			 	        dojo.connect(elm, "onclick", function (evt) {
			 	        	arriveTo.value = this.innerText || this.textContent;
			 	            dojo.style(arvlsId, {"display": "none"});
			 	            //this.innerText = "";
			 	            saerchTypes(arriveTo.id);
			 	        });
			 	    });
		 		
			 		_searchArrCodes("", window.Event || event, 0);
		 		}
		 	}
        	
			else{ 
			 	var flightTodayImg = dojo.byId("flightTodayImg");
	            dojo.style(flightTodayImg, {"display": "none"});
	            var flightlabel = dojo.byId("flightlabel");
	            dojo.style(flightlabel, {"display": "none"});
	            dojo.style(dojo.byId("displayer"), {"display": "none"});
	            dojo.style(dojo.byId("displayer1"), {"display": "none"});
	            
                dojo.query("tr[role=data-flight-set]").remove();
	               
	            var errorpopup = dojo.byId("errorpopup");
		        var todayFlights = jsonData.todaysFlightStatusData;
	        	var tomrwFlights = jsonData.tomorrowsFlightStatusData;
	        	dojo.style(errorpopup, {"display": "none"});
	        	if ((todayFlights == null || todayFlights.length == 0) && (tomrwFlights == null || tomrwFlights.length == 0)) {
	            	dojo.style(errorpopup, {"display": "inline-block"});
            		dojo.addClass(dojo.byId("readOnlyTom"),"error");
            		dojo.addClass(dojo.byId("flightnumber"),"error");
            		dojo.style(dojo.byId("flightnumber"), {"width":"71.3%"});
            		dojo.style(dojo.byId("readOnlyTom"), {
            			"color": "white",
            			"border": "1px solid rgb(139, 188, 227)"
            				
            		});
            		dojo.style(dojo.byId("data-grid-today-flights"), {"display": "none"})
            		dojo.style(dojo.byId("data-grid-tomorrow-flights"), {"display": "none"})
            		
            		//dojo.style(dojo.byId("HdrLbls"), {"display": "none"});
	                isFlightFound = false;
	                dojo.forEach(dojo.query(".sometext"), function(e){
	                	dojo.style(e, {"display":"none"});
	                });
	                dojo.forEach(dojo.query(".flight-results"), function(e){
	                	dojo.style(e, {"display":"none"});
	                });
	                dojo.forEach(dojo.query(".div1"), function(e){
	                	dojo.style(e, {"display":"none"});
	                });
	                var flightTodayImg = dojo.byId("flightTodayImg");
		            dojo.style(flightTodayImg, {"display": "none"});
		            var flightlabel = dojo.byId("flightlabel");
	                dojo.style(flightlabel, {"display": "none"});
	                dojo.style(dojo.byId("displayer"), {"display": "none"});
	                dojo.style(dojo.byId("displayer1"), {"display": "none"});
	                return;
	               
	            	}
	        	else{
	                dojo.style(errorpopup, {"display": "none"});
	                var flightTodayImg = dojo.byId("flightTodayImg");
		            dojo.style(flightTodayImg, {"display": "inline-block"});
		            var flightlabel = dojo.byId("flightlabel");
	                dojo.style(flightlabel, {"display": "inline-block"});
            	}
//************Today Flights**************		
	            for (var i = 0; i < todayFlights.length; i++) {
	            	
                    var flightTodayImg = dojo.byId("flightTodayImg");
		            dojo.style(flightTodayImg, {"display": "inline-block"});
		            var flightlabel = dojo.byId("flightlabel");
	                dojo.style(flightlabel, {"display": "inline-block"});
	                dojo.forEach(dojo.query(".flight-results"), function(e){
	                	dojo.style(e, {"display":"inline-block"});
	                });
	                
	                flag=true;
	                var splitTime = todayFlights[i].schdeptm;
	                var strs = addColons(splitTime);
	                strs = strs.substring(0, 5);
	                
	                var splitTime1 = todayFlights[i].scharrtm;
	                var strs1 = addColons(splitTime1);
	                var imgClass = "";
	                strs1 = strs1.substring(0, 5);
	                
	                var departStatus = todayFlights[i].depstatus;
	                
	                var flightNumberSpaced = todayFlights[i].flightnr.split('TOM').join('TOM ');
	                var boldStyle4deps = "";
	                var boldStyle4arvs = ""; 
	                var airportNameCodeCountry = "";
	                
	                var depAirportNameCodeCountry ="";
		               var arrAirportNameCodeCountry="";
		                	if(todayFlights[i].depAirCountryName.toLowerCase() == "United kingdom".toLowerCase()){
				 				depAirportNameCodeCountry = todayFlights[i].depAirportName+" ("+todayFlights[i].depAirpotCode+")";
				 				}//if
				 			else{
				 				depAirportNameCodeCountry =  todayFlights[i].depAirportName+" ("+todayFlights[i].depAirpotCode+") <br>"+todayFlights[i].depAirCountryName;
				 			}//else
			            	if(todayFlights[i].arrAirCountryName.toLowerCase() == "United kingdom".toLowerCase()){
				 				arrAirportNameCodeCountry = todayFlights[i].arrAirportName+" ("+todayFlights[i].arrAirpotCode+")";
				 				}//if
				 			else{
				 				arrAirportNameCodeCountry =  todayFlights[i].arrAirportName+" ("+todayFlights[i].arrAirpotCode+") <br>"+todayFlights[i].arrAirCountryName;
				 			}//else
                	
	                if(url.split("/")[0] == "allDepartures" || url.split("/")[0] == "searchByDeparture"){
	                	boldStyle4deps = " font-weight:bold;color: #333; ";
	                	if(url.split("/")[0] == "allDepartures"){
	                		dojo.query("#flightlabel").innerHTML("All Departures");
	                	}	
	                	else{
	                		dojo.query("#flightlabel").innerHTML("Flights Departing from "+todayFlights[i].depAirportName+" ("+todayFlights[i].depAirpotCode+")");
	                	}
	                	
	                	
	                }	
	                else if(url.split("/")[0] == "allArrivals" || url.split("/")[0] == "searchByArrival"){
	                	var departStatus = todayFlights[i].arrstatus;
	                	boldStyle4arvs = " font-weight:bolder;color: #333; ";
	                	if(url.split("/")[0] == "allArrivals"){
	                		dojo.query("#flightlabel").innerHTML("All Arrivals");
	                	}	
	                	else{
	                		dojo.query("#flightlabel").innerHTML("Flights Arriving to "+todayFlights[i].arrAirportName+" ("+todayFlights[i].arrAirpotCode+")");
	                	}
	                	
	                }
	                else if(url.split("/")[0] == "searchByFlightNumber"){ 
	                	if(todayFlights[i].schdepdt == today){
	                		boldStyle4deps = " font-weight:bolder;color: #333; ";
	                	}else if(todayFlights[i].scharrdt == today){
	                		var departStatus = todayFlights[i].arrstatus;
		                	boldStyle4arvs = " font-weight:bolder;color: #333; ";
	                	}
	                	
	                	dojo.query("#flightlabel").innerHTML("Flight " + flightNumberSpaced);
	                }
	                else if(url.split("/")[0] == "searchByAll"){
	                	
	                	if(url.split("/")[4] == "departFrom"){
		                	boldStyle4deps = " font-weight:bolder;color: #333; ";
	                		dojo.query("#flightlabel").innerHTML("Flights Departing from "+todayFlights[i].depAirportName+" ("+todayFlights[i].depAirpotCode+")"+" to "+todayFlights[i].arrAirportName+" ("+todayFlights[i].arrAirpotCode+")");
	                	}
	                	else if(url.split("/")[4] == "arriveTo"){
		                	var departStatus = todayFlights[i].arrstatus;
		                	boldStyle4arvs = " font-weight:bolder;color: #333; ";
		                	dojo.query("#flightlabel").innerHTML("Flights Arriving to "+todayFlights[i].arrAirportName+" ("+todayFlights[i].arrAirpotCode+")"+" from "+todayFlights[i].depAirportName+" ("+todayFlights[i].depAirpotCode+")");
	                	}else if(url.split("/")[4] == "flightnumber"){
			                		if(todayFlights[i].schdepdt == today){
				                		boldStyle4deps = " font-weight:bolder;color: #000; ";
				                	}else if(todayFlights[i].scharrdt == today){
				                		var departStatus = todayFlights[i].arrstatus;
					                	boldStyle4arvs = " font-weight:bolder;color: #000; ";
				                	}
				                	
				                	dojo.query("#flightlabel").innerHTML("Flight " + flightNumberSpaced);
			                		
			                	}
	                	
	                }
	                
	                //imageClass
	                var strstemp=strs;
	                var strs1temp =strs1;
	                var statusColor = "";
	                if(departStatus == "DELAYED" || departStatus == "RESCHEDULED"){
	                	imgClass = "newimg1";//"dldOrRsdmg";
	                	strstemp= strs+"<span id='estimate'>Estimated "+addColons(todayFlights[i].expdeptm).substring(0,5)+"</span>";
	                	strs1temp=strs1+"<span id='estimate'>Estimated "+addColons(todayFlights[i].exparrtm).substring(0,5)+"</span>";
						statusColor ='color: #e04b43;';			                }
					else if(departStatus == "AS SCHEDULED"){
						imgClass = "asScheduled";
						statusColor ='color: #9b9b9b;';
					}
				
	                else{
	                	imgClass = "newimg";//"infOrBrdImg";
	                	statusColor ='color: #a0c800;';
	                	 if(todayFlights[i].actdeptm.length >0 && 
	                			 todayFlights[i].actdeptm != todayFlights[i].schdeptm){
			                	strstemp=strs+"<span id='estimate'>Actual "+addColons(todayFlights[i].actdeptm).substring(0,5)+"</span>";
			                }
			                if(todayFlights[i].actarrtm.length >0 && 
		                			 todayFlights[i].actarrtm != todayFlights[i].scharrtm){
			                	strs1temp=strs1+"<span id='estimate'>Actual "+addColons(todayFlights[i].actarrtm).substring(0,5)+"</span>";
			                }
	                }        
	                
	                strs=strstemp;
	                strs1=strs1temp;
	                departStatus = initCapString(departStatus); 
	                
	                tableRow = '<tr class="data" role="data-flight-set"><td>'
					+ flightNumberSpaced
					+ '</td><td>'
					+ depAirportNameCodeCountry
					+ '</td><td style="font-size: 22.5px;color: #333;width:168px;'
					+ boldStyle4deps
					+ '">'
					+ strs
					+ '<input type="hidden" value="'
					+ splitTime
					+ '" /></td><td style="width: 130px;width: 205px; ">'
					+ arrAirportNameCodeCountry
					+ '</td><td style="font-size: 25px;color: #666;padding-left: 0px; '
					+ boldStyle4arvs
					+ '">'
					+ strs1
					+ '<input type="hidden" value="'
					+ splitTime1
					+ '" /></td><td style="'+statusColor+'"><span class="flightImg '
					+ imgClass
					+ ' style="margin-left: 0px;"></span>'
					+ departStatus
					+ '</td></tr>'; 
                	var tdy = dojo.byId("displayer");
                    var footerText = dojo.query(".sometext");
                    dojo.style(dojo.query(".underline"), {"display" : "block"});
                    dojo.style(footerText[0], {"display": "block"});
                    dojo.style(tdy, {"display": "inline-block"});
                    dojo.style(dojo.byId("data-grid-today-flights"), {
                    	"display":"table"
                    });
                    
                   if(tomrwFlights.length == 0){
	                    var tdy1 = dojo.byId("displayer1");
	                    dojo.style(tdy1, {"display": "none"});
	                    dojo.style(dojo.byId("data-grid-tomorrow-flights"), {"display": "none"});
                   }
                   dojo.query("table#data-grid-today-flights tbody").append(tableRow);
	            }//todayFor
	            dojo.style(dojo.byId("today-flight-results"), {
	            		"display":"block"
	            });

//************Tomorrow Flights**************		
				 		
	            for (var i = 0; i < tomrwFlights.length; i++) {
                    
                    var flightTodayImg = dojo.byId("flightTodayImg");
		            dojo.style(flightTodayImg, {"display": "inline-block"});
		            var flightlabel = dojo.byId("flightlabel");
	                dojo.style(flightlabel, {"display": "inline-block"});
	                
	                dojo.forEach(dojo.query(".flight-results"), function(e){
	                	dojo.style(e, {"display":"inline-block"});
	                });
	            	
	            	dojo.style(dojo.byId("displayer1"), {"display": "inline-block"});
		            flag=true;
	                var splitTime = tomrwFlights[i].schdeptm;
	                var strs = addColons(splitTime);
	                strs = strs.substring(0, 5);
	                var splitTime1 = tomrwFlights[i].scharrtm;
	                var strs1 = addColons(splitTime1);
	                var imgClass = "flightImg";
	                strs1 = strs1.substring(0, 5);
	                
	                //imgClass
	                var departStatus = tomrwFlights[i].depstatus;
	                
	                var flightNumberSpaced = tomrwFlights[i].flightnr.split('TOM').join('TOM ');
	                
	                //boldStyle
	                var boldStyle4deps = "";
	                var boldStyle4arvs = ""; 
                	
	                var depAirportNameCodeCountry ="";
		            var arrAirportNameCodeCountry="";
		                	if(tomrwFlights[i].depAirCountryName.toLowerCase() == "United kingdom".toLowerCase()){
				 				depAirportNameCodeCountry = tomrwFlights[i].depAirportName+" ("+tomrwFlights[i].depAirpotCode+")";
				 				}//if
				 			else{
				 				depAirportNameCodeCountry =  tomrwFlights[i].depAirportName+" ("+tomrwFlights[i].depAirpotCode+") <br>"+tomrwFlights[i].depAirCountryName;
				 			}//else
			            	if(tomrwFlights[i].arrAirCountryName.toLowerCase() == "United kingdom".toLowerCase()){
				 				arrAirportNameCodeCountry = tomrwFlights[i].arrAirportName+" ("+tomrwFlights[i].arrAirpotCode+")";
				 				}//if
				 			else{
				 				arrAirportNameCodeCountry =  tomrwFlights[i].arrAirportName+" ("+tomrwFlights[i].arrAirpotCode+") <br>"+tomrwFlights[i].arrAirCountryName;
				 			}//else

	                if(url.split("/")[0] == "allDepartures" || url.split("/")[0] == "searchByDeparture"){
	                	boldStyle4deps = " font-weight:bolder;color: #333; ";
	                	if(url.split("/")[0] == "allDepartures")
	                		dojo.query("#flightlabel").innerHTML("All Departures");
	                	else
	                		dojo.query("#flightlabel").innerHTML("Flights Departing from "+tomrwFlights[i].depAirportName+" ("+tomrwFlights[i].depAirpotCode+")");
	                	
	                }	
	                else if(url.split("/")[0] == "allArrivals" || url.split("/")[0] == "searchByArrival"){ 
	                	var departStatus = tomrwFlights[i].arrstatus;
	                	boldStyle4arvs = " font-weight:bolder;color: #333; ";
	                	if(url.split("/")[0] == "allArrivals"){
	                		dojo.query("#flightlabel").innerHTML("All Arrivals");
	                	}	
	                	else{
	                		dojo.query("#flightlabel").innerHTML("Flights Arriving to "+tomrwFlights[i].arrAirportName+" ("+tomrwFlights[i].arrAirpotCode+")");
	                	}

	                }
	                else if(url.split("/")[0] == "searchByFlightNumber"){
	                	if(tomrwFlights[i].schdepdt == tomday){
	                		boldStyle4deps = " font-weight:bolder;color: #000; ";
	                	}else if(tomrwFlights[i].scharrdt == tomday){
	                		var departStatus = tomrwFlights[i].arrstatus;
		                	boldStyle4arvs = " font-weight:bolder;color: #000; ";
	                	}
	                	dojo.query("#flightlabel").innerHTML("Flight " + flightNumberSpaced);
	                }
	                else if(url.split("/")[0] == "searchByAll"){
	                	
	                	if(url.split("/")[4] == "departFrom"){
		                	boldStyle4deps = " font-weight:bolder;color: #000; ";
	                		dojo.query("#flightlabel").innerHTML("Flights Departing from "+tomrwFlights[i].depAirportName+" ("+tomrwFlights[i].depAirpotCode+")"+" to "+tomrwFlights[i].arrAirportName+" ("+tomrwFlights[i].arrAirpotCode+")");
	                	}
	                	else if(url.split("/")[4] == "arriveTo"){
		                	var departStatus = tomrwFlights[i].arrstatus;
		                	boldStyle4arvs = " font-weight:bolder;color: #000; ";
		                	dojo.query("#flightlabel").innerHTML("Flights Arriving to "+tomrwFlights[i].arrAirportName+" ("+tomrwFlights[i].arrAirpotCode+")"+" from "+tomrwFlights[i].depAirportName+" ("+tomrwFlights[i].depAirpotCode+")");
	                	}else if(url.split("/")[4] == "flightnumber"){
				                	if(tomrwFlights[i].schdepdt == tomday){
				                		boldStyle4deps = " font-weight:bolder;color: #000; ";
				                	}else if(tomrwFlights[i].scharrdt == tomday){
				                		var departStatus = tomrwFlights[i].arrstatus;
					                	boldStyle4arvs = " font-weight:bolder;color: #000; ";
				                	}
				                	dojo.query("#flightlabel").innerHTML("Flight " + flightNumberSpaced);
			                		
			                	}
	                	
	                }

	                	
	                //imageClass
	                var strstemp=strs;
	                var strs1temp =strs1;
	                var statusColor = "";
	                if(departStatus == "DELAYED" || departStatus == "RESCHEDULED"){
	                	imgClass = "newimg1";//"dldOrRsdmg";
	                	strstemp= strs+"<span id='estimate'>Estimated "+addColons(tomrwFlights[i].expdeptm).substring(0,5)+"</span>";
	                	strs1temp=strs1+"<span id='estimate'>Estimated "+addColons(tomrwFlights[i].exparrtm).substring(0,5)+"</span>";
						statusColor ='color: #e04b43;';
	                }
	                else if(departStatus == "AS SCHEDULED"){
	                	imgClass = "asScheduled";
	                	statusColor ='color: #9b9b9b;';
	                }
	                else{
	                	imgClass = "newimg";//"infOrBrdImg";
	                	 statusColor ='color: #a0c800;';
	                	 if(tomrwFlights[i].actdeptm.length >0 && 
	                			 tomrwFlights[i].actdeptm != tomrwFlights[i].schdeptm){
			                	strstemp=strs+"<span id='estimate'>Actual "+addColons(tomrwFlights[i].actdeptm).substring(0,5)+"</span>";
			                }
			                if(tomrwFlights[i].actarrtm.length >0 && 
		                			 tomrwFlights[i].actarrtm != tomrwFlights[i].scharrtm){
			                	strs1temp=strs1+"<span id='estimate'>Actual "+addColons(tomrwFlights[i].actarrtm).substring(0,5)+"</span>";
			                }
	                }        
	                strs=strstemp;
	                strs1=strs1temp;	
	                departStatus = initCapString(departStatus); 
				    tableRow = '<tr class="data" role="data-flight-set"><td>'
						+ flightNumberSpaced
						+ '</td><td>'
						+ depAirportNameCodeCountry
						+ '</td><td style="font-size: 25px;color: #666;width:168px;'
						+ boldStyle4deps
						+ '">'
						+ strs
						+ '<input type="hidden" value="'
						+ tomrwFlights[i].schdeptm
						+ '" /></td><td style="width: 130px;width: 205px; ">'
						+ arrAirportNameCodeCountry
						+ '</td><td style="font-size: 25px;color: #666;padding-left: 0px; '
						+ boldStyle4arvs
						+ '">'
						+ strs1
						+ '<input type="hidden" value="'
						+ tomrwFlights[i].scharrtm
						+ '" /></td><td style="'+statusColor+'"><span class="'
						+ imgClass
						+ '" style="margin-left: 0px;"></span>'
						+ departStatus
						+ '</td></tr>';			                    var tdy1 = dojo.byId("displayer1");
                    var footerText = dojo.query(".sometext");
                    dojo.style(dojo.query(".underline"), {"display" : "block"});
                    dojo.style(footerText[0], {"display": "block"});
                    dojo.style(tdy1, {"display": "inline-block"});
                    dojo.style(dojo.byId("data-grid-tomorrow-flights"), {
                    	"display":"table"
                    });
                    if(todayFlights.length == 0){
	                    var tdy1 = dojo.byId("displayer");
	                    dojo.style(tdy1, {"display": "none"});
	                    dojo.style(dojo.byId("data-grid-today-flights"), {"display": "none"});
                    }
                    dojo.query("table#data-grid-tomorrow-flights tbody").append(tableRow);
	            }//tomorrow for
			            
	            dojo.style(dojo.byId("tomorrow-flight-results"), {"display":"block"});
	            dojo.query("#lastUpdtime").innerHTML("<b>Last updated: "+jsonData.currentFeedtime+"  GMT Standard Time</b>");	            
	            if (flag) {
	                var flightlabel = dojo.byId("flightlabel");
	                var flightTodayImg = dojo.byId("flightTodayImg");
	                var flightresults = dojo.query(".flight-results");
	                dojo.style(flightresults, {"display": "inline-block"});
	                dojo.style(flightlabel, {"display": "inline-block"});
	                if(url.split("/")[0] == "searchByAll"){
	                	
	                if(url.split("/")[4] == "departFrom"){
	                		dojo.style(dojo.byId("arrImg"), {"display": "none"});
		                	dojo.style(flightTodayImg, {"display": "inline-block"});
	                	}
	                	else if(url.split("/")[4] == "arriveTo"){
	                		dojo.style(flightTodayImg, {"display": "none"});
		                	dojo.style(dojo.byId("arrImg"), {"display": "inline-block"});
		                	
	                	}else if(url.split("/")[4] == "flightnumber"){
	                		dojo.style(dojo.byId("arrImg"), {"display": "none"});
		                	dojo.style(flightTodayImg, {"display": "inline-block"});  	
	                	}
	                }
	                else if(url.split("/")[0] == "allArrivals" || url.split("/")[0] == "searchByArrival"){ 
	                	dojo.style(flightTodayImg, {"display": "none"});
	                	dojo.style(dojo.byId("arrImg"), {"display": "inline-block"});
	                }
	                else{
	                	dojo.style(dojo.byId("arrImg"), {"display": "none"});
	                	dojo.style(flightTodayImg, {"display": "inline-block"});
	                }
	                flag = false;
	            } 
	            else if (flvalue.length === 3) {
		            var flightTodayImg = dojo.byId("flightTodayImg");
		            dojo.query("#flightlabel").innerHTML("");
		            dojo.style(flightTodayImg, {"display": "none"});
	            }
		 }//else         
		            
		 }//after results
	}//utils

/************************************** start of pull down menu  *******************************/
 
var idx = 0;
var departFrom = dojo.byId("departFrom");
var deprtsId = dojo.byId("list-of-deps");

var arriveTo = dojo.byId("arriveTo");
var arvlsId = dojo.byId("list-of-arrs");
    
dojo.connect(departFrom, "onkeypress", function (evt) {
        if (evt.keyCode === 40 || evt.keyCode === 38) {
            evt.preventDefault();
        } else if (evt.keyCode == 13) {
        	if(departFrom.value.length == 0) return;
        	departFrom.value = dojo.query("li[class='dept-code-li-visible hlight']").text() || dojo.query("li[class='hlight dept-code-li-visible']").text();
            dojo.style(deprtsId, { "display": "none" });
        }
});
 

dojo.connect(deprtsId, "onmouseover", function (evt) {
    dojo.query("#list-of-deps li.hlight").removeClass("hlight");
});



/*else if((evt.keyCode >= 65 && evt.keyCode <= 90) ||
		(evt.keyCode >= 97 && evt.keyCode <= 122)){
	//for checking only alphabets on caps or small
}
*/
dojo.connect(departFrom, "onkeyup", function (evt) {
	if(evt.keyCode == 13){
		 dojo.style(dojo.byId("errorpopupDep"),{"display": "none"});
		 dojo.removeClass(dojo.byId("departFrom"), "error");
		return;
	}else if (dojo.trim(departFrom.value) === "") {
        dojo.style(deprtsId, {"display": "none"});
        dojo.style(dojo.byId("errorpopupDep"),{"display": "none"});
        dojo.removeClass(dojo.byId("departFrom"), "error");
    }else {
        if (evt.keyCode == 40 && idx < dojo.query("#list-of-deps li.dept-code-li-visible").length - 1) {
            idx++;
        } else if (evt.keyCode == 38 && idx > 0) {
            idx--;
        } else if (evt.keyCode !== 40 && evt.keyCode !== 38) {
            idx = 0;
        }
        
        if(departFrom.value.length > 2 ){
	       dojo.style(deprtsId, {"display": "inline-block"});
	       _searchDeptCodes(departFrom.value, eventHandle.browserMode(), idx);
        }else{
        	dojo.style(dojo.byId("errorpopupDep"),{"display": "none"});	
        	dojo.removeClass(dojo.byId("departFrom"), "error");
        }
    }
    
});

var _searchDeptCodes = function (inputVal, e, idx) { 
	var list = dojo.query('#list-of-deps'),
    visibleIdx = 0;
	var count = 0;
	var count2 = 0;
	if (idx === undefined) {idx = 0;}
	//if(inputVal.indexOf("-") >= 0) return;
    dojo.forEach(list.query('li'), function (elm, i) { 
        var found = false;
        var regExp = new RegExp(inputVal, 'i');
        var elmText = elm.innerText || elm.textContent;
        if (regExp.test(elmText)) {
        	count2++; 
        	dojo.addClass(elm, "dept-code-li-visible");
        	dojo.style(dojo.byId("errorpopupDep"),{"display": "none"});
        	dojo.removeClass(dojo.byId("departFrom"), "error");
        	dojo.style(dojo.byId("departFrom"),{"borderLeftWidth": "5px"});
            if (visibleIdx === idx) {
            	count = 1;
                dojo.addClass(elm, "hlight");
            } else {
                dojo.removeClass(elm, "hlight");
            }
            visibleIdx++;
        } else {
        	dojo.removeClass(elm, "dept-code-li-visible");
        }
    });
    
    if(count == 0 ){
    	dojo.style(dojo.byId("errorpopupDep"),{"display": "block"});
    	dojo.style(dojo.byId("departFrom"),{"borderLeftWidth": "0px"});
    	dojo.addClass(dojo.byId("departFrom"), "error");
    	count = 1;
    	dojo.query("#list-of-deps")[0].style.height="0px";
    	return;
    	
    }
    else{
    	var scrollHt = count2*25;//alert(scrollHt)
    	var pix =  scrollHt+"px";
    	dojo.query("#list-of-deps")[0].style.height=pix;
    }
   
}


/*dojo.connect(dojo.byId("depArrowImg"), "onclick", function (e) {

});
*/

/************************************** end of pull down menu  *******************************/


/************************************** start of arrival pull down menu  *******************************/

var idxarr = 0;
var departFrom = dojo.byId("departFrom");
var deprtsId = dojo.byId("list-of-deps");

var arriveTo = dojo.byId("arriveTo");
var arvlsId = dojo.byId("list-of-arrs");
    
dojo.connect(arriveTo, "onkeypress", function (evt) {
        if (evt.keyCode === 40 || evt.keyCode === 38) {
            evt.preventDefault();
        } else if (evt.keyCode == 13) {
        	if(arriveTo.value.length == 0) return;
        	arriveTo.value = dojo.query("li[class='arvl-code-li-visible arrHlight']").text() || dojo.query("li[class='arrHlight arvl-code-li-visible']").text();
            dojo.style(arvlsId, {"display": "none"});
        }
});
   
dojo.connect(arvlsId, "onmouseover", function (evt) {
    dojo.query("#list-of-arrs li.arrHlight").removeClass("arrHlight");
});


dojo.connect(arriveTo, "onkeyup", function (evt) {
	if(evt.keyCode == 13){
		 dojo.style(dojo.byId("errorpopupArr"),{"display": "none"});
		 dojo.removeClass(dojo.byId("arriveTo"), "error");
		return;
	}
	else if (dojo.trim(arriveTo.value) === "") {
        dojo.style(arvlsId, {"display": "none"});
        dojo.style(dojo.byId("errorpopupArr"),{"display": "none"});
        dojo.removeClass(dojo.byId("arriveTo"), "error");
    }else {
        if (evt.keyCode == 40 && idxarr < dojo.query("#list-of-arrs li.arvl-code-li-visible").length - 1) {
        	idxarr++;
        } else if (evt.keyCode == 38 && idxarr > 0) {
        	idxarr--;
        } else if (evt.keyCode !== 40 && evt.keyCode !== 38) {
        	idxarr = 0;
        }
        if(arriveTo.value.length > 2){ 
 	       dojo.style(arvlsId, {"display": "inline-block"});
        	_searchArrCodes(arriveTo.value, evt, idxarr);
        }else{
        	dojo.style(dojo.byId("errorpopupArr"),{"display": "none"});	
        	dojo.removeClass(dojo.byId("arriveTo"), "error");
        }
    }

});


var _searchArrCodes = function (inputVal, e, idxarr) {
	
	var list = dojo.query('#list-of-arrs'),
    visibleIdx = 0;
	var count = 0;
	var count2 = 0;
    if (idxarr === undefined) {idxarr = 0;}
    
    //if(inputVal.indexOf("-") >= 0) return;	
    dojo.forEach(list.query('li'), function (elm, i) { 
    	
        var found = false;
        var regExp = new RegExp(inputVal, 'i');
        var elmText = elm.innerText || elm.textContent;
        if (regExp.test(elmText)) { 
        	count2++;
            dojo.addClass(elm, "arvl-code-li-visible");
            dojo.style(dojo.byId("errorpopupArr"),{"display": "none"});
            dojo.removeClass(dojo.byId("arriveTo"), "error");
            dojo.style(dojo.byId("arriveTo"),{"borderLeftWidth": "5px"});
            if (visibleIdx === idxarr) {
            	count = 1;
                dojo.addClass(elm, "arrHlight");
            } else {
                dojo.removeClass(elm, "arrHlight");
            }
            visibleIdx++;
        } else {
            dojo.removeClass(elm, "arvl-code-li-visible");
           // dojo.style(dojo.byId("errorpopupArr"),{"display": "block"});
        }
    });
    if(count == 0 ){
    	dojo.style(dojo.byId("errorpopupArr"),{"display": "block"});
    	dojo.style(dojo.byId("arriveTo"),{"borderLeftWidth": "0px"});
    	dojo.addClass(dojo.byId("arriveTo"), "error");
    	dojo.query("#list-of-arrs")[0].style.height="0px";
    	count = 1;
    	return;
    }
    else{
    	var scrollHt = count2*25;//alert(scrollHt)
    	var pix =  scrollHt+"px";
    	dojo.query("#list-of-arrs")[0].style.height=pix;
    }
}
/*dojo.connect(dojo.byId("arrArrowImg"), "onclick", function (e) {
	
});
*/

/************************************** end of arrival pull down menu  *******************************/

dojo.connect(dojo.byId("departFrom"), "onblur", function (e) {
	dojo.style(dojo.byId("departFrom"), {
		borderLeftWidth: "0px",
		width:"84%"
	});
});
dojo.connect(dojo.byId("arriveTo"), "onblur", function (e) {
	dojo.style(dojo.byId("arriveTo"), {
		borderLeftWidth: "0px",
		width:"84%"
	});
});

/********************************* on click events**************/
/*dojo.connect(dojo.byId("allDeps"), "onclick", function (e) {
	
});*/
/*dojo.connect(dojo.byId("allArvs"), "onclick", function (e) {

});*/
dojo.connect(dojo.query(".dept-code-li-visible"), "onclick", function (e) {
	departFrom.value = departFrom.value.split("(")[1].split(")")[0];
	utils.requestDa.url = "searchByDeparture/"+departFrom;
	utils.requestDa.callJson();
	utils.requestDa.url = "";
});
dojo.connect(dojo.query(".arvl-code-li-visible"), "onclick", function (e) {
	arriveTo.value = arriveTo.value.split("(")[1].split(")")[0];
	utils.requestDa.url = "searchByArrival/"+arriveToValue;
	utils.requestDa.callJson();
	utils.requestDa.url = "";
});

dojo.connect(document, "onclick", function (e) {
	if(dojo.style(dojo.byId("errorpopup")).display == "block"){
		dojo.style(dojo.byId("errorpopup"),{"display": "none"});
		dojo.byId("flightnumber").value = "";
		dojo.style(dojo.byId("readOnlyTom"), {"display": "none"});
		dojo.style(dojo.byId("flightnumber"), {"width": "84%"});
		dojo.removeClass(dojo.byId("flightnumber"), "error");
		dojo.removeClass(dojo.byId("readOnlyTom"), "error");
		//dojo.addClass(dojo.byId("readOnlyTom"), "readOnlyTom");
	}
	if(dojo.style(dojo.byId("errorpopupDep")).display == "block"){
		dojo.style(dojo.byId("errorpopupDep"),{"display": "none"});
		dojo.removeClass(dojo.byId("departFrom"), "error");
		dojo.byId("departFrom").value = "";
	}
	if(dojo.style(dojo.byId("errorpopupArr")).display == "block"){
		dojo.style(dojo.byId("errorpopupArr"),{"display": "none"});
		dojo.removeClass(dojo.byId("arriveTo"), "error");
		dojo.byId("arriveTo").value = "";
	}
	if(e.target.id != "depArrowImg"){	
		dojo.style(dojo.byId("list-of-deps"), {
	        "display": "none"
	    });
	}
	if(e.target.id != "arrArrowImg"){	
		dojo.style(dojo.byId("list-of-arrs"), {
	        "display": "none"
	    });
	}
	if(e.target.id == "flightnumber"){
		onFlightNumberFocus();
	}
	else if(e.target.id == "departFrom"){
		//dojo.style(dojo.byId("list"),{"display":"none"});
		onFlightNumberBlur();
		dojo.style(dojo.byId("arriveTo"), {"border": "1px solid rgb(139, 188, 227)"});
		dojo.style(dojo.byId("departFrom"), {
			borderLeftColor: "rgb(250, 174, 0)",
			borderLeftWidth: "5px",
			borderLeftStyle: "solid",
			width:"82.7%"
		});
		var flvalue = "TOM"+dojo.trim(dojo.byId("flightnumber").value);
		var url="";
		var fieldId=""; 
		if(arriveTo.value.length == 0 && flvalue.length <= 3){
			url = "departureAutoSuggestData/";
			fieldId = "departFrom";
		}else if(flvalue.length > 3){ 
			url="searchByFlightNumber/"+flvalue;
			fieldId = "departFrom";
		}
		else if(arriveTo.value.length != 0){
			if(arriveTo.value.indexOf("(") !== -1){
				url = "searchByArrival/"+arriveTo.value.split("(")[1].split(")")[0];
			}
			fieldId = "departFrom";
		}
		//dojo.style(deprtsId, { "display": "inline-block" });
		utils.requestDa.url = url;
		utils.requestDa.fieldId = fieldId;
		utils.requestDa.callJson();
		
	}
	else if(e.target.id == "arriveTo"){
		onFlightNumberBlur();
		dojo.style(dojo.byId("departFrom"), {"border": "1px solid rgb(139, 188, 227)"});
		dojo.style(dojo.byId("arriveTo"), {
			borderLeftColor: "rgb(250, 174, 0)",
			borderLeftWidth: "5px",
			borderLeftStyle: "solid",
			width:"82.3%"
		});
		var url="";
		var fieldId=""; 
		var flvalue = "TOM"+dojo.trim(dojo.byId("flightnumber").value);
		if(departFrom.value.length == 0 && flvalue.length <= 3){
			url = "arrivalAutoSuggestData/";
			fieldId = "arriveTo";
		}else if(flvalue.length > 3){ 
			url="searchByFlightNumber/"+flvalue;
			fieldId = "arriveTo";
		}else if(departFrom.value.length != 0){
			if(departFrom.value.indexOf("(") !== -1){
				url = "searchByDeparture/"+departFrom.value.split("(")[1].split(")")[0];
			}
			fieldId = "arriveTo";
		}
		//dojo.style(arvlsId, { "display": "inline-block" });
		utils.requestDa.url = url;
		utils.requestDa.fieldId = fieldId;
		utils.requestDa.callJson();
		utils.requestDa.url = "";	
	}
	else if(e.target.id == "depArrowImg"){
		onFlightNumberBlur();
		dojo.style(dojo.byId("departFrom"), {"borderLeftColor": "rgb(250, 174, 0)"});
		var url="";
		var fieldId=""; 
		var flvalue = "TOM"+dojo.trim(dojo.byId("flightnumber").value);
		if(dojo.trim(arriveTo.value) === "" && flvalue.length <= 3){
			url = "departureAutoSuggestData/";
			fieldId = "departFrom";
		}else if(flvalue.length > 3){ 
			url="searchByFlightNumber/"+flvalue;
			fieldId = "departFrom";
		}else if(arriveTo.value.length != 0){ 
			if(arriveTo.value.indexOf("(") !== -1){
				url = "searchByArrival/"+arriveTo.value.split("(")[1].split(")")[0];
			}
			fieldId = "departFrom";
		}
		
		dojo.style(deprtsId, { "display": "inline-block" });
		utils.requestDa.url = url;
		utils.requestDa.fieldId = fieldId;
		if(dojo.trim(utils.requestDa.url) !== ""){
			utils.requestDa.callJson();
		}
		departFrom.onFocus=true;
	}
	else if(e.target.id == "arrArrowImg"){
		onFlightNumberBlur();
		dojo.style(dojo.byId("arriveTo"), {"borderLeftColor": "rgb(250, 174, 0)"});
		var url="";
		var fieldId=""; 
		var flvalue = "TOM"+dojo.trim(dojo.byId("flightnumber").value);
		if(dojo.trim(departFrom.value) === "" && flvalue.length <= 3){
			url = "arrivalAutoSuggestData/";
			fieldId = "arriveTo";
		}else if(flvalue.length > 3){ 
			url="searchByFlightNumber/"+flvalue;
			fieldId = "arriveTo";
		}else{
			if(departFrom.value.indexOf("(") !== -1){
				url = "searchByDeparture/"+departFrom.value.split("(")[1].split(")")[0];
			}else{
				return;
			}
			fieldId = "arriveTo";
		}
		dojo.style(arvlsId, { "display": "inline-block" });
		utils.requestDa.url = url;
		utils.requestDa.fieldId = fieldId;
		utils.requestDa.callJson();
		utils.requestDa.url = "";
	}
	else if(e.target.id == "allDeps"){
		dojo.byId("flightnumber").value="";
		dojo.style(dojo.byId("readOnlyTom"), {"display": "none"});
		dojo.style(dojo.byId("flightnumber"), {"width": "84%"});
		dojo.byId("departFrom").value="";
		dojo.byId("arriveTo").value="";
		utils.requestDa.url = "allDepartures/";
		utils.requestDa.fieldId = "";
		utils.LOADING_PLACEHOLDER.innerHTML = utils.LOADING_ICON;
		dojo.forEach(dojo.query(".results"), function(elm, idx){
			dojo.style(elm, {
				"display":"none"
			})
		});
		utils.requestDa.callJson();
		utils.requestDa.url = "";
	}
	else if(e.target.id == "allArvs"){
		dojo.byId("flightnumber").value="";
		dojo.style(dojo.byId("readOnlyTom"), {"display": "none"});
		dojo.style(dojo.byId("flightnumber"), {"width": "260px"});
		dojo.byId("departFrom").value="";
		dojo.byId("arriveTo").value="";
		utils.requestDa.url = "allArrivals/";
		utils.requestDa.fieldId = "";
		utils.LOADING_PLACEHOLDER.innerHTML = utils.LOADING_ICON;
		dojo.forEach(dojo.query(".results"), function(elm, idx){
			dojo.style(elm, {
				"display":"none"
			})
		});
		utils.requestDa.callJson();
		utils.requestDa.url = "";	
	}
	else{
		onFlightNumberBlur();
	}
});

dojo.connect(dojo.byId("flightnumber"), "onblur", function (e) {
	dojo.style(dojo.byId("departFrom"), {"border": "1px solid rgb(139, 188, 227)"});
	dojo.style(dojo.byId("arriveTo"), {"border": "1px solid rgb(139, 188, 227)"});
	var flvalue = "TOM"+dojo.byId("flightnumber").value;
	if(flvalue.length == 3){
		dojo.style(dojo.byId("readOnlyTom"), {"display": "none"});
		dojo.style(dojo.byId("flightnumber"), {"width": "84%", "marginLeft": "7px", "border": "1px solid rgb(139, 188, 227)"});
	}
	else if(flvalue.length>3){
		if(dojo.style(dojo.byId("errorpopup")).display == "block"){
			dojo.style(dojo.byId("errorpopup"),{"display": "none"});
			dojo.byId("flightnumber").value = "";
			dojo.style(dojo.byId("readOnlyTom"), {"display": "none"});
			dojo.style(dojo.byId("flightnumber"), {"width": "84%"});
			dojo.removeClass(dojo.byId("flightnumber"),"error");
			dojo.removeClass(dojo.byId("readOnlyTom"), "error");
			return;
		}
		saerchTypes(dojo.byId("flightnumber").id);
	}
});

dojo.connect(dojo.byId("departFrom"), "onfocus", function (e) {
	dojo.byId("departFrom").placeholder = '';
});

dojo.connect(dojo.byId("departFrom"), "onblur", function (e) {
	dojo.byId("departFrom").placeholder = departOrArrFromPlaceHolder;
});

dojo.connect(dojo.byId("arriveTo"), "onfocus", function (e) {
	dojo.byId("arriveTo").placeholder = '';
});

dojo.connect(dojo.byId("arriveTo"), "onblur", function (e) {
	dojo.byId("arriveTo").placeholder = departOrArrFromPlaceHolder;
});

var onFlightNumberBlur = function(){
	dojo.style(dojo.byId("departFrom"), {"border": "1px solid rgb(139, 188, 227)"});
	dojo.style(dojo.byId("arriveTo"), {"border": "1px solid rgb(139, 188, 227)"});
	
	//Added by Arun to add the placeholder back to textbox when onblur
	dojo.byId("flightnumber").placeholder = flightNumberPlaceHolder;

	var flvalue = "TOM"+dojo.byId("flightnumber").value;
	if(flvalue.length == 3){
		dojo.style(dojo.byId("readOnlyTom"), {"display": "none"});
		dojo.style(dojo.byId("flightnumber"), {"width": "84%", "marginLeft": "7px", "border": "1px solid rgb(139, 188, 227)"});
	}
	else if(flvalue.length>3){
		if(dojo.style(dojo.byId("errorpopup")).display == "block"){
			dojo.style(dojo.byId("errorpopup"),{"display": "none"});
			dojo.byId("flightnumber").value = "";
			dojo.style(dojo.byId("readOnlyTom"), {"display": "none"});
			dojo.style(dojo.byId("flightnumber"), {"width": "84%"});
			dojo.removeClass(dojo.byId("flightnumber"),"error");
			dojo.removeClass(dojo.byId("readOnlyTom"), "error");
			return;
		}
		//saerchTypes(dojo.byId("flightnumber").id);
	}
	
}
var onFlightNumberFocus = function(){
	dojo.style(dojo.byId("departFrom"), {"border": "1px solid rgb(139, 188, 227)"});
	dojo.style(dojo.byId("arriveTo"), {"border": "1px solid rgb(139, 188, 227)"});
	
	//Added by Arun to clear the placeholder on textbox click
	dojo.byId("flightnumber").placeholder = ''
	
	 dojo.style(dojo.byId("readOnlyTom"), {
	        "display": "inline",
	        "width": "40px",
	        "left":"2px",
	        "color":"#aeaeae",
	        "borderLeft": "5px solid #FAAE00"
	    });
	    dojo.style(dojo.byId("flightnumber"), {
	    	"marginLeft": "-5px",
	        "width": "69.5%",
	        "position": "relative",
	        "borderLeftWidth": "0px"
	    });
	    dojo.style(dojo.byId("errorpopup"),{
	    	"display": "none"
		});
	    dojo.removeClass(dojo.byId("flightnumber"),"error");
		dojo.removeClass(dojo.byId("readOnlyTom"), "error");
}

var saerchTypes = function(targetId){
    
	var flvalue=dojo.trim(dojo.byId("flightnumber").value).replace(" ", "");
	if(flvalue != "tom") {
		  if ( flvalue.indexOf( "tom" ) > -1 ) {
	  		flvalue = flvalue.substring(3);		
	}
	}
	if(flvalue != "TOM") {
	   if ( flvalue.indexOf( "TOM" ) > -1 ) {
	 		flvalue = flvalue.substring(3);		
	   }
	}
		
	var arriveToValue = dojo.byId("arriveTo").value;
	if(arriveToValue.length!=0)	arriveToValue = arriveToValue.split("(")[1].split(")")[0];
	else arriveToValue = "nodata";
	
	var departFromValue = dojo.byId("departFrom").value;
	
	if(departFromValue.length!=0){
		departFromValue = departFromValue.split("(")[1].split(")")[0];
	}
	else departFromValue = "nodata";
	
	flvalue = "TOM"+flvalue;
	
	if(flvalue.length <= 3 ) flvalue = "nodata";

	if(flvalue=="nodata" && arriveToValue=="nodata" && departFromValue != "nodata"){//alert("searchByDeparture")
		url="searchByDeparture/"+departFromValue;
	}
	else if(flvalue=="nodata" && departFromValue=="nodata" && arriveToValue!="nodata"){//alert("searchByArrival")
		url = "searchByArrival/"+arriveToValue;
	}
	else if(arriveToValue=="nodata" && departFromValue=="nodata" && flvalue!="nodata"){//alert("searchByFlightNumber")
		url="searchByFlightNumber/"+flvalue;
	}
	else if(flvalue=="nodata" && arriveToValue=="nodata" && departFromValue == "nodata"){//alert("none")
		return;
	}
	else{ 
		url="searchByAll/"+flvalue+"/"+departFromValue+"/"+arriveToValue+"/"+targetId;
	}
	
	utils.requestDa.url = url;
	utils.requestDa.fieldId = "";
    utils.requestDa.callJson();
    utils.requestDa.url = "";
    dojo.byId("flightnumber").blur();
	
}
dojo.connect(document, "onkeyup", function (e) {
    if (e.keyCode == 13) {  
    	saerchTypes(e.target.id);   
    }	
    if(e.keyCode == 9 && dojo.byId("flightnumber").value.trim().length == 0) {
    	onFlightNumberBlur();
    }
    if(e.keyCode == 9){
    	dojo.style(dojo.byId("errorpopup"),{"display": "none"});
    }
    if (e.keyCode == 18) {  
    	onFlightNumberBlur();
    }	
    if(e.target.id =="departFrom"){
    	onFlightNumberBlur();
		dojo.style(dojo.byId("arriveTo"), {"border": "1px solid rgb(139, 188, 227)"});
		dojo.style(dojo.byId("departFrom"), {
			borderLeftColor: "rgb(250, 174, 0)",
			borderLeftWidth: "5px",
			borderLeftStyle: "solid",
			width:"82.7%"
		});
    }
    else if(e.target.id =="arriveTo"){
    	onFlightNumberBlur();
		dojo.style(dojo.byId("departFrom"), {"border": "1px solid rgb(139, 188, 227)"});
		dojo.style(dojo.byId("arriveTo"), {
			borderLeftColor: "rgb(250, 174, 0)",
			borderLeftWidth: "5px",
			borderLeftStyle: "solid",
			width:"82.3%"
		});
    }
    if(e.shiftKey && e.keyCode == 9) { 
    	if(dojo.byId("flightnumber").value.trim().length == 0) {
    		onFlightNumberFocus();
    	}
    	else if(e.target.id =="departFrom"){
        	onFlightNumberBlur();
    		dojo.style(dojo.byId("arriveTo"), {"border": "1px solid rgb(139, 188, 227)"});
    		dojo.style(dojo.byId("departFrom"), {
    			borderLeftColor: "rgb(250, 174, 0)",
    			borderLeftWidth: "5px",
    			borderLeftStyle: "solid",
    			width:"82.7%"
    		});
        }
        else if(e.target.id =="arriveTo"){
        	onFlightNumberBlur();
    		dojo.style(dojo.byId("departFrom"), {"border": "1px solid rgb(139, 188, 227)"});
    		dojo.style(dojo.byId("arriveTo"), {
    			borderLeftColor: "rgb(250, 174, 0)",
    			borderLeftWidth: "5px",
    			borderLeftStyle: "solid",
    			width:"82.3%"
    		});
        }
    	
    }
    

});
function initCapString(string){
	words = string.split(" ");
	var str1 = "";
	var str = "";
	for(var i = 0;i < words.length; i++){
		str = words[i].toLowerCase();
		str1+= str[0].toUpperCase() + str.slice(1)+" ";
	}
	return str1;
}

});
	});//dojo.onload
//dojoReady
}//onKeypress
