define ("tui/widget/FilterPanelModule", ["../.",
										"tui/widget/searchresults/model/SearchResultModel",
										 "tui/widget/form/sliders/Slider",
										 "dojo/NodeList-dom",
										 "dojo/NodeList-traverse",
										 "dojo/string",
										 "dojo/date/locale",
                                         "tui/widget/expand/Expandable"], function(dojo, searchResultModel){


	/***********************************************************************************/
   	/* tui.widget.FilterPanel														   */
   	/***********************************************************************************/
	dojo.declare("tui.widget.FilterPanel", [tui.widget.expand.Expandable], {

		selectionSelector: ".selection",

		clearSelector: ".clear-filter",

		channel: "tui/widget/FilterPanel",

		checkboxes: null,

		defaultOpen: [1],

		postCreate: function(){
			var filterPanel = this;
			filterPanel.inherited(arguments);
			filterPanel.addCheckboxEventListeners();
			filterPanel.addClearEventListeners();
			filterPanel.checkboxes.forEach(function(checkbox, index){
				checkbox.checked = false;
			});

			dojo.query(".opps").onclick(function(event){
				dojo.stopEvent(event);

				filterPanel.clearFilter();
			});

      filterPanel.inherited(arguments);
    },

		clearFilter: function(){
      var filterPanel = this;
			searchResultModel.set("query", {})
				filterPanel.checkboxes.forEach(function(checkbox, index){
					checkbox.checked = false;
				})
			var bSlider = dijit.byId("budgetSlider");
				//bSlider.setStep(512, 0);
				bSlider.setStep(1171, 0);
				bSlider.setSliderTrack();

				var rSlider = dijit.byId("ratingSlider");
				rSlider.setStep(3, 0);
				//rSlider.setStep(5, 1);
				rSlider.setSliderTrack();

				var tSlider = dijit.byId("tripadvisorSlider");
				tSlider.setStep(3, 0);
				//tSlider.setStep(5, 1);
				tSlider.setSliderTrack();
		},

		addCheckboxEventListeners: function(){
			var filterPanel = this;
			filterPanel.checkboxes = dojo.query("input[type=checkbox]", filterPanel.domNode);
			filterPanel.checkboxes.forEach(function(checkbox, index){
				var container = dojo.query(checkbox).parents(filterPanel.transitionOptions.itemSelector)[0];
				var selection = dojo.query(filterPanel.selectionSelector, container)[0];
				filterPanel.connect(checkbox, "onclick", function(event){

					var query = searchResultModel.get("query");
					if(checkbox.checked){
						var data = checkbox.value.split(":");
						var q = [];
						if (query[data[0]]){
							q = query[data[0]].dataSelected;
						}
						q.push(data[1]);

						query[data[0]] = function(item){
							if (dojo.isArray(item)){
								for(var i = 0; i < item.length; i++){
									if (_.indexOf(query[data[0]].dataSelected, item[i]) > -1){
										return true;
									}
								}
								return false;
							}
							return (_.indexOf(query[data[0]].dataSelected, item) > -1);
						}

						query[data[0]].dataSelected = q

						searchResultModel.set("query", query)

					} else {
						var query = searchResultModel.get("query");
						var data = checkbox.value.split(":");
						var q = null;
						if (query[data[0]]){
							q = query[data[0]].dataSelected;
						}

						var index = _.indexOf(query[data[0]].dataSelected, data[1])

						if(index > -1){
							q.splice(index,1);
						}

						if ((q == null) || (q.length === 0)){
							q = []
						}

						query[data[0]] = function(item){
							if (query[data[0]].dataSelected.length === 0){
							 	return true
							}

							if (dojo.isArray(item)){
								for(var i = 0; i < item.length; i++){
									if (_.indexOf(query[data[0]].dataSelected, item[i]) > -1){
										return true;
									}
								}
								return false;
							}

							return (_.indexOf(query[data[0]].dataSelected, item) > -1);
						}

						query[data[0]].dataSelected = q

						searchResultModel.set("query", query)
					}

					if (container){
						var len = dojo.query('input:checked', container).length;
						var action  = (len > 0) ? "block" : "none";
						//dojo.style(selection, "display", action);
						//selection.innerHTML = new String(len);
					}
					filterPanel.onUpdateFilterPanel();
				})
			})
		},

		addClearEventListeners: function(){
			var filterPanel = this;
			dojo.query(filterPanel.clearSelector, filterPanel.domNode)
								  .connect("onclick", function(event){
				dojo.stopEvent(event)
				/*dojo.query(filterPanel.selectionSelector, filterPanel.domNode).style("display", "none");
				filterPanel.checkboxes.forEach(function(checkbox, index){
					checkbox.checked = false;
				})
				searchResultModel.set("query", {})*/
				filterPanel.clearFilter();
			})
		},

		onUpdateFilterPanel: function(){
			var filterPanel = this;
			dojo.publish(filterPanel.channel, ["data from an interesting source"]);
		}
	})


	/***********************************************************************************/
   	/* tui.widget.FilterSlider														   */
   	/***********************************************************************************/
	dojo.declare("tui.widget.FilterSlider", [tui.widget.form.sliders.Slider], {

		filterPanel: null,

		onChange: function(handlePos, value, step){
			var filterSlider = this;
			filterSlider.filterPanel = filterSlider.filterPanel || filterSlider.getParent();
			filterSlider.filterPanel.onUpdateFilterPanel();
		}
	})

	dojo.declare("tui.widget.RatingSlider", [tui.widget.form.sliders.Slider], {
		displayMaker:true,
		snap:true,
		steps:3,
		range:[0,3],

		onEnd: function(handlePos, value, step){
		//	console.log(value);
			var query = searchResultModel.get("query");
			dojo.mixin(query, {
				rating: function(rating){
					return (rating >= value);
				}
			})
			searchResultModel.set("query", query)
		}
	})

	dojo.declare("tui.widget.TripadvisorSlider", [tui.widget.form.sliders.Slider], {
		displayMaker:true,
		snap:true,
		steps:2,
		range:[3,5],
			onEnd: function(handlePos, value, step){
		//	console.log(value);
			var query = searchResultModel.get("query");
			dojo.mixin(query, {
				tripadvisor: function(tripadvisor){
					return (tripadvisor >= value || tripadvisor === 0);
				}
			})
			searchResultModel.set("query", query)
			}
		})

	dojo.declare("tui.widget.BudgetSlider", [tui.widget.FilterSlider], {

		displayMaker: true,

		snap: true,

		steps: 10,

		range:[512, 1171],

		valueTmpl:'&pound;${value}',

		postCreate: function(){
			var budgetSlider = this;
			budgetSlider.inherited(arguments);
			budgetSlider.setStep(budgetSlider.step[1], 0);
			budgetSlider.setSliderTrack();
		},

		onEnd: function(handlePos, value, step){
			var query = searchResultModel.get("query");
			dojo.mixin(query, {
				pp: function(price){
					return (price <= value );
				}
			})
			searchResultModel.set("query", query)
		}
	})

	/***********************************************************************************/
   	/* tui.widget.FilterTimeSlider													   */
   	/***********************************************************************************/
	dojo.declare("tui.widget.FilterTimeSlider", [tui.widget.form.sliders.Slider], {

		filterPanel: null,

		onChange: function(handlePos, value, step){
			var filterTimeSlider = this;
			filterTimeSlider.filterPanel = filterTimeSlider.filterPanel || filterTimeSlider.getParent();
			filterTimeSlider.filterPanel.onUpdateFilterPanel();
		}
	})

	return tui.widget.FilterPanelModule;
})