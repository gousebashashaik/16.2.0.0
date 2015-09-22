<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="images" tagdir="/WEB-INF/tags/images" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="webrootPath" value="${pageContext.request.contextPath}" />
<c:set var="cdnDomain" value="${CDN_URL}" />
<script type="text/javascript">
    dojoConfig.addModuleName("tui/widget/LazyLoadImage", "tui/widget/taggable/LocationEditorial");
</script>

<%-- Location Editorial component (WF_COM_050) --%>
<c:set var="villas" value="${childLocationByType['VILLA']}"/>
<c:set var="hotels" value="${childLocationByType['HOTEL']}"/>
<c:if test="${not empty villas && not empty hotels}">
    <c:set var="pillSwitcherDojoType" value="data-dojo-type='tui.widget.PillsSwitcher'"/>
    <script>
        dojoConfig.addModuleName("tui/widget/PillsSwitcher");
    </script>
</c:if>
<!--Condition Stars--> <!-- thomson or firstchoise -->
	<c:set var="siteFlag" value="false"/> <!-- thomson or firstchoise -->
	<c:if test = "${sessionScope.siteName == 'cruise'}">
		<c:set var="siteFlag" value="true"/> <!--Cruise-->
	</c:if>
<!--Condition Ends-->
<%--Countries, Regions, Destionations: childLocationsByType--%>
<c:if test="${not empty villas || not empty hotels}">
    <div class="span-two-third" data-dojo-type="tui.widget.taggable.LocationEditorial">
        <c:if test="${not empty hotels}">
            <div id="locEditorialHotels" data-dojo-props="listenTo:'hotel'" ${pillSwitcherDojoType}>
                <c:forEach var="location" items="${hotels}" varStatus="counter">
                    <div class="content-block labeled <c:if test="${(counter.index mod 2) == 0}">first</c:if>">
                        <a href="${location.url}" class="ensLinkTrack" data-componentId="${componentUid}">
                            <h3>${location.name} <c:if test='${siteFlag eq true }'><i class="icon arrow-main product-arrow-b arrow-right arrow-right-bg">&#9658;</i></c:if></h3>
							<c:choose>
							<c:when test="${siteFlag eq true }">
							<images:first collection="${location.galleryImages}" placeholder="${cdnDomain}/images/${sessionScope.siteName}/default-medium.png" size="small" width="360" height="201" />
							</c:when>
							<c:otherwise>
                            <images:first collection="${location.galleryImages}" placeholder="${cdnDomain}/images/${sessionScope.siteName}/default-medium.png" size="small" width="317" height="150" />
							</c:otherwise>
							</c:choose>
                        </a>
                        <p>${location.featureCodesAndValues['intro1BodyLink'][0]}</p>
                    </div>
                    <c:if test="${(counter.index mod 2) == 1}"><div class="clear"></div></c:if>
                </c:forEach>
            </div>
        </c:if>
        <c:if test="${not empty villas}">
            <div id="locEditorialVillas" class="hide" data-dojo-props="listenTo:'villa'" ${pillSwitcherDojoType}>
                <c:forEach var="location" items="${villas}" varStatus="counter">
                    <div class="content-block labeled <c:if test="${(counter.index mod 2) == 0}">first</c:if>">
                        <a href="${location.url}" class="ensLinkTrack" data-componentId="${componentUid}">
                            <h3>${location.name}</h3>
                            <images:first collection="${location.galleryImages}" placeholder="${cdnDomain}/images/${sessionScope.siteName}/default-medium.png" size="small" width="317" height="150" />
                        </a>
                        <p>${location.featureCodesAndValues['intro1BodyLink'][0]}</p>
                    </div>
                    <c:if test="${(counter.index mod 2) == 1}"><div class="clear"></div></c:if>
                </c:forEach>
            </div>
        </c:if>
    </div>
