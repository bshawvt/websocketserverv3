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

	$token = $_POST["token"];
	$email = $_POST["email"];

	$newPassword = $_POST["new_password"];

	//echo "$token $email $newPassword";

	if (!validateEmail($email)) {
		$status = "Invalid email address.";
	}
	else {
		$sql = new mysqli($sqlAddress, $sqlUsername, $sqlPassword, $sqlDatabase, $sqlPort);

		if (!$sql->connect_errno) {	

			// find a valid recovery row matching the email and token
			$ps = $sql->prepare("SELECT * FROM useraccountrecovery WHERE email = ? AND token = ? AND active = 1 AND creationDate BETWEEN DATE_SUB(NOW(), INTERVAL 2 HOUR) AND NOW();");
			
			if ($ps) {
				
				$ps->bind_param("ss", $email, $token);


				if ($ps->execute()) {

					if ($ps->fetch() != NULL) { // only continue if the fetch produces any results
						$ps->close();
						// now invalidate this recovery row
						$ps2 = $sql->prepare("UPDATE useraccountrecovery SET active = 0 WHERE email = ? AND token = ?;");
						

						if ($ps2) {
							$ps2->bind_param("ss", $email, $token);
							$ps2->execute();
							$ps2->close();
						}

						$newSalt = base64_encode(myrandom_bytes(8));
						$newHash = hash("sha256", $newPassword . $newSalt, true); // straight from morroco
						
						$ps3 = $sql->prepare("UPDATE useraccounts SET hash = ?, salt = ? WHERE email = ?;");
						if ($ps3) {
							$ps3->bind_param("sss", $newHash, $newSalt, $email);

							if ($ps3->execute()) {
								$status = "Your password has been changed.";
							}
							$ps3->close();
						}
						else {
							$status = "Failed to change password.";
						}

					}
					else {
						$status = "No matching recovery profile.";
					}

				}
			}

		}

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
			.login {
				float: right;
			}
			.clearfix {
				clear: both;
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

		<script type="text/javascript" src="/3rd/sjcl.js"></script>

		<script type="text/javascript">

			jQuery(document.body).ready(function() {

				/*jQuery("#passwordreset").click(function() {

					var email = document.getElementById("esemail").value;
					var f = "<form id='hidden-form' class='hidden' action='recovery.php' method='POST'>";
					f += 	"<input type='text'  name='email' value='" + email + "'/>";
					f += 	"</form>";
					jQuery('body').append(f);
					jQuery("#hidden-form").submit();

				});*/
				function redirect() {
					document.location = "/index/";
				}
				setTimeout(redirect, 5000);

			});
		</script>

	</head>

	<body>
		<div class="nav-top">
			<span class="title">Password reset</span>
			<div class="clearfix"></div>
		</div>
		<div class="container">
			<?PHP echo $status; ?>
			<br/>
			<span>Redirecting to index in 5 seconds...</span>
		</div>
		<div class="nav-bottom"></div>

	</body>

</html>