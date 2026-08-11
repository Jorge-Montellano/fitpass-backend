package com.fitpass.membership.entity;

import com.fitpass.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import com.fitpass.common.entity.Audit;

@Entity
@Table(name = "user_memberships")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMembership extends Audit{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membership_id", nullable = false)
    private Membership membership;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

//    0 = PENDING
//    1 = ACTIVE
//    2 = EXPIRED
//    3 = CANCELLED
    @Column(nullable = false)
    private Integer status;

}