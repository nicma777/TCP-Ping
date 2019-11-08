import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SizeOf {
	
	public static int sizeof(String obj) throws IOException {

        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        DataOutputStream objectOutputStream = new DataOutputStream(byteOutputStream);

        objectOutputStream.writeUTF(obj);
        objectOutputStream.flush(); 
        objectOutputStream.close();

        return byteOutputStream.toByteArray().length;
    }

}
