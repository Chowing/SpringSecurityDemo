
package com.forezp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true) //提供方法级别的安全支持
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    /**
     * 以“/css/**”开头的资源和“/index”资源不需要验证，外界请求可以直接访问
     * 以“/user/**”和“/blogs/**”开头的资源需要验证，并且需要用户的角色是“Role”
     * 表单登录的地址是“/login”，登录失败的地址是“/login-error”
     * 异常处理会重定向到"/401"界面
     * 注销登录成功，重定向到首页
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/css/**", "/index").permitAll()
                .antMatchers("/user/**").hasRole("USER")
                .antMatchers("/blogs/**").hasRole("USER")
                .and()
                .formLogin().loginPage("/login").failureUrl("/login-error")
                .and()
                .exceptionHandling().accessDeniedPage("/401");
        http.logout().logoutSuccessUrl("/");
    }

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication().withUser("forezp").password("123456").roles("USER")

        //让Spring Security从数据库中获取用户的认证信息，而不是之前从内存中读取
        auth.userDetailsService(userDetailsService);
    }
}
