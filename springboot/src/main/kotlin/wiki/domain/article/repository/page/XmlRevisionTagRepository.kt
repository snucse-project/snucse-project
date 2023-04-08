package wiki.domain.article.repository.page

import org.springframework.data.jpa.repository.JpaRepository
import wiki.domain.article.model.page.XmlRevisionTag
import java.util.UUID

interface XmlRevisionTagRepository : JpaRepository<XmlRevisionTag, UUID?>
