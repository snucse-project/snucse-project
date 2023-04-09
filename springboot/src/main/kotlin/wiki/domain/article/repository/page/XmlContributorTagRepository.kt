package wiki.domain.article.repository.page

import org.springframework.data.jpa.repository.JpaRepository
import wiki.domain.article.model.page.XmlContributorTag
import java.util.UUID

interface XmlContributorTagRepository : JpaRepository<XmlContributorTag, UUID?>
