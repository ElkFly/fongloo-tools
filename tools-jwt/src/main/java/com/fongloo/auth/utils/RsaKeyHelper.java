package com.fongloo.auth.utils;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RsaKey 帮助类
 * 此类用于从指定目录中解析公钥和私钥 并得到PublicKey与PrivateKey对象
 */
public class RsaKeyHelper {

    /**
     * 获取公钥，用于解析Token
     *
     * @param fileName
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public PublicKey getPublicKey(String fileName) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        InputStream pubKeyStream = RsaKeyHelper.class.getClassLoader().getResourceAsStream(fileName);

        try (DataInputStream dis = new DataInputStream(pubKeyStream)) {
            byte[] bytes = new byte[pubKeyStream.available()];
            dis.readFully(bytes);

            X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
        }

    }

    /**
     * 获取秘钥 用于生成Token
     *
     * @param fileName
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public PrivateKey getPrivateKey(String fileName) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        InputStream priKeyStream = RsaKeyHelper.class.getClassLoader().getResourceAsStream(fileName);

        try (DataInputStream dis = new DataInputStream(priKeyStream)) {
            byte[] bytes = new byte[priKeyStream.available()];
            dis.readFully(bytes);

            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(bytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(spec);
        }
    }

}
