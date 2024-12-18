package com.monitoring.comunication.auth;

import com.monitoring.comunication.controller.AdminController;
import com.monitoring.comunication.controller.MonotoringController;
import com.monitoring.comunication.entity.Admins;
import com.monitoring.comunication.entity.MonitoringDevice;
import com.monitoring.comunication.repository.AdminRepository;
import com.monitoring.comunication.repository.MonitoringDeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final MonitoringDeviceRepository personRepository;
    private final AdminRepository adminRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        username = jwtService.extractId(jwt).toString();
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(jwtService.extractRole(jwt)));

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      //      UUID adminId = adminRepository.findById(UUID.fromString(username)).get().getIdAdmin();
            UUID personID = personRepository.findByIdPerson(UUID.fromString(username)).getIdPerson();

            System.out.println(personID);
            if (jwtService.isTokenValid(jwt, personID)) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
       String role= jwtService.extractRole((jwt).toString());
        System.out.println(role+"aaaaaaaroleee");
        filterChain.doFilter(request, response);
    }

}
