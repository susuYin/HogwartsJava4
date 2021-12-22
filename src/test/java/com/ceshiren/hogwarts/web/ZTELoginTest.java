package com.ceshiren.hogwarts.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class ZTELoginTest {
    WebDriver driver=new ChromeDriver();
    @Test
    void needlogin() throws IOException {

        driver.get("http://10.10.0.97:8080/uke-admin/");
        WebDriverWait webDriverWait=new WebDriverWait(driver,5);
        WebElement loginElement=driver.findElement(By.id("loginName"));
        webDriverWait.until(ExpectedConditions.visibilityOf(loginElement));
        loginElement.sendKeys("73949");
        driver.findElement(By.id("password")).sendKeys("tch*2017");
        driver.findElement(By.id("loginButton")).click();
        Set<Cookie> cookies=driver.manage().getCookies();
        System.out.println(cookies);
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.writeValue(new File("ztecookie.yaml"),cookies);
    }
    @Test
    void searchTest() throws IOException {
        File cookieyaml=new File("ztecookie.yaml");
        if(cookieyaml.exists()){
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            driver.get("http://10.10.0.97:8080/uke-admin/");
            driver.manage().deleteAllCookies();

            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            TypeReference typeReference = new TypeReference<List<HashMap<String, Object>>>() {
            };

            List<HashMap<String, Object>> cookies = (List<HashMap<String, Object>>) mapper.readValue(cookieyaml, typeReference);
            System.out.println(cookies);

            cookies.forEach(cookieMap -> {
                driver.manage().addCookie(new Cookie(cookieMap.get("name").toString(), cookieMap.get("value").toString()));
            });


            driver.navigate().refresh();
            System.out.println(driver.manage().getCookies());

        }
    }
}
