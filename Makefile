
build:
	mvn package

install:
	mvn install
	cp -v target/WhencePlugin-0.2-SNAPSHOT.jar /Users/ray/Projects/minecraftServ/work/plugins/

remote: install copy

copy:
	scp target/WhencePlugin-0.2-SNAPSHOT.jar minecraft@opencalaccess.org:/home/minecraft/plugins/


