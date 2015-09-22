<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="webrootPath" value="${pageContext.request.contextPath}" />
<c:set var="cdnDomain" value="${CDN_URL}" />
<!--Condition Stars--> <!-- thomson or firstchoise -->
	<c:set var="siteFlag" value="false"/> <!-- thomson or firstchoise -->
	<c:if test = "${sessionScope.siteName == 'cruise'}">
		<c:set var="siteFlag" value="true"/> <!--Cruise-->
	</c:if>
<script type="text/javascript">
    dojoConfig.addModuleName("tui/widget/LazyLoadImage");
</script>
<c:choose>
    <c:when test="${accommodationData.accommodationType eq 'VILLA'}">
        <c:set var="title" value="Where is ${accommodationData.featureCodesAndValues.name[0]}?"/>
        <c:set var="subtitle" value="Location"/>
        <c:set var="gettingTo" value="Getting to the villa"/>
    </c:when>
    <c:otherwise>
        <c:set var="title" value="What's ${resortData.name} like?"/>
        <c:set var="subtitle" value="Where is the hotel?"/>
        <c:set var="gettingTo" value="Getting to the hotel?"/>
    </c:otherwise>
</c:choose>
<%-- Accommodation Location component (WF_COM_071) --%>
    <div class="span half-bottom-margin">
        <h2 class="underline">${title}</h2>
    </div>
    <div class="span-third">
        <h3 class="decase <c:if test='${siteFlag eq true }'>info </c:if>">${subtitle}</h3>
        <ul class="square">
        <c:forEach var="accommodationinfo" items="${accommodationData.featureCodesAndValues['locationInformation']}" varStatus="status">
            <li><span class="sprite-img-grp-1"></span>${accommodationinfo}</li>
        </c:forEach>
        </ul>
        <h3 class="decase <c:if test='${siteFlag eq true }'>info </c:if>">${gettingTo}</h3>
        <ul class="square">
            <li><span class="sprite-img-grp-1"></span>${accommodationData.featureCodesAndValues['transferTime'][0]} minutes from ${accommodationData.featureCodesAndValues['airportName'][0]} (by ${accommodationData.featureCodesAndValues['primaryTransferMode'][0]})</li>
            <c:if test="${not empty accommodationData.featureCodesAndValues['distanceFromAirport'][0] }">
            <li><span class="sprite-img-grp-1"></span>${accommodationData.featureCodesAndValues['distanceFromAirport'][0]}</li>
            </c:if>
        </ul>
    </div>
    <div class="span-two-third">
        <div class="span-third">
            <p>${resortData.featureCodesAndValues['intro1Body'][0]}
                <c:if test="${accomPageType != 'bookflow'}">
                 <c:choose>
						<c:when test="${siteFlag eq true }">
						<c:if test="${!bookMode}">
							<h2 class="more-link no-margin"><a class="view-all ensLinkTrack has-icon-left" href="${resortData.url}"><i class="icon arrow-main product-arrow-sm product-arrow-left arrow-right-bg">&#8227;</i> More about ${resortData.name}</a>
							</h2>
						</c:if>
						</c:when>
						<c:otherwise>
							<br /><span class="uc more-about-text"><a href="${resortData.url}">More about ${resortData.name} <i class="arrow-main arrow-med arrow-inline arrow-right-hl">&nbsp;</i></a></span>
						</c:otherwise>
					</c:choose>
				</c:if>
            </p>
        </div>
        <div class="span-quarter">
            <c:choose>
                <c:when test="${accommodationData.featureCodesAndValues['resortThumbnailUrl'] != null}">
                    <img src="${cdnDomain}/images/b.gif" data-dojo-type="tui.widget.LazyLoadImage" data-dojo-props="source:'${accommodationData.featureCodesAndValues['resortThumbnailUrl'][0]}'" width="232" height="130" alt="Location thumbnail" />
                </c:when>
                <c:otherwise>
                     <img src="${cdnDomain}/images/${sessionScope.siteName}/default-small.png" alt="Image coming soon" width="232" height="130" />
                </c:otherwise>
            </c:choose>
        </div>
    </div>