# Override for other variants
ARG BASE_IMAGE=tdolby/experimental:ace-configurable-12.0.7.0-1
FROM $BASE_IMAGE

ARG LICENSE

# docker build --build-arg LICENSE=accept -t tdolby/experimental:ace-map-reader-1 .
# docker run -e LICENSE=accept -p 7800:7800 --rm -ti tdolby/experimental:ace-map-reader-1

COPY ReadMap.bar /tmp/ReadMap.bar
RUN bash -c "export LICENSE=${LICENSE} && . /opt/ibm/ace-12/server/bin/mqsiprofile && mqsibar -c -a /tmp/ReadMap.bar -w /home/aceuser/ace-server"
