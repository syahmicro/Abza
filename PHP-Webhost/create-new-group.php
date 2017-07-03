<?php
	include('connection.php');
	
	$groupName = addslashes($_POST['groupName']);
	$passcode = addslashes($_POST['passcode']);
	$user_id = $_POST['user_id'];
	$groupnull = null;
	$groupActivation = 'Inactive';
	
	$checkGroup = mysql_query("SELECT id FROM abzaa_group WHERE name = '".$groupName."' AND passcode = '".$passcode."' ");
	if (mysql_num_rows($checkGroup) == 0) {
		mysql_query("INSERT INTO abzaa_group (name, passcode, date_created, created_by, picture, status)
					VALUES ('".$groupName."', '".$passcode."', '".date('Y-m-d H:i:s')."', '".$user_id."', 'NULL', 'Inactive')") or die(mysql_error());
					
		$gid = mysql_insert_id();
		
		mysql_query("INSERT INTO abzaa_juz (user_id, group_id, round, juz_no, date_started, status) VALUES
					 ('".$user_id."', '".$gid."', 0, 0, '".date('Y-m-d H:i:s')."', 'Pending')
		");
					
		$response['status'] = 1;
		$response['passcode'] = $passcode;
		
		$findJuz2 = mysql_query("SELECT g.created_by, j.id 'JuzID', j.id, j.user_id, j.group_id, j.juz_no, j.date_started, g.picture, j.status, g.status as gstatus, g.name 
			FROM abzaa_group g, abzaa_juz j
			WHERE j.user_id = ".$user_id." AND j.group_id = g.id AND j.status != 'Passed'");
			$r2 = mysql_fetch_assoc($findJuz2);
			
			$response['id'] = $r2['id'];
			$response['user_id'] = $r2['user_id'];
			$response['group_id'] = $r2['group_id'];
			if ($r2['juz_no'] == 0) {
				$response['juz_no'] = 'not started';
			} else {
				$response['juz_no'] = $r2['juz_no'];
			}
			//$response['date_joined'] = $r2['date_joined'];
			$response['group_name'] = $r2['name'];
			$response['group_picture'] = $r2['picture'];
			$response['status1'] = $r2['status'];
			$response['gstatus'] = $r2['gstatus'];
			$response['created_by'] = $r2['created_by'];
	} else {
		$response['status'] = 2;
	}
	
	echo json_encode($response);
?>