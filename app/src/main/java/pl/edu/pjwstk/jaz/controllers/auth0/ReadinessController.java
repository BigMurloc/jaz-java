package pl.edu.pjwstk.jaz.controllers.auth0;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pjwstk.jaz.repositories.entities.Test1Entity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@RestController
public class ReadinessController {
    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/auth0/is-ready")
    @Transactional
    public void readinessTest() {
        var entity = new Test1Entity();
        entity.setName("sdavsda");
        entityManager.persist(entity);
    }
}
