<?php
//<!-- a class for Handle SQL requests -->

class SQLHelper {

    private $sql;

//    private $hostname = "server2.mizbaniweb.com";
        private $hostname = "localhost";
    private $username = "amirbaha_defineIt_admin";
    private $password = "MThZPdhXSS7q3XL";
    private $database_name = "amirbaha_sublearn_main_database";

    private $result = "";

    public function __construct()
    {
        $this->sql = new mysqli($this->getHostname(), $this->getUsername(), $this->getPassword(), $this->getDatabaseName());
        if ($this->getSql()->connect_error) {
            die("Connection failed: " . $this->getSql()->connect_error);
        }
    }

    public function sendQuery($query)
    {
        $r = $this->getSql()->query($query);
        $this->setResult($r);
    }

    public function setResult($result)
    {
        $this->result = $result;
    }

    public function getResult()
    {
        return $this->result;
    }

    public function getDatabaseName()
    {
        return $this->database_name;
    }

    public function getHostname()
    {
        return $this->hostname;
    }

    public function getPassword()
    {
        return $this->password;
    }

    public function getSql()
    {
        return $this->sql;
    }

    public function getUsername()
    {
        return $this->username;
    }

    public function closeConnection() {
        $this->getSql()->close();
    }

}
