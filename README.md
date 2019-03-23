# Magic: The Gathering
This project contains code to scrape and parse various sources of MTG data.

##Deckstats
TBC

##MTG Goldfish
Logic flow:
* HTTP-triggered Cloud Function to scrape decklists. 
  * Uses Pyppeteer as JS-rendered.
  * Scraped raw HTML stored in GCS
  * Adds single Cloud Task that references the stored GCS file 
* Cloud Task triggers AppEngine to parse the HTML
  * Parsed decklist CSV stored in GCS bucket
* The GCS storage event triggers another Cloud Function
  * Adds a Cloud Task for each individual deck in the list
* Cloud Tasks trigger AppEngine to fetch-parse-write each individual deck
  * Uses plain-ol' JSoup as standard HTML
  * Parsed deck CSV stored in GCS bucket 

Notes:  
* Some of the above bit clunky - no real reason to use AppEngine at all -
but some code already existed, so just reused.
* Currently only handles "containsCard" use case i.e. searching for decks with 
specific commander card