package gq.learningEnglish.configuration

import com.zaxxer.hikari.HikariDataSource
import gq.learningEnglish.common.JdbcDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class Config {
    @Bean
    @Autowired
    @ConditionalOnMissingBean(name = ["postgreDao"])
    open fun fdsDao(fdsDataSource: HikariDataSource): JdbcDao {
        println("===config===")
        println(fdsDataSource.password)
        println(fdsDataSource.username)
        return JdbcDao(fdsDataSource)
    }

    @Bean
    @Autowired
    open fun fdsDataSource(configuration: PostgreConfiguration): HikariDataSource = HikariDataSource(configuration)
}