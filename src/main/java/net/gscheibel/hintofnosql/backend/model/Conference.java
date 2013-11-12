package net.gscheibel.hintofnosql.backend.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @author Guillaume Scheibel <guillaume.scheibel@gmail.com>
 */
@Entity
public class Conference implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@NotNull
	private String name;
	@ManyToMany(mappedBy = "conferences")
	@JsonIgnore
	private Set<Presentation> presentations = new HashSet<>();

	public Conference() {
		super();
	}

	public Conference(String name) {
		this.name = name;
	}

	public Conference(String name, Long id) {
		this( name );
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public Set<Presentation> getPresentations() {
		return presentations;
	}

	public void setPresentations(Set<Presentation> presentation) {
		this.presentations = presentation;
	}

	@Override
	public String toString() {
		return "Conference{" +
				"id=" + id +
				", name='" + name + '\'' +
				", number of presentations=" + presentations.size() +
				'}';
	}

	@Override
	public boolean equals(Object that) {
		if ( this == that ) {
			return true;
		}
		if ( that == null ) {
			return false;
		}
		if ( getClass() != that.getClass() ) {
			return false;
		}
		if ( id != null ) {
			return id.equals( ( (Conference) that ).id );
		}
		return super.equals( that );
	}

	@Override
	public int hashCode() {
		if ( id != null ) {
			return id.hashCode();
		}
		return super.hashCode();
	}
}