package org.yidan.coco.settings;

/**
 * Created by kongxiangxin on 2017/8/1.
 */
public class JdbcProperty {
    private String driver;
    private String url;
    private String username;
    private String password;

    public JdbcProperty(String driver, String url, String username, String password) {
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public JdbcProperty() {
    }

    public String getDriver() {
        return driver;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
