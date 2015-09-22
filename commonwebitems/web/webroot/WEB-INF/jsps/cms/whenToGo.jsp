
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!--Condition Stars--> <!-- thomson or firstchoise --> 
	<c:set var="siteFlag" value="false"/> <!-- thomson or firstchoise --> 
	<c:if test = "${sessionScope.siteName == 'cruise'}">
		<c:set var="siteFlag" value="true"/> <!--Cruise-->
	</c:if>
<!--Condition Ends-->
<div class="${componentStyle}">
	<div class="content-block <c:if test='${siteFlag eq true }'> boxout pad24 when-to-go</c:if>"  >
		<h2 class="underline <c:if test='${siteFlag eq true }'> no-underline </c:if>">WHEN TO GO</h2>
		<c:if test="${not empty viewData.featureCodesAndValues['whenToGo'][0]}">
		<p>${viewData.featureCodesAndValues['whenToGo'][0]}</p>
		</c:if>
	</div>
</div>


