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
			var colors = [];
			function getRandomColor() {
			 	var letters = '0123456789ABCDEF';
			 	var color = '#';
			  	for (var i = 0; i < 6; i++) {
			    	color += letters[Math.floor(Math.random() * 16)];
			  	}
			  	if (!colors.includes(color)) {
			  		colors.push(color);
			  		colors.sort();
			  		return color;
			  	}
			  	return color;
			}
		</script>
        
        <div id="occupation-chart" class="chart navbar navbar-default flex flex-center flex-column">
            <h2>Occupation</h2>
            <canvas id="occupationChart" class="chartCanvas"></canvas>
            <script>
                var occupationContext = document.getElementById('occupationChart').getContext('2d');
                var occupationData = {
                    datasets: [{
                    data: [
                	    <c:forEach var="entry" items="${occupationPercentageMap}">
                	 		<c:out value="${entry.value}" escapeXml="false"/>,
                		</c:forEach>
                	],
                       backgroundColor: [
                	    <c:forEach var="entry" items="${occupationPercentageMap}">
                	 		<c:out value="getRandomColor()" escapeXml="false"/>,
                		</c:forEach>
                 	],
                }],
                labels: [
                <c:forEach var="entry" items="${occupationPercentageMap}">
             		<c:out value="\"${entry.key}\"" escapeXml="false"/>,
            	</c:forEach>
                ]};
                var occupationChart = new Chart(occupationContext, {
                    type: 'doughnut',
                    data: occupationData
                });
            </script>		
        </div>
        
        
        <div id="age-chart" class="chart navbar navbar-default flex flex-center flex-column">
            <h2>Age</h2>
            <canvas id="ageChart" class="chartCanvas"></canvas>
            <script>
                var ageContext = document.getElementById('ageChart').getContext('2d');
                var ageData = {
                    datasets: [{
                    data: [
                	    <c:forEach var="entry" items="${agePercentageMap}">
                	 		<c:out value="${entry.value}" escapeXml="false"/>,
                		</c:forEach>
                	],
                       backgroundColor: [
                	    <c:forEach var="entry" items="${agePercentageMap}">
                	 		<c:out value="getRandomColor()" escapeXml="false"/>,
                		</c:forEach>
                 	],
                }],
                labels: [
                <c:forEach var="entry" items="${agePercentageMap}">
             		<c:out value="\"${entry.key}\"" escapeXml="false"/>,
            	</c:forEach>
                ]};
                var ageChart = new Chart(ageContext, {
                    type: 'doughnut',
                    data: ageData
                });
            </script>		
        </div>
        
        <div id="education-chart" class="chart navbar navbar-default flex flex-center flex-column">
            <h2>Education</h2>
            <canvas id="educationChart" class="chartCanvas"></canvas>
            <script>
                var educationContext = document.getElementById('educationChart').getContext('2d');
                var educationData = {
                    datasets: [{
                    data: [
                	    <c:forEach var="entry" items="${educationPercentageMap}">
                	 		<c:out value="${entry.value}" escapeXml="false"/>,
                		</c:forEach>
                	],
                       backgroundColor: [
                	    <c:forEach var="entry" items="${educationPercentageMap}">
                	 		<c:out value="getRandomColor()" escapeXml="false"/>,
                		</c:forEach>
                 	],
                }],
                labels: [
                <c:forEach var="entry" items="${educationPercentageMap}">
             		<c:out value="\"${entry.key}\"" escapeXml="false"/>,
            	</c:forEach>
                ]};
                var occupationChart = new Chart(educationContext, {
                    type: 'doughnut',
                    data: educationData
                });
            </script>		
        </div>
        
        <div id="marital-chart" class="chart navbar navbar-default flex flex-center flex-column">
            <h2>Marital Status</h2>
            <canvas id="maritalChart" class="chartCanvas"></canvas>
            <script>
                var maritalContext = document.getElementById('maritalChart').getContext('2d');
                var maritalData = {
                    datasets: [{
                    data: [
                	    <c:forEach var="entry" items="${maritalPercentageMap}">
                	 		<c:out value="${entry.value}" escapeXml="false"/>,
                		</c:forEach>
                	],
                       backgroundColor: [
                	    <c:forEach var="entry" items="${maritalPercentageMap}">
                	 		<c:out value="getRandomColor()" escapeXml="false"/>,
                		</c:forEach>
                 	],
                }],
                labels: [
                <c:forEach var="entry" items="${maritalPercentageMap}">
             		<c:out value="\"${entry.key}\"" escapeXml="false"/>,
            	</c:forEach>
                ]};
                var maritalChart = new Chart(maritalContext, {
                    type: 'doughnut',
                    data: maritalData
                });
            </script>		
        </div>
        
        <div id="income-chart" class="chart navbar navbar-default flex flex-center flex-column">
            <h2>Annual Income</h2>
            <canvas id="incomeChart" class="chartCanvas"></canvas>
            <script>
                var incomeContext = document.getElementById('incomeChart').getContext('2d');
                var incomeData = {
                    datasets: [{
                    data: [
                	    <c:forEach var="entry" items="${incomePercentageMap}">
                	 		<c:out value="${entry.value}" escapeXml="false"/>,
                		</c:forEach>
                	],
                       backgroundColor: [
                	    <c:forEach var="entry" items="${incomePercentageMap}">
                	 		<c:out value="getRandomColor()" escapeXml="false"/>,
                		</c:forEach>
                 	],
                }],
                labels: [
                <c:forEach var="entry" items="${incomePercentageMap}">
             		<c:out value="\"${entry.key}\"" escapeXml="false"/>,
            	</c:forEach>
                ]};
                var maritalChart = new Chart(incomeContext, {
                    type: 'doughnut',
                    data: incomeData
                });
            </script>		
        </div>
        
        
        <div id="gender-chart" class="chart navbar navbar-default flex flex-center flex-column">
            <h2>Gender</h2>
            <canvas id="genderChart" class="chartCanvas"></canvas>
            <script>
                var genderContext = document.getElementById('genderChart').getContext('2d');
                var genderData = {
                    datasets: [{
                    data: [
                	    <c:forEach var="entry" items="${genderPercentageMap}">
                	 		<c:out value="${entry.value}" escapeXml="false"/>,
                		</c:forEach>
                	],
                       backgroundColor: [
                	    <c:forEach var="entry" items="${genderPercentageMap}">
                	 		<c:out value="getRandomColor()" escapeXml="false"/>,
                		</c:forEach>
                 	],
                }],
                labels: [
                <c:forEach var="entry" items="${genderPercentageMap}">
             		<c:out value="\"${entry.key}\"" escapeXml="false"/>,
            	</c:forEach>
                ]};
                var genderChart = new Chart(genderContext, {
                    type: 'doughnut',
                    data: genderData
                });
            </script>		
        </div>
        
        
        <div id="race-chart" class="chart navbar navbar-default flex flex-center flex-column">
            <h2>Race</h2>
            <canvas id="raceChart" class="chartCanvas"></canvas>
            <script>
                var raceContext = document.getElementById('raceChart').getContext('2d');
                var raceData = {
                    datasets: [{
                    data: [
                	    <c:forEach var="entry" items="${racePercentageMap}">
                	 		<c:out value="${entry.value}" escapeXml="false"/>,
                		</c:forEach>
                	],
                       backgroundColor: [
                	    <c:forEach var="entry" items="${racePercentageMap}">
                	 		<c:out value="getRandomColor()" escapeXml="false"/>,
                		</c:forEach>
                 	],
                }],
                labels: [
                <c:forEach var="entry" items="${racePercentageMap}">
             		<c:out value="\"${entry.key}\"" escapeXml="false"/>,
            	</c:forEach>
                ]};
                var raceChart = new Chart(raceContext, {
                    type: 'doughnut',
                    data: raceData
                });
            </script>		
        </div>
        
        
        
        <div id="country-chart" class="chart navbar navbar-default flex flex-center flex-column">
            <h2>Country</h2>
            <canvas id="countryChart" class="chartCanvas"></canvas>
            <script>
                var countryContext = document.getElementById('countryChart').getContext('2d');
                var countryData = {
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
                    data: countryData
                });
            </script>		
        </div>
        
        
    </div>
</body>