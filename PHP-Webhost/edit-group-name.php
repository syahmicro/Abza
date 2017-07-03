<?php
	include('connection.php');
	
	$groupName = addslashes($_POST['groupName']);
	$groupId = $_POST['groupId'];
	
	mysql_query("UPDATE abzaa_group SET name = '".$groupName."' WHERE id = '".$groupId."' ");
	
	$response['status'] = 1;
	$response['id'] = $groupId;
	
	echo json_encode($response);	

?>