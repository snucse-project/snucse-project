package wiki.domain.article.api

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import wiki.domain.article.dto.ArticleResponse
import wiki.domain.article.service.ArticleService

@RestController
@RequestMapping("/article")
class ArticleController(
    private val articleService: ArticleService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    fun saveArticle(
        @RequestParam(name = "xml") xmlDir: String
    ): ArticleResponse.IdResponse = articleService.xmlParser(xmlDir)

    @GetMapping("/info/{title}")
    @ResponseStatus(HttpStatus.OK)
    fun getArticleInfo(
        @PathVariable("title") title: String
    ): ArticleResponse.InfoResponse = articleService.getArticleInfo(title)

    @GetMapping("/content/{title}")
    @ResponseStatus(HttpStatus.OK)
    fun getArticleContent(
        @PathVariable("title") title: String
    ): ArticleResponse.ContentResponse = articleService.getArticleContent(title)

    @GetMapping("/{article_id}/siteinfo")
    @ResponseStatus(HttpStatus.OK)
    fun getSiteInfo(
        @PathVariable("article_id") articleId: String
    ): ArticleResponse.SiteInfoResponse = articleService.getSiteInfo(articleId)
}
