package test.com.sun.ts.lib;

import com.sun.ts.lib.harness.RemoteStatus;
import com.sun.ts.lib.harness.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Basic validation of serialization of RemoteStatus.
 */
public class RemoteStatusTest {
    @Test
    public void testSerializePass() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        RemoteStatus status = new RemoteStatus(Status.passed("test1"));
        try(ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(status);
        }

        byte[] data = bos.toByteArray();
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bis);
        RemoteStatus status2 = (RemoteStatus) ois.readObject();
        Assertions.assertTrue(status2.toStatus().isPassed(), "Status should be passed");
        Assertions.assertEquals("test1", status2.toStatus().getReason(), "Reason should be 'test1''");
    }

    @Test
    public void testSerializeFailNoError() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        RemoteStatus status = new RemoteStatus(Status.failed("test2"));
        try(ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(status);
        }

        byte[] data = bos.toByteArray();
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bis);
        RemoteStatus status2 = (RemoteStatus) ois.readObject();
        Assertions.assertFalse(status2.toStatus().isPassed(), "Status should be failed");
        Assertions.assertEquals("test2", status2.toStatus().getReason(), "Reason should be 'test2''");
        Assertions.assertNull(status2.getErrorMessage(), "Error message should be null");
        Assertions.assertNull(status2.getErrorTrace(), "Error trace should be null");
    }

    @Test
    public void testSerializeFailWithError() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        RemoteStatus status = new RemoteStatus(Status.failed("test3"), new IOException("Failed to read something"));
        try(ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(status);
        }

        byte[] data = bos.toByteArray();
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bis);
        RemoteStatus status2 = (RemoteStatus) ois.readObject();
        System.out.println("NOT AN ERROR, example failed status with error: ");
        System.out.println(status2);
        Assertions.assertFalse(status2.toStatus().isPassed(), "Status should be failed");
        Assertions.assertEquals("test3", status2.toStatus().getReason(), "Reason should be 'test3''");
        Assertions.assertEquals("Failed to read something", status2.getErrorMessage(), "Error message should be 'Failed to read something'");
        Assertions.assertNotNull(status2.getErrorTrace(), "Error trace should NOT be null");
    }
}
