<?php
	include('connection.php');
	
	$gid = $_POST['group_id'];
	$uid = $_POST['user_id'];
	
	$getInfo = mysql_query("SELECT g.date_period,g.stop_period,g.stop_period_status,g.force_stop_period_date, g.name, g.round, g.week, g.status,j.date_started FROM abzaa_group g, abzaa_juz j WHERE g.id = '".$gid."' AND g.id = j.group_id AND j.user_id = '".$uid."'");
	$r = mysql_fetch_assoc($getInfo);
	$response['name'] = $r['name'];
	$response['round'] = $r['round'];
	$response['week'] = $r['week'] . '/30';
	
	$date_started = $r['date_period'];
	$datetime = DateTime::createFromFormat('Y-m-d H:i:s', $date_started);
	$response['dateperiod'] = $datetime->format('Y-m-d H:i:s');
	
	$stopPeriod = $r['stop_period'];
	$stopPerioddate = DateTime::createFromFormat('Y-m-d H:i:s', $stopPeriod);
	$response['stopperioddate'] = $stopPerioddate->format('Y-m-d H:i:s');
	
	$forcestopPeriod = $r['force_stop_period_date'];
	$forcestopPerioddate = DateTime::createFromFormat('Y-m-d H:i:s', $forcestopPeriod);
	$response['force_stop_perioddate'] = $forcestopPerioddate->format('Y-m-d H:i:s');
	
	if ($r['status'] == 'Inactive') {
		$response['date'] = 'Not started';
		$response['currentdate'] = 'Not started';
	} else {
		$response['date'] = 'Every '.$datetime->format('l').', 11.59PM';
		$response['currentdate'] = $datetime->format('l');
	}
	
	$date_started_userJuz = $r['date_started'];
	$datetimeStarted = DateTime::createFromFormat('Y-m-d H:i:s', $date_started_userJuz);
	$response['datestarted'] = $datetimeStarted->format('Y-m-d H:i:s');
	
	
	$response['stopperiodstatus'] = $r['stop_period_status'];
		
	$getList = mysql_query("
		SELECT u.fname, u.lname,u.picture, j.champion, j.status, j.juz_no
		FROM abzaa_group g, abzaa_juz j, abzaa_user u
		WHERE g.id = j.group_id AND j.user_id = u.id AND g.id = '".$gid."' AND j.status != 'Passed'
	");
		
	if (mysql_num_rows($getList) > 0) {
		$response['status'] = 1;
		$response['list'] = array();
		
		while ($r = mysql_fetch_assoc($getList)) {
			$list = array();
			$list['name'] = $r['fname'] . ' '. $r['lname'];
			$list['juz_read'] = $r['juz_no'];
			$list['champ'] = $r['champion'];
			$list['status'] = $r['status'];
			$list['picture'] = $r['picture'];
			
			array_push($response['list'], $list);		
		}
	} else {
		$response['status'] = 0;
	}
	echo json_encode($response);
?>