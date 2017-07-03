<?php 
	
	include('connection.php');
	
	$gid = $_POST['group_id'];
	
	$response['status'] = 1;
	
	$getChamp = mysql_query("SELECT id FROM abzaa_juz WHERE group_id = '".$gid."' AND champion = 1");
	$response['no_champ'] = mysql_num_rows($getChamp);	
	
	$getTotal = mysql_query("SELECT SUM(total_hour) as tot FROM abzaa_juz j, abzaa_group g 
WHERE j.round = g.round AND j.week = g.week AND g.id = j.group_id AND j.group_id = '".$gid."' ");
	$r2 = mysql_fetch_assoc($getTotal);
	
	$total = $r2['tot'];
	
	if ($total < 24) {
		$msg = $total.' hours';
	} else {
		$msg = (int)($total/24).' days, '.(int)($total%24).' hours';
	} 
	
	if ($msg == '0 hours') {
		$msg = '-';
	}
	$response['avg_time'] = $msg;
	
	$getLate = mysql_query("SELECT id FROM abzaa_juz WHERE group_id = '".$gid."' AND status = 'Expired'");
	$response['no_late'] = mysql_num_rows($getLate);
	
	$getComplete = mysql_query("SELECT * FROM abzaa_juz WHERE group_id = '".$gid."' AND status = 'Passed' AND completed = 'Completed'");
	$response['complete'] = mysql_num_rows($getComplete);
	
	$getChampList = mysql_query("SELECT u.id, u.fname, u.lname FROM abzaa_juz j, abzaa_user u WHERE j.user_id = u.id AND champion = 1 AND j.group_id = '".$gid."' GROUP BY id ");
	
	
	$response['champList'] = array();
	while ($r = mysql_fetch_assoc($getChampList)) {
		$list = array();
		
		
		$list['name'] = $r['fname'].' '.$r['lname'];
		
		$findCount = mysql_query("SELECT id FROM abzaa_juz WHERE user_id = '".$r['id']."' AND champion = 1 AND group_id = '".$gid."' ");
		$list['count'] = mysql_num_rows($findCount);
		
		array_push($response['champList'], $list);
	}
	
	echo json_encode($response);
	
?>