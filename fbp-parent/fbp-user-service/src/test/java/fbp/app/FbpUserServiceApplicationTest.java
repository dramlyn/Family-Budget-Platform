package fbp.app;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class FbpUserServiceApplicationTest extends TestCase {
    public FbpUserServiceApplicationTest(String testName )
    {
        super( testName );
    }

    public static Test suite()
    {
        return new TestSuite( FbpUserServiceApplicationTest.class );
    }

    public void testApp()
    {
        assertTrue( true );
    }
}
