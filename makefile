JFLAGS = -g
JCC = javac
JVM = java

default: Step2.class

Step2.class: Step2.java
	$(JCC) $(JFLAGS) -classpath .:classes:/opt/pi4j/lib/'*' Step2.java

run: $(MAIN).class
	$(JVM) $(JFLAGS) -classpath .:classes:/opt/pi4j/lib/'*' Step2

clean: $(RM) *.class
