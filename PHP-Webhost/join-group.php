<?php
	include('connection.php');
	
	$groupName = addslashes($_POST['groupName']);
	$passcode = addslashes($_POST['passcode']);
	$user_id = $_POST['user_id'];
	
	$findGroup = mysql_query("SELECT * FROM abzaa_group WHERE name = '".$groupName."' AND passcode = '".$passcode."' ");
	
	if (mysql_num_rows($findGroup) == 1) {
		// Group found
		$r = mysql_fetch_assoc($findGroup);
		$group_id = $r['id'];
		$week = $r['week'];
		$round = $r['round'];
		
		$findJuz = mysql_query("SELECT * FROM abzaa_juz WHERE user_id = '".$user_id."' AND group_id = '".$group_id."' ");
		if (mysql_num_rows($findJuz) > 0) {
			$response['status'] = 2;
		} else {
			$findTotalUser = mysql_query("SELECT g.round
										FROM abzaa_group g, abzaa_juz j 
										WHERE g.id = j.group_id AND g.id = '".$group_id."'
										AND g.round = j.round");
										
			if (mysql_num_rows($findTotalUser) < 30) { 
				
				if ($round != 0) {
					$getTotalMember = mysql_query("SELECT id FROM abzaa_juz WHERE week = '".$week."' AND round = '".$round."' AND group_id='".$group_id."' ");
					$totalMember = mysql_num_rows($getTotalMember);
					
					$nextJuz = 0;
					if ($totalMember + $week > 30) {
						$nextJuz = $totalMember + $week - 30;
					} else {
						$nextJuz = $totalMember + $week;
					}
					mysql_query("INSERT INTO abzaa_juz (user_id, group_id, juz_no, date_started, status) 
									VALUES(".$user_id.", ".$group_id.", '".$nextJuz."', '".date('Y-m-d H:i:s')."', 'Pending')
									");	
					
					
				} else {
					mysql_query("INSERT INTO abzaa_juz (user_id, group_id, juz_no, date_started, status) 
									VALUES(".$user_id.", ".$group_id.", 'not started', '".date('Y-m-d H:i:s')."', 'Pending')
									");		
				}
				
				$response['status'] = 1;
				
				$findJuz2 = mysql_query("SELECT g.created_by,j.id 'JuzID', j.id, j.user_id, j.group_id, j.juz_no, j.date_started, g.picture, j.status, g.status as gstatus, g.name 
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
				$response['status'] = 3;
				$response['message'] = 'Group full';
			}
		}
	} else {
		$response['status'] = 0;
	}
	
	echo json_encode($response);
?>