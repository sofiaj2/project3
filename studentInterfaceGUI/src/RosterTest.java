package src;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This JUnit Test class tests the Roster add and remove methods
 * @author Sofia Juliani, Arnold Nguyen
 */
public class RosterTest {
    /**
    This test case adds a valid international student to the roster
     **/
    @Test
    public void test_add_true_forInternational(){
        Date d1 = new Date("5/1/1996");
        Major m1 = Major.BAIT;
        Profile p1 = new Profile("Doe", "Jane", d1);
        boolean studyAbroad = true;
        Student s1 = new International(p1, m1, 95, studyAbroad);
        Roster myRoster = new Roster();
        assertTrue(myRoster.add(s1));
    }

    /**
     * This test case returns false given that the international student
     * is too young to be in the roster.
     */
    @Test
    public void test_add_false_forInternational(){
        Date d1 = new Date("5/1/2020");
        Major m1 = Major.BAIT;
        Profile p1 = new Profile("Doe", "Jane", d1);
        boolean studyAbroad = true;
        Student s1 = new International(p1, m1, 95   , studyAbroad);
        Roster myRoster = new Roster();
        assertFalse(myRoster.add(s1));
    }

    /**
     * This test case returns true given that the tristate student is valid
     * to be added into the roster.
     */
    @Test
    public void test_add_true_forTriState(){
        Date d1 = new Date("5/1/1996");
        Major m1 = Major.BAIT;
        Profile p1 = new Profile("Doe", "Jane", d1);
        Student s1 = new TriState(p1, m1, 95, "NY");
        Roster myRoster = new Roster();
        assertTrue(myRoster.add(s1));
    }

    /**
     * This test case is invalid given that the age of the student is more
     * than the current year, thus having a "negative" age.
     */
    @Test
    public void test_add_false_forTriState(){
        Date d1 = new Date("5/1/2027");
        Major m1 = Major.BAIT;
        Profile p1 = new Profile("Doe", "Jane", d1);
        Student s1 = new TriState(p1, m1, 95, "NY");
        Roster myRoster = new Roster();
        assertFalse(myRoster.add(s1));
    }

    /**
     * This test case is invalid given that the International student
     * has an invalid birthdate.
     */
    @Test
    public void test_remove_false_forInternational() {
        Date d1 = new Date("5/1/2025");
        Major m1 = Major.BAIT;
        Profile p1 = new Profile("Box", "Jonathan", d1);
        boolean studyAbroad = true;
        Student s1 = new International(p1, m1, 95, studyAbroad);
        Roster myRoster = new Roster();
        myRoster.add(s1);
        assertFalse(myRoster.remove(s1));
    }

    /**
     * This test case is true because the International student has valid
     * dob, name, and credits.
     */
    @Test
    public void test_remove_true_forInternational(){
        Date d1 = new Date("5/1/1996");
        Major m1 = Major.BAIT;
        Profile p1 = new Profile("Doe", "Jane", d1);
        boolean studyAbroad = true;
        Student s1 = new International(p1, m1, 95, studyAbroad);
        Roster myRoster = new Roster();
        myRoster.add(s1);
        assertTrue(myRoster.remove(s1));
    }

    /**
     * This test case is false for TriState because the student being removed
     * does not exist in the array
     */
    @Test
    public void test_remove_false_forTriState() {
        Roster myRoster = new Roster();
        Date d1 = new Date("5/1/2025");
        Major m1 = Major.BAIT;
        Profile p1 = new Profile("Box", "Jonathan", d1);
        Student s1 = new TriState(p1, m1, 89, "NY");
        myRoster.add(s1);
        Date d2 = new Date("5/1/2027");
        Major m2 = Major.BAIT;
        Profile p2 = new Profile("Box", "Jonathan", d1);
        Student s2 = new TriState(p2, m2, 75, "NY");
        assertFalse(myRoster.remove(s2));
    }

    /**
     * This test case is correct because the TriState student is found
     * correctly and removed.
     */
    @Test
    public void test_remove_true_forTriState(){
        Date d1 = new Date("5/1/1995");
        Major m1 = Major.BAIT;
        Profile p1 = new Profile("Box", "Jonathan", d1);
        Student s1 = new TriState(p1, m1, 89, "CT");
        Roster myRoster = new Roster();
        myRoster.add(s1);
        assertTrue(myRoster.remove(s1));
    }
}