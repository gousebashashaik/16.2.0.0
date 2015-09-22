define("tui/widget/customeraccount/Expandable", [
  "dojo",
  "dojo/fx",
  "tui/widget/_TuiBaseWidget"
], function(dojo,fx) {

  dojo.declare("tui.widget.customeraccount.Expandable", [tui.widget._TuiBaseWidget], {
    

    postCreate: function() {
       var expandable = this;     
       expandable.inherited(arguments);

	   var id="item-toggle-"+expandable.wishlistEntryId;
	   var contentid="item-content-"+expandable.wishlistEntryId;
	   if(dojo.byId(id) != undefined){
	   var link = dojo.byId(id);
	   var flag = false;
	   dojo.connect(link, "onclick", function(event){ 
                var form = this;
				link.innerHTML = "";
				if(flag == false){
					link.innerHTML = 'hide holiday summary<i class="caret state grey"></i>';
					flag = true;
					
					if(dojo.byId(contentid) != undefined){
					dojo.byId(contentid).style.display = "block";
					dojo.query(".summary-container").addClass("open");					
					dojo.query(".summary-container").removeClass("close");
					}
					//var wipeTarget = dojo.byId(contentid);							
					//fx.wipeIn({ node: wipeTarget }).play();
					
				}
				else{
					link.innerHTML = 'See holiday summary<i class="caret state grey"></i>';
					flag = false;
					if(dojo.byId(contentid) != undefined){
					dojo.byId(contentid).style.display = "none";
					dojo.query(".summary-container").addClass("close");					
					dojo.query(".summary-container").removeClass("open");
					}
					//var wipeTarget = dojo.byId(contentid);							
					//fx.wipeOut({ node: wipeTarget }).play();
					
				}
		});
		}
    }
  });

  return tui.widget.customeraccount.Expandable;
});
