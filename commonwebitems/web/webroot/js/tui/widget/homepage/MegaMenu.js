define('tui/widget/homepage/MegaMenu', [
  'dojo',
  'dojo/query',
  'dojo/on',
  'tui/thirdparty/fastclick.min',
  'dojo/_base/lang',
  'dojo/html',
  'tui/widget/_TuiBaseWidget'
], function(dojo, query , on, FastClick) {

  dojo.declare('tui.widget.homepage.MegaMenu', [tui.widget._TuiBaseWidget], {

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
      megaMenu.touchSupport = dojo.hasClass(document.body, "touch");
      if(tuiSiteName == "thomson" ) {
     	 megaMenu.THholidaysMenu();
      }else if(tuiSiteName == "falcon" ){
    	  megaMenu.FJholidaysMenu();
      }else if(tuiSiteName == "firstchoice" ){
    	  megaMenu.FCholidaysMenu();
      }else{
    	  megaMenu.touchSupport = dojo.hasClass(document.getElementsByTagName("HTML")[0], "touch");
    	  megaMenu.cruiseMenu();
      }
    },

    FJholidaysMenu: function(){
    	var megaMenu = this;
    	 var htmlElement = query("html")[0];
         arrowElement = query(".homepage-main-nav a .arrow-down")[0];
         hoverElement = query(".homepage-main-nav a")[0];
         displayTmpl = query(".collections-editorial")[0];
   		 	var divInfo = dojo.position(displayTmpl, true),
   		    liInfo = dojo.position(hoverElement, true);
   		 	var liHeight = dojo.getStyle(query(".homepage-main-nav")[0], 'height');
   		 	var liYPos = liInfo.y + liHeight - 4;
   		 	dojo.style(displayTmpl, {
   		      top: liYPos + "px"
   		 	});
       if(!megaMenu.touchSupport){
       	 on(hoverElement, "mouseover", function (event) {
       		 dojo.setStyle(arrowElement, 'border-top', '5px solid #e29447');
       		 dojo.setStyle(hoverElement, {
       			    'color': '#e29447',
       			    'background': '#FFFFFF',
       			    'text-decoration': 'none'
       			  });

       		 clearTimeout( megaMenu.mouseOutFromMegamenu);
       		 megaMenu.clearTimeOut = setTimeout(function () {
       		 	dojo.setStyle(displayTmpl, "visibility", "visible");
             }, 300);
        });
   	 on(hoverElement, "mouseout", function (event) {
   		 clearTimeout(megaMenu.clearTimeOut);
       		 dojo.setStyle(arrowElement, 'border-top', '5px solid #ffffff');
       		 dojo.setStyle(hoverElement, {
    			    'color': '#FFFFFF',
    			    'background': '#404145',
    			    'text-decoration': 'none'
    			  });
       		 if(dojo.hasClass(htmlElement, "nav-holiday-active")){
   				 dojo.setStyle(hoverElement,  'background', '#505457');
   	    		};
   		 megaMenu.mouseOutTime = setTimeout(function () {
   			 	dojo.setStyle(displayTmpl, "visibility", "hidden");
   	          }, 200);
   	     });
   	 on(displayTmpl, "mouseover", function(event){
       		 dojo.setStyle(arrowElement, 'border-top', '5px solid #e29447');
       		 dojo.setStyle(hoverElement, {
    			    'color': '#e29447',
    			    'background': '#FFFFFF',
    			    'text-decoration': 'none'
    			  });
   		 clearTimeout(megaMenu.mouseOutTime);
   	    	dojo.setStyle(displayTmpl, "visibility", "visible");
   	    	dojo.addClass(hoverElement, "navHighlight");
   	     });
   	 on(displayTmpl, "mouseleave", function(event){
       		 megaMenu.mouseOutFromMegamenu = setTimeout(function(){
       			 dojo.setStyle(arrowElement, 'border-top', '5px solid #FFFFFF');
       			 dojo.setStyle(hoverElement, {
       	 			    'color': '#FFFFFF',
       	 			    'background': '#404145',
       	 			    'text-decoration': 'none'
       	 			  });
       			 if(dojo.hasClass(htmlElement, "nav-holiday-active")){
       				 dojo.setStyle(hoverElement,  'background', '#505457');
       	    		};
   	    	dojo.setStyle(displayTmpl, "visibility", "hidden");
       		 }, 200);

   	    	dojo.removeClass(hoverElement, "navHighlight");
   	     });
       }
        else {
       	 on(hoverElement, "click", function (event) {
       		 if(megaMenu.check) {
       			 dojo.setStyle(arrowElement, 'border-top', '5px solid #e29447');
       			 dojo.setStyle(hoverElement, {
        			    'color': '#e29447',
        			    'background': '#FFFFFF',
        			    'text-decoration': 'none'
        			  });
       			 dojo.setStyle(displayTmpl, "visibility", "visible");
       			 megaMenu.check = false;
       		 }
       		 else {
       			 dojo.setStyle(arrowElement, 'border-top', '5px solid #FFFFFF');
       			 dojo.setStyle(hoverElement, {
     	 			    'color': '#FFFFFF',
     	 			    'background': '#404145',
     	 			    'text-decoration': 'none'
       			 	});
       			 if(dojo.hasClass(htmlElement, "nav-holiday-active")){
       				 dojo.setStyle(hoverElement,  'background', '#505457');
       	    		};
       			 dojo.setStyle(displayTmpl, "visibility", "hidden");
       			 megaMenu.check = true;
       		 }
       	});
        }
    },

    THholidaysMenu: function(){
    	var megaMenu = this;
    	 var htmlElement = query("html")[0];
         arrowElement = query(".homepage-main-nav a .arrow-down")[0];
         hoverElement = query(".homepage-main-nav a")[0];
         displayTmpl = query(".collections-editorial")[0];
   		 	var divInfo = dojo.position(displayTmpl, true),
   		    liInfo = dojo.position(hoverElement, true);
   		 	var liHeight = dojo.getStyle(query(".homepage-main-nav")[0], 'height');
   		 	var liYPos = liInfo.y + liHeight - 4;
   		 	dojo.style(displayTmpl, {
   		      top: liYPos + "px"
   		 	});
       if(!megaMenu.touchSupport){
       	 on(hoverElement, "mouseover", function (event) {
       		 dojo.setStyle(arrowElement, 'border-top', '5px solid #73AFDC');
       		 dojo.setStyle(hoverElement, {
       			    'color': '#a0c8e6',
       			    'background': '#FFFFFF',
       			    'text-decoration': 'none'
       			  });

       		 clearTimeout( megaMenu.mouseOutFromMegamenu);
       		 megaMenu.clearTimeOut = setTimeout(function () {
       		 	dojo.setStyle(displayTmpl, "visibility", "visible");
             }, 300);
        });
   	 on(hoverElement, "mouseout", function (event) {
   		 clearTimeout(megaMenu.clearTimeOut);
       		 dojo.setStyle(arrowElement, 'border-top', '5px solid #119ca6');
       		 dojo.setStyle(hoverElement, {
    			    'color': '#FFFFFF',
    			    'background': '#a0c8e6',
    			    'text-decoration': 'none'
    			  });
       		 if(dojo.hasClass(htmlElement, "nav-holiday-active")){
   				 dojo.setStyle(hoverElement,  'background', '#73AFDC');
   	    		};
   		 megaMenu.mouseOutTime = setTimeout(function () {
   			 	dojo.setStyle(displayTmpl, "visibility", "hidden");
   	          }, 200);
   	     });
   	 on(displayTmpl, "mouseover", function(event){
       		 dojo.setStyle(arrowElement, 'border-top', '5px solid #73AFDC');
       		 dojo.setStyle(hoverElement, {
    			    'color': '#a0c8e6',
    			    'background': '#FFFFFF',
    			    'text-decoration': 'none'
    			  });
   		 clearTimeout(megaMenu.mouseOutTime);
   	    	dojo.setStyle(displayTmpl, "visibility", "visible");
   	    	dojo.addClass(hoverElement, "navHighlight");
   	     });
   	 on(displayTmpl, "mouseleave", function(event){
       		 megaMenu.mouseOutFromMegamenu = setTimeout(function(){
       			 dojo.setStyle(arrowElement, 'border-top', '5px solid #FFFFFF');
       			 dojo.setStyle(hoverElement, {
       	 			    'color': '#FFFFFF',
       	 			    'background': '#a0c8e6',
       	 			    'text-decoration': 'none'
       	 			  });
       			 if(dojo.hasClass(htmlElement, "nav-holiday-active")){
       				 dojo.setStyle(hoverElement,  'background', '#73AFDC');
       	    		};
   	    	dojo.setStyle(displayTmpl, "visibility", "hidden");
       		 }, 200);

   	    	dojo.removeClass(hoverElement, "navHighlight");
   	     });
       }
        else {
       	 on(hoverElement, "click", function (event) {
       		 if(megaMenu.check) {
       			 dojo.setStyle(arrowElement, 'border-top', '5px solid #73AFDC');
       			 dojo.setStyle(hoverElement, {
        			    'color': '#a0c8e6',
        			    'background': '#FFFFFF',
        			    'text-decoration': 'none'
        			  });
       			 dojo.setStyle(displayTmpl, "visibility", "visible");
       			 megaMenu.check = false;
       		 }
       		 else {
       			 dojo.setStyle(arrowElement, 'border-top', '5px solid #FFFFFF');
       			 dojo.setStyle(hoverElement, {
     	 			    'color': '#FFFFFF',
     	 			    'background': '#a0c8e6',
     	 			    'text-decoration': 'none'
       			 	});
       			 if(dojo.hasClass(htmlElement, "nav-holiday-active")){
       				 dojo.setStyle(hoverElement,  'background', '#73AFDC');
       	    		};
       			 dojo.setStyle(displayTmpl, "visibility", "hidden");
       			 megaMenu.check = true;
       		 }
       	});
        }
    },

    FCholidaysMenu: function(){
    	var megaMenu = this;
    	 var htmlElement = query("html")[0];
         arrowElement = query(".homepage-main-nav a .arrow-down")[0];
         hoverElement = query(".homepage-main-nav a")[0];
         displayTmpl = query(".collections-editorial")[0];
   		 	var divInfo = dojo.position(displayTmpl, true),
   		    liInfo = dojo.position(hoverElement, true);
   		 	var liHeight = dojo.getStyle(query(".homepage-main-nav")[0], 'height');
   		 	var liYPos = liInfo.y + liHeight - 4;
   		 	dojo.style(displayTmpl, {
   		      top: liYPos + "px"
   		 	});
       if(!megaMenu.touchSupport){
       	 on(hoverElement, "mouseover", function (event) {
       		 dojo.setStyle(arrowElement, 'border-top', '5px solid #73AFDC');
       		 dojo.setStyle(hoverElement, {
       			    'color': '#119ca6',
       			    'background': '#FFFFFF',
       			    'text-decoration': 'none'
       			  });

       		 clearTimeout( megaMenu.mouseOutFromMegamenu);
       		 megaMenu.clearTimeOut = setTimeout(function () {
       		 	dojo.setStyle(displayTmpl, "visibility", "visible");
             }, 300);
        });
   	 on(hoverElement, "mouseout", function (event) {
   		 clearTimeout(megaMenu.clearTimeOut);
       		 dojo.setStyle(arrowElement, 'border-top', '5px solid #119ca6');
       		 dojo.setStyle(hoverElement, {
    			    'color': '#FFFFFF',
    			    'background': '#119ca6',
    			    'text-decoration': 'none'
    			  });
       		 if(dojo.hasClass(htmlElement, "nav-holiday-active")){
   				 dojo.setStyle(hoverElement,  'background', '#73AFDC');
   	    		};
   		 megaMenu.mouseOutTime = setTimeout(function () {
   			 	dojo.setStyle(displayTmpl, "visibility", "hidden");
   	          }, 200);
   	     });
   	 on(displayTmpl, "mouseover", function(event){
       		 dojo.setStyle(arrowElement, 'border-top', '5px solid #73AFDC');
       		 dojo.setStyle(hoverElement, {
    			    'color': '#119ca6',
    			    'background': '#FFFFFF',
    			    'text-decoration': 'none'
    			  });
   		 clearTimeout(megaMenu.mouseOutTime);
   	    	dojo.setStyle(displayTmpl, "visibility", "visible");
   	    	dojo.addClass(hoverElement, "navHighlight");
   	     });
   	 on(displayTmpl, "mouseleave", function(event){
       		 megaMenu.mouseOutFromMegamenu = setTimeout(function(){
       			 dojo.setStyle(arrowElement, 'border-top', '5px solid #FFFFFF');
       			 dojo.setStyle(hoverElement, {
       	 			    'color': '#FFFFFF',
       	 			    'background': '#119ca6',
       	 			    'text-decoration': 'none'
       	 			  });
       			 if(dojo.hasClass(htmlElement, "nav-holiday-active")){
       				 dojo.setStyle(hoverElement,  'background', '#73AFDC');
       	    		};
   	    	dojo.setStyle(displayTmpl, "visibility", "hidden");
       		 }, 200);

   	    	dojo.removeClass(hoverElement, "navHighlight");
   	     });
       }
        else {
       	 on(hoverElement, "click", function (event) {
       		 if(megaMenu.check) {
       			 dojo.setStyle(arrowElement, 'border-top', '5px solid #73AFDC');
       			 dojo.setStyle(hoverElement, {
        			    'color': '#119ca6',
        			    'background': '#FFFFFF',
        			    'text-decoration': 'none'
        			  });
       			 dojo.setStyle(displayTmpl, "visibility", "visible");
       			 megaMenu.check = false;
       		 }
       		 else {
       			 dojo.setStyle(arrowElement, 'border-top', '5px solid #FFFFFF');
       			 dojo.setStyle(hoverElement, {
     	 			    'color': '#FFFFFF',
     	 			    'background': '#119ca6',
     	 			    'text-decoration': 'none'
       			 	});
       			 if(dojo.hasClass(htmlElement, "nav-holiday-active")){
       				 dojo.setStyle(hoverElement,  'background', '#73AFDC');
       	    		};
       			 dojo.setStyle(displayTmpl, "visibility", "hidden");
       			 megaMenu.check = true;
       		 }
       	});
        }
    },

    cruiseMenu: function(){
    	var megaMenu = this,
    	hoverElement = dojo.byId("cruiseMenu"),
    	hoverElementAnch = dojo.query("a.tab-dev", hoverElement)[0];
    	//dojo.setStyle(dojo.body(), {"height": "500px", "overflow": "hidden"})
       if( megaMenu.touchSupport){

    	   dojo.setStyle(dojo.byId("mega-menu"),  'display', 'none');
    	  query("html, #wrapper, #footer").on("click", function(e){
    		   if( !dojo.query(e.target).parents("#cruiseMenu").length && dojo.hasClass( hoverElement, "hover") ){
    			   dojo.removeClass( hoverElement, "hover");
    			   dojo.setStyle(dojo.byId("mega-menu"),  'display', 'none');
    			   megaMenu.check = true;
    		   }
       	   });
	   	   query("a", dojo.byId("mega-menu")).on("click", function(){
			   setTimeout(function(){
				   dojo.setStyle(dojo.byId("mega-menu"),  'display', 'none');
			   }, 300);
		   });

    	   FastClick.attach(hoverElementAnch);
    	   on(hoverElementAnch, "click", function (e) {

    		   if( dojo.hasClass(e.target, "clicked") ){
  	   			 e.preventDefault();
  	   			 location.href= tuiWebrootPath;
  	   		  }

  	   		 dojo.addClass(e.target, "clicked");
  	   		 setTimeout(function(){
  	   			dojo.removeClass(e.target, "clicked");
  	   		 }, 500);

	  	   	if(megaMenu.check) {
				 dojo.addClass(hoverElement, "hover");
				 dojo.setStyle(dojo.byId("mega-menu"),  'display', 'block');
				 megaMenu.check = false;
			 }
			 else {
				 dojo.removeClass(hoverElement, "hover");
				 dojo.setStyle(dojo.byId("mega-menu"),  'display', 'none');
				 megaMenu.check = true;
			 }

    	   });

       }
    }
  })
  return tui.widget.homepage.MegaMenu;
});

