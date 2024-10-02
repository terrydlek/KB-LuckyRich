package mul.cam.e.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "mul.cam.e")  // <- 공통 팩키지
public class WebConfig implements WebMvcConfigurer {

    /*@Bean   // <- object 생성
    public InternalResourceViewResolver viewResolver() {
        log.info("WebConfig viewResolver() ~~~");

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();

        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }*/

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("/resources/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        log.info("WebConfigurer~~~~~~~~~~~");
        // 접속 클라이언트를 허가(Restful)
        registry.addMapping("/**").allowedOrigins("*");
        //registry.addMapping("/**").allowedOrigins("http://localhost:3000");
    }
}
















