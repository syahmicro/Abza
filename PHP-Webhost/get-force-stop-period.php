<?php
	include('connection.php');
	
	$gid = $_POST['group_id'];

	//get saved day for display // AND force_stop_period != 'null'
	//------------------------------------------------------------------------------------------------------------------------------
	$getday = mysql_query("SELECT force_stop_period FROM abzaa_group WHERE id = '".$gid."' AND force_stop_period != 'null'");
	
	if (mysql_num_rows($getday) > 0)
	{
		$response['status'] = 1;
		
		/* $response['daySaved'] = $selectedDay; */
		while ($r = mysql_fetch_assoc($getday)) {
			$response['FSP'] = $r['force_stop_period'];
		}
	}
	else {
		$response['status'] = 0;
	}
	echo json_encode($response);
?>