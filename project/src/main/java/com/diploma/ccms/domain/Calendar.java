package com.diploma.ccms.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
@Configurable
public class Calendar {

    @NotNull
    private String title;

    @Value("false")
    private Boolean readOnly;

    @Value("false")
    private Boolean allDay;

    @ManyToOne
    private Worker WorkerName;

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
        EntityManager em = new Calendar().entityManager;
        if (em == null)
            throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countCalendars() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Calendar o", Long.class).getSingleResult();
    }

    public static List<Calendar> findAllCalendars() {
        return entityManager().createQuery("SELECT o FROM Calendar o", Calendar.class).getResultList();
    }

    public static Calendar findCalendar(Long id) {
        if (id == null)
            return null;
        return entityManager().find(Calendar.class, id);
    }

    public static List<Calendar> findCalendarEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Calendar o", Calendar.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Calendar attached = Calendar.findCalendar(this.id);
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
    public Calendar merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        Calendar merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getReadOnly() {
        return this.readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    public Boolean getAllDay() {
        return this.allDay;
    }

    public void setAllDay(Boolean allDay) {
        this.allDay = allDay;
    }

    public Worker getWorkerName() {
        return this.WorkerName;
    }

    public void setWorkerName(Worker WorkerName) {
        this.WorkerName = WorkerName;
    }

    public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }

    public static Calendar fromJsonToCalendar(String json) {
        return new JSONDeserializer<Calendar>().use(null, Calendar.class).deserialize(json);
    }

    public static String toJsonArray(Collection<Calendar> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }

    public static Collection<Calendar> fromJsonArrayToCalendars(String json) {
        return new JSONDeserializer<List<Calendar>>().use(null, ArrayList.class).use("values", Calendar.class).deserialize(json);
    }
}
