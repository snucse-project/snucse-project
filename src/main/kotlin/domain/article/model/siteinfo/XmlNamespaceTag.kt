package com.wafflestudio.msns.domain.article.model.siteinfo

import com.wafflestudio.msns.domain.base.BaseEntity
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlValue

@Entity(name = "namespace")
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "namespace", namespace = "http://www.mediawiki.org/xml/export-0.10/")
class XmlNamespaceTag : BaseEntity() {
    @XmlAttribute(name = "key")
    val _key: String = ""

    @XmlAttribute(name = "case")
    val _case: String = ""

    @XmlValue
    val text: String? = null

    @ManyToOne(fetch = FetchType.LAZY, cascade = [])
    @JoinColumn(name = "siteinfo_id", referencedColumnName = "id")
    var siteInfo: XmlSiteInfoTag = XmlSiteInfoTag()
}
