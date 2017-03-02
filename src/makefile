JFLAGS = -g
JC = javac

.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Node.java\
	ReadFile.java\
	hashtagcounter.java\
	MaxFibonacciHeap.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
