package com.example.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class Saving {

    public static final String DEFAULT_CONFIGURE = "";

    /**
     * @param 待加密的字符串
     * @return 加密字符串
     **/
    public static String getMD5Hash(String input) {
        try {
            // 获取 MD5 消息摘要实例
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 将输入字符串转换为字节数组并计算哈希值
            byte[] messageDigest = md.digest(input.getBytes());

            // 将字节数组转换为十六进制格式的字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("无法找到 MD5 算法", e);
        }
    }

    private List<Level> levels;

    private String user;

    private String password;

    public Saving(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public Saving(Saving o) {
        this.password = o.password;
        this.user = o.user;
    }

    public boolean equals(Saving o) {
        return (user == null ? o.user == null : user.equals(o.user))
                && (password == null ? o.password == null : password.equals(o.password));
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static String getDefaultConfigure() {
        return DEFAULT_CONFIGURE;
    }

    public List<Level> getLevels() {
        return levels;
    }

    public void setLevels(List<Level> levels) {
        this.levels = levels;
    }

    
}
