(This code is part of my thesis)

The event log (PSED II) has a large number of distinct cases and unstrucuted behavior, creating spaghetti model. In order to increase the comprehensibility of process mining results, clusteringg can be performed. For this, a program has been developed. In order to cluster, a distance between cases is required. Several methods have been used for this:
  * Graph Edit Distance - based on creating a cost matrix and with the Hungarioan algoritmn find the optimal costs - the code comes from a public library
  * Levenhstein Distance -  this code was also found in a public library
  * Distance of a Graph based on the structure

The clustering algoritmn is based on agglomerative hierachical clustering.

The output of the program:
* A similarity distribution
* A succes/failure matrix
* The average similitary of all the cases
* Clusters
* The average similarity of the clusters
* Output files of the clusters so that it can be used in other tools like ProM, Disco

How to use the program:
* Install an IDE for Java, e.g. NetBeans. The files are in branch source.
* Create a project and add the Java Classes in one directory
* Put the import files into an Import Directory - there are several files with a certain number of cases. The files are in branch import files.
* Create an export directory
* Main is in GED - you must change some parameters at the beginning of the program, e.g. the import en export directories
* Run Main 
