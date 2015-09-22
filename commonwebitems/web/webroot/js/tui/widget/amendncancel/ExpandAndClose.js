define('tui/widget/amendncancel/ExpandAndClose', [
  'dojo',
  'dojo/on',
  'dojo/dom-class',
  'dojo/dom-style',
  'dijit/_Widget'], function(dojo, on, domClass, domStyle) {

  dojo.declare('tui.widget.amendncancel.ExpandAndClose', [dijit._Widget], {


      //------------------------------------------------------properties

      //-------------------------------------------------------methods

      postCreate: function() {
          var widget = this;

          on(dojo.query('.hide-all', widget.domNode), 'click', function (e){

              dojo.forEach(
                  dojo.query(".item", widget.domNode),
                  function(content){
                      if(domClass.contains(content, "open")) {
                          dojo.removeClass(content, "open");
                      }
                  }
              );
              dojo.forEach(
                  dojo.query(".item-content", widget.domNode),
                  function(content){
                      domStyle.set(content, "display", "none");
                  }
              );
                 // all sub expands also to hide
              dojo.forEach(
                  dojo.query(".sub-item", widget.domNode),
                  function(content){
                      if(domClass.contains(content, "open")) {
                          dojo.removeClass(content, "open");
                      }
                  }
              );
              dojo.forEach(
                  dojo.query(".sub-item-content", widget.domNode),
                  function(content){
                      domStyle.set(content, "display", "none");
                  }
              );
          });

          on(dojo.query('.expand-all', widget.domNode), 'click', function (e){

              dojo.forEach(
                  dojo.query(".item", widget.domNode),
                  function(content){
                      if(!domClass.contains(content, "open")) {
                          dojo.addClass(content, "open");
                      }
                  }
              );
              dojo.forEach(
                  dojo.query(".item-content", widget.domNode),
                  function(content){
                      domStyle.set(content, "display", "block");
                  }
              );
              // all sub expands also to show
              dojo.forEach(
                  dojo.query(".sub-item", widget.domNode),
                  function(content){
                      if(!domClass.contains(content, "open")) {
                          dojo.addClass(content, "open");
                      }
                  }
              );
              dojo.forEach(
                  dojo.query(".sub-item-content", widget.domNode),
                  function(content){
                      domStyle.set(content, "display", "block");
                  }
              );
          });
      }

  });
    return tui.widget.amendncancel.ExpandAndClose;
});


