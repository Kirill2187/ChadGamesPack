#!/bin/bash

ip=$(cat ci/ip)
user=$USER
server_path=./server/build/libs/

echo "Building server..."
./gradlew server:jar

server_file=$(find $server_path -name "*.jar" -type f | head -1)

echo "Uploading server jar..."
scp "$server_file" $user@$ip:/home/$user/server.jar

echo "Restarting server..."
ssh $user@$ip "killall java; java -jar server.jar &> logs &"

echo "Done!"
