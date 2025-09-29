package com.example.eventplatform.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            switch (statusCode) {
                case 404:
                    return "404";
                case 403:
                    model.addAttribute("errorMessage", "접근 권한이 없습니다.");
                    return "error";
                case 500:
                    model.addAttribute("errorMessage", "서버 내부 오류가 발생했습니다.");
                    return "error";
                default:
                    model.addAttribute("errorMessage", "오류가 발생했습니다. (코드: " + statusCode + ")");
                    return "error";
            }
        }

        model.addAttribute("errorMessage", "알 수 없는 오류가 발생했습니다.");
        return "error";
    }
}