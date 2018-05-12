package com.demo.framework.security;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

/**
 * RSA 工具类。提供加密，解密，生成密钥对等方法。
 * 需要到http://www.bouncycastle.org下载bcprov-jdk14-123.jar。
 * 
 */
public class RSAUtil {

	/**
	 * 生成密钥对 
	 * @return KeyPair 
	 * @throws Exception
	 */
	public static KeyPair generateKeyPair() throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA",
				new org.bouncycastle.jce.provider.BouncyCastleProvider());
		final int KEY_SIZE = 1024;// 没什么好说的了，这个值关系到块加密的大小，可以更改，但是不要太大，否则效率会低
		keyPairGen.initialize(KEY_SIZE, new SecureRandom());
		KeyPair keyPair = keyPairGen.generateKeyPair();
		return keyPair;
	}

	public static KeyPair getKeyPair(String path) throws Exception {
		FileInputStream fis = new FileInputStream(path);
		ObjectInputStream oos = new ObjectInputStream(fis);
		KeyPair keyPair = (KeyPair) oos.readObject();
		oos.close();
		fis.close();
		return keyPair;
	}

	public static void saveKeyPair(KeyPair kp,String path) throws Exception {

		FileOutputStream fos = new FileOutputStream(path);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		// 生成密钥
		oos.writeObject(kp);
		oos.close();
		fos.close();
	}

	/**
	 * * 生成公钥 *
	 * 
	 * @param modulus
	 * @param publicExponent
	 * @return RSAPublicKey
	 * @throws Exception
	 */
	public static RSAPublicKey generateRSAPublicKey(byte[] modulus, byte[] publicExponent) throws Exception {
		KeyFactory keyFac = KeyFactory.getInstance("RSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
		RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger(modulus), new BigInteger(publicExponent));
		return (RSAPublicKey) keyFac.generatePublic(pubKeySpec);
	}

	/**
	 * 生成私钥
	 * 
	 * @param modulus
	 * @param privateExponent
	 * @return RSAPrivateKey
	 * @throws Exception
	 */
	public static RSAPrivateKey generateRSAPrivateKey(byte[] modulus, byte[] privateExponent) throws Exception {
		KeyFactory keyFac = KeyFactory.getInstance("RSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
		RSAPrivateKeySpec priKeySpec = new RSAPrivateKeySpec(new BigInteger(modulus), new BigInteger(privateExponent));
		return (RSAPrivateKey) keyFac.generatePrivate(priKeySpec);
	}

	/**
	 *  加密 
	 * @param key 加密的密钥 
	 * @param data 待加密的明文数据 
	 * @return 加密后的数据 
	 * @throws Exception
	 */
	public static byte[] encrypt(PublicKey key, byte[] data) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/NONE/NoPadding", new org.bouncycastle.jce.provider.BouncyCastleProvider());
		cipher.init(Cipher.ENCRYPT_MODE, key);
		int blockSize = cipher.getBlockSize();// 获得加密块大小，如：加密前数据为128个byte，而key_size=1024
		// 加密块大小为127
		// byte,加密后为128个byte;因此共有2个加密块，第一个127
		// byte第二个为1个byte
		int outputSize = cipher.getOutputSize(data.length);// 获得加密块加密后块大小
		int leavedSize = data.length % blockSize;
		int blocksSize = leavedSize != 0 ? data.length / blockSize + 1 : data.length / blockSize;
		byte[] raw = new byte[outputSize * blocksSize];
		int i = 0;
		while (data.length - i * blockSize > 0) {
			if (data.length - i * blockSize > blockSize)
				cipher.doFinal(data, i * blockSize, blockSize, raw, i * outputSize);
			else
				cipher.doFinal(data, i * blockSize, data.length - i * blockSize, raw, i * outputSize);
			// 这里面doUpdate方法不可用，查看源代码后发现每次doUpdate后并没有什么实际动作除了把byte[]放到
			// ByteArrayOutputStream中，而最后doFinal的时候才将所有的byte[]进行加密，可是到了此时加密块大小很可能已经超出了
			// OutputSize所以只好用dofinal方法。

			i++;
		}
		return raw;
	}

	/**
	 * 解密 
	 * @param key 解密的密钥 *
	 * @param raw  已经加密的数据 *
	 * @return 解密后的明文 *
	 * @throws Exception
	 */
	public static byte[] decrypt(PrivateKey key, byte[] raw) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/NONE/NoPadding", new org.bouncycastle.jce.provider.BouncyCastleProvider());
		cipher.init(Cipher.DECRYPT_MODE, key);
		int blockSize = cipher.getBlockSize();
		ByteArrayOutputStream bout = new ByteArrayOutputStream(64);
		int j = 0;

		while (raw.length - j * blockSize > 0) {
			bout.write(cipher.doFinal(raw, j * blockSize, blockSize));
			j++;
		}
		byte[] ret = bout.toByteArray();
		bout.close();
		return ret;
	}

    /**
     * 解密JS加密后的密文
     * @param str
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String decryptJSRsa(String str,PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/NONE/NoPadding",
                new BouncyCastleProvider());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        StringBuilder sb = new StringBuilder(new String(cipher.doFinal(Hex
                .decode(str))));
        return sb.reverse().toString();
    }
    /**
     * 解密JS加密后的密文
     * @param ciphertext
     * @param keyPath
     * @return
     * @throws Exception
     */
    public static String decryptJSRsa(String ciphertext,String keyPath )throws Exception {
        KeyPair key =getKeyPair(keyPath);
        Cipher cipher = Cipher.getInstance("RSA/NONE/NoPadding",
                new BouncyCastleProvider());
        cipher.init(Cipher.DECRYPT_MODE, key.getPrivate());
        StringBuilder sb = new StringBuilder(new String(cipher.doFinal(Hex
                .decode(ciphertext))));
        return sb.reverse().toString();
    }

	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String path="d:\\key.cer";
		//生成证书
		KeyPair keyPair = RSAUtil.generateKeyPair();
		RSAUtil.saveKeyPair(keyPair, path);
		System.out.println(keyPair.getPrivate());
		System.out.println(keyPair.getPublic());
		
//		KeyPair key =getKeyPair(path);
		
		
//		String test = "Ab123*";
//		byte[] en_test = encrypt(key.getPublic(), test.getBytes());
//		String ss = new BigInteger(en_test).toString(16);
//		System.out.println(ss);
//		byte[] de_test = decrypt(key.getPrivate(), new BigInteger(ss,16).toByteArray());
//		System.out.println(new String(de_test));
		
//		//JS密文进行解密
//		String result = "06dfb96ac77b2a49a7ae2c78570128cc7376400182a900067367531b3025a605c180460b589f704f4f5df49077509a01004ab29bd2123598b71be3a7cc393c4661357d0c00f925e6526b6663eea2cf3f9300b32dac18ee210ed8ce96e458526447729e3a8bf982f9dff51397dbab2679f642a9a6005361899c3a0aa758e0d484";
//	    System.out.println("还原密文："+decryptJSRsa(result,key.getPrivate()));
	}
	

}
