# LinearJ

![Java 21](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/UI-JavaFX-1f425f)
![Project status](https://img.shields.io/badge/status-work%20in%20progress-f0ad4e)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

LinearJ is a desktop matrix calculator and experimental DC circuit solver written in Java. It combines a JavaFX interface with linear-algebra routines and a circuit model based on **modified nodal analysis (MNA)**.

> [!IMPORTANT]
> This project is incomplete.

## Screenshots

### Matrix calculator and netlist workspace

![LinearJ main window showing the matrix calculator and netlist workspace](docs/images/linearj-main-window.png)

### Adding a circuit component

![LinearJ main window with the add-component dialog open](docs/images/linearj-add-component-dialog.png)

### Current workspace snapshot

![LinearJ workspace showing the matrix calculator, net list table, and console output area](docs/images/linearj-current-workspace-june-2026.png)

## What to expect?

- A JavaFX/FXML desktop interface
- Matrix addition, subtraction, multiplication, inversion, determinant, and transpose routines
- A text format for entering one or two matrices
- A netlist table and an add-component dialog
- Models for resistors, independent current sources, independent voltage sources, and circuit nodes
- Circuit-element stamping for assembling a linear system
- An LU-decomposition solver for equations of the form `A x = b`

The matrix calculator is the most directly usable part of the application today. The circuit classes and netlist UI represent active development; they should be treated as a preview rather than a complete simulator.

## Theory

### Matrices and linear systems

Many engineering problems can be express in the form:

```text
A x = b
```

Here, `A` stores the coefficients of the problem, `x` contains the unknown values, and `b` contains the known inputs. LinearJ includes general matrix operations as well as LU decomposition, which factors a square matrix into lower- and upper-triangular matrices so the system can be solved efficiently by forward and back substitution.

### Modified nodal analysis

Nodal analysis applies Kirchhoff's Current Law (KCL): the algebraic sum of currents at each node is zero. For a resistor between nodes `i` and `j`, its conductance `g = 1/R` contributes to the circuit matrix as:

```text
 A[i][i] += g       A[i][j] -= g
 A[j][j] += g       A[j][i] -= g
```

Current sources contribute to the right-hand-side vector. Ideal voltage sources require extra unknown currents and extra rows and columns, which is the modification in **modified** nodal analysis. After every component has “stamped” its contribution into the global matrix and vector, LinearJ solves the resulting system to obtain node voltages and voltage-source currents.

This design is reflected in the code: circuit elements know how to stamp themselves, while `CircuitSolver` assembles the system and `LUDecomposition` performs the numerical solve.

## Cool, now how do I get my own LinearJ?

### Requirements

- JDK 21
- Git
- macOS, Linux, or Windows with a graphical desktop

Maven does not need to be installed separately because the repository includes the Maven Wrapper.

### Run from source

```bash
git clone https://github.com/chilldotrelax/LinearJ.git
cd LinearJ
./mvnw javafx:run
```

On Windows, use `mvnw.cmd javafx:run` instead.

To compile and run the test suite:

```bash
./mvnw clean verify
```

## Matrix input format

Enter a matrix inside square brackets. Separate values in a row with spaces and separate rows with a semicolon:

```text
[1 2;3 4]
```

Operations that take two matrices should be separated by `/`:

```text
[1 2;3 4]/[5 6;7 8]
```

Unfortunately, the parser is slightly strict about formatting; I understand it's annoying :(

## Project structure

```text
src/main/java/org/andy/linearj/
├── Circuit/             Circuit elements, nodes, factory, and solver
├── Maths/               Matrix utilities and LU decomposition
├── Screen/controllers/  JavaFX controllers and view models
├── Screen/misc/         Error windows and custom exceptions
└── Screen/toplevel/      Application entry point

src/main/resources/org/andy/linearj/
└── *.fxml                JavaFX layouts
```

## License

LinearJ is available under the [MIT License](LICENSE).
