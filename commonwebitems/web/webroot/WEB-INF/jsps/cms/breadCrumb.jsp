<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript">
    dojoConfig.addModuleName("tui/widget/taggable/Breadcrumb");
</script>

<%-- Breadcrumb component (WF_COM_051) --%>
<div class='span-three-quarter <c:choose><c:when test="${accomPageType != 'bookflow'}">no-bottom-margin</c:when><c:otherwise>half-bottom-margin</c:otherwise></c:choose>'>
<p id="breadcrumbs" data-dojo-type="tui.widget.taggable.Breadcrumb">

<c:choose>
    <c:when test="${accomPageType != 'bookflow'}">
	<c:forEach var="breadcrumb" items="${viewData}" varStatus="counter">
		<c:choose>
			<c:when test="${breadcrumb.linkClass eq 'active'}">
				<c:if test="${counter.index gt 0 }"> &raquo; </c:if>
				<c:out value="${breadcrumb.name}" escapeXml="false" />
			</c:when>
			<c:otherwise>				
				<c:if test="${counter.index gt 0 }"> &raquo; </c:if>
				<a href="${breadcrumb.url}" class="ensLinkTrack" data-componentId="${componentUid}">${breadcrumb.name}</a>
			</c:otherwise>
		</c:choose>
	</c:forEach>
    </c:when>
    <c:otherwise>
        in
        <c:forEach var="breadcrumb" items="${viewData}" varStatus="counter">
            ${breadcrumb.name}
            <c:if test="${!counter.last}">,&nbsp;</c:if>
        </c:forEach>
    </c:otherwise>
</c:choose>
</p>
</div>