package gq.learningEnglish.configuration

import com.zaxxer.hikari.HikariDataSource
import gq.learningEnglish.common.JdbcDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
open class Config {

    @Bean
    @Autowired
    open fun postgreDao(postgreDataSource: HikariDataSource): JdbcDao = JdbcDao(postgreDataSource)

    @Bean
    @Autowired
    open fun postgreDataSource(configuration: PostgreConfiguration): HikariDataSource = HikariDataSource(configuration)

    @Bean
    open fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.any())
            .build()
    }
}