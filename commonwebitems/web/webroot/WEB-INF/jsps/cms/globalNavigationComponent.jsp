<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
 <%@ taglib prefix="jsonUtil" uri="/WEB-INF/tags/json/Json.tld"%>
<%--    ${jsonUtil:toJson(viewData)}     --%>
<c:choose>
<c:when test="${viewData.siteBrand eq 'CR'}">

	<c:choose>
	<c:when test="${sessionScope.SmallTab or sessionScope.LargeTab}">
			<c:set var="holidaysLink" value="/"/>
		</c:when>
		<c:otherwise>
			<c:set var="holidaysLink" value="/"/>
		</c:otherwise>
	</c:choose>
    </c:when>
    <c:otherwise>
    	<c:choose>
		<c:when test="${sessionScope.SmallTab or sessionScope.LargeTab}">
				<c:set var="holidaysLink" value="javascript:void(0);"/>
			</c:when>
			<c:otherwise>
				<c:set var="holidaysLink" value="javascript:void(0);"/>
			</c:otherwise>
		</c:choose>
    </c:otherwise>
</c:choose>
<c:set var="crActive" value="${viewData.crGlobalNavigationViewData.activeStyle}"/>
<c:set var="thActive" value="${viewData.tuiGlobalNavigationViewData.activeStyle}"/>
<c:set var="destActive" value="${viewData.destGlobalNavigationViewData.activeStyle}"/>

<script type="text/javascript">
    dojoConfig.addModuleName("tui/widget/popup/Tooltips", "tui/widget/homepage/globalNav");
</script>
<div id="nav" class="global-nav-component" >
	<ul>
		<li class="homepage-main-nav holiday-menu ${thActive}"  id="holidayMenu" data-dojo-type="tui.widget.homepage.globalNav">
		    <a href="${holidaysLink}" class="ensLinkTrack cruiseSelected desk-dev" data-componentid="WF_COM_088-4">Holidays
				<div class="arrow-down" >&nbsp;</div>
		    </a>
		    <a href="javascript:void(0)" class="ensLinkTrack cruiseSelected tab-dev">Holidays
				<div class="arrow-down" >&nbsp;</div>
		    </a>
			<!-- TH Holiday Menu -->
	<c:set var="thHolidayMenu" value="${viewData.tuiGlobalNavigationViewData.tuiHolidayNavigationViewData}"/>
