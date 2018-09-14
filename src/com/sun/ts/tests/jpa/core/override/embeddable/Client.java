/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package com.sun.ts.tests.jpa.core.override.embeddable;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import java.util.Properties;

public class Client extends PMClientBase {

  private static final Integer BOOKSTORE_ID = 12345;

  private static final String PUBLISHER_NAME = "Sun";

  private static final String PUBLISHER_LOCATION = "Santa Clara";

  private static final int COMPLAINT_NUMBER = 24680;

  private static final String APPLICANT_NAME = "Sunny";

  private static final String APPLICANT_ADDRESS = "Menlo Park";

  private static final Integer COMPLAINT_ID = 9;

  private static final Integer MOVIETICKET_ID = 6;

  private static final String FILM_CODE = "007";

  private static final String FILM_NAME = "Network is Computer";

  private static final Integer BOOK_ID = 5;

  private static final String PUBLISHER1_NAME = "Penguin";

  private static final String PUBLISHER1_STATE = "California";

  public Client() {
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup");
    try {
      super.setup(args, p);
      removeTestData();
    } catch (Exception e) {
      TestUtil.logErr("Exception:test failed ", e);
    }
  }

  /*
   * @testName: testOverrideTransient
   * 
   * @assertion_ids: PERSISTENCE:SPEC:513; PERSISTENCE:SPEC:523;
   * PERSISTENCE:SPEC:548; PERSISTENCE:SPEC:549; PERSISTENCE:SPEC:553;
   * PERSISTENCE:SPEC:556; PERSISTENCE:SPEC:557; PERSISTENCE:SPEC:560;
   * PERSISTENCE:SPEC:1026; PERSISTENCE:SPEC:1061; PERSISTENCE:SPEC:1063;
   * PERSISTENCE:SPEC:1064; PERSISTENCE:SPEC:1067; PERSISTENCE:SPEC:1069;
   * PERSISTENCE:SPEC:1127;PERSISTENCE:SPEC:1130;
   * 
   * @test_Strategy: A field in an entity which is declared as Basic is
   * overriden in orm.xml as Transient.
   */
  public void testOverrideTransient() throws Fault {

    getEntityTransaction().begin();
    Publisher publisher = new Publisher();
    publisher.setName(PUBLISHER_NAME);
    publisher.setLocation(PUBLISHER_LOCATION);
    BookStore bookstore = new BookStore();
    bookstore.setId(BOOKSTORE_ID);
    bookstore.setPublisher(publisher);
    getEntityManager().persist(bookstore);
    getEntityManager().flush();
    try {
      BookStore retrieveBook = getEntityManager().find(BookStore.class,
          BOOKSTORE_ID);
      retrieveBook.setPublisher(new Publisher());
      getEntityManager().refresh(retrieveBook);
      Publisher retrievePublisher = retrieveBook.getPublisher();
      if (retrievePublisher.getName().equals(PUBLISHER_NAME)) {
        if (retrievePublisher.getLocation() == null) {
          TestUtil.logTrace("Test Passed");
        } else {
          throw new Fault("The Location fields was expected to be empty, "
              + "expected Length - null, actual - " + ""
              + retrievePublisher.getLocation());
        }
      } else {
        throw new Fault(
            "Incorrect BookStore object obtained from the" + " database");
      }
    } catch (Exception e) {
      throw new Fault(
          "Exception thrown while testing testOverrideTransient" + e);
    } finally {
      getEntityManager().remove(bookstore);
      getEntityTransaction().commit();

    }
  }
  /*
   * @testName: testOverrideEmbeddable
   * 
   * @assertion_ids: PERSISTENCE:SPEC:513; PERSISTENCE:SPEC:523;
   * PERSISTENCE:SPEC:548; PERSISTENCE:SPEC:549; PERSISTENCE:SPEC:553;
   * PERSISTENCE:SPEC:556; PERSISTENCE:SPEC:557; PERSISTENCE:SPEC:560;
   * PERSISTENCE:SPEC:1026; PERSISTENCE:SPEC:1061; PERSISTENCE:SPEC:1063;
   * PERSISTENCE:SPEC:1064; PERSISTENCE:SPEC:1067; PERSISTENCE:SPEC:1069;
   * PERSISTENCE:SPEC:1127; PERSISTENCE:SPEC:1130;
   * 
   * @test_Strategy: Applicant class is declared as "Embeddable" in orm.xml
   * without using annotation and an entity named Complaint uses Applicant. The
   * following test test applies that by reading from the orm.xml.
   */

