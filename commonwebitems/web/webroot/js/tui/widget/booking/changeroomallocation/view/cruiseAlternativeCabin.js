define("tui/widget/booking/changeroomallocation/view/cruiseAlternativeCabin", [
    "dojo",
    "dojo/query",
    "dojo/dom-class",
    "dojo/dom",
    "dojo/dom-construct",
    "dojo/topic",
    "dojo/text!tui/widget/booking/changeroomallocation/view/Templates/criuseChangeCabinType.html",
    "tui/widget/booking/changeroomallocation/modal/RoomModel",
    "tui/widget/mixins/Templatable",
    "tui/widget/expand/Expandable",
    "tui/widget/_TuiBaseWidget",
    "tui/widget/booking/bookflowMsgs/nls/Bookflowi18nable",
	"tui/widget/booking/changeroomallocation/view/AlternativeRoom"
    ], function(dojo, query, domClass,dom,domConstruct,topic,criuseChangeCabinType,roomModel,Templatable,Expandable,_TuiBaseWidget,Bookflowi18nable) {

        dojo.declare('tui.widget.booking.changeroomallocation.view.cruiseAlternativeCabin', [tui.widget.booking.changeroomallocation.view.AlternativeRoom,Expandable,Bookflowi18nable], {

        	tmpl:criuseChangeCabinType,

            postCreate: function() {
            	this.siteName = dojoConfig.site;
            	console.log(this.jsonData);
           	 var widget = this;
           	 widget.initBookflowMessaging();
             widget.cruiseCabin = widget.jsonData.roomOptionsViewData;
             roomModel.accomIndex = widget.bookflowMessaging[dojoConfig.site].brandMapping[widget.jsonData.packageType].cruiseSummary;
                this.inherited(arguments);





            },

            refresh : function(field,response)
            {
            	//Re-painting the Component
            	if(field == "Cabins" || field == "Rooms" || field == "changeRoom")
        		{

            var widget = this;
            var widgetDom = widget.domNode;
            widget.jsonData = response;
            roomModel.alternateRooms = response;
            var widget = this;
            var widgetDom = widget.domNode;
            widget.jsonData = response;
            widget.cruiseCabin = widget.jsonData.roomOptionsViewData;
            var parseNode = dom.byId("temp");
            dojo.destroy(parseNode);
            var nodeAttach = dom.byId("alternative-room-container");
            var html = widget.renderTmpl(widget.tmpl, widget);
            domConstruct.place(html, widget.domNode, 'only');

            dojo.parser.parse(widget.domNode);

             }

            }

        });
    return tui.widget.booking.changeroomallocation.view.cruiseAlternativeCabin;
});