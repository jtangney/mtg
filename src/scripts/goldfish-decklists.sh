#!/bin/bash

url="https://europe-west1-jt-mtg.cloudfunctions.net/goldfish-scraper?containsCard=Xantcha,%20Sleeper%20Agent&page="
for i in {1..7}
    do
        page="$url$i"
        echo $page
        curl -s "$page"
        sleep 10
done