define ("tui/widget/search/AirportGuideExpandable", ["dojo",
													"dojo/text!tui/widget/search/templates/mainsearch/AirportGuideTmpl.html",
													"tui/widget/expand/SimpleExpandable"], function(dojo, airportGuide){
	
	dojo.declare("tui.widget.search.AirportGuideExpandable", [tui.widget.expand.SimpleExpandable], {
		
		tmpl: null,
				
		postCreate: function() {
			var airportGuideExpandable = this;
			airportGuideExpandable.tmpl = airportGuide;
			airportGuideExpandable.inherited(arguments);
			if (!airportGuideExpandable.expandableDom){
				airportGuideExpandable.createExpandable();
			}
			
			airportGuideExpandable.attachEventListeners();

			dojo.connect(airportGuideExpandable.domNode, "onclick", function(event){
				dojo.publish("tui/widget/form/SelectOption/onclick", [this, event]);
				dojo.publish("tui/widget/mixins/AutoCompleteable/onclick", [this, event]);
			});
			
			airportGuideExpandable.subscribe("tui/widget/search/AirportAutocomplete/airportguide", function(){
				airportGuideExpandable.domNode.focus();
				airportGuideExpandable.openExpandable();
			});
						
			airportGuideExpandable.subscribe("tui/widget/AirportAutocomplete/onChange", function(name, oldValue, value, date){
				var className = value.listData.cd.replace("*","");
				airportGuideExpandable.unSelectActive();
				dojo.query([".",className].join(""), airportGuideExpandable.expandableDom).addClass("active");
			});

			airportGuideExpandable.subscribe("tui/widget/search/AirportAutocomplete/airportUpdate", function(airport){
				dojo.query(".ag", airportGuideExpandable.expandableDom).addClass("disabled");
				_.forEach(airport, function(airportItem){
					var className = airportItem.cd.replace("*","");
					dojo.query([".",className].join(""), airportGuideExpandable.expandableDom).removeClass("disabled");
				});
			});

			airportGuideExpandable.subscribe("tui/widget/search/AirportAutocomplete/unSelect", function(){
				airportGuideExpandable.unSelectActive();
			});
		},

		attachEventListeners: function(){
         var airportGuideExpandable = this;

         if ((dojo.isIE !== 7) && (!dojo.isWebKit)){
            airportGuideExpandable.connect(airportGuideExpandable.domNode, "onblur" ,function(event){
               dojo.stopEvent(event);
               airportGuideExpandable.closeExpandable();
            });
            return;
         }

         airportGuideExpandable.connect(document.body, "onmousedown", function(event){
            var airportGuideExpandable = this;
            if (!airportGuideExpandable.expandableDom) return;
            if (airportGuideExpandable.isShowing(airportGuideExpandable.expandableDom)){
               airportGuideExpandable.closeExpandable();
            }
         });

      },

		unSelectActive: function(){
			var airportGuideExpandable = this;
			dojo.query(".active", airportGuideExpandable.expandableDom).removeClass("active");
		},

		onAfterTmplRender: function(){
			var airportGuideExpandable = this;
			airportGuideExpandable.inherited(arguments);
			var elements = dojo.query(".ag", airportGuideExpandable.expandableDom);
			airportGuideExpandable.attachEventToAirportElement(elements);
		},
		
		attachEventToAirportElement: function(elements){
			var airportGuideExpandable = this;
			_.forEach(elements, function(element){
				airportGuideExpandable.connect(element, "onmousedown" ,function(event){
					dojo.stopEvent(event);
					if (!dojo.hasClass(element, "disabled")){
						var id = dojo.attr(element, "data-dataid");
						dojo.query(".active", airportGuideExpandable.expandableDom).removeClass("active");
						dojo.addClass(element, "active");
						dojo.publish("tui/widget/search/AirportGuideExpandable/selected", [id]);
						airportGuideExpandable.closeExpandable();
					}
				});
			});
		},
		
		place: function(html){
			var airportGuideExpandable = this;
			return dojo.place(html, dojo.byId("search"), "last");
		}
	});
		
	return tui.widget.search.AirportGuideExpandable;
});