package com.diploma.ccms.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity



public class Wiki {

    @NotNull
    private String title;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date enterDate;

    @NotNull
    @ManyToOne
    private Worker WorkerName;

    @NotNull
    @ManyToOne
    private WikiCategory category;

	public static TypedQuery<Wiki> findWikisByCategory(WikiCategory category) {
        if (category == null) throw new IllegalArgumentException("The category argument is required");
        EntityManager em = Wiki.entityManager();
        TypedQuery<Wiki> q = em.createQuery("SELECT o FROM Wiki AS o WHERE o.category = :category", Wiki.class);
        q.setParameter("category", category);
        return q;
    }

	public static TypedQuery<Wiki> findWikisByTitleLike(String title) {
        if (title == null || title.length() == 0) throw new IllegalArgumentException("The title argument is required");
        title = title.replace('*', '%');
        if (title.charAt(0) != '%') {
            title = "%" + title;
        }
        if (title.charAt(title.length() - 1) != '%') {
            title = title + "%";
        }
        EntityManager em = Wiki.entityManager();
        TypedQuery<Wiki> q = em.createQuery("SELECT o FROM Wiki AS o WHERE LOWER(o.title) LIKE LOWER(:title)", Wiki.class);
        q.setParameter("title", title);
        return q;
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new Wiki().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countWikis() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Wiki o", Long.class).getSingleResult();
    }

	public static List<Wiki> findAllWikis() {
        return entityManager().createQuery("SELECT o FROM Wiki o", Wiki.class).getResultList();
    }

	public static Wiki findWiki(Long id) {
        if (id == null) return null;
        return entityManager().find(Wiki.class, id);
    }

	public static List<Wiki> findWikiEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Wiki o", Wiki.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Wiki attached = Wiki.findWiki(this.id);
            this.entityManager.remove(attached);
        }
    }

	@Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

	@Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }

	@Transactional
    public Wiki merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Wiki merged = this.entityManager.merge(this);
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

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String getTitle() {
        return this.title;
    }

	public void setTitle(String title) {
        this.title = title;
    }

	public Date getEnterDate() {
        return this.enterDate;
    }

	public void setEnterDate(Date enterDate) {
        this.enterDate = enterDate;
    }

	public Worker getWorkerName() {
        return this.WorkerName;
    }

	public void setWorkerName(Worker WorkerName) {
        this.WorkerName = WorkerName;
    }

	public WikiCategory getCategory() {
        return this.category;
    }

	public void setCategory(WikiCategory category) {
        this.category = category;
    }
}
