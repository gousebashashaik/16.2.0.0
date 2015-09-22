<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript">
	dojoConfig.addModuleName("tui/widget/WeatherChart");
</script>

<div class="${componentStyle}">
	<h2 class="underline">Weather</h2>
	<c:choose>
		<c:when test="${not empty viewData.weatherTypes}">
			<p>Average monthly temperature and rainfall for the ${destinations} location.</p>
			<p class="floater small"><span class="left">TEMPERATURE (&deg;C)</span><span class="right">RAINFALL (mm)</span></p>
			<div class="weather-chart" data-dojo-type="tui.widget.WeatherChart">	
				<table class="weather-data">
					<thead>
						<tr><th width="100">Month</th><th width="105">Temperature</th><th width="100">Rainfall</th></tr>
					</thead>
					<tbody>
					<c:forEach varStatus="status" begin="0" end="11" step="1">
						<tr>
							<td>${viewData.weatherTypes[0].weatherTypeValueViewDataList[status.count - 1].month}</td>
							<td>${viewData.weatherTypes[0].weatherTypeValueViewDataList[status.count - 1].average}</td>
							<td>${viewData.weatherTypes[1].weatherTypeValueViewDataList[status.count - 1].average}</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
		</c:when>
		<c:otherwise>
			<p>No weather data available.</p>
		</c:otherwise>
	</c:choose>
</div>