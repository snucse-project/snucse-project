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

    @GetMapping("/{title}")
    @ResponseStatus(HttpStatus.OK)
    fun getArticle(
        @PathVariable("title") title: String
    ): ArticleResponse.Response = articleService.getArticleByTitle(title)

    @GetMapping("/siteinfo/{siteinfo_id}")
    @ResponseStatus(HttpStatus.OK)
    fun getSiteInfo(
        @PathVariable("siteinfo_id") siteInfoId: String
    ): ArticleResponse.SiteInfoResponse = articleService.getSiteInfo(siteInfoId)

    @GetMapping("/contributor/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getArticleInfoByContributorId(
        @PathVariable("id") contributorId: Int
    ): ArticleResponse.ListResponse = articleService.getArticleByContributorId(contributorId)

    @GetMapping("/contributor/username/{username}")
    @ResponseStatus(HttpStatus.OK)
    fun getArticleInfoByContributorUsername(
        @PathVariable("username") contributorUsername: String
    ): ArticleResponse.ListResponse = articleService.getArticleByContributorUsername(contributorUsername)

    @GetMapping("/contributor/ip/{ip}")
    @ResponseStatus(HttpStatus.OK)
    fun getArticleInfoByContributorIP(
        @PathVariable("ip") contributorIp: String
    ): ArticleResponse.ListResponse = articleService.getArticleByContributorIP(contributorIp)
}
