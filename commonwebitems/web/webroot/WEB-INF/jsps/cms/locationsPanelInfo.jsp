<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!--Condition Stars--> <!-- thomson or firstchoise --> 
	<c:set var="siteFlag" value="false"/> <!-- thomson or firstchoise --> 
	<c:if test = "${sessionScope.siteName == 'cruise'}">
		<c:set var="siteFlag" value="true"/> <!--Cruise-->
	</c:if>
<!--Condition Ends-->
<!-- COM_037 -->
<script type="text/javascript">
    dojoConfig.addModuleName("tui/widget/taggable/LocationPanelAnalytics");
</script>
  
<c:choose>
    <c:when test="${pageType == 'GULET_CRUISE'}">
       <c:set var="isSubType" value="false" />
    </c:when>
    <c:when test="${pageType == 'CRUISE_ONLY'}">
      <c:set var="isSubType" value="false" />
    </c:when>   
    <c:when test="${pageType == 'CRUISE_AND_STAY_OVERVIEW'}">
      <c:set var="isSubType" value="false" />
    </c:when>    
    <c:when test="${accomPageSubType == 'cruise_and_stay_overview'}">
       <c:set var="isSubType" value="false" />
    </c:when>
    <c:when test="${accomPageSubType == 'safari_and_stay_overview'}">
      <c:set var="isSubType" value="false" />
    </c:when>     
    <c:when test="${accomPageSubType == 'cruise_only'}">
       <c:set var="isSubType" value="false" />
    </c:when>
    <c:when test="${accomPageSubType == 'gulet_cruise'}">
      <c:set var="isSubType" value="false" />
    </c:when>     
    <c:otherwise>
        <c:set var="isSubType" value="true" />
    </c:otherwise>
</c:choose> 

<div class="${componentStyle}">
	<div class="boxout <c:if test='${siteFlag eq true }'>key-facts-table </c:if>" data-dojo-type="tui.widget.taggable.LocationPanelAnalytics">
		<h2>LOCATION</h2>
		<ul class="<c:if test='${siteFlag eq false }'> square </c:if>">
			<c:forEach var="info" items="${viewData.featureCodesAndValues['locationInformation']}"	varStatus="status">
				<c:choose>
						<c:when test="${siteFlag eq true }">
							<c:choose>
								<c:when test="${status.count % 2 ==0}">
									<li class="striped-row-even">${info}</li>
								</c:when>
								<c:otherwise>
									<li class="striped-row">${info}</li>
								</c:otherwise>
							</c:choose>
						</c:when>
						<c:otherwise>
							<li><span class="sprite-img-grp-1"></span>${info}</li>
						</c:otherwise>
					</c:choose>
			</c:forEach>
		</ul>
		<!--
		<p>${viewData.featureCodesAndValues['latitude'][0]}</p>
		<p>${viewData.featureCodesAndValues['longitude'][0]}</p> 
		-->
<c:choose>
	<c:when test="${siteFlag eq true }">
		<h2 class="more-link"><a class="view-all ensLinkTrack has-icon-left" href="${viewData.locationMapUrl}" data-componentId=""><i class="icon arrow-main product-arrow-sm product-arrow-left arrow-right-bg">&#8227;</i> See more about the location</a>
		</h2>
	</c:when>     
	<c:otherwise>
		<c:if test="${isSubType}">
		<p><a href="${viewData.locationMapUrl}" class="readmore ensLinkTrack" data-componentId="${componentUid}">See more about the location &raquo;</a></p>
		</c:if>
	</c:otherwise>
</c:choose> 
	</div>
</div>

