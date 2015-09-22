define("tui/cruise/sailingDates/view/SailingDatesComponent", [
    'dojo',
    'dojo/_base/declare',
    'dojo/text!tui/cruise/sailingDates/view/templates/SailingDatesComponentTmpl.html',
    'dojo/on',
    "dojo/dom-construct",
    "dojo/query",
    'dojo/dom-attr',
    "dojo/dom-style",
    "dojo/parser",
    "tui/searchGetPrice/view/GetPriceModal",
    "tui/mvc/Klass",
    "tui/widget/mixins/Templatable",
    'tui/widget/_TuiBaseWidget'], function (dojo, declare, sailingTmpl, on, domConstruct, query, domAttr, domStyle, parser, GetPriceModal, Klass) {
	return declare("tui.cruise.sailingDates.view.SailingDatesComponent", [tui.widget._TuiBaseWidget,  tui.widget.mixins.Templatable], {

		jsonData: null,
		tmpl: sailingTmpl,
		currentIndex: 0,
		includeAllData : false,
		isMultipleShips: false,

		// ----------------------------------------------------------------------------- methods
        postCreate: function () {
            var sailingDatesComp = this, sailingData = [], urlParams, shipCode, includeAllflag = true, includeAll, includeAllNode, dateRange;
            var sailingDatesRange = sailingDatesComp.jsonData.sailingDatesRange;
            sailingDatesComp.inherited(arguments);
            sailingDatesComp.isMultipleShips = sailingDatesComp.jsonData.mutlipleShip;
            sailingDatesComp.jsonData = sailingDatesComp.jsonData.durationFilterDatas;
            //Checking the date range is belongs to same month and year, e.g, 'Apr 2015' == 'Apr 2015'
            // If there is only one sailing for the itinerary, then show only one month like 'Sailings for MMM YYYY'.
            if( sailingDatesRange[0] == sailingDatesRange[1] ||
              ( sailingDatesComp.jsonData.length === 1 && sailingDatesComp.jsonData[0].sailingDatas.length === 1 ) )
            {
            	dateRange = sailingDatesRange[0];
            }else{
            	var startDate = sailingDatesRange[0].split(" ");
            	var endDate = sailingDatesRange[1].split(" ");
            	dateRange = startDate[0] + " - " + endDate[0] + " " + endDate[1];
            }
            dojo.byId("sailingDateRangeTitle").innerHTML = dateRange;
            if(sailingDatesComp.isMultipleShips) {
            	/** Multiple Ship Scenario */
            	urlParams = document.URL.split("?")[1];
                if (!urlParams) {
                	/** Multiple Ship Scenario
                	 *  CASE: No Ship selected
                	 */
                	sailingData.push(sailingDatesComp.jsonData[0]);
                    sailingDatesComp.renderTemplate(sailingData, includeAllflag);
                } else {
                	/** Multiple Ship Scenario
                	 *  CASE: Ship selected
                	 */
                	shipCode = urlParams.split("=")[1];
                	if(!shipCode){
                		return;
                	}
            		// "Include All other Ships" Scenario
            		 includeAllNode = query("span.include-all", sailingDatesComp.domNode)[0];
            		 domStyle.set(includeAllNode, "display", "inline-block");

                	 includeAll = query("a.view-all", sailingDatesComp.domNode)[0];
                	 on(includeAll, "click", function() {
                		 var data = [];
                     	includeAllflag = false;
                     	sailingDatesComp.includeAllData = true;
                     	data.push(sailingDatesComp.jsonData[sailingDatesComp.currentIndex]);
                     	sailingData = sailingDatesComp.buildSailingList(data, shipCode, sailingDatesComp.includeAllData);
                     	sailingDatesComp.renderTemplate(sailingData, includeAllflag);
                     	domStyle.set(includeAllNode, "display", "none");
                     });
                	sailingData = sailingDatesComp.buildSailingList(sailingDatesComp.jsonData, shipCode, sailingDatesComp.includeAllData);
                	sailingDatesComp.renderTemplate(sailingData, includeAllflag);
                }
            }else {
            	/** Single Ship Scenario
            	 * (default) Ship selected
            	 */
            	sailingData.push(sailingDatesComp.jsonData[0]);
                sailingDatesComp.renderTemplate(sailingData, includeAllflag);
            }

            dojo.subscribe("tui/cruise/sailingDates/view/SailingDatesComponent/updateSailingList", function (newValue, shipCode) {
            	var data = [];
            	sailingDatesComp.currentIndex = newValue.listData.index;
            	data.push(sailingDatesComp.jsonData[sailingDatesComp.currentIndex ]);
            	sailingData = sailingDatesComp.buildSailingList(data, shipCode, sailingDatesComp.includeAllData);
            	sailingDatesComp.renderTemplate(sailingData, includeAllflag);
            });

            //DE39062
            dojo.subscribe("tui:channel=getPriceClosing", function(){
            	var when = dijit.registry.byId('get-When');
            	 when.setSelectedIndex(0);
            });
        },

        onAfterTemplatePlaced: function () {
  	      var sailingDatesComp = this, parentNode, loaderNode;
	  	  parentNode = sailingDatesComp.domNode.parentElement;
		  loaderNode = dojo.query('.loading', parentNode)[0];
		  domStyle.set(loaderNode, "height", parentNode.offsetHeight+"px");
		  domStyle.set(loaderNode, "width", parentNode.offsetWidth+"px");
		  domStyle.set(loaderNode, "display", "block");
	  	  setTimeout(function () {
	  	  	sailingDatesComp.hideLoading();
	  	  },1000);
        },

        hideLoading: function () {
  	      	var sailingDatesComp = this, parentNode, loaderNode;
	  	    parentNode = sailingDatesComp.domNode.parentElement;
	     	loaderNode = dojo.query('.loading', parentNode)[0];
	     	domStyle.set(loaderNode, "display", "none");
  	    },

        buildSailingList: function (data, shipCode, includeAllflag) {
        	var sailingData =[];
        	if (includeAllflag) {
				sailingData.push(data[0]);
        		return sailingData;
        	}
        	_.each(data, function(sailingDataList) {
        		var itemData = {"sailingDatas":""};
        		if (!shipCode) {
    				itemData.sailingDatas = sailingDataList.sailingDatas;
    				sailingData.push(itemData);
        		} else {
    				itemData.sailingDatas = dojo.filter(sailingDataList.sailingDatas, function(item) {
	    				return (item.shipCode === shipCode);
        			});
        			sailingData.push(itemData);
        		}
    		});
        	return sailingData;
        },

        renderTemplate: function (data, includeAllflag) {
        	var sailingDatesComp = this, node, html;
        	node = query("ul", sailingDatesComp.domNode)[0];
        	data[0].touchSupport = sailingDatesComp.touchSupport;
        	html = sailingDatesComp.renderTmpl(null, data[0] );
            domConstruct.place(html, node , "only");
  	  	  	if (!includeAllflag) {
  	  	  		sailingDatesComp.onAfterTemplatePlaced();
  	  	  	}
            parser.parse(sailingDatesComp.domNode);

            on(node, ".button:click", function (event) {
                dojo.stopEvent(event);
                var selectedLi = dojo.query(event.target).closest("li")[0];
                //TODO: Find an alternate way to do this... + move logic to backend
                var getPriceModal = dijit.registry.byId('tui_searchGetPrice_view_cruise_GetPriceModal_0');
                var getPriceController = new Klass(null, dojo.query("#get-price-modal")[0]).getCreatedInstance();
                var searchCriteria = data[0].sailingDatas[domAttr.get(selectedLi, 'data-index') - 1].searchCriteria;
                getPriceController.prePopulate(searchCriteria);

                //setTimeout(function () {
                    var when = dijit.registry.byId('get-When');
                    var selectedDate = _.find(when.listData, function(date){
                        var departureDate = dojo.date.locale.parse(date.value, {
                            datePattern: 'dd-MM-yyyy',
                            selector: "date"
                        });

                        var sailingDate = dojo.date.locale.parse(searchCriteria.when, {
                            datePattern: 'dd-MM-yyyy',
                            selector: "date"
                        });
                        if( !_.isNull(departureDate) &&
                            !_.isNull(sailingDate) &&
                            (sailingDate.getMonth() === departureDate.getMonth()) &&
                            (sailingDate.getYear() === departureDate.getYear())
                        )
                        return date;
                    });
                    when.setSelectedValue(selectedDate.value);
                    var duration = dijit.registry.byId('get-totalDuration');
                    var selectedDuration = _.find(duration.listData, function(duration){
                        if(duration.value != 0){
                           var res = duration.value.split("-");
                           return( (parseInt(res[0]) <= parseInt(searchCriteria.duration)) && (parseInt(searchCriteria.duration) <= parseInt(res[1])) )
                        }
                    });
                    duration.setSelectedValue(selectedDuration.value); //searchCriteria.duration
                getPriceModal.open(true);

                //DE39062
                setTimeout(function(){
                    var selectedLi = query("li[datavalue='"+ selectedDate.value+"']", when.listElementUL)[0];
                    dojo.query(selectedLi).style("display", "block");
    	    		dojo.query(selectedLi).addClass("active");
                    on.emit(selectedLi, "mouseover", {
    					bubbles: true,
    					cancelable: true
    				});
                    on.emit(selectedLi, "click", {
    					bubbles: true,
    					cancelable: true
    				});
                }, 500);


            });

        }

    });
});