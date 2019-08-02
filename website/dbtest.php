<?php 
	require("../apnetwork/dbconfig.php");

	$sql = new mysqli($sqlAddress, $sqlUsername, $sqlPassword, $sqlDatabase, $sqlPort);
	if (!$sql->connect_errno) {
		$ps = $sql->prepare("SELECT MakeHash(?, ?);");
		if ($ps) {
			$stupid = "test";
			$shit = "fuck";
			$ps->bind_param("ss", $stupid="", $shit="");

			if ($ps->execute()) {


				$sqlResult = $ps->get_result();
				if ($sqlResult != FALSE) {
					$fetch = $sqlResult->fetch_array();
					var_dump($fetch);
					print_r($fetch);
				}
				var_dump($sqlResult);
				print_r($sqlResult);

			}
			else {
				echo "execute failed";
			}
			$ps->close();
		}
		else {
			echo "prepare failed";
		}
	}
	else {
		echo "connect failed";
	}
?>