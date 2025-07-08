# Override for other variants
ARG BASE_IMAGE=ace-basic:12.0.7.0-ubuntu 
FROM $BASE_IMAGE

ARG LICENSE

# docker build -t tdolby/experimental:ace-configurable-12.0.7.0-1 .
# docker run -e LICENSE=accept -p 7800:7800 --rm -ti tdolby/experimental:ace-configurable-12.0.7.0-1

USER root
RUN apt-get update && apt-get install -y net-tools inetutils-telnet
USER aceuser

# Switch off the admin REST API for the server run, as we won't be deploying anything after start
RUN sed -i 's/#port: 7600/port: -1/g' /home/aceuser/ace-server/server.conf.yaml 

COPY init-config.sh /tmp/init-config.sh
RUN ls -l /tmp/init-config.sh
COPY run-server.sh /tmp/run-server.sh
RUN ls -l /tmp/run-server.sh

# Set entrypoint to run the server
ENTRYPOINT ["bash", "-c", ". /opt/ibm/ace-12/server/bin/mqsiprofile && /tmp/init-config.sh && /tmp/run-server.sh"]
