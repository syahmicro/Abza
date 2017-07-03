<?php
	include('connection.php');
	
	$gid = $_POST['group_id'];
	$datetime = $_POST['spdatetimefull'];

	/* '".date('Y-m-d H:i:s')."' */
	
	mysql_query("UPDATE abzaa_group SET status = 'Active', round = 1, week = 1, date_period = '".$datetime."' WHERE id = '".$gid."'") or die($response['error'] = mysql_error());
	
	
	$findMember = mysql_query("SELECT * FROM abzaa_juz WHERE group_id = '".$gid."' ");
	
	$index = 1;
	while($r = mysql_fetch_assoc($findMember)) {
		$uid = $r['user_id'];
		
		mysql_query("UPDATE abzaa_juz 
					SET juz_no = '".$index."', date_started = '".$datetime."', week = '1', round = 1
					WHERE user_id = '".$uid."' AND group_id = '".$gid."' ");	
		$index++;
	}	
	
	$response['status'] = 1;
	
	echo json_encode($response);
?>