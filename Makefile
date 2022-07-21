test:
	$(shell pwd)/scripts/test.sh
.PHONY: test

build:
	$(shell pwd)/scripts/build.sh
.PHONY: build

image:
	$(shell pwd)/scripts/image.sh
.PHONY: image

clean:
	$(shell pwd)/scripts/clean.sh
.PHONY: clean

all:
	$(shell pwd)/scripts/all.sh
.PHONY: all
