<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!--Condition Stars--> <!-- thomson or firstchoise --> 
	<c:set var="siteFlag" value="false"/> <!-- thomson or firstchoise --> 
	<c:if test = "${sessionScope.siteName == 'cruise'}">
		<c:set var="siteFlag" value="true"/> <!--Cruise-->
	</c:if>
<!--Condition Ends-->
	<div class="${componentStyle} <c:if test='${siteFlag eq true }'> padBottomZero</c:if>">
		<div class="boxout <c:if test='${siteFlag eq true }'>key-facts-table </c:if>">
			<h2>AT A GLANCE</h2>
			<ul  class="<c:if test='${siteFlag eq false }'> square </c:if>">
				<c:forEach items="${viewData.featureCodesAndValues['usps']}" var="usp" varStatus="status">
					<c:choose>
						<c:when test="${siteFlag eq true }">
							<c:choose>
								<c:when test="${status.count % 2 ==0}">
									<li class="striped-row-even">${usp}</li>
								</c:when>
								<c:otherwise>
									<li class="striped-row">${usp}</li>
								</c:otherwise>
							</c:choose>
						</c:when>
						<c:otherwise>
							<li><span class="sprite-img-grp-1"></span>${usp}</li>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</ul> 
		</div>
	</div>