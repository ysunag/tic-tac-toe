all: RESTJson

run: 
	java -cp bin:./libs/* edu.northeastern.cs5007.RESTJsonExample

RESTJson: src/edu/northeastern/cs5007/RESTJsonExample.java
	mkdir ./bin
	javac -Xlint:deprecation -d bin -sourcepath src -cp ./libs/*:. src/edu/northeastern/cs5007/RESTJsonExample.java

clean:
	rm -rf ./bin