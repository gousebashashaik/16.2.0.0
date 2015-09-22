define ("tui/widget/Textboxlist", ["dojo", "dojo/text!tui/widget/Templates/Textboxlist.html", 
								   "tui/widget/_TuiBaseWidget", "dijit/_TemplatedMixin", 
								   "tui/widget/form/AutoComplete", "dojo/NodeList-traverse",
								   "tui/widget/Growable", "dojox/string/BidiComplex"], function(dojo, textboxTemplate){
								   	
								   	
	dojo.declare("tui.widget.Textbox", null, {
		
		// summary:
		//		Class for creating a Textbox object. 
		//
		// description:
		//		A model which represents an entry in the textboxlist.  
		//
		// @author: Maurice Morgan.
    	
    	// ---------------------------------------------------------------- textbox Properties
    	
    	label: null,
     
     	value: null,
     	
     	name: null,
     	
     	removeConnect: null,
     	
     	domNode: null,
     	
    	// ---------------------------------------------------------------- constructor Methods
    	
    	constructor: function(args) {
    		var textbox = this;
        	dojo.safeMixin(textbox,args);
    	},
    	
    	// ---------------------------------------------------------------- textbox Methods
    	setName: function(name){
    		var textbox = this;
    		textbox.name = name;
    	},
    	
    	getName: function(){
    		var textbox = this;
    		return textbox.name;
    	},
    	
    	setLabel: function(label){
    		var textbox = this;
    		textbox.label = label;
    	},
    	
    	getLabel: function(){
    		var textbox = this;
    		return textbox.label;
    	},
    	
    	setValue: function(value){
    		var textbox = this;
    		textbox.value = value;
    	},
    	
    	getValue: function(){
    		var textbox = this;
    		return textbox.value;
    	},
    	
    	create: function(editTextField, removeCallback){
    		var textbox = this;
    		textbox.domNode = dojo.create("li", null, editTextField, "before");
    		
    		textbox.domNode.innerHTML = textbox.getLabel();
    		
    		var a = dojo.create("a", {
				href: '#',
				innerHTML: "delete"
			}, textbox.domNode);
    		
    		textbox.removeConnect = dojo.connect(a, "onclick", function(event){
    			//textbox.remove();
    			removeCallback(event);	
    		});
			
			var hiddenInput = dojo.create("input", {
				type  : 'hidden',
				value : textbox.getValue(),
				name  : textbox.getName()
			}, textbox.domNode);
			
			dojo.attr(hiddenInput, "data-label", textbox.getValue());
    	},
    	
    	remove: function(){
    		var textbox = this;
    		dojo.disconnect(textbox.removeConnect);
    		dojo.destroy(textbox.domNode);
    	},
    	
    	addClass: function(classname){
    		var textbox = this;
    		dojo.addClass(textbox.domNode, classname);
    	}
	});
	
	dojo.declare("tui.widget.Textboxlist", [tui.widget._TuiBaseWidget, dijit._TemplatedMixin], {
		
		// summary:
		//		Class for creating a TextboxList object. 
		//
		// description:
		//		  
		//
		// @author: Maurice Morgan.
		
		// ---------------------------------------------------------------- textboxlist Properties
		
		templateString: textboxTemplate,
		
		defaulttextboxes: [],
		
		autocomplete: null,
		
		growable: true,
		
		growableInput: null,
		
		textboxInput: null,
		
		textboxes: null,
		
		textboxvalues: null,
		
		textboxIndex: -1,
		
		autocompleteProps: null,
		
		allowDuplicate: false,	
		
		activeClass: "active",
			
		// ---------------------------------------------------------------- textboxlist methods
		 
		postCreate: function(){
			var textboxlist = this;
			textboxlist.textboxes = [];
			textboxlist.textboxvalues = [];
			if (textboxlist.growable){
				textboxlist.growableInput = tui.widget.Growable(textboxlist.textboxInput);
			}
			textboxlist.buildPreselectionTextboxes();
			textboxlist.attachTextboxlistEventListeners();
			dojo.style(textboxlist.domNode, "display", "block");
			var textbox = null;
			textboxlist.autocompleteProps.onElementListSelection = function(selectedData, listData){
					var autocomplete = this;
					if (selectedData){
						autocomplete.hideList();
						if (!textboxlist.allowDuplicate){
							var index = _.indexOf(textboxlist.textboxvalues, selectedData.value);
							
							if (index > -1) {
								textboxlist.resetTextboxInput();
								return
							};
						}
						
						textbox = new tui.widget.Textbox({
							label : selectedData.title,
							value : selectedData.value,
							name  : textboxlist.name
						})
						textboxlist.addNewTextbox(textbox);
					}
					textboxlist.textboxInput.focus();
			}
			textboxlist.autocompleteProps.elementRelativeTo = textboxlist.domNode;
			textboxlist.autocomplete = new tui.widget.form.AutoComplete(textboxlist.autocompleteProps, textboxlist.textboxInput);
			
			dojo.connect(textboxlist.domNode, "onclick", function(event){
				textboxlist.textboxInput.focus();
			})
			
			dojo.connect(textboxlist.textboxInput, "onfocus", function(event){
				textboxlist.defaulttextboxes.forEach(function(item, index){
					var textbox = textboxlist.textboxes[index]
					if (item.value === textbox.getValue()){
						textboxlist.removeTextbox(textbox)
					}
				})
				textboxlist.resetTextbox();
			})
			
			dojo.connect(textboxlist.textboxInput, "onblur", function(event){
				textboxlist.removeHighlight();
				textboxlist.textboxCompleteTimer = setTimeout(function(){
					clearTimeout(textboxlist.textboxCompleteTimer);
					textboxlist.textboxCompleteTimer = null;
					if (textboxlist.textboxes.length === 0){
						textboxlist.buildPreselectionTextboxes();
					}
				}, 200);
			
			})
			
			dojo.connect(textboxlist.textboxInput, "onkeydown", function(event){
				switch(event.keyCode){
					case dojo.keys.BACKSPACE:
						var position = dojox.string.BidiComplex._getCaretPos(event, this);
						if (position[0] === 0 && textboxlist.textboxIndex > -1){
							textboxlist.removeTextbox(textboxlist.textboxes[textboxlist.textboxIndex])
						}
					break;

					case dojo.keys.LEFT_ARROW:
						var position = dojox.string.BidiComplex._getCaretPos(event, this);
						if (position[0] === 0){
							if (textboxlist.textboxIndex === -1 && textboxlist.textboxes.length > 0){
								textboxlist.textboxIndex = textboxlist.textboxes.length - 1;
							} else if (!textboxlist.isIndexAtStart()){
								textboxlist.textboxIndex--;
							}
							textboxlist.updateTextboxHighlight();
						} else {
							textboxlist.resetTextbox();
						}
					break;
					
					case dojo.keys.RIGHT_ARROW:
						if (textboxlist.textboxIndex !== -1){
							dojo.stopEvent(event);
							textboxlist.textboxIndex++;
							textboxlist.updateTextboxHighlight();
							textboxlist.textboxIndex = (textboxlist.isIndexAtEnd())? -1 : textboxlist.textboxIndex
						} else {
							textboxlist.resetTextbox();
						}
					break;
					default:
						textboxlist.resetTextbox();
				}
			});
		},
		
		updateTextboxHighlight: function(){
			var textboxlist = this;
			textboxlist.removeHighlight();
			textboxlist.addHighlight();
		},
		
		addHighlight: function(){
			var textboxlist = this;
			var textbox = textboxlist.textboxes[textboxlist.textboxIndex];
			if (textbox){
				textbox.addClass(textboxlist.activeClass);
			}
		},
		
		removeHighlight: function(){
			var textboxlist = this;
			dojo.query(['.', textboxlist.activeClass].join(""), 
				textboxlist.domNode).removeClass(textboxlist.activeClass);
		},
		
		resetTextbox: function(){
			var textboxlist = this;
			textboxlist.removeHighlight();
			textboxlist.textboxIndex = -1;
		},
		
		isIndexAtStart: function(){
			var textboxlist = this;
			return (textboxlist.textboxIndex === 0)
		},
		
		isIndexAtEnd: function(){
			var textboxlist = this;
			return (textboxlist.textboxIndex === textboxlist.textboxes.length);
		},
			
		buildPreselectionTextboxes: function(){
			var textboxlist = this;
			var preselected = dojo.query(['input[type="hidden"][name="', textboxlist.name, '"]'].join(""));
			var textbox;
			preselected.forEach(function(element){
				textbox = new tui.widget.Textbox({
					label : dojo.attr(element, "data-label"),
					value :element.value,
					name  : textboxlist.name
				})
				textboxlist.addNewTextbox(textbox);
			})
			if (preselected.length === 0){			
				textboxlist.defaulttextboxes.forEach(function(defaulttextbox){
					defaulttextbox.name = textboxlist.name
					textboxlist.addNewTextbox(defaulttextbox);
				})
			}
		},
		
		attachTextboxlistEventListeners: function(){
			var textboxlist = this;
		},
	
		removeTextbox: function(textbox){
			var textboxlist = this;
			var index = _.indexOf(textboxlist.textboxes, textbox);
			if (index > -1){
				textbox.remove();
				textboxlist.textboxes.splice(index, 1);
				textboxlist.textboxvalues.splice(index, 1);
				textboxlist.textboxInput.focus();
			}
		},
		
		addNewTextbox: function(textbox){
			var textboxlist = this;
			textbox.create(textboxlist.editTextField, function(event){
				dojo.stopEvent(event);
				textboxlist.removeTextbox(textbox);
			})
			textboxlist.textboxes.push(textbox);
			textboxlist.textboxvalues.push(textbox.getValue());
			textboxlist.resetTextboxInput();
			textboxlist.onAddNewTextbox(textbox);

		},
		
		onAddNewTextbox: function(textbox){
				
		},
		
		resetTextboxInput: function(){
			var textboxlist = this;
			textboxlist.textboxInput.value = "";
			if (textboxlist.growable){
				textboxlist.growableInput.reset();
			}
		}
	})
	
	return tui.widget.Textboxlist;
})