# Bucket Sort Animation

Interactive step-by-step visualization of the **Bucket Sort** algorithm built with **Java** and **JavaFX**. The animation highlights the current line of code being executed, displays variable states in real time, and uses smooth element transitions to illustrate how data moves during the sort.

## How It Works

1. **Find max** — scans the array to determine the largest value.
2. **Distribute** — each element is placed into one of `n` buckets using the formula `index = value * nBuckets / (max + 1)`.
3. **Sort buckets** — Insertion Sort is applied inside every bucket.
4. **Merge** — sorted buckets are concatenated back into the original array.

## Features

- Animated element movement between the array and color-coded buckets (red, teal, blue).
- Source code panel with real-time line highlighting.
- Live variable inspector showing current indices, keys, and comparisons.
- "Generate Random" button to create a new dataset of 9 elements across 3 buckets.
- Dark theme (Catppuccin Mocha).

## Tech Stack

- **Language:** Java 26
- **UI Framework:** JavaFX 21.0.6
- **Build Tool:** Apache Maven
- **Dependencies:** `javafx-controls`, `javafx-fxml`

## Project Structure

```
├── pom.xml
└── src/main/java/com/bucket/animacaobucket/
    ├── BucketSortAnimation.java   # Main JavaFX application
    ├── BucketSort.java            # Sorting logic (console version)
    └── Main.java                  # Console test entry point
```

## How to Run

**Prerequisites:** JDK 21+ and Maven installed.

```bash
mvn clean javafx:run
```

## License

This project is for educational purposes.
