<?PHP

	require("../apnetwork/dbconfig.php");
	// credits to https://stackoverflow.com/a/46181
	// ported to php by me
	function validateEmail($email) {
		$re = '/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/';
		return preg_match($re, strtolower($email));
	}
	function myrandom_bytes($len) {
		$r = "";
		$i = 0;
		while($i < $len) {
			$r .= rand(0, 255);
			$i++;
		}
		return $r;
	}

	$username = $_POST["username"];
	$password = $_POST["password"];
	$email = $_POST["email"];

	if (!validateEmail($email) || strlen($username) <= 3 || strlen($password) <= 0 ) {
		$status = "Error creating account, please revise your username, password and/or email and try again.";
	}
	else {

		$sql = new mysqli($sqlAddress, $sqlUsername, $sqlPassword, $sqlDatabase, $sqlPort);

		$sqlResult 		= null;

		$status		 	= "";
		

		$ps = $sql->prepare("SELECT * FROM useraccounts WHERE email = ? or username = ?;");

		if ($ps) {

			$ps->bind_param("ss", $email, $username);

			if ($ps->execute()) {

				$sqlResult = $ps->fetch();
				//var_dump($sqlResult);
				if($sqlResult==FALSE) {
					$status = "username is and email are not in use!";

					$newSalt = base64_encode(myrandom_bytes(8));
					$newHash = hash("sha256", $password . $newSalt, true);

					$ps = $sql->prepare("INSERT INTO useraccounts VALUES(NULL, NULL, ?, ?, ?, ?, NOW(), 0, 0);");
					$ps->bind_param("ssss", $username, $newHash, $newSalt, $email);
					if($ps->execute()) {
						$status = "inserted new account successfully";
					} else {
						$status = "failed to create new account";
					}
				}
				else {
					$status = "username or email is in use";
				}
			}
			else {
				$status = "sql request failed";
			}

			$ps->close();
		}

		// done..
		$sql->close();

	}

?>

<!DOCTYPE html>
<html>
	
	<head>
		
		<style>
			body {
				background-color: rgb(200, 200, 200);
				margin: 10% 25% 0 25%;
				color: black;
			}
			.hidden {
				display: none;
			}
			.container {
				background-color: #fff;
				border: 1px solid #000;
				padding: 5px;
			}
			.nav-top {
				background-color: #f0f0f0;
				padding: 5px;
				border: 1px solid #000;
				border-bottom: 0px;

			}
			.nav-bottom {
				background-color: #f0f0f0;
				padding: 5px;
				border: 1px solid #000;
				border-top: 0px;
			}
			.bound-1 {
				margin: 0 auto;
				width: 300px;
			}
			.prettify {
				margin: 5px;
				border: 1px solid #000;
			}
			.padding-1 {
				padding: 5px;
				margin: 5px;
			}
			.login {
				float: right;
			}
			.title-inner {
				width: auto;
				background-color: #a0a0f0;
				color: #fff;
				height: 25px;
				text-align: center;
				padding:5px;
			}
			.create {
				width: 300px;
			}
			.reset {
				width: 300px;
			}
			.clearfix {
				clear: both;
			}
			.display-right {
				float: right;
			}
			.display-left {
				float: left;
			}
			a {
				color: black;
			}
			a:link {
				color: black;
			}
			a:visited {
				color: black;
			}
			a:hover {
				color: black;
			}
			a:active {
				color: black;
			}
			

		</style>

		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>

		<script type="text/javascript">

			// credits to https://stackoverflow.com/a/46181
			function validateEmail(email) {
				var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
				return re.test(String(email).toLowerCase());
			}

			/*jQuery(document.body).ready(function() {

				jQuery("#thing").click(function() {

					var f = "<form id='hidden-form' class='hidden' action='/butts/' method='POST'>";
					f += 	"<input type='text'  name='username' value='" + username + "'/>";
					f += 	"</form>";
					jQuery('body').append(f);
					jQuery("#hidden-form").submit();

				});

			});*/
		</script>

	</head>

	<body>
		<div class="nav-top">
			<span class="title">Account creation</span>
			<div class="clearfix"></div>
		</div>
		<div class="container">
			<h1><?PHP echo $status; ?></h2>
		</div>
		<div class="nav-bottom"></div>

	</body>

</html>