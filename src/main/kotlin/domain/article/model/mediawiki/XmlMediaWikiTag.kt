package com.wafflestudio.msns.domain.article.model.mediawiki

import com.wafflestudio.msns.domain.article.model.page.XmlPageTag
import com.wafflestudio.msns.domain.article.model.siteinfo.XmlSiteInfoTag
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "mediawiki", namespace = "http://www.mediawiki.org/xml/export-0.10/")
class XmlMediaWikiTag {
    @XmlElement(name = "siteinfo", namespace = "http://www.mediawiki.org/xml/export-0.10/")
    val siteInfo: XmlSiteInfoTag = XmlSiteInfoTag()

    @XmlElement(name = "page", namespace = "http://www.mediawiki.org/xml/export-0.10/")
    val pages: MutableList<XmlPageTag> = mutableListOf()
}
