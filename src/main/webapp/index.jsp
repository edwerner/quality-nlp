<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="d" %>
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
		<div class="match-found">
			<c:if test="${matchFound != null}">
				<div class="match-alert alert alert-success">
					<h1>Name Match Found!</h1>
				</div>
			</c:if>
		</div>
		<canvas id="dataChart"></canvas>
		<script>
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
			        'Red',
			        'Yellow',
			        'Blue'
			    ]
			};
			var myDoughnutChart = new Chart(context, {
			    type: 'doughnut',
			    data: data
			});
		</script>			
	</div>
</body>