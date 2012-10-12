import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class INIControllerStandard {
	void doChange(String filename, String key, String value) throws IOException {
		final File tmpFile = new File(filename + ".tmp");
	    final File file = new File(filename);
	    PrintWriter pw = new PrintWriter(tmpFile);
	    BufferedReader br = new BufferedReader(new FileReader(file));
	    boolean found = false;
	    final String toAdd = key + '=' + value;
	    for (String line; (line = br.readLine()) != null; ) {
	        if (line.startsWith(key + '=')) {
	            line = toAdd;
	            found = true;
	        }
	        pw.println(line);
	    }
	    if (!found)
	        pw.println(toAdd);
	    br.close();
	    pw.close();
	    file.delete();
    	boolean success = tmpFile.renameTo(new File(filename));
	    if (!success) {
	    	System.out.println("---> fail");
	    }
	}
}
