package net.gscheibel.hintofnosql.backend.rest;

import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import net.gscheibel.hintofnosql.backend.model.Presentation;
import org.codehaus.jackson.map.ObjectMapper;

import org.hibernate.Query;
import org.hibernate.Session;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.ok;
import static javax.ws.rs.core.Response.status;

/**
 * @author Guillaume Scheibel <guillaume.scheibel@gmail.com>
 */
@Path("/presentation")
@Produces(APPLICATION_JSON)
public class PresentationService {

	@PersistenceContext
	private EntityManager entityManager;
	private ObjectMapper objectMapper = new ObjectMapper();
	@Inject
	private UserTransaction ut;

	private Query getQuery(String queryString) {
		Session session = (Session) entityManager.getDelegate();
		return session.createQuery( queryString );
	}

	@GET
	public List<Presentation> getPresentations(@QueryParam("titleStartsWith") String title) throws Exception {

		Query query;
		if ( title != null && !title.equals( "" ) ) {
			query = getQuery( "FROM Presentation p WHERE p.title like :title" );
			query.setParameter( "title", title + '%' );
		}
		else {
			query = getQuery( "FROM Presentation p" );
		}

		return query.list();
	}

	@GET
	@Path("/{presentationId}")
	public Response getPresentationById(@PathParam("presentationId") Long presentationId) throws Exception {
		Presentation presentation = entityManager.find( Presentation.class, presentationId );
		Response build = status( NOT_FOUND ).build();
		ut.begin();
		if ( presentation != null ) {
			String jsonString = objectMapper.writeValueAsString( presentation );
			build = ok( jsonString ).build();
		}
		ut.commit();
		return build;
	}

	@PUT
	public Response updateVendor(Presentation presentation) {
		Response response;
		try {
			ut.begin();
			entityManager.merge( presentation );
			ut.commit();
			response = ok().build();
		}
		catch ( NotSupportedException e ) {
			try {
				ut.rollback();
				response = status( INTERNAL_SERVER_ERROR ).entity( e.getMessage() ).build();
			}
			catch ( SystemException e1 ) {
				response = status( INTERNAL_SERVER_ERROR ).entity( e1.getMessage() ).build();
			}
		}
		catch ( SystemException | HeuristicRollbackException | RollbackException |
				HeuristicMixedException e ) {
			response = status( INTERNAL_SERVER_ERROR ).entity( e.getMessage() ).build();
		}

		return response;
	}
}
