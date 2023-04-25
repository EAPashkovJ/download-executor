package com.eapashkov.downloadexecutor

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class DownloadExecutorApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(DownloadExecutorApplication::class.java, *args)
        }
    }
}
