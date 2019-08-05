<?PHP
	/*
		this page is single operation only.
	*/

	require("../apnetwork/dbconfig.php");
	
	$myuser = $_GET['username'];//$_POST["username"];
	$myhash = $_GET['hash'];//$_POST["hash"]
	$sessionip = $_SERVER["REMOTE_ADDR"];

	$token = NULL;
	// login
	if (strlen($myuser) > 3 && strlen($myhash) > 5) {
		$sql = new mysqli($sqlAddress, $sqlUsername, $sqlPassword, $sqlDatabase, $sqlPort);
		if (!$sql->connect_errno) {	
			
			$ps = $sql->prepare("CALL WSProc_GenerateSessionOnAuth(?, ?, ?);");
			if ($ps) {
				
				$ps->bind_param("sss", $myuser, $myhash, $sessionip);
				if ($ps->execute()) {
					
					$sqlResult = $ps->get_result();
					if ($sqlResult!=FALSE) {
					
						$resultSet = $sqlResult->fetch_array();
						$token = $resultSet["token"];
					}

				} 
				else {
					$status = "prepared statement error: " . $sql->errno;
				}

				$ps->close();
			}
			else {
				$status = "prepared statement error: " . $sql->errno;
			}
		}
		else {
			$status = "unable to connect to database: " . $sql->connect_errno;
		}
		// done..
		$sql->close();
	}

	#echo $token;
	if ($token != NULL) {
		header('Location: http://localhost:8080/bin/client.html?token='.$token);
	}

?>
