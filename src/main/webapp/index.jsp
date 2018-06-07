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
		<div class="navigation-bar">
	   		<nav class="navbar navbar-default">
		      <div class="container-fluid">
		         <div class="navbar-header">
		        	<button type="button" class="hint-button-01 btn btn-secondary btn-lg">Begins with</button
		         </div>
		      </div>
		   </nav>
		</div>
		<div class="content flex flex-row flex-center">	
			<form class="chat-form" method="POST" action="/">
  				<div class="form-container col-lg-6">
    				<div class="input-group">
      					<input type="text" name="input" class="form-control" placeholder="Guess a name, any name">
      					<span class="input-group-btn">
        				<button type="submit" value="submit" class="btn btn-primary">Submit</button>
      					</span>
    				</div>
  				</div>
			</form>
		</div>
		<div class="output flex flex-column">
			<div class="output-inner">
				<c:forEach var="entry" items="${inputArray}">
					<div class="entry">
						<c:out value="${entry}"/>
					</div>
				</c:forEach>
			</div>	
		</div>			
	</div>
</body>