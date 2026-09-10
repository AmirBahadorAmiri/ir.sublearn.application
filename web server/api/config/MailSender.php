<?php

use PHPMailer\PHPMailer\Exception;
use PHPMailer\PHPMailer\PHPMailer;

require '../../vendor/autoload.php';

class MailSender
{
    public $mail;
    private $username;
    private $password;
    private $emailTo;
    private $subject;
    private $body;

    public function __construct()
    {
        $this->mail = new PHPMailer(true);
    }

    public function sendEmail()
    {
        try {
            $this->mail->isSMTP();

//            465 => ssl
//            587 => tls

//            $this->mail->Host = 'smtp.gmail.com';
//            $this->mail->Username = "abadan918@gmail.com";
//            $this->mail->Port = 587;

            $this->mail->Host = 'mail.amirbahadoramiri.ir';
            $this->mail->Username = "defineit@amirbahadoramiri.ir";
            $this->mail->Port = 465;

            $this->mail->Password = "fnag vhwa uutz mvfh";
            $this->mail->SMTPAuth = true;
            $this->mail->SMTPSecure = 'ssl';
            $this->mail->isHTML(true);
            $this->mail->CharSet = 'utf-8';

            $this->mail->setFrom("defineit@amirbahadoramiri.ir", 'اپلیکیشن دیفاینیت');
//            $this->mail->addAddress($this->getEmailTo(), 'گیرنده');
            $this->mail->addAddress($this->getEmailTo());
            $this->mail->Subject = $this->getSubject();
            $this->mail->Body = $this->getBody();

            $this->mail->send();
//            echo 'ایمیل با موفقیت ارسال شد.';
        } catch (Exception $e) {
//            echo "خطا در ارسال ایمیل: {$this->mail->ErrorInfo}";
        }
    }

    public function getUsername()
    {
        return $this->username;
    }

    public function setUsername($username) : MailSender
    {
        $this->username = $username;
        return $this;
    }

    public function getPassword()
    {
        return $this->password;
    }

    public function setPassword($password) : MailSender
    {
        $this->password = $password;
        return $this;
    }


    public function getEmailTo()
    {
        return $this->emailTo;
    }

    public function setEmailTo($emailTo) : MailSender
    {
        $this->emailTo = $emailTo;
        return $this;
    }

    public function getSubject()
    {
        return $this->subject;
    }

    public function setSubject($subject) : MailSender
    {
        $this->subject = $subject;
        return $this;
    }

    public function getBody()
    {
        return $this->body;
    }

    public function setBody($body) : MailSender
    {
        $this->body = $body;
        return $this;
    }

}