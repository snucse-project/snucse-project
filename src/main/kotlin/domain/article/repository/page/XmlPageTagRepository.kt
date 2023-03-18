package com.wafflestudio.msns.domain.article.repository.page

import com.wafflestudio.msns.domain.article.model.page.XmlPageTag
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface XmlPageTagRepository : JpaRepository<XmlPageTag, UUID?>
