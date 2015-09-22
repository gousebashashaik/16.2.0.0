<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="images" tagdir="/WEB-INF/tags/images" %>
<c:set var="webrootPath" value="${pageContext.request.contextPath}" />
<c:set var="cdnDomain" value="${CDN_URL}" />
<script>
    dojoConfig.addModuleName("tui/widget/carousels/Carousel", "tui/widget/Pills", "tui/widget/popup/Tooltips", "tui/widget/LazyLoadImage");
</script>
<!--Condition Stars--> <!-- thomson or firstchoise -->
	<c:set var="siteFlag" value="false"/> <!-- thomson or firstchoise -->
	<c:if test = "${sessionScope.siteName == 'cruise'}">
		<c:set var="siteFlag" value="true"/> <!--Cruise-->
	</c:if>
<!--Condition Ends-->
<%-- Location carousel --%>
<c:if test="${not empty viewData.locationCarousels}">
    <div class="span shadow cntry">
        <div class="span">
            <div class="tabs-container" data-dojo-type="tui.widget.Pills">
				<c:choose>
					<c:when test="${siteFlag eq true }">
					<h2>Similar Destinations</h2>
                   <ul class="pills pills-inline">
                        <c:forEach var="crossSellcarousel" items="${viewData.locationCarousels}" varStatus="status">
                            <c:set var="itemClass" value="pill"/>
                            <%--Activate collection pill first if available and crossSellcarousel.defaultSelected not available--%>
                            <c:if test="${ status.count == 1}">
                                <c:set var="itemClass" value="pill active"/>
                            </c:if>
                            <li class="${itemClass}" analytics-instance="1" analytics-id="001" analytics-text="In ${fn:toUpperCase(crossSellcarousel.name)}"><a href="#${crossSellcarousel.code}${status.count}" class="ensLinkTrack" data-componentId="${componentUid}">In ${crossSellcarousel.name}
							<span class="selected">
                             	<i class="icon arrow-main pill-arrow" >&#9658;</i>
                            </span>
							</a>
                            </li>
                        </c:forEach>
                    </ul>
					</c:when>
					<c:otherwise>
					<h2>Similar Destinations
                    <ul class="pills pills-inline">
                        <c:forEach var="crossSellcarousel" items="${viewData.locationCarousels}" varStatus="status">
                            <li class="pill"><a href="#${crossSellcarousel.code}${status.count}" class="ensLinkTrack" data-componentId="${componentUid}">In ${crossSellcarousel.name}</a></li>
                        </c:forEach>
                    </ul>
				</h2>
					</c:otherwise>
				</c:choose>
				<c:forEach var="crossSellcarousel" items="${viewData.locationCarousels}" varStatus="status">
                    <div id="${crossSellcarousel.code}${status.count}" class="product-list carousel" data-dojo-type="tui.widget.carousels.Carousel">
                        <div class="viewport">
                            <ul class="plist">
                                <c:forEach var="locationData" items="${crossSellcarousel.locationDatas}" varStatus="status">
                                <li>
                                    <div class="product">
                                        <div class="image-container dimen_medium">
                                            <a href="${locationData.url}" class="ensLinkTrack" data-componentId="${componentUid}">
                                                <images:first collection="${locationData.galleryImages}" placeholder="${cdnDomain}/images/${sessionScope.siteName}/default-small.png" size="small" />
                                            </a>
                                        </div>
										<div class="min-height">
                                        <h4><a href="${locationData.url}" class="ensLinkTrack" data-componentId="${componentUid}">${locationData.name} </a></h4>
										</div>
									</div>
                                </li>
                                </c:forEach>
                            </ul>
                        </div>
                        <p class="load">Loading</p>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</c:if>

