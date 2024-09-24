# flow-log-parser
Parser for Flow Log records that tags the records. https://docs.aws.amazon.com/vpc/latest/userguide/flow-log-records.html

## Assumptions
1. The protocol field in logs is numeric, which is to its corresponding protocol string using standard [IANA Table](https://www.iana.org/assignments/protocol-numbers/protocol-numbers.xhtml)
2. The logs have fields that are strictly part of Version 2 of Flow Log Records.
3. Each log entry in `input.txt` has at least 7 fields and the 6th field is the destination port and the 7th field is the protocol number. The program checks if (`fields.length > 7`), but if the file has irregular formatting or missing fields, it will skip those entries without further validation.

## How to run
1. Compile the `Parser.java` with `javac`
```
javac Parser.java
```

2. Run the code
```
java Parser
```

The input logs are in the `input.txt` file, the `dstport` and `protocol` lookup is in `lookup.txt` and the IANA table is in `iana_lookup.txt`.  
The output is stored in the `output.txt` file.
