package com.chatapp.auth;

import com.chatapp.auth.dto.AuthResponse;
import com.chatapp.auth.dto.LoginRequest;
import com.chatapp.auth.dto.RegisterRequest;
import com.chatapp.auth.exceptions.InvalidCredentialsException;
import com.chatapp.auth.exceptions.UserAlreadyExistsException;
import com.chatapp.auth.security.jwt.JwtService;
import com.chatapp.auth.users.User;
import com.chatapp.auth.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;



    @Transactional
    public AuthResponse register(RegisterRequest request){
        if (userRepository.existsByEmail(request.email())){
            throw new UserAlreadyExistsException("Email is already in use");
        }

        if (userRepository.existsByEmail(request.username())){
            throw new UserAlreadyExistsException("Username is already in use");
        }

        User user=User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();

        User saved = userRepository.save(user);
        String token=jwtService.generateToken(saved);

        return new AuthResponse(saved.getUsername(),saved.getEmail(),token);
    }


    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request){
        User user=userRepository.findByUsername(request.username())
                .orElseThrow(()->new InvalidCredentialsException(
                        "Invalid credentials"
                ));

        if (!passwordEncoder.matches(request.password(), user.getPassword())){
            throw new InvalidCredentialsException("Invalid credentiasl");
        }

        String token=jwtService.generateToken(user);
        return new AuthResponse(user.getUsername(), user.getEmail(),token);
    }
}
