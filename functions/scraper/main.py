import asyncio
from datetime import datetime
import logging
import os
import re

from pyppeteer import launch
from google.cloud import storage
from google.cloud import tasks_v2beta3


gcs = None
dir = 'mtggoldfish/commander-decklists-scraped'
url_pattern = 'https://www.mtggoldfish.com/deck/custom?page={}&utf8=✓&mformat=commander&commander={}&commit=Search#paper'


def scrape_html(request):
    request_args = request.args
    # only handling use case with specific commander for now
    if 'containsCard' not in request_args:
        return ('URL parameter containsCard required', 400)

    commander_name = request_args['containsCard']
    page = request_args['page'] if 'page' in request_args else 1
    url = url_pattern.format(page, commander_name)
    logging.info(url)

    html = asyncio.get_event_loop().run_until_complete(get_page_html(url))
    project = os.environ.get('GCP_PROJECT', 'GCP_PROJECT environment variable is not set.')
    bucket = project+'.appspot.com'
    filename = get_full_filepath(commander_name, page)
    write_to_gcs(html, bucket, filename)

    client = tasks_v2beta3.CloudTasksClient()
    parent = client.queue_path(project, 'europe-west1', 'decklist-queue')
    rel_uri = '/mtggoldfish/list?bucket={}&file={}'.format(bucket, filename)
    logging.info('Task relative_uri: '+rel_uri)
    task = {
        'app_engine_http_request': {  # Specify the type of request.
            'http_method': 'GET',
            'relative_uri': rel_uri
        }
    }
    response = client.create_task(parent, task)
    logging.info('Created task '+response.name)


def write_to_gcs(html, bucketname, filename):
    """Filenames of the form mtggoldfish/commander-decklists-scraped/$cardName/$date-$pageNo"""
    global gcs
    if not gcs:
        gcs = storage.Client()

    # just use the default app engine project bucket
    bucket = gcs.get_bucket(bucketname)
    blob = bucket.blob(filename)
    blob.upload_from_string(html)
    logging.info('Wrote file gs://{}/{}'.format(bucket.name, filename))


def get_full_filepath(card_name, page_num):
    filepath = '{}/{}'.format(dir, get_formatted_card_name(card_name))
    filename = get_file_name(page_num)
    return '{}/{}'.format(filepath, filename)


def get_formatted_card_name(card_name):
    formatted = card_name.replace(' ', '-')
    formatted = re.sub(r'[,\']+', '', formatted)
    return formatted.lower()


def get_file_name(page_num):
    return '{}-page{}'.format(datetime.today().strftime('%Y%m%d'), page_num)


async def get_page_html(url):
    browser = await launch(headless=True, args=['--no-sandbox'])
    page = await browser.newPage()
    await page.goto(url)
    html = await page.content()
    await browser.close()
    return html
