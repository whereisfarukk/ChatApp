import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.StringReader;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Scanner;

public class SymmetricEn {
    //AES algo
    private static final String AES= "AES";
    //using block cipher (CBS)
    private static final String AES_CIP = "AES/CBC/PKCS5PADDING";
    private static Scanner msg;

    //Method to create secret key

    public static SecretKey createAESKey() throws Exception {
        SecureRandom secureRandom= new SecureRandom();

        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
        keyGenerator.init(256,secureRandom);
        SecretKey key = keyGenerator.generateKey();

        return key;
    }
    // Method to init vector with any value
    public static byte[] createInitialVector(){
        //used with encrypt
        byte[] InitialVector = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(InitialVector);

        return InitialVector;
    }

    public static byte[] do_AESEncryption(
            String plainText,
            SecretKey secretKey,byte[] InitialVector) throws Exception{
        Cipher cipher = Cipher.getInstance(AES_CIP); // block of cipher text

        IvParameterSpec ivParameterSpec = new IvParameterSpec(InitialVector);
        // init cipher block
        cipher.init(Cipher.ENCRYPT_MODE,secretKey,ivParameterSpec);

        return cipher.doFinal(plainText.getBytes());
    }
    //Method to perform Cipher text --> plain Text

    public static String do_AESDecryption(
            byte[] cipherText,
            SecretKey secretKey,
            byte[] InitialVector) throws Exception{
        Cipher cipher = Cipher.getInstance(AES_CIP);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(InitialVector);
        // Decrypt mode
        cipher.init(Cipher.DECRYPT_MODE,secretKey,ivParameterSpec);

        byte[] result = cipher.doFinal(cipherText);
        return new String(result);
    }

    public static void main(String[] args) throws Exception {
        SecretKey sk = createAESKey();
        System.out.println("-----Encrypt Mode-----");
        System.out.println("The symmetric Key"+sk);
        byte[] Initial_Vector = createInitialVector();
        System.out.println("Enter Msg:");
        Scanner msg = new Scanner(System.in);
        String plainText = msg.nextLine();

        // Encrypting the message
        // using the symmetric key
        byte[] cipherText = do_AESEncryption(plainText, sk, Initial_Vector);

        System.out.println("The ciphertext or " + "Encrypted Message is: " + cipherText);

        // Decrypting the encrypted
        // message
        System.out.println("\n");
        System.out.println("-*-Decrypt Mode-*-");
        String decryptedText = do_AESDecryption(cipherText, sk, Initial_Vector);

        System.out.println("Your original message is: " + decryptedText);
    }

}
