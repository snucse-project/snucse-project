package wiki.domain.article.model.page

import wiki.domain.base.BaseEntity
import javax.persistence.Entity
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@Entity(name = "contributor")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "contributor", namespace = "http://www.mediawiki.org/xml/export-0.10/")
class XmlContributorTag : BaseEntity() {
    @XmlElement(name = "username", namespace = "http://www.mediawiki.org/xml/export-0.10/")
    val username: String? = null

    @XmlElement(name = "id", namespace = "http://www.mediawiki.org/xml/export-0.10/")
    val _id: Int? = null

    @XmlElement(name = "ip", namespace = "http://www.mediawiki.org/xml/export-0.10/")
    val ip: String? = null
}
