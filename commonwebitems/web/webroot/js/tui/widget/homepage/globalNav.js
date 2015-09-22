define('tui/widget/homepage/globalNav', [
  'dojo',
  'dojo/query',
  'dojo/on',
  'tui/thirdparty/fastclick.min',
  'dojo/_base/lang',
  'dojo/html',
  'tui/widget/_TuiBaseWidget'
], function(dojo, query , on, FastClick) {

  dojo.declare('tui.widget.homepage.globalNav', [tui.widget._TuiBaseWidget], {

    // ----------------------------------------------------------------------------- methods

	mousePosition: false,

	clearTimeOut: null,

	mouseOutTime: null,

	mouseOutFromMegamenu: null,

	hoverElement: null,

	displayTmpl: null,

	check : true,

	arrowElement: null,

    postCreate: function() {
      var megaMenu = this;
      megaMenu.inherited(arguments);
      megaMenu.touchSupport = dojo.hasClass(query("html")[0], "touch");
	  megaMenu.initialiseMegaMenu();
    },

    initialiseMegaMenu: function(){

   	 var megaMenu = this;
   	 var megaMenuLi = megaMenu.domNode;
   	 var htmlElement = query("html")[0];
     var arrowElement = query("> a .arrow-down", megaMenuLi)[0];
     var hoverElement = query("> a" + ( megaMenu.touchSupport ? ".tab-dev" : ".desk-dev" ) , megaMenuLi)[0];
     var displayTmpl = query(".megaMenu", megaMenuLi)[0];

     //Tablet Devices
       if(megaMenu.touchSupport){

	    	  dojo.setStyle(displayTmpl,  'visibility', 'hidden');

	    	  dojo.subscribe("tui.widget.homepage.globalNav.closeAllOverlays", function(){
	    		  dojo.setStyle(displayTmpl,  'visibility', 'hidden');
	    		  dojo.removeClass(megaMenuLi, "hover");
	    		  dojo.query(".tooltip").style({display: "none"});
	    		  megaMenu.check = true;
	    	  } );

	    	  query("html, #wrapper, #footer").on("click", function(e){
	    		   if( !dojo.query(e.target).parents(".homepage-main-nav").length && dojo.hasClass( megaMenuLi, "hover") ){
	    			   dojo.removeClass( megaMenuLi, "hover");
	    			   dojo.setStyle(displayTmpl,  'visibility', 'hidden');
	    			   megaMenu.check = true;
	    		   }
	       	   });

	    	  query("a", displayTmpl).on("click", function(){
				   setTimeout(function(){
					   dojo.setStyle(displayTmpl,  'visibility', 'hidden');
					   dojo.removeClass(megaMenuLi, "hover");
				   }, 300);
			   });
	    	  //dojo.addClass(document.body, "needsclick");
	    	  // FastClick.attach(hoverElement);
	    	   on(hoverElement, "click", function (e) {

	    		 /*if( dojo.hasClass(e.target, "clicked") ){
	  	   			 e.preventDefault();
	  	   			 location.href= dojo.query(e.target).prev()[0].href;
	  	   		 }

	  	   		 dojo.addClass(e.target, "clicked");
	  	   		 setTimeout(function(){
	  	   			dojo.removeClass(e.target, "clicked");
	  	   		 },250);*/

	    		 if( dojo.hasClass(megaMenuLi, "hover") ){
	  	   			 e.preventDefault();
	  	   			 location.href= dojo.query(e.target).prev()[0].href;
	  	   		 }

		  	   	if(megaMenu.check) {
		  	   		 dojo.publish("tui.widget.homepage.globalNav.closeAllOverlays");
					 dojo.addClass(megaMenuLi, "hover");
					 dojo.setStyle(displayTmpl,  'visibility', 'visible');
					 megaMenu.check = false;
					 //alert("Hovered");
				 }
				 else {
					 dojo.query(".tooltip").style({display: "none"});
					 dojo.removeClass(megaMenuLi, "hover");
					 dojo.setStyle(displayTmpl,  'visibility', 'hidden');
					 megaMenu.check = true;
					// alert("Hover Removed");
				 }

	    	   });


       }
   }



  })
  return tui.widget.homepage.globalNav;
});