  public void testOverrideEmbeddable() throws Fault {

    getEntityTransaction().begin();
    Applicant applicant = new Applicant();
    applicant.setName(APPLICANT_NAME);
    applicant.setAddress(APPLICANT_ADDRESS);
    Complaint complaint = new Complaint();
    complaint.setId(COMPLAINT_ID);
    complaint.setComplaintNumber(COMPLAINT_NUMBER);
    complaint.setApplicant(applicant);
    getEntityManager().persist(complaint);
    getEntityManager().flush();
    try {
      Complaint retrieveComplaint = getEntityManager().find(Complaint.class,
          COMPLAINT_ID);
      Applicant retrieveApplicant = retrieveComplaint.getApplicant();
      if (retrieveComplaint.getComplaintNumber() == COMPLAINT_NUMBER
          && retrieveApplicant.getName().equals(APPLICANT_NAME)
          && retrieveApplicant.getAddress().equals(APPLICANT_ADDRESS)) {
        TestUtil.logTrace("Test Passed");
      } else {
        throw new Fault("Expected Complaint Number COMPLAINT_NUMBER to be"
            + " retrieved; complaint in DB - "
            + retrieveComplaint.getComplaintNumber());
      }
    } catch (Exception e) {
      throw new Fault(
          "Exception thrown while testing testOverrideEmbeddable" + e);
    } finally {
      getEntityManager().remove(complaint);
      getEntityTransaction().commit();
    }
  }
  /*
   * @testName: testOverrideEmbedded
   * 
   * @assertion_ids: PERSISTENCE:SPEC:513; PERSISTENCE:SPEC:523;
   * PERSISTENCE:SPEC:548; PERSISTENCE:SPEC:549; PERSISTENCE:SPEC:553;
   * PERSISTENCE:SPEC:556; PERSISTENCE:SPEC:557; PERSISTENCE:SPEC:560;
   * PERSISTENCE:SPEC:1026; PERSISTENCE:SPEC:1061; PERSISTENCE:SPEC:1063;
   * PERSISTENCE:SPEC:1064; PERSISTENCE:SPEC:1067; PERSISTENCE:SPEC:1069;
   * PERSISTENCE:SPEC:1127; PERSISTENCE:SPEC:1130;
   * 
   * @test_Strategy: Film is an Embeddable class. MovieTicket is an entity has
   * Film embedded in it by defining it as "embedded" in orm.xml instead of
   * using annotation. The following test checks for the above.
   * 
   */

  public void testOverrideEmbedded() throws Fault {

    getEntityTransaction().begin();
    Film film = new Film();
    film.setFilmCode(FILM_CODE);
    film.setFilmName(FILM_NAME);
    MovieTicket ticket = new MovieTicket();
    ticket.setId(MOVIETICKET_ID);
    ticket.setFilm(film);
    getEntityManager().persist(ticket);
    getEntityManager().flush();
    try {
      MovieTicket retrieveTicket = getEntityManager().find(MovieTicket.class,
          MOVIETICKET_ID);
      Film retrieveFilm = retrieveTicket.getFilm();
      if (retrieveFilm.getFilmName().equals(FILM_NAME)
          && retrieveFilm.getFilmCode().equals(FILM_CODE)) {
        TestUtil.logTrace("Test Passed");
      } else {
        throw new Fault("Expected MovieTicket(FILM_NAME)"
            + " to be retrieved; film in DB - " + retrieveFilm.getFilmName());
      }
    } catch (Exception e) {
      throw new Fault(
          "Exception thrown while testing testOverrideEmbedded" + e);
    } finally {
      getEntityManager().remove(ticket);
      getEntityTransaction().commit();
    }
  }

