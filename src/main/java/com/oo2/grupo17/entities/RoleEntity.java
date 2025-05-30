package com.oo2.grupo17.entities;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_role")
    @Setter(AccessLevel.NONE)
    private Integer id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "name_role", nullable = false, length = 80, unique = true)
    private RoleType type;

    @Column(name = "create_at_role")
    @CreationTimestamp
    private Timestamp createAt;

    @Column(name = "update_at_role")
    @UpdateTimestamp
    private Timestamp updateAt;

    public RoleEntity(@NotNull RoleType type){
        this.type = type;
    }
}
