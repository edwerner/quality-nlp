<!DOCTYPE HTML>
<head>
	<!-- Latest compiled and minified CSS -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" 
	integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
	<style><%@ include file="styles.css"%></style>
	<style><%@ include file="flex.css"%></style>
</head>
<body>
	<div class="container flex flex-row flex-center">
		<div class="content flex flex-row flex-center">
			<form class="chat-form" method="POST" action="/">
  				<div class="col-lg-6">
    					<div class="input-group">
      						<input type="text" name="input" class="form-control" placeholder="Ask me anything">
      						<span class="input-group-btn">
        						<button type="submit" value="submit" class="btn btn-primary">Submit</button>
      						</span>
    					</div>
  				</div>
			</form>	
		</div>			
	</div>
</body>