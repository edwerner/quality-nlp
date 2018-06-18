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
	<div class="container flex flex-column flex-center">
		<div class="output flex flex-column">
			<div class="output-inner">
				<c:forEach var="entry" items="${inputArray}">
					<div class="entry">
						<c:out value="${entry}">
							<canvas id="dataChart"></canvas>
						</c:out>
					</div>
				</c:forEach>
			</div>	
		</div>
		<canvas id="dataChart"></canvas>
	
			<c:forEach var="entry" items="${countryPercentageMap}">
			 	<c:out value="${entry}"/>
			</c:forEach>
			
		<script>
			// needs two data sets created in javascript
			// first data set stores data
			// second data set stores random colors
			var context = document.getElementById('dataChart').getContext('2d');
			
			var data = {
			    datasets: [{
			        data: [10, 20, 30],
		        	backgroundColor: [
			        	"red",
			        	"yellow",
			        	"blue"
			        ],
			    }],
			
			    // These labels appear in the legend and in the tooltips when hovering different arcs
			    labels: [
			        "Red",
			        "Yellow",
			        "Blue"
			    ]
			};
			var myDoughnutChart = new Chart(context, {
			    type: 'doughnut',
			    data: data
			});
			
			function getRandomColor() {
			 	var letters = '0123456789ABCDEF';
			 	var color = '#';
			  	for (var i = 0; i < 6; i++) {
			    	color += letters[Math.floor(Math.random() * 16)];
			  	}
			  	return color;
			}
		</script>			
	</div>
</body>