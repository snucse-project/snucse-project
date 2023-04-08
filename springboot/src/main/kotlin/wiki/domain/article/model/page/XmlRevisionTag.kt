package wiki.domain.article.model.page

import wiki.domain.base.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@Entity(name = "revision")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "revision", namespace = "http://www.mediawiki.org/xml/export-0.10/")
class XmlRevisionTag : BaseEntity() {
    @Column(name = "revision_id")
    @XmlElement(name = "id", namespace = "http://www.mediawiki.org/xml/export-0.10/")
    val revisionId: Int = 0

    @XmlElement(name = "parentid", namespace = "http://www.mediawiki.org/xml/export-0.10/")
    val parentId: Int = 0

    @XmlElement(name = "timestamp", namespace = "http://www.mediawiki.org/xml/export-0.10/")
    val timestamp: String = ""

    @OneToOne
    @JoinColumn(name = "contributor_id", referencedColumnName = "id")
    @XmlElement(name = "contributor", namespace = "http://www.mediawiki.org/xml/export-0.10/")
    val contributor: XmlContributorTag = XmlContributorTag()

    @OneToOne
    @JoinColumn(name = "minor_id", referencedColumnName = "id")
    @XmlElement(name = "minor", namespace = "http://www.mediawiki.org/xml/export-0.10/")
    val minor: XmlMinorTag = XmlMinorTag()

    @XmlElement(name = "comment", namespace = "http://www.mediawiki.org/xml/export-0.10/")
    @Column(length = 10000)
    val comment: String = ""

    @XmlElement(name = "model", namespace = "http://www.mediawiki.org/xml/export-0.10/")
    val model: String = ""

    @XmlElement(name = "format", namespace = "http://www.mediawiki.org/xml/export-0.10/")
    val format: String = ""

    @OneToOne
    @JoinColumn(name = "text_id", referencedColumnName = "id")
    @XmlElement(name = "text", namespace = "http://www.mediawiki.org/xml/export-0.10/")
    val text: XmlTextTag = XmlTextTag()

    @OneToOne
    @JoinColumn(name = "sha1_id", referencedColumnName = "id")
    @XmlElement(name = "sha1", namespace = "http://www.mediawiki.org/xml/export-0.10/")
    val sha1: XmlSha1Tag = XmlSha1Tag()
}
