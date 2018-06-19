<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!DOCTYPE HTML>
<head>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" 
        integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <link href="https://fonts.googleapis.com/css?family=Inconsolata" rel="stylesheet">
    <style><%@ include file="styles.css"%></style>
    <style><%@ include file="flex.css"%></style>
    <script src="https://d3js.org/d3.v5.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.4.0/Chart.min.js"></script>
</head>
<body>
    <div id="charts" class="flex flex-center flex-column">
    	<h1 class="headline">1994 United States Census Data</h1>
		<script>
			function getRandomColor() {
			 	var letters = '0123456789ABCDEF';
			 	var color = '#';
			  	for (var i = 0; i < 6; i++) {
			    	color += letters[Math.floor(Math.random() * 16)];
			  	}
			  	return color;
			}
		</script>
        <div id="country-chart" class="chart navbar navbar-default flex flex-center flex-column">
            <h2>Country</h2>
            <canvas id="countryChart" class="chartCanvas"></canvas>
            <script>
                var countryContext = document.getElementById('countryChart').getContext('2d');
                var data = {
                    datasets: [{
                    data: [
                	    <c:forEach var="entry" items="${countryPercentageMap}">
                	 		<c:out value="${entry.value}" escapeXml="false"/>,
                		</c:forEach>
                	],
                       backgroundColor: [
                	    <c:forEach var="entry" items="${countryPercentageMap}">
                	 		<c:out value="getRandomColor()" escapeXml="false"/>,
                		</c:forEach>
                 	],
                }],
                labels: [
                <c:forEach var="entry" items="${countryPercentageMap}">
             		<c:out value="\"${entry.key}\"" escapeXml="false"/>,
            	</c:forEach>
                ]};
                var countryChart = new Chart(countryContext, {
                    type: 'doughnut',
                    data: data
                });
            </script>		
        </div>
    </div>
</body>