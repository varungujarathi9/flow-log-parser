# flow-log-parser
Parser for Flow Log records that tags the records. https://docs.aws.amazon.com/vpc/latest/userguide/flow-log-records.html

## Assumptions
1. The protocol field in logs is numeric, which is to its corresponding protocol string using standard [IANA Table](https://www.iana.org/assignments/protocol-numbers/protocol-numbers.xhtml)
