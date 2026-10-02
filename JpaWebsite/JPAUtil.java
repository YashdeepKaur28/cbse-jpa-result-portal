package my;

import javax.persistence.*;

public class JPAUtil {

    private static EntityManagerFactory EMF;

    static {
        try {
            System.out.println(">>> JPAUtil: loading persistence unit cbsePU...");
            EMF = Persistence.createEntityManagerFactory("cbsePU");
            System.out.println(">>> JPAUtil: EMF created = " + EMF);
        } catch (Throwable ex) {
            System.err.println(">>> JPAUtil: FAILED to create EMF");
            System.err.println(">>> Class: " + ex.getClass().getName());
            System.err.println(">>> Msg  : " + ex.getMessage());
            ex.printStackTrace(System.err);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static EntityManager getEntityManager() {
        return EMF.createEntityManager();
    }
}