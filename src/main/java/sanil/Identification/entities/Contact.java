package sanil.Identification.entities;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import org.hibernate.annotations.*;
import sanil.Identification.enums.LinkPrecedence;

import java.time.LocalDateTime;

@Entity
@Table(name="contact")
@SQLDelete(sql="UPDATE contact SET deleted_at=current_timestamp where id=?")
@SQLRestriction("deleted_at is null")
public class Contact {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    private Integer id;
    @Column(name="phone_number")
    private String phoneNumber;
    @Column(name="email")
    private String email;
    @ManyToOne
    @JoinColumn(name="linked_id")
    private Contact linkedId;
    @Enumerated(EnumType.STRING)
    private LinkPrecedence linkPrecedence;
    @Column(name="created_at", nullable = false)
    @CreationTimestamp(source = SourceType.DB)
    private LocalDateTime createdAt;
    @Column(name="updated_at", nullable = false)
    @UpdateTimestamp(source = SourceType.DB)
    private LocalDateTime updatedAt;
    @Column(name="deleted_at")
    private LocalDateTime deletedAt = null;

}
