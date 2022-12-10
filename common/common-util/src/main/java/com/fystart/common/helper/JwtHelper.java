package com.fystart.common.helper;

import io.jsonwebtoken.*;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * 生成JSON Web令牌的工具类
 *
 * @author fy
 * @date 2022/12/10 19:14
 */
public class JwtHelper {

    /**
     * token过期时间
     */
    private static long tokenExpiration = 365 * 24 * 60 * 60 * 1000;
    /**
     * 加密密钥
     */
    private static String tokenSignKey = "123456";

    /**
     * 根据用户id和用户名称生成token字符串
     *
     * @param userId
     * @param username
     * @return
     */
    public static String createToken(String userId, String username) {
        String token = Jwts.builder()
                .setSubject("AUTH-USER")

                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))

                .claim("userId", userId)
                .claim("username", username)

                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
                .compressWith(CompressionCodecs.GZIP)
                .compact();
        return token;
    }

    /**
     * 从token字符串中取出用户id
     *
     * @param token
     * @return
     */
    public static String getUserId(String token) {
        try {
            if (StringUtils.isEmpty(token)) {
                return null;
            }

            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            String userId = (String) claims.get("userId");
            return userId;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从token字符串中取出用户名称
     *
     * @param token
     * @return
     */
    public static String getUsername(String token) {
        try {
            if (StringUtils.isEmpty(token)) {
                return "";
            }

            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            return (String) claims.get("username");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * jwt token无需删除，客户端扔掉即可。
     *
     * @param token
     */
    public static void removeToken(String token) {

    }

    public static void main(String[] args) {
        String admin = JwtHelper.createToken("1", "admin");
        System.out.println(admin);
    }
}
