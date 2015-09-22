define("tui/filterBPanel/view/options/Destination", [
  "dojo/_base/declare",
  "dojo/text!tui/filterBPanel/view/templates/destinationFilters.html",
  "dojo/_base/lang",
  "dojo/dom-construct",
  "dojo/dom-attr",
  "tui/filterPanel/view/options/OptionsFilter"], function (declare, tmpl, lang, domConstruct, domAttr) {

  return declare("tui.filterBPanel.view.options.Destination", [tui.filterBPanel.view.options.OptionsFilter], {

    dataPath: 'destinationOptions',

    visibilityKey : 'destinationOptionsFilter',

    tmpl : tmpl,

    columns : 3,

    buildFilterLabel: function(data){
    	var widget = this,
    		labelValue = "";

    	if(data.filters){
            _.each(data.filters, function (filter) {
              _.each(filter.values, function (parent) {
                parent.labelName = parent.name;
                if(widget.tuiConfig[dojoConfig.site] && dojoConfig.dualBrandSwitch){
  	          	  labelValue = widget.tuiConfig[dojoConfig.site].dualBrandConfig.optionLabel[parent.name.toLowerCase().replace(/\s/g,"")];
  	          	  if(labelValue){
  	          		parent.labelName = labelValue;
  	          	  }
                }
                _.each(parent.children, function (child) {
              	  child.labelName = child.name;
              	  if(widget.tuiConfig[dojoConfig.site] && dojoConfig.dualBrandSwitch){
  	            	  labelValue = widget.tuiConfig[dojoConfig.site].dualBrandConfig.optionLabel[child.name.toLowerCase().replace(/\s/g,"")];
  	            	  if(labelValue){
  	            		  child.labelName = labelValue;
  	            	  }
              	  }
                });
              });
            });
            }
        else{
          _.each(data.values, function (parent) {
              parent.labelName = parent.name;
              if(widget.tuiConfig[dojoConfig.site] && dojoConfig.dualBrandSwitch){
  	    	  labelValue = widget.tuiConfig[dojoConfig.site].dualBrandConfig.optionLabel[parent.name.toLowerCase().replace(/\s/g,"")];
  	    	  if(labelValue){
  	    		parent.labelName = labelValue;
  	    	  }
              }
            _.each(parent.children, function (child) {
          	  child.labelName = child.name;
          	  if(widget.tuiConfig[dojoConfig.site] && dojoConfig.dualBrandSwitch){
  	        	  labelValue = widget.tuiConfig[dojoConfig.site].dualBrandConfig.optionLabel[child.name.toLowerCase().replace(/\s/g,"")];
  	        	  if(labelValue){
  	        		  child.labelName = labelValue;
  	        	  }
          	  }
            });
          });
        }

    },

    draw: function (data) {
      var destination = this;
      var wrap = dojo.query(".item-content-wrap", destination.domNode),
      placeNode = wrap.length ? wrap[0] : destination.domNode;
      destination.buildFilterLabel(data);
	  if(data.name == 'Destination Options') {
          var destinations = JSON.parse(JSON.stringify(data));
			var child = 0;
			 var totalChildrens = 0;
			 _.each(destinations.filters[0].values ,function (object){
			 child = object.children.length;
			 totalChildrens += child;
         });
          var totalCount = destinations.filters[0].values.length + totalChildrens;
		  var maxColumnLength = Math.ceil(totalCount/destination.columns);
		  var newFilters = [];
		  for (i=1 ; i<=destination.columns; i++){
			  var columni= [];
			  newFilters.push(columni);
		  }
		  var newData = dojo.clone(data);
		  var count = 0;
		  var extraitems = 0;
		  var columnFilledLength =0;
		  var currentCol = 1;
		  for(var i in newData.filters[0].values) {
			var item = newData.filters[0].values[i];
			count += Number(newData.filters[0].values[i].children.length) + Number(1);
			if(columnFilledLength < maxColumnLength) {
				if(count > maxColumnLength && (count - maxColumnLength != item.children.length)){
					 var cloneItem = dojo.clone(item);
					 var extraItems = count - maxColumnLength;
					 cloneItem.children = item.children.splice(item.children.length - extraItems, extraItems);
					 cloneItem.subsequentChildGroup = true;
				 }

				if(count > maxColumnLength){
					if(cloneItem){
						newFilters[0].push(item);
						if(cloneItem.children.length > maxColumnLength){
							 var cloneItemTwo = dojo.clone(cloneItem);
							 extraItems = cloneItem.children.length - maxColumnLength;
							cloneItemTwo.children = cloneItem.children.splice(cloneItem.children.length - extraItems, extraItems);
							cloneItemTwo.subsequentChildGroup = true;
							newFilters[1].push(cloneItem);
							newFilters[2].push(cloneItemTwo);
						}
						else{
							newFilters[1].push(cloneItem);
						}
					}
					else{
						newFilters[1].push(item);
					}
				}
				else{
					newFilters[0].push(item);
				}
				columnFilledLength = count;
			}
			else if(columnFilledLength >= maxColumnLength && columnFilledLength < ((maxColumnLength * 2))) {
				cloneItem = null;
				if(count > 2*maxColumnLength && (count - 2*maxColumnLength != item.children.length)){
					 cloneItem = dojo.clone(item);
					 extraItems = count - 2*maxColumnLength;
					 cloneItem.children = item.children.splice(item.children.length - extraItems, extraItems);
					 cloneItem.subsequentChildGroup = true;
				 }


				if(count > 2*maxColumnLength){
					if(cloneItem){
						newFilters[1].push(item);
						newFilters[2].push(cloneItem);
					}else{
						newFilters[2].push(item);
					}
				}
				else
					{
					newFilters[1].push(item);

					}
				columnFilledLength = count;
			}
			else {
				newFilters[2].push(item);
				columnFilledLength = count;
			}
		}
	 }
      var template = new dojox.dtl.Template(destination.tmpl);
      if(newData.name == 'Destination Options') {
      	var context = new dojox.dtl.Context(lang.mixin(data, { totalOptions:newFilters }));
          var html = template.render(context);
          domConstruct.place(html, placeNode, "only");
      }
      destination.inherited(arguments);
      delete data.totalOptions;
      destination.defineNumber();
      _.each(dijit.findWidgets(destination.domNode), function (w) {
        w.tag = destination.tagMappingTable.table[destination.declaredClass];
        domAttr.set(w.domNode, 'analytics-id', w.tag);
        domAttr.set(w.domNode, 'analytics-instance', destination.number);
        if(w.model != undefined)
        domAttr.set(w.domNode, 'analytics-text', w.model.name);
      });
    }

  });
});