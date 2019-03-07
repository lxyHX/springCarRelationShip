package car.guangqi;

import car.guangqi.util.SpringUtil ;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by lee on 19/3/7.
 */
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})

@EnableScheduling
@EnableAsync(proxyTargetClass = true)
@MapperScan(basePackages = "car.guangqi.mapper")
public class Application {

    public static void main(String[] args) throws Exception {
        SpringUtil.setApplicationContext(SpringApplication.run(Application.class, args));
    }

}