<div class="holiday-collections megaMenu collections-editorial">

    <div class="collections-gallery">
	    <h2 class="collections-h2">${thHolidayMenu.collectionHeading}
	    <a class="view-all ensLinkTrack" data-componentid="${componentUid}" href="${thHolidayMenu.collectionHeadingLinkURL}" analytics-id="335" analytics-instance="1" analytics-text="ttdviewall" enslinktrackattached="true">
			<div class="arrow-rgt">&nbsp;</div>
		    ${thHolidayMenu.collectionHeadingLinkTitle}
	    </a>
	    </h2>

    	<ul class="gallery-list promo-prod">
	        <c:if test="${not empty thHolidayMenu.collection1}"><li class="${thHolidayMenu.collection1.title}"><a class="ensLinkTrack" enslinktrackattached ="true" data-componentid="${componentUid}" href="${thHolidayMenu.collection1.collectionLinkUrl}"><span class="promo-prod-badge">${thHolidayMenu.collection1.featureTitle}</span><img src="${thHolidayMenu.collection1.imageUrlsMap.small}" /></a></li></c:if>
	        <c:if test="${not empty thHolidayMenu.collection2}"><li class="${thHolidayMenu.collection2.title}"><a class="ensLinkTrack" enslinktrackattached ="true" data-componentid="${componentUid}" href="${thHolidayMenu.collection2.collectionLinkUrl}"><span class="promo-prod-badge">${thHolidayMenu.collection2.featureTitle}</span><img class="borderRImg" src="${thHolidayMenu.collection2.imageUrlsMap.small}" /></a></li></c:if>
	        <c:if test="${not empty thHolidayMenu.collection3}"><li class="${thHolidayMenu.collection3.title}"><a class="ensLinkTrack" enslinktrackattached ="true" data-componentid="${componentUid}" href="${thHolidayMenu.collection3.collectionLinkUrl}"><span class="promo-prod-badge">${thHolidayMenu.collection3.featureTitle}</span><img src="${thHolidayMenu.collection3.imageUrlsMap.small}" /></a></li></c:if>
	        <c:if test="${not empty thHolidayMenu.collection4}"><li class="${thHolidayMenu.collection4.title}"><a class="ensLinkTrack" enslinktrackattached ="true" data-componentid="${componentUid}" href="${thHolidayMenu.collection4.collectionLinkUrl}"><span class="promo-prod-badge">${thHolidayMenu.collection4.featureTitle}</span><img src="${thHolidayMenu.collection4.imageUrlsMap.small}" /></a></li></c:if>
	        <c:if test="${not empty thHolidayMenu.collection5}"><li class="${thHolidayMenu.collection5.title}"><a class="ensLinkTrack" enslinktrackattached ="true" data-componentid="${componentUid}" href="${thHolidayMenu.collection5.collectionLinkUrl}"><span class="promo-prod-badge">${thHolidayMenu.collection5.featureTitle}</span><img class="borderLImg" src="${thHolidayMenu.collection5.imageUrlsMap.small}" /></a></li></c:if>
	        <c:if test="${not empty thHolidayMenu.collection6}"><li class="${thHolidayMenu.collection6.title}"><a class="ensLinkTrack" enslinktrackattached ="true" data-componentid="${componentUid}" href="${thHolidayMenu.collection6.collectionLinkUrl}"><span class="promo-prod-badge">${thHolidayMenu.collection6.featureTitle}</span><img src="${thHolidayMenu.collection6.imageUrlsMap.small}" /></a></li></c:if>
       </ul>

    </div>

	<div class="collection-list">

        <h2 class="collections-h2">${thHolidayMenu.bestForHeading}<a class="view-all ensLinkTrack" data-componentid="${componentUid}" href="${thHolidayMenu.bestForHeadingLinkURL}" analytics-id="335" analytics-instance="1" analytics-text="ttdviewall" enslinktrackattached="true">
        <div class="arrow-rgt">&nbsp;</div>
        ${thHolidayMenu.bestForHeadingLinkTitle}</a>
        </h2>

        <ul>
            <c:if test="${not empty thHolidayMenu.bestForTitle1}">
            <li>
            <h3><a class="ensLinkTrack" data-componentid="${componentUid}" href="${thHolidayMenu.bestForLink1}">${thHolidayMenu.bestForTitle1}</a></h3>
            </li>
            </c:if>
            <c:if test="${not empty thHolidayMenu.bestForTitle2}">
            <li>
            <h3><a class="ensLinkTrack" data-componentid="${componentUid}" href="${thHolidayMenu.bestForLink2}">${thHolidayMenu.bestForTitle2}</a></h3>
            </li>
            </c:if>
            <c:if test="${not empty thHolidayMenu.bestForTitle3}">
            <li>
            <h3><a class="ensLinkTrack" data-componentid="${componentUid}" href="${thHolidayMenu.bestForLink3}">${thHolidayMenu.bestForTitle3}</a></h3>
            </li>
            </c:if>
            <c:if test="${not empty thHolidayMenu.bestForTitle4}">
            <li>
            <h3><a class="ensLinkTrack" data-componentid="${componentUid}" href="${thHolidayMenu.bestForLink4}">${thHolidayMenu.bestForTitle4}</a></h3>
            </li>
            </c:if>
            <c:if test="${not empty thHolidayMenu.bestForTitle5}">
            <li>
            <h3><a class="ensLinkTrack" data-componentid="${componentUid}" href="${thHolidayMenu.bestForLink5}">${thHolidayMenu.bestForTitle5}</a></h3>
            </li>
            </c:if>
            <c:if test="${not empty thHolidayMenu.bestForTitle6}">
            <li>
            <h3><a class="ensLinkTrack" data-componentid="${componentUid}" href="${thHolidayMenu.bestForLink6}">${thHolidayMenu.bestForTitle6}</a></h3>
            </li>
            </c:if>
            <c:if test="${not empty thHolidayMenu.bestForTitle7}">
            <li>
            <h3><a class="ensLinkTrack" data-componentid="${componentUid}" href="${thHolidayMenu.bestForLink7}">${thHolidayMenu.bestForTitle7}</a></h3>
            </li>
            </c:if>
            <c:if test="${not empty thHolidayMenu.bestForTitle8}">
            <li>
            <h3><a class="ensLinkTrack" data-componentid="${componentUid}" href="${thHolidayMenu.bestForLink8}">${thHolidayMenu.bestForTitle8}</a></h3>
            </li>
            </c:if>
            <c:if test="${not empty thHolidayMenu.bestForTitle9}">
            <li>
            <h3><a class="ensLinkTrack" data-componentid="${componentUid}" href="${thHolidayMenu.bestForLink9}">${thHolidayMenu.bestForTitle9}</a></h3>
            </li>
            </c:if>
        </ul>

    </div>

	<div class="collection-list best-for">

        <h2 class="collections-h2">${thHolidayMenu.locationHeading}<a class="view-all ensLinkTrack" data-componentid="${componentUid}" href="${thHolidayMenu.locationHeadingLinkURL}" analytics-id="335" analytics-instance="1" analytics-text="ttdviewall" enslinktrackattached="true">
        <div class="arrow-rgt">&nbsp;</div>
        ${thHolidayMenu.locationHeadingLinkTitle}</a>
        </h2>

        <ul class="collection-sep">
            <c:if test="${not empty thHolidayMenu.location1}">
            <li>
            <h3><a class="ensLinkTrack" data-componentid="${componentUid}" href="${thHolidayMenu.locationLink1}">${thHolidayMenu.location1}</a></h3>
            </li>
            </c:if>
            <c:if test="${not empty thHolidayMenu.location2}">
            <li>
            <h3><a class="ensLinkTrack" data-componentid="${componentUid}" href="${thHolidayMenu.locationLink2}">${thHolidayMenu.location2}</a></h3>
            </li>
            </c:if>
            <c:if test="${not empty thHolidayMenu.location3}">
            <li>
            <h3><a class="ensLinkTrack" data-componentid="${componentUid}" href="${thHolidayMenu.locationLink3}">${thHolidayMenu.location3}</a></h3>
            </li>
            </c:if>
            <c:if test="${not empty thHolidayMenu.location4}">
            <li>
            <h3><a class="ensLinkTrack" data-componentid="${componentUid}" href="${thHolidayMenu.locationLink4}">${thHolidayMenu.location4}</a></h3>
            </li>
            </c:if>
        </ul>

        <h2 class="deal-menu">${thHolidayMenu.dealsHeading} <a class="view-all ensLinkTrack" data-componentid="${componentUid}" href="${thHolidayMenu.dealsHeadingLinkURL}" analytics-id="335" analytics-instance="1" analytics-text="ttdviewall" enslinktrackattached="true">
        <div class="arrow-rgt">&nbsp;</div>
        ${thHolidayMenu.dealsHeadingLinkTitle}</a>
        </h2>

        <ul class="deals-collection">
            <c:if test="${not empty thHolidayMenu.deal1}">
            <li>
            <h3><a class="ensLinkTrack" data-componentid="${componentUid}" href="${thHolidayMenu.dealLink1}">${thHolidayMenu.deal1}</a></h3>
            </li>
            </c:if>
            <c:if test="${not empty thHolidayMenu.deal2}">
            <li>
            <h3><a class="ensLinkTrack" data-componentid="${componentUid}" href="${thHolidayMenu.dealLink2}">${thHolidayMenu.deal2}</a></h3>
            </li>
            </c:if>
            <c:if test="${not empty thHolidayMenu.deal3}">
            <li>
            <h3><a class="ensLinkTrack" data-componentid="${componentUid}" href="${thHolidayMenu.dealLink3}">${thHolidayMenu.deal3}</a></h3>
            </li>
            </c:if>
        </ul>
        </div>

        </div>
