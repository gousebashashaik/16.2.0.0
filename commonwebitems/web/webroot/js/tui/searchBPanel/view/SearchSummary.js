define("tui/searchBPanel/view/SearchSummary", [
  "dojo",
  "dojo/on",
  "dojo/dom-attr",
  'dojo/topic',
  "tui/config/TuiConfig",
  "tui/widget/_TuiBaseWidget"], function (dojo, on, domAttr, topic, tuiConfig) {

  dojo.declare("tui.searchBPanel.view.SearchSummary", [tui.widget._TuiBaseWidget, tui.searchB.nls.Searchi18nable], {

    // ----------------------------------------------------------------------------- properties

    searchDomNode: null,

    searchDomContainer: null,

    whereFromNode: null,

    whereToNode: null,
    
    siteName: dojoConfig.site,

  

    tuiConfig: tuiConfig,

    // ----------------------------------------------------------------------------- methods

    postCreate: function () {
      // summary:
      //		show summary if view is set to summary in model
      var searchSummary = this;
      searchSummary.inherited(arguments);
      searchSummary.initSearchMessaging();
      searchSummary.searchDomNode = dojo.query(".search")[0];
      searchSummary.whereFromNode = dojo.query(".where-from-summary", searchSummary.domNode)[0];
      searchSummary.whereToNode = dojo.query(".where-to-summary", searchSummary.domNode)[0];
      
      searchSummary.editButtonListener();

      searchSummary.searchPanelModel.watch("view", function (name, oldValue, newValue) {
    	  
        if (newValue === "summary") {
          searchSummary.handleSummaryData();
          searchSummary.open();
        } else {
          searchSummary.close();
        }
		topic.publish('tui/searchBPanel/view/SearchSummary?view=changed', {});
      });

      searchSummary.tagElement(searchSummary.domNode, "search-summary");
      searchSummary.tagElement(dojo.query(".search-edit", searchSummary.domNode)[0], "edit-search");
    },

    open: function () {
      // summary:
      //		Opens summary view
      var searchSummary = this;
      dojo.removeClass(searchSummary.domNode, "closed");
      dojo.addClass(searchSummary.domNode, "open");
      dojo.removeClass(searchSummary.searchDomNode, "open");
      dojo.addClass(searchSummary.searchDomNode, "closed");
    },

    close: function () {
      // summary:
      //		Closes summary view
      var searchSummary = this;
      dojo.removeClass(searchSummary.searchDomNode, "closed");
      dojo.addClass(searchSummary.searchDomNode, "open");
      dojo.removeClass(searchSummary.domNode, "open");
      dojo.addClass(searchSummary.domNode, "closed");
    },

    editButtonListener: function () {
      // summary:
      //		Hide summary on click
      var searchSummary = this;
      on(searchSummary.domNode, ".search-edit:click", function (event) {
        dojo.stopEvent(event);
        dojo.publish("tui.searchResults.view.InfoPopup.close");
        searchSummary.searchPanelModel.set("view", "search");
      });
    },

    handleSummaryData: function () {
      // summary:
      //		Collect data from model and insert to html
      var searchSummary = this,
		 newDurationText = "";
      var jsnObj = localStorage[dojoConfig.site+ "/search/main"] ? JSON.parse(localStorage[dojoConfig.site+ "/search/main"]) : null;
      if( jsnObj ){
			newDurationText = dojo.query("select#howLong option[value='"+jsnObj.duration+"']")[0].innerHTML;
		}
      // set text on targets
      searchSummary.whereFromNode.innerHTML = searchSummary.formatMultifieldSummary("from");
      dojo.query(".where-from-summary-print", searchSummary.domNode)[0].innerHTML = searchSummary.getPrintSummary("from");
      searchSummary.whereToNode.innerHTML = searchSummary.formatMultifieldSummary("to");
      dojo.query(".where-to-summary-print", searchSummary.domNode)[0].innerHTML = searchSummary.getPrintSummary("to");
      // show flexible notifier with date if flexible === true
      dojo.query(".when-summary", searchSummary.domNode).text(searchSummary.searchPanelModel.get("date") +
          (searchSummary.searchPanelModel.flexible ? (" (+/- " + searchSummary.searchPanelModel.flexibleDays + " days)") : ""));
      dojo.query(".total-duration", searchSummary.domNode)[0].innerHTML = newDurationText;
      //dojo.query(".total-duration", searchSummary.domNode).text(searchSummary.searchPanelModel.get("duration"));
      dojo.query(".total-adults", searchSummary.domNode).text(searchSummary.searchPanelModel.get("adults"));
      dojo.query(".total-seniors", searchSummary.domNode).text(searchSummary.searchPanelModel.get("seniors"));
      dojo.query(".total-children", searchSummary.domNode).text(searchSummary.searchPanelModel.get("children"));

      var whereFromCount = dojo.query('.more-count', searchSummary.whereFromNode)[0],
          whereToCount = dojo.query('.more-count', searchSummary.whereToNode)[0];
      whereFromCount ? searchSummary.tagElement(whereFromCount, 'Airport From X More') : null;
      whereToCount ? searchSummary.tagElement(whereToCount, 'Destination To X More') : null;
    },

    getPrintSummary: function (fieldName) {
      var searchSummary = this, text = [];

      // no entries in field, show placeholder
      if (searchSummary.searchPanelModel[fieldName].query().total === 0) {
        return searchSummary.searchMessaging[fieldName].placeholder;
      }

      _.each(searchSummary.searchPanelModel[fieldName].getStorageData(["name"]), function (item, index) {
    	  if(searchSummary.tuiConfig[dojoConfig.site] && dojoConfig.dualBrandSwitch) {
    	  labelValue = searchSummary.tuiConfig[dojoConfig.site].dualBrandConfig.optionLabel[item.name.toLowerCase().replace(/\s/g,"")];
	    	  if(labelValue){
	    		  item.name = labelValue.toUpperCase();
	    	  }
    	  }
    	  if(dojoConfig.site == "thomson"){
			  if((item.name == "Sensimar")){
    		  item.name = "COUPLES/SENSIMAR";
    	  }
		  }
        text.push(item.name);
      });

      return text.join(', ');
    },

    formatMultifieldSummary: function (fieldName) {
      // summary:
      //		Formats whereFrom/whereTo fields with appropriate summary
      var searchSummary = this,
      	  labelValue = "",
          count,
          text;

      // no entries in field, show placeholder
      if (searchSummary.searchPanelModel[fieldName].query().total === 0) {
        return searchSummary.searchMessaging[fieldName].placeholder;
      }

      count = searchSummary.searchPanelModel[fieldName].summariseCount();
      text = searchSummary.searchPanelModel[fieldName].getStorageData(["name"])[0].name;
	  

      if(searchSummary.tuiConfig[dojoConfig.site] && dojoConfig.dualBrandSwitch) {
    	  labelValue = searchSummary.tuiConfig[dojoConfig.site].dualBrandConfig.optionLabel[text.toLowerCase().replace(/\s/g,"")];
    	  if(labelValue){
    		  text = labelValue.toUpperCase();
    	  }
      }

      if ((text == "Sensimar" && dojoConfig.site == "thomson")){
    	  text = "COUPLES/SENSIMAR";
      }

      // if entries > 1, indicate number of hidden entries
      if (searchSummary.searchPanelModel[fieldName].query().total > 1) {
        text = text + "... <span class='more-count'>+" + count + " more</span>";
      }
      return text;
    }

  });

  return tui.searchBPanel.view.SearchSummary;
});