define("tui/recommendations/homeRecommendation", [
	"dojo",
	"dojo/cookie",
	"dojo/io/script",
	"dojo/_base/lang",
	"dojo/parser",
	"dojo/on",
	"dojo/dom",
	"dojo/_base/xhr",
	"dojo/text!tui/recommendations/view/templates/homeRecommendationTmpl.html",
	"tui/config/TuiConfig",
	"dojo/topic",
	"tui/widget/mixins/Templatable"
	], function (dojo, cookie, script, lang, parser,on,dom,xhr,tmpl, tuiConfig, topic) {

	dojo.declare("tui.recommendations.homeRecommendation", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable], {
		browseTmpl: tmpl,
		tmplPlaceHolder: '',
		identifierFetchAjaxUrl: '/ws/baynoteHomePage',
		tuiConfig: tuiConfig,

		postCreate: function() {
			var rec = this;
			rec.inherited(arguments);
			rec.tmplPlaceHolder = rec.domNode;
			var url = rec.webrootPath + rec.identifierFetchAjaxUrl;
			rec.generateRequest(url);

		},

		generateRequest: function(value) {
	    	 var rec = this;
	    	 console.log("ajax call..");
	    	 var results = xhr.get({
	             url: value,
	             handleAs: "json",
	             headers: {Accept: "application/javascript, application/json"},
	             error: function (jxhr,err) {
	                 if (dojoConfig.devDebug) {
	                     console.error(jxhr);
	                 }
	                 console.log(err.xhr.responseText);
	                 rec.afterFailure(err.xhr.responseText);
	             }
	         });

	    	 dojo.when(results,function(response) {
	    		 var modifiedCode;
	    		 if (!response) {
	    			 rec.displayErrorMessage("Endeca Response Failed");
					return;
	    		 }
	    		 if(response && response.accomodationDatas){
	    			 topic.publish("tui/recommendations/prominentRecommendation/getRecommendation", response);
		    		 if(response.accomodationDatas.length < 3) {
		    			 dojo.setStyle(rec.domNode, {"display": "none"});
		    			 dojo.setStyle(dojo.query(".recsDefaultHomePage"), {"display": "none"});
		    		 }
		    		 else {
		    			 dojo.removeClass(rec.domNode, 'spinner');
		    			 rec.jsonData = response;
		    			 if(rec.jsonData.accomodationDatas && dojoConfig.dualBrandSwitch && rec.tuiConfig[dojoConfig.site]){
		    				 _.each(rec.jsonData.accomodationDatas, function(accomodationData){
		    					 if(accomodationData.productRanges && accomodationData.productRanges.length){
		    						 modifiedCode = rec.tuiConfig[dojoConfig.site].dualBrandConfig.differentiatedCodeLarge[accomodationData.productRanges[0].code.toLowerCase().replace(/\s/g,"")];
		    	                    	if(modifiedCode){
		    	                    		accomodationData.productRanges[0].code = modifiedCode;
		    	                    	}
		    					 }
		    				 });
		    			 }
		    			 var html = rec.renderTmpl(rec.browseTmpl,rec);
		    			 dojo.place(html, rec.tmplPlaceHolder, 'only');
		    			 rec.tmplPostRender();
		    		 }
	    		 }
	    	 });
	     },

	     afterFailure: function(html) {
	        console.log(html);
	     },

	     displayErrorMessage: function(errMsg){
					var rec = this;
					dojo.query(".recs-carousel").style("display", "none");
					dojo.removeClass(rec.domNode, 'loading');
					dojo.addClass(rec.domNode, 'error');
					console.log(errMsg);
		},

	     tmplPostRender: function() {
	    	 var rec = this;
	    	 parser.parse({ rootNode: rec.domNode });
	     }

	});

	return tui.recommendations.homeRecommendation;
});
