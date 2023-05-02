#!/bin/bash

ip=$(cat ip.txt)

sshpass -f < password.txt ssh root@$ip "cd ChadGamesPack && git pull && killall java && ./gradlew server:build && ./gradlew server:run  &"
