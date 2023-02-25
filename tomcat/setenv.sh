# place in /bin directory of Tomcat installation

OCT_CONFPATH="{path to confpath directory}"

export JAVA_OPTS="$JAVA_OPTS -Doct.datapath=${OCT_CONFPATH}"

