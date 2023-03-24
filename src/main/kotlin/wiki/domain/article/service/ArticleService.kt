package wiki.domain.article.service

import org.springframework.stereotype.Service
import wiki.domain.article.dto.ArticleResponse
import wiki.domain.article.exception.ArticleNotFoundByTitleException
import wiki.domain.article.model.mediawiki.XmlMediaWikiTag
import wiki.domain.article.model.siteinfo.XmlSiteInfoTag
import wiki.domain.article.repository.page.XmlContributorTagRepository
import wiki.domain.article.repository.page.XmlMinorTagRepository
import wiki.domain.article.repository.page.XmlPageTagRepository
import wiki.domain.article.repository.page.XmlRedirectTagRepository
import wiki.domain.article.repository.page.XmlRevisionTagRepository
import wiki.domain.article.repository.page.XmlSha1TagRepository
import wiki.domain.article.repository.page.XmlTextTagRepository
import wiki.domain.article.repository.siteinfo.XmlNamespaceTagRepository
import wiki.domain.article.repository.siteinfo.XmlSiteInfoTagRepository
import java.io.FileInputStream
import java.util.UUID
import javax.xml.bind.JAXBContext

@Service
class ArticleService(
    private val xmlSiteInfoTagRepository: XmlSiteInfoTagRepository,
    private val xmlNamespaceTagRepository: XmlNamespaceTagRepository,
    private val xmlPageTagRepository: XmlPageTagRepository,
    private val xmlRedirectTagRepository: XmlRedirectTagRepository,
    private val xmlRevisionTagRepository: XmlRevisionTagRepository,
    private val xmlContributorTagRepository: XmlContributorTagRepository,
    private val xmlTextTagRepository: XmlTextTagRepository,
    private val xmlMinorTagRepository: XmlMinorTagRepository,
    private val xmlSha1TagRepository: XmlSha1TagRepository
) {
    fun xmlParser(xmlDir: String): ArticleResponse.IdResponse {
        println(System.getProperty("user.dir"))

        val fileInputStream = FileInputStream(xmlDir)
        val jaxbContext: JAXBContext = JAXBContext.newInstance(XmlMediaWikiTag::class.java)
        val unmarshaller = jaxbContext.createUnmarshaller()

        val xmlObject: XmlMediaWikiTag = unmarshaller.unmarshal(fileInputStream) as XmlMediaWikiTag
        fileInputStream.close()

        println("Saving ${xmlObject.siteInfo.siteName} pages into H2 database...")

        // save siteInfo into H2 database
        val siteInfo = xmlObject.siteInfo
        val namespaces = siteInfo.namespaces
        namespaces.map { it.siteInfo = siteInfo }
        siteInfo.namespaces = mutableListOf()

        val parsedSiteInfoTag: XmlSiteInfoTag = xmlSiteInfoTagRepository.save(siteInfo)
        xmlNamespaceTagRepository.saveAll(namespaces)

        // save page into H2 database
        val pages = xmlObject.pages
        val redirects = pages.map { it.redirect }
        val revisions = pages.map { it.revision }
        pages.map { page -> page.siteInfo = siteInfo }
        pages.zip(redirects).map { (page, redirect) -> page.redirect = redirect }
        pages.zip(revisions).map { (page, revision) -> page.revision = revision }

        xmlRedirectTagRepository.saveAll(redirects)
        xmlMinorTagRepository.saveAll(revisions.map { it.minor })
        xmlContributorTagRepository.saveAll(revisions.map { it.contributor })
        xmlSha1TagRepository.saveAll(revisions.map { it.sha1 })
        xmlTextTagRepository.saveAll(revisions.map { it.text })

        xmlRevisionTagRepository.saveAll(revisions)
        xmlRedirectTagRepository.saveAll(redirects)

        xmlPageTagRepository.saveAll(pages)

        return ArticleResponse.IdResponse(parsedSiteInfoTag.id)
    }

    fun getArticleInfo(title: String): ArticleResponse.InfoResponse =
        xmlPageTagRepository.findByTitle(title)
            ?.let { pageTag ->
                ArticleResponse.InfoResponse(
                    pageTag.title,
                    pageTag.ns,
                    pageTag._id,
                    ArticleResponse.RedirectResponse(pageTag.redirect._title)
                )
            }
            ?: throw ArticleNotFoundByTitleException("article is not found with the given title.")

    fun getArticleContent(title: String): ArticleResponse.ContentResponse =
        xmlPageTagRepository.findByTitle(title)
            ?.revision
            ?.let { tag ->
                ArticleResponse.ContentResponse(
                    tag.revisionId,
                    tag.parentId,
                    tag.timestamp,
                    ArticleResponse.ContributorResponse(
                        tag.contributor.username,
                        tag.contributor._id
                    ),
                    tag.comment,
                    tag.model,
                    tag.format,
                    ArticleResponse.TextResponse(
                        tag.text._bytes.toInt(),
                        tag.text.text
                    )
                )
            }
            ?: throw ArticleNotFoundByTitleException("article is not found with the given title.")

    fun getSiteInfo(articleId: String): ArticleResponse.SiteInfoResponse =
        xmlSiteInfoTagRepository.findXmlSiteInfoTagById(UUID.fromString(articleId))
            ?.let { siteInfo ->
                ArticleResponse.SiteInfoResponse(
                    siteInfo.siteName,
                    siteInfo.dbName,
                    siteInfo.base,
                    siteInfo.generator,
                    siteInfo._case,
                    siteInfo.namespaces.toList().map { namespace ->
                        ArticleResponse.NamespaceResponse(
                            namespace._key,
                            namespace._case,
                            namespace.text
                        )
                    }
                )
            }
            ?: throw ArticleNotFoundByTitleException("article is not found with the given id.")
}
