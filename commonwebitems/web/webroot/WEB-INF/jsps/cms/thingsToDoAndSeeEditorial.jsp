<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<div class="${componentStyle}">
	<div class="when-to-go">
		<h2 class="underline">Things to see and do</h2>
		<c:if test="${not empty viewData.featureCodesAndValues['thingsToSeeAndDoEditorial'][0]}">
			<p>${viewData.featureCodesAndValues['thingsToSeeAndDoEditorial'][0]}</p>
		</c:if>
	</div>
</div>