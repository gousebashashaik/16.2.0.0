<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
 <%@ taglib prefix="jsonUtil" uri="/WEB-INF/tags/json/Json.tld"%>
<c:set var="resortsData" value="${viewData.locationsData}" scope="page"/>
<c:set var="hotelsData" value="${viewData.accommodationsData}" scope="page"/>
<c:set var="villaData" value="${viewData.villaData}" scope="page"/>
<c:set var="webrootPath" value="${pageContext.request.contextPath}" />
<c:set var="cdnDomain" value="${CDN_URL}" />
<!--Condition Stars--> <!-- thomson or firstchoise -->
	<c:set var="siteFlag" value="false"/> <!-- thomson or firstchoise -->
	<c:if test = "${sessionScope.siteName == 'cruise'}">
		<c:set var="siteFlag" value="true"/> <!--Cruise-->
	</c:if>
<!--Condition Ends-->
<!-- ${jsonUtil:toJson(viewData)} -->
<script>
    dojoConfig.addModuleName("tui/widget/popup/Tooltips", "tui/widget/LazyLoadImage", "tui/widget/taggable/TopPlaces");
</script>
<div class="span half-bottom-margin">
	<c:if test="${not empty resortsData.locationDatas}">
   <div class="top-x title-left">
		<h2>
			<span class="top">Top <c:choose><c:when test="${fn:length(resortsData.locationDatas) > 4 }">5</c:when><c:otherwise>${fn:length(resortsData.locationDatas)}</c:otherwise></c:choose></span>
			<span class="location"><c:choose><c:when test="${fn:length(resortsData.locationDatas) eq 1 }">resort</c:when><c:otherwise>resorts</c:otherwise></c:choose></span>
			<span class="in"><c:if test="${not empty viewData.location}">in <c:if test="${siteFlag eq true }"><br/></c:if>${viewData.location.name}</c:if></span>
		</h2>
		<div class="product-list">
			<div class="viewport">
				<ul class="plist" data-dojo-type="tui.widget.taggable.TopPlaces">
					<c:if test="${not empty resortsData.locationDatas}">
					<c:forEach var="resort" items="${resortsData.locationDatas}" begin="0" end="${resortsData.visibleItems-1}" varStatus="counter">
					<li>
						<div class="product xs <c:if test='${siteFlag eq true }'> topBtmlayout</c:if>">
							<div class="image-container">
								<c:choose>
									<c:when test="${siteFlag eq true }">
										<span class="count">${counter.index+1}</span> <span class="corner-top"></span>
									</c:when>
									<c:otherwise>
										<span>${counter.index+1}</span>
									</c:otherwise>
								</c:choose>
								<c:set var="imgplaced" value="false"/>
								<c:forEach var="image" items="${resort.images}" varStatus="status">
								    <c:if test="${image.format eq 'thumbnail' && imgplaced eq 'false'}">
                                <a href="${resort.url}" class="ensLinkTrack" data-componentId="${componentUid}"><img src="${cdnDomain}/images/b.gif" data-dojo-type="tui.widget.LazyLoadImage" data-dojo-props="source:'${image.url}'" alt="${resort.name}" width="145" height="83" /></a>
								        <c:set var="imgplaced" value="true"/>
								    </c:if>
								</c:forEach>
                                <c:if test="${imgplaced eq 'false'}">
                                    <img src="${cdnDomain}/images/${sessionScope.siteName}/default-small.png" alt="" width="145"/>
                                </c:if>
							</div>
							<h3 <c:if test='${siteFlag eq true }'>class="padLeftZero"</c:if>>
								<a href="${resort.url}" class="ensLinkTrack" data-componentId="${componentUid}">${resort.name}</a>
							</h3>
							<%--<p class="price left">From &pound;<fmt:formatNumber type="number" maxFractionDigits="2" value="${resort.priceFrom}" /></p>--%>
						</div>
					</li>
					</c:forEach>
					</c:if>
				</ul>
			</div>
		</div>
	</div>
	<div class="clear"></div>
	</c:if>
	<c:if test="${not empty hotelsData.accomodationDatas}">
   <div class="top-x title-left">
		<h2>
			<span class="top">Top <c:choose><c:when test="${fn:length(hotelsData.accomodationDatas) > 4 }">5</c:when><c:otherwise>${fn:length(hotelsData.accomodationDatas)}</c:otherwise></c:choose></span>
			<span class="location"><c:choose><c:when test="${fn:length(hotelsData.accomodationDatas) eq 1 }">hotel</c:when><c:otherwise>hotels</c:otherwise></c:choose></span>
			<span class="in"><c:if test="${not empty viewData.location}">in <c:if test="${siteFlag eq true }"><br/></c:if>${viewData.location.name}</c:if></span>
		</h2>
		<div class="product-list">
			<div class="viewport">
				<ul class="plist" data-dojo-type="tui.widget.taggable.TopPlaces">
					<c:if test="${not empty hotelsData.accomodationDatas}">
					<c:forEach var="hotel" items="${hotelsData.accomodationDatas}" begin="0" end="${hotelsData.visibleItems-1}" varStatus="counter">
					<li>
						<c:set var="isSpecial" value=""/>
						<c:if test="${not empty hotel.productRanges[0].code}">
							<c:set var="isSpecial" value="special"/>
						</c:if>
						<div class="product xs ${isSpecial} <c:if test='${siteFlag eq true }'> topBtmlayout</c:if>">
							<div class="image-container">
                        <c:set var="className" value="diff-label ${fn:toLowerCase(hotel.productRanges[0].code)}" />
							<c:if test="${fn:toLowerCase(hotel.productRanges[0].code) eq 'smr'}">
							   <c:set var="className" value="diff-label ${fn:toLowerCase(hotel.productRanges[0].code)}-sensimar" />
							</c:if>

                        <span data-dojo-type="tui.widget.popup.Tooltips" data-dojo-props="floatWhere:'position-top-center', text:'${hotel.productRanges[0].featureCodesAndValues.strapline[0]}'" class="${className}"></span>
								<span class="count">${counter.index+1}</span><c:if test="${siteFlag eq true }"> <span class="corner-top"></span></c:if>
								<c:set var="imgplaced" value="false"/>
								<c:forEach var="image" items="${hotel.images}" varStatus="status">
								<c:if test="${image.format eq 'thumbnail' && imgplaced eq 'false'}">
                           <a href="${hotel.url}" class="ensLinkTrack" data-componentId="${componentUid}"><img src="${cdnDomain}/images/b.gif" data-dojo-type="tui.widget.LazyLoadImage" data-dojo-props="source:'${image.url}'" alt="${hotel.name}" width="145" height="83" /></a>
									<c:set var="imgplaced" value="true"/>
								</c:if>
								</c:forEach>
								<c:if test="${imgplaced eq 'false'}">
                                    <img src="${cdnDomain}/images/${sessionScope.siteName}/default-small.png" alt="" width="145" />
                                </c:if>
							</div>
							<h3 <c:if test='${siteFlag eq true }'>class="padLeftZero"</c:if>>
								<a href="${hotel.url}" class="ensLinkTrack" data-componentId="${componentUid}">${hotel.name}</a>
							</h3>
							<c:if test="${siteFlag eq false }">
							<p class="price left">From &pound;<fmt:formatNumber type="number" maxFractionDigits="2" value="${hotel.priceFrom}" /></p>
							</c:if>
						</div>
					</li>
					</c:forEach>
					</c:if>
				</ul>
			</div>
		</div>
	</div>
	<div class="clear"></div>
	</c:if>
	<c:if test="${not empty villaData.accomodationDatas}">
   <div class="top-x title-left">
		<h2>
			<span class="top">Top <c:choose><c:when test="${fn:length(villaData.accomodationDatas) > 4 }">5</c:when><c:otherwise>${fn:length(villaData.accomodationDatas)}</c:otherwise></c:choose></span>
			<span class="location"><c:choose><c:when test="${fn:length(villaData.accomodationDatas) eq 1 }">villa</c:when><c:otherwise>villas</c:otherwise></c:choose></span>
			<span class="in"><c:if test="${not empty viewData.location}">in <c:if test="${siteFlag eq true }"><br/></c:if>${viewData.location.name}</c:if></span>
		</h2>
		<div class="product-list">
			<div class="viewport">
				<ul class="plist" data-dojo-type="tui.widget.taggable.TopPlaces">
					<c:if test="${not empty villaData.accomodationDatas}">
					<c:forEach var="hotel" items="${villaData.accomodationDatas}" begin="0" end="${villaData.visibleItems-1}" varStatus="counter">
					<li>
						<c:set var="isSpecial" value=""/>
						<c:if test="${not empty hotel.productRanges[0].code}">
							<c:set var="isSpecial" value="special"/>
						</c:if>
						<div class="product xs ${isSpecial} <c:if test='${siteFlag eq true }'> topBtmlayout</c:if>">
							<div class="image-container">
                       <c:set var="className" value="diff-label ${fn:toLowerCase(hotel.productRanges[0].code)}" />
							<c:if test="${fn:toLowerCase(hotel.productRanges[0].code) eq 'smr'}">
							   <c:set var="className" value="diff-label ${fn:toLowerCase(hotel.productRanges[0].code)}-sensimar" />
							</c:if>

                        <span data-dojo-type="tui.widget.popup.Tooltips" data-dojo-props="floatWhere:'position-top-center', text:'${hotel.productRanges[0].featureCodesAndValues.strapline[0]}'" class="${className}"></span>
								<span class="count">${counter.index+1}</span><c:if test="${siteFlag eq true }"> <span class="corner-top"></span></c:if>
								<c:set var="imgplaced" value="false"/>
								<c:forEach var="image" items="${hotel.images}" varStatus="status">
								<c:if test="${image.format eq 'thumbnail' && imgplaced eq 'false'}">
                           <a href="${hotel.url}" class="ensLinkTrack" data-componentId="${componentUid}"><img src="${cdnDomain}/images/b.gif" data-dojo-type="tui.widget.LazyLoadImage" data-dojo-props="source:'${image.url}'" alt="${hotel.name}" width="145" height="83" /></a>
									<c:set var="imgplaced" value="true"/>
								</c:if>
								</c:forEach>
								<c:if test="${imgplaced eq 'false'}">
                                    <img src="${cdnDomain}/images/${sessionScope.siteName}/default-small.png" alt="" width="145" />
                                </c:if>
							</div>
							<h3 <c:if test='${siteFlag eq true }'>class="padLeftZero"</c:if>>
								<a href="${hotel.url}" class="ensLinkTrack" data-componentId="${componentUid}">${hotel.name}</a>
							</h3>
							<p class="price left">From &pound;<fmt:formatNumber type="number" maxFractionDigits="2" value="${hotel.priceFrom}" /></p>
						</div>
					</li>
					</c:forEach>
					</c:if>
				</ul>
			</div>
		</div>
	</div>
	<div class="clear"></div>
	</c:if>
</div>