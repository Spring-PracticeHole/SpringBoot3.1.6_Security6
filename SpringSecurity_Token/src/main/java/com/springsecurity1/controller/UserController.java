package com.springsecurity1.controller;

import com.springsecurity1.entity.Role;
import com.springsecurity1.entity.User;
import com.springsecurity1.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
@Log4j2
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 이용약관 페이지로 이동하는 메서드입니다.
     *
     * @param model 화면에 전달할 데이터를 담는 Model 객체
     * @return 이용약관 페이지 뷰의 경로
     */
    @GetMapping("/term")
    public String Term(Model model) {
        return "/user/term";
    }

    /**
     * 로그인 페이지로 이동하는 메서드입니다.
     *
     * @param model 화면에 전달할 데이터를 담는 Model 객체
     * @return 로그인 페이지 뷰의 경로
     */
    @GetMapping("/login")
    public String Login(Model model) {
        return "/user/login";
    }

    /**
     * 활성화 페이지로 이동하는 메서드입니다.
     *
     * @param model 화면에 전달할 데이터를 담는 Model 객체
     * @return 활성화 페이지 뷰의 경로
     */
    @GetMapping("/active")
    public String active(Model model) {
        return "/user/active";
    }

    /**
     * 사용자 상태를 확인하고 알림 페이지로 이동하는 메서드입니다.
     *
     * @param model     화면에 전달할 데이터를 담는 Model 객체
     * @param principal 현재 사용자 정보
     * @return 알림 페이지 뷰의 경로
     */
    @GetMapping("/status")
    public String status(Model model, Principal principal) {
        String id = principal.getName();
        int pass = userService.loginPro(id);

        if (pass == 1) {
            model.addAttribute("msg", "로그인 되었습니다.");
            model.addAttribute("url",  "/");
            return "/alert";
        } else if (pass == 2) {
            model.addAttribute("msg", "해당 게정은 휴면 계정입니다. 휴면을 풀어주세요.");
            model.addAttribute("url", "/active");
            return "/alert";
        } else if (pass == 3) {
            model.addAttribute("msg", "해당 계정은 탈퇴한 계정입니다.");
            model.addAttribute("url", "/");
            return "/alert";
        } else {
            model.addAttribute("msg", "못맞췄지로오오오오오오옹");
            model.addAttribute("url", "/");
            return "/alert";
        }
    }

    /**
     * 회원가입 폼으로 이동하는 메서드입니다.
     *
     * @param model 화면에 전달할 데이터를 담는 Model 객체
     * @return 회원가입 폼 뷰의 경로
     */
    @GetMapping("/join")
    public String JoinForm(Model model) {
        return "/user/join";
    }

    /**
     * 회원가입 처리를 수행하는 메서드입니다.
     *
     * @param model 화면에 전달할 데이터를 담는 Model 객체
     * @param user  가입할 사용자 정보
     * @return 홈 페이지로 리다이렉트
     */
    @PostMapping("/joinPro")
    public String Join(Model model, User user) {
        userService.userInsert(user);
        return "/index";
    }

    /**
     * 에러 페이지로 이동하는 메서드입니다.
     *
     * @param model 화면에 전달할 데이터를 담는 Model 객체
     * @return 홈 페이지 뷰의 경로
     */
    @GetMapping("/error")
    public String error(Model model) {
        return "/index";
    }

    /**
     * 내 정보 페이지로 이동하는 메서드입니다.
     *
     * @param model     화면에 전달할 데이터를 담는 Model 객체
     * @param principal 현재 사용자 정보
     * @return 내 정보 페이지 뷰의 경로
     */
    @GetMapping("/myPage")
    public String mypage(Model model, Principal principal) {
        User user = userService.getId(principal.getName());
        model.addAttribute("principal", principal);
        model.addAttribute("user", user);
        return "/user/myPage";
    }

    /**
     * 사용자 정보를 수정하고 홈 페이지로 리다이렉트하는 메서드입니다.
     *
     * @param principal 현재 사용자 정보
     * @param model     화면에 전달할 데이터를 담는 Model 객체
     * @param role      변경할 사용자 권한
     * @return 홈 페이지로 리다이렉트
     */
    @PostMapping("/out")
    public String out1(Principal principal, Model model, Role role) {
        User user = userService.getId(principal.getName());
        user.setRole(role);
        user.setAddr1("1234번지");
        userService.userUpdate(user);
        return "redirect:/";
    }
}
