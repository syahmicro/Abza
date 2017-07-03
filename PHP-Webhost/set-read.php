<?php
	
	include('connection.php');
	
	$uid = $_POST['user_id'];
	$gid = $_POST['group_id'];
	
	$query1 = mysql_query("SELECT id FROM abzaa_juz WHERE group_id = '".$gid."' AND user_id = '".$uid."' AND status = 'Completed'");
	if (mysql_num_rows($query1) == 0) {
		
		$getTime = mysql_query("SELECT date_started FROM abzaa_juz WHERE group_id = '".$gid."' AND user_id = '".$uid."' AND status = 'Pending'");
		$r = mysql_fetch_assoc($getTime);
	
		
		$startDate = new DateTime($r['date_started']);
		$today = new DateTime();
	    $difference = $startDate->diff($today);	
	    
	    $h = $difference->format("%d");
	    $d = $difference->format("%h");
	    
	    $tot = $h * 24 + $d;
	    
		$query = mysql_query("SELECT j.id FROM abzaa_juz j, abzaa_group g WHERE g.round = j.round AND j.group_id = '".$gid."' AND j.group_id = g.id AND j.status = 'Completed'");
	
		if (mysql_num_rows($query) == 0) {
			mysql_query("UPDATE abzaa_juz SET completed = 'Completed', status = 'Completed', champion = 1, date_ended = '".date('Y-m-d H:i:s')."', total_hour = '".$tot."' WHERE user_id = '".$uid."' AND group_id = '".$gid."' AND status = 'Pending'");
		} else {
			mysql_query("UPDATE abzaa_juz SET completed = 'Completed', status = 'Completed', date_ended = '".date('Y-m-d H:i:s')."', total_hour = '".$tot."'  WHERE user_id = '".$uid."' AND group_id = '".$gid."' AND status = 'Pending'");
		}
		
		$findlate = mysql_query("SELECT j.id FROM abzaa_juz j, abzaa_group g WHERE g.round = j.round AND j.group_id = '".$gid."' AND j.group_id = g.id AND j.status = 'Late'");
		if (mysql_num_rows($findlate) > 0) {
			mysql_query("UPDATE abzaa_juz SET champion = 0,status = 'Completed', date_ended = '".date('Y-m-d H:i:s')."', total_hour = '".$tot."' WHERE user_id = '".$uid."' AND group_id = '".$gid."' AND status = 'Late'");
		}
		
		
		$findCompleted = mysql_query("SELECT id FROM abzaa_juz WHERE group_id = '".$gid."' AND user_id = '".$uid."' AND status = 'Completed'");
		$response['totalCompleted'] = mysql_num_rows($findCompleted);
		
		$response['status'] = 1;
	} else {
		$response['status'] = 0;
	}
	
	echo json_encode($response);
?>