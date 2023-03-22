package wiki.domain.article.repository.page

import org.springframework.data.jpa.repository.JpaRepository
import wiki.domain.article.model.page.XmlMinorTag
import java.util.UUID

interface XmlMinorTagRepository : JpaRepository<XmlMinorTag, UUID?>
