
install:
	mvn install
	cp -v target/WhencePlugin-0.1-SNAPSHOT.jar /Users/ray/Projects/minecraftServ/work/plugins/

remote:
	scp target/WhencePlugin-0.1-SNAPSHOT.jar minecraft@opencalaccess.org:/home/minecraft/server/work/plugins/WhencePlugin-0.1-SNAPSHOT.jar

