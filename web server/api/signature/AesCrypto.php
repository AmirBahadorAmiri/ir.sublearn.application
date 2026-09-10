<?php

class AesCrypto
{
    // رمزنگاری متن
    public static function encrypt($plaintext, $password)
    {
        $key = self::generateKey($password);

        // در Java از Cipher.getInstance("AES") استفاده شده
        // که برابر با AES/ECB/PKCS5Padding است.
        $cipher = "AES-128-ECB";

        $encrypted = openssl_encrypt($plaintext, $cipher, $key, OPENSSL_RAW_DATA);
        return base64_encode($encrypted);
    }

    // رمزگشایی متن
    public static function decrypt($encryptedText, $password)
    {
        $key = self::generateKey($password);
        $cipher = "AES-128-ECB";

        $decoded = base64_decode($encryptedText);
        $decrypted = openssl_decrypt($decoded, $cipher, $key, OPENSSL_RAW_DATA);
        return $decrypted;
    }

    // تولید کلید ۱۶ بایتی از پسورد
    private static function generateKey($password)
    {
        // هم‌رفتار با نسخه جاوا: کوتاه کردن یا صفرپر کردن
        $key = str_pad(substr($password, 0, 16), 16, "\0");
        return $key;
    }
}

?>