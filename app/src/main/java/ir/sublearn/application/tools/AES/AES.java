package ir.sublearn.application.tools.AES;

import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AES {

    public static String password;

    public static String getPassword() {
        if ( password == null)
            generateUltraSecurePassword();
        return password;
    }

    public static void setPassword(String password) {
        AES.password = password;
    }

    public static void generateUltraSecurePassword() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[32]; // 256 بیت آنتروپی
        secureRandom.nextBytes(randomBytes);

        // Base64 URL-safe بدون padding
        String password = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);

        // 22 کاراکتر → ~132 بیت آنتروپی (کاملاً کافی و امن)
        setPassword(password.substring(0, 22));
    }

    // این متد برای رمزنگاری متن استفاده می‌شود.
    public static String encrypt(String plaintext, String password) throws Exception {
        // کلید رمزنگاری رو از پسورد می‌سازیم.
        SecretKey secretKey = generateKey(password);

        // یک نمونه Cipher می‌سازیم و اون رو برای رمزنگاری آماده می‌کنیم.
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        // متن رو به بایت تبدیل و رمزنگاری می‌کنیم.
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());

        // بایت‌های رمزنگاری شده رو به فرمت Base64 تبدیل می‌کنیم تا به صورت یک رشته قابل ذخیره و انتقال باشه.
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // این متد برای رمزگشایی متن استفاده می‌شود.
    public static String decrypt(String encryptedText, String password) throws Exception {
        // کلید رمزنگاری رو از پسورد می‌سازیم.
        SecretKey secretKey = generateKey(password);

        // یک نمونه Cipher می‌سازیم و اون رو برای رمزگشایی آماده می‌کنیم.
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        // متن رمزنگاری شده Base64 رو به بایت تبدیل و رمزگشایی می‌کنیم.
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));

        // بایت‌های رمزگشایی شده رو به یک رشته قابل خواندن تبدیل می‌کنیم.
        return new String(decryptedBytes);
    }

    // این متد یک کلید رمزنگاری از روی پسورد متنی می‌سازه.
    private static SecretKey generateKey(String password) {
        // پسورد باید حتما 16 بایت باشه برای استفاده در AES.
        // در این مثال ساده، پسورد رو کوتاه می‌کنیم یا با صفر پر می‌کنیم تا 16 بایت بشه.
        byte[] keyBytes = new byte[16];
        byte[] passwordBytes = password.getBytes();
        System.arraycopy(passwordBytes, 0, keyBytes, 0, Math.min(passwordBytes.length, 16));
        return new SecretKeySpec(keyBytes, "AES");
    }

}
