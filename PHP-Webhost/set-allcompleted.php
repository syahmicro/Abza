<?php
	
	include('connection.php');
	
	$uid = $_POST['user_id'];
	$gid = $_POST['group_id'];

	mysql_query("UPDATE abzaa_juz SET date_ended = '".date('Y-m-d H:i:s')."', status = 'Pending' WHERE user_id = '".$uid."' AND group_id = '".$gid."' AND status = 'Completed'");
			
	$response['status'] = 1;
	
	echo json_encode($response);
?>