package nz.ac.wgtn.swen301.assignment1;

import nz.ac.wgtn.swen301.studentdb.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * A student managers providing basic CRUD operations for instances of Student, and a read operation for instances of Degree.
 * @author jens dietrich
 */
public class StudentManager {

    public static NoSuchRecordException noSuchRecordException;

    // DO NOT REMOVE THE FOLLOWING -- THIS WILL ENSURE THAT THE DATABASE IS AVAILABLE
    // AND THE APPLICATION CAN CONNECT TO IT WITH JDBC
    static {
        StudentDB.init();
    }
    // DO NOT REMOVE BLOCK ENDS HERE

    // THE FOLLOWING METHODS MUST IMPLEMENTED :

    /**
     * Return a student instance with values from the row with the respective id in the database.
     * If an instance with this id already exists, return the existing instance and do not create a second one.
     * @param id
     * @return
     * @throws NoSuchRecordException if no record with such an id exists in the database
     * This functionality is to be tested in test.nz.ac.wgtn.swen301.assignment1.TestStudentManager::test_readStudent (followed by optional numbers if multiple tests are used)
     */
    public static Student readStudent(String id) throws NoSuchRecordException {
        try {
            //Create a connection to directory given
            Connection conn = DriverManager.getConnection("jdbc:derby:memory:studentdb");
            //create prepared statement for inserting parameters of the sql query
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM STUDENTS WHERE ID = ?");
            //insert parameter
            ps.setString(1,id);
            ResultSet rs = ps.executeQuery();
            //iterate through the result set
            while (rs.next()) {
                String name = rs.getString("name");
                String firstName = rs.getString("first_name");
                String degreeID = rs.getString("degree");//get degreeID
                conn.close();
                Degree degree = readDegree(degreeID);//get degree Name
                //return the student instant
                return new Student(id,name,firstName,degree);
            }
            if(!rs.next()) {throw new NoSuchRecordException(); }
            }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Return a degree instance with values from the row with the respective id in the database.
     * If an instance with this id already exists, return the existing instance and do not create a second one.
     * @param id
     * @return
     * @throws NoSuchRecordException if no record with such an id exists in the database
     * This functionality is to be tested in test.nz.ac.wgtn.swen301.assignment1.TestStudentManager::test_readDegree (followed by optional numbers if multiple tests are used)
     */
    public static Degree readDegree(String id) throws NoSuchRecordException {
        try {
            //Create a connection to directory given
            Connection conn = DriverManager.getConnection("jdbc:derby:memory:studentdb");
            //create prepared statement for inserting parameters of the sql query
            PreparedStatement ps = conn.prepareStatement("SELECT name FROM DEGREES WHERE ID = ?");
            //insert parameter
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            //iterate through the result set
            while(rs.next()){
                String degreeName = rs.getString("name");
                conn.close();
                //return degree instant
                return new Degree(id,degreeName);
            }
            if(!rs.next()) {throw new NoSuchRecordException(); }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Delete a student instance from the database.
     * I.e., after this, trying to read a student with this id will result in a NoSuchRecordException.
     * @param student
     * @throws NoSuchRecordException if no record corresponding to this student instance exists in the database
     * This functionality is to be tested in test.nz.ac.wgtn.swen301.assignment1.TestStudentManager::test_delete
     */
    public static void delete(Student student) throws NoSuchRecordException {
        try {
            //Create a connection to directory given
            Connection conn = DriverManager.getConnection("jdbc:derby:memory:studentdb");
            //create prepared statement for inserting ID parameter of the sql query
            PreparedStatement ps = conn.prepareStatement("DELETE FROM STUDENTS WHERE ID = ?");
            //insert parameter
            ps.setString(1,student.getId());
            //execute update
            ps.executeUpdate();
            conn.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Update (synchronize) a student instance with the database.
     * The id will not be changed, but the values for first names or degree in the database might be changed by this operation.
     * After executing this command, the attribute values of the object and the respective database value are consistent.
     * Note that names and first names can only be max 1o characters long.
     * There is no special handling required to enforce this, just ensure that tests only use values with < 10 characters.
     * @param student
     * @throws NoSuchRecordException if no record corresponding to this student instance exists in the database
     * This functionality is to be tested in test.nz.ac.wgtn.swen301.assignment1.TestStudentManager::test_update (followed by optional numbers if multiple tests are used)
     */
    public static void update(Student student) throws NoSuchRecordException {
        try {
            //Create a connection to directory given
            Connection conn = DriverManager.getConnection("jdbc:derby:memory:studentdb");
            //create prepared statement for inserting ID parameter of the sql query
            PreparedStatement ps = conn.prepareStatement("UPDATE STUDENTS SET name = ?, first_name = ?, degree = ? WHERE ID = ?");
            //insert parameter
            ps.setString(1,student.getName());
            ps.setString(2,student.getFirstName());
            ps.setString(3,student.getDegree().getId());
            ps.setString(4,student.getId());
            //execute update
            if(student.getName().length()<=10 && student.getFirstName().length()<=10) {
                int rowsAffected = ps.executeUpdate();
                System.out.println("Affected row =" + rowsAffected);
                conn.close();
            }
            else {
                System.out.println("names and first names can only be max 1o characters long");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Create a new student with the values provided, and save it to the database.
     * The student must have a new id that is not been used by any other Student instance or STUDENTS record (row).
     * Note that names and first names can only be max 1o characters long.
     * There is no special handling required to enforce this, just ensure that tests only use values with < 10 characters.
     * @param name
     * @param firstName
     * @param degree
     * @return a freshly created student instance
     * This functionality is to be tested in test.nz.ac.wgtn.swen301.assignment1.TestStudentManager::test_createStudent (followed by optional numbers if multiple tests are used)
     */
    public static Student createStudent(String name,String firstName,Degree degree) {
        try {
            //Create a connection to directory given
            Connection conn = DriverManager.getConnection("jdbc:derby:memory:studentdb");
            //create prepared statement for inserting ID parameter of the sql query
            PreparedStatement ps = conn.prepareStatement("INSERT INTO STUDENTS VALUES(?,?,?,?)");
            Statement stmt = conn.createStatement();
            String sql = "SELECT id FROM STUDENTS";
            ResultSet rs = stmt.executeQuery(sql);
            int count = 0;
            while(rs.next()){
                count++;
            }
            String id = "id" + Integer.toString(count);
            PreparedStatement ps1 = conn.prepareStatement("SELECT * FROM STUDENTS WHERE ID = ?");
            //insert parameter
            ps1.setString(1,id);
            ResultSet rs1 = ps1.executeQuery();
            if(rs1.next()){
                System.out.println("The student ID is already exist");
                return null;
            }
            ps.setString(1, id);
            ps.setString(2, firstName);
            ps.setString(3, name);
            ps.setString(4, degree.getId());
            if(name.length()<=10 && firstName.length()<=10) {
                ps.executeUpdate();
                conn.close();
            }
            else {
                System.out.println("names and first names can only be max 1o characters long");
            }
            return new Student(id,name,firstName,degree);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        //return new Student(name,firstName,degree);
        return null;
    }

    /**
     * Get all student ids currently being used in the database.
     * @return
     * This functionality is to be tested in test.nz.ac.wgtn.swen301.assignment1.TestStudentManager::test_getAllStudentIds (followed by optional numbers if multiple tests are used)
     */
    public static Collection<String> getAllStudentIds() {
        try {
            //Create a connection to directory given
            Connection conn = DriverManager.getConnection("jdbc:derby:memory:studentdb");
            Statement stmt = conn.createStatement();
            String sql = "SELECT id FROM STUDENTS";
            ResultSet rs = stmt.executeQuery(sql);
            Collection<String> allStudentIds = new ArrayList<>();
            while(rs.next()) {
                allStudentIds.add(rs.getString("id"));
            }
            conn.close();
            return allStudentIds;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get all degree ids currently being used in the database.
     * @return
     * This functionality is to be tested in test.nz.ac.wgtn.swen301.assignment1.TestStudentManager::test_getAllDegreeIds (followed by optional numbers if multiple tests are used)
     */
    public static Iterable<String> getAllDegreeIds() {
        try {
            //Create a connection to directory given
            Connection conn = DriverManager.getConnection("jdbc:derby:memory:studentdb");
            Statement stmt = conn.createStatement();
            String sql = "SELECT id FROM DEGREES";
            ResultSet rs = stmt.executeQuery(sql);
            Collection<String> allDegreeIds = new ArrayList<>();
            while(rs.next()) {
                allDegreeIds.add(rs.getString("id"));
            }
            conn.close();
            return allDegreeIds;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}
