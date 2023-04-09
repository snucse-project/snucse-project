package wiki.domain.article.repository.siteinfo

import org.springframework.data.jpa.repository.JpaRepository
import wiki.domain.article.model.siteinfo.XmlNamespaceTag
import java.util.UUID

interface XmlNamespaceTagRepository : JpaRepository<XmlNamespaceTag, UUID?>
