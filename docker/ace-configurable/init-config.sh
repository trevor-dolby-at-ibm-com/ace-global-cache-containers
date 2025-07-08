#!/bin/bash
#
# Copyright (c) 2022 Open Technologies for Integration
# Licensed under the MIT license (see LICENSE for details)
#

#set +x

echo "Copying configuration files into place"

if [ ! -n "$ACE_CONFIGMAP_ROOT" ]
then
    export ACE_CONFIGMAP_ROOT=/var/configmap
fi

if [[ -e "$ACE_CONFIGMAP_ROOT" ]]
then
    echo "Contents of ACE_CONFIGMAP_ROOT ($ACE_CONFIGMAP_ROOT):"
    find $ACE_CONFIGMAP_ROOT -ls

    if [[ -e "$ACE_CONFIGMAP_ROOT/server.conf.yaml" ]]
    then
	echo "Copying $ACE_CONFIGMAP_ROOT/server.conf.yaml to /home/aceuser/ace-server/overrides/server.conf.yaml"
	cp $ACE_CONFIGMAP_ROOT/server.conf.yaml /home/aceuser/ace-server/overrides/server.conf.yaml
	chmod 664 /home/aceuser/ace-server/overrides/server.conf.yaml
    fi
    if [[ -e "$ACE_CONFIGMAP_ROOT/server.components.yaml" ]]
    then
	echo "Copying $ACE_CONFIGMAP_ROOT/server.components.yaml to /home/aceuser/ace-server/server.components.yaml "
	cp $ACE_CONFIGMAP_ROOT/server.components.yaml /home/aceuser/ace-server/server.components.yaml
	chmod 664 /home/aceuser/ace-server/server.components.yaml
    fi
else
    echo "ACE_CONFIGMAP_ROOT ($ACE_CONFIGMAP_ROOT) does not exist"
fi

if [ -n "$ACE_SLEEP_ON_STARTUP" ]
then
    echo "Sleeping for ${ACE_SLEEP_ON_STARTUP} seconds as requested by ACE_SLEEP_ON_STARTUP . . . "
    sleep $ACE_SLEEP_ON_STARTUP
    echo "Continuing after sleep"
fi
