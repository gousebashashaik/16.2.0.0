define("tui/widget/popup/cruise/ajaxJSFile", 
    ["dojo", "dojo/ready", "tui/widget/_TuiBaseWidget"], function (dojo, ready) {

    dojo.declare("tui.widget.popup.cruise.ajaxJSFile", [tui.widget._TuiBaseWidget], {

        postMixInProperties:function(){
        	
        	var ajaxJSFile = this; 
        	ajaxJSFile.inherited(arguments);
        	ready(function(){
        		dojo.xhrGet({ //
        	        // The following URL must match that used to test the server.
        	        url: "http://cr.localhost.co.uk:7001/cruise/itineraries/ajax/Sensatori-Resort-Crete-000006/",
        	        handleAs: "text",

        	        timeout: 5000, // Time in milliseconds

        	        // The LOAD function will be called on a successful response.
        	        load: function(response, ioArgs) { //
        	          dojo.byId("replace").innerHTML = "<div>"+ioArgs.xhr.responseText+"</div>"; //
        	          //return response; //
        	        },

        	        // The ERROR function will be called in an error case.
        	        error: function(response, ioArgs) { //
        	          console.error("HTTP status code: ", ioArgs.xhr.status); //
        	          dojo.byId("replace").innerHTML = 'Loading the ressource from the server did not work'; //
        	          return response; //
        	          }
        	        }); 
        	});
        	
        }
      
    });
    return tui.widget.popup.cruise.ajaxJSFile;
});
