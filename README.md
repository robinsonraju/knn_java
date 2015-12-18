## A Simple KNN implementation in Java

### build
`` mvn package ``

### run
template ->  `` java -cp knn.jar cs286.Knn <hold-out %> <k> <distance> <input> <output>``

e.g -> ``java -cp knn.jar cs286.Knn 10 10 euclidean src/main/resources/iris-data.txt output.txt``

Sample output
```
k = 10
distance = euclidean
accuracy for species 1 = 100.0
accuracy for species 2 = 100.0
accuracy for species 3 = 100.0
```
