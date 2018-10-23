package www.xcd.com.mylibrary.utils.key;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;


/** */
/**
 * <p>
 * RSA公钥/私钥/签名工具包
 * </p>
 * <p>
 * 罗纳德·李维斯特（Ron [R]ivest）、阿迪·萨莫尔（Adi [S]hamir）和伦纳德·阿德曼（Leonard [A]dleman）
 * </p>
 * <p>
 * 字符串格式的密钥在未在特殊说明情况下都为BASE64编码格式<br/>
 * 由于非对称加密速度极其缓慢，一般文件不使用它来加密而是使用对称加密，<br/>
 * 非对称加密算法可以用来对对称加密的密钥加密，这样保证密钥的安全也就保证了数据的安全
 * </p>
 * 
 * @author IceWee
 * @date 2012-4-26
 * @version 1.0
 */
public class RSAUtils {
	private static String RSAPUBLICKeyStore = "/rsa/public.pem";
	private static String RSAPRIVATEKeyStore = "/rsa/private.pem";
	/** */
	/**
	 * 加密算法RSA
	 */
	public static final String KEY_ALGORITHM = "RSA";

	/** */
	/**
	 * 签名算法
	 */
	public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

	/** */
	/**
	 * 获取公钥的key
	 */
	private static final String PUBLIC_KEY = "RSAPublicKey";

	/** */
	/**
	 * 获取私钥的key
	 */
	private static final String PRIVATE_KEY = "RSAPrivateKey";

	/** */
	/**
	 * RSA最大加密明文大小
	 */
	private static final int MAX_ENCRYPT_BLOCK = 117;

	/** */
	/**
	 * RSA最大解密密文大小
	 */
	private static final int MAX_DECRYPT_BLOCK = 128;

