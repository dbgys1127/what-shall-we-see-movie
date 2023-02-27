package shallwe.movie.audit;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class WriterAudit extends TimeAudit {
    @Column(updatable = false)
    private String createdBy;

    @Column
    private String updatedBy;
}
