# place in /bin directory of Tomcat installation

OCTAGON_CONFPATH="{path to confpath directory}"

export JAVA_OPTS="$JAVA_OPTS -Doctagon.confpath=${OCTAGON_CONFPATH} -DDoctagon.allcanupdate=false  -DDoctagon.archiveWebUrl=localhost:8087/colorlines -DDoctagon.archiveDir=/rv/data/library/pywb/collections/colorlines/archive"

