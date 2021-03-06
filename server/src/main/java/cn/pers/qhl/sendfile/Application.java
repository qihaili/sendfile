package cn.pers.qhl.sendfile;

import cn.pers.qhl.sendfile.config.SendFileConfig;
import cn.pers.qhl.sendfile.config.TtlOption;
import com.google.common.base.Predicates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.util.unit.DataSize;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
@EnableSwagger2
@EnableAsync(proxyTargetClass = true)
public class Application implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Value("${swagger.show:true}")
    private boolean swaggerShow;

    @Autowired
    private SendFileConfig config;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(swaggerShow)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(Predicates.not(PathSelectors.regex("/error"))) // Exclude Spring error controllers
                .build();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (TtlOption ttl : config.getShare().getTtlOptions()) {
            logger.debug("过期时间：" + ttl.toString());
        }
        logger.debug("扫描间隔：" + config.getShare().getScanInterval());
        logger.debug("最大文件：" + config.getShare().getMaxFileSize());
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //  单个数据大小
//        factory.setMaxFileSize(DataSize.ofBytes(new Double(config.getShare().getMaxFileSize() * 1024 * 1024).longValue()));
        factory.setMaxFileSize(DataSize.parse(config.getShare().getMaxFileSize()));
        /// 总上传数据大小
        factory.setMaxRequestSize(DataSize.ofBytes(-1));
        return factory.createMultipartConfig();
    }
}
