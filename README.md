# Sorting Algorithm Animations

Interactive step-by-step visualizations of sorting algorithms built with **Java** and **JavaFX**. Each animation highlights the current line of code being executed, displays variable states in real time, and uses smooth element transitions to illustrate how data moves during the sort.

## Algorithms

### Bucket Sort (`animacao-bucket/`)

Demonstrates the Bucket Sort algorithm with Insertion Sort as the internal sub-routine.

**How it works:**

1. **Find max** — scans the array to determine the largest value.
2. **Distribute** — each element is placed into one of `n` buckets using the formula `index = value * nBuckets / (max + 1)`.
3. **Sort buckets** — Insertion Sort is applied inside every bucket.
4. **Merge** — sorted buckets are concatenated back into the original array.

**Features:**
- Animated element movement between the array and color-coded buckets.
- Source code panel with real-time line highlighting.
- Live variable inspector showing current indices, keys, and comparisons.
- "Generate Random" button to create a new dataset of 9 elements across 3 buckets.

### Radix Sort (`animacao-radix/`)

Demonstrates the Radix Sort algorithm using Counting Sort as the stable sub-routine for each digit pass (units, tens, hundreds).

## Tech Stack

- **Language:** Java 26
- **UI Framework:** JavaFX 21.0.6
- **Build Tool:** Apache Maven
- **Dependencies:** `javafx-controls`, `javafx-fxml`

## Project Structure

```
├── animacao-bucket/
│   ├── pom.xml
│   └── src/main/java/com/bucket/animacaobucket/
│       ├── BucketSortAnimation.java   # Main JavaFX application
│       ├── BucketSort.java            # Sorting logic (console version)
│       └── Main.java                  # Console test entry point
│
├── animacao-radix/
│   ├── pom.xml
│   └── src/main/java/com/fipp/animacao/
│       ├── app/Main.java              # Application entry point
│       ├── view/RadixView.java        # UI and animation
│       └── util/Animacao.java         # Animation utilities
│
└── README.md
```

## How to Run

**Prerequisites:** JDK 21+ and Maven installed.

```bash
# Bucket Sort animation
cd animacao-bucket
mvn clean javafx:run

# Radix Sort animation
cd animacao-radix
mvn clean javafx:run
```

## Screenshots

The Bucket Sort animation features a dark theme (Catppuccin Mocha) with:
- A top array showing the unsorted/sorted elements
- Three color-coded buckets (red, teal, blue) below
- A scrollable code panel on the right with line-by-line highlighting
- A variable inspector at the bottom

## License

This project is for educational purposes.