</c:if>
<%-- Resorts: childLocationDatas--%>
<%-- <c:if test="${empty villas || empty hotels}"> --%>
<%--     <c:choose>
        <c:when test="${not empty childLocationDatas}">
            <div class="span-two-third" data-dojo-type="tui.widget.taggable.LocationEditorial">
                <c:forEach var="location" items="${childLocationDatas}" begin="0" varStatus="counter">
                    <div class="content-block labeled <c:if test="${(counter.index mod 2) == 0}">first</c:if>">
                        <a href="${location.url}" class="ensLinkTrack" data-componentId="${componentUid}">
                            <h3>${location.name}</h3>
                            <images:first collection="${location.galleryImages}" placeholder="${cdnDomain}/images/${sessionScope.siteName}/default-medium.png" size="small" width="317" height="150" />
                            <images:first collection="${location.galleryImages}" placeholder="http://placehold.it/317x170" size="small" resize-width="317" resize-height="150"/>
                        </a>
                        <p>${location.featureCodesAndValues['intro1BodyLink'][0]}</p>
                    </div>
                    <c:if test="${(counter.index mod 2) == 1}"><div class="clear"></div></c:if>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>  --%>
            <c:if test="${not empty childAccommodationDatas}">
                <div class="span" data-dojo-type="tui.widget.taggable.LocationEditorial">
                    <h2 class="underline">Places To Stay In ${locationName}</h2>
                    <div class="product-list no-border">
                        <div class="viewport">
                            <ul class="plist">
                                <c:forEach var="accom" items="${childAccommodationDatas}" begin="0" varStatus="counter">
                                <li>
                                    <c:set var="isSpecial" value=""/>
                                    <c:if test="${not empty accom.productRanges[0].code}">
                                        <c:set var="isSpecial" value="special"/>
                                    </c:if>
                                    <div class="product ${isSpecial}">
                                        <div class="image-container">
                                        <span data-dojo-type="tui.widget.popup.Tooltips" data-dojo-props="floatWhere:'position-top-center', text:'${accom.productRanges[0].featureCodesAndValues.strapline[0]}'" class="diff-label ${fn:toLowerCase(accom.productRanges[0].code)}"></span>
                                            <a href="${accom.url}" class="ensLinkTrack" data-componentId="${componentUid}">
                                                <images:first collection="${accom.galleryImages}" placeholder="${cdnDomain}/images/${sessionScope.siteName}/default-small.png" size="small" width="232" height="130" />
                                            </a>
                                        </div>
                                        <h4><a href="${accom.url}" class="ensLinkTrack" data-componentId="${componentUid}">${accom.name}</a></h4>
                                        <c:if test="${!empty accom.featureCodesAndValues['tRating'][0]}">
                                            <p class="rating t${fn:replace(accom.featureCodesAndValues['tRating'][0],' ','')}">Rating: ${accom.featureCodesAndValues['tRating'][0]}</p>
                                        </c:if>
                                        <%-- <h5>&lt;Cala Millor, Majorca&gt;</h5>
                                        <p>&lt;Descriptive summary here&gt;</p>--%>
                                        <c:if test="${not empty accom.featureCodesAndValues['introduction'][0]}">
                                            <p>
                                                <c:set var="stringToEllipse" value="${accom.featureCodesAndValues.introduction[0]}"/>
                                                <c:set var="limit" value="120"/>
                                                <c:set var="length" value="${fn:length(stringToEllipse)}"/>
                                                <c:set var="secondHalf" value="${fn:substring(stringToEllipse, limit, length)}"/>
                                                <c:set var="secondHalfWords" value="${fn:split(secondHalf, ' ')}"/>

                                                ${fn:substring(stringToEllipse, 0, limit)}${secondHalfWords[0]}&hellip;
                                            </p>
                                            <c:choose>
												<c:when test="${siteFlag eq true }">
													<h2 class="more-link"><a class="view-all ensLinkTrack has-icon-left" href="${accom.url}" data-componentId="${componentUid}"><i class="icon arrow-main product-arrow-sm product-arrow-left arrow-right-bg">&#8227;</i> View details</a>
													</h2>
												</c:when>
												<c:otherwise>
                                            <a href="${accom.url}" class="ensLinkTrack viewDetail" data-componentId="${componentUid}">View Details &raquo;</a>
												</c:otherwise>
											</c:choose>

                                        </c:if>
                                        <c:if test="${not empty accom.priceFrom}">
                                            <div class="boxout floater">
                                                <p class="price left">
                                                    From
                                                    <strong>&pound;<fmt:formatNumber type="number" maxFractionDigits="2" value="${accom.priceFrom}" /></strong>
                                                    per person
                                                </p>
                                                <a href="#" class="button right ensLinkTrack" data-componentId="${componentUid}">View details</a>
                                            </div>
                                        </c:if>
                                    </div>
                                </li>
                                <c:if test="${(counter.count mod 4) == 0}"><li class="clear-it"></li></c:if>
                                </c:forEach>
                            </ul>
                        </div>
                    </div>
                </div>
            </c:if>
<%--         </c:otherwise>
    </c:choose>
</c:if> --%>