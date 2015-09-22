define("tui/bookflow/view/BoardBasisSelection", [
  "dojo",
  "dojo/query",
  "dojo/on",
  "dojo/dom-attr",
  "dojo/dom-class",
  "dojo/topic",
  "dojo/_base/lang",
  "dojo/_base/xhr",
  "tui/widget/_TuiBaseWidget"], function (dojo, query, on, domAttr, domClass, topic, lang, xhr) {

  dojo.declare("tui.bookflow.view.BoardBasisSelection", [tui.widget._TuiBaseWidget], {

    // ---------------------------------------------------------------- properties

    selectedID: null,

    itemSelector: '.board-type',

    itemPriceSelector: '.board-type-price',

    boardTypeItems: null,

    boardTypeData: null,

    origUrl: null,

    // dom nodes to manipulate

    panelNode: null,

    totalPriceNode: null,

    ppPriceNode: null,

    iconNode: null,

    btnNode: null,

    greatDealPercentageNode: null,

    greatDealOfferTextNode: null,

    // templates

    itemPriceTmpl: '{prefix}'+dojoConfig.currency+'{price} total',

    ppPriceTmpl: '{price}',

    totalPriceTmpl: dojoConfig.currency+'{price}',

    iconTmpl: '{name} <i></i>',

    // ---------------------------------------------------------------- methods

    postCreate: function() {
      var boardBasis = this;
      boardBasis.inherited(arguments);

      // initialise all nodes TODO: make 'em all widgets and pub/sub ?
      boardBasis.boardTypeData = boardBasis.jsonData.alternateBoardPrice;
      boardBasis.boardTypeItems = query(boardBasis.itemSelector, boardBasis.domNode);
      boardBasis.panelNode = query('.bookflow-accommodation-summary-panel').length ?  query('.bookflow-accommodation-summary-panel')[0] :  query('.summary-panel-accommodation')[0];
      boardBasis.totalPriceNode = query('.price-total', boardBasis.panelNode)[0];
      boardBasis.ppPriceNode = query('.price-value', boardBasis.panelNode)[0];
      boardBasis.iconNode = query('.js-board-basis-feature', boardBasis.panelNode)[0];
      boardBasis.btnNode = query('.cta-buttons .cta', boardBasis.panelNode)[0];
      boardBasis.greatDealPercentageNode = query('.great-deal-percentage', boardBasis.panelNode)[0];
      boardBasis.greatDealOfferTextNode = query('.great-deal-text-off', boardBasis.panelNode)[0];
      boardBasis.origUrl = boardBasis.btnNode.href;

      boardBasis.addEventListeners();

        // making the board basis present in the package as the defaultly selected board basis
      var bbParam =  _.filter(window.location.search.split('&'),function(param){return /^bb=/.test(param)})[0];
      if(bbParam){
    	  var defaultBB = 	bbParam.split("=")[1];
    	  var defalutBBDom =query('li.board-type[data-value='+defaultBB+']', boardBasis.panelNode)[0];
    	  boardBasis.switchBoardBasisDisplay(defaultBB, defalutBBDom);
      }
       else{
      // update nodes with price diffs
      boardBasis.setPriceDiffs(boardBasis.getSelectedData(boardBasis.selectedID));
       }

      boardBasis.showDefaultBoardBasis();
    },

    showDefaultBoardBasis : function(){
    	 var boardBasis = this;
    	 priceCnt = dojo.query(".party-composition-price-info");
    	 dojo.removeClass(priceCnt[0], "hide");
    },


    addEventListeners: function () {
      var boardBasis = this, id, target;
      on(boardBasis.domNode, on.selector(boardBasis.itemSelector, 'click'), function(e){
        target = e.target.tagName.toLowerCase() !== 'li' ? query(e.target).parents('.board-type')[0] : e.target;
        id = domAttr.get(target, 'data-value');
        if(domClass.contains(target, 'active')) return;
        boardBasis.switchBoardBasisDisplay(id, target);
      });
    },

    switchBoardBasisDisplay: function (id, target) {
      var boardBasis = this,
          thisData = boardBasis.getSelectedData(id),
          diffPriceNode = query(boardBasis.itemPriceSelector, target)[0];

      // set active element
      boardBasis.boardTypeItems.removeClass('active');
      domClass.add(target, 'active');

      if(boardBasis.greatDealPercentageNode && boardBasis.greatDealOfferTextNode) {
	      domClass.remove(boardBasis.greatDealPercentageNode, 'hide-block');
	      domClass.remove(boardBasis.greatDealOfferTextNode, 'hide-block');

	      // Show discount percentage only if default board basis is selected
	      _.every(boardBasis.jsonData.alternateBoardPrice, function(boardOption){
	          if (boardOption.boardbasisCode === id) {
	        	  if(!boardOption.defaultBoardBasis) {
	        		  domClass.add(boardBasis.greatDealPercentageNode, 'hide-block');
	        	      domClass.add(boardBasis.greatDealOfferTextNode, 'hide-block');
	        	  }
	        	  return false;
	          }
	          return true;
	      });
      }
      // set active element's price to 'Selected'
      diffPriceNode.innerHTML = 'Selected';

      // publish to tooltips
      topic.publish('tui.bookflow.view.SummaryTooltip.toggleSelf', id);

      // set Prices
      boardBasis.setPrices(thisData.total, thisData.totalPricePP);

      // swap Icon text
      boardBasis.setIcon(thisData.name);

      // calculate price differences and set
      boardBasis.setPriceDiffs(thisData);

      // update cta button url
      boardBasis.setUrl(id);

      //making of ajax

      boardBasis.generateRequest(id);

    },

    getSelectedData: function(id) {
      var boardBasis = this;
      return _.find(boardBasis.boardTypeData, {'boardbasisCode': id});
    },

    setPrices: function(totalPrice, ppPrice) {
      var boardBasis = this;
      boardBasis.totalPriceNode.innerHTML = lang.replace(boardBasis.totalPriceTmpl, {
        price: parseInt(totalPrice, 10)
      });
      boardBasis.ppPriceNode.innerHTML = lang.replace(boardBasis.ppPriceTmpl, {
        price: parseInt(ppPrice, 10)
      });
    },

    setIcon: function(name) {
      var boardBasis = this;
      boardBasis.iconNode.innerHTML = lang.replace(boardBasis.iconTmpl, {
        name: name
      });
    },

    setUrl: function (boardBasisCode) {
      var boardBasis = this;
      boardBasis.btnNode.href = boardBasis.btnNode.href.replace(/boardBasisCode=[a-z,A-Z]{2}/, 'boardBasisCode=' + boardBasisCode);
    },

    setPriceDiffs: function (selectedData) {
      var boardBasis = this, thisData, prefix, thisPriceNode, diff,
          nodesToSet = _.filter(boardBasis.boardTypeItems, function(item){
            return domAttr.get(item, 'data-value') !== selectedData.boardbasisCode;
          });

      _.each(nodesToSet, function(node){
        thisData = _.find(boardBasis.boardTypeData, {'boardbasisCode': domAttr.get(node, 'data-value')});
        thisPriceNode = query(boardBasis.itemPriceSelector, node)[0];

        selectedData.total=parseInt(selectedData.total, 10);
        thisData.total=parseInt(thisData.total, 10);

if (thisData.accomdboardpriceDiffrence && thisData.accomdboardpriceDiffrence > 0 && thisData.accomdboardpriceDiffrence < 1) {
           thisData.total = thisData.total + parseFloat(thisData.accomdboardpriceDiffrence );
        }
if (selectedData.accomdboardpriceDiffrence && selectedData.accomdboardpriceDiffrence > 0 && selectedData.accomdboardpriceDiffrence < 1) {
           selectedData.total = selectedData.total + parseFloat(selectedData.accomdboardpriceDiffrence);
        }

        prefix = selectedData.total > thisData.total ? '-' : '+';
        diff = selectedData.total > thisData.total ? (selectedData.total - thisData.total) : (thisData.total - selectedData.total);
           diff = parseFloat(diff).toFixed(2);

        thisPriceNode.innerHTML = lang.replace(boardBasis.itemPriceTmpl, {
          prefix: prefix,
          price: diff
        });
      })
    },
    generateRequest: function(boardBasisCode)
    {
    	var boardBasis = this;
    	//var boadrBasisUrl = "/destinations/updateboard";
    	var boadrBasisUrl = dojoConfig.paths.webRoot+"/updateboard";


    	var results = xhr.post({
            url: boadrBasisUrl,
            content: {boardBasisCode: boardBasisCode},
            handleAs: "json",
            error: function (err) {
              if (dojoConfig.devDebug) {

              }
            }
          });

          dojo.when(results, function (response) {

        	  boardBasis.afterSuccess(response);
          });

    },
    afterSuccess: function(response){
    	console.log(response);
    }

  });

  return tui.bookflow.view.BoardBasisSelection;
});