<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <c:set var="webrootPath" value="${pageContext.request.contextPath}" />
    <c:set var="cdnDomain" value="${CDN_URL}" />
<!--Condition Stars--> <!-- thomson or firstchoise -->
	<c:set var="siteFlag" value="false"/> <!-- thomson or firstchoise -->
	<c:if test = "${sessionScope.siteName == 'cruise'}">
		<c:set var="siteFlag" value="true"/> <!--Cruise-->
	</c:if>
<!--Condition Ends-->
<script type="text/javascript">
    dojoConfig.addModuleName("tui/widget/taggable/FacilitiesHighlights", "tui/widget/LazyLoadImage");
</script>
<c:set var="checkLink" value="true" />
<c:if
	test="${accommodationData.accommodationType eq 'SHIP'}">
	<c:set var="checkLink" value="false" />
</c:if>
<c:if test="${not empty accommodationData.facilities}">
	<div class="${componentStyle} span-two-third"
		data-dojo-type="tui.widget.taggable.FacilitiesHighlights">

		<h2 class="underline">
			Facilities
			<c:if test="${accommodationData.accommodationType ne 'SHIP'}">
				<a class="view-all ensLinkTrack"
					href="${accommodationData.facilitiesUrl}"
					data-componentId="${componentUid}">&#9658; View all </a>
			</c:if>
		</h2>

		<c:forEach var="facilityData" items="${accommodationData.facilities}"
			varStatus="counter">
			<div
				class="panel labeled image-left <c:if test="${counter.count == 1}">first</c:if>">
            <c:choose>
					<c:when test="${checkLink}">
						<a href="${accommodationData.facilitiesUrl}" class="ensLinkTrack"
							data-componentId="${componentUid}"> <c:choose>
                <c:when test="${not empty facilityData.galleryImages}">
                    <c:set var="imgplaced" value="false"/>
									<c:forEach var="image" items="${facilityData.galleryImages}"
										varStatus="counter">
                        <c:if test="${'medium' eq image.size && imgplaced eq 'false'}" >
											<img src="${cdnDomain}/images/b.gif"
												data-dojo-type="tui.widget.LazyLoadImage"
												data-dojo-props="source:'${image.mainSrc}'"
												alt="${image.altText}" width="360" height="201" />
                            <c:set var="imgplaced" value="true"/>
                        </c:if>
                    </c:forEach>
                </c:when>
                <c:otherwise>
									<img
										src="${cdnDomain}/images/${sessionScope.siteName}/default-large.png"
										alt="" width="360" height="201" />
                </c:otherwise>
            </c:choose>
            </a>
            <div class="copy">
							<a href="${accommodationData.facilitiesUrl}" class="ensLinkTrack"
								data-componentId="${componentUid}"><h3>${facilityData.name} <c:if test='${siteFlag eq true }'><i class="icon arrow-main product-arrow-b arrow-right arrow-right-bg">&#9658;</i></c:if></h3></a>
							<p>${facilityData.description}</p>
						</div>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${not empty facilityData.galleryImages}">
								<c:set var="imgplaced" value="false" />
								<c:forEach var="image" items="${facilityData.galleryImages}"
									varStatus="counter">
									<c:if test="${'medium' eq image.size && imgplaced eq 'false'}">
										<img src="${cdnDomain}/images/b.gif"
											data-dojo-type="tui.widget.LazyLoadImage"
											data-dojo-props="source:'${image.mainSrc}'"
											alt="${image.altText}" width="360" height="201" />
										<c:set var="imgplaced" value="true" />
									</c:if>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<img
									src="${cdnDomain}/images/${sessionScope.siteName}/default-large.png"
									alt="" width="360" height="201" />
							</c:otherwise>
						</c:choose>
						<div class="copy">
							<h3 class="product-highlight-title-nolink">${facilityData.name} <c:if test='${siteFlag eq true }'><i class="icon arrow-main product-arrow-b arrow-right arrow-right-bg">&#9658;</i></c:if></h3>
                <p>${facilityData.description}</p>
            </div>
					</c:otherwise>
				</c:choose>
        </div>
        </c:forEach>
    </div>
</c:if>