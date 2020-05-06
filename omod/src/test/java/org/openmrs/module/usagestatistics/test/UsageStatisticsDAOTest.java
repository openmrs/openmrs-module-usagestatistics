package org.openmrs.module.usagestatistics.test;

import junit.framework.Assert;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.hibernate.classic.Session;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Patient;
import org.openmrs.module.usagestatistics.db.hibernate.HibernateUsageStatisticsDAO;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class UsageStatisticsDAOTest {

    private HibernateUsageStatisticsDAO usageStatisticsDAO;

    private DbSessionFactory mockSessionFactory;

    private DbSession mockSession;

    private SQLQuery mockSQLQuery;

    @Before
    public void setup() {
        usageStatisticsDAO = new HibernateUsageStatisticsDAO();

        mockSessionFactory = mock(DbSessionFactory.class);
        mockSession = mock(DbSession.class);
        mockSQLQuery = mock(SQLQuery.class);

        usageStatisticsDAO.setSessionFactory(mockSessionFactory);
        when(mockSessionFactory.getCurrentSession()).thenReturn(mockSession);

    }


    @Test
    public void testIfPatientIsVoidedShouldHandleNumberResult() {
        Number result = new Integer(1);
        Patient patient = new Patient(1);

        when(mockSession.createSQLQuery("SELECT voided FROM patient WHERE patient_id = 1;")).thenReturn(mockSQLQuery);
        when(mockSQLQuery.uniqueResult()).thenReturn(result);

        Assert.assertTrue(usageStatisticsDAO.isPatientVoidedInDatabase(patient));
    }

    @Test
    public void testIfPatientIsVoidedShouldHandleBooleanResult() {
        Boolean result = true;
        Patient patient = new Patient(1);

        when(mockSession.createSQLQuery("SELECT voided FROM patient WHERE patient_id = 1;")).thenReturn(mockSQLQuery);
        when(mockSQLQuery.uniqueResult()).thenReturn(result);

        Assert.assertTrue(usageStatisticsDAO.isPatientVoidedInDatabase(patient));
    }


}
