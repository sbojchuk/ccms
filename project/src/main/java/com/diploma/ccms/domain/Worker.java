package com.diploma.ccms.domain;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
public class Worker implements UserDetails {
    private static final long serialVersionUID = 1148968754347580432L;

    @NotNull
    @Column(unique = true)
    private String login;

    @NotNull
    private String pass;

    @NotNull
    private String name;

    @NotNull
    private String surname;

    private String phone;

    private String privateMail;

    private String street;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date birthday;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date dateHire;

    @ManyToOne
    private WorkerRole roleName;

    @NotNull
    @ManyToOne
    private Region regionName;

    @NotNull
    @ManyToOne
    private Team teamName;

    @NotNull
    @ManyToOne
    private WorkerJobType jobTypeName;

    @Lob
    private byte[] photo;

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return this.pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPrivateMail() {
        return this.privateMail;
    }

    public void setPrivateMail(String privateMail) {
        this.privateMail = privateMail;
    }

    public String getStreet() {
        return this.street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Date getBirthday() {
        return this.birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getDateHire() {
        return this.dateHire;
    }

    public void setDateHire(Date dateHire) {
        this.dateHire = dateHire;
    }

    public WorkerRole getRoleName() {
        return this.roleName;
    }

    public void setRoleName(WorkerRole roleName) {
        this.roleName = roleName;
    }

    public Region getRegionName() {
        return this.regionName;
    }

    public void setRegionName(Region regionName) {
        this.regionName = regionName;
    }

    public Team getTeamName() {
        return this.teamName;
    }

    public void setTeamName(Team teamName) {
        this.teamName = teamName;
    }

    public WorkerJobType getJobTypeName() {
        return this.jobTypeName;
    }

    public void setJobTypeName(WorkerJobType jobTypeName) {
        this.jobTypeName = jobTypeName;
    }

    public byte[] getPhoto() {
        return this.photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    @PersistenceContext
    transient EntityManager entityManager;

    public static final EntityManager entityManager() {
        EntityManager em = new Worker().entityManager;
        if (em == null)
            throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countWorkers() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Worker o", Long.class).getSingleResult();
    }

    public static List<Worker> findAllWorkers() {
        return entityManager().createQuery("SELECT o FROM Worker o", Worker.class).getResultList();
    }

    public static Worker findWorker(Long id) {
        if (id == null)
            return null;
        return entityManager().find(Worker.class, id);
    }

    public static List<Worker> findWorkerEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Worker o", Worker.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Worker attached = Worker.findWorker(this.id);
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
    public Worker merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        Worker merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public static TypedQuery<Worker> findWorkersByLoginEquals(String login) {
        if (login == null || login.length() == 0)
            throw new IllegalArgumentException("The login argument is required");
        EntityManager em = Worker.entityManager();
        TypedQuery<Worker> q = em.createQuery("SELECT o FROM Worker AS o WHERE o.login = :login", Worker.class);
        q.setParameter("login", login);
        return q;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> roles = new HashSet<GrantedAuthority>();
        roles.add(roleName);
        return roles;

    }

    @Override
    public String getPassword() {
        return pass;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
