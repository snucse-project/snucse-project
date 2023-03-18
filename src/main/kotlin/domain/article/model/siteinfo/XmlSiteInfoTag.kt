package com.wafflestudio.msns.domain.article.model.siteinfo

import com.wafflestudio.msns.domain.base.BaseEntity
import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlElementWrapper
import javax.xml.bind.annotation.XmlRootElement

@Entity(name = "site_info")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "siteinfo", namespace = "http://www.mediawiki.org/xml/export-0.10/")
class XmlSiteInfoTag : BaseEntity() {
    @XmlElement(name = "sitename", namespace = "http://www.mediawiki.org/xml/export-0.10/")
    val siteName: String = ""

    @XmlElement(name = "dbname", namespace = "http://www.mediawiki.org/xml/export-0.10/")
    val dbName: String = ""

    @XmlElement(name = "base", namespace = "http://www.mediawiki.org/xml/export-0.10/")
    val base: String = ""

    @XmlElement(name = "generator", namespace = "http://www.mediawiki.org/xml/export-0.10/")
    val generator: String = ""

    @XmlElement(name = "case", namespace = "http://www.mediawiki.org/xml/export-0.10/")
    val _case: String = ""

    @OneToMany(mappedBy = "siteInfo")
    @XmlElementWrapper(name = "namespaces", namespace = "http://www.mediawiki.org/xml/export-0.10/")
    @XmlElement(name = "namespace", namespace = "http://www.mediawiki.org/xml/export-0.10/")
    var namespaces: MutableList<XmlNamespaceTag> = mutableListOf()
}
