package net.gscheibel.hintofnosql.backend.loader;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.gscheibel.hintofnosql.backend.model.Conference;
import net.gscheibel.hintofnosql.backend.model.Presentation;

import static javax.ejb.TransactionAttributeType.REQUIRES_NEW;

/**
 * @author Guillaume Scheibel <guillaume.scheibel@gmail.com>
 */
@Singleton
@Startup
public class EntityLoader {
	@PersistenceContext
	private EntityManager em;

	@PostConstruct
	@TransactionAttribute(REQUIRES_NEW)
	public void init() {
		Conference devoxxBE = new Conference( "Devoxx BE" );
		Conference softShake = new Conference( "Soft-Shake" );
		Conference devoxxFR = new Conference( "Devoxx FR" );
		em.persist( devoxxBE );
		em.persist( devoxxFR );
		em.persist( softShake );
		em.flush();

		Presentation hintOfNoSQL = new Presentation( "A hint of NoSQL into my JavaEE" );
		Presentation mongoDBRaspberry = new Presentation( "MongoDB and Raspberry Pi" );
		em.persist( hintOfNoSQL );
		em.persist( mongoDBRaspberry );
		em.flush();

		devoxxBE.getPresentations().add( hintOfNoSQL );
		softShake.getPresentations().add( hintOfNoSQL );
		devoxxFR.getPresentations().add( mongoDBRaspberry );
		devoxxBE.getPresentations().add( hintOfNoSQL );

		hintOfNoSQL.getConferences().add( devoxxBE );
		hintOfNoSQL.getConferences().add( softShake );
		hintOfNoSQL.getConferences().add( devoxxFR );
		mongoDBRaspberry.getConferences().add( devoxxFR );


		em.merge( hintOfNoSQL );
		em.merge( mongoDBRaspberry );
		em.merge( devoxxBE );
		em.merge( devoxxFR );
		em.merge( softShake );
	}
}
