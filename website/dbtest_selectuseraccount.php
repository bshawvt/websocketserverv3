<?php 
	require("../apnetwork/dbconfig.php");

	$sql = new mysqli($sqlAddress, $sqlUsername, $sqlPassword, $sqlDatabase, $sqlPort);
	if (!$sql->connect_errno) {
		//$ps = $sql->prepare("SELECT * FROM wss_debug.useraccounts;");//
		//$ps = $sql->prepare("SELECT WSService_MakeHash(?, ?);");
		$ps = $sql->prepare("CALL WSProc_SelectUserAccountOnAuth(?, ?);");
		if ($ps) {
			$stupid = $_GET['a'];
			$shit = $_GET['b'];
			$ps->bind_param("ss", $stupid, $shit);

			if ($ps->execute()) {


				$sqlResult = $ps->get_result();
				if ($sqlResult != FALSE) {
					$fetch = $sqlResult->fetch_array();
					//var_dump($fetch);
					print_r($fetch);
				}
				//var_dump($sqlResult);
				echo "<br/>";
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