package mul.cam.e.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

// Database 설정
@Configuration
@PropertySource({"classpath:/application-secret.properties"})
@MapperScan("mul.cam.e")
public class AppConfig {

    @Value("${aws.driver}") String driver;
    @Value("${aws.url}") String url;
    @Value("${aws.username}") String username;
    @Value("${aws.password}") String password;

    @Bean
    public DataSource dataSource(){
        System.out.println("~~~ AppConfig dataSource()");

        BasicDataSource dataSource = new BasicDataSource();
//        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
//        dataSource.setUrl("jdbc:mysql://localhost:3306/kb_final?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8&useUnicode=true");
//        dataSource.setUsername("root");
//        dataSource.setPassword("0000");

        // AWS database

        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        dataSource.setInitialSize(5);
        dataSource.setMaxTotal(10);
        return dataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        System.out.println("~~~ AppConfig sqlSessionFactory()");

        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);

        //sessionFactory.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath:sql/*.xml");
        sessionFactory.setMapperLocations(resources);
        sessionFactory.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);

        return sessionFactory.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}

