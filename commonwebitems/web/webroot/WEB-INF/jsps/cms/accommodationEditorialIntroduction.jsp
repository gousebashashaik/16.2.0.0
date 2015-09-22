<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="java.util.regex.Pattern"%>

<script type="text/javascript">
   dojoConfig.addModuleName("tui/widget/IntroExpandable","tui/widget/modules/PopupModule","tui/widget/popup/Tooltips", "tui/widget/TripAdvisorSmoothScroll");
</script>

<c:if test="${not empty viewData.featureCodesAndValues['strapline'][0]}"> 
 	<p class="intro-strapline">${viewData.featureCodesAndValues['strapline'][0]}</p>
</c:if>

<div class="${componentStyle} intro-ta-keyfacts clearfix" data-dojo-type="tui.widget.IntroExpandable" data-dojo-props="openTxt:'Hide more &lsaquo;&lsaquo;'">
   <c:if test="${not empty viewData.ratingBar || travelAward eq 'true'}"> 
      <c:set var="className" value="fl intro-text"/>
    </c:if>    
    <p id="intro-main-copy" class="copy ${className}">   	
    	<c:choose>
	    	<c:when test="${not empty viewData.featureCodesAndValues['overview_1_new'][0]}">
	    		${viewData.featureCodesAndValues['overview_1_new'][0]}
	    	</c:when>
	    	<c:otherwise>
	    		${viewData.featureCodesAndValues['introduction'][0]}
	    	</c:otherwise>
    	</c:choose> 
    <c:if test="${not empty viewData.featureCodesAndValues['overview_2_new'][0]}">
    	<br/><br/>${viewData.featureCodesAndValues['overview_2_new'][0]}
    </c:if>
    <c:if test="${not empty viewData.featureCodesAndValues['overview_3_new'][0]}">
       <span class="copy-show-hide"><br/><br/>${viewData.featureCodesAndValues['overview_3_new'][0]}</span>
       <a href="javascript:void(0);" class="link-hide-show">Read more &rsaquo;&rsaquo; </a>
    </c:if>
    </p>
   <c:if test="${not empty viewData.ratingBar}">
       <div class="fl trip-advisor-summary" data-dojo-type="tui.widget.TripAdvisorSmoothScroll" data-dojo-props="target:'tripAdvisor'">
           <p class="title">TripAdvisor Traveller Rating</p>
           <img alt="" src="${viewData.ratingBar}" class="ensLinkTrack" data-componentId="${componentUid}">
           <p class="based-on">Based On <a href="javascript:void(0);" class="sprite-img-grp-1" >${viewData.reviewCount} reviews</a></p>
       </div>
    </c:if>
     <c:forEach var="travel" items="${viewData.featureCodesAndValues['awards']}" varStatus="counter">
     <c:if test="${fn:contains(travel, 'Travellers')}"><img class="travel-choice"  src="/holiday/images/TravelersChoice.jpg"/></c:if>
     </c:forEach>

   <c:set var="officalratingstarsize" value="${fn:length(viewData.featureCodesAndValues['officialRating'][0])}"/>
	<c:choose>
        <c:when test="${fn:startsWith(viewData.featureCodesAndValues['officialRating'][0], '5 Star')==true && officalratingstarsize lt 7 }">
            <c:set var="officialRating" value="is-5star"/>
        </c:when>
       <c:when test="${fn:startsWith(viewData.featureCodesAndValues['officialRating'][0], '4 Star')==true && officalratingstarsize lt 7 }">
            <c:set var="officialRating" value="is-4star"/>
        </c:when>
        <c:when test="${fn:startsWith(viewData.featureCodesAndValues['officialRating'][0], '3 Star')==true && officalratingstarsize lt 7 }">
            <c:set var="officialRating" value="is-3star"/>
        </c:when>
        <c:when test="${fn:startsWith(viewData.featureCodesAndValues['officialRating'][0], '2 Star')==true && officalratingstarsize lt 7 }">
            <c:set var="officialRating" value="is-2star"/>
        </c:when>
        <c:when test="${fn:startsWith(viewData.featureCodesAndValues['officialRating'][0], '1 Star')==true && officalratingstarsize lt 7 }">
            <c:set var="officialRating" value="is-1star"/>
        </c:when>
        <c:otherwise>
            <!--<c:set var="officialRating" value="none"/>-->
            <c:set var="officialRatingtext" value = "${viewData.featureCodesAndValues['officialRating'][0] }"/>
            
                        
        </c:otherwise>
    </c:choose>

<c:set var="diffProduct" value=""/>
<c:if test="${not empty viewData.productRanges[0].code}">
    <c:set var="diffProduct" value="${fn:toLowerCase(viewData.productRanges[0].code)}"/>
</c:if>

<%-- TODO: Apparantly Buildings, floors, rooms and lifts are not numbers some time. Based on what is being returned,
we need to decide where to show them on the front end.. This is a hack to handle this for go live.
In long term we need to restrict the data to numbers or change the UI maybe --%>
<c:set var="noOfBuildings" value = "${viewData.featureCodesAndValues['noOfBuildings'][0] }"/>
<c:set var="noOfFloors" value = "${viewData.featureCodesAndValues['noOfFloors'][0] }"/>
<c:set var="noOfRooms" value = "${viewData.featureCodesAndValues['noOfrooms'][0] }"/>
<c:set var="noOfLifts" value = "${viewData.featureCodesAndValues['noOfLifts'][0] }"/>
<c:set var="dismag" value = "${viewData.featureCodesAndValues['DISMAG'][0] }"/>

