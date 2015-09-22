define("tui/widget/amendncancel/SearchBookingComponentPanel", [
"dojo",
"dojo/dom-construct",
"dojo/dom-attr",
"dojo/dom-geometry",
"dojo/_base/xhr",
"dojo/dom-style",
"dojo/query",
"tui/mixins/MethodSubscribable",
"dojo/_base/lang",
"dijit/focus",
"dojo/on",
"dijit/registry",
"dojo/dom-class",
"tui/widget/_TuiBaseWidget"], function (dojo, domConstruct, domAttr,domGeom, xhr, domStyle, query, MethodSubscribable, lang, focusUtil, on, registry, domClass) {

  dojo.declare("tui.widget.amendncancel.SearchBookingComponentPanel", [tui.widget._TuiBaseWidget, MethodSubscribable], {
	  
	//Properties of MethodSubscribable
	  subscribableMethods: ["clearSearchFields"],
	  startup:function() {
		  var refNode = registry.byNode(query(".passengerSurName",this.domNode)[0]);
		  on(refNode, 'blur', lang.hitch(this,this.handleChangeEvent, refNode));
	  },
	  handleChangeEvent : function(nodeRef) {
			if (nodeRef.value != '' || (dojo.hasClass(nodeRef,"highlight-border-color"))) {
				dojo.removeClass(dojo.byId("psurname"), "highlight-border-color");
			}
		},
  
	  postCreate:function(){
		  searchPanel = this;
		  var newSearch = dojo.byId("new-search");
		  var surName = dojo.byId("psurname");
		  dojo.connect(newSearch, "onclick", function(evt){
			  if(dojo.query(".unabletofind-booking")[0]){
				  dojo.query(".unabletofind-booking")[0].style.display = "none";
			  }
			  searchPanel.clearSearchFields();
			  dojo.addClass(dojo.byId("psurname"), "highlight-border-color");
			  focusUtil.focus(dojo.byId('psurname'));
		  });
		  
		  var search = dojo.query(".search-submit")[0];
		  dojo.connect(search, "onclick", function(evt){
			  var retrieveForm = dijit.byId("searchBookingByNameForm");
			 
	
				if(retrieveForm.validate() && dojo.byId('date').value != "Select a date" && dojo.byId("errDate").innerHTML === ""){
					
						
					var myNode = dojo.byId("content");
					var margin = domGeom.getMarginBox(myNode);
					var grayedArea = dojo.query('.amend-loading')[0];
					grayedArea.style.display = "block";
					domGeom.setMarginBox(grayedArea, {w: margin.w, h: margin.h});
					var results = xhr.post({
			             url: "search",
			             content: {
			            	 departureDate: dojo.byId("date").value,
			            	 surName: dojo.byId("psurname").value
			             },
			             
			             handleAs: "json",
			             async: true,
			             headers: {Accept: "application/javascript, application/json"},
			             load:function(resp){
			            	 var1 = resp;
			            	 dojo.query('.amend-loading')[0].style.display = "none";
			            	 dojo.publish("tui.widget.amendncancel.bookingSearchResultsComponent.renderBookingResults",var1);
			            	 },
			             error: function (err) {
			            	 dojo.query('.amend-loading')[0].style.display = "none";
			                 alert(err);
			             }
			         });
				}
				if(!lang.exists('.unabletofind-booking')){
					focusUtil.focus(dojo.byId('psurname'));
					dojo.addClass(dojo.byId("psurname"), "highlight-border-color");
				 }
				if(dojo.byId('date').value === "Select a date" && dojo.byId("errDate").innerHTML === ""){
					var errorDate = domConstruct.toDom("<tr><td><div class='error-notation error-message-text'>Please select a date</div></td></tr>");
					domConstruct.place(errorDate,  "errDate");
					var img_node = dojo.query('#dateImg')[0];
					img_node.style.display = "block";
					if (dojo.hasClass(img_node,"image-success")) {
						dojo.removeClass(img_node,"image-success");
					}
				    dojo.addClass(img_node, "image-failure");
					  query(".image-failure").style({
						    marginTop: "-63px",
						    marginLeft: "174px"
						  });
					}
		  });
	  },
	  
	  clearSearchFields:function(){
	      //To reset datepicker focused date to default
		  if(dojo.byId("date").value != "Select a date"){
		    dojo.publish("tui.widget.amendncancel.AmendDatePicker.clearDateField",null);
		  }
		  domAttr.set(dojo.query(".passengerSurName")[0].children[0].children[0],"style",{"display":"none"});
		  if(dojo.query(".passengerSurName")[0].children[2]){
			  domAttr.set(dojo.query(".passengerSurName")[0].children[2],"style",{"display":"none"});
		  }
		  dojo.query(".passengerSurName")[0].children[1].children[0].value = "";
		  dojo.byId("date").value = "Select a date";
		  dojo.style(dojo.byId('date'),"color","#989898");
		  dojo.query(".errClass")[0].innerHTML = "";
		  var img_node = dojo.query('#dateImg')[0];
		  img_node.style.display = "none";
	  }
	  
	  
  });
  
  return tui.widget.amendncancel.SearchBookingComponentPanel;
  
});