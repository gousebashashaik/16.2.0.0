<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="webrootPath" value="${pageContext.request.contextPath}" />
<c:set var="cdnDomain" value="${CDN_URL}" />
<c:set var="dojoProps" value="" />
<!--Condition Stars--> <!-- thomson or firstchoise -->
	<c:set var="siteFlag" value="false"/> <!-- thomson or firstchoise -->
	<c:if test = "${sessionScope.siteName == 'cruise'}">
		<c:set var="siteFlag" value="true"/> <!--Cruise-->
	</c:if>
<!--Condition Ends-->
<script>
    dojoConfig.addModuleName("tui/widget/popup/Tooltips","tui/widget/LazyLoadImage" );
</script>
<c:if test="${not empty viewData.hotelCarouselViewData && not empty viewData.villaCarouselViewData}">
    <script>
        dojoConfig.addModuleName("tui/widget/StatefulPills");
    </script>
    <c:set var="dojoProps" value='data-dojo-type="tui.widget.StatefulPills"' />
</c:if>
<div class="span-two-third" ${dojoProps}>
    <div class="tabs-container">
        <h2 class="underline <c:if test='${siteFlag eq true}'> margin-bot24</c:if>">
            <span class="component-title">Places To Stay In ${viewData.name}</span>
			<c:choose>
				<c:when test='${siteFlag eq true }'>
				<a class="view-all ensLinkTrack has-icon-left" itemprop="url" href="${viewAllURL}" data-componentId="${componentUid}">
					<i class="icon arrow-main arrow-sm product-arrow-left arrow-right-bg">&raquo;</i> View all</a>
				</c:when>
				<c:otherwise>
				<a class="view-all ensLinkTrack has-icon-right" href="${viewAllURL}" data-componentId="${componentUid}">
            View all places to stay <i class="icon arrow-main arrow-sm arrow-right arrow-right-bg">&raquo;</i>
            </a>
				</c:otherwise>
			</c:choose>
        </h2>
        <c:if test="${not empty viewData.hotelCarouselViewData && not empty viewData.villaCarouselViewData}">
            <ul class="pills">
                <li class="pill uc" data-dojo-value="hotel"><a href="#placesToStayHotels" class="ensLinkTrack" data-componentId="${componentUid}">Hotels / Apartments</a></li>
                <li class="pill uc" data-dojo-value="villa"><a href="#placesToStayVillas" class="ensLinkTrack" data-componentId="${componentUid}">Villas</a></li>
            </ul>
        </c:if>
        <c:if test="${not empty viewData.hotelCarouselViewData}">
            <div id="placesToStayHotels" class="places-to-stay">
                <c:forEach var="hotelsCarouselData" items="${viewData.hotelCarouselViewData}" varStatus="status">
                    <c:forEach var="hotels" items="${hotelsCarouselData.accomodationDatas}" varStatus="status">
                        <c:set var="imgplaced" value="false"/>
                        <c:set var="isSpecial" value=""/>
                        <c:if test="${not empty hotels.productRanges[0].code}">
                            <c:set var="isSpecial" value="special"/>
                        </c:if>
                        <div class="${fn:toLowerCase(hotels.productRanges[0].code)} places-to-stay ${isSpecial}">
                            <div class="image-container" >
                                <a href="${hotels.url}" class="ensLinkTrack" data-componentId="${componentUid}">
                                   <c:if test="${not empty hotels.productRanges[0].code}">
										<c:set var="className" value="diff-label diff-label-block ${fn:toLowerCase(hotels.productRanges[0].code)}" />
										<c:if test="${fn:toLowerCase(hotels.productRanges[0].code) eq 'smr'}">
										   <c:set var="className" value="diff-label diff-label-block cou-sensimar" />
										</c:if>
									    <c:if test="${fn:toLowerCase(hotels.productRanges[0].code) eq 'fam'}">
									    	<c:if test="${thFamilyDualBrandingSwitchOnFlag eq 'true'}">
										   		<c:set var="className" value="diff-label diff-label-block fam-life-large" />
										   </c:if>
										</c:if>
										
                                        <span class="${className}" data-dojo-type="tui.widget.popup.Tooltips" data-dojo-props="floatWhere:'position-top-center', text:'${hotels.productRanges[0].featureCodesAndValues.strapline[0]}'"></span>
                                    </c:if>
                                    <c:forEach var="image" items="${hotels.images}">
                                        <c:if test="${image.format eq 'Main Pic' && imgplaced eq 'false'}">
                                            <img src="${cdnDomain}/images/b.gif" data-dojo-type="tui.widget.LazyLoadImage" data-dojo-props="source:'${image.url}'" width="360" height="201" alt=""/>
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
                                    <h3><a href="${hotels.url}" class="ensLinkTrack" data-componentId="${componentUid}">${hotels.name}</a></h3>
                                    <span class="spaced-out"></span>
                                    <c:if test="${!empty hotels.featureCodesAndValues['tRating'][0]}">
                                    <span>
                                        <span class="rating rating-inline rating-indent t${fn:replace(hotels.featureCodesAndValues['tRating'][0],' ','')}"></span>
                                    </span>
                                    </c:if>
                                </div>
                                <div class="list-n-trip">
                                    <ul class="square">
                                        <c:forEach var="usp" items="${hotels.featureCodesAndValues['usps']}" varStatus="status">
                                            <li><span class="sprite-img-grp-1"></span>${usp}</li>
                                        </c:forEach>
                                    </ul>
                                    <c:if test="${not empty hotels.reviewRating && hotels.reviewRating != 0.0}">
                                    <div class="clear"></div>
                                    <p class="tripadvisor rating${fn:replace(hotels.reviewRating, '.', '')}">TripAdvisor Traveller Rating <span>rating: ${hotels.reviewRating}</span></p>
                                    </c:if>
                                </div>
                                <div class="price-n-view floater">
								  <c:if test="${not empty hotels.priceFrom}">
                                    <p class="price left">
                                        Prices from
                                        <strong><span class="currency">&pound;</span><fmt:formatNumber type="number" maxFractionDigits="2" value="${hotels.priceFrom}" /></strong>
                                        per person
                                    </p>
									 </c:if>
                                    <a href="${hotels.url}" class="button cta large left ensLinkTrack" data-componentId="${componentUid}">View details</a>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:forEach>
            </div>
        </c:if>
        <c:if test="${not empty viewData.villaCarouselViewData}">
            <div id="placesToStayVillas" class="places-to-stay">
                <c:forEach var="villasCarouselData" items="${viewData.villaCarouselViewData}" varStatus="status">
                    <c:forEach var="villas" items="${villasCarouselData.accomodationDatas}" varStatus="status">
                        <c:set var="imgplaced" value="false"/>
                        <c:set var="isSpecial" value=""/>
                        <c:if test="${not empty villas.productRanges[0].code}">
                            <c:set var="isSpecial" value="special"/>
                        </c:if>
                        <div class="${fn:toLowerCase(villas.productRanges[0].code)} places-to-stay ${isSpecial}">
                            <div class="image-container">
                                <a href="${villas.url}" class="ensLinkTrack" data-componentId="${componentUid}">
                                    <c:if test="${not empty villas.productRanges[0].code}">
										<c:set var="className" value="diff-label diff-label-block ${fn:toLowerCase(villas.productRanges[0].code)}" />
										<c:if test="${fn:toLowerCase(villas.productRanges[0].code) eq 'smr'}">
										   <c:set var="className" value="diff-label diff-label-block cou-sensimar" />
										</c:if>
										<c:if test="${fn:toLowerCase(villas.productRanges[0].code) eq 'fam'}">
									    	<c:if test="${thFamilyDualBrandingSwitchOnFlag eq 'true'}">
										   		<c:set var="className" value="diff-label diff-label-block fam-life-large" />
										   </c:if>
										</c:if>
                                        <span class="${className}" data-dojo-type="tui.widget.popup.Tooltips" data-dojo-props="floatWhere:'position-top-center', text:'${villas.productRanges[0].featureCodesAndValues.strapline[0]}'"></span>
                                    </c:if>
                                    <c:forEach var="image" items="${villas.images}">
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
                                    <h3><a href="${villas.url}" class="ensLinkTrack" data-componentId="${componentUid}">${villas.name}</a></h3>
                                    <span class="spaced-out"></span>
                                    <c:if test="${!empty villas.featureCodesAndValues['tRating'][0]}">
                                    <div>
                                        <span class="rating rating-inline rating-indent t${fn:replace(villas.featureCodesAndValues['tRating'][0],' ','')}">T Rating: ${villas.featureCodesAndValues['tRating'][0]}</span>
                                    </div>
                                    </c:if>
                                </div>
                                <div class="list-n-trip">
                                    <ul class="square">
                                        <c:forEach var="usp" items="${villas.featureCodesAndValues['usps']}" varStatus="status">
                                            <li><span class="sprite-img-grp-1"></span>${usp}</li>
                                        </c:forEach>
                                    </ul>
                                    <c:if test="${not empty villas.reviewRating && villas.reviewRating != 0.0}">
                                    	<div class="clear"></div>
                                        <p class="tripadvisor rating${fn:replace(villas.reviewRating, '.', '')}">TripAdvisor <span>rating: ${villas.reviewRating}</span></p>
                                    </c:if>
                                </div>
                                <div class="price-n-view floater">
									<c:if test="${not empty villas.priceFrom}">
                                    <p class="price left">
                                        Prices from
                                        <strong><span class="currency">&pound;</span><fmt:formatNumber type="number" maxFractionDigits="2" value="${villas.priceFrom}" /></strong>
                                        per person
                                    </p>
									 </c:if>
                                    <a href="${villas.url}" class="button cta large left ensLinkTrack" data-componentId="${componentUid}">View details</a>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:forEach>
            </div>
        </c:if>
    </div>
</div>