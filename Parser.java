
/**
* read the input log file
* read the lookup file
* iterate through logs
*   read field 6, 7 
*   map it according the lookup table
*   store count
*/
import java.util.*;
import java.io.*;

public class Parser {

  private static Map<String, String> portProtocolToTag = new HashMap<>();
  private static Map<String, Integer> tagCounts = new HashMap<>();
  private static Map<String, Integer> portProtocolCounts = new HashMap<>();
  private static Map<Integer, String> IanaLookup = new HashMap<>();

  private static void readIanaLookup() throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader("iana_lookup.txt"));
    String line;
    while((line = reader.readLine()) != null){
      String[] parts = line.split(" ");
      IanaLookup.put(Integer.parseInt(parts[0]), parts[1]);
    }
  }

  private static void readLookupTable() throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader("lookup.txt"));
    String line;
    while ((line = reader.readLine()) != null) {
      String[] parts = line.split(",");
      if (parts.length == 3) {
        String port = parts[0].trim();
        String protocol = parts[1].trim().toLowerCase();
        String tag = parts[2].trim();
        String key = port + "," + protocol;
        portProtocolToTag.put(key, tag);
      }
    }
    reader.close();
  }

  private static void readInputFile() throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
    String log;
    while ((log = reader.readLine()) != null) {
      String[] fields = log.split(" ");
      if (fields.length > 6) {
        String dstPort = fields[6].trim();
        String protocol = IanaLookup.getOrDefault(Integer.parseInt(fields[7].trim()), "Unassigned").toLowerCase();

        String lookupKey = dstPort + "," + protocol;
        String tag = portProtocolToTag.getOrDefault(lookupKey, "Untagged");

        tagCounts.put(tag, tagCounts.getOrDefault(tag, 0)+1);

        portProtocolCounts.put(lookupKey, portProtocolCounts.getOrDefault(lookupKey, 0)+1);

      }
    }
    reader.close();
  }

  private static void writeOutputFiles() throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));

    writer.write("Tag,Count\n");
    for (Map.Entry<String, Integer> entry : tagCounts.entrySet()) {
      writer.write(entry.getKey() + "," + entry.getValue() + "\n");
    }

    writer.write("\nPort,Protocol,Count\n");
    for (Map.Entry<String, Integer> entry : portProtocolCounts.entrySet()) {
      String[] parts = entry.getKey().split(",");
      writer.write(parts[0] + "," + parts[1] + "," + entry.getValue() + "\n");
    }

    writer.close();
  }

  public static void main(String[] args) {
    try {
      readIanaLookup();
      readLookupTable();
      readInputFile();
      writeOutputFiles();
    } catch (Exception e) {
      System.err.println("Error occurred");
      e.printStackTrace();
    }
  }

}
