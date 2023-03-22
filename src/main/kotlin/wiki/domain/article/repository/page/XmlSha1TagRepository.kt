package wiki.domain.article.repository.page

import org.springframework.data.jpa.repository.JpaRepository
import wiki.domain.article.model.page.XmlSha1Tag
import java.util.UUID

interface XmlSha1TagRepository : JpaRepository<XmlSha1Tag, UUID?>
