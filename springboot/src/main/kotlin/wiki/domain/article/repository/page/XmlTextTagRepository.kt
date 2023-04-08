package wiki.domain.article.repository.page

import org.springframework.data.jpa.repository.JpaRepository
import wiki.domain.article.model.page.XmlTextTag
import java.util.UUID

interface XmlTextTagRepository : JpaRepository<XmlTextTag, UUID?>
