import cn.hutool.core.io.FileUtil;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;

/**
 * 公钥私钥生成
 */
public class RsaKeyHelperTest {
    public static void main(String[] args) throws Exception {
        // 自定义随机密码
        String password = "~!@#$%^&*()_+<>?";

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        SecureRandom secureRandom = new SecureRandom(password.getBytes());
        keyPairGenerator.initialize(1024, secureRandom);
        KeyPair keyPair = keyPairGenerator.genKeyPair();

        byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
        byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();

        FileUtil.writeBytes(publicKeyBytes,"src/main/resources/pub.key");
        FileUtil.writeBytes(privateKeyBytes,"src/main/resources/pri.key");
    }
}
