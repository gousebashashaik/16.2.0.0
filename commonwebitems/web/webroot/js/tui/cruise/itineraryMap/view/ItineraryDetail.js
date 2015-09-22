define("tui/cruise/itineraryMap/view/ItineraryDetail", [
    "dojo",
    "dojo/query",
    "dojo/parser",
    "dojo/json",
    "dojo/text!tui/cruise/itineraryMap/view/templates/itineraryDetail.html",
    "tui/widget/_TuiBaseWidget",
    "tui/widget/mixins/Templatable"
], function (dojo, query, parser, JSON, tmpl) {

	 function truncateTex (description){
		var MAX_WORDS = 60;
		description = description.replace(/  +/g, "");
		var truncText = description.split(" ");
		if( truncText.length > MAX_WORDS) {
		truncText.splice(MAX_WORDS, truncText.length - MAX_WORDS);
		description = truncText.join(" ");
		}
		return description;
		}
		
    dojo.declare("tui.cruise.itineraryMap.view.ItineraryDetail", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable], {

       tmpl: tmpl,
	   
       constructor: function(data, locationName, atSea, bookMode){
          var itineraryDetail = this;
          itineraryDetail.data = data;
          itineraryDetail.data.locationName = locationName;
          itineraryDetail.atSea =  atSea;
          itineraryDetail.bookMode = bookMode;
       },

       addToView: function(node, atSea){
          var itineraryDetail = this;
          var defaultImage = dojoConfig.paths.webRoot + '/images/cruise/default-large.png';
          if(itineraryDetail.atSea){
              _.map(itineraryDetail.data.facilities, function(facility){	
                  facility.image = facility.imageUrl;
                  //facility.description = facility.description;
				   facility.descriptionItineraryPage = truncateTex(facility.description);
                  //facility.name = facility.name;
                  facility.json = JSON.stringify(facility);
                  return facility;
              });
          }else{
              _.map(itineraryDetail.data.excursion, function(exc){
                  exc.image = _.isEmpty(exc.smallImages) ? defaultImage : exc.galleryImages[0].mainSrc;
                  //exc.description = exc.description;
				  exc.descriptionItineraryPage = truncateTex(exc.description);
                 // exc.excursionUrl = exc.excursionUrl;
                 // exc.excursionName = exc.excursionName;
                  exc.json = JSON.stringify(exc);
                  return exc;
              });
              //updating 'View All' link for Shore Excursions shown
              query('a', query('.view-all-itinerary', node)[0])[0].href = itineraryDetail.data.viewAllShoreExcursionUrl;
          }

          itineraryDetail.data.bookMode = itineraryDetail.bookMode;
          var html = itineraryDetail.renderTmpl(itineraryDetail.tmpl, itineraryDetail.data);

          var nodeTobePlaced = atSea ? node : query('.view-all-itinerary', node)[0] ;
          var position = atSea ? "last" : "before";
          var dom = dojo.place(html, nodeTobePlaced, position);
          parser.parse(node);

       }



    });

    return tui.cruise.itineraryMap.view.ItineraryDetail;
});