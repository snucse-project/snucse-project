package com.wafflestudio.msns.domain.article.api

import com.wafflestudio.msns.domain.article.dto.ArticleResponse
import com.wafflestudio.msns.domain.article.service.ArticleService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/article")
class ArticleController(
    private val articleService: ArticleService
) {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getArticle(
        @RequestParam(name = "xml") xmlDir: String
    ): ArticleResponse.TitleResponse = articleService.xmlParser(xmlDir)
}
