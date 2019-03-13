import csv
from io import StringIO
import logging

from google.cloud import storage
from google.cloud import tasks_v2beta3

def add_tasks(event, context):
    """Triggered by upload of decklist file to a Cloud Storage bucket.
    A Task is added to fetch-parse-write each individual deck in the list
    """
    file = event
    # only interested in lists files
    if "decklists" not in file["name"]:
        return

    logging.info('Processing GCS file: '+file['name'])

    # cloud tasks
    tasks_client = tasks_v2beta3.CloudTasksClient()
    parent = tasks_client.queue_path("jt-mtg", "europe-west1", "deck-queue")

    # get the file from GCS
    storage_client = storage.Client()
    bucket = storage_client.get_bucket(file['bucket'])
    blob = bucket.get_blob(file['name'])
    contents = blob.download_as_string().decode('utf-8')
    source = file['name'].split('/')[0]
    # print(contents)
    string_io = StringIO(contents)
    dict_reader = csv.DictReader(string_io)
    count = 0
    for row in dict_reader:
        # uri = "/deckstats/deck?deckUrl="+row["DeckUrl"]
        uri = "/{}/deck?deckUrl={}".format(source, row["DeckUrl"])
        task = {
            "app_engine_http_request": {
                "http_method": "GET",
                "relative_uri": uri
            }
        }
        response = tasks_client.create_task(parent, task)
        count += 1
    logging.info("Added {} {} tasks".format(count, source))
