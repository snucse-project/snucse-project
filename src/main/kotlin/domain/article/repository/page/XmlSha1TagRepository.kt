package com.wafflestudio.msns.domain.article.repository.page

import com.wafflestudio.msns.domain.article.model.page.XmlSha1Tag
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface XmlSha1TagRepository : JpaRepository<XmlSha1Tag, UUID?>
