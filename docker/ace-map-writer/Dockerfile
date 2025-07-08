# Override for other variants
ARG BASE_IMAGE=tdolby/experimental:ace-configurable-12.0.7.0-1
FROM $BASE_IMAGE

ARG LICENSE

# docker build --build-arg LICENSE=accept -t tdolby/experimental:ace-map-writer-1 .
# docker run -e LICENSE=accept -p 7800:7800 --rm -ti tdolby/experimental:ace-map-writer-1

COPY WriteMap.bar /tmp/WriteMap.bar
RUN bash -c "export LICENSE=${LICENSE} && . /opt/ibm/ace-12/server/bin/mqsiprofile && mqsibar -c -a /tmp/WriteMap.bar -w /home/aceuser/ace-server"
