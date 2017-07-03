<?php
	include('connection.php');
	
	$uid = $_POST['user_id'];
	$gid = $_POST['group_id'];
	$response['status'] = 1;
	
	$getChamp = mysql_query("SELECT id FROM abzaa_juz WHERE user_id = '".$uid."' AND group_id = '".$gid."' AND champion = 1 AND completed = 'Completed'");
	$response['no_champ'] = mysql_num_rows($getChamp);	
	
	
	$getTotalHour = mysql_query("SELECT SUM(total_hour) as total FROM abzaa_juz j, abzaa_group g 
WHERE j.round = g.round AND j.week = g.week AND g.id = j.group_id AND j.user_id = '".$uid."' AND j.group_id = '".$gid."' ");
	$r = mysql_fetch_assoc($getTotalHour);
	$total = $r['total'];
	
	if ($total < 24) {
		$msg = $total.' hours';
	} else {
		$msg = (int)($total/24).' days, '.(int)($total%24).' hours';
	} 
	
	if ($msg == ' hours') {
		$msg = '-';
	}
	$response['avg_time'] = $msg;
	
	$getLate = mysql_query("SELECT id FROM abzaa_juz WHERE user_id = '".$uid."' AND group_id = '".$gid."' AND completed = 'Late' AND status = 'Passed'");
	$response['no_late'] = mysql_num_rows($getLate);
	
	echo json_encode($response);
	
?>