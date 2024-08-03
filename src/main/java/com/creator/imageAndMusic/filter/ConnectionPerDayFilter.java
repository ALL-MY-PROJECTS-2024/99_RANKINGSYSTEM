package com.creator.imageAndMusic.filter;


import com.creator.imageAndMusic.config.auth.PrincipalDetails;
import com.creator.imageAndMusic.config.auth.jwt.JwtProperties;
import com.creator.imageAndMusic.config.auth.jwt.JwtTokenProvider;
import com.creator.imageAndMusic.domain.entity.ConnectionUser;
import com.creator.imageAndMusic.domain.repository.ConnectionUserRepository;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@WebFilter("/*")
@Component
public class ConnectionPerDayFilter implements Filter {

    @Autowired
    private ConnectionUserRepository connectionRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    Long previousTime = 0L;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        //전
        Long nowTime = System.currentTimeMillis();

        HttpServletRequest req1 = (HttpServletRequest) request;
        String token = null;
        if(req1.getCookies()!=null)
        {
            token = Arrays.stream(req1.getCookies())
                    .filter(c -> c.getName().equals(JwtProperties.COOKIE_NAME)).findFirst()
                    .map(c -> c.getValue())
                    .orElse(null);
        }

        String username = "ANONYMOUS";
        if(token != null)
        {
            Authentication authentication =  jwtTokenProvider.getAuthentication(token);
            PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();;
            username = principalDetails.getUserDto().getUsername();
        }

        if(nowTime - previousTime <= 1000*3)  //3초미만 반복 요청시
        {
            String ip = request.getRemoteAddr();
            HttpServletRequest req = (HttpServletRequest)request;
            String uri = req.getRequestURI();
            ConnectionUser connectionUser =  connectionRepository.findByTodayAndRemoteIpAndUsername(LocalDate.now(),ip,username);

            if(connectionUser==null){
                ConnectionUser connection = new ConnectionUser();
                connection.setRequestUri(uri);
                connection.setRepeatPerSecond(0L);
                connection.setCount(1L);
                connection.setRemoteIp(ip);
                connection.setToday(LocalDate.now());
                connection.setUsername(username);
                connectionRepository.save(connection);

            }else{

                connectionUser.setRepeatPerSecond(connectionUser.getRepeatPerSecond()+1);
                connectionRepository.save(connectionUser);
            }
        }
        else    //요청이 3초가 넘어섰을때
        {

            String ip = request.getRemoteAddr();
            HttpServletRequest req = (HttpServletRequest)request;
            String uri = req.getRequestURI();
            ConnectionUser connectionUser =  connectionRepository.findByTodayAndRemoteIpAndUsername(LocalDate.now(),ip,username);
            if(connectionUser==null){
                ConnectionUser connection = new ConnectionUser();
                connection.setRequestUri(uri);
                connection.setRepeatPerSecond(0L);
                connection.setCount(1L);
                connection.setRemoteIp(ip);
                connection.setToday(LocalDate.now());
                connection.setUsername(username);
                connectionRepository.save(connection);
            }else{
                connectionUser.setCount(connectionUser.getCount() + 1);
                connectionRepository.save(connectionUser);
            }
        }

        previousTime = nowTime;

        chain.doFilter(request, response);
        //후
    }
}
