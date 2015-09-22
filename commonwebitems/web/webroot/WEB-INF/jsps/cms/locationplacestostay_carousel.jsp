<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="images" tagdir="/WEB-INF/tags/images" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="webrootPath" value="${pageContext.request.contextPath}" />
<c:set var="cdnDomain" value="${CDN_URL}" />
<!--Condition Stars--> <!-- thomson or firstchoise -->
	<c:set var="siteFlag" value="false"/> <!-- thomson or firstchoise -->
	<c:if test = "${sessionScope.siteName == 'cruise'}">
		<c:set var="siteFlag" value="true"/> <!--Cruise-->
	</c:if>
<!--Condition Ends-->
<script>
	dojoConfig.addModuleName("tui/widget/Tabs", "tui/widget/StatefulPills");
</script>
<c:set var="tabProps" value=""/>
 <c:if test="${not empty viewData.hotelLocations && not empty viewData.villaLocations && not empty viewData.villaLocations[0].accommodations && not empty viewData.hotelLocations[0].accommodations}">
    <c:set var="tabProps" value="data-dojo-type='tui.widget.StatefulPills'"/>
</c:if>
 <div class="span-two-third" >
    <div class="tabs-container place-to-stay" ${tabProps}>
        <h2 class="underline">Places To Stay <c:if test="${not empty viewData.topLocationName}">In ${viewData.topLocationName} </c:if>
            <a class="view-all ensLinkTrack has-icon-right" href="${viewAllURL}" data-componentId="${componentUid}">
                View all places to stay <i class="icon arrow-main arrow-sm arrow-right arrow-right-bg">&raquo;</i>
            </a>
        </h2>
        <c:if test="${not empty viewData.hotelLocations && not empty viewData.villaLocations && not empty viewData.villaLocations[0].accommodations && not empty viewData.hotelLocations[0].accommodations }">
        <ul class="pills">
            <li class="pill uc" data-dojo-value="hotel"><a href="#placeHotel" class="ensLinkTrack" data-componentId="${componentUid}">Hotels / Apartments</a></li>
            <li class="pill uc" data-dojo-value="villa"><a href="#placeVilla" class="ensLinkTrack" data-componentId="${componentUid}">Villas</a></li>
        </ul>
        </c:if>
        <c:if test="${not empty viewData.hotelLocations}">
        <div id="placeHotel" class="places-to-stay">
            <c:forEach var="locationData" items="${viewData.hotelLocations}" varStatus="status" begin="0">
                <c:choose>
                    <c:when test="${not empty locationData.code and not empty locationData.name}">
                        <div class="panel labeled image-left <c:if test="${status.index == 0}">first</c:if>">
                            <a href="${locationData.url}" class="ensLinkTrack" data-componentId="${componentUid}">
                                <images:first collection="${locationData.galleryImages}" placeholder="${cdnDomain}/images/${sessionScope.siteName}/default-large.png" size="medium" width="360" height="201"/>
                            </a>
                            <div class="copy">
                                <a href="${locationData.url}" class="ensLinkTrack" data-componentId="${componentUid}"><h3>${locationData.name}<c:if test='${siteFlag eq true }'><i class="icon arrow-main product-arrow-b arrow-right arrow-right-bg">&#9658;</i></c:if></h3></a>
                                <p>${locationData.featureCodesAndValues['intro1Body'][0]}</p>
                                <ul
								<c:choose>
								<c:when test='${siteFlag eq true }'> class="square-with-arrow uc"</c:when>
								<c:otherwise> class="square"</c:otherwise>
								</c:choose>
								>
                                    <c:if test="${not empty locationData.subLocations}">
                                        <c:forEach var="subLocationData" items="${locationData.subLocations}" varStatus="counter">
                                            <li><c:if test='${siteFlag eq false }'><span class="sprite-img-grp-1"></c:if></span><a href="${subLocationData.url}" class="ensLinkTrack" data-componentId="${componentUid}">${subLocationData.name}</a></li>
                                        </c:forEach>
                                     </c:if>
                                     <c:if test="${not empty locationData.accommodations}">
                                        <c:forEach var="accommodationData" items="${locationData.accommodations}" varStatus="counter">
                                            <li><c:if test='${siteFlag eq false }'><span class="sprite-img-grp-1"></c:if></span><a href="${accommodationData.url}" class="ensLinkTrack" data-componentId="${componentUid}">${accommodationData.name}</a></li>
                                        </c:forEach>
                                     </c:if>
                                </ul>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="accomodationData" items="${locationData.accommodations}" varStatus="status">
                            <c:set var="imgplaced" value="false"/>
                            <c:set var="isSpecial" value=""/>
                            <c:if test="${not empty accomodationData.productRanges[0].code}">
                                <c:set var="isSpecial" value="special"/>
                            </c:if>
                            <div class="${fn:toLowerCase(accomodationData.productRanges[0].code)}  places-to-stay ${isSpecial}">
                                <div class=" image-container">
                                    <a href="${accomodationData.url}" class="ensLinkTrack" data-componentId="${componentUid}">
                                        <span data-dojo-type="tui.widget.popup.Tooltips" data-dojo-props="floatWhere:'position-top-center', text:'${accomodationData.productRanges[0].featureCodesAndValues.strapline[0]}'"></span>
                                        <c:forEach var="image" items="${accomodationData.images}">
                                            <c:if test="${image.format eq 'Main Pic' && imgplaced eq 'false'}">
                                                <img src="${image.url}" width="360" height="201" alt=""/>
                                                <c:set var="imgplaced" value="true"/>
                                            </c:if>
                                        </c:forEach>
                                        <c:if test='${imgplaced eq "false"}'>
                                            <img src="${cdnDomain}/images/default-large.gif" width="360" height="201" alt=""/>
                                        </c:if>
                                    </a>
                                </div>
                                <div class="copy">
                                    <div class="hotel-name">
                                        <h3><a href="${accomodationData.url}" class="ensLinkTrack" data-componentId="${componentUid}">${accomodationData.name}</a></h3>
                                        <span class="spaced-out"></span>
                                        <c:if test="${!empty accomodationData.featureCodesAndValues['tRating'][0]}">
                                        <span>
                                            <span class="rating rating-inline rating-indent t${fn:replace(accomodationData.featureCodesAndValues['tRating'][0],' ','')}">T Rating: ${accomodationData.featureCodesAndValues['tRating'][0]}</span>
                                  		</span>
                                        </c:if>
                                    </div>
                                    <div class="list-n-trip">
                                        <ul class="square">
                                            <c:forEach var="usp" items="${accomodationData.featureCodesAndValues['usps']}" varStatus="status">
                                                <li>${usp}</li>

                                            </c:forEach>
                                        </ul>
                                        <c:if test="${not empty accomodationData.reviewRating && accomodationData.reviewRating != 0.0}">
                                        <p class="tripadvisor rating${fn:replace(accomodationData.reviewRating, '.', '')}">TripAdvisor <span>rating: ${accomodationData.reviewRating}</span></p>
                                        </c:if>
                                    </div>
                                    <div class="price-n-view floater">
                                        <p class="price left">
                                            Prices from
                                            <strong>&pound;<fmt:formatNumber type="number" maxFractionDigits="2" value="${accomodationData.priceFrom}" /></strong>
                                            per person
                                        </p>
                                        <a href="${accomodationData.url}" class="button cta large left ensLinkTrack" data-componentId="${componentUid}">View details</a>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        </c:if>
        <c:if test="${not empty viewData.villaLocations}">
        <div id="placeVilla" class="places-to-stay">
            <c:forEach var="locationData" items="${viewData.villaLocations}" varStatus="status" begin="0">
                <c:choose>
                    <c:when test="${not empty locationData.code and not empty locationData.name}">
                        <div class="panel labeled image-left <c:if test="${status.index == 0}">first</c:if>">
                            <a href="${locationData.url}" class="ensLinkTrack" data-componentId="${componentUid}">
                                <images:first collection="${locationData.galleryImages}" placeholder="${cdnDomain}/images/${sessionScope.siteName}/default-large.png" size="medium" width="360" height="201"/>
                            </a>
                            <div class="copy">
                                <a href="${locationData.url}" class="ensLinkTrack" data-componentId="${componentUid}"><h3>${locationData.name}</h3></a>
                                <p>${locationData.featureCodesAndValues['intro1Body'][0]}</p>
                                <ul class="square">
                                    <c:if test="${not empty locationData.subLocations}">
                                        <c:forEach var="subLocationData" items="${locationData.subLocations}" varStatus="counter">
                                            <li><a href="${subLocationData.url}" class="ensLinkTrack" data-componentId="${componentUid}">${subLocationData.name}</a></li>
                                        </c:forEach>
                                     </c:if>
                                     <c:if test="${not empty locationData.accommodations}">
                                        <c:forEach var="accommodationData" items="${locationData.accommodations}" varStatus="counter">
                                            <li><a href="${accommodationData.url}" class="ensLinkTrack" data-componentId="${componentUid}">${accommodationData.name}</a></li>
                                        </c:forEach>
                                     </c:if>
                                </ul>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="accomodationData" items="${locationData.accommodations}" varStatus="status">
                            <c:set var="imgplaced" value="false"/>
                            <c:set var="isSpecial" value=""/>
                            <c:if test="${not empty accomodationData.productRanges[0].code}">
                                <c:set var="isSpecial" value="special"/>
                            </c:if>
                            <div class="${fn:toLowerCase(accomodationData.productRanges[0].code)} places-to-stay ${isSpecial}">
                                <div class=" image-container">
                                    <a href="${accomodationData.url}" class="ensLinkTrack" data-componentId="${componentUid}">
                                        <span data-dojo-type="tui.widget.popup.Tooltips" data-dojo-props="floatWhere:'position-top-center', text:'${accomodationData.productRanges[0].featureCodesAndValues.strapline[0]}'"></span>
                                        <c:forEach var="image" items="${accomodationData.images}">
                                            <c:if test="${image.format eq 'Main Pic' && imgplaced eq 'false'}">
                                                <img src="${image.url}" width="360" height="201" alt=""/>
                                                <c:set var="imgplaced" value="true"/>
                                            </c:if>
                                        </c:forEach>
                                        <c:if test='${imgplaced eq "false"}'>
                                            <img src="${cdnDomain}/images/${sessionScope.siteName}/default-large.png" width="360" height="201" alt=""/>
                                        </c:if>
                                    </a>
                                </div>
                                <div class="copy">
                                    <div class="hotel-name">
                                        <h3><a href="${accomodationData.url}" class="ensLinkTrack" data-componentId="${componentUid}">${accomodationData.name}</a></h3>
                                        <span class="spaced-out"></span>
                                        <c:if test="${!empty accomodationData.featureCodesAndValues['tRating'][0]}">
                                        <span>
                                            <span class="score t${fn:replace(accomodationData.featureCodesAndValues['tRating'][0],' ','')}">T Rating: ${accomodationData.featureCodesAndValues['tRating'][0]}</span>
                                        </span>
                                        </c:if>
                                    </div>
                                    <div class="list-n-trip">
                                        <ul class="square">
                                            <c:forEach var="usp" items="${accomodationData.featureCodesAndValues['usps']}" varStatus="status">
                                                <li>${usp}</li>
                                            </c:forEach>
                                        </ul>
                                        <c:if test="${not empty accomodationData.reviewRating && accomodationData.reviewRating != 0.0}">
                                        <p class="tripadvisor rating${fn:replace(accomodationData.reviewRating, '.', '')}">TripAdvisor <span>rating: ${accomodationData.reviewRating}</span></p>
                                        </c:if>
                                    </div>
                                    <div class="price-n-view floater">
                                        <p class="price left">
                                            Prices from
                                            <strong>&pound;<fmt:formatNumber type="number" maxFractionDigits="2" value="${accomodationData.priceFrom}" /></strong>
                                            per person
                                        </p>
                                        <a href="${accomodationData.url}" class="button cta large left ensLinkTrack" data-componentId="${componentUid}">View details</a>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        </c:if>
    </div>
</div>