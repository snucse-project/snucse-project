package wiki.domain.article.repository.page

import org.springframework.data.jpa.repository.JpaRepository
import wiki.domain.article.model.page.XmlPageTag
import java.util.UUID

interface XmlPageTagRepository : JpaRepository<XmlPageTag, UUID?> {
    fun findByTitle(title: String): XmlPageTag?

    fun findAllByRevision_Contributor__id(id: Int): List<XmlPageTag>

    fun findAllByRevision_Contributor_Username(username: String): List<XmlPageTag>

    fun findAllByRevision_Contributor_Ip(ip: String): List<XmlPageTag>
}
