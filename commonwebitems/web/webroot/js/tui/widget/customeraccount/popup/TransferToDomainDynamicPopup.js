define ("tui/widget/customeraccount/popup/TransferToDomainDynamicPopup", ["dojo","tui/widget/_TuiBaseWidget"], function(dojo){

    dojo.declare("tui.widget.customeraccount.popup.TransferToDomainDynamicPopup",[tui.widget._TuiBaseWidget], {
    	
        widgetId: null,
		delay:3000,
		wishlistEntryId:null,
        postCreate: function(){ 
            var self = this;
			var className = ".blue"+self.wishlistEntryId;
			dojo.query(className).onclick(function(event){ 
				dojo.stopEvent(event); console.log(self.href);
				if(self.widgetId == "transferringToOtherTH"){
					dojo.addClass("transferringToOtherTH","show");
				}
				else if(self.widgetId == "transferringToOtherFC"){
					dojo.addClass("transferringToOtherFC","show");
				}
				var href = self.href;
				setTimeout(function(){
					if(self.href){
						window.location = self.href;
					}			
				}, self.delay);
			});
        }
		
		
    });

    return tui.widget.customeraccount.popup.TransferToDomainDynamicPopup;
})