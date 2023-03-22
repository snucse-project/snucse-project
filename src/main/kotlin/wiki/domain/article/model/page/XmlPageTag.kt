package wiki.domain.article.model.page

import wiki.domain.base.BaseEntity
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@Entity(name = "page")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "page", namespace = "http://www.mediawiki.org/xml/export-0.10/")
class XmlPageTag : BaseEntity() {
    @XmlElement(name = "title", namespace = "http://www.mediawiki.org/xml/export-0.10/")
    val title: String = ""

    @XmlElement(name = "ns", namespace = "http://www.mediawiki.org/xml/export-0.10/")
    val ns: Int = 0

    @XmlElement(name = "id", namespace = "http://www.mediawiki.org/xml/export-0.10/")
    val _id: Int = 0

    @OneToOne
    @JoinColumn(name = "redirect_id", referencedColumnName = "id")
    @XmlElement(name = "redirect", namespace = "http://www.mediawiki.org/xml/export-0.10/")
    var redirect: XmlRedirectTag = XmlRedirectTag()

    @OneToOne
    @JoinColumn(name = "revision_id", referencedColumnName = "id")
    @XmlElement(name = "revision", namespace = "http://www.mediawiki.org/xml/export-0.10/")
    var revision: XmlRevisionTag = XmlRevisionTag()
}
