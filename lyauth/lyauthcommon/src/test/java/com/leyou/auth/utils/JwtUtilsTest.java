package com.leyou.auth.utils;

import com.leyou.auth.entity.UserInfo;

import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;


/**
 * @author: oyyb
 * @data: 2020-04-10
 * @version: 1.0.0
 * @descript:
 */
public class JwtUtilsTest {

    private static final String pubKeyPath = "D:/heima/rsa/rsa.pub";

    private static final String priKeyPath = "D:/heima/rsa/rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    @Test
    public void testRsa() throws Exception {
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "234");
    }

    @Before
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    @Test
    public void testGenerateToken() throws Exception {
        // 生成token
        String token = JwtUtils.generateToken(new UserInfo(20L, "jack"), privateKey, 5);
        System.out.println("token = " + token);
    }

    @Test
    public void testParseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJuYW1lIjoiamFjayIsImV4cCI6MTU4NjUzMTE0MH0.hlWEWKag7_AWA7CTXmAtC85lWcKA7U1N1He7EHQB53dAzJeTJPMNj9AOH5LVOLmeoezWTamNigky5um3x8CYDZTPDoRMvCiy2T8Uj2em2_Fa67olMGRKkA4m48fTVXWXRpaJuZje5OZdU4imxrzRXu3R9h7CQEscwe-wmM6cvjI";

        // 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }
}