<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!--Condition Stars--> <!-- thomson or firstchoise --> 
	<c:set var="siteFlag" value="false"/> <!-- thomson or firstchoise --> 
	<c:if test = "${sessionScope.siteName == 'cruise'}">
		<c:set var="siteFlag" value="true"/> <!--Cruise-->
	</c:if>
<!--Condition Ends-->

<div class="${componentStyle}">
	<div class="key-facts <c:if test='${siteFlag eq true }'>key-facts-withul</c:if>">
		<h2 class="<c:if test='${siteFlag eq false }'>underline</c:if>">Key facts</h2>
		<c:choose>
		<c:when test="${siteFlag eq true }">
		<ul class="square">
					<li>Currency: ${viewData.featureCodesAndValues['currency'][0]}</li>
					<li>Language: ${viewData.featureCodesAndValues['language'][0]}</li>
				</ul>	
				<ul class="square">
					<li>Flight duration: ${viewData.featureCodesAndValues['flightDurationFromUk'][0]}</li>
					<li>Timezones: ${viewData.featureCodesAndValues['timezone'][0]}</li>
				</ul>
		</c:when>
		<c:otherwise>
		<dl class="half">
					<dt>Currency:</dt>
					<dd>${viewData.featureCodesAndValues['currency'][0]}</dd>
					<dt>Language:</dt>
					<dd>${viewData.featureCodesAndValues['language'][0]}</dd>
				</dl>
				<dl class="half">
					<dt>Flight duration:</dt>
					<dd>${viewData.featureCodesAndValues['flightDurationFromUk'][0]}</dd>
					<dt>Timezones:</dt>
					<dd>${viewData.featureCodesAndValues['timezone'][0]}</dd>
				</dl>
		</c:otherwise>
		</c:choose>
	</div>
</div>
