#!/bin/bash
#
# Copyright (c) 2022 Open Technologies for Integration
# Licensed under the MIT license (see LICENSE for details)
#

#set +x

i="0"
while [ $i -lt 300 ]
do
  echo "Running IntegrationServer"
  IntegrationServer -w /home/aceuser/ace-server --no-nodejs
  sleep 1
  i=$[$i+1]
done
