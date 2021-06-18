## SWEN301 Assignment 1 Template

Please refer to the assignment brief for details. 

a.How you checked the correctness of dependencies between the UI and the domain layer using the generated jdepend reports.  

From the generated jdepend report, dependency metrics for our java packages are generated based on each java packages. For my current project, the 'nz.ac.wgtn.swen301.assignment1.cli' represent UI layer and the 'nz.ac.wgtn.swen301.assignment1' represent the Domain layer. The Domain layer have 1 package depend on it and depend on 4 packages, the UI layer depend on 5 packages including the Domain layer.  Both packages have high instability and no abstract type. For Domain layer, the sum of A and I is 0.83 so the D is 0.17(1-0.83) and for the UI layer, the I and D are both 1，The D is really low which seem correct to me. High instability means there are many dependencies on other packages and there are no cyclic dependency, from that we can tell the upper layer depend on lower layer(UI layer depend on domain layer depend on data layer), and it is correct.

b.How to generate the standalone CLI application with mvn, and how to use
To generate a standalone application we should add a plugin into the Pom.xml to collect all dependencies and make a runnable jar file.
I used spring-boot-maven-plugin which allows to package executable jar and run an application “in-place”.
 After get pom.xml ready
 All we have to do is use the terminal or command propt to get into the project dirctory 
 and run 'mvn package'
then a jar file called studentfinder.jar should be created
then we run : 'java -jar target/studentfinder.jar id42' in terminal to execute the jar file


c.Discuss (less than 100 words) whether your design is prone to memory leaks by interfering with Garbage Collection and how this has been or could be addressed.
The amount of memory leak within my program is small.First of all, my design doesn't have static field which could remain in the memory throughout the lifetime of the application and cause memory leaking. Second of all, unclosed resources are also likely to leave garbage collection in the memory to cause memory leaks especially in my program, I would have to create a connection to the student database. If I do not close the connection then it will consume memory and deteriorate performance.  There are some instances and collections are kept reference until the program stop such as in read student ID method, the collection of String remain reference until the program stop which may also cause memory leak.Also, in read student method, I called read degree method to get degree instance to create student instance, the degree instance remain referenced even it is not being used.

