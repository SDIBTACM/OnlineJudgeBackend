package cn.edu.sdtbu.config;

import cn.edu.sdtbu.interceptor.CookieInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-04-07 17:16
 */


@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    CookieInterceptor cookieInterceptor;
    public WebConfig(CookieInterceptor cookieInterceptor) {
        this.cookieInterceptor = cookieInterceptor;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
            .addResourceLocations("classpath:/META-INF/resources/")
            .addResourceLocations("classpath:/resources/")
            .addResourceLocations("classpath:/resources/templates/")
            .addResourceLocations("classpath:/static/")
            .addResourceLocations("classpath:/public/");
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> exclude = new ArrayList<>(8);
        exclude.add("/js/**");
        exclude.add("/css/**");
        exclude.add("/favicon.ico");
        exclude.add("/");
        registry.addInterceptor(cookieInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns(exclude);
    }
}
