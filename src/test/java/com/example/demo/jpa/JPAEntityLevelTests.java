package com.example.demo.jpa;

import com.example.entity.Customer;
import com.example.entity.DTHSubsriptions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class JPAEntityLevelTests {

    @Autowired
    private EntityManagerFactory entityManagerFactory;


    /**
     * org.hibernate.cache.internal.StandardQueryCache holds the cached query results.
     * <p>
     * org.hibernate.cache.spi.UpdateTimestampsCache holds timestamps of the most recent updates to queryable tables.
     * These timestamps validate results served from the query cache.
     */
    @Test
    public void testSecondLevelCachingHibernate() {
        CachingProvider provider = Caching.getCachingProvider();
        CacheManager cacheManager = provider.getCacheManager();
        Iterable<String> caches = printAvailableCacheNames(cacheManager);

        printCaches(caches, cacheManager);

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Session session = entityManager.unwrap(Session.class);

        Customer customer = session
                .byId(Customer.class)
                .load(1);
        System.out.println(customer.toString());

        SessionFactory sessionFactory = session.getSessionFactory();
        printCacheStatistics(sessionFactory);

        System.out.println("Action: Removed same customer object from cache");
        System.out.println("---------------------------------------");
        session.evict(customer);
        customer = session
                .byId(Customer.class)
                .load(1);

        printCacheStatistics(sessionFactory);
        //session.getStatistics().getEntityKeys().forEach(System.out::println);
        printCaches(caches, cacheManager);


        //this is just to increase put count in 2nd level cache
        List<DTHSubsriptions> dthSubsriptions = entityManager.
                createQuery("Select dth from DTHSubsriptions dth", DTHSubsriptions.class)
                .setHint("org.hibernate.cacheable", true).getResultList();
        printCacheStatistics(sessionFactory);
        printCaches(caches, cacheManager);

    }

    /**
     * ReF:
     * https://javarevisited.blogspot.com/2017/03/difference-between-first-and-second-level-cache-in-Hibernate.html
     * http://mrbool.com/understanding-the-hibernate-cache-l1-and-l2-in-detail/33437
     * http://mrbool.com/how-to-use-hibernate-query-cache/33439
     */
    @Test
    public void testSecondLevelCacheHitCountHibernate() {
        CachingProvider provider = Caching.getCachingProvider();
        CacheManager cacheManager = provider.getCacheManager();
        Iterable<String> caches = printAvailableCacheNames(cacheManager);


        // this is the hibernate way of creating sessions
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);

        Session session = sessionFactory.openSession();
        // Object is searched for the first time and loaded in Cache L1 and Cache L2
        // hence misscount+1 and putcount+1 for L1 and L2 cache
        Customer customer = session
                .byId(Customer.class)
                .load(1);
        customer.getMobileNumber();
        printCacheStatistics(sessionFactory);

        // We search the object again, it is return by the Cache L1 because it is in the same Session
        // since L2 cache is not queried, its stats remain same
        Customer customer1 = session
                .byId(Customer.class)
                .load(1);
        customer.getMobileNumber();
        printCacheStatistics(sessionFactory);


        printCaches(caches, cacheManager);

        // We clean the object from the Cache L1
        // But this object is still present in L2 cache as we can see in printCaches() method it shows entry for customerId=1 in before
        // and after evict
        session.evict(customer1);
        System.out.println("Removing object from L1 cache?");
        printCaches(caches, cacheManager);

        // We search the object again, it is copied from Cache L2 to Cache L1
        // doubt here is then why misscount of L2 increased if it was already in cache?
        customer1 = session
                .byId(Customer.class)
                .load(1);
        printCaches(caches, cacheManager);
        printCacheStatistics(sessionFactory);
        session.close();


        // We search the object again, but now in a new Session. It is copied from Cache L2 to Cache L1,
        // hence hitcount+1 for L2 cache
        Session newSession = sessionFactory.openSession();
        System.out.println(newSession.isOpen() + ":" + newSession.isDirty());
        customer1 = newSession
                .byId(Customer.class)
                .load(1);
        printCaches(caches, cacheManager);
        printCacheStatistics(sessionFactory);
        newSession.close();
    }

    private void printCacheStatistics(SessionFactory sessionFactory) {
        System.out.println("SecondLevelCacheHitCount : " + sessionFactory.getStatistics().getSecondLevelCacheHitCount());
        System.out.println("SecondLevelCacheMissCount : " + sessionFactory.getStatistics().getSecondLevelCacheMissCount());
        System.out.println("SecondLevelCachePutCount : " + sessionFactory.getStatistics().getSecondLevelCachePutCount());
        System.out.println("-------------------------------------------------");
    }

    private Iterable<String> printAvailableCacheNames(CacheManager cacheManager) {
        Iterable<String> cacheNames = cacheManager.getCacheNames();
        cacheNames.forEach(System.out::println);
        return cacheNames;
    }

    private void printCaches(Iterable<String> caches, CacheManager cacheManager) {
        System.out.println("-------------------------------------------------");
        for (String cache : caches) {
            System.out.println("-- " + cache + "-----------------------------");
            Cache<Object, Object> user = cacheManager.getCache(cache);
            for (Cache.Entry<Object, Object> objectObjectEntry : user) {
                System.out.println(objectObjectEntry.getKey() + ":" + objectObjectEntry.getValue());
            }
        }
        System.out.println("-------------------------------------------------");
    }

}