<%
    if (pageContext.getAttribute("noOfBuildings") != null)
    {
        String buildings = pageContext.getAttribute("noOfBuildings").toString();
    request.setAttribute("isBuildingsaNumber", false);
    if (buildings != null && Pattern.compile("\\d+").matcher(buildings).matches()){
        request.setAttribute("isBuildingsaNumber", true);
    }
    }

    if (pageContext.getAttribute("noOfFloors") != null)
    {
        String floors = pageContext.getAttribute("noOfFloors").toString();
    request.setAttribute("isFlooraNumber", false);
    if (floors != null && Pattern.compile("\\d+").matcher(floors).matches()){
        request.setAttribute("isFlooraNumber", true);
    }
    }
    if (pageContext.getAttribute("noOfRooms") != null)
    {
        String rooms = pageContext.getAttribute("noOfRooms").toString();
    request.setAttribute("isRoomaNumber", false);
    if (rooms != null && Pattern.compile("\\d+").matcher(rooms).matches()){
        request.setAttribute("isRoomaNumber", true);
    }
    }

    if (pageContext.getAttribute("noOfLifts") != null)
    {
        String lifts = pageContext.getAttribute("noOfLifts").toString();
    request.setAttribute("isLiftaNumber", false);
    if (lifts != null && Pattern.compile("\\d+").matcher(lifts).matches()){
        request.setAttribute("isLiftaNumber", true);
    }
    }

%>

<c:set var="ratingStyle" value="is-${fn:replace(viewData.featureCodesAndValues['officialRating'][0],' ', '')}"/>
<c:set var="officalratingtexsize" value="${fn:length(officialRatingtext)}"/>
<%-- <c:set var="wifi" value = "${viewData.accomwifi }"/> --%>


   <ul class="keyfacts">
        <c:if test="${not empty officialRating && officialRating ne 'none'}"><li id="rate"><span class="${diffProduct} hotel ${officialRating}"></span>Official Rating</li></c:if>
        <c:if test="${not empty officialRatingtext && officalratingtexsize lt 14}"><li><br/>Official Rating &nbsp;&nbsp;<span></span><br/>${officialRatingtext}</li></c:if>
        <c:forEach var="gold" items="${viewData.featureCodesAndValues['awards']}" varStatus="counter">
            <c:if test="${fn:contains(gold, 'Gold Medal')}"><li><span class="goldAward" data-dojo-type="tui.widget.popup.Tooltips" data-dojo-props="floatWhere:'position-top-center', text:'Awarded to the best hotel in its class according to your feedback'"></span></li></c:if>
      </c:forEach>
      <c:if test="${not empty noOfBuildings && isBuildingsaNumber eq 'true'}"><li><span class="number">${noOfBuildings}</span><c:choose><c:when test="${not empty noOfBuildings && isBuildingsaNumber eq 'true' && noOfBuildings gt 1}">Buildings</c:when><c:otherwise>Building</c:otherwise></c:choose></li></c:if>
      <c:if test="${not empty noOfFloors && isFlooraNumber eq 'true'}"><li><span class="number">${noOfFloors}</span><c:choose><c:when test="${not empty noOfFloors && isFlooraNumber eq 'true' && noOfFloors gt 1}">Floors</c:when><c:otherwise>Floor</c:otherwise></c:choose></li></c:if>
      <c:if test="${not empty noOfRooms && isRoomaNumber eq 'true'}"><li><span class="number">${noOfRooms}</span><c:choose><c:when test="${not empty noOfRooms && isRoomaNumber eq 'true' && noOfRooms gt 1}">Rooms</c:when><c:otherwise>Room</c:otherwise></c:choose></li></c:if>
      <c:if test="${not empty noOfLifts && isLiftaNumber eq 'true' && noOfLifts gt 0}"><li><span class="number">${noOfLifts}</span><c:choose><c:when test="${not empty noOfLifts && isLiftaNumber eq 'true' && noOfLifts gt 1}">Lifts</c:when><c:otherwise>Lift</c:otherwise></c:choose></li></c:if>
<!--     <li id="only-available">Only from First Choice <small>In the UK, this hotel is only available from the First Choice Group</small></li> -->
<c:if test="${viewData.accomwifi}"><li><span class="wifi-img" data-dojo-type="tui.widget.popup.Tooltips" data-dojo-props="floatWhere:'position-top-center', text:'Free Wi-Fi zones available. See FACILITIES tab for details.'"></span></li> </c:if>
        <c:if test="${not empty officialRatingtext && officalratingtexsize gt 13}"><li class="string-data"><span class="title">Official Rating: </span>${officialRatingtext}</li></c:if> <!--For officialrating Long text display  --> 
      <c:if test="${not empty noOfBuildings && isBuildingsaNumber eq 'false'}"><li class="string-data first"><span class="title">Buildings: </span>${noOfBuildings}</li></c:if>
      <c:if test="${not empty noOfFloors && isFlooraNumber eq 'false'}"><li class="string-data"><span class="title">Floors: </span>${noOfFloors}</li> </c:if>
      <c:if test="${not empty noOfRooms && isRoomaNumber eq 'false'}"><li class="string-data"><span class="title">Rooms: </span>${noOfRooms}</li> </c:if>
      <c:if test="${not empty noOfLifts && isLiftaNumber eq 'false'}"><li class="string-data"><span class="title">Lifts: </span>${noOfLifts}</li> </c:if>
      
      
   </ul>

<c:if test="${not empty dismag}">   
   <div class="disneymagic">
      <img src="http://phoenix-business-config/destinations/images/FLO_DIS_ANK_F047.jpg">
   </div>
</c:if>
   
</div>