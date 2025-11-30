package org.vtinstitute.controller;

import org.vtinstitute.connection.Database;
import org.vtinstitute.handler.SAXHandler;
import org.vtinstitute.models.Student;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentsController {
    private SAXParser saxParser = null;
    private Database db = new Database();

    private SAXParser createSaxParser() {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            saxParser = factory.newSAXParser();
        } catch (ParserConfigurationException | SAXException ex) {
            Logger lgr = Logger.getLogger(StudentsController.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

            return saxParser;
        }
        return null;
    }

    private void addStudentDB(Student student) {
        String sql = "INSERT INTO students (idcard, firstname, lastname, phone, email) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = new Database().openConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, student.getIdCard());
            stmt.setString(2, student.getFirstName());
            stmt.setString(3, student.getLastName());
            stmt.setString(4, student.getPhone());
            stmt.setString(5, student.getEmail());

            stmt.executeUpdate();
            System.out.println("Student " + student.getFirstName() + " added.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public boolean studentExists(String idCard) throws SQLException {
        Database db = new Database();

        String sql = "SELECT 1 FROM students WHERE idcard = ?";

        Connection conn = db.openConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, idCard);

        ResultSet rs = stmt.executeQuery();
        conn.close();

        return rs.next();
    }

    private List<Student> parseStundents() {
        var handler = new SAXHandler();
        String fileName = "src/main/resources/Students.xml";
        File xmlDocument = Paths.get(fileName).toFile();

        try {
            SAXParser parser = createSaxParser();
            assert parser != null;
            parser.parse(xmlDocument, handler);

        } catch (SAXException | IOException ex) {

            Logger lgr = Logger.getLogger(StudentsController.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return handler.getStudents();
    }

    // Function that return a Student by a idCard.
    public Student getStudent(String idCard) {
        String sql = "SELECT * FROM students WHERE idcard = ?";
        Student student = null;

        try (Connection conn = db.openConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, idCard);
            var rs = stmt.executeQuery();

            if (rs.next()) {
                student = new Student(
                        rs.getString("idcard"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("phone"),
                        rs.getString("email")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return student;
    }

    public void addStudentsXML() {
        var xsdFile = new File("src/main/resources/Students.xsd");
        String xmlFile = "src/main/resources/Students.xml";
        List<Student> students = new ArrayList<>();

        try {
            Path xmlPath = Paths.get(xmlFile);
            Reader reader = Files.newBufferedReader(xmlPath);

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
            System.err.println("ERROR: XML NOT VALID.");
            ex.printStackTrace();
            students = new ArrayList<>();
        }

        if (!students.isEmpty()) {
            for (Student student : students) {
                addStudentDB(student);
            }
        }

        System.out.println("\nStudents list size: " + students.size());
    }
}
