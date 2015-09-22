define("tui/widget/amendncancel/RetrieveBookingComponentPanel", [
"dojo",
"dojo/dom-construct",
"dojo/dom-attr",
"dojo/dom-geometry",
"dojo/_base/xhr",
"dojo/dom-style",
"dojo/query",
"tui/widget/_TuiBaseWidget"], function (dojo, domConstruct, domAttr,domGeom, xhr, domStyle, query) {

  dojo.declare("tui.widget.amendncancel.RetrieveBookingComponentPanel", [tui.widget._TuiBaseWidget], {
	  
	  postCreate:function(){
		  
		  var retrieve = dojo.query(".retrive-booking-text-button")[0];
		  dojo.connect(retrieve, "onclick", function(evt){
			    var retrieveForm = dijit.byId("retrieveBookingForm");
				if(retrieveForm.validate()){
					retrieveForm.submit();
				}
		  });
		  
		  
		  dojo.connect(document , "onmousedown", function(evt){
			  if(evt.target.nodeName === "INPUT"){
				  if(dojo.query('.error-message-section')[0]){
						domStyle.set(dojo.query('.error-message-section')[0], {display: 'none'});
					}
			  }
			});
	  }
	  
  });
  
  return tui.widget.amendncancel.RetrieveBookingComponentPanel;
  
});