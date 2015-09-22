<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cache" uri="http://hybris.com/tld/ywebcache"%>
    <c:set var="webrootPath" value="${pageContext.request.contextPath}" />
    <div id="wrapper">
    	<c:if test="${webalert.displayComponent}">
	       <div class="web-alert">
			 <div class="left">				
				 <c:if test="${webalert.displayOnPage == 'ALLBROWSE'}">
					<div
						<c:choose>
		    			<c:when test="${webalert.severity == 'NEWSALERT'}">
		        			class="title-description news-alert"
		    			</c:when>
		    			<c:when test="${webalert.severity == 'SEVEREALERT'}">
		       				 class="title-description severe-alert"
		    			</c:when>
		    			<c:when test="${webalert.severity == 'STANDARDALERT'}">
		       				 class="title-description standard-alert"
		    			</c:when>
						</c:choose>
						<c:if test="${empty webalert.alertText}">id="no-description"</c:if>>
	     				<a href="${webalert.alertURL}" target="_blank" class="title">
							<span>${webalert.alertHeading}</span> <c:if
								test="${empty webalert.alertText}">&#9658;</c:if>
						</a> <span class="date">Updated ${webAlertTime}</span>
                        <div>
							<a href="${webalert.alertURL}" target="_blank"
								class="description"> <span class="des-text">${webalert.alertText}</span>
								<c:if test="${not empty webalert.alertText}">
									<span>&#9658;</span>
								</c:if></a>
	        		    </div>
	                </div>
	             </c:if>	         
	          </div>	       
	        </div>
	    </c:if>
    
	    <c:if test="${darkSite.displayComponent}">
	    <script type="text/javascript">
		  dojoConfig.addModuleName("tui/widget/DarkSitePopup");
	    </script>
	    <c:set var="darkSiteText" value="${darkSite.darkSiteText}" />
  		<c:set var="search" value="'" />
    	<c:set var="replace" value="\\'" />
		<c:set var="darkSiteText"
			value="${fn:replace(darkSiteText, search, replace)}" />
		<c:set var="search" value='"' />
    	<c:set var="replace" value='~' />
		<c:set var="darkSiteText"
			value="${fn:replace(darkSiteText, search, replace)}" />
		<div data-dojo-type="tui.widget.DarkSitePopup"
			data-dojo-props="darkSiteText:'${darkSiteText}', darkSiteHeading:'${darkSite.darkSiteHeading}', darkSiteUrl:'${darkSite.darkSiteURL}',darkSiteTime:'${darkSiteTime}'">
	    </div>
	    </c:if>

	    
        <div id="header">
		<cache:cache cache="fragmentCache" id="template1-header">
	        <cms:slot contentSlot="${cms_standard_header}" var="comp">
	            <cms:component component="${comp}" />
	        </cms:slot>
		</cache:cache>
		<cms:slot contentSlot="${cms_search_panel}" var="comp">
			<cms:component component="${comp}" />
		</cms:slot>
        </div>
		<div id="content">

			<div class="top">
				<cms:slot contentSlot="${cms_common_top}" var="comp">
					<cms:component component="${comp}" />
				</cms:slot>
				<!-- top END -->
			</div>

			<div class="main">
				<cms:slot contentSlot="${cms_common_main}" var="comp">
					<cms:component component="${comp}" />
				</cms:slot>
				<!-- main END -->
			</div>

			<div class="sidebar">
				<cms:slot contentSlot="${cms_common_sidebar}" var="comp">
					<cms:component component="${comp}" />
				</cms:slot>
				<!-- sidebar END -->
			</div>

			<div class="lower">
				<cms:slot contentSlot="${cms_common_lower}" var="comp">
					<cms:component component="${comp}" />
				</cms:slot>
				<!-- lower END -->
			</div>

			<!-- content END -->

		</div>
	<cache:cache cache="fragmentCache" id="template1-inner-footer">
		<div id="inner-footer">
	    	<cms:slot contentSlot="${cms_standard_innerfooter}" var="comp">
	            <cms:component component="${comp}" />
	        </cms:slot>
	    </div>
	</cache:cache>
    </div>

    <footer id="footer">
	<cache:cache cache="fragmentCache" id="template1-footer">
        <cms:slot contentSlot="${cms_standard_footer}" var="comp">
            <cms:component component="${comp}" />
        </cms:slot>
	</cache:cache>
	<cms:slot contentSlot="${cms_analytics_footer}" var="comp">
		<cms:component component="${comp}" />
	</cms:slot>
    </footer>
