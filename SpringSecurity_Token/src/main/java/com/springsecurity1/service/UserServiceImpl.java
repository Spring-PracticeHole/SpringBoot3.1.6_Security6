package com.springsecurity1.service;

import com.springsecurity1.entity.Role;
import com.springsecurity1.entity.Status;
import com.springsecurity1.entity.User;
import com.springsecurity1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;


    /**
     * 사용자를 등록하고 암호화된 비밀번호, 역할, 상태를 설정하여 저장합니다.
     *
     * @param user 등록할 사용자
     * @return 저장된 사용자 정보
     */
    @Override
    public User userInsert(User user) {
        user.setPw(passwordEncoder.encode(user.getPw()));
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIVE);
        return userRepository.save(user);
    }

    /**
     * PasswordEncoder를 반환합니다.
     *
     * @return PasswordEncoder
     */
    @Override
    public PasswordEncoder passwordEncoder() {
        return this.passwordEncoder;
    }

    /**
     * 주어진 아이디로 사용자 정보를 조회합니다.
     *
     * @param id 조회할 사용자의 아이디
     * @return 조회된 사용자 정보
     */
    @Override
    public User getId(String id) {
        return userRepository.getId(id);
    }

    /**
     * 사용자 정보를 업데이트합니다.
     *
     * @param user 업데이트할 사용자 정보
     * @return 업데이트된 사용자 정보
     */
    @Override
    public User userUpdate(User user) {
        return userRepository.save(user);
    }

    /**
     * 사용자 로그인 처리를 수행합니다.
     *
     * @param id 로그인할 사용자의 아이디
     * @return 로그인 결과 코드
     *         - 1: 로그인 성공
     *         - 2: 로그인 실패 (REST 상태)
     *         - 3: 로그인 실패 (OUT 상태)
     */
    @Override
    public int loginPro(String id) {
        int pass = 0;
        User user = userRepository.getId(id);
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(30);

        if (localDateTime.isAfter(user.getLoginAt())) {
            user.setStatus(Status.REST);
            userRepository.save(user);
            pass = 2;
        } else {
            if (user.getStatus().equals(Status.ACTIVE)) {
                user.setLoginAt(LocalDateTime.now());
                userRepository.save(user);
                pass = 1;
            } else if (user.getStatus().equals(Status.REST)) {
                new AntPathRequestMatcher("/logout");
                pass = 2;
            } else if (user.getStatus().equals(Status.OUT)) {
                new AntPathRequestMatcher("/logout");
                pass = 3;
            }
            return pass;
        }
        return pass;
    }
}
