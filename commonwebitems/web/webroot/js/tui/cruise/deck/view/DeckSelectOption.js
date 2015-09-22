define("tui/cruise/deck/view/DeckSelectOption", [
    "dojo",
    "dojo/on",
    "dojo/dom-attr",
    "dojo/query",
    "dojo/_base/xhr",
    "dojo/has",
    "dojo/_base/sniff",
    "tui/widget/form/SelectOption"], function (dojo, on, domAttr, query, xhr) {

	var TARGET_URL = dojoConfig.paths.webRoot + "/deckplans";

    dojo.declare("tui.cruise.deck.view.DeckSelectOption", [tui.widget.form.SelectOption], {

        /*index: null,*/

        jsonData : [],

        shipCode: null,

        type: null,
		code: null,

        postCreate: function () {
            var selectOption = this;
            selectOption.inherited(arguments);
            selectOption.listableInit();
            dojo.subscribe("tui/widget/popup/cruise/DeckPopup/updateDropdown", function (decks) {
            	selectOption.shipCode = decks.shipCode ? decks.shipCode : "" ;
            	selectOption.type = decks.type ? decks.type : "" ;
				selectOption.code = decks.code ? decks.code : "" ;
                selectOption.buildListData(decks);
                selectOption.setSelectedValue(decks.key);
            });

            dojo.connect(selectOption.selectDropdown, "onclick", function (event) {
                //dojo.stopEvent(event);
                dojo.publish("tui/widget/form/SelectOption/onclick", [selectOption, event]);
                dojo.addClass(selectOption.selectDropdown, "open");
                selectOption.selectDropdown.focus();
                selectOption.onSelectOptionsClick(event, this);

                if (selectOption.listShowing) {
                    selectOption.hideList();
                    return;
                }

                if (selectOption.fixedWidth) {
                    var width = dojo.style(selectOption.selectDropdown, "width");
                    dojo.style(selectOption.listElement, "width", [width, "px"].join(""));
                }
                selectOption.showList();
            });

            // add/remove focus class on focus/blur
            on(selectOption.selectDropdown, "focus", function (event) {
                dojo.addClass(selectOption.selectDropdown, "focus");
            });

            on(selectOption.selectDropdown, "blur", function () {
                dojo.removeClass(selectOption.selectDropdown, "focus");
            });

            selectOption.subscribe("tui/widget/form/SelectOption/onclick", function (selectOptionObj) {
                var selectOption = this;
                if (selectOption === selectOptionObj) return;
                selectOption.hideList();
            });


            dojo.connect(document.body, "onclick", function (event) {
                if (document.activeElement === selectOption.selectDropdown || !selectOption.listShowing) {
                    return;
                }
                selectOption.hideList();
            });

            selectOption.attachListKeyEvent(selectOption.selectDropdown);
            selectOption.renderList();

            selectOption.inherited(arguments);
            dojo.attr(selectOption.selectDropdown, "tabindex", dojo.attr(selectOption.selectNode, "tabindex") || 0);
            dojo.attr(selectOption.domNode, "tabindex", -1);
            dojo.attr(selectOption.selectNode, "tabindex", -1);
            dojo.subscribe("tui:channel=modalOpening", function () {
                if (selectOption.listShowing) selectOption.hideList();
            });
        },

        buildListData: function (dataresults) {
            var deckSelectOption = this;
            deckSelectOption.listData.length = 0;
            deckSelectOption.parseJsonData(dataresults);
            deckSelectOption.createOptions();
            deckSelectOption.renderList();
        },

        parseJsonData: function (jsonData) {
            var selectOption = this;
            selectOption.jsonData = jsonData;
            if(jsonData.decks instanceof Array ){
            	 _.forEach(jsonData.decks, function (item) {
                     selectOption.listData.push({
                         text: 'DECK '+item.no,
                         value: jsonData.title + item.no,
                         no:   item.no,
                         disabled: false
                     });
                 });
            }else {
            	selectOption.listData.push({
                    text: 'DECK '+jsonData.decks.no,
                    value: jsonData.title + jsonData.decks.no,
                    no:   jsonData.decks.no,
                    disabled: false
                });
            }
        },
        // ----------------------------------------------------------------------------- methods
        onChange: function (name, oldValue, newvalue) {
            var deckSelectOption = this;
            dojo.html.set(deckSelectOption.selectDropdownLabel, newvalue.key);
            for (var i = 0; i < deckSelectOption.selectNode.options.length; i++) {
                if (deckSelectOption.selectNode.options[i].value === newvalue.value) {
                    deckSelectOption.selectNode.options[i].selected = true;
                }
            }
            //Prevent to fire AJAX for "Default" case
            if(deckSelectOption.type != null && deckSelectOption.code != null && oldValue.key != "Default" && newvalue.listData.no != null && deckSelectOption.shipCode!= null ){
            	deckSelectOption.fireAjax(newvalue);
            }
        },

        fetchSVG : function(deckResponse, newvalue){
        	var deckSVGController = this, xhrReq, svgUrl;
        	svgUrl = deckResponse.deckData.svgCdnUrl;
        	//For IE8 and 9, XDomainRequest is used for svgs to avoid access denied issue
        	var invocation = dojo.isIE && dojo.isIE <= 9 ?  new window.XDomainRequest() : new XMLHttpRequest();
				  if(invocation) {
				  invocation.open('GET', svgUrl , true);
				  if( dojo.isIE && dojo.isIE <= 9 ){
					  	//XDomainRequest uses onload instead of onreadystatechange
	            	   invocation.onload = function(){
	            		   dojo.publish("tui/widget/popup/cruise/DeckPopup/deckSVG", {"response":deckResponse, "bindEvent":false, "deckNo": newvalue.listData.no, "SVGEle":invocation.responseText,"responseDeckData":deckResponse.deckData,"responseDeckDataCabinCategories":deckResponse.deckData.cabinCategories,"responseDeckDataFacilityTypeMap":deckResponse.deckData.facilityTypeMap,"responseDeckDataCabinTypeMap":deckResponse.deckData.cabinTypeMap});
	            	   };
				  	}else{
					  invocation.onreadystatechange = function(){
						  if (invocation.readyState==4 && invocation.status==200){
							  dojo.publish("tui/widget/popup/cruise/DeckPopup/deckSVG", {"response":deckResponse, "bindEvent":false, "deckNo": newvalue.listData.no, "SVGEle":invocation.responseText,"responseDeckData":deckResponse.deckData,"responseDeckDataCabinCategories":deckResponse.deckData.cabinCategories,"responseDeckDataFacilityTypeMap":deckResponse.deckData.facilityTypeMap,"responseDeckDataCabinTypeMap":deckResponse.deckData.cabinTypeMap});
						  }
					  };
				   }
				   invocation.send();
				   console.log(invocation);
				}
        },

        fireAjax : function(newvalue) {
        	var deckSelectOption = this, xhrReq;
        	xhrReq = xhr.get({
                url: TARGET_URL+"?shipCode="+deckSelectOption.shipCode+"&deckNo="+newvalue.listData.no+"&type="+deckSelectOption.type+"&code="+deckSelectOption.code,
                handleAs: "json",
                load: function (response, options) {
                    dojo.publish("tui/widget/popup/cruise/DeckPopup/deckTitle", {"deckData":response.deckData, "deckNo": newvalue.listData.no});
                    dojo.publish("tui/widget/popup/cruise/DeckPopup/deckSummary", {"deckData":response.deckData, "deckNo":newvalue.listData.no});
                    dojo.publish("tui/widget/popup/cruise/DeckPopup/deckLegend", {"deckData":response.deckData.cabinCategories, "flag": false});
                    dojo.publish("tui/widget/popup/cruise/DeckPopup/deckFacility", {"deckData":response.deckData, "deckNo": newvalue.listData.no, "flag": true});
                    dojo.publish("tui/widget/popup/cruise/DeckPopup/deckOptions", {"deckData":response.deckData, "bindEvent":false, "deckNo": newvalue.listData.no});
                    dojo.publish("tui.widget.popup.cruise.DeckPopup.resize");
                    if( dojo.isIE ){
               		 dojo.isIE > 8 ? deckSelectOption.fetchSVG(response, newvalue) :
               			dojo.publish("tui/widget/popup/cruise/DeckPopup/deckSVG", {"response":response, "bindEvent":false, "deckNo": newvalue.listData.no, "SVGEle":'',"responseDeckData":response.deckData,"responseDeckDataCabinCategories":response.deckData.cabinCategories,"responseDeckDataFacilityTypeMap":response.deckData.facilityTypeMap,"responseDeckDataCabinTypeMap":response.deckData.cabinTypeMap});
	               	}else{
	               		deckSelectOption.fetchSVG(response, newvalue);
	               	}
                },
                error: function (err) {
                	console.log("AJAX Error message: "+error);
                }
            });
        }
    });

    return tui.cruise.deck.view.DeckSelectOption;
});