
# KIBO4_TH Yume TEAM

Separated into 2 parts:

## 1. C for Cal(Calculate)

The main idea for solving this problem comes from dividing the given maximum and minimum coordinates into single nodes. After that, we use the _**Floyd-Warshall**_ algorithm to solve every possible path between any two nodes.

**Constraint:** The time complexity of **Floyd-Warshall** is O(N^3). The nodes we divide are not very precise, and this means **Astrobee** has fewer nodes to move through.

## 2. YourServices

This file is the main part for running Astrobee. We represent the nodes from the calculations as an array. When we need to use it, we just call node A and node B, and we will get the path.
```java
ArrayList<Point> path = pathfinder.getPath(currentPos, destinationNode);
```
After we get the list of paths, we just put it into the move function.
