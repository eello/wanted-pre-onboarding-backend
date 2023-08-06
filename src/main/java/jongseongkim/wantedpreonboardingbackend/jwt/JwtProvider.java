package jongseongkim.wantedpreonboardingbackend.jwt;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jongseongkim.wantedpreonboardingbackend.entity.User;

@Component
public class JwtProvider {

	public static final Long ACCESS_TOKEN_EXPIRES = 3600L; // 1 Hour
	public static final Long REFRESH_TOKEN_EXPIRES = 604800L; // 1 Week

	private final Key secretKey;

	public JwtProvider(@Value("${jwt.secret}") String secret) {
		String jwtSecret = Base64.getEncoder().encodeToString(secret.getBytes());
		byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
		this.secretKey = Keys.hmacShaKeyFor(keyBytes);
	}

	public String generateAccessToken(User user) {
		return generateToken(user, ACCESS_TOKEN_EXPIRES);
	}

	public String generateRefreshToken(User user) {
		return generateToken(user, REFRESH_TOKEN_EXPIRES);
	}

	private String generateToken(User user, Long expiration) {
		Date now = new Date();
		Date expires = new Date(now.getTime() + expiration);

		return Jwts.builder()
			.setSubject(user.getEmail())
			.setIssuer("wpob")
			.setIssuedAt(now)
			.setExpiration(expires)
			.signWith(secretKey)
			.compact();
	}

	public void validateToken(String token) {
		Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
	}

	public Claims parseClaims(String token) {
		Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
		return claims.getBody();
	}
}
