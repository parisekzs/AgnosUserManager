package hu.mi.user.manager.service;

import hu.mi.jwt.model.token.JwtToken;
import hu.mi.user.manager.repository.HazelcastKeyStore;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Service
public class JwtUtil {

    @Autowired
    private HazelcastKeyStore hazelcastRepo;

    private int jwtExpirationInMs;

    private int jwtUsableInMs;

    private int keyRingMaxSize;

    private String keyStoreName;

    @Value("${jwt.expirationTimeInMs}")
    public void setJwtExpirationInMs(int jwtExpirationInMs) {
        this.jwtExpirationInMs = jwtExpirationInMs;
    }

    @Value("${jwt.usableTimeInMs}")
    public void setJwtUsableInMs(int jwtUsableInMs) {
        this.jwtUsableInMs = jwtUsableInMs;
    }

    @Value("${jwt.keyRingMaxSize}")
    public void setKeyRingMaxSize(int keyRingMaxSize) {
        this.keyRingMaxSize = keyRingMaxSize;
    }

    @Value("${jwt.keyStoreName}")
    public void setKeyStoreName(String keyStoreName) {
        this.keyStoreName = keyStoreName;
    }

    public String generateToken(UserDetails userDetails) {   
        return JwtToken.builder()
                .keyStoreName(this.keyStoreName)
                .subject(userDetails.getUsername())
                .claims(userDetails.getAuthorities())
                .keyStore(hazelcastRepo)
                .keyRingMaxSize(keyRingMaxSize)
                .build()
                .generateToken();
    }

    public String validateToken(String authToken) {
//        long s1 = System.nanoTime();
        String r = JwtToken.builder()
                .keyStoreName(this.keyStoreName)
                .keyStore(hazelcastRepo)
                .keyRingMaxSize(keyRingMaxSize)
                .build()
                .validateToken(authToken, jwtExpirationInMs, jwtUsableInMs);
//        long s2 = System.nanoTime();
//        System.out.println("validation time: " + (s2 - s1));
        return r;
    }

    public String getUsernameFromToken(String authToken) {
        return JwtToken.getUsernameFromToken(authToken);
    }

    public List<SimpleGrantedAuthority> getRolesFromToken(String authToken) {
        return JwtToken.getRolesFromToken(authToken);
    }

}
