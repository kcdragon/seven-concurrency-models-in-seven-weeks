JAVAC = javac
JAVA = java
sources = $(wildcard *.java)
classes = $(sources:.java=.class)

default:
	$(JAVAC) $(sources)

run:
	$(JAVA) Philosopher $(ARGS)

clean:
	$(RM) *.class