<!-- TH Holiday Menu End -->
	    </li>
<!-- CR Cruise Megamenu Menu -->

<c:set var="crCruiseMenu" value="${viewData.crGlobalNavigationViewData}"/>
		<li class="homepage-main-nav ${crActive} " id="cruiseMenu" data-dojo-type="tui.widget.homepage.globalNav">
			<a href="/cruise/" class="ensLinkTrack cruiseSelected desk-dev">Cruises
			<div class="arrow-down" >&nbsp;</div>
			</a>
			<a href="javascript:void(0)" class="ensLinkTrack cruiseSelected tab-dev">Cruises
			<div class="arrow-down" >&nbsp;</div>
			</a>
	<%-- mega menu in header --%>
	<div id="mega-menu" class="megaMenu menu-items"  >
		<div class="menu-overlay">
		<div class="menu-list">
			<div class="firstDiv destinations">
				<div class="titleBox">
					<h3>${crCruiseMenu.cruiseDestViewData.title}</h3>
					<h2><a class="view-all ensLinkTrack has-icon-left view-link" href="${crCruiseMenu.cruiseDestViewData.linkUrl}"><i class="icon arrow-grt small">&#9658;</i> ${crCruiseMenu.cruiseDestViewData.linktitle}</a></h2>
				</div>
				<div class="item-list">
				<ul class="listEven">
				<c:set var="nileCruise" value="North Africa And Middle East" />
				<c:forEach var="destcrCruiseMenusEven"  items="${crCruiseMenu.cruiseDestViewData.destViewDatas}" varStatus="counter">
					<c:if test="${counter.index % 2 == 0}">
							<li	class="dest-item uc" style="">
								<a href="${destcrCruiseMenusEven.url}">${destcrCruiseMenusEven.cruiseAreaName}</a>
								<span class="item-tooltip" data-dojo-type="tui.widget.popup.Tooltips" data-dojo-props="floatWhere: 'position-top-center', className: 'item-tooltip megaMenuTooltip', text:'${destcrCruiseMenusEven.countryNames}'"></span>
								<c:if test="${destcrCruiseMenusEven.cruiseAreaName eq nileCruise}">
								<span class="item-dropdown">
									<a href="${crCruiseMenu.cruiseDestViewData.nileCruiseUrl}" target="_blank">${crCruiseMenu.cruiseDestViewData.nileCruise}</a>
								</span>
								</c:if>
							</li>
					</c:if>
				</c:forEach>
				</ul>
				<ul class="listOdd">
				<c:forEach var="destcrCruiseMenusOdd" items="${crCruiseMenu.cruiseDestViewData.destViewDatas}" varStatus="counter">
					<c:if test="${counter.index % 2 != 0}">
							<li class="dest-item uc">
								<a href="${destcrCruiseMenusOdd.url}">${destcrCruiseMenusOdd.cruiseAreaName}</a>
								<span class="item-tooltip" data-dojo-type="tui.widget.popup.Tooltips"
								data-dojo-props="floatWhere: 'position-top-center', className: 'item-tooltip megaMenuTooltip', text:'${destcrCruiseMenusOdd.countryNames}'"></span>
								<c:if test="${destcrCruiseMenusOdd.cruiseAreaName eq nileCruise}">
								<span class="item-dropdown">
									<a href="${crCruiseMenu.cruiseDestViewData.nileCruiseUrl}" target="_blank">${crCruiseMenu.cruiseDestViewData.nileCruise}</a>
								</span>
								</c:if>
							</li>
					</c:if>
				</c:forEach>
				</ul>
				</div>
			</div>
			<div class="centerDiv poc-lists">
				<div class="titleBox">
					<h3>${crCruiseMenu.topPortOfCallViewData.title}</h3>
					<h2><a class="view-all ensLinkTrack has-icon-left view-link" href="${crCruiseMenu.topPortOfCallViewData.linkUrl}"><i class="icon arrow-grt small">&#9658;</i> ${crCruiseMenu.topPortOfCallViewData.linktitle}</a>
					</h2>
				</div>
				<div class="item-list">
					<ul class="list">
					<c:forEach var="poccrCruiseMenus"  items="${crCruiseMenu.topPortOfCallViewData.pocViewDatas}" varStatus="counter">
					<li class="poc-item uc">
						<a href="${poccrCruiseMenus.url}">${poccrCruiseMenus.name}</a>
						<span class="country">, ${poccrCruiseMenus.countryName}</span>
					</li>
					</c:forEach>
					</ul>
				</div>
			</div>


			<div class="lastDiv nav-list">
				<ul class="list">
				<c:forEach  var="navComponent" items="${crCruiseMenu.navComViewData.navigationBarComponents}" varStatus="counter">
					<li class="nav-item">
						<span class="megaMenu-${navComponent.styleClass}"></span>
						<a href="${navComponent.navigationNode.links[0].url}">${navComponent.navigationNode.title}</a><br/>
						<c:if test="${not empty navComponent.navigationNode.children}">
						<span class="sub-nav-item">
							<c:forEach  var="chidcrCruiseMenu" items="${navComponent.navigationNode.children}" varStatus="counter1">
								<a href="${chidcrCruiseMenu.links[0].url}">${chidcrCruiseMenu.title}</a>
								<c:if test="${!counter1.last}">
									<span class="devider">|</span>
								</c:if>
							</c:forEach>
						</span>
						</c:if>
					</li>
				</c:forEach>
				</ul>
			</div>

		</div>
	</div>
	</div>
<!-- CR Cruise Megamenu Menu End -->
		</li>
		<li><a href="${viewData.globalLinks.flights}" class="ensLinkTrack" >Flights</a></li>
		<li><a href="${viewData.globalLinks.Deals}" class="ensLinkTrack" >Deals</a></li>
		<li class="holiday-destination ${destActive}"><a href="${viewData.globalLinks.destinations}" class="ensLinkTrack" >Destinations</a></li>
		<li><a href="${viewData.globalLinks.extras}" class="ensLinkTrack" >Extras</a></li>
      </ul>
</div>