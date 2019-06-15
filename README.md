# Typogenetics
Typogenetics, from Douglas Hofstadter's *Gödel, Escher, Bach*, is a formal
system inspired by the process of protein synthesis in cells.

The follow-up problem is to find a strand of DNA that can self-replicate. This
program allows you to experiment by translating strands into enzymes and
carrying out the enzymes' commands on them.

## Example

Start with strand `CGGATAATGC`. Enter this strand at the prompt.

    Enter base sequence: (ctrl-C to exit)
    CGGATAATGC
    Enzyme: cop-ina-rpy-swi-inc
    This enzyme can bind on base G
    On which G base should it bind? (0, 1, 2, 3, ...)
    0

The output gives

 1. The amino acid chain that make up this enzyme -- these are also the
    commands that will be executed on the strand.
 2. The binding preference of the enzyme as determined by its tertiary
    structure.
 3. An option to choose the ordinal of the binding. There are two T bases in
    this strand so enter 0 or 1 (n.b. the indexing is zero-based). In this
    example 0 is selected.

Continuing, the commands are carried out and the strands at each step are
printed. The vertical line | shows the current position of the enzyme. Notice
that there are two strands and that the top is empty at the start. The status
of copy mode (on/off) is also shown.

    - - - - - - - - - - -
    -C-G-G-A-T-A-A-T-G-C-
       |

    Copy off, executing cop

    - -C- - - - - - - - -
    -C-G-G-A-T-A-A-T-G-C-
       |

    Copy on, executing ina

    - -C-T- - - - - - - - -
    -C-G-A-G-A-T-A-A-T-G-C-
         |

    Copy on, executing rpy

    - -C-T-C-T-A- - - - - -
    -C-G-A-G-A-T-A-A-T-G-C-
               |

    Copy on, executing swi

    -C-G-T-A-A-T-A-G-A-G-C-
    - - - - - -A-T-C-T-C- -
               |

    Copy on, executing inc

    -C-G-T-A-A-T-G-A-G-A-G-C-
    - - - - - -A-C-T-C-T-C- -
                 |

    Resulting strands:
    ACTCTC
    CGAGAGTAATGC

If your self-replication method involves multiple steps, you can copy one of
the resulting strands and run the program on it to reproduce the original, or
the see the code section for more complex sequences.

## Code

To program a more complex procedure to find a self-replicator, start with the
the snippet below.

```java
    Strand strand = new Strand("CGGATACTGC");

    // Ribosome translation may yield multiple enzymes from a single strand
    List<Enzyme> enzymes = Ribosome.translate(strand, Enzyme.class);
    Enzyme enzyme = enzymes.get(0);

    // Print the steps between amino acid commands
    enzyme.setOption("output", "true");

    // The second parameter of bind() selects the index of the binding base
    // (as determined by the its tertiary structure) -- here 0 binds to the
    // first G, 1 binds to second G etc.
    List<Strand> products = enzyme.bind(strand, 0);

    // From the list of strands, you can create more enzymes and bind them to
    // any other of the resulting strands in any arbitrary order
    Strand one = products.get(0);
    Strand two = products.get(1);
    enzyme = Ribosome.translate(two, Enzyme.class).get(0);
    products = enzyme.bind(one, 0);

    if (products.get(0).equals(strand)) {
        // Self-rep
    }
```

## Implementation Notes

The original description of Typogenetics is ambiguous: many of the enzyme
commands can be interpreted in more than one way, and in some cases, enzyme
behaviour is undefined. Even the folding rules are incomplete. If this program
outputs something different from what you'd expect, unless it's due to a bug
it's probably the case that I've interpreted the rules of the game differently.
Feel free to change the code to reflect your own rules.

The `int` command (insert) is renamed `ist` because `int` is a reserved keyword
in Java.

Typogenetics is essentially a model of a biological system. I wanted the
behaviour and structure of each biological component to be mirrored in the
code, hence the verbose OOP design. This was in hopes that the system could be
understood intuitively from the cellular perspective. There are more elegant
implementations around but (I hoped, at least) that this one could be easily
read and amended e.g. if you want to change the way ribosomes translate strands
to proteins, go to the Ribosome class and edit the translate() method. I doubt
the readability I hoped for was actually attained though -- there's still a lot
of refactoring that could be done.

## Installation

If you have the JDK, you can compile and run straight from the command line.

    git clone git@github.com:infinitelytight/typogenetics.git
    cd typogenetics
    javac -d bin src/main/java/typogenetics/*
    java -cp bin typogenetics.Typogenetics

Alternatively import the project in your IDE of choice.

If you don't have the JDK, use the jar file.

    java -jar bin/typogenetics.jar

