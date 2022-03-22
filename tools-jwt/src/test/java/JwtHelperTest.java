import com.fongloo.auth.utils.JwtHelper;
import com.fongloo.auth.utils.JwtUserInfo;
import com.fongloo.auth.utils.Token;

/**
 * JwtToken 生成与解析 测试
 */
public class JwtHelperTest {
    /**
     * 验证自己生成的 公钥私钥能否 成功生成token 解析token
     *
     * @param args
     */
    public static void main(String[] args) {
        JwtUserInfo jwtUserInfo = new JwtUserInfo(1L, "fongloo", "feng", 1L, 1L);
        int expire = 7200;

        //生成Token  注意： 确保该模块 src/main/resources/pub.key 目录下已经有了私钥
        Token token = JwtHelper.generateUserToken(jwtUserInfo, "src/main/resources/pri.key", expire);
        System.out.println(token);

        //解析Token  注意： 确保该模块 src/main/resources/pub.key 目录下已经有了公钥
        JwtUserInfo jwtFromToken = JwtHelper.getJwtFromToken(token.getToken(), "src/main/resources/pub.key");
        System.out.println(jwtFromToken);
    }
}
