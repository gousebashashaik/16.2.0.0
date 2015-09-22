define ("tui/widget/DarkSitePopup", ['dojo',
                                     'dojo/ready',
                                     'dojo/text!tui/widget/Templates/AlertTmpl.html',
                                     'dojo/cookie',
									 'tui/widget/popup/Popup'], function(dojo,ready,alertTmpl,cookie,popup){

	dojo.declare("tui.widget.DarkSitePopup",  [tui.widget.popup.Popup], {



		modal: true,
		
		tmpl: alertTmpl,
		
		darkSiteText: "",
		
		darkSiteHeading: "",
		
		darkSiteUrl: "",
		
		darkSiteTime:"",
		
		COOKIE_NAME: "darkSiteCookie",
		
		
		postCreate: function() {
			var darkSiteAlert = this;	
			darkSiteAlert.inherited(arguments);
			darkSiteAlert.darkSiteText=darkSiteAlert.darkSiteText.replace(/~/g,'"'); 
				if(cookie(darkSiteAlert.COOKIE_NAME)!=darkSiteAlert.darkSiteTime){
					darkSiteAlert.open();	
					}
	    },
	  
	    onClose: function() {
	    	var darkSiteAlert = this;
		     cookie(darkSiteAlert.COOKIE_NAME, darkSiteAlert.darkSiteTime);
	    }
	  
	})

	return tui.widget.DarkSitePopup;
});
