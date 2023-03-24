package wiki.domain.article.repository.siteinfo

import org.springframework.data.jpa.repository.JpaRepository
import wiki.domain.article.model.siteinfo.XmlSiteInfoTag
import java.util.UUID

interface XmlSiteInfoTagRepository : JpaRepository<XmlSiteInfoTag, UUID?> {
    fun findXmlSiteInfoTagById(id: UUID): XmlSiteInfoTag?
}
