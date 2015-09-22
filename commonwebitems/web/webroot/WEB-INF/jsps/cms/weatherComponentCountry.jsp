<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page import="com.google.gson.Gson"%>

<script type="text/javascript">
	dojoConfig.addModuleName("tui/widget/WeatherChart", "tui/widget/WeatherChartCreator");
</script>
	
<div class="${componentStyle}">
	<h2 class="underline">Weather</h3>
	<c:set var = "weatherData" value="${viewData}"/>
	<%	
		String jsonWeatherData = new Gson().toJson(pageContext.getAttribute("weatherData"));
	    request.setAttribute("jsonWeatherData", jsonWeatherData);
	%>
	<p <c:if test="${fn:length(viewData) == 1}">style="display:none" </c:if>>
		Average monthly temperature and rainfall for	
		<select data-dojo-type="tui.widget.WeatherChartCreator" data-dojo-props='jsonData:${jsonWeatherData}' class="dropdown weather-chart-dropdown branded">
			<c:forEach var="weatherData" items="${viewData}"  varStatus="counter">
				<option value="${weatherData.locationName}" >${weatherData.locationName}</option>
			</c:forEach>
		</select>
	</p>
	<p class="floater small"><span class="left">TEMPERATURE (&deg;C)</span><span class="right">RAINFALL (mm)</span></p>
	<div id="weatherChartContainer">
		<div class="weather-chart" id="weatherWidget"></div>
	</div>	
</div>
