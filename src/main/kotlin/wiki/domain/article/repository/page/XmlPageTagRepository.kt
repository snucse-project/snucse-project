package wiki.domain.article.repository.page

import org.springframework.data.jpa.repository.JpaRepository
import wiki.domain.article.model.page.XmlPageTag
import java.util.UUID

interface XmlPageTagRepository : JpaRepository<XmlPageTag, UUID?> {
    fun findByTitle(title: String): XmlPageTag?
}
