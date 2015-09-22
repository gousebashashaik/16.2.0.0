define("tui/widgetFx/paging/Paging", ["dojo", "dojo/_base/fx"], function(dojo, baseFx){
	
	dojo.declare("tui.widgetFx.paging.Paging", null, {
					 
		// Number for paging.
		page: null,
		
	
		currentPage: 1,
		
		// Flag for displaying pagination.
		displayPagination: true,
		
		// Reference to pagination node.
		paginationNode: null,
		
		// Selector in which pagination is in relation to.   
		paginationSelector: null,
		
		// Position of where to place pagination element 
		// in relation to paginationRelToSelector.
		where: "first",
		
		// ---------------------------------------------------------------- methods
		
   		renderPagination: function(/* Object */ renderOptions){
   			// summary:
			//		Method which renders simple pagination for widgets. 
			// description:
			//		Renders pagination based on the page attribute. 
			//		An onclick eventlistener is attached to each pagination element.	
   			var paging = this;
   			renderOptions = renderOptions || {};
   			if (!paging.displayPagination) return;
   			if (paging.page > 1 ){
   				paging.paginationNode = dojo.create("ul", {
   					className: "paging",
   					id: [paging.domNode.id, ".paging"].join("") 
   				})
   				paging.renderPaginationItems();
   				var pageContainer = (paging.paginationSelector === null)
                        ? paging.domNode
                        : dojo.query(paging.paginationSelector, paging.domNode);
   				dojo.place(paging.paginationNode, pageContainer, renderOptions.where);
   			}   	
   		},
   		
   		renderPaginationItems: function(){
   			var paging = this;
   			var className = "active";
   			var href, a, li;
			for (var i = 0; i < paging.page; i++){
				className = (i === 0) ? className : "";
				a = dojo.create("a", {href: "#"})
				li = dojo.create("li", {className: className})
				dojo.place(a, li, "last");
				dojo.place(li, paging.paginationNode, "last");
				paging.addPageEventListeners(li, i);
			}
   		},
   		
   		addPageEventListeners: function(/* DOM element */ element, /* Number */ index){
   			// summary:
			//		Method which attaches an onclick event to given pagination
			//		element. 
			// description:
			//		When pagination element is clicked the index of the element 
			//		which is clicked to passed.	
   			var paging = this;
   			dojo.connect(element, "onclick", function(event){
				dojo.stopEvent(event);
   				paging.onPage(index, element, paging);
			})
   		},
   		
   		onPage: function(index, element, transition){
   			var paging = this;
            index = parseInt(index, 10);
            paging.currentPage = index + 1;
            if (!paging.displayPagination) return;
   			var paginationEle = dojo.query("li", paging.paginationNode).removeClass("active");
   			dojo.addClass(element || paginationEle[index], "active");
   		},
   		
   		showPagination: function(){
   			// summary:
			//		Method which displays the pagination element node
			// description:
			//		Sets the pagination node to block.
   			var paging = this;
   			if (paging.paginationNode){
   				dojo.style(paging.paginationNode, "display", "block");
   			}
   		},
   		
   		hidePagination: function(){
   			// summary:
			//		Method which hides the pagination element node
			// description:
			//		Sets the pagination node to none.
   			var paging = this;
   			if (paging.paginationNode){
   				dojo.style(paging.paginationNode, "display", "none");
   			}
   		}
	})
	
	return tui.widgetFx.paging.Paging;
})
