package gq.learningEnglish

import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Controller

@Controller
class Controller {
    @Bean
    fun start() {
        println("Test")
    }
}