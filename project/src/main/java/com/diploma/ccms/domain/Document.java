package com.diploma.ccms.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
public class Document {

    @NotNull
    private String title;

    private String description;

    @NotNull
    @Value("false")
    private Boolean shared;

    @NotNull
    @ManyToOne
    private Worker WorkerName;

    @NotNull
    @ManyToOne
    private DocumentCategory category;

    @NotNull
    @Lob
    private byte[] file;

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getShared() {
        return this.shared;
    }

    public void setShared(Boolean shared) {
        this.shared = shared;
    }

    public Worker getWorkerName() {
        return this.WorkerName;
    }

    public void setWorkerName(Worker WorkerName) {
        this.WorkerName = WorkerName;
    }

    public DocumentCategory getCategory() {
        return this.category;
    }

    public void setCategory(DocumentCategory category) {
        this.category = category;
    }

    public byte[] getFile() {
        return this.file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public static TypedQuery<Document> findDocumentsByDescriptionLike(String description) {
        if (description == null || description.length() == 0)
            throw new IllegalArgumentException("The description argument is required");
        description = description.replace('*', '%');
        if (description.charAt(0) != '%') {
            description = "%" + description;
        }
        if (description.charAt(description.length() - 1) != '%') {
            description = description + "%";
        }
        EntityManager em = Document.entityManager();
        TypedQuery<Document> q = em.createQuery("SELECT o FROM Document AS o WHERE LOWER(o.description) LIKE LOWER(:description)", Document.class);
        q.setParameter("description", description);
        return q;
    }

    public static TypedQuery<Document> findDocumentsByTitleLike(String title) {
        if (title == null || title.length() == 0)
            throw new IllegalArgumentException("The title argument is required");
        title = title.replace('*', '%');
        if (title.charAt(0) != '%') {
            title = "%" + title;
        }
        if (title.charAt(title.length() - 1) != '%') {
            title = title + "%";
        }
        EntityManager em = Document.entityManager();
        TypedQuery<Document> q = em.createQuery("SELECT o FROM Document AS o WHERE LOWER(o.title) LIKE LOWER(:title)", Document.class);
        q.setParameter("title", title);
        return q;
    }

    @PersistenceContext
    transient EntityManager entityManager;

    public static final EntityManager entityManager() {
        EntityManager em = new Document().entityManager;
        if (em == null)
            throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countDocuments() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Document o", Long.class).getSingleResult();
    }

    public static List<Document> findAllDocuments() {
        return entityManager().createQuery("SELECT o FROM Document o", Document.class).getResultList();
    }

    public static Document findDocument(Long id) {
        if (id == null)
            return null;
        return entityManager().find(Document.class, id);
    }

    public static List<Document> findDocumentEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Document o", Document.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Document attached = Document.findDocument(this.id);
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
    public Document merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        Document merged = this.entityManager.merge(this);
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
}
