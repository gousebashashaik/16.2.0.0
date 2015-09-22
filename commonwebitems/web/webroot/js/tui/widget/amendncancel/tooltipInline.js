define("tui/widget/amendncancel/tooltipInline", [
	"dojo",
	"dojo/on",
	"dojo/dom-construct",
	"dojo/dom-style",
	"dojo/query",
	"dojo/text!tui/widget/amendncancel/templates/tooltipInline.html",
	"tui/widget/mixins/Templatable",
	"tui/widget/_TuiBaseWidget"], function (dojo, on, domConstruct, domStyle, query, tooltipInlineTmpl){

	dojo.declare("tui.widget.amendncancel.tooltipInline", [tui.widget.mixins.Templatable,tui.widget._TuiBaseWidget], {

	tmpl: tooltipInlineTmpl,

	text:'',
	
	baseDomNode: null,

	postCreate: function() {
		var widget = this;
		widget.baseDomNode = widget.renderTemplate();
		var trigger = query(".trigger", widget.domNode)[0];
		on(trigger, 'click', function(e) {
			dojo.stopEvent(e);
			widget.clickHandler();
		});
		on(dojo.query("body"), 'click', function(e) {
			widget.tooltipClose();
		});
		if(document.addEventListener) {
			document.body.addEventListener('touchstart', function(e) {
				widget.tooltipClose();
			  });
		}
		
		var otherTooltips = query(".ac-ques-tooltip-link");
		console.log(otherTooltips);
		for (var i=0; i<otherTooltips.length; i++) {
			if(otherTooltips[i] != trigger) {
				on(otherTooltips[i], "click", function(e) {
						widget.tooltipClose();
				});
			}
		}
	},
	renderTemplate: function() {
		var widget = this;
		var html = widget.renderTmpl(widget.tmpl, widget);
		return domConstruct.place(html, widget.domNode, 1);
	},
	clickHandler: function() {
		var widget = this;
		var tooltipDomNode = query(".tooltip", widget.domNode)[0];
		console.log(tooltipDomNode);
		if(domStyle.get(tooltipDomNode, "display") == "none") {
			domStyle.set(tooltipDomNode, "display", "block");
			return;
		}
		else {
			domStyle.set(tooltipDomNode, "display", "none");
			return;
		}
	},
	tooltipClose: function() {
		
		var widget = this;
		var tooltipDomNode = query(".tooltip", widget.domNode)[0];
		if(domStyle.get(tooltipDomNode, "display") == "block") {
			domStyle.set(tooltipDomNode, "display", "none");
			return;
		}
		
	}

	});
	return tui.widget.amendncancel.tooltipInline;
});