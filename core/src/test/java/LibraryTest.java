import org.testng.annotations.*;
import static org.testng.Assert.assertTrue;

/*
 * This Java source file was auto generated by running 'gradle init --type java-library --with testng'
 * by 'ykyang' at '11/8/16 4:19 PM' with Gradle 3.1
 *
 * @author ykyang, @date 11/8/16 4:19 PM
 */
public class LibraryTest {
    @Test public void someLibraryMethodReturnsTrue() {
        Library classUnderTest = new Library();
        assertTrue(classUnderTest.someLibraryMethod(), "someLibraryMethod should return 'true'");
    }
}
