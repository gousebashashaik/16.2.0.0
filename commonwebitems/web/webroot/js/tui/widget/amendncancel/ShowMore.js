define('tui/widget/amendncancel/ShowMore', [
  'dojo',
  'dojo/on',
  'dojo/dom-class',
  'dijit/_Widget'], function(dojo, on, domClass) {

  dojo.declare('tui.widget.amendncancel.ShowMore', [dijit._Widget], {


      //------------------------------------------------------properties

      targetClass: null,

      textLess:null,

      textMore: null,
      //-------------------------------------------------------methods

      postCreate: function() {
          var widget = this;

          on(dojo.query('.more-less', widget.domNode), 'click', function (e){

              dojo.forEach(
                  dojo.query("." + widget.targetClass, widget.domNode),
                  function(content){
                      if(domClass.contains(content, "hide")) {
                          dojo.removeClass(content, "hide");
                          dojo.query('.more-less', widget.domNode)[0].innerHTML = widget.textLess;
                      }
                      else {
                          dojo.addClass(content, "hide");
                          dojo.query('.more-less', widget.domNode)[0].innerHTML = widget.textMore;
                      }
                  }
              );
          });
      }

  });
    return tui.widget.amendncancel.ShowMore;
});


