# !/bin/bash

# @file export.sh
#
# This shell script builds the plugin and then installs it into Jenkins.
# This script assumes that the Jenkins home directory is located at
# '$/.jenkins'

echo "Building the plugin library..."
cd ../lib
mvn install
cd ../plugin

echo "Building the plugin core..."
mvn install
cp ./target/chat-client.hpi ~/.jenkins/plugins
