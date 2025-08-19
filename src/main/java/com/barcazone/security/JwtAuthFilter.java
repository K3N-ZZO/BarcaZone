package com.barcazone.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwt; private final UserDetailsService uds;

    @Override protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        String h=req.getHeader("Authorization");
        if(h!=null && h.startsWith("Bearer ")){
            String token=h.substring(7);
            try{
                String username=jwt.parseUsername(token);
                if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                    UserDetails ud=uds.loadUserByUsername(username);
                    var auth=new UsernamePasswordAuthenticationToken(ud,null,ud.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }catch(Exception ignored){}
        }
        chain.doFilter(req,res);
    }
}