<%--  Accommodation carousel --%>
<c:if test="${not empty viewData.accommodationCarousels}">
    <div class="span shadow">
		<div class="${componentStyle}">
			<div class="tabs-container" data-dojo-type="tui.widget.Pills">
				<c:choose>
					<c:when test="${siteFlag eq true }">
						<c:choose>
						<c:when test="${viewData.accommodationCarousels[0].accomodationDatas[0].accommodationType == 'VILLA'}">
						<h2>Similar Villas</h2>
						</c:when>
						<c:otherwise>
						<h2>Similar Accommodations</h2>
						</c:otherwise>
						</c:choose>
                   <ul class="pills pills-inline">
                        <c:forEach var="crossSellcarousel" items="${viewData.accommodationCarousels}" varStatus="status">
                            <c:set var="itemClass" value="pill"/>
                            <%--Activate collection pill first if available--%>
                            <c:if test="${(fn:toLowerCase(crossSellcarousel.code) == 'holidayvillage') ||
                                            (fn:toLowerCase(crossSellcarousel.code) == 'splashworld') ||
                                            (fn:toLowerCase(crossSellcarousel.code) == 'premier') ||
                                            (fn:toLowerCase(crossSellcarousel.code) == 'premierfamilies') ||
                                            (fn:toLowerCase(crossSellcarousel.code) == 'suneoclub') ||
                                            (fn:toLowerCase(crossSellcarousel.code) == 'clubmagiclife') ||
                                            (fn:toLowerCase(crossSellcarousel.code) == 'thomsongold') ||
                                            (fn:toLowerCase(crossSellcarousel.code) == 'thomsonhandpicked') ||
                                            (fn:toLowerCase(crossSellcarousel.code) == 'thomsonalacarte') ||
                                            (fn:toLowerCase(crossSellcarousel.code) == 'thomsonplatinum') ||
                                            (fn:toLowerCase(crossSellcarousel.code) == 'thomsonvillaswithpools') ||
                                            (fn:toLowerCase(crossSellcarousel.code) == 'sensatori') ||
                                            (fn:toLowerCase(crossSellcarousel.code) == 'thomsoncouples') ||
                                            (fn:toLowerCase(crossSellcarousel.code) == 'thomsonfamilyresort') ||
                                            (fn:toLowerCase(crossSellcarousel.code) == 'thomsonchicunique') ||
                                            (fn:toLowerCase(crossSellcarousel.code) == 'lapland') ||
                                            (fn:toLowerCase(crossSellcarousel.code) == 'wentys') ||
                                            (fn:toLowerCase(crossSellcarousel.code) == 'simply') ||
                                            (fn:toLowerCase(crossSellcarousel.code) == 'thomsonsmallfriendly')}">
                                <c:set var="itemClass" value="pill active"/>
                            </c:if>
                            <li class="${itemClass}"><a href="#${fn:toLowerCase(crossSellcarousel.code)}${status.count}" class="ensLinkTrack" data-componentId="${componentUid}" analytics-text="In ${fn:toUpperCase(crossSellcarousel.name)}">In ${crossSellcarousel.name}
							<span class="selected">
                             	<i class="icon arrow-main pill-arrow" >&#9658;</i>
                            </span>
							</a>
                            </li>
                        </c:forEach>
                    </ul>
					</c:when>
					<c:otherwise>
					<c:choose>
						<c:when test="${viewData.accommodationCarousels[0].accomodationDatas[0].accommodationType == 'VILLA'}">
						<h2>Similar Villas
						</c:when>
						<c:otherwise>
						<h2>Similar Accommodations
						</c:otherwise>
					</c:choose>
                    <ul class="pills pills-inline">
                        <c:forEach var="crossSellcarousel" items="${viewData.accommodationCarousels}" varStatus="status">
                            <c:set var="itemClass" value="pill"/>
                            <%--Activate collection pill first if available--%>
                            <c:if test="${(fn:toLowerCase(crossSellcarousel.code) == 'holidayvillage') ||
                                            (fn:toLowerCase(crossSellcarousel.code) == 'splashworld') ||
                                            (fn:toLowerCase(crossSellcarousel.code) == 'premier') ||
                                            (fn:toLowerCase(crossSellcarousel.code) == 'premierluxury') ||
                                            (fn:toLowerCase(crossSellcarousel.code) == 'premierfamilies') ||
                                            (fn:toLowerCase(crossSellcarousel.code) == 'suneoclub') ||
                                            (fn:toLowerCase(crossSellcarousel.code) == 'clubmagiclife') ||
                                            (fn:toLowerCase(crossSellcarousel.code) == 'thomsongold') ||
                                            (fn:toLowerCase(crossSellcarousel.code) == 'thomsonhandpicked') ||
                                            (fn:toLowerCase(crossSellcarousel.code) == 'thomsonalacarte') ||
                                            (fn:toLowerCase(crossSellcarousel.code) == 'thomsonplatinum') ||
                                            (fn:toLowerCase(crossSellcarousel.code) == 'thomsonplatinumlifestyle') ||
                                            (fn:toLowerCase(crossSellcarousel.code) == 'thomsonvillaswithpools') ||
                                            (fn:toLowerCase(crossSellcarousel.code) == 'sensatori') ||
                                            (fn:toLowerCase(crossSellcarousel.code) == 'thomsoncouples') ||
                                            (fn:toLowerCase(crossSellcarousel.code) == 'thomsonfamilyresort') ||
                                            (fn:toLowerCase(crossSellcarousel.code) == 'thomsonchicunique') ||
                                            (fn:toLowerCase(crossSellcarousel.code) == 'lapland') ||
                                            (fn:toLowerCase(crossSellcarousel.code) == 'wentys') ||
                                            (fn:toLowerCase(crossSellcarousel.code) == 'simply') ||
                                            (fn:toLowerCase(crossSellcarousel.code) == 'thomsonsmallfriendly')}">
                                <c:set var="itemClass" value="pill active"/>
                            </c:if>
                            <li class="${itemClass}"><a href="#${fn:toLowerCase(crossSellcarousel.code)}${status.count}" class="ensLinkTrack" data-componentId="${componentUid}">In ${crossSellcarousel.name}</a></li>
                        </c:forEach>
                    </ul>
                </h2>
					</c:otherwise>
				</c:choose>
				<c:forEach var="crossSellcarousel" items="${viewData.accommodationCarousels}" varStatus="status">
                    <div id="${fn:toLowerCase(crossSellcarousel.code)}${status.count}" class="product-list carousel" data-dojo-type="tui.widget.carousels.Carousel">
                        <div class="viewport">
                            <ul class="plist">
                                <c:forEach var="accommodationData" items="${crossSellcarousel.accomodationDatas}" varStatus="status">
                                <li>
                                <c:set var="isSpecial" value=""/>
                                <c:if test="${not empty accommodationData.productRanges[0].code}">
                                    <c:set var="isSpecial" value="special"/>
                                </c:if>
                                    <div class="product ${isSpecial} ">
                                        <div class="image-container dimen_medium">
                                            <c:set var="className" value="diff-label ${fn:toLowerCase(accommodationData.productRanges[0].code)}" />
											<c:if test="${fn:toLowerCase(accommodationData.productRanges[0].code) eq 'smr'}">
											   <c:set var="className" value="diff-label ${fn:toLowerCase(accommodationData.productRanges[0].code)}-sensimar" />
											</c:if>
											<c:if test="${fn:toLowerCase(accommodationData.productRanges[0].code) eq 'fam'}">
												<c:if test="${thFamilyDualBrandingSwitchOnFlag eq 'true'}">
													<c:set var="className" value="diff-label ${fn:toLowerCase(accommodationData.productRanges[0].code)}-life-large" />
												</c:if>
											</c:if>
											
											<span data-dojo-type="tui.widget.popup.Tooltips" data-dojo-props="floatWhere:'position-top-center', text:'${accommodationData.productRanges[0].featureCodesAndValues.strapline[0]}'" class="diff-label ${className}"></span>
                                            <a href="${accommodationData.url}" class="ensLinkTrack" data-componentId="${componentUid}">
                                                <images:first collection="${accommodationData.galleryImages}" placeholder="${cdnDomain}/images/${sessionScope.siteName}/default-small.png" size="small" />
                                            </a>
                                            <c:if test="${not empty accommodationData.featureCodesAndValues['productName'][0]}">
                                                <p>${accommodationData.featureCodesAndValues['productName'][0]}</p>
                                            </c:if>
                                        </div>
										<div class="min-height">
                                         <h4><a href="${accommodationData.url}" class="ensLinkTrack" data-componentId="${componentUid}">${accommodationData.name}</a></h4>
                                        <c:if test="${not empty accommodationData.featureCodesAndValues['tRating'][0]}">
                                        <p class="rating t${fn:replace(accommodationData.featureCodesAndValues['tRating'][0],' ','')}">Rating: ${accommodationData.featureCodesAndValues['tRating'][0]}</p>
                                       </c:if>
									   </div>
                                        <div class="boxout floater">
                                            <c:if test="${accommodationData.priceFrom!=null}">
                                                <p class="price left">
                                                    <%--TODO: sort out board basis--%>
                                                    <c:if test="${not empty accommodationData.boardBasis}">${accommodationData.boardBasis[0].name}</c:if>
                                                    <strong>&pound;<fmt:formatNumber type="number" maxFractionDigits="2" value="${accommodationData.priceFrom}" /></strong>
                                                    per person
                                                </p>
                                            </c:if>
                                            <a class="button right ensLinkTrack" href="${accommodationData.url}" data-componentId="${componentUid}">View details</a>
                                        </div>
                                    </div>
                                </li>
                                </c:forEach>
                            </ul>
                        </div>
                        <p class="load">Loading</p>
                    </div>
				</c:forEach>
			</div>
		</div>
	</div>
