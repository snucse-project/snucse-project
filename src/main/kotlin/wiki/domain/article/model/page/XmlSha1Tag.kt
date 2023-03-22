package wiki.domain.article.model.page

import wiki.domain.base.BaseEntity
import javax.persistence.Entity
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlValue

@Entity(name = "sha1")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "sha1", namespace = "http://www.mediawiki.org/xml/export-0.10/")
class XmlSha1Tag : BaseEntity() {
    @XmlValue
    val sha1: String = ""
}
