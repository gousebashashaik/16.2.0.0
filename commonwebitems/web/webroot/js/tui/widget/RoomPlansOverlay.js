define("tui/widget/RoomPlansOverlay", [
  "dojo",
  "dojo/_base/declare",
  "dojo/dom-class",
  "dojo/dom-style",
  "dojo/on",
  "dojo/query",
  "dojo/cookie",
  "tui/widget/popup/Popup",
  "dojo/dom-construct",
  "dojo/text!tui/widget/Templates/RoomPlansOverlayTmpl.html",
  "dojo/NodeList-dom",
  "tui/widget/mixins/Templatable",
  "tui/widget/_TuiBaseWidget"
  ],
  //Holiday Village Kos has multiple seat option, URL below
  //http://fc.localhost.co.uk:9001/holiday/bookaccommodation?productCode=047406&tab=overview&noOfAdults=2&noOfChildren=0&childrenAge=&duration=7&flexibleDays=3&airports[]=LGW|LTN|SEN|STN&flexibility=true&noOfSeniors=0&when=31-07-2014&units[]=&packageId=047406HVMUCT140693760000014070240000004692140762880000014076288000004693DY158&index=4&multiSelect=true&brandType=F&finPos=4
  //http://fc.uktapp30-hybrisa-sp/destinations/destinations.html
    function (dojo, declare, domClass, domStyle, on, query, cookie, popup, domConstruct, overlayTmpl) {

    return declare("tui.widget.RoomPlansOverlay", [
          tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable, tui.widget.popup.Popup], {

	modal: true,

	tmpl: overlayTmpl,

    floatWhere: "position-center",

    title:'',

    imageSrc:'',

    includeScroll:true,

    idx:0,

	postCreate: function () {
		var roomPlanOverlay = this,
		close = query('#room-plans-overlay .close');
		if (close && close.length) {
			on(close, "click", function(e) {
				widget.close();
			});
		}
		roomPlanOverlay.title = roomPlanOverlay.title.replace(/<\/?[^>]+>/gi,"");
		roomPlanOverlay.setPosOffset(roomPlanOverlay.floatWhere);
 		roomPlanOverlay.inherited(arguments);
	},

	open: function () {
        var roomPlanOverlay = this;
        roomPlanOverlay.inherited(arguments);
    },

    onOpen:function(){
    	var roomPlanOverlay = this;
        var containerDiv;
        var imageLoadTimeInterval = null;
        var roomPlanImage = null;
    	containerDiv = query('.room-overlay-' + roomPlanOverlay.idx + ' p.roomTitle')[0];
    	roomPlanImage = query(".room-plan-img", roomPlanOverlay.popupDomNode)[0];
        roomPlanImageLoader = query(".imageLoader", roomPlanOverlay.popupDomNode)[0];
        if(containerDiv){
        	roomPlanOverlay.resizeToFit(containerDiv);
        }
        imageLoadTimeInterval = setInterval(function(){
        	if(roomPlanImage.complete){
                domClass.remove(roomPlanImage,"displayNone");
                domClass.add(roomPlanImageLoader,"displayNone");
				roomPlanOverlay.posElement(roomPlanOverlay.popupDomNode);
                clearInterval(imageLoadTimeInterval);
        	}
        },300);
    },

    resizeToFit: function(containerDiv) {
    	var roomPlanOverlay = this;
        var contentSpan = query('span.content',containerDiv)[0];
        var fontSize = domStyle.get(contentSpan,"fontSize");
        var contentHeight = domStyle.get(contentSpan,"height");
        var containerHeight = domStyle.get(containerDiv,"height");

        if(contentHeight >= containerHeight){
        	domStyle.set(contentSpan,"fontSize",parseFloat(fontSize) - 1 + 'px');
        	roomPlanOverlay.resizeToFit(containerDiv);
        }
    },

    close: function() {
            var widget = this,
              modalBackground = query(".modal");
            widget.inherited(arguments);
            if(modalBackground){
              domStyle.set(modalBackground[0],"display","none");
              if(dojoConfig.site == "cruise"){
            	  dojo.window.scrollIntoView(widget.domNode);
              }
            }

      }

    });
  });