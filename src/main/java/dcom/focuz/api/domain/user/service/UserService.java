package dcom.focuz.api.domain.user.service;

import dcom.focuz.api.domain.user.Role;
import dcom.focuz.api.domain.user.User;
import dcom.focuz.api.domain.user.dto.UserRequestDto;
import dcom.focuz.api.domain.user.dto.UserResponseDto;
import dcom.focuz.api.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
public class UserService {
    protected final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<UserResponseDto.Simple> getAllUser(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserResponseDto.Simple::of);
    }

    @Transactional(readOnly = true)
    public UserResponseDto.Profile findUserById(Integer id) {
        return UserResponseDto.Profile.of(userRepository.getAllInfoOfUserById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."
                )
        ));
    }

    @Transactional(readOnly = true)
    public UserResponseDto.Profile getMyProfile() {
        return UserResponseDto.Profile.of(userRepository.getAllInfoOfUserById(getCurrentUser().getId()).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."
                )
        ));
    }

    @Transactional(readOnly = true)
    public UserResponseDto.Simple getMySimpleProfile() {
        return UserResponseDto.Simple.of(userRepository.findById(getCurrentUser().getId()).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."
                )
        ));
    }

    @Transactional
    public UserResponseDto.Profile register(UserRequestDto.Register data) {
        User user = getCurrentUser();

        if (user.getRole() != Role.GUEST) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "손님 권한을 가진 사람만 등록 할 수 있습니다."
            );
        }

        user.setMotto(data.getMotto());
        user.setNickname(data.getNickname());
        user.setRole(Role.USER);

        return UserResponseDto.Profile.of(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal().equals("anonymousUser")) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "로그인 되지 않았습니다."
            );
        }

        return (User) authentication.getPrincipal();
    }
}