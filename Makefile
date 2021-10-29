# Sample Makefile for the WACC Compiler lab: edit this to build your own comiler
# Locations

ANTLR_DIR	:= antlr_config
SOURCE_DIR	:= src/main
OUTPUT_DIR	:= bin 

# Tools

ANTLR	:= antlrBuild
FIND	:= find
RM	:= rm -rf
MKDIR	:= mkdir -p
JAVA	:= java
JAVAC	:= javac
MVN     := mvn

JFLAGS	:= -sourcepath $(SOURCE_DIR) -d $(OUTPUT_DIR) -cp lib/antlr-4.7-complete.jar

# the make rules

all: rules

# runs the antlr build script then attempts to compile all .java files within src
rules:
	cd $(ANTLR_DIR) && ./$(ANTLR)
	$(MVN) clean compile

clean:
	$(RM) rules $(OUTPUT_DIR) $(SOURCE_DIR)/main/java/group37/antlr
	$(MVN) clean

test:
	cd $(ANTLR_DIR) && ./$(ANTLR)
	$(MVN) -e test


.PHONY: all rules clean


