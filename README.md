This code is part of my thesis. It investigates an eventlog with cases. Each case can be seen as an instance of a proces.

The cases seems te be very different (spagetti). So we developed an program to cluster the cases. To create clusters we need to know the distance between each proces. 
We used serveral method for the distance:
  * Graph Edit Distance - based on creating a cost matrix and with the Hungarioan algoritmn find the optimal costs - the code comes from a public library
  * Levenhstein Distance -  this method was found in a public library
  * Distance of a Graph based on the structure.

The clustering algoritmn is based on hierachical clustering.

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

The final result:
*  The average similarity is very low, for all 3 used distance methods
*  After clustering the average similarity is a tiny bit higher but still very low.
*  It seems that the cases in the event log are so different that a common succes or failure proces cannot be found in the data.
