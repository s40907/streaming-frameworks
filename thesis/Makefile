MAINFILE = thesis

LATEX = pdflatex
LATEXFLAGS = -interaction=nonstopmode

MAKEINDEX = makeindex
BIBTEX = bibtex
BIBONE = lit
BIBONENAME = literaturVerzeichnis
BIBTWO = int
BIBTWONAME = internetQuellen
DEL = rm -f
ECHO = echo
VIEWPDF = xdg-open


.PHONY: help clean cleanaux cleanall cleanpdf showpdf

index: $(MAINFILE).ind $(MAINFILE).ilg
abbr: $(MAINFILE).nls
bib: $(MAINFILE).blg $(MAINFILE).bbl

help:
	@$(ECHO) Regeln
	@$(ECHO) all  
	@$(ECHO) cleanall cleanpdf cleanaux
	@$(ECHO) showpdf

normal:
	@$(ECHO) in interaction mode
	$(LATEX) $(MAINFILE).tex

all: 
	@$(ECHO) init pass
	@ln -s $(BIBONENAME).bib $(BIBONE).bib
	@ln -s $(BIBTWONAME).bib $(BIBTWO).bib
	$(LATEX) $(LATEXFLAGS) $(MAINFILE).tex
	@$(ECHO) Create bibliography $(BIBONE)
	$(BIBTEX) $(BIBONE).aux
	@$(ECHO) Create bibliography $(BIBTWO)
	$(BIBTEX) $(BIBTWO).aux
	$(LATEX) $(LATEXFLAGS) $(MAINFILE).tex
	@$(ECHO) acronyme
	$(MAKEINDEX) -s $(MAINFILE).ist -t $(MAINFILE).alg -o $(MAINFILE).acr $(MAINFILE).acn
	@$(ECHO) glossar
	$(MAKEINDEX) -s $(MAINFILE).ist -t $(MAINFILE).glg -o $(MAINFILE).gls $(MAINFILE).glo
	$(LATEX) $(LATEXFLAGS) $(MAINFILE).tex
	@rm $(MAINFILE).log
	$(LATEX) $(LATEXFLAGS) $(MAINFILE).tex
	@-rm $(BIBONE).bib
	@-rm $(BIBTWO).bib
	@$(ECHO) -- HBOXES:
	grep "hbox (badness" $(MAINFILE).log
	@$(ECHO) -- WARNINGS/ERRORS:
	@cat $(MAINFILE).log | grep "Warning:"
	@grep "Error:" $(MAINFILE).log; \
	status=$$?; \
	if [ $$status = 0 ]; \
	then grep "Error:" $(MAINFILE).log; exit 1; \
	else exit 0; \
	fi

clean:
	$(DEL) $(MAINFILE).aux
	$(DEL) $(MAINFILE).bbl
	$(DEL) $(MAINFILE).bit
	$(DEL) $(MAINFILE).blg
	$(DEL) $(MAINFILE).dvi
	$(DEL) $(MAINFILE).glo
	$(DEL) $(MAINFILE).idx
	$(DEL) $(MAINFILE).ilg
	$(DEL) $(MAINFILE).ilg
	$(DEL) $(MAINFILE).ind
	$(DEL) $(MAINFILE).lof
	$(DEL) $(MAINFILE).log
	$(DEL) $(MAINFILE).lot
	$(DEL) $(MAINFILE).nlo
	$(DEL) $(MAINFILE).nls
	$(DEL) $(MAINFILE).out
	$(DEL) $(MAINFILE).toc
	$(DEL) $(MAINFILE).acn
	$(DEL) $(MAINFILE).cb
	$(DEL) $(MAINFILE).cb2
	$(DEL) $(MAINFILE).ist
	$(DEL) $(MAINFILE).upa
	$(DEL) $(MAINFILE).alg
	$(DEL) $(MAINFILE).glg
	$(DEL) $(MAINFILE).gls
	$(DEL) $(MAINFILE).acr
	$(DEL) $(BIBONE).aux
	$(DEL) $(BIBONE).bbl
	$(DEL) $(BIBONE).blg
	$(DEL) $(BIBONE).bib
	$(DEL) $(BIBTWO).bbl
	$(DEL) $(BIBTWO).blg
	$(DEL) $(BIBTWO).aux
	$(DEL) $(BIBTWO).bib

cleanaux:
	$(DEL) *.aux

cleanpdf:
	$(DEL) $(MAINFILE).pdf

cleanall: clean cleanaux cleanpdf

showpdf: $(MAINFILE).pdf
	$(VIEWPDF) $(MAINFILE).pdf
