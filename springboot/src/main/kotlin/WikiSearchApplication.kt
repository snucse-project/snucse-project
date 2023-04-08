package wiki

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WikiSearchApplication

fun main(args: Array<String>) {
    runApplication<WikiSearchApplication>(*args)
}
