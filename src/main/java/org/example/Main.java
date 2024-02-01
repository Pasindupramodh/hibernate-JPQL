package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.example.entity.Product;
import org.example.persistence.PersistenceUnitInfo;
import org.hibernate.jpa.HibernatePersistenceProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {

        Map<String, String> props = new HashMap<>();

        props.put("hibernate.show_sql", "true");
        props.put("hibernate.hbm2ddl.auto", "none");


//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-persistence-unit");
        EntityManagerFactory emf = new HibernatePersistenceProvider()
                .createContainerEntityManagerFactory(new PersistenceUnitInfo(), props);
        EntityManager entityManager = emf.createEntityManager();//represent the context
        try {
            entityManager.getTransaction().begin();

            /*

            --------select query--------
            String jpql = "select p from Product p where price > 500";

            //never space after :
            //String jpql = "select p from Product p where price > :price and name like :name";
            TypedQuery<Product> q = entityManager.createQuery(jpql, Product.class);
            q.setParameter("price",500);
            q.setParameter("name","%b%");

            List<Product> products = q.getResultList();

            for(Product product: products){
                System.out.println(product);
            }

            //never filter by getting stream do filtering in query

            Stream<Product> resultStream = q.getResultStream();

            --------------------------------------

             */

            /*
            String jpql = "SELECT AVG(p.price) FROM Product p";
            TypedQuery<Double> q = entityManager.createQuery(jpql, Double.class);

            Double avg = q.getSingleResult();

            System.out.println(avg);
            */

            /*
            String jpql = "SELECT COUNT (p) FROM Product p";//AVG,SUM,MIN,MAX...
            //count comes as long
            TypedQuery<Long> q = entityManager.createQuery(jpql, Long.class);

            long count = q.getSingleResult();

            System.out.println(count);
            */

            /*
            String jpql = """
                SELECT p.name,AVG(p.price)
                FROM Product p group by p.name
                """;
            TypedQuery<Object[]> q = entityManager.createQuery(jpql, Object[].class);

            q.getResultList().forEach(objects -> {
                System.out.println(objects[0]+" "+objects[1]);
            });

            */

            //if nothing in database returns NoResultException
            String jpql = "SELECT p FROM Product p where p.name like  'cobol'";
            TypedQuery<Product> q = entityManager.createQuery(jpql, Product.class);

            Optional<Product> p;
            try {
                p = Optional.of(q.getSingleResult());
            }catch (NoResultException e){
                p = Optional.empty();
                System.out.println();
            }

            p.ifPresentOrElse(
                    System.out::println,
                    ()->System.out.println("No result")
            );

            entityManager.getTransaction().commit();
        } finally {
            entityManager.close();
        }
    }
}