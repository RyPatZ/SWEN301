package test.nz.ac.wgtn.swen301.assignment1;

import nz.ac.wgtn.swen301.assignment1.StudentManager;
import nz.ac.wgtn.swen301.studentdb.Degree;
import nz.ac.wgtn.swen301.studentdb.NoSuchRecordException;
import nz.ac.wgtn.swen301.studentdb.Student;
import nz.ac.wgtn.swen301.studentdb.StudentDB;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLDataException;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


public class TestStudentManager {

    // DO NOT REMOVE THE FOLLOWING -- THIS WILL ENSURE THAT THE DATABASE IS AVAILABLE
    // AND IN ITS INITIAL STATE BEFORE EACH TEST RUNS
    @Before
    public  void init () {
        StudentDB.init();
    }
    // DO NOT REMOVE BLOCK ENDS HERE

    @Test
    public void dummyTest() throws Exception {
        Student student = new StudentManager().readStudent("id42");
        // THIS WILL INITIALLY FAIL !!
        assertNotNull(student);
    }

    @Test
    public void readStudentTest1() throws Exception {
        Student student = new StudentManager().readStudent("id42");
        // Check if the id are the same
        assert(student.getId()=="id42");
    }

    @Test
    public void readStudentTest2() throws Exception {
        Student student = new StudentManager().readStudent("id9999");
        // Check if the id are the same
        assert(student.getId()=="id9999");
    }

    @Test
    public void readStudentTest3() throws Exception {
        Student student = new StudentManager().readStudent("id0");
        // Check if the id are the same
        assert(student.getId()=="id0");
        assert(student.getFirstName().equals("James"));
        assert(student.getName().equals("Smith"));
        assert(student.getDegree().getId().equals("deg0"));
    }

    //if no record with such an id exists in the database, should throw exception
    @Test(expected = NoSuchRecordException.class)
    public void readStudentTest4() throws Exception {
        Student student = new StudentManager().readStudent("id10000");
        assertNull(student);
    }

    @Test
    public void readDegreeTest1() throws Exception {
        Degree degree = new StudentManager().readDegree("deg0");
        // Check if the degree name are the same
        assert (degree.getId().equals("deg0"));
        assert(degree.getName().equals("BSc Computer Science"));
    }

    @Test
    public void readDegreeTest2() throws Exception {
        Degree degree = new StudentManager().readDegree("deg9");
        // Check if the degree name are the same
        assert (degree.getId().equals("deg9"));
        assert(degree.getName().equals("BCom Marketing"));
    }

    //if no record with such an id exists in the database, should throw exception
    @Test(expected = NoSuchRecordException.class)
    public void readDegreeTest3() throws Exception {
        Degree degree = new StudentManager().readDegree("deg10");
        assertNull(degree);
    }

    //if no record with such an id exists in the database, should throw exception
    @Test(expected = NoSuchRecordException.class)
    public void deleteTest1() throws Exception {
        String id = "id42";
        Student student = new StudentManager().readStudent(id);
        StudentManager studentManager= new StudentManager();
        studentManager.delete(student);
        // Check if the student has been deleted
        assertNull(studentManager.readStudent(id));
    }

    @Test
    public void updateTest1() throws Exception {
        String id = "id42";
        //create a new student for testing
        Student newStudent = new Student(id,"New Stud","New",StudentManager.readDegree("deg1"));
        StudentManager studentManager= new StudentManager();
        studentManager.update(newStudent);
        // Check if the new student has been updated
        assert(studentManager.readStudent(id).getName().equals("New Stud"));
        assert(studentManager.readStudent(id).getFirstName().equals("New"));
        assert(studentManager.readStudent(id).getDegree().getId().equals("deg1"));
    }

    @Test
    public void updateTest2() throws Exception {
        String id = "id42";
        StudentManager studentManager= new StudentManager();
        Student oldStudent = studentManager.readStudent(id);
        Student newStudent = new Student(id,"New Student","New",StudentManager.readDegree("deg1"));
        studentManager.update(newStudent);
        // the name is longer than 10 characters so the student should be the same
        assert(studentManager.readStudent(id).getName().equals(oldStudent.getName()));
    }

    @Test
    public void createStudentTest1() throws Exception {
        StudentManager studentManager= new StudentManager();
        //create a new student for testing
        Student student=studentManager.createStudent("New Stu","New",StudentManager.readDegree("deg1"));
        String id = student.getId();
        System.out.println(id);
        // Check if the student has been deleted
        assert(studentManager.readStudent(id).getName().equals("New Stu"));
        assert(studentManager.readStudent(id).getFirstName().equals("New"));
        assert(studentManager.readStudent(id).getDegree().getId().equals("deg1"));
    }

    @Test(expected = NoSuchRecordException.class)
    public void createStudentTest2() throws Exception {
        StudentManager studentManager= new StudentManager();
        Student student=studentManager.createStudent("New Student","New",StudentManager.readDegree("deg1"));
        String id = student.getId();
        // the name is longer than 10 characters so the student should not be created
        assertNull(studentManager.readStudent(id));
    }

    @Test
    public void getAllStudentIDsTest1() throws Exception{
        StudentManager studentManager= new StudentManager();
        List<String> IDs = (List<String>) studentManager.getAllStudentIds();
        //check if all ID have been recorded
        assert (IDs.size()==10000);
        assert (IDs.get(0).equals(studentManager.readStudent("id0").getId()));
    }

    @Test
    public void getAllDegreeIDsTest1() throws Exception{
        StudentManager studentManager= new StudentManager();
        List<String> IDs = (List<String>) studentManager.getAllDegreeIds();
        //check if all ID have been recorded
        assert (IDs.size()==10);
    }

    @Test
    public void getAllDegreeIDsTest2() throws Exception{
        StudentManager studentManager= new StudentManager();
        List<String> IDs = (List<String>) studentManager.getAllDegreeIds();
        for(int i=0; i<10;i++) {
            String id = "deg" + Integer.toString(i);
            assert (IDs.get(i).equals(studentManager.readDegree(id).getId()));
        }
    }

    @Test
    public void testPerformance() throws Exception{
        long start = System.currentTimeMillis();
        for(int i = 0; i<1000; i++) {
            String id = "id" + Integer.toString(i);
            StudentManager.readStudent(id);
        }
        long end = System.currentTimeMillis();
        long time = end - start;
        System.out.println(time);
        //worked fine with the lab computer, my own conputer is bit slow
        assert(time < 1000);
    }
}
