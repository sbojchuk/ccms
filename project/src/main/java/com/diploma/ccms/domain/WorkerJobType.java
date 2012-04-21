package com.diploma.ccms.domain;

import java.io.Serializable;
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
import org.springframework.transaction.annotation.Transactional;

import com.diploma.ccms.domain.service.DomainUserIntarface;

@Entity
@Configurable
public class WorkerJobType implements Serializable, DomainUserIntarface{

    /**
     * 
     */
    private static final long serialVersionUID = -3422647717132957909L;

    @NotNull
    @Column(unique = true)
    private String jobTypeName;

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @PersistenceContext
    transient EntityManager entityManager;

    public static final EntityManager entityManager() {
        EntityManager em = new WorkerJobType().entityManager;
        if (em == null)
            throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countWorkerJobTypes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM WorkerJobType o", Long.class).getSingleResult();
    }

    public static List<WorkerJobType> findAllWorkerJobTypes() {
        return entityManager().createQuery("SELECT o FROM WorkerJobType o", WorkerJobType.class).getResultList();
    }

    public static WorkerJobType findWorkerJobType(Long id) {
        if (id == null)
            return null;
        return entityManager().find(WorkerJobType.class, id);
    }

    public static List<WorkerJobType> findWorkerJobTypeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM WorkerJobType o", WorkerJobType.class).setFirstResult(firstResult).setMaxResults(maxResults)
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
            WorkerJobType attached = WorkerJobType.findWorkerJobType(this.id);
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
    public WorkerJobType merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        WorkerJobType merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

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

    public String getJobTypeName() {
        return this.jobTypeName;
    }

    public void setJobTypeName(String jobTypeName) {
        this.jobTypeName = jobTypeName;
    }

    @Override
    public String getUiValue() {
        return jobTypeName;
    }
}
