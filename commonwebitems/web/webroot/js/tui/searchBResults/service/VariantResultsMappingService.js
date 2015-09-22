define("tui/searchBResults/service/VariantResultsMappingService", ["dojo/_base/lang"], function (lang) {

  function mapFilters (previousFilters, responseFilters, newValue) {
    var checkBox, checkBoxChild, inGroup = false;

    _.each(previousFilters, function(filterGroup, key){
      if(_.has(filterGroup, 'filters')) {
        _.each(filterGroup.filters, function(filter, i){
          if (_.has(filter, "filterType")) {
            // Checkboxes
            // If entire group is missing, fill it in
            if(!responseFilters[key].filters[i]) {
              responseFilters[key].filters.splice(i, 0, filter);
            }
            // Determine if filtering is triggered by any of group members
            inGroup = _.any(filter.values, function(subFilter){
              if(subFilter.id === newValue.checkId) return true;
              if(subFilter.children.length) {
                return _.any(subFilter.children, function(child){
                  return child.id === newValue.checkId;
                });
              }
            });
            _.each(filter.values, function(subFilter, ii){
              // Is this filter in the response?
              checkBox = _.find(responseFilters[key].filters[i].values, {id: subFilter.id});
              if(!checkBox) {
                // Not in the response so map it in from previous set
                responseFilters[key].filters[i].values.splice(ii, 0, lang.mixin(subFilter, {
                  disabled: inGroup ? false : subFilter.selected ? false : _.has(newValue, 'checkId') ? subFilter.id !== newValue.checkId : true,
                  selected: subFilter.selected ? true : _.has(newValue, 'checkId') ? subFilter.id === newValue.checkId : false
                }));
              } else {
                // In the response, update selected/disabled state as needed
                checkBox = lang.mixin(checkBox, {
                  disabled: inGroup ? false : subFilter.selected ? false : subFilter.disabled || false,
                  selected: subFilter.selected ? true : _.has(newValue, 'checkId') ? subFilter.id === newValue.checkId : false
                });
              }
              if (subFilter.children.length) {
                _.each(subFilter.children, function(child, iii){
                  checkBoxChild = _.find(responseFilters[key].filters[i].values[ii].children, {id: child.id});
                  if(!checkBoxChild) {
                    responseFilters[key].filters[i].values[ii].children.splice(iii, 0, lang.mixin(child, {
                      disabled: inGroup ? false : child.selected ? false : subFilter.disabled || true,
                      selected: child.selected ? true : _.has(newValue, 'checkId') ? subFilter.id === newValue.checkId || child.id === newValue.checkId : false
                    }));
                  } else {
                    checkBoxChild = lang.mixin(checkBoxChild, {
                      disabled: inGroup ? false : child.selected ? false : subFilter.disabled || false,
                      selected: child.selected ? true : _.has(newValue, 'checkId') ? subFilter.id === newValue.checkId || child.id === newValue.checkId : false
                    });
                  }
                  //console.log(_.find(responseFilters[key].filters[i].values[ii].children, {id: child.id}))
                });
              }
            });
          } /*else {
            // Sliders
            console.log('slider: ' + filter.id)
          }*/
        });
      }
    });
  }

  return {
    mapResponse: function (previousData, responseData, field, newValue) {
      var hasFilters = responseData.searchResult.filterPanel && previousData.searchResult.filterPanel;

      // Duration selections are never returned so clone from model
      //responseData.searchResult.durationSelection.defaultDisplay = lang.clone(previousData.searchResult.durationSelection.defaultDisplay);
      //responseData.searchResult.durationSelection.moreChoices = lang.clone(previousData.searchResult.durationSelection.moreChoices);

      /*if((field !== 'duration' || field !== 'rooms') && hasFilters) {
        mapFilters(previousData.searchResult.filterPanel, responseData.searchResult.filterPanel, newValue);
      }*/
     
     var repaint = hasFilters ? responseData.searchResult.filterPanel.repaint : '';
      
      // return early if request is new duration or rooms as this is a new search
      if (field === 'duration' || field === 'rooms' || repaint) {
        return responseData;
      }

      // update response > fill in missing data from model
      if (hasFilters) {
        responseData.searchResult.filterPanel.totalbudget = lang.clone(previousData.searchResult.filterPanel.totalbudget);
        responseData.searchResult.filterPanel.ppbudget = lang.clone(previousData.searchResult.filterPanel.ppbudget);
      }

      return responseData;
    }
  };

});