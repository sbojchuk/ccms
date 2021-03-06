package com.diploma.ccms.domain;

import java.util.Collection;
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

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONSerializer;

@Entity
@Configurable
public class Todo {

    @NotNull
    private String title;

    @NotNull
    private String text;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date start;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date end;

    @Value("false")
    private Boolean viewed;

    @Value("false")
    private Boolean done;

    @ManyToOne
    private Worker reporter;

    @ManyToOne
    private Worker assignee;

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

    public Date getStart() {
        return this.start;
    }

    public void setStart(Date enterDate) {
        this.start = enterDate;
    }

    public Date getEnd() {
        return this.end;
    }

    public void setEnd(Date dueDate) {
        this.end = dueDate;
    }

    public Boolean getViewed() {
        return this.viewed;
    }

    public void setViewed(Boolean viewed) {
        this.viewed = viewed;
    }

    public Boolean getDone() {
        return this.done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public Worker getReporter() {
        return this.reporter;
    }

    public void setReporter(Worker reporter) {
        this.reporter = reporter;
    }

    public Worker getAssignee() {
        return this.assignee;
    }

    public void setAssignee(Worker assignee) {
        this.assignee = assignee;
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
        EntityManager em = new Todo().entityManager;
        if (em == null)
            throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countTodoes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Todo o WHERE o.assignee = :assignee AND o.done = false", Long.class).setParameter("assignee", Worker.getPrincipal()).getSingleResult();
    }

    public static List<Todo> findAllTodoes() {
        return entityManager().createQuery("SELECT o FROM Todo o WHERE o.assignee = :assignee ORDER by o.id DESC", Todo.class).setParameter("assignee", Worker.getPrincipal()).getResultList();
    }

    public static Todo findTodo(Long id) {
        if (id == null)
            return null;
        return entityManager().find(Todo.class, id);
    }

    public static List<Todo> findTodoEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Todo o", Todo.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Todo attached = Todo.findTodo(this.id);
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
    public Todo merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        Todo merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    
    public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
    
    public static String toJsonArray(Collection<Todo> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
    }
}