	/** */
	/**
	 * <p>
	 * 生成密钥对(公钥和私钥)
	 * </p>
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> genKeyPair() throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		keyPairGen.initialize(1024);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		Map<String, Object> keyMap = new HashMap<String, Object>(2);
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;
	}

	/** */
	/**
	 * <p>
	 * 用私钥对信息生成数字签名
	 * </p>
	 * 
	 * @param data
	 *            已加密数据
	 * @param privateKey
	 *            私钥(BASE64编码)
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String sign(byte[] data, String privateKey) throws Exception {
		byte[] keyBytes = Base64Utils.decode(privateKey);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initSign(privateK);
		signature.update(data);
		return Base64Utils.encode(signature.sign());
	}

	/** */
	/**
	 * <p>
	 * 校验数字签名
	 * </p>
	 * 
	 * @param data
	 *            已加密数据
	 * @param publicKey
	 *            公钥(BASE64编码)
	 * @param sign
	 *            数字签名
	 * 
	 * @return
	 * @throws Exception
	 * 
	 */
	public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
		byte[] keyBytes = Base64Utils.decode(publicKey);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PublicKey publicK = keyFactory.generatePublic(keySpec);
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initVerify(publicK);
		signature.update(data);
		return signature.verify(Base64Utils.decode(sign));
	}

	/** */
	/**
	 * <P>
	 * 私钥解密
	 * </p>
	 * 
	 * @param encryptedData
	 *            已加密数据
	 * @param privateKey
	 *            私钥(BASE64编码)
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) throws Exception {
		byte[] keyBytes = Base64Utils.decode(privateKey);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, privateK);
		int inputLen = encryptedData.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段解密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
				cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();
		return decryptedData;
	}

	/** */
	/**
	 * <p>
	 * 公钥解密
	 * </p>
	 * 
	 * @param encryptedData
	 *            已加密数据
	 * @param publicKey
	 *            公钥(BASE64编码)
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey) throws Exception {
		byte[] keyBytes = Base64Utils.decode(publicKey);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicK = keyFactory.generatePublic(x509KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicK);
		int inputLen = encryptedData.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段解密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
				cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();
		return decryptedData;
	}

	/** */
	/**
	 * <p>
	 * 公钥加密
	 * </p>
	 * 
	 * @param data
	 *            源数据
	 * @param publicKey
	 *            公钥(BASE64编码)
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPublicKey(byte[] data, String publicKey)  {
		try {
			byte[] keyBytes = Base64Utils.decode(publicKey);
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			Key publicK = keyFactory.generatePublic(x509KeySpec);
			// 对数据加密
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, publicK);
			int inputLen = data.length;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int offSet = 0;
			byte[] cache;
			int i = 0;
			// 对数据分段加密
			while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
			byte[] encryptedData = out.toByteArray();
			out.close();
			return encryptedData;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/** */
	/**
	 * <p>
	 * 私钥加密
	 * </p>
	 * 
	 * @param data
	 *            源数据
	 * @param privateKey
	 *            私钥(BASE64编码)
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPrivateKey(byte[] data, String privateKey) throws Exception {
		byte[] keyBytes = Base64Utils.decode(privateKey);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateK);
		int inputLen = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段加密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
				cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(data, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_ENCRYPT_BLOCK;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();
		return encryptedData;
	}

	/** */
	/**
	 * <p>
	 * 获取私钥
	 * </p>
	 * 
	 * @param keyMap
	 *            密钥对
	 * @return
	 * @throws Exception
	 */
	public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
		Key key = (Key) keyMap.get(PRIVATE_KEY);
		return Base64Utils.encode(key.getEncoded());
	}

	/** */
	/**
	 * <p>
	 * 获取公钥
	 * </p>
	 * 
	 * @param keyMap
	 *            密钥对 xs
	 * @return
	 * @throws Exception
	 */
	public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
		Key key = (Key) keyMap.get(PUBLIC_KEY);
		return Base64Utils.encode(key.getEncoded());
	}

	public static String getPrivateKey() throws Exception {
		/*URL url = RSAUtils.class.getResource(RSAPRIVATEKeyStore);
		BufferedReader br = new BufferedReader(new FileReader(url.getPath()));
		String s = br.readLine();
		StringBuffer publickey = new StringBuffer();
		s = br.readLine();
		while (s.charAt(0) != '-') {
			publickey.append(s + "\r");
			s = br.readLine();
		}
		return publickey.toString();*/
		return "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAK+IuHpKFh6P0qHAVMxaAqssUPKo7Hi0tCB2yT+EhTKIXrm9fOAtF1w07jO5rChjBqXSARS2uYrGgtADyQBcQ8yLzQMn3ds0XXROnsl7Cp//yg1zebFT04pXyA9ho69cruLSog2JMz6zDlQk2QfiunHHjbivKSj7WcyKiyWNwQs9AgMBAAECgYA529UC6hy0Yi/JhT3uRdUPWwIrmian+thMwxq+xw2ohG7ExoK82eATUpcZkZ//h/SO/9O/YiNybNB9hGHirobh/4jzkefXanbujRlYCUdE+GFik83Z0BNNG5K+8ZLoIq+Pyh9Ey0zgLU4OfkPhsmr5cJjkh6nKWQ+eaiNPJzlcOQJBANZ21YPyhI7oMLXf/YRcQhzS4fQ+qxB48myJFcqf3ibZ1CXXLJFVsFSsnX0C6OX0kUJsl10u5+Jdvda/hpSHRDcCQQDRh7mce+bp7XdQIAfThCzUH8hvLfgvbnD3jH8Ywj0Up1rtLUzf3BfDobvRmBIpTNXJz/yNduyRY389bu7LPRorAkEAwwT+teSzi+wyV0DfHRxz9hgB5oOSNQF2mSGzUjU74yJT2zEm0uYT2Xu6HnKl7G4QJVHv9olvzZWmyoARVkEG0wJAPrVeKaUzYMN/N+2+KeGfAQVaGVwqsRdem9fimehYG3xSzDUIkt7uBdC7w2i91rp2KWYj8bqr5n9er2Yp9Y2d7QJAL821v8dzfWE7HxnXHqO41Dr9Gr2yNiPf66mHcez1Na0STUcY5WCTG9aP9MyBdUxThlNyD6EsDRk5v9v8gbUKQw==";

	}

	public static String getPublicKey() {
		/*URL url = RSAUtils.class.getResource(RSAPUBLICKeyStore);
		BufferedReader br = new BufferedReader(new FileReader(url.getPath()));
		String s = br.readLine();
		StringBuffer publickey = new StringBuffer();
		s = br.readLine();
		while (s.charAt(0) != '-') {
			publickey.append(s + "\r");
			s = br.readLine();
		}
		return publickey.toString();*/
		return "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCviLh6ShYej9KhwFTMWgKrLFDyqOx4tLQgdsk/hIUyiF65vXzgLRdcNO4zuawoYwal0gEUtrmKxoLQA8kAXEPMi80DJ93bNF10Tp7Jewqf/8oNc3mxU9OKV8gPYaOvXK7i0qINiTM+sw5UJNkH4rpxx424ryko+1nMiosljcELPQIDAQAB";
	}

	public static String decryptByPrivateKey(String body) {
		byte[] en_result = Base64_.decode(body);
		byte[] de_test;
		try {
			de_test = decryptByPrivateKey(en_result, getPrivateKey());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}
		return new String(de_test);
	}
	
	public static void main(String[] args) throws Exception {


//		byte[] en_test = encrypt(rsap, test.getBytes("utf-8"));
//		System.out.println(bytesToHex(en_test));
		// byte[] en_result =
		// "1bcd6ef22c1873b79dbc4434eb5989477b980846de7f81a13b67d31d50a802510045713ae05dddbf7e29bebce219cc7c5bf781bc0ce049bfb01a0693bad56609d25a6f6f3020ab496bba97ad126f2d81f9e86515441979e36460a93d19a5b8b1944457e28278d9e2b9c6f379824363744414a46524931d6ed01c354c69e7eada".getBytes("utf-8");
//		byte[] en_result = toBytes(
//				"682308478c0ccbda351e3f7bfb46c73f31d19279d0c05ac035ab564e1bdcdb4f6cac9a89e9a830642e4825f1eb4a74ffe1fd492e83a4dd3ff1cb8b3ef1f7f9029106f26a171769bfc4291cbc5f0fef230d712d92c1979067722f41b3cdd8d9beabf78129d83588d3bee3ff35810815f5d740e8c8c7fc0aa6a45db53d8c4ed219");
//		byte[] en_result =Base64.decodeBase64("i1bm2wkAye7a05lXPowmPA7wlEZ7iRfb2m1ajVkuuiWeiH+srM/JjIgErzniA0dNNEYNMo8G8z3h36dEvXKKqN13MlbKauQJyelHDDU3fK+xyA5PnUhNXhXSd/HSSA28wqFRajY1wRYDbqQgvwqGzV/PmTIx8x4TQMZMc3E5gTA=");
//		byte[] de_test = decrypt(rsapri, en_result);
		byte[] en_result = RSAUtils.encryptByPublicKey("{\"account\":18035541466,\"password\":123456,\"sign\":\"6ee5f97294cc226d8d348dd20e367bc8\"}".getBytes(), getPublicKey());
//		String en_result = "bqTZaVlUhlXckRm6D8MthmSDOp4wvwf5cKT1tLZsfSw8hI2rrtPjlkfClXg/Gamd+SH0R02eJUNTBth+2C0RJkRtKCG6pvlmpGiblALxWgOc3HmH7id9hY6aUE2zQEfoeJKVXB+v9OJxYcW6jkeAzCz2H6Mpk3GpHECEZN7/wmM=";
//		System.out.println(Arrays.toString(en_result));
//		String str =Base64.encodeBase64String(en_result);
//		System.out.println(str);
//		byte[] de_test = decryptByPrivateKey(str.getBytes("utf-8"), getPrivateKey());
		
//		System.out.println(RSAUtils.decryptByPrivateKey(str));
//		System.out.println(Base64.encodeBase64String(RSAUtils.encryptByPublicKey("2".getBytes(), getPublicKey())));
		
	}
}
