package www.xcd.com.mylibrary.utils.key;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
* Created by Lix on 16/9/21.
*/
public class AESUtils {
   /*
    * 加密用的Key 可以用26个字母和数字组成 此处使用AES-128-CBC加密模式，key需要为16位。
    */
   private String sKey = "J93bNF10Tp7Jewqf";
   private String ivParameter = "EBAQUAA4GNA12345";
   private static AESUtils instance = null;

   private AESUtils() {

   }

   public static AESUtils getInstance() {
       if (instance == null)
           instance = new AESUtils();
       return instance;
   }

   public static String Encrypt(String encData ,String secretKey,String vector) throws Exception {

       if(secretKey == null) {
           return null;
       }
       if(secretKey.length() != 16) {
           return null;
       }
       Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
       byte[] raw = secretKey.getBytes();
       SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
       IvParameterSpec iv = new IvParameterSpec(vector.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
       cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
       byte[] encrypted = cipher.doFinal(encData.getBytes("utf-8"));
       return new String(Base64_.encode(encrypted));// 此处使用Base64_做转码。
   }


   // 加密
   public String encrypt(String sSrc) throws Exception {
       Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
       byte[] raw = sKey.getBytes();
       SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
       IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
       cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
       byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
       return new String(Base64_.encode(encrypted));// 此处使用Base64_做转码。
       }
   
   public String encrypt(byte[] sSrc) throws Exception {
       Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
       byte[] raw = sKey.getBytes();
       SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
       IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
       cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
       byte[] encrypted = cipher.doFinal(sSrc);
       return new String(Base64_.encode(encrypted));// 此处使用Base64_做转码。
       }

   // 解密
   public String decrypt(String sSrc) throws Exception {
       try {
           byte[] raw = sKey.getBytes("ASCII");
           SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
           Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
           IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
           cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
           byte[] encrypted1 = Base64_.decode(sSrc);// 先用Base64_解密
           byte[] original = cipher.doFinal(encrypted1);
           String originalString = new String(original, "utf-8");
           return originalString;
       } catch (Exception ex) {
           return null;
       }
   }

   public String decrypt(String sSrc,String key,String ivs) throws Exception {
       try {
           byte[] raw = key.getBytes("ASCII");
           SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
           Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
           IvParameterSpec iv = new IvParameterSpec(ivs.getBytes());
           cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
           byte[] encrypted1 = Base64_.decode(sSrc);// 先用Base64_解密
           byte[] original = cipher.doFinal(encrypted1);
           String originalString = new String(original, "utf-8");
           return originalString;
       } catch (Exception ex) {
           return null;
       }
   }

   public static String encodeBytes(byte[] bytes) {
       StringBuffer strBuf = new StringBuffer();

       for (int i = 0; i < bytes.length; i++) {
           strBuf.append((char) (((bytes[i] >> 4) & 0xF) + ((int) 'a')));
           strBuf.append((char) (((bytes[i]) & 0xF) + ((int) 'a')));
       }

       return strBuf.toString();
   }

   public static void main(String[] args) throws Exception {
       // 需要加密的字串
//       String cSrc = "{\"msg\":\"请重新登录\",\"code\":\"401\"}";
//
//       // 加密
//       long lStart = System.currentTimeMillis();
//       String enString = AESUtils.getInstance().encrypt(cSrc);
//       System.out.println("加密后的字串是：" + enString);
//
//       long lUseTime = System.currentTimeMillis() - lStart;
//       System.out.println("加密耗时：" + lUseTime + "毫秒");
//       // 解密
//       lStart = System.currentTimeMillis();
//       String DeString = AESUtils.getInstance().decrypt("nEWKJyXuqe2atRPnO4n/t0pLB3+pJSxcR0gbg+TV/ADuvfNhRdOvoQXTVW9SKt1i");
//       System.out.println("解密后的字串是：" + DeString);
//       lUseTime = System.currentTimeMillis() - lStart;
//       System.out.println("解密耗时：" + lUseTime + "毫秒");
   }
}