</c:if>

<%--  Excursion ,Attractions Events & Sights carousel --%>
<c:if test="${not empty viewData.attractionCarousels}">
	<div class="span shadow cntry">
		<div class="${componentStyle}">
			<div class="tabs-container" data-dojo-type="tui.widget.Pills"><c:choose>
					<c:when test="${siteFlag eq true }">
					<h2>Similar Attractions</h2>
                   <ul class="pills pills-inline">
                        <c:forEach var="crossSellcarousel" items="${viewData.attractionCarousels}" varStatus="status">
                            <c:set var="itemClass" value="pill"/>
                            <%--Activate collection pill first if available and crossSellcarousel.defaultSelected not available--%>
                            <c:if test="${ status.count == 1}">
                                <c:set var="itemClass" value="pill active"/>
                            </c:if>
                            <li class="${itemClass}"><a href="#${crossSellcarousel.code}${status.count}" class="ensLinkTrack" data-componentId="${componentUid}">In ${crossSellcarousel.name}
							<span class="selected">
                             	<i class="icon arrow-main pill-arrow" >&#9658;</i>
                            </span>
							</a>
                            </li>
                        </c:forEach>
                    </ul>
					</c:when>
					<c:otherwise>
					<h2>Similar Attractions
                    <ul class="pills pills-inline">
                        <c:forEach var="crossSellcarousel" items="${viewData.attractionCarousels}" varStatus="status">
                            <li class="pill"><a href="#${crossSellcarousel.code}${status.count}" class="ensLinkTrack" data-componentId="${componentUid}">In ${crossSellcarousel.name}</a></li>
                        </c:forEach>
                    </ul>
					</h2>
					</c:otherwise>
				</c:choose>
				<c:forEach var="crossSellcarousel" items="${viewData.attractionCarousels}" varStatus="status">
				<div id="${crossSellcarousel.code}${status.count}" class="product-list carousel" data-dojo-type="tui.widget.carousels.Carousel">
					<div class="viewport">
						<ul class="plist">
							<c:forEach var="excursionData" items="${crossSellcarousel.excursions}" varStatus="status">
							<li>
								<div class="product">
									<div class="image-container dimen_medium">
									   <a href="${excursionData.url}" class="ensLinkTrack" data-componentId="${componentUid}">
                                            <images:first collection="${excursionData.galleryImages}" placeholder="${cdnDomain}/images/${sessionScope.siteName}/excursions-default-small.png" size="small" />
                                       </a>
									</div>
									<div class="min-height">
									<h4><a href="${excursionData.url}" class="ensLinkTrack" data-componentId="${componentUid}">${excursionData.name}</a></h4>

									<%--
									<c:if test="${excursionData.fromPrice!=null}">
										<h5>From
											<c:choose>
												<c:when test="${excursionData.fromPrice eq '0.00'}">FREE</c:when>
												<c:otherwise>
													<c:set var="price" value="${excursionData.fromPrice}"/>
													<c:set var="cleanPrice" value="${fn:replace(price,'.00','')}"/>
													&pound;${cleanPrice}
												</c:otherwise>
											</c:choose>
											per person
										</h5>
									</c:if>
									 --%>
									<p>
										<c:set var="stringToEllipse" value="${excursionData.description}"/>
										<c:set var="limit" value="120"/>
										<c:set var="length" value="${fn:length(stringToEllipse)}"/>
										<c:set var="secondHalf" value="${fn:substring(stringToEllipse, limit, length)}"/>
										<c:set var="secondHalfWords" value="${fn:split(secondHalf, ' ')}"/>
										${fn:substring(stringToEllipse, 0, limit)}${secondHalfWords[0]}&hellip;
									</p>
									<a href="${excursionData.bookingUrl}" class="ensLinkTrack" data-componentId="${componentUid}">View Details &raquo;</a>
									</div>
									<%--
									<div class="boxout floater">
										<c:if test="${excursionData.fromPrice!=null}">
											<p class="price left">
												From
												<c:choose>
													  <c:when test="${excursionData.fromPrice eq '0.00'}">
														  <strong>Free</strong>
													  </c:when>
													  <c:otherwise>
														  <c:set var="price" value="${excursionData.fromPrice}"/>
														   <c:set var="cleanPrice" value="${fn:replace(price,'.00','')}"/>
														   <strong>&pound;${cleanPrice}</strong>
													  </c:otherwise>
												  </c:choose>

												per person
											</p>
										</c:if>
										<a class="button right" href="${excursionData.bookingUrl}" class="ensLinkTrack" data-componentId="${componentUid}">View details</a>
									</div>
									--%>
								</div>
							</li>
							</c:forEach>
							<c:forEach var="attractionData" items="${crossSellcarousel.attractions}" varStatus="status">
							<li>
								<div class="product">
									<div class="image-container dimen_medium">
									   <a href="${attractionData.url}" class="ensLinkTrack" data-componentId="${componentUid}"><images:first collection="${attractionData.galleryImages}" placeholder="${cdnDomain}/images/${sessionScope.siteName}/default-small.png" size="small" /></a>
									</div>
									<div class="min-height">
									<h4><a href="${attractionData.url}" class="ensLinkTrack" data-componentId="${componentUid}">${attractionData.name}</a></h4>
									<p>${fn:substring(attractionData.editorialContent, 0, 120)}...</p>
									<a href="${attractionData.url}" class="ensLinkTrack" data-componentId="${componentUid}">View details &raquo;</a>
								</div>
								</div>
							</li>
							</c:forEach>
						</ul>
					</div>
					<p class="load">Loading</p>
				</div>
				</c:forEach>
			</div>
		</div>
	</div>
</c:if>