define('tui/widget/booking/changeroomallocation/view/cruiseChangeCabinAllocation', [
  'dojo',
  'dojo/query',
  'dojo/dom-class',
  "dojo/dom-construct",
  "dojo/dom",
  "dojo/on",
  "dijit/registry",
  "dojo/Evented",
  "dojo/dom-attr",
  "dojo/topic",
  "dojo/number",
  'tui/widget/booking/changeroomallocation/modal/RoomModel',
  "tui/widget/booking/constants/BookflowUrl",
  "dojo/text!tui/widget/booking/changeroomallocation/view/Templates/ChangeTypeAllocationTmpl.html",
  "tui/widget/booking/changeroomallocation/view/ChangeRoomAllocation",
  "tui/widget/booking/changeroomallocation/view/RoomTypeAllocation",
  "tui/widget/_TuiBaseWidget",
  'tui/widget/mixins/Templatable'], function(dojo, query, domClass,domConstruct,dom,on,registry,Evented,domAttr,topic,number,roomModel,BookflowUrl,changeTypeAllocationTmpl) {

dojo.declare('tui.widget.booking.changeroomallocation.view.cruiseChangeCabinAllocation', [tui.widget.booking.changeroomallocation.view.ChangeRoomAllocation], {
	
	url : BookflowUrl.cruisecabinsurl,
	postCreate: function() {
		this.inherited(arguments);
		
	

	   
		
		
	},
	dataRefContainer: function(RoomData){
    	 
    	 var widget = this;
    	 roomModel.roomSellingCode = RoomData[0]["listOfCabinTypeViewData"][0][widget.sellingCodeRefPath][0].sellingCode;
    	 
     }

		

	});
	return tui.widget.booking.changeroomallocation.view.cruiseChangeCabinAllocation;
});