<?php
	$email = $_REQUEST['email'];
        
    include("config.php");
	
	$query = mysqli_query($con,"SELECT Cid FROM customer WHERE Email='$email'");
	$cid = mysqli_fetch_assoc($query);
	$cid = $cid['Cid'];
	
	$query1 = mysqli_query($con, "SELECT * FROM `reservation` WHERE Cid = '$cid'");
	while($row = mysqli_fetch_assoc($query1))
	{
		$array[] = array("tno"=>$row['Tno'],"rdate"=> $row['Rdate'],"starttime" => $row['StartTime'],"endtime" => $row['EndTime'],"deposit" => $row['DepositAmount'],"guest" => $row['Guests']);
	}
	echo json_encode($array);
?>
