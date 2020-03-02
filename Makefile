
build:
	mvn package

install:
	mvn install
	cp -v target/WhencePlugin-0.3-SNAPSHOT.jar /Users/ray/Projects/minecraftServ/work/plugins/

remote: build install copy

copy:
	scp target/WhencePlugin-0.3-SNAPSHOT.jar minecraft@opencalaccess.org:/home/minecraft/plugins/


