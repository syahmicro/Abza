<?php
	include 'connection.php';
	$status = 0;

	if (isset($_POST['submit'])) {
		$title = addslashes($_POST['title']);
		$info = addslashes($_POST['message']);
		$juz_no = addslashes($_POST['juz_no']);

		mysql_query("INSERT INTO abzaa_tips (title, info, juz_no, status) VALUES ('".$title."', '".$info."', '".$juz_no."', 'Active') ");

		$status = 1;
	}
   
?>

<!DOCTYPE html>


<html>
<head lang="en">
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
	<meta http-equiv="x-ua-compatible" content="ie=edge">
	<title>ABZAA</title>

	<link href="img/favicon.144x144.png" rel="apple-touch-icon" type="image/png" sizes="144x144">
	<link href="img/favicon.114x114.png" rel="apple-touch-icon" type="image/png" sizes="114x114">
	<link href="img/favicon.72x72.png" rel="apple-touch-icon" type="image/png" sizes="72x72">
	<link href="img/favicon.57x57.png" rel="apple-touch-icon" type="image/png">
	<link href="img/favicon.png" rel="icon" type="image/png">
	<link href="img/favicon.ico" rel="shortcut icon">

	<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
	<!--[if lt IE 9]>
	<script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
	<script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
	<![endif]-->
    <link rel="stylesheet" href="css/lib/font-awesome/font-awesome.min.css">
    <link rel="stylesheet" href="css/main.css">
</head>
<body>

    <div class="page-center">
        <div class="page-center-in">
            <div class="container-fluid">
                <div class="page-error-box">
	                <?php
		              	if ($status == 1) {
			              	echo '<div class="alert alert-success alert-icon alert-close alert-dismissible fade in" role="alert">
									<button type="button" class="close" data-dismiss="alert" aria-label="Close">
										<span aria-hidden="true">&times;</span>
									</button>
									<i class="font-icon font-icon-warning"></i>
									Successfully add new tips.
								</div> ';
		              	}  
		            ?>
		               
	                <h5 class="m-t-lg with-border">Add an Tips & Extras</h5>
                
                
	                <form method="POST" action="" name="add">
						<div class="form-group row">
							<label class="col-sm-2 form-control-label">Juz No</label>
							<div class="col-sm-10">
								<p class="form-control-static"><input type="text" name="juz_no" class="form-control"></p>
							</div>
						</div>
						<div class="form-group row">
							<label class="col-sm-2 form-control-label">Title</label>
							<div class="col-sm-10">
								<p class="form-control-static"><input type="text" name="title" class="form-control"></p>
							</div>
						</div>
						<div class="form-group row">
							<label class="col-sm-2 form-control-label">Info</label>
							<div class="col-sm-10">
								<p class="form-control-static"><textarea class="form-control" name="message"></textarea></p>
							</div>
						</div>
						<center><button type="submit" name="submit" class="btn btn-inline btn-primary">Add</button></center>
					</form>
					<br><center><button type="button" onclick="location.href='tips.php'" class="btn btn-inline btn-primary">Show All</button></center>
                </div>
            </div>
        </div>
    </div><!--.page-center-->
    <script src="js/lib/jquery/jquery.min.js"></script>
	<script src="js/lib/tether/tether.min.js"></script>
	<script src="js/lib/bootstrap/bootstrap.min.js"></script>
	<script src="js/plugins.js"></script>

<script src="js/app.js"></script>
</body>
</html>