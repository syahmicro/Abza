<?php
	include('connection.php');
	
	//$gid = 5;
	
	$findGroup = mysql_query("
		SELECT id, date_period, name FROM abzaa_group 
		WHERE DATE_FORMAT(DATE_ADD(date_period, INTERVAL 7 DAY),'%Y') = DATE_FORMAT(NOW(),'%Y') AND
			  DATE_FORMAT(DATE_ADD(date_period, INTERVAL 7 DAY),'%m') = DATE_FORMAT(NOW(),'%m') AND
			  DATE_FORMAT(DATE_ADD(date_period, INTERVAL 7 DAY),'%d') = DATE_FORMAT(NOW(),'%d') AND status = 'Active'
		
	");
	
	echo 'Total updated group: '.mysql_num_rows($findGroup);
	
	while ($rGroup = mysql_fetch_assoc($findGroup)) {
		$gid = $rGroup['id'];
		
		echo "Group name: " . $rGroup['name'] . '</br></br>';
		
		
		$query = mysql_query("SELECT g.id, j.user_id, g.week, g.round, j.juz_no FROM abzaa_juz j, abzaa_group g WHERE g.id = '".$gid."' AND g.id = j.group_id AND g.round = j.round AND j.status != 'Passed'");

	
		$findWeek = mysql_query("SELECT round, week FROM abzaa_group WHERE id = '".$gid."' ");
		$fw = mysql_fetch_assoc($findWeek);
		$week = $fw['week'];
		$round = $fw['round'] + 1;
		
		$dateEnd = date('Y-m-d H:i:s');
		
		if ($week == 30) {
			$week = 1;
			
			setHour($gid);
			
			mysql_query("UPDATE abzaa_juz SET status = 'Passed' WHERE group_id = '".$gid."'");
			
			mysql_query("UPDATE abzaa_group SET week = '".$week."', week = '".$week."', date_period = '".date('Y-m-d H:i:s')."' WHERE id = '".$gid."' ");
			
			$count = 1;
			
			while ($r = mysql_fetch_assoc($query)) {
				
				$juzNo = $r['juz_no'];
				//echo $r['juz_no'];
				if ($juzNo == 30) {
					$juzNo = 1;
				} else {
					$juzNo = $juzNo + 1;
				}
				
				mysql_query("INSERT INTO abzaa_juz (user_id, group_id, round, week, juz_no, date_started, status)
							 VALUES ('".$r['user_id']."', '".$gid."', '".$r['round']."', '".$week."', '".$count."', '".date('Y-m-d H:i:s')."', 'Pending')");
			
				$count++;
				
			}
			
		} else {
			$week = $week + 1;
			setHour($gid);
			mysql_query("UPDATE abzaa_juz SET status = 'Passed' WHERE group_id = '".$gid."'");	
			mysql_query("UPDATE abzaa_group SET week = '".$week."', date_period = '".date('Y-m-d H:i:s')."' WHERE id = '".$gid."' ");

			while ($r = mysql_fetch_assoc($query)) {
				
				$juzNo = $r['juz_no'];
				if ($juzNo == 30) {
					$juzNo = 1;
				} else {
					$juzNo = $juzNo + 1;
				}
				
				mysql_query("INSERT INTO abzaa_juz (user_id, group_id, round, week, juz_no, date_started, status)
							 VALUES ('".$r['user_id']."', '".$gid."', '".$r['round']."', '".$week."', '".$juzNo."', '".date('Y-m-d H:i:s')."', 'Pending')");
			}
		}
	}
	
	function setHour($gid) {
		$q = mysql_query("SELECT date_started FROM abzaa_juz WHERE group_id = '".$gid."' AND date_ended = '0000-00-00 00:00:00' AND status = 'Pending'");
		
		$r = mysql_fetch_assoc($q);
		$startDate = new DateTime($r['date_started']);
		$today = new DateTime();
	    $difference = $startDate->diff($today);	    
	    $h = $difference->format("%d");
	    $d = $difference->format("%h");

	    $tot = $h * 24 + $d;
	    
	    mysql_query("UPDATE abzaa_juz SET date_ended = '".date('Y-m-d H:i:s')."', total_hour = '".$tot."', completed = 'Late' WHERE group_id = '".$gid."' AND status = 'Pending' ");
	}
?>