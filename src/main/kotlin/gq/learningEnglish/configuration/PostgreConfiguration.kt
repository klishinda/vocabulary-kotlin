package gq.learningEnglish.configuration

import com.zaxxer.hikari.HikariConfig
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("postgres.datasource")
open class PostgreConfiguration : HikariConfig()