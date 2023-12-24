package com.mission.tablereservation.config;

import com.mission.tablereservation.model.CustomerDto;
import com.mission.tablereservation.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;
    private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60; // 1 시간을 의미
    private final MemberService memberService;

    public String createToken(CustomerDto customer, List<UserType> roles){

        Claims claims = Jwts.claims().setSubject(customer.getEmail());
        claims.put("roles", roles);
        claims.put("name", customer.getName());

        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS512, this.secretKey)
                .compact();

    }

    public Authentication getAuthentication(String jwt){
        UserDetails userDetails = this.memberService.loadUserByUsername(this.getUserEmail(jwt));
//        UserDetails userDetails = this.memberService.loadUserByUsername(this.getId(jwt));
        System.out.println("UserDetails => " + userDetails.getUsername());
        System.out.println(userDetails.getAuthorities());

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String getUserEmail(String token){
        return this.parseClaims(token).getSubject();
    }

    private Long getId(String token){

        return this.parseClaims(token).get("id", Long.class);
    }

    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)){
            return false;
        }

        Claims claims = this.parseClaims(token);
        return !claims.getExpiration().before(new Date());
    }

    private Claims parseClaims(String token){
        try{
            return Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody();
        }catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }

}
