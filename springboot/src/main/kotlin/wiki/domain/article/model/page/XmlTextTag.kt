package wiki.domain.article.model.page

import wiki.domain.base.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlValue

@Entity(name = "text")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "text", namespace = "http://www.mediawiki.org/xml/export-0.10/")
class XmlTextTag : BaseEntity() {
    @XmlAttribute(name = "bytes")
    val _bytes: String = ""

    @XmlValue
    @Column(length = 1000000)
    val text: String = ""
}
