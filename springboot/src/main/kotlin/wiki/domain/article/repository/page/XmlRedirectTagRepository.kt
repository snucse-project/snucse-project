package wiki.domain.article.repository.page

import org.springframework.data.jpa.repository.JpaRepository
import wiki.domain.article.model.page.XmlRedirectTag
import java.util.UUID

interface XmlRedirectTagRepository : JpaRepository<XmlRedirectTag, UUID?>