  /*
   * @testName: testMetadataCompleteness
   * 
   * @assertion_ids: PERSISTENCE:SPEC:513; PERSISTENCE:SPEC:523;
   * PERSISTENCE:SPEC:548; PERSISTENCE:SPEC:549; PERSISTENCE:SPEC:553;
   * PERSISTENCE:SPEC:556; PERSISTENCE:SPEC:557; PERSISTENCE:SPEC:560;
   * PERSISTENCE:SPEC:1026; PERSISTENCE:SPEC:1061; PERSISTENCE:SPEC:1063;
   * PERSISTENCE:SPEC:1064; PERSISTENCE:SPEC:1067; PERSISTENCE:SPEC:1069;
   * PERSISTENCE:SPEC:1127; PERSISTENCE:SPEC:1130;
   * 
   * @test_Strategy: Book is an entity and has an embedded field publisher1
   * which is an object of an embeddable class Publisher1. Publisher1 has two
   * fields name that is declared Transient and state that is declared of length
   * 2. The orm.xml has metadata-complete=true. The following test checks for
   * the metadata completeness.
   * 
   */
  public void testMetadataCompleteness() throws Fault {

    getEntityTransaction().begin();
    Book book = new Book();
    Publisher1 publisher1 = new Publisher1();
    book.setId(BOOK_ID);
    publisher1.setName(PUBLISHER1_NAME);
    publisher1.setState(PUBLISHER1_STATE);
    book.setPublisher1(publisher1);
    getEntityManager().persist(book);
    getEntityManager().flush();
    try {
      Book retrieveBook = getEntityManager().find(Book.class, BOOK_ID);
      /*
       * setting Publisher in order to refresh the entity after it has been
       * overriden
       */
      retrieveBook.setPublisher1(new Publisher1());
      getEntityManager().refresh(retrieveBook);
      Publisher1 retrievePublisher1 = retrieveBook.getPublisher1();
      if (retrievePublisher1.getName().equals(PUBLISHER1_NAME)
          && retrievePublisher1.getState().equals(PUBLISHER1_STATE)) {
        TestUtil.logTrace("Test Passed");
      } else {
        throw new Fault("Publisher1's name and state were not persisted "
            + "as expected -- metadata-complete=true is not"
            + " read from orm.xml");
      }
    } catch (Exception e) {
      throw new Fault(
          "Exception thrown while testing testMetadataCompleteness" + e);
    } finally {
      getEntityManager().remove(book);
      getEntityTransaction().commit();
    }
  }

  public void cleanup() throws Fault {
    TestUtil.logTrace("Cleanup data");
    removeTestData();
    TestUtil.logTrace("cleanup complete, calling super.cleanup");
    super.cleanup();
  }

  private void removeTestData() {
    TestUtil.logTrace("removeTestData");
    if (getEntityTransaction().isActive()) {
      getEntityTransaction().rollback();
    }
    try {
      getEntityTransaction().begin();
      getEntityManager().createNativeQuery("DELETE FROM BOOK").executeUpdate();
      getEntityManager().createNativeQuery("DELETE FROM COMPLAINT")
          .executeUpdate();
      getEntityManager().createNativeQuery("DELETE FROM MOVIETICKET")
          .executeUpdate();
      getEntityManager().createNativeQuery("DELETE FROM BOOKSTORE")
          .executeUpdate();

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Exception encountered while removing entities:", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception in removeTestData:", re);
      }
    }
  }
}
