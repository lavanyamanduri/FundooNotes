package com.bridgelabz.fundoonotes.utility;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Service
public class JwtUtil {

	private String SECRET_KEY = "secret";

	public String jwtGenerateToken(String userMail) {
		Map<String, Object> claims = new HashMap<>();
		return createJwtToken(claims, userMail);
	}

	private String createJwtToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}

	public String parse(String token) {
		boolean isValid = false;
		String email = null;
		if (token != null) {
			Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
			email = claims.getSubject();
			isValid = claims.getExpiration().after(Date.from(Instant.now()));
		}
		if (isValid) {
			return email;
		} else {
			return null;
		}
	}

}