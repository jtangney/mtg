import csv
from io import StringIO
import logging

from google.cloud import storage
from google.cloud import tasks

gcs = None


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
    tasks_client = tasks.CloudTasksClient()
    parent = tasks_client.queue_path("jt-mtg", "europe-west1", "deck-queue")

    # lazy init
    global gcs
    if not gcs:
        gcs = storage.Client()

    # get the file from GCS
    bucket = storage.Bucket(name=file['bucket'], client=gcs)
    blob = bucket.blob(file['name'])
    contents = blob.download_as_string().decode('utf-8')
    fileparts = file['name'].split('/')
    source = fileparts[0]
    # print(contents)
    string_io = StringIO(contents)
    dict_reader = csv.DictReader(string_io)
    count = 0
    for row in dict_reader:
        uri = "/{}/deck?deckUrl={}".format(source, row["DeckUrl"])
        if len(fileparts) == 4:
            uri += "&containsCard="+fileparts[2]
        task = {
            "app_engine_http_request": {
                "http_method": "GET",
                "relative_uri": uri,
                # "scheduleTime": None,
            }
        }
        response = tasks_client.create_task(parent=parent, task=task)
        count += 1
    logging.info("Added {} {} tasks".format(count, source))
