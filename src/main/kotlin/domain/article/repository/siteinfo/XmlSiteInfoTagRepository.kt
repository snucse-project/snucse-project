package com.wafflestudio.msns.domain.article.repository.siteinfo

import com.wafflestudio.msns.domain.article.model.siteinfo.XmlSiteInfoTag
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface XmlSiteInfoTagRepository : JpaRepository<XmlSiteInfoTag, UUID?>
