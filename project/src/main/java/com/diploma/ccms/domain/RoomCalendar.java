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

@Configurable
@Entity




public class RoomCalendar {

    @NotNull
    private String title;

    private String body;

    @Value("false")
    private Boolean readOnly;

    @Value("false")
    private Boolean allDay;

    @ManyToOne
    private Room roomName;

    @ManyToOne
    private Worker WorkerName;

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

	public Room getRoomName() {
        return this.roomName;
    }

	public void setRoomName(Room roomName) {
        this.roomName = roomName;
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

	public static RoomCalendar fromJsonToRoomCalendar(String json) {
        return new JSONDeserializer<RoomCalendar>().use(null, RoomCalendar.class).deserialize(json);
    }

	public static String toJsonArray(Collection<RoomCalendar> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }

	public static Collection<RoomCalendar> fromJsonArrayToRoomCalendars(String json) {
        return new JSONDeserializer<List<RoomCalendar>>().use(null, ArrayList.class).use("values", RoomCalendar.class).deserialize(json);
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new RoomCalendar().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countRoomCalendars() {
        return entityManager().createQuery("SELECT COUNT(o) FROM RoomCalendar o", Long.class).getSingleResult();
    }

	public static List<RoomCalendar> findAllRoomCalendars() {
        return entityManager().createQuery("SELECT o FROM RoomCalendar o", RoomCalendar.class).getResultList();
    }

	public static RoomCalendar findRoomCalendar(Long id) {
        if (id == null) return null;
        return entityManager().find(RoomCalendar.class, id);
    }

	public static List<RoomCalendar> findRoomCalendarEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM RoomCalendar o", RoomCalendar.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            RoomCalendar attached = RoomCalendar.findRoomCalendar(this.id);
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
    public RoomCalendar merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        RoomCalendar merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

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
}
