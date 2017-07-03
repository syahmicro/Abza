<?php
@session_start();
date_default_timezone_set("Asia/Kuala_Lumpur");
class DB_CONNECT {
 
    function __construct() {
        $this->connect();
    }

    function __destruct() {
        $this->close();
    }
 
    function connect() {

		define('DB_USER', "id1532163_root"); // db user
		define('DB_PASSWORD', "12345678"); // db password (mention your db password here)
		define('DB_DATABASE', "id1532163_abzadb"); // database name
		define('DB_SERVER', "localhost"); // db server
 
        $con = mysql_connect(DB_SERVER, DB_USER, DB_PASSWORD) or die(mysql_error());
 
        $db = mysql_select_db(DB_DATABASE) or die(mysql_error()) or die(mysql_error());

        return $con;
    }
    function close() {
        mysql_close();
    }
}

$db = new DB_CONNECT();
?>