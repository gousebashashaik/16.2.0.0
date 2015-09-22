define("tui/cruise/deck/view/DeckCabinOptions", [
    'dojo',
    'dojo/text!tui/cruise/deck/view/templates/deckCabinOptions.html',
    'dojo/on',
    "dojo/query",
    "dojo/dom-attr",
    "dojo/dom-style",
    "dojo/dom-class",
    'tui/widget/_TuiBaseWidget',
    'tui/widget/mixins/Templatable'], function (dojo, tmpl, on, query, domAttr, domStyle, domClass) {
    dojo.declare("tui.cruise.deck.view.DeckCabinOptions", [tui.widget._TuiBaseWidget, tui.widget.mixins.Templatable], {

        data: null,

        tmpl:tmpl,

        postCreate: function () {
            var deckOptions = this;
            if(deckOptions.getParent() != null) {
            	deckOptions.getParent().getParent().deckOptionWgts.push(deckOptions);
            } 
            deckOptions.inherited(arguments);
            dojo.subscribe("tui/widget/popup/cruise/DeckPopup/deckOptions", function (deckData) {
            	deckOptions.updateTemplate(deckData);
            });
        },
    
	    updateTemplate : function (deckData) {
	    	  var deckOptions = this, cabinOptionsMap = {}, option, flag;
	    	  deckOptions.cabinCategoriesRef = deckData.deckData.cabinCategories;
	    	  deckOptions.cabinContentMapRef = deckData.deckData.cabinTypeMap;
	    	  if(!_.isEmpty(deckData.deckData.cabinTypeMap)){
	    		  var html = deckOptions.renderTmpl(null, deckData);
	              deckOptions.domNode.innerHTML = html;
	              _.each(query("input.cabin-checkBox", deckOptions.domNode), function(node){
		              on(node, "click", function(event){
		              	option = domAttr.get(this, 'data-option-name');
		              	flag = this.checked ? true : false;
		              	//showing the loader - need to do 
		               //dojo.publish("tui/cruise/deck/view/DeckInteractiveSVG/loader", );
		              	if(flag){
		              		cabinOptionsMap[option] = "checked";
		              		domClass.add(this.parentNode, "selected");
		              	}else {
		              		cabinOptionsMap[option] = "unchecked";
                            domClass.remove(this.parentNode, "selected");
		              	}
		              	dojo.publish("tui/cruise/deck/view/DeckInteractiveSVG/updateCabinColor", {"deckData": deckData, "bindEvent":deckData.bindEvent, "cabinOptions":cabinOptionsMap,"cabinCategoriesRefAtr":deckOptions.cabinCategoriesRef,"cabinContentMapRefAtr":deckOptions.cabinContentMapRef});
		              });
	              });  
	    	  }else {
	    		  deckOptions.domNode.innerHTML = "";
	    	  }
	    	  
	    }
    });

    return tui.cruise.deck.view.DeckCabinOptions;
});
