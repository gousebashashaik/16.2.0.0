
define('tui/widget/booking/LuggageAllowanceComponent', 
		[ 'dojo', 'dojo/query',
          'dojo/dom-class',
          "dojo/dom-style",
          "dojo/dom",
          "dojo/dom-construct",
          "dojo/text!tui/widget/Templates/LuggageAllowanceComponent.html",
          "dojo/html" ], 
          function(dojo, query, domClass,domStyle,dom,domConstruct,LuggageAllowanceComponentTmpl,html) {

			dojo.declare('tui.widget.booking.LuggageAllowanceComponent',[ tui.widget._TuiBaseWidget ], 
                                    					
                                    					{

                                    						tmpl:LuggageAllowanceComponentTmpl,
                                    						
                                    						
                                    						controller:  null,
                                    						baggageCount: null,
                                    						postCreate : function() {

                                    							var widget = this;
                                    							var widgetDom = widget.domNode;							
                                    							console.log(widget.jsonData);
                                    							
                                    							console.log(jsonData.extraFacilityViewDataContainer.mealOptions.extraFacilityViewData.length)
                                    							 
                                    							
                                    							
                                    							  var template = new dojox.dtl.Template(widget.tmpl);
                                    						      var context = new dojox.dtl.Context(widget);
                                    						      var html = template.render(context);

                                    						      domConstruct.place(html, widget.domNode, 'only');
                                    						      dojo.parser.parse(widget.domNode);
                                    						      
                                    						     
                                    						      widget.labelDisplay();	
                                    							
                                    						
                                    							console.log(dijit.registry.byId("controllerWidget"));
                                    							widget.controller = dijit.registry.byId("controllerWidget");
                                    							console.log(widget.controller);
                                    							var testvar = widget.controller.registerView(widget);
                                    							console.log(widgetDom);
                                    					
                                    							
                                    						},
                                    						refresh : function(field,response)
                                    						{
                                    							var widget = this;
                                    							
                                    							console.log(field);
                                    							console.log(response);
                                    							widget.jsonData = response;
                                    							console.log(response);
                                    							
                                    							console.log(widget.jsonData );
                                    							console.log(widget);
                                    							
                                    							
                                    							  var template = new dojox.dtl.Template(widget.tmpl);
                                    						      var context = new dojox.dtl.Context(widget);
                                    						      var html = template.render(context);

                                    						      domConstruct.place(html, widget.domNode, 'only');
                                    						      dojo.parser.parse(widget.domNode);
                                    						      widget.labelDisplay();
                                    							
                                    							
                                    						},
                                    						labelDisplay: function()
                                    						{
                                    							var widget = this;
                                    							var widgetDom = widget.domNode;
                                    							
                                    							widget.baggageCount =widget.jsonData.extraFacilityViewDataContainer.baggageOptions.extraFacilityViewData.length;
                                    							console.log(widget.jsonData.baggageSectionDisplayed);
                                    							if (widget.jsonData.baggageSectionDisplayed == true) {
																	
																
                                    							if (jsonData.packageViewData.paxViewData.noOfInfants > 0) {
																	
                                    								widget.baggageCount = widget.baggageCount+1;
																}
                                    							
                                    							var bagCount ="with-"+widget.baggageCount+"-BagExtras"; 
                                    							
                                    							domClass.add(dom.byId("luggage-allowance"),bagCount);
                                    							}else{
                                    								if (jsonData.packageViewData.paxViewData.noOfInfants > 0)
                                									{
                                    									var bagCount ="with-"+2+"-BagExtras"; 
                                    									domClass.add(dom.byId("luggage-allowance"),bagCount);
                                									}else
                                									{
                                										
                                									}
                                							}
                                    							
                                    							var passengerIdentifiers = query('.person-type', widgetDom);
                                    							_.each(passengerIdentifiers, function(passengerIdentifier)
                                    									{
                                    									
                                    						for (var i = 0; i < widget.jsonData.packageViewData.passenger.length; i++){
																			
                                    							var passengerType = widget.jsonData.packageViewData.passenger[i].type;
                                    							var passengerId= widget.jsonData.packageViewData.passenger[i].identifier;
                                    							var childsAge =	widget.jsonData.packageViewData.passenger[i].age;
                                    										if (passengerType == "CHILD")
                                    											{
                                    											console.log(passengerId);
                                    											console.log(dojo.attr(passengerIdentifier, "id"));
                                    											var id= dojo.attr(passengerIdentifier, "id");
                                    										if(passengerId == id)
                                    											{
                                    											
                                    											var idValue= id - jsonData.packageViewData.paxViewData.noOfAdults - jsonData.packageViewData.paxViewData.noOfSeniors ;
                                    											console.log(idValue);
                                    											var string = "Child"+" "+idValue+" "+"(age"+" "+childsAge+")";
                                    											console.log(string);
                                    											passengerIdentifier.innerHTML =  string ;
                                    												}
                                    											}
                                    										else if (passengerType == "INFANT") {
                                    											
                                    											console.log(passengerId);
                                    											console.log(dojo.attr(passengerIdentifier, "id"));
                                    											var id= dojo.attr(passengerIdentifier, "id");
                                    										if(passengerId == id)
                                    											{
                                    											
                                    											var idValue= id - jsonData.packageViewData.paxViewData.noOfAdults - jsonData.packageViewData.paxViewData.noOfSeniors - jsonData.packageViewData.paxViewData.noOfChildren ;
                                    											console.log(idValue);
                                    											var string = "Infant"+" "+idValue+" "+"(age 0-2)";
                                    											console.log(string);
                                    											passengerIdentifier.innerHTML =  string ;
                                    												}
																			}
																		}
                                    									
                                    									});
                                    						
                                    							
                                    					}
                                    					});
                                    	return tui.widget.booking.LuggageAllowanceComponent;
                                    	
                                    });