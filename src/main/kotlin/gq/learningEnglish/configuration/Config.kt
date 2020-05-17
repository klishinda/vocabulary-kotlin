package gq.learningEnglish.configuration

import com.zaxxer.hikari.HikariDataSource
import gq.learningEnglish.common.JdbcDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class Config {
    @Bean
    @Autowired
    open fun postgreDao(postgreDataSource: HikariDataSource): JdbcDao = JdbcDao(postgreDataSource)

    @Bean
    @Autowired
    open fun postgreDataSource(configuration: PostgreConfiguration): HikariDataSource = HikariDataSource(configuration)
}