define ("tui/widget/search/DestinationAutocomplete", ["dojo",
                                         "dojo/text!tui/widget/Templates/Search/DestinationNoMatchTmpl.html",
                                         "dojo/text!tui/widget/Templates/Search/DestinationItemTmpl.html",
                                         "dojo/json",
                                         "tui/widget/popup/OverflowTooltip",
                                         "dojo/topic",
                                         "dojo/date/locale",
                                         "tui/widget/form/AutoComplete",
                                         "dojo/NodeList-traverse",
                                         "dojo/html",
                                         "dojo/dom-attr",
                                         "tui/widget/search/CookieSearchSave"], function(dojo, destinationNoMatchTmpl, destinationItemTmpl, json, overflowTooltip){


   dojo.declare("tui.widget.search.DestinationAutocomplete", [tui.widget.form.AutoComplete,
                                                tui.widget.search.CookieSearchSave], {
      
      // summary:
      //       DestinationAutocomplete class extends autocomplete, and gives up autocomplete
      //    filter behaviour for given destinations passed from iscape application.
      
      // url which provides us with destination json data for iscape application.
      targetURL: "/fcsun/page/common/search/freetextsearchpanelupdate.page",
      
      // char length of when to perfom autocomplete.
      charNo: 3,
      
      //autoCompleteItemTmpl: "Templates/DestinationItemTmpl.html",
      
      liContentTemplate: destinationItemTmpl,
      
      titleProp: "name",
      
      valueProp: "destinationCode",
      
      idProperty: "name",
      
      searchProperty: "name",
      
      descriptionProp: "descr",
      
      cacheonly: true,
      
      appendResults: false,
      
      isDestinationStillAvailable: false,
      
      selectedDestinationType: "",
      
      parentWidget: null,
      
      postCreate: function() {
         
         var destinationAutocomplete = this;
         
         //destinationAutocomplete.liContentTemplate = destinationItemTmpl;
         destinationAutocomplete.inherited(arguments);
         destinationAutocomplete.parentWidget = destinationAutocomplete.getParent();
         
         var data = [];
         _.forEach(destinationAutocomplete.jsonData, function(dataItem){
            destinationAutocomplete.parseCountry(dataItem.cL, data);
         });
         
         destinationAutocomplete.destinationData = destinationAutocomplete.jsonData;
         destinationAutocomplete.jsonData = data;
         destinationAutocomplete.setData(destinationAutocomplete.jsonData);
         
         destinationAutocomplete.attachEventSubscribe();
         
         var savedsearch = destinationAutocomplete.getSaveFormData("sc/ss");
         if (savedsearch){
            savedsearch = dojo.fromJson(savedsearch);
            destinationAutocomplete.setSelectedValue(savedsearch.searchto);
         } else {
            destinationAutocomplete.setFormFieldValues();
         }
         
         new overflowTooltip({
            elementRelativeTo: destinationAutocomplete.domNode
         }, null);

         destinationAutocomplete.subscribe("tui/lazyload", function(){
            dojo.publish("tui/widget/search/DestinationAutocomplete/domready", destinationAutocomplete);
            delete destinationAutocomplete.destinationData;
            if(savedsearch) {
               destinationAutocomplete.lapland({value:savedsearch.durationType});
            }
            
         });
         
         destinationAutocomplete.subscribe("tui/widget/SearchDurationOption/onChange", function(value){
            destinationAutocomplete.lapland(value);
         });
      },

      lapland: function(value){
        // hardcoded hack method used solely for lapland day trip options
        // will be replaced in 1b

         var destinationAutocomplete = this;
         var selectedDataCountryName = null;
         var selectedDataCountry = null;
         var dgItems = dojo.query(".dg");
         if(dgItems.length === 0) return;

         if(destinationAutocomplete.getSelectedData()){
           selectedDataCountryName = destinationAutocomplete.getSelectedData().listData.countryName;
           selectedDataCountry = destinationAutocomplete.getSelectedData().listData.name;
         }

         if(value.value === "1/w"){
            destinationAutocomplete.setSelectedValue("Lapland");
            dgItems.forEach(function(item){
               if((!dojo.hasClass(item, "active")) && (dojo.attr(item,"data-dataid") !== "Arctic Circle")) {
                  if(dojo.hasClass(item, "disabled")) {
                     dojo.addClass(item,"lapland");
                  }
                  dojo.addClass(item, "disabled");
               }
            });
         } else if (selectedDataCountry === "Lapland" || selectedDataCountryName === "Arctic Circle") {
            destinationAutocomplete.unSelect();

            dgItems.forEach(function(item){
               if(!dojo.hasClass(item, "lapland")) {
                  dojo.removeClass(item,"disabled");
               }
               dojo.removeClass(item,"lapland");
            });
         }
      },

      attachEventSubscribe: function(){
         var destinationAutocomplete = this;
         
         destinationAutocomplete.subscribe("tui/widget/search/DestinationGuideExpandable/selected", function(name){
            destinationAutocomplete.setSelectedValue(name);
         });
         
         destinationAutocomplete.subscribe("tui/widget/AirportAutocomplete/onChange", function(name, oldvalue, value, date){
            dojo.publish("tui/widget/search/DestinationAutocomplete/domready", destinationAutocomplete);
            destinationAutocomplete.onAirportChange(value, date);
            delete destinationAutocomplete.destinationData;
         });
      },
      
      onAirportChange: function(airportData, date){
         
         var destinationAutocomplete = this;
         if (airportData === null){
            destinationAutocomplete.setData(destinationAutocomplete.jsonData);
            return;
         }
         
         var formateDate = dojo.date.locale.format(date, {selector: "date", datePattern: "d/MM/yyyy"});
         var key = airportData.value + "-destination-" + formateDate.replace(/\//g,'');
         
         var results = (typeof(Storage) !== "undefined" && sessionStorage[key]) ?
               dojo.fromJson(sessionStorage[key]) : destinationAutocomplete.requestJson(["?selectedDate=", formateDate, "&selectedDepartureAirport=", airportData.value].join(""));
         
         destinationAutocomplete._newDetinationParse = true;
         
         dojo.when(results, function(dataresults){
            try {
               if (typeof(Storage)!== "undefined"){
                  if(dataresults!== null){
                     sessionStorage[key] = json.stringify(dataresults);
                  }
               }
            } catch (e) {
               if (e == QUOTA_EXCEEDED_ERR)
                  sessionStorage.clear();
            }
            
            var data = [];
            var selectedDestination = destinationAutocomplete.getSelectedData();

            for(var i = 0; i < dataresults.length; i++){
               destinationAutocomplete.parseCountry(dataresults[i].cL, data, selectedDestination);
            }
            destinationAutocomplete.setData(data);
            
            if (selectedDestination){
               var dataItem = destinationAutocomplete._cacheStore.get(selectedDestination.listData.name);

               if (!dataItem){
                  var error = tui.showDefaultSearchErrorPopup(destinationAutocomplete.domNode, {
                        airportname: airportData.key,
                        destinationname: destinationAutocomplete.domNode.value,
                        onOpen: function(popupDomNode, popupBase){
                           dojo.query(".destinationguide-ac", popupDomNode).onclick(function(){
                              dojo.publish("tui/widget/search/DestinationAutocomplete/destinationguide");
                              tui.removeErrorPopup(destinationAutocomplete.domNode);
                           });
                        }
                     }, "search/templates/DestinationRouteError.html");
      
                  destinationAutocomplete.unSelect();
               }
            }
            destinationAutocomplete.setData(data);
         });
      },
      
      parseCountry: function(countryList, dataArray, selectedDestination, i){
         var destinationAutocomplete = this;
      
         _.forEach(countryList, function(country, index){
            dataArray.push({
               name: country.cN,
               destinationCode: country.cI,
               selectedDestination: country.cI,
               destinationType: tui.widget.search.DestinationAutocomplete.COUNTRY,
               descr: "COUNTRY"
            });

            dojo.publish("tui/widget/search/DestinationAutocomplete/parseCountry", [dataArray[dataArray.length - 1], destinationAutocomplete._newDetinationParse]);
            destinationAutocomplete._newDetinationParse = false;
            if (country.dL){
               destinationAutocomplete.parseDestination(country.dL, dataArray, country, selectedDestination);
            }
            
         });
      },
      
      parseDestination: function(destinationList, dataArray, country, selectedDestination){
         var destinationAutocomplete = this;
         _.forEach(destinationList, function(destination, index){
    
            dataArray.push({
               name: destination.dN,
               destinationCode: destination.dI,
               selectedDestination: destination.dI,
               countryCode: country.cI,
               countryName: country.cN,
               destinationType: tui.widget.search.DestinationAutocomplete.DESTINATION,
               descr: "DESTINATION"
            });
            
            dojo.publish("tui/widget/search/DestinationAutocomplete/parseCountry", [dataArray[dataArray.length - 1], destinationAutocomplete._newDetinationParse]);
            
            if (destination.rL){
               destinationAutocomplete.parseResort(destination.rL, dataArray, country, destination, selectedDestination);
            }
         });
      },
      
      parseResort: function(resortList, dataArray, country, destination, selectedDestination){
         var destinationAutocomplete = this;
         _.forEach(resortList, function(resort){
 
            dataArray.push({
               name: resort.rN,
               resortCode: resort.rI,
               selectedResort: resort.rI,
               countryCode: country.cI,
               countryName: country.cN,
               destinationCode: destination.dI,
               selectedDestination: destination.dI,
               destinationType: tui.widget.search.DestinationAutocomplete.RESORT,
               descr: "DESTINATION"
            })
            if (resort.aL){
               destinationAutocomplete.parseAccommodationList(resort.aL, dataArray, country, destination, resort, selectedDestination);
            }
         });
      },
      
      parseAccommodationList: function(accommodationList, dataArray, country, destination, resort, selectedDestination){
         var destinationAutocomplete = this;
         _.forEach(accommodationList, function(accommodation){
    
            dataArray.push({
               name: accommodation.aN,
               accommodationCode: accommodation.aI,
               selectedAccommodation: accommodation.aI,
               countryCode: country.cI,
               countryName: country.cN,
               destinationCode: destination.dI,
               selectedDestination: destination.dI,
               resortCode: resort.rI,
               selectedResort: resort.rI,
               descr: "HOTEL",
               destinationType: tui.widget.search.DestinationAutocomplete.ACCOMMODATION
            })
         });
      },
      
      onChange: function(name, oldValue, value){
         // summary:
         //    Method is called when a value change has occured on the destination autocomplete.
         //    We then set the appropriate values for the selected destination data onto the search form.   
         var destinationAutocomplete = this;
         if (value == null) return;
         if (value.listData[destinationAutocomplete.valueProp] === "destinationguide") {
            destinationAutocomplete.unSelect();
            destinationAutocomplete.destinationGuideSelected()
            return;
         }
         destinationAutocomplete.inherited(arguments);
         var destinationData = (value) ? value.listData : {};
         // if country name exists, we need to append it onto the end of the autocomplete field. 
         if (destinationData.countryName){
                value.key = [destinationAutocomplete.domNode.value, ", ", destinationData.countryName].join("");
                destinationAutocomplete.domNode.value = value.key;
         }

         dojo.publish("tui/widget/search/DestinationAutocomplete/onChange", [name, oldValue, value]);
         destinationAutocomplete.setFormFieldValues(destinationData);
      },
      
      setFormFieldValues: function(destinationData){
         var destinationAutocomplete = this;
         var value, propname, formField; 
         for (var i = 0; i < tui.widget.search.DestinationAutocomplete.DESTINATION_FIELDS.length; i++){
            propname = tui.widget.search.DestinationAutocomplete.DESTINATION_FIELDS[i];
            formField = tui.getFormElementByName(propname, destinationAutocomplete.parentWidget.domNode);
            value = "";
            if (destinationData){
               value = destinationData[propname] || "";
            }
            formField.value = value;
            if (propname == "destinationType"){
               destinationAutocomplete.selectedDestinationType = value;
            }
         }
      },
      
      destinationGuideSelected: function(){
         dojo.publish("tui/widget/search/DestinationAutocomplete/destinationguide");
      },
      
      unSelect: function(){
         var destinationAutocomplete = this;
         destinationAutocomplete.domNode.value = "";
            dojo.publish("tui/widget/search/DestinationAutocomplete/unSelect");
         destinationAutocomplete.inherited(arguments);
      },
      
      onRenderLiContent: function(text, data){
         // summary:
         //    Event method is called each time a list item in the autocomplete is rendered.
         //    The return string from this method will be use as the item text.
         //    text <String> - This is the text of the titleProp given for the autocomplete of the selected item.
         //    data <Object> - Object containing the selected data, of the selected item.
         var destinationAutocomplete = this;
         text = (data.countryName) ? [text +", "+ data.countryName].join("") : text;
         var typedText = destinationAutocomplete.domNode.value;
         var contentHTML = dojo.string.substitute(destinationAutocomplete.liContentTemplate, {
            autocompletetext: text.replace(new RegExp('(' + typedText + ')', 'i'), '<strong>$1</strong>'),
            description: data[destinationAutocomplete.descriptionProp]
         });
         return contentHTML;
      },
      
      onResults: function(data){
         // summary:
         //    Method is overriden from AutoCompleteable, so we can add destination guide option into list.
         var destinationAutocomplete = this;
         if (destinationAutocomplete.noMatchConnect){
            destinationAutocomplete.disconnect(destinationAutocomplete.noMatchConnect);
         }
         if (data !== null && data.length > 0){
            var destinationguide = {};
            destinationguide[destinationAutocomplete.titleProp] = "Destination guide";
            destinationguide[destinationAutocomplete.valueProp] = "destinationguide";
            destinationguide[destinationAutocomplete.descriptionProp] = " ";
            data.push(destinationguide);
         }
         destinationAutocomplete.inherited(arguments);
      },
      
      onNoResults: function(listElementUL){
         var destinationAutocomplete = this;
         var noNoMatch = dojo.html.set(listElementUL, destinationAutocomplete.noMatchMessage || destinationAutocomplete.createNoMatchMessage());
         var destinationguide = dojo.query(".destinationguide-ac", noNoMatch)[0];
         destinationAutocomplete.noMatchConnect = destinationAutocomplete.connect(destinationguide, "onclick", function(){
            destinationAutocomplete.destinationGuideSelected();
         });
      },
      
      createNoMatchMessage: function(){
         var destinationAutocomplete = this;
         var context = {templateName: "destinationNoMatch"};
         destinationAutocomplete.noMatchMessage = destinationAutocomplete.createTmpl(context);
         return  destinationAutocomplete.noMatchMessage;
      },
      
      createTmpl: function(context){
         var template = new dojox.dtl.Template(destinationNoMatchTmpl);
         context  = new dojox.dtl.Context(context);
         return template.render(context);
      }
   });
   
   tui.widget.search.DestinationAutocomplete.DESTINATION_FIELDS = ["destinationCode","resortCode","accommodationCode", "selectedResort", "selectedAccommodation", "selectedDestination", "destinationType"];
   tui.widget.search.DestinationAutocomplete.COUNTRY = "country";
   tui.widget.search.DestinationAutocomplete.DESTINATION = "destination";
   tui.widget.search.DestinationAutocomplete.RESORT = "resort";
   tui.widget.search.DestinationAutocomplete.ACCOMMODATION = "accommodation";
   tui.widget.search.DestinationAutocomplete.ROUTE_ERROR = "We don't fly on this route on this date. You will need to change your date or destination or you can see our";
   
   return tui.widget.search.DestinationAutocomplete;
});