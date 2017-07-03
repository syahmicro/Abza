<?php
	include('connection.php');
	
	$gid = $_POST['group_id'];
	$dayid = $_POST['day'];
	$fspDate = $_POST['fspdate'];

	mysql_query("UPDATE abzaa_group SET force_stop_period = '".$dayid."' , force_stop_period_date = '".$fspDate."' WHERE id = '".$gid."'") or die($response['error'] = mysql_error());
	
	$response['status'] = 1;
	
	echo json_encode($response);
?>