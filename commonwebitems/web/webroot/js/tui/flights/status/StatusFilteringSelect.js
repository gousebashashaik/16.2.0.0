define(["dojo/_base/declare",
        "tui/widget/form/flights/FilteringSelect"
        ],function(declare,FilteringSelect){
        declare("tui.flights.status.StatusFilteringSelect",[FilteringSelect], {

        	/** Properties Start**/


            doValidation: true, // overriden method for validate, whether todo validation or not

            counter : 0,

            postCreate: function(){
            	 this.inherited(arguments);
            },

            validate: function(){
            	var StatusFilteringSelect = this;
                if(this.doValidation){
                    this.inherited(arguments);
                    if(StatusFilteringSelect._opened){
                		StatusFilteringSelect.set("state","Incomplete");
                	}
                    if(this.state == "Error" && this.displayedValue.length > 2){
                    	this.onError();
                    } else {
                    	this.onNoError();
                    }
                } else {
                	this.onNoError();
                    return;
                }
            },

            onError: function(){
            	var StatusFilteringSelect = this;

            	if(StatusFilteringSelect.id == "flyFromStat"){
            		dojo.byId("errorpopupDep").style.display = "inline-block";
					dojo.query(".dijitInputContainer",StatusFilteringSelect.domNode).addClass("error");
            	}

            	if(StatusFilteringSelect.id == "flyToStat"){
            		dojo.byId("errorpopupArr").style.display = "inline-block";
					dojo.query(".dijitInputContainer",StatusFilteringSelect.domNode).addClass("error");
            	}

            	if(StatusFilteringSelect.id == "flightNumber"){
            		dojo.byId("errorpopup").style.display = "inline-block";
					dojo.query(".dijitInputContainer",StatusFilteringSelect.domNode).addClass("error");
            	}
            },

            onNoError: function(){
            	var StatusFilteringSelect = this;
            	if(StatusFilteringSelect.id == "flyFromStat"){
            		dojo.byId("errorpopupDep").style.display = "none";
    				dojo.query(".dijitInputContainer",StatusFilteringSelect.domNode).removeClass("error");
            	}

            	if(StatusFilteringSelect.id == "flyToStat"){
            		dojo.byId("errorpopupArr").style.display = "none";
    				dojo.query(".dijitInputContainer",StatusFilteringSelect.domNode).removeClass("error");
            	}

            	if(StatusFilteringSelect.id == "flightNumber"){
            		dojo.byId("errorpopup").style.display = "none";
					dojo.query(".dijitInputContainer",StatusFilteringSelect.domNode).removeClass("error");
            	}

            },

            onBlur: function() {
            	var StatusFilteringSelect = this;
            	if(StatusFilteringSelect.state == "Error" || StatusFilteringSelect.state == "Incomplete"){
	            	dojo.query(".tooltip.error",StatusFilteringSelect.domNode.parentNode).style("display","none");
	            	dojo.query(".dijitInputContainer",StatusFilteringSelect.domNode).removeClass("error");
	            	StatusFilteringSelect.set("displayedValue","");
            	}
            },

            _showResultList: function(){
            	var StatusFilteringSelect = this;
            	if(StatusFilteringSelect.state == "Error"){
	            	StatusFilteringSelect.set("displayedValue","");
            	}
            	StatusFilteringSelect.inherited(arguments);

            },

            _handleOnChange: function(/*anything*/ newValue, /*Boolean?*/ priorityChange){
            	this.inherited(arguments);
            	if((this.getValue() !== "" && this.counter==0) || this.getValue() == "0"){
            		this.onChange();
            		this.counter++;
            	}
            },
            _startSearch: function(key){
            	var _this = this;
            	if(key.indexOf("TOM") != -1 || key.indexOf("tom") != -1){
            		key = dojo.trim(key.substring(0,3)) + " " + dojo.trim(key.substring(3));
            	}
            	_this.inherited(arguments);
            }

    })
    return tui.flights.status.StatusFilteringSelect;
})