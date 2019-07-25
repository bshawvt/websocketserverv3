<?PHP 
	

	$token = $_GET['token'];
	$email = $_GET['email'];

?>

<!DOCTYPE html>
<html>

	<head>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
		<script type="text/javascript" src="/3rd/sjcl.js"></script>

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

		<script type="text/javascript">

			jQuery(document.body).ready(function() {

				var token = '<?PHP echo $token; ?>';
				var email = '<?PHP echo $email; ?>';

				jQuery("#submit").click(function() {

					var pass1 = document.getElementById("password1").value;
					var pass2 = document.getElementById("password2").value;

					if (pass1 === pass2 && pass1.length > 8) {

						var crypt = new sjcl.hash.sha256();
						crypt.update(pass1);
						pass1 = sjcl.codec.base64.fromBits(crypt.finalize());

						var f = "<form id='hidden-form' class='hidden' action='/reset/' method='POST'>";
						f += 	"<input type='text'  name='new_password' value='" + pass1 + "'/>";
						f += 	"<input type='text'  name='token' value='" + token + "'/>";
						f += 	"<input type='text'  name='email' value='" + email + "'/>";
						f += 	"</form>";
						jQuery('body').append(f);
						jQuery("#hidden-form").submit();

					}
					else {
						alert("Password is invalid");
					}
					/*var user = document.getElementById("useraccounts").value;

					if (user.length == 0) return;

					var f = "<form id='hidden-form' class='hidden' action='admin.php' method='POST'>";
					f += 	"<input type='text'  name='useraccount' value='" + user + "'/>";
					f += 	"</form>";
					jQuery('body').append(f);
					jQuery("#hidden-form").submit();*/

				});
			});

		</script>
	</head>
	

	<body>
		<div class="nav-top">
			<span class="title">Please enter a new password.</span>
			<div class="clearfix"></div>
		</div>
		<div class="container">
			<input id="password1" type="password" placeholder="New password" name="newpassword1"/><br/>
			<input id="password2" type="password" placeholder="Confirm password" name="newpassword2"/><br/>
			<button id="submit">Send</button>
		</div>
		<div class="nav-bottom"></div>

	</body>

</html>