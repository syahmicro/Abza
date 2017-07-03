<?php
	include('connection.php');
	
	$gid = $_POST['group_id'];	
	
 	$findMember = mysql_query("SELECT * FROM abzaa_juz WHERE group_id = '".$gid."' ");
	
	while($r = mysql_fetch_assoc($findMember)) {
		$uid = $r['user_id'];
		//, champion = '0'
		mysql_query("UPDATE abzaa_juz 
					SET status = 'Late', completed = 'Late'
					WHERE user_id = '".$uid."' AND group_id = '".$gid."' AND status = 'Pending' ");	
	} 	
	
	$response['status'] = 1;
	
	echo json_encode($response);
?>