# flow-log-parser
Parser for Flow Log records that tags the records. https://docs.aws.amazon.com/vpc/latest/userguide/flow-log-records.html

## Assumptions
1. The protocol field in logs is numeric, which is to its corresponding protocol string using standard [IANA Table](https://www.iana.org/assignments/protocol-numbers/protocol-numbers.xhtml)

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
