<?php
	include('connection.php');
	
	$phone = addslashes($_POST['phone']);
	$password = addslashes($_POST['password']);
	
	$checkAccount = mysql_query("SELECT * FROM abzaa_user WHERE phone = '".$phone."' AND password = '".$password."' ");
	
	if (mysql_num_rows($checkAccount) == 1) {
		$response['status'] = 1;
		$r = mysql_fetch_assoc($checkAccount);
		$response['id'] = $r['id'];
		$response['fname'] = $r['fname'];
		$response['lname'] = $r['lname'];
		$response['phone'] = $phone;
		$response['pwd'] = $password;
		$response['groups'] = array();
		
		$checkGroup = mysql_query("SELECT g.created_by, j.id 'JuzID', j.id, j.user_id, j.group_id, j.juz_no, j.date_started, g.picture, j.status, g.status as gstatus, g.name 
			FROM abzaa_group g, abzaa_juz j
			WHERE j.user_id = ".$r['id']." AND j.group_id = g.id AND j.status != 'Passed'");	
		if (mysql_num_rows($checkGroup) > 0) {
			$response['groups'] = array();
			while ($r2 = mysql_fetch_assoc($checkGroup)) {
				$group = array();
				
				$group['id'] = $r2['id'];
				$group['user_id'] = $r2['user_id'];
				$group['group_id'] = $r2['group_id'];
				if ($r2['juz_no'] == 0) {
					$group['juz_no'] = 'not started';
				} else {
					$group['juz_no'] = $r2['juz_no'];
				}
				// $group['date_joined'] = $r2['date_joined']; date_joined has been comment on 3/5/2017
				$group['group_name'] = $r2['name'];
				$group['group_picture'] = $r2['picture'];
				$group['status'] = $r2['status'];
				$group['gstatus'] = $r2['gstatus'];
				$group['created_by'] = $r2['created_by'];
			
				array_push($response['groups'], $group);
				
			}
		}
		
	} else {
		$response['status'] = 0;
	}
	
	echo json_encode($response);
?>