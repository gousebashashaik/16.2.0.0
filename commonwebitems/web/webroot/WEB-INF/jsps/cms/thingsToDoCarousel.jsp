<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="images" tagdir="/WEB-INF/tags/images" %>
    <c:set var="webrootPath" value="${pageContext.request.contextPath}" />
    <c:set var="cdnDomain" value="${CDN_URL}" />
<script>
    dojoConfig.addModuleName("tui/widget/carousels/Carousel", "tui/widget/LazyLoadImage");
</script>
<c:if test="${accomPageType == 'bookflow' || accomPageType == 'cruise'}">
<script>
    dojoConfig.addModuleName("tui/widget/popup/LightboxPopup");
</script>
</c:if>
<!--Condition Stars--> <!-- thomson or firstchoise -->
	<c:set var="siteFlag" value="false"/> <!-- thomson or firstchoise -->
	<c:if test = "${sessionScope.siteName == 'cruise'}">
		<c:set var="siteFlag" value="true"/> <!--Cruise-->
	</c:if>
<!--Condition Ends-->
<!-- pageType: ${accomPageType} -->
<c:set var="excursions" value="${viewData.excursions}" scope="page"/>
<c:set var="sights" value="${viewData.sights}" scope="page"/>
<c:set var="events" value="${viewData.events}" scope="page"/>
<c:if test="${not empty excursions || not empty sights || not empty events}">
	<div class="${componentStyle}">
		<h2 <c:if test="${siteFlag eq true}"> class="underline pad-bot18 margin-bot12"</c:if>>${viewData.heading}
            <a class="view-all ensLinkTrack has-icon-right" href="${viewAllURL}" data-componentId="${componentUid}">
                View all <i class="icon arrow-main arrow-sm arrow-right arrow-right-bg">&raquo;</i>
            </a>
        </h2>
		<div class="product-list carousel" data-dojo-type="tui.widget.carousels.Carousel">
			<div class="viewport">
				<ul class="plist"<c:if test="${accomPageType == 'bookflow' || accomPageType == 'cruise'}"> data-dojo-type="tui.widget.popup.LightboxPopup" data-dojo-props='pageType: "${accomPageType}"'</c:if>>
					<c:forEach var="data" items="${excursions}" varStatus="status">
					<li>
						<div class="product" <c:if test="${siteFlag eq true}"> oncontextmenu="return false;"</c:if>>
							<div class="image-container dimen_medium">
							     <a href="${data.url}" class="ensLinkTrack" data-componentId="${componentUid}"><images:first collection="${data.galleryImages}" placeholder="${cdnDomain}/images/${sessionScope.siteName}/excursions-default-small.png" size="small"/></a>
							</div>

								<c:choose>
								<c:when test="${siteFlag eq true }">
								<h3>${data.name}</h3>
								</c:when>
								<c:otherwise>
								<h4><a href="${data.url}">${data.name}</a></h4>
								</c:otherwise>
								</c:choose>

							<%--<h5>From
							<c:choose>
								<c:when test="${data.fromPrice eq '0.00'}">
									FREE
								</c:when>
								<c:otherwise>
									<c:set var="price" value="${data.fromPrice}"/>
									<c:set var="cleanPrice" value="${fn:replace(price,'.00','')}"/>
									&pound;${cleanPrice}
								</c:otherwise>
							</c:choose>
							per person
							</h5>--%>
							<p>
								<c:if test="${not empty data.description}">

									<c:set var="stringToEllipse" value="${data.description}"/>
									<c:set var="limit" value="120"/>
									<c:set var="length" value="${fn:length(stringToEllipse)}"/>
									<c:set var="secondHalf" value="${fn:substring(stringToEllipse, limit, length)}"/>
									<c:set var="secondHalfWords" value="${fn:split(secondHalf, ' ')}"/>

									${fn:substring(stringToEllipse, 0, limit)}${secondHalfWords[0]}&hellip;

								</c:if>
							</p>
					 <c:choose>
						<c:when test="${siteFlag eq true }">
							<h2 class="more-link"><a class="view-all ensLinkTrack has-icon-left" href="${data.url}"><i class="icon arrow-main product-arrow-sm product-arrow-left arrow-right-bg">&#8227;</i> View details</a>
							</h2>
						</c:when>
						<c:otherwise>
							<a href="${data.url}" class="ensLinkTrack" data-componentId="${componentUid}">View details &raquo;</a>
						</c:otherwise>
					</c:choose>
						</div>
					</li>
					</c:forEach>

					<c:if test="${not empty events}">
						<c:forEach var="data" items="${events}" varStatus="status">
	                    <li>
	                        <div class="product">
	                            <div class="image-container dimen_medium">
	                                <a href="${data.url}" class="ensLinkTrack" data-componentId="${componentUid}"><images:first collection="${data.galleryImages}" placeholder="${cdnDomain}/images/${sessionScope.siteName}/events-default-small.png" size="small"/></a>
	                            </div>

								<c:choose>
								<c:when test="${siteFlag eq true }">
								  <h3>${data.name}  </h3>
								</c:when>
								<c:otherwise>
								  <h4><a href="${data.url}" class="ensLinkTrack" data-componentId="${componentUid}">${data.name}</a></h4>
								</c:otherwise>
								</c:choose>

	                            <p>
									<c:if test="${not empty data.editorialContent}">

										<c:set var="stringToEllipse" value="${data.editorialContent}"/>
										<c:set var="limit" value="120"/>
										<c:set var="length" value="${fn:length(stringToEllipse)}"/>
										<c:set var="secondHalf" value="${fn:substring(stringToEllipse, limit, length)}"/>
										<c:set var="secondHalfWords" value="${fn:split(secondHalf, ' ')}"/>

										${fn:substring(stringToEllipse, 0, limit)}${secondHalfWords[0]}&hellip;

									</c:if>
								</p>
								 <c:choose>
									<c:when test="${siteFlag eq true }">
										<h2 class="more-link"><a class="view-all ensLinkTrack has-icon-left" href="${data.url}"><i class="icon arrow-main product-arrow-sm product-arrow-left arrow-right-bg">&#8227;</i> View details</a>
										</h2>
									</c:when>
									<c:otherwise>
										<a href="${data.url}" class="ensLinkTrack" data-componentId="${componentUid}">View details &raquo;</a>
									</c:otherwise>
								</c:choose>
	                        </div>
	                    </li>
	                    </c:forEach>
	                </c:if>

	                <c:if test="${not empty sights}">
						<c:forEach var="data" items="${sights}" varStatus="status">
                        <li>
                        <div class="product">
                            <div class="image-container dimen_medium">
                                <a href="${data.url}" class="ensLinkTrack" data-componentId="${componentUid}"><images:first collection="${data.galleryImages}" placeholder="${cdnDomain}/images/${sessionScope.siteName}/default-small.png" size="small"/></a>
                            </div>

								<c:choose>
								<c:when test="${siteFlag eq true }">
								<h3>${data.name}  </h3>
								</c:when>
								<c:otherwise>
								<h4><a href="${data.url}" class="ensLinkTrack" data-componentId="${componentUid}">${data.name}</a></h4>
								</c:otherwise>
								</c:choose>

							<p>
								<c:if test="${not empty data.editorialContent}">

									<c:set var="stringToEllipse" value="${data.editorialContent}"/>
									<c:set var="limit" value="120"/>
									<c:set var="length" value="${fn:length(stringToEllipse)}"/>
									<c:set var="secondHalf" value="${fn:substring(stringToEllipse, limit, length)}"/>
									<c:set var="secondHalfWords" value="${fn:split(secondHalf, ' ')}"/>

									${fn:substring(stringToEllipse, 0, limit)}${secondHalfWords[0]}&hellip;

								</c:if>
							</p>
                            <c:choose>
									<c:when test="${siteFlag eq true }">
										<h2 class="more-link"><a class="view-all ensLinkTrack has-icon-left" href="${data.url}"><i class="icon arrow-main product-arrow-sm product-arrow-left arrow-right-bg">&#8227;</i> View details</a>
										</h2>
									</c:when>
									<c:otherwise>
										<a href="${data.url}" class="ensLinkTrack" data-componentId="${componentUid}">View details &raquo;</a>
									</c:otherwise>
								</c:choose>
							</div>
						</li>
						</c:forEach>
	                </c:if>
				</ul>
			</div>
			<p class="load">Loading</p>
		</div>
	</div>
</c:if>
