
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

  /**
   * Reads the IANA lookup table from the "iana_lookup.txt" file.
   * Maps port numbers to protocol names and stores them in the IanaLookup map.
   * 
   * @throws IOException if an I/O error occurs while reading the file
   */
  private static void readIanaLookup() throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader("iana_lookup.txt"));
    String line;
    while ((line = reader.readLine()) != null) {
      String[] parts = line.split(" ");

      // First part is the port number, second part is the protocol name
      // Store in IanaLookup map
      IanaLookup.put(Integer.parseInt(parts[0]), parts[1]);
    }
    reader.close();
  }

  /**
   * Reads the lookup table from the "lookup.txt" file.
   * Maps port-protocol pairs to tags and stores them in the portProtocolToTag
   * map.
   * 
   * @throws IOException if an I/O error occurs while reading the file
   */
  private static void readPortProtocolLookup() throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader("lookup.txt"));
    String line;
    while ((line = reader.readLine()) != null) {
      String[] parts = line.split(",");

      // Ensure the line has exactly three parts (port, protocol, and tag)
      if (parts.length == 3) {
        String port = parts[0].trim();
        String protocol = parts[1].trim().toLowerCase(); // Convert protocol to lowercase
        String tag = parts[2].trim();

        // Create a unique key by combining port and protocol
        String key = port + "," + protocol;
        // Store the key-value pair in the portProtocolToTag map
        portProtocolToTag.put(key, tag);
      }
    }
    reader.close();
  }

  /**
   * Reads log entries from the "input.txt" file, categorizes them by port and
   * protocol,
   * and updates the tagCounts and portProtocolCounts maps with the appropriate
   * counts.
   * 
   * @throws IOException if an I/O error occurs while reading the file
   */
  private static void readInputFile() throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
    String log;
    while ((log = reader.readLine()) != null) {
      String[] fields = log.split(" ");
      if (fields.length > 7) {
        String dstPort = fields[6].trim();
        // Lookup protocol using IanaLookup map (7th field is the protocol)
        // If the protocol is not found, it defaults to "Unassigned"
        String protocol = IanaLookup.getOrDefault(Integer.parseInt(fields[7].trim()), "Unassigned").toLowerCase();

        // Lookup the tag for the port-protocol pair, default to "Untagged" if not found
        String lookupKey = dstPort + "," + protocol;
        String tag = portProtocolToTag.getOrDefault(lookupKey, "Untagged");

        // Increment the tag count for the current tag
        tagCounts.put(tag, tagCounts.getOrDefault(tag, 0) + 1);

        // Increment the port-protocol count for the current combination
        portProtocolCounts.put(lookupKey, portProtocolCounts.getOrDefault(lookupKey, 0) + 1);

      }
    }
    reader.close();
  }

  /**
   * Writes the tag counts and port-protocol counts to the "output.txt" file.
   * Outputs two sections: one for tag counts and one for port-protocol counts.
   * 
   * @throws IOException if an I/O error occurs while writing the file
   */
  private static void writeOutputFiles() throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));

    // Write each tag and its corresponding count
    writer.write("Tag,Count\n");
    for (Map.Entry<String, Integer> entry : tagCounts.entrySet()) {
      writer.write(entry.getKey() + "," + entry.getValue() + "\n");
    }

    // Write each port-protocol pair and its corresponding count
    writer.write("\nPort,Protocol,Count\n");
    for (Map.Entry<String, Integer> entry : portProtocolCounts.entrySet()) {
      String[] parts = entry.getKey().split(",");
      writer.write(parts[0] + "," + parts[1] + "," + entry.getValue() + "\n");
    }

    writer.close();
  }

  public static void main(String[] args) {
    try {
      readIanaLookup(); // Read the IANA Lookup
      readPortProtocolLookup(); // Read the dstport, protocol lookup
      readInputFile(); // Read the logs file
      writeOutputFiles(); // Write the output to a txt file
    } catch (Exception e) {
      System.err.println("Error occurred");
      e.printStackTrace();
    }
  }

}
