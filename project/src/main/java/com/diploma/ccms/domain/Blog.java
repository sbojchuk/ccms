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
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
public class Blog {

    @NotNull
    private String title;

    @NotNull
    @Size(max = 1000000)
    private String body;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date enterDate;

    @NotNull
    @ManyToOne
    private Worker WorkerName;

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
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

    @PersistenceContext
    transient EntityManager entityManager;

    public static final EntityManager entityManager() {
        EntityManager em = new Blog().entityManager;
        if (em == null)
            throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countBlogs() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Blog o", Long.class).getSingleResult();
    }

    public static List<Blog> findAllBlogs() {
        return entityManager().createQuery("SELECT o FROM Blog o", Blog.class).getResultList();
    }

    public static Blog findBlog(Long id) {
        if (id == null)
            return null;
        return entityManager().find(Blog.class, id);
    }

    public static List<Blog> findBlogEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Blog o", Blog.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Blog attached = Blog.findBlog(this.id);
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
    public Blog merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        Blog merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

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

    public static TypedQuery<Blog> findBlogsByBodyLike(String body) {
        if (body == null || body.length() == 0)
            throw new IllegalArgumentException("The body argument is required");
        body = body.replace('*', '%');
        if (body.charAt(0) != '%') {
            body = "%" + body;
        }
        if (body.charAt(body.length() - 1) != '%') {
            body = body + "%";
        }
        EntityManager em = Blog.entityManager();
        TypedQuery<Blog> q = em.createQuery("SELECT o FROM Blog AS o WHERE LOWER(o.body) LIKE LOWER(:body)", Blog.class);
        q.setParameter("body", body);
        return q;
    }

    public static TypedQuery<Blog> findBlogsByTitleLike(String title) {
        if (title == null || title.length() == 0)
            throw new IllegalArgumentException("The title argument is required");
        title = title.replace('*', '%');
        if (title.charAt(0) != '%') {
            title = "%" + title;
        }
        if (title.charAt(title.length() - 1) != '%') {
            title = title + "%";
        }
        EntityManager em = Blog.entityManager();
        TypedQuery<Blog> q = em.createQuery("SELECT o FROM Blog AS o WHERE LOWER(o.title) LIKE LOWER(:title)", Blog.class);
        q.setParameter("title", title);
        return q;
    }
}
