# Simplified_Java_Verifier
Java verifier - a tool able to verify the validity of Java code; it knows how to read Java code and determine its validity, but not to translate it to bytecode.

Design Remarks:

One of the biggest questions was how to store the variables. At first glance
An ArrayList of Variable objects might make sense, but the order of the
Variables doesn't necessarily have any meaning. Also there should be no
Duplicate variables. In our implementation, we used hashmaps in order to
Ensure that every variable inserted, removed or searched takes an average
constant time. In addition, the values of variables, including any delimiters
(" or '), are stored in the Variable object. This allows the API to be used
For an actual java compiler, not just a code checker.

main package:
The main method in this class is responsible for creating an array list
Of the lines of code. This ArrayList is then sent to the main parser.


parser package:

We use a main parser that receives all the lines of the file,
parsing them one by one.
At first, the parser saves all the member variables and the methods defined
in the code. In order to do this, it
uses the classes "VariableFactory"(*) and "Method".
Then, the main parser calls the "MethodParser" which runs over the list of all
the methods and checks the legality
of the lines in each method. If there is another block inside the method,
the parser calls the "BlockParser" which
parses the lines of that block.
In each iteration on a method we create another list of all the local
variables of that method, in order to
separate between member variables and local variables.
The ParseTools class contains several helper functions.

(*)In order to create a Variable, the parsers call the 
which is responsible for creating all Variable objects that appear in a
single line.


variable and scope packages:
The scope package contains the if/while block class and the method class.
The classes "Variable" and "Method" represent a single variable/method
accordingly. If the parsers encounter a new
variable or method in the code they will try to create a new Object of
Variable/Method accordingly.
In any point of the code, if the parsers encounter a line which do not
fit the pattern of a legal line (e.g a line
must end with or , a problem while trying to create or assign a
Variable) it will throw an exception that
fits the error, and then the main method (in class ) will exit
the program. The exceptions mechanism that
we used is described in the Answers to Questions section.

syntaxtools package:
The Tool class supplies several helper functions that are used in different
Areas of the program.

The Regex class contains the common regular expressions used in the program.

The symbols class contains the common symbols used in the program.

Each package contains exception that classes in the package may throw.
