<?php
	include 'connection.php';
	$status = 0;

	if (isset($_GET['id'])) {
		$id = addslashes($_GET['id']);

		mysql_query("UPDATE abzaa_tips SET status='Deleted' WHERE id= '".$id."' ");

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
									Successfully add remove tip.
								</div> ';
		              	}  
		            ?>
		               
	                <h5 class="m-t-lg with-border"> Tips & Extras</h5>
                
					<?php
						$query = mysql_query("SELECT * FROM abzaa_tips WHERE status = 'Active' ORDER BY juz_no");
						
						while ($r = mysql_fetch_assoc($query)) {
							echo 'Juz: '.$r['juz_no'].'<br>';
							echo '<b>'.$r['title'].'</b>: ';
							echo $r['info'].'<br><br>';
							?>
							<center><button type="button" name="remove" onclick="location.href='tips.php?id=<?php echo $r['id']; ?>'" class="btn btn-inline btn-danger">Remove</button></center>
							<?php
						}	
						
					?>
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