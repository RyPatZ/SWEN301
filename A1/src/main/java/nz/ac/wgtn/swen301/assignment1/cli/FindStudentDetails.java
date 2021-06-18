package nz.ac.wgtn.swen301.assignment1.cli;

import nz.ac.wgtn.swen301.assignment1.StudentManager;
import nz.ac.wgtn.swen301.studentdb.NoSuchRecordException;
import nz.ac.wgtn.swen301.studentdb.Student;

import javax.swing.plaf.synth.SynthEditorPaneUI;

public class FindStudentDetails {

    // THE FOLLOWING METHOD MUST IMPLEMENTED
    /**
     * Executable: the user will provide a student id as single argument, and if the details are found,
     * the respective details are printed to the console.
     * E.g. a user could invoke this by running "java -cp <someclasspath> nz.ac.wgtn.swen301.assignment1.cli.FindStudentDetails id42"
     * command I used from my personal laptop: java -cp /Users/patrickzhang/Desktop/SWEN301/swen301-2021-assignment1-template-master/target/classes:/Users/patrickzhang/Desktop/SWEN301/swen301-2021-assignment1-template-master/lib/studentdb-1.1.1.jar:/Users/patrickzhang/.m2/repository/org/apache/derby/derby/10.14.2.0/derby-10.14.2.0.jar nz.ac.wgtn.swen301.assignment1.cli.FindStudentDetails id42
     * @param arg
     */
    public static void main (String[] arg) throws NoSuchRecordException {
        if(arg.length<1){
            System.out.println("invalid student ID");
            return;
        }
        StudentManager studentManager = new StudentManager();
        System.out.println(arg);
        String id = arg[0];
        Student student = studentManager.readStudent(id);
        if (student != null){
            System.out.println("student ID = "+student.getId() );
            System.out.println("student Name = "+student.getName() );
            System.out.println("student First Name = "+student.getFirstName());
            System.out.println("student Degree ID = "+student.getDegree().getId());
            System.out.println("student Degree Name = "+student.getDegree().getName());
        }
        else {
            System.out.println("invalid student ID");
        }
        //Here is the test run output:
        //patrickzhang@PatrickdeMacBook-Pro ~ % java -cp /Users/patrickzhang/Desktop/SWEN301/swen301-2021-assignment1-template-master/target/classes:/Users/patrickzhang/Desktop/SWEN301/swen301-2021-assignment1-template-master/lib/studentdb-1.1.1.jar:/Users/patrickzhang/.m2/repository/org/apache/derby/derby/10.14.2.0/derby-10.14.2.0.jar nz.ac.wgtn.swen301.assignment1.cli.FindStudentDetails id42

//        Database ready
//        10000 rows in STUDENTS with columns id,first_name,name,degree (all strings)
//        10 rows in DEGREES with columns with columns id, name (all strings)
//        Use the following URL to connect: "jdbc:derby:memory:studentdb"
//                [Ljava.lang.String;@6c2be147
//        student ID = id42
//        student Name = Tipene
//        student First Name = Sue
//        student Degree ID = deg2
//        student Degree Name = BE Cybersecurity
    }
}
