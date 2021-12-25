package si.fri.rso.charging.services.producers;


import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import si.fri.rso.charging.models.Chargers;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@RequestScoped
public class ChargerBean {

    @Inject
    private EntityManager em;

    @Inject
    private ChargerBean ChargersBean;

    public List<Chargers> getChargers() {

        TypedQuery<Chargers> query = em.createNamedQuery("Charger.getAll", Chargers.class);

        return query.getResultList();

    }

    public Chargers getChargers(String ChargerId) {

        Chargers chargers = em.find(Chargers.class, ChargerId);

        if (chargers == null) {
            throw new NotFoundException();
        }
        return chargers;
    }

    public Chargers createCharger(Chargers charger) {

        try {
            beginTx();
            em.persist(charger);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return charger;
    }

    public Chargers putCharger(String chargerId, Chargers charger) {

        Chargers c = em.find(Chargers.class, chargerId);

        if (c == null) {
            return null;
        }

        try {
            beginTx();
            charger.setId(c.getId());
            charger = em.merge(charger);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return charger;
    }

    public boolean deleteCharger(String chargerId) {

        Chargers charger = em.find(Chargers.class, chargerId);

        if (charger != null) {
            try {
                beginTx();
                em.remove(charger);
                commitTx();
            } catch (Exception e) {
                rollbackTx();
            }
        } else
            return false;

        return true;
    }

    private void beginTx() {
        if (!em.getTransaction().isActive())
            em.getTransaction().begin();
    }

    private void commitTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().commit();
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().rollback();
    }


}
