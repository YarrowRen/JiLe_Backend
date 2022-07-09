package cn.ywrby.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;

/**
 * jwt工具类
 */
public class JwtUtils {
    public static final String TOKEN_LOGIN_NAME="loginName";
    public static final String TOKEN_LOGIN_ID="userId";
    public static final String TOKEN_SUCCESS="success: ";
    public static final String TOKEN_FAIL="fail: ";

    //过期时间
    private static final long EXPIRE_TIME=24*60*60*1000;

    //token私钥
    private static final String TOKEN_SECRET="ywrby0214";

    //生成签名，十五分钟后过期
    public static String sign(String username,String userId){
        //过期时间
        Date date=new Date(System.currentTimeMillis()+EXPIRE_TIME);
        //私钥以及加密算法
        Algorithm algorithm=Algorithm.HMAC256(TOKEN_SECRET);
        //设置头信息
        HashMap<String,Object> header=new HashMap<>(2);
        header.put("typ","JWT");
        header.put("alg","HS256");
        //附带username和ID生成签名
        return JWT.create().withHeader(header).withClaim(TOKEN_LOGIN_NAME,username)
                .withClaim(TOKEN_LOGIN_ID,userId).withExpiresAt(date).sign(algorithm);
    }

    public static String verify(String token){
        String result=TOKEN_SUCCESS;
        try {
            Algorithm algorithm=Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier=JWT.require(algorithm).build();
            DecodedJWT jwt=verifier.verify(token);
            result+=jwt.getClaims().get(TOKEN_LOGIN_NAME).asString();
            return result;
        }catch (IllegalArgumentException e){
            return TOKEN_FAIL+e.getMessage();
        }catch (JWTVerificationException e){
            return TOKEN_FAIL+e.getMessage();
        }catch (Exception e){
            return TOKEN_FAIL+e.getMessage();
        }
    }
}
