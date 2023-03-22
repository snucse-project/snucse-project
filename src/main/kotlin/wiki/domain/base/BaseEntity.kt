package wiki.domain.base

import org.hibernate.annotations.Type
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.UUID
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.Id
import javax.persistence.MappedSuperclass
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlTransient

@XmlTransient
@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
open class BaseEntity(
    @Id
    @Column(name = "id", columnDefinition = "CHAR(36)")
    @Type(type = "uuid-char")
    @XmlAttribute
    open val id: UUID = UUID.randomUUID(),
)
