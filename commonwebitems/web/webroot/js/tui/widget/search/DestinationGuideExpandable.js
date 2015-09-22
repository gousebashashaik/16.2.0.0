define ("tui/widget/search/DestinationGuideExpandable", ["dojo",
                                            "dojo/text!tui/widget/search/templates/mainsearch/DestinationGuideTmpl.html",
                                            "tui/widget/expand/SimpleExpandable",
                                            "dojo/topic"], function(dojo, destinationGuide){
   
   dojo.declare("tui.widget.search.DestinationGuideExpandable", [tui.widget.expand.SimpleExpandable], {
      
      destinationData: null,
      
      tmpl: null,

        createDestinationGuide: function(destinationac){
            var destinationGuideExpandable = this;
            if (!destinationGuideExpandable.destinationData) return;
            var destinationData = destinationac.destinationData;
            
            destinationData = destinationData[0].cL.concat(destinationData[1].cL, destinationData[2].cL);
            destinationData.sort(function(a, b){
                return a.cN > b.cN;
            });
            var amount = Math.ceil(destinationData.length / 6);
            for(var i = 0; i < 6; i++){
                destinationGuideExpandable.destinationData.push(destinationData.splice(0, amount));
            }

            // create expandable.
            if (!destinationGuideExpandable.expandableDom) destinationGuideExpandable.createExpandable();

            // attach event listeners.
            destinationGuideExpandable.attachEventListeners();
            var data = destinationac.getSelectedData();
            if (data) destinationGuideExpandable.selectGuideItem(data);
        },

      postCreate: function() {

            var destinationGuideExpandable = this;
         // set defaults
         destinationGuideExpandable.inherited(arguments);
         destinationGuideExpandable.tmpl = destinationGuide;
         destinationGuideExpandable.destinationData = [];
         destinationGuideExpandable.availableDesinations = [];

            destinationGuideExpandable.subscribe("tui/widget/search/DestinationAutocomplete/domready", function(destinationac){
                destinationGuideExpandable.createDestinationGuide(destinationac);
            });

            destinationGuideExpandable.connect(destinationGuideExpandable.domNode, "onclick", function(event){
                dojo.publish("tui/widget/form/SelectOption/onclick", [this, event]);
                dojo.publish("tui/widget/mixins/AutoCompleteable/onclick", [this, event]);
            });

            destinationGuideExpandable.subscribe("tui/widget/search/DestinationAutocomplete/destinationguide", function(){
            destinationGuideExpandable.domNode.focus();
            destinationGuideExpandable.openExpandable();
         });
         
         destinationGuideExpandable.subscribe("tui/widget/search/DestinationAutocomplete/parseCountry", function(countryData, firstPass){
            if (firstPass) dojo.query(".dg", destinationGuideExpandable.expandableDom).addClass("disabled");
            var id = ["dg-" + countryData.destinationCode].join("");
            var element = dojo.byId(id);
            dojo.removeClass(element, "disabled");
         });
         
         destinationGuideExpandable.subscribe("tui/widget/search/DestinationAutocomplete/onChange", function(name, oldCountryData, countryData){
            destinationGuideExpandable.selectGuideItem(countryData);
         });

            destinationGuideExpandable.subscribe("tui/widget/search/DestinationAutocomplete/unSelect", function(){
                destinationGuideExpandable.unSelectActive();
            });
      },
      
      attachEventListeners: function(){
         var destinationGuideExpandable = this;
         
         if ((dojo.isIE !== 7) && (!dojo.isWebKit)){
            destinationGuideExpandable.connect(destinationGuideExpandable.domNode, "onblur" ,function(event){
               dojo.stopEvent(event);
               destinationGuideExpandable.closeExpandable();
            });
            return;
         }

         destinationGuideExpandable.connect(document.body, "onmousedown", function(event){
            var destinationGuideExpandable = this;
            if (!destinationGuideExpandable.expandableDom) return;
            if (destinationGuideExpandable.isShowing(destinationGuideExpandable.expandableDom)){
               destinationGuideExpandable.closeExpandable();
            }
         });

      },

        unSelectActive: function(){
            var destinationGuideExpandable = this;
            dojo.query(".active", destinationGuideExpandable.expandableDom).removeClass("active");
        },

      selectGuideItem: function(countryData){
         var destinationGuideExpandable = this;
            destinationGuideExpandable.unSelectActive();
         var id = ["dg-" + countryData.listData.destinationCode].join("");
         var element = dojo.byId(id);
         dojo.addClass(element, "active");
      },
      
      place: function(html){
         var destinationGuideExpandable = this;
         return dojo.place(html, dojo.byId("search"), "last");
      },
      
      onAfterTmplRender: function(){
         var destinationGuideExpandable = this;
         delete destinationGuideExpandable.destinationData;
         destinationGuideExpandable.inherited(arguments);
         var elements = dojo.query(".dg", destinationGuideExpandable.expandableDom);
         _.forEach(elements, function(element){
            destinationGuideExpandable.connect(element, "onmousedown" ,function(event){
               dojo.stopEvent(event);
               if (!dojo.hasClass(element, "disabled")){
                  var id = dojo.attr(element, "data-dataid");
                  //dojo.query(".active", destinationGuideExpandable.expandableDom).removeClass("active");
                  //dojo.addClass(element, "active");
                  dojo.publish("tui/widget/search/DestinationGuideExpandable/selected", [id]);
                  destinationGuideExpandable.closeExpandable();
               }
               
            });
         });
      }
   });
      
   return tui.widget.search.DestinationGuideExpandable;
});