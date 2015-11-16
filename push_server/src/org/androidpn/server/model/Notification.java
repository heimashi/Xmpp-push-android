package org.androidpn.server.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "notification")
public class Notification implements Serializable{

	private static final long serialVersionUID = -8185019956659917022L;

	public Notification() {
	}

	public Notification(String uuid, String title, String message, String uri, String apiKey) {
		super();
		this.uuid = uuid;
		this.title = title;
		this.message = message;
		this.uri = uri;
		this.apiKey = apiKey;
	}

	@Id
	@Column(name="no_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long no_id;
	
	@Column(name = "uuid", nullable = false, length = 128, unique = true)
	private String uuid;
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@ManyToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name="user_notification",
            joinColumns=@JoinColumn(name="user_id"),
            inverseJoinColumns=@JoinColumn(name="nitify_id")
    )
	//@ManyToMany(targetEntity = User.class, fetch = FetchType.EAGER)
	//@JoinTable(name = "user_notification", joinColumns = @JoinColumn(name = "nitify_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<User> users = new HashSet<User>();
	
	@Column(name = "title", length = 128)
	private String title;
	
	@Column(name = "message", length = 2560)
	private String message;
	
	@Column(name = "uri", length = 128)
	private String uri;
	
	@Column(name = "apiKey", length = 128)
	private String apiKey;

	public Long getId() {
		return no_id;
	}

	public void setId(Long no_id) {
		this.no_id = no_id;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	@Override
	public String toString() {
		return "Notification [users=" + users + ", title=" + title
				+ ", message=" + message + ", uri=" + uri + ", apiKey="
				+ apiKey + "]";
	}

}
