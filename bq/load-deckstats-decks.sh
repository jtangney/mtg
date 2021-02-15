bq load \
  --source_format=CSV \
  --skip_leading_rows=1 \
  jt-mtg:deckstats.decks \
  gs://jt-mtg.appspot.com/deckstats/commander-decks/esika-god-of-the-tree--the-prismatic-bridge/*.csv \
  ./deckstats-decks-schema.json

