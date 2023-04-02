package wiki.domain.article.service

import org.springframework.stereotype.Service
import wiki.domain.article.dto.ArticleResponse
import wiki.domain.article.exception.ArticleNotFoundException
import wiki.domain.article.model.mediawiki.XmlMediaWikiTag
import wiki.domain.article.model.page.XmlPageTag
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
            ?: throw ArticleNotFoundException("article is not found.")

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
                        tag.contributor._id,
                        tag.contributor.ip
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
            ?: throw ArticleNotFoundException("article is not found with the given title.")

    fun getSiteInfo(siteInfoId: String): ArticleResponse.SiteInfoResponse =
        xmlSiteInfoTagRepository.findXmlSiteInfoTagById(UUID.fromString(siteInfoId))
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
            ?: throw ArticleNotFoundException("article is not found with the given siteinfo_id.")

    fun getArticleByTitle(title: String): ArticleResponse.Response =
        getArticleFromEntity(xmlPageTagRepository.findByTitle(title), "title")

    fun getArticleByContributorId(id: Int): ArticleResponse.ListResponse =
        ArticleResponse.ListResponse(
            xmlPageTagRepository.findAllByRevision_Contributor__id(id).map { getArticleFromEntity(it, "contributor id") }
        )

    fun getArticleByContributorUsername(username: String): ArticleResponse.ListResponse =
        ArticleResponse.ListResponse(
            xmlPageTagRepository.findAllByRevision_Contributor_Username(username).map { getArticleFromEntity(it, "contributor username") }
        )

    fun getArticleByContributorIP(ip: String): ArticleResponse.ListResponse =
        ArticleResponse.ListResponse(
            xmlPageTagRepository.findAllByRevision_Contributor_Ip(ip).map { getArticleFromEntity(it, "contributor IP") }
        )

    fun getArticleFromEntity(xmlPageTag: XmlPageTag?, obj: String): ArticleResponse.Response =
        xmlPageTag
            ?.let { tag ->
                ArticleResponse.Response(
                    info = ArticleResponse.InfoResponse(
                        tag.title,
                        tag.ns,
                        tag._id,
                        ArticleResponse.RedirectResponse(tag.redirect._title)
                    ),
                    content = ArticleResponse.ContentResponse(
                        tag.revision.revisionId,
                        tag.revision.parentId,
                        tag.revision.timestamp,
                        ArticleResponse.ContributorResponse(
                            tag.revision.contributor.username,
                            tag.revision.contributor._id,
                            tag.revision.contributor.ip
                        ),
                        tag.revision.comment,
                        tag.revision.model,
                        tag.revision.format,
                        ArticleResponse.TextResponse(
                            tag.revision.text._bytes.toInt(),
                            tag.revision.text.text
                        )
                    )
                )
            }
            ?: throw ArticleNotFoundException("article is not found with the given $obj.")
}
