<?PHP

	require("../apnetwork/dbconfig.php");

	$filename =  "../apnetwork/projects/status";
	$queryTime = filemtime($filename);

	if (time() > $queryTime + 300) { // every 5 minutes query the database

		$sql = new mysqli($sqlAddress, $sqlUsername, $sqlPassword, $sqlDatabase, $sqlPort);
		if (!$sql->connect_errno) {
			$ps = $sql->prepare("SELECT * FROM serverstatus WHERE pingDate BETWEEN DATE_SUB(NOW(), INTERVAL 10 MINUTE) AND NOW();");
			if ($ps) {
				if ($ps->execute()) {

					$sqlResult = $ps->fetch();
					if($sqlResult==TRUE) {
						$status = "{\"status\":\"1\"}";
					}
					else {
						$status = "{\"status\":\"0\"}";
					}

				}
				else {
					$status = "{\"status\":\"-1\"}";
				}
				$ps->close();
			}
			else {
				$status = "{\"status\":\"-2\"}";
			}
		}
		else {
			$status = "{\"status\":\"-3\"}";
		}
		$sql->close();

		// update the file time
		$file = fopen($filename, "w");
		fwrite($file, $status);
		fclose($file);
	}

	else {

		$len = filesize($filename);
		$status = "{\"status\":\"-4\"}";
		if ($len > 0) {
			$file = fopen($filename, "r");
			$status = fread($file, $len); // get the freakin' status
			fclose($file);
		}

	}

	echo $status;

?>