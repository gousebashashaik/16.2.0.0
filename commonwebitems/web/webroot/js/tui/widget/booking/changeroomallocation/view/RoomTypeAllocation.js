
define('tui/widget/booking/changeroomallocation/view/RoomTypeAllocation', [
  'dojo',
  'dojo/query',
  'dojo/dom-class',
  "dojo/dom-construct",
  'tui/widget/booking/changeroomallocation/modal/RoomModel',
  "dojo/text!tui/widget/booking/changeroomallocation/view/Templates/RoomTypeAllocationTmpl.html",
  'tui/widget/mixins/Templatable'
  
], function(dojo, query, domClass,domConstruct,roomModel,RoomTypeAllocationTmpl) {

dojo.declare('tui.widget.booking.changeroomallocation.view.RoomTypeAllocation', [tui.widget._TuiBaseWidget,tui.widget.mixins.Templatable], {	
	
	tmpl: RoomTypeAllocationTmpl, 
	
	tempjsonData: null,
	
    childsCount:null,
    
    adultsCount:null,
    
    postCreate: function() {		
    	
	        var widget = this;
		 	var widgetDom = widget.domNode;
		 	var container= dojo.query("#room_row_content",document.body)[0];
	 	 	widget.childsCount = [];
            widget.adultsCount= [];
           
            widget.refTotalAdultsCount = roomModel.jsonData.packageViewData.paxViewData.noOfAdults + widget.jsonData.packageViewData.paxViewData.noOfSeniors;
            widget.refTotalChildsCount = roomModel.jsonData.packageViewData.paxViewData.noOfChildren + widget.jsonData.packageViewData.paxViewData.noOfInfants ;
           
            for ( var i = 1 ;i<=widget.refTotalChildsCount; i++){
            	widget.childsCount.push(i)
           }
            for ( var i = 1 ;i<=widget.refTotalAdultsCount; i++){
            	 widget.adultsCount.push(i)
            }
			 widget.childsCount.splice(0,0,"0");
	         widget.childsCount.splice(0,0,"-");
	         if(roomModel.jsonData.inventoryType == "TRACS"){
	        	
	        	 widget.adultsCount.splice(0,0,"-");
	        }else{
	        	 widget.adultsCount.splice(0,0,"0");
	        	 widget.adultsCount.splice(0,0,"-");
	        }
	        
		 	 
		     var html = widget.renderTmpl(widget.tmpl, widget); 
		     domConstruct.place(html, widgetDom, 'only');
		     this.inherited(arguments);
		     widget.tagElements(dojo.query('button.addroom'),"addroom");
		     widget.tagElements(dojo.query('a.cancel'),"cancelSelectRooms");
		     widget.tagElements(dojo.query('button.submit'),"continueSelectRooms");
		     widget.tagElements(dojo.query('a.close'),"closeSelectRooms");
		     var roomrowDom=dojo.query('.room_row',widgetDom);
		    	 	
	 }
	});
	return tui.widget.booking.changeroomallocation.view.RoomTypeAllocation;
});