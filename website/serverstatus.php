<?PHP

	require("../apnetwork/dbconfig.php");

	$filename =  "../apnetwork/status.tmp";
	$queryTime = filemtime($filename);

	if (time() > $queryTime + 300) { // every 5 minutes allow the database to be queried

		$sql = new mysqli($sqlAddress, $sqlUsername, $sqlPassword, $sqlDatabase, $sqlPort);
		if (!$sql->connect_errno) {
			$ps = $sql->prepare("SELECT * FROM serverstatus WHERE pingDate BETWEEN DATE_SUB(NOW(), INTERVAL 10 MINUTE) AND NOW();");
			if ($ps) {
				if ($ps->execute()) {

					$sqlResult = $ps->fetch();
					if($sqlResult==TRUE) {
						$status = "{\"message\":\"Online\", \"result\":0}";
					}
					else {
						$status = "{\"message\":\"Offline\", \"result\":1}";
					}

				}
				else {
					$status = "{\"message\":\"Error 1\", \"result\":2}";
				}
				$ps->close();
			}
			else {
				$status = "{\"message\":\"Error 2\", \"result\":3}"; // prepared badly, tables probably different 
			}
		}
		else {
			$status = "{\"message\":\"Error 3\", \"result\":4}"; // cannot connect to database
		}
		$sql->close();

		// update the file time
		$file = fopen($filename, "w");
		fwrite($file, $status);
		fclose($file);
	}

	else {

		$len = filesize($filename);
		$status = "{\"message\":\"Error 3\", \"result\":4}";
		if ($len > 0) {
			$file = fopen($filename, "r");
			$status = fread($file, $len); // get the freakin' status
			fclose($file);
		}

	}

	header('Content-Type: application/json');
	echo $status;

?>