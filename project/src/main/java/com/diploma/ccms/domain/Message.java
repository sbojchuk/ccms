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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
public class Message {

    @NotNull
    private String title;

    @NotNull
    @Size(max = 1000000)
    private String text;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date datetime;

    @Value("false")
    private Boolean viewed;

    @ManyToOne
    private Worker fromWorker;

    @ManyToOne
    private Worker toWorker;

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

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDatetime() {
        return this.datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public Boolean getViewed() {
        return this.viewed;
    }

    public void setViewed(Boolean viewed) {
        this.viewed = viewed;
    }

    public Worker getFromWorker() {
        return this.fromWorker;
    }

    public void setFromWorker(Worker fromWorker) {
        this.fromWorker = fromWorker;
    }

    public Worker getToWorker() {
        return this.toWorker;
    }

    public void setToWorker(Worker toWorker) {
        this.toWorker = toWorker;
    }

    @PersistenceContext
    transient EntityManager entityManager;

    public static final EntityManager entityManager() {
        EntityManager em = new Message().entityManager;
        if (em == null)
            throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countMessages() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Message o WHERE o.toWorker = :toWorker and o.viewed = false", Long.class).setParameter("toWorker", Worker.getPrincipal()) .getSingleResult();
    }

    public static List<Message> findAllMessages() {
        return entityManager().createQuery("SELECT o FROM Message o ORDER by o.id DESC", Message.class).getResultList();
    }

    public static Message findMessage(Long id) {
        if (id == null)
            return null;
        return entityManager().find(Message.class, id);
    }

    public static List<Message> findMessageEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Message o", Message.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Message attached = Message.findMessage(this.id);
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
    public Message merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        Message merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    public static TypedQuery<Message> findMessagesByFromWorkerEquals(Worker fromWorker) {
        if (fromWorker == null)
            throw new IllegalArgumentException("The fromWorker argument is required");
        EntityManager em = Message.entityManager();
        TypedQuery<Message> q = em.createQuery("SELECT o FROM Message AS o WHERE o.fromWorker = :fromWorker", Message.class);
        q.setParameter("fromWorker", fromWorker);
        return q;
    }

    public static TypedQuery<Message> findMessagesByTextLike(String text) {
        if (text == null || text.length() == 0)
            throw new IllegalArgumentException("The text argument is required");
        text = text.replace('*', '%');
        if (text.charAt(0) != '%') {
            text = "%" + text;
        }
        if (text.charAt(text.length() - 1) != '%') {
            text = text + "%";
        }
        EntityManager em = Message.entityManager();
        TypedQuery<Message> q = em.createQuery("SELECT o FROM Message AS o WHERE LOWER(o.text) LIKE LOWER(:text)", Message.class);
        q.setParameter("text", text);
        return q;
    }

    public static TypedQuery<Message> findMessagesByTitleLike(String title) {
        if (title == null || title.length() == 0)
            throw new IllegalArgumentException("The title argument is required");
        title = title.replace('*', '%');
        if (title.charAt(0) != '%') {
            title = "%" + title;
        }
        if (title.charAt(title.length() - 1) != '%') {
            title = title + "%";
        }
        EntityManager em = Message.entityManager();
        TypedQuery<Message> q = em.createQuery("SELECT o FROM Message AS o WHERE LOWER(o.title) LIKE LOWER(:title)", Message.class);
        q.setParameter("title", title);
        return q;
    }

    public static TypedQuery<Message> findMessagesByToWorkerEquals(Worker toWorker) {
        if (toWorker == null)
            throw new IllegalArgumentException("The toWorker argument is required");
        EntityManager em = Message.entityManager();
        TypedQuery<Message> q = em.createQuery("SELECT o FROM Message AS o WHERE o.toWorker = :toWorker ORDER by o.id DESC", Message.class);
        q.setParameter("toWorker", toWorker);
        return q;
    }
}
