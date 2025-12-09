package org.vtinstitute.handler;

import org.vtinstitute.models.Student;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class SAXHandler extends DefaultHandler {
    private List<Student> students = new ArrayList<>();
    private Student student;

    private boolean bIdCard = false;
    private boolean bFirstName = false;
    private boolean bLastName = false;
    private boolean bPhone = false;
    private boolean bEmail = false;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {

        if ("student".equals(qName)) {
            student = new Student();
            String idCard = atts.getValue("idcard");
            student.setIdcard(idCard);
        }
        switch (qName) {
            case "firstname" -> bFirstName = true;
            case "lastname" -> bLastName = true;
            case "phone" -> bPhone = true;
            case "email" -> bEmail = true;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String content = new String(ch, start, length);

        if (bIdCard) {
            student.setIdcard(content);
            bIdCard = false;
        }
        if (bFirstName) {
            student.setFirstname(content);
            bFirstName = false;
        }
        if (bLastName) {
            student.setLastname(content);
            bLastName = false;
        }
        if (bPhone) {
            student.setPhone(content);
            bPhone = false;
        }
        if (bEmail) {
            student.setEmail(content);
            bEmail = false;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if ("student".equals(qName)) {
            students.add(student);
        }
    }

    public List<Student> getStudents() {
        return students;
    }
}
