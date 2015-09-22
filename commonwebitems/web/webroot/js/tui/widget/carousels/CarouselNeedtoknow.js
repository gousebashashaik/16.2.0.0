define(
		"tui/widget/carousels/CarouselNeedtoknow",
		"dojo dojo/query tui/widget/_TuiBaseWidget tui/widgetFx/Transitiable dojox/dtl dojo/cache dojox/dtl/Context dojox/dtl/tag/logic tui/widgetFx/Slide"
				.split(" "), function(b) {
			b.declare("tui.widget.carousels.CarouselNeedtoknow", [
					tui.widget._TuiBaseWidget, tui.widgetFx.Transitiable ],
					{
						tmplPath : "",
						tmpl : null,
						transitionType : "Slide",
						transitionOptions : {
							duration : 300
						},
						slideSelector : "ul.plistt",
						itemSelector : "\x3e li",
						jsonDataPostLoaded : !1,
						displayPagination : false,
						pageAmount : 1,
						controlTmpl : null,
						dynamicHeight : !0,
						controlsInViewport : !0,
						postCreate : function() {
							this.inherited(arguments);
							var a = b.style(this.domNode, "display");
							b.style(this.domNode, "display", "block");
							this.setupTransition();
							this.addNewContent();
							b.style(this.domNode, "display", a);
							this.tagCarousel();
						},
						setupTransition : function() {
							var a = this;
							a.onBeforeSetupTransition();
							b.mixin(a.transitionOptions, {
								domNode : a.domNode,
								slideSelector : a.slideSelector,
								itemSelector : a.itemSelector,
								displayPagination : a.displayPagination,
								pageAmount : a.pageAmount,
								controlTmpl : a.controlTmpl,
								dynamicHeight : a.dynamicHeight,
								controlsInViewport : a.controlsInViewport
							});
							a.transitionOptions.jsonData = null;
							a.jsonData
									&& (a.transitionOptions.jsonData = a
											.getCarouselData());
							a.transition = a.addTransition();
							b.connect(a.transition, "onPage", function(b, c, d,
									f) {
								a.onPage(b, c, d, f)
							});
							b.connect(a.transition, "onAfterSlide", function(b,
									c) {
								a.onAfterSlide(b, c)
							})
						},
						tagCarousel : function() {
							var a = this;
							  dojo.addClass(dojo.query('.prev', a.domNode)[0], 'prev-ctrl');
						      dojo.removeClass(dojo.query('.prev', a.domNode)[0], 'prev');
						      dojo.addClass(dojo.query('.next', a.domNode)[0], 'next-ctrl');
						      dojo.removeClass(dojo.query('.next', a.domNode)[0], 'next');
							_.each(b.query("ul.plistt li", a.domNode),
									function(c) {
										var d = b.query("h4 a", c);
										d.length
												&& a.tagElement(c,
														d[0].innerHTML)
									});
							a.tagElements(b.query("a.prev", a.domNode),
									"leftNav");
							a.tagElements(b.query("a.next", a.domNode),
									"rightNav");
							
							if (a.displayPagination) {
								var e = 0;
								a.tagElements(b
										.query("ul.plistt li", a.domNode),
										function(a) {
											return "plistt" + e++
										})
							}
						},

						getCarouselData : function() {
							return this.jsonData
						},
						getTransition : function() {
							return this.transition
						},
						showPagination : function() {
							this.transition.showPagination
									&& this.transition.showPagination()
						},
						hidePagination : function() {
							this.transition.hidePagination
									&& this.transition.hidePagination()
						},
						createTmpl : function(a) {
							a = new dojox.dtl.Context(a || this);
							return (new dojox.dtl.Template(this.tmpl))
									.render(a)
						},
						addNewContent : function() {
						},
						onPage : function(a, b, c, d) {
						},
						onBeforeSetupTransition : function() {
						},
						onAfterSlide : function(a, b) {
						}
					});
			return tui.widget.carousels.CarouselNeedtoknow
		});