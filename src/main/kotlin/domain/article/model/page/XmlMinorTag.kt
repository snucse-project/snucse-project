package com.wafflestudio.msns.domain.article.model.page

import com.wafflestudio.msns.domain.base.BaseEntity
import javax.persistence.Entity
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlRootElement

@Entity(name = "minor")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "minor", namespace = "http://www.mediawiki.org/xml/export-0.10/")
class XmlMinorTag : BaseEntity()
