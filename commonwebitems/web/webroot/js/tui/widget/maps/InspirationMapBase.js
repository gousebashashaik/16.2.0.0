define('tui/widget/maps/InspirationMapBase', [
  'dojo',
  'dojo/on',
  'dojo/text!tui/widget/maps/template/mapTmpl.html',
  'tui/widget/maps/MapBase',
  'tui/widget/maps/MapSidePanel',
  'tui/widget/maps/MapTab',
  'tui/widget/maps/MapScrollPanel',
  'dojox/dtl/filter/strings',
  'tui/dtl/filter/strings',
  'dojox/dtl/_DomTemplated',
  'tui/widget/mixins/Templatable',
  'dojox/uuid/generateRandomUuid'
], function(dojo, on, mapTmpl) {

  dojo.declare('tui.widget.maps.InspirationMapBase', [tui.widget.maps.MapBase, dojox.dtl._DomTemplated, tui.widget.mixins.Templatable], {

    templateString: mapTmpl,

    mapFilter: true,

    mapSidePanel: null,

    templateview: 'map',

    selectedType: null,

    selectedLocations: null,

    currentMarkerFromCluster: null,

    showEntitiesTypes: null,

    pageType: null,

    cdnDomain: dojoConfig.paths.cdnDomain,

    placeHolderImages: null,

    cruiseBookFlowAccom : true,


    postMixInProperties: function () {
      var inspirMapBase = this;
      inspirMapBase.placeHolderImages = {
        tiny: '<img alt="Image coming soon" src="' + dojoConfig.paths.cdnDomain + '/images/' + inspirMapBase.siteName + '/default-small.png" width="105" />',
        small: '<img alt="Image coming soon" src="' + dojoConfig.paths.cdnDomain + '/images/' + inspirMapBase.siteName + '/default-small.png" width="232" height="130" />'
      };
      inspirMapBase.inherited(arguments);
    },

    postCreate: function() {
      var inspirMapBase = this;
      inspirMapBase.inherited(arguments);
      dojo.parser.parse(inspirMapBase.domNode);
      google.maps.event.addListener(inspirMapBase.map, 'zoom_changed', function() {
        inspirMapBase.unselectListItem();
        inspirMapBase.addCurrentMarkerToCluster();
      });
      google.maps.event.addListener(inspirMapBase.currentPopup, 'closeclick', function() {
        inspirMapBase.unselectListItem();
        inspirMapBase.addCurrentMarkerToCluster();
        for (var i = 0; i < inspirMapBase.markers.length; i++) {
          var marker = inspirMapBase.markers[i];
          marker.setIcon(marker.imageoff);
        }
      });
    },

    unselectListItem: function() {
      var inspirMapBase = this;
      dojo.query('.active', inspirMapBase.mapSidePanel.getMapTab().domNode).removeClass('active');
    },

    selectListItem: function(listItem) {
      var inspirMapBase = this;
      inspirMapBase.unselectListItem();
      dojo.addClass(listItem, 'active');
    },

    scrollListItemIntoView: function(listItem, index) {
      var inspirMapBase = this;
      var mapScrollPanels = inspirMapBase.mapSidePanel.getMapTab().getMapScrollPanels();
      mapScrollPanels[index].moveElementInView(listItem);
    },

    renderTemplateView: function(templateview, cnxt) {
      var inspirMapBase = this;
      cnxt = cnxt || inspirMapBase;
      cnxt.templateview = templateview;
      return inspirMapBase.renderTmpl(mapTmpl, cnxt);
    },

    addCurrentMarkerToCluster: function(marker) {
      var inspirMapBase = this;
      if (inspirMapBase.currentMarkerFromCluster) {
        inspirMapBase.markerClustered.addMarker(inspirMapBase.currentMarkerFromCluster);
      }
      inspirMapBase.currentMarkerFromCluster = (marker && (!marker.getVisible())) ? marker : null;
    },

    addMarkerEventListener: function(/*google.maps.Marker*/ marker, /*Object*/ markerData) {
      // summary:
      //    Adds marker 'onclick' eventlisteners, which will display marker popup window,
      //    and highlight markers element from list.
      // marker: google.maps.Marker
      //    Google marker object to attach event
      // markerData: Object
      //    Object contain marker data.
      var inspirMapBase = this, markerFlag=true;
      var listItem = dojo.query(['.marker_', marker.entitydata.id].join(''), inspirMapBase.domNode)[0];

      google.maps.event.addListener(marker, 'click', function() {
        inspirMapBase.addCurrentMarkerToCluster();
        inspirMapBase.currentPopup.close();
        inspirMapBase.selectListItem(listItem);
        inspirMapBase.scrollListItemIntoView(listItem, 0);
        dojo.addClass(listItem, 'active');
        var html = inspirMapBase.renderTemplateView('popup-content', dojo.mixin({
          webRoot:dojoConfig.paths.webRoot,
          siteName:dojoConfig.site,
          pageType:inspirMapBase.pageType,
          cruiseBookFlowAccom: inspirMapBase.pageType === "bookflow" ||  inspirMapBase.pageId === "bookFlowAccommodationLocationPage",
          placeHolderImages:inspirMapBase.placeHolderImages,
          currency : dojoConfig.currency
        }, marker.entitydata));
        var index = dojo.getAttr(listItem, 'idx');
        inspirMapBase.currentPopup.setContent("<span class='count'>" + index + "<span class='corner-top'></span></span>" + html);
        inspirMapBase.currentPopup.open(inspirMapBase.map, marker);
        inspirMapBase.currentPopup.currentMarker = marker;
        if(markerFlag){
        	//DE42773: Fix for Firefox map centering issue and, list item highlight issue fix
  			google.maps.event.addListenerOnce(inspirMapBase.map, 'tilesloaded', function(){
  				on.emit(dojo.query(".marker_" + marker.entitydata.id)[0], "click", {
      				bubbles: true,
      				cancelable: true
      			});
  			});
      		markerFlag = false
      	}

      });

      inspirMapBase.connect(listItem, 'onclick', function(event) {
        inspirMapBase.addCurrentMarkerToCluster(marker);
        inspirMapBase.currentPopup.close();
        inspirMapBase.onMarkerClick(marker);
        inspirMapBase.showMarkerInCluster(marker, function() {
          inspirMapBase.selectListItem(listItem);
          inspirMapBase.scrollListItemIntoView(listItem, 0);
          var index = dojo.getAttr(listItem, 'idx');
          var html = inspirMapBase.renderTemplateView('popup-content', dojo.mixin({
              webRoot:dojoConfig.paths.webRoot,
              siteName:dojoConfig.site,
              pageType:inspirMapBase.pageType,
              cruiseBookFlowAccom: inspirMapBase.pageType === "bookflow" ||  inspirMapBase.pageId === "bookFlowAccommodationLocationPage",
              placeHolderImages:inspirMapBase.placeHolderImages,
              currency : dojoConfig.currency
            }, marker.entitydata));

          //modified for cruise need to change -nagaraj
          inspirMapBase.currentPopup.setContent("<span class='count'>" + index + "<span class='corner-top'></span></span>" + html, marker.entitydata);
          inspirMapBase.currentPopup.open(inspirMapBase.map, marker);
          inspirMapBase.currentPopup.currentMarker = marker;

          inspirMapBase.tagElement(inspirMapBase.currentPopup.content_, marker.entitydata.featureCodesAndValues.name);
          // We need to update the analytics text as container is reused.
          dojo.setAttr(inspirMapBase.currentPopup.content_, 'analytics-text', marker.entitydata.featureCodesAndValues.name);
        });
      });
    },

    removeMarkersFromList: function() {
      var inspirMapBase = this;
      dojo.empty(inspirMapBase.markerListDomNode);
    }
  });

  return tui.widget.maps.InspirationMapBase;
});