<?php
	class UserAccountModel {
		public $id = NULL;
		public $username = NULL;
		public $combinedHash = NULL;
		public $salt = NULL;
		public $email = NULL;
		public $permission = NULL;
		public $locked = NULL;
		public $dateOfCreation = NULL;
		public $lastLoginDate = NULL;
		public $sessionIP = NULL;
		public $sessionExpirationDate = NULL;
		public $sessionToken = NULL;

		/*
			@param : $resultSet = $sql->get_result()->fetch_array()
		*/
		function __construct($resultSet) {
			if (array_key_exists("id", $resultSet)) {
				$this->$id = $resultSet["id"];
			};
			if (array_key_exists("username", $resultSet)) {
				$this->$username = $resultSet["username"];
			};
			if (array_key_exists("combinedHash", $resultSet)) {
				$this->$combinedHash = $resultSet["combinedHash"];
			};
			if (array_key_exists("salt", $resultSet)) {
				$this->salt = $resultSet["salt"];
			};
			if (array_key_exists("email", $resultSet)) {
				$this->$email = $resultSet["email"];
			};
			if (array_key_exists("permission", $resultSet)) {
				$this->$permission = $resultSet["permission"];
			};
			if (array_key_exists("locked", $resultSet)) {
				$this->$locked = $resultSet["locked"];
			};
			if (array_key_exists("dateOfCreation", $resultSet)) {
				$this->$dateOfCreation = $resultSet["dateOfCreation"];
			};
			if (array_key_exists("lastLoginDate", $resultSet)) {
				$this->$lastLoginDate = $resultSet["lastLoginDate"];
			};
			if (array_key_exists("sessionIP", $resultSet)) {
				$this->$sessionIP = $resultSet["sessionIP"];
			};
			if (array_key_exists("sessionExpirationDate", $resultSet)) {
				$this->$sessionExpirationDate = $resultSet["sessionExpirationDate"];
			};
			if (array_key_exists("sessionToken", $resultSet)) {
				$this->$sessionToken = $resultSet["sessionToken"];
			};
		}
	}
?>