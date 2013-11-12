package net.gscheibel.hintofnosql.backend.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

/**
 * @author Guillaume Scheibel <guillaume.scheibel@gmail.com>
 */
@Entity
public class Presentation {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id = null;
	@NotNull
	private String title;
	@ManyToMany(fetch = EAGER, cascade = ALL)
	private Set<Conference> conferences = new HashSet<>();

	@ElementCollection(fetch = EAGER)
	private Set<String> comments;

	public Presentation() {
		super();
	}

	public Presentation(String title) {
		this.title = title;
	}

	public Presentation(String title, Long id) {
		this( title );
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String typeName) {
		this.title = typeName;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public Set<Conference> getConferences() {
		return conferences;
	}

	public void setConferences(Set<Conference> vendors) {
		this.conferences = vendors;
	}

	public Set<String> getComments() {
		return comments;
	}

	public void setComments(Set<String> comments) {
		this.comments = comments;
	}

	@Override
	public String toString() {
		String result = getClass().getSimpleName() + " ";
		if ( id != null ) {
			result += "id: " + id;
		}
		return result;
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
			return id.equals( ( (Presentation) that ).id );
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