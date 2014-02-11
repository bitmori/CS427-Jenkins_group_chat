COMPILER = rubber
COMPILER_FLAGS = --pdf

all : FinalDocumentation.pdf

FinalDocumentation.pdf : FinalDocumentation.tex
	$(COMPILER) $(COMPILER_FLAGS) FinalDocumentation.tex

clean :
	-rm -rf *.aux *.toc *.dvi *.log *.pdf *.out
