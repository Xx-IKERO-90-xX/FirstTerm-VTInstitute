package org.vtinstitute.controller;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.vtinstitute.handler.SAXHandler;
import org.vtinstitute.models.Student;
import org.vtinstitute.tools.HibernateUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class StudentsController {
    private SAXParser saxParser = null;
    private String xmlPath = "src/main/resources/Students.xml";
    private String xsdPath = "src/main/resources/Students.xsd";
    private LogsController logsController = new LogsController();

    // Function that creates the SAX parser.
    private SAXParser createSaxParser() {
        try {
            logsController.logInfo("Creating SAXParser");

            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(true);
            return factory.newSAXParser();
        } catch (ParserConfigurationException | SAXException ex) {
            logsController.logError(ex.getMessage());
            System.err.println("There was an error creating SAXParser");
            return saxParser;
        }
    }

    // Function that adds Students to a DB
    private void addStudentDB(Student student) {
        Transaction tx = null;

        try (Session session = HibernateUtils.getSessionFactory().openSession() ) {
            logsController.logInfo("Adding student " + student.getIdcard() + " to DB");
            tx = session.beginTransaction();
            session.persist(student);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            logsController.logError(e.getMessage());
            System.err.println("There was an error adding student " + student.getIdcard() + " to DB");
        }
    }

    // Function that checks if a Student exists in the DB.
    public boolean studentExists(String idCard) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            logsController.logInfo("Checking if student " + idCard + " exists");
            return session.createQuery(
                    "FROM Student s WHERE s.idcard = :idcard", Student.class)
                    .setParameter("idcard", idCard)
                    .uniqueResultOptional()
                    .isPresent();
        } catch (Exception e) {
            logsController.logError(e.getMessage());
            System.err.println("There was an error checking student " + idCard + " exists");
            return false;
        }
    }

    private List<Student> parseStundents() {
        var handler = new SAXHandler();
        File xmlDocument = Paths.get(xmlPath).toFile();

        if (!xmlDocument.exists()) {
            throw new RuntimeException("ERROR: XML NOT FOUND at " + xmlDocument.getAbsolutePath());
        }

        try {
            logsController.logInfo("Parsing XML");
            SAXParser parser = createSaxParser();

            if (parser == null) {
                throw new RuntimeException("ERROR: SAXParser could not be created");
            }
            parser.parse(xmlDocument, handler);

        } catch (SAXException | IOException ex) {
            logsController.logError(ex.getMessage());
            System.err.println("There was an error parsing XML");
        }
        return handler.getStudents();
    }


    public void addStudentsXML() {
        var xsdFile = new File(xsdPath);
        List<Student> students = new ArrayList<>();

        try {
            logsController.logInfo("Adding students to XML");
            Path xmlPathFile = Paths.get(xmlPath);
            Reader reader = Files.newBufferedReader(xmlPathFile);

            String schemaLang = XMLConstants.W3C_XML_SCHEMA_NS_URI;
            SchemaFactory factory = SchemaFactory.newInstance(schemaLang);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            Schema schema = factory.newSchema(xsdFile);

            Validator validator = schema.newValidator();

            var source = new SAXSource(new InputSource(reader));
            validator.validate(source);

            System.out.println("The document was validated OK");

            students = parseStundents();
        } catch (Exception ex) {
            logsController.logError(ex.getMessage());
            System.err.println("ERROR: XML NOT VALID.");
            students = new ArrayList<>();
        }

        if (!students.isEmpty()) {
            for (Student student : students) {
                try {
                    addStudentDB(student);
                } catch (Exception e) {
                    System.out.println("Skipping Student " + student.getIdcard() + " because exists.");
                }
            }
        }
        System.out.println("\nStudents list size: " + students.size());
    }

    // Function that gives a Student by idCard.
    public Student getStudent(String idCard) {
        Session session = HibernateUtils.getSession();
        try {
            logsController.logInfo("Getting student " + idCard);
            String hql = "FROM Student s WHERE s.idcard = :idcard";
            return session.createQuery(hql, Student.class)
                    .setParameter("idcard", idCard)
                    .getSingleResult();
        }catch (Exception ex) {
            logsController.logError(ex.getMessage());
            System.err.println("There was an error getting student " + idCard);
            return null;
        } finally {
            session.close();
        }

    }
}
