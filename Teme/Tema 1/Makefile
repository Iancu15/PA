SRC = $(wildcard ./src/*.java)

build:
	javac $(SRC)

RUN = java -classpath src/

run-p1:
	$(RUN) Crypto

run-p2:
	$(RUN) Stocks

run-p3:
	$(RUN) Valley

run-p4:
	$(RUN) Ridge

run-p5:
	$(RUN) Trigigel

COMPILED = $(wildcard ./src/*.class)

clean:
	rm -f $(COMPILED)
