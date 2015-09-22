define("tui/searchPanel/view/SearchAutoComplete", [
	"dojo",
	"tui/config/TuiConfig",
	"tui/widget/form/AutoComplete"], function (dojo, TuiConfig) {

	dojo.declare("tui.searchPanel.view.SearchAutoComplete", [tui.widget.form.AutoComplete], {

		// ----------------------------------------------------------------------------- properties

		highlightSelectText: false,

		posOffset: {top: 6, left: 0},

		error: null,

		highlightFirstOnShow: false,

		infoPopupTemplate: null,

		scrollerSelector: "",

		tuiConfig : TuiConfig,

		// ----------------------------------------------------------------------------- methods
		postCreate: function () {
			var searchAutoComplete = this;
			searchAutoComplete.inherited(arguments);


			searchAutoComplete.subscribe("tui/searchPanel/searchOpening", function (component) {
				if (component !== searchAutoComplete) {
					searchAutoComplete.hideList();
				}
			});
		},

        /*onBeforeRender: function () {
            var searchAutoComplete = this;
            if(searchAutoComplete.multiField){
                dojo.attr(searchAutoComplete.listElement, "tabindex", dojo.attr(searchAutoComplete.multiField.domNode, "tabindex"));
            }
        },*/

		createListElement: function () {
			// summary:
			//      Extends createListElement method, and adds a css class called "autocomplete" to listElement.
			var searchAutoComplete = this;
			searchAutoComplete.inherited(arguments);
			dojo.addClass(searchAutoComplete.listElement, "ms");
			dojo.place("<span class='arrow'></span>", searchAutoComplete.listElement, "first");
		},

		query: function () {
			// summary:
			//      Extends query to adding loading class.
			var searchAutoComplete = this;
			dojo.addClass(searchAutoComplete.domNode, "loading");
			searchAutoComplete.inherited(arguments);
		},

		createQueryObject: function (element, keycode) {
			// summary:
			//    Create query for airport autocomplete.
			var searchAutoComplete = this;

			var searchquery = searchAutoComplete.searchPanelModel.generateQueryObject();

			var queryObj = {
				'to[]': searchquery.to,
				'from[]': searchquery.from,
				'when': searchquery.date || '',
				'until': searchquery.until || '',
				'flexible': searchquery.flexible,
        'multiSelect': searchquery.multiSelect
			};

			queryObj[searchAutoComplete.searchProperty] = element.value;
			return queryObj;
		},

		onResults: function () {
			// summary:
			//      Extends onResults to removing loading class.
			var searchAutoComplete = this;
			dojo.removeClass(searchAutoComplete.domNode, "loading");
			searchAutoComplete.inherited(arguments);
		},

		onBeforeSetResults: function (dataresults) {
			var searchAutoComplete = this;
			if (dataresults.error) {
				searchAutoComplete.error = {
					code: dataresults.error.code,
					entry: dataresults.error.entry,
					matches: dataresults.error.matches
				};
			}
		},

		createNoMatchMessage: function () {
			var searchAutoComplete = this;
			var template = new dojox.dtl.Template(noAcResultsTmpl);
			var context = new dojox.dtl.Context(searchAutoComplete);
			return template.render(context);
		},

		renderList: function () {
			var searchAutoComplete = this;
			var string = [];
			searchAutoComplete.clearList();
			searchAutoComplete.onBeforeRender(searchAutoComplete, searchAutoComplete.listData);

			if(dojoConfig.site == "thomson"){
			_.forEach(searchAutoComplete.listData, function (item, index) {
				if (item.name == "Sensimar"){
					searchAutoComplete.listData[index].name = "COUPLES/SENSIMAR";
				}
			});
			}
		      if(searchAutoComplete.tuiConfig[dojoConfig.site] && dojoConfig.dualBrandSwitch) {
			      _.each(searchAutoComplete.listData, function (item, index) {
			    	  labelValue = searchAutoComplete.tuiConfig[dojoConfig.site].dualBrandConfig.optionLabel[item.name.toLowerCase().replace(/\s/g,"")];
			    	  if(labelValue){
			    		  searchAutoComplete.listData[index].name = labelValue.toUpperCase();
			    	  }
			      });
		       }

			_.forEach(searchAutoComplete.listData, function (item, index) {
				string.push(item[searchAutoComplete.titleProp]);
				if (item.synonym) {
					string.push(['(', item.synonym, ')'].join(''));
				}
				if (item.parentName && item.type === "HOTEL") {
					string.push([', ',item.parentName].join(''));
				}
				var li = dojo.create("li", {
					innerHTML: searchAutoComplete.onRenderLiContent(string.join(' '), item)
				});
				string.length = 0;
				searchAutoComplete.onBeforeListItemRender(li, item, searchAutoComplete);
				dojo.query(li).data("list-data", searchAutoComplete.createListData(item));
				searchAutoComplete.addToList(li);
				searchAutoComplete._connectEvents(li, index);
				searchAutoComplete.onAfterListItemRender(li, item, searchAutoComplete);
			});
			searchAutoComplete.onAfterRender(searchAutoComplete, searchAutoComplete.listData);
			searchAutoComplete.isScrollable();
		},

		hideList: function () {
			var searchAutoComplete = this;
			// if (searchPanelModel.validRoute) {
			searchAutoComplete.inherited(arguments);
			// }
		}
	});

	return tui.searchPanel.view.SearchAutoComplete;
});
