<?php
        
    include("config.php");
	

	$query1 = mysqli_query($con, "SELECT * FROM `reservation`");
	while($row = mysqli_fetch_assoc($query1))
	{
		$array[] = array("tno"=>$row['Tno'],"rdate"=> $row['Rdate'],"starttime" => $row['StartTime'],"endtime" => $row['EndTime'],"deposit" => $row['DepositAmount'],"guest" => $row['Guests']);
	}
	echo json_encode($array);
?>

