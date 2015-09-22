define('tui/widget/common/autocomplete/Editor', [
  'dojo',
  'dojo/_base/connect',
  'dojo/dom-class',
  'dojo/on',
  'dojo/dom-construct',
  'dojo/dom-attr',
  'dojo/_base/lang'
], function(dojo, connect, domClass, on, domConstruct, domAttr, lang) {

  var template = '<li><label>{name}</label><span class="hit-area"><span class="caret erase grey"></span></span></li>';

  function Editor(dom, entityStore) {
    var editor = this;
    editor.dom = dom;
    editor.entities = entityStore.query();

    on(dojo.query('.button-container > .done', dom)[0], 'click', function() {
      editor.hide();
    });

    editor.entities.observe(function(entity, removed, added) {
      var container = dojo.query('ul', editor.dom)[0];
      if (added > -1) {
        var element = domConstruct.place(lang.replace(template, entity), container, 'last');
        dojo.connect(dojo.query('.hit-area', element)[0], 'click', function() {
          var finder = dojo.byId('finder').value;
          if (finder === 'holidayfinder') {
            dojo.byId('holidaySearch').disabled = false;
            dojo.byId('adults').disabled = false;
            dojo.byId("children").disabled = false;
          } else if (finder === 'checkprice') {
            dojo.byId('priceSearch').disabled = false;
            dojo.byId('priceadults').disabled = false;
            dojo.byId('pricechildren').disabled = false;
          }
          entityStore.remove(entity.id);
          domConstruct.destroy(element);

          // When there are no airports selected, we close the popup.
          if (!entityStore.query().length) {
            editor.hide();
          }
        });

        dojo.connect(dojo.byId('clearFrom'), 'click', function() {
            var finder = dojo.byId('finder').value;
            var aiportid ="";
            var destinationid = "";
            if (finder === 'holidayfinder') {
                dojo.byId('holidaySearch').disabled = false;
                dojo.byId('adults').disabled = false;
                dojo.byId("children").disabled = false;
                 aiportid = dojo.byId('clearFrom');
                 destinationid = dojo.byId('clearTo');
              } else if (finder === 'checkprice') {
                dojo.byId('priceSearch').disabled = false;
                dojo.byId('priceadults').disabled = false;
                dojo.byId('pricechildren').disabled = false;
				airportlist = dojo.byId('clearFromGetprice');
				destinationlist = dojo.byId('clearToGetprice');
              }

          if (aiportid != null && destinationid != null) {
            aiportid = domAttr.get(aiportid, 'data-airports');
            var a_id = "";
            var a_name = "";
            var a_type = "";
            var a_multiSelect = "";
            if(aiportid != "") {
            var a_split = aiportid.split('|');
            var a_id = a_split[0];
            var a_name = a_split[1];
            var a_type = a_split[2];
            var a_multiSelect = a_split[3];
            }
            destinationid = domAttr.get(destinationid, 'data-destinations');
            d_multi = destinationid.split(',');
            for (var i = 0; i < d_multi.length; i++) {
              var d_split = d_multi[i].split('|');
              var d_id = d_split[0];
              var d_name = d_split[1];
              var d_type = d_split[2];
              var d_multiSelect = d_split[3];
              entityStore.remove(d_id);
              for(var j=0;j<dojo.query('.where-from-summary ul li').length; j++)
        	  {
        	  	if(dojo.query('.where-from-summary ul li label')[j].innerHTML == d_name)
        	  		{
        	  		 domConstruct.destroy(dojo.query('.where-from-summary ul li')[j]);
        	  		}
        	  }

            }
           // var elementD = dojo.query('.where-to-summary li')[0];
           // var elementA = dojo.query('.where-from-summary li')[0];
            //domConstruct.destroy(elementA);
            connect.publish('holidayfinder/airport', [
              {'id': a_id, 'name': a_name, 'type': a_type, 'multiSelect':a_multiSelect}
            ]);
            //entityStore.put({'id': a_id, 'name': a_name, 'type': a_type});
          }
          connect.publish('holidayfinder/whereToSelectDateEmpty', [
                                                    {'id': "dateEmpty"}
                                                  ]);
        });

        dojo.connect(dojo.byId('clearTo'), 'click', function() {
            var finder = dojo.byId('finder').value;
            var aiportid ="";
            var destinationid = "";
            if (finder === 'holidayfinder') {
          dojo.byId('holidaySearch').disabled = false;
          dojo.byId('adults').disabled = false;
          dojo.byId("children").disabled = false;
                 aiportid = dojo.byId('clearFrom');
                 destinationid = dojo.byId('clearTo');
              } else if (finder === 'checkprice') {
                dojo.byId('priceSearch').disabled = false;
                dojo.byId('priceadults').disabled = false;
                dojo.byId('pricechildren').disabled = false;
				airportlist = dojo.byId('clearFromGetprice');
				destinationlist = dojo.byId('clearToGetprice');
              }  
          if (aiportid != null && destinationid != null) {
            aiportid = domAttr.get(aiportid, 'data-airports');
            destinationid = domAttr.get(destinationid, 'data-destinations');
            var d_split = destinationid.split('|');
            var d_id = d_split[0];
            var d_name = d_split[1];
            a_multi = aiportid.split(',');
            for (var i = 0; i < a_multi.length; i++) {
              var a_split = a_multi[i].split('|');
              var a_id = a_split[0];
              var a_name = a_split[1];
              var a_type = a_split[2];
              var a_multiSelect = a_split[3];
              if(a_id == "") {
            	  connect.publish('holidayfinder/removeDestination', [
            	                                            {'id': a_id}
            	                                          ]);
              }
              else {
              entityStore.remove(a_id);
              }
              for(var j=0;j<dojo.query('.where-to-summary ul li').length; j++)
            	  {
            	  	if(dojo.query('.where-to-summary ul li label')[j].innerHTML == a_name)
            	  		{
            	  		 domConstruct.destroy(dojo.query('.where-to-summary ul li')[j]);
            	  		}
            	  }
            }
           // var elementD = dojo.query('.where-to-summary ul li')[0];
            //var elementA = dojo.query('.where-from-summary ul li')[0];
           // domConstruct.destroy(elementD);
            connect.publish('holidayfinder/destination', [
              {'id': d_id, 'name': d_name}
            ]);
            // entityStore.put({'id': a_id, 'name': a_name, 'type': a_type});
          }
         
          connect.publish('holidayfinder/whereFromSelectDateEmpty', [
                                                                   {'id': "dateEmpty"}
                                                                 ]);
          
        });
        

        
        
        
      }
    });

    return this;
  }

  Editor.prototype.show = function() {
    var editor = this;
    domClass.add(editor.dom.parentNode, 'show');
    domClass.add(editor.dom, 'show');

  };

  Editor.prototype.hide = function() {
    var editor = this;
    domClass.remove(editor.dom.parentNode, 'show');
    domClass.remove(editor.dom, 'show');
  };

  return Editor;
});
