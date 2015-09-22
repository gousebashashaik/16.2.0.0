define("tui/widget/form/MultiFieldTextbox", ["dojo"], function(dojo) {

	dojo.declare("tui.widget.form.MultiFieldTextbox", null, {

		// summary:
		//		Class for creating a Textbox object.
		//
		// description:
		//		A model which represents an entry in the multiFieldList.
		//
		// @author: Maurice Morgan.

		// ---------------------------------------------------------------- multifieldtextbox Properties

		label : null,

		value : null,

		name : null,

		removeConnect : null,

		domNode : null,

		display : true,

		activeTextboxClass : "selected",

		// ---------------------------------------------------------------- constructor Methods

		constructor : function(args) {
			var multifieldtextbox = this;
			dojo.mixin(multifieldtextbox, args);
		},

		// ---------------------------------------------------------------- multifieldtextbox Methods
		setName : function(name) {
			var multifieldtextbox = this;
			multifieldtextbox.name = name;
		},

		getName : function() {
			var multifieldtextbox = this;
			return multifieldtextbox.name;
		},

		setLabel : function(label) {
			var multifieldtextbox = this;
			multifieldtextbox.label = label;
		},

		getLabel : function() {
			var multifieldtextbox = this;
			return multifieldtextbox.label;
		},

		setValue : function(value) {
			var multifieldtextbox = this;
			multifieldtextbox.value = value;
		},

		getValue : function() {
			var multifieldtextbox = this;
			return multifieldtextbox.value;
		},

		create : function(editTextField) {
			var multifieldtextbox = this;

			//TODO: move to a template

			multifieldtextbox.domNode = dojo.create("div", null, editTextField, "before");

			var display = (multifieldtextbox.display) ? "block" : "none";
			dojo.style(multifieldtextbox.domNode, "display", display);
			dojo.style(multifieldtextbox.domNode, "float", "left");
			dojo.addClass(multifieldtextbox.domNode, "textbox");

			dojo.connect(multifieldtextbox.domNode, "onclick", function(event) {
				dojo.publish("tui/widget/Textbox/onclick", [editTextField, multifieldtextbox]);
			});

			multifieldtextbox.domNode.innerHTML = "<span>" + multifieldtextbox.getLabel() + "</span>";

            // no hash for href due to needing to remove stopEvent
			var a = dojo.create("a", {
				href : 'javascript:void(0);',
				innerHTML : "delete",
                tabIndex: "-1",
                "class": "sprite-img-grp-1"
			}, multifieldtextbox.domNode);

			multifieldtextbox.removeConnect = dojo.connect(a, "onclick", function(event) {
				//dojo.stopEvent(event);
				dojo.publish("tui/widget/Textbox/onremove", [editTextField, multifieldtextbox]);
			});

            // TODO: this isn't being used, not sure if it's still needed
			/*var hiddenInput = dojo.create("input", {
				type : 'hidden',
				value : multifieldtextbox.getValue(),
				name : multifieldtextbox.getName()
			}, multifieldtextbox.domNode);

			dojo.attr(hiddenInput, "data-label", multifieldtextbox.getValue());*/
		},

		remove : function() {
			var multifieldtextbox = this;
			dojo.disconnect(multifieldtextbox.removeConnect);
			dojo.destroy(multifieldtextbox.domNode);
		}
	});

	return tui.widget.form.MultiFieldTextbox;
})