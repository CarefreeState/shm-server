package com.macaron.homeschool.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Created With Intellij IDEA
 * Description:
 * User: 马拉圈
 * Date: 2024-11-09
 * Time: 20:32
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class CenterController {

    @Value("${spring.loginPage}")
    private String loginPage;

    @GetMapping("/")
    public RedirectView rootHtml()  {
        return new RedirectView(loginPage);
    }
}
