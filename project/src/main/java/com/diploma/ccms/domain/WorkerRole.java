package com.diploma.ccms.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.transaction.annotation.Transactional;

import com.diploma.ccms.domain.service.DomainUserIntarface;


//TODO move in future to enum types, not to DB
@Entity
@Configurable
public class WorkerRole implements GrantedAuthority, DomainUserIntarface {
    private static final long serialVersionUID = 8267682475527493178L;

    @NotNull
    @Column(unique = true)
    private String roleName;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Version
    @Column(name = "version")
    private Integer version;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @PersistenceContext
    transient EntityManager entityManager;

    public static final EntityManager entityManager() {
        EntityManager em = new WorkerRole().entityManager;
        if (em == null)
            throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countWorkerRoles() {
        return entityManager().createQuery("SELECT COUNT(o) FROM WorkerRole o", Long.class).getSingleResult();
    }

    public static List<WorkerRole> findAllWorkerRoles() {
        return entityManager().createQuery("SELECT o FROM WorkerRole o", WorkerRole.class).getResultList();
    }

    public static WorkerRole findWorkerRole(Long id) {
        if (id == null)
            return null;
        return entityManager().find(WorkerRole.class, id);
    }

    public static List<WorkerRole> findWorkerRoleEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM WorkerRole o", WorkerRole.class).setFirstResult(firstResult).setMaxResults(maxResults)
                .getResultList();
    }

    @Transactional
    public void persist() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

    @Transactional
    public void remove() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            WorkerRole attached = WorkerRole.findWorkerRole(this.id);
            this.entityManager.remove(attached);
        }
    }

    @Transactional
    public void flush() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        this.entityManager.flush();
    }

    @Transactional
    public void clear() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        this.entityManager.clear();
    }

    @Transactional
    public WorkerRole merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        WorkerRole merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String getAuthority() {
        return roleName;
    }
    
    public String getUiValue(){
        return roleName;
    }
}
