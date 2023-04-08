package wiki.domain.article.model.page

import wiki.domain.base.BaseEntity
import javax.persistence.Entity
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlRootElement

@Entity(name = "redirect")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "redirect", namespace = "http://www.mediawiki.org/xml/export-0.10/")
class XmlRedirectTag : BaseEntity() {
    @XmlAttribute(name = "title")
    val _title: String? = null
}
