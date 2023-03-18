package com.wafflestudio.msns.domain.article.service

import com.wafflestudio.msns.domain.article.dto.ArticleResponse
import com.wafflestudio.msns.domain.article.model.mediawiki.XmlMediaWikiTag
import com.wafflestudio.msns.domain.article.repository.page.XmlContributorTagRepository
import com.wafflestudio.msns.domain.article.repository.page.XmlMinorTagRepository
import com.wafflestudio.msns.domain.article.repository.page.XmlPageTagRepository
import com.wafflestudio.msns.domain.article.repository.page.XmlRedirectTagRepository
import com.wafflestudio.msns.domain.article.repository.page.XmlRevisionTagRepository
import com.wafflestudio.msns.domain.article.repository.page.XmlSha1TagRepository
import com.wafflestudio.msns.domain.article.repository.page.XmlTextTagRepository
import com.wafflestudio.msns.domain.article.repository.siteinfo.XmlNamespaceTagRepository
import com.wafflestudio.msns.domain.article.repository.siteinfo.XmlSiteInfoTagRepository
import org.springframework.stereotype.Service
import java.io.FileInputStream
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
    fun xmlParser(xmlDir: String): ArticleResponse.TitleResponse {
        println(System.getProperty("user.dir"))

        val fileInputStream = FileInputStream(xmlDir)
        val jaxbContext: JAXBContext = JAXBContext.newInstance(XmlMediaWikiTag::class.java)
        val unmarshaller = jaxbContext.createUnmarshaller()

        val xmlObject: XmlMediaWikiTag = unmarshaller.unmarshal(fileInputStream) as XmlMediaWikiTag
        fileInputStream.close()

        // save siteInfo into H2 database
        val siteInfo = xmlObject.siteInfo
        val namespaces = siteInfo.namespaces
        namespaces.map { it.siteInfo = siteInfo }
        siteInfo.namespaces = mutableListOf()

        xmlSiteInfoTagRepository.save(siteInfo)
        xmlNamespaceTagRepository.saveAll(namespaces)

        // save page into H2 database
        val pages = xmlObject.pages
        val redirects = pages.map { it.redirect }
        val revisions = pages.map { it.revision }
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

        return ArticleResponse.TitleResponse(xmlObject.siteInfo._case)
    }
}
