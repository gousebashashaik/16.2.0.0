define('tui/widget/common/ToolTip', [
  'dojo',
  'dojo/on',
  'dojo/dom-attr',
  'dojo/text!tui/widget/common/templates/tooltip.html',
  'dojo/dom-class',
  'tui/common/TemplateUtil',
  'dojo/_base/window',
  'dojo/dom-construct',
  'dojo/dom-style',
  'dojo/dom-geometry',
  'dojo/_base/connect',
  'tui/widget/mobile/Widget'], function (dojo, on, domAttr, tooltipTemplate, domClass, templateUtil, win, domConstruct, domStyle, geom, connect){

  function calculateOffset(){
    var widget = this;
    var pos = geom.position(widget.domNode);
    var subjectPos = geom.position(widget.tipDom);
    var top = null;
    var left = null;
    switch (widget.position) {
      case "top":
        top = _.pixels(Math.ceil(pos.y + win.global.scrollY + pos.h));
        left = _.pixels(Math.ceil((pos.x + pos.w / 2) - (subjectPos.w / 2)));
        break;
      case "top left":
        top = _.pixels(Math.ceil(pos.y + win.global.scrollY + pos.h));
        left = _.pixels(Math.ceil((pos.x + pos.w / 2) - (subjectPos.w / 2) + (subjectPos.w / 4)));
        break;
      case "top right":
        top = _.pixels(Math.ceil(pos.y + win.global.scrollY + pos.h));
        left = _.pixels(Math.ceil((pos.x + pos.w / 2) - (subjectPos.w / 2) - (subjectPos.w / 4)));
        break;
      case "bottom":
        top = _.pixels(Math.ceil((pos.y + win.global.scrollY) - (pos.h + widget.offsetY)));
        left = _.pixels(Math.ceil((pos.x + pos.w / 2) - (subjectPos.w / 2)));
        break;
      case "bottom left":
        top = _.pixels(Math.ceil((pos.y + win.global.scrollY) - (pos.h + widget.offsetY)));
        left = _.pixels(Math.ceil((pos.x + pos.w / 2) - (subjectPos.w / 2) + (subjectPos.w / 4)));
        break;
      case "bottom right":
        top = _.pixels(Math.ceil((pos.y + win.global.scrollY) - (pos.h + widget.offsetY)));
        left = _.pixels(Math.ceil((pos.x + pos.w / 2) - (subjectPos.w / 2) - (subjectPos.w / 4)));
        break;
    }
    return {"left": left, "top": top};
  }

  dojo.declare('tui.widget.common.ToolTip', [tui.widget.mobile.Widget], {

    position: 'top',

    text: null,

    lists: null,
    
    comment:null,

    listClass: null,

    tipDom: null,

    offsetX: 21,

    offsetY: 70,

    offset: null,

    listHtml: null,

    continuouslyAdapt: true,

    onMobile: function (){
      var widget = this;
      widget.renderTip();
      widget.offset = calculateOffset.bind(widget);
    },

    onDesktop: function (){
      var widget = this;
      widget.renderTip();
      widget.offset = calculateOffset.bind(widget);
    },

    onMiniTablet: function (){
      this.onDesktop()
    },

    renderTip: function (){
      var widget = this;
      if (widget.tipDom) {
        dojo.destroy(widget.tipDom);
      }
      widget.tipDom = domConstruct.place(templateUtil.render(tooltipTemplate,
        {'position': widget.position, 'text': widget.text, 'list': widget.lists, 'listClass': widget.listClass, 'comment': widget.comment}), dojo.byId('page'), 'last');
    },

    showTip: function (){
      var widget = this;

      function show(dom){
        domStyle.set(dom, 'display', 'block');
        var offset = widget.offset();
        domStyle.set(dom, 'top', offset.top);
        domStyle.set(dom, 'left', offset.left);
      }

      widget.tipDom ? show(widget.tipDom) : [widget.renderTip(), show(widget.tipDom)];
    },

    hideTip: function (){
      var widget = this;
      widget.tipDom ? domStyle.set(widget.tipDom, 'display', 'none') : null;
    },

      /**
       * Add ability to reference a domId by passing refId as a parameter to text
       *
       * @example: text: { refId : 'domNodeID' }
       *
       * @extends: tui.widget._TuiBaseWidget
       */
    postMixInProperties: function () {
        var widget = this;
        if (widget.listHtml && !dojo.isString(widget.listHtml)) {
            widget.lists = dojo.trim(dojo.byId(widget.listHtml.refId).innerHTML);
        }
        widget.inherited(arguments);
    },
    postCreate: function (){
      var widget = this;
      on(widget.domNode, 'click', function (e){
        dojo.stopEvent(e);
        connect.publish("searchResults/tooltip/clear", null);
        widget.showTip();
      });
      on(dojo.byId('page'), 'click', function (e){
        widget.hideTip();
      });

      connect.subscribe("searchResults/tooltip/clear", function (){
        widget.hideTip();
      });

      widget.inherited(arguments);
    }

  });
});
