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
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
public class BlogComment {

    @NotNull
    private String title;

    @NotNull
    @Size(max = 1000)
    private String body;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date enterDate;

    @NotNull
    @ManyToOne
    private Worker WorkerName;

    @NotNull
    @ManyToOne
    private Blog blogReference;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
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

    public Blog getBlogReference() {
        return this.blogReference;
    }

    public void setBlogReference(Blog blogReference) {
        this.blogReference = blogReference;
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

    @PersistenceContext
    transient EntityManager entityManager;

    public static final EntityManager entityManager() {
        EntityManager em = new BlogComment().entityManager;
        if (em == null)
            throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countBlogComments() {
        return entityManager().createQuery("SELECT COUNT(o) FROM BlogComment o", Long.class).getSingleResult();
    }

    public static List<BlogComment> findAllBlogComments() {
        return entityManager().createQuery("SELECT o FROM BlogComment o", BlogComment.class).getResultList();
    }

    public static BlogComment findBlogComment(Long id) {
        if (id == null)
            return null;
        return entityManager().find(BlogComment.class, id);
    }

    public static List<BlogComment> findBlogCommentEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM BlogComment o", BlogComment.class).setFirstResult(firstResult).setMaxResults(maxResults)
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
            BlogComment attached = BlogComment.findBlogComment(this.id);
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
    public BlogComment merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        BlogComment merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
