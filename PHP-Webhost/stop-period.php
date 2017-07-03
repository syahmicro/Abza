<?php
	include('connection.php');
	
	$gid = $_POST['group_id'];
	$datetime = $_POST['spdatetimefull'];
	
	/* '".date('Y-m-d H:i:s')."' */

	mysql_query("UPDATE abzaa_group SET status = 'Inactive', stop_period = '".$datetime."', stop_period_status = '1' WHERE id = '".$gid."'") or die($response['error'] = mysql_error());
	
	
 	$findMember = mysql_query("SELECT * FROM abzaa_juz WHERE group_id = '".$gid."' ");
	
	while($r = mysql_fetch_assoc($findMember)) {
		$uid = $r['user_id'];
		//, champion = '0'
		mysql_query("UPDATE abzaa_juz 
					SET date_ended = '".$datetime."' , status = 'Pending'
					WHERE user_id = '".$uid."' AND group_id = '".$gid."' ");	
	} 	
	
	$response['status'] = 1;
	
	echo json_encode($response);
?>