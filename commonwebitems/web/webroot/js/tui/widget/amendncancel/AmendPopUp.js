define("tui/widget/amendncancel/AmendPopUp",['dojo',
                                    'dojo/ready',
                                    'dojo/text!tui/widget/amendncancel/templates/AmendPopUp.html',
									 'tui/widget/popup/Popup'], function(dojo,ready,template){

    dojo.declare("tui.widget.amendncancel.AmendPopUp", [tui.widget.popup.Popup], {
		
		 // ---------------------------------------------------------------- tui.widget.mixins.Templatable properties
		 
    	modal:true,
    	
        tmpl: template,
        
        //-----------------------------Custom Properties
        passengerData:null,
        
	    postCreate: function () {
            amendPopUp = this;
            amendPopUp.inherited(arguments);
            dojo.connect(document,"onclick",function(evt){ 
            	if(evt.target.className === "overlay-close"){
            		amendPopUp.close();
            	}
            });
        }
    })
	
	return tui.widget.amendncancel.AmendPopUp;
})