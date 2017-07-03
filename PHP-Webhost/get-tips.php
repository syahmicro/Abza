<?php
	
	include 'connection.php';
	
	$juz_no = $_POST['juz_no'];
	$response['juz_no'] = $juz_no;
	
	$query = mysql_query(" SELECT * FROM abzaa_tips WHERE status='Active' AND juz_no='".$juz_no."' ");
	
	$response['tips'] = array();
	$response['status'] = 0;
	
	while ($r = mysql_fetch_assoc($query)) {
		$response['status'] = 1;
		$arr = array();
		
		$arr['id'] = $r['id'];
		$arr['juz_no'] = $r['juz_no'];
		$arr['title'] = $r['title'];
		$arr['info'] = $r['info'];
		$arr['status'] = $r['status'];
		
		array_push($response['tips'], $arr);
	}	
	
	echo json_encode($response);
	
?>